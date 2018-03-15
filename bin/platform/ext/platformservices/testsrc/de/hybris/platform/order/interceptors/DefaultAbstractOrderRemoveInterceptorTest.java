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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultAbstractOrderRemoveInterceptorTest
{
	private DefaultAbstractOrderRemoveInterceptor interceptor;

	@Mock
	private ModelService mockModelService;

	private AbstractOrderModel order;

	@Mock
	private InterceptorContext mockInterceptorContext;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		interceptor = new DefaultAbstractOrderRemoveInterceptor();

		order = new AbstractOrderModel();
		when(mockInterceptorContext.getModelService()).thenReturn(mockModelService);
	}

	@Test
	public void testOnRemoveNullMembers() throws InterceptorException
	{
		interceptor.onRemove(order, mockInterceptorContext);
		//addresses were null
		verify(mockModelService, never()).remove(Mockito.any(AddressModel.class));
		//paymentInfo was null
		verify(mockModelService, never()).remove(Mockito.any(PaymentInfoModel.class));
	}

	@Test
	public void testOnRemoveAdressClonesCascadeRemove() throws InterceptorException
	{
		//different addresses, both clones
		final AddressModel paymentAddress = new AddressModel();
		paymentAddress.setDuplicate(Boolean.TRUE);
		final AddressModel deliveryAddress = new AddressModel();
		deliveryAddress.setDuplicate(Boolean.TRUE);
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentAddress(paymentAddress);

		final PaymentInfoModel paymentInfo = null;
		order.setPaymentInfo(paymentInfo);


		interceptor.onRemove(order, mockInterceptorContext);
		verify(mockModelService, times(1)).remove(paymentAddress);
		verify(mockModelService, times(1)).remove(deliveryAddress);
		//paymentInfo was null
		verify(mockModelService, never()).remove(paymentInfo);

	}

	@Test
	public void testOnRemoveDeliveryAdressCloneCascadeRemove() throws InterceptorException
	{
		//different addresses, 1 clone
		final AddressModel paymentAddress = new AddressModel();
		paymentAddress.setDuplicate(Boolean.TRUE);
		final AddressModel deliveryAddress = new AddressModel();
		//deliveryAddress - not cloned
		deliveryAddress.setDuplicate(Boolean.FALSE);
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentAddress(paymentAddress);

		final PaymentInfoModel paymentInfo = null;
		order.setPaymentInfo(paymentInfo);

		interceptor.onRemove(order, mockInterceptorContext);
		verify(mockModelService, times(1)).remove(paymentAddress);
		//no call for deliveryAddress
		verify(mockModelService, never()).remove(deliveryAddress);
		//paymentInfo was null
		verify(mockModelService, never()).remove(paymentInfo);
	}

	@Test
	public void testOnRemovePaymentAdressCloneCascadeRemove() throws InterceptorException
	{
		//different addresses, 1 clone
		final AddressModel paymentAddress = new AddressModel();
		paymentAddress.setDuplicate(Boolean.FALSE);
		final AddressModel deliveryAddress = new AddressModel();
		deliveryAddress.setDuplicate(Boolean.TRUE);
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentAddress(paymentAddress);

		final PaymentInfoModel paymentInfo = null;
		order.setPaymentInfo(paymentInfo);

		interceptor.onRemove(order, mockInterceptorContext);
		//no call for paymentAddress
		verify(mockModelService, never()).remove(paymentAddress);
		verify(mockModelService, times(1)).remove(deliveryAddress);
		//paymentInfo was null
		verify(mockModelService, never()).remove(paymentInfo);
	}

	@Test
	public void testOnRemoveEqualAdressesCascadeRemove() throws InterceptorException
	{

		final AddressModel deliveryAddress = new AddressModel();
		deliveryAddress.setDuplicate(Boolean.TRUE);
		final AddressModel paymentAddress = deliveryAddress;
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentAddress(paymentAddress);

		final PaymentInfoModel paymentInfo = null;
		order.setPaymentInfo(paymentInfo);

		interceptor.onRemove(order, mockInterceptorContext);
		//only one deletion - paymentAddress
		verify(mockModelService, times(1)).remove(Mockito.any(AddressModel.class));
		verify(mockModelService, times(1)).remove(paymentAddress);
		//paymentInfo was null
		verify(mockModelService, never()).remove(paymentInfo);

	}

	@Test
	public void testOnRemovePaymentInfoCloneCascadeRemove() throws InterceptorException
	{
		final PaymentInfoModel paymentInfo = new PaymentInfoModel();
		paymentInfo.setDuplicate(Boolean.TRUE);
		order.setPaymentInfo(paymentInfo);

		interceptor.onRemove(order, mockInterceptorContext);
		//paymentInfo was a clone
		verify(mockModelService, times(1)).remove(paymentInfo);
	}

	@Test
	public void testOnRemovePaymentInfoCascadeRemove() throws InterceptorException
	{
		final PaymentInfoModel paymentInfo = new PaymentInfoModel();
		paymentInfo.setDuplicate(Boolean.FALSE);
		order.setPaymentInfo(paymentInfo);

		interceptor.onRemove(order, mockInterceptorContext);
		//paymentInfo was NOT a clone
		verify(mockModelService, never()).remove(paymentInfo);
	}

}
