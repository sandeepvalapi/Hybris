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

import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CalculationServiceTest extends ServicelayerTransactionalTest
{

	private ProductModel productA, productB, productC, giveAwayProduct;
	private UnitModel unitModel1, unitModel2;
	private OrderModel orderModel, giveAwayOrder;
	private CurrencyModel curr;
	private ZoneDeliveryModeModel deliveryMode;
	private CountryModel country;

	@Resource
	private CalculationService calculationService;

	@Resource
	private ModelService modelService;

	@Resource
	private OrderService orderService;

	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private ProductService productService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	UserService userService;

	@Resource
	private OrderEntryService orderEntryService;



	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/platformservices/test/testOrderCalculation.csv", "utf-8");

		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion("testCatalog", "Online");

		productA = productService.getProductForCode("pA");
		productB = productService.getProductForCode("pB");
		productC = productService.getProductForCode("pC");

		giveAwayProduct = modelService.create(ProductModel.class);
		giveAwayProduct.setCode("Product B (Give Away, no price defined)");
		giveAwayProduct.setCatalogVersion(catalogVersionModel);

		modelService.saveAll(giveAwayProduct);

		unitModel1 = productA.getUnit();
		unitModel2 = productA.getUnit();

		final CustomerModel customerModel = modelService.create(CustomerModel.class);
		customerModel.setUid("testCustomer");
		customerModel.setGroups(Collections.singleton((PrincipalGroupModel) userService.getUserGroupForUID("customergroup")));
		customerModel.setName("test Customer");
		customerModel.setCustomerID("testCustomerID");
		customerModel.setLoginDisabled(false);

		curr = commonI18NService.getCurrency("PLN");

		orderModel = modelService.create(OrderModel.class);
		orderModel.setCode("order calc test");
		orderModel.setUser(customerModel);
		orderModel.setCurrency(curr);
		orderModel.setDate(new Date());
		orderModel.setNet(Boolean.FALSE);

		giveAwayOrder = modelService.create(OrderModel.class);
		giveAwayOrder.setCode("order calc test (including giveaways)");
		giveAwayOrder.setUser(customerModel);
		giveAwayOrder.setCurrency(curr);
		giveAwayOrder.setDate(new Date());
		giveAwayOrder.setNet(Boolean.FALSE);

		modelService.saveAll(customerModel, orderModel, giveAwayOrder);



		//deliveryMode.set
		country = commonI18NService.getCountry("DE");

		final AddressModel deliveryAddress = modelService.create(AddressModel.class);
		deliveryAddress.setCountry(country);
		deliveryAddress.setOwner(customerModel);
		modelService.save(deliveryAddress);
		orderModel.setDeliveryAddress(deliveryAddress);

		final ZoneModel zone = modelService.create(ZoneModel.class);
		zone.setCountries(new HashSet<CountryModel>(Arrays.asList(country)));
		zone.setCode("zone");

		modelService.save(zone);


		deliveryMode = modelService.create(ZoneDeliveryModeModel.class);
		deliveryMode.setNet(Boolean.TRUE);
		deliveryMode.setCode("code");
		modelService.save(deliveryMode);

		final ZoneDeliveryModeValueModel value = modelService.create(ZoneDeliveryModeValueModel.class);
		value.setZone(zone);

		value.setMinimum(Double.valueOf(0));
		value.setValue(Double.valueOf(5));
		value.setCurrency(curr);

		value.setDeliveryMode(deliveryMode);
		modelService.save(value);
	}

	@Test
	public void testRequiresCalculation() throws CalculationException
	{
		// test besic values
		assertFalse("order shouldnt be calculated yet", orderModel.getCalculated().booleanValue());
		assertEquals(curr, orderModel.getCurrency());
		assertFalse("order wasnt gross", orderModel.getNet().booleanValue());

		assertTrue(calculationService.requiresCalculation(orderModel));

		// try empty calculation
		calculationService.calculateTotals(orderModel, false);
		checkOrderEmpty(orderModel);
		assertFalse(calculationService.requiresCalculation(orderModel));
	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.order.CalculationService#calculate(de.hybris.platform.core.model.order.AbstractOrderModel)}
	 * .
	 *
	 * @throws CalculationException
	 */
	@Test
	public void testCalculateAbstractOrderModel() throws CalculationException
	{
		// test besic values
		assertFalse("order shouldnt be calculated yet", orderModel.getCalculated().booleanValue());
		assertEquals(curr, orderModel.getCurrency());
		assertFalse("order wasnt gross", orderModel.getNet().booleanValue());

		assertTrue(calculationService.requiresCalculation(orderModel));

		// try empty calculation
		calculationService.calculateTotals(orderModel, false);
		checkOrderEmpty(orderModel);
		assertFalse(calculationService.requiresCalculation(orderModel));
		// try with two entries - still without prices

		final OrderEntryModel oe1 = orderService.addNewEntry(orderModel, productA, 10, unitModel1);
		final OrderEntryModel oe2 = orderService.addNewEntry(orderModel, productB, 3, unitModel2);
		modelService.saveAll(orderModel, oe1, oe2);
		assertTrue(calculationService.requiresCalculation(orderModel));
		calculationService.calculateTotals(orderModel, false);
		assertFalse(calculationService.requiresCalculation(orderModel));
		checkOrderEmpty(orderModel);
		for (final Iterator it = orderModel.getEntries().iterator(); it.hasNext();)
		{
			checkOrderEntryEmpty((AbstractOrderEntryModel) it.next());
		}
		/*
		 * give entries prices 10 x 1.234 = 12.340 , 16% VAT FULL 3 x 3.333 = 9.999 , 7% VAT HALF, -0.999 DISC A, - 10%
		 * DISC B = 8.100
		 */
		oe1.setQuantity(Long.valueOf(10));
		oe1.setBasePrice(Double.valueOf(1.234));
		orderEntryService.addTaxValue(oe1, new TaxValue("VAT FULL", 16.0, false, curr.getIsocode()));

		oe2.setQuantity(Long.valueOf(3));
		oe2.setBasePrice(Double.valueOf(3.333));

		orderEntryService.addTaxValue(oe2, new TaxValue("VAT HALF", 7.0, false, curr.getIsocode()));
		orderEntryService.addAllDiscountValues(oe2, Arrays.asList(new DiscountValue("DISC A", 0.333, true, curr.getIsocode()),
				new DiscountValue("DISC B", 10, false, null)));
		// entry total should still be 0
		modelService.saveAll(orderModel, oe1, oe2);

		assertEquals(0.0, oe1.getTotalPrice().doubleValue(), 0.0001);
		assertEquals(0.0, oe2.getTotalPrice().doubleValue(), 0.0001);
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());
		/*
		 * now calculate and check order: 10 x 10 x 1.234 , 16% VAT FULL = 12.340 3 x 3.333 = 9.999 , 7% VAT HALF, -3 x
		 * 0.333 = -0.999 DISC A, - (10%,0.900) DISC B = 8.100 -------- subtotal = 20,440 VAT FULL 16% = 1,974 VAT HALF 7%
		 * = 0,567 taxes = 2,541 discounts = 0,000 total = 20.440
		 */
		calculationService.calculateTotals(orderModel, false);
		checkOrderEntry(oe1, 1.234, // base price
				12.340, // total price
				Arrays.asList(new TaxValue("VAT FULL", 16.0, false, 1.702, curr.getIsocode())), // tax values
				Collections.EMPTY_LIST // discount values
		);
		checkOrderEntry(oe2, 3.333, // base price
				8.100, // total price
				Arrays.asList(new TaxValue("VAT HALF", 7.0, false, 0.530, curr.getIsocode())), // tax values
				Arrays.asList(new DiscountValue("DISC A", 0.333, true, 0.999, curr.getIsocode()), new DiscountValue("DISC B", 10,
						false, 0.9, null)) // discount values
		);
		checkOrder(orderModel, 20.44, // subtotal
				0.0, // total discounts
				2.541, // total taxes
				20.44, // total
				0.0, // delivery cost
				0.0, // payment cost
				Arrays.asList(new TaxValue("VAT FULL", 16, false, 1.702, curr.getIsocode()), new TaxValue("VAT HALF", 7, false,
						0.530, curr.getIsocode())), // tax values
				Collections.EMPTY_LIST // discount values
		);
		/*
		 * test global discount subtotal = 20,440 ----------- - 10% -> -2.044 = 18,396 - 3 -> -3 = 15,396 -5% -> 0,770 =
		 * 14,626 discounts = 5,814 ----------- (internal) tax factor = 14,626 / 20,440 =
		 * 0,71555772994129158512720156555773 ----------- VAT FULL 16% = 1,413 VAT HALF 7% = 0,406 taxes = 1,819 total =
		 * 14,626
		 */
		orderService.addGlobalDiscountValue(orderModel, new DiscountValue("10%off", 10, false, null)); // -10%
		orderService.addGlobalDiscountValue(orderModel, new DiscountValue("3off", 3, true, curr.getIsocode())); // -3
		orderService.addGlobalDiscountValue(orderModel, new DiscountValue("5%off", 5, false, null)); // -3
		modelService.save(orderModel);//need to save manually
		// calculate totals again
		calculationService.calculateTotals(orderModel, false);
		checkOrder(orderModel, 20.440, // subtotal
				5.814, // discount totals
				1.819, // tax totals
				14.626, // total
				0.0, // delivery cost
				0.0, // payment cost
				Arrays.asList(new TaxValue("VAT FULL", 16, false, 1.218, curr.getIsocode()), new TaxValue("VAT HALF", 7, false,
						0.379, curr.getIsocode())), // tax values
				Arrays.asList(new DiscountValue("10%off", 10, false, 2.044, null),
						new DiscountValue("3off", 3, true, 3.0, curr.getIsocode()), new DiscountValue("5%off", 5, false, 0.770, null)) // discount
		// values
		);
		/*
		 * add payment and delivery costs subtotal = 20,440 ----------- - 10% -> -2.044 = 18,396 - 3 -> -3 = 15,396 -5% ->
		 * 0,770 = 14,626 discounts = 5,814 ----------- delivery = 4,44 payment = 2,222 ----------- (internal) tax factor
		 * = ( 14,626 + 4,44 + 2,222 ) / 20,440 = 1,0414872798434442270058708414873 ----------- VAT FULL 16% = 2,056 VAT
		 * HALF 7% = 0,591 taxes = 2,647 total = 21,288
		 */
		orderModel.setPaymentCost(Double.valueOf(4.44));

		orderModel.setDeliveryCost(Double.valueOf(2.222));
		modelService.save(orderModel);
		calculationService.calculateTotals(orderModel, false);
		checkOrder(orderModel, 20.440, // subtotal
				5.814, // discount totals
				2.647, // tax totals
				21.288, // total
				2.222, // delivery cost
				4.44, // payment cost
				Arrays.asList(new TaxValue("VAT FULL", 16, false, 1.773, curr.getIsocode()), new TaxValue("VAT HALF", 7, false,
						0.552, curr.getIsocode())), // tax values
				Arrays.asList(new DiscountValue("10%off", 10, false, 2.044, null),
						new DiscountValue("3off", 3, true, 3.0, curr.getIsocode()), new DiscountValue("5%off", 5, false, 0.770, null)) // discount
		// values
		);

		orderModel.setDeliveryMode(deliveryMode);
		orderModel.setCalculated(Boolean.FALSE);
		calculationService.calculate(orderModel);
		checkOrder(orderModel, 20.440, // subtotal
				0.0, // discount totals
				2.647, // tax totals
				26.59, // total
				6.15, // delivery cost
				0.0, // payment cost
				Arrays.asList(new TaxValue("VAT HALF", 7, false, 0.689, curr.getIsocode()), new TaxValue("VAT FULL", 16, false,
						2.214, curr.getIsocode())), // tax values
				Collections.EMPTY_LIST // discount
		// values
		);

	}


	@Test
	public void testRecalculateOrderEntry() throws CalculationException
	{
		// test besic values
		assertFalse("order shouldnt be calculated yet", orderModel.getCalculated().booleanValue());
		assertEquals(curr, orderModel.getCurrency());
		assertFalse("order wasnt gross", orderModel.getNet().booleanValue());

		assertTrue(calculationService.requiresCalculation(orderModel));

		// try empty calculation
		calculationService.calculateTotals(orderModel, false);
		checkOrderEmpty(orderModel);
		assertFalse(calculationService.requiresCalculation(orderModel));

		final OrderEntryModel oe1 = orderService.addNewEntry(orderModel, productA, 10, unitModel1);
		oe1.setQuantity(Long.valueOf(10));
		oe1.setBasePrice(Double.valueOf(1.234));
		orderEntryService.addTaxValue(oe1, new TaxValue("VAT FULL", 16.0, false, curr.getIsocode()));
		modelService.saveAll(orderModel, oe1);

		assertTrue(calculationService.requiresCalculation(orderModel));
		calculationService.calculateTotals(orderModel, false);
		assertFalse(calculationService.requiresCalculation(orderModel));

		oe1.setBasePrice(Double.valueOf(1.234));
		modelService.saveAll(oe1, orderModel);
		calculationService.recalculate(oe1);

		assertTrue("orderEntryModel should be calculated", oe1.getCalculated().booleanValue());

		assertTrue("orderModel should not be calculated", calculationService.requiresCalculation(orderModel));

		calculationService.recalculate(orderModel);
		assertFalse("orderModel should be calculated", calculationService.requiresCalculation(orderModel));

	}

	@Test
	public void testGiveAwayHandling()
	{

		// CASE 1 ( PRICE = n/a, ISGIVEAWAY = n/a, ISRECJECTED = n/a)
		final AbstractOrderEntryModel entry = orderService.addNewEntry(giveAwayOrder, giveAwayProduct, 1, unitModel1);
		orderService.saveOrder(giveAwayOrder);

		boolean welldone = false;

		try
		{
			calculationService.calculate(giveAwayOrder);
		}
		catch (final CalculationException e)
		{
			welldone = true;
		}

		if (!welldone)
		{
			fail("Invalid state of order entry (" + entry + ") [ " + "product.price: n/a, " + "entry.ISGIVEAWAY: "
					+ entry.getGiveAway() + ", " + "entry.ISREJECTED: " + entry.getRejected() + "]");
		}

		// CASE 2 (PRICE = n/a, ISGIVEAWAY = true, ISRECJECTED = false
		entry.setGiveAway(Boolean.TRUE);
		entry.setRejected(Boolean.FALSE);
		modelService.save(entry);

		try
		{
			calculationService.calculate(giveAwayOrder);
		}
		catch (final CalculationException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		//	 CASE 3 (PRICE = n/a, ISGIVEAWAY = true, ISRECJECTED = true
		entry.setGiveAway(Boolean.TRUE);
		entry.setRejected(Boolean.TRUE);
		modelService.save(entry);

		try
		{
			calculationService.recalculate(giveAwayOrder);
		}
		catch (final CalculationException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testEntryCalculation() throws Exception
	{

		// try with two entries - still without prices
		final OrderEntryModel oe1 = orderService.addNewEntry(orderModel, productB, 10, unitModel1);
		final OrderEntryModel oe2 = orderService.addNewEntry(orderModel, productC, 3, unitModel2);
		modelService.saveAll(orderModel, oe1, oe2);
		calculationService.calculateTotals(orderModel, false);

		checkOrderEmpty(orderModel);
		/*
		 * give entries prices 10 x 1.234 = 12.340 , 16% VAT FULL 3 x 3.333 = 9.999 , 7% VAT HALF, -0.999 DISC A, - 10%
		 * DISC B = 8.100
		 */
		oe1.setQuantity(Long.valueOf(10));
		//need to set price, tax - we are not going to fetch them from find - strategy in this test
		oe1.setBasePrice(Double.valueOf(1.234));
		//oe1.setTaxValues(Collections.singletonList(new TaxValue("VAT FULL", 16.0, false, curr.getIsocode())));
		orderEntryService.addTaxValue(oe1, new TaxValue("VAT FULL", 16.0, false, curr.getIsocode()));

		oe2.setQuantity(Long.valueOf(3));
		//need to set price, tax, discount - we are not going to fetch them from find - strategy in this test
		oe2.setBasePrice(Double.valueOf(3.333));
		orderEntryService.addTaxValue(oe2, new TaxValue("VAT HALF", 7.0, false, curr.getIsocode()));
		orderEntryService.addAllDiscountValues(oe2, Arrays.asList(new DiscountValue("DISC A", 0.333, true, curr.getIsocode()),
				new DiscountValue("DISC B", 10, false, null)));

		// entry total should still be 0
		modelService.saveAll(oe1, oe2);

		assertEquals(0.0, oe1.getTotalPrice().doubleValue(), 0.0001);
		assertEquals(0.0, oe2.getTotalPrice().doubleValue(), 0.0001);
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());
		assertFalse("orderEntry should not be calculated", oe1.getCalculated().booleanValue());
		assertFalse("orderEntry should not be calculated", oe2.getCalculated().booleanValue());

		calculationService.calculateTotals(oe1, true);
		checkOrderEntry(oe1, 1.234, // base price
				12.340, // total price
				Arrays.asList(new TaxValue("VAT FULL", 16.0, false, 1.702, curr.getIsocode())), // tax values
				Collections.EMPTY_LIST // discount values
		);

		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());
		assertTrue("orderEntry should be calculated", oe1.getCalculated().booleanValue());
		assertFalse("orderEntry should not be calculated", oe2.getCalculated().booleanValue());

		calculationService.calculateTotals(oe2, true);
		checkOrderEntry(oe2, 3.333, // base price
				8.100, // total price
				Arrays.asList(new TaxValue("VAT HALF", 7.0, false, 0.530, curr.getIsocode())), // tax values
				Arrays.asList(new DiscountValue("DISC A", 0.333, true, 0.999, curr.getIsocode()), new DiscountValue("DISC B", 10,
						false, 0.9, null)) // discount values
		);

		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());
		assertTrue("orderEntry should be calculated", oe1.getCalculated().booleanValue());
		assertTrue("orderEntry should be calculated", oe2.getCalculated().booleanValue());
	}

	@Test
	public void testApplyGlobalDiscounts() throws CalculationException
	{
		final AbstractOrderEntryModel oe1 = orderService.addNewEntry(orderModel, productB, 1, unitModel1);
		oe1.setBasePrice(Double.valueOf(10));
		modelService.save(oe1);
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(10, orderModel.getTotalPrice().doubleValue(), 0.001);

		final DiscountValue percentageDiscount = new DiscountValue("testDiscount 50% off", 50, false, 5.0, null);
		final DiscountValue absoluteDiscount = new DiscountValue("testDiscount -2 ", 2, true, orderModel.getCurrency().getIsocode());
		orderService.addGlobalDiscountValue(orderModel, percentageDiscount);
		modelService.save(orderModel);//need to save manually

		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());
		// Now calculate: totalprice = 10, discount 50%
		calculationService.calculateTotals(orderModel, true);

		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(5, orderModel.getTotalPrice().doubleValue(), 0.001);

		orderService.addGlobalDiscountValue(orderModel, absoluteDiscount);
		modelService.save(orderModel);//need to save manually

		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(3, orderModel.getTotalPrice().doubleValue(), 0.001);

		//now we remove discounts in reverse orders
		orderService.removeGlobalDiscountValue(orderModel, percentageDiscount);
		modelService.save(orderModel);//need to save manually
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(8, orderModel.getTotalPrice().doubleValue(), 0.001);

		orderService.removeGlobalDiscountValue(orderModel, orderService.getGlobalDiscountValue(orderModel, absoluteDiscount));
		modelService.save(orderModel);//need to save manually
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(10, orderModel.getTotalPrice().doubleValue(), 0.001);
	}

	@Test
	public void testApplyTaxes() throws CalculationException
	{
		//test NET order for taxes:
		orderModel.setNet(Boolean.TRUE);
		final OrderEntryModel oe1 = orderService.addNewEntry(orderModel, productB, 1, unitModel1);
		oe1.setBasePrice(Double.valueOf(10));
		orderService.saveOrder(orderModel);

		calculationService.calculateTotals(orderModel, true);

		final TaxValue tax_25Percent = new TaxValue("TAX + 25%", 25, false, curr.getIsocode());
		final TaxValue tax_2Absolute = new TaxValue("TAX + 2", 2, true, curr.getIsocode());

		orderEntryService.addTaxValue(oe1, tax_25Percent);
		modelService.save(oe1);// need to save manually

		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(10, orderModel.getTotalPrice().doubleValue(), 0.001);
		assertEquals(2.5, orderModel.getTotalTax().doubleValue(), 0.001);

		orderEntryService.addTaxValue(oe1, tax_2Absolute);
		modelService.save(oe1);// need to save manually
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(10, orderModel.getTotalPrice().doubleValue(), 0.001);
		assertEquals(4.5, orderModel.getTotalTax().doubleValue(), 0.001);

		//remove applied tax
		final TaxValue tax_25Percent_applied = tax_25Percent.apply(oe1.getQuantity().doubleValue(), oe1.getTotalPrice()
				.doubleValue(), orderModel.getCurrency().getDigits().intValue(), orderModel.getNet().booleanValue(), orderModel
				.getCurrency().getIsocode());

		orderEntryService.removeTaxValue(oe1, tax_25Percent_applied);
		modelService.save(oe1);// need to save manually
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(10, orderModel.getTotalPrice().doubleValue(), 0.001);
		assertEquals(2, orderModel.getTotalTax().doubleValue(), 0.001);
	}

	@Test
	public void testApplyDiscounts() throws CalculationException
	{

		final OrderEntryModel oe1 = orderService.addNewEntry(orderModel, productB, 1, unitModel1);
		oe1.setBasePrice(Double.valueOf(10));
		orderService.saveOrder(orderModel);

		calculationService.calculateTotals(orderModel, true);

		final DiscountValue _10Percent = new DiscountValue("10% off", 10, false, curr.getIsocode());
		final DiscountValue _2Absolute = new DiscountValue("-2", 2, true, curr.getIsocode());

		orderEntryService.addDiscountValue(oe1, _2Absolute);
		modelService.save(oe1);//need to save manually;
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(8, orderModel.getTotalPrice().doubleValue(), 0.001);

		orderEntryService.addDiscountValue(oe1, _10Percent);
		modelService.save(oe1);//need to save manually;
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(7.2, orderModel.getTotalPrice().doubleValue(), 0.001);

		//remove applied discounts
		orderEntryService.removeDiscountValue(oe1, orderEntryService.getGlobalDiscountValue(oe1, _2Absolute));
		modelService.save(oe1);//need to save manually;
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(9, orderModel.getTotalPrice().doubleValue(), 0.001);

		orderEntryService.removeDiscountValue(oe1, orderEntryService.getGlobalDiscountValue(oe1, _10Percent));
		modelService.save(oe1);//need to save manually;
		assertFalse("order should not be calculated", orderModel.getCalculated().booleanValue());

		calculationService.calculateTotals(orderModel, true);
		assertTrue("order should be calculated", orderModel.getCalculated().booleanValue());
		assertEquals(10, orderModel.getTotalPrice().doubleValue(), 0.001);
	}


	@Test
	public void testConvertPriceNetGross()
	{
		final TaxValue tax_25Percent = new TaxValue("TAX + 25%", 25, false, curr.getIsocode());
		final TaxValue tax_2Absolute = new TaxValue("TAX + 2", 2, true, curr.getIsocode());
		final Collection<TaxValue> taxValues = Arrays.asList(tax_25Percent, tax_2Absolute);
		final PriceValue basePriceNet = new PriceValue(curr.getIsocode(), 20.75, true);

		//convert with targetPrice=net - result should be the same as basePrice (given as net)
		final PriceValue resultNet = ((DefaultCalculationService) calculationService).convertPriceIfNecessary(basePriceNet, true,
				curr, taxValues);
		assertNotNull(resultNet);
		assertEquals(20.75, resultNet.getValue(), 0.001);
		//		final PriceValue jaloResultNet = JaloTools.convertPriceIfNecessary(basePriceNet, true,
		//				(Currency) modelService.getSource(curr), taxValues);

		//convert with targetPrice=gross - result should include taxes and has to be rounded properly
		final PriceValue resultGross = ((DefaultCalculationService) calculationService).convertPriceIfNecessary(basePriceNet,
				false, curr, taxValues);
		assertNotNull(resultGross);
		assertEquals(3, curr.getDigits().intValue()); //rounding to 3 digits expected (28,4375 -> 28,438)
		assertEquals(28.438, resultGross.getValue(), 0.001);
		//		final PriceValue jaloResultGross = JaloTools.convertPriceIfNecessary(basePriceNet, false,
		//				(Currency) modelService.getSource(curr), taxValues);
	}

	@Test
	public void testConvertPriceCurrency()
	{
		final TaxValue tax_25Percent = new TaxValue("TAX + 25%", 25, false, curr.getIsocode());
		final TaxValue tax_2Absolute = new TaxValue("TAX + 2", 2, true, curr.getIsocode());
		final Collection<TaxValue> taxValues = Arrays.asList(tax_25Percent, tax_2Absolute);
		final PriceValue basePriceNet = new PriceValue(commonI18NService.getCurrency("EUR").getIsocode(), 20.75, true);
		final CurrencyModel targetCurrency = commonI18NService.getCurrency("USD");

		//convert EUR-USD, taxes doesn't matter (no net-gross conversion)
		final PriceValue resultNet = ((DefaultCalculationService) calculationService).convertPriceIfNecessary(basePriceNet, true,
				targetCurrency, taxValues);
		assertNotNull(resultNet);
		assertEquals(2, targetCurrency.getDigits().intValue()); //rounding to 2 digits expected (28,635 -> 28,64)
		assertEquals(28.64, resultNet.getValue(), 0.001);
		//		final PriceValue jaloResultNet = JaloTools.convertPriceIfNecessary(basePriceNet, true,
		//				(Currency) modelService.getSource(targetCurrency), taxValues);
	}

	@Test
	public void shouldRecalculateEntryEvenWhenItIsAlreadyCalculated() throws CalculationException
	{
		final OrderEntryModel entry = givenCalculatedOrderEntry();
		final Double oldTotal = entry.getTotalPrice();
		final Double newTotal = Double.valueOf(oldTotal.doubleValue() + 13.0);

		entry.setTotalPrice(newTotal);
		modelService.save(entry);
		assertTrue(entry.getCalculated().booleanValue());
		assertEquals(newTotal, entry.getTotalPrice());

		calculationService.recalculate(entry);

		assertTrue(entry.getCalculated().booleanValue());
		assertEquals(oldTotal, entry.getTotalPrice());
	}

	private OrderEntryModel givenCalculatedOrderEntry() throws CalculationException
	{
		final OrderEntryModel entry = orderService.addNewEntry(orderModel, productA, 10, unitModel1);
		entry.setQuantity(Long.valueOf(10));
		entry.setBasePrice(Double.valueOf(1.234));
		orderEntryService.addTaxValue(entry, new TaxValue("VAT FULL", 16.0, false, curr.getIsocode()));
		modelService.saveAll(orderModel, entry);
		calculationService.recalculate(entry);
		assertTrue(entry.getCalculated().booleanValue());
		return entry;
	}

	private void checkOrderEmpty(final AbstractOrderModel abstractOrderModel)
	{
		checkOrder(abstractOrderModel, 0.0, // subtotal
				0.0, // total discounts
				0.0, // total taxes
				0.0, // total
				0.0, // delivery cost
				0.0, // payment cost
				Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		assertNull(abstractOrderModel.getDeliveryMode());
		assertNotNull(abstractOrderModel.getDeliveryAddress());
		assertNull(abstractOrderModel.getPaymentMode());
		assertNull(abstractOrderModel.getPaymentAddress());
	}

	private void checkOrder(final AbstractOrderModel abstractOrderModel, final double subtotal, final double totalDiscounts,
			@SuppressWarnings("unused") final double totalTaxes, final double total, final double deliveryCost,
			final double paymentCost, final Collection taxValues, final Collection discountValues)
	{
		assertTrue(abstractOrderModel.getCalculated().booleanValue());
		assertEquals(total, abstractOrderModel.getTotalPrice().doubleValue(), 0.0001);
		assertEquals(subtotal, abstractOrderModel.getSubtotal().doubleValue(), 0.0001);
		assertEquals(totalDiscounts, abstractOrderModel.getTotalDiscounts().doubleValue(), 0.0001);
		assertEquals(deliveryCost, abstractOrderModel.getDeliveryCost().doubleValue(), 0.0001);
		assertCollection(taxValues, abstractOrderModel.getTotalTaxValues());
		assertEquals(discountValues, abstractOrderModel.getGlobalDiscountValues()); // order is important here so we dont use assertCollection !!!
		assertEquals(paymentCost, abstractOrderModel.getPaymentCost().doubleValue(), 0.0001);
	}

	private void checkOrderEntryEmpty(final AbstractOrderEntryModel abstractOrderEntryModel)
	{
		checkOrderEntry(abstractOrderEntryModel, 0.0, 0.0, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}

	private void checkOrderEntry(final AbstractOrderEntryModel abstractOrderEntryModel, final double basePrice,
			final double totalPrice, final Collection taxValues, final List discountValues)
	{
		assertTrue(abstractOrderEntryModel.getCalculated().booleanValue());
		assertEquals(totalPrice, abstractOrderEntryModel.getTotalPrice().doubleValue(), 0.0001);
		assertEquals(basePrice, abstractOrderEntryModel.getBasePrice().doubleValue(), 0.0001);
		assertEquals(discountValues, abstractOrderEntryModel.getDiscountValues()); // order is important here so we dont use assertCollection !!!
		assertCollection(taxValues, abstractOrderEntryModel.getTaxValues());
	}


}
