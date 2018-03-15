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
 *
 */
package de.hybris.platform.catalog.synchronization;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.JobSearchRestrictionModel;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class CatalogSynchronizationWithRestrictionsTest extends ServicelayerBaseTest
{
	private static final String CATALOG = "testCatalog";
	private static final String SRC_CATALOG_VERSION = "srcCatalog";
	private static final String DST_CATALOG_VERSION = "dstCatalog";
	private static final String PRODUCT1 = "product1";
	private static final String PRODUCT2 = "product2";

	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private TypeService typeService;
	@Resource
	private UserService userService;
	@Resource
	private SearchRestrictionService searchRestrictionService;
	@Resource
	private CatalogSynchronizationService catalogSynchronizationService;

	@Test
	public void shouldSynchronizedProductsThatMeetsRestrictionsWithSessionCondition() throws Exception
	{

		givenTestCatalogWithVersions();

		UserManager.getInstance().createEmployee("syncUser");
		final UserModel syncUser = userService.getUserForUID("syncUser");

		final CatalogVersionSyncJobModel job = modelService.create(CatalogVersionSyncJobModel.class);

		final JobSearchRestrictionModel searchRestriction = modelService.create(JobSearchRestrictionModel.class);
		searchRestriction.setCode("some code");
		searchRestriction.setQuery("{onlineDate} IS NOT NULL AND ?session.user.currentdate >= {onlineDate}");
		searchRestriction.setType(typeService.getComposedTypeForClass(ProductModel.class));
		modelService.save(searchRestriction);

		job.setActive(true);
		job.setCode("mytestjob");
		job.setSourceVersion(catalogVersion(SRC_CATALOG_VERSION));
		job.setTargetVersion(catalogVersion(DST_CATALOG_VERSION));
		job.setRestrictions(Collections.singletonList(searchRestriction));
		job.setSessionUser(syncUser);
		modelService.save(job);

		searchRestrictionService.enableSearchRestrictions();

		assertThat(allProductsFor(catalogVersion(DST_CATALOG_VERSION))).isEmpty();


		final SyncResult synchronize = catalogSynchronizationService.synchronize(job, createSyncConfig());

		assertThat(synchronize.isFinished()).isTrue();
		assertThat(synchronize.isError()).isFalse();

		assertThat(allProductsFor(catalogVersion(DST_CATALOG_VERSION))).isNotNull().hasSize(1);
		assertThat(productFrom(PRODUCT1, catalogVersion(DST_CATALOG_VERSION))).isNotNull();
		assertThat(catchThrowable(() -> productFrom(PRODUCT2, catalogVersion(DST_CATALOG_VERSION)))).isInstanceOf(ModelNotFoundException.class);
	}


	private void givenTestCatalogWithVersions()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(CATALOG);

		final CatalogVersionModel sourceVersion = modelService.create(CatalogVersionModel.class);
		sourceVersion.setCatalog(catalog);
		sourceVersion.setVersion(SRC_CATALOG_VERSION);

		final CatalogVersionModel targetVersion = modelService.create(CatalogVersionModel.class);
		targetVersion.setCatalog(catalog);
		targetVersion.setVersion(DST_CATALOG_VERSION);

		final ProductModel p1 = modelService.create(ProductModel.class);
		p1.setCatalogVersion(sourceVersion);
		p1.setCode(PRODUCT1);
		p1.setOnlineDate(Date.from(Instant.now().minus(Duration.ofDays(1))));

		final ProductModel p2 = modelService.create(ProductModel.class);
		p2.setCatalogVersion(sourceVersion);
		p2.setCode(PRODUCT2);
		p2.setOnlineDate(Date.from(Instant.now().plus(Duration.ofDays(1))));

		modelService.saveAll();
	}

	private CatalogModel testCatalog()
	{
		final CatalogModel example = new CatalogModel();
		example.setId(CATALOG);
		return flexibleSearchService.getModelByExample(example);
	}

	private CatalogVersionModel catalogVersion(final String version)
	{
		final CatalogVersionModel example = new CatalogVersionModel();
		example.setCatalog(testCatalog());
		example.setVersion(version);
		return flexibleSearchService.getModelByExample(example);
	}

	private Collection<ProductModel> allProductsFor(final CatalogVersionModel catalogVersion)
	{
		final ProductModel example = new ProductModel();
		example.setCatalogVersion(catalogVersion);
		return flexibleSearchService.getModelsByExample(example);
	}

	private ProductModel productFrom(final String productCode, final CatalogVersionModel catalogVersion)
	{
		final ProductModel example = new ProductModel();
		example.setCatalogVersion(catalogVersion);
		example.setCode(productCode);
		return flexibleSearchService.getModelByExample(example);
	}


	private SyncConfig createSyncConfig()
	{
		final SyncConfig syncConfig = new SyncConfig();
		syncConfig.setCreateSavedValues(Boolean.TRUE);
		syncConfig.setForceUpdate(Boolean.TRUE);
		syncConfig.setLogLevelDatabase(JobLogLevel.WARNING);
		syncConfig.setLogLevelFile(JobLogLevel.WARNING);
		syncConfig.setLogToFile(Boolean.TRUE);
		syncConfig.setLogToDatabase(Boolean.FALSE);
		syncConfig.setSynchronous(true);
		return syncConfig;
	}
}
