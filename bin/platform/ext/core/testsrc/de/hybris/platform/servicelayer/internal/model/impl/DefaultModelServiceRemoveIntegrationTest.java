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
package de.hybris.platform.servicelayer.internal.model.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.impl.AnonymousCacheUnit;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.directpersistence.MutableChangeSet;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.directpersistence.cache.DefaultSLDDataContainerProvider;
import de.hybris.platform.directpersistence.impl.DefaultChangeSet;
import de.hybris.platform.directpersistence.impl.DefaultWritePersistenceGateway;
import de.hybris.platform.directpersistence.record.DefaultRelationChanges;
import de.hybris.platform.directpersistence.record.InsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.LocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.impl.DefaultInsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.impl.DefaultLocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.impl.DefaultNonLocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.impl.DeleteRecord;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.RelationMetaInfo;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.seed.TestDataCreator;
import de.hybris.platform.util.ItemPropertyValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


/**
 * Tests the remove() and removeAll() methods from
 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService}. The test scenarios cover the
 * following points:
 * <ul>
 * <li>preparing different kind of items (simple, with references, relations, etc...) and persisting them into DB
 * "directly" (via WritePersistenceGateway)</li>
 * <li>firing the remove() or removeAll() method from DefautlModelService</li>
 * <li>checking if the cache has been invalidated correctly</li>
 * <li>checking if removed models have been detached from context</li>
 * <li>checking if the items have been removed properly (using modelService.get())</li>
 * <li>cleaning (using ModelPersistencegateway)</li>
 * </ul>
 */
@IntegrationTest
public class DefaultModelServiceRemoveIntegrationTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(DefaultModelServiceRemoveIntegrationTest.class);

	@Resource
	private DefaultWritePersistenceGateway writePersistenceGateway;
	@Resource
	private ModelService modelService;
	@Resource
	private CommonI18NService commonI18NService;

	private InsertRecord insertRecordUnit, insertRecordRegion1, insertRecordRegion2, insertRecordCountry, insertRecordCategory,
			insertRecordKeyw1En, insertRecordKeyw2En, insertRecordKeyw1De, insertRecordKeyw2De, insertRecordCustomer,
			insertRecordAddress1, insertRecordAddress2;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("en");
		getOrCreateLanguage("de");


        final LanguageModel langEN = commonI18NService.getLanguage("en");
		final LanguageModel langDE = commonI18NService.getLanguage("de");

		PK pk = generatePkForCode("Unit");

		insertRecordUnit = new InsertRecord(pk, "Unit",
				Sets.newHashSet(new PropertyHolder("Code", "insert1"), new PropertyHolder("UnitType", "testUnitType")));

		//country and region - helpful for one2many relation tests
		pk = generatePkForCode("Country");
		insertRecordCountry = new InsertRecord(pk, "Country",
				Sets.newHashSet(new PropertyHolder("isocode", "c1"), new PropertyHolder("active", Boolean.TRUE)));
		pk = generatePkForCode("Region");
		insertRecordRegion1 = new InsertRecord(pk, "Region",
				Sets.newHashSet(new PropertyHolder("isocode", "r1"), new PropertyHolder("active", Boolean.TRUE),
						new PropertyHolder("country", new ItemPropertyValue(insertRecordCountry.getPK()))));
		pk = generatePkForCode("Region");
		insertRecordRegion2 = new InsertRecord(pk, "Region",
				Sets.newHashSet(new PropertyHolder("isocode", "r2"), new PropertyHolder("active", Boolean.TRUE),
						new PropertyHolder("country", new ItemPropertyValue(insertRecordCountry.getPK()))));

		//category and keywords - helpful for many2many tests
		pk = generatePkForCode("Category");
		insertRecordCategory = new InsertRecord(pk, "Category", Sets.newHashSet(new PropertyHolder("Code", "cat1")));

        final TestDataCreator testDataCreator = new TestDataCreator(modelService);
        final CatalogModel testCatalog = testDataCreator.createCatalog("testCatalog");
        final CatalogVersionModel catalogVersion = testDataCreator.createCatalogVersion("testVersion", testCatalog);

		pk = generatePkForCode("Keyword");
		insertRecordKeyw1En = new InsertRecord(pk, "Keyword",
				Sets.newHashSet(new PropertyHolder("keyword", "keyw1En"),
						new PropertyHolder("language", new ItemPropertyValue(langEN.getPk())),
						new PropertyHolder("catalogVersion", new ItemPropertyValue(catalogVersion.getPk()))));
		pk = generatePkForCode("Keyword");
		insertRecordKeyw2En = new InsertRecord(pk, "Keyword",
				Sets.newHashSet(new PropertyHolder("keyword", "keyw2En"),
						new PropertyHolder("language", new ItemPropertyValue(langEN.getPk())),
						new PropertyHolder("catalogVersion", new ItemPropertyValue(catalogVersion.getPk()))));
		pk = generatePkForCode("Keyword");
		insertRecordKeyw1De = new InsertRecord(pk, "Keyword",
				Sets.newHashSet(new PropertyHolder("keyword", "keyw1De"),
						new PropertyHolder("language", new ItemPropertyValue(langDE.getPk())),
						new PropertyHolder("catalogVersion", new ItemPropertyValue(catalogVersion.getPk()))));
		pk = generatePkForCode("Keyword");
		insertRecordKeyw2De = new InsertRecord(pk, "Keyword",
				Sets.newHashSet(new PropertyHolder("keyword", "keyw2De"),
						new PropertyHolder("language", new ItemPropertyValue(langDE.getPk())),
						new PropertyHolder("catalogVersion", new ItemPropertyValue(catalogVersion.getPk()))));

		//customer and addresses - references linked via "Owner" field under Addresses
		pk = generatePkForCode("Customer");
		insertRecordCustomer = new InsertRecord(pk, "Customer", Sets.newHashSet(new PropertyHolder("Uid", "Jan")));
		pk = generatePkForCode("Address");
		insertRecordAddress1 = new InsertRecord(pk, "Address", Sets.newHashSet(new PropertyHolder("StreetName", "Lompy str."),
				new PropertyHolder("Owner", new ItemPropertyValue(insertRecordCustomer.getPK()))));
		pk = generatePkForCode("Address");
		insertRecordAddress2 = new InsertRecord(pk, "Address", Sets.newHashSet(new PropertyHolder("StreetName", "Dworcowa str."),
				new PropertyHolder("Owner", new ItemPropertyValue(insertRecordCustomer.getPK()))));

		final MutableChangeSet changeSet = new DefaultChangeSet();
		changeSet.add(insertRecordUnit, insertRecordCountry, insertRecordRegion1, insertRecordRegion2, insertRecordCategory,
				insertRecordKeyw1En, insertRecordKeyw2En, insertRecordKeyw1De, insertRecordKeyw2De, insertRecordCustomer,
				insertRecordAddress1, insertRecordAddress2);
		final Collection<PersistResult> givenResults = writePersistenceGateway.persist(changeSet);
		assertThat(givenResults).hasSize(12);

	}

	@After
	public void clear() throws Exception
	{
		final MutableChangeSet changeSet = new DefaultChangeSet();
		int expectedItemCountToDelete = 0;

		//collect all existing changes into the changeset
		final UnitModel unit = (UnitModel) getSavedModel(insertRecordUnit.getPK());
		if (unit != null)
		{
			changeSet.add(new DeleteRecord(unit.getPk(), "unit", unit.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final CountryModel country = (CountryModel) getSavedModel(insertRecordCountry.getPK());
		if (country != null)
		{
			changeSet.add(new DeleteRecord(country.getPk(), "country", country.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final RegionModel region1 = (RegionModel) getSavedModel(insertRecordRegion1.getPK());
		if (region1 != null)
		{
			changeSet.add(new DeleteRecord(region1.getPk(), "region", region1.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final RegionModel region2 = (RegionModel) getSavedModel(insertRecordRegion2.getPK());
		if (region2 != null)
		{
			changeSet.add(new DeleteRecord(region2.getPk(), "region", region2.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final KeywordModel keyword1En = (KeywordModel) getSavedModel(insertRecordKeyw1En.getPK());
		if (keyword1En != null)
		{
			changeSet.add(new DeleteRecord(keyword1En.getPk(), "keyword", keyword1En.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final KeywordModel keyword2En = (KeywordModel) getSavedModel(insertRecordKeyw2En.getPK());
		if (keyword2En != null)
		{
			changeSet.add(new DeleteRecord(keyword2En.getPk(), "keyword", keyword2En.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final KeywordModel keyword1De = (KeywordModel) getSavedModel(insertRecordKeyw1De.getPK());
		if (keyword1De != null)
		{
			changeSet.add(new DeleteRecord(keyword1De.getPk(), "keyword", keyword1De.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final KeywordModel keyword2De = (KeywordModel) getSavedModel(insertRecordKeyw2De.getPK());
		if (keyword2De != null)
		{
			changeSet.add(new DeleteRecord(keyword2De.getPk(), "keyword", keyword2De.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final CategoryModel cat1 = (CategoryModel) getSavedModel(insertRecordCategory.getPK());
		if (cat1 != null)
		{
			changeSet.add(new DeleteRecord(cat1.getPk(), "category", cat1.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final CustomerModel customer = (CustomerModel) getSavedModel(insertRecordCustomer.getPK());
		if (customer != null)
		{
			changeSet.add(new DeleteRecord(customer.getPk(), "customer", customer.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final AddressModel address1 = (AddressModel) getSavedModel(insertRecordAddress1.getPK());
		if (address1 != null)
		{
			changeSet.add(new DeleteRecord(address1.getPk(), "address", address1.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}
		final AddressModel address2 = (AddressModel) getSavedModel(insertRecordAddress2.getPK());
		if (address2 != null)
		{
			changeSet.add(new DeleteRecord(address2.getPk(), "address", address2.getItemModelContext().getPersistenceVersion()));
			expectedItemCountToDelete++;
		}

		//remove everything collected in the changeset
		final Collection<PersistResult> persistResults = writePersistenceGateway.persist(changeSet);
		assertEquals(expectedItemCountToDelete, persistResults.size());
	}

	private PK generatePkForCode(final String typeCode)
	{
		final TypeInfoMap persistenceInfo = Registry.getCurrentTenant().getPersistenceManager().getPersistenceInfo(typeCode);
		return PK.createCounterPK(persistenceInfo.getItemTypeCode());
	}

	@Test
	public void testDeleteSingleModel()
	{
		final PK pk = insertRecordUnit.getPK();
		//item has been persisted but is not in the cache yet
		assertFalse(objectExistsInCache(getCacheUnitForPK(pk)));
		final UnitModel savedModel = modelService.get(pk);
		assertNotNull(savedModel);
		assertTrue(objectExistsInCache(getCacheUnitForPK(pk)));

		modelService.remove(savedModel);
		assertFalse(modelService.isAttached(savedModel));
		assertFalse(objectExistsInCache(getCacheUnitForPK(pk)));

		UnitModel removedModel = null;
		try
		{
			removedModel = modelService.get(pk);
			fail("Exception expected but not thrown - model should not be found after removing");
		}
		catch (final ModelLoadingException e)
		{
			//ok
			assertNull(removedModel);
		}
		assertTrue(modelService.isRemoved(savedModel));

	}

	@Test
	public void testDeleteSimpleIndependentModels()
	{
		final UnitModel savedUnitModel = modelService.get(insertRecordUnit.getPK());
		assertNotNull(savedUnitModel);
		final RegionModel savedRegion1Model = modelService.get(insertRecordRegion1.getPK());
		assertNotNull(savedRegion1Model);
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordUnit.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordRegion1.getPK())));

		modelService.removeAll(Arrays.asList(savedUnitModel, savedRegion1Model));
		assertFalse(modelService.isAttached(savedUnitModel) || modelService.isAttached(savedRegion1Model));

		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordUnit.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordRegion1.getPK())));

		final Collection<PK> removedPKs = Arrays.asList(insertRecordUnit.getPK(), insertRecordRegion1.getPK());
		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}
		assertTrue(modelService.isRemoved(savedUnitModel));
		assertTrue(modelService.isRemoved(savedRegion1Model));
	}

	@Test
	public void testDeleteModelWithRelationsImplicitly()
	{
		final CountryModel savedCountryModel = modelService.get(insertRecordCountry.getPK());
		assertNotNull(savedCountryModel);
		final RegionModel savedRegion1Model = modelService.get(insertRecordRegion1.getPK());
		assertNotNull(savedRegion1Model);
		final RegionModel savedRegion2Model = modelService.get(insertRecordRegion2.getPK());
		assertNotNull(savedRegion2Model);

		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordCountry.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordRegion1.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordRegion2.getPK())));

		modelService.remove(savedCountryModel); //the related regions should be removed as well
		assertFalse(modelService.isAttached(savedCountryModel) || modelService.isAttached(savedRegion1Model)
				|| modelService.isAttached(savedRegion2Model));

		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordCountry.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordRegion1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordRegion2.getPK())));

		final Collection<PK> removedPKs = Arrays.asList(insertRecordCountry.getPK(), insertRecordRegion1.getPK(),
				insertRecordRegion2.getPK());

		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}
		assertTrue(modelService.isRemoved(savedCountryModel));
		assertTrue(modelService.isRemoved(savedRegion1Model));
		assertTrue(modelService.isRemoved(savedRegion2Model));

	}


	@Test
	public void testDeleteModelWithRelationsExplicitlyOneSideFirst()
	{
		final CountryModel savedCountryModel = modelService.get(insertRecordCountry.getPK());
		assertNotNull(savedCountryModel);
		final RegionModel savedRegion1Model = modelService.get(insertRecordRegion1.getPK());
		assertNotNull(savedRegion1Model);
		final RegionModel savedRegion2Model = modelService.get(insertRecordRegion2.getPK());
		assertNotNull(savedRegion2Model);

		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordCountry.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordRegion1.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordRegion2.getPK())));

		modelService.removeAll(Arrays.asList(savedCountryModel, savedRegion1Model, savedRegion2Model));

		assertFalse(modelService.isAttached(savedCountryModel) || modelService.isAttached(savedRegion1Model)
				|| modelService.isAttached(savedRegion2Model));

		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordCountry.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordRegion1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordRegion2.getPK())));

		final Collection<PK> removedPKs = Arrays.asList(insertRecordCountry.getPK(), insertRecordRegion1.getPK(),
				insertRecordRegion2.getPK());
		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}
		assertTrue(modelService.isRemoved(savedCountryModel));
		assertTrue(modelService.isRemoved(savedRegion1Model));
		assertTrue(modelService.isRemoved(savedRegion2Model));
	}

	@Test
	public void testDeleteModelWithRelationsExplicitlyManySideFirst()
	{
		final CountryModel savedCountryModel = modelService.get(insertRecordCountry.getPK());
		assertNotNull(savedCountryModel);
		final RegionModel savedRegion1Model = modelService.get(insertRecordRegion1.getPK());
		assertNotNull(savedRegion1Model);
		final RegionModel savedRegion2Model = modelService.get(insertRecordRegion2.getPK());
		assertNotNull(savedRegion2Model);

		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordCountry.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordRegion1.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordRegion2.getPK())));

		modelService.removeAll(Arrays.asList(savedRegion1Model, savedRegion2Model, savedCountryModel));

		assertFalse(modelService.isAttached(savedCountryModel) || modelService.isAttached(savedRegion1Model)
				|| modelService.isAttached(savedRegion2Model));

		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordCountry.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordRegion1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordRegion2.getPK())));

		final Collection<PK> removedPKs = Arrays.asList(insertRecordCountry.getPK(), insertRecordRegion1.getPK(),
				insertRecordRegion2.getPK());
		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}
		assertTrue(modelService.isRemoved(savedCountryModel));
		assertTrue(modelService.isRemoved(savedRegion1Model));
		assertTrue(modelService.isRemoved(savedRegion2Model));

	}

	@Test
	public void testDeleteModelWithReferencesImplicitly()
	{
		//given - Customer with 2 Addresses. No real relation type! The link is the Owner field in Address
		final AddressModel savedAddressModel1 = modelService.get(insertRecordAddress1.getPK());
		assertNotNull(savedAddressModel1);
		final AddressModel savedAddressModel2 = modelService.get(insertRecordAddress2.getPK());
		assertNotNull(savedAddressModel2);
		final CustomerModel savedCustomerModel = modelService.get(insertRecordCustomer.getPK());
		assertNotNull(savedCustomerModel);

		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordAddress1.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordAddress2.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordCustomer.getPK())));

		modelService.remove(savedCustomerModel);//this should also removed the addresses

		assertFalse(modelService.isAttached(savedCustomerModel) || modelService.isAttached(savedAddressModel1)
				|| modelService.isAttached(savedAddressModel2)); //all removed models detached from context

		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordAddress1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordAddress2.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordCustomer.getPK())));

		final Collection<PK> removedPKs = Arrays.asList(insertRecordCustomer.getPK(), insertRecordAddress1.getPK(),
				insertRecordAddress2.getPK());
		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}
		assertTrue(modelService.isRemoved(savedCustomerModel));
		assertTrue(modelService.isRemoved(savedAddressModel1));
		assertTrue(modelService.isRemoved(savedAddressModel2));

	}

	@Test
	public void testDeleteModelWithReferencesExplicitly()
	{
		//given - Customer with 2 Addresses. No real relation type! They are linked with the Owner field in Address
		final AddressModel savedAddressModel1 = modelService.get(insertRecordAddress1.getPK());
		assertNotNull(savedAddressModel1);
		final AddressModel savedAddressModel2 = modelService.get(insertRecordAddress2.getPK());
		assertNotNull(savedAddressModel2);
		final CustomerModel savedCustomerModel = modelService.get(insertRecordCustomer.getPK());
		assertNotNull(savedCustomerModel);

		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordAddress1.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordAddress2.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordCustomer.getPK())));

		modelService.removeAll(savedCustomerModel, savedAddressModel1, savedAddressModel2);

		assertTrue(modelService.isRemoved(savedCustomerModel));
		assertTrue(modelService.isRemoved(savedAddressModel1));
		assertTrue(modelService.isRemoved(savedAddressModel2));

		//all removed models detached from context
		assertFalse(modelService.isAttached(savedCustomerModel));
		assertFalse(modelService.isAttached(savedAddressModel1));
		assertFalse(modelService.isAttached(savedAddressModel2));

		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordAddress1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordAddress2.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordCustomer.getPK())));

		final Collection<PK> removedPKs = Arrays.asList(insertRecordCustomer.getPK(), insertRecordAddress1.getPK(),
				insertRecordAddress2.getPK());
		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}
		assertTrue(modelService.isRemoved(savedCustomerModel));
		assertTrue(modelService.isRemoved(savedAddressModel1));
		assertTrue(modelService.isRemoved(savedAddressModel2));
	}

	@Test
	public void testDeleteModelWithReferencesExplicitlyTargetSideFirst()
	{
		//given - Customer with 2 Addresses. No real relation type! They are linked with the Owner field in Address
		final AddressModel savedAddressModel1 = modelService.get(insertRecordAddress1.getPK());
		assertNotNull(savedAddressModel1);
		final AddressModel savedAddressModel2 = modelService.get(insertRecordAddress2.getPK());
		assertNotNull(savedAddressModel2);
		final CustomerModel savedCustomerModel = modelService.get(insertRecordCustomer.getPK());
		assertNotNull(savedCustomerModel);

		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordAddress1.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordAddress2.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordCustomer.getPK())));

		modelService.removeAll(Arrays.asList(savedAddressModel1, savedAddressModel2, savedCustomerModel));

		assertFalse(modelService.isAttached(savedCustomerModel) || modelService.isAttached(savedAddressModel1)
				|| modelService.isAttached(savedAddressModel2)); //all removed models detached from context

		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordAddress1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordAddress2.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecordCustomer.getPK())));

		final Collection<PK> removedPKs = Arrays.asList(insertRecordCustomer.getPK(), insertRecordAddress1.getPK(),
				insertRecordAddress2.getPK());
		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}
		assertTrue(modelService.isRemoved(savedCustomerModel));
		assertTrue(modelService.isRemoved(savedAddressModel1));
		assertTrue(modelService.isRemoved(savedAddressModel2));

	}

	@Test
	public void testDeleteModelWithManyToManyRelationsImplicitly()
	{
		//given - one category and 4 keywords, not linked
		final CategoryModel savedCategoryModel = modelService.get(insertRecordCategory.getPK());
		assertNotNull(savedCategoryModel);
		KeywordModel savedKeyw1ENModel = modelService.get(insertRecordKeyw1En.getPK());
		assertNotNull(savedKeyw1ENModel);
		KeywordModel savedKeyw1DEModel = modelService.get(insertRecordKeyw1De.getPK());
		assertNotNull(savedKeyw1DEModel);
		final KeywordModel savedKeyw2ENModel = modelService.get(insertRecordKeyw2En.getPK());
		assertNotNull(savedKeyw2ENModel);
		final KeywordModel savedKeyw2DEModel = modelService.get(insertRecordKeyw2De.getPK());
		assertNotNull(savedKeyw2DEModel);
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordCategory.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordKeyw1En.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordKeyw2En.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordKeyw1De.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordKeyw2De.getPK())));

		//create relation - link category and keywords
		final MutableChangeSet changeSet = new DefaultChangeSet();

		final LocalizedRelationChanges locRelationChanges = new DefaultLocalizedRelationChanges(
				RelationMetaInfo.builder().relationName("Category2KeywordRelation").sourceOrdered(true).targetOrdered(true).build());
		changeSet.putRelationChanges("Category2KeywordRelation", locRelationChanges);
		final DefaultRelationChanges englishRelationChanges = new DefaultNonLocalizedRelationChanges(
				RelationMetaInfo.builder().relationName("Category2KeywordRelation").sourceOrdered(true).targetOrdered(true).build());
		final DefaultRelationChanges germanRelationChanges = new DefaultNonLocalizedRelationChanges(
				RelationMetaInfo.builder().relationName("Category2KeywordRelation").sourceOrdered(true).targetOrdered(true).build());
		locRelationChanges.put(Locale.ENGLISH, englishRelationChanges);
		locRelationChanges.put(Locale.GERMAN, germanRelationChanges);

		final InsertManyToManyRelationRecord catToKeyw1ENRelationRecord = new DefaultInsertManyToManyRelationRecord(
				savedCategoryModel.getPk(), savedKeyw1ENModel.getPk(), true);
		final InsertManyToManyRelationRecord catToKeyw1DERelationRecord = new DefaultInsertManyToManyRelationRecord(
				savedCategoryModel.getPk(), savedKeyw1DEModel.getPk(), true);

		englishRelationChanges.add(catToKeyw1ENRelationRecord);
		germanRelationChanges.add(catToKeyw1DERelationRecord);

		final Collection<PersistResult> persistResults = writePersistenceGateway.persist(changeSet);
		assertThat(persistResults).hasSize(2);
		final List<LinkModel> savedRelations = getSavedRelations(persistResults);
		assertThat(savedRelations).hasSize(2);

		assertTrue(modelService.isAttached(savedCategoryModel));
		assertTrue(modelService.isAttached(savedRelations.get(0)));
		assertTrue(modelService.isAttached(savedRelations.get(1)));
		//remove category and - implicitly - 2 records from many2many table
		modelService.remove(savedCategoryModel);

		assertFalse(modelService.isAttached(savedCategoryModel));
		//		assertFalse(modelService.isAttached(savedRelations.get(0)));
		//		assertFalse(modelService.isAttached(savedRelations.get(1)));

		//the keywords should NOT be deleted, only the links
		savedKeyw1ENModel = modelService.get(insertRecordKeyw1En.getPK());
		assertNotNull(savedKeyw1ENModel); //
		savedKeyw1DEModel = modelService.get(insertRecordKeyw1De.getPK());
		assertNotNull(savedKeyw1DEModel);

		final Collection<PK> removedPKs = Arrays.asList(insertRecordCategory.getPK(), savedRelations.get(0).getPk(),
				savedRelations.get(1).getPk());

		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}
		assertTrue(modelService.isRemoved(savedCategoryModel));
		assertTrue(modelService.isRemoved(savedRelations.get(0)));
		assertTrue(modelService.isRemoved(savedRelations.get(1)));
		assertFalse(modelService.isRemoved(savedKeyw1ENModel));
		assertFalse(modelService.isRemoved(savedKeyw2ENModel));
		assertFalse(modelService.isRemoved(savedKeyw1DEModel));
		assertFalse(modelService.isRemoved(savedKeyw2DEModel));

	}

	private List<LinkModel> getSavedRelations(final Collection<PersistResult> persistResults)
	{
		final List<LinkModel> savedRelations = new ArrayList<LinkModel>();
		for (final PersistResult res : persistResults)
		{
			final LinkModel savedRelation = modelService.get(res.getPk());
			savedRelations.add(savedRelation);
		}
		return savedRelations;
	}

	@Test
	public void testDeleteModelWithManyToManyRelationsAndTarget()
	{
		//given - one category and 4 keywords, not linked
		final CategoryModel savedCategoryModel = modelService.get(insertRecordCategory.getPK());
		assertNotNull(savedCategoryModel);
		final KeywordModel savedKeyw1ENModel = modelService.get(insertRecordKeyw1En.getPK());
		assertNotNull(savedKeyw1ENModel);
		final KeywordModel savedKeyw1DEModel = modelService.get(insertRecordKeyw1De.getPK());
		assertNotNull(savedKeyw1DEModel);
		final KeywordModel savedKeyw2ENModel = modelService.get(insertRecordKeyw2En.getPK());
		assertNotNull(savedKeyw2ENModel);
		final KeywordModel savedKeyw2DEModel = modelService.get(insertRecordKeyw2De.getPK());
		assertNotNull(savedKeyw2DEModel);
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordCategory.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordKeyw1En.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordKeyw2En.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordKeyw1De.getPK())));
		assertTrue(objectExistsInCache(getCacheUnitForPK(insertRecordKeyw2De.getPK())));

		//create relation - link category and keywords
		final MutableChangeSet changeSet = new DefaultChangeSet();

		final LocalizedRelationChanges locRelationChanges = new DefaultLocalizedRelationChanges(
				RelationMetaInfo.builder().relationName("Category2KeywordRelation").sourceOrdered(true).targetOrdered(true).build());
		changeSet.putRelationChanges("Category2KeywordRelation", locRelationChanges);
		final DefaultRelationChanges englishRelationChanges = new DefaultNonLocalizedRelationChanges(
				RelationMetaInfo.builder().relationName("Category2KeywordRelation").sourceOrdered(true).targetOrdered(true).build());
		final DefaultRelationChanges germanRelationChanges = new DefaultNonLocalizedRelationChanges(
				RelationMetaInfo.builder().relationName("Category2KeywordRelation").sourceOrdered(true).targetOrdered(true).build());
		locRelationChanges.put(Locale.ENGLISH, englishRelationChanges);
		locRelationChanges.put(Locale.GERMAN, germanRelationChanges);

		final InsertManyToManyRelationRecord catToKeyw1ENRelationRecord = new DefaultInsertManyToManyRelationRecord(
				savedCategoryModel.getPk(), savedKeyw1ENModel.getPk(), true);
		final InsertManyToManyRelationRecord catToKeyw1DERelationRecord = new DefaultInsertManyToManyRelationRecord(
				savedCategoryModel.getPk(), savedKeyw1DEModel.getPk(), true);

		englishRelationChanges.add(catToKeyw1ENRelationRecord);
		germanRelationChanges.add(catToKeyw1DERelationRecord);

		final Collection<PersistResult> persistResults = writePersistenceGateway.persist(changeSet);
		assertThat(persistResults).hasSize(2);
		final List<LinkModel> savedRelations = getSavedRelations(persistResults);
		assertThat(savedRelations).hasSize(2);

		//remove category and 2 linked keywords - the relation should be removed implicitly as well
		modelService.removeAll(Arrays.asList(savedCategoryModel, savedKeyw1ENModel, savedKeyw1DEModel));

		assertFalse(modelService.isAttached(savedCategoryModel) || modelService.isAttached(savedKeyw1ENModel)
				|| modelService.isAttached(savedKeyw1DEModel)); //all removed models detached from context

		final Collection<PK> removedPKs = Arrays.asList(insertRecordCategory.getPK(), insertRecordKeyw1En.getPK(),
				insertRecordKeyw1De.getPK(), savedRelations.get(0).getPk(), savedRelations.get(1).getPk());

		for (final PK pk : removedPKs)
		{
			ItemModel removedModel = null;
			try
			{
				removedModel = modelService.get(pk);
				fail("Exception expected but not thrown - model with PK: " + pk + " should not be found after removing");
			}
			catch (final ModelLoadingException e)
			{
				//ok
				assertNull(removedModel);
			}
		}

		assertTrue(modelService.isRemoved(savedCategoryModel));
		assertTrue(modelService.isRemoved(savedKeyw1ENModel));
		assertTrue(modelService.isRemoved(savedKeyw1DEModel));
		assertTrue(modelService.isRemoved(savedRelations.get(0)));
		assertTrue(modelService.isRemoved(savedRelations.get(1)));
		assertFalse(modelService.isRemoved(savedKeyw2ENModel));
		assertFalse(modelService.isRemoved(savedKeyw2DEModel));

	}

	private ItemModel getSavedModel(final PK pk)
	{
		ItemModel savedModel = null;
		try
		{
			savedModel = modelService.get(pk);
			modelService.refresh(savedModel);
		}
		catch (final ModelLoadingException e)
		{
			//item not found - null be returned, instead of throwing exception
		}
		return savedModel;
	}

	private AbstractCacheUnit[] getCacheUnitForPK(final PK pk)
	{
		final AbstractCacheUnit hjmpCacheUnit = Registry.getCurrentTenant().getCache().getAbstractCacheUnit(Cache.CACHEKEY_HJMP,
				Cache.CACHEKEY_ENTITY, pk.getTypeCodeAsString(), pk);
		final AbstractCacheUnit sldCacheUnit = Registry.getCurrentTenant().getCache()
				.getUnit(new AnonymousCacheUnit(Registry.getCurrentTenant().getCache(), new Object[]
		{ DefaultSLDDataContainerProvider.SLD_CACHE_KEY, pk.getTypeCodeAsString(), pk }));

		return new AbstractCacheUnit[]
		{ hjmpCacheUnit, sldCacheUnit };

	}

	private boolean objectExistsInCache(final AbstractCacheUnit... cacheUnits)
	{
		for (final AbstractCacheUnit cacheUnit : cacheUnits)
		{
			try
			{
				if (cacheUnit != null)
				{
					LOG.info("Checking cache unit is in cache  :" + cacheUnit);
					if (cacheUnit.get() != null)
					{
						return true;
					}
				}
			}
			catch (final Exception e)
			{
				LOG.info("Cache unit can not be calculated :" + e.getMessage());
				return false;
			}
		}
		return false;
	}

}
