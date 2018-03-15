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
package de.hybris.platform.validation.model.constraints.jsr303;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Testing the Digits constraint
 */
@IntegrationTest
public class DigitsConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: CurrencyModel.conversition must be in the style xx.yyy
	 */
	private void createDigitConstraint()
	{
		final AttributeDescriptorModel attrDesc1 = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.CONVERSION);
		final DigitsConstraintModel digitConstraint1 = modelService.create(DigitsConstraintModel.class);
		digitConstraint1.setId("digitConstraint1");
		digitConstraint1.setInteger(Integer.valueOf(2));
		digitConstraint1.setFraction(Integer.valueOf(3));
		digitConstraint1.setDescriptor(attrDesc1);
		modelService.saveAll(digitConstraint1);
		Assert.assertEquals(getDefaultMessage(Constraint.DIGITS.msgKey), digitConstraint1.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	@Test
	public void testDigitsOk1()
	{
		createDigitConstraint();

		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("xxx");
		curr.setConversion(Double.valueOf(12.345));
		curr.setDigits(Integer.valueOf(1));

		assertTrue(validationService.validate(curr).isEmpty());
	}

	@Test
	public void testDigitsFalse1()
	{
		createDigitConstraint();

		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("xxx");
		curr.setConversion(Double.valueOf(12.3456));
		curr.setDigits(Integer.valueOf(1));

		assertEquals(1, validationService.validate(curr).size());
	}

	@Test
	public void testDigitsFalse2()
	{
		createDigitConstraint();

		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("xxx");
		curr.setConversion(Double.valueOf(112.3));
		curr.setDigits(Integer.valueOf(1));

		assertEquals(1, validationService.validate(curr).size());
	}

	@Test
	public void testDigitsFalse3()
	{
		createDigitConstraint();

		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("xxx");
		curr.setConversion(Double.valueOf(112.345));
		curr.setDigits(Integer.valueOf(1));

		assertEquals(1, validationService.validate(curr).size());
	}

	/**
	 * The value is 2.3, but the value is set as 02.300 (2 before comma, 3 after comma) and it is valid! wow!
	 */
	@Test
	public void testDigitsOk2()
	{
		createDigitConstraint();

		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("xxx");
		curr.setConversion(Double.valueOf(02.300));
		curr.setDigits(Integer.valueOf(1));
		assertEquals(Double.valueOf(2.3), curr.getConversion());
		assertEquals(0, validationService.validate(curr).size());
	}
}
