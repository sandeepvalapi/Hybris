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
 * Testing the Min Constraint
 */
@IntegrationTest
public class MinConstraintTest extends AbstractConstraintTest
{
	private static final int BIG_VALUE = Integer.MAX_VALUE - 1;
	private static final int SMALL_VALUE = 2;

	@Test
	public void testMinConstraintUpperBorderCaseOneExplicit()
	{
		final CurrencyModel curr = createModel(1);
		createMaxConstraint(BIG_VALUE);
		assertExpectSaveFailed(curr);
	}

	@Test
	public void testMinConstraintUpperBorderCaseOneImplicit()
	{
		final CurrencyModel curr = createModel(1);
		modelService.save(curr);
		createMaxConstraint(BIG_VALUE);
		validateViolations(curr);
	}

	@Test
	public void testMinConstraintUpperBorderCaseBelowExplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE - 1);
		createMaxConstraint(BIG_VALUE);
		assertExpectSaveFailed(curr);
	}

	@Test
	public void testMinConstraintUpperBorderCaseBelowImplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE - 1);
		modelService.save(curr);
		createMaxConstraint(BIG_VALUE);
		validateViolations(curr);
	}

	@Test
	public void testMinConstraintUpperBorderCaseEqualExplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE);
		createMaxConstraint(BIG_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMinConstraintUpperBorderCaseEqualImplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE);
		modelService.save(curr);
		createMaxConstraint(BIG_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMinConstraintUpperBorderCaseAboveExplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE + 1);
		createMaxConstraint(BIG_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMinConstraintUpperBorderCaseAboveImplicit()
	{
		final CurrencyModel curr = createModel(BIG_VALUE + 1);
		modelService.save(curr);
		createMaxConstraint(BIG_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMinConstraintLowerBorderCaseOneExplicit()
	{
		final CurrencyModel curr = createModel(1);
		createMaxConstraint(SMALL_VALUE);
		assertExpectSaveFailed(curr);
	}

	@Test
	public void testMinConstraintLowerBorderCaseOneImplicit()
	{
		final CurrencyModel curr = createModel(1);
		modelService.save(curr);
		createMaxConstraint(SMALL_VALUE);
		validateViolations(curr);
	}

	@Test
	public void testMinConstraintLowerBorderCaseBelowExplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE - 1);
		createMaxConstraint(SMALL_VALUE);
		assertExpectSaveFailed(curr);
	}

	@Test
	public void testMinConstraintLowerBorderCaseBelowImplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE - 1);
		modelService.save(curr);
		createMaxConstraint(SMALL_VALUE);
		validateViolations(curr);
	}

	@Test
	public void testMinConstraintLowerBorderCaseEqualExplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE);
		createMaxConstraint(SMALL_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMinConstraintLowerBorderCaseEqualImplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE);
		modelService.save(curr);
		createMaxConstraint(SMALL_VALUE);
		validateNoViolations(curr);
	}

	@Test
	public void testMinConstraintLowerBorderCaseAboveExplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE + 1);
		createMaxConstraint(SMALL_VALUE);
		modelService.save(curr);
	}

	@Test
	public void testMinConstraintLowerBorderCaseAboveImplicit()
	{
		final CurrencyModel curr = createModel(SMALL_VALUE + 1);
		modelService.save(curr);
		createMaxConstraint(SMALL_VALUE);
		validateNoViolations(curr);
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

		final MinConstraintModel minConstraint = new MinConstraintModel();
		modelService.initDefaults(minConstraint);
		minConstraint.setId("maxConstraint");
		minConstraint.setValue(Long.valueOf(value));
		minConstraint.setDescriptor(attrDesc);
		modelService.save(minConstraint);
		Assert.assertEquals(getDefaultMessage(Constraint.MIN.msgKey), minConstraint.getDefaultMessage());
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
			assertModelSavingExceptionWithMessageKey(e, Constraint.MIN.msgKey, "digits");
		}
	}
}
