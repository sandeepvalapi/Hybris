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
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


@IntegrationTest
public class DefaultSynchronizationStatusServiceTest extends BaseSynchronizationStatusServiceTest
{
	@Resource
	private TypeService typeService;
	@Resource
	private UserService userService;

	private CatalogModel catalog;
	private CatalogVersionModel sourceCatalogVersion;
	private CatalogVersionModel targetCatalogVersion;
	private ProductModel product;
	private SyncItemJobModel baseSyncItemJob;


	@Before
	public void setUp()
	{

		catalog = createCatalog(String.format("%s%s", "test_catalog", RandomStringUtils.randomAlphanumeric(3)));
		sourceCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "test_source_version", RandomStringUtils.randomAlphanumeric(3)));
		targetCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "test_target_version", RandomStringUtils.randomAlphanumeric(3)));
		baseSyncItemJob = createSyncJob(sourceCatalogVersion, targetCatalogVersion);
		product = createProductInCatalogVersion(String.format("%s%s", "test_product_", RandomStringUtils.randomAlphanumeric(3)),
				sourceCatalogVersion);

		modelService.saveAll();
	}

	@Test
	public void testOutboundSynchronizations()
	{
		// when
		Collection<SyncItemJobModel> outboundSynchronization = synchronizationStatusService.getOutboundSynchronizations(product);


		// then
		Assertions.assertThat(outboundSynchronization).hasSize(1);
		Assertions.assertThat(outboundSynchronization).contains(baseSyncItemJob);
		Assertions.assertThat(synchronizationStatusService.getInboundSynchronizations(product)).isEmpty();

		// given
		final CatalogVersionModel additionalCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "additional_source_version", RandomStringUtils.randomAlphanumeric(3)));

		final SyncItemJobModel additionalSyncJob = createSyncJob(sourceCatalogVersion, additionalCatalogVersion);
		sourceCatalogVersion.setSynchronizations(Lists.newArrayList(baseSyncItemJob, additionalSyncJob));

		modelService.saveAll();
		// when

		outboundSynchronization = synchronizationStatusService.getOutboundSynchronizations(product);

		// then
		Assertions.assertThat(outboundSynchronization).hasSize(2);
		Assertions.assertThat(outboundSynchronization).contains(baseSyncItemJob, additionalSyncJob);
		Assertions.assertThat(synchronizationStatusService.getInboundSynchronizations(product)).isEmpty();
	}

	@Test
	public void testInboundSynchronizations()
	{
		// given

		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");

		Collection<SyncItemJobModel> inboundSynchronizations = synchronizationStatusService
				.getInboundSynchronizations(productCounterpart);

		Assertions.assertThat(inboundSynchronizations).hasSize(1);
		Assertions.assertThat(inboundSynchronizations).contains(baseSyncItemJob);

		Assertions.assertThat(synchronizationStatusService.getOutboundSynchronizations(productCounterpart)).isEmpty();


		final CatalogVersionModel additionalCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "additional_source_version", RandomStringUtils.randomAlphanumeric(3)));
		modelService.save(additionalCatalogVersion);


		final SyncItemJobModel additionalSyncJob = createSyncJob(sourceCatalogVersion, additionalCatalogVersion);
		modelService.save(additionalSyncJob);

		performSynchronization(additionalSyncJob, Lists.newArrayList(product), prepareSyncConfig());

		productCounterpart = resolveCounterpart(additionalCatalogVersion, product, "code");

		inboundSynchronizations = synchronizationStatusService.getInboundSynchronizations(productCounterpart);

		Assertions.assertThat(inboundSynchronizations).hasSize(1);
		Assertions.assertThat(inboundSynchronizations).contains(additionalSyncJob);

		Assertions.assertThat(synchronizationStatusService.getOutboundSynchronizations(productCounterpart)).isEmpty();

	}

	@Test
	public void testGetSyncStatusWhenTypeNotMatchConfiguredRootTypes()
	{
		// given
		baseSyncItemJob.setRootTypes(Lists.newArrayList(typeService.getComposedTypeForCode("Category")));
		modelService.saveAll(baseSyncItemJob);

		// when
		final List<SyncItemJobModel> outboundSynchronizations = synchronizationStatusService.getOutboundSynchronizations(product);
		final List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(product, outboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		final SyncItemInfo syncInfo = syncInfos.iterator().next();
		assertSyncInfoEquals(syncInfo, SyncItemStatus.NOT_APPLICABLE, baseSyncItemJob.getPk(), product.getPk());

	}

	@Test
	public void testGetSyncStatusWhenUserDoesNotHaveSufficientRights()
	{
		// given
		final UserModel sampleUser = modelService.create(UserModel.class);
		sampleUser.setUid(String.format("%s%s", "sample", RandomStringUtils.randomAlphanumeric(3)));
		modelService.save(sampleUser);

		userService.setCurrentUser(sampleUser);

		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());

		// when
		final List<SyncItemJobModel> outboundSynchronizations = synchronizationStatusService.getOutboundSynchronizations(product);
		final List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(product, outboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		final SyncItemInfo syncInfo = syncInfos.iterator().next();
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());

	}


	@Test
	public void testGetPullSyncStatusWhenUserDoesNotHaveSufficientRights()
	{
		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());

		final ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");

		final UserModel sampleUser = modelService.create(UserModel.class);
		sampleUser.setUid(String.format("%s%s", "sample", RandomStringUtils.randomAlphanumeric(3)));
		modelService.save(sampleUser);
		userService.setCurrentUser(sampleUser);

		// when
		final List<SyncItemJobModel> inboundSynchronizations = synchronizationStatusService
				.getInboundSynchronizations(productCounterpart);
		final List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart,
				inboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		final SyncItemInfo syncInfo = syncInfos.iterator().next();
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());

	}


	@Test
	public void testGetSyncStatusForNonCatalogAwareItem()
	{

		// given
		final UserModel localUser = modelService.create(UserModel.class);
		localUser.setUid(String.format("%s%s", "localUser", RandomStringUtils.randomAlphanumeric(3)));

		final CatalogModel localCatalog = modelService.create(CatalogModel.class);
		localCatalog.setId(String.format("%s%s", "localCatalog", RandomStringUtils.randomAlphanumeric(3)));
		final CatalogVersionModel localCatalogVersion = modelService.create(CatalogVersionModel.class);
		localCatalogVersion.setVersion(String.format("%s%s", "localVersion", RandomStringUtils.randomAlphanumeric(3)));
		localCatalogVersion.setCatalog(localCatalog);

		modelService.saveAll();

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(localUser,
				Collections.singletonList(baseSyncItemJob));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		SyncItemInfo syncInfo = syncInfos.iterator().next();
		assertSyncInfoEquals(syncInfo, SyncItemStatus.NOT_APPLICABLE, baseSyncItemJob.getPk(), localUser.getPk());

		// when
		syncInfos = synchronizationStatusService.getSyncInfo(localUser, Collections.singletonList(baseSyncItemJob));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.iterator().next();
		assertSyncInfoEquals(syncInfo, SyncItemStatus.NOT_APPLICABLE, baseSyncItemJob.getPk(), localUser.getPk());

	}


	@Test
	public void testGetPullSyncStatusForNonCatalogAwareItem()
	{

		// given
		final UserModel localUser = modelService.create(UserModel.class);
		localUser.setUid(String.format("%s%s", "localUser", RandomStringUtils.randomAlphanumeric(3)));

		final CatalogModel localCatalog = modelService.create(CatalogModel.class);
		localCatalog.setId(String.format("%s%s", "localCatalog", RandomStringUtils.randomAlphanumeric(3)));
		final CatalogVersionModel localCatalogVersion = modelService.create(CatalogVersionModel.class);
		localCatalogVersion.setVersion(String.format("%s%s", "localVersion", RandomStringUtils.randomAlphanumeric(3)));
		localCatalogVersion.setCatalog(localCatalog);


		modelService.saveAll();

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(localUser,
				Collections.singletonList(baseSyncItemJob));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		SyncItemInfo syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.NOT_APPLICABLE, baseSyncItemJob.getPk(), localUser.getPk());

		// when
		syncInfos = synchronizationStatusService.getSyncInfo(localUser, Collections.singletonList(baseSyncItemJob));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.NOT_APPLICABLE, baseSyncItemJob.getPk(), localUser.getPk());
	}


	@Test
	public void testGetSyncStatusCatalogAwareItem()
	{
		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(product,
				synchronizationStatusService.getOutboundSynchronizations(product));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		SyncItemInfo syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());


		// some source item modification
		product.setEan("has been changed");
		modelService.save(product);

		syncInfos = synchronizationStatusService.getSyncInfo(product,
				synchronizationStatusService.getOutboundSynchronizations(product));

		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), product.getPk());

	}


	@Test
	public void testGetSyncStatusCatalogAwareItemWhenNewSourceItemCreated()
	{
		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(product,
				synchronizationStatusService.getOutboundSynchronizations(product));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		SyncItemInfo syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());

		final ProductModel newlyCreatedProduct = createProductInCatalogVersion(
				String.format("%s%s", "newly_created_test_product", RandomStringUtils.randomAlphanumeric(3)), sourceCatalogVersion);
		modelService.save(newlyCreatedProduct);

		syncInfos = synchronizationStatusService.getSyncInfo(newlyCreatedProduct,
				synchronizationStatusService.getOutboundSynchronizations(newlyCreatedProduct));

		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(), newlyCreatedProduct.getPk());


		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(newlyCreatedProduct), prepareSyncConfig());
		// when
		syncInfos = synchronizationStatusService.getSyncInfo(newlyCreatedProduct,
				synchronizationStatusService.getOutboundSynchronizations(newlyCreatedProduct));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), newlyCreatedProduct.getPk());
	}


	@Test
	public void testGetPullSyncStatusCatalogAwareItemWhenNewTargetItemCreated()
	{
		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());

		final ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart,
				synchronizationStatusService.getInboundSynchronizations(productCounterpart));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		SyncItemInfo syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());

		final ProductModel newlyCreatedProduct = createProductInCatalogVersion(
				String.format("%s%s", "newly_created_test_product", RandomStringUtils.randomAlphanumeric(3)), targetCatalogVersion);
		modelService.save(newlyCreatedProduct);

		syncInfos = synchronizationStatusService.getSyncInfo(newlyCreatedProduct,
				synchronizationStatusService.getInboundSynchronizations(newlyCreatedProduct));

		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(), newlyCreatedProduct.getPk());

	}


	@Test
	public void testGetSyncStatusCatalogAwareItemWhenSourceItemWasRemoved()
	{
		// given
		final ProductModel newlyCreatedProduct = createProductInCatalogVersion(
				String.format("%s%s", "newly_created_product", RandomStringUtils.randomAlphanumeric(3)), sourceCatalogVersion);
		modelService.save(newlyCreatedProduct);

		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, newlyCreatedProduct), prepareSyncConfig());

		final ProductModel productModelCounterpart = resolveCounterpart(targetCatalogVersion, newlyCreatedProduct, "code");

		final List<SyncItemJobModel> outboundSynchronizations = synchronizationStatusService
				.getOutboundSynchronizations(newlyCreatedProduct);
		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(newlyCreatedProduct, outboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		SyncItemInfo syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), newlyCreatedProduct.getPk());

		// when
		modelService.remove(newlyCreatedProduct);
		syncInfos = synchronizationStatusService.getSyncInfo(newlyCreatedProduct, outboundSynchronizations);
		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.ITEM_MISSING, baseSyncItemJob.getPk(), newlyCreatedProduct.getPk());

		// when
		syncInfos = synchronizationStatusService.getSyncInfo(productModelCounterpart,
				synchronizationStatusService.getInboundSynchronizations(productModelCounterpart));
		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(),
				productModelCounterpart.getPk());

	}


	@Test
	public void testGetSyncStatusCatalogAwareItemWhenTargetItemWasRemoved()
	{
		// given
		final ProductModel newlyCreatedProduct = createProductInCatalogVersion(
				String.format("%s%s", "newly_created_product", RandomStringUtils.randomAlphanumeric(3)), sourceCatalogVersion);
		modelService.save(newlyCreatedProduct);


		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, newlyCreatedProduct), prepareSyncConfig());
		final ProductModel productModelCounterpart = resolveCounterpart(targetCatalogVersion, newlyCreatedProduct, "code");

		final List<SyncItemJobModel> inboundSynchronizations = synchronizationStatusService
				.getInboundSynchronizations(productModelCounterpart);

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(productModelCounterpart,
				inboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		SyncItemInfo syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productModelCounterpart.getPk());


		modelService.remove(productModelCounterpart);

		syncInfos = synchronizationStatusService.getSyncInfo(productModelCounterpart, inboundSynchronizations);

		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.ITEM_MISSING, baseSyncItemJob.getPk(), productModelCounterpart.getPk());


		syncInfos = synchronizationStatusService.getSyncInfo(newlyCreatedProduct,
				synchronizationStatusService.getOutboundSynchronizations(newlyCreatedProduct));

		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.get(0);
		assertSyncInfoEquals(syncInfo, SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(), newlyCreatedProduct.getPk());
	}


	@Test
	public void testGetPullSyncStatusForCatalogAwareItem()
	{
		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		product = modelService.get(product.getPk());

		final ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");
		final List<SyncItemJobModel> inboundSynchronizations = synchronizationStatusService
				.getInboundSynchronizations(productCounterpart);

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart, inboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		SyncItemInfo syncInfo = syncInfos.iterator().next();
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());


		// some source item modification
		product.setEan("has been changed");
		modelService.save(product);

		syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart, inboundSynchronizations);

		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		syncInfo = syncInfos.iterator().next();
		assertSyncInfoEquals(syncInfo, SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());

	}

	@Test
	public void testGetSyncStatusCatalogAwareItemForMultipleTargets()
	{
		final CatalogVersionModel additionalCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "additional_target_version", RandomStringUtils.randomAlphanumeric(3)));
		final SyncItemJobModel additionalSyncJob = createSyncJob(sourceCatalogVersion, additionalCatalogVersion);

		modelService.saveAll();

		// perform initial synchronization against two sync item jobs
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		performSynchronization(additionalSyncJob, Lists.newArrayList(product), prepareSyncConfig());


		final List<SyncItemJobModel> outboundSynchronizations = synchronizationStatusService.getOutboundSynchronizations(product);

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(product, outboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, additionalSyncJob.getPk(), product.getPk());


		// some source item modification
		product.setEan("has been changed");
		modelService.save(product);

		// check sync status again
		syncInfos = synchronizationStatusService.getSyncInfo(product, outboundSynchronizations);

		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.NOT_SYNC, additionalSyncJob.getPk(), product.getPk());


		// perform synchronization only against baseSyncItemJob
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());

		// when
		syncInfos = synchronizationStatusService.getSyncInfo(product, Collections.singletonList(baseSyncItemJob));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());


		// when
		syncInfos = synchronizationStatusService.getSyncInfo(product, Collections.singletonList(additionalSyncJob));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.NOT_SYNC, additionalSyncJob.getPk(), product.getPk());
	}


	@Test
	public void testGetPullSyncStatusCatalogAwareItemForMultipleTargets()
	{
		// given
		final CatalogVersionModel additionalCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "additional_target_version", RandomStringUtils.randomAlphanumeric(3)));
		final SyncItemJobModel additionalSyncJob = createSyncJob(sourceCatalogVersion, additionalCatalogVersion);
		modelService.saveAll();

		// perform initial synchronization against two sync item jobs
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		performSynchronization(additionalSyncJob, Lists.newArrayList(product), prepareSyncConfig());
		product = modelService.get(product.getPk());

		final ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");


		final List<SyncItemJobModel> inboundSynchronizations = synchronizationStatusService
				.getInboundSynchronizations(productCounterpart);

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart, inboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());

		// some source item modification
		product.setEan("has been changed");
		modelService.save(product);

		// check sync status again
		syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart, inboundSynchronizations);

		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());

		// perform synchronization only against baseSyncItemJob
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());

		// when
		syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart, inboundSynchronizations);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());


		// perform synchronization only against additionalSyncJob
		performSynchronization(additionalSyncJob, Lists.newArrayList(product), prepareSyncConfig());

		// when
		syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart, Collections.singletonList(additionalSyncJob));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.NOT_APPLICABLE, additionalSyncJob.getPk(),
				productCounterpart.getPk());
	}


	@Test
	public void testGetPullSyncStatusCatalogAwareItemForMultipleSources()
	{
		// given

		final CatalogVersionModel additionalCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "additional_source_version_2", RandomStringUtils.randomAlphanumeric(3)));
		final SyncItemJobModel additionalSyncJob = createSyncJob(additionalCatalogVersion, targetCatalogVersion);

		ProductModel additionalProduct = createProductInCatalogVersion("test_product_14", additionalCatalogVersion);
		baseSyncItemJob.setRemoveMissingItems(false);
		additionalSyncJob.setRemoveMissingItems(false);
		modelService.saveAll();

		// perform initial synchronization against two sync item jobs
		performSynchronization(additionalSyncJob, Lists.newArrayList(additionalProduct), prepareSyncConfig());
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		additionalProduct = modelService.get(additionalProduct.getPk());
		product = modelService.get(product.getPk());



		final ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");
		final ProductModel additionalProductCounterpart = resolveCounterpart(targetCatalogVersion, additionalProduct, "code");


		final List<SyncItemJobModel> inboundSynchronizations = synchronizationStatusService
				.getInboundSynchronizations(productCounterpart);
		final List<SyncItemJobModel> additionalInboundSynchronizations = synchronizationStatusService
				.getInboundSynchronizations(additionalProductCounterpart);

		// when
		List<SyncItemInfo> productSyncInfos = synchronizationStatusService.getSyncInfo(productCounterpart,
				inboundSynchronizations);
		List<SyncItemInfo> additionalSyncInfos = synchronizationStatusService.getSyncInfo(additionalProductCounterpart,
				additionalInboundSynchronizations);

		// then
		Assertions.assertThat(productSyncInfos).isNotNull().hasSize(2);
		assertSyncInfoEquals(productSyncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());
		assertSyncInfoEquals(productSyncInfos.get(1), SyncItemStatus.COUNTERPART_MISSING, additionalSyncJob.getPk(),
				productCounterpart.getPk());

		Assertions.assertThat(additionalSyncInfos).isNotNull().hasSize(2);
		assertSyncInfoEquals(additionalSyncInfos.get(0), SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(),
				additionalProductCounterpart.getPk());
		assertSyncInfoEquals(additionalSyncInfos.get(1), SyncItemStatus.IN_SYNC, additionalSyncJob.getPk(),
				additionalProductCounterpart.getPk());

		// given
		// some source item modification
		product.setEan("has been changed");
		modelService.save(product);

		// when
		productSyncInfos = synchronizationStatusService.getSyncInfo(productCounterpart,
				Collections.singletonList(baseSyncItemJob));
		additionalSyncInfos = synchronizationStatusService.getSyncInfo(additionalProductCounterpart,
				Collections.singletonList(additionalSyncJob));

		// then
		Assertions.assertThat(productSyncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(productSyncInfos.get(0), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());
		Assertions.assertThat(additionalSyncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(additionalSyncInfos.get(0), SyncItemStatus.IN_SYNC, additionalSyncJob.getPk(),
				additionalProductCounterpart.getPk());


		// given
		additionalProduct.setEan("has been changed");
		modelService.save(additionalProduct);

		// when
		productSyncInfos = synchronizationStatusService.getSyncInfo(productCounterpart,
				Collections.singletonList(baseSyncItemJob));
		additionalSyncInfos = synchronizationStatusService.getSyncInfo(additionalProductCounterpart,
				Collections.singletonList(additionalSyncJob));

		// then
		Assertions.assertThat(productSyncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(productSyncInfos.get(0), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());
		Assertions.assertThat(additionalSyncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(additionalSyncInfos.get(0), SyncItemStatus.NOT_SYNC, additionalSyncJob.getPk(),
				additionalProductCounterpart.getPk());

	}


	@Test
	public void testGetPullSyncStatusCatalogAwareItemWhenSourceWasRemoved()
	{

		// given
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		final ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart,
				synchronizationStatusService.getInboundSynchronizations(productCounterpart));


		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());

		// when
		modelService.remove(product);
		syncInfos = synchronizationStatusService.getSyncInfo(productCounterpart,
				synchronizationStatusService.getInboundSynchronizations(productCounterpart));

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(1);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(),
				productCounterpart.getPk());

	}

	@Test
	public void testGetSyncStatusForManyItems()
	{
		// given
		final ProductModel additionalProduct = createProductInCatalogVersion(
				String.format("%s%s", "additional_test_product", RandomStringUtils.randomAlphanumeric(3)), sourceCatalogVersion);
		modelService.save(additionalProduct);

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService.getSyncInfo(Lists.newArrayList(product, additionalProduct),
				baseSyncItemJob);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(),
				additionalProduct.getPk());

		// when
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, additionalProduct), prepareSyncConfig());
		syncInfos = synchronizationStatusService.getSyncInfo(Lists.newArrayList(product, additionalProduct), baseSyncItemJob);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), additionalProduct.getPk());

		// when
		product.setEan("has been changed");
		modelService.save(product);

		syncInfos = synchronizationStatusService.getSyncInfo(Lists.newArrayList(product, additionalProduct), baseSyncItemJob);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), additionalProduct.getPk());

		// when
		additionalProduct.setEan("has been changed");
		modelService.save(additionalProduct);


		syncInfos = synchronizationStatusService.getSyncInfo(Lists.newArrayList(product, additionalProduct), baseSyncItemJob);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), additionalProduct.getPk());
	}


	@Test
	public void testGetPullSyncStatusForManyItems() throws InterruptedException
	{
		// given
		ProductModel additionalProduct = createProductInCatalogVersion(
				String.format("%s%s", "additional_test_product", RandomStringUtils.randomAlphanumeric(3)), sourceCatalogVersion);
		modelService.save(additionalProduct);

		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, additionalProduct), prepareSyncConfig());
		product = modelService.get(product.getPk());
		additionalProduct = modelService.get(additionalProduct.getPk());

		final ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");
		final ProductModel additionalProductCounterpart = resolveCounterpart(targetCatalogVersion, additionalProduct, "code");

		// when
		List<SyncItemInfo> syncInfos = synchronizationStatusService
				.getSyncInfo(Lists.newArrayList(productCounterpart, additionalProductCounterpart), baseSyncItemJob);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(),
				additionalProductCounterpart.getPk());

		// when
		product.setEan("has been changed");
		modelService.save(product);

		syncInfos = synchronizationStatusService
				.getSyncInfo(Lists.newArrayList(productCounterpart, additionalProductCounterpart), baseSyncItemJob);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(),
				additionalProductCounterpart.getPk());

		// when
		additionalProduct.setEan("has been changed");
		modelService.save(additionalProduct);


		syncInfos = synchronizationStatusService
				.getSyncInfo(Lists.newArrayList(productCounterpart, additionalProductCounterpart), baseSyncItemJob);

		// then
		Assertions.assertThat(syncInfos).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfos.get(0), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(), productCounterpart.getPk());
		assertSyncInfoEquals(syncInfos.get(1), SyncItemStatus.NOT_SYNC, baseSyncItemJob.getPk(),
				additionalProductCounterpart.getPk());
	}


	@Test
	public void testWhetherGivenItemListAreInSync()
	{

		// given
		final CategoryModel theCategory = modelService.create(CategoryModel.class);
		theCategory.setCode("theCategory");
		theCategory.setCatalogVersion(sourceCatalogVersion);
		theCategory.setProducts(Lists.newArrayList(product));

		modelService.save(theCategory);


		final List<SyncItemJobModel> outboundSynchronizations = synchronizationStatusService.getOutboundSynchronizations(product);
		// when
		boolean matchesInSync = synchronizationStatusService.matchesSyncStatus(Lists.newArrayList(product, theCategory),
				outboundSynchronizations, SyncItemStatus.IN_SYNC);
		final boolean matchesInitialNeeded = synchronizationStatusService.matchesSyncStatus(
				Lists.newArrayList(product, theCategory), outboundSynchronizations, SyncItemStatus.COUNTERPART_MISSING);


		// then
		Assertions.assertThat(matchesInSync).isFalse();
		Assertions.assertThat(matchesInitialNeeded).isTrue();


		// when
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, theCategory), prepareSyncConfig());
		matchesInSync = synchronizationStatusService.matchesSyncStatus(Lists.newArrayList(theCategory, product),
				outboundSynchronizations, SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(matchesInSync).isTrue();

		product.setEan("has been changed");
		modelService.save(product);

		matchesInSync = synchronizationStatusService.matchesSyncStatus(Lists.newArrayList(product, theCategory),
				outboundSynchronizations, SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(matchesInSync).isFalse();

		// when
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, theCategory), prepareSyncConfig());
		matchesInSync = synchronizationStatusService.matchesSyncStatus(Lists.newArrayList(product, theCategory),
				outboundSynchronizations, SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(matchesInSync).isTrue();

		theCategory.setDescription("has been changed");
		modelService.save(theCategory);
		matchesInSync = synchronizationStatusService.matchesSyncStatus(Lists.newArrayList(product, theCategory),
				outboundSynchronizations, SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(matchesInSync).isFalse();

	}


	@Test
	public void testWhetherGivenItemListAreInSyncTargetEnd()
	{

		// given
		CategoryModel theCategory = modelService.create(CategoryModel.class);
		theCategory.setCode("theCategory");
		theCategory.setCatalogVersion(sourceCatalogVersion);
		theCategory.setProducts(Lists.newArrayList(product));

		modelService.save(theCategory);
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, theCategory), prepareSyncConfig());
		product = modelService.get(product.getPk());
		theCategory = modelService.get(theCategory.getPk());


		final ProductModel productCounterpart = resolveCounterpart(targetCatalogVersion, product, "code");
		final CategoryModel categoryCounterpart = resolveCounterpart(targetCatalogVersion, theCategory, "code");


		final List<SyncItemJobModel> inboundSynchronizations = synchronizationStatusService
				.getInboundSynchronizations(productCounterpart);
		// when
		boolean matchesInSync = synchronizationStatusService.matchesSyncStatus(
				Lists.newArrayList(productCounterpart, categoryCounterpart), inboundSynchronizations, SyncItemStatus.IN_SYNC);
		final boolean matchesCounterpartMissing = synchronizationStatusService.matchesSyncStatus(
				Lists.newArrayList(productCounterpart, theCategory), inboundSynchronizations, SyncItemStatus.COUNTERPART_MISSING);


		// then
		Assertions.assertThat(matchesInSync).isTrue();
		Assertions.assertThat(matchesCounterpartMissing).isFalse();


		product.setEan("has been changed");
		modelService.save(product);

		matchesInSync = synchronizationStatusService.matchesSyncStatus(
				Lists.newArrayList(productCounterpart, categoryCounterpart), inboundSynchronizations, SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(matchesInSync).isFalse();

		// when
		performSynchronization(baseSyncItemJob, Lists.newArrayList(product, theCategory), prepareSyncConfig());
		product = modelService.get(product.getPk());
		theCategory = modelService.get(theCategory.getPk());

		matchesInSync = synchronizationStatusService.matchesSyncStatus(
				Lists.newArrayList(productCounterpart, categoryCounterpart), inboundSynchronizations, SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(matchesInSync).isTrue();

		theCategory.setDescription("has been changed");
		modelService.save(theCategory);
		matchesInSync = synchronizationStatusService.matchesSyncStatus(
				Lists.newArrayList(productCounterpart, categoryCounterpart), inboundSynchronizations, SyncItemStatus.IN_SYNC);

		// then
		Assertions.assertThat(matchesInSync).isFalse();

	}

	@Test
	public void testGetSyncStatusForTwoSourcesOneTargetAndDifferentRootTypes()
	{


		baseSyncItemJob.setRootTypes(Lists.newArrayList(typeService.getComposedTypeForCode("Product")));


		final CatalogVersionModel additionalCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "additional_source_version", RandomStringUtils.randomAlphanumeric(3)));


		final SyncItemJobModel additionalSyncJob = createSyncJob(additionalCatalogVersion, targetCatalogVersion);
		additionalSyncJob.setRootTypes(Lists.newArrayList(typeService.getComposedTypeForCode("Media")));

		final MediaModel thumbnail = modelService.create(MediaModel.class);
		thumbnail.setCode("thumbnail");
		thumbnail.setCatalogVersion(additionalCatalogVersion);

		modelService.saveAll();

		performSynchronization(baseSyncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		performSynchronization(additionalSyncJob, Lists.newArrayList(thumbnail), prepareSyncConfig());


		final boolean productInSync = synchronizationStatusService.matchesSyncStatus(Lists.newArrayList(product),
				Lists.newArrayList(baseSyncItemJob), SyncItemStatus.IN_SYNC);
		final boolean mediaInSync = synchronizationStatusService.matchesSyncStatus(Lists.newArrayList(thumbnail),
				Lists.newArrayList(additionalSyncJob), SyncItemStatus.IN_SYNC);

		Assertions.assertThat(productInSync).isTrue();
		Assertions.assertThat(mediaInSync).isTrue();


		// when
		final List<SyncItemInfo> syncInfosAgainstBaseSyncJob = synchronizationStatusService
				.getSyncInfo(Lists.newArrayList(product, thumbnail), baseSyncItemJob);
		final List<SyncItemInfo> syncInfosAgainstAdditionalSyncJob = synchronizationStatusService
				.getSyncInfo(Lists.newArrayList(product, thumbnail), additionalSyncJob);


		// then
		Assertions.assertThat(syncInfosAgainstBaseSyncJob).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfosAgainstBaseSyncJob.get(0), SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(syncInfosAgainstBaseSyncJob.get(1), SyncItemStatus.NOT_APPLICABLE, baseSyncItemJob.getPk(),
				thumbnail.getPk());

		Assertions.assertThat(syncInfosAgainstAdditionalSyncJob).isNotEmpty().hasSize(2);
		assertSyncInfoEquals(syncInfosAgainstAdditionalSyncJob.get(0), SyncItemStatus.NOT_APPLICABLE, additionalSyncJob.getPk(),
				product.getPk());
		assertSyncInfoEquals(syncInfosAgainstAdditionalSyncJob.get(1), SyncItemStatus.IN_SYNC, additionalSyncJob.getPk(),
				thumbnail.getPk());



	}

	@Test
	public void testWhetherGivenItemsAreSync()
	{

		// given
		final ProductModel additionalProduct = createProductInCatalogVersion(
				String.format("%s%s", "additional_test_product", RandomStringUtils.randomAlphanumeric(3)), sourceCatalogVersion);
		modelService.save(additionalProduct);


		final ProductModel extraProduct = createProductInCatalogVersion(
				String.format("%s%s", "additional_test_product", RandomStringUtils.randomAlphanumeric(3)), sourceCatalogVersion);
		modelService.save(extraProduct);


		// when
		performSynchronization(baseSyncItemJob, Lists.newArrayList(additionalProduct), prepareSyncConfig());

		SyncItemInfo additionalProductSyncInfo = synchronizationStatusService.getSyncInfo(additionalProduct, baseSyncItemJob);
		SyncItemInfo extraProductSyncInfo = synchronizationStatusService.getSyncInfo(extraProduct, baseSyncItemJob);

		// then
		assertSyncInfoEquals(additionalProductSyncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), additionalProduct.getPk());
		assertSyncInfoEquals(extraProductSyncInfo, SyncItemStatus.COUNTERPART_MISSING, baseSyncItemJob.getPk(),
				extraProduct.getPk());

		// when
		performSynchronization(baseSyncItemJob, Lists.newArrayList(extraProduct), prepareSyncConfig());


		additionalProductSyncInfo = synchronizationStatusService.getSyncInfo(additionalProduct, baseSyncItemJob);
		extraProductSyncInfo = synchronizationStatusService.getSyncInfo(extraProduct, baseSyncItemJob);

		// then
		assertSyncInfoEquals(additionalProductSyncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), additionalProduct.getPk());
		assertSyncInfoEquals(extraProductSyncInfo, SyncItemStatus.IN_SYNC, baseSyncItemJob.getPk(), extraProduct.getPk());

	}







}
