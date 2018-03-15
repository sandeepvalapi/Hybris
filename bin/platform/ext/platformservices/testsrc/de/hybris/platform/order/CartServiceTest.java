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
package de.hybris.platform.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteEntryModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CartServiceTest extends ServicelayerTransactionalTest
{
	private static final String TEST_USER_ID = "cartTestUser";

	@Resource
	private ProductService productService;
	@Resource
	private CartService cartService;
	@Resource
	private CalculationService calculationService;
	@Resource
	private I18NService i18nService;
	@Resource
	private SessionService sessionService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private CartFactory cartFactory;
	@Resource
	private ConfigurationService configurationService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		createTestUser();
	}

	@Test
	public void testAddToCart() throws InvalidCartException
	{
		final ProductModel product = productService.getProduct("testProduct0");
		final CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product, 2, null);
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		assertEquals("Number of entries", 1, entries.size());
		final AbstractOrderEntryModel entry = entries.iterator().next();
		assertEquals("Quantity", Long.valueOf(2), entry.getQuantity());
		assertEquals("Cart total", entry.getTotalPrice(), cart.getTotalPrice());
	}

	@Test
	public void testGetSessionCart()
	{
		assertFalse("Got unexpected session cart before calling getSessionCart()", cartService.hasSessionCart());
		final CartModel cart = cartService.getSessionCart();
		assertNotNull("No cart auto-created", cart);
		assertEquals("SL cart <> jalo cart", modelService.getSource(cart), getJaloSessionCartWithoutAutoCreate());

		assertSame("repeated call to CartService.getSessionCart() returned different carts", cart, cartService.getSessionCart());
	}

	@Test
	public void testGetSessionCartInLocalView()
	{
		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				testGetSessionCart();
			}
		});
	}

	@Test
	public void testGetSessionCartWhenJaloSessionUpdates()
	{
		assertFalse("JaloSession had cart before test", jaloSession.hasCart());
		assertFalse("CartService had cart before test", cartService.hasSessionCart());

		final Cart jaloCartAutoCreated = jaloSession.getCart();

		assertNotNull("Jalo cart has not been auto-created", jaloCartAutoCreated);
		final CartModel cart = cartService.getSessionCart();
		assertNotNull("Cart service doesnt have cart after Jalo cart auto-creation", cart);
		assertEquals("CartService got different session cart than JaloSession", jaloCartAutoCreated, modelService.getSource(cart));
		assertSame("repeated getSessionCart() returnde different models", cart, cartService.getSessionCart());

		jaloSession.removeCart();

		assertFalse("JaloSession hads cart after removal", jaloSession.hasCart());
		assertFalse("CartService hads cart after removal", cartService.hasSessionCart());

		final Cart jaloCartAutoCreated2 = jaloSession.getCart();

		assertNotNull("Jalo cart has not been auto-created", jaloCartAutoCreated2);
		final CartModel cart2 = cartService.getSessionCart();
		assertNotNull("Cart service doesnt have cart after Jalo cart auto-creation", cart2);
		assertEquals("CartService got different session cart than JaloSession", jaloCartAutoCreated2,
				modelService.getSource(cart2));
		assertSame("repeated getSessionCart() returnde different models", cart2, cartService.getSessionCart());
		assertFalse(cart.equals(cart2));
	}

	private Cart getJaloSessionCartWithoutAutoCreate()
	{
		return jaloSession.hasCart() ? jaloSession.getCart() : null;
	}

	@Test
	public void testRemoveSessionCart()
	{
		assertFalse("Got unexpected session cart before calling getSessionCart()", cartService.hasSessionCart());

		final CartModel oldCart = cartService.getSessionCart();
		assertNotNull("No cart auto-created", oldCart);

		cartService.removeSessionCart();

		assertNull("Jalo session cart not removed after CartService.removeSessionCart()", getJaloSessionCartWithoutAutoCreate());
		assertFalse("Still got SL session cart after CartService.removeSessionCart()", cartService.hasSessionCart());
		assertTrue("previous session cart not removed", modelService.isRemoved(oldCart));

		final CartModel newCart = cartService.getSessionCart();
		assertNotNull("No new cart auto-created", newCart);
		assertFalse(oldCart.equals(newCart));
		assertEquals(modelService.getSource(newCart), getJaloSessionCartWithoutAutoCreate());
	}

	@Test
	public void testSetSessionCart()
	{
		assertFalse("JaloSession had cart before test", jaloSession.hasCart());
		assertFalse("CartService had cart before test", cartService.hasSessionCart());

		final CartModel newOne = createNewCart();

		assertFalse("JaloSession has cart after creating new one", jaloSession.hasCart());
		assertFalse("CartService has cart after creating new one", cartService.hasSessionCart());

		cartService.setSessionCart(newOne);

		assertTrue("JaloSession has no cart after setting new one", jaloSession.hasCart());
		assertTrue("CartService has no cart after setting new one", cartService.hasSessionCart());

		assertEquals(newOne, cartService.getSessionCart());
		assertEquals(modelService.getSource(newOne), getJaloSessionCartWithoutAutoCreate());

		cartService.setSessionCart(null);

		assertFalse("JaloSession has cart after setting NULL", jaloSession.hasCart());
		assertFalse("CartService has cart after setting NULL", cartService.hasSessionCart());
	}

	@Test
	/*
	 * * PLA-7681
	 */
	public void testChangeQuantity() throws InvalidCartException
	{
		final ProductModel product = productService.getProduct("testProduct0");
		final CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product, 1, null);
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		assertEquals("Number of entries", 1, entries.size());
		final AbstractOrderEntryModel entry = entries.iterator().next();
		assertEquals("Quantity", Long.valueOf(1), entry.getQuantity());
		assertEquals("Cart total", entry.getTotalPrice(), cart.getTotalPrice());

		final Double singleArticlePrice = cart.getTotalPrice();
		final Double doublePrice = Double.valueOf(2 * singleArticlePrice.doubleValue());
		// change quantity
		final List<Long> newQuantities = new ArrayList<Long>();
		newQuantities.add(Long.valueOf(2));
		cartService.updateQuantities(cart, newQuantities);
		cartService.calculateCart(cart);
		final CartModel cartAfterQuantityChange = cartService.getSessionCart();
		final List<AbstractOrderEntryModel> entriesDouble = cartAfterQuantityChange.getEntries();
		assertEquals("Number of entries for two items of product", 1, entriesDouble.size());
		final AbstractOrderEntryModel entryDouble = entriesDouble.iterator().next();
		assertEquals("Quantity for two items of product", Long.valueOf(2), entryDouble.getQuantity());
		assertEquals("Cart total for two items of product", doublePrice, cartAfterQuantityChange.getTotalPrice());
		assertEquals("Total price for two items of product", doublePrice, entryDouble.getTotalPrice());
	}

	/*
	 * PLA-7735 - CartService.calculateCart throws exception when setting a quantity to zero
	 */
	@Test
	public void testSetCartEntryQuantityToZero() throws InvalidCartException
	{
		final ProductModel product = productService.getProduct("testProduct0");
		final CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product, 1, null);
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		assertEquals("Number of entries", 1, entries.size());
		final AbstractOrderEntryModel entry = entries.iterator().next();
		assertEquals("Quantity", Long.valueOf(1), entry.getQuantity());
		assertEquals("Cart total", entry.getTotalPrice(), cart.getTotalPrice());
		//set quantity to zero
		final List<Long> newQuantities = new ArrayList<Long>();
		newQuantities.add(Long.valueOf(0));
		cartService.updateQuantities(cart, newQuantities);
		cartService.calculateCart(cart);
	}

	@Test
	public void testChangeSomeQuantitiesInCartOldStyle() throws InvalidCartException
	{
		final ProductModel product0 = productService.getProduct("testProduct0");
		assertNotNull("no product", product0);
		final ProductModel product1 = productService.getProduct("testProduct1");
		assertNotNull("no product", product1);
		final ProductModel product2 = productService.getProduct("testProduct2");
		assertNotNull("no product", product2);
		final ProductModel product3 = productService.getProduct("testProduct3");
		assertNotNull("no product", product3);

		final CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product0, 10, null);
		cartService.addToCart(cart, product1, 15, null);
		cartService.addToCart(cart, product2, 1, null);
		cartService.addToCart(cart, product3, 18, null);

		assertEquals("Number of entries", 4, cart.getEntries().size());
		cartService.calculateCart(cart);
		assertEquals("Number of entries", 4, cart.getEntries().size());

		final List<Long> newQuantities = new ArrayList<Long>();
		newQuantities.add(Long.valueOf(5));
		newQuantities.add(Long.valueOf(0));
		newQuantities.add(Long.valueOf(0));
		newQuantities.add(Long.valueOf(10));
		cartService.updateQuantities(cart, newQuantities);// should trigger save of cart as well as result of prepare interceptors!!!

		//zero values
		assertEquals("Number of entries", 2, cart.getEntries().size());
		cartService.calculateCart(cart);
		assertEquals("Number of entries", 2, cart.getEntries().size());

		newQuantities.clear();
		newQuantities.add(Long.valueOf(-5)); // means: remove since it's < 1
		newQuantities.add(Long.valueOf(5));

		cartService.updateQuantities(cart, newQuantities);
		cartService.calculateCart(cart);

		assertEquals(1, cart.getEntries().size());
	}

	@Test
	public void testChangeSomeQuantitiesInCart() throws InvalidCartException, CalculationException
	{
		final ProductModel product0 = productService.getProduct("testProduct0");
		final ProductModel product1 = productService.getProduct("testProduct1");
		final ProductModel product2 = productService.getProduct("testProduct2");
		final ProductModel product3 = productService.getProduct("testProduct3");
		assertNotNull(product0);
		assertNotNull(product1);
		assertNotNull(product2);
		assertNotNull(product3);

		final CartModel cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product0, 10, null);
		cartService.addNewEntry(cart, product1, 15, null);
		cartService.addNewEntry(cart, product2, 1, null);
		cartService.addNewEntry(cart, product3, 18, null);
		modelService.save(cart);

		assertEquals("Number of entries", 4, cart.getEntries().size());
		calculationService.calculate(cart);
		assertEquals("Number of entries", 4, cart.getEntries().size());

		assertEquals(Long.valueOf(10), cartService.getEntryForNumber(cart, 0).getQuantity());
		assertEquals(Long.valueOf(15), cartService.getEntryForNumber(cart, 1).getQuantity());
		assertEquals(Long.valueOf(1), cartService.getEntryForNumber(cart, 2).getQuantity());
		assertEquals(Long.valueOf(18), cartService.getEntryForNumber(cart, 3).getQuantity());

		final Map<Integer, Long> newQuantities = new HashMap<Integer, Long>();
		newQuantities.put(Integer.valueOf(0), Long.valueOf(5));
		newQuantities.put(Integer.valueOf(1), Long.valueOf(0));
		newQuantities.put(Integer.valueOf(2), Long.valueOf(0));
		newQuantities.put(Integer.valueOf(3), Long.valueOf(10));
		cartService.updateQuantities(cart, newQuantities);

		//zero values
		assertEquals("Number of entries", 2, cart.getEntries().size());
		calculationService.calculate(cart);
		assertEquals("Number of entries", 2, cart.getEntries().size());

		assertEquals(Long.valueOf(5), cartService.getEntryForNumber(cart, 0).getQuantity());
		assertEquals(Long.valueOf(10), cartService.getEntryForNumber(cart, 3).getQuantity());

		newQuantities.clear();
		newQuantities.put(Integer.valueOf(3), Long.valueOf(30));

		cartService.updateQuantities(cart, newQuantities);
		assertEquals("Number of entries", 2, cart.getEntries().size());

		calculationService.calculate(cart);
		assertEquals("Number of entries", 2, cart.getEntries().size());

		assertEquals(Long.valueOf(5), cartService.getEntryForNumber(cart, 0).getQuantity());
		assertEquals(Long.valueOf(30), cartService.getEntryForNumber(cart, 3).getQuantity());

		newQuantities.clear();
		newQuantities.put(Integer.valueOf(0), Long.valueOf(-5)); // means: remove since it's < 1
		newQuantities.put(Integer.valueOf(3), Long.valueOf(5));

		cartService.updateQuantities(cart, newQuantities);
		assertEquals("Number of entries", 1, cart.getEntries().size());

		calculationService.calculate(cart);
		assertEquals("Number of entries", 1, cart.getEntries().size());

		assertEquals(Long.valueOf(5), cartService.getEntryForNumber(cart, 3).getQuantity());
	}

	/**
	 * Test checks currency information in cart after currency change. When currency has been changed cart is
	 * recalculated.
	 */
	@Test
	public void testCartAfterChangeCurrency() throws InvalidCartException
	{
		final ProductModel product = productService.getProduct("testProduct0");
		final CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product, 1, null);
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		assertEquals("Number of entries", 1, entries.size());
		final AbstractOrderEntryModel entry = entries.iterator().next();
		assertEquals("Quantity", Long.valueOf(1), entry.getQuantity());
		assertEquals("Cart total", entry.getTotalPrice(), cart.getTotalPrice());

		final CurrencyModel currentCurrency = i18nService.getCurrentCurrency();
		final Set<CurrencyModel> allCurrencies = i18nService.getAllCurrencies();
		CurrencyModel newCurrency = null;
		for (final CurrencyModel currencyModel : allCurrencies)
		{
			if (!currentCurrency.getIsocode().equals(currencyModel.getIsocode()))
			{
				newCurrency = currencyModel;
				break;
			}
		}
		i18nService.setCurrentCurrency(newCurrency);
		modelService.refresh(cart);
		final boolean calculateResult = cartService.calculateCart(cart);
		assertEquals(true, calculateResult);

		final CurrencyModel wholeCartCurrency = cart.getCurrency();
		assertNotNull(wholeCartCurrency);
		final List<AbstractOrderEntryModel> cartEntries = cart.getEntries();
		assertNotNull(cartEntries);
		assertEquals(1, cartEntries.size());
		final AbstractOrderEntryModel firstEntry = cartEntries.get(0);
		assertNotNull(firstEntry.getOrder());
		final CurrencyModel firstEntryCurrency = firstEntry.getOrder().getCurrency();
		assertNotNull(firstEntryCurrency);

		assertEquals(newCurrency.getIsocode(), wholeCartCurrency.getIsocode());
		assertEquals(newCurrency.getIsocode(), firstEntryCurrency.getIsocode());

	}

	/**
	 * Test adds into cart one product and change currency of environment. Currency is changed to first currency
	 * available in system, different than currennt currency. <br>
	 * After currency change cart id loaded again and test checks if cart and cart items have correct currency settings.
	 *
	 * @throws InvalidCartException
	 */
	@Test
	public void testRefreshCartAfterChangeCurrency() throws InvalidCartException
	{
		// add product to cart
		final ProductModel product = productService.getProduct("testProduct0");
		CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product, 1, null);
		cart = null;

		// load available currencies
		final CurrencyModel currentCurrency = i18nService.getCurrentCurrency();
		final Set<CurrencyModel> allCurrencies = i18nService.getAllCurrencies();
		CurrencyModel newCurrency = null;
		for (final CurrencyModel currencyModel : allCurrencies)
		{
			if (!currentCurrency.getIsocode().equals(currencyModel.getIsocode()))
			{
				newCurrency = currencyModel;
				break;
			}
		}
		// set new currency
		i18nService.setCurrentCurrency(newCurrency);

		// load cart again and validate new currency settings
		cart = cartService.getSessionCart();
		// changing the current currency sets the currency on the jalo-cart only,
		// an existing cart model doesn't reflect this changes without refresh!
		modelService.refresh(cart);
		final CurrencyModel wholeCartCurrency = cart.getCurrency();
		assertNotNull(wholeCartCurrency);
		final List<AbstractOrderEntryModel> cartEntries = cart.getEntries();
		assertNotNull(cartEntries);
		assertEquals(1, cartEntries.size());
		final AbstractOrderEntryModel firstEntry = cartEntries.get(0);
		assertNotNull(firstEntry.getOrder());
		final CurrencyModel firstEntryCurrency = firstEntry.getOrder().getCurrency();
		assertNotNull(firstEntryCurrency);

		assertEquals(newCurrency.getIsocode(), wholeCartCurrency.getIsocode());
		assertEquals(newCurrency.getIsocode(), firstEntryCurrency.getIsocode());

	}

	/**
	 * Tests appending cloned cart entries of source cart to the target cart
	 */
	@Test
	public void testAppendToCart()
	{
		final ProductModel product0 = productService.getProductForCode("testProduct0");
		final ProductModel product1 = productService.getProductForCode("testProduct1");
		final ProductModel product2 = productService.getProductForCode("testProduct2");

		//target cart
		final CartModel cartTarget = createNewCart();
		cartService.addNewEntry(cartTarget, product0, 1, null);
		cartService.saveOrder(cartTarget);

		//try some corner cases first
		boolean success = false;
		try
		{
			cartService.appendToCart(null, cartTarget);
			fail("IllegalArgumentException expected for null sourceCart");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		assertTrue("IllegalArgumentException expected for null sourceCart", success);

		//source cart

		final CartModel cartSource = createNewCart();
		cartService.addNewEntry(cartSource, product1, 2, null);
		cartService.addNewEntry(cartSource, product2, 3, null);
		cartService.saveOrder(cartSource);

		success = false;
		try
		{
			cartService.appendToCart(cartSource, null);
			fail("IllegalArgumentException expected for null targetCart");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		assertTrue("IllegalArgumentException expected for null targetCart", success);

		//call the business method
		cartService.appendToCart(cartSource, cartTarget);

		//check the merged target cart's size
		assertEquals("Cart entries size should be 3", 3, cartTarget.getEntries().size());

		//source cart should be unchanged
		assertEquals("Source cart entries size should be 2", 2, cartSource.getEntries().size());

		//check type
		assertEquals("wrong entry type", "CartEntry", modelService.getModelType(cartTarget.getEntries().get(0)));
		assertEquals("wrong entry type", "CartEntry", modelService.getModelType(cartTarget.getEntries().get(1)));
		assertEquals("wrong entry type", "CartEntry", modelService.getModelType(cartTarget.getEntries().get(2)));

		//check products
		assertEquals("Product in cart entry 0 not as expected", product0, cartTarget.getEntries().get(0).getProduct());
		assertEquals("Product in cart entry 1 not as expected", cartSource.getEntries().get(0).getProduct(),
				cartTarget.getEntries().get(1).getProduct());
		assertEquals("Product in cart entry 2 not as expected", cartSource.getEntries().get(1).getProduct(),
				cartTarget.getEntries().get(2).getProduct());

		//check quantities
		assertEquals("Cart entry quantity not as expected", 1, cartTarget.getEntries().get(0).getQuantity().intValue());
		assertEquals("Cart entry quantity not as expected", 2, cartTarget.getEntries().get(1).getQuantity().intValue());
		assertEquals("Cart entry quantity not as expected", 3, cartTarget.getEntries().get(2).getQuantity().intValue());

		//check entry numbers
		assertEquals("Cart entry number not as expected", 0, cartTarget.getEntries().get(0).getEntryNumber().intValue());
		assertEquals("Cart entry number not as expected", 1, cartTarget.getEntries().get(1).getEntryNumber().intValue());
		assertEquals("Cart entry number not as expected", 2, cartTarget.getEntries().get(2).getEntryNumber().intValue());

		//append to the InMemoryCart:
		final InMemoryCartModel inMemoryTargetCart = modelService.create(InMemoryCartModel.class);
		inMemoryTargetCart.setUser(userService.getCurrentUser());
		inMemoryTargetCart.setCurrency(commonI18NService.getCurrentCurrency());
		inMemoryTargetCart.setDate(new Date());
		inMemoryTargetCart.setNet(Boolean.TRUE);
		modelService.save(inMemoryTargetCart);

		cartService.appendToCart(cartSource, inMemoryTargetCart);

		//check type
		assertEquals("wrong entry type", "InMemoryCartEntry", modelService.getModelType(inMemoryTargetCart.getEntries().get(0)));
		assertEquals("wrong entry type", "InMemoryCartEntry", modelService.getModelType(inMemoryTargetCart.getEntries().get(1)));

		//check products
		assertEquals("Product in cart entry 0 not as expected", cartSource.getEntries().get(0).getProduct(),
				inMemoryTargetCart.getEntries().get(0).getProduct());
		assertEquals("Product in cart entry 1 not as expected", cartSource.getEntries().get(1).getProduct(),
				inMemoryTargetCart.getEntries().get(1).getProduct());

		//check quantities
		assertEquals("Cart entry quantity not as expected", 2, inMemoryTargetCart.getEntries().get(0).getQuantity().intValue());
		assertEquals("Cart entry quantity not as expected", 3, inMemoryTargetCart.getEntries().get(1).getQuantity().intValue());


		//check entry numbers
		assertEquals("Cart entry number not as expected", 0, inMemoryTargetCart.getEntries().get(0).getEntryNumber().intValue());
		assertEquals("Cart entry number not as expected", 1, inMemoryTargetCart.getEntries().get(1).getEntryNumber().intValue());
	}

	@Test
	public void testCustomCartTypeCartService()
	{
		final Configuration config = configurationService.getConfiguration();

		//Old cart is restored at the end of the test, as exceptions occur during HybrisJUnit4Test.finish()...
		final String configuredCartType = config.getString(JaloSession.CART_TYPE, "Cart");
		//cartService.setSessionCart(null);
		cartService.removeSessionCart();

		config.setProperty(JaloSession.CART_TYPE, "Cart");

		final CartModel standardCart = cartService.getSessionCart();
		assertEquals(CartModel.class, standardCart.getClass());
		cartService.removeSessionCart();

		config.setProperty(JaloSession.CART_TYPE, "InMemoryCart");
		final CartModel inMemoryCart = cartService.getSessionCart();
		assertEquals(InMemoryCartModel.class, inMemoryCart.getClass());

		config.setProperty(JaloSession.CART_TYPE, configuredCartType);
		cartService.removeSessionCart();
	}

	@Test
	public void testCustomCartTypeCartFactory()
	{
		final Configuration config = configurationService.getConfiguration();
		final String configuredCartType = config.getString(JaloSession.CART_TYPE, "Cart");
		config.setProperty(JaloSession.CART_TYPE, "Cart");
		final CartModel standardCart = cartFactory.createCart();
		assertEquals(CartModel.class, standardCart.getClass());

		config.setProperty(JaloSession.CART_TYPE, "InMemoryCart");
		final CartModel inMemoryCart = cartFactory.createCart();
		assertEquals(InMemoryCartModel.class, inMemoryCart.getClass());

		config.setProperty(JaloSession.CART_TYPE, configuredCartType);
	}

	@Test
	public void testChangeCurrentCartUser()
	{
		boolean success = false;
		try
		{
			cartService.changeCurrentCartUser(null);
			fail("IllegalArgumentException expected for the null user input");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		Assert.assertTrue("IllegalArgumentException expected for the null user input", success);

		final UserModel previousUser = cartService.getSessionCart().getUser();

		final UserModel anonymous = userService.getAnonymousUser();
		cartService.changeCurrentCartUser(anonymous);
		modelService.detachAll();
		Assert.assertEquals("Current cart user : anonymous expected", anonymous, cartService.getSessionCart().getUser());

		final UserModel admin = userService.getAdminUser();
		cartService.changeCurrentCartUser(admin);
		modelService.detachAll();
		Assert.assertEquals("Current cart user : admin expected", admin, cartService.getSessionCart().getUser());

		final UserModel testUser = userService.getUserForUID(TEST_USER_ID);
		cartService.changeCurrentCartUser(testUser);
		modelService.detachAll();
		Assert.assertEquals("Current cart user : testUser expected", testUser, cartService.getSessionCart().getUser());

		cartService.changeCurrentCartUser(previousUser);
	}

	@Test
	public void testCreateCartFromQuote()
	{
		final QuoteModel quote = modelService.create(QuoteModel.class);
		quote.setCode("quoteCode");
		quote.setState(QuoteState.DRAFT);
		quote.setVersion(Integer.valueOf(1));
		quote.setDate(new Date());
		quote.setCurrency(commonI18NService.getCurrentCurrency());
		quote.setUser(userService.getCurrentUser());
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		final ProductModel product0 = productService.getProduct("testProduct0");
		final int qty = 10;
		final QuoteEntryModel entry0 = new QuoteEntryModel();
		entry0.setQuantity(Long.valueOf(qty));
		entry0.setProduct(product0);
		entry0.setUnit(product0.getUnit());
		entry0.setOrder(quote);
		entries.add(entry0);
		quote.setEntries(entries);
		modelService.save(quote);

		final CartModel cart = cartService.createCartFromQuote(quote);
		assertNotNull("Cart is null", cart);
		assertNotNull("Cart entries are null", cart.getEntries());
		assertEquals("Size of cart entries is wrong", 1, cart.getEntries().size());
		assertEquals("Cart entry product is wrong", product0, cart.getEntries().get(0).getProduct());
		assertEquals("Cart entry product qty is wrong", qty, cart.getEntries().get(0).getQuantity().intValue());
	}

	/**
	 * Utility method to prevent the test case from using session carts
	 */
	private CartModel createNewCart()
	{
		final CartModel cart = modelService.create(CartModel.class);
		cart.setUser(userService.getCurrentUser());
		cart.setCurrency(commonI18NService.getCurrentCurrency());
		cart.setDate(new Date());
		cart.setNet(Boolean.TRUE);
		modelService.save(cart);
		return cart;
	}

	/**
	 * test user for changing the current cart user
	 */
	private void createTestUser()
	{
		final UserModel newCustomer = modelService.create(UserModel.class);
		newCustomer.setUid(TEST_USER_ID);
		//newCustomer.setCustomerID(TEST_USER_ID);
		newCustomer.setSessionCurrency(commonI18NService.getCurrency("USD"));
		newCustomer.setSessionLanguage(commonI18NService.getLanguage("en"));
		newCustomer.setGroups(Collections.<PrincipalGroupModel> singleton(userService.getUserGroupForUID("customergroup")));
		modelService.save(newCustomer);
	}
}
