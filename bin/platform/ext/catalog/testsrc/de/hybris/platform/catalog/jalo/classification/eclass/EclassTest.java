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
package de.hybris.platform.catalog.jalo.classification.eclass;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class EclassTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(EclassTest.class); //NOPMD

	@Test
	public void testFieldFormatParsing()
	{
		final EnumerationValue STRING = EnumerationManager.getInstance().getEnumerationValue(
				CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
				CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING);
		final EnumerationValue BOOLEAN = EnumerationManager.getInstance().getEnumerationValue(
				CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
				CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN);
		final EnumerationValue NUMBER = EnumerationManager.getInstance().getEnumerationValue(
				CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
				CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER);

		// X..30
		EClassFieldFormat fieldFormat = new EClassFieldFormat("X..30");
		assertEquals("X", fieldFormat.getFieldType());
		assertEquals(30, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertFalse(fieldFormat.isFixedLength());
		assertEquals(STRING, fieldFormat.getClassificationAttributeType());

		// X 30
		fieldFormat = new EClassFieldFormat("X 30");
		assertEquals("X", fieldFormat.getFieldType());
		assertEquals(30, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertTrue(fieldFormat.isFixedLength());
		assertEquals(STRING, fieldFormat.getClassificationAttributeType());

		// X..1
		fieldFormat = new EClassFieldFormat("X..1");
		assertEquals("X", fieldFormat.getFieldType());
		assertEquals(1, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertFalse(fieldFormat.isFixedLength());
		assertEquals(BOOLEAN, fieldFormat.getClassificationAttributeType());

		// X  1
		fieldFormat = new EClassFieldFormat("X  1");
		assertEquals("X", fieldFormat.getFieldType());
		assertEquals(1, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertTrue(fieldFormat.isFixedLength());
		assertEquals(BOOLEAN, fieldFormat.getClassificationAttributeType());

		// X.1
		fieldFormat = new EClassFieldFormat("X.1");
		assertEquals("X", fieldFormat.getFieldType());
		assertEquals(1, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertFalse(fieldFormat.isFixedLength());
		assertEquals(BOOLEAN, fieldFormat.getClassificationAttributeType());

		// V
		fieldFormat = new EClassFieldFormat("V");
		assertEquals("V", fieldFormat.getFieldType());
		assertEquals(0, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertTrue(fieldFormat.isFixedLength());
		assertEquals(BOOLEAN, fieldFormat.getClassificationAttributeType());

		// NR1..3
		fieldFormat = new EClassFieldFormat("NR1..3");
		assertEquals("NR1", fieldFormat.getFieldType());
		assertEquals(3, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertFalse(fieldFormat.isFixedLength());
		assertEquals(NUMBER, fieldFormat.getClassificationAttributeType());

		// NR1 3
		fieldFormat = new EClassFieldFormat("NR1 3");
		assertEquals("NR1", fieldFormat.getFieldType());
		assertEquals(3, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertTrue(fieldFormat.isFixedLength());
		assertEquals(NUMBER, fieldFormat.getClassificationAttributeType());

		// NR1 S..3
		fieldFormat = new EClassFieldFormat("NR1 S..3");
		assertEquals("NR1", fieldFormat.getFieldType());
		assertEquals(3, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertTrue(fieldFormat.isSigned());
		assertFalse(fieldFormat.isFixedLength());
		assertEquals(NUMBER, fieldFormat.getClassificationAttributeType());

		// NR1 S 3
		fieldFormat = new EClassFieldFormat("NR1 S 3");
		assertEquals("NR1", fieldFormat.getFieldType());
		assertEquals(3, fieldFormat.getFieldLength());
		assertEquals(0, fieldFormat.getFractionDigits());
		assertTrue(fieldFormat.isSigned());
		assertTrue(fieldFormat.isFixedLength());
		assertEquals(NUMBER, fieldFormat.getClassificationAttributeType());

		// NR2 S..3.3
		fieldFormat = new EClassFieldFormat("NR2 S..3.3");
		assertEquals("NR2", fieldFormat.getFieldType());
		assertEquals(3, fieldFormat.getFieldLength());
		assertEquals(3, fieldFormat.getFractionDigits());
		assertTrue(fieldFormat.isSigned());
		assertFalse(fieldFormat.isFixedLength());
		assertEquals(NUMBER, fieldFormat.getClassificationAttributeType());

		// NR2 3.3
		fieldFormat = new EClassFieldFormat("NR2 3.3");
		assertEquals("NR2", fieldFormat.getFieldType());
		assertEquals(3, fieldFormat.getFieldLength());
		assertEquals(3, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertTrue(fieldFormat.isFixedLength());
		assertEquals(NUMBER, fieldFormat.getClassificationAttributeType());

		// NR3..3.4E2
		fieldFormat = new EClassFieldFormat("NR3..3.4E2");
		assertEquals("NR3", fieldFormat.getFieldType());
		assertEquals(3, fieldFormat.getFieldLength());
		assertEquals(4, fieldFormat.getFractionDigits());
		assertFalse(fieldFormat.isSigned());
		assertFalse(fieldFormat.isFixedLength());
		assertEquals(NUMBER, fieldFormat.getClassificationAttributeType());
	}
}
