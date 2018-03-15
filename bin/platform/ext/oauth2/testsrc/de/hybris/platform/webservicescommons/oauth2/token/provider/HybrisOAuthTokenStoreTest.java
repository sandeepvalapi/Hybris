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
package de.hybris.platform.webservicescommons.oauth2.token.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;
import de.hybris.platform.webservicescommons.oauth2.token.OAuthTokenService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;


@UnitTest
public class HybrisOAuthTokenStoreTest
{
	private static final String ACCESS_TOKEN_ID = "1";
	private static final String ACCESS_TOKEN_VALUE = "accessTokenValue";
	private static final String REFRESH_TOKEN_ID = "2";
	private static final String REFRESH_TOKEN_VALUE = "refreshTokenValue";
	private static final String AUTHENTICATION_ID = "userAuthId";
	private static final String CLIENT_ID = "clientId";
	private static final String USER_NAME = "userName";

	private static String NOT_EXISTING_TOKEN = "notExisitngToken";

	@Mock
	private OAuthTokenService oauthTokenService;
	private HybrisOAuthTokenStore tokenStore;

	private DefaultOAuth2AccessToken oauthAccessToken;
	private OAuth2RefreshToken oauthRefreshToken;
	private OAuth2Authentication oauthAuthentication;

	private OAuthAccessTokenModel accessTokenModel;
	private OAuthRefreshTokenModel refreshTokenModel;
	private List<OAuthAccessTokenModel> accessTokenModelList;
	private OAuthClientDetailsModel client;

	@Before
	public void setUp()
	{
		client = new OAuthClientDetailsModel();
		client.setClientId(CLIENT_ID);
		MockitoAnnotations.initMocks(this);
		tokenStore = new HybrisOAuthTokenStore();
		tokenStore.setOauthTokenService(oauthTokenService);

		oauthRefreshToken = new DefaultOAuth2RefreshToken(REFRESH_TOKEN_VALUE);
		oauthAccessToken = new DefaultOAuth2AccessToken(ACCESS_TOKEN_VALUE);
		oauthAccessToken.setRefreshToken(oauthRefreshToken);
		final Authentication authentication = new MockAuthentication(USER_NAME, false);
		oauthAuthentication = new OAuth2Authentication(
				new OAuth2Request(null, CLIENT_ID, null, true, null, null, null, null, null), authentication);

		accessTokenModel = new OAuthAccessTokenModel();
		accessTokenModel.setTokenId(ACCESS_TOKEN_ID);
		accessTokenModel.setToken(SerializationUtils.serialize(oauthAccessToken));
		accessTokenModel.setAuthenticationId(AUTHENTICATION_ID);
		accessTokenModel.setAuthentication(SerializationUtils.serialize(oauthAuthentication));
		accessTokenModel.setClient(client);
		refreshTokenModel = new OAuthRefreshTokenModel();
		refreshTokenModel.setTokenId(REFRESH_TOKEN_ID);
		refreshTokenModel.setToken(SerializationUtils.serialize(oauthRefreshToken));
		refreshTokenModel.setAuthentication(SerializationUtils.serialize(oauthAuthentication));
		accessTokenModel.setRefreshToken(refreshTokenModel);

		accessTokenModelList = new ArrayList<>();
		accessTokenModelList.add(accessTokenModel);
	}

	@Test
	public void testReadAuthentication()
	{
		given(oauthTokenService.getAccessToken(anyString())).willReturn(accessTokenModel);
		final OAuth2Authentication result = tokenStore.readAuthentication(oauthAccessToken);
		Assert.assertEquals(oauthAuthentication, result);
	}

	@Test
	public void testReadAuthenticationForNotExistingToken()
	{
		given(oauthTokenService.getAccessToken(anyString())).willThrow(new UnknownIdentifierException("Unknown identifier"));
		assertNull(tokenStore.readAuthentication(NOT_EXISTING_TOKEN));
	}

	@Test
	public void testReadAccessToken()
	{
		given(oauthTokenService.getAccessToken(anyString())).willReturn(accessTokenModel);
		final OAuth2AccessToken result = tokenStore.readAccessToken(ACCESS_TOKEN_VALUE);
		Assert.assertEquals(oauthAccessToken, result);
	}

	@Test
	public void testReadNotExistingAccessToken()
	{
		given(oauthTokenService.getAccessToken(anyString())).willThrow(new UnknownIdentifierException("Unknown identifier"));
		assertNull(tokenStore.readAccessToken(NOT_EXISTING_TOKEN));
	}

	@Test
	public void testStoreAccessToken()
	{
		given(oauthTokenService.getRefreshToken(anyString())).willThrow(new UnknownIdentifierException("Unknown identifier"));

		tokenStore.storeAccessToken(oauthAccessToken, oauthAuthentication);

		verify(oauthTokenService, times(1)).saveRefreshToken(anyString(), any(), any());
		verify(oauthTokenService, times(1)).saveAccessToken(anyString(), any(byte[].class), anyString(), any(byte[].class),
				eq(USER_NAME), eq(CLIENT_ID), any(OAuthRefreshTokenModel.class));
	}

	@Test
	public void testRemoveAccessToken()
	{
		tokenStore.removeAccessToken(oauthAccessToken);
		verify(oauthTokenService, times(1)).removeAccessToken(anyString());
	}

	@Test
	public void testFindAccessTokensByUserName()
	{
		given(oauthTokenService.getAccessTokensForUser(USER_NAME)).willReturn(accessTokenModelList);
		final Collection<OAuth2AccessToken> result = tokenStore.findTokensByUserName(USER_NAME);
		assertEquals(1, result.size());
		assertEquals(oauthAccessToken, result.toArray()[0]);
	}

	@Test
	public void testFindAccessTokensByClientId()
	{
		given(oauthTokenService.getAccessTokensForClient(CLIENT_ID)).willReturn(accessTokenModelList);
		final Collection<OAuth2AccessToken> result = tokenStore.findTokensByClientId(CLIENT_ID);
		assertEquals(1, result.size());
		assertEquals(oauthAccessToken, result.toArray()[0]);
	}

	@Test
	public void testReadRefreshToken()
	{
		given(oauthTokenService.getRefreshToken(anyString())).willReturn(refreshTokenModel);
		final OAuth2RefreshToken result = tokenStore.readRefreshToken(REFRESH_TOKEN_VALUE);
		Assert.assertEquals(oauthRefreshToken, result);
	}

	@Test
	public void testReadNotExistingRefreshToken()
	{
		given(oauthTokenService.getRefreshToken(anyString())).willThrow(new UnknownIdentifierException("Unknown identifier"));
		assertNull(tokenStore.readRefreshToken(NOT_EXISTING_TOKEN));
	}

	@Test
	public void testStoreRefreshToken()
	{
		tokenStore.storeRefreshToken(oauthRefreshToken, oauthAuthentication);
		verify(oauthTokenService, times(1)).saveRefreshToken(anyString(), any(), any());
	}

	@Test
	public void testReadAuthenticationForRefreshToken()
	{
		given(oauthTokenService.getRefreshToken(anyString())).willReturn(refreshTokenModel);
		final OAuth2Authentication result = tokenStore.readAuthenticationForRefreshToken(REFRESH_TOKEN_VALUE);
		assertEquals(oauthAuthentication, result);
	}

	@Test
	public void testReadAuthenticationForNotExistingRefreshToken()
	{
		given(oauthTokenService.getRefreshToken(anyString())).willThrow(new UnknownIdentifierException("Unknown identifier"));
		assertNull(tokenStore.readAuthenticationForRefreshToken(REFRESH_TOKEN_VALUE));
	}

	@Test
	public void testRemoveRefreshToken()
	{
		tokenStore.removeRefreshToken(oauthRefreshToken);
		verify(oauthTokenService, times(1)).removeRefreshToken(anyString());
	}

	@Test
	public void testGetAccessTokenNotFoundException()
	{
		given(oauthTokenService.getAccessTokenForAuthentication(anyString())).willThrow(
				new UnknownIdentifierException("Unknown identifier"));
		assertNull(tokenStore.getAccessToken(oauthAuthentication));
		verify(oauthTokenService, times(1)).getAccessTokenForAuthentication(anyString());
	}

	@Test
	public void testGetAccessTokenInvalidAuthenticationError()
	{
		given(oauthTokenService.getAccessTokenForAuthentication(anyString())).willThrow(
				new ClassCastException("Could not extract access token"));
		assertNull(tokenStore.getAccessToken(oauthAuthentication));
		verify(oauthTokenService, times(1)).getAccessTokenForAuthentication(anyString());
	}

	@Test
	public void testGetAccess()
	{
		given(oauthTokenService.getAccessTokenForAuthentication(anyString())).willReturn(accessTokenModel);
		final OAuth2AccessToken accessToken = tokenStore.getAccessToken(oauthAuthentication);

		verify(oauthTokenService, times(1)).getAccessTokenForAuthentication(anyString());
		assertEquals(tokenStore.deserializeAccessToken((byte[]) accessTokenModel.getToken()), accessToken);
	}

	protected static class MockAuthentication extends AbstractAuthenticationToken
	{
		private final String principal;

		public MockAuthentication(final String name, final boolean authenticated)
		{
			super(null);
			setAuthenticated(authenticated);
			this.principal = name;
		}

		@Override
		public Object getCredentials()
		{
			return null;
		}

		@Override
		public Object getPrincipal()
		{
			return this.principal;
		}
	}
}
