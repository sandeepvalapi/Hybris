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
package de.hybris.platform.paymentstandard.jalo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.payment.JaloPaymentModeException;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.paymentstandard.constants.StandardPaymentModeConstants;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class StandardPaymentModeTest extends HybrisJUnit4TransactionalTest
{
	private StandardPaymentMode pmCreditCard;
	private ComposedType spmType;
	private ComposedType ccType;
	private Currency eu; //NOPMD
	private Tax tax;

	private Product product1;
	private Product product2;
	private Unit unit1;
	private Address adr;
	private Customer dirk;
	private Title titleMr;
	private Country deC;
	private Order order;
	@SuppressWarnings("unused")
	private AbstractOrderEntry entry1;
	@SuppressWarnings("unused")
	private AbstractOrderEntry entry2;


	@Before
	public void setUp() throws Exception
	{
		assertNotNull(dirk = jaloSession.getUserManager().createCustomer("DIRK"));
		dirk.setName("Dirk Ahlrichs");
		dirk.setDescription("dirk surfer");
		dirk.setPassword("krid");
		assertNotNull(adr = dirk.createAddress());
		assertNotNull(titleMr = jaloSession.getUserManager().createTitle("mr"));
		assertNotNull(deC = jaloSession.getC2LManager().createCountry("DE"));
		adr.setTitle(titleMr);
		adr.setAttribute(Address.FIRSTNAME, "Dirk");
		adr.setAttribute(Address.LASTNAME, "Bahlrichs");
		adr.setAttribute(Address.STREETNAME, "Beilerstr.");
		adr.setAttribute(Address.STREETNUMBER, "88");
		adr.setAttribute(Address.TOWN, "Buenchen");
		adr.setAttribute(Address.POSTALCODE, "88888");
		adr.setCountry(deC);
		dirk.setDefaultPaymentAddress(adr);
		dirk.setDefaultDeliveryAddress(adr);

		assertNotNull(product1 = jaloSession.getProductManager().createProduct("PRODUCT_1"));
		assertNotNull(product2 = jaloSession.getProductManager().createProduct("PRODUCT_2"));
		assertNotNull(unit1 = jaloSession.getProductManager().createUnit("piece", "test.unit1"));

		assertNotNull(eu = jaloSession.getC2LManager().createCurrency("EURO"));

		assertNotNull(tax = jaloSession.getOrderManager().createTax("TAX"));
		tax.setValue(1);

		spmType = jaloSession.getTypeManager().getComposedType(StandardPaymentModeConstants.TC.STANDARDPAYMENTMODE);
		ccType = jaloSession.getTypeManager().getComposedType(Constants.TYPES.CreditCardTypeType);
		assertNotNull(pmCreditCard = (StandardPaymentMode) jaloSession.getOrderManager().createPaymentMode(spmType, "creditCard",
				ccType));


		assertNotNull(order = jaloSession.getOrderManager().createOrder("ORDER_1", dirk, eu, new Date(System.currentTimeMillis()),
				true));
		order.setPaymentMode(pmCreditCard);
		final EnumerationValue status = createOrderStatus("Pending");
		assertNotNull(status);
		order.setStatus(status);
		order.setDeliveryAddress(dirk.getDefaultDeliveryAddress());
		order.setPaymentAddress(dirk.getDefaultPaymentAddress());
		entry1 = order.addNewEntry(product1, 1, unit1);
		entry2 = order.addNewEntry(product2, 1, unit1);
	}

	/** test setting/getting net */
	@Test
	public void testNet() throws Exception
	{
		assertFalse(pmCreditCard.isNetAsPrimitive());
		pmCreditCard.setNet(true);
		assertTrue(pmCreditCard.isNetAsPrimitive());
		pmCreditCard.setNet(false);
		assertFalse(pmCreditCard.isNetAsPrimitive());
	}

	/** test equals, just for fun */
	@Test
	public void testEquals() throws Exception
	{
		StandardPaymentMode pmCard;
		assertNotNull(pmCard = (StandardPaymentMode) jaloSession.getOrderManager().createPaymentMode(spmType, "card", ccType));

		assertFalse(pmCreditCard.equals(pmCard));
		assertEquals(pmCreditCard, pmCreditCard);
		assertEquals(pmCreditCard, jaloSession.getOrderManager().getPaymentModeByCode("creditCard"));
	}

	/** test Code just to be safe */
	@Test
	public void testCode() throws Exception
	{
		StandardPaymentMode pmCard;
		assertNotNull(pmCard = (StandardPaymentMode) jaloSession.getOrderManager().createPaymentMode(spmType, "card", ccType));

		try
		// set code to existing one
		{
			pmCard.setCode("creditCard");
			fail("Code should be unique.");
		}
		catch (final ConsistencyCheckException e)
		{
			// DOCTODO document reason why this is empty
		}
		try
		// create new paymentmode with existing code
		{
			assertNotNull(jaloSession.getOrderManager().createPaymentMode(spmType, "card", ccType));
			fail("Code should be unique.");
		}
		catch (final ConsistencyCheckException e)
		{
			// DOCTODO document reason why this is empty
		}
		try
		//  create new paymentmode with existing code and different type
		{
			assertNotNull(jaloSession.getOrderManager().createPaymentMode(spmType, "card", ccType));
			fail("Code should be unique.");
		}
		catch (final ConsistencyCheckException e)
		{
			// DOCTODO document reason why this is empty
		}
	}


	/**
	 * costs are saved in StandardPaymentValue-Objects whose cost/currencies are returned costs are added with setCost()
	 * and removed with removeCost(), and getValues==getAllCosts will be modified after fix of bug 393: getCosts enabled
	 * to convert currencies
	 */
	@Test
	public void testGetValues() throws Exception
	{
		StandardPaymentMode pmCard;
		assertNotNull(pmCard = (StandardPaymentMode) jaloSession.getOrderManager().createPaymentMode(spmType, "card", ccType));

		assertTrue(pmCard.getValues().keySet().isEmpty());

		pmCard.setCost(eu, 3.0);

		assertTrue(pmCard.getValues().keySet().contains(eu));
		assertEquals(3.0, pmCard.getValues().get(eu).doubleValue(), 0.0);

		Currency dm; //NOPMD
		assertNotNull(dm = jaloSession.getC2LManager().createCurrency("DM"));
		pmCard.setCost(dm, 6.0);

		assertTrue(pmCard.getValues().keySet().contains(eu));
		assertTrue(pmCard.getValues().keySet().contains(dm));
		assertEquals(6.0, pmCard.getValues().get(dm).doubleValue(), 0.0);

		pmCard.removeCost(dm);
		assertFalse(pmCard.getValues().keySet().contains(dm));
	}


	/** test setting/getting costs with variated settings for price, tax, net */
	@Test
	public void testGetCost() throws Exception
	{
		// nothing was set
		try
		{
			pmCreditCard.getCost(order);
			fail("No tax defined for paymentmode and currency, getCost() fails.");
		}
		catch (final JaloPaymentModeException e)
		{
			// DOCTODO document reason why this is empty
		}

		try
		{
			pmCreditCard.getCost(order);
			fail("No price defined for paymentmode and currency, getCost() fails.");
		}
		catch (final Exception e)
		{
			// DOCTODO document reason why this is empty

		}

		// assign Price, but no Tax
		pmCreditCard.setCost(eu, 3);
		//it's commented because it doesn't check the tax
		/*
		 * pmCreditCard.setTax( null ); try { pmCreditCard.getCost( order );
		 * fail("No tax defined for paymentmode and currency, getCost() fails."); } catch(JaloPaymentModeException e) {
		 * assertTrue(true); }
		 */

		// no we set Price and Tax
		//pmCreditCard.setTax( tax );
		assertFalse(pmCreditCard.isNetAsPrimitive());
		assertEquals(3.0, pmCreditCard.getCost(order).getValue(), 0.0);
		assertEquals(eu.getIsoCode(), pmCreditCard.getCost(order).getCurrencyIso());
		assertEquals(pmCreditCard.isNetAsPrimitive(), pmCreditCard.getCost(order).isNet());

		// commented because getCosts in StandardPaymentModeManagerEJB returns EJBPriceValue and instead of Taxes is Collections.EMPTY_LIST     
		//assertTrue( pmCreditCard.getCost( order ).getTaxes().contains( tax ) );
		//assertEquals( 1, pmCreditCard.getCost( order ).getTaxes().size());
		//assertEquals( Collections.EMPTY_LIST, pmCreditCard.getCost( order ).getTaxes() );
	}

	private static EnumerationValue createOrderStatus(final String statusCode) throws ConsistencyCheckException
	{
		return JaloSession.getCurrentSession().getEnumerationManager()
				.createEnumerationValue(JaloSession.getCurrentSession().getOrderManager().getOrderStatusType(), statusCode);
	}
}
