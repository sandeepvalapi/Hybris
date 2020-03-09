/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import com.hybris.training.fulfilmentprocess.actions.order.CheckTransactionReviewStatusAction;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;


@UnitTest
public class CheckTransactionReviewStatusActionTest
{
	private static final Logger LOG = LoggerFactory.getLogger(CheckTransactionReviewStatusActionTest.class);
	protected static final String OK = "OK";
	protected static final String NOK = "NOK";
	protected static final String WAIT = "WAIT";

	private final CheckTransactionReviewStatusAction action = new CheckTransactionReviewStatusAction();
	private PaymentTransactionEntryModel authorizationAccepted;
	private PaymentTransactionEntryModel authorizationReview;
	private PaymentTransactionEntryModel reviewAccepted;
	private PaymentTransactionEntryModel reviewRejected;
	private OrderProcessModel process = new OrderProcessModel();
	private List<PaymentTransactionEntryModel> paymentTransactionEntriesList = new ArrayList<PaymentTransactionEntryModel>();

	@Mock
	private ModelService modelService;
	@Mock
	private TicketBusinessService ticketBusinessService;


	protected PaymentTransactionEntryModel createPaymentTransactionEntry(final PaymentTransactionType type,
			final TransactionStatus status)
	{
		final PaymentTransactionEntryModel paymentTransactionEntry = new PaymentTransactionEntryModel();
		paymentTransactionEntry.setType(type);
		paymentTransactionEntry.setTransactionStatus(status.toString());
		return paymentTransactionEntry;
	}

	@Before
	public void setUp() throws Exception
	{
		// Used for MockitoAnnotations annotations
		MockitoAnnotations.initMocks(this);
		action.setModelService(modelService);
		action.setTicketBusinessService(ticketBusinessService);
		BDDMockito.given(modelService.create(CsTicketModel.class)).willReturn(new CsTicketModel());

		authorizationAccepted = createPaymentTransactionEntry(PaymentTransactionType.AUTHORIZATION, TransactionStatus.ACCEPTED);
		authorizationReview = createPaymentTransactionEntry(PaymentTransactionType.AUTHORIZATION, TransactionStatus.REVIEW);
		reviewAccepted = createPaymentTransactionEntry(PaymentTransactionType.REVIEW_DECISION, TransactionStatus.ACCEPTED);
		reviewRejected = createPaymentTransactionEntry(PaymentTransactionType.REVIEW_DECISION, TransactionStatus.REJECTED);

		process = new OrderProcessModel();
		final OrderModel order = new OrderModel();
		process.setOrder(order);
		final List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();
		order.setPaymentTransactions(paymentTransactionList);
		final PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();
		paymentTransactionList.add(paymentTransactionModel);
		paymentTransactionEntriesList = new ArrayList<PaymentTransactionEntryModel>();
		paymentTransactionModel.setEntries(paymentTransactionEntriesList);
	}

	@Test
	public void testAcceptedAuthorization()
	{
		paymentTransactionEntriesList.add(authorizationAccepted);
		try
		{
			final String result = action.execute(process);
			Assert.assertEquals(OK, result);
		}
		catch (final RetryLaterException e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
		catch (final Exception e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
	}


	@Ignore
	@Test
	public void testReviewAuthorization()
	{
		paymentTransactionEntriesList.add(authorizationReview);

		try
		{
			final String result = action.execute(process);
			Assert.assertEquals(WAIT, result);
		}
		catch (final RetryLaterException e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
		catch (final Exception e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
	}

	@Test
	public void testAcceptedReviewAuthorization()
	{
		paymentTransactionEntriesList.add(authorizationReview);
		paymentTransactionEntriesList.add(reviewAccepted);

		try
		{
			final String result = action.execute(process);
			Assert.assertEquals(OK, result);
		}
		catch (final RetryLaterException e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
		catch (final Exception e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
	}

	@Test
	public void testRejectedReviewAuthorization()
	{
		paymentTransactionEntriesList.add(authorizationReview);
		paymentTransactionEntriesList.add(reviewRejected);

		try
		{
			final String result = action.execute(process);
			Assert.assertEquals(NOK, result);
		}
		catch (final RetryLaterException e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
		catch (final Exception e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
	}

	@Ignore
	@Test
	public void testMultipleReviewAuthorization()
	{
		paymentTransactionEntriesList.add(authorizationReview);
		paymentTransactionEntriesList.add(reviewRejected);
		paymentTransactionEntriesList.add(authorizationReview);

		try
		{
			final String result = action.execute(process);
			Assert.assertEquals(WAIT, result);
		}
		catch (final RetryLaterException e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
		catch (final Exception e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
	}

	@Test
	public void testMultipleReview()
	{
		paymentTransactionEntriesList.add(authorizationReview);
		paymentTransactionEntriesList.add(reviewRejected);
		paymentTransactionEntriesList.add(authorizationReview);
		paymentTransactionEntriesList.add(reviewAccepted);

		try
		{
			final String result = action.execute(process);
			Assert.assertEquals(OK, result);
		}
		catch (final RetryLaterException e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
		catch (final Exception e)
		{
			LOG.error("Exception : ", e);
			fail();
		}
	}

}
