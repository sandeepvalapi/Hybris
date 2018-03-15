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
 * Testing the DecimalMax Constraint.
 */
@IntegrationTest
@Ignore("due to PLA-11224")
public class DecimalMaxConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: CurrencyModel.conversition must be equal or below 1.0000001
	 */
	private void createDecimalMaxConstraint()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.CONVERSION);
		final DecimalMaxConstraintModel decimalMax = new DecimalMaxConstraintModel();
		modelService.initDefaults(decimalMax);
		decimalMax.setId("decimalMax");
		decimalMax.setValue(BigDecimal.valueOf(1.0000001));
		decimalMax.setDescriptor(attrDesc);
		modelService.save(decimalMax);
		Assert.assertEquals(getDefaultMessage(Constraint.DECIMAL_MAX.msgKey), decimalMax.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	/**
	 * must fail because value is 0.000000001 larger as rule
	 */
	@Test
	public void testMaxConstraintFails1()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.000000101));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		modelService.save(curr);

		assertEquals(BigDecimal.valueOf(curr.getConversion().doubleValue()), BigDecimal.valueOf(1.000000101));
		createDecimalMaxConstraint();
		assertFalse(validationService.validate(curr).isEmpty());
	}

	@Test
	public void testMaxConstraintEqual()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.0000001));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		modelService.save(curr);

		assertEquals(BigDecimal.valueOf(curr.getConversion().doubleValue()), BigDecimal.valueOf(1.0000001));
		createDecimalMaxConstraint();
		assertFalse(validationService.validate(curr).isEmpty());
	}

	/**
	 * must fail because value is 0.000000001 larger as rule
	 */
	@Test
	public void testMaxConstraintFalse2()
	{
		createDecimalMaxConstraint();

		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.000000101));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		try
		{
			modelService.save(curr);
			fail("test should fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.DECIMAL_MAX.msgKey, "conversion");
		}
	}

	/**
	 * is ok, difference is 0.00000000001 below border
	 */
	@Test
	public void testMaxConstraintOk1()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.00000009999));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		modelService.save(curr);

		createDecimalMaxConstraint();
		assertTrue(validationService.validate(curr).isEmpty());
	}

	/**
	 * is ok, difference is 0.00000000001 below border
	 */
	@Test
	public void testMaxConstraintOk2()
	{
		createDecimalMaxConstraint();

		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.00000009999));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		modelService.save(curr);
	}
}
