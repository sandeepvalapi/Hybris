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
package de.hybris.platform.category;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelContext;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.internal.model.impl.ModelContextProxy;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@PerformanceTest
public class CategoryPerformanceTest extends ServicelayerTest
{
	private final static Logger LOG = Logger.getLogger(CategoryPerformanceTest.class);

	@Resource
	private CatalogService catalogService;
	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	public void testSingleStepCreation()
	{

		final DefaultModelContext ctx = getModelContext();

		final CatalogVersionModel catalogVersion = catalogService.getCatalogVersion("testCatalog", "Online");

		UserModel dummyModel = this.modelService.create(UserModel.class);
		dummyModel.setUid("dummy");
		this.modelService.save(dummyModel);

		UserModel owner = this.modelService.get(dummyModel.getPk());
		CategoryModel category = this.modelService.create(CategoryModel.class);
		category.setCatalogVersion(catalogVersion);
		category.setCode("id_x");
		category.setDescription("deschription_x");
		category.setName("name_x");
		category.setOwner(owner);
		this.modelService.save(category);

		category.setCode("id_y");
		this.modelService.save(category);

		category = null;
		owner = null;
		dummyModel = null;

		System.gc();

		LOG.info(ctx.getStats());

	}

	@Test
	public void testPerformance()
	{
		final CatalogVersionModel catalogVersion = catalogService.getCatalogVersion("testCatalog", "Online");

		final UserModel dummyModel = this.modelService.create(UserModel.class);
		dummyModel.setUid("dummy");
		this.modelService.save(dummyModel);

		long time1 = System.currentTimeMillis();

		final DefaultModelContext ctx = getModelContext();

		for (int i = 0; i < 50000; i++)
		{
			final UserModel owner = this.modelService.get(dummyModel.getPk());
			final CategoryModel category = this.modelService.create(CategoryModel.class);
			category.setCatalogVersion(catalogVersion);
			category.setCode("id_" + i);
			category.setDescription("deschription_" + i);
			category.setName("name_" + i);
			category.setOwner(owner);
			this.modelService.save(category);

			// uncomment this to avoid memory leak
			//this.modelService.detachAll();
			//
			if (i > 0 && (i % 100) == 0)
			{
				final long time2 = System.currentTimeMillis();
				LOG.info(i + " speed is " + (time2 - time1) / 100 + " ms per save for the last 100 models");
				LOG.info(ctx.getStats());
				time1 = time2;
			}
		}
	}

	private DefaultModelContext getModelContext()
	{
		ModelContext modelContext = ((DefaultModelService) modelService).getModelContext();
		if (modelContext instanceof ModelContextProxy)
		{
			modelContext = ((ModelContextProxy) modelContext).getActiveContext();
		}
		return (DefaultModelContext) modelContext;
	}
}
