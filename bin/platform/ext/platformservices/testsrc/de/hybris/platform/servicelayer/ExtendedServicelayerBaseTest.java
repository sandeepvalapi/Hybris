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
package de.hybris.platform.servicelayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.jalo.imp.DefaultDumpHandler;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Ignore;


/**
 * Service layer test with possibility to create sample data.
 */
@Ignore
public class ExtendedServicelayerBaseTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(ExtendedServicelayerBaseTest.class);

	public static void createDefaultCatalog() throws Exception
	{
		LOG.info("Creating test catalog ..");
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());
		final long startTime = System.currentTimeMillis();

		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		Assert.assertNotNull(flexibleSearchService);
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		Assert.assertNotNull(modelService);

		importCsv("/servicelayer/test/testCatalog.csv", "windows-1252");

		// checking imported stuff
		final CatalogModel catalog = flexibleSearchService
				.<CatalogModel> search("SELECT {PK} FROM {Catalog} WHERE {id}='testCatalog'").getResult().get(0);
		Assert.assertNotNull(catalog);
		final CatalogVersionModel version = flexibleSearchService
				.<CatalogVersionModel> search("SELECT {PK} FROM {CatalogVersion} WHERE {version}='Online' AND {catalog}=?catalog",
						Collections.singletonMap("catalog", catalog)).getResult().get(0);
		Assert.assertNotNull(version);

		JaloSession.getCurrentSession().getSessionContext()
				.setAttribute("catalogversions", modelService.toPersistenceLayer(Collections.singletonList(version)));

		//setting catalog to session and admin user
		final CategoryModel category = flexibleSearchService
				.<CategoryModel> search("SELECT {PK} FROM {Category} WHERE {code}='testCategory0'").getResult().get(0);
		Assert.assertNotNull(category);

		final ProductModel product = flexibleSearchService
				.<ProductModel> search("SELECT {PK} FROM {Product} WHERE {code}='testProduct0'").getResult().get(0);
		Assert.assertNotNull(product);

		Assert.assertTrue(product.getSupercategories().contains(category));

		LOG.info("Finished creating test catalog in " + (System.currentTimeMillis() - startTime) + "ms");
	}

	/**
	 * Imports given csv file from classpath using given encoding. Fails in case of import errors.
	 * 
	 * @param csvFile
	 *           name of file to import from classpath
	 * @param encoding
	 *           encoding to use
	 * @throws ImpExException
	 */
	protected static void importCsv(final String csvFile, final String encoding) throws ImpExException
	{
		LOG.info("importing resource " + csvFile);
		//get file stream
		assertNotNull("Given file is null", csvFile);
		final InputStream is = ServicelayerTest.class.getResourceAsStream(csvFile);
		assertNotNull("Given file " + csvFile + " can not be found in classpath", is);

		importStream(is, encoding, csvFile);
	}

	protected static void importStream(final InputStream is, final String encoding, final String resourceName)
			throws ImpExException
	{
		//create stream reader
		CSVReader reader = null;
		try
		{
			reader = new CSVReader(is, encoding);
		}
		catch (final UnsupportedEncodingException e)
		{
			fail("Given encoding " + encoding + " is not supported");
		}

		// import
		MediaDataTranslator.setMediaDataHandler(new DefaultMediaDataHandler());
		Importer importer = null;
		ImpExException error = null;
		try
		{
			importer = new Importer(reader);
			importer.getReader().enableCodeExecution(true);
			importer.setMaxPass(-1);
			importer.setDumpHandler(new FirstLinesDumpReader());
			importer.importAll();
		}
		catch (final ImpExException e)
		{
			error = e;
		}
		finally
		{
			MediaDataTranslator.unsetMediaDataHandler();
		}

		// failure handling
		if (importer.hasUnresolvedLines())
		{
			fail("Import has " + importer.getDumpedLineCountPerPass() + "+unresolved lines, first lines are:\n"
					+ importer.getDumpHandler().getDumpAsString());
		}
		assertNull("Import of resource " + resourceName + " failed" + (error == null ? "" : error.getMessage()), error);
		assertFalse("Import of resource " + resourceName + " failed", importer.hadError());
	}

	private static class FirstLinesDumpReader extends DefaultDumpHandler
	{
		@Override
		public String getDumpAsString()
		{
			final StringBuffer result = new StringBuffer(100);
			try
			{
				final BufferedReader reader = new BufferedReader(new FileReader(getDumpAsFile()));
				result.append(reader.readLine() + "\n");
				result.append(reader.readLine() + "\n");
				result.append(reader.readLine() + "\n");
				reader.close();
			}
			catch (final FileNotFoundException e)
			{
				result.append("Error while reading dump " + e.getMessage());
			}
			catch (final IOException e)
			{
				result.append("Error while reading dump " + e.getMessage());
			}
			return result.toString();
		}
	}

	public static void createCoreData() throws Exception
	{
		LOG.info("Creating essential data for core ..");
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		// importing test csv
		importCsv("/servicelayer/test/testBasics.csv", "windows-1252");

		LOG.info("Finished creating essential data for core in " + (System.currentTimeMillis() - startTime) + "ms");
	}
}
