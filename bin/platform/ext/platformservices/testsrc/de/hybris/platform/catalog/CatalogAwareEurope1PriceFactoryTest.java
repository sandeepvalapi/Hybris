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
package de.hybris.platform.catalog;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindTaxValuesStrategy;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.TaxValue;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CatalogAwareEurope1PriceFactoryTest extends ServicelayerTest
{

	private PriceFactory previousPriceFactory;
	private boolean wasCachingEnabled;

	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private ModelService modelService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private CartService cartService;
	@Resource(name = "currentFactoryFindPricingStrategy")
	private FindTaxValuesStrategy findTaxValuesStrategy;
	@Resource
	private UserService userService;

	private CatalogVersionModel onlineVersion;
	private Collection<CatalogVersionModel> previousCatalogVersions;

	private ProductModel testProduct;

	private TaxModel testTax;
	private TaxRowModel testTaxRow;

	private UnitModel testUnit;

	private UserModel testUser;

	private UserGroupModel testUserGroup;
	private ProductTaxGroup testProductTaxGroup;

	@Before
	public void setUp() throws Exception
	{

		testProductTaxGroup = ProductTaxGroup.valueOf("testTaxtGroup");
		modelService.save(testProductTaxGroup);

		previousPriceFactory = jaloSession.getPriceFactory();
		wasCachingEnabled = configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES, false);
		if (!wasCachingEnabled)
		{
			configurationService.getConfiguration().setProperty(Europe1Constants.KEY_CACHE_TAXES, Boolean.TRUE.toString());
		}
		if (!(previousPriceFactory instanceof Europe1PriceFactory))
		{
			jaloSession.setPriceFactory(Europe1PriceFactory.getInstance());
		}
		previousCatalogVersions = catalogVersionService.getSessionCatalogVersions();

		testUser = modelService.create(UserModel.class);
		testUser.setUid("testUser");

		testUserGroup = modelService.create(UserGroupModel.class);
		testUserGroup.setUid("testUserGroup");
		testUserGroup.setName("test group");
		testUser.setGroups(Collections.<PrincipalGroupModel> singleton(testUserGroup));

		testUnit = modelService.create(UnitModel.class);
		testUnit.setCode("myUnit");
		testUnit.setName("myUnit");
		testUnit.setUnitType("test");

		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("testCatalog");

		// Create test online catalog version
		onlineVersion = modelService.create(CatalogVersionModel.class);
		onlineVersion.setCatalog(catalog);
		onlineVersion.setVersion("Online");
		// ..with some test products

		testProduct = modelService.create(ProductModel.class);
		testProduct.setCatalogVersion(onlineVersion);
		testProduct.setCode("testProduct");
		testProduct.setUnit(testUnit);
		testProduct.setEurope1PriceFactory_PTG(testProductTaxGroup);

		testTax = modelService.create(TaxModel.class);
		testTax.setCode("testTax");
		testTax.setName("testTax");
		testTax.setValue(Double.valueOf(3d));

		testTaxRow = modelService.create(TaxRowModel.class);
		testTaxRow.setCatalogVersion(onlineVersion);
		testTaxRow.setPg(testProductTaxGroup);
		testTaxRow.setTax(testTax);
		testTaxRow.setValue(Double.valueOf(31d));

		modelService.saveAll();

		userService.setCurrentUser(testUser);

	}


	@After
	public void tearDown()
	{
		jaloSession.setPriceFactory(previousPriceFactory);
		if (!wasCachingEnabled)
		{
			configurationService.getConfiguration().setProperty(Europe1Constants.KEY_CACHE_TAXES, Boolean.FALSE.toString());
		}
		if (previousCatalogVersions != null)
		{
			catalogVersionService.setSessionCatalogVersions(previousCatalogVersions);
		}
	}

	@Test
	public void testTaxValue() throws CalculationException
	{
		assertTrue("Tax caching must be enabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));


		final CartModel sessionCart = cartService.getSessionCart();

		final AbstractOrderEntryModel testCartEntry = cartService.addNewEntry(sessionCart, testProduct, 1, null);

		modelService.saveAll(testCartEntry);

		final Collection<TaxValue> testProductTaxes = findTaxValuesStrategy.findTaxValues(testCartEntry);
		Assertions.assertThat(testProductTaxes).isNotEmpty().hasSize(1);

		boolean foundFromOnline = false;

		for (final TaxValue taxValue : testProductTaxes)
		{
			if (testTaxRow.getValue().doubleValue() == taxValue.getValue())
			{
				foundFromOnline = true;
			}
		}

		Assert.assertTrue("tax value from online catalog version should be retrieved from cache", foundFromOnline);
	}
}
