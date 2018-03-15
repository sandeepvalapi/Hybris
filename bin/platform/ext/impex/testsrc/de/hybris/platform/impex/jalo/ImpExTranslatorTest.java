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
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.PK.PKException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.impex.jalo.exp.ImpExCSVExportWriter;
import de.hybris.platform.impex.jalo.exp.ImpExExportWriter;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor.ColumnParams;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.AtomicValueTranslator;
import de.hybris.platform.impex.jalo.translators.CollectionValueTranslator;
import de.hybris.platform.impex.jalo.translators.ConvertPlaintextToEncodedUserPasswordTranslator;
import de.hybris.platform.impex.jalo.translators.ItemExpressionTranslator;
import de.hybris.platform.impex.jalo.translators.ItemPKTranslator;
import de.hybris.platform.impex.jalo.translators.MapValueTranslator;
import de.hybris.platform.impex.jalo.translators.UserPasswordTranslator;
import de.hybris.platform.impex.jalo.translators.VelocityTranslator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.security.CannotDecodePasswordException;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.persistence.security.SaltedMD5PasswordEncoder;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.migration.MigrationUtilities;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



/**
 * This test class is used for testing the different translators provided by ImpEx.
 */
@IntegrationTest
public class ImpExTranslatorTest extends AbstractImpExTest
{
	/** Default number format for double. */
	private NumberFormat DEF_DOUBLE_FORMAT;
	/** Default number format for integer. */
	private NumberFormat DEF_INT_FORMAT;
	/** Default number format for date. */
	private DateFormat DEF_DATE_FORMAT;
	private String originalSalt = null;

	/** Abbreviation for ",,". */
	private static final String DELIMITERS = String.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER)//NOPMD
			+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER; // :=",,"

	/**
	 * Atomic test data for import.<br>
	 * Format: {Class, import value, expected value}
	 */
	private Object[][] ATOMIC_TEST_DATA_IMPORT;

	/**
	 * Atomic test data for export.<br>
	 * Format: {Class, expected value, export value}
	 */
	private Object[][] ATOMIC_TEST_DATA_EXPORT;

	/**
	 * Collection test data for import.<br>
	 * Format: {Class, delimiter, import value, expected value}<br>
	 * CORE-3347, CORE-3348 --> CORE-3378
	 */
	private Object[][] COLLECTION_TEST_DATA_IMPORT;

	/**
	 * Collection test data for export.<br>
	 * Format: {Class, delimiter, expected value, export value}<br>
	 * CORE-3347, CORE-3348 --> CORE-3378
	 */
	private Object[][] COLLECTION_TEST_DATA_EXPORT;

	/**
	 * Test data for exporting a customer. Format: Address.STREET, Address.NUMBER, Address->Country.ISOCODE,
	 * Address->Title.CODE
	 */
	private static final String[][] CUSTOMER_TEST_DATA = new String[][]
	{
			{ "street", "number", "countryisocode", "title" },
			{ "street", "number", "countryisocode", null },
			{ "street", "number", null, null },
			{ "street", "", null, null },
			{ "street", null, null, null },
			{ "", "", null, null }, };

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
		//////////////////////////////////

		DEF_DOUBLE_FORMAT = new AtomicValueTranslator(null, Double.class).getDefaultNumberFormat();
		DEF_INT_FORMAT = new AtomicValueTranslator(null, Integer.class).getDefaultNumberFormat();
		DEF_DATE_FORMAT = new AtomicValueTranslator(null, null).getDefaultDateFormat();

		ATOMIC_TEST_DATA_IMPORT = new Object[][]
		{
				{ String.class, "tral la la", "tral la la" },
				{ String.class, "", null },
				{ String.class, null, null },
				{ String.class, ",, ,, ,,;:;", ",, ,, ,,;:;" },

				{ PK.class, "", null },
				{ PK.class, null, null },
				{ PK.class, "VnkIWoLF6fqB7Ubul9BBEd-1", PK.parse("446150616192395") },
				{ PK.class, "446150616192395", PK.parse("446150616192395") },

				{ Boolean.class, "true", Boolean.TRUE },
				{ Boolean.class, "TRuE", Boolean.TRUE },
				{ Boolean.class, "false", Boolean.FALSE },
				{ Boolean.class, "FalSE", Boolean.FALSE },
				{ Boolean.class, "", null },
				{ Boolean.class, null, null },

				{ Integer.class, null, null },
				{ Integer.class, "", null },
				{ Integer.class, DEF_INT_FORMAT.format(12345), Integer.valueOf(12345) },
				{ Integer.class, DEF_INT_FORMAT.format(-12345), Integer.valueOf(-12345) },
				{ Integer.class, DEF_INT_FORMAT.format(0), Integer.valueOf(0) },

				{ Double.class, null, null },
				{ Double.class, "", null },
				{ Double.class, DEF_DOUBLE_FORMAT.format(12.345), new Double(12.345) },
				{ Double.class, DEF_DOUBLE_FORMAT.format(-12345.000), new Double(-12345.0) },
				{ Double.class, DEF_DOUBLE_FORMAT.format(0.000), new Double(0.0) },
				{ Double.class, DEF_DOUBLE_FORMAT.format(444), new Double(444.0) },

				{ Date.class, null, null },
				{ Date.class, "", null },
				{ Date.class, DEF_DATE_FORMAT.format(new Date(1234567L * 1000L)), new Date(1234567L * 1000L) },

				{ StandardDateRange.class, null, null },
				{ StandardDateRange.class,
						ImpExConstants.Syntax.MODIFIER_SEPARATOR + DEF_DATE_FORMAT.format(new Date(1234567L * 1000L)),
						new StandardDateRange(new Date(0), new Date(1234567L * 1000L)) },
				{
						StandardDateRange.class,
						DEF_DATE_FORMAT.format(new Date(1234567L * 1000L)) + ImpExConstants.Syntax.MODIFIER_SEPARATOR
								+ DEF_DATE_FORMAT.format(new Date(1234568L * 1000L)),
						new StandardDateRange(new Date(1234567L * 1000L), new Date(1234568L * 1000L)) },

				{ BigDecimal.class, DEF_DOUBLE_FORMAT.format(new BigDecimal("1346134.6")), new BigDecimal("1346134.6") } };

		ATOMIC_TEST_DATA_EXPORT = new Object[][]
		{
				{ String.class, "tral la la", "tral la la" },
				{ String.class, "", null },
				{ String.class, ",, ,, ,,;:;", ",, ,, ,,;:;" },

				{ Boolean.class, "true", Boolean.TRUE },
				{ Boolean.class, "false", Boolean.FALSE },
				{ Boolean.class, "", null },

				{ Integer.class, "", null },
				{ Integer.class, DEF_INT_FORMAT.format(12345), Integer.valueOf(12345) },
				{ Integer.class, DEF_INT_FORMAT.format(-12345), Integer.valueOf(-12345) },
				{ Integer.class, DEF_INT_FORMAT.format(0), Integer.valueOf(0) },

				{ Double.class, "", null },
				{ Double.class, DEF_DOUBLE_FORMAT.format(12.345), new Double(12.345) },
				{ Double.class, DEF_DOUBLE_FORMAT.format(-12345.000), new Double(-12345.0) },
				{ Double.class, DEF_DOUBLE_FORMAT.format(0.0), new Double(0.0) },
				{ Double.class, DEF_DOUBLE_FORMAT.format(444.0), new Double(444.0) },

				{ Date.class, "", null },
				{ Date.class, DEF_DATE_FORMAT.format(new Date(1234567L * 1000L)), new Date(1234567L * 1000L) },

				{ BigDecimal.class, DEF_DOUBLE_FORMAT.format(new BigDecimal("1346134.6")), new BigDecimal("1346134.6") } };


		COLLECTION_TEST_DATA_IMPORT = new Object[][]
		{
				{
						String.class,
						Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER),
						"aaa" + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "bbb  "
								+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "ccc"
								+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + " ddd", Arrays.asList(new String[]
				{ "aaa", "bbb", "ccc", "ddd" }) },
				{ String.class, Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER), "",
						Collections.EMPTY_LIST },
				{ String.class, Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER), DELIMITERS,
						Collections.EMPTY_LIST },
				{
						String.class,
						Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER),
						ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "middle"
								+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER, Collections.singletonList("middle") },

				{
						Boolean.class,
						Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER),
						"true" + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "falSe"
								+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "false", Arrays.asList(new Object[]
				{ Boolean.TRUE, Boolean.FALSE, Boolean.FALSE }) },

				{ Integer.class, Character.valueOf(';'), // CORE-3378
						"123;;45678;91011;", Arrays.asList(new Object[]
				{ Integer.valueOf(123), Integer.valueOf(45678), Integer.valueOf(91011) }) },

				{ Double.class, Character.valueOf(';'), // CORE-3378
						";1,234 ; -567,89;  910,11 ;", Arrays.asList(new Object[]
				{ new Double(1.234), new Double(-567.89), new Double(910.11) }) },

				{
						Date.class, // CORE-4186
						Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER),
						DEF_DATE_FORMAT.format(new Date(1234567L * 1000L)) + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER
								+ DEF_DATE_FORMAT.format(new Date(33L * 1000L))
								+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER, Arrays.asList(new Object[]
				{ new Date(1234567L * 1000L), new Date(33L * 1000L) }) } };


		COLLECTION_TEST_DATA_EXPORT = new Object[][]
		{
				{
						String.class,
						Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER),
						"aaa" + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "bbb"
								+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "ccc"
								+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "ddd", Arrays.asList(new String[]
				{ "aaa", "bbb", "ccc", "ddd" }) },
				{ String.class, Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER), "",
						Collections.EMPTY_LIST },
				{ String.class, Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER), "middle",
						Collections.singletonList("middle") },
				{
						Boolean.class,
						Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER),
						"true" + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "false"
								+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "false", Arrays.asList(new Object[]
				{ Boolean.TRUE, Boolean.FALSE, Boolean.FALSE }) },
				{ Integer.class,
						Character.valueOf(';'), // CORE-3378
						DEF_INT_FORMAT.format(123) + ";" + DEF_INT_FORMAT.format(45678) + ";" + DEF_INT_FORMAT.format(91011),
						Arrays.asList(new Object[]
				{ Integer.valueOf(123), Integer.valueOf(45678), Integer.valueOf(91011) }) },
				{ Double.class,
						Character.valueOf(';'), // CORE-3378
						DEF_DOUBLE_FORMAT.format(1.234) + ";" + DEF_DOUBLE_FORMAT.format(-567.89) + ";"
								+ DEF_DOUBLE_FORMAT.format(910.11), Arrays.asList(new Object[]
				{ new Double(1.234), new Double(-567.89), new Double(910.11) }) },
				{
						Date.class, // CORE-4186
						Character.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER),
						DEF_DATE_FORMAT.format(new Date(1234567L * 1000L)) + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER
								+ DEF_DATE_FORMAT.format(new Date(33L * 1000L)), Arrays.asList(new Object[]
				{ new Date(1234567L * 1000L), new Date(33L * 1000L) }) } };

	}

	@After
	public void tearDown()
	{
		//	 TODO after the relase 3.0 was published, we have to move this code to JaloTest !!!

		if (originalSalt != null)
		{
			final Tenant tenant = Registry.getCurrentTenant();
			if (tenant instanceof MasterTenant)
			{
				((MasterTenant) tenant).getConfig().setParameter("password.md5.salt", originalSalt);
			}
		}
		////////////////////////////////////////////
	}

	/**
	 * Test <code>importValue</code> method of {@link AtomicValueTranslator}.
	 */
	@Test
	public void testAtomicValueTranslatorImport()
	{
		// atomic translators
		for (int i = 0; i < ATOMIC_TEST_DATA_IMPORT.length; i++)
		{
			final Object[] testData = ATOMIC_TEST_DATA_IMPORT[i];
			final AtomicValueTranslator avt = new AtomicValueTranslator(null, (Class) testData[0]);
			Object object = null;
			try
			{
				object = avt.importValue((String) testData[1], null);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
			assertEquals(Arrays.asList(testData).toString(), testData[2], object);
		}
	}

	/**
	 * Test <code>exportValue</code> method of {@link AtomicValueTranslator}.
	 */
	@Test
	public void testAtomicValueTranslatorExport()
	{
		// atomic translators
		Object exportedValue = null;
		Object expectedValue = null;
		for (int i = 0; i < ATOMIC_TEST_DATA_EXPORT.length; i++)
		{
			final Object[] testData = ATOMIC_TEST_DATA_EXPORT[i];
			final AtomicValueTranslator avt = new AtomicValueTranslator(null, (Class) testData[0]);
			try
			{
				exportedValue = avt.exportValue(testData[2]);
				expectedValue = testData[1];
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
			assertEquals("worked on:" + Arrays.asList(testData).toString(), expectedValue, exportedValue);
		}
	}

	/**
	 * Test <code>importValue</code> method of {@link CollectionValueTranslator}.
	 */
	@Test
	public void testCollectionValueTranslatorImport()
	{
		CollectionType collT = null;
		try
		{
			collT = TypeManager.getInstance().createCollectionType("CollTransCollType",
					TypeManager.getInstance().getType("java.lang.Object"));
		}
		catch (final JaloDuplicateCodeException e)
		{
			fail(e.getMessage());
		}
		for (int i = 0; i < COLLECTION_TEST_DATA_IMPORT.length; i++)
		{
			final Object[] testData = COLLECTION_TEST_DATA_IMPORT[i];
			final AtomicValueTranslator avt = new AtomicValueTranslator(null, (Class) testData[0]);
			final CollectionValueTranslator cvt = new CollectionValueTranslator(collT, avt, ((Character) testData[1]).charValue());
			final Object object = cvt.importValue((String) testData[2], null);
			assertEquals(Arrays.asList(testData).toString(), testData[3], object);

		}

		// check if 'set' property will be preserved. see PLA-5005
		try
		{
			// create set type for TestItem
			final ComposedType testItemType = TypeManager.getInstance().getComposedType(TestItem.class);
			collT = TypeManager.getInstance().createCollectionType("CollTransSetType",
					TypeManager.getInstance().getType("java.lang.Object"), CollectionType.SET);
			final AttributeDescriptor attributeDescriptor = testItemType.createAttributeDescriptor("set", collT, AttributeDescriptor.READ_FLAG
					+ AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG + AttributeDescriptor.REMOVE_FLAG);
			assertNotNull(attributeDescriptor);
			// instantiate TestItem
			final Item myItem = testItemType.newInstance(Collections.EMPTY_MAP);
			// create translator
			final AtomicValueTranslator avt = new AtomicValueTranslator(null, String.class);
			final CollectionValueTranslator cvt = new CollectionValueTranslator(collT, avt, ',');
			// initialize translator (use header to get valid descriptor)
			final HeaderDescriptor header = ImpExReader.parseHeader("INSERT TestItem;set");
			final AbstractColumnDescriptor cdSet = header.getColumnsByQualifier("set").iterator().next();
			cvt.init((StandardColumnDescriptor) cdSet);
			// import collection with two equal elements in normal mode
			Object object = cvt.importValue("hello,hello", myItem);
			assertEquals("[hello]", object.toString());
			myItem.setAttribute("set", object);
			// import collection with one element equal to contained element in append mode
			object = cvt.importValue(ImpExConstants.Syntax.COLLECTION_APPEND_PREFIX + "hello", myItem);
			assertEquals("[hello]", object.toString());
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Test <code>exportValue</code> method of {@link CollectionValueTranslator}.
	 */
	@Test
	public void testCollectionValueTranslatorExport()
	{
		CollectionType collT = null;
		Object exportedValue = null;
		Object expectedValue = null;
		try
		{
			collT = TypeManager.getInstance().createCollectionType("CollTransCollType",
					TypeManager.getInstance().getType("java.lang.Object"));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		for (int i = 0; i < COLLECTION_TEST_DATA_EXPORT.length; i++)
		{
			final Object[] testData = COLLECTION_TEST_DATA_EXPORT[i];
			final AtomicValueTranslator avt = new AtomicValueTranslator(null, (Class) testData[0]);
			final CollectionValueTranslator cvt = new CollectionValueTranslator(collT, avt, ((Character) testData[1]).charValue());
			try
			{
				exportedValue = cvt.exportValue(testData[3]);
				expectedValue = testData[2];
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
			assertEquals("worked on:" + Arrays.asList(testData).toString(), expectedValue, exportedValue);
		}
	}

	/**
	 * Test <code>importValue</code> method of {@link ItemPKTranslator}.
	 */
	@Test
	public void testItemPKTranslatorImport()
	{
		final ItemPKTranslator pkTrans = new ItemPKTranslator(TypeManager.getInstance().getComposedType(Language.class));

		assertEquals(null, pkTrans.importValue("", null));
		assertFalse(pkTrans.wasUnresolved());
		assertEquals(german, pkTrans.importValue(german.getPK().toString(), null));
		assertFalse(pkTrans.wasUnresolved());
		assertEquals(english, pkTrans.importValue(english.getPK().toString(), null));
		assertFalse(pkTrans.wasUnresolved());

		// test missing item
		assertEquals(null, pkTrans.importValue(PK.createFixedUUIDPK(Constants.TC.Country, 312323).toString(), null));
		assertTrue(pkTrans.wasUnresolved());

		// wrong format
		try
		{
			pkTrans.importValue("blub blubb no type code, juhu", null);
			fail("expected IllegalArgumentException");
		}
		catch (final PKException e)
		{
			// fine
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e.getMessage());
		}

		// test wrong type
		try
		{
			pkTrans.importValue(UserManager.getInstance().getAnonymousCustomer().getPK().toString(), null);
			fail("expected JaloInvalidParameterException");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail("expected JaloInvalidParameterException but got " + e.getClass().getName());
		}

		// test pks in collection
		CollectionType collT = null;
		try
		{
			collT = TypeManager.getInstance().createCollectionType("PKCollTransCollType",
					TypeManager.getInstance().getComposedType(Item.class));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		final String delimiters = String.valueOf(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER)//NOPMD
				+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER; // := '||'
		final CollectionValueTranslator cvt = new CollectionValueTranslator(collT, pkTrans);
		assertEquals(Collections.singletonList(german),
				cvt.importValue(delimiters + german.getPK().toString() + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER, null));
		assertEquals(
				Arrays.asList(new Object[]
				{ german, english }),
				cvt.importValue(delimiters + german.getPK().toString() + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER
						+ english.getPK().toString(), null));
		assertEquals(
				Arrays.asList(new Object[]
				{ english, german }),
				cvt.importValue(delimiters + english.getPK().toString() + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER
						+ german.getPK().toString(), null));

		// test oldPK migration
		final ItemPKTranslator productPKTrans = new ItemPKTranslator(TypeManager.getInstance().getComposedType(Product.class));
		final String testProductOldPK = "VnkIWoLF6fqB7Ubul9BBEd-1";
		final PK pk = MigrationUtilities.convertOldPK(testProductOldPK);
		final Product testProduct = ProductManager.getInstance().createProduct(jaloSession.getSessionContext(), pk, "test");
		assertEquals(testProduct, productPKTrans.importValue(testProductOldPK, null));
		assertFalse(productPKTrans.wasUnresolved());

		final CollectionValueTranslator productCvt = new CollectionValueTranslator(collT, productPKTrans);
		assertEquals(Collections.singletonList(testProduct), productCvt.importValue(delimiters + testProductOldPK
				+ ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER, null));
	}

	/**
	 * Test <code>exportValue</code> method of {@link ItemPKTranslator}.
	 */
	@Test
	public void testItemPkTranslatorExport()
	{
		final ItemPKTranslator pkTrans = new ItemPKTranslator(TypeManager.getInstance().getComposedType(Language.class));
		final Language deTest = getOrCreateLanguage("deTest");
		final Language enTest = getOrCreateLanguage("enTest");

		final String dePk = deTest.getPK().toString();
		final String enPk = enTest.getPK().toString();

		//Test Single PKs
		assertEquals("working on: DE", dePk, pkTrans.exportValue(deTest));
		assertEquals("working on: EN", enPk, pkTrans.exportValue(enTest));

		//Testing null

		//ItemPkTranslator combined with a CollectionValueTranslator
		CollectionType collT = null;
		try
		{
			collT = TypeManager.getInstance().createCollectionType("PKCollTransCollType",
					TypeManager.getInstance().getComposedType(Item.class));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		final CollectionValueTranslator cvt = new CollectionValueTranslator(collT, pkTrans);
		final Collection collection1 = Arrays.asList(new Object[]
		{ deTest, enTest });
		assertEquals("working on:" + collection1.toString(), dePk + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + enPk,
				cvt.exportValue(collection1));
	}

	/**
	 * Test <code>performExport</code> method of {@link VelocityTranslator}.
	 */
	@Test
	public void testVelocityTranslatorExport()
	{
		final VelocityTranslator velocityTranslator = new VelocityTranslator("test1", "$item." + Item.OWNER + ".getName()\n"
				+ "$item.getAttribute(\"" + Address.STREETNAME + "\") " + "$item.getAttribute(\"" + Address.STREETNUMBER + "\")\n"
				+ "$item.getAttribute(\"" + Address.POSTALCODE + "\") " + "$item.getAttribute(\"" + Address.TOWN + "\")\n"
				+ "## a simple comment\n" + "#set($para=\"" + Address.COMPANY + "\")\n" + "Company=" + "$item.getAttribute( $para )");
		Customer cust = null;
		Address adr;
		try
		{
			cust = UserManager.getInstance().createCustomer("xyz");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		cust.setName("Thomas Hertz");
		adr = cust.createAddress();
		try
		{
			adr.setAttribute(Address.STREETNAME, "Schwere-Reiter-Str.");
			adr.setAttribute(Address.STREETNUMBER, "35");
			adr.setAttribute(Address.POSTALCODE, "80797");
			adr.setAttribute(Address.TOWN, "Muenchen");
			adr.setAttribute(Address.COMPANY, "hybris GmbH");
		}
		catch (final JaloBusinessException e)
		{
			fail(e.getMessage());
		}

		assertEquals(cust, adr.getOwner());

		try
		{
			assertEquals("Thomas Hertz\n" + "Schwere-Reiter-Str. 35\n" + "80797 Muenchen\n" + "Company=hybris GmbH",
					velocityTranslator.performExport(adr));
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}


	}

	@Test
	public void shouldNotAllowJavaReflectionAPICalls() throws ImpExException
	{
		final VelocityTranslator velocityTranslator = new VelocityTranslator("test1",
				"#set( $str = \"\" )\n#set( $systemClass = ${str.getClass().forName( \"java.lang.System\" )} )\n${systemClass.exit(1)}\n$item."
						+ Item.OWNER + ".getName()\n" + "$item.getAttribute(\"" + Address.STREETNAME + "\") " + "$item.getAttribute(\""
						+ Address.STREETNUMBER + "\")\n" + "$item.getAttribute(\"" + Address.POSTALCODE + "\") "
						+ "$item.getAttribute(\"" + Address.TOWN + "\")\n" + "## a simple comment\n" + "#set($para=\"" + Address.COMPANY
						+ "\")\n" + "Company=" + "$item.getAttribute( $para )");
		Customer cust = null;
		Address adr;
		try
		{
			cust = UserManager.getInstance().createCustomer("xyz");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		cust.setName("Thomas Hertz");
		adr = cust.createAddress();
		try
		{
			adr.setAttribute(Address.STREETNAME, "Schwere-Reiter-Str.");
			adr.setAttribute(Address.STREETNUMBER, "35");
			adr.setAttribute(Address.POSTALCODE, "80797");
			adr.setAttribute(Address.TOWN, "Muenchen");
			adr.setAttribute(Address.COMPANY, "hybris GmbH");
		}
		catch (final JaloBusinessException e)
		{
			fail(e.getMessage());
		}

		assertEquals(cust, adr.getOwner());

		try
		{
			assertEquals("${systemClass.exit(1)}\n" + "Thomas Hertz\n" + "Schwere-Reiter-Str. 35\n" + "80797 Muenchen\n"
					+ "Company=hybris GmbH", velocityTranslator.performExport(adr));
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test <code>importValue</code> method of {@link ItemExpressionTranslator}.
	 */
	@Test
	public void testItemExpressionTranslatorImport()
	{
		final String pattern = Customer.DEFAULT_PAYMENT_ADDRESS + "(" + Address.STREETNAME + "," + Address.STREETNUMBER + ","
				+ Address.COUNTRY + "(" + Country.ISOCODE + ")," + Address.TITLE + "(" + Title.CODE + ")" + ")";
		List<ColumnParams> patternList = null;
		try
		{
			final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
			assertNotNull(lists);
			assertEquals(1, lists.length);
			patternList = lists[0];
		}
		catch (final HeaderValidationException e)
		{
			fail(e.getMessage());
		}
		// defaultpaymentaddr()
		assertEquals(1, patternList.size());
		final ColumnParams element = patternList.get(0);
		assertEquals(Customer.DEFAULT_PAYMENT_ADDRESS, element.getQualifier());
		assertEquals(Collections.EMPTY_MAP, element.getModifiers());
		// streetname,streetnumber,country(),title()
		final List<ColumnParams>[] subLists = element.getItemPatternLists();
		assertNotNull(subLists);
		assertEquals(1, subLists.length);
		assertEquals(4, subLists[0].size());

		assertNull(subLists[0].get(0).getItemPatternLists());
		assertEquals(Address.STREETNAME, subLists[0].get(0).getQualifier());

		assertNull(subLists[0].get(1).getItemPatternLists());
		assertEquals(Address.STREETNUMBER, subLists[0].get(1).getQualifier());

		assertNotNull(subLists[0].get(2).getItemPatternLists());
		assertEquals(1, subLists[0].get(2).getItemPatternLists().length);
		assertEquals(Address.COUNTRY, subLists[0].get(2).getQualifier());
		assertNull(subLists[0].get(2).getItemPatternLists()[0].get(0).getItemPatternLists());
		assertEquals(Country.ISOCODE, subLists[0].get(2).getItemPatternLists()[0].get(0).getQualifier());

		assertNotNull(subLists[0].get(3).getItemPatternLists());
		assertEquals(1, subLists[0].get(3).getItemPatternLists().length);
		assertEquals(Address.TITLE, subLists[0].get(3).getQualifier());
		assertNull(subLists[0].get(3).getItemPatternLists()[0].get(0).getItemPatternLists());
		assertEquals(Title.CODE, subLists[0].get(3).getItemPatternLists()[0].get(0).getQualifier());

		try
		{
			final ItemExpressionTranslator itemExpressionTranslator = new ItemExpressionTranslator(TypeManager.getInstance().getComposedType(
					Customer.class), patternList);

			assertEquals(null, itemExpressionTranslator.importValue(null, null));
			assertFalse(itemExpressionTranslator.wasUnresolved());
			assertEquals(null, itemExpressionTranslator.importValue("", null));
			assertFalse(itemExpressionTranslator.wasUnresolved());
			// assertEquals(null, xt.importValue(":::",null));
			// assertFalse(xt.wasUnresolved());

			assertEquals(null, itemExpressionTranslator.importValue("Borngraben:59:XTC:BLUBB", null));
			assertTrue(itemExpressionTranslator.wasUnresolved());

			final Customer cust = UserManager.getInstance().createCustomer("ExprTestCust");
			final Address adr = UserManager.getInstance().createAddress(cust);
			cust.setDefaultPaymentAddress(adr);
			assertEquals(adr, cust.getDefaultPaymentAddress());
			adr.setAttribute(Address.STREETNAME, "Borngraben");
			adr.setAttribute(Address.STREETNUMBER, "59");
			final Country country = C2LManager.getInstance().createCountry("XTC");
			adr.setCountry(country);
			final Title title = UserManager.getInstance().createTitle("BLUBB");
			adr.setTitle(title);

			final Address notToFind = UserManager.getInstance().createAddress(cust);
			assertNotNull(notToFind);
			final Country notToFind2 = C2LManager.getInstance().createCountry("xyz");
			assertNotNull(notToFind2);
			final Title notToFind3 = UserManager.getInstance().createTitle("jiha");
			assertNotNull(notToFind3);

			assertEquals(cust, itemExpressionTranslator.importValue("Borngraben:59:XTC:BLUBB", null));
			assertFalse(itemExpressionTranslator.wasUnresolved());
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		catch (final HeaderValidationException e)
		{
			fail(e.getMessage());
		}
		catch (final JaloBusinessException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Test <code>exportValue</code> method of {@link ItemExpressionTranslator}.
	 */
	@Test
	public void testItemExpressionTranslatorExport()
	{
		//defaultPaymentAdress(streetname,streetnumber,country(isocode),title(code)
		final String pattern = Customer.DEFAULT_PAYMENT_ADDRESS + "(" + Address.STREETNAME + "," + Address.STREETNUMBER + ","
				+ Address.COUNTRY + "(" + Country.ISOCODE + ")," + Address.TITLE + "(" + Title.CODE + ")" + ")";

		//extractItemPathElements() already tested in ImpExText
		List<ColumnParams> patternList = null;
		try
		{
			final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
			assertNotNull(lists);
			assertEquals(1, lists.length);
			patternList = lists[0];
		}
		catch (final HeaderValidationException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		//create a customer
		Customer customer = null;
		Address address = null;
		Country country = null;
		Title title = null;
		try
		{
			customer = (Customer) TypeManager.getInstance().getComposedType(Customer.class)
					.newInstance(Collections.singletonMap(User.UID, "USERID"));
			address = (Address) TypeManager.getInstance().getComposedType(Address.class)
					.newInstance(Collections.singletonMap(Address.OWNER, customer));
			country = C2LManager.getInstance().createCountry(CUSTOMER_TEST_DATA[0][2]);
			title = (Title) TypeManager.getInstance().getComposedType(Title.class)
					.newInstance(Collections.singletonMap(Title.CODE, CUSTOMER_TEST_DATA[0][3]));
			address.setAttribute(Address.COUNTRY, country);
			address.setAttribute(Address.TITLE, title);
			customer.setAttribute(Customer.DEFAULT_PAYMENT_ADDRESS, address);
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}

		ItemExpressionTranslator itemExpressionTranslator = null;
		try
		{
			itemExpressionTranslator = new ItemExpressionTranslator(TypeManager.getInstance().getComposedType(Customer.class), patternList);
		}
		catch (final HeaderValidationException e1)
		{
			e1.printStackTrace();
			fail(e1.getMessage());
		}
		catch (final JaloItemNotFoundException e1)
		{
			e1.printStackTrace();
			fail(e1.getMessage());
		}

		for (int i = 0; i < CUSTOMER_TEST_DATA.length; i++)
		{
			final String[] values = CUSTOMER_TEST_DATA[i];
			try
			{
				address.setAttribute(Address.STREETNAME, values[0]);
				address.setAttribute(Address.STREETNUMBER, values[1]);
				address.setAttribute(Address.COUNTRY, (values[2] != null) ? country : null);
				address.setAttribute(Address.TITLE, (values[3] != null) ? title : null);
			}
			catch (final Exception e)
			{
				fail(e.getMessage());
			}
			final String expected = values[0] + ":" + ((values[1] != null) ? values[1] : "") + ":"
					+ ((values[2] != null) ? values[2] : "") + ":" + ((values[3] != null) ? values[3] : "");
			final String exported = itemExpressionTranslator.exportValue(customer);
			assertEquals("working on:" + Arrays.asList(values), expected, exported);
		}
	}

	@Test
	public void testConvertPlaintextToEncodedUserPasswordTranslator()
	{
		final String line = "INSERT_UPDATE Employee;" + Employee.UID + "[unique=true];"
				+ ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + "password[" + ImpExConstants.Syntax.Modifier.TRANSLATOR + "="
				+ ConvertPlaintextToEncodedUserPasswordTranslator.class.getName() + "];" + Employee.NAME + "\n"
				+ "; foo ; md5:foobar ; Foo";

		PasswordEncoder enc = null;
		try
		{
			enc = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder("md5");
		}
		catch (final PasswordEncoderNotFoundException e)
		{
			fail("cannot get MD5 password encoder (though it is mapped) : " + e);
		}

		final ImpExImportReader impExImportReader = new ImpExImportReader(line);

		Employee foo = null;

		try
		{
			foo = (Employee) impExImportReader.readLine();
			assertEquals("foo", foo.getLogin());
			assertEquals("foo", foo.getUID());
			assertEquals("Foo", foo.getName());

			if (enc instanceof SaltedMD5PasswordEncoder)
			{
				assertEquals("68ca291d3667b184fe5df7e35e2a3636", foo.getEncodedPassword());
			}
			else
			{
				fail("Unsupported MD5 encoder configured");
			}
			try
			{
				foo.getPassword();
				fail("CannotDecodePasswordException expected");
			}
			catch (final CannotDecodePasswordException e)
			{
				// fine
			}
			assertEquals("md5", foo.getPasswordEncoding());
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests an import using the {@link UserPasswordTranslator}.
	 */
	@Test
	public void testUserPasswordTranslatorImport()
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

		String lines = null;

		if (enc instanceof SaltedMD5PasswordEncoder)
		{
			lines = "INSERT_UPDATE Employee;" + Employee.UID + "[unique=true];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX
					+ "password[" + ImpExConstants.Syntax.Modifier.TRANSLATOR + "=" + UserPasswordTranslator.class.getName() + "];"
					+ Employee.NAME + "\n" + "# empty line \n" + "; axel ; md5:570bfc8e78236027d68be7b5bbd9c9fb ; Axel \n"
					+ "; axel2 ; *:axel2 ; noch einer\n";
		}
		else
		{
			lines = "INSERT_UPDATE Employee;" + Employee.UID + "[unique=true];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX
					+ "password[" + ImpExConstants.Syntax.Modifier.TRANSLATOR + "=" + UserPasswordTranslator.class.getName() + "];"
					+ Employee.NAME + "\n" + "# empty line \n" + "; axel ; md5:a7c15c415c37626de8fa648127ba1ae5 ; Axel \n"
					+ "; axel2 ; *:axel2 ; noch einer\n";
		}

		final ImpExImportReader impExImportReader = new ImpExImportReader(lines);

		Employee axel = null, axel2 = null;

		try
		{
			axel = (Employee) impExImportReader.readLine();
			assertEquals("axel", axel.getLogin());
			assertEquals("axel", axel.getUID());
			assertEquals("Axel", axel.getName());

			if (enc instanceof SaltedMD5PasswordEncoder)
			{
				assertEquals("570bfc8e78236027d68be7b5bbd9c9fb", axel.getEncodedPassword());
			}
			else
			{
				assertEquals("a7c15c415c37626de8fa648127ba1ae5", axel.getEncodedPassword());
			}
			try
			{
				axel.getPassword();
				fail("CannotDecodePasswordException expected");
			}
			catch (final CannotDecodePasswordException e)
			{
				// fine
			}
			assertEquals("md5", axel.getPasswordEncoding());

			assertTrue(axel.checkPassword("axel"));

			axel2 = (Employee) impExImportReader.readLine();

			assertEquals("axel2", axel2.getLogin());
			assertEquals("axel2", axel2.getUID());
			assertEquals("noch einer", axel2.getName());
			assertEquals("axel2", axel2.getEncodedPassword());
			assertEquals("axel2", axel2.getPassword());

			assertEquals(SystemEJB.DEFAULT_ENCODING, axel2.getPasswordEncoding());

			assertNull(impExImportReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests an export using the {@link UserPasswordTranslator}. Creates two sample customers, exports them and compares
	 * the resulting text with the created customers.
	 */
	@Test
	public void testUserPasswordTranslatorExport()
	{
		// Create first sample user
		Employee axel = null, axel2 = null, empty = null;
		try
		{
			axel = UserManager.getInstance().createEmployee("axel");
			axel.setName("Axel");
			axel.setPassword("axel", "md5");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}

		assertEquals("axel", axel.getLogin());
		assertEquals("axel", axel.getUID());
		assertEquals("Axel", axel.getName());

		PasswordEncoder enc = null;
		try
		{
			enc = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder("md5");
		}
		catch (final PasswordEncoderNotFoundException e)
		{
			fail("cannot get MD5 password encoder (though it is mapped) : " + e);
		}

		if (enc instanceof SaltedMD5PasswordEncoder)
		{
			assertEquals("570bfc8e78236027d68be7b5bbd9c9fb", axel.getEncodedPassword());
		}
		else
		{
			assertEquals("a7c15c415c37626de8fa648127ba1ae5", axel.getEncodedPassword());
		}

		try
		{
			axel.getPassword();
			fail("CannotDecodePasswordException expected");
		}
		catch (final CannotDecodePasswordException e)
		{
			// fine
		}
		assertEquals("md5", axel.getPasswordEncoding());
		assertTrue(axel.checkPassword("axel"));

		//create second sample user
		try
		{
			axel2 = UserManager.getInstance().createEmployee("axel2");
			axel2.setName("noch einer");
			axel2.setPassword("axel2");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}

		assertEquals("axel2", axel2.getLogin());
		assertEquals("axel2", axel2.getUID());
		assertEquals("noch einer", axel2.getName());
		assertEquals(Config.getString("default.password.encoding", SystemEJB.DEFAULT_ENCODING), axel2.getPasswordEncoding());

		//create second sample user
		try
		{
			empty = UserManager.getInstance().createEmployee("empty");
			empty.setName("empty");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}
		assertEquals("empty", empty.getLogin());
		assertEquals("empty", empty.getUID());
		assertEquals("empty", empty.getName());
		assertNull(empty.getEncodedPassword());
		assertNull(empty.getPassword());
		assertEquals(SystemEJB.DEFAULT_ENCODING, empty.getPasswordEncoding());


		// export sample customers
		final String header = "INSERT_UPDATE Employee;" + Employee.UID + "[unique=true];"
				+ ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + "password[" + ImpExConstants.Syntax.Modifier.TRANSLATOR + "="
				+ UserPasswordTranslator.class.getName() + "];" + Employee.NAME;

		final StringWriter str = new StringWriter();
		final ImpExExportWriter impExExportWriter = new ImpExCSVExportWriter(new CSVWriter(str));

		try
		{
			impExExportWriter.setCurrentHeader(header, ImpExManager.getImportStrictMode());
			impExExportWriter.writeCurrentHeader(false);
			impExExportWriter.writeLine(axel);
			impExExportWriter.writeLine(axel2);
			impExExportWriter.writeLine(empty);

		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally
		{
			impExExportWriter.close();
		}

		/*
		 * Note: After adding default value == SystemEJB.DEFAULT_ENCODING to passwordEncoding attribute of User type, The
		 * entry for "empty" user (no password) looks like that: "*:". See DEL-320
		 */
		// compare result with customers
		final String expected = header + CSVConstants.DEFAULT_LINE_SEPARATOR + ";" + axel.getUID() + ";"
				+ axel.getPasswordEncoding() + ":" + axel.getEncodedPassword() + ";" + axel.getName()
				+ CSVConstants.DEFAULT_LINE_SEPARATOR + ";" + axel2.getUID() + ";" + axel2.getPasswordEncoding() + ":"
				+ axel2.getEncodedPassword() + ";" + axel2.getName() + CSVConstants.DEFAULT_LINE_SEPARATOR + ";empty;"
				+ empty.getPasswordEncoding() + ":" + ";" + empty.getName() + CSVConstants.DEFAULT_LINE_SEPARATOR;
		final String actual = str.getBuffer().toString();
		assertEquals(expected, actual);
	}

	/**
	 * Test <code>importValue</code> method of {@link MapValueTranslator}.
	 */
	@Test
	public void testMapValueTranslatorImport() throws JaloDuplicateCodeException, ImpExException
	{
		final String myKey1 = "myKey1";
		final String myValue1 = "myValue1";
		final String myKey2 = "myKey2";
		final String myValue2 = "myValue2";

		MapType mapType = null;

		final Map test = new HashMap();
		test.put(myKey1, myValue1);
		test.put(myKey2, myValue2);

		mapType = TypeManager.getInstance().createMapType("myMapType", TypeManager.getInstance().getRootAtomicType(String.class),
				TypeManager.getInstance().getRootAtomicType(String.class));

		AbstractValueTranslator keyTranslator = null;
		AbstractValueTranslator valueTranslator = null;
		MapValueTranslator translator = null;

		keyTranslator = MapValueTranslator.getTranslator(mapType.getArgumentType(null));
		valueTranslator = MapValueTranslator.getTranslator(mapType.getReturnType(null));
		translator = new MapValueTranslator(keyTranslator, valueTranslator, '|', ">>");

		final Object back = translator.importValue(myKey1 + ">>" + myValue1 + "|" + myKey2 + ">>" + myValue2 + "|", null);
		assertEquals(test, back);
	}

	/**
	 * Test <code>exportValue</code> method of {@link MapValueTranslator}.
	 */
	@Test
	public void testMapValueTranslatorExport()
	{
		final String myKey1 = "myKey1";
		final String myValue1 = "myValue1";
		final String myKey2 = "myKey2";
		final String myValue2 = "myValue2";

		MapType mapType = null;

		final Map test = new LinkedHashMap();
		test.put(myKey1, myValue1);
		test.put(myKey2, myValue2);

		try
		{
			mapType = TypeManager.getInstance().createMapType("myMapType", TypeManager.getInstance().getRootAtomicType(String.class),
					TypeManager.getInstance().getRootAtomicType(String.class));
		}
		catch (final JaloDuplicateCodeException e)
		{
			fail(e.getMessage());
		}

		AbstractValueTranslator keyTranslator = null;
		AbstractValueTranslator valueTranslator = null;
		MapValueTranslator translator = null;
		try
		{
			keyTranslator = MapValueTranslator.getTranslator(mapType.getArgumentType(null));
			valueTranslator = MapValueTranslator.getTranslator(mapType.getReturnType(null));
			translator = new MapValueTranslator(keyTranslator, valueTranslator, '|', ">>");
		}
		catch (final HeaderValidationException e)
		{
			fail(e.getMessage());
		}
		final String back = translator.exportValue(test);
		assertEquals(myKey1 + ">>" + myValue1 + "|" + myKey2 + ">>" + myValue2, back);
	}

	/**
	 * Test <code>importValue</code> method of {@link ItemExpressionTranslator} with nested dateformat stuff.
	 */
	@Test
	public void testNestedDateFormatImport()
	{
		final DateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
		final String pattern = Media.MEDIACONTAINER + "(" + MediaContainer.CREATION_TIME + "["
				+ ImpExConstants.Syntax.Modifier.DATEFORMAT + "=HH:mm:ss dd.MM.yyyy]" + ")";
		List<ColumnParams> patternList = null;
		try
		{
			final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
			assertNotNull(lists);
			assertEquals(1, lists.length);
			patternList = lists[0];
		}
		catch (final HeaderValidationException e)
		{
			fail(e.getMessage());
		}

		try
		{
			final Media media = MediaManager.getInstance().createMedia("test");
			final MediaContainer container = MediaManager.getInstance().createMediaContainer("test");
			media.setMediaContainer(container);

			final ItemExpressionTranslator itemExpressionTranslator = new ItemExpressionTranslator(TypeManager.getInstance().getComposedType(Media.class),
					patternList);
			itemExpressionTranslator.init(null);
			itemExpressionTranslator.importValue(format.format(container.getCreationTime()).replace(":", "\\:"), null);
		}
		catch (final HeaderValidationException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Test <code>exportValue</code> method of {@link ItemExpressionTranslator} with nested dateformat stuff.
	 */
	@Test
	public void testNestedDateFormatExport()
	{
		final DateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

		final Media media = MediaManager.getInstance().createMedia("test");
		final MediaContainer container = MediaManager.getInstance().createMediaContainer("test");
		media.setMediaContainer(container);

		final String pattern = Media.MEDIACONTAINER + "(" + MediaContainer.CREATION_TIME + "["
				+ ImpExConstants.Syntax.Modifier.DATEFORMAT + "=HH:mm:ss dd.MM.yyyy]" + ")";
		List<ColumnParams> patternList = null;
		try
		{
			final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
			assertNotNull(lists);
			assertEquals(1, lists.length);
			patternList = lists[0];
		}
		catch (final HeaderValidationException e)
		{
			fail(e.getMessage());
		}

		ItemExpressionTranslator itemExpressionTranslator = null;
		try
		{
			itemExpressionTranslator = new ItemExpressionTranslator(TypeManager.getInstance().getComposedType(Media.class), patternList);
			itemExpressionTranslator.init(null);
		}
		catch (final HeaderValidationException e1)
		{
			e1.printStackTrace();
			fail(e1.getMessage());
		}
		assertEquals(format.format(container.getCreationTime()).replace(":", "\\:"), itemExpressionTranslator.exportValue(media));
	}

	/**
	 * Test <code>importValue</code> method of {@link ItemExpressionTranslator} with nested pk translation PLA-6104.
	 */
	@Test
	public void testItemPKItemExpressionTranslatorImport()
	{
		final String pattern = ImpExImportCronJob.MODE + "(" + Item.TYPE + "," + EnumerationValue.CODE + ")";
		List<ColumnParams> patternList = null;
		try
		{
			final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
			assertNotNull(lists);
			assertEquals(1, lists.length);
			patternList = lists[0];
		}
		catch (final HeaderValidationException e)
		{
			fail(e.getMessage());
		}

		try
		{
			final ItemExpressionTranslator itemExpressionTranslator = new ItemExpressionTranslator(TypeManager.getInstance().getComposedType(
					ImpExImportCronJob.class), patternList);
			itemExpressionTranslator.init(null);
			itemExpressionTranslator.importValue(ImpExManager.getImportStrictMode().getComposedTypePK().getLongValue() + ":"
					+ ImpExManager.getImportStrictMode().getCode(), null);
		}
		catch (final HeaderValidationException e)
		{
			fail(e.getMessage());
		}
	}
}
