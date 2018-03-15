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
package de.hybris.platform.servicelayer.impex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.jalo.imp.DefaultDumpHandler;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.TestImportCsvUtil;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.util.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;


public class DefaultTestImportCsvUtil implements TestImportCsvUtil
{
	private static final Logger LOG = Logger.getLogger(DefaultTestImportCsvUtil.class);

	@Override
	public void importCsv(final String csvFile, final String encoding)
	{
		LOG.info("importing resource " + csvFile);
		//get file stream
		assertNotNull("Given file is null", csvFile);
		final InputStream inputStream = ServicelayerTest.class.getResourceAsStream(csvFile);
		assertNotNull("Given file " + csvFile + "can not be found in classpath", inputStream);

		importStream(inputStream, encoding, csvFile);

	}

	private void importStream(final InputStream inputStream, final String encoding, final String resourceName)
	{
		//create stream reader
		CSVReader reader = null;
		try
		{
			reader = new CSVReader(inputStream, encoding);
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
			Registry.getCoreApplicationContext().getBean("modelContext", ModelContext.class).clear();
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

}
