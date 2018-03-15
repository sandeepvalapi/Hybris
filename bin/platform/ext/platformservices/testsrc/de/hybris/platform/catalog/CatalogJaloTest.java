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

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CatalogJaloTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private SessionService sessionService;

	private UserModel user;
	private CatalogModel defaultCatalog;

	@Before
	public void prepareTest()
	{
		user = modelService.create(UserModel.class);
		user.setUid(asUUID());

		defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);
	}

	@Test
	public void shouldAllowEditingPricesNotAssignedToCatalogVersion()
	{
		final CatalogVersionModel accessibleCatVersion = createCatalogVersion();
		final CatalogVersionModel deniedCatVersion = createCatalogVersion();

		user.setWritableCatalogVersions(of(accessibleCatVersion));
		modelService.saveAll();

		final User jaloUser = modelService.getSource(user);
		final CatalogManager catalogManager = CatalogManager.getInstance();

		assertThat(catalogManager.canWrite(jaloCtx(), jaloUser, toJalo(accessibleCatVersion))).isTrue();
		assertThat(catalogManager.canWrite(jaloCtx(), jaloUser, toJalo(deniedCatVersion))).isFalse();
		assertThat(catalogManager.canWrite(jaloCtx(), jaloUser, null)).isTrue();
	}


	CatalogVersion toJalo(final CatalogVersionModel catalogVersionModel)
	{
		return modelService.getSource(catalogVersionModel);
	}

	private SessionContext jaloCtx()
	{
		return JaloSession.getCurrentSession().getSessionContext();
	}

	private CatalogVersionModel createCatalogVersion()
	{
		final CatalogVersionModel accessibleCatVersion = modelService.create(CatalogVersionModel.class);
		accessibleCatVersion.setCatalog(defaultCatalog);
		accessibleCatVersion.setVersion(asUUID());
		return accessibleCatVersion;
	}

	private String asUUID()
	{
		return UUID.randomUUID().toString();
	}
}
