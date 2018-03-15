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
package de.hybris.platform.persistence.property;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class PropertyTableDefinitionTest
{
	private final boolean NON_LOCALIZED = false;
	private final boolean LOCALIZED = true;

	private static final String TRUNCATED_LONG_DESCRIPTOR = "columnNamecolumnNamecolumnName";
	private static final String SOME_LONG_DESCRIPTOR = "columnNamecolumnNamecolumnNamecolumnNamecolumnNamecolumnNamecolumnName";
	private static final String SOME_SHORT_DESCRIPTOR = "columnNamecolumn";

	private static final String COLUMN_QUALIFIER_1 = "OneBigColumnQ";
	private static final String COLUMN_QUALIFIER_2 = "TwoBigColumnQ";
	private static final String COLUMN_QUALIFIER_3 = "ThreeBigColumnQ";
	private static final String COLUMN_QUALIFIER_4 = "FourBigColumnQ";



	private static final String SOME_DEFINITION = "SQL definition here";
	private static final String OTHER_DEFINITION = "Other SQL definition here";

	private PropertyTableDefinition definition;


	@Before
	public void prepareDefintion()
	{
		definition = new PropertyTableDefinition("dumpTable", "tableOne", "tableTwo", 997);
	}

	@Test
	public void testColumnNameCaseIsPersisted()
	{
		final boolean localizedFlag = true;
		definition.addColumn(COLUMN_QUALIFIER_2, "one", String.class, SOME_DEFINITION, localizedFlag, true);
		definition.addColumn(COLUMN_QUALIFIER_2, "Two", String.class, SOME_DEFINITION, localizedFlag, true);
		definition.addColumn(COLUMN_QUALIFIER_2, "THreE", String.class, SOME_DEFINITION, localizedFlag, true);
		definition.addColumn(COLUMN_QUALIFIER_2, "fOuR", String.class, SOME_DEFINITION, localizedFlag, true);

		final Set<String> originalColumns = definition.getColumnNames(localizedFlag);
		Assert.assertTrue(originalColumns.contains("one"));
		Assert.assertTrue(originalColumns.contains("Two"));
		Assert.assertTrue(originalColumns.contains("Two".toLowerCase()));
		Assert.assertTrue(originalColumns.contains("THreE"));
		Assert.assertTrue(originalColumns.contains("THreE".toLowerCase()));
		Assert.assertTrue(originalColumns.contains("fOuR"));
		Assert.assertTrue(originalColumns.contains("fOuR".toUpperCase()));

		Assert.assertEquals(4, definition.getColumnNames(localizedFlag).size());

	}

	@Test
	public void testGetColumnDefinitionCaseUnAware()
	{
		final boolean localizedFlag = true;
		definition.addColumn(COLUMN_QUALIFIER_2, "columnNameWITHBig", String.class, SOME_DEFINITION, localizedFlag, true);

		//try to get this definition in case unaware way 
		Assert.assertNotNull(definition.getColumnDefinition("colUmnNaMewItHBig", localizedFlag));
		Assert.assertEquals(COLUMN_QUALIFIER_2, definition.getColumnPropertyName("colUmnNaMewItHBig", localizedFlag));
		Assert.assertEquals(SOME_DEFINITION, definition.getSqlColumnDescription("colUmnNaMewItHBig", localizedFlag));
		Assert.assertEquals(String.class, definition.getColumnDefinition("colUmnNaMewItHBig", localizedFlag));

		Assert.assertNotNull(definition.getColumnDefinition("colUmnNaMewItHBig".toLowerCase(), localizedFlag));
		Assert.assertEquals(String.class, definition.getColumnDefinition("colUmnNaMewItHBig".toLowerCase(), localizedFlag));
		Assert.assertEquals(SOME_DEFINITION, definition.getSqlColumnDescription("colUmnNaMewItHBig".toLowerCase(), localizedFlag));
		Assert.assertEquals(COLUMN_QUALIFIER_2, definition.getColumnPropertyName("colUmnNaMewItHBig".toLowerCase(), localizedFlag));

		Assert.assertNotNull(definition.getColumnDefinition("colUmnNaMewItHBig".toUpperCase(), localizedFlag));
		Assert.assertEquals(String.class, definition.getColumnDefinition("colUmnNaMewItHBig".toUpperCase(), localizedFlag));
		Assert.assertEquals(SOME_DEFINITION, definition.getSqlColumnDescription("colUmnNaMewItHBig".toUpperCase(), localizedFlag));
		Assert.assertEquals(COLUMN_QUALIFIER_2, definition.getColumnPropertyName("colUmnNaMewItHBig".toUpperCase(), localizedFlag));
	}


	@Test
	public void testGetOriginalColumnDefintions()
	{
		final boolean localizedFlag = true;
		definition.addColumn("One", "identifier" + 1, String.class, SOME_DEFINITION, localizedFlag, true);
		definition.addColumn("Two", "iDenTifIer" + 2, String.class, SOME_DEFINITION, localizedFlag, true);
		definition.addColumn("Three", "IDenTifiEr" + 3, String.class, SOME_DEFINITION, localizedFlag, true);
		definition.addColumn("Four", ("IDenTifiEr".toLowerCase()) + 4, String.class, SOME_DEFINITION, localizedFlag, true);
		definition.addColumn("Five", ("IDenTifiEr".toUpperCase()) + 5, String.class, SOME_DEFINITION, localizedFlag, true);

		//try to get this definition in case unaware way 
		Assert.assertNotNull(definition.getColumnNames(localizedFlag));
		Assert.assertEquals(5, definition.getColumnNames(localizedFlag).size());
		final Set<String> names = definition.getColumnNames(localizedFlag);
		Assert.assertTrue(names.contains("identifier" + 1));
		Assert.assertTrue(names.contains("iDenTifIer" + 2));
		Assert.assertTrue(names.contains("IDenTifiEr" + 3));
		Assert.assertTrue(names.contains(("IDenTifiEr".toLowerCase()) + 4));
		Assert.assertTrue(names.contains(("IDenTifiEr".toUpperCase()) + 5));

	}

	@Test
	public void testAddAmbiguityColumn()
	{
		final boolean localizedFlag = true;
		definition.addColumn(COLUMN_QUALIFIER_1, "columnName", String.class, SOME_DEFINITION, localizedFlag, true);//ok
		definition.addColumn(COLUMN_QUALIFIER_2, "columnNameWITHBig", String.class, SOME_DEFINITION, !localizedFlag, true);//ok not localized

		definition.addColumn(COLUMN_QUALIFIER_3, "columnNameWITHBig".toLowerCase(), String.class, SOME_DEFINITION, localizedFlag,
				true);
		definition.addColumn(COLUMN_QUALIFIER_4, "columnNameWITHBig".toUpperCase(), String.class, SOME_DEFINITION, localizedFlag,
				true);
	}


	@Test
	public void testCheckNoColumnTruncation()
	{
		final boolean localizedFlag = true;
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, String.class, SOME_DEFINITION, localizedFlag, true);//ok


	}

	@Test
	public void testCheckColumnTruncation()
	{
		final boolean localizedFlag = true;
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Integer.class, SOME_DEFINITION, localizedFlag, true);//ok


		Assert.assertNull("column descriptor should be truncated",
				definition.getColumnDefinition(SOME_LONG_DESCRIPTOR, localizedFlag));

		Assert.assertEquals(Integer.class, definition.getColumnDefinition(TRUNCATED_LONG_DESCRIPTOR, localizedFlag));

	}


	@Test
	public void testAddColumnDescriptorTrimNotExistsLocalized()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, String.class, SOME_DEFINITION, NON_LOCALIZED, true);//prepare

		final String truncatedQualifier = definition.addColumn(COLUMN_QUALIFIER_2, SOME_LONG_DESCRIPTOR, Object.class,
				OTHER_DEFINITION, NON_LOCALIZED, true);//ok

		Class entry = definition.getColumnDefinition(COLUMN_QUALIFIER_2, LOCALIZED);
		Assert.assertNull(entry);

		entry = definition.getColumnDefinition(truncatedQualifier, LOCALIZED);//test get localized
		Assert.assertNull(entry);

		entry = definition.getColumnDefinition(truncatedQualifier, NON_LOCALIZED);//test get nonlocalized
		Assert.assertTrue(entry == Object.class);

		Assert.assertEquals(OTHER_DEFINITION, definition.getSqlColumnDescription(truncatedQualifier, NON_LOCALIZED));
	}



	@Test
	public void testAddNewColumnDescriptorAdjustSQLColumnDefinition()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, String.class, null, NON_LOCALIZED, true);//prepare

		final String truncatedQualifier = definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Object.class,
				OTHER_DEFINITION, NON_LOCALIZED, true);//ok

		Class entry = definition.getColumnDefinition(COLUMN_QUALIFIER_2, LOCALIZED);
		Assert.assertNull(entry);

		entry = definition.getColumnDefinition(truncatedQualifier, LOCALIZED);//test get localized
		Assert.assertNull(entry);

		entry = definition.getColumnDefinition(truncatedQualifier, NON_LOCALIZED);//test get nonlocalized
		Assert.assertEquals(Object.class, entry);

		Assert.assertEquals("SQL column definition should have been adjusted ", OTHER_DEFINITION,
				definition.getSqlColumnDescription(truncatedQualifier, NON_LOCALIZED));

	}


	@Test
	public void testAddColumnDescriptorTrimExistsAssureColumnAdded()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, String.class, null, NON_LOCALIZED, true);//prepare

		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Object.class, OTHER_DEFINITION, NON_LOCALIZED, true);//ok

		Assert.assertEquals(2, definition.getColumnNames(NON_LOCALIZED).size());
	}


	@Test
	public void testAddColumnDescriptorAdjustSQLColumnDefinition()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Object.class, null, NON_LOCALIZED, true);//prepare

		final String truncatedQualifier = definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Object.class,
				OTHER_DEFINITION, NON_LOCALIZED, true);//ok

		Class entry = definition.getColumnDefinition(COLUMN_QUALIFIER_1, LOCALIZED);
		Assert.assertNull(entry);

		entry = definition.getColumnDefinition(truncatedQualifier, LOCALIZED);//test get localized
		Assert.assertNull(entry);

		entry = definition.getColumnDefinition(truncatedQualifier, NON_LOCALIZED);//test get nonlocalized
		Assert.assertTrue(entry == Object.class);

		Assert.assertEquals("SQL column definition should have been adjusted ", OTHER_DEFINITION,
				definition.getSqlColumnDescription(truncatedQualifier, NON_LOCALIZED));

		Assert.assertEquals(1, definition.getColumnNames(NON_LOCALIZED).size());
	}


	@Test
	public void testAddColumnDescriptorAdjustSQLColumnDefinitionAssureColumnNotAdded()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Object.class, null, NON_LOCALIZED, true);//prepare

		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Object.class, OTHER_DEFINITION, NON_LOCALIZED, true);//ok

		Assert.assertEquals(1, definition.getColumnNames(NON_LOCALIZED).size());
	}


	@Test
	public void testCanAddColumnDifferentDefinitions()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Object.class, SOME_DEFINITION, NON_LOCALIZED, true);//prepare

		Assert.assertTrue(definition.canAddColumn(COLUMN_QUALIFIER_2, SOME_SHORT_DESCRIPTOR, String.class, OTHER_DEFINITION,
				NON_LOCALIZED));

	}

	@Test
	public void testCanAddColumnWithTheSameQualifier()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_LONG_DESCRIPTOR, Object.class, SOME_DEFINITION, NON_LOCALIZED, true);//prepare

		Assert.assertTrue(definition.canAddColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, String.class, OTHER_DEFINITION,
				NON_LOCALIZED));

	}


	@Test
	public void testCanAddColumnWithTheSameDescriptorQualifiersDiffers()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, Object.class, SOME_DEFINITION, NON_LOCALIZED, true);//prepare

		Assert.assertFalse(definition.canAddColumn(COLUMN_QUALIFIER_2, SOME_SHORT_DESCRIPTOR, String.class, OTHER_DEFINITION,
				NON_LOCALIZED));

	}

	@Test
	public void testCanAddColumnWithTheSameDescriptorSameQualifiersDefintionsDiffers()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, Object.class, SOME_DEFINITION, NON_LOCALIZED, true);//prepare

		Assert.assertFalse(definition.canAddColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, String.class, OTHER_DEFINITION,
				NON_LOCALIZED));

	}

	@Test
	public void testCanAddColumnWithTheSameDescriptorSameQualifiersAndDefintions()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, Object.class, SOME_DEFINITION, NON_LOCALIZED, true);//prepare

		Assert.assertTrue(definition.canAddColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, String.class, SOME_DEFINITION,
				NON_LOCALIZED));

	}


	@Test
	public void testCanAddColumnWithTheSameDescriptorSameQualifiersNullDefintionsDifferentClassDefintions()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, Object.class, null, NON_LOCALIZED, true);//prepare

		Assert.assertFalse(definition.canAddColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, String.class, null, NON_LOCALIZED));

	}


	@Test
	public void testCanAddColumnWithTheSameDescriptorSameQualifiersNullDefintionsSameClassDefintions()
	{
		definition.addColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, Object.class, null, NON_LOCALIZED, true);//prepare

		Assert.assertTrue(definition.canAddColumn(COLUMN_QUALIFIER_1, SOME_SHORT_DESCRIPTOR, Object.class, null, NON_LOCALIZED));

	}

}
