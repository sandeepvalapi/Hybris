/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.model.ModelService;
import com.hybris.training.fulfilmentprocess.actions.order.TakePaymentAction;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class TakePaymentActionTest
{
	@Mock
	private PaymentService paymentService;
	@Mock
	private ModelService modelService;

	@InjectMocks
	private TakePaymentAction action;

	private OrderProcessModel businessProcessModel;
	private OrderModel order;


	@Before
	public void setup()
	{
		businessProcessModel = mock(OrderProcessModel.class);
		order = new OrderModel();
		given(businessProcessModel.getOrder()).willReturn(order);

	}

	protected PaymentTransactionModel createPaymentTransactionWithStatus(final TransactionStatus transactionStatus)
	{
		final PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
		final PaymentInfoModel paymentInfo = new CreditCardPaymentInfoModel();
		final PaymentTransactionEntryModel entry = new PaymentTransactionEntryModel();

		given(paymentService.capture(paymentTransaction)).willReturn(entry);

		entry.setTransactionStatus(transactionStatus.name());
		paymentTransaction.setInfo(paymentInfo);
		return paymentTransaction;
	}

	@Test
	public void shouldExecuteReturnOKforAcceptedTransaction()
	{
		order.setPaymentTransactions(Collections.singletonList(createPaymentTransactionWithStatus(TransactionStatus.ACCEPTED)));
		Assert.assertEquals("Execution should return OK", AbstractSimpleDecisionAction.Transition.OK,
				action.executeAction(businessProcessModel));
	}

	@Test
	public void shouldExecuteReturnNOKforRejectedTransaction()
	{
		order.setPaymentTransactions(Collections.singletonList(createPaymentTransactionWithStatus(TransactionStatus.REJECTED)));
		Assert.assertEquals("Execution should return NOK", AbstractSimpleDecisionAction.Transition.NOK,
				action.executeAction(businessProcessModel));
	}

	@Test
	public void shouldExecuteReturnOKforMultipleAcceptedTransaction()
	{
		order.setPaymentTransactions(Arrays.asList(createPaymentTransactionWithStatus(TransactionStatus.ACCEPTED),
				createPaymentTransactionWithStatus(TransactionStatus.ACCEPTED)));
		Assert.assertEquals("Execution should return OK", AbstractSimpleDecisionAction.Transition.OK,
				action.executeAction(businessProcessModel));
		// TakePamentAction support multiple payment types on the same order
		verify(paymentService, times(2)).capture(any(PaymentTransactionModel.class));
	}

	@Test
	public void shouldExecuteReturnNOKforAtLeastOneRejectedTransaction()
	{
		order.setPaymentTransactions(Arrays.asList(createPaymentTransactionWithStatus(TransactionStatus.ACCEPTED),
				createPaymentTransactionWithStatus(TransactionStatus.REJECTED)));
		Assert.assertEquals("Execution should return NOK", AbstractSimpleDecisionAction.Transition.NOK,
				action.executeAction(businessProcessModel));
	}
}
