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
package de.hybris.platform.validation.model.constraints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;


/**
 * Testing the DecimalMin Constraint.
 */
@IntegrationTest
public class HybrisDecimalMinConstraintTest extends AbstractConstraintTest
{
    private static final Logger LOG = Logger.getLogger(HybrisDecimalMinConstraintTest.class);

    private double testedValue;
    private double delta;

    @Before
    public void prepareDelta()
    {
        delta = Math.pow(10D, calculateFractionPrecision(1));
        testedValue = 1D + Math.pow(10D, calculateFractionPrecision(0));

        LOG.info(" detecting precision " + this.getClass().getSimpleName() + " value (" + testedValue + ") delta (" +
                String.format("%.10f", delta) + ") ");
    }

    /**
	 * Testsample: CurrencyModel.conversition must be equal or above 1.0000001
	 */
	private void createDecimalMinConstraint()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.CONVERSION);
		final HybrisDecimalMinConstraintModel decimalMin = new HybrisDecimalMinConstraintModel();
		modelService.initDefaults(decimalMin);
		decimalMin.setId("decimalMin");
		decimalMin.setValue(BigDecimal.valueOf(testedValue));
		decimalMin.setDescriptor(attrDesc);
		modelService.save(decimalMin);
		Assert.assertEquals(getDefaultMessage(Constraint.HYBRIS_DECIMAL_MIN.msgKey), decimalMin.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	/**
	 * {@link HybrisDecimalMaxConstraintTest}, here it is the opposite
	 */
	@Test
	public void testMinConstraintGreaterExplicit()
	{
		final CurrencyModel curr = createCurrencyModel(testedValue + delta);
		modelService.save(curr);
		assertEquals(BigDecimal.valueOf(curr.getConversion().doubleValue()), BigDecimal.valueOf(testedValue + delta));
		createDecimalMinConstraint();
		assertTrue(validationService.validate(curr).isEmpty());
	}

	@Test
	public void testMinConstraintEqualExplicit()
	{
		final CurrencyModel curr = createCurrencyModel(testedValue);
		assertEquals(BigDecimal.valueOf(curr.getConversion().doubleValue()), BigDecimal.valueOf(testedValue));
		createDecimalMinConstraint();
		assertTrue(validationService.validate(curr).isEmpty());
	}

	/**
	 * {@link HybrisDecimalMaxConstraintTest}, here it is the opposite
	 */
	@Test
	public void testMinConstraintGreaterImplicit()
	{
		createDecimalMinConstraint();
		final CurrencyModel curr = createCurrencyModel(testedValue + delta);
		modelService.save(curr);
	}

	/**
	 * {@link HybrisDecimalMaxConstraintTest},  here it is the opposite
	 */
	@Test
	public void testMinConstraintLowerExplicit()
	{
		final CurrencyModel curr = createCurrencyModel(testedValue - delta);
		createDecimalMinConstraint();
		assertFalse(validationService.validate(curr).isEmpty());
	}

	/**
	 * {@link HybrisDecimalMaxConstraintTest},  here it is the opposite
	 */
	@Test
	public void testMinConstraintLowerImplicit()
	{
		createDecimalMinConstraint();
		final CurrencyModel curr = createCurrencyModel(testedValue - delta);
		try
		{
			modelService.save(curr);
			fail("test should fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.HYBRIS_DECIMAL_MIN.msgKey, "conversion");
		}
	}

	private CurrencyModel createCurrencyModel(final double value)
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(value));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(0));
		return curr;
	}
}
