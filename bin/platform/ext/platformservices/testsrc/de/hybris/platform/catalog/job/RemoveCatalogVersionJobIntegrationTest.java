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
package de.hybris.platform.catalog.job;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class RemoveCatalogVersionJobIntegrationTest extends AbstractJobIntegrationTest
{

	@Resource
	private MediaService mediaService;

	@Resource
	private ModelService modelService;

	@Before
	public void prepare()
	{
		setUp();

		//
		mainCatalog.setCatalogVersions(new HashSet<CatalogVersionModel>(Arrays.asList(source)));
		modelService.save(mainCatalog);

		try
		{
			mediaService.getRootFolder();
		}
		catch (final ModelNotFoundException mnfe)
		{
			final MediaFolderModel newFolder = modelService.create(MediaFolderModel.class);
			newFolder.setQualifier("root");
			modelService.save(newFolder);
		}

	}

	private RemoveCatalogVersionCronJobModel createCronJob(final CatalogVersionModel catalogVersion, final CatalogModel catalog)
	{
		//final RemoveCatalogVersionJobModel jobModel = modelService.create(RemoveCatalogVersionJobModel.class);
		final ServicelayerJobModel jobModel = modelService.create(ServicelayerJobModel.class);
		jobModel.setCode("compareMightyCatalogsTestJob_" + System.currentTimeMillis());
		jobModel.setSpringId("removeCatalogVersionJobPerformable");

		modelService.save(jobModel);

		final RemoveCatalogVersionCronJobModel cronJobModel = modelService.create(RemoveCatalogVersionCronJobModel.class);
		cronJobModel.setJob(jobModel);
		cronJobModel.setCode("compareMightyCatalogsTestCronJob");
		cronJobModel.setCatalog(catalog);
		cronJobModel.setCatalogVersion(catalogVersion);

		modelService.save(cronJobModel);
		return cronJobModel;
	}


	@Test
	public void testRemoveEmptyCatalog()
	{
		final RemoveCatalogVersionCronJobModel cronJobModel = createCronJob(null, mainCatalog);

		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());

		final CatalogModel example = new CatalogModel();
		example.setId(MAIN_CATALOG);
		try
		{
			flexibleSearchService.getModelByExample(example);
			Assert.fail("Should have removed the catalog " + MAIN_CATALOG);
		}
		catch (final ModelNotFoundException e)
		{
			//
		}
	}


	@Test
	public void testRemoveVersion()
	{
		final RemoveCatalogVersionCronJobModel cronJobModel = createCronJob(source, mainCatalog);

		addCategoriesAndProducts(source);

		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());



		final CatalogVersionModel example = new CatalogVersionModel();
		example.setVersion(SOURCE_CATALOGVERSION);
		try
		{
			flexibleSearchService.getModelByExample(example);
			Assert.fail("Should have removed the catalogversion " + SOURCE_CATALOGVERSION);
		}
		catch (final ModelNotFoundException e)
		{
			//
		}


	}


	@Test
	public void testRemoveCatalog()
	{
		final CatalogVersionModel target = modelService.create(CatalogVersionModel.class);
		target.setVersion("target");
		target.setCatalog(mainCatalog);
		modelService.save(target);

		addCategoriesAndProducts(target);

		final RemoveCatalogVersionCronJobModel cronJobModel = createCronJob(null, mainCatalog);

		addCategoriesAndProducts(source);

		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());



		final CatalogVersionModel example = new CatalogVersionModel();
		example.setVersion(SOURCE_CATALOGVERSION);
		try
		{
			flexibleSearchService.getModelByExample(example);
			Assert.fail("Should have removed the catalogversion " + SOURCE_CATALOGVERSION);
		}
		catch (final ModelNotFoundException e)
		{
			//
		}

		example.setVersion("target");
		try
		{
			flexibleSearchService.getModelByExample(example);
			Assert.fail("Should have removed the catalogversion " + "target");
		}
		catch (final ModelNotFoundException e)
		{
			//
		}

		final CatalogModel catalogExample = new CatalogModel();
		catalogExample.setId(MAIN_CATALOG);
		try
		{
			flexibleSearchService.getModelByExample(catalogExample);
			Assert.fail("Should have removed the catalog " + MAIN_CATALOG);
		}
		catch (final ModelNotFoundException e)
		{
			//
		}


	}

}
