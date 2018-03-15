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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.order.payment.PaymentInfo.CreditCardPaymentInfo;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PaymentInfoJaloTest extends HybrisJUnit4TransactionalTest
{
	private PaymentInfo info;

	private OrderManager om;

	@Before
	public void setUp() throws Exception
	{
		om = OrderManager.getInstance();

		final EnumerationValue visatype = EnumerationManager.getInstance().getEnumerationValue(Constants.TYPES.CreditCardTypeType,
				"visa");
		final User user = jaloSession.getUser();
		assertNotNull(info = user.createPaymentInfo("visa"));
		info.setComposedType(jaloSession.getTypeManager().getComposedType(Constants.TYPES.CreditCardPaymentInfo));
		info.setProperty(CreditCardPaymentInfo.NUMBER, "4111111111111111");
		info.setProperty(CreditCardPaymentInfo.TYPE, visatype);
	}

	@Test
	public void testCloningAndOrderRemoval() throws ConsistencyCheckException
	{
		Order o;
		assertNotNull(o = om.createOrder(jaloSession.getUser(), jaloSession.getSessionContext().getCurrency(), new Date(), false));

		o.setPaymentInfo(info);
		final PaymentInfo i2 = o.getPaymentInfo();
		assertFalse(info.equals(i2));
		assertTrue(i2.isDuplicateAsPrimitive());
		assertEquals(info, i2.getOriginal());

		o.remove();
		assertFalse(o.isAlive());
		assertNotNull(o);
		assertFalse(i2.isAlive());
		assertTrue(info.isAlive());

		assertNotNull(o = om.createOrder(jaloSession.getUser(), jaloSession.getSessionContext().getCurrency(), new Date(), false));

		o.setPaymentInfoNoCopy(info);
		assertEquals(info, o.getPaymentInfo());
		o.remove();
		assertFalse(o.isAlive());
		assertNotNull(o);
		assertTrue(info.isAlive());
	}

	@Test
	public void testNoCloning() throws ConsistencyCheckException
	{
		final Order o = om.createOrder(jaloSession.getUser(), jaloSession.getSessionContext().getCurrency(), new Date(), false);

		final PaymentInfo specialInfo = jaloSession.getUser().createPaymentInfo("foo");
		specialInfo.setDuplicate(true);
		specialInfo.setOwner(o);

		o.setPaymentInfo(specialInfo);
		assertEquals(specialInfo, o.getPaymentInfo());

		o.setPaymentInfo(null);
		assertNull(o.getPaymentInfo());
		assertFalse(specialInfo.isAlive());

		// test no-cloning not applicable ( info is copy BUT has wrong owner )

		final Order o2 = om.createOrder(jaloSession.getUser(), jaloSession.getSessionContext().getCurrency(), new Date(), false);

		o.setPaymentInfo(info);
		final PaymentInfo infoCopy = o.getPaymentInfo();
		assertFalse(info.equals(infoCopy));
		assertTrue(infoCopy.isDuplicateAsPrimitive());
		assertEquals(o, infoCopy.getOwner());
		assertEquals(info, infoCopy.getOriginal());

		o2.setPaymentInfo(infoCopy);
		final PaymentInfo infoCopyCopy = o2.getPaymentInfo();
		assertFalse(info.equals(infoCopyCopy));
		assertFalse(infoCopy.equals(infoCopyCopy));
		assertTrue(infoCopyCopy.isDuplicateAsPrimitive());
		assertEquals(o2, infoCopyCopy.getOwner());
		assertEquals(infoCopy, infoCopyCopy.getOriginal());
	}

	@Test
	public void testSetInfoField() throws Exception
	{
		assertNull(info.getProperty(CreditCardPaymentInfo.OWNER));
		info.setProperty(CreditCardPaymentInfo.OWNER, "test");
		assertEquals("test", info.getProperty(CreditCardPaymentInfo.OWNER));
		assertEquals("test", info.getAttribute(CreditCardPaymentInfo.OWNER));
	}
}
