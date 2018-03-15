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
package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.directpersistence.JaloAccessorsService;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.internal.model.extractor.Cascader;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.internal.model.impl.InterceptorContextSnapshot;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapperContext;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.WrapperRegistry;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultCascader}
 */
public class DefaultCascaderTest extends ServicelayerTransactionalBaseTest
{

	public static final String UID = Customer.UID;
	public static final String PASSWORD_ENCODING = Customer.PASSWORDENCODING;
	public static final String LOGIN_DISABLED = Customer.LOGINDISABLED;
	public static final String DESCRIPTION = Customer.DESCRIPTION;
	public static final String DEFAULT_PAYMENT_ADDRESS = Customer.DEFAULTPAYMENTADDRESS;
	public static final String DEFAULT_SHIPMENT_ADDRESS = Customer.DEFAULTSHIPMENTADDRESS;
	public static final String ADDRESSES = Customer.ADDRESSES;
	public static final String KEYWORDS = "keywords"; //try to find accessible generated constants (category/ catalog?)
	public static final String ACTIVE = Region.ACTIVE;
	public static final String ISOCODE = Region.ISOCODE;
	public static final String REGIONS = Country.REGIONS;

	@Resource
	private ModelService modelService;
	@Resource
	private Cascader cascader;
	@Resource
	private JaloAccessorsService jaloAccessorsService;

	private CustomerModel customer;
	private AddressModel ad1;
	private AddressModel ad2;
	private AddressModel ad3;
	private CategoryModel cat1;
	private KeywordModel keywEN1, keywEN2, keywDE1, keywDE2;
	private CountryModel country1;
	private RegionModel region1, region2;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("de");
		//prepare UNSAVED models
		customer = modelService.create(CustomerModel.class);
		customer.setUid("Jan");
		customer.setDescription("Customer for cascader test");

		ad1 = modelService.create(AddressModel.class);
		ad1.setOwner(customer);
		ad1.setStreetname("1111");

		ad2 = modelService.create(AddressModel.class);
		ad2.setOwner(customer);
		ad2.setStreetname("2222");

		ad3 = modelService.create(AddressModel.class);
		ad3.setOwner(customer);
		ad3.setStreetname("3333");

		customer.setAddresses(Arrays.asList(ad1, ad2, ad3));
		customer.setDefaultPaymentAddress(ad1);
		customer.setDefaultShipmentAddress(ad2);

		//prepare Unsaved models with localized references
		cat1 = modelService.create(CategoryModel.class);
		keywEN1 = modelService.create(KeywordModel.class);
		keywEN2 = modelService.create(KeywordModel.class);
		keywDE1 = modelService.create(KeywordModel.class);
		keywDE2 = modelService.create(KeywordModel.class);
		cat1.setKeywords(Arrays.asList(keywEN1, keywEN2), Locale.ENGLISH);
		cat1.setKeywords(Arrays.asList(keywDE1, keywDE2), Locale.GERMAN);


		//prepare relations
		country1 = modelService.create(CountryModel.class);
		country1.setActive(Boolean.TRUE);
		country1.setIsocode("c1");
		region1 = modelService.create(RegionModel.class);
		region1.setActive(Boolean.TRUE);
		region1.setCountry(country1);
		region1.setIsocode("r1");
		region2 = modelService.create(RegionModel.class);
		region2.setActive(Boolean.TRUE);
		region2.setCountry(country1);
		region2.setIsocode("r2");
		country1.setRegions(Arrays.asList(region1, region2));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultCascader#getNewModels(ModelWrapper, InterceptorContextSnapshot, WrapperRegistry)}
	 * given argument: one unsaved Model (Customer) <br/>
	 * expected result: 3 AddressModels recognized as new Model references for the Customer
	 */
	@Test
	public void testGetNewModelsForUnsavedModelWithReferences()
	{
		//given
		final WrapperRegistry wrapperReg = getWrapperRegistry();
		final ModelWrapper customerModelWrapper = wrapperReg.createWrapper(customer, PersistenceOperation.SAVE);
		assertTrue(customerModelWrapper.isNew());

		final ModelValueHistory history = ((ItemModelContextImpl) ModelContextUtils
				.getItemModelContext((AbstractItemModel) customerModelWrapper.getModel())).getValueHistory();
		assertNotNull(history);
		//changes only in unlocalized attributes expected
		//final Set<String> dirtyUnlocalizedAttr = history.getDirtyAttributes();
		//model was not saved before in the DB, therefore some additional attributes (with default values) are marked as dirty as well, even if not explicitly set.
		//		assertThat(dirtyUnlocalizedAttr).containsOnly(UID, PASSWORD_ENCODING, LOGIN_DISABLED, DESCRIPTION, DEFAULT_PAYMENT_ADDRESS,
		//				DEFAULT_SHIPMENT_ADDRESS, ADDRESSES); //commented out, since this list may change quite often with items.xml changes
		assertThat(history.getDirtyLocalizedAttributes()).isEmpty();

		final Collection<ModelWrapper> newModels = cascader.getNewModels(customerModelWrapper, new InterceptorContextSnapshot(),
				wrapperReg);

		assertNotNull(newModels);
		assertTrue(newModels.size() == 3);

		checkNewModels(newModels, ad1, ad2, ad3);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultCascader#getNewModels(ModelWrapper, InterceptorContextSnapshot, WrapperRegistry)}
	 * given argument: few unsaved Models (Customer with Collection of 3 Addresses) <br/>
	 * expected result: No new models found by Cascader
	 */
	@Test
	public void testGetNewModelsForManyUnsavedModelsWithReferences()
	{
		//given
		final WrapperRegistry wrapperReg = getWrapperRegistry();
		final ModelWrapper customerModelWrapper = wrapperReg.createWrapper(customer, PersistenceOperation.SAVE);
		final ModelWrapper ad1ModelWrapper = wrapperReg.createWrapper(ad1, PersistenceOperation.SAVE);
		final ModelWrapper ad2ModelWrapper = wrapperReg.createWrapper(ad2, PersistenceOperation.SAVE);
		final ModelWrapper ad3ModelWrapper = wrapperReg.createWrapper(ad3, PersistenceOperation.SAVE);

		assertTrue(customerModelWrapper.isNew() && ad1ModelWrapper.isNew() && ad2ModelWrapper.isNew() && ad3ModelWrapper.isNew());

		final ModelValueHistory historyForCustomer = ((ItemModelContextImpl) ModelContextUtils
				.getItemModelContext((AbstractItemModel) customerModelWrapper.getModel())).getValueHistory();
		assertNotNull(historyForCustomer);
		//changes only in unlocalized attributes expected
		Set<String> dirtyUnlocalizedAttr = historyForCustomer.getDirtyAttributes();
		//model was not saved before in the DB, therefore some other attributes (with default values) are marked as dirty, even if not explicitly set.
		//assertThat(dirtyUnlocalizedAttr).containsOnly(UID, PASSWORD_ENCODING, LOGIN_DISABLED, DESCRIPTION, DEFAULT_PAYMENT_ADDRESS,
		//	DEFAULT_SHIPMENT_ADDRESS, ADDRESSES); //commented out, since this list may change quite often with items.xml changes
		assertThat(historyForCustomer.getDirtyLocalizedAttributes()).isEmpty();

		final ModelValueHistory historyForAd1 = ((ItemModelContextImpl) ModelContextUtils
				.getItemModelContext((AbstractItemModel) ad1ModelWrapper.getModel())).getValueHistory();
		assertNotNull(historyForAd1);
		//changes only in unlocalized attributes expected
		dirtyUnlocalizedAttr = historyForAd1.getDirtyAttributes();
		//model was not saved before in th DB, therefore some other attributes (with default values) are marked as dirty, even if not explicitly set.
		//this is pretty since the model can change - a new attributes can appear
		assertThat(dirtyUnlocalizedAttr).contains("owner", "billingAddress", "shippingAddress", "unloadingAddress", "streetname",
				"contactAddress");
		assertThat(historyForAd1.getDirtyLocalizedAttributes()).isEmpty();

		final Collection<ModelWrapper> newModels = cascader.getNewModels(customerModelWrapper, new InterceptorContextSnapshot(),
				wrapperReg);

		assertTrue(CollectionUtils.isEmpty(newModels));
		//checkNewModels(newModels);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultCascader#getNewModels(ModelWrapper, InterceptorContextSnapshot, WrapperRegistry)}
	 * given argument: one existing (saved) Model holding an updated list of addresses (4 Addresses instead of 3, 1 new
	 * Address) <br/>
	 * expected result: Only one AddressModel (the new one).
	 */
	@Test
	public void testGetNewModelsForExistingModelAndChangedReferences()
	{
		//given
		modelService.save(customer);

		final AddressModel ad4 = modelService.create(AddressModel.class);
		ad4.setOwner(customer);
		ad4.setStreetname("4444");
		customer.setAddresses(Arrays.asList(ad1, ad2, ad3, ad4));

		final WrapperRegistry wrapperReg = getWrapperRegistry();
		final ModelWrapper customerModelWrapper = wrapperReg.createWrapper(customer, PersistenceOperation.SAVE);
		assertFalse(customerModelWrapper.isNew());

		final ModelValueHistory history = ((ItemModelContextImpl) ModelContextUtils.getItemModelContext(customer))
				.getValueHistory();
		assertNotNull(history);
		//changes only in unlocalized attributes expected
		final Set<String> dirtyUnlocalizedAttr = history.getDirtyAttributes();
		assertThat(dirtyUnlocalizedAttr).containsOnly(ADDRESSES);
		assertThat(history.getDirtyLocalizedAttributes()).isEmpty();

		final Collection<ModelWrapper> newModels = cascader.getNewModels(customerModelWrapper, new InterceptorContextSnapshot(),
				wrapperReg);

		assertNotNull(newModels);
		assertTrue(newModels.size() == 1);

		//only ad4 is expected as new. Even if the "whole" addresses" attribute (collection with ad1, ad2, ad3, ad4) is marked as dirty
		checkNewModels(newModels, ad4);
	}

	private WrapperRegistry getWrapperRegistry()
	{
		return new WrapperRegistry(new ModelWrapperContext(((DefaultModelService) modelService).getConverterRegistry(),
				((DefaultModelService) modelService).getInterceptorRegistry(), cascader));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultCascader#getNewModels(ModelWrapper, InterceptorContextSnapshot, WrapperRegistry)}
	 * given argument: one unsaved Model - Category, holding 4 localized references (English and German Keywords) <br/>
	 * expected result: 4 Keywords (2 EN, 2DE), recognized as new Models by the Cascader
	 */
	@Test
	public void testGetNewModelsForUnsavedModelWithLocalizedMany2ManyRelation()
	{
		//given cat<->keyword relation
		final WrapperRegistry wrapperReg = getWrapperRegistry();
		final ModelWrapper cat1ModelWrapper = wrapperReg.createWrapper(cat1, PersistenceOperation.SAVE);

		assertTrue(cat1ModelWrapper.isNew());

		final ModelValueHistory history = ((ItemModelContextImpl) ModelContextUtils
				.getItemModelContext((AbstractItemModel) cat1ModelWrapper.getModel())).getValueHistory();
		assertNotNull(history);

		final Set<String> dirtyUnlocalizedAttr = history.getDirtyAttributes();
		assertThat(dirtyUnlocalizedAttr).isEmpty();
		//changes in localized reference attributes expected
		final Map<Locale, Set<String>> dirtyLocAttrMap = history.getDirtyLocalizedAttributes();
		assertThat(dirtyLocAttrMap).isNotEmpty();
		assertTrue(dirtyLocAttrMap.size() == 2);
		assertTrue(dirtyLocAttrMap.containsKey(Locale.ENGLISH));
		assertTrue(dirtyLocAttrMap.containsKey(Locale.GERMAN));
		assertThat(dirtyLocAttrMap.get(Locale.ENGLISH)).containsOnly(KEYWORDS);
		assertThat(dirtyLocAttrMap.get(Locale.GERMAN)).containsOnly(KEYWORDS);

		final Collection<ModelWrapper> newModels = cascader.getNewModels(cat1ModelWrapper, new InterceptorContextSnapshot(),
				wrapperReg);

		assertNotNull(newModels);
		assertTrue(newModels.size() == 4);

		checkNewModels(newModels, keywEN1, keywEN2, keywDE1, keywDE2);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultCascader#getNewModels(ModelWrapper, InterceptorContextSnapshot, WrapperRegistry)}
	 * given argument: few unsaved Models (Country with 2 Regions). (One to many Relation) <br/>
	 * expected result: No new Models found by Cascader
	 */
	@Test
	public void testGetNewModelsForManyUnsavedModelsWithOneToManyRelation()
	{
		//given
		final WrapperRegistry wrapperReg = getWrapperRegistry();
		final ModelWrapper countryModelWrapper = wrapperReg.createWrapper(country1, PersistenceOperation.SAVE);
		final ModelWrapper reg1ModelWrapper = wrapperReg.createWrapper(region1, PersistenceOperation.SAVE);
		final ModelWrapper reg2ModelWrapper = wrapperReg.createWrapper(region2, PersistenceOperation.SAVE);

		assertTrue(countryModelWrapper.isNew() && reg1ModelWrapper.isNew() && reg2ModelWrapper.isNew());


		final ModelValueHistory historyForCountry = ((ItemModelContextImpl) ModelContextUtils
				.getItemModelContext((AbstractItemModel) countryModelWrapper.getModel())).getValueHistory();
		assertNotNull(historyForCountry);
		//changes only in unlocalized attributes expected
		Set<String> dirtyUnlocalizedAttr = historyForCountry.getDirtyAttributes();
		//model was not saved before in the DB, therefore some additional attributes (with default values) are marked as dirty as well, even if not explicitly set.
		assertThat(dirtyUnlocalizedAttr).containsOnly(ACTIVE, ISOCODE, REGIONS);
		assertThat(historyForCountry.getDirtyLocalizedAttributes()).isEmpty();

		final ModelValueHistory historyForRegion1 = ((ItemModelContextImpl) ModelContextUtils
				.getItemModelContext((AbstractItemModel) reg1ModelWrapper.getModel())).getValueHistory();
		assertNotNull(historyForCountry);
		//changes only in unlocalized attributes expected
		dirtyUnlocalizedAttr = historyForRegion1.getDirtyAttributes();
		//model was not saved before in the DB, therefore some additional attributes (with default values) are marked as dirty as well, even if not explicitly set.
		assertThat(dirtyUnlocalizedAttr).containsOnly(ACTIVE, ISOCODE, "country");
		assertThat(historyForCountry.getDirtyLocalizedAttributes()).isEmpty();

		final Collection<ModelWrapper> newModels = cascader.getNewModels(countryModelWrapper, new InterceptorContextSnapshot(),
				wrapperReg);
		assertTrue(CollectionUtils.isEmpty(newModels));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultCascader#getNewModels(ModelWrapper, InterceptorContextSnapshot, WrapperRegistry)}
	 * given argument: one unsaved Model - Country, holding 2 references (Regions). (One to many relation)<br/>
	 * expected result: 2 Regions, recognized as new by the Cascader.
	 */
	@Test
	public void testGetNewModelsForUnsavedModelWithOneToManyRelation()
	{
		//given
		final WrapperRegistry wrapperReg = getWrapperRegistry();
		final ModelWrapper countryModelWrapper = wrapperReg.createWrapper(country1, PersistenceOperation.SAVE);
		assertTrue(countryModelWrapper.isNew());

		final ModelValueHistory historyForCountry = ((ItemModelContextImpl) ModelContextUtils
				.getItemModelContext((AbstractItemModel) countryModelWrapper.getModel())).getValueHistory();
		assertNotNull(historyForCountry);
		//changes only in unlocalized attributes expected
		final Set<String> dirtyUnlocalizedAttr = historyForCountry.getDirtyAttributes();
		//model was not saved before in the DB, therefore some additional attributes (with default values) are marked as dirty as well, even if not explicitly set.
		assertThat(dirtyUnlocalizedAttr).containsOnly(ACTIVE, ISOCODE, REGIONS);
		assertThat(historyForCountry.getDirtyLocalizedAttributes()).isEmpty();

		final Collection<ModelWrapper> newModels = cascader.getNewModels(countryModelWrapper, new InterceptorContextSnapshot(),
				wrapperReg);

		assertNotNull(newModels);
		assertTrue(newModels.size() == 2);

		checkNewModels(newModels, region1, region2);
	}

	/**
	 *
	 */
	private void checkNewModels(final Collection<ModelWrapper> newModels, final Object... expectedModels)
	{
		final Set<Object> models = new HashSet<Object>();
		for (final ModelWrapper wr : newModels)
		{
			models.add(wr.getModel());
		}
		assertThat(models).containsOnly(expectedModels);
	}

}
