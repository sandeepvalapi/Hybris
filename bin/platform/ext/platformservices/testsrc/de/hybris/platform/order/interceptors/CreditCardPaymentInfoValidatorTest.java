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
package de.hybris.platform.order.interceptors;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.order.strategies.paymentinfo.CreditCardNumberHelper;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CreditCardPaymentInfoValidatorTest
{

	private CreditCardPaymentInfoValidator interceptor;

	@Mock
	private InterceptorContext ctx;

	@Mock
	private CreditCardNumberHelper mockCreditCardHelper;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		interceptor = new CreditCardPaymentInfoValidator();
		interceptor.setCreditCardNumberHelper(mockCreditCardHelper);
	}

	/**
	 * Tests what happens if the card data: number and type were unchanged.
	 */
	@Test
	public void testUnchanged() throws BusinessException
	{
		final CreditCardPaymentInfoModel creditCardPaymentInfo = new CreditCardPaymentInfoModel();
		creditCardPaymentInfo.setType(CreditCardType.VISA);
		creditCardPaymentInfo.setNumber("234");
		when(Boolean.valueOf(ctx.isModified(creditCardPaymentInfo, CreditCardPaymentInfoModel.NUMBER))).thenReturn(Boolean.FALSE);
		when(Boolean.valueOf(ctx.isModified(creditCardPaymentInfo, CreditCardPaymentInfoModel.TYPE))).thenReturn(Boolean.FALSE);
		try
		{
			interceptor.onValidate(creditCardPaymentInfo, ctx);
		}
		catch (final InterceptorException e)
		{
			fail("Unexpected Validator exception");
		}
		//never called, as the card number and type was unchanged
		verify(mockCreditCardHelper, never()).isValidCardNumber(creditCardPaymentInfo.getNumber(), creditCardPaymentInfo.getType());
	}

	@Test
	public void testValid() throws BusinessException
	{

		final CreditCardPaymentInfoModel creditCardPaymentInfo = new CreditCardPaymentInfoModel();
		creditCardPaymentInfo.setType(CreditCardType.VISA);
		creditCardPaymentInfo.setNumber("234");
		when(Boolean.valueOf(ctx.isModified(creditCardPaymentInfo, CreditCardPaymentInfoModel.NUMBER))).thenReturn(Boolean.TRUE);
		when(Boolean.valueOf(ctx.isModified(creditCardPaymentInfo, CreditCardPaymentInfoModel.TYPE))).thenReturn(Boolean.FALSE);
		when(
				Boolean.valueOf(mockCreditCardHelper.isValidCardNumber(creditCardPaymentInfo.getNumber(),
						creditCardPaymentInfo.getType()))).thenReturn(Boolean.TRUE);
		try
		{
			interceptor.onValidate(creditCardPaymentInfo, ctx);
		}
		catch (final InterceptorException e)
		{
			fail("Unexpected Validator exception");
		}
	}

	@Test(expected = InterceptorException.class)
	public void testInValid() throws BusinessException
	{
		final CreditCardPaymentInfoModel creditCardPaymentInfo = new CreditCardPaymentInfoModel();
		creditCardPaymentInfo.setType(CreditCardType.AMEX);
		creditCardPaymentInfo.setNumber("098");
		when(Boolean.valueOf(ctx.isModified(creditCardPaymentInfo, CreditCardPaymentInfoModel.NUMBER))).thenReturn(Boolean.FALSE);
		when(Boolean.valueOf(ctx.isModified(creditCardPaymentInfo, CreditCardPaymentInfoModel.TYPE))).thenReturn(Boolean.TRUE);
		when(
				Boolean.valueOf(mockCreditCardHelper.isValidCardNumber(creditCardPaymentInfo.getNumber(),
						creditCardPaymentInfo.getType()))).thenReturn(Boolean.FALSE);
		interceptor.onValidate(creditCardPaymentInfo, ctx);
	}
}
