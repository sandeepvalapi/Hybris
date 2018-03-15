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

import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.cronjob.ImpExExportCronJob;
import de.hybris.platform.impex.jalo.exp.ImpExCSVExportWriter;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.AmbiguousItemException;
import de.hybris.platform.impex.jalo.imp.DefaultImportProcessor;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.InsufficientDataException;
import de.hybris.platform.impex.jalo.imp.ItemConflictException;
import de.hybris.platform.impex.jalo.translators.UserPasswordTranslator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.persistence.security.SaltedMD5PasswordEncoder;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * This test class is used for testing the different modifiers possible in header descriptors.
 */
@IntegrationTest
public class ImpExModifierTest extends AbstractImpExTest
{
	private static final Logger LOG = Logger.getLogger(ImpExModifierTest.class.getName());

	private String originalSalt = null;

	@Before
	public void setUp()
	{
		// TODO after the relase 3.0 was published, we have to move this code to JaloTest !!!

		final Tenant tenant = Registry.getCurrentTenant();
		if (tenant instanceof MasterTenant)
		{
			originalSalt = ((MasterTenant) tenant).getConfig().getParameter("password.md5.salt");
			((MasterTenant) tenant).getConfig().setParameter("password.md5.salt", "JUnit");
		}
		else
		{
			((SlaveTenant) tenant).getOwnConfig().setParameter("password.md5.salt", "JUnit");
		}
		// ////////////////////////////////
	}

	@After
	public void tearDown()
	{
		// TODO after the relase 3.0 was published, we have to move this code to JaloTest !!!

		if (originalSalt != null)
		{
			final Tenant tenant = Registry.getCurrentTenant();
			if (tenant instanceof MasterTenant)
			{
				((MasterTenant) tenant).getConfig().setParameter("password.md5.salt", originalSalt);
			}
		}
		// //////////////////////////////////////////
	}

	/**
	 * Tests the validation with and without modifier <code>allowNull</code>.
	 */
	@Test
	public void testModifierAllowNull()
	{
		final ImpExImportReader importReader = new ImpExImportReader(
				"UPDATE Region; isocode[unique=true]; active; country[unique=true] \n" // 1st test
						+ "; THEREGION;true;;\n"
						+ "INSERT Region; isocode[unique=true]; active; country[unique=true, allownull=true] \n" // 2nd test
						+ "; THEREGION;true;;\n"
						+ "INSERT Region; isocode[unique=true]; active[allownull=true]; country(isocode)[unique=true] \n" // 3rd
						// test
						+ "; THEREGION;;testCountry;");
		importReader.enableCodeExecution(true);

		// 1st test, missing modifier: allownull
		try
		{
			assertNotNull(importReader.readLine());
			fail("Exception 'missing value for mandatory attribute country' expected");
		}
		catch (final InsufficientDataException e)
		{
			// well done
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}

		// 2nd test, usage of modifier: allownull wit no success
		try
		{
			assertNotNull(importReader.readLine());
			fail("region could be created!!?");
		}
		catch (final ImpExException e)
		{
			final Throwable nestedE = e.getCause();
			if (nestedE instanceof JaloInvalidParameterException)
			{
				if (!e.getMessage().contains("missing [country] got"))
				{
					fail(e.getMessage());
				}
			}
			else
			{
				fail("this exception should not occur:" + e);
			}
		}

		// 3. test, usage of modifier: allownull eith success
		try
		{
			final Country country = C2LManager.getInstance().createCountry("testCountry");
			assertNotNull(country);
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		try
		{
			assertNotNull(importReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail("this exception should not occur:" + e);
		}
		catch (final JaloInvalidParameterException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testSwitchingToLegacyHandlerForJaloOnlyFeatures()
	{
		final String script = "INSERT_UPDATE PaymentMode;code[unique=true];paymentinfotype[allowNull=true];"
				+ "active[allowNull=true]\n" + ";Visa Credit;;;\n" + ";Mastercard;;;\n" + ";Visa Debit;;;";

		final ImpExImportReader importReaderLegacy = createTestImpExImportReader(true, script);
		final ImpExImportReader importReaderSL = createTestImpExImportReader(false, script);

		try
		{
			// import the same lines with different processors (legacy, SL), mixed way, both should be able to do insert
			// and update
			// Should work for both (in SL mode internally switching to legacy processor)
			assertNotNull(importReaderLegacy.readLine());
			assertNotNull(importReaderSL.readLine());
			assertNotNull(importReaderSL.readLine());
			assertNotNull(importReaderLegacy.readLine());
			assertNotNull(importReaderLegacy.readLine());
			assertNotNull(importReaderSL.readLine());
		}
		catch (final Exception e)
		{
			fail("this exception should not occur:" + e);
		}
	}

	@Test
	public void testSwitchingToLegacyHandlerForImpexLegacyFlagInHeader()
	{

		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/testImpexLegacySwitch.csv");

		ImpExImportReader importReaderSL = null;
		try
		{
			importReaderSL = createTestImpExImportReader(false, inputStream);
			assertNotNull(importReaderSL);
		}
		catch (final UnsupportedEncodingException e)
		{
			fail("this exception should not occur:" + e);
		}

		try
		{
			Object result;
			assertNotNull(result = importReaderSL.readLine());
			assertEquals("foo", ((Title) result).getCode());
			assertNotNull(result = importReaderSL.readLine());
			assertEquals("barJalo", ((Title) result).getCode());
		}
		catch (final Exception e)
		{
			fail("this exception should not occur:" + e);
		}
		finally
		{
			try
			{
				importReaderSL.close();
			}
			catch (final IOException e)
			{
				fail("this exception should not occur:" + e);
			}
		}
	}

	@Test
	public void testImportWithNonLegacyHandlerOnly()
	{
		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/testImpexNoLegacySwitch.csv");
		ImpExImportReader importReaderSL = null;
		try
		{
			importReaderSL = createTestImpExImportReader(false, inputStream);
			assertNotNull(importReaderSL);
		}
		catch (final UnsupportedEncodingException e)
		{
			fail("this exception should not occur:" + e);
		}

		try
		{
			Object result;
			assertNotNull(result = importReaderSL.readLine());
			assertEquals("foo", ((Title) result).getCode());
			assertNotNull(result = importReaderSL.readLine());
			assertEquals("barSL", ((Title) result).getCode());
		}
		catch (final Exception e)
		{
			fail("this exception should not occur:" + e);
		}
		finally
		{
			try
			{
				importReaderSL.close();
			}
			catch (final IOException e)
			{
				fail("this exception should not occur:" + e);
			}
		}
	}

	@Test
	public void testImportWithLegacyHandlerOnly()
	{
		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/testImpexNoLegacySwitch.csv");
		ImpExImportReader importReaderLegacy = null;
		try
		{
			importReaderLegacy = createTestImpExImportReader(true, inputStream);
			assertNotNull(importReaderLegacy);
		}
		catch (final UnsupportedEncodingException e)
		{
			fail("this exception should not occur:" + e);
		}

		try
		{
			Object result;
			assertNotNull(result = importReaderLegacy.readLine());
			assertEquals("foo", ((Title) result).getCode());
			assertNotNull(result = importReaderLegacy.readLine());
			assertEquals("barJalo", ((Title) result).getCode());
		}
		catch (final Exception e)
		{
			fail("this exception should not occur:" + e);
		}
		finally
		{
			try
			{
				importReaderLegacy.close();
			}
			catch (final IOException e)
			{
				fail("this exception should not occur:" + e);
			}
		}
	}

	@Test
	public void testSwitchingToLegacyHandlerUsedByDefaultNotNeededForImpexLegacyFlagInHeader()
	{

		final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/testImpexLegacySwitch.csv");

		ImpExImportReader importReaderLegacy = null;
		try
		{
			importReaderLegacy = createTestImpExImportReader(true, inputStream);
			assertNotNull(importReaderLegacy);
		}
		catch (final UnsupportedEncodingException e)
		{
			fail("this exception should not occur:" + e);
		}

		try
		{
			Object result;
			assertNotNull(result = importReaderLegacy.readLine());
			assertEquals("foo", ((Title) result).getCode());
			assertNotNull(result = importReaderLegacy.readLine());
			assertEquals("barJalo", ((Title) result).getCode());
		}
		catch (final Exception e)
		{
			fail("this exception should not occur:" + e);
		}
		finally
		{
			try
			{
				importReaderLegacy.close();
			}
			catch (final IOException e)
			{
				fail("this exception should not occur:" + e);
			}
		}
	}

	private ImpExImportReader createTestImpExImportReader(final boolean legacyMode, final InputStream script)
			throws UnsupportedEncodingException
	{
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, Boolean.toString(legacyMode));
		final ImpExImportReader importReader = new ImpExImportReader(new CSVReader(script, windows1252.getCode()));
		importReader.enableCodeExecution(true);
		return importReader;
	}

	private ImpExImportReader createTestImpExImportReader(final boolean legacyMode, final String script)
	{
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, Boolean.toString(legacyMode));
		final ImpExImportReader importReader = new ImpExImportReader(script);
		importReader.enableCodeExecution(true);
		return importReader;
	}


	/**
	 * Tests the default modifier in simple cases.
	 */
	@Test
	public void testModifierDefault()
	{
		final SessionContext deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(german);

		// simple test
		Country country = null;
		try
		{
			country = C2LManager.getInstance().createCountry("defValTest");
			country.setName(deCtx, "xxx");
			assertFalse(country.isActive().booleanValue());
			assertEquals("xxx", country.getName(deCtx));
			country.addNewRegion("devValReg");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}

		StringWriter dump = new StringWriter();
		ImpExImportReader importReader = new ImpExImportReader(new CSVReader("INSERT_UPDATE Region;" + Region.CODE
				+ "[unique=true];" + Region.ACTIVE + "[default=false];" + Region.COUNTRY + "(" + Country.ISOCODE + "[default='"
				+ country.getIsoCode() + "']," + Country.ACTIVE + "[default='false']," + Country.NAME + "[lang='"
				+ german.getIsoCode() + "',default='" + country.getName(deCtx) + "']" + ")\n" + ";test1;; \n" // no value
				+ ";test2;; " + country.getIsoCode() + ":false:xxx \n" // fully qualified
				+ ";test3;; " + country.getIsoCode() + " \n" // 'active' and name missing
				+ ";test4;; " + country.getIsoCode() + ":: \n" // 'active' and name missing but separators present
				+ ";test5;; " + country.getIsoCode() + "::xxx \n"// 'active' missing but separators present
		), new CSVWriter(dump));

		try
		{
			final Region test1;
			final Region test2;
			final Region test3;
			final Region test4;
			final Region test5;
			test1 = (Region) importReader.readLine();
			assertEquals(0, importReader.getDumpedLineCount());
			assertEquals(country, test1.getCountry());

			test2 = (Region) importReader.readLine();
			assertEquals(0, importReader.getDumpedLineCount());
			assertEquals(country, test2.getCountry());

			test3 = (Region) importReader.readLine();
			assertEquals(0, importReader.getDumpedLineCount());
			assertEquals(country, test3.getCountry());

			test4 = (Region) importReader.readLine();
			assertEquals(0, importReader.getDumpedLineCount());
			assertEquals(country, test4.getCountry());

			test5 = (Region) importReader.readLine();
			assertEquals(0, importReader.getDumpedLineCount());
			assertEquals(country, test5.getCountry());

			assertNull(importReader.readLine());
			importReader.close();
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}

		// test dumping of unresolvable default values
		dump = new StringWriter();
		final CSVWriter csvWriter = new CSVWriter(dump);
		importReader = new ImpExImportReader(new CSVReader("INSERT_UPDATE Region;" + Region.CODE + "[unique=true];" + Region.ACTIVE
				+ "[default=false];" + Region.COUNTRY + "(" + Country.ISOCODE + "[default='DoesntExist']," + Country.ACTIVE
				+ "[default='true']," + Country.NAME + "[lang='" + german.getIsoCode() + "',default='blah']" + ")\n" + ";test10;"
				+ ";" + " " + "\n" // no value to test default value dumping
		), csvWriter);

		assertEquals(csvWriter, importReader.getCSVWriter());
		try
		{
			assertNull(importReader.readLine());
			importReader.close();
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
		assertEquals(1, importReader.getDumpedLineCount());
		Map[] dumpedLines = CSVReader.parse(dump.getBuffer().toString());
		assertEquals(2, dumpedLines.length);
		assertEquals("test10", dumpedLines[1].get(Integer.valueOf(1)));
		assertTrue(dumpedLines[1].get(Integer.valueOf(2)) == null || "".equals(dumpedLines[1].get(Integer.valueOf(2))));

		// test dumping of unresolvable default values again
		dump = new StringWriter();
		importReader = new ImpExImportReader(new CSVReader("INSERT_UPDATE Region;" + Region.CODE + "[unique=true];" + Region.ACTIVE
				+ "[default=false];" + Region.COUNTRY + "(" + Country.ISOCODE + "," + Country.ACTIVE + "," + "" + Country.NAME
				+ "[lang='" + german.getIsoCode() + "']" + ")[default='DoesntExist:true:blah']\n" + ";test11;; \n" // no
																																					// value
																																					// to
																																					// test
																																					// default
				// value dumping
		), new CSVWriter(dump));

		try
		{
			assertNull(importReader.readLine());
			importReader.close();
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
		assertEquals(1, importReader.getDumpedLineCount());
		dumpedLines = CSVReader.parse(dump.getBuffer().toString());
		assertEquals(2, dumpedLines.length);
		assertEquals("test11", dumpedLines[1].get(Integer.valueOf(1)));
		assertTrue(dumpedLines[1].get(Integer.valueOf(2)) == null || "".equals(dumpedLines[1].get(Integer.valueOf(2))));
	}

	/**
	 * Tests the default modifier behind another modifier. See CORE-3932.
	 */
	@Test
	public void testModifierDefaultBehind()
	{
		if (!ExtensionManager.getInstance().getExtensionNames().contains("catalog"))
		{
			fail("catalog extension required for test");
		}

		Item cv1 = null, cv2 = null;
		final ImpExImportReader importReader = new ImpExImportReader("INSERT Catalog; id[unique=true] \n" + "; c1 \n"
				+ "INSERT CatalogVersion; version[unique=true] ; catalog(id)[unique=true] \n" + "; cv1 ; c1 \n" + "; cv2 ; c1 \n"
				+ "INSERT Product; code[unique=true]; catalogVersion(version,catalog(id))[unique=true, " + "default='cv1:c1'] \n"
				+ "; p1 ; cv2:c1 \n" + "; p2 ;  \n"
				+ "INSERT Product; code[unique=true]; catalogVersion(version,catalog(id[default='c1']))[unique=true, "
				+ "default='cv1:c1'] \n" + "; p3 ; cv2 \n" + "; p4 ;  \n");
		try
		{
			final Item catalog = (Item) importReader.readLine();
			// c1
			assertNotNull(catalog);
			assertEquals("c1", catalog.getAttribute("id"));
			// cv1
			cv1 = (Item) importReader.readLine();
			assertNotNull(cv1);
			assertEquals("cv1", cv1.getAttribute("version"));
			assertEquals(catalog, cv1.getAttribute("catalog"));
			// cv2
			cv2 = (Item) importReader.readLine();
			assertNotNull(cv2);
			assertEquals("cv2", cv2.getAttribute("version"));
			assertEquals(catalog, cv2.getAttribute("catalog"));
			// product1
			final Item product1 = (Item) importReader.readLine();
			assertNotNull(product1);
			assertEquals("p1", product1.getAttribute("code"));
			assertEquals(cv2, product1.getAttribute("catalogVersion"));
			// product2
			final Item product2 = (Item) importReader.readLine();
			assertNotNull(product2);
			assertEquals("p2", product2.getAttribute("code"));
			assertEquals(cv1, product2.getAttribute("catalogVersion"));
			// product3
			final Item product3 = (Item) importReader.readLine();
			assertNotNull(product3);
			assertEquals("p3", product3.getAttribute("code"));
			assertEquals(cv2, product3.getAttribute("catalogVersion"));
			// product4
			final Item product4 = (Item) importReader.readLine();
			assertNotNull(product4);
			assertEquals("p4", product4.getAttribute("code"));
			assertEquals(cv1, product4.getAttribute("catalogVersion"));

			assertNull(importReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getMessage());
		}
		catch (final JaloSecurityException e)
		{
			fail("unexpected exception " + e.getMessage());
		}
	}

	/**
	 * Tests the default modifier in case of translators. See CORE-3932.
	 */
	@Test
	public void testModifierDefaultTranslator()
	{
		if (!ExtensionManager.getInstance().getExtensionNames().contains("europe1"))
		{
			fail("europe1 extension required for test");
		}
		Currency eur = null;
		try
		{
			eur = C2LManager.getInstance().getCurrencyByIsoCode("eur");
		}
		catch (final JaloItemNotFoundException e)
		{
			try
			{
				eur = C2LManager.getInstance().createCurrency("eur");
			}
			catch (final ConsistencyCheckException e1)
			{
				fail(e1.getMessage());
			}
		}
		Unit pieces = ProductManager.getInstance().getUnit("pieces");
		if (pieces == null)
		{
			pieces = ProductManager.getInstance().createUnit("pieces", "pieces");
		}

		final ImpExImportReader importReader = new ImpExImportReader("INSERT Product; " + "code[unique=true]; "
				+ "unit(code)[default='pieces']; " + "catalogVersion(version,catalog(id))[unique=true, allowNull=true];"
				+ "europe1prices[translator=de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator, default='10,00 EUR'] \n"
				+ "; p5 ; pieces; ; 23 EUR \n" + "; p6 \n");

		try
		{
			// product5
			final Item product5 = (Item) importReader.readLine();
			assertNotNull(product5);
			assertEquals("p5", product5.getAttribute("code"));
			// assertEquals( cv2, p5.getAttribute( "catalogVersion" ) );
			assertEquals(pieces, product5.getAttribute("unit"));
			Collection<Item> priceRows = (Collection<Item>) product5.getAttribute("europe1prices");
			assertNotNull(priceRows);
			assertEquals(1, priceRows.size());
			Item priceRow = priceRows.iterator().next();
			assertEquals(product5, priceRow.getAttribute("product"));
			assertEquals(eur, priceRow.getAttribute("currency"));
			assertEquals(Double.valueOf(23.0), priceRow.getAttribute("price"));
			// product6
			final Item product6 = (Item) importReader.readLine();
			assertNotNull(product6);
			assertEquals("p6", product6.getAttribute("code"));
			// assertEquals( cv1, p6.getAttributeBeforeOperation( "catalogVersion" ) );
			assertEquals(pieces, product6.getAttribute("unit"));
			priceRows = (Collection<Item>) product6.getAttribute("europe1prices");
			assertNotNull(priceRows);
			assertEquals(1, priceRows.size());
			priceRow = priceRows.iterator().next();
			assertEquals(product6, priceRow.getAttribute("product"));
			assertEquals(eur, priceRow.getAttribute("currency"));
			assertEquals(Double.valueOf(10.0), priceRow.getAttribute("price"));

			assertNull(importReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getMessage());
		}
		catch (final JaloSecurityException e)
		{
			fail("unexpected exception " + e.getMessage());
		}
	}

	/**
	 * Tests the default modifier in case of special translators. See CORE-3932.
	 */
	@Test
	public void testModifierDefaultSpecialTranslator()
	{
		PasswordEncoder enc = null;
		try
		{
			enc = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder("md5");
		}
		catch (final PasswordEncoderNotFoundException e)
		{
			fail("cannot get MD5 password encoder (though it is mapped) : " + e);
		}

		ImpExImportReader importReader = null;

		if (enc instanceof SaltedMD5PasswordEncoder)
		{
			importReader = new ImpExImportReader("INSERT_UPDATE Employee;" + Employee.UID + "[unique=true];"
					+ ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + "password[" + ImpExConstants.Syntax.Modifier.TRANSLATOR + "="
					+ UserPasswordTranslator.class.getName() + "," + ImpExConstants.Syntax.Modifier.DEFAULT
					+ "='md5:570bfc8e78236027d68be7b5bbd9c9fb'" + "] \n" + "; axel ; \n" // <- use default value
					+ "; axel2 ; *:axel2 \n" // <- set directly
			);
		}
		else
		{
			importReader = new ImpExImportReader("INSERT_UPDATE Employee;" + Employee.UID + "[unique=true];"
					+ ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + "password[" + ImpExConstants.Syntax.Modifier.TRANSLATOR + "="
					+ UserPasswordTranslator.class.getName() + "," + ImpExConstants.Syntax.Modifier.DEFAULT
					+ "='md5:a7c15c415c37626de8fa648127ba1ae5'" + "] \n" + "; axel ; \n" // <- use default value
					+ "; axel2 ; *:axel2 \n" // <- set directly
			);
		}

		try
		{
			final Employee axel = (Employee) importReader.readLine();
			assertNotNull(axel);


			if (enc instanceof SaltedMD5PasswordEncoder)
			{
				assertEquals("570bfc8e78236027d68be7b5bbd9c9fb", axel.getEncodedPassword());
			}
			else
			{
				assertEquals("a7c15c415c37626de8fa648127ba1ae5", axel.getEncodedPassword());
			}

			assertEquals("md5", axel.getPasswordEncoding());
			assertTrue(axel.checkPassword("axel"));

			final Employee axel2 = (Employee) importReader.readLine();
			assertNotNull(axel2);
			assertEquals("axel2", axel2.getEncodedPassword());
			assertEquals(SystemEJB.DEFAULT_ENCODING, axel2.getPasswordEncoding());
			assertTrue(axel2.checkPassword("axel2"));

			assertNull(importReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail("unexpected error " + e.getMessage());
		}
	}

	/**
	 * Tests the modifier batch mode.
	 */
	@Test
	public void testModifierBatchMode()
	{
		final ImpExImportReader importReader = new ImpExImportReader("INSERT Title; " + Title.CODE + " \n" + "; t1 \n" + "; t2 \n"
				+ "; t3 \n" + "; t4 \n" + "INSERT Link; " + Link.QUALIFIER + ";" + Link.SOURCE + "(Title." + Title.CODE + ");"
				+ Link.TARGET + "(Title." + Title.CODE + ")\n" + ";test;t1;t2 \n" + ";test;t1;t3 \n" + ";test;t1;t4 \n"
				+ ";test;t2;t3 \n" + ";test;t3;t4 \n" + "UPDATE Link[batchmode=true]; " + Link.SOURCE + "(Title." + Title.CODE
				+ ")[unique=true];" + Link.QUALIFIER + " \n" + ";t1; testtest \n" + ";t3; testtesttest \n" + "UPDATE Link; "
				+ Link.SOURCE + "(Title." + Title.CODE + ")[unique=true];" + Link.QUALIFIER + " \n" + ";t1; xxx \n");

		Object object = null;
		final Title title1;
		final Title title2;
		final Title title3;
		final Title title4;
		final Link link1to2;
		final Link link1to3;
		final Link link1to4;
		final Link link2to3;
		final Link link3to4;

		try
		{
			object = importReader.readLine();
			assertNotNull(object);
			title1 = (Title) object;
			assertEquals("t1", title1.getCode());

			object = importReader.readLine();
			assertNotNull(object);
			title2 = (Title) object;
			assertEquals("t2", title2.getCode());

			object = importReader.readLine();
			assertNotNull(object);
			title3 = (Title) object;
			assertEquals("t3", title3.getCode());

			object = importReader.readLine();
			assertNotNull(object);
			title4 = (Title) object;
			assertEquals("t4", title4.getCode());

			object = importReader.readLine();
			assertNotNull(object);
			link1to2 = (Link) object;
			assertEquals(title1, link1to2.getSource());
			assertEquals(title2, link1to2.getTarget());
			assertEquals("test", link1to2.getQualifier());

			object = importReader.readLine();
			assertNotNull(object);
			link1to3 = (Link) object;
			assertEquals(title1, link1to3.getSource());
			assertEquals(title3, link1to3.getTarget());
			assertEquals("test", link1to3.getQualifier());

			object = importReader.readLine();
			assertNotNull(object);
			link1to4 = (Link) object;
			assertEquals(title1, link1to4.getSource());
			assertEquals(title4, link1to4.getTarget());
			assertEquals("test", link1to4.getQualifier());

			object = importReader.readLine();
			assertNotNull(object);
			link2to3 = (Link) object;
			assertEquals(title2, link2to3.getSource());
			assertEquals(title3, link2to3.getTarget());
			assertEquals("test", link2to3.getQualifier());

			object = importReader.readLine();
			assertNotNull(object);
			link3to4 = (Link) object;
			assertEquals(title3, link3to4.getSource());
			assertEquals(title4, link3to4.getTarget());
			assertEquals("test", link3to4.getQualifier());

			// now try mass update
			object = importReader.readLine();
			assertNotNull(object);
			assertTrue(object.equals(link1to2) || object.equals(link1to3) || object.equals(link1to4)); // dont know which
																																		// one is first
			assertEquals("testtest", link1to2.getQualifier());
			assertEquals("testtest", link1to3.getQualifier());
			assertEquals("testtest", link1to4.getQualifier());

			object = importReader.readLine();
			assertNotNull(object);
			assertEquals(link3to4, object); // just one so we're sure ;)
			assertEquals("testtesttest", link3to4.getQualifier());

			// other links still unchanged ?
			assertEquals("test", link2to3.getQualifier());

			// now test exception: batch update should not work for t1 now

			try
			{
				importReader.readLine();
				fail("missing AmbiguousItemException");
			}
			catch (final AmbiguousItemException e)
			{
				// fine here
			}
		}
		catch (final ImpExException e)
		{
			fail("error: " + e.getMessage());
		}
	}

	/**
	 * Tests the modifier for caching unique keys and related
	 * {@link de.hybris.platform.impex.jalo.imp.CachingExistingItemResolver}.
	 */
	@Test
	public void testModifierCacheUniqueKeys()
	{
		final ImpExImportReader importReader = new ImpExImportReader("INSERT Title; " + Title.CODE + "[unique=true] \n" + "; t1 \n"
				+ "; t2 \n" + "INSERT Title[" + ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS + "=true]; " + Title.CODE
				+ "[unique=true] \n" + "; t3 \n" + "; t4 \n" + "INSERT_UPDATE Title["
				+ ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS + "=true]; " + Title.CODE + "[unique=true] \n" + "; t3 \n"
				+ "; t4 \n" + "INSERT Title["
				+ ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS
				+ "=true]; "
				+ Title.CODE
				+ "[unique=true] \n"
				+ "; t5 \n"
				+ "; t1 \n" // should fail since 't1' already exists
				+ "; t5 \n" // should fail since 't5' already exists
				+ "INSERT UserGroup[" + ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS + "=true]; " + UserGroup.UID
				+ "[unique=true] ;" + UserGroup.DESCRIPTION + " \n" + "; g1; group 1 \n"
				+ "; g2; group 2 \n"
				// now test cached search via jalo-only attribute 'groups'
				+ "INSERT Employee[" + ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS + "=true]; " + Employee.UID + ";"
				+ Employee.NAME + "[unique=true];" + Employee.GROUPS + "(" + UserGroup.UID + ")[unique=true] \n"
				+ ";pcpl_1; max ; g1 \n" + ";pcpl_2; max ; g2 \n" + ";pcpl_3; moritz ; g1,g2 \n" + ";pcpl_4; max ; g1 \n" //
				// should fail since there is alreay one 'max' inside 'g1'
				+ ";pcpl_5; moritz ; g1,g2 \n" // should fail since there is alreay one 'moritz' inside 'g1' and 'g2' - note
															// the
				// changed group order !
				+ "INSERT Title[" + ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS + "=true]; " + Title.CODE + "\n" + "; t6 \n");

		final Title title1;
		final Title title2;
		final Title title3;
		final Title title4;
		final Title title5;
		try
		{
			title1 = (Title) importReader.readLine();
			assertNotNull(title1);
			assertEquals("t1", title1.getCode());
			assertNull(importReader.getCurrentHeader().getDescriptorData()
					.getModifier(ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS));

			title2 = (Title) importReader.readLine();
			assertNotNull(title2);
			assertEquals("t2", title2.getCode());

			title3 = (Title) importReader.readLine();
			assertNotNull(title3);
			assertEquals("t3", title3.getCode());

			assertEquals("true",
					importReader.getCurrentHeader().getDescriptorData().getModifier(ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS));

			title4 = (Title) importReader.readLine();
			assertNotNull(title4);
			assertEquals("t4", title4.getCode());

			assertEquals(title3, importReader.readLine());
			assertEquals("true",
					importReader.getCurrentHeader().getDescriptorData().getModifier(ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS));
			assertEquals(title4, importReader.readLine());

			title5 = (Title) importReader.readLine();
			assertNotNull(title5);
			assertEquals("t5", title5.getCode());

			assertEquals("true",
					importReader.getCurrentHeader().getDescriptorData().getModifier(ImpExConstants.Syntax.Modifier.CACHE_UNIQUE_KEYS));

			try
			{
				importReader.readLine();
				fail("expected ItemConflictException");
			}
			catch (final ItemConflictException e)
			{
				// fine here
			}

			try
			{
				importReader.readLine();
				fail("expected ItemConflictException");
			}
			catch (final ItemConflictException e)
			{
				// fine here
			}

			final UserGroup userGroup1;
			final UserGroup userGroup2;
			final Employee principal1;
			final Employee principal2;
			final Employee principal3;

			userGroup1 = (UserGroup) importReader.readLine();
			assertNotNull(userGroup1);
			assertEquals("g1", userGroup1.getUID());

			userGroup2 = (UserGroup) importReader.readLine();
			assertNotNull(userGroup2);
			assertEquals("g2", userGroup2.getUID());

			principal1 = (Employee) importReader.readLine();
			assertNotNull(principal1);
			assertEquals("pcpl_1", principal1.getUID());
			assertEquals("max", principal1.getName());
			assertEquals(Collections.singleton(userGroup1), principal1.getGroups());

			principal2 = (Employee) importReader.readLine();
			assertNotNull(principal2);
			assertEquals("pcpl_2", principal2.getUID());
			assertEquals("max", principal2.getName());
			assertEquals(Collections.singleton(userGroup2), principal2.getGroups());

			principal3 = (Employee) importReader.readLine();
			assertNotNull(principal3);
			assertEquals("pcpl_3", principal3.getUID());
			assertEquals("moritz", principal3.getName());
			assertEquals(new HashSet(Arrays.asList(new UserGroup[] { userGroup1, userGroup2 })), principal3.getGroups());

			try
			{
				assertNotNull(importReader.readLine());
				fail("expected ItemConflictException");
			}
			catch (final ItemConflictException e)
			{
				// fine here
			}

			try
			{
				assertNotNull(importReader.readLine());
				fail("expected ItemConflictException");
			}
			catch (final ItemConflictException e)
			{
				// fine here
			}

			try
			{
				assertNotNull(importReader.readLine());
				fail("expected HeaderValidationException");
			}
			catch (final HeaderValidationException e)
			{
				// fine here
			}
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests import of collections using different collection delimiters with modifier collection-delimiter.
	 */
	@Test
	public void testModifierCollectionDelimiter() throws ImpExException
	{
		final String data =
		// create Languages
		"INSERT Language ; isoCode[unique=true]; active \n"//
				+ "; l1 ; false\n"//
				+ "; l2 ; false\n"//
				+ "; l3 ; false\n"
				// create fallbacks
				+ "INSERT Language; isocode[unique=true]; active; \n"//
				+ "; f1; true \n"//
				+ "; f2; true \n"//
				+ "; f3; true \n"
				// test standard , delimiter
				+ "UPDATE Language ; isoCode[unique=true]; fallbackLanguages(isoCode) \n"//
				+ "; l1 ; f1,f2,f3 \n"
				// test custom | delimiter
				+ "UPDATE Language ; isoCode[unique=true]; fallbackLanguages(isocode)[collection-delimiter='|'] \n"//
				+ "; l2 ; f1|f2|f3 \n"
				// test custom ; delimiter
				+ "UPDATE Language ; isoCode[unique=true]; \"fallbackLanguages(isocode)[collection-delimiter=';']\" \n"//
				+ "; l3 ; \"f1;f2;f3 \"\n";

		final ImpExImportReader importReader = new ImpExImportReader(new CSVReader(new StringReader(data)), null);

		final Language language1;
		final Language language2;
		final Language language3;
		final Language fallback1;
		final Language fallback2;
		final Language fallback3;

		// create Languages
		language1 = (Language) importReader.readLine();
		assertNotNull(language1);
		language2 = (Language) importReader.readLine();
		assertNotNull(language2);
		language3 = (Language) importReader.readLine();
		assertNotNull(language3);

		// create fallbacks
		fallback1 = (Language) importReader.readLine();
		assertNotNull(fallback1);
		fallback2 = (Language) importReader.readLine();
		assertNotNull(fallback2);
		fallback3 = (Language) importReader.readLine();
		assertNotNull(fallback3);

		// assign fallbacks via collection attribute
		final List coll = Arrays.asList(new Object[] { fallback1, fallback2, fallback3 });

		assertEquals(language1, importReader.readLine()); // updates l1
		assertCollection(coll, language1.getFallbackLanguages());

		assertEquals(language2, importReader.readLine()); // updates l2
		assertCollection(coll, language2.getFallbackLanguages());

		assertEquals(language3, importReader.readLine()); // updates l3
		assertCollection(coll, language3.getFallbackLanguages());

		assertNull(importReader.readLine());
	}

	/**
	 * Tests if the default value for an collection attribute can be composed from default values from item path. See
	 * PLA-5.
	 */
	@Test
	public void testModifierDefaultCollection()
	{
		if (ExtensionManager.getInstance().getExtension("catalog") == null)
		{
			LOG.warn("Extension  catalog not available - skipping test ");
			return;
		}
		final String lines = "INSERT_UPDATE Catalog;id[unique=true]\n" // create test catalog
				+ ";testCatalog\n"
				// create test version
				+ "INSERT_UPDATE CatalogVersion;catalog(id)[unique=true];version[unique=true];languages(isocode)\n"
				+ ";testCatalog;testVersion;de\n"
				// create test category
				+ "INSERT_UPDATE category;code[unique=true];catalogVersion(version,catalog(id));supercategories;\n"
				+ ";testCategory;testVersion:testCatalog;\n"

				// 1.full separate defaults
				+ "INSERT_UPDATE Product;code[unique=true];catalogVersion(version[default=testVersion],"
				+ "catalog(id[default=testCatalog]));supercategories(code[default=testCategory],"
				+ "catalogVersion(version[default=testVersion],catalog(id[default=testCatalog])))[virtual=true]\n"
				+ ";dirk1\n"
				// 2.incomplete separate defaults
				+ "INSERT_UPDATE Product;code[unique=true];catalogVersion(version[default=testVersion],"
				+ "catalog(id[default=testCatalog]));supercategories(code,catalogVersion(version[default=testVersion],"
				+ "catalog(id[default=testCatalog])))[virtual=true]\n"
				+ ";dirk2\n"
				// 3.full global default
				+ "INSERT_UPDATE Product;code[unique=true];catalogVersion(version[default=testVersion],"
				+ "catalog(id[default=testCatalog]));supercategories(code,catalogVersion(version,catalog(id)))[virtual=true,"
				+ "default=testCategory:testVersion:testCatalog]\n"
				+ ";dirk3\n"
				// 4.full separate defaults + add mode
				+ "INSERT_UPDATE Product;code[unique=true];catalogVersion(version[default=testVersion],"
				+ "catalog(id[default=testCatalog]));supercategories(code[default=testCategory],"
				+ "catalogVersion(version[default=testVersion],catalog(id[default=testCatalog])))[append=true,virtual=true]\n"
				+ ";dirk4\n"
				// 5.no defaults
				+ "INSERT_UPDATE Product;code[unique=true];catalogVersion(version[default=testVersion],"
				+ "catalog(id[default=testCatalog]));supercategories(code,catalogVersion(version,catalog(id)))[virtual=true]\n"
				+ ";dirk5\n";

		final ImpExImportReader reader = new ImpExImportReader(lines);
		try
		{
			final Item testCatalog = (Item) reader.readLine();
			assertNotNull(testCatalog);

			final Item testVersion = (Item) reader.readLine();
			assertNotNull(testVersion);

			final Item testCategory = (Item) reader.readLine();
			assertNotNull(testCategory);

			final Product dirk1 = (Product) reader.readLine();
			assertTrue(((Collection) testCategory.getAttribute("PRODUCTS")).size() == 1);
			assertEquals("dirk1", ((Product) ((Collection) testCategory.getAttribute("PRODUCTS")).iterator().next()).getCode());
			dirk1.remove();

			final Product dirk2 = (Product) reader.readLine();
			assertTrue(((Collection) testCategory.getAttribute("PRODUCTS")).size() == 0);
			dirk2.remove();

			final Product dirk3 = (Product) reader.readLine();
			assertTrue(((Collection) testCategory.getAttribute("PRODUCTS")).size() == 1);
			assertEquals("dirk3", ((Product) ((Collection) testCategory.getAttribute("PRODUCTS")).iterator().next()).getCode());
			dirk3.remove();

			final Product dirk4 = (Product) reader.readLine();
			assertTrue(((Collection) testCategory.getAttribute("PRODUCTS")).size() == 1);
			assertEquals("dirk4", ((Product) ((Collection) testCategory.getAttribute("PRODUCTS")).iterator().next()).getCode());
			dirk4.remove();

			final Product dirk5 = (Product) reader.readLine();
			assertTrue(((Collection) testCategory.getAttribute("PRODUCTS")).size() == 0);
			dirk5.remove();
		}
		catch (final Exception e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}
		try
		{
			reader.close();
		}
		catch (final IOException e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}
	}

	/**
	 * Tests the forceWrite modifier.
	 * <p/>
	 * Tests only insertion of non modifiable because there are no real case where there is used a non-initial
	 * not-writable attribute :
	 * <p/>
	 * Or you have full mutable attribute- which is not a case here;
	 * <p/>
	 * Or you have non writable but modified only for initial purposes attribute;
	 */
	@Test
	public void testModifierForceWrite()
	{
		CronJobManager.getInstance().createBatchJob("someBatchJob");

		final Integer itemsMaxCount1 = 100;
		final Integer itemsMaxCount2 = 200;
		final Integer itemsMaxCount3 = 300;

		final StringBuilder script = new StringBuilder(
				"INSERT ImpExExportCronJob;code[unique=true];active;job(code);itemsMaxCount;\n");
		script.append(";testTitleCode1;false;someBatchJob;" + itemsMaxCount1 + ";\n");

		script.append("INSERT ImpExExportCronJob;code[unique=true];active;job(code);itemsMaxCount[forceWrite=true];\n");
		script.append(";testTitleCode2;false;someBatchJob;" + itemsMaxCount2 + ";\n");

		script.append("UPDATE ImpExExportCronJob;code[unique=true];active;job(code);itemsMaxCount[forceWrite=true];\n");
		script.append(";testTitleCode2;false;someBatchJob;" + itemsMaxCount3 + ";\n");

		final ImpExImportReader importReader = new ImpExImportReader(script.toString());

		try
		{
			final ImpExExportCronJob link1 = (ImpExExportCronJob) importReader.readLine();
			assertNotNull(link1);
			assertEquals("testTitleCode1", link1.getCode());
			assertFalse("non writable attribute should not be adjusted if no forceWrite provided ",
					itemsMaxCount1.equals(link1.getItemsMaxCount()));
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}

		try
		{
			final ImpExExportCronJob link2 = (ImpExExportCronJob) importReader.readLine();
			assertNotNull(link2);
			assertEquals("testTitleCode2", link2.getCode());
			assertEquals(itemsMaxCount2, link2.getItemsMaxCount());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}

		try
		{
			final ImpExExportCronJob link3 = (ImpExExportCronJob) importReader.readLine();
			assertNotNull(link3);
			assertEquals("testTitleCode2", link3.getCode());
			assertEquals(itemsMaxCount3, link3.getItemsMaxCount());

		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}

	}

	/**
	 * Tests modifier path-delimiter.
	 */
	@Test
	public void testModifierPathDelimiter()
	{
		Country country = null;
		Region region = null;
		try
		{
			country = C2LManager.getInstance().createCountry("patternTest");
			assertFalse(country.isActive().booleanValue());
			region = country.addNewRegion("impexReg");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}

		String header = "INSERT_UPDATE Region;" + Region.CODE + "[unique=true];" + Region.COUNTRY + "(" + Country.ISOCODE + ","
				+ Country.ACTIVE + ")";

		try
		{
			final StringWriter stringWriter = new StringWriter();
			final ImpExCSVExportWriter writer = new ImpExCSVExportWriter(new CSVWriter(stringWriter));
			writer.setCurrentHeader(header, ImpExManager.getImportRelaxedMode());
			writer.setColumnOffset(0);
			writer.writeCurrentHeader(false);
			writer.writeLine(region);
			writer.close();

			final Map[] lines = CSVReader.parse(stringWriter.getBuffer().toString());
			assertEquals(2, lines.length);
			assertEquals(region.getCode(), lines[1].get(Integer.valueOf(1)));
			assertEquals(country.getIsoCode() + ":false", lines[1].get(Integer.valueOf(2)));
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}

		// test if escaping works
		try
		{
			country.setIsoCode("pattern:Test");
			final StringWriter stringWriter = new StringWriter();
			final ImpExCSVExportWriter writer = new ImpExCSVExportWriter(new CSVWriter(stringWriter));
			writer.setColumnOffset(0);
			writer.setCurrentHeader(header, ImpExManager.getImportRelaxedMode());
			writer.writeCurrentHeader(false);
			writer.writeLine(region);
			writer.close();

			final Map[] lines = CSVReader.parse(stringWriter.getBuffer().toString());
			assertEquals(2, lines.length);
			assertEquals(region.getCode(), lines[1].get(Integer.valueOf(1)));
			assertEquals("pattern\\:Test:false", lines[1].get(Integer.valueOf(2)));

			// test re-import
			final ImpExImportReader read = new ImpExImportReader(header + "\n" + stringWriter.getBuffer().toString() + "\n");
			read.setValidationMode(ImpExManager.getImportRelaxedMode());

			final Object object = read.readLine();
			assertEquals(0, read.getDumpedLineCount());
			assertEquals(region, object);
			read.close();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}

		// test custom delimiter (including escaping)
		try
		{
			country.setIsoCode("pattern_Test");
			header = "INSERT_UPDATE Region;" + Region.CODE + "[unique=true];" + Region.COUNTRY + "(" + Country.ISOCODE + ","
					+ Country.ACTIVE + ")[" + ImpExConstants.Syntax.Modifier.ATTRIBUTE_PATH_DELIMITER + "='_']";

			final StringWriter stringWriter = new StringWriter();
			final ImpExCSVExportWriter writer = new ImpExCSVExportWriter(new CSVWriter(stringWriter));
			writer.setColumnOffset(0);
			writer.setCurrentHeader(header, ImpExManager.getImportRelaxedMode());
			writer.writeCurrentHeader(false);
			writer.writeLine(region);
			writer.close();

			final Map[] lines = CSVReader.parse(stringWriter.getBuffer().toString());
			assertEquals(2, lines.length);
			assertEquals(region.getCode(), lines[1].get(Integer.valueOf(1)));
			assertEquals("pattern\\_Test_false", lines[1].get(Integer.valueOf(2)));
			// test re-import
			final ImpExImportReader read = new ImpExImportReader(header + "\n" + stringWriter.getBuffer().toString() + "\n");
			read.setValidationMode(ImpExManager.getImportRelaxedMode());

			final Object object = read.readLine();
			assertEquals(0, read.getDumpedLineCount());
			assertEquals(region, object);
			read.close();
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the modifier <code>pos</code>.
	 */
	@Test
	public void testModifierPosition()
	{
		// try to use header where first column misses position modifier
		ImpExImportReader importReader = null;
		importReader = new ImpExImportReader("INSERT Unit; " + Unit.UNITTYPE + " ; " + Unit.CODE + "["
				+ ImpExConstants.Syntax.Modifier.UNIQUE + "=true, " + ImpExConstants.Syntax.Modifier.POSITION + "=4]; "
				+ Unit.CONVERSION + "[" + ImpExConstants.Syntax.Modifier.POSITION + "=6] ; " + Unit.NAME + "["
				+ ImpExConstants.Syntax.Modifier.LANGUAGE + "=" + german.getIsoCode() + "," + ImpExConstants.Syntax.Modifier.POSITION
				+ "=7] \n" + "; fill ; unit1 ; fill ; kg ; fill; 1 ; Kilogramm \n"
				+ "; fill ; unit2 ; fill; g ; fill; 0,001 ; Gramm \n");
        importReader.setInvalidHeaderPolicy(InvalidHeaderPolicy.THROW_EXCEPTION);
		try
		{
			importReader.readLine();
			fail();
		}
		catch (final ImpExException e1)
		{
			// OK
		}
		// try to use header where last column misses position modifier
		importReader = new ImpExImportReader("INSERT Unit; " + Unit.UNITTYPE + "[" + ImpExConstants.Syntax.Modifier.POSITION
				+ "=2] ; " + Unit.CODE + "[" + ImpExConstants.Syntax.Modifier.UNIQUE + "=true, "
				+ ImpExConstants.Syntax.Modifier.POSITION + "=4]; " + Unit.CONVERSION + "[" + ImpExConstants.Syntax.Modifier.POSITION
				+ "=2] ; " + Unit.NAME + "[" + ImpExConstants.Syntax.Modifier.LANGUAGE + "=" + german.getIsoCode() + "] \n"
				+ "; fill ; unit1 ; fill ; kg ; fill; 1 ; Kilogramm \n" + "; fill ; unit2 ; fill; g ; fill; 0,001 ; Gramm \n");
        importReader.setInvalidHeaderPolicy(InvalidHeaderPolicy.THROW_EXCEPTION);
		try
		{
			importReader.readLine();
			fail();
		}
		catch (final ImpExException e1)
		{
			// OK
		}
		// try to use header where a position is used twice
		importReader = new ImpExImportReader("INSERT Unit; " + Unit.UNITTYPE + "[" + ImpExConstants.Syntax.Modifier.POSITION
				+ "=2] ; " + Unit.CODE + "[" + ImpExConstants.Syntax.Modifier.UNIQUE + "=true, "
				+ ImpExConstants.Syntax.Modifier.POSITION + "=4]; " + Unit.CONVERSION + "[" + ImpExConstants.Syntax.Modifier.POSITION
				+ "=2] ; " + Unit.NAME + "[" + ImpExConstants.Syntax.Modifier.LANGUAGE + "=" + german.getIsoCode() + ","
				+ ImpExConstants.Syntax.Modifier.POSITION + "=7] \n" + "; fill ; unit1 ; fill ; kg ; fill; 1 ; Kilogramm \n"
				+ "; fill ; unit2 ; fill; g ; fill; 0,001 ; Gramm \n");
        importReader.setInvalidHeaderPolicy(InvalidHeaderPolicy.THROW_EXCEPTION);
		try
		{
			importReader.readLine();
			fail();
		}
		catch (final ImpExException e1)
		{
			// OK
		}
		// create test header
		importReader = new ImpExImportReader("INSERT Unit; " + Unit.UNITTYPE + "[" + ImpExConstants.Syntax.Modifier.POSITION
				+ "=2] ; " + Unit.CONVERSION + "[" + ImpExConstants.Syntax.Modifier.POSITION + "=6] ; " + Unit.CODE + "["
				+ ImpExConstants.Syntax.Modifier.UNIQUE + "=true, " + ImpExConstants.Syntax.Modifier.POSITION + "=4]; " + Unit.NAME
				+ "[" + ImpExConstants.Syntax.Modifier.LANGUAGE + "=" + german.getIsoCode() + ","
				+ ImpExConstants.Syntax.Modifier.POSITION + "=7] \n" + "; fill ; unit1 ; fill ; kg ; fill; 1 ; Kilogramm \n"
				+ "; fill ; unit2 ; fill; g ; fill; 0,001 ; Gramm \n");
        importReader.setInvalidHeaderPolicy(InvalidHeaderPolicy.THROW_EXCEPTION);
		final Unit unit1;
		final Unit unit2;
		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(german);
		try
		{
			// chech first unit
			unit1 = (Unit) importReader.readLine();
			assertNotNull(unit1);
			assertEquals("unit1", unit1.getUnitType());
			assertEquals(1d, unit1.getConversionFactor(), 0);
			assertEquals("kg", unit1.getCode());
			assertEquals("Kilogramm", unit1.getName(ctx));

			// check column positions in header
			final HeaderDescriptor headerDescriptor = importReader.getCurrentHeader();
			assertEquals(4, headerDescriptor.getColumns().size());
			Collection<AbstractColumnDescriptor> col = headerDescriptor.getColumnsByQualifier(Unit.UNITTYPE);
			assertEquals(col.size(), 1);
			assertEquals(col.iterator().next().getValuePosition(), 2);
			col = headerDescriptor.getColumnsByQualifier(Unit.CONVERSION);
			assertEquals(col.size(), 1);
			assertEquals(col.iterator().next().getValuePosition(), 6);
			col = headerDescriptor.getColumnsByQualifier(Unit.CODE);
			assertEquals(col.size(), 1);
			assertEquals(col.iterator().next().getValuePosition(), 4);
			col = headerDescriptor.getColumnsByQualifier(Unit.NAME);
			assertEquals(col.size(), 1);
			assertEquals(col.iterator().next().getValuePosition(), 7);
			// check second unit
			unit2 = (Unit) importReader.readLine();
			assertNotNull(unit2);
			assertEquals("unit2", unit2.getUnitType());
			assertEquals(0.001d, unit2.getConversionFactor(), 0);
			assertEquals("g", unit2.getCode());
			assertEquals("Gramm", unit2.getName(ctx));
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Empty extension of <code>DefaultImportProcessor</code> for testing the modifier <code>processor</code> at tests
	 * case {@link ImpExModifierTest#testModifierProcessor()} (there is no other implementation of an
	 * <code>ImportProcessor</code>).
	 */
	public static class TestImportProcessor extends DefaultImportProcessor
	{
		// OK
	}

	/**
	 * Tests the <code>processor</code> modifier.
	 */
	@Test
	public void testModifierProcessor()
	{
		final ImpExImportReader importReader = new ImpExImportReader("INSERT_UPDATE Country" + ImpExConstants.Syntax.MODIFIER_START
				+ ImpExConstants.Syntax.Modifier.PROCESSOR + ImpExConstants.Syntax.MODIFIER_EQUAL
				+ TestImportProcessor.class.getName() + ImpExConstants.Syntax.MODIFIER_END + ";" + Country.ISOCODE + "[unique=true];"
				+ Country.ACTIVE + "\n" + ";test1;false \n" + "INSERT_UPDATE Country;" + Country.ISOCODE + "[unique=true];"
				+ Country.ACTIVE + "\n" + ";test1;false \n" + "INSERT_UPDATE Country;" + Country.ISOCODE + "[unique=true,"
				+ ImpExConstants.Syntax.Modifier.PROCESSOR + ImpExConstants.Syntax.MODIFIER_EQUAL
				+ TestImportProcessor.class.getName() + ImpExConstants.Syntax.MODIFIER_END + ";" + Country.ACTIVE + "\n"
				+ ";test1;false \n" + "INSERT_UPDATE Country" + ImpExConstants.Syntax.MODIFIER_START
				+ ImpExConstants.Syntax.Modifier.PROCESSOR + "r" + ImpExConstants.Syntax.MODIFIER_EQUAL
				+ TestImportProcessor.class.getName() + ImpExConstants.Syntax.MODIFIER_END + ";" + Country.ISOCODE + "[unique=true];"
				+ Country.ACTIVE + "\n" + ";test1;false \n");
		Country test = null;
		try
		{
			test = (Country) importReader.readLine();
			assertEquals(TestImportProcessor.class, importReader.getImportProcessor().getClass());
			assertNotNull(test);
			test = (Country) importReader.readLine();
			assertEquals(DefaultImportProcessor.class, importReader.getImportProcessor().getClass());
			assertNotNull(test);
			test = (Country) importReader.readLine();
			assertEquals(DefaultImportProcessor.class, importReader.getImportProcessor().getClass());
			assertNotNull(test);
			test = (Country) importReader.readLine();
			assertEquals(DefaultImportProcessor.class, importReader.getImportProcessor().getClass());
			assertNotNull(test);
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}

		try
		{
			importReader.close();
		}
		catch (final IOException e)
		{
			fail();
		}
	}

	/**
	 * Tests the modifier virtual.
	 */
	@Test
	public void testModifierVirtual()
	{
		final ImpExImportReader importReader = new ImpExImportReader("$unitType=" + Unit.UNITTYPE + "["
				+ ImpExConstants.Syntax.Modifier.DEFAULT + "='xxx'" + "] \n" + "$conv=" + Unit.CONVERSION + "["
				+ ImpExConstants.Syntax.Modifier.DEFAULT + "='2'" + "] \n" + "INSERT Unit; $unitType["
				+ ImpExConstants.Syntax.Modifier.VIRTUAL + "=true] ; " + Unit.CODE + "[unique=true]; $conv["
				+ ImpExConstants.Syntax.Modifier.VIRTUAL + "=true] ; name[lang=" + german.getIsoCode() + "] \n"
				+ "; unit1 ; name unit 1 \n" + "; unit2 ; name unit 2 \n" + "; unit3 ; name unit 3 \n");

		final Unit unit1;
		final Unit unit2;
		final Unit unit3;
		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(german);
		try
		{
			unit1 = (Unit) importReader.readLine();
			assertNotNull(unit1);
			assertEquals("xxx", unit1.getUnitType());
			assertEquals(2d, unit1.getConversionFactor(), 0);

			assertEquals("unit1", unit1.getCode());
			assertEquals("name unit 1", unit1.getName(ctx));

			final HeaderDescriptor headerDescriptor = importReader.getCurrentHeader();
			assertEquals(4, headerDescriptor.getColumns().size());
			final Iterator iter = headerDescriptor.getColumns().iterator();

			final StandardColumnDescriptor cdUnit = (StandardColumnDescriptor) iter.next();
			assertTrue(cdUnit.isVirtual());
			assertEquals(-1, cdUnit.getValuePosition());
			assertEquals("xxx", cdUnit.getDefaultValue());
			assertTrue(iter.hasNext());

			final StandardColumnDescriptor cdCode = (StandardColumnDescriptor) iter.next();
			assertFalse(cdCode.isVirtual());
			assertEquals(1, cdCode.getValuePosition());
			assertNull(cdCode.getDefaultValue());
			assertTrue(iter.hasNext());

			final StandardColumnDescriptor cdConv = (StandardColumnDescriptor) iter.next();
			assertTrue(cdConv.isVirtual());
			assertEquals(-3, cdConv.getValuePosition());
			assertEquals(new Double(2d), cdConv.getDefaultValue());
			assertTrue(iter.hasNext());

			final StandardColumnDescriptor cdName = (StandardColumnDescriptor) iter.next();
			assertFalse(cdName.isVirtual());
			assertEquals(2, cdName.getValuePosition());
			assertNull(cdName.getDefaultValue());
			assertEquals(german.getIsoCode(), cdName.getLanguageIso());
			assertFalse(iter.hasNext());

			unit2 = (Unit) importReader.readLine();
			assertNotNull(unit2);
			assertEquals("xxx", unit2.getUnitType());
			assertEquals(2d, unit2.getConversionFactor(), 0);
			assertEquals("unit2", unit2.getCode());
			assertEquals("name unit 2", unit2.getName(ctx));

			unit3 = (Unit) importReader.readLine();
			assertNotNull(unit3);
			assertEquals("xxx", unit3.getUnitType());
			assertEquals(2d, unit3.getConversionFactor(), 0);
			assertEquals("unit3", unit3.getCode());
			assertEquals("name unit 3", unit3.getName(ctx));
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the modifier <code>lang</code>.
	 */
	@Test
	public void testModifierLanguage()
	{
		final SessionContext deCtx = JaloSession.getCurrentSession().createSessionContext();
		deCtx.setLanguage(german);
		final SessionContext enCtx = JaloSession.getCurrentSession().createSessionContext();
		enCtx.setLanguage(english);

		final ImpExImportReader importReader = new ImpExImportReader("INSERT_UPDATE Unit;unitType[unique=true];"
				+ "code[unique=true];" + "name[lang=" + german.getIsoCode() + "];name[lang=" + english.getIsoCode() + "]\n"
				+ ";test1;test1;test_de;test_en\n" + "INSERT_UPDATE Unit;unitType[unique=true];code[unique=true];name[lang="
				+ german.getPK() + "];name[lang=" + english.getPK() + "]\n" + ";test2;test2;test_de;test_en");
		try
		{
			Unit unit = (Unit) importReader.readLine();
			assertEquals("test1", unit.getUnitType());
			assertEquals("test1", unit.getCode());
			assertEquals("test_de", unit.getName(deCtx));
			assertEquals("test_en", unit.getName(enCtx));

			unit = (Unit) importReader.readLine();
			assertEquals("test2", unit.getUnitType());
			assertEquals("test2", unit.getCode());
			assertEquals("test_de", unit.getName(deCtx));
			assertEquals("test_en", unit.getName(enCtx));
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}
}
