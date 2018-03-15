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
import de.hybris.platform.cronjob.jalo.BatchJob;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.cronjob.jalo.RemoveItemsJob;
import de.hybris.platform.cronjob.jalo.TypeSystemExportJob;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor.ColumnParams;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor.HeaderParams;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.CSVFromPropertiesReader;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.FixedLengthCSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;



/**
 * This test cases check the script parsing and header validation of the impex extension.
 */
@IntegrationTest
public class ImpExTest extends AbstractImpExTest
{

	/**
	 * Tests different kinds of equal attribute name in header.
	 */
	@Test
	public void testSiblingAttributes()
	{
		ComposedType shoeType = null, shirtType = null;
		EnumerationType shirtSizeEnumType = null;
		@SuppressWarnings("unused")
		AttributeDescriptor shoeSize = null, shirtSize = null;

		final TypeManager typeManager = jaloSession.getTypeManager();
		try
		{
			final EnumerationManager enumerationManager = EnumerationManager.getInstance();
			shirtSizeEnumType = enumerationManager.createEnumerationType("shirtSize", null);
			enumerationManager.createEnumerationValue(shirtSizeEnumType, "s");
			enumerationManager.createEnumerationValue(shirtSizeEnumType, "m");
			enumerationManager.createEnumerationValue(shirtSizeEnumType, "l");
			enumerationManager.createEnumerationValue(shirtSizeEnumType, "xl");
			enumerationManager.createEnumerationValue(shirtSizeEnumType, "xxl");

			shoeType = typeManager.createComposedType(typeManager.getComposedType(Product.class), "shoe");
			shoeSize = shoeType.createAttributeDescriptor("size",
					(AtomicType) typeManager.getAtomicTypesForJavaClass(String.class).iterator().next(), AttributeDescriptor.READ_FLAG
							+ AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG + AttributeDescriptor.REMOVE_FLAG);
			/* final AttributeDescriptor shoeColor = */shoeType.createAttributeDescriptor("color",
					(AtomicType) typeManager.getAtomicTypesForJavaClass(String.class).iterator().next(), AttributeDescriptor.READ_FLAG
							+ AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG + AttributeDescriptor.REMOVE_FLAG);
			shirtType = typeManager.createComposedType(typeManager.getComposedType(Product.class), "shirt");
			shirtSize = shirtType.createAttributeDescriptor("size", shirtSizeEnumType, AttributeDescriptor.READ_FLAG
					+ AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG + AttributeDescriptor.REMOVE_FLAG);
			/* final AttributeDescriptor shirtColor = */shirtType.createAttributeDescriptor("color",
					(AtomicType) typeManager.getAtomicTypesForJavaClass(String.class).iterator().next(), AttributeDescriptor.READ_FLAG
							+ AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG + AttributeDescriptor.REMOVE_FLAG);
		}

		catch (final JaloDuplicateCodeException e)
		{
			fail(e.getMessage());
		}
		catch (final JaloDuplicateQualifierException e)
		{
			fail(e.getMessage());
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}

		final ComposedType composedType = TypeManager.getInstance().getComposedType(Product.class);
		ImpExReader reader = null;
		String lines = "";

		// 1. subtypes have same attributes within the same column
		// will fail
		lines = lines + "INSERT Product;" + Product.CODE + "[unique=true];" + shirtSize.getQualifier() + ";"
				+ getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier() }))) + "\n";

		// 2. subtypes have same attributes in different columns
		// will fail
		lines = lines + "INSERT Product;" + Product.CODE + "[unique=true];" + shirtSize.getQualifier() + ";" + Product.UNIT + ";"
				+ shoeSize.getQualifier() + ";" + getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier(), Product.UNIT, shoeSize.getQualifier() }))) + "\n";

		// 3. subtypes have same attributes in different columns and exact type code
		// will pass
		lines = lines + "INSERT Product;" + Product.CODE + "[unique=true];" + shirtType.getCode() + "." + shirtSize.getQualifier()
				+ ";" + Product.UNIT + ";" + shoeType.getCode() + "." + shoeSize.getQualifier() + ";"
				+ getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier(), Product.UNIT, shoeSize.getQualifier() }))) + "\n";

		//	4. subtypes have same attributes in different columns and exact type code, and doubled attribute with different languages
		// will pass
		lines = lines + "INSERT Product;" + Product.CODE + "[unique=true];" + Product.NAME + "[lang=de];" + Product.NAME
				+ "[lang=en];" + shirtType.getCode() + "." + shirtSize.getQualifier() + ";" + Product.UNIT + ";" + shoeType.getCode()
				+ "." + shoeSize.getQualifier() + ";"
				+ getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier(), Product.UNIT, shoeSize.getQualifier(), Product.NAME }))) + "\n";

		// 5. subtypes have same attributes in different columns and exact type code, and doubled attribute with same languages
		// will fail
		lines = lines + "INSERT Product;" + Product.CODE + "[unique=true];" + Product.NAME + "[lang=de];" + Product.NAME
				+ "[lang=de];" + shirtType.getCode() + "." + shirtSize.getQualifier() + ";" + Product.UNIT + ";" + shoeType.getCode()
				+ "." + shoeSize.getQualifier() + ";"
				+ getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier(), Product.UNIT, shoeSize.getQualifier(), Product.NAME }))) + "\n";

		// 6. subtypes have same attributes in different columns and exact type code, and doubled attribute with different unique
		// will fail
		lines = lines + "INSERT Product;" + Product.CODE + "[unique=true];" + Product.CODE + ";" + shirtType.getCode() + "."
				+ shirtSize.getQualifier() + ";" + Product.UNIT + ";" + shoeType.getCode() + "." + shoeSize.getQualifier() + ";"
				+ getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier(), Product.UNIT, shoeSize.getQualifier(), Product.NAME }))) + "\n";

		//	7. subtypes have same attributes in different columns and exact type code, and doubled attribute with different unique
		// will pass
		lines = lines + "UPDATE Product;" + Product.CODE + "[unique=true];" + Product.CODE + ";" + shirtType.getCode() + "."
				+ shirtSize.getQualifier() + ";" + Product.UNIT + ";" + shoeType.getCode() + "." + shoeSize.getQualifier() + ";"
				+ getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier(), Product.UNIT, shoeSize.getQualifier(), Product.NAME }))) + "\n";

		// 8. subtypes have same attributes in different columns and exact type code, and doubled attribute with same unique
		// will fail
		lines = lines + "UPDATE Product;" + Product.CODE + "[unique=true];" + Product.CODE + ";" + Product.CODE + ";"
				+ shirtType.getCode() + "." + shirtSize.getQualifier() + ";" + Product.UNIT + ";" + shoeType.getCode() + "."
				+ shoeSize.getQualifier() + ";" + getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier(), Product.UNIT, shoeSize.getQualifier(), Product.NAME }))) + "\n";

		//	 9. subtypes have same attributes in different columns and exact type code, and different languages, but one doubled with unique
		// will pass
		lines = lines + "UPDATE Product;" + Product.CODE + "[unique=true];" + Product.NAME + "[lang=en];" + Product.NAME
				+ "[lang=de,unique=true];" + Product.NAME + "[lang=de];" + shirtType.getCode() + "." + shirtSize.getQualifier() + ";"
				+ Product.UNIT + ";" + shoeType.getCode() + "." + shoeSize.getQualifier() + ";"
				+ getOtherRequiredAttributes(composedType, new ArrayList(Arrays.asList(new String[]
				{ Product.CODE, shirtSize.getQualifier(), Product.UNIT, shoeSize.getQualifier(), Product.NAME }))) + "\n";

		//now test it
		reader = new ImpExReader(new CSVReader(lines), true);

		// 1. will fail
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

		//2. will fail
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

		//3. will pass
		try
		{
			reader.readLine();
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}

		// 4. will pass
		try
		{
			reader.readLine();
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}

		//5. will fail
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

		// 6. will fail
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

		// 7. will pass
		try
		{
			reader.readLine();
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}

		// 8. will fail
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

		// 9. will pass
		try
		{
			reader.readLine();
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
			fail();
		}
	}

	/**
	 * Gets semicolon separated string with all attributes of given type not listed in <code>alreadyGot</code>.
	 *
	 * @param composedType
	 *           type for which all attributes are needed
	 * @param alreadyGot
	 *           list of attributes which have to be excluded
	 * @return semicolon separated string of attributes
	 */
	protected String getOtherRequiredAttributes(final ComposedType composedType, final List alreadyGot)
	{
		String otherRequired = "";
		int index = 0;
		for (final Iterator iter = composedType.getAttributeDescriptors().iterator(); iter.hasNext();)
		{
			final AttributeDescriptor attributeDescriptor = (AttributeDescriptor) iter.next();
			if (!alreadyGot.contains(attributeDescriptor.getQualifier()) && !attributeDescriptor.isOptional()
					&& attributeDescriptor.isWritable() && attributeDescriptor.getDefaultValue(null) == null)
			{
				otherRequired += (index > 0 ? ";" : "") + attributeDescriptor.getQualifier();
				alreadyGot.add(attributeDescriptor.getQualifier());
				index++;
			}
		}
		return otherRequired;
	}

	/**
	 * Tests the header validation with different errors.
	 */
	@Test
	public void testHeaderErrors()
	{
		// unknown type
		try
		{
			new ImpExReader(new CSVReader(new StringReader("INSERT BlaBlah; juhu; trallala")), true).readLine();
			fail("HeaderValidationException expected");
		}
		catch (final HeaderValidationException e)
		{
			assertEquals(HeaderValidationException.MISSING_ITEMTYPE, e.getErrorCode());
		}
		catch (final Exception e)
		{
			fail("unexpected exception " + e.getMessage());
		}
		// no permitted types: try to insert products without code
		try
		{
			new ImpExReader(new CSVReader(new StringReader("INSERT Product; unit(code)")), true).readLine();
			fail("HeaderValidationException expected");
		}
		catch (final HeaderValidationException e)
		{
			assertEquals(HeaderValidationException.NO_PERMITTED_TYPES, e.getErrorCode());
		}
		catch (final Exception e)
		{
			fail("unexpected exception " + e);
		}
		// unknown attribute
		try
		{
			new ImpExReader(
					new CSVReader(new StringReader("INSERT Unit; " + Unit.CODE + ";" + Unit.UNITTYPE + "; jajaDasGibsNicht ")), true)
							.readLine();
			fail("HeaderValidationException expected");
		}
		catch (final HeaderValidationException e)
		{
			assertEquals(HeaderValidationException.UNKNOWN_ATTRIBUTE, e.getErrorCode());
		}
		catch (final Exception e)
		{
			fail("unexpected exception " + e);
		}

		// unknown attribute with language (see CORE-3310)
		if (!ExtensionManager.getInstance().getExtensionNames().contains("europe1"))
		{
			fail("europe1 seems not to be installed");
		}

		final String lines = "INSERT_UPDATE ProductTaxGroup;code[unique=true];xyz[lang=de];xyz[lang=en]\n"
				+ ";Tax_Full;voller Steuersatz;halber Steuersatz\n" + ";Tax_Half;full tax rate;half tax rate ";

		final ImpExReader impExReader = new ImpExReader(new CSVReader(lines), false);
		try
		{
			impExReader.readLine();
			fail("HeaderValidationException expected");
		}
		catch (final HeaderValidationException e)
		{
			assertEquals(HeaderValidationException.UNKNOWN_ATTRIBUTE, e.getErrorCode());
		}
		catch (final ImpExException e)
		{
			fail("got bug 3310 error : " + e.getMessage());
		}
		finally
		{
			try
			{
				impExReader.close();
			}
			catch (final Exception e)
			{
				fail(e.getMessage());
			}
		}

		// unknown attribute language
		try
		{
			C2LManager.getInstance().getLanguageByIsoCode("xyz");
			fail("unexpected language xyz found");
		}
		catch (final JaloItemNotFoundException e)
		{
			// fine
		}
		try
		{
			new ImpExReader(new CSVReader(
					new StringReader("INSERT Unit; " + Unit.CODE + ";" + Unit.UNITTYPE + ";" + Unit.NAME + "[lang=xyz]")), true)
							.readLine();
		}
		catch (final HeaderValidationException e)
		{
			fail("HeaderValidationException should not be thrown any more (allowing unknown language codes)");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail("unexpected exception " + e);
		}
		// duplicate attributes
		try
		{
			new ImpExReader(new CSVReader(new StringReader("INSERT Unit; " + Unit.CODE + ";" + Unit.UNITTYPE + ";" + Unit.CODE)),
					true).readLine();
			fail("HeaderValidationException expected");
		}
		catch (final HeaderValidationException e)
		{
			assertEquals(HeaderValidationException.AMBIGUOUS_SIBLING_ATTRIBUTE, e.getErrorCode());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail("unexpected exception " + e);
		}
		// unsupported attribute type: SavedQuery.params::Map{String->Type}
		try
		{
			new ImpExReader(new CSVReader(
					new StringReader("INSERT " + TypeManager.getInstance().getComposedType(SavedQuery.class).getCode() + ";"
							+ SavedQuery.CODE + ";" + SavedQuery.QUERY + ";" + SavedQuery.PARAMS + ";" + SavedQuery.RESULTTYPE + ";")),
					true).readLine();
		}
		catch (final HeaderValidationException e)
		{
			fail("HeaderValidationException(UNSUPPORTED_ATTRIBUTE_TYPE) should not be thrown any more (support for MapTypes added)");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail("unexpected exception " + e);
		}
		// item reference errors: reference attribute unknown
		try
		{
			new ImpExReader(new CSVReader(
					new StringReader("INSERT Country;" + Country.ISOCODE + ";regions(" + Region.ISOCODE + ",blahBlubb)")), true)
							.readLine();
			fail("HeaderValidationException expected");
		}
		catch (final HeaderValidationException e)
		{
			assertEquals(HeaderValidationException.UNKNOWN_ATTRIBUTE, e.getErrorCode());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail("unexpected exception " + e);
		}
		// XXX: test resolving by unsupported attribute types, non-searchable attributes etc
	}

	/**
	 * Checks if header lines are detected correct. See PLA-5115.
	 */
	@Test
	public void testHeaderDetection()
	{
		final String line =
				// 1. header
				"INSERT       RemoveItemsJob;code\n"
						// 2. ValueLine
						+ "INSERT  ;code\n"
						// 3. ValueLine
						+ "INSERTRemoveItemsJob;code\n"
						// 4. Header
						+ "   INSERT       RemoveItemsJob;code\n"
						// 5. ValueLine
						+ "  remove\n"
						//	6. Header
						+ "INSERT       RemoveItemsJob[a=b];code\n"
						// 7. header
						+ "INSERT RemoveItemsJob;code\n";

		try
		{
			final ImpExReader reader = new ImpExReader(new CSVReader(line), false);
			// 1. header
			Object object = reader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof HeaderDescriptor);
			//	 2. ValueLine
			object = reader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof ValueLine);
			//	 3. ValueLine
			object = reader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof ValueLine);
			//	 4. header
			object = reader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof HeaderDescriptor);
			//	 5. ValueLine
			object = reader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof ValueLine);
			//	 6. header
			object = reader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof HeaderDescriptor);
			//  7. header
			object = reader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof HeaderDescriptor);
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the correct parsing of headers using ImpExReader.
	 */
	@Test
	public void testHeaderParsing()
	{
		final ComposedType composedType = TypeManager.getInstance().getComposedType(Product.class);
		final String line = "INSERT " + composedType.getCode() + ";" + Product.CODE + "[ unique= true];" + Product.NAME + "[lang="
				+ german.getIsoCode() + "];" + Product.NAME + "[lang=" + english.getIsoCode() + "];" + Product.UNIT + "(" + Unit.CODE
				+ ");" + Product.DESCRIPTION + ";" + Product.THUMBNAIL + "(" + Media.CODE + "," + Media.REALFILENAME + ")" + ";"
				+ "catalogVersion[allowNull=true]";
		// append other required columns
		final List expected = new ArrayList(Arrays.asList(new String[]
		{ Product.CODE, Product.NAME, Product.NAME, Product.UNIT, Product.DESCRIPTION, Product.THUMBNAIL, "catalogVersion" }));
		//final String otherRequired = getOtherRequiredAttributes( t, expected );
		ImpExReader reader = null;
		Object object1 = null, object2 = null, object3 = null;
		try
		{
			reader = new ImpExReader(new CSVReader("\n" + "# some comments - should be ignored\n" + "\n" + line + ";" + "\n" + "\n"
					+ "; CodeCode ; Name_de; Name_en; pieces; ; \n" + "\n" + "# juhu - we're done"), false);
			object1 = reader.readLine(); // should read header directly - empty lines and comments are ignored
			object2 = reader.readLine(); // should read value line
			object3 = reader.readLine(); // must be null due to end of file
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		assertTrue("no instance of " + HeaderDescriptor.class.getName(), object1 instanceof HeaderDescriptor);
		assertTrue("no instance of " + ValueLine.class.getName(), object2 instanceof ValueLine);
		assertNull(object3);

		final HeaderDescriptor header = (HeaderDescriptor) object1;
		assertTrue(header.isInsertMode());
		assertEquals(EnumerationManager.getInstance().getEnumerationValue(ImpExConstants.TC.IMPEXPROCESSMODEENUM,
				ImpExConstants.Enumerations.ImpExProcessModeEnum.INSERT), header.getMode());
		assertEquals(composedType, header.getConfiguredComposedType());
		assertEquals(expected, header.getAllColumnQualifiers());

		// code
		List<AbstractColumnDescriptor> cols = new ArrayList(header.getColumnsByQualifier(Product.CODE));
		assertEquals(1, cols.size());
		StandardColumnDescriptor columnDescriptor = (StandardColumnDescriptor) cols.iterator().next();
		checkColumnDescriptor(columnDescriptor, header, Product.CODE, 1,
				composedType.getAttributeDescriptorIncludingPrivate(Product.CODE), false, false, false, false, true, true, null,
				Collections.singletonMap("unique", "true"));
		assertNull(((AbstractDescriptor.ColumnParams) columnDescriptor.getDescriptorData()).getItemPatternLists());

		// name (de + en)
		cols = new ArrayList(header.getColumnsByQualifier(Product.NAME));
		assertEquals(2, cols.size());
		// name de
		columnDescriptor = (StandardColumnDescriptor) cols.get(0);
		Map<String, String> modifiers = new HashMap();
		modifiers.put("lang", german.getIsoCode());
		checkColumnDescriptor(columnDescriptor, header, Product.NAME, 2,
				composedType.getAttributeDescriptorIncludingPrivate(Product.NAME), true, false, false, false, false, false,
				german.getIsoCode(), modifiers);
		assertNull(((AbstractDescriptor.ColumnParams) columnDescriptor.getDescriptorData()).getItemPatternLists());
		// name en
		columnDescriptor = (StandardColumnDescriptor) cols.get(1);
		modifiers = new HashMap();
		modifiers.put("lang", english.getIsoCode());
		checkColumnDescriptor(columnDescriptor, header, Product.NAME, 3,
				composedType.getAttributeDescriptorIncludingPrivate(Product.NAME), true, false, false, false, false, false,
				english.getIsoCode(), modifiers);
		assertNull(((AbstractDescriptor.ColumnParams) columnDescriptor.getDescriptorData()).getItemPatternLists());

		// unit(code)
		cols = new ArrayList(header.getColumnsByQualifier(Product.UNIT));
		assertEquals(1, cols.size());
		columnDescriptor = (StandardColumnDescriptor) cols.iterator().next();
		checkColumnDescriptor(columnDescriptor, header, Product.UNIT, 4,
				composedType.getAttributeDescriptorIncludingPrivate(Product.UNIT), false, false, false, false, false, false, null,
				Collections.EMPTY_MAP);
		List<ColumnParams>[] patternLists = ((AbstractDescriptor.ColumnParams) columnDescriptor.getDescriptorData())
				.getItemPatternLists();
		assertNotNull(patternLists);
		assertEquals(1, patternLists.length);
		assertEquals(1, patternLists[0].size());
		AbstractDescriptor.ColumnParams patternElement = patternLists[0].get(0);
		assertEquals(Unit.CODE, patternElement.getQualifier());
		assertEquals(Collections.EMPTY_MAP, patternElement.getModifiers());
		assertNull(patternElement.getItemPatternLists());

		// description (no lang iso!)
		cols = new ArrayList(header.getColumnsByQualifier(Product.DESCRIPTION));
		assertEquals(1, cols.size());
		columnDescriptor = (StandardColumnDescriptor) cols.get(0);
		checkColumnDescriptor(columnDescriptor, header, Product.DESCRIPTION, 5,
				composedType.getAttributeDescriptorIncludingPrivate(Product.DESCRIPTION), true, false, false, false, false, false,
				null, Collections.EMPTY_MAP);
		assertNull(((AbstractDescriptor.ColumnParams) columnDescriptor.getDescriptorData()).getItemPatternLists());
		// thumbnail (code,realfilenam)
		cols = new ArrayList(header.getColumnsByQualifier(Product.THUMBNAIL));
		assertEquals(1, cols.size());
		columnDescriptor = (StandardColumnDescriptor) cols.get(0);
		checkColumnDescriptor(columnDescriptor, header, Product.THUMBNAIL, 6,
				composedType.getAttributeDescriptorIncludingPrivate(Product.THUMBNAIL), false, false, false, false, false, false,
				null, Collections.EMPTY_MAP);
		patternLists = ((AbstractDescriptor.ColumnParams) columnDescriptor.getDescriptorData()).getItemPatternLists();
		assertNotNull(patternLists);
		assertEquals(1, patternLists.length);
		assertEquals(2, patternLists[0].size()); // code, realfilename
		patternElement = patternLists[0].get(0); // code
		assertEquals(Media.CODE, patternElement.getQualifier());
		assertEquals(Collections.EMPTY_MAP, patternElement.getModifiers());
		assertNull(patternElement.getItemPatternLists());
		patternElement = patternLists[0].get(1); // realfilename
		assertEquals(Media.REALFILENAME, patternElement.getQualifier());
		assertEquals(Collections.EMPTY_MAP, patternElement.getModifiers());
		assertNull(patternElement.getItemPatternLists());
	}

	/**
	 * Tests if inserts and updates of abstract types are handled correct.<br>
	 * See PLA-5106.
	 */
	@Test
	public void testAbstractInsertUpdate()
	{
		final String lines =
				// 1. will fail
				"INSERT Job;" + Job.CODE + "[unique=true];\n" + ";testjob;\n"
				// 2. will fail
						+ "INSERT Job;" + Job.CODE + "[unique=true];" + BatchJob.STEPS + "\n" + ";testjob;bla\n"
						// 3. will fail
						+ "INSERT_UPDATE Job;" + Job.CODE + "[unique=true];" + BatchJob.STEPS + "\n" + ";testjob;bla\n"
						// 4. will pass
						+ "INSERT Job;" + Job.CODE + "[unique=true];" + BatchJob.STEPS + "\n" //
						+ "RemoveItemsJob;testjob1;bla\n" //
						+ "TypeSystemExportJob;testjob2;bla\n" //
						// 5. will pass
						+ "UPDATE Job;" + Job.CODE + "[unique=true];" + Job.CODE + "\n" //
						+ ";testjob1;testjob3\n" //
						+ ";testjob2;testjob4\n" //
						//	6. will pass
						+ "UPDATE Job;" + Job.CODE + "[unique=true];" + BatchJob.STEPS + ";" + RemoveItemsJob.CODE + "\n"
						+ ";testjob3;bla;testjob5\n" + ";testjob4;bla;testjob6\n";

		final ImpExImportReader reader = new ImpExImportReader(new CSVReader(lines));
		//	 1. will fail
		try
		{
			reader.readLine();
			fail("Has to throw an exception, because you can not create an item of an abstract type");
		}
		catch (final ImpExException e)
		{
			//OK
		}
		//	2. will fail
		try
		{
			reader.readLine();
			fail("Has to throw an exception, because you can not create an item of an abstract type");
		}
		catch (final ImpExException e)
		{
			//OK
		}
		// 3. will fail
		try
		{
			reader.readLine();
			fail("Has to throw an exception, because you can not create an item of an abstract type");
		}
		catch (final ImpExException e)
		{
			//OK
		}
		//	4. will pass
		RemoveItemsJob rjob = null;
		TypeSystemExportJob sjob = null;
		try
		{
			rjob = (RemoveItemsJob) reader.readLine();
			assertNotNull(rjob);
			assertEquals("testjob1", rjob.getCode());
			sjob = (TypeSystemExportJob) reader.readLine();
			assertNotNull(sjob);
			assertEquals("testjob2", sjob.getCode());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		// 5. will pass
		try
		{
			RemoveItemsJob rjob2 = null;
			TypeSystemExportJob sjob2 = null;
			rjob2 = (RemoveItemsJob) reader.readLine();
			assertNotNull(rjob2);
			assertEquals(rjob, rjob2);
			assertEquals("testjob3", rjob.getCode());
			sjob2 = (TypeSystemExportJob) reader.readLine();
			assertNotNull(sjob2);
			assertEquals(sjob, sjob2);
			assertEquals("testjob4", sjob.getCode());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		// 6. will pass
		try
		{
			RemoveItemsJob rjob2 = null;
			rjob2 = (RemoveItemsJob) reader.readLine();
			assertNotNull(rjob2);
			assertEquals(rjob, rjob2);
			assertEquals("testjob5", rjob.getCode());
			TypeSystemExportJob sjob2 = null;
			sjob2 = (TypeSystemExportJob) reader.readLine();
			assertNotNull(sjob2);
			assertEquals(sjob, sjob2);
			assertEquals("testjob6", sjob.getCode());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		try
		{
			assertNull(reader.readLine());
			assertEquals(3, reader.getDumpedLineCount());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the usage of subtype references in header descriptor.
	 */
	@Test
	public void testSubtypeReferenceExpression()
	{
		String lines = "INSERT Customer; " + Customer.UID + "[unique=true]\n" + "; axel \n" + "# asdlasjdlk \n" + "INSERT Title; "
				+ Title.OWNER + "(Customer." + Customer.UID + ");" + Title.CODE + "\n" + "; axel ; Dipl Inf \n";

		Customer axel;
		Title title1;

		ImpExImportReader impExImportReader = new ImpExImportReader(lines);

		try
		{
			axel = (Customer) impExImportReader.readLine();
			assertEquals("axel", axel.getUID());

			title1 = (Title) impExImportReader.readLine();
			assertEquals(axel, title1.getOwner());
			assertEquals("Dipl Inf", title1.getCode());

			assertNull(impExImportReader.readLine());
			assertEquals(0, impExImportReader.getDumpedLineCount());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		finally
		{
			try
			{
				impExImportReader.close();
			}
			catch (final IOException e)
			{
				fail(e.getMessage());
			}
		}

		// PLA-5011
		PrincipalGroup group = null;
		Principal user = null;
		try
		{
			group = UserManager.getInstance().createUserGroup("testgroup");
			user = UserManager.getInstance().createUser("testuser");
			user.addToGroup(group);
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}

		lines = "INSERT_UPDATE PRINCIPALGROUP ; uid[unique=true]; members( groups( uid ) )\n" + "; testgroup ; testgroup";

		Object object;
		try
		{
			impExImportReader = new ImpExImportReader(lines);
			object = impExImportReader.readLine();
			assertEquals(group, object);
			assertEquals(user, group.getMembers().iterator().next());
			assertEquals(1, group.getMembers().size());
		}
		catch (final ImpExException e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}

		lines = "INSERT_UPDATE PRINCIPALGROUP ; uid[unique=true]; members( groups( Company.buyer ) )\n" + "; testgroup ; true";

		try
		{
			impExImportReader = new ImpExImportReader(lines);
			object = impExImportReader.readLine();
			assertEquals(group, object);
			assertEquals(1, group.getMembers().size());
		}
		catch (final ImpExException e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the <code>afterEach</code> bean shell marker.
	 */
	@Test
	public void testBeanShellAfterEach()
	{
		final Language before = jaloSession.getSessionContext().getLanguage();
		try
		{
			jaloSession.getSessionContext().setLanguage(german);

			final String afterEachCode = "lastItem.setName( lastItem.getCode() )";
			final ImpExImportReader impExImportReader = new ImpExImportReader("INSERT Title; code[unique=true]; name[lang="
					+ german.getIsoCode() + "] \n" + "; t1 ; name1 \n" + "#% " + ImpExConstants.Syntax.CodeMarkers.AFTER_EACH + " "
					+ afterEachCode + "\n" + "; t2 ; name2 \n" + "; t3 ; name3 \n" + "; t4 ; name4 \n" + "#% "
					+ ImpExConstants.Syntax.CodeMarkers.AFTER_EACH_END + "\n" + "; t5 ; name5 \n");
			impExImportReader.enableCodeExecution(true);
			impExImportReader.enableExternalCodeExecution(false);
			assertNull(impExImportReader.getAfterEachCode());


			Title title = (Title) impExImportReader.readLine();

			assertNull(impExImportReader.getAfterEachCode());
			assertNotNull(title);
			assertEquals("t1", title.getCode());
			assertEquals("name1", title.getName());

			title = (Title) impExImportReader.readLine();
			assertEquals(afterEachCode, impExImportReader.getAfterEachCode().getExecutableCode());
			assertNotNull(title);
			assertEquals("t2", title.getCode());
			assertEquals(title.getCode(), title.getName());

			title = (Title) impExImportReader.readLine();
			assertEquals(afterEachCode, impExImportReader.getAfterEachCode().getExecutableCode());
			assertNotNull(title);
			assertEquals("t3", title.getCode());
			assertEquals(title.getCode(), title.getName());

			title = (Title) impExImportReader.readLine();
			assertEquals(afterEachCode, impExImportReader.getAfterEachCode().getExecutableCode());
			assertNotNull(title);
			assertEquals("t4", title.getCode());
			assertEquals(title.getCode(), title.getName());

			title = (Title) impExImportReader.readLine();
			assertNull(impExImportReader.getAfterEachCode());
			assertNotNull(title);
			assertEquals("t5", title.getCode());
			assertEquals("name5", title.getName());

			assertNull(impExImportReader.readLine());
			assertNull(impExImportReader.getAfterEachCode());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		finally
		{
			jaloSession.getSessionContext().setLanguage(before);
		}
	}

	/**
	 * Tests the usage of bean shell.
	 */
	@Test
	public void testBeanShell()
	{
		ImpExImportReader impExImportReader = new ImpExImportReader(
				"\"#% \n" + "impex.info(\"\"current header = \"\"+impex.getCurrentHeader() ); \n"
						+ "impex.info(\"\"dumped line count = \"\"+impex.getDumpedLineCount() ); \n"
						+ "impex.info(\"\"last item = \"\"+impex.getLastImportedItem() ); \n"
						+ "impex.info(\"\"last item line number = \"\"+impex.getLastImportedItemLineNumber());\"\n");
		impExImportReader.enableCodeExecution(true);
		try
		{
			assertNull(impExImportReader.readLine());
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		impExImportReader = new ImpExImportReader("\"*% impex.info(\"\"current header = \"\"+impex.getCurrentHeader() ); \"\n"
				+ "\"*% impex.info(\"\"dumped line count = \"\"+impex.getDumpedLineCount() ); \"\n"
				+ "\"$% impex.info(\"\"last item = \"\"+impex.getLastImportedItem() ); \"\n"
				+ "\"$% impex.info(\"\"last item line number = \"\"+impex.getLastImportedItemLineNumber());\"\n");
		impExImportReader.enableCodeExecution(true);
		impExImportReader.getCSVReader().setCommentOut(new char[]
		{ '*', '$' });
		try
		{
			assertNull(impExImportReader.readLine());
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		impExImportReader = new ImpExImportReader("\"#% impex.info(\"\"current header = \"\"+impex.getCurrentHeader() ); \"\n");
		impExImportReader.enableCodeExecution(true);
		impExImportReader.getCSVReader().setCommentOut(new char[]
		{ '*', '$' });
		impExImportReader.setInvalidHeaderPolicy(InvalidHeaderPolicy.THROW_EXCEPTION);
		try
		{
			impExImportReader.readLine();
			fail();
		}
		catch (final ImpExException e)
		{
			//OK
		}
	}

	/**
	 * Tests the bean shell <code>if</code> condition.
	 */
	@Test
	public void testBeanShellIf()
	{
		final String script = "#%import de.hybris.platform.jalo.product.ProductManager;\n"
				+ "INSERT_UPDATE Unit; unitType[unique=true]; code \n" + "#%if: 5 == 5; \n"
				+ "  #%ProductManager.getInstance().createUnit( \"unit1\", \"u1\" ); \n" + "  ; unit2; u2 ;\n" + "  #%if: 5==4 \n"
				+ "    #%ProductManager.getInstance().createUnit( \"unit3\", \"u3\" ); \n" + "    ; unit4 ; u4 ;\n"
				+ "    #%if: 4==4 \n" + "      #%ProductManager.getInstance().createUnit( \"unit5\", \"u5\" ); \n"
				+ "      ; unit6 ; u6 ;\n" + "    #%endif:\n" + "  #%endif: \n"
				+ "  #%ProductManager.getInstance().createUnit( \"unit7\", \"u7\" ); \n" + "  ; unit8 ; u8 ;\n";

		final ImpExImportReader impExImportReader = new ImpExImportReader(script);
		impExImportReader.enableCodeExecution(true);
		for (int i = 0; i < 15; i++)
		{
			try
			{
				impExImportReader.readLine();
			}
			catch (final ImpExException e)
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
		}

		Collection<Unit> col = ProductManager.getInstance().getUnitsByUnitType("unit1");
		assertEquals(1, col.size());
		assertNotNull(col.toArray()[0]);
		col = ProductManager.getInstance().getUnitsByUnitType("unit2");
		assertEquals(1, col.size());
		assertNotNull(col.toArray()[0]);
		col = ProductManager.getInstance().getUnitsByUnitType("unit3");
		assertEquals(0, col.size());
		col = ProductManager.getInstance().getUnitsByUnitType("unit4");
		assertEquals(0, col.size());
		col = ProductManager.getInstance().getUnitsByUnitType("unit5");
		assertEquals(0, col.size());
		col = ProductManager.getInstance().getUnitsByUnitType("unit6");
		assertEquals(0, col.size());
		col = ProductManager.getInstance().getUnitsByUnitType("unit7");
		assertEquals(1, col.size());
		assertNotNull(col.toArray()[0]);
		col = ProductManager.getInstance().getUnitsByUnitType("unit8");
		assertEquals(1, col.size());
		assertNotNull(col.toArray()[0]);
	}

	/**
	 * Tests the <code>includeExternalData</code> statement of bean shell.
	 */
	@Test
	public void testBeanShellIncludeExternaDataChained()
	{
		try
		{
			final File mainScript = File.createTempFile("impextest", ".impex");
			final File subScript = File.createTempFile("impextest", ".impex");
			final File subSubScript = File.createTempFile("impextest", ".impex");

			PrintWriter writer = new PrintWriter(mainScript);
			writer.println("INSERT Title; " + Title.CODE + "; " + Title.NAME + "[lang=" + german.getIsoCode() + "]");
			writer.println(";t1;name1");
			writer.println("\"#%impex.includeExternalData(\"\"" + subScript.getAbsolutePath().replace("\\", "\\\\")
					+ "\"\",\"\"windows-1252\"\",0)\"");
			writer.close();

			writer = new PrintWriter(subScript);
			writer.println("t2;name2");
			writer.println("\"#%impex.includeExternalData(\"\"" + subSubScript.getAbsolutePath().replace("\\", "\\\\")
					+ "\"\",\"\"windows-1252\"\",0)\"");
			writer.close();

			writer = new PrintWriter(subSubScript);
			writer.println("t3;name3");
			writer.close();

			final ImpExImportReader ir1 = new ImpExImportReader(new CSVReader(mainScript, "windows-1252"));
			ir1.enableCodeExecution(true);
			ir1.enableExternalSyntaxParsing(true);
			ir1.enableExternalCodeExecution(true);

			Title title;
			final SessionContext deCtx = jaloSession.createSessionContext();
			deCtx.setLanguage(german);

			title = (Title) ir1.readLine();
			assertNotNull(title);
			assertEquals("t1", title.getCode());
			assertEquals("name1", title.getName(deCtx));

			title = (Title) ir1.readLine();
			assertNotNull(title);
			assertEquals("t2", title.getCode());
			assertEquals("name2", title.getName(deCtx));

			title = (Title) ir1.readLine();
			assertNotNull(title);
			assertEquals("t3", title.getCode());
			assertEquals("name3", title.getName(deCtx));

			assertNull(ir1.readLine());

			ir1.close();

			if (!mainScript.delete())
			{
				fail("Can not delete " + mainScript.getAbsolutePath());
			}
			if (!subScript.delete())
			{
				fail("Can not delete " + subScript.getAbsolutePath());
			}
			if (!subSubScript.delete())
			{
				fail("Can not delete " + subSubScript.getAbsolutePath());
			}
		}
		catch (final ImpExException e)
		{
			fail("error:" + e.getMessage());
		}
		catch (final UnsupportedEncodingException e)
		{
			fail("error:" + e.getMessage());
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testIncludeCSVFromProperties()
	{
		//	This is how one could include properties based data as CSV so that
		// ImpEx can use it for localized attributes:
		//
		//		final Reader reader = new FileReader("foo/bar.props");
		//		final CSVFromPropertiesReader example = CSVFromPropertiesReader.builder(reader).withColumn("name", 1).withColumn("description", 2).build();
		//
		// The props -> csv reader will match properties like <id>.<attribute>=<value>. The resulting csv line looks like this:
		// <id> ; <value> ; <value> ; <value>
		//

		final int itemCount = 10;

		ImpExImportReader ir = null;
		File deProps = null;
		File enProps = null;
		try
		{

			enProps = File.createTempFile("locales_en", ".properties");
			deProps = File.createTempFile("locales_de", ".properties");

			// XYZ_1.name=name-de-1
			// XYZ_1.description=description-de-1
			// XYZ_2.name=name-de-2
			// XYZ_2.description=description-de-2
			// ... and so on ...
			try (PrintWriter deWR = new PrintWriter(deProps); PrintWriter enWR = new PrintWriter(enProps))
			{
				for (int i = 0; i < itemCount; i++)
				{
					deWR.println("XYZ_" + i + ".name=name-de-" + i);
					deWR.println("XYZ_" + i + ".description=description-de-" + i);

					enWR.println("XYZ_" + i + ".name=name-en-" + i);
					enWR.println("XYZ_" + i + ".description=description-en-" + i);
				}
			}

			final String deCSVReaderCommand = CSVFromPropertiesReader.class.getName() + ".builder(" + //
					"new " + FileReader.class.getName() + "(" + //
					"\"" + deProps.getAbsolutePath().replace("\\", "\\\\") + "\")" + //
					").withColumn(\"name\", 1).withColumn(\"description\", 2).build()";


			final String enCSVReaderCommand = CSVFromPropertiesReader.class.getName() + ".builder(" + //
					"new " + FileReader.class.getName() + "(" + //
					"\"" + enProps.getAbsolutePath().replace("\\", "\\\\") + "\")" + //
					").withColumn(\"name\", 1).withColumn(\"description\", 2).build()";


			ir = new ImpExImportReader(new CSVReader(//
					"INSERT_UPDATE DeliveryMode; code[unique=true]; name[lang=de]; description[lang=de] \n" + //
							"\"#%impex.includeExternalData( " + CSVUtils.escapeString(deCSVReaderCommand, new char[]
							{ '"' }, true) + " );\"\n" + //
							"\n" + //
							"INSERT_UPDATE DeliveryMode; code[unique=true]; name[lang=en]; description[lang=en] \n" + //
							"\"#%impex.includeExternalData( " + CSVUtils.escapeString(enCSVReaderCommand, new char[]
							{ '"' }, true) + " );\"\n" + //
							"\n"));

			ir.enableCodeExecution(true);


			final SessionContext deCtx = jaloSession.createSessionContext();
			deCtx.setLanguage(getOrCreateLanguage("de"));

			final SessionContext enCtx = jaloSession.createSessionContext();
			enCtx.setLanguage(getOrCreateLanguage("en"));

			final Set<DeliveryMode> imported = new HashSet<>();

			// 1. creating items + setting DE attributes
			for (int i = 0; i < itemCount; i++)
			{
				final DeliveryMode item = (DeliveryMode) ir.readLine();
				assertNotNull(item);
				assertFalse(imported.contains(item));
				assertEquals("XYZ_" + i, item.getCode());
				assertEquals("name-de-" + i, item.getName(deCtx));
				assertNull(item.getName(enCtx));
				assertEquals("description-de-" + i, item.getDescription(deCtx));
				assertNull(item.getDescription(enCtx));
				imported.add(item);
			}

			// 2. updating items with EN attributes
			for (int i = 0; i < itemCount; i++)
			{
				final DeliveryMode item = (DeliveryMode) ir.readLine();
				assertNotNull(item);
				assertTrue(imported.contains(item));
				assertEquals("XYZ_" + i, item.getCode());
				assertEquals("name-de-" + i, item.getName(deCtx));
				assertEquals("name-en-" + i, item.getName(enCtx));
				assertEquals("description-de-" + i, item.getDescription(deCtx));
				assertEquals("description-en-" + i, item.getDescription(enCtx));
				imported.add(item);
			}

			assertNull(ir.readLine());
		}
		catch (final IOException e)
		{
			fail("unpexted io error " + e);
		}
		catch (final ImpExException e)
		{
			fail("unpexted impex error " + e);
		}
		finally
		{
			ImpExUtils.closeQuietly(ir);
			FileUtils.deleteQuietly(deProps);
			FileUtils.deleteQuietly(enProps);
		}
	}

	@Test
	public void testIncludeCSVFromPropertiesIgnoreMissingValues() throws ConsistencyCheckException
	{
		//
		// tests whether missing properties are correctly leading to <ignore> values in generated
		// CSV lines --> means that imported items must not be changed for these attributes, which
		// is what we test here
		//

		ImpExImportReader ir = null;
		File props = null;
		try
		{
			final SessionContext deCtx = jaloSession.createSessionContext();
			deCtx.setLanguage(getOrCreateLanguage("de"));

			final String CODE = "TestItem";
			final DeliveryMode item = OrderManager.getInstance().createDeliveryMode(null, CODE);
			item.setName(deCtx, "name-before-de");
			item.setDescription(deCtx, "description-before-de");

			// change 'name' but leave 'description' untouched
			props = File.createTempFile("locales_de", ".properties");
			try (PrintWriter wr = new PrintWriter(props);)
			{
				wr.println(CODE + ".name=name-after-de");
			}

			final String deCSVReaderCommand = CSVFromPropertiesReader.class.getName() + ".builder(" + //
					"new " + FileReader.class.getName() + "(" + //
					"\"" + props.getAbsolutePath().replace("\\", "\\\\") + "\")" + //
					").withColumn(\"name\", 1).withColumn(\"description\", 2).build()";


			ir = new ImpExImportReader(new CSVReader(//
					"INSERT_UPDATE DeliveryMode; code[unique=true]; name[lang=de]; description[lang=de] \n" + //
							"\"#%impex.includeExternalData( " + CSVUtils.escapeString(deCSVReaderCommand, new char[]
							{ '"' }, true) + " );\"\n" //
			));

			ir.enableCodeExecution(true);

			final DeliveryMode itemAgain = (DeliveryMode) ir.readLine();
			assertNotNull("no item returned from impex", item);
			assertEquals("wrong code - changed during import ?", CODE, itemAgain.getCode());
			assertEquals("name wasn't changed to properties value", "name-after-de", itemAgain.getName(deCtx));
			assertEquals("description was changed but shouldn't", "description-before-de", itemAgain.getDescription(deCtx));

			assertNull(ir.readLine());
		}
		catch (final IOException e)
		{
			fail("unpexted io error " + e);
		}
		catch (final ImpExException e)
		{
			fail("unpexted impex error " + e);
		}
		finally
		{
			ImpExUtils.closeQuietly(ir);
			FileUtils.deleteQuietly(props);
		}
	}

	/**
	 * Test the {@link ReaderManager} class.
	 */
	@Test
	public void testReaderManager()
	{
		final CSVReader mainReader = new CSVReader("Test line 0");
		final ReaderManager manager = new ReaderManager(mainReader);
		assertNotNull(manager.peekReader());
		assertEquals(0, manager.getCurrentColumnOffset());
		assertNotNull(manager.getCurrentLocation());
		for (int i = 1; i <= 10; i++)
		{
			manager.pushReader(new CSVReader("Test line " + i), i, "reader " + i);
		}
		assertEquals(mainReader, manager.getMainReader());
		assertEquals(11, manager.readerCount());
		for (int i = 10; i > 0; i--)
		{
			assertEquals(i, manager.getCurrentColumnOffset());
			assertTrue(manager.popReader().isFinished());
		}
		assertEquals(mainReader, manager.getMainReader());
		assertEquals(mainReader, manager.peekReader());
		manager.clear();
		assertNull(manager.peekReader());
	}

	/**
	 * Tests the including of external data in chained case.
	 */
	@Test
	public void testBeanShellIncludeExternaData()
	{
		final ImpExImportReader ir1 = new ImpExImportReader(
				"INSERT Title; " + Title.CODE + "[unique=true]; " + Title.NAME + "[lang=" + german.getIsoCode() + "]\n" + "\"#%\n"
						+ "impex.includeExternalData( \n" + "	new de.hybris.platform.util.CSVReader( \n"
						+ "		\"\"t1; name1 \\n\"\"+ \n" + "		\"\"t2; name2 \\n\"\" \n" + "	) \n" + ")\"\n" + "; t3; name3; \n"
						+ "# some ordinary comment \n" + "INSERT UserRight; " + UserRight.CODE + "[unique=true];\n" + "; r0;\n "
						+ "\"#%\n" + "impex.includeExternalData( \n" + "	new de.hybris.platform.util.CSVReader( \n"
						+ "		\"\";;r1\\n\"\"+ \n" + "		\"\";;r2\\n\"\" \n" + "	), 1 \n" + ")\"\n");
		ir1.enableCodeExecution(true);

		final ImpExImportReader ir2 = new ImpExImportReader(
				"INSERT Title; " + Title.CODE + "[unique=true]; " + Title.NAME + "[lang=" + german.getIsoCode() + "]\n" + "\"#%\n"
						+ "impex.includeExternalData( \n" + "	new de.hybris.platform.util.CSVReader( \n"
						+ "		\"\"t4; name4 \\n\"\"+ \n" + "		\"\"t5; name5 \\n\"\" \n" + "	) \n" + ")\"\n" + "; t6; name6; \n"
						+ "# some ordinary comment \n" + "INSERT UserRight; " + UserRight.CODE + "[unique=true];\n" + "; r3;\n "
						+ "\"#%\n" + "impex.includeExternalData( \n" + "	new de.hybris.platform.util.CSVReader( \n"
						+ "		\"\";;r4\\n\"\"+ \n" + "		\"\";;r5\\n\"\" \n" + "	), 1 \n" + ")\"\n");
		ir2.enableCodeExecution(false);

		final ImpExImportReader ir3 = new ImpExImportReader(
				"INSERT Title; " + Title.CODE + "[unique=true]; " + Title.NAME + "[lang=" + german.getIsoCode() + "]\n" + "\"#%\n"
						+ "impex.includeExternalData( \n" + "	new de.hybris.platform.util.CSVReader( \n"
						+ "		\"\";t8; name8 \\n\"\"+ \n" + "		\"\"#% impex.enableExternalDataCodeExecution( true ); \\n\"\" \n" // try to enable external code execution by externally
						+ "	),0 \n" + ")\"\n" + "; t7; name7; \n");
		ir3.enableCodeExecution(true);
		ir3.enableExternalSyntaxParsing(true);
		ir3.enableExternalCodeExecution(false);

		final ImpExImportReader ir4 = new ImpExImportReader(
				"INSERT Title; " + Title.CODE + "[unique=true]; " + Title.NAME + "[lang=" + german.getIsoCode() + "]\n" + "\"#%\n"
						+ "impex.includeExternalData( \n" + "	new de.hybris.platform.util.CSVReader( \n"
						+ "		\"\";t10; name10 \\n\"\"+ \n" + "		\"\"#% impex.enableExternalCodeExecution( false ); \\n\"\" \n" // try to disable external code execution by externally
						+ "	),0 \n" + ")\"\n" + "; t9; name9; \n");
		ir4.enableCodeExecution(true);
		ir4.enableExternalSyntaxParsing(true);
		ir4.enableExternalCodeExecution(true);

		Title title;
		UserRight userRight;
		final SessionContext deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(german);
		try
		{
			title = (Title) ir1.readLine();
			assertNotNull(title);
			assertEquals("t1", title.getCode());
			assertEquals("name1", title.getName(deCtx));

			title = (Title) ir1.readLine();
			assertNotNull(title);
			assertEquals("t2", title.getCode());
			assertEquals("name2", title.getName(deCtx));

			title = (Title) ir1.readLine();
			assertNotNull(title);
			assertEquals("t3", title.getCode());
			assertEquals("name3", title.getName(deCtx));

			userRight = (UserRight) ir1.readLine();
			assertNotNull(userRight);
			assertEquals("r0", userRight.getCode());

			userRight = (UserRight) ir1.readLine();
			assertNotNull(userRight);
			assertEquals("r1", userRight.getCode());

			userRight = (UserRight) ir1.readLine();
			assertNotNull(userRight);
			assertEquals("r2", userRight.getCode());

			assertNull(ir1.readLine());

		}
		catch (final ImpExException e)
		{
			fail("error:" + e.getMessage());
		}

		// test importing without code execution : import statements should be ignored
		try
		{
			// t4 and t5 shall be skipped
			title = (Title) ir2.readLine();
			assertNotNull(title);
			assertEquals("t6", title.getCode());
			assertEquals("name6", title.getName(deCtx));

			userRight = (UserRight) ir2.readLine();
			assertNotNull(userRight);
			assertEquals("r3", userRight.getCode());

			// r4 and r5 shall be skipped
			assertNull(ir2.readLine());
		}
		catch (final ImpExException e)
		{
			fail("error:" + e.getMessage());
		}

		// test importing with code execution but without external code execution
		try
		{
			// t
			title = (Title) ir3.readLine();
			assertNotNull(title);
			assertEquals("t8", title.getCode());
			assertEquals("name8", title.getName(deCtx));

			title = (Title) ir3.readLine();
			assertNotNull(title);
			assertEquals("t7", title.getCode());
			assertEquals("name7", title.getName(deCtx));

			assertNull(ir3.readLine());
			assertTrue(ir3.isCodeExecutionEnabled());
			assertFalse(ir3.isExternalCodeExecutionEnabled());
		}
		catch (final ImpExException e)
		{
			fail("error:" + e.getMessage());
		}

		// test importing with code execution and external code execution
		try
		{
			// t
			title = (Title) ir4.readLine();
			assertNotNull(title);
			assertEquals("t10", title.getCode());
			assertEquals("name10", title.getName(deCtx));

			title = (Title) ir4.readLine();
			assertNotNull(title);
			assertEquals("t9", title.getCode());
			assertEquals("name9", title.getName(deCtx));

			assertNull(ir4.readLine());
			assertTrue(ir4.isCodeExecutionEnabled());
			assertFalse(ir4.isExternalCodeExecutionEnabled());
		}
		catch (final ImpExException e)
		{
			fail("error:" + e.getMessage());
		}
	}

	/**
	 * Tests the partial update of localized attributes, means the update of only some localizations.
	 */
	@Test
	public void testLocalizedAttributePartialChange()
	{
		Language language = null;
		Title title = null;
		try
		{
			language = C2LManager.getInstance().createLanguage("locAttrLang");
			title = UserManager.getInstance().createTitle("locTitle");
		}
		catch (final ConsistencyCheckException e)
		{
			fail(e.getMessage());
		}

		SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(null);

		final Map names = new HashMap();
		names.put(german, "name_de");
		names.put(english, "name_en");
		names.put(language, "another");
		title.setAllNames(ctx, names);

		ctx.setLanguage(german);
		assertEquals("name_de", title.getName(ctx));
		ctx.setLanguage(english);
		assertEquals("name_en", title.getName(ctx));
		ctx.setLanguage(language);
		assertEquals("another", title.getName(ctx));

		final ImpExImportReader impExImportReader = new ImpExImportReader(
				"UPDATE Title; code[unique=true]; name[lang=de] ; name[lang=en]\n" + ";" + title.getCode() + "; newname_de; "
						+ ImpExConstants.Syntax.IGNORE_PREFIX + " \n" + ";" + title.getCode() + ";  ; newname_en \n");

		Title title2;
		try
		{
			title2 = (Title) impExImportReader.readLine();
			ctx = jaloSession.createSessionContext();
			assertEquals(title, title2);
			ctx.setLanguage(german);
			assertEquals("newname_de", title.getName(ctx));
			ctx.setLanguage(english);
			assertEquals("name_en", title2.getName(ctx));
			ctx.setLanguage(language);
			assertEquals("another", title2.getName(ctx));

			title2 = (Title) impExImportReader.readLine();
			assertEquals(title, title2);
			ctx.setLanguage(german);
			assertNull(title2.getName(ctx));
			ctx.setLanguage(english);
			assertEquals("newname_en", title2.getName(ctx));
			ctx.setLanguage(language);
			assertEquals("another", title2.getName(ctx));

			assertNull(impExImportReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail("error:" + e);
		}
	}

	/**
	 * Tests the {@link FixedLengthCSVReader}.
	 */
	@Test
	public void testFixedLengthCSVReader()
	{
		final String data = "test01kg Kilogramm      kilogram        1       \n"
				+ "test2 mmmGramm          gram            0,001   \n" + "test3 t  Tonnen         ton             1000    \n";

		final FixedLengthCSVReader fixedLengthCSVReader = new FixedLengthCSVReader(data);
		fixedLengthCSVReader.setFieldTrimming(true);
		fixedLengthCSVReader.addField(0, 5, 0);
		fixedLengthCSVReader.addField(6, 8, 1);
		fixedLengthCSVReader.addField(9, 23, 2);
		fixedLengthCSVReader.addField(24, 39, 3);
		fixedLengthCSVReader.addField(40, 47, 4);

		fixedLengthCSVReader.readNextLine();
		Map<Integer, String> line = fixedLengthCSVReader.getLine();
		assertEquals("test01", line.get(Integer.valueOf(0)));
		assertEquals("kg", line.get(Integer.valueOf(1)));
		assertEquals("Kilogramm", line.get(Integer.valueOf(2)));
		assertEquals("kilogram", line.get(Integer.valueOf(3)));
		assertEquals("1", line.get(Integer.valueOf(4)));

		fixedLengthCSVReader.readNextLine();
		line = fixedLengthCSVReader.getLine();
		assertEquals("test2", line.get(Integer.valueOf(0)));
		assertEquals("mmm", line.get(Integer.valueOf(1)));
		assertEquals("Gramm", line.get(Integer.valueOf(2)));
		assertEquals("gram", line.get(Integer.valueOf(3)));
		assertEquals("0,001", line.get(Integer.valueOf(4)));

		fixedLengthCSVReader.readNextLine();
		line = fixedLengthCSVReader.getLine();
		assertEquals("test3", line.get(Integer.valueOf(0)));
		assertEquals("t", line.get(Integer.valueOf(1)));
		assertEquals("Tonnen", line.get(Integer.valueOf(2)));
		assertEquals("ton", line.get(Integer.valueOf(3)));
		assertEquals("1000", line.get(Integer.valueOf(4)));
	}

	/**
	 * Tests external syntax parsing.
	 */
	@Test
	public void testBeanShellExternalSyntaxParsing()
	{
		final String lines = "INSERT Title;code; \n" + "; blah ; fasel \n" + "\"#% " + "	impex.includeExternalData(\n"
				+ "		new CSVReader(\n" + "			new StringReader(\n" + "				\"\" ;hallo; axel \\n\"\"+\n"
				+ "				\"\"# no comment at all \\n\"\"+\n" + "				\"\"REMOVE Title; code[unique=true]; \\n\"\"+\n"
				+ "				\"\";value1; value2; \\n\"\"+\n" + "				\"\"#% this is no code line !!! \\n\"\"+\n"
				+ "				\"\"; ; ; \\n\"\"\n" + "			)\n" + "		), 0 \n" + "	);\"\n";
		ImpExReader impExReader = new ImpExReader(new CSVReader(new StringReader(lines)), false);
		assertFalse(impExReader.isCodeExecutionEnabled());
		impExReader.enableCodeExecution(true);

		// check defaults
		assertTrue(impExReader.isCodeExecutionEnabled());
		assertFalse(impExReader.isExternalCodeExecutionEnabled());
		assertFalse(impExReader.isExternalSyntaxParsingEnabled());
		assertFalse(impExReader.getValidationMode().equals(ImpExManager.getImportRelaxedMode()));

		// first test correct external data handling WITHOUT ImpEx syntax
		try
		{
			// INSERT Product;code;name[lang=de]
			Object line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof HeaderDescriptor);
			assertEquals(TypeManager.getInstance().getComposedType(Title.class),
					((HeaderDescriptor) line).getConfiguredComposedType());
			assertEquals(ImpExConstants.Syntax.Mode.INSERT,
					((HeaderParams) ((HeaderDescriptor) line).getDescriptorData()).getMode());

			// ; blah ; fasel
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("blah", ((ValueLine) line).getSource().get(Integer.valueOf(1)));
			assertEquals("fasel", ((ValueLine) line).getSource().get(Integer.valueOf(2)));

			// external: hallo; axel
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("hallo", ((ValueLine) line).getSource().get(Integer.valueOf(1)));
			assertEquals("axel", ((ValueLine) line).getSource().get(Integer.valueOf(2)));

			// external: # no comment at all
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("# no comment at all", ((ValueLine) line).getSource().get(Integer.valueOf(0)));

			// external: REMOVE Title; code[unique=true]
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("REMOVE Title", ((ValueLine) line).getSource().get(Integer.valueOf(0)));
			assertEquals("code[unique=true]", ((ValueLine) line).getSource().get(Integer.valueOf(1)));

			// external: value1; value2;
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("value1", ((ValueLine) line).getSource().get(Integer.valueOf(1)));
			assertEquals("value2", ((ValueLine) line).getSource().get(Integer.valueOf(2)));

			// external: #% this is no code line !!!
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("#% this is no code line !!!", ((ValueLine) line).getSource().get(Integer.valueOf(0)));

			assertNull(impExReader.readLine());
			impExReader.close();
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}

		// now test external data handling WITH ImpEx syntax
		impExReader = new ImpExReader(new CSVReader(new StringReader(lines)), false);
		assertFalse(impExReader.isCodeExecutionEnabled());
		impExReader.enableCodeExecution(true);
		impExReader.enableExternalSyntaxParsing(true);

		assertTrue(impExReader.isCodeExecutionEnabled());
		assertFalse(impExReader.isExternalCodeExecutionEnabled());
		assertTrue(impExReader.isExternalSyntaxParsingEnabled());
		assertFalse(impExReader.getValidationMode().equals(ImpExManager.getImportRelaxedMode()));
		try
		{
			// INSERT Product;code;name[lang=de]
			Object line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof HeaderDescriptor);
			assertEquals(TypeManager.getInstance().getComposedType(Title.class),
					((HeaderDescriptor) line).getConfiguredComposedType());
			assertEquals(ImpExConstants.Syntax.Mode.INSERT,
					((HeaderParams) ((HeaderDescriptor) line).getDescriptorData()).getMode());

			// ; blah ; fasel
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("blah", ((ValueLine) line).getValueEntry(1).getCellValue());
			assertEquals("fasel", ((ValueLine) line).getValueEntry(2).getCellValue());

			// external: hallo; axel
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("hallo", ((ValueLine) line).getValueEntry(1).getCellValue());
			assertEquals("axel", ((ValueLine) line).getValueEntry(2).getCellValue());

			// external: # no comment at all
			// is skipped now !

			// external: REMOVE Title; code[unique=true]
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof HeaderDescriptor);
			assertEquals(TypeManager.getInstance().getComposedType(Title.class),
					((HeaderDescriptor) line).getConfiguredComposedType());
			assertEquals(ImpExConstants.Syntax.Mode.REMOVE,
					((HeaderParams) ((HeaderDescriptor) line).getDescriptorData()).getMode());

			// external: value1; value2;
			line = impExReader.readLine();
			assertNotNull(line);
			assertTrue(line instanceof ValueLine);
			assertEquals("value1", ((ValueLine) line).getValueEntry(1).getCellValue());
			assertEquals("value2", ((ValueLine) line).getValueEntry(2).getCellValue());

			// external: #% this is no code line !!!
			// must be skipped since external code execution is switched off

			assertNull(impExReader.readLine());

			impExReader.close();
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the usage of definitions.
	 */
	@Test
	public void testDefinitions()
	{
		final ImpExReader impExReader = new ImpExReader(new CSVReader("$xxx=Blah \n" + "$xxxyyy=Blubb \n" + "$prefixFree=Test \n"
				+ "$prefixFree=Free \n" + "INSERT Title; code \n" + "; 1:$prefixFree2:$xxx3:$xxxyyyxxx\n"), false);
		impExReader.getCSVReader().setShowComments(true);
		try
		{
			Object object = impExReader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof HeaderDescriptor);
			object = impExReader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof ValueLine);
			assertEquals("1:Free2:Blah3:Blubbxxx", ((ValueLine) object).getValueEntry(1).getCellValue());

			assertNull(impExReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		finally
		{
			try
			{
				impExReader.close();
			}
			catch (final IOException e)
			{
				fail(e.getMessage());
			}
		}
	}

	/**
	 * Tests for correct errors if using illegal attribute types in header descriptor.
	 */
	@Test
	public void testIllegalAttributeType()
	{
		CollectionType collType = null;
		MapType mapType = null;
		try
		{
			final ComposedType userType = TypeManager.getInstance().getComposedType(User.class);
			collType = TypeManager.getInstance().createCollectionType("CT" + System.currentTimeMillis(),
					TypeManager.getInstance().getComposedType(Language.class));
			mapType = TypeManager.getInstance().createMapType("MT" + System.currentTimeMillis(),
					TypeManager.getInstance().getType("java.lang.String"), TypeManager.getInstance().getType("java.lang.Boolean"));

			userType.createAttributeDescriptor("illegal1", collType, AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
					+ AttributeDescriptor.SEARCH_FLAG + AttributeDescriptor.OPTIONAL_FLAG + AttributeDescriptor.REMOVE_FLAG);
			userType.createAttributeDescriptor("illegal2", mapType, AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
					+ AttributeDescriptor.SEARCH_FLAG + AttributeDescriptor.OPTIONAL_FLAG + AttributeDescriptor.REMOVE_FLAG);
		}
		catch (final JaloDuplicateCodeException e)
		{
			fail(e.getMessage());
		}
		catch (final JaloDuplicateQualifierException e)
		{
			fail(e.getMessage());
		}

		ImpExReader impExReader = new ImpExReader(
				new CSVReader("UPDATE UserGroup ; uid[unique=true]; members( uid, User.illegal1(isocode) );"), true);

		try
		{
			impExReader.readLine();
		}
		catch (final HeaderValidationException e)
		{
			// check for correct message
			assertTrue(e.getMessage().indexOf(collType.getCode()) > -1);
			assertTrue(e.getMessage().indexOf("not supported") > -1);
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}

		impExReader = new ImpExReader(new CSVReader("UPDATE UserGroup ; uid[unique=true]; members( uid, User.illegal2 );"), true);

		try
		{
			impExReader.readLine();
		}
		catch (final HeaderValidationException e)
		{
			// check for correct message
			assertTrue(e.getMessage().indexOf(mapType.getCode()) > -1);
			assertTrue(e.getMessage().indexOf("not supported") > -1);
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}

		impExReader = new ImpExReader(new CSVReader("UPDATE UserGroup ; uid[unique=true]; members( uid );"), true);

		try
		{
			final Object object = impExReader.readLine();
			assertNotNull(object);
			assertTrue(object instanceof HeaderDescriptor);

			assertNull(impExReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception " + e.getClass() + " : " + e);
		}
	}

	/**
	 * Checks if given column descriptor has all given attributes.
	 *
	 * @param columnDescriptor
	 *           column descriptor which will be checked
	 * @param header
	 *           expected header
	 * @param qualifier
	 *           expected qualifier
	 * @param position
	 *           expected position
	 * @param attributeDescriptor
	 *           expected attribute descriptor
	 * @param localized
	 *           expected localized flag
	 * @param initial
	 *           expected initial flag
	 * @param readonly
	 *           expected readonly flag
	 * @param partof
	 *           expected partof flag
	 * @param mandatory
	 *           expected mandatory flag
	 * @param unique
	 *           expected unique flag
	 * @param languageIso
	 *           expected languageIso
	 * @param modifiers
	 *           expected modifiers
	 */
	protected void checkColumnDescriptor(final StandardColumnDescriptor columnDescriptor, final HeaderDescriptor header,
			final String qualifier, final int position, final AttributeDescriptor attributeDescriptor, final boolean localized,
			final boolean initial, final boolean readonly, final boolean partof, final boolean mandatory, final boolean unique,
			final String languageIso, final Map modifiers)
	{
		assertEquals(columnDescriptor.toString(), qualifier, columnDescriptor.getQualifier());
		assertEquals(columnDescriptor.toString(), position, columnDescriptor.getValuePosition());
		assertEquals(columnDescriptor.toString(), attributeDescriptor, columnDescriptor.getAttributeDescriptor());
		assertEquals(columnDescriptor.toString(), header, columnDescriptor.getHeader());
		assertEquals(columnDescriptor.toString(), Boolean.valueOf(localized), Boolean.valueOf(columnDescriptor.isLocalized()));
		assertEquals(columnDescriptor.toString(), Boolean.valueOf(initial), Boolean.valueOf(columnDescriptor.isInitialOnly()));
		assertEquals(columnDescriptor.toString(), Boolean.valueOf(readonly), Boolean.valueOf(columnDescriptor.isReadOnly()));
		assertEquals(columnDescriptor.toString(), Boolean.valueOf(partof), Boolean.valueOf(columnDescriptor.isPartOf()));
		assertEquals(columnDescriptor.toString(), Boolean.valueOf(mandatory), Boolean.valueOf(columnDescriptor.isMandatory()));
		assertEquals(columnDescriptor.toString(), Boolean.valueOf(unique), Boolean.valueOf(columnDescriptor.isUnique()));
		assertEquals(columnDescriptor.toString(), languageIso, columnDescriptor.getLanguageIso());
		assertEquals(columnDescriptor.toString(), qualifier,
				((AbstractDescriptor.ColumnParams) columnDescriptor.getDescriptorData()).getQualifier());
		assertEquals(columnDescriptor.toString(), modifiers,
				((AbstractDescriptor.ColumnParams) columnDescriptor.getDescriptorData()).getModifiers());
	}

	/**
	 * Tests usage of relation as reference type in header descriptor.
	 */
	@Test
	public void testReferenceByRelation()
	{
		// set 'groups' default value back to null (changed by CoreBasicDataCreator.createBasicUserGroups())
		final ComposedType employeeT = TypeManager.getInstance().getComposedType(Employee.class);
		employeeT.getAttributeDescriptor(Customer.GROUPS).setDefaultValue(null);

		final ComposedType customerT = TypeManager.getInstance().getComposedType(Customer.class);
		customerT.getAttributeDescriptor(Customer.GROUPS).setDefaultValue(null);


		final ImpExImportReader impExImportReader = new ImpExImportReader(//
				"INSERT UserGroup ; uid[unique=true] ; groups(uid) \n" + //
						"; grp100 ; \n" + //
						"; grp110 ; grp100 \n" + //
						"; grp120 ; grp100 \n " + //
						"; grp111 ; grp110 \n " + //
						"; grp121 ; grp120 \n " + //
						" \n " + //
						"INSERT Employee ; uid[unique=true] ; groups( uid, groups( uid ) ) \n" + //
						"; thomas ; grp111:grp110, grp121:grp120 \n" + //
						"; dummy ; grp111:grp100 \n"//
		);

		UserGroup grp100 = null, grp110 = null, grp120 = null, grp111 = null, grp121 = null;
		Employee thomas = null, dummy = null;
		try
		{
			grp100 = (UserGroup) impExImportReader.readLine();

			grp110 = (UserGroup) impExImportReader.readLine();
			assertGroups(grp110, grp100);

			grp120 = (UserGroup) impExImportReader.readLine();
			assertGroups(grp120, grp100);

			grp111 = (UserGroup) impExImportReader.readLine();
			assertGroups(grp111, grp110);

			grp121 = (UserGroup) impExImportReader.readLine();
			assertGroups(grp121, grp120);

			thomas = (Employee) impExImportReader.readLine();
			assertEquals(0, impExImportReader.getDumpedLineCount());
			assertGroups(thomas, grp111, grp121);

			dummy = (Employee) impExImportReader.readLine();
			assertGroups(dummy, null);

			assertEquals(1, impExImportReader.getDumpedLineCount());

			assertNull(impExImportReader.readLine());

			impExImportReader.close();
		}
		catch (final ImpExException e)
		{
			fail("unexpected error " + e);
		}
		catch (final IOException e)
		{
			fail("unexpected error " + e);
		}
		finally
		{
			removeQuietly(dummy, thomas, grp121, grp120, grp111, grp110, grp100);
		}
	}

	private void assertGroups(final Principal principal, final PrincipalGroup... groups)
	{
		final Set<PrincipalGroup> groupsSet = groups == null ? Collections.EMPTY_SET
				: new HashSet<PrincipalGroup>(Arrays.asList(groups));

		assertCollection("wrong groups " + groupsToString(groupsSet), groupsSet, principal.getGroups());
	}

	private String groupsToString(final Iterable<PrincipalGroup> groups)
	{
		return "[" + Joiner.on(',').join(Iterables.transform(groups, new Function<PrincipalGroup, String>()
		{
			@Override
			@Nullable
			public String apply(@Nullable final PrincipalGroup grp)
			{
				return grp == null ? "<null>" : grp.getUid();
			}
		})) + "]";
	}

	protected void removeQuietly(final Item... items)
	{
		for (final Item item : items)
		{
			if (item != null)
			{
				try
				{
					item.remove();
				}
				catch (final Exception e)
				{
					// swallow
				}
			}
		}
	}

	/**
	 * Tests bean shell method <code>insetLine</code>.
	 */
	@Test
	public void testBeanShellInsertLine()
	{
		final SessionContext deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(german);
		final ImpExImportReader impExImportReader = new ImpExImportReader("INSERT Title; code[unique=true]; name[lang="
				+ german.getIsoCode() + "]; \n" + "; t1 ; t1name \n" + "\"#% \n" + "Map m = new HashMap(); \n"
				+ "m.put(Integer.valueOf(1), \"\"t2\"\"); \n" + "m.put(Integer.valueOf(2), \"\"t2name\"\"); \n"
				+ "impex.insertLine( m ); \n" + "m.clear(); \n" + "m.put(Integer.valueOf(1), \"\"t3\"\"); \n"
				+ "m.put(Integer.valueOf(2), \"\"t3name\"\"); \n" + "impex.insertLine( m );\"\n" + "; t4 ; t4name \n");
		impExImportReader.enableCodeExecution(true);
		try
		{
			Title title;

			title = (Title) impExImportReader.readLine();
			assertNotNull(title);
			assertEquals("t1", title.getCode());
			assertEquals("t1name", title.getName(deCtx));

			title = (Title) impExImportReader.readLine();
			assertNotNull(title);
			assertEquals("t2", title.getCode());
			assertEquals("t2name", title.getName(deCtx));

			title = (Title) impExImportReader.readLine();
			assertNotNull(title);
			assertEquals("t3", title.getCode());
			assertEquals("t3name", title.getName(deCtx));

			title = (Title) impExImportReader.readLine();
			assertNotNull(title);
			assertEquals("t4", title.getCode());
			assertEquals("t4name", title.getName(deCtx));

			assertNull(impExImportReader.readLine());
		}
		catch (final ImpExException e)
		{
			fail("unexpected impex error " + e.getMessage());
		}
	}

	/**
	 * Tests the methods <code>discardLine</code> and <code>dumpLine</code> for bean shell usage.
	 */
	@Test
	public void testBeanShellDiscardAndDumpLine()
	{
		final ComposedType langType = TypeManager.getInstance().getComposedType(Language.class);

		final String script = //
				ImpExConstants.Syntax.Mode.INSERT + " " + langType.getCode() + ";" //
						+ Language.ISOCODE + "[unique=true];" //
						+ Language.ACTIVE + "[default=false];\n" //
						+ "#% beforeEach: impex.discardNextLine();\n" //
						+ ";" + "iso1" + ";" + "true" + ";" + "\n" //
						+ "#% beforeEach:end\n" //
						+ ";" + "iso2" + ";" + "\n" //
						+ "\"#% beforeEach: impex.dumpNextLine(\"\"Darum\"\");\"\n" //
						+ ";" + "iso3" + ";" + "true" + ";" + "\n" //
						+ "#% beforeEach:end";

		final ImpExImportReader reader = new ImpExImportReader(script);
		reader.enableCodeExecution(true);
		try
		{
			final Language lang1 = (Language) reader.readLine();
			assertEquals("iso2", lang1.getIsoCode());

			final Item nullItem = (Item) reader.readLine();
			assertNull(nullItem);

			assertEquals(1, reader.getDumpedLineCount());
		}
		catch (final ImpExException e)
		{
			fail("Unexpexted error: " + e.getMessage());
		}
	}

	public static List<String> nextLine = Collections.synchronizedList(new ArrayList<String>());

	/**
	 * Tests the methods <code>discardLine</code> and <code>dumpLine</code> for bean shell usage.
	 */
	@Test
	public void testBeforeEach() throws IOException
	{
		final ComposedType langType = TypeManager.getInstance().getComposedType(Language.class);

		final File subScript = File.createTempFile("impextest", ".impex");
		final PrintWriter writer = new PrintWriter(subScript);
		writer.println("iso2;true");
		writer.close();

		final String script = //
				ImpExConstants.Syntax.Mode.INSERT + " " + langType.getCode() + ";" //
						+ Language.ISOCODE + "[unique=true];" //
						+ Language.ACTIVE + "[default=false];\n" //
						+ "#% beforeEach: " + getClass().getName() + ".nextLine.add(line.get(Integer.valueOf(1)));\n" //
						+ ";" + "iso1" + ";" + "true" + ";\n" //
						+ "\"#%impex.includeExternalData(\"\"" + subScript.getAbsolutePath().replace("\\", "\\\\")
						+ "\"\",\"\"windows-1252\"\",0)\"";

		final ImpExImportReader reader = new ImpExImportReader(script);
		reader.enableCodeExecution(true);
		try
		{
			nextLine.clear();
			final Language lang1 = (Language) reader.readLine();
			assertNotNull(lang1);
			assertEquals("iso1", lang1.getIsoCode());
			assertEquals("iso1", nextLine.get(0));
			nextLine.clear();

			final Language lang2 = (Language) reader.readLine();
			assertNotNull(lang2);
			assertEquals("iso2", lang2.getIsoCode());
			assertEquals("iso2", nextLine.get(0));

			final Item nullItem = (Item) reader.readLine();
			assertNull(nullItem);

			assertEquals(0, reader.getDumpedLineCount());
		}
		catch (final ImpExException e)
		{
			fail("Unexpexted error: " + e.getMessage());
		}
	}

	@Test
	public void testBeforeEachMultiThread() throws IOException
	{
		final ComposedType langType = TypeManager.getInstance().getComposedType(Language.class);

		final File subScript = File.createTempFile("impextest", ".impex");
		final PrintWriter writer = new PrintWriter(subScript);
		writer.println("iso2;true");
		writer.close();

		final String script = //
				ImpExConstants.Syntax.Mode.INSERT + " " + langType.getCode() + ";" //
						+ Language.ISOCODE + "[unique=true];" //
						+ Language.ACTIVE + "[default=false];\n" //
						+ "#% beforeEach: " + getClass().getName() + ".nextLine.add(line.get(Integer.valueOf(1)));\n" //
						+ ";" + "iso1" + ";" + "true" + ";\n" //
						+ "\"#%impex.includeExternalData(\"\"" + subScript.getAbsolutePath().replace("\\", "\\\\")
						+ "\"\",\"\"windows-1252\"\",0)\"";

		final ImpExImportReader reader = new MultiThreadedImpExImportReader(script);
		reader.enableCodeExecution(true);
		try
		{
			boolean iso1 = false;
			boolean iso2 = false;
			nextLine.clear();
			final Language lang1 = (Language) reader.readLine();
			assertNotNull(lang1);
			if (lang1.getIsoCode().equals("iso1"))
			{
				iso1 = true;
				assertEquals("iso1", lang1.getIsoCode());
				assertTrue(nextLine.contains("iso1"));
			}
			else if (lang1.getIsoCode().equals("iso2"))
			{
				iso2 = true;
				assertEquals("iso2", lang1.getIsoCode());
				assertTrue(nextLine.contains("iso2"));
			}
			else
			{
				fail("iso1 and iso2 where not imported");
			}

			final Language lang2 = (Language) reader.readLine();
			assertNotNull(lang2);
			if (lang2.getIsoCode().equals("iso1") && !iso1)
			{
				assertEquals("iso1", lang2.getIsoCode());
				assertTrue(nextLine.contains("iso1"));
			}
			else if (lang2.getIsoCode().equals("iso2") && !iso2)
			{
				assertEquals("iso2", lang2.getIsoCode());
				assertTrue(nextLine.contains("iso2"));
			}
			else
			{
				fail("iso1 and iso2 where not imported");
			}

			final Item nullItem = (Item) reader.readLine();
			assertNull(nullItem);

			assertEquals(0, reader.getDumpedLineCount());
		}
		catch (final ImpExException e)
		{
			fail("Unexpexted error: " + e.getMessage());
		}
	}

	/**
	 * Tests the {@link ImpExManager#createItem(String, String, String)} method. See PLA-8.
	 */
	@Test
	public void testCreateItem()
	{
		Language lang = null;
		try
		{
			lang = (Language) ImpExManager.getInstance().createItem("Language", "isocode; active", "testLang; false");
		}
		catch (final ImpExException e)
		{
			fail("unexpected exception: " + e.getMessage());
		}
		assertEquals("testLang", lang.getIsoCode());
		assertFalse(lang.isActive().booleanValue());
	}

	/**
	 * Tests the {@link AbstractDescriptor#parseColumnDescriptor(String)} method.
	 */
	@Test
	public void testParseColumnDescriptor()
	{
		String expression = "";
		ColumnParams params = null;
		try
		{
			params = AbstractDescriptor.parseColumnDescriptor(expression);
		}
		catch (final HeaderValidationException e)
		{
			fail("unexpected exception: " + e.getMessage());
		}
		assertEquals(0, params.getModifiers().size());
		assertEquals("", params.getQualifier());

		expression = "a(b(c,d))[e=f,g=h]";
		try
		{
			params = AbstractDescriptor.parseColumnDescriptor(expression);
		}
		catch (final HeaderValidationException e)
		{
			fail("unexpected exception: " + e.getMessage());
		}
		assertEquals(2, params.getModifiers().size());
		assertEquals("f", params.getModifier("e"));
		assertEquals("h", params.getModifier("g"));
		assertEquals("a", params.getQualifier());
		final ColumnParams params2 = (ColumnParams) ((ArrayList) params.getItemPatternLists()[0]).get(0);
		assertEquals(0, params2.getModifiers().size());
		assertEquals("b", params2.getQualifier());
		final ColumnParams params3 = (ColumnParams) ((ArrayList) params2.getItemPatternLists()[0]).get(0);
		assertEquals(0, params3.getModifiers().size());
		assertEquals("c", params3.getQualifier());
		final ColumnParams params4 = (ColumnParams) ((ArrayList) params2.getItemPatternLists()[0]).get(1);
		assertEquals(0, params4.getModifiers().size());
		assertEquals("d", params4.getQualifier());

		expression = "a[e!\"=f%&,g;*=h()]";
		try
		{
			params = AbstractDescriptor.parseColumnDescriptor(expression);
		}
		catch (final HeaderValidationException e)
		{
			fail("unexpected exception: " + e.getMessage());
		}
		assertEquals(2, params.getModifiers().size());
		assertEquals("f%&", params.getModifier("e!\""));
		assertEquals("h()", params.getModifier("g;*"));

		expression = "a[e!\"=f%&,g;*=h()]"; // PLA-4876
		try
		{
			params = AbstractDescriptor.parseColumnDescriptor(expression);
		}
		catch (final HeaderValidationException e)
		{
			fail("unexpected exception: " + e.getMessage());
		}
		assertEquals(2, params.getModifiers().size());
		assertEquals("f%&", params.getModifier("e!\""));
		assertEquals("h()", params.getModifier("g;*"));

		expression = "a[a='b=c',d='\"''''e''\"']"; // PLA-4999
		try
		{
			params = AbstractDescriptor.parseColumnDescriptor(expression);
		}
		catch (final HeaderValidationException e)
		{
			fail("unexpected exception: " + e.getMessage());
		}
		assertEquals(2, params.getModifiers().size());
		assertEquals("b=c", params.getModifier("a"));
		assertEquals("\"''e'\"", params.getModifier("d"));
		assertEquals(null, params.getModifier("f"));

		expression = "a[f='g'''h']"; // PLA-4999
		try
		{
			params = AbstractDescriptor.parseColumnDescriptor(expression);
			fail("expexted HeaderValidationException");
		}
		catch (final HeaderValidationException e)
		{
			// OK
		}
	}
}
