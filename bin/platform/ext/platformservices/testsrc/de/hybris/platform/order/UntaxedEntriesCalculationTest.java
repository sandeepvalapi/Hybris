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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests different abstract order entries with mixed taxes, see PLA-11851.
 */
@IntegrationTest
public class UntaxedEntriesCalculationTest extends ServicelayerTest
{

	@Resource
	private CalculationService calculationService;
	@Resource
	private ModelService modelService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private ProductService productService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	UserService userService;
	@Resource
	private CartService cartService;
	@Resource
	private DeliveryModeService deliveryModeService;
	@Resource
	private CartFactory cartFactory;

	private UserModel user;
	private CatalogVersionModel catVersion;

	private DeliveryModeModel deliveryModeNet;
	private DeliveryModeModel deliveryModeGross;

	private String cfgBefore;

	private static final double DELTA = 0.00001;

	@Before
	public void setUp() throws Exception
	{
		cfgBefore = Config.getParameter("abstractorder.taxFreeEntrySupport");
		Config.setParameter("abstractorder.taxFreeEntrySupport", "true");
		final DefaultCalculationService defaultCalculationService = (DefaultCalculationService) calculationService;
		defaultCalculationService.setTaxFreeEntrySupport(true);

		importCsv("/platformservices/test/calculationMixedTaxTestData.csv", "utf-8");
		prepareData();
	}

	@After
	public void tearDown()
	{
		Config.setParameter("abstractorder.taxFreeEntrySupport", cfgBefore);

		final DefaultCalculationService defaultCalculationService = (DefaultCalculationService) calculationService;
		defaultCalculationService.setTaxFreeEntrySupport(StringUtils.isNotBlank(cfgBefore) && Boolean.parseBoolean(cfgBefore));
	}

	/**
	 * Test gross mixed tax for different entries:
	 * <ul>
	 * <li>creates 2 taxes: 10% and 20%,</li>
	 * <li>creates 2 product tax group with 10% and 20% as well,</li>
	 * <li>add product01 (110 Euro gross) including 10% tax ==> 100(net) + 10(tax) = 110 Euro,</li>
	 * <li>add product02 (240 Euro gross) including 20% tax ==> 200(net) + 40(tax) = 240 Euro,</li>
	 * <li>add product03 (300 Euro gross) with NO tax ==> 300(net) = 300 Euro,</li>
	 * <li>delivery cost is 10 Euro gross,</li>
	 * <li>checks that the sub total (gross) is 650,(110 + 240 + 300)</li>
	 * <li>checks that 10% tax has value 10.29 and 20% tax has value 41.14.</li>
	 * </ul>
	 * 
	 * @throws JaloPriceFactoryException
	 */
	@Test
	public void testGrossMixedTaxEntries() throws CalculationException, JaloPriceFactoryException
	{
		CartModel cartWithNotTaxedEntry = prepareCart(false, "product_01", "product_02", "product_03");
		calculationService.calculate(cartWithNotTaxedEntry);
		modelService.refresh(cartWithNotTaxedEntry);

		assertLastEntryNotTaxed(cartWithNotTaxedEntry);
		assertCart(cartWithNotTaxedEntry, false, 650.0);

		// test illegal states: taxed total < 0
		final List<DiscountValue> globalDiscounts = new ArrayList<DiscountValue>(cartWithNotTaxedEntry.getGlobalDiscountValues());
		globalDiscounts.add(new DiscountValue("Boom", 360.0, true, "EUR"));
		cartWithNotTaxedEntry.setGlobalDiscountValues(globalDiscounts);
		modelService.save(cartWithNotTaxedEntry);
		try
		{
			calculationService.calculateTotals(cartWithNotTaxedEntry, false);
			fail("CalculationException expected");
		}
		catch (final CalculationException e)
		{
			// fine
		}

		// test jalo layer
		final Cart cartItem = (Cart) modelService.getSource(cartWithNotTaxedEntry);
		cartItem.recalculate();
		cartWithNotTaxedEntry = (CartModel) modelService.get(cartItem);
		modelService.refresh(cartWithNotTaxedEntry);

		assertLastEntryNotTaxed(cartWithNotTaxedEntry);
		assertCart(cartWithNotTaxedEntry, false, 650.0);

		// test illegal states: taxed total < 0
		cartItem.addGlobalDiscountValue(new DiscountValue("Bang", 370, true, "EUR"));
		try
		{
			cartItem.calculateTotals(false);
			fail("CalculationException expected");
		}
		catch (final JaloPriceFactoryException e)
		{
			// fine
		}
	}

	@Test
	public void testGrossNormalTaxEntries() throws CalculationException, JaloPriceFactoryException
	{
		final CartModel cartWithoutNotTaxedEntry = prepareCart(false, "product_01", "product_02");
		calculationService.calculate(cartWithoutNotTaxedEntry);
		modelService.refresh(cartWithoutNotTaxedEntry);

		assertCart(cartWithoutNotTaxedEntry, false, 350.0);

		final Cart cartItem = (Cart) modelService.getSource(cartWithoutNotTaxedEntry);
		cartItem.recalculate();
		final CartModel cartModel = (CartModel) modelService.get(cartItem);
		modelService.refresh(cartModel);

		assertCart(cartModel, false, 350.0);
	}

	/**
	 * Test net mixed tax for different entries:
	 * <ul>
	 * <li>creates 2 taxes: 10% and 20%,</li>
	 * <li>creates 2 product tax group with 10% and 20% as well,</li>
	 * <li>add product41 (110 Euro net) with extra 10% tax ==> 110(net) + 11(tax) = 121 Euro,</li>
	 * <li>add product42 (240 Euro net) with extra 20% tax ==> 240(net) + 44(tax) = 288 Euro,</li>
	 * <li>add product43 (300 Euro net) with NO tax ==> 300(net) = 300 Euro,</li>
	 * <li>delivery cost is 10 Euro net,</li>
	 * <li>checks that the sub total (net) is 650,(110 + 240 + 300)</li>
	 * <li>checks that 10% tax has value 10.31 and 20% tax has value 49.37.</li>
	 * </ul>
	 * 
	 * @throws JaloPriceFactoryException
	 */
	@Test
	public void testNetMixedTaxEntries() throws CalculationException, JaloPriceFactoryException
	{
		final CartModel cart = prepareCart(true, "product_41", "product_42", "product_43");
		calculationService.calculate(cart);
		modelService.refresh(cart);

		assertLastEntryNotTaxed(cart);
		assertCart(cart, true, 650.0);

		final Cart cartItem = (Cart) modelService.getSource(cart);
		cartItem.recalculate();
		final CartModel cartModel = (CartModel) modelService.get(cartItem);
		modelService.refresh(cartModel);

		assertLastEntryNotTaxed(cartModel);
		assertCart(cartModel, true, 650.0);
	}

	@Test
	public void testNetNormalTaxEntries() throws CalculationException, JaloPriceFactoryException
	{
		final CartModel cart = prepareCart(true, "product_41", "product_42");
		calculationService.calculate(cart);
		modelService.refresh(cart);

		assertCart(cart, true, 350.0);

		final Cart cartItem = (Cart) modelService.getSource(cart);
		cartItem.recalculate();
		final CartModel cartModel = (CartModel) modelService.get(cartItem);
		modelService.refresh(cartModel);

		assertCart(cartModel, true, 350.0);
	}

	private void assertCart(final CartModel cart, final boolean net, final double expectedTotal)
	{
		assertEquals("sub total of cart", expectedTotal, cart.getSubtotal().doubleValue(), DELTA);

		final Collection<TaxValue> totalTaxValues = cart.getTotalTaxValues();
		assertEquals("size of totalTaxValues(gross)", 2, totalTaxValues.size());

		final Map<String, Double> netTaxValues = getTaxValues(net);
		for (final TaxValue taxValue : totalTaxValues)
		{
			final String taxCode = taxValue.getCode();
			assertTrue(netTaxValues.keySet().contains(taxCode));
			assertEquals("net tax value", netTaxValues.get(taxCode).doubleValue(), taxValue.getAppliedValue(), DELTA);
		}
	}

	private CartModel prepareCart(final boolean net, final String... products)
	{
		final CartModel cart = cartFactory.createCart();
		for (final String productCode : products)
		{
			final ProductModel product = productService.getProductForCode(catVersion, productCode);
			cartService.addNewEntry(cart, product, 1, product.getUnit());
		}
		cart.setNet(Boolean.valueOf(net));
		cart.setDeliveryAddress(user.getDefaultShipmentAddress());
		assertNotNull(cart.getDeliveryAddress());
		assertEquals("DE", cart.getDeliveryAddress().getCountry().getIsocode());
		cart.setDeliveryMode(net ? deliveryModeNet : deliveryModeGross);
		modelService.save(cart);
		return cart;
	}

	private void prepareData()
	{
		user = userService.getAnonymousUser();

		final AddressModel address = modelService.create(AddressModel.class);
		address.setCountry(commonI18NService.getCountry("DE"));
		address.setOwner(user);
		user.setDefaultShipmentAddress(address);
		modelService.saveAll(user);

		userService.setCurrentUser(user);
		final CurrencyModel curr = commonI18NService.getCurrency("EUR");
		commonI18NService.setCurrentCurrency(curr);
		deliveryModeGross = deliveryModeService.getDeliveryModeForCode("dhl_gross");
		deliveryModeNet = deliveryModeService.getDeliveryModeForCode("dhl_net");
		catVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
	}

	private Map<String, Double> getTaxValues(final boolean net)
	{
		final Map<String, Double> taxValues = new HashMap<String, Double>();
		if (net)
		{
			taxValues.put("10_percent", Double.valueOf(11.31));
			taxValues.put("20_percent", Double.valueOf(49.37));
		}
		else
		{
			taxValues.put("10_percent", Double.valueOf(10.29));
			taxValues.put("20_percent", Double.valueOf(41.14));
		}
		return taxValues;
	}

	private void assertLastEntryNotTaxed(final CartModel cart)
	{
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		assertEquals(1, entries.get(0).getTaxValues().size());
		assertEquals(1, entries.get(1).getTaxValues().size());
		assertEquals(0, entries.get(2).getTaxValues().size());
	}
}
