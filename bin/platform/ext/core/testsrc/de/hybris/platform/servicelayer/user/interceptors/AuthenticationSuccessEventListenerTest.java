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
package de.hybris.platform.servicelayer.user.interceptors;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;


@IntegrationTest
public class AuthenticationSuccessEventListenerTest extends AbstractAuthenticationEventListenerTest
{
	@Resource(name = "authenticationSuccessEventListener")
	private ApplicationListener listener;

	@Test
	public void testWithoutAttemptsRecord() throws Exception
	{
		listener.onApplicationEvent(new AuthenticationSuccessEvent(new UsernamePasswordAuthenticationToken(testUid, "pwd")));
		try
		{
			findAttempts();
			org.junit.Assert.fail("should not be any attempt record");
		}
		catch (final ModelNotFoundException e)
		{
			// OK
		}
	}

	@Test
	public void testReset() throws Exception
	{
		final BruteForceLoginAttemptsModel attempts = modelService.create(BruteForceLoginAttemptsModel.class);
		attempts.setUid(userService.getUserForUID(testUid).getUid());
		attempts.setAttempts(Integer.valueOf(1));
		modelService.save(attempts);
		listener.onApplicationEvent(new AuthenticationSuccessEvent(new UsernamePasswordAuthenticationToken(testUid, "pwd")));
		assertThat(findAttempts().getAttempts()).isEqualTo(0);
	}

}
