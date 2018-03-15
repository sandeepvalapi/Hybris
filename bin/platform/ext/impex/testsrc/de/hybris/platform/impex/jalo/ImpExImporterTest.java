/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.impex.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.cronjob.CronJobDumpHandler;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Integration test for {@link Importer}
 */
@IntegrationTest
public class ImpExImporterTest extends AbstractImpExTest
{
	private static final Logger LOG = Logger.getLogger(ImpExImporterTest.class);

	@Test
	public void testFullImport()
	{
		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productFull.impex");
			assertNotNull("Can not read from jar file 'productFull.impex'", inputStream);
			final CSVReader reader = new CSVReader(inputStream, windows1252.getCode());
			final Importer importer = new Importer(reader);
			final Product product = (Product) importer.importNext();
			assertNotNull("Imported product was null", product);
			importer.close();
			assertEquals("Dump file is at " + importer.getDumpHandler().getDumpAsFile().getAbsolutePath(), 0,
					importer.getDumpedLineCountPerPass());
		}
		catch (final UnsupportedEncodingException e)
		{
			fail(e.getMessage());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void test2CycleImport()
	{
		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/product2Cycle.impex");
			assertNotNull("Can not read from jar file 'product2Cycle.impex'", inputStream);
			final CSVReader reader = new CSVReader(inputStream, windows1252.getCode());
			final Importer importer = new Importer(reader);
			final Product product = (Product) importer.importNext();
			assertNotNull("Imported product was null", product);
			final Unit unit = (Unit) importer.importNext();
			assertNotNull("Imported unit was null", unit);
			final Product product2 = (Product) importer.importNext();
			assertEquals(product, product2);
			assertEquals(0, importer.getDumpedLineCountPerPass());
			assertEquals(1, importer.getDumpedLineCountOverall());
			importer.close();
		}
		catch (final UnsupportedEncodingException e)
		{
			fail(e.getMessage());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testUnresolvedImport()
	{
		Importer importer = null;
		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productUnresolved.impex");
			assertNotNull("Can not read from jar file 'productUnresolved.impex'", inputStream);
			final CSVReader reader = new CSVReader(inputStream, windows1252.getCode());
			importer = new Importer(reader);
			final Product product = (Product) importer.importNext();
			assertNotNull("Imported product was null", product);
			final Product product2 = (Product) importer.importNext();
			assertEquals(product, product2);
			assertEquals(1, importer.getDumpedLineCountPerPass());
			final Product product3 = (Product) importer.importNext();
			assertEquals(product, product3);
			assertEquals(1, importer.getDumpedLineCountPerPass());
			importer.importNext();

			fail("ImpExException expected caused by unresolved lines");
		}
		catch (final UnsupportedEncodingException e)
		{
			fail(e.getMessage());
		}
		catch (final ImpExException e)
		{
			if (e.getErrorCode() != ImpExException.ErrorCodes.CAN_NOT_RESOLVE_ANYMORE)
			{
				fail(e.getMessage());
			}
			importer.close();
			importer.getDumpHandler().getDumpAsFile().delete();
		}
	}

	@Test
	public void testUnresolvedImport2() throws JaloGenericCreationException, JaloAbstractTypeException, JaloItemNotFoundException
	{
		Importer importer = null;
		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productUnresolved.impex");
			assertNotNull("Can not read from jar file 'productUnresolved.impex'", inputStream);
			final CSVReader reader = new CSVReader(inputStream, windows1252.getCode());
			importer = new Importer(reader);
			final Product product = (Product) importer.importNext();
			assertNotNull("Imported product was null", product);
			final Product product2 = (Product) importer.importNext();
			assertEquals(product, product2);
			assertEquals(1, importer.getDumpedLineCountPerPass());
			final Product product3 = (Product) importer.importNext();
			assertEquals(product, product3);
			assertEquals(1, importer.getDumpedLineCountPerPass());
			importer.importAll();

			fail("ImpExException expected caused by unresolved lines");
		}
		catch (final UnsupportedEncodingException e)
		{
			fail(e.getMessage());
		}
		catch (final ImpExException e)
		{
			if (e.getErrorCode() != ImpExException.ErrorCodes.CAN_NOT_RESOLVE_ANYMORE)
			{
				fail(e.getMessage());
			}
			importer.close();
			importer.getDumpHandler().getDumpAsFile().delete();
		}
	}

	@Test
	public void testCronJob() throws UnsupportedEncodingException
	{
		InputStream imputStream = null;
		ImpExImportCronJob job = null;
		try
		{
			imputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productUnresolved.impex");
			job = ImpExManager.getInstance().createDefaultImpExImportCronJob();
			final ImpExMedia jobMedia = ImpExManager.getInstance().createImpExMedia("test media", CSVConstants.HYBRIS_ENCODING);
			jobMedia.setRemoveOnSuccess(true);
			job.setJobMedia(jobMedia);
			final Importer importer = new Importer(new CSVReader(imputStream, windows1252.getCode()));
			importer.setDumpHandler(new CronJobDumpHandler(job));
			importer.importAll();
			fail("Impex exception expected");
		}
		catch (final ImpExException e)
		{
			// correct
		}
		catch (final Exception e)
		{
			LOG.error("Unexpected exception", e);
			fail("Unexpected exception: " + e);
		}
		finally
		{
			IOUtils.closeQuietly(imputStream);
			try
			{
				job.remove();
			}
			catch (final ConsistencyCheckException e)
			{
				// remove silently
			}
		}
	}
	
	@Test
	public void testLegacyModeInMultiplePasses() throws IOException, ImpExException
	{
		try (InputStream in = ImpExManager.class.getResourceAsStream("/impex/testfiles/productCategoryMultiplePasses.impex"))
		{
			// Given
			Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, Boolean.FALSE.toString());
			final ImpExImportReader reader = new ImpExImportReader(new CSVReader(in, utf8.getCode()), true);
			final Importer importer = new Importer(reader);

			// When - first pass
			importer.importNext();		// Product (cannot be resolved now)
			importer.importNext();		// Category

			// Then
			assertTrue("Product should be unresolved in first pass", importer.hasUnresolvedLines());
			assertEquals("No lines remaining for next pass", 1, importer.getDumpedLineCountPerPass());
			
			// When - second pass
			importer.importNext();		// 2nd attempt for Product
			
			assertFalse("Product should be resolved in second pass", importer.hasUnresolvedLines());
			assertEquals("Lines remaining after second pass", 0, importer.getDumpedLineCountPerPass());
			assertNotNull("No ImpEx import reader availabe for check", importer.getReader());
			assertTrue("Reader for next pass should be in legacy mode", importer.getReader().isLegacyMode());
			
			// Import should be finished
			Item item = importer.importNext();
			assertNull("No items should be left for importing", item);
			assertTrue("Import should be finished", importer.isFinished());
		}
	}
}
