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

import static de.hybris.platform.oauth2.AuthFailureListstenerIT.findAttempts;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@IntegrationTest
public class AuthSuccessListenerIT extends ServicelayerTransactionalBaseTest
{
	public final static String testId = "testclient";
	@Resource(name = "authSuccessListener")
	private ApplicationListener listener;
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService search;
	@Resource(name = "modelService")
	private ModelService model;

	@Test(expected = ModelNotFoundException.class)
	public void ignoreSuccess() throws Exception
	{
		listener.onApplicationEvent(new AuthenticationSuccessEvent(new UsernamePasswordAuthenticationToken(testId, "pwd")));
		findAttempts(search, testId);
	}

	@Test
	public void resetAttempts() throws Exception
	{
		final BruteForceLoginAttemptsModel attempts = model.create(BruteForceLoginAttemptsModel.class);
		attempts.setUid(testId);
		attempts.setAttempts(Integer.valueOf(1));
		model.save(attempts);
		listener.onApplicationEvent(new AuthenticationSuccessEvent(new UsernamePasswordAuthenticationToken(testId, "pwd")));
		assertThat(findAttempts(search, testId).getAttempts()).isEqualTo(0);
	}
}
