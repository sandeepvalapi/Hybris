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
package de.hybris.platform.europe1.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory.CachedTaxValue;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TaxValuesCountTest extends ServicelayerTest
{
	private boolean wasCachingEnabled;

	@Resource
	private ConfigurationService configurationService;

	@Resource
	private ModelService modelService;

	private CatalogModel testCatalog;
	private CatalogVersionModel testCatalogVersionOnline;
	private ProductModel testProduct;
	private TaxRowModel taxRow1;
	private TaxRowModel taxRow2;
	private TaxRowModel taxRow3;
	private TaxModel tax1;
	private OrderEntryModel orderEntry1;
	private OrderModel order1;

	private UserModel testUser;

	private CurrencyModel currency;

	private UnitModel unit;

	private CatalogVersionModel testCatalogVersionStaged;

	@Before
	public void setUp()
	{
		wasCachingEnabled = configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES, false);
		testCatalog = modelService.create(CatalogModel.class);
		testCatalog.setId("testCatalog");
		modelService.save(testCatalog);
		testCatalogVersionOnline = modelService.create(CatalogVersionModel.class);
		testCatalogVersionOnline.setCatalog(testCatalog);
		testCatalogVersionOnline.setVersion("online");
		modelService.save(testCatalogVersionOnline);
		testCatalogVersionStaged = modelService.create(CatalogVersionModel.class);
		testCatalogVersionStaged.setCatalog(testCatalog);
		testCatalogVersionStaged.setVersion("staged");
		modelService.save(testCatalogVersionStaged);
		testProduct = modelService.create(ProductModel.class);
		testProduct.setCode("testProduct");
		testProduct.setCatalogVersion(testCatalogVersionOnline);
		modelService.save(testProduct);
		tax1 = modelService.create(TaxModel.class);
		tax1.setCode("testTax1");
		tax1.setValue(Double.valueOf(20));
		modelService.save(tax1);
		taxRow1 = modelService.create(TaxRowModel.class);
		taxRow1.setTax(tax1);
		taxRow1.setCatalogVersion(testCatalogVersionOnline);
		modelService.save(taxRow1);
		taxRow2 = modelService.create(TaxRowModel.class);
		taxRow2.setTax(tax1);
		taxRow2.setCatalogVersion(testCatalogVersionStaged);
		modelService.save(taxRow2);
		taxRow3 = modelService.create(TaxRowModel.class);
		taxRow3.setTax(tax1);
		taxRow3.setCatalogVersion(null);
		modelService.save(taxRow3);
		testUser = modelService.create(UserModel.class);
		testUser.setUid("testUser");
		modelService.save(testUser);
		currency = modelService.create(CurrencyModel.class);
		currency.setActive(Boolean.TRUE);
		currency.setSymbol("foo");
		currency.setIsocode("foo");
		modelService.save(currency);
		order1 = modelService.create(OrderModel.class);
		order1.setCurrency(currency);
		order1.setUser(testUser);
		order1.setDate(new Date());
		modelService.save(order1);
		unit = modelService.create(UnitModel.class);
		unit.setCode("foo");
		unit.setUnitType("bar");
		modelService.save(unit);
		orderEntry1 = modelService.create(OrderEntryModel.class);
		orderEntry1.setProduct(testProduct);
		orderEntry1.setOrder(order1);
		orderEntry1.setQuantity(Long.valueOf(1));
		orderEntry1.setUnit(unit);
		modelService.save(orderEntry1);
	}

	@After
	public void tearDown()
	{
		configurationService.getConfiguration().setProperty(Europe1Constants.KEY_CACHE_TAXES, String.valueOf(wasCachingEnabled));
		modelService.remove(testProduct);
		modelService.remove(testCatalogVersionOnline);
		modelService.remove(testCatalogVersionStaged);
		modelService.remove(testCatalog);
		modelService.remove(taxRow1);
		modelService.remove(taxRow2);
		modelService.remove(taxRow3);
		modelService.remove(tax1);
		modelService.remove(orderEntry1);
		modelService.remove(order1);
		modelService.remove(testUser);
		modelService.remove(currency);
		modelService.remove(unit);
	}

	@Test
	public void taxRowsMatchWhenCacheEnabledTest() throws Exception
	{
		final Europe1PriceFactory europe1PriceFactory = Europe1PriceFactory.getInstance();

		final AbstractOrderEntry entry = modelService.getSource(orderEntry1);
		configurationService.getConfiguration().setProperty(Europe1Constants.KEY_CACHE_TAXES, Boolean.TRUE.toString());
		assertTrue("Tax caching must be enabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));
		europe1PriceFactory.invalidateTaxCache();
		final int taxValuesCountWhenCacheIsEnabled = europe1PriceFactory.getTaxValues(entry).size();
		assertEquals("invalid number of tax values when cache is enabled: ", 2, taxValuesCountWhenCacheIsEnabled);
	}


	@Test
	public void taxRowsChangeInCacheIfTaxValueChanged() throws Exception
	{
		final Europe1PriceFactory europe1PriceFactory = Europe1PriceFactory.getInstance();

		final AbstractOrderEntry entry = modelService.getSource(orderEntry1);
		configurationService.getConfiguration().setProperty(Europe1Constants.KEY_CACHE_TAXES, Boolean.TRUE.toString());
		assertTrue("Tax caching must be enabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));
		europe1PriceFactory.invalidateTaxCache();
		tax1.setValue(Double.valueOf(40d));
		modelService.save(tax1);
		final Collection taxValues = europe1PriceFactory.getTaxValues(entry);
		assertEquals("invalid number of tax values when cache is enabled: ", 2, taxValues.size());
		final Object firstTaxValue = taxValues.iterator().next();
		assertTrue("tax value should be cached ", firstTaxValue instanceof CachedTaxValue);
		assertTrue(((CachedTaxValue) firstTaxValue).getValue() == 40d);

	}

	@Test
	public void taxRowsMatchWhenCacheDisabledTest() throws Exception
	{
		final Europe1PriceFactory europe1PriceFactory = Europe1PriceFactory.getInstance();

		final AbstractOrderEntry entry = modelService.getSource(orderEntry1);
		configurationService.getConfiguration().setProperty(Europe1Constants.KEY_CACHE_TAXES, Boolean.FALSE.toString());
		assertFalse("Tax caching must be disabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));
		final int taxValuesCountWhenCacheIsDisabled = europe1PriceFactory.getTaxValues(entry).size();
		assertEquals("invalid number of tax values when cache is enabled: ", 2, taxValuesCountWhenCacheIsDisabled);
	}

	@Test
	public void taxRowsMatchCompareTest() throws Exception
	{
		final Europe1PriceFactory europe1PriceFactory = Europe1PriceFactory.getInstance();

		final AbstractOrderEntry entry = modelService.getSource(orderEntry1);
		configurationService.getConfiguration().setProperty(Europe1Constants.KEY_CACHE_TAXES, Boolean.TRUE.toString());
		assertTrue("Tax caching must be enabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));
		final int taxValuesCountWhenCacheIsEnabled = europe1PriceFactory.getTaxValues(entry).size();

		configurationService.getConfiguration().setProperty(Europe1Constants.KEY_CACHE_TAXES, Boolean.FALSE.toString());
		assertFalse("Tax caching must be disabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));
		europe1PriceFactory.invalidateTaxCache();
		final int taxValuesCountWhenCacheIsDisabled = europe1PriceFactory.getTaxValues(entry).size();
		assertEquals("tax values count when cache is disabled can't be different than tax values count when cache is enabled",
				taxValuesCountWhenCacheIsEnabled, taxValuesCountWhenCacheIsDisabled);
	}
}
