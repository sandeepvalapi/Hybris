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
package de.hybris.platform.webservicescommons.interceptors;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@IntegrationTest
public class OAuthClientInterceptorTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	private OAuthClientInterceptor oauthClientInterceptor;

	@Before
	public void setUp() throws Exception
	{
		oauthClientInterceptor = new OAuthClientInterceptor();
		oauthClientInterceptor.setClientSecretEncoder(new BCryptPasswordEncoder());
	}

	private OAuthClientDetailsModel getAnyModel()
	{
		final OAuthClientDetailsModel model = modelService.create(OAuthClientDetailsModel.class);
		model.setClientId("test_anonymous_client");
		modelService.save(model);
		return model;
	}

	@Test
	public void testOnRemoveValid()
	{
		final OAuthClientDetailsModel model = getAnyModel();
		modelService.remove(model);
	}


	@Test
	public void testEncodeClientSecret()
	{
		//given
		final String secret = "newSecret";
		final OAuthClientDetailsModel model = getAnyModel();
		model.setClientSecret(secret);

		//when
		modelService.save(model);

		//then
		final String encodedPassword = model.getClientSecret();
		Assert.assertTrue(oauthClientInterceptor.getClientSecretEncoder().matches(secret, encodedPassword));
	}

	@Test
	public void testSetNullClientSecret()
	{
		//given
		final OAuthClientDetailsModel model = getAnyModel();
		model.setClientSecret(null);

		//when
		modelService.save(model);

		//then
		Assert.assertNull(model.getClientSecret());
	}
}
