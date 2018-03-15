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
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindTaxValuesStrategy;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
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


/**
 * Please see PLA-10978 for motivation.
 */
@IntegrationTest
public class TaxRowCachingForMultipleCatalogVersionsTest extends ServicelayerBaseTest
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
	@Resource
	private TypeService typeService;
	@Resource
	private SearchRestrictionService searchRestrictionService;

	private CatalogVersionModel onlineVersion;
	private CatalogVersionModel stagedVersion;
	private Collection<CatalogVersionModel> previousCatalogVersions;

	private ProductModel product1Online;
	private ProductModel product1Staged;
	private ProductModel product2Online;
	private ProductModel product2Staged;

	private ProductModel product3Online;
	private ProductModel product3Staged;
	private ProductModel product4Staged;

	private TaxModel tax1;
	private TaxModel tax2;
	private TaxModel tax3;
	private TaxModel tax4;

	private TaxRowModel taxRowP1Online;
	private TaxRowModel taxRowP1Staged;
	private TaxRowModel taxRowP2Online;
	private TaxRowModel taxRowP2Staged;
	private TaxRowModel taxRowForGroupOnline;
	private TaxRowModel taxRowForGroupStaged;
	private TaxRowModel taxRowForTaxedUser;
	private TaxRowModel taxRowForUserTaxGroup;

	private UnitModel unit;

	private UserModel testUser;
	private UserModel testTaxedUser;

	private UserGroupModel testUserGroup;
	private ProductTaxGroup testProductTaxGroup;
	private UserTaxGroup testUserTaxGroup;







	@Before
	public void setUp() throws Exception
	{
		testProductTaxGroup = ProductTaxGroup.valueOf("testTaxtGroup");
		testUserTaxGroup = UserTaxGroup.valueOf("testUserTaxGroup");

		modelService.saveAll(testProductTaxGroup, testUserTaxGroup);

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

		testTaxedUser = modelService.create(UserModel.class);
		testTaxedUser.setUid("testTaxedUser");

		testUserGroup = modelService.create(UserGroupModel.class);
		testUserGroup.setUid("testUserGroup");
		testUserGroup.setName("test group");
		testUser.setGroups(Collections.<PrincipalGroupModel> singleton(testUserGroup));
		testTaxedUser.setGroups(Collections.<PrincipalGroupModel> singleton(testUserGroup));

		unit = modelService.create(UnitModel.class);
		unit.setCode("myUnit");
		unit.setName("myUnit");
		unit.setUnitType("test");

		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("testCatalog");

		// Create test online catalog version
		onlineVersion = modelService.create(CatalogVersionModel.class);
		onlineVersion.setCatalog(catalog);
		onlineVersion.setVersion("Online");
		// ..with some test products
		product1Online = modelService.create(ProductModel.class);
		product1Online.setCatalogVersion(onlineVersion);
		product1Online.setCode("P1Online");
		product1Online.setUnit(unit);

		product2Online = modelService.create(ProductModel.class);
		product2Online.setCatalogVersion(onlineVersion);
		product2Online.setCode("P2Online");
		product2Online.setUnit(unit);

		// product 3 will be in product tax group
		product3Online = modelService.create(ProductModel.class);
		product3Online.setCatalogVersion(onlineVersion);
		product3Online.setCode("P3Online");
		product3Online.setUnit(unit);
		product3Online.setEurope1PriceFactory_PTG(testProductTaxGroup);

		// Create staged test catalog version
		stagedVersion = modelService.create(CatalogVersionModel.class);
		stagedVersion.setCatalog(catalog);
		stagedVersion.setVersion("Staged");
		// ..with some test products
		product1Staged = modelService.create(ProductModel.class);
		product1Staged.setCatalogVersion(stagedVersion);
		product1Staged.setCode("P1Staged");
		product1Staged.setUnit(unit);

		product2Staged = modelService.create(ProductModel.class);
		product2Staged.setCatalogVersion(stagedVersion);
		product2Staged.setCode("P2Staged");
		product2Staged.setUnit(unit);
		// product 3 will be in product tax group
		product3Staged = modelService.create(ProductModel.class);
		product3Staged.setCatalogVersion(stagedVersion);
		product3Staged.setCode("P3Staged");
		product3Staged.setUnit(unit);
		product3Staged.setEurope1PriceFactory_PTG(testProductTaxGroup);

		// product 4 - no tax row relation, no tax group
		product4Staged = modelService.create(ProductModel.class);
		product4Staged.setCatalogVersion(stagedVersion);
		product4Staged.setCode("P4Staged");
		product4Staged.setUnit(unit);

		// setting tax rows for online products
		tax1 = modelService.create(TaxModel.class);
		tax1.setCode("testTax1");
		tax1.setName("testTax1");

		taxRowP1Online = modelService.create(TaxRowModel.class);
		taxRowP1Online.setCatalogVersion(onlineVersion);
		taxRowP1Online.setProduct(product1Online);
		taxRowP1Online.setTax(tax1);
		taxRowP1Online.setValue(Double.valueOf(11d));

		taxRowP2Online = modelService.create(TaxRowModel.class);
		taxRowP2Online.setCatalogVersion(onlineVersion);
		taxRowP2Online.setProduct(product2Online);
		taxRowP2Online.setTax(tax1);
		taxRowP2Online.setValue(Double.valueOf(12d));

		// setting tax rows for staged products
		tax2 = modelService.create(TaxModel.class);
		tax2.setCode("testTax2");
		tax2.setName("testTax2");
		tax2.setValue(Double.valueOf(2d));
		taxRowP1Staged = modelService.create(TaxRowModel.class);
		taxRowP1Staged.setCatalogVersion(stagedVersion);
		taxRowP1Staged.setProduct(product1Staged);
		taxRowP1Staged.setTax(tax2);
		taxRowP1Staged.setValue(Double.valueOf(21d));

		taxRowP2Staged = modelService.create(TaxRowModel.class);
		taxRowP2Staged.setCatalogVersion(stagedVersion);
		taxRowP2Staged.setProduct(product2Staged);
		taxRowP2Staged.setTax(tax2);
		taxRowP2Staged.setValue(Double.valueOf(22d));

		// setting tax rows for product tax groups
		tax3 = modelService.create(TaxModel.class);
		tax3.setCode("testTax3");
		tax3.setName("testTax3");
		tax3.setValue(Double.valueOf(3d));


		taxRowForGroupOnline = modelService.create(TaxRowModel.class);
		taxRowForGroupOnline.setCatalogVersion(onlineVersion);
		taxRowForGroupOnline.setPg(testProductTaxGroup);
		taxRowForGroupOnline.setTax(tax3);
		taxRowForGroupOnline.setValue(Double.valueOf(31d));

		taxRowForGroupStaged = modelService.create(TaxRowModel.class);
		taxRowForGroupStaged.setCatalogVersion(stagedVersion);
		taxRowForGroupStaged.setPg(testProductTaxGroup);
		taxRowForGroupStaged.setTax(tax3);
		taxRowForGroupStaged.setValue(Double.valueOf(32d));

		// setting tax rows for user tax group
		tax4 = modelService.create(TaxModel.class);
		tax4.setCode("testTax4");
		tax4.setName("testTax4");
		tax4.setValue(Double.valueOf(4d));

		taxRowForUserTaxGroup = modelService.create(TaxRowModel.class);
		// NO CATALOG VERSION
		taxRowForUserTaxGroup.setUg(testUserTaxGroup);
		taxRowForUserTaxGroup.setTax(tax4);
		taxRowForUserTaxGroup.setValue(Double.valueOf(41d));

		taxRowForTaxedUser = modelService.create(TaxRowModel.class);
		// NO CATALOG VERSION
		taxRowForTaxedUser.setUser(testTaxedUser);
		taxRowForTaxedUser.setTax(tax4);
		taxRowForTaxedUser.setValue(Double.valueOf(42d));

		testTaxedUser.setEurope1PriceFactory_UTG(testUserTaxGroup);

		// create search restriction for the test scenario
		final ComposedTypeModel taxRowType = typeService.getComposedTypeForClass(TaxRowModel.class);
		final SearchRestrictionModel searchRestriction = (SearchRestrictionModel) modelService.create(SearchRestrictionModel.class);
		searchRestriction.setCode("restrict tax rows to those from catalog version");
		searchRestriction.setActive(Boolean.TRUE);
		searchRestriction.setQuery(" {item:catalogVersion} IS NULL OR {item:catalogVersion} IN (?session.catalogversions)");
		searchRestriction.setRestrictedType(taxRowType);
		searchRestriction.setPrincipal(testUserGroup);
		searchRestriction.setGenerate(Boolean.TRUE);

		modelService.saveAll();

		Assertions.assertThat(
				searchRestrictionService.getSearchRestrictions(testUserGroup, true, Collections.singleton(taxRowType))).contains(
				searchRestriction);
		userService.setCurrentUser(testUser);

	}

	@Test
	public void testTaxRowCacheByProductMatch() throws CalculationException
	{

		assertTrue("Tax caching must be enabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));

		// setting online catalog version as session catalog version
		catalogVersionService.setSessionCatalogVersions(Collections.singletonList(onlineVersion));

		CartModel sessionCart = cartService.getSessionCart();
		final AbstractOrderEntryModel cartEntry1Cat1 = cartService.addNewEntry(sessionCart, product1Online, 1, null);
		final AbstractOrderEntryModel cartEntry2Cat1 = cartService.addNewEntry(sessionCart, product2Online, 1, null);
		modelService.saveAll(cartEntry1Cat1, cartEntry2Cat1);

		final Collection<TaxValue> taxesOfProduct1Catalog1 = findTaxValuesStrategy.findTaxValues(cartEntry1Cat1);
		Assertions.assertThat(taxesOfProduct1Catalog1).isNotEmpty();
		Assertions.assertThat(taxesOfProduct1Catalog1).hasSize(1);
		final TaxValue taxValueP1C1 = taxesOfProduct1Catalog1.iterator().next();
		Assert.assertEquals("tax value for product 1 from catalog 1 was not as expected", taxRowP1Online.getValue().doubleValue(),
				taxValueP1C1.getValue(), 0.0001);

		final Collection<TaxValue> taxesOfProduct2Catalog1 = findTaxValuesStrategy.findTaxValues(cartEntry2Cat1);
		Assertions.assertThat(taxesOfProduct2Catalog1).isNotEmpty();
		Assertions.assertThat(taxesOfProduct2Catalog1).hasSize(1);
		final TaxValue taxValueP2C1 = taxesOfProduct2Catalog1.iterator().next();
		Assert.assertEquals("tax value for product 2 from catalog 1 was not as expected", taxRowP2Online.getValue().doubleValue(),
				taxValueP2C1.getValue(), 0.0001);


		// setting staged catalog version as session catalog version
		catalogVersionService.setSessionCatalogVersions(Collections.singletonList(stagedVersion));

		sessionCart = cartService.getSessionCart();
		final AbstractOrderEntryModel cartEntry1Cat2 = cartService.addNewEntry(sessionCart, product1Staged, 1, null);
		final AbstractOrderEntryModel cartEntry2Cat2 = cartService.addNewEntry(sessionCart, product2Staged, 1, null);
		modelService.saveAll(cartEntry1Cat2, cartEntry2Cat2);

		final Collection<TaxValue> taxesOfProduct1Catalog2 = findTaxValuesStrategy.findTaxValues(cartEntry1Cat2);
		Assertions.assertThat(taxesOfProduct1Catalog2).isNotEmpty();
		Assertions.assertThat(taxesOfProduct1Catalog2).hasSize(1);
		final TaxValue taxValueP1C2 = taxesOfProduct1Catalog2.iterator().next();
		Assert.assertEquals("tax value for product 1 from catalog 2 was not as expected", taxRowP1Staged.getValue().doubleValue(),
				taxValueP1C2.getValue(), 0.0001);

		final Collection<TaxValue> taxesOfProduct2Catalog2 = findTaxValuesStrategy.findTaxValues(cartEntry2Cat2);
		Assertions.assertThat(taxesOfProduct2Catalog2).isNotEmpty();
		Assertions.assertThat(taxesOfProduct2Catalog2).hasSize(1);
		final TaxValue taxValueP2C2 = taxesOfProduct2Catalog2.iterator().next();
		Assert.assertEquals("tax value for product 2 from catalog 2 was not as expected", taxRowP2Staged.getValue().doubleValue(),
				taxValueP2C2.getValue(), 0.0001);
	}

	@Test
	public void testTaxRowCacheByProductTaxGroupMatch() throws CalculationException
	{
		assertTrue("Tax caching must be enabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));

		// setting online catalog version as session catalog version
		catalogVersionService.setSessionCatalogVersions(Collections.singletonList(onlineVersion));

		CartModel sessionCart = cartService.getSessionCart();
		final AbstractOrderEntryModel cartEntry3Online = cartService.addNewEntry(sessionCart, product3Online, 1, null);
		modelService.saveAll(cartEntry3Online);

		final Collection<TaxValue> taxesOfProduct3Online = findTaxValuesStrategy.findTaxValues(cartEntry3Online);
		Assertions.assertThat(taxesOfProduct3Online).isNotEmpty().hasSize(1);
		final TaxValue taxValueP3Online = taxesOfProduct3Online.iterator().next();
		Assert.assertEquals("tax value for product 3 from catalog online was not as expected", taxRowForGroupOnline.getValue()
				.doubleValue(), taxValueP3Online.getValue(), 0.0001);

		// setting staged catalog version as session catalog version
		catalogVersionService.setSessionCatalogVersions(Collections.singletonList(stagedVersion));

		sessionCart = cartService.getSessionCart();
		final AbstractOrderEntryModel cartEntry3Staged = cartService.addNewEntry(sessionCart, product3Staged, 1, null);
		modelService.saveAll(cartEntry3Staged);

		final Collection<TaxValue> taxesOfProduct3Staged = findTaxValuesStrategy.findTaxValues(cartEntry3Staged);
		Assertions.assertThat(taxesOfProduct3Staged).isNotEmpty().hasSize(1);
		final TaxValue taxValueP3Staged = taxesOfProduct3Staged.iterator().next();
		Assert.assertEquals("tax value for product 3 from staged catalog was not as expected", taxRowForGroupStaged.getValue()
				.doubleValue(), taxValueP3Staged.getValue(), 0.0001);
	}

	@Test
	public void testTaxRowCacheWithNoCatalogVersions() throws CalculationException
	{
		assertTrue("Tax caching must be enabled",
				configurationService.getConfiguration().getBoolean(Europe1Constants.KEY_CACHE_TAXES));

		// setting staged catalog version
		catalogVersionService.setSessionCatalogVersions(Collections.singletonList(stagedVersion));

		// user with user tax group and specific taxRows
		userService.setCurrentUser(testTaxedUser);

		final CartModel sessionCart = cartService.getSessionCart();
		// product 4 doesn't belong to product tax row group, cache entry will be matched by user and user tax group.
		final AbstractOrderEntryModel cartEntry4Staged = cartService.addNewEntry(sessionCart, product4Staged, 1, null);
		modelService.saveAll(cartEntry4Staged);

		final Collection<TaxValue> taxesOfProduct4 = findTaxValuesStrategy.findTaxValues(cartEntry4Staged);
		// should find both of them (user specific, user tax group specific, as none of them has catalog version)
		Assertions.assertThat(taxesOfProduct4).isNotEmpty().hasSize(2);
		boolean foundFromUser = false;
		boolean foundFromUserTaxgroup = false;
		for (final TaxValue taxValueP4 : taxesOfProduct4)
		{
			if (taxRowForUserTaxGroup.getValue().doubleValue() == taxValueP4.getValue())
			{
				foundFromUserTaxgroup = true;
			}
			if (taxRowForTaxedUser.getValue().doubleValue() == taxValueP4.getValue())
			{
				foundFromUser = true;
			}
		}
		Assert.assertTrue("tax value from user should be retrieved from cache", foundFromUser);
		Assert.assertTrue("tax value from user tax group should be retrieved from cache", foundFromUserTaxgroup);
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

}
