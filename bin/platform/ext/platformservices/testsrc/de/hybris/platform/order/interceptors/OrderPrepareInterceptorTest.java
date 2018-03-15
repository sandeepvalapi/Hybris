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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.strategies.ordercloning.OrderPartOfMembersCloningStrategy;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Unit test of {@link DefaultOrderPrepareInterceptor}
 */
@UnitTest
public class OrderPrepareInterceptorTest
{

	private DefaultOrderPrepareInterceptor interceptor;

	@Mock
	private OrderPartOfMembersCloningStrategy mockOrderPartOfMembersCloningStrategy;
	@Mock
	private ModelService mockModelService;
	@Mock
	private InterceptorContext mockInterceptorContext;


	private OrderModel order;
	private PaymentInfoModel paymentInfo;
	private AddressModel deliveryAddress;
	private AddressModel paymentAddress;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		interceptor = new DefaultOrderPrepareInterceptor();
		interceptor.setOrderPartOfMembersCloningStrategy(mockOrderPartOfMembersCloningStrategy);

		order = new OrderModel();
		paymentInfo = new PaymentInfoModel();
		paymentAddress = new AddressModel();
		deliveryAddress = new AddressModel();

		when(mockInterceptorContext.getModelService()).thenReturn(mockModelService);
	}

	@Test
	public void testMembersNotChanged() throws InterceptorException
	{
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentAddress(paymentAddress);
		order.setPaymentInfo(paymentInfo);

		recordInterceptorContextModifyReturns(false);

		interceptor.onPrepare(order, mockInterceptorContext);

		//cloning never investigated
		verify(mockOrderPartOfMembersCloningStrategy, never()).addressNeedsCloning(deliveryAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).addressNeedsCloning(paymentAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).paymentInfoNeedsCloning(paymentInfo, order);

		//cloning never performed
		verify(mockOrderPartOfMembersCloningStrategy, never()).cloneAddressForOrder(paymentAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).cloneAddressForOrder(deliveryAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).clonePaymentInfoForOrder(paymentInfo, order);

		//interceptor should not change anything
		Assert.assertEquals("nothing should change", deliveryAddress, order.getDeliveryAddress());
		Assert.assertEquals("nothing should change", paymentAddress, order.getPaymentAddress());
		Assert.assertEquals("nothing should change", paymentInfo, order.getPaymentInfo());
	}

	@Test
	public void testMembersChangedToNull() throws InterceptorException
	{
		interceptor.onPrepare(order, mockInterceptorContext);

		verify(mockOrderPartOfMembersCloningStrategy, never()).addressNeedsCloning(deliveryAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).addressNeedsCloning(paymentAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).paymentInfoNeedsCloning(paymentInfo, order);

		verify(mockOrderPartOfMembersCloningStrategy, never()).cloneAddressForOrder(paymentAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).cloneAddressForOrder(deliveryAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).clonePaymentInfoForOrder(paymentInfo, order);

		Assert.assertNull("nothing should change", order.getDeliveryAddress());
		Assert.assertNull("nothing should change", order.getPaymentAddress());
		Assert.assertNull("nothing should change", order.getPaymentInfo());
	}


	@Test
	public void testMembersChangedToModelsThatDontNeedCloning() throws InterceptorException
	{
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentAddress(paymentAddress);
		order.setPaymentInfo(paymentInfo);

		//..yes they are changed
		recordInterceptorContextModifyReturns(true);
		//..but do not need cloning
		recordCloningStrategyReturns(false);
		recordOrderIsNew();

		interceptor.onPrepare(order, mockInterceptorContext);

		// interceptor asks if cloning needed
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).addressNeedsCloning(deliveryAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).addressNeedsCloning(paymentAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).paymentInfoNeedsCloning(paymentInfo, order);

		//..cloning never occurs.
		verify(mockOrderPartOfMembersCloningStrategy, never()).cloneAddressForOrder(paymentAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).cloneAddressForOrder(deliveryAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, never()).clonePaymentInfoForOrder(paymentInfo, order);


		//interceptor should not change anything
		Assert.assertEquals("nothing should change", deliveryAddress, order.getDeliveryAddress());
		Assert.assertEquals("nothing should change", paymentAddress, order.getPaymentAddress());
		Assert.assertEquals("nothing should change", paymentInfo, order.getPaymentInfo());
	}

	@Test
	public void testMembersChangedToModelsThatNeedCloning() throws InterceptorException
	{
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentAddress(paymentAddress);
		order.setPaymentInfo(paymentInfo);

		//..yes they are changed
		recordInterceptorContextModifyReturns(true);
		//..they do need cloning : i.e. they are owned by the customer
		recordCloningStrategyReturns(true);
		//..will need mocked clones
		recordAddressCloningReturn(deliveryAddress, order);
		recordAddressCloningReturn(paymentAddress, order);
		recordPaymentInfoCloningReturn(order);
		recordOrderIsNew();

		interceptor.onPrepare(order, mockInterceptorContext);

		// interceptor asks if cloning needed
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).addressNeedsCloning(deliveryAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).addressNeedsCloning(paymentAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).paymentInfoNeedsCloning(paymentInfo, order);

		//..cloning occurs.
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).cloneAddressForOrder(paymentAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).cloneAddressForOrder(deliveryAddress, order);
		verify(mockOrderPartOfMembersCloningStrategy, times(1)).clonePaymentInfoForOrder(paymentInfo, order);

		//interceptor should not change anything
		Assert.assertNotSame("Delivery address should change", deliveryAddress, order.getDeliveryAddress());
		Assert.assertNotSame("Payment address should change", paymentAddress, order.getPaymentAddress());
		Assert.assertNotSame("PaymentInfo should change", paymentInfo, order.getPaymentInfo());

		//assert that clones are owned by order
		Assert.assertEquals("Improper clone owner", order, order.getPaymentAddress().getOwner());
		Assert.assertEquals("Improper clone owner", order, order.getPaymentInfo().getOwner());
		Assert.assertEquals("Improper clone owner", order, order.getDeliveryAddress().getOwner());
	}

	private void recordOrderIsNew()
	{
		when(Boolean.valueOf(mockInterceptorContext.isNew(order))).thenReturn(true);
	}

	private void recordInterceptorContextModifyReturns(final boolean returnValue)
	{
		when(Boolean.valueOf(mockInterceptorContext.isModified(order, AbstractOrderModel.DELIVERYADDRESS))).thenReturn(
				Boolean.valueOf(returnValue));
		when(Boolean.valueOf(mockInterceptorContext.isModified(order, AbstractOrderModel.PAYMENTADDRESS))).thenReturn(
				Boolean.valueOf(returnValue));
		when(Boolean.valueOf(mockInterceptorContext.isModified(order, AbstractOrderModel.PAYMENTINFO))).thenReturn(
				Boolean.valueOf(returnValue));
	}

	private void recordCloningStrategyReturns(final boolean returnValue)
	{
		when(Boolean.valueOf(mockOrderPartOfMembersCloningStrategy.addressNeedsCloning(deliveryAddress, order))).thenReturn(
				Boolean.valueOf(returnValue));
		when(Boolean.valueOf(mockOrderPartOfMembersCloningStrategy.addressNeedsCloning(paymentAddress, order))).thenReturn(
				Boolean.valueOf(returnValue));
		when(Boolean.valueOf(mockOrderPartOfMembersCloningStrategy.paymentInfoNeedsCloning(paymentInfo, order))).thenReturn(
				Boolean.valueOf(returnValue));
	}

	private void recordAddressCloningReturn(final AddressModel address, final OrderModel order)
	{
		final AddressModel addressclone = new AddressModel();
		addressclone.setOwner(order);
		when(mockOrderPartOfMembersCloningStrategy.cloneAddressForOrder(address, order)).thenReturn(addressclone);
	}

	private void recordPaymentInfoCloningReturn(final OrderModel order)
	{
		final PaymentInfoModel clone = new PaymentInfoModel();
		clone.setOwner(order);
		when(mockOrderPartOfMembersCloningStrategy.clonePaymentInfoForOrder(paymentInfo, order)).thenReturn(clone);
	}
}
