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
package de.hybris.platform.webservicescommons.oauth2.client.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;


@IntegrationTest
public class ClientDetailsDaoTest extends ServicelayerTransactionalBaseTest
{
	private static final String CLIENT_ID = "test_client";

	@Resource
	private ClientDetailsDao oauthClientDetailsDao;

	@Resource
	private ModelService modelService;

	@Before
	public void setup()
	{
		final OAuthClientDetailsModel client = new OAuthClientDetailsModel();
		client.setClientId(CLIENT_ID);
		modelService.save(client);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testNull()
	{
		oauthClientDetailsDao.findClientById(null);
	}

	@Test
	public void testSingleValid()
	{
		final OAuthClientDetailsModel clientDetailsModel = oauthClientDetailsDao.findClientById(CLIENT_ID);
		Assert.assertNotNull(clientDetailsModel);
		Assert.assertEquals(CLIENT_ID, clientDetailsModel.getClientId());
	}

	@Test
	public void testInvalid()
	{
		final OAuthClientDetailsModel clientDetailsModel = oauthClientDetailsDao.findClientById("qwertyuiop");
		Assert.assertNull("ClientDetails should be null", clientDetailsModel);
	}
}
