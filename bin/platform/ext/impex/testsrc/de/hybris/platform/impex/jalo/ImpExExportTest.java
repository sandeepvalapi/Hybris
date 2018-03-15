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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.ExportConfiguration;
import de.hybris.platform.impex.jalo.exp.ExportUtils;
import de.hybris.platform.impex.jalo.exp.Exporter;
import de.hybris.platform.impex.jalo.exp.HeaderLibrary;
import de.hybris.platform.impex.jalo.exp.ImpExCSVExportWriter;
import de.hybris.platform.impex.jalo.exp.ImpExExportMedia;
import de.hybris.platform.impex.jalo.exp.ImpExExportWriter;
import de.hybris.platform.impex.jalo.exp.converter.DefaultExportResultHandler;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


/**
 * This test class contains all tests considering the export process of the impex extension.
 */
@IntegrationTest
public class ImpExExportTest extends AbstractImpExTest
{
	private static final String EXPECTED_MESSAGE = "Processed column Product.keywords is a localized collection but there is no language provided, please specify a language modifier for column";

	private static final Logger LOG = Logger.getLogger(ImpExExportTest.class);
	
	/**
	 * Scenario: execution of an script based export mode: strict input: script.
	 */
	@Test
	public void testScriptBasedExport() throws ImpExException, IOException, JaloBusinessException
	{
		// fields
		ImpExMedia scriptmedia = null;
		ImpExExportMedia exportedDataTarget = null;
		Product product1 = null, product2 = null;
		Export export = null;

		product1 = ProductManager.getInstance().createProduct("test_p1");
		product2 = ProductManager.getInstance().createProduct("test_p2");

		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productexportscript.impex");
		assertNotNull("Can not read from jar file '/testfiles/productexportscript.impex'", inputStream);

		final File script = File.createTempFile("productexportscript", ".temp");
		final FileOutputStream fos = new FileOutputStream(script, true);

		copy(inputStream, fos);

		scriptmedia = ImpExManager.getInstance().createImpExMedia("test_productexportscript");
		scriptmedia.setFieldSeparator('|');

		scriptmedia.setFile(script);

		exportedDataTarget = ExportUtils.createDataExportTarget("test_productexport");

		final ExportConfiguration config = new ExportConfiguration(scriptmedia, ImpExManager.getExportOnlyMode());
		config.setFieldSeparator("|");
		config.setDataExportTarget(exportedDataTarget);

		final Exporter exporter = new Exporter(config);

		export = exporter.export();

		try (final DefaultExportResultHandler handler = new DefaultExportResultHandler())
		{
			handler.setExport(export);

			final List<ZipEntry> entries = handler.getZipEntries(export.getExportedData());
			StringBuilder dataResult = new StringBuilder();
			StringBuilder mainResult = new StringBuilder();

			for (final ZipEntry entry : entries)
			{
				if (entry.getName().equals("Product.csv"))
				{
					dataResult = handler.getDataContent(entry);
				}
				if (entry.getName().equals("importscript.impex"))
				{
					mainResult = handler.getDataContent(entry);
				}
			}
			assertTrue(mainResult.length() > 0);
			assertTrue(dataResult.length() > 0);
			LOG.info("Resulting header:\n" + mainResult.toString());
			assertTrue(mainResult.toString().contains("\"#% impex.setValidationMode( export_only );\""));
			assertTrue(mainResult.toString().contains("\"#% impex.setLocale( new Locale( \"\"de\"\" , \"\"\"\" ) );\""));
			assertTrue(mainResult.toString().contains("insert_update Product|pk[unique=true]|code"));
			assertTrue(mainResult.toString().contains("\"#% impex.includeExternalDataMedia( \"\"Product.csv\"\" , \"\""
					+ exportedDataTarget.getEncoding().getCode() + "\"\", '" + config.getFieldSeparator() + "',  1 , -1 );\""));

			assertEquals(product1.getPK().toString() + "|" + product1.getCode() + "\n" + product2.getPK().toString() + "|"
					+ product2.getCode() + "\n", dataResult.toString());
		}
	}

	/**
	 * Scenario: execution of an localized script based export mode: strict input: script.
	 */
	@Test
	public void testScriptBasedLocalizedExport() throws ImpExException, IOException, JaloBusinessException
	{
		// fields
		ImpExMedia scriptmedia = null;
		ImpExExportMedia exportedDataTarget = null;
		Product product1 = null, product2 = null;
		Export export = null;
		final SessionContext ctxEn = JaloSession.getCurrentSession().createSessionContext();
		ctxEn.setLanguage(C2LManager.getInstance().getLanguageByIsoCode("en"));
		final SessionContext ctxDe = JaloSession.getCurrentSession().createSessionContext();
		ctxDe.setLanguage(C2LManager.getInstance().getLanguageByIsoCode("de"));

		final Language foo = getOrCreateLanguage("foo");

		product1 = ProductManager.getInstance().createProduct("test_p1");
		product1.setName(ctxEn, "p1_name-EN");
		product1.setName(ctxDe, "p1_name-DE");
		product2 = ProductManager.getInstance().createProduct("test_p2");
		JaloSession.getCurrentSession().getSessionContext().setLanguage(C2LManager.getInstance().getLanguageByIsoCode("en"));
		product2.setName(ctxEn, "p2_name-EN");
		JaloSession.getCurrentSession().getSessionContext().setLanguage(C2LManager.getInstance().getLanguageByIsoCode("de"));
		product2.setName(ctxDe, "p2_name-DE");

		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/productexportscript3.impex");
		assertNotNull("Can not read from jar file '/testfiles/productexportscript3.impex'", inputStream);

		final File script = File.createTempFile("productexportscript", ".temp");
		final FileOutputStream fos = new FileOutputStream(script, true);

		copy(inputStream, fos);

		scriptmedia = ImpExManager.getInstance().createImpExMedia("test_productexportscript");
		scriptmedia.setFieldSeparator('|');

		scriptmedia.setFile(script);

		exportedDataTarget = ExportUtils.createDataExportTarget("test_productexport");

		final ExportConfiguration config = new ExportConfiguration(scriptmedia, ImpExManager.getExportOnlyMode());
		config.setFieldSeparator("|");
		config.setDataExportTarget(exportedDataTarget);

		final Exporter exporter = new Exporter(config);

		jaloSession.getSessionContext().setLanguage(foo);

		export = exporter.export();

		try (final DefaultExportResultHandler handler = new DefaultExportResultHandler()) 
		{
			handler.setExport(export);

			final List<ZipEntry> entries = handler.getZipEntries(export.getExportedData());
			StringBuilder dataResult = new StringBuilder();
			StringBuilder mainResult = new StringBuilder();

			for (final ZipEntry entry : entries)
			{
				if (entry.getName().equals("Product.csv"))
				{
					dataResult = handler.getDataContent(entry);
				}
				if (entry.getName().equals("importscript.impex"))
				{
					mainResult = handler.getDataContent(entry);
				}
			}
			assertTrue(mainResult.length() > 0);
			assertTrue(dataResult.length() > 0);
			LOG.info("Resulting header:\n" + mainResult.toString());
			assertTrue(mainResult.toString().contains("\"#% impex.setValidationMode( export_only );\""));
			assertTrue(mainResult.toString().contains("\"#% impex.setLocale( new Locale( \"\"de\"\" , \"\"\"\" ) );\""));
			assertTrue(mainResult.toString().contains("insert_update Product|pk[unique=true]|code|name[lang=en]|name[lang=de]"));
			assertTrue(mainResult.toString().contains("\"#% impex.includeExternalDataMedia( \"\"Product.csv\"\" , \"\""
					+ exportedDataTarget.getEncoding().getCode() + "\"\", '" + config.getFieldSeparator() + "',  1 , -1 );\""));

			assertEquals(product1.getPK().toString() + "|" + product1.getCode() + "|" + product1.getName(ctxEn) + "|"
					+ product1.getName(ctxDe) + "\n" + product2.getPK().toString() + "|" + product2.getCode() + "|"
					+ product2.getName(ctxEn) + "|" + product2.getName(ctxDe) + "\n", dataResult.toString());
		}
	}

	/**
	 * Scenario: execution of an script based export mode: strict input: script.
	 */
	@Test
	public void testExportWithColumnOffset()
	{
		Media media1 = null, media2 = null;
		try
		{
			media1 = MediaManager.getInstance().createMedia("test_m1");
			media2 = ImpExManager.getInstance().createImpExMedia("test_m2");

			media1.setRealFileName("m1realfilename");
			media2.setRealFileName("m2realfilename");

			for (int offset = 1; offset >= -3; --offset)
			{
				testColumnOffset(media1, media2, offset, false,"testExportWithColumnOffset"+offset+"_");
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void testColumnOffset(final Media media1, final Media media2, final int offset, final boolean singleFile, String uniqueMediaWorkPrefix) throws Exception
	{
		ImpExMedia scriptmedia = null;
		ImpExExportMedia exportedDataTarget = null;
		Export export = null;
		final String script = "#  -------------------------------------------------------" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ "\"#% impex.setValidationMode( \"\"export_only\"\" );\"" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ "\"#% impex.setLocale( new Locale( \"\"de\"\" , \"\"\"\" ) );\"" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ "#  -------------------------------------------------------" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "# ---- Extension: core ---- Type: Media ----"
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "\"#% impex.setTargetFile( \"\"Media.csv\"\" , false, 1, " + offset + " );\""
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "insert_update Media;pk[unique=true]; code; realfilename;"
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "\"#% impex.exportItems( \"\"Media\"\" , true );\"";

		final String[] m1Cols = new String[]
		{ "Media", media1.getPK().toString(), media1.getCode(), media1.getRealFileName() };
		final String[] m2Cols = new String[]
		{ "ImpExMedia", media2.getPK().toString(), media2.getCode(), media2.getRealFileName() };

		File scriptFile = null;
		FileOutputStream fos = null;
		try
		{
			scriptFile = File.createTempFile("mediaexportscript", ".temp");
			fos = new FileOutputStream(scriptFile);
			fos.write(script.getBytes());
			scriptmedia = ImpExManager.getInstance().createImpExMedia(uniqueMediaWorkPrefix+"test_mediaexportscript");
			scriptmedia.setFile(scriptFile);
			exportedDataTarget = ExportUtils.createDataExportTarget(uniqueMediaWorkPrefix+"test_mediaexportscript_tgt");

			final ExportConfiguration config = new ExportConfiguration(scriptmedia, ImpExManager.getExportOnlyMode());

			config.setDataExportTarget(exportedDataTarget);
			config.setSingleFile(singleFile);
			final Exporter exporter = new Exporter(config);

			if (LOG.isInfoEnabled())
			{
				LOG.info("Exporting media...");
			}
			export = exporter.export();

			// get exported data
			StringBuilder result = new StringBuilder();
			if (LOG.isInfoEnabled())
			{
				LOG.info("Getting exported media...");
			}
			if (singleFile)
			{
				final ImpExExportMedia expMedia = export.getExportedData();
				assertNotNull(expMedia);
				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(expMedia.getDataFromStreamSure()));
				String line = "";
				while ((line = bufferedReader.readLine()) != null)
				{
					result.append(line);
					result.append(CSVConstants.DEFAULT_LINE_SEPARATOR);
				}
			}
			else
			{
				try (final DefaultExportResultHandler handler = new DefaultExportResultHandler())
				{
					handler.setExport(export);

					final List<ZipEntry> entries = handler.getZipEntries(export.getExportedData());
					StringBuilder importScriptResult = new StringBuilder();

					for (final ZipEntry entry : entries)
					{
						if (entry.getName().equals("Media.csv"))
						{
							result = handler.getDataContent(entry);
						}
						else if (entry.getName().equals("importscript.impex"))
						{
							importScriptResult = handler.getDataContent(entry);
							if (LOG.isDebugEnabled())
							{
								LOG.debug("importScriptResult = \n'" + importScriptResult + "'\n");
							}
							assertTrue(
									"Following data does not contain correct values: " + importScriptResult,
									importScriptResult
											.toString()
											.toLowerCase()
											.contains(
													"\"#% impex.includeExternalDataMedia( \"\"Media.csv\"\" , \"\"UTF-8\"\", ';',  1 , "
															.toLowerCase() + offset + " );\""));
						}
					}
				}
			}
			assertTrue(result.length() > 0);
			if (LOG.isInfoEnabled())
			{
				LOG.info("Exported media: \n" + result.toString());
			}

			// generate expected data
			String expected1 = "";
			String expected2 = "";

			if (singleFile)
			{
				// when using single file export, a column offset of 0 should be used
				for (int i = 0; i < m1Cols.length; i++)
				{
					expected1 += (m1Cols[i].equals("Media") ? "" : m1Cols[i])
							+ (i == m1Cols.length - 1 ? "" : Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
					expected2 += m2Cols[i] + (i == m1Cols.length - 1 ? "" : Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
				}
			}
			else
			{
				for (int i = -offset; i < m1Cols.length; i++)
				{
					if (i < 0)
					{
						expected1 += CSVConstants.DEFAULT_FIELD_SEPARATOR;
						expected2 += CSVConstants.DEFAULT_FIELD_SEPARATOR;
					}
					else
					{
						expected1 += (m1Cols[i].equals("Media") ? "" : m1Cols[i])
								+ (i == m1Cols.length - 1 ? "" : Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
						expected2 += (m2Cols[i].equals("Media") ? "" : m2Cols[i])
								+ (i == m1Cols.length - 1 ? "" : Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
					}
				}
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("offset = '" + offset + "', expected1 = '" + expected1 + "', expected2 = '" + expected2 + "'");
				LOG.debug("result =\n" + result.toString() + "\n");
			}

			// test some stuff
			final StringTokenizer toki = new StringTokenizer(result.toString(), CSVConstants.DEFAULT_LINE_SEPARATOR);
			final List<String> resultLines = new ArrayList<String>();
			while (toki.hasMoreTokens())
			{
				resultLines.add(toki.nextToken());
			}
			assertTrue(resultLines.contains(expected1));
			assertTrue(resultLines.contains(expected2));
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (final Exception e)
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug(e.getMessage());
					}
				}
			}
		}

	}

	/**
	 * Scenario: execution of an item collection mode: strict input: headerlibrary, item collection.
	 */
	@Test
	public void testSearchResultExport()
	{
		// fields
		Product product1 = null, product2 = null;
		ImpExMedia exportScript = null;
		ImpExExportMedia destmedia = null;
		Export export = null;

		product1 = ProductManager.getInstance().createProduct("export_p1");
		product2 = ProductManager.getInstance().createProduct("export_p2");

		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/headerlibrary.impex");
			assertNotNull("Can not read from jar file '/impex/testfiles/headerlibrary.impex'", inputStream);

			final File script = File.createTempFile("productexportscript", ".temp");
			final FileOutputStream fos = new FileOutputStream(script, true);
			copy(inputStream, fos);

			final Map attributeValues = new HashMap();
			attributeValues.put(HeaderLibrary.CODE, "test_headerlibrary");

			final HeaderLibrary headerlibrary = ImpExManager.getInstance().createHeaderLibrary(attributeValues);
			headerlibrary.setFile(script);

			final Collection items = new ArrayList();
			items.add(product1);
			items.add(product2);

			exportScript = ExportUtils.createExportScript(headerlibrary, items);
			destmedia = ExportUtils.createDataExportTarget("result_productexportscript");

			try
			{
				final ExportConfiguration config = new ExportConfiguration(exportScript, ImpExManager.getExportOnlyMode());
				config.setDataExportTarget(destmedia);

				final Exporter exporter = new Exporter(config);

				export = exporter.export();

				try (final DefaultExportResultHandler handler = new DefaultExportResultHandler())
				{
					handler.setExport(export);

					final List<ZipEntry> entries = handler.getZipEntries(export.getExportedData());
					StringBuilder result = new StringBuilder();

					for (final ZipEntry entry : entries)
					{
						if (entry.getName().equals("Product.csv"))
						{
							result = handler.getDataContent(entry);
						}
					}

					assertTrue(result.indexOf(product1.getPK().toString()) != -1);
					assertTrue(result.indexOf(product2.getCode()) != -1);
					assertTrue(result.indexOf(product1.getPK().toString()) != -1);
					assertTrue(result.indexOf(product2.getCode()) != -1);
				}
			}
			catch (final ImpExException e)
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the alternative pattern syntax of header for export. Exports a {@link Link} using the
	 * {@link ImpExCSVExportWriter}, reads the exported string with a {@link CSVReader} and compares it.
	 */
	@Test
	public void testAlternativePatterns()
	{
		// create test links programmatically
		Title title1 = null, title2 = null;
		Country country1 = null, ct2 = null, country2 = null;
		Link link1 = null, link2 = null, link3 = null, link4 = null;
		try
		{
			title1 = UserManager.getInstance().createTitle("t1");
			title2 = UserManager.getInstance().createTitle("t2");
			country1 = C2LManager.getInstance().createCountry("c1");
			ct2 = C2LManager.getInstance().createCountry("t2");
			country2 = C2LManager.getInstance().createCountry("c2");
			int number = 0;
			link1 = LinkManager.getInstance().createLink("l1", title1, title1, number++, 0); // t -> t
			link2 = LinkManager.getInstance().createLink("l2", title2, ct2, number++, 0); // t -> c
			link3 = LinkManager.getInstance().createLink("l3", country1, country2, number++, 0); // c -> c
			link4 = LinkManager.getInstance().createLink("l4", ct2, title1, number++, 0); // c -> t
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		// create header for export
		final StringWriter buffer = new StringWriter();
		final ImpExCSVExportWriter writer = new ImpExCSVExportWriter(new CSVWriter(buffer));
		try
		{
			writer.setCurrentHeader("INSERT Link; " + Link.QUALIFIER + "[unique=true];" + Link.SOURCE + "(Title." + Title.CODE
					+ "|Country." + Country.ISOCODE + "," + Item.PK + ")[unique=true];" + Link.TARGET + "(Country." + Country.ISOCODE
					+ "|Title." + Title.CODE + ")[unique=true];", ImpExManager.getImportStrictMode());
			assertNotNull(writer.getCurrentHeader());
			writer.writeCurrentHeader(false);
			// export all links
			writer.writeLine(link1);
			writer.writeLine(link2);
			writer.writeLine(link3);
			writer.writeLine(link4);
			writer.close();
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		// read in exported links
		final Map[] lines = CSVReader.parse(buffer.getBuffer().toString());
		assertEquals(5, lines.length);
		// compare read links with created one
		assertEquals("l1", lines[1].get(Integer.valueOf(1)));
		assertEquals("l2", lines[2].get(Integer.valueOf(1)));
		assertEquals("l3", lines[3].get(Integer.valueOf(1)));
		assertEquals("l4", lines[4].get(Integer.valueOf(1)));
		assertEquals("t1:" + title1.getPK().toString(), lines[1].get(Integer.valueOf(2)));
		assertEquals("t2:" + title2.getPK().toString(), lines[2].get(Integer.valueOf(2)));
		assertEquals("c1:" + country1.getPK().toString(), lines[3].get(Integer.valueOf(2)));
		assertEquals("t2:" + ct2.getPK().toString(), lines[4].get(Integer.valueOf(2)));
		assertEquals("t1", lines[1].get(Integer.valueOf(3)));
		assertEquals("t2", lines[2].get(Integer.valueOf(3)));
		assertEquals("c2", lines[3].get(Integer.valueOf(3)));
		assertEquals("t1", lines[4].get(Integer.valueOf(3)));
	}

	/**
	 * Tests a simple export of items. Creates units, exports them using the {@link ImpExExportWriter} and compares the
	 * resulting text with expected text.
	 */
	@Test
	public void testSimpleExport()
	{
		Currency currency = null;
		Unit unit1, unit2, unit3;
		try
		{
			currency = C2LManager.getInstance().createCurrency("ccc");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		unit1 = ProductManager.getInstance().createUnit("mass", "u1");
		unit1.setConversionFactor(1.0);
		unit2 = ProductManager.getInstance().createUnit("mass", "u2");
		unit2.setConversionFactor(1.0);
		unit3 = ProductManager.getInstance().createUnit("mass", "u3");
		unit3.setConversionFactor(1.0);
		assertEquals("u1", unit1.getCode());
		assertEquals("u2", unit2.getCode());
		assertEquals("u3", unit3.getCode());
		assertEquals(1.0, unit1.getConversionFactor(), 0.0001);
		assertEquals(1.0, unit2.getConversionFactor(), 0.0001);
		assertEquals(1.0, unit3.getConversionFactor(), 0.0001);
		final StringWriter res = new StringWriter();
		final ImpExExportWriter writer = new ImpExCSVExportWriter(new CSVWriter(res));
		writer.comment("test test");
		try
		{
			final String header = "insert " + TypeManager.getInstance().getComposedType(Unit.class).getCode() + ";" + Unit.CODE
					+ "[unique=true];" + Unit.UNITTYPE + ";" + Unit.CONVERSION + ";";
			// String header = "INSERT PRODUCT; code[unique=true]; catalogVersion; supercategories(code[default=bath], catalogVersion(version[default=Staged],
			// catalog( id[default=Default] )))";
			writer.setCurrentHeader(header, ImpExManager.getImportStrictMode());
			writer.writeCurrentHeader(false);
			writer.newLine();
			writer.writeLine(unit1);
			// test wrong item type
			try
			{
				writer.writeLine(currency);
				fail("expected ImpExException");
			}
			catch (final ImpExException e)
			{
				// OK
			}
			writer.writeLine(unit3);
			writer.comment("juhu");
			writer.writeLine(unit2);
			writer.close();
			final String expected =
			// test test
			CSVConstants.DEFAULT_COMMENT_CHAR
					+ " "
					+ "test test"
					+ CSVConstants.DEFAULT_LINE_SEPARATOR
					// header
					+ ImpExConstants.Syntax.Mode.INSERT + " " + TypeManager.getInstance().getComposedType(Unit.class).getCode()
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR + Unit.CODE + ImpExConstants.Syntax.MODIFIER_START
					+ ImpExConstants.Syntax.Modifier.UNIQUE + ImpExConstants.Syntax.MODIFIER_EQUAL + Boolean.TRUE
					+ ImpExConstants.Syntax.MODIFIER_END
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR
					+ Unit.UNITTYPE
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR
					+ Unit.CONVERSION
					+ CSVConstants.DEFAULT_LINE_SEPARATOR
					// newline
					+ CSVConstants.DEFAULT_LINE_SEPARATOR
					// u1
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR + "u1" + CSVConstants.DEFAULT_FIELD_SEPARATOR + "mass"
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR + "1"
					+ CSVConstants.DEFAULT_LINE_SEPARATOR
					// u3
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR + "u3" + CSVConstants.DEFAULT_FIELD_SEPARATOR + "mass"
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR + "1" + CSVConstants.DEFAULT_LINE_SEPARATOR
					// juhu
					+ CSVConstants.DEFAULT_COMMENT_CHAR + " " + "juhu" + CSVConstants.DEFAULT_LINE_SEPARATOR
					// u2
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR + "u2" + CSVConstants.DEFAULT_FIELD_SEPARATOR + "mass"
					+ CSVConstants.DEFAULT_FIELD_SEPARATOR + "1" + CSVConstants.DEFAULT_LINE_SEPARATOR;
			final String exported = res.getBuffer().toString();
			assertEquals(expected, exported);
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the document ID column descriptor while import.
	 */
	@Test
	public void testExportWithDocumentID()
	{
		Customer customer1 = null, customer2 = null;
		Address address1, address2, address3, address4;
		try
		{
			customer1 = UserManager.getInstance().createCustomer("andy");
			customer1.setUID("andy");
			customer2 = UserManager.getInstance().createCustomer("rigge");
			customer2.setUID("rigge");
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail();
		}
		address1 = UserManager.getInstance().createAddress(customer1);
		customer1.setDefaultPaymentAddress(address1);
		address2 = UserManager.getInstance().createAddress(customer2);
		customer2.setDefaultPaymentAddress(address2);
		address3 = UserManager.getInstance().createAddress(customer1);
		customer1.setDefaultDeliveryAddress(address3);
		address4 = UserManager.getInstance().createAddress(customer2);
		customer2.setDefaultDeliveryAddress(address4);
		final StringWriter res = new StringWriter();
		final StringWriter docIds = new StringWriter();
		final DocumentIDRegistry docIDregistry = new DocumentIDRegistry(new CSVWriter(docIds));
		final ImpExExportWriter writer = new ImpExCSVExportWriter(new CSVWriter(res));
		try
		{
			String header = "INSERT_UPDATE Customer; uid[unique=true]; defaultPaymentAddress( &payAddress ); defaultShipmentAddress( &delAddress )";
			writer.setCurrentHeader(header, ImpExManager.getImportStrictMode(), docIDregistry);
			writer.writeCurrentHeader(false);
			writer.writeLine(customer1);
			writer.writeLine(customer2);
			header = "INSERT Address; &payAddress; &delAddress ; owner( Customer.uid )";
			writer.setCurrentHeader(header, ImpExManager.getImportStrictMode(), docIDregistry);
			writer.writeCurrentHeader(false);
			writer.writeLine(address1);
			writer.writeLine(address2);
			writer.writeLine(address3);
			writer.writeLine(address4);
			writer.close();
			docIDregistry.closeStreams();
			final String expected =
			// header1
			"INSERT_UPDATE Customer;uid[unique=true];defaultPaymentAddress( &payAddress );defaultShipmentAddress( &delAddress )"
					+ CSVConstants.DEFAULT_LINE_SEPARATOR
					// c1
					+ ";andy;payAddress0;delAddress0" + CSVConstants.DEFAULT_LINE_SEPARATOR
					// c2
					+ ";rigge;payAddress1;delAddress1" + CSVConstants.DEFAULT_LINE_SEPARATOR
					// new line
					// + CSVConstants.DEFAULT_LINE_SEPARATOR
					// header2
					+ "INSERT Address;&payAddress;&delAddress;owner( Customer.uid )" + CSVConstants.DEFAULT_LINE_SEPARATOR
					// a1
					+ ";payAddress0;delAddress2;andy" + CSVConstants.DEFAULT_LINE_SEPARATOR
					// a3
					+ ";payAddress1;delAddress3;rigge" + CSVConstants.DEFAULT_LINE_SEPARATOR
					// a3
					+ ";payAddress2;delAddress0;andy" + CSVConstants.DEFAULT_LINE_SEPARATOR
					// a4
					+ ";payAddress3;delAddress1;rigge" + CSVConstants.DEFAULT_LINE_SEPARATOR;
			assertEquals(expected, res.getBuffer().toString());
			assertTrue(docIds.getBuffer().length() > 0);
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the document ID column descriptor while import.
	 */
	@Test
	public void testSingletonExport()
	{
		ComposedType type = null;
		final TypeManager manager = TypeManager.getInstance();
		try
		{
			type = manager.createComposedType(manager.getComposedType(GenericItem.class), "TestSingleton");
			type.setSingleton(true);
			type.createAttributeDescriptor("test1", TypeManager.getInstance().getType(String.class.getName()),
					AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG);
			type.createAttributeDescriptor("test2", TypeManager.getInstance().getType(String.class.getName()),
					AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG);
		}
		catch (final JaloDuplicateCodeException e)
		{
			fail(e.getMessage());
		}
		catch (final JaloDuplicateQualifierException e)
		{
			fail(e.getMessage());
		}
		assertTrue(type.isSingleton());
		final Item instance = type.getSingletonInstance();

		final StringWriter res = new StringWriter();
		final ImpExExportWriter writer = new ImpExCSVExportWriter(new CSVWriter(res));
		try
		{
			final String header = "INSERT_UPDATE TestSingleton; pk[unique=true]; test1; test2";
			writer.setCurrentHeader(header, ImpExManager.getImportStrictMode());
			writer.writeCurrentHeader(false);
			writer.writeLine(instance);
			writer.close();
			final String expected =
			// header1
			"INSERT_UPDATE TestSingleton;pk[unique=true];test1;test2" + CSVConstants.DEFAULT_LINE_SEPARATOR + ";"
					+ instance.getPK().getLongValue() + ";;" + CSVConstants.DEFAULT_LINE_SEPARATOR;
			assertEquals(expected, res.getBuffer().toString());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}


	@Test
	public void testLocalizedCollectionModification() throws Exception
	{
		final String LANG_ONE = "maui";
		final String LANG_TWO = "kula";
		final String CATALOG = "testCatalog";
		final String CATALOG_VERSION = "testCatalogVersion";
		final String PRODUCT_CODE = "FooProductIdentifer";
		final String PRODUCT_NAME = "FooProductName";

		final String KEYWORD_OVERWRITE = "overwriteKeyword";
		final String KEYWORD_MODIFY = "modifyKeyword";


		final StringBuilder prepareImpexScript = new StringBuilder(100);
		prepareImpexScript.append("INSERT_UPDATE Language;isocode[unique=true];active;;;;;;").append("\n");
		prepareImpexScript.append(";" + LANG_ONE + ";true;;;;;;").append("\n");
		prepareImpexScript.append(";" + LANG_TWO + ";true;;;;;;").append("\n");

		prepareImpexScript.append("INSERT_UPDATE Catalog;id[unique=true];defaultCatalog").append("\n");
		prepareImpexScript.append(";" + CATALOG + ";true").append("\n");

		prepareImpexScript.append("INSERT_UPDATE CatalogVersion; catalog(id)[unique=true]; version[unique=true]; active;").append(
				"\n");
		prepareImpexScript.append(";" + CATALOG + ";" + CATALOG_VERSION + ";true;").append("\n");

		prepareImpexScript.append(
				"INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];name[lang=" + LANG_ONE
						+ "];name[lang=" + LANG_TWO + "];").append("\n");
		prepareImpexScript.append(
				";" + PRODUCT_CODE + ";" + CATALOG + ":" + CATALOG_VERSION + ";" + PRODUCT_NAME + ";" + PRODUCT_NAME + ";").append(
				"\n");

		//create keywords
		prepareImpexScript
				.append(
						"INSERT_UPDATE Keyword;keyword[unique=true];language(isocode)[allowNull=true];catalogVersion(catalog(id),version)[unique=true,allowNull=true];")
				.append("\n");
		prepareImpexScript.append(";" + KEYWORD_OVERWRITE + ";;" + CATALOG + ":" + CATALOG_VERSION + ";;;;;").append("\n");
		prepareImpexScript.append(";" + KEYWORD_MODIFY + ";;" + CATALOG + ":" + CATALOG_VERSION + ";;;;;").append("\n");


		final ImpExImportReader dataPrepareReader = new ImpExImportReader(new CSVReader(new StringReader(
				prepareImpexScript.toString())));
		dataPrepareReader.readAll();


		final StringBuilder keywordReplaceImpexScript = new StringBuilder(100);

		keywordReplaceImpexScript.append("UPDATE Product;code[unique=true];keywords(keyword);").append("\n"); //appendMode		
		keywordReplaceImpexScript.append(";" + PRODUCT_CODE + ";" + KEYWORD_OVERWRITE + ";").append("\n");

		final ImpExImportReader keywordOverWriteAllReader = new ImpExImportReader(new CSVReader(new StringReader(
				keywordReplaceImpexScript.toString())));

		verifyKeyword(KEYWORD_OVERWRITE, keywordOverWriteAllReader.readLine(), jaloSession.getSessionContext().getLanguage());
		//assert keyword assigned 


		final StringBuilder modifyAddKeywordScript = new StringBuilder();
		modifyAddKeywordScript.append("UPDATE Product;code[unique=true];keywords(keyword)[mode=append];").append("\n"); //appendMode
		modifyAddKeywordScript.append(";" + PRODUCT_CODE + ";" + KEYWORD_MODIFY + ";").append("\n");

		try
		{
			final ImpExImportReader modifyReader = new ImpExImportReader(new CSVReader(new StringReader(
					modifyAddKeywordScript.toString())));
			modifyReader.readLine();
			Assert.fail("Append mode without  languge should not succeed");
		}
		catch (final ImpExException e)
		{
			verifyException(e);
			//fine here
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
		}

		//assert keyword not  assigned 
		final StringBuilder modifyRemoveKeywordScript = new StringBuilder();
		modifyRemoveKeywordScript.append("UPDATE Product;code[unique=true];keywords(keyword)[mode=remove];").append("\n"); //appendMode
		modifyRemoveKeywordScript.append(";" + PRODUCT_CODE + ";" + KEYWORD_OVERWRITE + ";").append("\n");

		try
		{
			final ImpExImportReader modifyReader = new ImpExImportReader(new CSVReader(new StringReader(
					modifyRemoveKeywordScript.toString())));
			modifyReader.readLine();
			Assert.fail("Append mode without  languge should not succeed");
		}
		catch (final ImpExException e)
		{
			verifyException(e);
			//fine here 
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
		}

		//assert keyword not  assigned
		final StringBuilder modifyAddKeywordScriptWithLanguage = new StringBuilder();
		modifyAddKeywordScriptWithLanguage.append(
				"UPDATE Product;code[unique=true];keywords(keyword)[mode=append,lang=" + LANG_ONE + "];").append("\n"); //appendMode
		modifyAddKeywordScriptWithLanguage.append(";" + PRODUCT_CODE + ";" + KEYWORD_MODIFY + ";").append("\n");

		try
		{
			final ImpExImportReader modifyReader = new ImpExImportReader(new CSVReader(new StringReader(
					modifyAddKeywordScriptWithLanguage.toString())));
			verifyKeyword(KEYWORD_MODIFY, modifyReader.readLine(), jaloSession.getC2LManager().getLanguageByIsoCode(LANG_ONE));
			//assert keyword added  assigned
		}
		catch (final Exception e)
		{
			Assert.fail("Append mode with languge should  succeed");
		}
		finally
		{
			jaloSession.removeLocalSessionContext();
		}

		final StringBuilder modifyRemoveKeywordScriptWithLanguage = new StringBuilder();
		modifyRemoveKeywordScriptWithLanguage.append(
				"UPDATE Product;code[unique=true];keywords(keyword)[mode=remove,lang=" + LANG_ONE + "];").append("\n"); //appendMode
		modifyRemoveKeywordScriptWithLanguage.append(";" + PRODUCT_CODE + ";" + KEYWORD_MODIFY + ";").append("\n");

		try
		{
			final ImpExImportReader modifyReader = new ImpExImportReader(new CSVReader(new StringReader(
					modifyRemoveKeywordScriptWithLanguage.toString())));
			final Product result = (Product) modifyReader.readLine();
			Assert.assertNotNull(result);
			final SessionContext ctx = jaloSession.createLocalSessionContext();
			ctx.setLanguage(jaloSession.getC2LManager().getLanguageByIsoCode(LANG_ONE));
			Assert.assertTrue(result.getAttribute("keywords") instanceof List);
			final List keywords = (List) result.getAttribute("keywords");
			Assert.assertTrue(keywords.isEmpty());
			//assert keyword removed  
		}
		catch (final Exception e)
		{
			Assert.fail("Append mode with languge should  succeed");
		}
	}

	/**
	 * 
	 */
	private void verifyException(final ImpExException exception)
	{
		if (!EXPECTED_MESSAGE.equals(exception.getMessage()))
		{
			Assert.fail(exception.getMessage());
		}
	}

	/**
	 * 
	 */
	private void verifyKeyword(final String keywordName, final Object testProductObject, final Language ctxLang)
			throws JaloSecurityException
	{
		try
		{
			Assert.assertTrue("Imported item should be a product ", testProductObject instanceof Product);
			final Product testProduct = (Product) testProductObject;
			final SessionContext ctx = jaloSession.createLocalSessionContext();
			ctx.setLanguage(ctxLang);
			Assert.assertTrue("Keywords result should be a list ", testProduct.getAttribute("keywords") instanceof List);
			final List keywords = (List) testProduct.getAttribute("keywords");
			Assert.assertTrue("Keywords list should be of size 1 ", keywords.size() == 1);
			Assert.assertTrue("Keywords list first item should be of type Keyword ",
					"Keyword".equals(((Item) keywords.get(0)).getComposedType().getCode()));
			final Item keyword = (Item) keywords.get(0);
			Assert.assertTrue("Keyword should meet exepected value '" + keywordName + "' ",
					keywordName.equals(keyword.getAttribute("keyword")));
		}
		finally
		{
			jaloSession.removeLocalSessionContext();
		}
	}


	/**
	 * Tests the "single file" export mode.
	 */
	@Test
	public void testSingleFileExport()
	{
		ImpExMedia scriptmedia = null;
		ImpExExportMedia exportedDataTarget = null;
		Media media1 = null;
		Media media2 = null;
		Export export = null;
		final String script = "#  -------------------------------------------------------" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ "\"#% impex.setValidationMode( \"\"export_only\"\" );\"" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ "\"#% impex.setLocale( new Locale( \"\"de\"\" , \"\"\"\" ) );\"" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ "#  -------------------------------------------------------" + CSVConstants.DEFAULT_LINE_SEPARATOR
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "# ---- Extension: core ---- Type: Media ----"
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "\"#% impex.setTargetFile( \"\"Media.csv\"\");\""
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "insert_update Media;pk[unique=true]; code; realfilename;"
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + "\"#% impex.exportItems( \"\"Media\"\" , true );\"";

		String[] m1Cols = null;
		String[] m2Cols = null;

		File scriptFile = null;
		FileOutputStream fos = null;
		try
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("Creating media to be exported...");
			}
			media1 = MediaManager.getInstance().createMedia("test_m1");
			media2 = ImpExManager.getInstance().createImpExMedia("test_m2");

			media1.setRealFileName("m1realfilename");
			media2.setRealFileName("m2realfilename");

			m1Cols = new String[]
			{ "", media1.getPK().toString(), media1.getCode(), media1.getRealFileName() };
			m2Cols = new String[]
			{ "ImpExMedia", media2.getPK().toString(), media2.getCode(), media2.getRealFileName() };

			scriptFile = File.createTempFile("mediaexportscript", ".temp");
			fos = new FileOutputStream(scriptFile);
			fos.write(script.getBytes());
			scriptmedia = ImpExManager.getInstance().createImpExMedia("test_mediaexportscript");
			scriptmedia.setFile(scriptFile);
			exportedDataTarget = ExportUtils.createDataExportTarget("test_mediaexportscript_tgt");

			final ExportConfiguration config = new ExportConfiguration(scriptmedia, ImpExManager.getExportOnlyMode());
			config.setSingleFile(true);
			config.setDataExportTarget(exportedDataTarget);

			final Exporter exporter = new Exporter(config);

			if (LOG.isInfoEnabled())
			{
				LOG.info("Exporting media...");
			}
			export = exporter.export();

			// get exported data
			if (LOG.isInfoEnabled())
			{
				LOG.info("Getting exported media from file...");
			}

			final ImpExExportMedia expMedia = export.getExportedData();
			assertNotNull(expMedia);
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(expMedia.getDataFromStreamSure()));
			final StringBuilder result = new StringBuilder();
			String line = "";
			while ((line = bufferedReader.readLine()) != null)
			{
				result.append(line);
				result.append(CSVConstants.DEFAULT_LINE_SEPARATOR);
			}
			assertTrue(result.length() > 0);
			if (LOG.isInfoEnabled())
			{
				LOG.info("Exported media: \n" + result.toString());
			}

			// generate expected data
			String expected1 = "";
			String expected2 = "";
			for (int i = 0; i < m1Cols.length; i++)
			{
				expected1 += m1Cols[i] + (i == m1Cols.length - 1 ? "" : Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
				expected2 += m2Cols[i] + (i == m1Cols.length - 1 ? "" : Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
			}

			if (LOG.isInfoEnabled())
			{
				LOG.info("Validating exported data...");
			}

			final StringTokenizer toki = new StringTokenizer(result.toString(), CSVConstants.DEFAULT_LINE_SEPARATOR);
			// skip first line
			toki.nextToken();
			final List<String> resultLines = new ArrayList<String>();
			while (toki.hasMoreTokens())
			{
				resultLines.add(toki.nextToken());
			}
			assertTrue(resultLines.contains(expected1));
			assertTrue(resultLines.contains(expected2));

		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (final Exception e)
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug(e.getMessage());
					}
				}
			}
		}
	}

	@Test
	public void testSingleFileExportWithColumnOffset()
	{
		Media media1 = null, media2 = null;
		try
		{
			media1 = MediaManager.getInstance().createMedia("test_m1");
			media2 = ImpExManager.getInstance().createImpExMedia("test_m2");

			media1.setRealFileName("m1realfilename");
			media2.setRealFileName("m2realfilename");

			for (int offset = 1; offset >= -3; --offset)
			{
				testColumnOffset(media1, media2, offset, true,"testSingleFileExportWithColumnOffset"+offset+"_");
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void copy(final InputStream fis, final OutputStream fos)
	{
		try
		{
			final byte buffer[] = new byte[0xffff];
			int nbytes;
			while ((nbytes = fis.read(buffer)) != -1)
			{
				fos.write(buffer, 0, nbytes);
			}
		}
		catch (final IOException e)
		{
			LOG.error(e.getMessage(), e);
		}
		finally
		{
			if (fis != null)
			{
				try
				{
					fis.close();
				}
				catch (final IOException e)
				{
					LOG.error(e.getMessage(), e);
				}
			}
			try
			{
				if (fos != null)
				{
					fos.close();
				}
			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
	}
}
