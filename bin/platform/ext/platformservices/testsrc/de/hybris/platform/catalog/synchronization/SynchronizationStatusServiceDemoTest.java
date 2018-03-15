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
package de.hybris.platform.catalog.synchronization;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.collector.GenericItemVisitorForTest;
import de.hybris.platform.servicelayer.model.collector.RelatedItemsCollector;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitorRegistry;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@IntegrationTest
public class SynchronizationStatusServiceDemoTest extends BaseSynchronizationStatusServiceTest
{
	@Resource
	private RelatedItemsCollector relatedItemsCollector;
	@Resource
	@InjectMocks
	private ItemVisitorRegistry itemVisitorRegistry;
	@Resource
	private TypeService typeService;
	@Mock
	private Map<String, ItemVisitor> visitors;

	private CatalogModel catalog;
	private CategoryModel category;
	private UnitModel unitModel;
	private VariantProductModel productVariant;
	private CatalogVersionModel sourceCatalogVersion;
	private CatalogVersionModel targetCatalogVersion;
	private ProductModel product;
	private SyncItemJobModel baseSyncItemJob;


	@Before
	public void setUp()
	{

		MockitoAnnotations.initMocks(this);

		catalog = createCatalog(String.format("%s%s", "test_catalog", RandomStringUtils.randomAlphanumeric(3)));
		sourceCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "test_source_version", RandomStringUtils.randomAlphanumeric(3)));
		targetCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "test_target_version", RandomStringUtils.randomAlphanumeric(3)));
		baseSyncItemJob = createSyncJob(sourceCatalogVersion, targetCatalogVersion);
		product = createProductInCatalogVersion(String.format("%s%s", "test_product_", RandomStringUtils.randomAlphanumeric(3)),
				sourceCatalogVersion);

		category = modelService.create(CategoryModel.class);
		category.setCode("givenCategory");
		category.setCatalogVersion(sourceCatalogVersion);

		unitModel = modelService.create(UnitModel.class);
		unitModel.setUnitType("sampleUnit");
		unitModel.setCode(String.format("%s%s", "test_unit", RandomStringUtils.randomAlphanumeric(3)));
		unitModel.setName("unit");

		product.setSupercategories(Lists.newArrayList(category));
		product.setUnit(unitModel);

		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel.class);
		variantTypeModel.setCode(String.format("%s%s", "MyVariantType", RandomStringUtils.randomAlphanumeric(3)));
		variantTypeModel.setSingleton(false);
		variantTypeModel.setGenerate(true);
		variantTypeModel.setCatalogItemType(true);

		product.setVariantType(variantTypeModel);
		modelService.save(product);


		productVariant = modelService.create(VariantProductModel.class);
		productVariant.setCatalogVersion(sourceCatalogVersion);
		productVariant.setBaseProduct(product);
		productVariant.setCode("variant");

		modelService.saveAll();
	}


	@Test
	public void testWhetherGivenProductIsSynchronizedInSourceCatalogVersion()
	{

		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, category, productVariant), prepareSyncConfig());
		product = modelService.get(product.getPk());
		category = modelService.get(category.getPk());
		productVariant = modelService.get(productVariant.getPk());

		final Map<String, ItemVisitor<? extends ItemModel>> strategyMap = new HashMap<>();
		strategyMap.put("Item", createGenericCrawlerStrategy(Lists.newArrayList("superCategories", "variants")));
		prepareVisitorRegistry(strategyMap);

		final Map<String, Object> context = new HashMap();
		final List<ItemModel> collected = relatedItemsCollector.collect(product, context);

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(collected, baseSyncItemJob);
		boolean inSync = synchronizationStatusService.matchesSyncStatus(collected, Lists.newArrayList(baseSyncItemJob),
				SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(collected.size());
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), category.getPk());
		assertSyncInfoEquals(syncInfos.get(2), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productVariant.getPk());
		Assertions.assertThat(inSync).isTrue();

		// perform changes
		productVariant.setDescription("has been changes");
		modelService.save(productVariant);

		// when
		syncInfos = synchronizationStatusService.getSyncInfo(collected, baseSyncItemJob);
		inSync = synchronizationStatusService.matchesSyncStatus(collected, Lists.newArrayList(baseSyncItemJob), SyncItemStatus.IN_SYNC);


		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(collected.size());
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), category.getPk());
		assertSyncInfoEquals(syncInfos.get(2), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), productVariant.getPk());
		Assertions.assertThat(inSync).isFalse();

	}


	@Test
	public void testWhetherGivenProductIsSynchronizedInTargetCatalogVersion()
	{

		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, category, productVariant), prepareSyncConfig());
		product = modelService.get(product.getPk());
		category = modelService.get(category.getPk());
		productVariant = modelService.get(productVariant.getPk());

		final ProductModel productCouterpart = resolveCounterpart(targetCatalogVersion, product, "code");
		final CategoryModel categoryCouterpart = resolveCounterpart(targetCatalogVersion, category, "code");
		final VariantProductModel variantCouterpart = resolveCounterpart(targetCatalogVersion, productVariant, "code");

		final Map<String, ItemVisitor<? extends ItemModel>> strategyMap = new HashMap<>();
		strategyMap.put("Item", createGenericCrawlerStrategy(Lists.newArrayList("superCategories", "variants")));
		prepareVisitorRegistry(strategyMap);

		final Map<String, Object> context = new HashMap();

		final List<ItemModel> collected = relatedItemsCollector.collect(productCouterpart, context);

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(collected, baseSyncItemJob);
		boolean inSync = synchronizationStatusService.matchesSyncStatus(collected, Lists.newArrayList(baseSyncItemJob),
				SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(collected.size());
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCouterpart.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), categoryCouterpart.getPk());
		assertSyncInfoEquals(syncInfos.get(2), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), variantCouterpart.getPk());
		Assertions.assertThat(inSync).isTrue();

		// perform changes
		productVariant.setDescription("has been changes");
		modelService.save(productVariant);

		// when
		syncInfos = synchronizationStatusService.getSyncInfo(collected, baseSyncItemJob);
		inSync = synchronizationStatusService.matchesSyncStatus(collected, Lists.newArrayList(baseSyncItemJob),
				SyncItemStatus.IN_SYNC);


		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(collected.size());
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCouterpart.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), categoryCouterpart.getPk());
		assertSyncInfoEquals(syncInfos.get(2), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), variantCouterpart.getPk());
		Assertions.assertThat(inSync).isFalse();

	}

	private ItemVisitor<ItemModel> createGenericCrawlerStrategy(final List<String> qualifiers)
	{
		return new GenericItemVisitorForTest(typeService, qualifiers);
	}

	private void prepareVisitorRegistry(final Map<String, ItemVisitor<? extends ItemModel>> visitorRegistry)
	{

		Mockito.when(visitors.keySet()).thenReturn(visitorRegistry.keySet());
		for (final String theKey : visitorRegistry.keySet())
		{
			Mockito.when(visitors.get(theKey)).thenReturn(visitorRegistry.get(theKey));
			if (StringUtils.contains(theKey, "!"))
			{
				Mockito.when(visitors.containsKey(theKey)).thenReturn(true);
			}
		}
	}
}
