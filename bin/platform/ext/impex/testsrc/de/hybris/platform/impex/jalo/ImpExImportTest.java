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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.imp.DefaultImportProcessor;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.InsufficientDataException;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.JaloTypeException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.test.MyUnit;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.CoreImpExConstants;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


/**
 * This test class contains all tests considering the import process of the impex extension.
 */
@IntegrationTest
public class ImpExImportTest extends AbstractImpExTest
{
	private static final String MANDATORY_ATTR_QUALIFIER = "mandatoryAttr";
	private static final String MANDATORY_ATTR_VALUE = "important";

	/**
	 * Tests the document ID column descriptor while import.
	 */
	@Test
	public void testImportWithDocumentID() throws JaloBusinessException, IOException
	{
		final User rigge = UserManager.getInstance().createCustomer("rigge");
		rigge.setUID("rigge");

		Address address = UserManager.getInstance().createAddress(rigge);
		address.setAttribute("department", "r1");

		address = UserManager.getInstance().createAddress(rigge);
		address.setAttribute("department", "r2");


		final String script = "INSERT_UPDATE Customer; &customer; uid[unique=true]; defaultPaymentAddress( &payAddress ); defaultShipmentAddress( &delAddress ) ; owner( Customer.uid ) \n"
				+ "; customer0 ; andy ; payAddress0 ; delAddress2\n"
				+ "; customer1 ; rigge; payAddress1 ; delAddress3 ; customer0 \n"
				+ "INSERT Address; &payAddress; &delAddress ; owner( Customer.uid ) ; department\n"
				+ "; payAddress0 ; delAddress0 ; andy  ; a1\n"
				+ "; payAddress2 ; delAddress2 ; andy  ; a2\n"
				+ "UPDATE Address; &payAddress; &delAddress ; owner( owner ( &customer ) ) ; department[unique=true]\n"
				+ "; payAddress1 ; delAddress1 ;           ; r1\n" + "; payAddress3 ; delAddress3 ; customer0 ; r2\n";

		final StringWriter dump = new StringWriter();
		final StringWriter docIds = new StringWriter();
		DocumentIDRegistry docIDregistry = new DocumentIDRegistry(new CSVWriter(docIds));
		ImpExImportReader reader = new ImpExImportReader(new CSVReader(script), new CSVWriter(dump), docIDregistry,
				new DefaultImportProcessor(), ImpExManager.getImportStrictMode());

		reader.readLine();
		assertEquals(1, reader.getDumpedLineCount());
		for (int i = 0; i < 5; i++)
		{
			reader.readLine();
			assertEquals(2, reader.getDumpedLineCount());
		}

		reader.close();

		docIDregistry.closeStreams();
		assertTrue(docIds.getBuffer().length() > 0);

		final StringWriter dump2 = new StringWriter();
		final StringWriter docIds2 = new StringWriter();
		docIDregistry = new DocumentIDRegistry(new CSVReader(docIds.getBuffer().toString()), new CSVWriter(docIds2));
		reader = new ImpExImportReader(new CSVReader(dump.getBuffer().toString()), new CSVWriter(dump2), docIDregistry,
				new DefaultImportProcessor(), ImpExManager.getImportStrictMode());

		while (reader.readLine() != null)
		{
			assertEquals(0, reader.getDumpedLineCount());
		}

		reader.close();

		Customer cu1 = UserManager.getInstance().getCustomerByLogin("andy");
		assertNotNull(cu1);
		Address payAddress = cu1.getDefaultPaymentAddress();
		assertNotNull(payAddress);
		User cu2 = payAddress.getUser();
		assertEquals(cu1, cu2);
		Address delAddress = cu1.getDefaultDeliveryAddress();
		assertNotNull(delAddress);
		User cu3 = delAddress.getUser();
		assertEquals(cu1, cu3);

		cu1.setDefaultPaymentAddress(null);
		cu1.setDefaultDeliveryAddress(null);

		cu1 = UserManager.getInstance().getCustomerByLogin("rigge");
		assertNotNull(cu1);
		payAddress = cu1.getDefaultPaymentAddress();
		assertNotNull(payAddress);
		cu2 = payAddress.getUser();
		assertEquals(cu1, cu2);
		delAddress = cu1.getDefaultDeliveryAddress();
		assertEquals(cu1, delAddress.getOwner());
		assertNotNull(delAddress);
		cu3 = delAddress.getUser();
		assertEquals(cu1, cu3);

		cu1.setDefaultPaymentAddress(null);
		cu1.setDefaultDeliveryAddress(null);

		docIDregistry.closeStreams();
		assertTrue(docIds2.getBuffer().length() > 0);
	}

	/**
	 * Tests the import of user rights using semicolon as separator.
	 */
	@Test
	public void testUserRightsImportSemicolon() throws ImpExException, ConsistencyCheckException
	{
		testUserRightsImport(';');
	}

	/**
	 * Tests the import of user rights using comma as separator.
	 */
	@Test
	public void testUserRightsImportComma() throws ImpExException, ConsistencyCheckException
	{
		testUserRightsImport(',');
	}

	private void testUserRightsImport(final char sep) throws ImpExException, ConsistencyCheckException
	{
		final AccessManager accessManager = AccessManager.getInstance();
		final UserGroup dummygroup = UserManager.getInstance().createUserGroup("dummygroup");

		final String lines = ImpExConstants.Syntax.DEFINITION_PREFIX + ImpExConstants.Syntax.START_USERRIGHTS + "\n" + "Type" + sep
				+ "UID" + sep + "MemberOfGroups" + sep + "Password" + sep + "Target" + sep + "read" + sep + "change" + sep + "create"
				+ sep + "remove" + sep + "change_perm\n" + "UserGroup" + sep + "dummygroup" + sep + sep + sep + sep + sep + sep + sep
				+ sep + "\n" + sep + sep + sep + sep + "Product" + sep + "+" + sep + "+" + sep + "-" + sep + "+" + sep + "+\n" + sep
				+ sep + sep + sep + "Unit" + sep + "+" + sep + "+" + sep + "-" + sep + "+" + sep + "+\n" + sep + sep + sep + sep
				+ "Language" + sep + "+" + sep + "+" + sep + "-" + sep + "+" + sep + "+\n" + ImpExConstants.Syntax.DEFINITION_PREFIX
				+ ImpExConstants.Syntax.END_USERRIGHTS;

		final CSVReader csvReader = new CSVReader(lines);
		csvReader.setFieldSeparator(new char[]
		{ sep });
		final ImpExImportReader reader = new ImpExImportReader(csvReader);

		reader.readLine();

		final UserRight READ = accessManager.getUserRightByCode(AccessManager.READ);
		final UserRight CREATE = accessManager.getUserRightByCode(AccessManager.CREATE);
		final UserRight CHANGE = accessManager.getUserRightByCode(AccessManager.CHANGE);
		final UserRight REMOVE = accessManager.getUserRightByCode(AccessManager.REMOVE);
		final UserRight PERMISSIONS = accessManager.getUserRightByCode(AccessManager.CHANGE_PERMISSIONS);

		assertEquals(
				Integer.valueOf(1),
				FlexibleSearch
						.getInstance()
						.search("SELECT count({PK}) FROM {UserRight} WHERE {code}=?code",
								Collections.singletonMap("code", AccessManager.READ), Integer.class).getResult().get(0));
		assertEquals(
				Integer.valueOf(1),
				FlexibleSearch
						.getInstance()
						.search("SELECT count({PK}) FROM {UserRight} WHERE {code}=?code",
								Collections.singletonMap("code", AccessManager.CREATE), Integer.class).getResult().get(0));
		assertEquals(
				Integer.valueOf(1),
				FlexibleSearch
						.getInstance()
						.search("SELECT count({PK}) FROM {UserRight} WHERE {code}=?code",
								Collections.singletonMap("code", AccessManager.CHANGE), Integer.class).getResult().get(0));
		assertEquals(
				Integer.valueOf(1),
				FlexibleSearch
						.getInstance()
						.search("SELECT count({PK}) FROM {UserRight} WHERE {code}=?code",
								Collections.singletonMap("code", AccessManager.REMOVE), Integer.class).getResult().get(0));
		assertEquals(
				Integer.valueOf(1),
				FlexibleSearch
						.getInstance()
						.search("SELECT count({PK}) FROM {UserRight} WHERE {code}=?code",
								Collections.singletonMap("code", AccessManager.CHANGE_PERMISSIONS), Integer.class).getResult().get(0));

		assertNotNull(READ);
		assertNotNull(CHANGE);
		assertNotNull(CREATE);
		assertNotNull(REMOVE);
		assertNotNull(PERMISSIONS);

		ComposedType productType = TypeManager.getInstance().getComposedType(Product.class);

		assertTrue(productType.checkPermission(dummygroup, READ));
		assertTrue(productType.checkPermission(dummygroup, CHANGE));
		assertFalse(productType.checkPermission(dummygroup, CREATE));
		assertTrue(productType.checkPermission(dummygroup, REMOVE));
		assertTrue(productType.checkPermission(dummygroup, PERMISSIONS));

		productType = TypeManager.getInstance().getComposedType(Unit.class);

		assertTrue(productType.checkPermission(dummygroup, READ));
		assertTrue(productType.checkPermission(dummygroup, CHANGE));
		assertFalse(productType.checkPermission(dummygroup, CREATE));
		assertTrue(productType.checkPermission(dummygroup, REMOVE));
		assertTrue(productType.checkPermission(dummygroup, PERMISSIONS));

		productType = TypeManager.getInstance().getComposedType(Language.class);

		assertTrue(productType.checkPermission(dummygroup, READ));
		assertTrue(productType.checkPermission(dummygroup, CHANGE));
		assertFalse(productType.checkPermission(dummygroup, CREATE));
		assertTrue(productType.checkPermission(dummygroup, REMOVE));
		assertTrue(productType.checkPermission(dummygroup, PERMISSIONS));
	}

	/**
	 * Tests the alternative pattern syntax of header for import.
	 */
	@Test
	public void testAlternativePatterns() throws ImpExException
	{
		final ImpExImportReader reader = new ImpExImportReader("INSERT Title; " + Title.CODE + "[unique=true] \n" + "; t1 \n"
				+ "; t2 \n" + "INSERT Country; " + Country.ISOCODE + "[unique=true]; " + Country.ACTIVE + "[default='false'] \n"
				+ "; c1; \n" + "; t2; \n" + "; c2; false \n" + "INSERT Link; " + Link.QUALIFIER + "[unique=true];" + Link.SOURCE
				+ "(Title." + Title.CODE + "|Country." + Country.ISOCODE + ")[unique=true]; " + Link.TARGET + "(Country."
				+ Country.ISOCODE + "|Title." + Title.CODE + ")[unique=true] \n" + "; l1 ; t1 ; t1 \n" // title -> title
				+ "; l2 ; t2 ; t2 \n" // title -> country ( same code as title! )
				+ "; l3 ; c2 ; c1 \n" // country -> country
				+ "; l4 ; c1 ; t1 \n" // country -> title
				+ "; l5 ; c1 ; c1 \n" // country -> country
		);

		final Title title1 = (Title) reader.readLine();
		assertEquals("t1", title1.getCode());
		final Title title2 = (Title) reader.readLine();
		assertEquals("t2", title2.getCode());

		final Country country1 = (Country) reader.readLine();
		assertEquals("c1", country1.getIsoCode());
		final Country countryTitle2 = (Country) reader.readLine();
		assertEquals("t2", countryTitle2.getIsoCode());
		final Country country2 = (Country) reader.readLine();
		assertEquals("c2", country2.getIsoCode());

		Link link = (Link) reader.readLine();
		assertEquals("l1", link.getQualifier());
		assertEquals(title1, link.getSource());
		assertEquals(title1, link.getTarget());

		link = (Link) reader.readLine();
		assertEquals("l2", link.getQualifier());
		assertEquals(title2, link.getSource());
		assertEquals(countryTitle2, link.getTarget());

		link = (Link) reader.readLine();
		assertEquals("l3", link.getQualifier());
		assertEquals(country2, link.getSource());
		assertEquals(country1, link.getTarget());

		link = (Link) reader.readLine();
		assertEquals("l4", link.getQualifier());
		assertEquals(country1, link.getSource());
		assertEquals(title1, link.getTarget());

		link = (Link) reader.readLine();
		assertEquals("l5", link.getQualifier());
		assertEquals(country1, link.getSource());
		assertEquals(country1, link.getTarget());

		assertNull(reader.readLine());
	}

	/**
	 * Tests if the import of private attributes is possible.
	 */
	@Test
	public void testImportingPrivateAttributes() throws JaloDuplicateQualifierException, JaloSecurityException, ImpExException
	{
		final ComposedType testItemType = TypeManager.getInstance().getComposedType(TestItem.class);
		final AttributeDescriptor attribute = testItemType.createAttributeDescriptor("privateAttribute", jaloSession
				.getTypeManager().getRootAtomicType(Boolean.class), AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
				+ AttributeDescriptor.SEARCH_FLAG + AttributeDescriptor.REMOVE_FLAG + AttributeDescriptor.PRIVATE_FLAG);
		assertTrue(attribute.isPrivate());

		final String data = "INSERT TestItem; privateAttribute \n" + "; true \n" + "; false ";

		final ImpExImportReader reader = new ImpExImportReader(new CSVReader(new StringReader(data)), null);
		final TestItem test1 = (TestItem) reader.readLine();
		assertNotNull(test1);
		assertTrue(((Boolean) test1.getAttribute("privateAttribute")).booleanValue());
		final TestItem test2 = (TestItem) reader.readLine();
		assertNotNull(test2);
		assertFalse(((Boolean) test2.getAttribute("privateAttribute")).booleanValue());
	}

	/**
	 * Tests adding and removing of values to an collection using the different possibilities. This functionality is
	 * described by CORE-3484.
	 */
	@Test
	public void testCollectionModification() throws ConsistencyCheckException, ImpExException
	{
		// create some languages
		final Language language1 = C2LManager.getInstance().createLanguage("l1");
		final Language language2 = C2LManager.getInstance().createLanguage("l2");

		final Language fallback1 = C2LManager.getInstance().createLanguage("f1");
		final Language fallback2 = C2LManager.getInstance().createLanguage("f2");
		final Language fallback3 = C2LManager.getInstance().createLanguage("f3");

		// Test replacing collection by a one-element collection
		// CORE-3519
		String data = "UPDATE Language; isocode[unique=true]; fallbackLanguages(isocode) \n" + "; l2;f1;\n";

		assertEquals(0, language2.getFallbackLanguages().size());

		ImpExImportReader reader = new ImpExImportReader(new CSVReader(new StringReader(data)), null);

		Language updatedLang = (Language) reader.readLine();

		assertEquals(0, reader.getDumpedLineCount());
		assertEquals(1, updatedLang.getFallbackLanguages().size());
		assertEquals(language2, updatedLang);

		//reset
		language2.setFallbackLanguages(Collections.EMPTY_LIST);

		// test replacing, adding and removing of collection elements
		data = "UPDATE Language ; isoCode[unique=true]; fallbackLanguages(isoCode) \n" // h1
				+ "; l1 ; (+)f1 \n" //
				+ "; l1 ; (+)f2 \n" //
				+ "UPDATE Language ; isoCode[unique=true]; fallbackLanguages(isoCode) \n" // h2
				+ "; l1 ; (-)f1 \n" //
				+ "; l1 ; (-)f2 \n" //
				+ "UPDATE Language; isocode[unique=true]; fallbackLanguages(isocode)[mode=append] \n" // h3
				+ "; l2; f1\n"//
				+ "; l2; f2, f3\n"//
				+ "UPDATE Language; isocode[unique=true]; fallbackLanguages(isocode)[mode=remove] \n" // h4
				+ "; l2; f2, f3";

		reader = new ImpExImportReader(new CSVReader(new StringReader(data)), null);

		// h1
		updatedLang = (Language) reader.readLine();
		assertEquals(language1, updatedLang);
		assertCollection(Arrays.asList(new Object[]
		{ fallback1 }), updatedLang.getFallbackLanguages());

		updatedLang = (Language) reader.readLine();
		assertEquals(language1, updatedLang);
		assertCollection(Arrays.asList(new Object[]
		{ fallback1, fallback2 }), updatedLang.getFallbackLanguages());
		// h2
		updatedLang = (Language) reader.readLine();
		assertEquals(language1, updatedLang);
		assertCollection(Arrays.asList(new Object[]
		{ fallback2 }), updatedLang.getFallbackLanguages());

		updatedLang = (Language) reader.readLine();
		assertEquals(language1, updatedLang);
		assertCollection(Arrays.asList(new Object[] {}), updatedLang.getFallbackLanguages());
		// h3
		updatedLang = (Language) reader.readLine();
		assertEquals(language2, updatedLang);
		assertCollection(Arrays.asList(new Object[]
		{ fallback1 }), updatedLang.getFallbackLanguages());

		updatedLang = (Language) reader.readLine();
		assertEquals(language2, updatedLang);
		assertCollection(Arrays.asList(new Object[]
		{ fallback1, fallback2, fallback3 }), updatedLang.getFallbackLanguages());
		// h6
		updatedLang = (Language) reader.readLine();
		assertEquals(language2, updatedLang);
		assertCollection(Arrays.asList(new Object[]
		{ fallback1 }), updatedLang.getFallbackLanguages());
	}

	/**
	 * Tests the import in update mode.
	 */
	@Test
	public void testModeUpdate() throws IOException, ImpExException
	{
		final ComposedType unitType = TypeManager.getInstance().getComposedType(Unit.class);
		final String header = "UPDATE " + unitType.getCode() + ";" + Unit.CODE + "[ unique= true];" + Unit.CONVERSION
				+ "[default=1];" + Unit.UNITTYPE + "[default='blah',unique=true];" + Unit.NAME + "[lang='"
				+ german.getIsoCode().toUpperCase() + "'];" + Unit.NAME + "[lang='" + english.getIsoCode() + "', default   ='juhu']";

		final String values = " ; g ; 1 ; mass; Gramm; gramm \n" + " ; kg ; 1000 ; mass; Kilogramm; kilo \n" + "\n"
				+ " ; t ; 1000000 ; mass; Tonne ; ton \n" + " ; mg ; 0,001 ; mass; Milligramm; milligram \n" + "\n"
				+ "# blub blubb\n" + " ; default-test ; ; ; was \n" // <- last cell missing !
				+ " ; default-test ; ; differentType "; // <- last 2 cells missing, default value

		final Unit unitT = ProductManager.getInstance().createUnit("mass", "t");
		assertEquals(null, unitT.getAllNames(null).get(german));
		assertEquals(null, unitT.getAllNames(null).get(english));
		final Unit unitDefaultTest = ProductManager.getInstance().createUnit("blah", "default-test");
		assertEquals(null, unitDefaultTest.getAllNames(null).get(german));
		assertEquals(null, unitDefaultTest.getAllNames(null).get(english));

		final StringWriter dump = new StringWriter();

		ImpExImportReader reader = new ImpExImportReader(new CSVReader(new StringReader(header + "\n" + values)), new CSVWriter(
				dump));
		Object line = null;
		final List units = new ArrayList();

		try
		{
			do
			{
				line = reader.readLine();
				if (line != null)
				{
					assertTrue(line instanceof Unit);
					units.add(line);
				}
			}
			while (line != null);
		}
		finally
		{
			reader.close();
		}
		assertEquals(4, reader.getDumpedLineCount());

		// 6 to update - 4 non-existing = 2
		assertEquals(2, units.size());
		assertEquals(Arrays.asList(new Object[]
		{ unitT, unitDefaultTest }), units);
		checkUnit(unitT, unitType, "t", 1000000.0, "mass", "Tonne", "ton");
		checkUnit(unitDefaultTest, unitType, "default-test", 1.0, "blah", "was", "juhu"); // <- default value

		final Unit unitG = ProductManager.getInstance().createUnit("mass", "g");
		final Unit unitKg = ProductManager.getInstance().createUnit("mass", "kg");
		final Unit unitMg = ProductManager.getInstance().createUnit("mass", "mg");
		final Unit unitDef = ProductManager.getInstance().createUnit("differentType", "default-test");

		final StringWriter dump2 = new StringWriter();

		reader = new ImpExImportReader(new CSVReader(new StringReader(dump.getBuffer().toString())), new CSVWriter(dump2));
		reader.setIsSecondPass();

		final List secondPassUnits = new ArrayList();
		try
		{
			do
			{
				line = reader.readLine();
				if (line != null)
				{
					assertTrue(line instanceof Unit);
					secondPassUnits.add(line);
				}
			}
			while (line != null);
		}
		finally
		{
			reader.close();
		}
		// 4 to resolve
		assertEquals(4, secondPassUnits.size());
		assertEquals(Arrays.asList(new Object[]
		{ unitG, unitKg, unitMg, unitDef }), secondPassUnits);
		checkUnit(unitG, unitType, "g", 1.0, "mass", "Gramm", "gramm");
		checkUnit(unitKg, unitType, "kg", 1000.0, "mass", "Kilogramm", "kilo");
		checkUnit(unitT, unitType, "t", 1000000.0, "mass", "Tonne", "ton");
		checkUnit(unitMg, unitType, "mg", 0.001, "mass", "Milligramm", "milligram");
		checkUnit(unitDefaultTest, unitType, "default-test", 1.0, "blah", "was", "juhu"); // <- default value
		checkUnit(unitDef, unitType, "default-test", 1.0, "differentType", null, "juhu");

		assertEquals(0, reader.getDumpedLineCount());
	}

	/**
	 * Tests the import in remove mode.
	 */
	@Test
	public void testModeRemove() throws ConsistencyCheckException, ImpExException
	{
		final ComposedType titleType = TypeManager.getInstance().getComposedType(Title.class);
		final String header = "REMOVE " + titleType.getCode() + ";" + Title.CODE + "[unique=true]";

		Title title1 = null, title2 = null, title3 = null;
		title1 = UserManager.getInstance().createTitle("t1");
		title2 = UserManager.getInstance().createTitle("t2");
		title3 = UserManager.getInstance().createTitle("t3");

		assertTrue(title1.isAlive());
		assertTrue(title2.isAlive());
		assertTrue(title3.isAlive());
		assertEquals("t1", title1.getCode());
		assertEquals("t2", title2.getCode());
		assertEquals("t3", title3.getCode());

		final String data = ";" + "t1" + "\n" + ";" + "t3" + "\n" + "# once again - this should just log a error: \n" + ";" + "t1"
				+ "\n" + ";" + "t2" + "\n";

		final StringWriter dump = new StringWriter();
		final ImpExImportReader reader = new ImpExImportReader(new CSVReader(new StringReader(header + "\n" + data)),
				new CSVWriter(dump));

		// remove t1
		assertEquals(title1, reader.readLine());
		assertFalse(title1.isAlive());

		// remove t3
		assertEquals(title3, reader.readLine());
		assertFalse(title3.isAlive());

		// removing t1 again should just log a error - nothing will appear here !

		// remove t2
		assertEquals(title2, reader.readLine());
		assertFalse(title2.isAlive());

		assertNull(reader.readLine());
	}

	/**
	 * Tests the import in insert update mode.
	 */
	@Test
	public void testModeInsertUpdate() throws ImpExException
	{
		final ComposedType unitType = TypeManager.getInstance().getComposedType(Unit.class);
		final String header = "INSERT_UPDATE " + unitType.getCode() + ";" + Unit.CODE + "[ unique= true];" + Unit.CONVERSION
				+ "[default=1];" + Unit.UNITTYPE + "[default='blah',unique=true];" + Unit.NAME + "[lang='"
				+ german.getIsoCode().toUpperCase() + "'];" + Unit.NAME + "[lang='" + english.getIsoCode() + "', default   ='juhu']";

		final String values = " ; g ; 1 ; mass; Gramm; gramm \n" + " ; kg ; 1000 ; mass; Kilogramm; kilo \n" + "\n"
				+ " ; t ; 1000000 ; mass; Tonne ; ton \n" + " ; mg ; 0,001 ; mass; Milligramm; milligram \n" + "\n"
				+ "# blub blubb\n" + " ; default-test ; ; ; was \n" // <- last cell missing !
				+ " ; default-test ; ; differentType "; // <- last 2 cells missing, default value

		final Unit unitT;
        final Unit unitMg;
        unitT = ProductManager.getInstance().createUnit("mass", "t");
		assertNull(unitT.getAllNames(null).get(german));
		assertNull(unitT.getAllNames(null).get(english));
		unitMg = ProductManager.getInstance().createUnit("mass", "mg");
		assertNull(unitMg.getAllNames(null).get(german));
		assertNull(unitMg.getAllNames(null).get(english));

		final StringWriter dump = new StringWriter();
		final ImpExImportReader reader = new ImpExImportReader(new CSVReader(new StringReader(header + "\n" + values)),
				new CSVWriter(dump));
		Unit unitG = null, unitKg = null, unitDef1 = null, unitDef2 = null;
		Object item;
		// g
		item = reader.readLine();
		assertTrue(item instanceof Unit);
		unitG = (Unit) item;
		// kg
		item = reader.readLine();
		assertTrue(item instanceof Unit);
		unitKg = (Unit) item;
		// t
		item = reader.readLine();
		assertTrue(item instanceof Unit);
		assertEquals(unitT, item);
		// mg
		item = reader.readLine();
		assertTrue(item instanceof Unit);
		assertEquals(unitMg, item);
		// default 1
		item = reader.readLine();
		assertTrue(item instanceof Unit);
		unitDef1 = (Unit) item;
		// default 2
		item = reader.readLine();
		assertTrue(item instanceof Unit);
		unitDef2 = (Unit) item;

		// end of file
		assertNull(reader.readLine());

		checkUnit(unitG, unitType, "g", 1.0, "mass", "Gramm", "gramm");
		checkUnit(unitKg, unitType, "kg", 1000.0, "mass", "Kilogramm", "kilo");
		checkUnit(unitT, unitType, "t", 1000000.0, "mass", "Tonne", "ton");
		checkUnit(unitMg, unitType, "mg", 0.001, "mass", "Milligramm", "milligram");
		checkUnit(unitDef1, unitType, "default-test", 1.0, "blah", "was", "juhu"); // <- default value
		checkUnit(unitDef2, unitType, "default-test", 1.0, "differentType", null, "juhu");
	}

	/**
	 * Test the insert in insert mode.
	 */
	@Test
	public void testInsertMode()
	{
		final ComposedType unitType = TypeManager.getInstance().getComposedType(Unit.class);
		final String header = "INSERT " + unitType.getCode() + ";" + Unit.CODE + "[ unique= true];" + Unit.CONVERSION
				+ "[default=1];" + Unit.UNITTYPE + "[default='blah',unique=true];" + Unit.NAME + "[lang='"
				+ german.getIsoCode().toUpperCase() + "'];" + Unit.NAME + "[lang='" + english.getIsoCode() + "', default   ='juhu']";

		final String values = " ; g ; 1 ; mass; Gramm; gramm \n" + " ; kg ; 1000 ; mass; Kilogramm; kilo \n" + "\n"
				+ " ; t ; 1000000 ; mass; Tonne ; ton \n" + " ; mg ; 0,001 ; mass; Milligramm; milligram \n" + "\n"
				+ "# blub blubb\n" + " ; default-test ; ; ; was \n" // <- last cell missing !
				+ " ; default-test ; ; differentType "; // <- last 2 cells missing, default value

		final String duplicateValues = " ; t ; 1000000 ; mass \n" + " ; default-test ; ; blah ";

		// create reader
		final ImpExImportReader reader = new ImpExImportReader(new CSVReader("# testInsertMode \n" + header + "\n" + values + "\n"
				+ "\n" + duplicateValues), new CSVWriter(new StringWriter()));

		// import all units
		final List units = new ArrayList();
		Unit unit = null;
		int conflictCount = 0;
		do
		{
			try
			{
				unit = (Unit) reader.readLine();
				if (unit != null)
				{
					units.add(unit);
				}
			}
			catch (final ImpExException e)
			{
				// make sure that previous 6 have been read properly
				assertEquals(6, units.size());
				conflictCount++;
				// leave u != null so we're trying next lines too
			}
			catch (final ClassCastException e)
			{
				fail("wrong result class - expected Unit but was " + unit.getClass().getName() + " : " + e.getMessage());
			}
			catch (final Exception e)
			{
				fail("unexpected error : " + e.getMessage());
			}
		}
		while (unit != null);

		/*
		 * check them: " ; g ; 1 ; mass \n"+ " ; kg ; 1000 ; mass \n"+ " ; t ; 1000000 ; mass \n"+
		 * " ; mg ; 0.001 ; mass \n"+ " ; default-test ; ; "; " ; default-test ; ; differentType ";
		 */
		assertEquals(6, units.size());
		checkUnit((Unit) units.get(0), unitType, "g", 1.0, "mass", "Gramm", "gramm");
		checkUnit((Unit) units.get(1), unitType, "kg", 1000.0, "mass", "Kilogramm", "kilo");
		checkUnit((Unit) units.get(2), unitType, "t", 1000000.0, "mass", "Tonne", "ton");
		checkUnit((Unit) units.get(3), unitType, "mg", 0.001, "mass", "Milligramm", "milligram");
		checkUnit((Unit) units.get(4), unitType, "default-test", 1.0, "blah", "was", "juhu"); // <- default value
		checkUnit((Unit) units.get(5), unitType, "default-test", 1.0, "differentType", null, "juhu");

		assertEquals(2, reader.getDumpedLineCount());
		assertEquals(2, conflictCount);
	}

	/**
	 * Tests the import with dumped lines.
	 */
	@Test
	public void testDumpingLines()
	{
		final ComposedType langType = TypeManager.getInstance().getComposedType(Language.class);

		final String script = ImpExConstants.Syntax.Mode.INSERT + " " + langType.getCode() + ";" + Language.ISOCODE
				+ "[unique=true];" + Language.ACTIVE + "[default=false];" + Language.FALLBACKLANGUAGES + "(" + Language.ISOCODE
				+ ")\n" + ";" + "iso1" + ";" + "true" + ";" + "iso2" + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER
				+ "iso3" + "\n" + ";" + "iso2" + ";" + ";" + "iso1" + "\n" + ";" + "iso3" + ";" + "true" + ";" + "\n"
				+ ImpExConstants.Syntax.Mode.UPDATE + " " + langType.getCode() + ";" + Language.ISOCODE + "[unique=true];"
				+ Language.ACTIVE + "\n" + ";" + "iso4" + ";" + "true" + "\n";

		StringWriter dump = new StringWriter();
		ImpExImportReader importReader = new ImpExImportReader(new CSVReader(script), new CSVWriter(dump));

		final List items = new ArrayList();
		try
		{
			Object line = null;
			int index = 1;
			do
			{
				line = importReader.readLine();
				if (line != null)
				{
					assertTrue(line instanceof Language);
					items.add(line);
					assertEquals("iso" + index, ((Language) line).getIsoCode());
					index++;
				}
			}
			while (line != null);
		}
		catch (final ImpExException e)
		{
			fail("unexepected error " + e.getMessage());
		}
		finally
		{
			try
			{
				importReader.close();
			}
			catch (final IOException e)
			{
				fail("error during closing reader : " + e.getMessage());
			}
		}
		assertEquals(3, items.size());
		assertEquals(2, importReader.getDumpedLineCount());

		String dumpedLines = dump.getBuffer().toString();
		CSVReader csvReader = new CSVReader(dumpedLines);
		// dumped header1
		assertTrue(csvReader.readNextLine());
		Map lineMap = csvReader.getLine();
		assertEquals(ImpExConstants.Syntax.Mode.INSERT + " " + langType.getCode(), lineMap.get(Integer.valueOf(0)));
		assertEquals(Language.ISOCODE + "[unique=true]", lineMap.get(Integer.valueOf(1)));
		assertEquals(Language.ACTIVE + "[default=false]", lineMap.get(Integer.valueOf(2)));
		assertEquals(Language.FALLBACKLANGUAGES + "(" + Language.ISOCODE + ")", lineMap.get(Integer.valueOf(3)));
		// dumped line 'iso1'
		assertTrue(csvReader.readNextLine());
		lineMap = csvReader.getLine();
		assertEquals(langType.getCode() + "," + ((Language) items.get(0)).getPK().toString()
				+ ",,,column 3: cannot resolve value 'iso2,iso3' for attribute 'fallbackLanguages'", lineMap.get(Integer.valueOf(0)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX + "iso1", lineMap.get(Integer.valueOf(1)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX + "true", lineMap.get(Integer.valueOf(2)));
		assertEquals("iso2" + ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER + "iso3", lineMap.get(Integer.valueOf(3)));
		// dumped header2
		assertTrue(csvReader.readNextLine());
		lineMap = csvReader.getLine();
		assertEquals(ImpExConstants.Syntax.Mode.UPDATE + " " + langType.getCode(), lineMap.get(Integer.valueOf(0)));
		assertEquals(Language.ISOCODE + "[unique=true]", lineMap.get(Integer.valueOf(1)));
		assertEquals(Language.ACTIVE, lineMap.get(Integer.valueOf(2)));
		// dumped line 'iso14' -> test dump reason of a sort
		assertTrue(csvReader.readNextLine());
		lineMap = csvReader.getLine();
		assertEquals(",,,,no existing item found for update", lineMap.get(Integer.valueOf(0)));
		assertEquals("iso4", lineMap.get(Integer.valueOf(1)));
		assertEquals("true", lineMap.get(Integer.valueOf(2)));
		//
		assertFalse(csvReader.readNextLine());

		// create a second-pass reader
		dump = new StringWriter();
		importReader = new ImpExImportReader(new CSVReader(new StringReader(dumpedLines)), new CSVWriter(dump));
		importReader.setIsSecondPass();

		final List secondPassItems = new ArrayList();
		try
		{
			Object line = null;
			do
			{
				line = importReader.readLine();
				if (line != null)
				{
					assertTrue(line instanceof Language);
					secondPassItems.add(line);
				}
			}
			while (line != null);
		}
		catch (final ImpExException e)
		{
			fail("unexepected error " + e.getMessage());
		}
		finally
		{
			try
			{
				importReader.close();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
				fail("error during closing reader : " + e.getMessage());
			}
		}
		assertEquals(1, secondPassItems.size());
		assertEquals(1, importReader.getDumpedLineCount());

		dumpedLines = dump.getBuffer().toString();
		csvReader = new CSVReader(dumpedLines);
		// dumped header2
		assertTrue(csvReader.readNextLine());
		lineMap = csvReader.getLine();
		assertEquals(ImpExConstants.Syntax.Mode.UPDATE + " " + langType.getCode(), lineMap.get(Integer.valueOf(0)));
		assertEquals(Language.ISOCODE + "[unique=true]", lineMap.get(Integer.valueOf(1)));
		assertEquals(Language.ACTIVE, lineMap.get(Integer.valueOf(2)));
		// dumped line 'iso14' -> test dump reason of a sort
		assertTrue(csvReader.readNextLine());
		lineMap = csvReader.getLine();
		assertEquals(",,,,no existing item found for update", lineMap.get(Integer.valueOf(0)));
		assertEquals("iso4", lineMap.get(Integer.valueOf(1)));
		assertEquals("true", lineMap.get(Integer.valueOf(2)));
		//
		assertFalse(csvReader.readNextLine());

		assertEquals(items.get(0), secondPassItems.get(0));

		final Language language1 = (Language) items.get(0);
		final Language language2 = (Language) items.get(1);
		final Language language3 = (Language) items.get(2);

		assertEquals("iso1", language1.getIsoCode());
		assertEquals("iso2", language2.getIsoCode());
		assertEquals("iso3", language3.getIsoCode());
		assertTrue(language1.isActive().booleanValue());
		assertFalse(language2.isActive().booleanValue());
		assertTrue(language3.isActive().booleanValue());
		assertEquals(Arrays.asList(new Object[]
		{ language2, language3 }), language1.getFallbackLanguages());
		assertEquals(Collections.singletonList(language1), language2.getFallbackLanguages());
		assertEquals(Collections.EMPTY_LIST, language3.getFallbackLanguages());
	}

	/**
	 * Tests the update mode using localized attributes with unique modifier. (The item resolving with unique localized
	 * attributes is the test case). See CORE-4204.
	 */
	@Test
	public void testModeUpdateWithUniqueLocalizedAttribut()
	{
		final SessionContext ctx = jaloSession.createSessionContext();
		final ImpExImportReader importReader = new ImpExImportReader("INSERT_UPDATE Country;" + Country.ISOCODE + "[unique=true];"
				+ Country.NAME + "[lang=de,unique=true];" + Country.NAME + "[lang=en,unique=true];" + Country.ACTIVE + "\n"
				+ ";c1;na1;nb1;false \n" + ";c1;na1;nb1;false \n" + ";c1;na1;nc1;false \n" + "INSERT_UPDATE Country;"
				+ Country.ISOCODE + "[unique=true];" + Country.NAME + "[lang=de];" + Country.NAME + "[lang=en];" + Country.ACTIVE
				+ "\n" + ";c2;na1;nb1;false \n" + ";c2;na1;nb1;false \n" + ";c2;na1;nc1;false \n");
		Country test = null;
		try
		{
			test = (Country) importReader.readLine();
			assertEquals("c1", test.getIsoCode());
			ctx.setLanguage(german);
			assertEquals("na1", test.getName(ctx));
			ctx.setLanguage(english);
			assertEquals("nb1", test.getName(ctx));
			test = (Country) importReader.readLine();
			assertEquals("c1", test.getIsoCode());
			ctx.setLanguage(german);
			assertEquals("na1", test.getName(ctx));
			ctx.setLanguage(english);
			assertEquals("nb1", test.getName(ctx));
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		try
		{
			test = (Country) importReader.readLine();
			fail("Exception 'duplicate isocode' expected");
		}
		catch (final ImpExException e)
		{
			// OK
		}
		try
		{
			test = (Country) importReader.readLine();
			assertEquals("c2", test.getIsoCode());
			ctx.setLanguage(german);
			assertEquals("na1", test.getName(ctx));
			ctx.setLanguage(english);
			assertEquals("nb1", test.getName(ctx));
			test = (Country) importReader.readLine();
			assertEquals("c2", test.getIsoCode());
			ctx.setLanguage(german);
			assertEquals("na1", test.getName(ctx));
			ctx.setLanguage(english);
			assertEquals("nb1", test.getName(ctx));
			test = (Country) importReader.readLine();
			assertEquals("c2", test.getIsoCode());
			ctx.setLanguage(german);
			assertEquals("na1", test.getName(ctx));
			ctx.setLanguage(english);
			assertEquals("nc1", test.getName(ctx));
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
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
	 * Tests the update mode for updating unique attributes. See CORE-3377.
	 */
	@Test
	public void testModeUpdateWithUniqueAttribute()
	{
		final String lines = "INSERT LANGUAGE; isocode[unique=true]; active\n" + ";thalersch;false\n"
				+ "UPDATE LANGUAGE; isocode[unique=true];isocode;active\n" + ";thalersch;jupp;true\n"
				+ "INSERT_UPDATE LANGUAGE; isocode[unique=true];isocode;active\n" + ";jupp;juppupp;true";
		final ImpExImportReader reader = new ImpExImportReader(lines);
        reader.setInvalidHeaderPolicy(InvalidHeaderPolicy.THROW_EXCEPTION);
		try
		{
			final Language lang = (Language) reader.readLine();
			assertEquals("thalersch", lang.getIsoCode());
			assertFalse(lang.isActive().booleanValue());
			reader.readLine();
			assertEquals("jupp", lang.getIsoCode());
			assertTrue(lang.isActive().booleanValue());
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}

		try
		{
			reader.readLine();
		}
		catch (final HeaderValidationException e)
		{
			assertEquals(HeaderValidationException.AMBIGUOUS_SIBLING_ATTRIBUTE, e.getErrorCode());
		}
		catch (final ImpExException e)
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
	 * See PLA-5010.
	 */
	@Test
	public void testLocalizedReference()
	{
		try
		{
			JaloSession.getCurrentSession().createLocalSessionContext();
			JaloSession.getCurrentSession().getSessionContext().setLanguage(null);
			final String lines = "INSERT Unit;unitType;code;name[lang=de]\n" + ";test;test;test\n"
					+ "INSERT product;code;catalogversion(catalog(id), version)[allowNull=true];unit(name[lang=de])\n"
					+ ";test;;test\n" + "INSERT product;code;catalogversion(catalog(id), version)[allowNull=true];unit(name)\n"
					+ ";test2;;test";
			final ImpExImportReader reader = new ImpExImportReader(lines);
            reader.setInvalidHeaderPolicy(InvalidHeaderPolicy.THROW_EXCEPTION);
			final Unit unit = (Unit) reader.readLine();
			assertEquals(0, reader.getDumpedLineCount());
			assertEquals("test", unit.getUnitType());
			assertEquals("test", unit.getCode());
			final Product product = (Product) reader.readLine();
			assertEquals(0, reader.getDumpedLineCount());
			assertEquals("test", product.getCode());
			assertEquals("test", product.getUnit().getCode());
			try
			{
				reader.readLine();
				fail("referencing a localized attribute without language modifier is not permitted");
			}
			catch (final ImpExException e)
			{
				//OK
			}
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}

	/**
	 * Tests the import of addresses if duplicate flag is set. (There should not be duplicated while import) See
	 * CORE-3655.
	 */
	@Test
	public void testDuplicatedAddresses()
	{
		assertNull(JaloSession.getCurrentSession().getAttribute(CoreImpExConstants.IMPORT_MODE));
		try
		{
			C2LManager.getInstance().createCurrency("cur");
		}
		catch (final ConsistencyCheckException e1)
		{
			fail();
		}

		final String lines = "INSERT Address;" + "owner(Principal.uid);appartment;duplicate\n" + ";admin;a1;false\n"
				+ ";admin;a2;true\n" + "INSERT ORDER;" + "code;deliveryAddress(appartment);"
				+ "net; user(uid); currency(isocode); date[dateformat=dd.MM.yyyy]; calculated[allowNull=true]\n"
				+ ";o1;a1;false;admin;cur;01.01.2010;;\n" + ";o2;a2;false;admin;cur;01.01.2010;;";

		final ImpExImportReader reader = new ImpExImportReader(lines);
		try
		{
			final Address addA = (Address) reader.readLine();
			assertFalse(addA.isDuplicate().booleanValue());

			final Address addB = (Address) reader.readLine();
			assertTrue(addB.isDuplicate().booleanValue());

			final Order orderA = (Order) reader.readLine();
			assertNotSame(addA, orderA.getDeliveryAddress());
			assertTrue(orderA.getDeliveryAddress().isDuplicate().booleanValue());

			final Order orderB = (Order) reader.readLine();
			assertEquals(addB, orderB.getDeliveryAddress());

			SearchResult<Address> result = FlexibleSearch.getInstance().search("SELECT {PK} FROM {Address} WHERE {appartment}='a1'",
					Address.class);
			assertEquals(2, result.getCount());

			result = FlexibleSearch.getInstance().search("SELECT {PK} FROM {Address} WHERE {appartment}='a2'", Address.class);
			assertEquals(1, result.getCount());

			assertEquals(0, reader.getDumpedLineCount());
		}
		catch (final ImpExException e)
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
		assertNull(JaloSession.getCurrentSession().getAttribute(CoreImpExConstants.IMPORT_MODE));
	}

	@Test
	public void testSingletonImport()
	{
		ComposedType type = null;
		final TypeManager manager = TypeManager.getInstance();
		try
		{
			type = manager.createComposedType(manager.getComposedType(GenericItem.class), "TestSingleton");
			type.setSingleton(true);
			type.createAttributeDescriptor("test1", TypeManager.getInstance().getType(String.class.getName()),
					AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.OPTIONAL_FLAG);
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
		final String header = "INSERT " + type.getCode() + ";test1[ unique= true];test2\n" + ";bla11;bla12\n" + "INSERT "
				+ type.getCode() + ";test1;test2\n" + ";bla21;bla22\n" + "UPDATE " + type.getCode() + ";test1[ unique= true];test2\n"
				+ ";bla31;bla32\n" + "UPDATE " + type.getCode() + ";test1;test2\n" + ";bla41;bla42\n" + "INSERT_UPDATE "
				+ type.getCode() + ";test1[ unique= true];test2\n" + ";bla51;bla52\n" + "REMOVE " + type.getCode()
				+ ";test1[ unique= true];test2\n" + ";bla61;bla62\n" + "INSERT_UPDATE " + type.getCode() + ";test1;test2\n"
				+ ";bla71;bla72\n" + "REMOVE " + type.getCode() + ";test1;test2\n" + ";bla81;bla82\n";


		// create reader
		final ImpExImportReader importReader = new ImpExImportReader(new CSVReader(header));
		try
		{
			Item singleton = (Item) importReader.readLine();
			assertNotNull(singleton);
			assertEquals("bla11", singleton.getAttribute("test1"));
			assertEquals("bla12", singleton.getAttribute("test2"));
			Item singleton2 = (Item) importReader.readLine();
			assertNotNull(singleton2);
			assertEquals(singleton, singleton2);
			assertEquals("bla21", singleton.getAttribute("test1"));
			assertEquals("bla22", singleton.getAttribute("test2"));
			singleton2 = (Item) importReader.readLine();
			assertNotNull(singleton2);
			assertEquals(singleton, singleton2);
			assertEquals("bla31", singleton.getAttribute("test1"));
			assertEquals("bla32", singleton.getAttribute("test2"));
			singleton2 = (Item) importReader.readLine();
			assertNotNull(singleton2);
			assertEquals(singleton, singleton2);
			assertEquals("bla41", singleton.getAttribute("test1"));
			assertEquals("bla42", singleton.getAttribute("test2"));
			singleton2 = (Item) importReader.readLine();
			assertNotNull(singleton2);
			assertEquals(singleton, singleton2);
			assertEquals("bla51", singleton.getAttribute("test1"));
			assertEquals("bla52", singleton.getAttribute("test2"));
			singleton2 = (Item) importReader.readLine();
			assertNotNull(singleton2);
			assertFalse(singleton2.isAlive());
			assertEquals(0, type.getAllInstancesCount());
			assertEquals(singleton, singleton2);
			singleton2 = (Item) importReader.readLine();
			assertNotNull(singleton2);
			assertNotSame(singleton, singleton2);
			singleton = singleton2;
			assertEquals("bla71", singleton.getAttribute("test1"));
			assertEquals("bla72", singleton.getAttribute("test2"));
			singleton2 = (Item) importReader.readLine();
			assertNotNull(singleton2);
			assertFalse(singleton2.isAlive());
			assertEquals(0, type.getAllInstancesCount());
			assertEquals(singleton, singleton2);
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		catch (final JaloSecurityException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Checks for given unit if it has all given attributes.
	 * 
	 * @param toCheck
	 *           unit to check
	 * @param composedType
	 *           expected type
	 * @param code
	 *           expected code
	 * @param conversion
	 *           expected conversion
	 * @param type
	 *           expected unit type
	 * @param nameDe
	 *           expected german language
	 * @param nameEn
	 *           expected english language
	 */
	protected void checkUnit(final Unit toCheck, final ComposedType composedType, final String code, final double conversion,
			final String type, final String nameDe, final String nameEn)
	{
		assertNotNull(toCheck);
		assertEquals(composedType, toCheck.getComposedType());
		assertEquals(code, toCheck.getCode());
		assertEquals(conversion, toCheck.getConversionFactor(), 0.000001);
		assertEquals(type, toCheck.getUnitType());
		final Map names = toCheck.getAllNames(null);
		assertEquals(nameDe, names.get(german));
		assertEquals(nameEn, names.get(english));
	}

	@Test
	public void testModificatedTimeByInsertUpdate() throws ImpExException, JaloDuplicateQualifierException,
			JaloInvalidParameterException, JaloSecurityException, InterruptedException
	{
		TestUtils.disableFileAnalyzer("log error expected");
		final ComposedType lang_ct = TypeManager.getInstance().getComposedType(Language.class);
		final Type colltype = TypeManager.getInstance().getType("LanguageCollection");
		lang_ct.createAttributeDescriptor("testcoll", colltype, AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG
				| AttributeDescriptor.PROPERTY_FLAG);

		final String impexcode = "INSERT_UPDATE Language;isocode[unique=true];name[lang=de];active;fallbacklanguages(isocode);testcoll(isocode)\n"
				+ ";fb1;fb1;false;;;\n" + ";fb2;fb2;false;;;\n" + ";testlang;testlang_de;false;fb1,fb2;fb1,fb2";
		ImpExImportReader reader = new ImpExImportReader(impexcode);

		final SessionContext ctx1 = JaloSession.getCurrentSession().createLocalSessionContext();
		ctx1.setLanguage(C2LManager.getInstance().getLanguageByIsoCode("de"));

		final Language fb1 = (Language) reader.readLine();
		assertEquals("fb1", fb1.getIsoCode());
		final Language fb2 = (Language) reader.readLine();
		assertEquals("fb2", fb2.getName(ctx1));
		final Language testlang = (Language) reader.readLine();
		assertEquals(2, testlang.getFallbackLanguages().size());
		assertEquals(2, ((Collection<Language>) testlang.getAttribute("testcoll")).size());
		assertTrue(((Collection<Language>) testlang.getAttribute("testcoll")).contains(fb1));
		assertTrue(((Collection<Language>) testlang.getAttribute("testcoll")).contains(fb2));

		final Date modtime1 = fb1.getModificationTime();
		final Date modtime2 = fb2.getModificationTime();
		final Date modtestlang = testlang.getModificationTime();

		//2sec waiting between the two impex operations, so we can be sure that both operations are done in different times
		Thread.sleep(2000);

		final String impexcode2 = "UPDATE Language;isocode[unique=true];name[lang=de];active;fallbacklanguages(isocode);testcoll(isocode);\n"
				+ ";fb1;fb1;false;;;\n" + ";fb2;fb2;false;;;\n" + ";testlang;testlang_de;false;fb1,fb2;fb1,fb2";


		reader = new ImpExImportReader(impexcode2);
		final Language fb1a = (Language) reader.readLine();
		final Language fb2a = (Language) reader.readLine();
		final Language testlanga = (Language) reader.readLine();

		assertEquals(modtime1, fb1a.getModificationTime());
		assertEquals(modtime2, fb2a.getModificationTime());
		assertEquals(modtestlang, testlanga.getModificationTime());
		TestUtils.enableFileAnalyzer();
	}

	@Test(expected = InsufficientDataException.class)
	@Transactional
	public void testInsertMode_WithSubtype() throws Exception
	{
		final ComposedType composedType = createSubtype();

		createSubtypedItem(composedType);

		final String lines = "INSERT Unit;code[unique=true];unitType\n" + "MyUnit;<code4711>;test";

		final ImpExImportReader reader = new ImpExImportReader(lines);

		final MyUnit unit = (MyUnit) reader.readLine();

		assertNotNull(unit);
		assertEquals("test", unit.getUnitType());
	}

	@Test
	@Transactional
	public void testInsertUpdateMode_Insert() throws Exception
	{
		final ComposedType composedType = createSubtype();

		createSubtypedItem(composedType);

		final String lines = "INSERT_UPDATE Unit;code[unique=true];unitType\n" + ";<code22>;test";

		final ImpExImportReader reader = new ImpExImportReader(lines);

		final Unit unit = (Unit) reader.readLine();

		assertNotNull(unit);
		assertEquals("test", unit.getUnitType());
	}

	@Test
	@Transactional
	public void testInsertUpdateMode_UpdateWithSubtype() throws Exception
	{
		final ComposedType composedType = createSubtype();

		createSubtypedItem(composedType);

		final String lines = "INSERT_UPDATE Unit;code[unique=true];unitType\n" + ";<code>;test";

		final ImpExImportReader reader = new ImpExImportReader(lines);

		final MyUnit unit = (MyUnit) reader.readLine();

		assertNotNull(unit);
		assertEquals("test", unit.getUnitType());
	}


	@Test
	@Transactional
	public void testUpdateMode_WithSubtype() throws Exception
	{
		final ComposedType composedType = createSubtype();

		createSubtypedItem(composedType);

		final String lines = "UPDATE Unit;code[unique=true];unitType\n" + ";<code>;test";

		final ImpExImportReader reader = new ImpExImportReader(lines);

		final MyUnit unit = (MyUnit) reader.readLine();

		assertNotNull(unit);
		assertEquals("test", unit.getUnitType());
	}


	@Test
	public void testReferencesWithNullValues() throws JaloGenericCreationException, JaloAbstractTypeException,
			JaloItemNotFoundException, ImpExException
	{
		final Customer cust = ComposedType.newInstance(null, Customer.class, Customer.UID, "Hugo");
		final Country c = ComposedType.newInstance(null, Country.class, Country.ISOCODE, "COUNTRY4711");
		final Address a = ComposedType.newInstance(null, Address.class, Address.OWNER, cust, Address.COUNTRY, c);

		assertEquals(Collections.singletonList(a), cust.getAddresses());
		assertNull(cust.getDefaultPaymentAddress());
		assertNull(cust.getDefaultShipmentAddress());

		final String lines = "UPDATE Customer; uid[unique=true]; " + //
				"defaultPaymentAddress(country(isocode),streetname); " + //
				"defaultShipmentAddress(country(isocode),streetname[default='<null>']) \n" + //
				"; Hugo; COUNTRY4711:<null>; COUNTRY4711\n";

		new ImpExImportReader(lines).readAll();

		assertEquals(Collections.singletonList(a), cust.getAddresses());
		assertEquals(a, cust.getDefaultPaymentAddress());
		assertEquals(a, cust.getDefaultShipmentAddress());
	}

	private ComposedType createSubtype() throws JaloTypeException
	{
		final ComposedType unitType = TypeManager.getInstance().getComposedType(Unit.class);
		final ComposedType composedType = TypeManager.getInstance().createComposedType(unitType, "MyUnit");
		composedType.setJaloClass(MyUnit.class);

		final AtomicType attrType = TypeManager.getInstance().getRootAtomicType(String.class);

		composedType.createAttributeDescriptor(MANDATORY_ATTR_QUALIFIER, attrType, AttributeDescriptor.READ_FLAG
				| AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG);

		return composedType;
	}

	private MyUnit createSubtypedItem(final ComposedType composedType) throws JaloTypeException, JaloBusinessException
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(Unit.CODE, "code");
		params.put(Unit.UNITTYPE, "type");
		params.put(MANDATORY_ATTR_QUALIFIER, MANDATORY_ATTR_VALUE);

		final MyUnit unit = (MyUnit) composedType.newInstance(params);

		assertNotNull(unit);
		assertEquals("<code>", unit.getCode());
		assertEquals(MANDATORY_ATTR_VALUE, unit.getAttribute(MANDATORY_ATTR_QUALIFIER));

		assertEquals(
				Integer.valueOf(1),
				FlexibleSearch
						.getInstance()
						.search("SELECT count({PK}) FROM {MyUnit} WHERE {code}=?code", Collections.singletonMap("code", "<code>"),
								Integer.class).getResult().get(0));

		return unit;
	}
}
