/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.order.interceptors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 *
 */
@IntegrationTest
public class AbstractOrderEntryPreparerIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;

	@Resource
	TypeService typeService;

	@Resource
	ConfigurationService configurationService;

	@Resource
	UserService userService;

	@Resource
	CalculationService calculationService;

	DefaultAbstractOrderEntryPreparer preparer;

	CurrencyModel curr;
	UserModel user;
	ProductModel prod;
	UnitModel unit;

	@Before
	public void setUp() throws JaloSystemException
	{
		preparer = new DefaultAbstractOrderEntryPreparer();
		preparer.setTypeService(typeService);
		preparer.setConfigurationService(configurationService);

		unit = modelService.create(UnitModel.class);
		unit.setCode("unit");
		unit.setConversion(Double.valueOf(1.0));
		unit.setUnitType("type");
		modelService.save(unit);

		curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("XYZ");
		curr.setActive(Boolean.TRUE);
		curr.setConversion(Double.valueOf(1.0));
		curr.setDigits(Integer.valueOf(2));
		curr.setSymbol("CCC");
		modelService.save(curr);

		final CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId("catalog");
		final CatalogVersionModel cv = modelService.create(CatalogVersionModel.class);
		cv.setCatalog(cat);
		cv.setVersion("online");
		cv.setActive(Boolean.TRUE);
		modelService.saveAll(cat, cv);

		prod = modelService.create(ProductModel.class);
		prod.setCode("product");
		prod.setUnit(unit);
		prod.setCatalogVersion(cv);
		prod.setApprovalStatus(ArticleApprovalStatus.APPROVED);

		user = userService.getAnonymousUser();
	}

	@Test
	public void testNewOrderAddNewEntry() throws CalculationException
	{
		// given
		final OrderModel order = createOrder("neworder1");
		final OrderEntryModel entry = createEntry(order, 0, 10, "9.99");

		// when
		modelService.save(entry);

		// then
		assertEquals(Boolean.FALSE, order.getCalculated());
		assertEquals(Boolean.FALSE, entry.getCalculated());
		assertTrue(modelService.isUpToDate(order));
		assertTrue(modelService.isUpToDate(entry));

		// and when
		calculationService.calculateTotals(order, false);

		//then
		assertEquals(Boolean.TRUE, order.getCalculated());
		assertEquals(Boolean.TRUE, entry.getCalculated());
		assertEquals(Double.valueOf("99.90"), order.getTotalPrice());
		assertEquals(Double.valueOf("99.90"), entry.getTotalPrice());
		assertTrue(modelService.isUpToDate(order));
		assertTrue(modelService.isUpToDate(entry));
	}

	@Test
	public void testAddNewEntry() throws CalculationException
	{
		// given
		final OrderModel order = createOrder("neworder2");
		order.setCalculated(Boolean.TRUE);
		modelService.save(order);
		assertEquals(Boolean.TRUE, order.getCalculated());
		assertTrue(modelService.isUpToDate(order));

		final OrderEntryModel entry = createEntry(order, 0, 10, "9.99");
		assertTrue(modelService.isNew(entry));

		// when
		modelService.save(entry);
		assertEquals(Boolean.FALSE, order.getCalculated());
		assertEquals(Boolean.FALSE, entry.getCalculated());
		assertTrue(modelService.isUpToDate(order));
		assertTrue(modelService.isUpToDate(entry));

		// and when
		calculationService.calculateTotals(order, false);

		//then
		assertEquals(Boolean.TRUE, order.getCalculated());
		assertEquals(Boolean.TRUE, entry.getCalculated());
		assertEquals(Double.valueOf("99.90"), order.getTotalPrice());
		assertEquals(Double.valueOf("99.90"), entry.getTotalPrice());
		assertTrue(modelService.isUpToDate(order));
		assertTrue(modelService.isUpToDate(entry));
	}

	@Test
	public void testChangeEntry() throws CalculationException
	{
		// given
		final OrderModel order = createOrder("neworder2");
		order.setCalculated(Boolean.TRUE);
		modelService.save(order);
		assertEquals(Boolean.TRUE, order.getCalculated());
		assertTrue(modelService.isUpToDate(order));

		final OrderEntryModel entry = createEntry(order, 0, 1, "9.99");
		modelService.save(entry);
		assertTrue(modelService.isUpToDate(entry));

		// when
		calculationService.calculateTotals(order, false);

		//then
		assertEquals(Boolean.TRUE, order.getCalculated());
		assertEquals(Boolean.TRUE, entry.getCalculated());
		assertEquals(Double.valueOf("9.99"), order.getTotalPrice());
		assertEquals(Double.valueOf("9.99"), entry.getTotalPrice());
		assertTrue(modelService.isUpToDate(order));
		assertTrue(modelService.isUpToDate(entry));

		// and when
		entry.setQuantity(Long.valueOf(10));
		modelService.save(entry);

		//then
		assertEquals(Boolean.FALSE, order.getCalculated());
		assertEquals(Boolean.FALSE, entry.getCalculated());
		assertTrue(modelService.isUpToDate(order));
		assertTrue(modelService.isUpToDate(entry));

		// and when
		calculationService.calculateTotals(order, false);

		//then
		assertEquals(Boolean.TRUE, order.getCalculated());
		assertEquals(Boolean.TRUE, entry.getCalculated());
		assertEquals(Double.valueOf("99.90"), order.getTotalPrice());
		assertEquals(Double.valueOf("99.90"), entry.getTotalPrice());
		assertTrue(modelService.isUpToDate(order));
		assertTrue(modelService.isUpToDate(entry));

	}

	OrderEntryModel createEntry(final OrderModel order, final int number, final int quantity, final String basePrice)
	{
		final OrderEntryModel entry = modelService.create(OrderEntryModel.class);
		entry.setOrder(order);
		entry.setEntryNumber(Integer.valueOf(number));
		entry.setProduct(prod);
		entry.setUnit(unit);
		entry.setQuantity(Long.valueOf(quantity));
		entry.setBasePrice(Double.valueOf(basePrice));
		return entry;
	}

	OrderModel createOrder(final String code)
	{
		final OrderModel order = modelService.create(OrderModel.class);
		order.setCode(code);
		order.setDate(new Date());
		order.setCurrency(curr);
		order.setNet(Boolean.TRUE);
		order.setUser(user);
		return order;
	}
}
