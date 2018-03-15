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
package de.hybris.platform.order;


import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.order.daos.PaymentModeDao;
import de.hybris.platform.order.impl.DefaultPaymentModeService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class PaymentModeServiceTest
{
	private DefaultPaymentModeService defaultPaymentModeService;

	@Mock
	private PaymentModeDao paymentModeDao;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultPaymentModeService = new DefaultPaymentModeService();
		defaultPaymentModeService.setPaymentModeDao(paymentModeDao);

	}

	@Test
	public void testFindPaymentModeByCodeDublicatedError()
	{
		final String code = "code";
		final PaymentModeModel paymentModeModel1 = new PaymentModeModel();
		final PaymentModeModel paymentModeModel2 = new PaymentModeModel();

		Mockito.when(paymentModeDao.findPaymentModeForCode(code)).thenReturn(Arrays.asList(paymentModeModel1, paymentModeModel2));

		try
		{
			defaultPaymentModeService.getPaymentModeForCode(code);
			fail();
		}
		catch (final AmbiguousIdentifierException e)//NOPMD
		{
			//ok
		}
		catch (final Exception e)
		{
			fail("got unknown exception " + e);
		}


	}

	@Test
	public void testFindPaymentModeByCode()
	{
		final String code = "code";
		final PaymentModeModel paymentModeModel1 = new PaymentModeModel();


		Mockito.when(paymentModeDao.findPaymentModeForCode(code)).thenReturn(Arrays.asList(paymentModeModel1));

		assertThat(paymentModeModel1).isEqualTo(defaultPaymentModeService.getPaymentModeForCode(code));

	}


	@Test
	public void testFindPaymentModeByCodeError()
	{
		final String code = "code";

		Mockito.when(paymentModeDao.findPaymentModeForCode(code)).thenReturn(new ArrayList<PaymentModeModel>());

		try
		{
			defaultPaymentModeService.getPaymentModeForCode(code);
			fail();
		}
		catch (final UnknownIdentifierException e)//NOPMD
		{
			//ok
		}
		catch (final Exception e)
		{
			fail("got unknown exception " + e);
		}

	}

	@Test
	public void testFindPaymentModeByCodeNullCode()
	{
		try
		{
			defaultPaymentModeService.getPaymentModeForCode(null);
			fail();
		}
		catch (final IllegalArgumentException e)//NOPMD
		{
			//ok
		}
		catch (final Exception e)
		{
			fail("got unknown exception " + e);
		}
	}


	@Test
	public void testGetAllPaymentModes()
	{

		final PaymentModeModel paymentModeModel1 = new PaymentModeModel();
		final PaymentModeModel paymentModeModel2 = new PaymentModeModel();


		Mockito.when(paymentModeDao.findAllPaymentModes()).thenReturn(Arrays.asList(paymentModeModel1, paymentModeModel2));

		final List<PaymentModeModel> res = defaultPaymentModeService.getAllPaymentModes();

		assertThat(res).containsExactly(paymentModeModel1, paymentModeModel2);

	}

}
