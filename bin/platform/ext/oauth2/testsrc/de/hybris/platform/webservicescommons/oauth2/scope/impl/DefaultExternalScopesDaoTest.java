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
 */
package de.hybris.platform.webservicescommons.oauth2.scope.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultUserDao;
import de.hybris.platform.webservicescommons.model.OpenIDClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OpenIDExternalScopesModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultExternalScopesDaoTest extends ServicelayerTransactionalTest
{
	private static final String CLIENT_ID = "test_client";

	@Resource
	private ClientDetailsDao oauthClientDetailsDao;

	@Resource
	private DefaultUserDao userDao;

	@Resource
	private ModelService modelService;

	@Resource
	private DefaultExternalScopesDao externalScopesDao;

	private UserModel user1;
	private UserModel user2;
	private OpenIDClientDetailsModel client;

	@Before
	public void setup()
	{
		user1 = new UserModel();
		user1.setUid("1");
		modelService.save(user1);

		user2 = new UserModel();
		user2.setUid("2");
		modelService.save(user2);

		client = new OpenIDClientDetailsModel();
		client.setClientId(CLIENT_ID);
		client.setIssuer("ec");
		modelService.save(client);

		final OpenIDExternalScopesModel scopesModel1 = new OpenIDExternalScopesModel();
		scopesModel1.setClientDetailsId(client);
		scopesModel1.setCode("a");
		scopesModel1.setPermittedPrincipals(Stream.of(user1, user2).collect(Collectors.toSet()));
		scopesModel1.setScope(Stream.of("a").collect(Collectors.toSet()));
		modelService.save(scopesModel1);

		final OpenIDExternalScopesModel scopesModel2 = new OpenIDExternalScopesModel();
		scopesModel2.setClientDetailsId(client);
		scopesModel2.setCode("b");
		scopesModel2.setPermittedPrincipals(Stream.of(user2).collect(Collectors.toSet()));
		scopesModel2.setScope(Stream.of("b").collect(Collectors.toSet()));
		modelService.save(scopesModel2);
	}

	@Test
	public void testFindScopesByClientAndPrincipal()
	{
		final List<OpenIDExternalScopesModel> externalScopesModels1 = externalScopesDao.findScopesByClientAndPrincipal(client,
				user1);

		Assert.assertNotNull(externalScopesModels1);

		boolean found1 = false;

		for (final OpenIDExternalScopesModel model : externalScopesModels1)
		{
			if (model.getScope() != null && model.getScope().contains("a"))
			{
				found1 = true;
				break;
			}
		}
		Assert.assertTrue("scope not found", found1);

		final List<OpenIDExternalScopesModel> externalScopesModels2 = externalScopesDao.findScopesByClientAndPrincipal(client,
				user2);

		Assert.assertNotNull(externalScopesModels2);

		boolean found2 = false;

		for (final OpenIDExternalScopesModel model : externalScopesModels2)
		{
			if (model.getScope() != null && model.getScope().contains("b"))
			{
				found2 = true;
				break;
			}
		}
		Assert.assertTrue("scope not found", found2);
	}
}

