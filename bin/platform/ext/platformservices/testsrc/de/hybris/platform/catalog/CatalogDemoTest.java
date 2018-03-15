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

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@DemoTest
public class CatalogDemoTest extends ServicelayerTransactionalTest
{

	private static final String CATALOG_A = "catalogA";
	private static final String CATALOG_B = "catalogB";

	private static final String STAGED = "Staged";
	private static final String ONLINE = "Online";

	@Resource
	private ModelService modelService;
	@Resource
	private CatalogService catalogService;


	//test models
	private CatalogModel catalogA = null;
	private CatalogModel catalogB = null;

	private CatalogVersionModel aOnline = null;
	private CatalogVersionModel aStaged = null;
	private CatalogVersionModel bOnline = null;
	private CatalogVersionModel bStaged = null;

	@Before
	public void before()
	{
		//createCoreData();

		catalogA = modelService.create(CatalogModel.class);
		catalogA.setId(CATALOG_A);
		catalogA.setName(CATALOG_A);
		catalogA.setDefaultCatalog(Boolean.TRUE);

		catalogB = modelService.create(CatalogModel.class);
		catalogB.setId(CATALOG_B);
		catalogB.setName(CATALOG_B);
		catalogB.setDefaultCatalog(Boolean.FALSE);

		aOnline = modelService.create(CatalogVersionModel.class);
		aOnline.setCatalog(catalogA);
		aOnline.setVersion(ONLINE);

		aStaged = modelService.create(CatalogVersionModel.class);
		aStaged.setCatalog(catalogA);
		aStaged.setVersion(STAGED);

		bOnline = modelService.create(CatalogVersionModel.class);
		bOnline.setCatalog(catalogB);
		bOnline.setVersion(ONLINE);

		bStaged = modelService.create(CatalogVersionModel.class);
		bStaged.setCatalog(catalogB);
		bStaged.setVersion(STAGED);

		modelService.saveAll();

	}

	/**
	 * This test demonstrates how to set active catalog version and tests the {@link PrepareInterceptor}s for
	 * {@link CatalogModel} and {@link CatalogVersionModel}.
	 */
	@Test
	public void testSettingActiveCatalogVersion()
	{
		for (final CatalogVersionModel aCV : catalogA.getCatalogVersions())
		{
			Assert.assertFalse("catalogVersion should not be active", aCV.getActive().booleanValue());
		}
		//You can set active catalog version within the catalog in two ways:
		//1.) Set ACTIVE flag on the catalog version itself

		//1a.) set active on ONLINE version
		aOnline.setActive(Boolean.TRUE);
		modelService.save(aOnline);
		modelService.refresh(catalogA);

		Assert.assertTrue("online catalogVersion should be active", aOnline.getActive().booleanValue());
		Assert.assertFalse("staged catalogVersion should not be active", aStaged.getActive().booleanValue());
		Assert.assertEquals("unexpected active catalog version", aOnline, catalogA.getActiveCatalogVersion());

		//1b.) set active on STAGED version
		aStaged.setActive(Boolean.TRUE);
		modelService.save(aStaged);
		modelService.refresh(catalogA);

		Assert.assertTrue("staged catalogVersion should be active", aStaged.getActive().booleanValue());
		Assert.assertFalse("online catalogVersion should not be active", aOnline.getActive().booleanValue());
		Assert.assertEquals("unexpected active catalog version", aStaged, catalogA.getActiveCatalogVersion());

		//1c.)reset ACTIVE flag

		aStaged.setActive(Boolean.FALSE);
		modelService.save(aStaged);
		modelService.refresh(catalogA);

		Assert.assertFalse("staged catalogVersion should not be active", aStaged.getActive().booleanValue());
		Assert.assertFalse("online catalogVersion should not be active", aOnline.getActive().booleanValue());
		Assert.assertNull("unexpected active catalog version", catalogA.getActiveCatalogVersion());


		//2.) Set activeCatalogVersion on Catalog
		boolean success = false;
		//but it must belong to the target catalog!
		try
		{
			catalogA.setActiveCatalogVersion(bOnline);
			modelService.save(catalogA);
			Assert.fail("Code should have failed with ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			//OK
			success = true;
		}
		Assert.assertTrue("Code should have failed with ModelSavingException", success);

		//1a.) set ONLINE as active version
		catalogA.setActiveCatalogVersion(aOnline);
		modelService.save(catalogA);
		modelService.refresh(aStaged);
		modelService.refresh(aOnline);

		Assert.assertTrue("online catalogVersion should be active", aOnline.getActive().booleanValue());
		Assert.assertFalse("staged catalogVersion should not be active", aStaged.getActive().booleanValue());
		Assert.assertEquals("unexpected active catalog version", aOnline, catalogA.getActiveCatalogVersion());

		//1b.) set STAGED as active version
		catalogA.setActiveCatalogVersion(aStaged);
		modelService.save(catalogA);
		modelService.refresh(aStaged);
		modelService.refresh(aOnline);

		Assert.assertTrue("staged catalogVersion should be active", aStaged.getActive().booleanValue());
		Assert.assertFalse("online catalogVersion should not be active", aOnline.getActive().booleanValue());
		Assert.assertEquals("unexpected active catalog version", aStaged, catalogA.getActiveCatalogVersion());

		//1c.) set null as active version
		catalogA.setActiveCatalogVersion(null);
		modelService.save(catalogA);
		modelService.refresh(aStaged);
		modelService.refresh(aOnline);

		Assert.assertFalse("staged catalogVersion should not be active", aStaged.getActive().booleanValue());
		Assert.assertFalse("online catalogVersion should not be active", aOnline.getActive().booleanValue());
		Assert.assertNull("unexpected active catalog version", catalogA.getActiveCatalogVersion());
	}

	/**
	 * This test demonstrates how to set default catalog and tests the {@link PrepareInterceptor}s for
	 * {@link CatalogModel}.
	 */
	@Test
	public void testSettingDefaultCatalog()
	{
		//Currently we have one default catalog version:
		Assert.assertTrue("CatalogA should be default", catalogA.getDefaultCatalog().booleanValue());
		Assert.assertFalse("CatalogB should not be default", catalogB.getDefaultCatalog().booleanValue());
		Assert.assertEquals("CatalogA should be default", catalogA, catalogService.getDefaultCatalog());

		//When setting other catalog to default, the flag on the previous default catalog should be reset.
		catalogB.setDefaultCatalog(Boolean.TRUE);
		modelService.save(catalogB);

		Assert.assertFalse("CatalogA should not be default", catalogA.getDefaultCatalog().booleanValue());
		Assert.assertTrue("CatalogB should be default", catalogB.getDefaultCatalog().booleanValue());
		Assert.assertEquals("CatalogA should be default", catalogB, catalogService.getDefaultCatalog());

	}
}
