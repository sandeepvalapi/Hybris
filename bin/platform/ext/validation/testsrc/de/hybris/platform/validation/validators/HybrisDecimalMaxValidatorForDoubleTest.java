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
import de.hybris.platform.validation.annotations.HybrisDecimalMax;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;


/**
 * Note precision for upper limits is highly decreased due to problems with rounding for {@link Double} comparison
 */
@UnitTest
public class HybrisDecimalMaxValidatorForDoubleTest extends AbstractHybrisNumberValidatorTest<Double, HybrisDecimalMax>
{
	@Before
	public void init()
	{
		validator = new HybrisDecimalMaxValidator();
	}

	//small number cases
	@Test
	public void testCheckLowerBorderCaseGreater()
	{
		validator.initialize(prepareMinAnnotationInstance());
		final Double borderValueMax = getValueAboveGivenMin(getBorderCaseMin());
		assertIsNotValidValue(borderValueMax);
	}

	@Test
	public void testCheckLowerBorderCaseLower()
	{
		validator.initialize(prepareMinAnnotationInstance());
		final Double borderValueMax = getValueBelowGivenMin(getBorderCaseMin());
		assertIsValidValue(borderValueMax);
	}

	@Test
	public void testCheckLowerBorderCaseEqual()
	{
		validator.initialize(prepareMinAnnotationInstance());
		assertIsValidValue(getBorderCaseMin());
	}

	@Override
	protected Double getValueAboveGivenMin(final Double borderValueMax)
	{
		final BigDecimal decimal = BigDecimal.valueOf(borderValueMax.doubleValue());
		final BigDecimal deltaValue = new BigDecimal(BigInteger.ONE, 320);
		return Double.valueOf(decimal.add(deltaValue).doubleValue());
	}

	@Override
	protected Double getValueBelowGivenMin(final Double borderValueMax)
	{
		final BigDecimal decimal = BigDecimal.valueOf(borderValueMax.doubleValue());
		final BigDecimal deltaValue = new BigDecimal(BigInteger.ONE, 320);
		return Double.valueOf(decimal.subtract(deltaValue).doubleValue());
	}

	@Override
	protected Double getBorderCaseMin()
	{
		final BigDecimal borderCaseValue = new BigDecimal(BigInteger.valueOf(4), 320);
		return Double.valueOf(borderCaseValue.doubleValue());
	}

	//	big number cases
	@Test
	public void testCheckUpperBorderCaseGreater()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		final Double borderValueMax = getValueAboveGivenMax(getBorderCaseMax());
		assertIsNotValidValue(borderValueMax);
	}

	@Test
	public void testCheckUpperBorderCaseLower()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		final Double borderValueMax = getValueBelowGivenMax(getBorderCaseMax());
		assertIsValidValue(borderValueMax);
	}

	@Test
	public void testCheckUpperBorderCaseEqual()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		assertIsValidValue(getBorderCaseMax());
	}

	@Override
	protected Double getValueAboveGivenMax(final Double borderValueMax)
	{
		final BigDecimal decimal = BigDecimal.valueOf(borderValueMax.doubleValue());
		return Double.valueOf(decimal.add(BigDecimal.ONE).doubleValue());
	}

	@Override
	protected Double getValueBelowGivenMax(final Double borderValueMax)
	{
		final BigDecimal decimal = BigDecimal.valueOf(borderValueMax.doubleValue());
		return Double.valueOf(decimal.subtract(BigDecimal.ONE).doubleValue());
	}

	@Override
	protected Double getBorderCaseMax()
	{
		final BigDecimal borderCaseMaxValue = new BigDecimal(BigInteger.valueOf(1), -10);
		return Double.valueOf(borderCaseMaxValue.doubleValue());
	}
}
