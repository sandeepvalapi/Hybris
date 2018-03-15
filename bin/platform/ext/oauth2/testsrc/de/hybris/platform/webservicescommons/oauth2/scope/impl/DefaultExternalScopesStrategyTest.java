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

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.webservicescommons.model.OpenIDClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OpenIDExternalScopesModel;
import de.hybris.platform.webservicescommons.oauth2.scope.ExternalScopesDao;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


/**
 *
 */
public class DefaultExternalScopesStrategyTest
{

	private static final String SCOPE_B = "b";
	private static final String SCOPE_A = "a";

	private static final String CLIENT_ID = "client_1";

	private UserService userService;

	private DefaultExternalScopesStrategy externalScopesStrategy;

	private ExternalScopesDao externalScopesDao;

	private UserModel user1;
	private UserModel user2;
	private UserModel user3;
	private OpenIDClientDetailsModel client;

	private OpenIDExternalScopesModel scopesModel1;
	private OpenIDExternalScopesModel scopesModel2;


	@Before
	public void setUp()
	{
		// mock user dao
		externalScopesDao = Mockito.mock(DefaultExternalScopesDao.class);
		userService = Mockito.mock(DefaultUserService.class);

		final PrincipalGroupModel groupModel = new PrincipalGroupModel();
		groupModel.setUid("g1");

		user1 = new UserModel();
		user1.setUid("1");
		user1.setGroups(Stream.of(groupModel).collect(Collectors.toSet()));

		user2 = new UserModel();
		user2.setUid("2");
		groupModel.setUid("g1");

		user3 = new UserModel();
		user3.setUid("3");

		client = new OpenIDClientDetailsModel();
		client.setClientId(CLIENT_ID);
		client.setIssuer("ec");

		scopesModel1 = new OpenIDExternalScopesModel();
		scopesModel1.setClientDetailsId(client);
		scopesModel1.setCode(SCOPE_A);
		scopesModel1.setPermittedPrincipals(Stream.of(user1, user2).collect(Collectors.toSet()));
		scopesModel1.setScope(Stream.of(SCOPE_A).collect(Collectors.toSet()));


		scopesModel2 = new OpenIDExternalScopesModel();
		scopesModel2.setClientDetailsId(client);
		scopesModel2.setCode(SCOPE_B);
		scopesModel2.setPermittedPrincipals(Stream.of(groupModel).collect(Collectors.toSet()));
		scopesModel2.setScope(Stream.of(SCOPE_B).collect(Collectors.toSet()));


		externalScopesStrategy = new DefaultExternalScopesStrategy();
		externalScopesStrategy.setExternalScopesDao(externalScopesDao);
		externalScopesStrategy.setUserService(userService);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.webservicescommons.oauth2.scope.impl.DefaultExternalScopesStrategy#getExternalScopes(de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel, java.lang.String)}.
	 */
	@Test
	public void testGetExternalScopesMultiple()
	{
		Mockito.when(userService.getUserForUID(user1.getUid())).thenReturn(user1);

		Mockito.when(externalScopesDao.findScopesByClientAndPrincipal(client, user1))
				.thenReturn(Stream.of(scopesModel1, scopesModel2).collect(Collectors.toList()));

		final List<String> externalScopes1 = externalScopesStrategy.getExternalScopes(client, "1");

		Assert.assertNotNull(externalScopes1);

		Assert.assertTrue(externalScopes1.contains(SCOPE_A) && externalScopes1.contains(SCOPE_B));
	}

	@Test
	public void testGetExternalScopesSingle()
	{
		Mockito.when(userService.getUserForUID(user2.getUid())).thenReturn(user2);

		Mockito.when(externalScopesDao.findScopesByClientAndPrincipal(client, user2))
				.thenReturn(Stream.of(scopesModel1).collect(Collectors.toList()));

		final List<String> externalScopes1 = externalScopesStrategy.getExternalScopes(client, "2");

		Assert.assertNotNull(externalScopes1);

		Assert.assertTrue(externalScopes1.contains(SCOPE_A));
		Assert.assertTrue(!externalScopes1.contains(SCOPE_B));
	}

	@Test
	public void testGetExternalScopesEmpty()
	{
		Mockito.when(userService.getUserForUID(user3.getUid())).thenReturn(user3);

		Mockito.when(externalScopesDao.findScopesByClientAndPrincipal(client, user3)).thenReturn(Collections.emptyList());

		final List<String> externalScopes1 = externalScopesStrategy.getExternalScopes(client, "3");

		Assert.assertNotNull(externalScopes1);

		Assert.assertTrue(externalScopes1.isEmpty());
	}
}
