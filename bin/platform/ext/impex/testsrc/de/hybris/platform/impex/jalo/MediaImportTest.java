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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.imp.DefaultDumpHandler;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class MediaImportTest extends HybrisJUnit4TransactionalTest
{
	static final Logger LOG = Logger.getLogger(MediaImportTest.class.getName());
	private static final String MEDIA_CODE1 = "dummy_media_1";
	private static final String MEDIA_CODE2 = "dummy_media_2";
	private static final String MEDIA_FILE1 = "image.png";
	private static final String MEDIA_FILE2 = "image with space.png";
	private static final String CATALOG_DATA = "dummy_catalog";
	private static final String CATALOG_VERSION_DATA = "dummy_catalog_version";
	private static final String LANG = "en";
	private static final String CURR = "EUR";
	private static final String IMPORT_DATA =
	//
	"$catalogversion=catalogversion(catalog(id[default='"
			+ CATALOG_DATA
			+ "']),version[default='"
			+ CATALOG_VERSION_DATA
			+ "'])"
			+ "[unique=true,default='"
			+ CATALOG_DATA
			+ ":"
			+ CATALOG_VERSION_DATA
			+ "'] \n"
			+ "INSERT_UPDATE Catalog;id[unique=true];name[lang=en];defaultCatalog\n"
			+ ";"
			+ CATALOG_DATA
			+ ";"
			+ CATALOG_DATA
			+ ";true \n"//
			+ "INSERT_UPDATE CatalogVersion; catalog(id)[unique=true]; version[unique=true]; active;defaultCurrency(isocode) \n"
			+ //
			";"
			+ CATALOG_DATA
			+ ";"
			+ CATALOG_VERSION_DATA
			+ ";true;"
			+ CURR
			+ ";en \n"//
			+ "INSERT_UPDATE Media;code[unique=true];$catalogversion;mime;realfilename;@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator]\n"
			+ //
			";" + MEDIA_CODE1 + ";;image/png;;jar:/impex/testfiles/import/media/dummymedia/" + MEDIA_FILE1 + "\n" + //
			";" + MEDIA_CODE2 + ";;image/png;;jar:/impex/testfiles/import/media/dummymedia/" + MEDIA_FILE2;

	private String legacyModeBackup;


	//private final MediaFilter theMediaFilter = new MediaFilter();

	@Before
	public void setUp()
	{
		legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		getOrCreateLanguage(LANG);
		getOrCreateCurrency(CURR);
	}

	@After
	public void setLegacyMode()
	{
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
	}

	//check compatibility
	// TODO: check what was the intention of the test (jira), probably some compatibility with URLs (pretty URLs are dead!)
	@Test
	public void testPLA9830() throws Exception
	{
		// given
		importCsv(IMPORT_DATA, "UTF-8");

		// when
		final Media foundMedia1 = getMediaByCode(MEDIA_CODE1);
		final Media foundMedia2 = getMediaByCode(MEDIA_CODE2);

		// then
		assertThat(foundMedia1).isNotNull();
		assertThat(foundMedia2).isNotNull();
	}

	private Media getMediaByCode(final String mediaCode)
	{
		return (Media) MediaManager.getInstance().getMediaByCode(mediaCode).iterator().next();
	}


	protected void importCsv(final String data, final String encoding) throws ImpExException
	{
		LOG.debug("importing resource " + data);

		//get file stream
		org.junit.Assert.assertNotNull("Given data is null", data);
		final InputStream inputStream = new ByteArrayInputStream(data.getBytes());

		//create stream reader
		CSVReader reader = null;
		try
		{
			reader = new CSVReader(inputStream, encoding);
		}
		catch (final UnsupportedEncodingException e)
		{
			org.junit.Assert.fail("Given encoding " + encoding + " is not supported");
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
			org.junit.Assert.fail("Import has " + importer.getDumpedLineCountPerPass() + "+unresolved lines, first lines are:\n"
					+ importer.getDumpHandler().getDumpAsString());
		}
		org.junit.Assert.assertNull("Import of resource " + data + " failed" + (error == null ? "" : error.getMessage()), error);
		org.junit.Assert.assertFalse("Import of resource " + data + " failed", importer.hadError());
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
