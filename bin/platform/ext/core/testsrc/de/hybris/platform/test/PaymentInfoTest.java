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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.order.payment.PaymentInfo.CreditCardPaymentInfo;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


@IntegrationTest
public class PaymentInfoTest extends HybrisJUnit4TransactionalTest
{
	@Test
	public void testCreditCardNumberValidation() throws JaloBusinessException
	{
		assertTrue("", PaymentInfo.isVisa("4111 1111 1111 1111")); // 16 digits
		assertTrue("", PaymentInfo.isVisa("4 9291 2312 3123")); // 13 digits old style
		assertFalse("", PaymentInfo.isVisa("4111 1111 1111 1111 1111")); // 20 digits,  invalid
		assertFalse("", PaymentInfo.isVisa("4111 1111 1111")); // 12 digits, invalid
		assertFalse("", PaymentInfo.isVisa("1111 1111 1111 1111")); // 16 digits but missing leading '4', invald
		assertTrue("", PaymentInfo.isDiners("3000 0000 0000 04"));
		assertTrue("", PaymentInfo.isMaster("5500 0000 0000 0004"));
		assertTrue("", PaymentInfo.isAmericanExpress("3400 0000 0000 009"));
	}

	@Test
	public void testCCPaymentInfoPLA6933exceptions() throws ConsistencyCheckException, JaloGenericCreationException,
			JaloAbstractTypeException
	{
		final ComposedType cctype = TypeManager.getInstance().getComposedType("CreditCardPaymentInfo");
		final User user = UserManager.getInstance().createCustomer("customer");
		final EnumerationValue visatype = EnumerationManager.getInstance().getEnumerationValue(Constants.TYPES.CreditCardTypeType,
				"visa");
		assertNotNull("", visatype);
		final EnumerationValue amextype = EnumerationManager.getInstance().getEnumerationValue(Constants.TYPES.CreditCardTypeType,
				"amex");
		assertNotNull("", amextype);
		final EnumerationValue dinertype = EnumerationManager.getInstance().getEnumerationValue(Constants.TYPES.CreditCardTypeType,
				"diners");
		assertNotNull("", dinertype);
		final EnumerationValue mastertype = EnumerationManager.getInstance().getEnumerationValue(
				Constants.TYPES.CreditCardTypeType, "master");
		assertNotNull("", mastertype);
		final Map<String, Object> valueMap = new HashMap<String, Object>();
		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			cctype.newInstance(valueMap);
		}
		catch (final JaloBusinessException e)
		{
			assertEquals("", "number or type is null!", e.getMessage());
		}

		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			valueMap.put(CreditCardPaymentInfo.TYPE, visatype);
			valueMap.put(CreditCardPaymentInfo.NUMBER, "111");
			cctype.newInstance(valueMap);
		}
		catch (final JaloInvalidParameterException e)
		{
			assertEquals("", "The CreditCard number 111 is not a valid VISA creditcard number!", e.getMessage());
		}

		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			valueMap.put(CreditCardPaymentInfo.TYPE, amextype);
			valueMap.put(CreditCardPaymentInfo.NUMBER, "111");
			cctype.newInstance(valueMap);
		}
		catch (final JaloInvalidParameterException e)
		{
			assertEquals("", "The CreditCard number 111 is not a valid AmericanExpress creditcard number!", e.getMessage());
		}

		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			valueMap.put(CreditCardPaymentInfo.TYPE, dinertype);
			valueMap.put(CreditCardPaymentInfo.NUMBER, "111");
			cctype.newInstance(valueMap);
		}
		catch (final JaloInvalidParameterException e)
		{
			assertEquals("", "The CreditCard number 111 is not a valid Diners creditcard number!", e.getMessage());
		}

		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			valueMap.put(CreditCardPaymentInfo.TYPE, mastertype);
			valueMap.put(CreditCardPaymentInfo.NUMBER, "111");
			cctype.newInstance(valueMap);
		}
		catch (final JaloInvalidParameterException e)
		{
			assertEquals("", "The CreditCard number 111 is not a valid Master creditcard number!", e.getMessage());
		}
	}

	@Test
	public void testCCPaymentInfoPLA6933attributechanges() throws ConsistencyCheckException
	{
		final ComposedType cctype = TypeManager.getInstance().getComposedType("CreditCardPaymentInfo");
		final User user = UserManager.getInstance().createCustomer("customer");
		final EnumerationValue visatype = EnumerationManager.getInstance().getEnumerationValue(Constants.TYPES.CreditCardTypeType,
				"visa");
		assertNotNull("", visatype);
		final Map<String, Object> valueMap = new HashMap<String, Object>();
		PaymentInfo ccpi = null;
		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			valueMap.put(CreditCardPaymentInfo.TYPE, visatype);
			valueMap.put(CreditCardPaymentInfo.NUMBER, "4111111111111111");
			ccpi = (PaymentInfo) cctype.newInstance(valueMap);
		}
		catch (final JaloBusinessException e)
		{
			fail();
		}

		assertNotNull("", ccpi);
		try
		{
			ccpi.setAttribute(CreditCardPaymentInfo.NUMBER, "4111113111111111");
		}
		catch (final JaloBusinessException e)
		{
			assertEquals("", "The CreditCard number 4111113111111111 is not a valid VISA creditcard number!", e.getMessage());
		}
	}

	@Test
	public void testCCPaymentInfoPLA6933configvariable() throws ConsistencyCheckException
	{
		//see advanced.properties, paymentinfo.creditcard.checknumber=true

		final ComposedType cctype = TypeManager.getInstance().getComposedType("CreditCardPaymentInfo");
		final User user = UserManager.getInstance().createCustomer("customer");
		final EnumerationValue visatype = EnumerationManager.getInstance().getEnumerationValue(Constants.TYPES.CreditCardTypeType,
				"visa");

		//test1, removing key
		Config.setParameter("paymentinfo.creditcard.checknumber", "");


		final Map<String, Object> valueMap = new HashMap<String, Object>();
		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			valueMap.put(CreditCardPaymentInfo.TYPE, visatype);
			valueMap.put(CreditCardPaymentInfo.NUMBER, "4111111211111111");
			cctype.newInstance(valueMap);
		}
		catch (final JaloBusinessException e)
		{
			fail(); //number is false, but the check is disabled, therefore no exception here
		}

		Config.setParameter("paymentinfo.creditcard.checknumber", "false");
		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			valueMap.put(CreditCardPaymentInfo.TYPE, visatype);
			valueMap.put(CreditCardPaymentInfo.NUMBER, "4111111211111111");
			cctype.newInstance(valueMap);
		}
		catch (final JaloBusinessException e)
		{
			fail(); //number is false, but the check is disabled, therefore no exception here
		}

		Config.setParameter("paymentinfo.creditcard.checknumber", "true");
		try
		{
			valueMap.clear();
			valueMap.put(PaymentInfo.CODE, "test1");
			valueMap.put(PaymentInfo.USER, user);
			valueMap.put(CreditCardPaymentInfo.TYPE, visatype);
			valueMap.put(CreditCardPaymentInfo.NUMBER, "4111111211111111");
			cctype.newInstance(valueMap);
			fail(); //number is false and check is enabled
		}
		catch (final JaloInvalidParameterException e)
		{
			//exception is ok here
		}
		catch (final JaloGenericCreationException e)
		{
			fail(); //those exceptions are not ok here
		}
		catch (final JaloAbstractTypeException e)
		{
			fail(); //those exceptions are not ok here
		}
	}
}
