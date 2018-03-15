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
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.ExportConfiguration;
import de.hybris.platform.impex.jalo.exp.ExportUtils;
import de.hybris.platform.impex.jalo.exp.ImpExExportMedia;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Test;



/**
 * This test class contains all tests considering the import process of the impex extension.
 * 
 */
@IntegrationTest
public class ImpExManagerTest extends AbstractImpExTest
{

	private static final Logger LOG = Logger.getLogger(ImpExManagerTest.class);

	@Test
	public void testImportDataLightStream()
	{
		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productFull.impex");
			assertNotNull("Can not read from jar file 'productFull.impex'", inputStream);
			final Importer importer = ImpExManager.getInstance().importDataLight(inputStream, windows1252.getCode(), false);
			assertNotNull(importer);
			assertFalse(importer.hadError());
		}
		catch (final ImpExException e1)
		{
			fail(e1.getMessage());
		}

		// clean up
		final Collection<Product> products = ProductManager.getInstance().getProductsByCode("testProduct");
		assertEquals(1, products.size());
		try
		{
			products.iterator().next().remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testImportDataLightReader()
	{
		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productFull.impex");
			assertNotNull("Can not read from jar file 'productFull.impex'", inputStream);
			final Importer importer = ImpExManager.getInstance().importDataLight(new CSVReader(inputStream, windows1252.getCode()), false, 1);
			assertNotNull(importer);
			assertFalse(importer.hadError());
		}
		catch (final UnsupportedEncodingException e1)
		{
			fail(e1.getMessage());
		}
		catch (final ImpExException e1)
		{
			fail(e1.getMessage());
		}

		// clean up
		final Collection<Product> products = ProductManager.getInstance().getProductsByCode("testProduct");
		assertEquals(1, products.size());
		try
		{
			products.iterator().next().remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testImportDataFull()
	{
		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productFull.impex");
		assertNotNull("Can not read from jar file 'productFull.impex'", inputStream);
		final ImpExImportCronJob cronJob = ImpExManager.getInstance().importData(inputStream, windows1252.getCode(),
				CSVConstants.HYBRIS_FIELD_SEPARATOR, CSVConstants.HYBRIS_QUOTE_CHARACTER, false);
		assertNull(cronJob);
		// clean up
		final Collection<Product> products = ProductManager.getInstance().getProductsByCode("testProduct");
		assertEquals(1, products.size());
		try
		{
			products.iterator().next().remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testImportData2Cycle()
	{
		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/product2Cycle.impex");
		assertNotNull("Can not read from jar file 'product2Cycle.impex'", inputStream);
		final ImpExImportCronJob cronJob = ImpExManager.getInstance().importData(inputStream, windows1252.getCode(),
				CSVConstants.HYBRIS_FIELD_SEPARATOR, CSVConstants.HYBRIS_QUOTE_CHARACTER, false);
		assertNull(cronJob);
		// clean up
		final Collection<Product> products = ProductManager.getInstance().getProductsByCode("testProduct");
		assertEquals(1, products.size());
		try
		{
			products.iterator().next().remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		final Unit unit = ProductManager.getInstance().getUnit("testUnit");
		assertNotNull(unit);
		try
		{
			unit.remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testImportDataUnresolved()
	{
		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productUnresolved.impex");
		assertNotNull("Can not read from jar file 'productUnresolved.impex'", inputStream);
		ImpExImportCronJob cronJob = null;
		try
		{
			TestUtils.disableFileAnalyzer("the import results in one not imported line - this is ok, needed for the test here", 100);
			cronJob = ImpExManager.getInstance().importData(inputStream, windows1252.getCode(), CSVConstants.HYBRIS_FIELD_SEPARATOR,
					CSVConstants.HYBRIS_QUOTE_CHARACTER, false);
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
		assertNotNull(cronJob);
		assertNotNull(cronJob.getUnresolvedDataStore());
		assertEquals(cronJob.getFailureResult(), cronJob.getResult());
		try
		{
			cronJob.getJobMedia().remove();
			cronJob.remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		// clean up
		final Collection<Product> products = ProductManager.getInstance().getProductsByCode("testProduct");
		assertEquals(1, products.size());
		try
		{
			products.iterator().next().remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testImportDataLightNested()
	{
		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productFullNested.impex");
			assertNotNull("Can not read from jar file 'productFullNested.impex'", inputStream);
			final Importer importer = ImpExManager.getInstance().importDataLight(new CSVReader(inputStream, windows1252.getCode()), true, 1);
			assertNotNull(importer);
			assertFalse(importer.hadError());
		}
		catch (final UnsupportedEncodingException e1)
		{
			fail(e1.getMessage());
		}
		catch (final ImpExException e1)
		{
			fail(e1.getMessage());
		}

		// clean up
		final Collection<Product> products = ProductManager.getInstance().getProductsByCode("testProduct");
		assertEquals(1, products.size());
		try
		{
			products.iterator().next().remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testImportDataNested()
	{
		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productFullNested.impex");
		assertNotNull("Can not read from jar file 'productFull.impex'", inputStream);
		final ImpExImportCronJob cronJob = ImpExManager.getInstance().importData(inputStream, windows1252.getCode(),
				CSVConstants.HYBRIS_FIELD_SEPARATOR, CSVConstants.HYBRIS_QUOTE_CHARACTER, true);
		assertNull(cronJob);
		// clean up
		final Collection<Product> products = ProductManager.getInstance().getProductsByCode("testProduct");
		assertEquals(1, products.size());
		try
		{
			products.iterator().next().remove();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testImportDataAndMediaUsingStreams()
	{
		InputStream dataIs = null;
		InputStream mediaIs = null;

		ImpExImportCronJob cronJob = null;
		// try to import stuff
		try
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("Opening data streams...");
			}
			dataIs = ImpExManager.class.getResourceAsStream("/impex/testfiles/testmedia.impex");
			assertNotNull("The file 'mediatest.impex' could not be read from jar", dataIs);
			mediaIs = ImpExManager.class.getResourceAsStream("/impex/testfiles/testmedia.zip");
			assertNotNull("The file 'mediatest.zip' could not be read from jar", mediaIs);
			if (LOG.isInfoEnabled())
			{
				LOG.info("Importing data...");
			}
			cronJob = ImpExManager.getInstance().importData(dataIs, mediaIs, windows1252.getCode(),
					CSVConstants.HYBRIS_FIELD_SEPARATOR, CSVConstants.HYBRIS_QUOTE_CHARACTER, true);
		}
		catch (final Exception e)
		{
			fail("Exception occured when trying to import data and media: '" + e.getMessage() + "'");
		}
		finally
		{
			this.closeInputStreamStream(dataIs);
			this.closeInputStreamStream(mediaIs);
		}
		assertNull("Import failed", cronJob);
	}

	@Test
	public void testImportDataAndMedia()
	{
		InputStream dataIs = null;
		InputStream mediaIs = null;

		ImpExImportCronJob cronJob = null;

		// now import stuff using the importData(ImpExMedia, ImpExMedia, ...) method and make sure imported stuff != null
		try
		{
			dataIs = ImpExManager.class.getResourceAsStream("/impex/testfiles/testmedia.impex");
			assertNotNull("The file 'mediatest.impex' could not be read from jar", dataIs);
			mediaIs = ImpExManager.class.getResourceAsStream("/impex/testfiles/testmedia.zip");
			assertNotNull("The file 'mediatest.zip' could not be read from jar", mediaIs);
			if (LOG.isInfoEnabled())
			{
				LOG.info("Creating job media...");
			}
			final ImpExMedia jobMedia = ImpExManager.getInstance().createImpExMedia("generated impex media - "+UUID.randomUUID(), windows1252.getCode());
			assertNotNull(jobMedia);
			jobMedia.setFieldSeparator(CSVConstants.HYBRIS_FIELD_SEPARATOR);
			jobMedia.setQuoteCharacter(CSVConstants.HYBRIS_QUOTE_CHARACTER);
			jobMedia.setData(new DataInputStream(dataIs), jobMedia.getCode() + "." + ImpExConstants.File.EXTENSION_CSV,
					ImpExConstants.File.MIME_TYPE_CSV);

			if (LOG.isInfoEnabled())
			{
				LOG.info("Creating zip media...");
			}
			final ImpExMedia zipMedia = ImpExManager.getInstance().createImpExMedia("generated impex zip media");
			assertNotNull(zipMedia);
			zipMedia.setData(new DataInputStream(mediaIs), zipMedia.getCode() + "." + ImpExConstants.File.EXTENSION_ZIP,
					ImpExConstants.File.MIME_TYPE_ZIP);
			if (LOG.isInfoEnabled())
			{
				LOG.info("Creating cron job...");
			}
			cronJob = ImpExManager.getInstance().createDefaultImpExImportCronJob();

			cronJob.setEnableCodeExecution(true);
			cronJob.setJobMedia(jobMedia);
			cronJob.setExternalDataCollection(null);
			cronJob.setMediasMedia(zipMedia);
			if (LOG.isInfoEnabled())
			{
				LOG.info("Importing data...");
			}
			cronJob = ImpExManager.getInstance().importData(cronJob, true, false);

			assertNotNull("The ImpExImportCronJob shouldn't be null when not using \"remove on success\"", cronJob);

			assertNotNull(cronJob.getJobMedia());
			assertNotNull(cronJob.getMediasMedia());
		}
		catch (final Exception e)
		{
			fail("Exception occured when trying to import data and media: '" + e.getMessage() + "'");
		}
		finally
		{
			this.closeInputStreamStream(dataIs);
			this.closeInputStreamStream(mediaIs);
		}
	}

	@Test
	public void testExportImport()
	{
		Product product = null;
		File scriptFile = null;
		FileOutputStream fos = null;
		ImpExExportMedia exportedDataTarget = null;
		ImpExMedia scriptMedia = null;
		Export export = null;

		// export script
		final String script = "# ---- Extension: core ---- Type: Product ----" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ "\"#% impex.setTargetFile( \"\"Product.csv\"\" );\"" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ "insert_update Product; code[unique=true]; catalogVersion[unique=true,allownull=true]"
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "\"#% impex.exportItems( \"\"Product\"\" , false );\"";

		try
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("Creating item to be used for exporting/importing...");
			}
			product = ProductManager.getInstance().createProduct("myProduct");
			product.setName("myProductName");
			// create an export script file
			scriptFile = File.createTempFile("test_importexport_exportscript", ".temp");
			fos = new FileOutputStream(scriptFile);
			fos.write(script.getBytes());

			// create export script media and set the export script file
			if (LOG.isInfoEnabled())
			{
				LOG.info("Creating export script media...");
			}
			scriptMedia = ImpExManager.getInstance().createImpExMedia("test_importexport_exportscript");
			scriptMedia.setFile(scriptFile);
			exportedDataTarget = ExportUtils.createDataExportTarget("test_importexport_export");
			final ExportConfiguration config = new ExportConfiguration(scriptMedia, ImpExManager.getExportReimportStrictMode());
			config.setDataExportTarget(exportedDataTarget);

			// export data
			if (LOG.isInfoEnabled())
			{
				LOG.info("Exporting data...");
			}
			export = ImpExManager.getInstance().exportData(config, true);

			assertNotNull("Export instance is null", export);
			assertNotNull("Exported data is null", export.getExportedData());

			// remove product
			if (LOG.isInfoEnabled())
			{
				LOG.info("Removing item...");
			}
			try
			{
				product.remove();
			}
			catch (final ConsistencyCheckException cce)
			{
				fail("Could not remove the created product (" + cce.getMessage() + ")");
			}

			// make sure product is gone
			List<Product> products = new ArrayList<Product>(ProductManager.getInstance().getProductsByCode("myProduct"));
			assertTrue("The removed product is still available", products.isEmpty());

			// re-import the product using the import script created when exporting it
			if (LOG.isInfoEnabled())
			{
				LOG.info("Re-importing...");
			}
			assertTrue(export.getExportedData().getSize().intValue() > 0);
			final ImpExImportCronJob importCronJob = ImpExManager.getInstance().importData(export.getExportedData(), null, true,
					true, true);
			assertNull("Import failed", importCronJob);

			products = (List<Product>) ProductManager.getInstance().getProductsByCode("myProduct");
			assertTrue("The imported item was not found", products != null && !products.isEmpty());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void closeInputStreamStream(final InputStream inputStream)
	{
		if (inputStream != null)
		{
			try
			{
				inputStream.close();
			}
			catch (final Exception e)
			{
				//ignore
			}
		}
	}
}
