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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Testing the Max Constraint
 */
@IntegrationTest
public class MaxConstraintTest extends AbstractConstraintTest
{
	private static final int BIG_VALUE = Integer.MAX_VALUE - 1;
	private static final int SMALL_VALUE = 2;

	@Test
	public void testMaxConstraintUpperBorderCaseOneExplicit()
	{
		final CurrencyModel curr = createModel(1);
		createMaxConstraint(BIG_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMaxConstraintUpperBorderCaseOneImplicit()
	{
		final CurrencyModel curr = createModel(1);
		modelService.save(curr);
		createMaxConstraint(BIG_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMaxConstraintUpperBorderCaseBelowExplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE - 1);
		createMaxConstraint(BIG_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMaxConstraintUpperBorderCaseBelowImplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE - 1);
		modelService.save(curr);
		createMaxConstraint(BIG_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMaxConstraintUpperBorderCaseEqualExplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE);
		createMaxConstraint(BIG_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMaxConstraintUpperBorderCaseEqualImplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE);
		modelService.save(curr);
		createMaxConstraint(BIG_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMaxConstraintUpperBorderCaseAboveExplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE + 1);
		createMaxConstraint(BIG_VALUE);
		assertExpectSaveFailed(curr);
	}

	@Test
	public void testMaxConstraintUpperBorderCaseAboveImplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE + 1);
		modelService.save(curr);
		createMaxConstraint(BIG_VALUE);
		validateViolations(curr);
	}

	@Test
	public void testMaxConstraintLowerBorderCaseOneExplicit()
	{
		final CurrencyModel curr = createModel(1);
		createMaxConstraint(SMALL_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMaxConstraintLowerBorderCaseOneImplicit()
	{
		final CurrencyModel curr = createModel(1);
		modelService.save(curr);
		createMaxConstraint(SMALL_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMaxConstraintLowerBorderCaseBelowExplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE - 1);
		createMaxConstraint(SMALL_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMaxConstraintLowerBorderCaseBelowImplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE - 1);
		modelService.save(curr);
		createMaxConstraint(SMALL_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMaxConstraintLowerBorderCaseEqualExplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE);
		createMaxConstraint(SMALL_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMaxConstraintLowerBorderCaseEqualImplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE);
		modelService.save(curr);
		createMaxConstraint(SMALL_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMaxConstraintLowerBorderCaseAboveExplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE + 1);
		createMaxConstraint(SMALL_VALUE);
		assertExpectSaveFailed(curr);
	}

	@Test
	public void testMaxConstraintLowerBorderCaseAboveImplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE + 1);
		modelService.save(curr);
		createMaxConstraint(SMALL_VALUE);
		validateViolations(curr);
	}

	private void validateNoViolations(final CurrencyModel curr)
	{
		assertTrue(validationService.validate(curr).isEmpty());
	}

	private void validateViolations(final CurrencyModel curr)
	{
		assertFalse(validationService.validate(curr).isEmpty());
	}

	private CurrencyModel createModel(final int digits)
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("curr");
		curr.setActive(Boolean.TRUE);
		curr.setSymbol("C");
		curr.setDigits(Integer.valueOf(digits));
		curr.setConversion(Double.valueOf(2.0));
		return curr;
	}

	private void createMaxConstraint(final long value)
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.DIGITS);

		final MaxConstraintModel maxConstraint = new MaxConstraintModel();
		modelService.initDefaults(maxConstraint);
		maxConstraint.setId("maxConstraint");
		maxConstraint.setValue(Long.valueOf(value));
		maxConstraint.setDescriptor(attrDesc);
		modelService.save(maxConstraint);
		Assert.assertEquals(getDefaultMessage(Constraint.MAX.msgKey), maxConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	private void assertExpectSaveFailed(final CurrencyModel curr)
	{
		try
		{
			modelService.save(curr);
			fail("test should fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.MAX.msgKey, "digits");
		}
	}
}
