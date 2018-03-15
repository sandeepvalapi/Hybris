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


import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.catalog.exceptions.CatalogAwareObjectResolvingException;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.assertj.core.util.Maps;
import org.fest.assertions.Assertions;
import org.junit.Ignore;


@Ignore
public class BaseSynchronizationStatusServiceTest extends ServicelayerBaseTest
{
	@Resource
	protected ModelService modelService;
	@Resource
	protected CatalogTypeService catalogTypeService;
	@Resource
	protected SynchronizationStatusService synchronizationStatusService;
	@Resource
	protected CatalogSynchronizationService catalogSynchronizationService;


	protected <T> T resolveCounterpart(final CatalogVersionModel targetCatalogVersion, final ItemModel itemModel,
			final String qualifier)
	{
		T itemCounterpart = null;
		try
		{
			itemCounterpart = (T) catalogTypeService.getCatalogVersionAwareModel(targetCatalogVersion, itemModel.getItemtype(),
					Maps.newHashMap(qualifier, itemModel.getProperty(qualifier)));
		}
		catch (final CatalogAwareObjectResolvingException e)
		{

		}
		return itemCounterpart;
	}

	protected void performSynchronization(final SyncItemJobModel syncItemJobModel, final List<ItemModel> items,
			final SyncConfig syncConfig)
	{
		catalogSynchronizationService.performSynchronization(items, syncItemJobModel, syncConfig);
	}


	protected void assertSyncInfoEquals(final SyncItemInfo actual, final SyncItemStatus expectedStatus, final PK expectedSyncJobPk,
			final PK expectedPk)
	{

		Assertions.assertThat(actual.getSyncStatus()).isEqualTo(expectedStatus);
		Assertions.assertThat(actual.getSyncJobPk()).isEqualTo(expectedSyncJobPk);
		Assertions.assertThat(actual.getItemPk()).isEqualTo(expectedPk);

	}

	protected SyncConfig prepareSyncConfig()
	{
		final SyncConfig syncConfig = new SyncConfig();
		syncConfig.setCreateSavedValues(Boolean.FALSE);
		syncConfig.setForceUpdate(Boolean.FALSE);
		syncConfig.setLogLevelDatabase(JobLogLevel.WARNING);
		syncConfig.setLogLevelFile(JobLogLevel.WARNING);
		syncConfig.setLogToFile(Boolean.TRUE);
		syncConfig.setLogToDatabase(Boolean.FALSE);
		syncConfig.setSynchronous(Boolean.TRUE);
		return syncConfig;
	}

	protected SyncItemJobModel createSyncJob(final String code, final CatalogVersionModel source, final CatalogVersionModel target){

		final CatalogVersionSyncJobModel job = modelService.create(CatalogVersionSyncJobModel.class);
		job.setCode(code);
		job.setSourceVersion(source);
		job.setTargetVersion(target);
		job.setMaxThreads(getMaxThreads());
		job.setCreateNewItems(true);
		job.setRemoveMissingItems(true);
		return job;
	}

	protected SyncItemJobModel createSyncJob(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		return createSyncJob(RandomStringUtils.randomAlphanumeric(5), source, target);
	}



	protected CatalogModel createCatalog(final String id)
	{

		CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(id);
		return catalog;
	}

	protected CatalogVersionModel createCatalogVersion(final CatalogModel catalog,final String version)
	{
		CatalogVersionModel catalogVersionModel = modelService.create(CatalogVersionModel.class);
		catalogVersionModel.setCatalog(catalog);
		catalogVersionModel.setVersion(version);
		return catalogVersionModel;
	}


	protected ProductModel createProductInCatalogVersion(final String theCode, final CatalogVersionModel catalogVersion)
	{
		ProductModel product = modelService.create(ProductModel.class);
		product.setCode(theCode);
		product.setCatalogVersion(catalogVersion);
		return product;
	}

	protected Integer getMaxThreads()
	{
		return Integer.valueOf(CatalogVersionSyncJob.getDefaultMaxThreads(Registry.getCurrentTenantNoFallback()));
	}


	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	public void setCatalogTypeService(final CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}


	public void setSynchronizationStatusService(final SynchronizationStatusService synchronizationStatusService)
	{
		this.synchronizationStatusService = synchronizationStatusService;
	}

}
