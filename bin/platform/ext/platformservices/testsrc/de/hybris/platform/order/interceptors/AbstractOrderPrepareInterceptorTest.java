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

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class AbstractOrderPrepareInterceptorTest
{

	private DefaultAbstractOrderPrepareInterceptor interceptor;

	@Mock
	private KeyGenerator mockKeyGenerator;

	@Mock
	private InterceptorContext mockInterceptorContext;

	private OrderModel order;
	private AbstractOrderEntryModel entry;

	@Before
	public void init() throws JaloSystemException
	{
		MockitoAnnotations.initMocks(this);

		interceptor = new DefaultAbstractOrderPrepareInterceptor();
		interceptor.setKeyGenerator(mockKeyGenerator);

		order = new OrderModel();
		order.setCalculated(Boolean.TRUE);
		entry = new OrderEntryModel();
		entry.setCalculated(Boolean.TRUE);
		order.setEntries(Collections.singletonList(entry));
		order.setCode("123");
	}


	/**
	 * Tests order prepare interceptor: check the status of 'calculated' flag and the automatically generated code value.
	 */
	@Test
	public void testOrderPrepareInterceptor() throws Exception
	{
		final OrderModel newOrder1 = new OrderModel();
		newOrder1.setCalculated(Boolean.TRUE);
		recordMockInterceptorContext(newOrder1, "");

		newOrder1.setCode("testOrder1");
		interceptor.onPrepare(newOrder1, mockInterceptorContext);

		assertEquals("Order code is incorrect", "testOrder1", newOrder1.getCode());
		assertTrue("Order should be still calculated", newOrder1.getCalculated().booleanValue());

		when(mockKeyGenerator.generate()).thenReturn("0001");

		final OrderModel newOrder2 = new OrderModel();
		recordMockInterceptorContext(newOrder2, "");
		newOrder2.setCalculated(Boolean.TRUE);
		interceptor.onPrepare(newOrder2, mockInterceptorContext);

		assertFalse("Order should not be calculated", newOrder2.getCalculated().booleanValue());
		assertNotNull("Order code should not be null", newOrder2.getCode());
		assertEquals("Order code was incorrect", "0001", newOrder2.getCode());
	}

	@Test
	public void testOrderPrepareInterceptorStatusChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.STATUS);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, true, true);
	}

	@Test
	public void testOrderPrepareInterceptorDateChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.DATE);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, false);
	}

	@Test
	public void testOrderPrepareInterceptorUserChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.USER);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, false);
	}

	@Test
	public void testOrderPrepareInterceptorCurrencyChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.CURRENCY);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, false);
	}

	@Test
	public void testOrderPrepareInterceptorNetChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.NET);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, false);
	}

	@Test
	public void testOrderPrepareInterceptorDeliveryModeChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.DELIVERYMODE);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorDeliveryCostChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.DELIVERYCOST);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorPaymentModeChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.PAYMENTMODE);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorPaymentCostChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.PAYMENTCOST);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorDiscountsChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.DISCOUNTS);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorDiscountDeliveryCostFlagChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.DISCOUNTSINCLUDEDELIVERYCOST);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorDiscountPaymentCostFlagChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.DISCOUNTSINCLUDEPAYMENTCOST);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorTotalTaxValuesChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.TOTALTAXVALUES);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorDeliveryAddressChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.DELIVERYADDRESS);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}

	@Test
	public void testOrderPrepareInterceptorPaymentAddressChange() throws Exception
	{
		recordMockInterceptorContext(order, AbstractOrderModel.PAYMENTADDRESS);
		interceptor.onPrepare(order, mockInterceptorContext);
		assertOrderCalculatedStatus(order, false, true);
	}



	private void recordMockInterceptorContext(final AbstractOrderModel order, final String attributeChanged)
	{
		final Collection<String> parameters = Arrays
				.asList(AbstractOrderModel.DATE, AbstractOrderModel.USER, AbstractOrderModel.CURRENCY, AbstractOrderModel.NET,
						AbstractOrderModel.DELIVERYMODE, AbstractOrderModel.DELIVERYCOST, AbstractOrderModel.PAYMENTMODE,
						AbstractOrderModel.PAYMENTCOST, AbstractOrderModel.TOTALTAXVALUES, AbstractOrderModel.DISCOUNTS,
						AbstractOrderModel.DISCOUNTSINCLUDEDELIVERYCOST, AbstractOrderModel.DISCOUNTSINCLUDEPAYMENTCOST,
						AbstractOrderModel.DELIVERYADDRESS, AbstractOrderModel.PAYMENTADDRESS);

		for (final String parameter : parameters)
		{
			if (StringUtils.equals(attributeChanged, parameter))
			{
				when(Boolean.valueOf(mockInterceptorContext.isModified(order, parameter))).thenReturn(Boolean.TRUE);
			}
			else
			{
				when(Boolean.valueOf(mockInterceptorContext.isModified(order, parameter))).thenReturn(Boolean.FALSE);
			}
		}
	}

	private void assertOrderCalculatedStatus(final AbstractOrderModel order, final boolean expectedOrderFlag,
			final boolean expectedEntryFlag)
	{
		assertTrue("Order should " + (expectedOrderFlag ? "" : " not ") + " be calculated",
				order.getCalculated().booleanValue() == expectedOrderFlag);
		if (order.getEntries() != null)
		{
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				assertTrue("Order entry should " + (expectedEntryFlag ? "" : " not ") + " be calculated", entry.getCalculated()
						.booleanValue() == expectedEntryFlag);
			}
		}
	}
}
