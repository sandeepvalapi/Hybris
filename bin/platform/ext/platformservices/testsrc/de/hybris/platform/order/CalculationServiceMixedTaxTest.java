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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.TaxValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CalculationServiceMixedTaxTest extends ServicelayerTransactionalTest
{

	private ProductModel productA, productB;
	private UnitModel unit1;
	private OrderModel order;
	private CurrencyModel curr;

	@Resource
	private CalculationService calculationService;

	@Resource
	private ModelService modelService;

	@Resource
	private OrderService orderService;

	@Resource
	private ProductService productService;

	@Resource
	private UserService userService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();

		curr = modelService.create(CurrencyModel.class);
		curr.setDigits(Integer.valueOf(2));
		curr.setActive(Boolean.TRUE);
		curr.setIsocode("PLN");
		curr.setConversion(Double.valueOf(1d));
		curr.setSymbol("PLN");
		modelService.save(curr);

		productA = productService.getProductForCode("testProduct0");
		productB = productService.getProductForCode("testProduct1");

		unit1 = modelService.create(UnitModel.class);
		unit1.setConversion(Double.valueOf(1d));
		unit1.setCode("testUnit");
		unit1.setUnitType("package");
		modelService.save(unit1);

		final CustomerModel user = modelService.create(CustomerModel.class);
		user.setUid("testCustomer");
		user.setGroups(Collections.singleton((PrincipalGroupModel) userService.getUserGroupForUID("customergroup")));
		user.setName("test Customer");
		user.setCustomerID("testCustomerID");
		user.setLoginDisabled(false);

		order = modelService.create(OrderModel.class);
		order.setCode("order calc test");
		order.setUser(user);
		order.setCurrency(curr);
		order.setDate(new Date());
		order.setNet(Boolean.FALSE);

		modelService.saveAll(user, order);
	}


	@Test
	public void testMixedTaxedEntriesGross() throws CalculationException
	{
		addEntry(order, productA, unit1, 1, 20.0, new TaxValue("VAT_FULL", 19, false, null), new TaxValue("CUSTOM", 2, false, null));
		addEntry(order, productB, unit1, 1, 30.0, new TaxValue("VAT_FULL", 19, false, null));

		calculationService.calculateTotals(order, true);

		final Collection<TaxValue> totalTaxValues = order.getTotalTaxValues();
		assertEquals(2, totalTaxValues.size());

		final TaxValue full = getTaxValue(totalTaxValues, "VAT_FULL");
		assertNotNull(full);
		final double expected_full = CoreAlgorithms.round((20.0 * 19.0 / 121.0) + (30.0 * 19.0 / 119.0), 2);
		assertEquals(expected_full, full.getAppliedValue(), 0.0000001);

		final TaxValue custom = getTaxValue(totalTaxValues, "CUSTOM");
		assertNotNull(custom);
		final double expected_custom = CoreAlgorithms.round(20.0 * 2.0 / 121.0, 2);
		assertEquals(expected_custom, custom.getAppliedValue(), 0.0000001);

		assertEquals(expected_custom + expected_full, order.getTotalTax().doubleValue(), 0.000001);
	}

	private TaxValue getTaxValue(final Collection<TaxValue> totalTaxValues, final String code)
	{
		for (final TaxValue tv : totalTaxValues)
		{
			if (code.equalsIgnoreCase(tv.getCode()))
			{
				return tv;
			}
		}
		return null;
	}

	private OrderEntryModel addEntry(final OrderModel orderModel, final ProductModel productModel, final UnitModel unitModel, final long quantity,
			final double basePrice, final TaxValue... taxValues)
	{
		final OrderEntryModel oe1 = orderService.addNewEntry(orderModel, productModel, quantity, unitModel);
		oe1.setBasePrice(Double.valueOf(basePrice));
		orderService.saveOrder(orderModel);
		if (taxValues != null)
		{
			oe1.setTaxValues(Arrays.asList(taxValues));
			modelService.save(oe1);
		}

		return oe1;
	}

}
