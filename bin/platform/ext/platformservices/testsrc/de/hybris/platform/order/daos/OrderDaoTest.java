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
package de.hybris.platform.order.daos;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.fest.assertions.GenericAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrderDaoTest extends ServicelayerTransactionalTest
{
	private final static Logger LOG = Logger.getLogger(OrderDaoTest.class);

	private static final String DEFAULT_CART = "";
	private static final String IN_MEMORY_CART = InMemoryCartModel._TYPECODE;

	private static final int NOT_EXISTING_ORDER_ENTRY_NUMBER = 0;
	private static final int FIRST_ORDER_ENTRY_NUMBER = NOT_EXISTING_ORDER_ENTRY_NUMBER + 11;
	private static final int SECOND_ORDER_ENTRY_NUMBER = FIRST_ORDER_ENTRY_NUMBER + 11;
	private static final int THIRD_ORDER_ENTRY_NUMBER = SECOND_ORDER_ENTRY_NUMBER + 11;

	@Resource
	private CartService cartService;
	@Resource
	private OrderService orderService;
	@Resource
	private OrderDao orderDao;
	@Resource
	private ProductService productService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private ConfigurationService configurationService;

	private CartModel cart;
	private OrderModel order;
	private ProductModel product;
	private ProductModel otherProduct;
	private ProductModel unknownProduct;
	private DebitPaymentInfoModel paymentInfo;
	private AddressModel deliveryAddress;
	private CurrencyModel myCurrency;
	private DeliveryModeModel deliveryMode;
	private PaymentModeModel paymentMode;
	private UnitModel unit;
	private String cartTypeToRestore;

	@Before
	public void setUp()
	{
		LOG.info("### Before -> Session cart type: " + jaloSession.getSessionContext().getAttribute(JaloSession.CART_TYPE)
				+ " configured cart " + jaloSession.getTenant().getConfig().getParameter(JaloSession.CART_TYPE));
		cartTypeToRestore = (String) configurationService.getConfiguration().getProperty(JaloSession.CART_TYPE);
		LOG.info("### Before -> Cart type to restore -> " + cartTypeToRestore);
	}

	@After
	public void tearDown()
	{
		LOG.info("### After -> Cart type to restore -> " + cartTypeToRestore);
		configureCartTypeTo(cartTypeToRestore);
		LOG.info("### After -> Session cart type: " + jaloSession.getSessionContext().getAttribute(JaloSession.CART_TYPE)
				+ " configured cart " + jaloSession.getTenant().getConfig().getParameter(JaloSession.CART_TYPE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOrdersByCurrencyForDefaultCart()
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByCurrency();
	}

	@Test
	public void testFindOrdersByCurrencyFoundForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByCurrencyFound();
	}

	@Test
	public void testFindOrdersByCurrencyNotFoundForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByCurrencyNotFound();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOrdersByDeliveryModeForDefaultCart()
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByDeliveryMode();
	}

	@Test
	public void testFindOrdersByDeliveryModeFoundForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByDeliveryModeFound();
	}

	@Test
	public void testFindOrdersByDeliveryModeNotFoundForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByDeliveryModeNotFound();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOrdersByPaymentModeForDefaultCart()
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByPaymentMode();
	}

	@Test
	public void testFindOrdersByPaymentModeFoundForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByPaymentModeFound();
	}

	@Test
	public void testFindOrdersByPaymentModeNotFoundForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		testFindOrdersByPaymentModeNotFound();
	}

	@Test
	public void shouldFindNoEntriesForNotExistingEntryNumberForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		shouldFindNoEntriesForNotExistingEntryNumber();
	}

	@Test
	public void shouldFindEntriesForExistingEntryNumberForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		shouldFindEntriesForExistingEntryNumber();
	}

	@Test
	public void shouldFindNoEntriesForInvalidEntryNumberRangeForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		shouldFindNoEntriesForInvalidEntryNumberRange();
	}

	@Test
	public void shouldFindEntriesForValidEntryNumberRangeForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		shouldFindEntriesForValidEntryNumberRange();
	}

	@Test
	public void shouldFindNoEntriesForUnknownProductForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		shouldFindNoEntriesForUnknownProduct();
	}

	@Test
	public void shouldFindEntriesForValidProductForDefaultCart() throws InvalidCartException
	{
		setUpWithCartType(DEFAULT_CART);
		shouldFindEntriesForValidProduct();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOrdersByCurrencyForInMemoryCart()
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByCurrency();
	}

	@Test
	public void testFindOrdersByCurrencyFoundForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByCurrencyFound();
	}

	@Test
	public void testFindOrdersByCurrencyNotFoundForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByCurrencyNotFound();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOrdersByDeliveryModeForInMemoryCart()
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByDeliveryMode();
	}

	@Test
	public void testFindOrdersByDeliveryModeFoundForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByDeliveryModeFound();
	}

	@Test
	public void testFindOrdersByDeliveryModeNotFoundForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByDeliveryModeNotFound();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOrdersByPaymentModeForInMemoryCart()
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByPaymentMode();
	}

	@Test
	public void testFindOrdersByPaymentModeFoundForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByPaymentModeFound();
	}

	@Test
	public void testFindOrdersByPaymentModeNotFoundForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		testFindOrdersByPaymentModeNotFound();
	}

	@Test
	public void shouldFindNoEntriesForNotExistingEntryNumberForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		shouldFindNoEntriesForNotExistingEntryNumber();
	}

	@Test
	public void shouldFindEntriesForExistingEntryNumberForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		shouldFindEntriesForExistingEntryNumber();
	}

	@Test
	public void shouldFindNoEntriesForInvalidEntryNumberRangeForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		shouldFindNoEntriesForInvalidEntryNumberRange();
	}

	@Test
	public void shouldFindEntriesForValidEntryNumberRangeForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		shouldFindEntriesForValidEntryNumberRange();
	}

	@Test
	public void shouldFindNoEntriesForUnknownProductForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		shouldFindNoEntriesForUnknownProduct();
	}

	@Test
	public void shouldFindEntriesForValidProductForInMemoryCart() throws InvalidCartException
	{
		setUpWithCartType(IN_MEMORY_CART);
		shouldFindEntriesForValidProduct();
	}

	private void testFindOrdersByCurrency()
	{
		orderDao.findOrdersByCurrency(null);
	}

	private void testFindOrdersByCurrencyFound() throws InvalidCartException
	{

		cartService.addToCart(cart, product, 2, null);
		cart.setCurrency(myCurrency);
		order = orderService.placeOrder(cart, deliveryAddress, null, paymentInfo);
		assertEquals("Orders currency is not as expected", myCurrency, order.getCurrency());
		final Collection<AbstractOrderModel> fetchedOrders = orderDao.findOrdersByCurrency(myCurrency);
		Assert.assertCollectionElements(fetchedOrders, order);
	}

	private void testFindOrdersByCurrencyNotFound() throws InvalidCartException
	{
		cartService.addToCart(cart, product, 2, null);
		order = orderService.placeOrder(cart, deliveryAddress, null, paymentInfo);
		Assert.assertNotEquals("Orders currency is not as expected", order.getCurrency(), myCurrency);
		final Collection<AbstractOrderModel> fetchedOrders = orderDao.findOrdersByCurrency(myCurrency);
		assertTrue("Fetched collection is expected to be empty", fetchedOrders.isEmpty());
	}

	private void testFindOrdersByDeliveryMode()
	{
		orderDao.findOrdersByDelivereMode(null);
	}

	private void testFindOrdersByDeliveryModeFound() throws InvalidCartException
	{
		cartService.addToCart(cart, product, 2, null);
		cart.setDeliveryMode(deliveryMode);
		order = orderService.placeOrder(cart, deliveryAddress, null, paymentInfo);
		assertEquals("Orders delivery mode is not as expected", deliveryMode, order.getDeliveryMode());
		final Collection<AbstractOrderModel> fetchedOrders = orderDao.findOrdersByDelivereMode(deliveryMode);
		Assert.assertCollectionElements(fetchedOrders, order);
	}

	private void testFindOrdersByDeliveryModeNotFound() throws InvalidCartException
	{
		cartService.addToCart(cart, product, 2, null);
		order = orderService.placeOrder(cart, deliveryAddress, null, paymentInfo);
		assertNull("Orders delivery mode is not as expected", order.getDeliveryMode());
		final Collection<AbstractOrderModel> fetchedOrders = orderDao.findOrdersByDelivereMode(deliveryMode);
		assertTrue("Fetched collection is expected to be empty", fetchedOrders.isEmpty());
	}

	private void testFindOrdersByPaymentMode()
	{
		orderDao.findOrdersByPaymentMode(null);
	}

	private void testFindOrdersByPaymentModeFound() throws InvalidCartException
	{
		cartService.addToCart(cart, product, 2, null);
		cart.setPaymentMode(paymentMode);
		order = orderService.placeOrder(cart, deliveryAddress, null, paymentInfo);
		assertEquals("Orders payment mode is not as expected", paymentMode, order.getPaymentMode());
		final Collection<AbstractOrderModel> fetchedOrders = orderDao.findOrdersByPaymentMode(paymentMode);
		Assert.assertCollectionElements(fetchedOrders, order);
	}

	private void testFindOrdersByPaymentModeNotFound() throws InvalidCartException
	{
		cartService.addToCart(cart, product, 2, null);
		order = orderService.placeOrder(cart, deliveryAddress, null, paymentInfo);
		assertNull("Orders payment mode is not as expected", order.getPaymentMode());
		final Collection<AbstractOrderModel> fetchedOrders = orderDao.findOrdersByPaymentMode(paymentMode);
		assertTrue("Fetched collection is expected to be empty", fetchedOrders.isEmpty());
	}

	private void shouldFindNoEntriesForNotExistingEntryNumber() throws InvalidCartException
	{
		final CartModel orderToTest = givenCartWithEntries();

		final List<AbstractOrderEntryModel> entries = orderDao.findEntriesByNumber(CartEntryModel._TYPECODE, orderToTest,
				NOT_EXISTING_ORDER_ENTRY_NUMBER);

		assertThat(entries).isNotNull().isEmpty();
	}

	private void shouldFindEntriesForExistingEntryNumber() throws InvalidCartException
	{
		final CartModel orderToTest = givenCartWithEntries();

		final List<AbstractOrderEntryModel> entries = orderDao.findEntriesByNumber(CartEntryModel._TYPECODE, orderToTest,
				SECOND_ORDER_ENTRY_NUMBER);

		assertThat(entries).isNotNull().hasSize(1);
		assertThatEntry(entries.get(0)).isNotNull().hasProduct(otherProduct).hasEntryNumber(SECOND_ORDER_ENTRY_NUMBER);
	}

	private void shouldFindNoEntriesForInvalidEntryNumberRange() throws InvalidCartException
	{
		final CartModel orderToTest = givenCartWithEntries();

		final List<AbstractOrderEntryModel> entries = orderDao.findEntriesByNumber(CartEntryModel._TYPECODE, orderToTest,
				THIRD_ORDER_ENTRY_NUMBER, NOT_EXISTING_ORDER_ENTRY_NUMBER);

		assertThat(entries).isNotNull().isEmpty();
	}

	private void shouldFindEntriesForValidEntryNumberRange() throws InvalidCartException
	{
		final CartModel orderToTest = givenCartWithEntries();

		final List<AbstractOrderEntryModel> entries = orderDao.findEntriesByNumber(CartEntryModel._TYPECODE, orderToTest,
				FIRST_ORDER_ENTRY_NUMBER, SECOND_ORDER_ENTRY_NUMBER);

		assertThat(entries).isNotNull().hasSize(2);
		assertThatEntry(entries.get(0)).isNotNull().hasProduct(product).hasEntryNumber(FIRST_ORDER_ENTRY_NUMBER);
		assertThatEntry(entries.get(1)).isNotNull().hasProduct(otherProduct).hasEntryNumber(SECOND_ORDER_ENTRY_NUMBER);
	}

	private void shouldFindNoEntriesForUnknownProduct() throws InvalidCartException
	{
		final CartModel orderToTest = givenCartWithEntries();

		final List<AbstractOrderEntryModel> entries = orderDao.findEntriesByProduct(CartEntryModel._TYPECODE, orderToTest,
				unknownProduct);

		assertThat(entries).isNotNull().isEmpty();
	}

	private void shouldFindEntriesForValidProduct() throws InvalidCartException
	{
		final CartModel orderToTest = givenCartWithEntries();

		final List<AbstractOrderEntryModel> entries = orderDao.findEntriesByProduct(CartEntryModel._TYPECODE, orderToTest, product);

		assertThat(entries).isNotNull().hasSize(2);
		assertThatEntry(entries.get(0)).isNotNull().hasProduct(product).hasEntryNumber(FIRST_ORDER_ENTRY_NUMBER);
		assertThatEntry(entries.get(1)).isNotNull().hasProduct(product).hasEntryNumber(THIRD_ORDER_ENTRY_NUMBER);
	}

	private void setUpWithCartType(final String cartType)
	{
		configureCartTypeTo(cartType);
		try
		{
			createCoreData();
			createDefaultCatalog();
		}
		catch (final Exception e)
		{
			org.junit.Assert.fail(e.getMessage());
		}

		product = productService.getProductForCode("testProduct0");
		otherProduct = productService.getProductForCode("testProduct1");
		unknownProduct = productService.getProductForCode("testProduct3");
		cart = cartService.getSessionCart();
		final UserModel user = userService.getCurrentUser();

		deliveryAddress = modelService.create(AddressModel.class);
		deliveryAddress.setOwner(user);
		deliveryAddress.setFirstname("Juergen");
		deliveryAddress.setLastname("Albertsen");
		deliveryAddress.setTown("Muenchen");

		paymentInfo = modelService.create(DebitPaymentInfoModel.class);
		paymentInfo.setOwner(cart);
		paymentInfo.setBank("MeineBank");
		paymentInfo.setUser(user);
		paymentInfo.setAccountNumber("34434");
		paymentInfo.setBankIDNumber("1111112");
		paymentInfo.setBaOwner("Ich");

		myCurrency = modelService.create(CurrencyModel.class);
		myCurrency.setActive(Boolean.TRUE);
		myCurrency.setIsocode("MCURR");
		myCurrency.setName("mYCurrency");
		myCurrency.setSymbol("mc");
		myCurrency.setConversion(Double.valueOf(1.3));
		modelService.save(myCurrency);

		deliveryMode = modelService.create(DeliveryModeModel.class);
		deliveryMode.setActive(Boolean.TRUE);
		deliveryMode.setCode("myTestDeliveryMode");
		deliveryMode.setName("my delivery mode");
		modelService.save(deliveryMode);

		paymentMode = modelService.create(PaymentModeModel.class);
		paymentMode.setActive(Boolean.TRUE);
		paymentMode.setCode("myTestPaymentMode");
		paymentMode.setName("my payment mode");
		paymentMode.setPaymentInfoType(typeService.getComposedTypeForCode(DebitPaymentInfoModel._TYPECODE));
		modelService.save(paymentMode);

		unit = modelService.create(UnitModel.class);
		unit.setCode("uN");
		unit.setUnitType("uT");
		modelService.save(unit);
	}

	private void configureCartTypeTo(final String cartType)
	{
		configurationService.getConfiguration().setProperty(JaloSession.CART_TYPE, cartType);
		jaloSession.getTenant().getConfig().setParameter(JaloSession.CART_TYPE, cartType);
	}

	private CartModel givenCartWithEntries()
	{
		cartService.addNewEntry(cart, product, 1, unit, FIRST_ORDER_ENTRY_NUMBER, false);
		cartService.addNewEntry(cart, otherProduct, 2, unit, SECOND_ORDER_ENTRY_NUMBER, false);
		cartService.addNewEntry(cart, product, 3, unit, THIRD_ORDER_ENTRY_NUMBER, false);
		modelService.save(cart);
		return cart;
	}

	private AbstractOrderEntryModelAssert assertThatEntry(final AbstractOrderEntryModel model)
	{
		return new AbstractOrderEntryModelAssert(Objects.requireNonNull(model));
	}

	private static class AbstractOrderEntryModelAssert extends
			GenericAssert<AbstractOrderEntryModelAssert, AbstractOrderEntryModel>
	{
		private final AbstractOrderEntryModel model;

		public AbstractOrderEntryModelAssert(final AbstractOrderEntryModel actual)
		{
			super(AbstractOrderEntryModelAssert.class, actual);
			this.model = actual;
		}

		public AbstractOrderEntryModelAssert hasProduct(final ProductModel product)
		{
			isNotNull();
			assertThat(model.getProduct()).isEqualTo(product);
			return this;
		}

		public AbstractOrderEntryModelAssert hasEntryNumber(final int number)
		{
			isNotNull();
			assertThat(model.getEntryNumber()).isEqualTo(number);
			return this;
		}
	}
}
