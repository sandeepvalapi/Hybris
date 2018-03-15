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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.model.OpenIDClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;


@UnitTest
public class OpenIDClientDetailsServiceTest
{
	private static final String SINGLE_ID = "A";

	private ClientDetailsService clientDetailsService;

	@Before
	public void setup()
	{
		final ClientDetailsDao dao = Mockito.mock(ClientDetailsDao.class);
		Mockito.when(dao.findClientById(SINGLE_ID)).thenReturn(getClientDetails(SINGLE_ID));

		final OpenIDClientDetailsService service = new OpenIDClientDetailsService();
		service.setClientDetailsDao(dao);

		clientDetailsService = service;
	}

	private OpenIDClientDetailsModel getClientDetails(final String id)
	{
		final OpenIDClientDetailsModel result = new OpenIDClientDetailsModel();
		result.setClientId(id);
		result.setScope(Stream.of("openid").collect(Collectors.toSet()));
		return result;
	}

	@Test
	public void testSingleValidId()
	{
		final ClientDetails clientDetails = clientDetailsService.loadClientByClientId(SINGLE_ID);
		Assert.assertEquals(SINGLE_ID, clientDetails.getClientId());
	}
}
