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
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;




@IntegrationTest
public class DeliveryModeTest extends HybrisJUnit4Test
{
	DeliveryMode deliveryMode;
	PaymentMode paymentMode;


	@Before
	public void setUp() throws Exception
	{
		final ComposedType dmType = TypeManager.getInstance().getRootComposedTypeForJaloClass(DeliveryMode.class);
		final ComposedType pmType = TypeManager.getInstance().getRootComposedTypeForJaloClass(PaymentMode.class);
		assertNotNull(deliveryMode = OrderManager.getInstance().createDeliveryMode(dmType, "entity.delivery"));
		assertNotNull(paymentMode = OrderManager.getInstance().createPaymentMode(pmType, "entity.delivery/payment",
				TypeManager.getInstance().getComposedType(Constants.TYPES.CreditCardPaymentInfo)));
	}

	@Test
	public void testTransaction() throws ConsistencyCheckException, EJBInvalidParameterException
	{
		final Transaction tx = Transaction.current();
		tx.begin();

		try
		{
			assertEquals(Collections.EMPTY_SET, deliveryMode.getSupportedPaymentModes());
			deliveryMode.addSupportedPaymentMode(paymentMode);
			final Collection<PaymentMode> paymentModes = deliveryMode.getSupportedPaymentModes();
			assertEquals(1, paymentModes.size());
			assertEquals(paymentMode, paymentModes.iterator().next());
		}
		finally
		{
			tx.rollback();
		}

		assertEquals(Collections.EMPTY_SET, deliveryMode.getSupportedPaymentModes());
	}

}
