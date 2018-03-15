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

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */

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
 * Testing the HybrisDecimalMax Constraint.
 */
@IntegrationTest
public class HybrisDecimalMaxConstraintTest extends AbstractConstraintTest
{
    private static final Logger LOG = Logger.getLogger(HybrisDecimalMaxConstraintTest.class);

    private double testedValue;
    private double delta;

    @Before
    public void prepareDelta(){
        delta = Math.pow(10D, calculateFractionPrecision(1));
        testedValue = 1D + Math.pow(10D, calculateFractionPrecision(0));
        LOG.info(" detecting precision "+this.getClass().getSimpleName()+" value ("+ testedValue +") delta ("+  String.format
					 ("%.10f", Double.valueOf(delta)) +") ");
    }

    /**
	 * Testsample: CurrencyModel.conversition must be equal or below {@link #testedValue} +/- {@link #delta}
	 */
	private void createDecimalMaxConstraint()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.CONVERSION);
		final HybrisDecimalMaxConstraintModel decimalMax = new HybrisDecimalMaxConstraintModel();
		modelService.initDefaults(decimalMax);
		decimalMax.setId("decimalMax");
		decimalMax.setValue(BigDecimal.valueOf(testedValue));
		decimalMax.setDescriptor(attrDesc);
		modelService.save(decimalMax);
		Assert.assertEquals(getDefaultMessage(Constraint.HYBRIS_DECIMAL_MAX.msgKey), decimalMax.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	private CurrencyModel createCurrencyForValidation(final double value)
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(value));
		curr.setSymbol("CUR");
		curr.setDigits(Integer.valueOf(7));
		return curr;
	}

	/**
	 * must fail because value is a {@link #delta} larger than constraint
	 */
	@Test
	public void testMaxConstraintGreaterExplicit()
	{
		final CurrencyModel curr = createCurrencyForValidation(testedValue + delta);
		modelService.save(curr);
		assertEquals(BigDecimal.valueOf(curr.getConversion().doubleValue()), BigDecimal.valueOf(testedValue + delta));
		createDecimalMaxConstraint();
		assertFalse(validationService.validate(curr).isEmpty());
	}

	@Test
	public void testMaxConstraintEqual()
	{
		final CurrencyModel curr = createCurrencyForValidation(testedValue);
		modelService.save(curr);

		assertEquals(BigDecimal.valueOf(curr.getConversion().doubleValue()), BigDecimal.valueOf(testedValue));
		createDecimalMaxConstraint();
		assertFalse(validationService.validate(curr).isEmpty());
	}

	/**
	 * must fail because value is a {@link #delta} larger than constraint
	 */
	@Test
	public void testMaxConstraintGreaterImplicit()
	{
		createDecimalMaxConstraint();

		final CurrencyModel curr = createCurrencyForValidation(testedValue + delta);
		try
		{
			modelService.save(curr);
			fail("test should fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.HYBRIS_DECIMAL_MAX.msgKey, "conversion");
		}
	}

	/**
	 * is ok, difference is a {@link #delta} smaller than constraint
	 */
	@Test
	public void testMaxConstraintLowerExplicit()
	{
		final CurrencyModel curr = createCurrencyForValidation(testedValue - delta);
		modelService.save(curr);

		createDecimalMaxConstraint();
		assertTrue(validationService.validate(curr).isEmpty());
	}

	/**
	 * is ok, difference is a {@link #delta} larger than constraint
	 */
	@Test
	public void testMaxConstraintLowerImplicit()
	{
		createDecimalMaxConstraint();
		final CurrencyModel curr = createCurrencyForValidation(testedValue - delta);
		modelService.save(curr);
	}
}
