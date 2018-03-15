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

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class HybrisDecimalMinValidatorForLongTest extends AbstractHybrisNumberValidatorTest<Long, HybrisDecimalMin>
{
	@Before
	public void init()
	{
		validator = new HybrisDecimalMinValidator();
	}

	//small number cases
	@Test
	public void testCheckLowerBorderCaseGreater()
	{
		validator.initialize(prepareMinAnnotationInstance());
		final Long borderValueMax = getValueAboveGivenMin(getBorderCaseMin());
		assertIsValidValue(borderValueMax);
	}

	@Test
	public void testCheckLowerBorderCaseLower()
	{
		validator.initialize(prepareMinAnnotationInstance());
		final Long borderValueMax = getValueBelowGivenMin(getBorderCaseMin());
		assertIsNotValidValue(borderValueMax);
	}

	@Test
	public void testCheckLowerBorderCaseEqual()
	{
		validator.initialize(prepareMinAnnotationInstance());
		assertIsValidValue(getBorderCaseMin());
	}

	@Override
	protected Long getValueAboveGivenMin(final Long borderValueMax)
	{
		return Long.valueOf(borderValueMax.longValue() + 1);
	}

	@Override
	protected Long getValueBelowGivenMin(final Long borderValueMax)
	{
		return Long.valueOf(borderValueMax.longValue() - 1);
	}

	@Override
	protected Long getBorderCaseMin()
	{
		//final BigDecimal borderCaseValue = new BigDecimal(BigInteger.valueOf(-4), -320);
		return Long.valueOf(Long.MIN_VALUE + 1);
	}

	//	big number cases
	@Test
	public void testCheckUpperBorderCaseGreater()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		final Long borderValueMax = getValueAboveGivenMax(getBorderCaseMax());
		assertIsValidValue(borderValueMax);
	}

	@Test
	public void testCheckUpperBorderCaseLower()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		final Long borderValueMax = getValueBelowGivenMax(getBorderCaseMax());
		assertIsNotValidValue(borderValueMax);
	}

	@Test
	public void testCheckUpperBorderCaseEqual()
	{
		validator.initialize(prepareMaxAnnotationInstance());
		assertIsValidValue(getBorderCaseMax());
	}

	@Override
	protected Long getValueAboveGivenMax(final Long borderValueMax)
	{
		return Long.valueOf(borderValueMax.longValue() + 1);
	}

	@Override
	protected Long getValueBelowGivenMax(final Long borderValueMax)
	{
		return Long.valueOf(borderValueMax.longValue() - 1);
	}

	@Override
	protected Long getBorderCaseMax()
	{
		return Long.valueOf(Long.MAX_VALUE - 1);
	}
}
