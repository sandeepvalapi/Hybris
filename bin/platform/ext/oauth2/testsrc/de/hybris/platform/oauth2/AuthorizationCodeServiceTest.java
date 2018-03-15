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
package de.hybris.platform.oauth2;

import static com.google.common.collect.ImmutableSet.of;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

@IntegrationTest
public class AuthorizationCodeServiceTest extends ServicelayerTransactionalTest
{
	@Resource(name = "oauthAuthorizationCode")
	private AuthorizationCodeService service;

	@Test
	public void testCorrectCodeUsage() throws Exception
	{
		OAuth2Authentication auth = new OAuth2Authentication(
				new OAuth2Request(null, "clientId", null, true, of("read"), null, "http://localhost:8080", null, null),
				new UsernamePasswordAuthenticationToken("admin", "nimda"));
		service.store("code", auth);
	}

	@Test(expected = RuntimeException.class)
	public void testCodeUsedTwice() throws Exception
	{
		testCorrectCodeUsage();
		testCorrectCodeUsage();
	}

	@Test
	public void testCodeStoredTwice() throws Exception
	{
		testCorrectCodeUsage();
		final OAuth2Authentication auth = service.remove("code");
		final OAuth2Request request = auth.getOAuth2Request();
		assertThat(request.getClientId()).isEqualTo("clientId");
		assertThat(request.getRedirectUri()).isEqualTo("http://localhost:8080");
		final Authentication authentication = auth.getUserAuthentication();
		assertThat(authentication.getPrincipal()).isEqualTo("admin");
		assertThat(authentication.getCredentials()).isEqualTo("nimda");
	}

	@Test(expected = ModelNotFoundException.class)
	public void testRemovedTwice() throws Exception
	{
		testCorrectCodeUsage();
		service.remove("code");
		service.remove("code");
	}

	@Test(expected = ModelNotFoundException.class)
	public void testUseNonExistingCode() throws Exception
	{
		service.remove("doesNotExists");
	}
}
