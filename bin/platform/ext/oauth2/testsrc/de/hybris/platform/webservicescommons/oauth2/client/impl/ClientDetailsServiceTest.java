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
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;


@UnitTest
public class ClientDetailsServiceTest
{
	private static final String EMPTY_ID = null;
	private static final String SINGLE_ID = "A";
	private static final String MULTIPLE_ID = "B";
	private static final String UNKNOWN_ID = "C";


	private ClientDetailsService clientDetailsService;


	@Before
	public void setup()
	{
		final ClientDetailsDao dao = Mockito.mock(ClientDetailsDao.class);
		Mockito.when(dao.findClientById(EMPTY_ID)).thenReturn(null);
		Mockito.when(dao.findClientById(SINGLE_ID)).thenReturn(getClientDetails(SINGLE_ID));
		Mockito.when(dao.findClientById(MULTIPLE_ID)).thenThrow(new AmbiguousIdentifierException(""));
		Mockito.when(dao.findClientById(UNKNOWN_ID)).thenReturn(null);

		final DefaultClientDetailsService service = new DefaultClientDetailsService();
		service.setClientDetailsDao(dao);

		clientDetailsService = service;
	}

	private OAuthClientDetailsModel getClientDetails(final String id)
	{
		final OAuthClientDetailsModel result = new OAuthClientDetailsModel();
		result.setClientId(id);
		return result;
	}

	@Test(expected = ClientRegistrationException.class)
	public void testEmptyId()
	{
		clientDetailsService.loadClientByClientId(EMPTY_ID);
	}

	@Test
	public void testSingleValidId()
	{
		final ClientDetails clientDetails = clientDetailsService.loadClientByClientId(SINGLE_ID);
		Assert.assertEquals(SINGLE_ID, clientDetails.getClientId());
	}

	@Test(expected = ClientRegistrationException.class)
	public void testMultipleValidId()
	{
		clientDetailsService.loadClientByClientId(MULTIPLE_ID);
	}

	@Test(expected = NoSuchClientException.class)
	public void testInvalidId()
	{
		clientDetailsService.loadClientByClientId(UNKNOWN_ID);
	}
}
