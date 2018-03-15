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
package de.hybris.platform.validation.validators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.validation.annotations.HybrisDecimalMin;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class HybrisDecimalMinValidatorForBigDecimalTest extends AbstractHybrisNumberValidatorTest<BigDecimal, HybrisDecimalMin>
{
	@Before
	public void init()
	{
		validator = new HybrisDecimalMinValidator();
	}

	private final BigDecimal borderCaseMinValue = new BigDecimal(BigInteger.valueOf(3), -Double.MIN_EXPONENT);
	private final BigDecimal borderCaseMaxValue = new BigDecimal(BigInteger.valueOf(1), -Double.MAX_EXPONENT);

	//small number cases
	@Test
	public void testCheckLowerBorderCaseGreater()
	{
		validator.initialize(prepareMinAnnotationInstance());
		final BigDecimal borderValueMax = getValueAboveGivenMin(getBorderCaseMin());
		assertIsValidValue(borderValueMax);
	}

	@Test
	public void testCheckLowerBorderCaseLower()
	{
		validator.initialize(prepareMinAnnotationInstance());
		final BigDecimal borderValueMax = getValueBelowGivenMin(getBorderCaseMin());
		assertIsNotValidValue(borderValueMax);
	}

	@Test
	public void testCheckLowerBorderCaseEqual()
	{
		validator.initialize(prepareMinAnnotationInstance());
		assertIsValidValue(getBorderCaseMin());
	}

	@Override
	protected BigDecimal getValueAboveGivenMin(final BigDecimal borderValueMax)
	{
		final BigDecimal deltaValue = new BigDecimal(BigInteger.ONE, -Double.MIN_EXPONENT);
		return borderValueMax.add(deltaValue);
	}

	@Override
	protected BigDecimal getValueBelowGivenMin(final BigDecimal borderValueMax)
	{
		final BigDecimal deltaValue = new BigDecimal(BigInteger.ONE, -Double.MIN_EXPONENT);
		return borderValueMax.subtract(deltaValue);
	}

	@Override
	protected BigDecimal getBorderCaseMin()
	{
		return borderCaseMinValue;
	}

	//	big number cases
	@Test
	public void testCheckUpperBorderCaseGreater()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		final BigDecimal borderValueMax = getValueAboveGivenMax(getBorderCaseMax());
		assertIsValidValue(borderValueMax);
	}

	@Test
	public void testCheckUpperBorderCaseLower()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		final BigDecimal borderValueMax = getValueBelowGivenMax(getBorderCaseMax());
		assertIsNotValidValue(borderValueMax);
	}

	@Test
	public void testCheckUpperBorderCaseEqual()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		assertIsValidValue(getBorderCaseMax());
	}

	@Override
	protected BigDecimal getValueAboveGivenMax(final BigDecimal borderValueMax)
	{
		final BigDecimal deltaValue = new BigDecimal(BigInteger.ONE, -Double.MIN_EXPONENT);
		return borderValueMax.add(deltaValue);
	}

	@Override
	protected BigDecimal getValueBelowGivenMax(final BigDecimal borderValueMax)
	{
		final BigDecimal deltaValue = new BigDecimal(BigInteger.ONE, -Double.MIN_EXPONENT);
		return borderValueMax.subtract(deltaValue);
	}

	@Override
	protected BigDecimal getBorderCaseMax()
	{
		return borderCaseMaxValue;
	}
}
