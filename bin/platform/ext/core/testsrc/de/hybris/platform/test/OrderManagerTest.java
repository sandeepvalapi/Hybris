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
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SearchContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Utilities;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrderManagerTest extends HybrisJUnit4TransactionalTest
{
	static final Logger LOG = Logger.getLogger(OrderManagerTest.class.getName());

	private PaymentInfo pi1 = null;
	private PaymentInfo pi2 = null;
	private Customer cust1 = null;
	private Customer cust2 = null;
	private ProductManager prm = null;
	private TypeManager typeManager = null;
	private UserManager userManager = null;
	private EnumerationManager enumerationManager = null;
	private OrderManager orderManager = null;
	private final String piCode = "paymentInfo";
	private final String pi1Code = piCode + "1";
	private final String pi2Code = piCode + "2";
	private Order order1 = null;
	private final String o1Code = "orderCode1";
	private Order order2 = null;
	private Currency curr = null;
	private final boolean net = true;
	private final String o1Status = "o1Status";
	private final String cartCode = "cartCode";
	private Date startDate = null;
	private Date endDate = null;
	private Tax tax1 = null;
	private Tax tax2 = null;
	private final String taxCode = "taxCode";
	private final String taxCode1 = taxCode + "1";
	private final String taxCode2 = taxCode + "2";
	private Discount discount1 = null;
	private Discount discount2 = null;
	private final String discountCode = "discountCode";
	private final String discountCode1 = discountCode + "1";
	private final String discountCode2 = discountCode + "2";
	private final boolean globalDiscountTrue = true;
	private final boolean globalDiscountFalse = false;
	private DeliveryMode deliveryMode = null;
	private final String deliveryModeCode = "deliveryModeCode";
	private final String[] paymentType =
	{ "creditcard", Constants.TYPES.CreditCardPaymentInfo };
	private PaymentMode paymentMode = null;
	private EnumerationValue o1StatusEnum = null;
	private Cart cart = null;

	@Before
	public void setUp() throws Exception
	{
		typeManager = TypeManager.getInstance();
		userManager = jaloSession.getUserManager();
		orderManager = jaloSession.getOrderManager();
		enumerationManager = jaloSession.getEnumerationManager();
		prm = ProductManager.getInstance();
		curr = jaloSession.getC2LManager().createCurrency("testCurr");
		assertNotNull(curr);
		cust1 = userManager.createCustomer("testCustomer1");
		assertNotNull(cust1);
		cust2 = userManager.createCustomer("testCustomer2");
		assertNotNull(cust2);
		pi1 = cust1.createPaymentInfo(pi1Code);
		assertNotNull(pi1);
		pi2 = cust2.createPaymentInfo(pi2Code);
		assertNotNull(pi2);
		order1 = orderManager.createOrder(o1Code, cust1, curr, new Date(), net);
		assertNotNull(order1);
		order2 = orderManager.createOrder("orderCode2", cust2, curr, new Date(), net);
		assertNotNull(order2);

		final EnumerationType enumType;
		assertNotNull(enumType = jaloSession.getEnumerationManager().createDefaultEnumerationType("orderStatusTestType"));
		o1StatusEnum = enumerationManager.createEnumerationValue(enumType, o1Status);
		assertNotNull(o1StatusEnum);
		order1.setStatus(o1StatusEnum);

		cart = orderManager.createCart(cartCode, cust2, curr, new Date(), net);
		assertNotNull(cart);

		/*
		 * TODO: pi1.set pi1.setType( pi1Type ); pi2.setType( pi2Type );
		 */

		final Calendar cal1 = Utilities.getDefaultCalendar();
		cal1.set(Calendar.DAY_OF_MONTH, 1);
		cal1.set(Calendar.MONTH, 1);
		cal1.set(Calendar.YEAR, 1980);
		startDate = cal1.getTime();

		final Calendar cal2 = Utilities.getDefaultCalendar();
		cal2.set(Calendar.DAY_OF_MONTH, 1);
		cal2.set(Calendar.MONTH, 1);
		cal2.set(Calendar.YEAR, 2040);
		endDate = cal2.getTime();

		tax1 = orderManager.createTax(taxCode1);
		assertNotNull(tax1);
		tax2 = orderManager.createTax(taxCode2);
		assertNotNull(tax2);

		discount1 = orderManager.createDiscount(discountCode1);
		assertNotNull(discount1);
		discount1.setGlobal(globalDiscountTrue);

		discount2 = orderManager.createDiscount(discountCode2);
		assertNotNull(discount2);
		discount2.setGlobal(globalDiscountFalse);

		final ComposedType dmType = typeManager.getComposedType(DeliveryMode.class);
		deliveryMode = orderManager.createDeliveryMode(dmType, deliveryModeCode);
		assertNotNull(deliveryMode);

		final ComposedType paymentInfoType = typeManager.getComposedType(paymentType[1]);
		final ComposedType pmType = typeManager.getComposedType(PaymentMode.class);
		paymentMode = orderManager.createPaymentMode(pmType, paymentType[0], paymentInfoType);
		assertNotNull(paymentMode);
		paymentMode.setActive(true);
		paymentMode.addSupportedDeliveryMode(deliveryMode);

		deliveryMode.addSupportedPaymentMode(paymentMode);
	}

	@Test
	public void testOrderTypeCopying() throws JaloInvalidParameterException, JaloDuplicateCodeException,
			JaloItemNotFoundException, ConsistencyCheckException
	{
		final ComposedType orderType = typeManager.getComposedType(Order.class);
		final ComposedType entryType = typeManager.getComposedType(OrderEntry.class);
		ComposedType orderSubType;
		ComposedType orderEntrySubType;
		assertNotNull(orderSubType = typeManager.createComposedType(orderType, "OrderSub"));
		assertNotNull(orderEntrySubType = typeManager.createComposedType(entryType, "OrderEntrySub"));

		Discount discount1, discount2;
		Order plainOne, subTyped;
		assertNotNull(plainOne = orderManager.createOrder(userManager.getAnonymousCustomer(), curr, new Date(), false));
		assertNotNull(subTyped = orderManager.createOrder(orderSubType, "subtyped", userManager.getAnonymousCustomer(), curr,
				new Date(), false));

		assertNotNull(discount1 = orderManager.createDiscount("blah"));
		assertNotNull(discount2 = orderManager.createDiscount("fasel"));

		plainOne.setDiscounts(Arrays.asList(discount1, discount2));
		subTyped.setDiscounts(Arrays.asList(discount2));

		assertEquals(orderType, plainOne.getComposedType());
		assertEquals(orderSubType, subTyped.getComposedType());

		Product product;
		Unit unit;
		assertNotNull(product = prm.createProduct("ppp"));
		assertNotNull(unit = prm.createUnit("u", "u"));

		final AbstractOrderEntry plainEntry1 = plainOne.addNewEntry(product, 1, unit);
		final AbstractOrderEntry plainEntry2 = subTyped.addNewEntry(product, 2, unit);

		assertEquals(entryType, plainEntry1.getComposedType());
		assertEquals(entryType, plainEntry2.getComposedType());

		// clone without custom type -> plain order -> original type
		Order clonedPlain;
		assertNotNull(clonedPlain = orderManager.createOrder(plainOne));
		assertEquals(orderType, clonedPlain.getComposedType());

		// clone without custom type -> subtyped order -> subtype
		Order clonedSub1;
		assertNotNull(clonedSub1 = orderManager.createOrder(subTyped));
		assertEquals(1, clonedSub1.getAllEntries().size());
		assertEquals(orderSubType, clonedSub1.getComposedType());
		assertEquals(Arrays.asList(discount2), clonedSub1.getDiscounts());

		// cloned incl custom types for order and entries -> plain order -> subtype both
		Order clonedSub2;
		assertNotNull(clonedSub2 = orderManager.createOrder(orderSubType, orderEntrySubType, plainOne));
		assertEquals(orderSubType, clonedSub2.getComposedType());
		assertEquals(1, clonedSub2.getAllEntries().size());
		assertEquals(orderEntrySubType, clonedSub2.getEntry(0).getComposedType());
		assertEquals(Arrays.asList(discount1, discount2), clonedSub2.getDiscounts());

		// cloned incl custom types for order and entries -> subtyped order -> subtype both
		Order clonedSub3;
		assertNotNull(clonedSub3 = orderManager.createOrder(orderSubType, orderEntrySubType, subTyped));
		assertEquals(orderSubType, clonedSub3.getComposedType());
		assertEquals(1, clonedSub3.getAllEntries().size());
		assertEquals(orderEntrySubType, clonedSub2.getEntry(0).getComposedType());
		assertEquals(Arrays.asList(discount2), clonedSub3.getDiscounts());
	}

	@Test
	public void testCartTypeCopying() throws JaloInvalidParameterException, JaloDuplicateCodeException, JaloItemNotFoundException,
			ConsistencyCheckException
	{
		final ComposedType cartType = typeManager.getComposedType(Cart.class);
		final ComposedType entryType = typeManager.getComposedType(CartEntry.class);
		ComposedType cartSubType;
		ComposedType cartEntrySubType;
		assertNotNull(cartSubType = typeManager.createComposedType(cartType, "CartSub"));
		assertNotNull(cartEntrySubType = typeManager.createComposedType(entryType, "CartEntrySub"));

		Discount discount1, discount2;
		Cart plainOne, subTyped;
		assertNotNull(plainOne = orderManager.createCart(userManager.getAnonymousCustomer(), curr, new Date(), false));
		assertNotNull(subTyped = orderManager.createCart(cartSubType, "subtyped", userManager.getAnonymousCustomer(), curr,
				new Date(), false));

		assertNotNull(discount1 = orderManager.createDiscount("blah"));
		assertNotNull(discount2 = orderManager.createDiscount("fasel"));

		plainOne.setDiscounts(Arrays.asList(discount1, discount2));
		subTyped.setDiscounts(Arrays.asList(discount2));

		assertEquals(cartType, plainOne.getComposedType());
		assertEquals(cartSubType, subTyped.getComposedType());

		Product product;
		Unit unit;
		assertNotNull(product = prm.createProduct("ppp"));
		assertNotNull(unit = prm.createUnit("u", "u"));

		final AbstractOrderEntry plainEntry1 = plainOne.addNewEntry(product, 1, unit);
		final AbstractOrderEntry plainEntry2 = subTyped.addNewEntry(product, 2, unit);

		assertEquals(entryType, plainEntry1.getComposedType());
		assertEquals(entryType, plainEntry2.getComposedType());

		// clone without custom type -> plain order -> original type
		Cart clonedPlain;
		assertNotNull(clonedPlain = orderManager.createCart(plainOne, null));
		assertEquals(cartType, clonedPlain.getComposedType());

		// clone without custom type -> subtyped order -> subtype
		Cart clonedSub1;
		assertNotNull(clonedSub1 = orderManager.createCart(subTyped, null));
		assertEquals(1, clonedSub1.getAllEntries().size());
		assertEquals(cartSubType, clonedSub1.getComposedType());
		assertEquals(Arrays.asList(discount2), clonedSub1.getDiscounts());

		// cloned incl custom types for order and entries -> plain order -> subtype both
		Cart clonedSub2;
		assertNotNull(clonedSub2 = orderManager.createCart(cartSubType, cartEntrySubType, plainOne, null));
		assertEquals(cartSubType, clonedSub2.getComposedType());
		assertEquals(1, clonedSub2.getAllEntries().size());
		assertEquals(cartEntrySubType, clonedSub2.getEntry(0).getComposedType());
		assertEquals(Arrays.asList(discount1, discount2), clonedSub2.getDiscounts());

		// cloned incl custom types for order and entries -> subtyped order -> subtype both
		Cart clonedSub3;
		assertNotNull(clonedSub3 = orderManager.createCart(cartSubType, cartEntrySubType, subTyped, null));
		assertEquals(cartSubType, clonedSub3.getComposedType());
		assertEquals(1, clonedSub3.getAllEntries().size());
		assertEquals(cartEntrySubType, clonedSub2.getEntry(0).getComposedType());
		assertEquals(Arrays.asList(discount2), clonedSub3.getDiscounts());
	}

	@Test
	public void testPropertyCopying()
	{
		Order order = null;
		Cart cart2 = null;
		try
		{
			final Customer customer = (Customer) jaloSession.getUser();
			final Currency curr = jaloSession.getSessionContext().getCurrency();
			final Cart cart = jaloSession.getCart();

			// some general tests
			assertNotNull("cart was NULL", cart);
			assertTrue("cart.getUser() != jaloSession.getSessionContext().getUser()", cart.getUser().equals(customer));
			assertTrue("cart.getCurrency() != jaloSession.getSessionContext().getCurrency()", cart.getCurrency().equals(curr));
			// add some properties
			cart.setProperty("color", "blue");
			cart.setProperty("weight", new Double(123.456));
			// clone cart
			order = orderManager.createOrder(cart);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("new order is " + order);
			}
			// some general tests atgain
			assertNotNull("order was NULL", order);
			assertTrue("order.getUser() != jaloSession.getSessionContext().getUser()", order.getUser().equals(customer));
			assertTrue("order.getCurrency() != jaloSession.getSessionContext().getCurrency()", order.getCurrency().equals(curr));

			//Create new cart   
			cart2 = orderManager.createCart("c2", customer, curr, cart.getDate(), true);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("new cart2 is " + cart2);
			}
			// some general tests atgain
			assertNotNull("cart2 was NULL");
			assertTrue("c2.getUser() != jaloSession.getSessionContext().getUser()", cart2.getUser().equals(customer));
			assertTrue("c2.getCurrency() != jaloSession.getSessionContext().getCurrency()", cart2.getCurrency().equals(curr));

			// test if properties are copied too
			Object value = order.getProperty("color");
			assertTrue("'color' property was not copied (value = " + value + " )", value != null && value.equals("blue"));
			value = order.getProperty("weight");
			assertTrue("'weight' property was not copied (value = " + value + " )",
					value != null && value.equals(new Double(123.456)));
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("an error occurred : " + e);
		}
		finally
		{
			if (order != null)
			{
				try
				{
					order.remove();
					if (LOG.isDebugEnabled())
					{
						LOG.debug("removed order " + order);
					}
				}
				catch (final Exception e)
				{
					/* conv-LOG-err */LOG.error("could not remove order " + order);
				}
			}

			if (cart2 != null)
			{
				try
				{
					cart2.remove();
					if (LOG.isDebugEnabled())
					{
						LOG.debug("removed cart2 " + cart2);
					}
				}
				catch (final Exception e)
				{
					/* conv-LOG-err */LOG.error("could not remove cart2 " + cart2);
				}
			}
		}
	}

	@Test
	public void testGetAllPaymentInfos() throws Exception
	{
		final Collection coll = orderManager.getAllPaymentInfos();
		assertEquals(coll.size(), 2);
	}

	@Test
	public void testGetPaymentInfosByType() throws Exception
	{
		/*
		 * TODO:type...no such thing ???Collection coll = om.getPaymentInfos( pi1Type ); assertEquals( coll.size(), 1 );
		 * assertEquals( ((PaymentInfo)coll.iterator().next()).getCode(),
		 */
	}

	@Test
	public void testGetPaymentInfosByCode() throws Exception
	{
		Collection coll = orderManager.getPaymentInfosByCode(pi1Code);
		assertEquals(1, coll.size());

		coll = orderManager.getPaymentInfosByCode("%" + piCode + "%");
		assertEquals(2, coll.size());
	}

	@Test
	public void testGetAllOrders() throws Exception
	{
		final Collection coll = orderManager.getAllOrders();
		assertEquals(2, coll.size());
	}

	@Test
	public void testGetOrdersByUser() throws Exception
	{
		assertEquals(Collections.singletonList(order1), cust1.getOrders());
	}

	@Test
	public void testSearchOrders() throws Exception
	{
		final SearchContext sctx = jaloSession.createSearchContext();
		sctx.setProperty(AbstractOrder.CODE, o1Code);
		sctx.setProperty(AbstractOrder.USER, cust1);
		sctx.setProperty(Order.START_DATE, startDate);
		sctx.setProperty(Order.START_DATE, endDate);
		sctx.setProperty(AbstractOrder.CURRENCY, curr);
		sctx.setProperty(AbstractOrder.NET, Boolean.valueOf(net));
		sctx.setProperty(AbstractOrder.STATUS, o1StatusEnum);

		final Collection coll = orderManager.searchOrders(sctx);

		assertEquals(coll.size(), 1);
	}

	@Test
	public void testSearchCarts() throws Exception
	{
		final SearchContext sctx = jaloSession.createSearchContext();
		sctx.setProperty(AbstractOrder.CODE, cartCode);
		sctx.setProperty(AbstractOrder.USER, cust2);
		sctx.setProperty(Cart.START_DATE, startDate);
		sctx.setProperty(Cart.START_DATE, endDate);
		sctx.setProperty(AbstractOrder.CURRENCY, curr);
		sctx.setProperty(AbstractOrder.NET, Boolean.valueOf(net));

		final Collection coll = orderManager.searchCarts(sctx);

		assertEquals(1, coll.size());
	}

	@Test
	public void testSearchTaxes() throws Exception
	{
		final SearchContext sctx = jaloSession.createSearchContext();
		sctx.setProperty(Tax.CODE, taxCode1);

		final Collection coll = orderManager.searchTaxes(sctx);
		assertEquals(1, coll.size());
	}

	@Test
	public void testSearchDiscounts() throws Exception
	{
		final SearchContext sctx = jaloSession.createSearchContext();
		sctx.setProperty(Discount.CODE, discountCode1);

		final Collection coll = orderManager.searchDiscounts(sctx);
		assertEquals(1, coll.size());
	}

	@Test
	public void testGetAllTaxes() throws Exception
	{
		final Collection coll = orderManager.getAllTaxes();
		assertEquals(2, coll.size());
	}

	@Test
	public void testGetTaxesByCode() throws Exception
	{
		final Collection coll = orderManager.getTaxesByCode("%" + taxCode + "%");
		assertEquals(2, coll.size());
	}

	@Test
	public void testGetAllDiscounts() throws Exception
	{
		final Collection coll = orderManager.getAllDiscounts();
		assertEquals(2, coll.size());
	}

	@Test
	public void testGetAllDiscountsByGlobal() throws Exception
	{
		final Collection coll = orderManager.getAllDiscounts(globalDiscountTrue);
		assertEquals(1, coll.size());
		assertEquals(((Discount) coll.iterator().next()).getCode(), discountCode1);
	}

	@Test
	public void testGetDiscountByCode() throws Exception
	{
		final Discount discount = orderManager.getDiscountByCode(discountCode1);
		assertEquals(discount1, discount);
		assertEquals(discountCode1, discount1.getCode());
	}

	@Test
	public void testGetDiscountsByCode() throws Exception
	{
		final Collection coll = orderManager.getDiscountsByCode("%" + discountCode + "%");
		assertEquals(2, coll.size());
	}

	@Test
	public void testGetAllDeliveryModes() throws Exception
	{
		final Collection coll = orderManager.getAllDeliveryModes();
		assertEquals(1, coll.size());
	}

	@Test
	public void testGetDeliveryModeByCode() throws Exception
	{
		final DeliveryMode dm0 = orderManager.getDeliveryModeByCode(deliveryModeCode);
		assertEquals(dm0, deliveryMode);
	}

	@Test
	public void testGetPaymentModeByCode() throws Exception
	{
		final PaymentMode pm0 = orderManager.getPaymentModeByCode(paymentType[0]);
		assertEquals(pm0, paymentMode);
	}

	@Test
	public void testGetAllPaymentModes() throws Exception
	{
		final Collection coll = orderManager.getAllPaymentModes();
		assertEquals(1, coll.size());
	}

	@Test
	@SuppressWarnings("cast")
	public void testGetPaymentInfosByUser() throws Exception
	{
		final Collection coll = orderManager.getPaymentInfosByUser(cust1);
		assertEquals(1, coll.size());
		assertEquals(pi1, coll.iterator().next());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testGetPaymentInfoByCode() throws Exception
	{
		final PaymentInfo paymentInfo = orderManager.getPaymentInfoByCode(pi1Code);
		assertEquals(paymentInfo, pi1);
	}

	@Test
	@SuppressWarnings("cast")
	public void testGetSupportedDeliveryModes() throws Exception
	{
		final Collection coll = orderManager.getSupportedDeliveryModes(paymentMode);
		assertEquals(1, coll.size());
		assertEquals(deliveryMode, coll.iterator().next());
	}

	@Test
	public void testGetTaxByCode() throws Exception
	{
		final Tax tax = orderManager.getTaxByCode(taxCode1);
		assertEquals(tax1, tax);
	}

	@Test
	public void testCartCloningWithPreviouslyRemovedEntries() throws JaloInvalidParameterException, ConsistencyCheckException
	{
		final Cart cart = OrderManager.getInstance().createCart(jaloSession.getUser(),
				jaloSession.getSessionContext().getCurrency(), new Date(), true);
		final Product product = ProductManager.getInstance().createProduct("foo");
		final Unit unit = ProductManager.getInstance().createUnit("type", "code");

		final CartEntry ce1 = (CartEntry) cart.addNewEntry(product, 10, unit, -1, false);
		final CartEntry ce2 = (CartEntry) cart.addNewEntry(product, 1, unit, -1, false);
		final CartEntry ce3 = (CartEntry) cart.addNewEntry(product, 7, unit, -1, false);

		assertEquals(Arrays.asList(ce1, ce2, ce3), cart.getAllEntries());
		assertEquals(0, ce1.getEntryNumber().intValue());
		assertEquals(1, ce2.getEntryNumber().intValue());
		assertEquals(2, ce3.getEntryNumber().intValue());

		cart.removeEntry(ce1);

		assertEquals(Arrays.asList(ce2, ce3), cart.getAllEntries());
		assertFalse(ce1.isAlive());
		assertEquals(1, ce2.getEntryNumber().intValue());
		assertEquals(2, ce3.getEntryNumber().intValue());

		final Order order = OrderManager.getInstance().createOrder(cart);

		assertEquals(Arrays.asList(ce2, ce3), cart.getAllEntries());
		assertFalse(ce1.isAlive());
		assertEquals(1, ce2.getEntryNumber().intValue());
		assertEquals(2, ce3.getEntryNumber().intValue());

		assertEquals(cart.getUser(), order.getUser());
		assertEquals(cart.getCurrency(), order.getCurrency());
		assertEquals(cart.isNet(), order.isNet());



		final List<CartEntry> cartEntries = cart.getAllEntries();
		final List<OrderEntry> orderEntries = order.getAllEntries();

		assertEquals(cartEntries.size(), orderEntries.size());

		for (int i = 0; i < cartEntries.size(); i++)
		{
			final CartEntry cartEntry = cartEntries.get(i);
			final OrderEntry orderEntry = orderEntries.get(i);
			assertEquals(cartEntry.getEntryNumber(), orderEntry.getEntryNumber());
			assertEquals(cartEntry.getProduct(), orderEntry.getProduct());
			assertEquals(cartEntry.getUnit(), orderEntry.getUnit());
			assertEquals(cartEntry.getQuantity(), orderEntry.getQuantity());
		}
	}
}
