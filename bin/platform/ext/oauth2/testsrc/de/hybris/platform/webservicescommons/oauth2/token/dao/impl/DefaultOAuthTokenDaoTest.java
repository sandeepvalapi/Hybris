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
package de.hybris.platform.webservicescommons.oauth2.token.dao.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;
import de.hybris.platform.webservicescommons.oauth2.token.dao.OAuthTokenDao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultOAuthTokenDaoTest extends ServicelayerTransactionalTest
{
	private static final String ACCESS_TOKEN_ID = "1";
	private static final String ACCESS_TOKEN_OBJECT = "accessTokenObject";
	private static final String REFRESH_TOKEN_ID = "2";
	private static final String REFRESH_TOKEN_OBJECT = "refreshTokenObject";
	private static final String AUTHENTICATION_ID = "userAuthId";
	private static final String AUTHENTICATION_OBJECT = "userAuthObject";
	private static final String CLIENT_ID = "clientId";
	private static final String USER_NAME = "userName";

	private static final String ACCESS_TOKEN_ID_2 = "3";
	private static final String ACCESS_TOKEN_OBJECT_2 = "accessTokenObject2";
	private static final String REFRESH_TOKEN_ID_2 = "4";
	private static final String REFRESH_TOKEN_OBJECT_2 = "refreshTokenObject2";
	private static final String AUTHENTICATION_ID_2 = "userAuthId2";
	private static final String AUTHENTICATION_OBJECT_2 = "userAuthObject2";
	private static final String CLIENT_ID_2 = "clientId2";
	private static final String USER_NAME_2 = "userName2";

	private static final String NOT_EXISTING_ID = "notExistingId";

	@Resource(name = "defaultOAuthTokenDao")
	private OAuthTokenDao oauthTokenDao;

	@Resource
	private ModelService modelService;
	private UserModel user;
	private OAuthClientDetailsModel client;
	private OAuthClientDetailsModel client2;

	@Before
	public void setUp()
	{
		user = new UserModel();
		user.setUid(USER_NAME);
		client = new OAuthClientDetailsModel();
		client.setClientId(CLIENT_ID);
		client2 = new OAuthClientDetailsModel();
		client2.setClientId(CLIENT_ID_2);
		modelService.saveAll(client, client2);
		createTokens(ACCESS_TOKEN_ID, ACCESS_TOKEN_OBJECT, AUTHENTICATION_ID, AUTHENTICATION_OBJECT, client, user,
				REFRESH_TOKEN_ID, REFRESH_TOKEN_OBJECT);
	}

	private void createTokens(final String accessTokenId, final Object accessTokenObject, final String authenticationId,
			final Object authenticationObject, final OAuthClientDetailsModel client, final UserModel user,
			final String refreshTokenId, final Object refreshTokenObject)
	{
		final OAuthAccessTokenModel accessToken = modelService.create(OAuthAccessTokenModel.class);
		accessToken.setTokenId(accessTokenId);
		accessToken.setToken(accessTokenObject);
		accessToken.setAuthenticationId(authenticationId);
		accessToken.setAuthentication(authenticationObject);
		accessToken.setClient(client);
		accessToken.setUser(user);

		final OAuthRefreshTokenModel refreshToken = modelService.create(OAuthRefreshTokenModel.class);
		refreshToken.setTokenId(refreshTokenId);
		refreshToken.setToken(refreshTokenObject);
		refreshToken.setAuthentication(authenticationObject);
		accessToken.setRefreshToken(refreshToken);
		modelService.save(accessToken);
	}

	private void createTokens(final String accessTokenId, final Object accessTokenObject, final String authenticationId,
			final Object authenticationObject, final OAuthClientDetailsModel client, final UserModel user,
			final OAuthRefreshTokenModel refreshToken)
	{
		final OAuthAccessTokenModel accessToken = modelService.create(OAuthAccessTokenModel.class);
		accessToken.setTokenId(accessTokenId);
		accessToken.setToken(accessTokenObject);
		accessToken.setAuthenticationId(authenticationId);
		accessToken.setAuthentication(authenticationObject);
		accessToken.setClient(client);
		accessToken.setUser(user);
		accessToken.setRefreshToken(refreshToken);
		modelService.saveAll(accessToken, user);
	}

	@Test
	public void findAccessTokenByIdTest()
	{
		final OAuthAccessTokenModel accessToken = oauthTokenDao.findAccessTokenById(ACCESS_TOKEN_ID);
		Assert.assertEquals(ACCESS_TOKEN_ID, accessToken.getTokenId());
		Assert.assertEquals(ACCESS_TOKEN_OBJECT, accessToken.getToken());
		Assert.assertEquals(AUTHENTICATION_ID, accessToken.getAuthenticationId());
		Assert.assertEquals(AUTHENTICATION_OBJECT, accessToken.getAuthentication());
		Assert.assertEquals(CLIENT_ID, accessToken.getClient().getClientId());
		Assert.assertEquals(USER_NAME, accessToken.getUser().getUid());
		Assert.assertNotNull(accessToken.getRefreshToken());
		Assert.assertEquals(REFRESH_TOKEN_ID, accessToken.getRefreshToken().getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT, accessToken.getRefreshToken().getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT, accessToken.getRefreshToken().getAuthentication());
	}

	@Test(expected = ModelNotFoundException.class)
	public void findNotExistingAccessTokenTest()
	{
		oauthTokenDao.findAccessTokenById(NOT_EXISTING_ID);
	}

	@Test
	public void findRefreshTokenByIdTest()
	{
		final OAuthRefreshTokenModel refreshToken = oauthTokenDao.findRefreshTokenById(REFRESH_TOKEN_ID);
		Assert.assertEquals(REFRESH_TOKEN_ID, refreshToken.getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT, refreshToken.getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT, refreshToken.getAuthentication());
	}

	@Test(expected = ModelNotFoundException.class)
	public void findNotExistingRefreshTokenTest()
	{
		oauthTokenDao.findRefreshTokenById(NOT_EXISTING_ID);
	}

	@Test
	public void findAccessTokenByRefreshTokenIdTest()
	{
		final OAuthAccessTokenModel accessToken = oauthTokenDao.findAccessTokenByRefreshTokenId(REFRESH_TOKEN_ID);
		Assert.assertEquals(ACCESS_TOKEN_ID, accessToken.getTokenId());
		Assert.assertNotNull(accessToken.getRefreshToken());
		Assert.assertEquals(REFRESH_TOKEN_ID, accessToken.getRefreshToken().getTokenId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void findAccessTokenForNotExistingRefreshTokenTest()
	{
		oauthTokenDao.findAccessTokenByRefreshTokenId(NOT_EXISTING_ID);
	}

	@Test(expected = AmbiguousIdentifierException.class)
	public void findMultipleAccessTokenByRefreshTokenIdTest()
	{
		final OAuthRefreshTokenModel refreshToken = oauthTokenDao.findRefreshTokenById(REFRESH_TOKEN_ID);
		createTokens(ACCESS_TOKEN_ID_2, ACCESS_TOKEN_OBJECT_2, AUTHENTICATION_ID_2, AUTHENTICATION_OBJECT_2, client2,
				new UserModel()
				{{
					setUid(USER_NAME_2);
				}}, refreshToken);

		oauthTokenDao.findAccessTokenByRefreshTokenId(REFRESH_TOKEN_ID);
	}

	@Test
	public void findAccessTokenListByRefreshTokenIdTest()
	{
		List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListByRefreshTokenId(REFRESH_TOKEN_ID);
		Assert.assertEquals(1, accessTokenList.size());
		Assert.assertEquals(ACCESS_TOKEN_ID, accessTokenList.get(0).getTokenId());
		Assert.assertNotNull(accessTokenList.get(0).getRefreshToken());
		Assert.assertEquals(REFRESH_TOKEN_ID, accessTokenList.get(0).getRefreshToken().getTokenId());

		createTokens(ACCESS_TOKEN_ID_2, ACCESS_TOKEN_OBJECT_2, AUTHENTICATION_ID_2, AUTHENTICATION_OBJECT_2, client2,
				new UserModel()
				{{
					setUid(USER_NAME_2);
				}}, accessTokenList.get(0).getRefreshToken());

		accessTokenList = oauthTokenDao.findAccessTokenListByRefreshTokenId(REFRESH_TOKEN_ID);
		Assert.assertEquals(2, accessTokenList.size());
		Assert.assertNotNull(accessTokenList.get(0).getRefreshToken());
		Assert.assertEquals(REFRESH_TOKEN_ID, accessTokenList.get(0).getRefreshToken().getTokenId());
		Assert.assertNotNull(accessTokenList.get(1).getRefreshToken());
		Assert.assertEquals(REFRESH_TOKEN_ID, accessTokenList.get(1).getRefreshToken().getTokenId());
	}

	@Test
	public void findAccessTokenListForNotExistingRefreshTokenTest()
	{
		final List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListByRefreshTokenId(NOT_EXISTING_ID);
		Assert.assertTrue(accessTokenList.isEmpty());
	}

	@Test(expected = ModelNotFoundException.class)
	public void findAccessTokenForNotExistingAuthenticationTest()
	{
		oauthTokenDao.findAccessTokenByAuthenticationId(NOT_EXISTING_ID);
	}

	@Test
	public void findAccessTokenListForClientTest()
	{
		List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListForClient(CLIENT_ID);
		Assert.assertEquals(1, accessTokenList.size());
		Assert.assertEquals(ACCESS_TOKEN_ID, accessTokenList.get(0).getTokenId());
		Assert.assertEquals(CLIENT_ID, accessTokenList.get(0).getClient().getClientId());

		createTokens(ACCESS_TOKEN_ID_2, ACCESS_TOKEN_OBJECT_2, AUTHENTICATION_ID_2, AUTHENTICATION_OBJECT_2, client,
				new UserModel()
				{{
					setUid(USER_NAME_2);
				}}, REFRESH_TOKEN_ID_2, REFRESH_TOKEN_OBJECT_2);

		accessTokenList = oauthTokenDao.findAccessTokenListForClient(CLIENT_ID);
		Assert.assertEquals(2, accessTokenList.size());
		Assert.assertEquals(CLIENT_ID, accessTokenList.get(0).getClient().getClientId());
		Assert.assertEquals(CLIENT_ID, accessTokenList.get(1).getClient().getClientId());
	}

	@Test
	public void findAccessTokenListForNotExistingClientTest()
	{
		final List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListForClient(NOT_EXISTING_ID);
		Assert.assertTrue(accessTokenList.isEmpty());
	}

	@Test
	public void findAccessTokenListForUserTest()
	{
		List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListForUser(USER_NAME);
		Assert.assertEquals(1, accessTokenList.size());
		Assert.assertEquals(ACCESS_TOKEN_ID, accessTokenList.get(0).getTokenId());
		Assert.assertEquals(USER_NAME, accessTokenList.get(0).getUser().getUid());

		createTokens(ACCESS_TOKEN_ID_2, ACCESS_TOKEN_OBJECT_2, AUTHENTICATION_ID_2, AUTHENTICATION_OBJECT_2, client2, user,
				REFRESH_TOKEN_ID_2, REFRESH_TOKEN_OBJECT_2);

		accessTokenList = oauthTokenDao.findAccessTokenListForUser(USER_NAME);
		Assert.assertEquals(2, accessTokenList.size());
		Assert.assertEquals(USER_NAME, accessTokenList.get(0).getUser().getUid());
		Assert.assertEquals(USER_NAME, accessTokenList.get(1).getUser().getUid());
	}

	@Test
	public void findAccessTokenListForNotExistingUserNameTest()
	{
		final List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListForUser(NOT_EXISTING_ID);
		Assert.assertTrue(accessTokenList.isEmpty());
	}

	@Test
	public void findAccessTokenListForClientAndUserTest()
	{
		List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListForClientAndUser(CLIENT_ID, USER_NAME);
		Assert.assertEquals(1, accessTokenList.size());
		Assert.assertEquals(ACCESS_TOKEN_ID, accessTokenList.get(0).getTokenId());
		Assert.assertEquals(CLIENT_ID, accessTokenList.get(0).getClient().getClientId());
		Assert.assertEquals(USER_NAME, accessTokenList.get(0).getUser().getUid());

		createTokens(ACCESS_TOKEN_ID_2, ACCESS_TOKEN_OBJECT_2, AUTHENTICATION_ID_2, AUTHENTICATION_OBJECT_2, client, user,
				REFRESH_TOKEN_ID_2, REFRESH_TOKEN_OBJECT_2);

		accessTokenList = oauthTokenDao.findAccessTokenListForClientAndUser(CLIENT_ID, USER_NAME);
		Assert.assertEquals(2, accessTokenList.size());
		Assert.assertEquals(CLIENT_ID, accessTokenList.get(0).getClient().getClientId());
		Assert.assertEquals(USER_NAME, accessTokenList.get(0).getUser().getUid());
		Assert.assertEquals(CLIENT_ID, accessTokenList.get(1).getClient().getClientId());
		Assert.assertEquals(USER_NAME, accessTokenList.get(1).getUser().getUid());
	}

	@Test
	public void findAccessTokenListForNotExistingClientAndUserTest()
	{
		final List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListForClientAndUser(NOT_EXISTING_ID,
				USER_NAME);
		Assert.assertTrue(accessTokenList.isEmpty());
	}

	@Test
	public void findAccessTokenListForClientAndNotExistingUserTest()
	{
		final List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListForClientAndUser(CLIENT_ID,
				NOT_EXISTING_ID);
		Assert.assertTrue(accessTokenList.isEmpty());
	}

	@Test
	public void findAccessTokenListForNotExistingClientAndNotExistingUserTest()
	{
		final List<OAuthAccessTokenModel> accessTokenList = oauthTokenDao.findAccessTokenListForClientAndUser(NOT_EXISTING_ID,
				NOT_EXISTING_ID);
		Assert.assertTrue(accessTokenList.isEmpty());
	}
}
