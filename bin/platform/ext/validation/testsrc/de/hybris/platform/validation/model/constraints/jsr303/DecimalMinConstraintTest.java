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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Testing the DecimalMin Constraint.
 */
@IntegrationTest
@Ignore("due to PLA-11224")
public class DecimalMinConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: CurrencyModel.conversition must be equal or above 1.0000001
	 */
	private void createDecimalMinConstraint()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.CONVERSION);
		final DecimalMinConstraintModel decimalMin = new DecimalMinConstraintModel();
		modelService.initDefaults(decimalMin);
		decimalMin.setId("decimalMin");
		decimalMin.setValue(BigDecimal.valueOf(1.0000001));
		decimalMin.setDescriptor(attrDesc);
		modelService.save(decimalMin);
		Assert.assertEquals(getDefaultMessage(Constraint.DECIMAL_MIN.msgKey), decimalMin.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	/**
	 * see DecimalMaxConstraintTest, here it is the opposite
	 */
	@Test
	public void testMinConstraintOk1()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.000000101));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		modelService.save(curr);

		assertEquals(BigDecimal.valueOf(curr.getConversion().doubleValue()), BigDecimal.valueOf(1.000000101));
		createDecimalMinConstraint();
		assertTrue(validationService.validate(curr).isEmpty());
	}

	@Test
	public void testMinConstraintEqual()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.0000001));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		modelService.save(curr);

		assertEquals(BigDecimal.valueOf(curr.getConversion().doubleValue()), BigDecimal.valueOf(1.0000001));
		createDecimalMinConstraint();
		assertTrue(validationService.validate(curr).isEmpty());
	}

	/**
	 * see DecimalMaxConstraintTest, here it is the opposite
	 */
	@Test
	public void testMinConstraintOk2()
	{
		createDecimalMinConstraint();

		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.000000101));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		modelService.save(curr);
	}

	/**
	 * see DecimalMaxConstraintTest, here it is the opposite
	 */
	@Test
	public void testMinConstraintFails1()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.00000009999));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		modelService.save(curr);

		createDecimalMinConstraint();
		assertFalse(validationService.validate(curr).isEmpty());
	}

	/**
	 * see DecimalMaxConstraintTest, here it is the opposite
	 */
	@Test
	public void testMinConstraintFails2()
	{
		createDecimalMinConstraint();

		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.00000009999));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		try
		{
			modelService.save(curr);
			fail("test should fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.DECIMAL_MIN.msgKey, "conversion");
		}
	}
}
