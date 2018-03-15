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
package de.hybris.platform.catalog.model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CatalogUnawareMediaModelTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;

	@Resource
	private CatalogService catalogService;

	@Before
	public void setUp()
	{
		if (catalogService.getDefaultCatalog() == null)
		{
			final CatalogModel cat = modelService.create(CatalogModel.class);
			cat.setId("testDefaultCat");
			cat.setDefaultCatalog(Boolean.TRUE);
			final CatalogVersionModel version = modelService.create(CatalogVersionModel.class);
			version.setVersion("version");
			version.setCatalog(cat);
			modelService.saveAll(cat, version);
		}
	}

	@Test
	public void testCatalogNonObligatoryAnonymous()
	{
		assertTrue(catalogService.getDefaultCatalog() != null);

		CatalogUnawareMediaModel media = modelService.create(CatalogUnawareMediaModel.class);
		media.setCode("cvUnawareMedia1");
		media.setCatalogVersion(null); // explicit NULL
		modelService.save(media);
		assertNull(media.getCatalogVersion());

		media = modelService.create(CatalogUnawareMediaModel.class);
		media.setCode("cvUnawareMedia2");
		//media.setCatalogVersion(null); // implicit NULL
		modelService.save(media);
		assertNull(media.getCatalogVersion());
	}

	@Test
	public void testCatalogNonObligatoryAdmin()
	{
		assertTrue(catalogService.getDefaultCatalog() != null);

		SessionContext ctx = null;
		try
		{
			ctx = JaloSession.getCurrentSession().createLocalSessionContext();
			final EmployeeModel admin = userService.getAdminUser();
			ctx.setUser((User) modelService.getSource(admin));
			final CatalogUnawareMediaModel media = modelService.create(CatalogUnawareMediaModel.class);
			media.setCode("cvUnawareMedia1");
			media.setCatalogVersion(null);
			modelService.save(media);
		}
		finally
		{
			if (ctx != null)
			{
				JaloSession.getCurrentSession().removeLocalSessionContext();
			}
		}
	}

}
