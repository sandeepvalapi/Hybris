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
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import com.google.common.collect.Sets;


@UnitTest
public class ClientDetailsTest
{
	private final DefaultClientDetailsService service = new DefaultClientDetailsService();

	@Test
	public void testIsSecretRequiredPositive()
	{
		final OAuthClientDetailsModel model = new OAuthClientDetailsModel();
		model.setClientSecret("abc");
		final ClientDetails dcd = service.convertClient(model);

		Assert.assertTrue("Client secret should be required", dcd.isSecretRequired());
	}

	@Test
	public void testIsSecretRequiredNegative()
	{
		final OAuthClientDetailsModel model = new OAuthClientDetailsModel();
		final ClientDetails dcd = service.convertClient(model);

		Assert.assertFalse("Client secret should not be required", dcd.isSecretRequired());
	}

	@Test
	public void testGetAuthorities()
	{
		final String expected = "abc";

		final OAuthClientDetailsModel model = new OAuthClientDetailsModel();
		model.setAuthorities(Sets.newHashSet(expected));
		final ClientDetails dcd = service.convertClient(model);

		Assert.assertNotNull("Authorities should not be null", dcd.getAuthorities());
		Assert.assertFalse("Authorities should not be empty", dcd.getAuthorities().isEmpty());

		final GrantedAuthority actual = dcd.getAuthorities().iterator().next();

		Assert.assertEquals(expected, actual.getAuthority());
	}

	@Test
	public void testGetAuthoritiesEmpty()
	{
		final OAuthClientDetailsModel model = new OAuthClientDetailsModel();
		final ClientDetails dcd = service.convertClient(model);

		Assert.assertNotNull("Authorities should not be null", dcd.getAuthorities());
		Assert.assertTrue("Authorities should be empty", dcd.getAuthorities().isEmpty());
	}

	@Test
	public void testIsAutoApprovePositive()
	{
		final String expected = "abc";

		final OAuthClientDetailsModel model = new OAuthClientDetailsModel();
		model.setAutoApprove(Sets.newHashSet(expected));
		final ClientDetails dcd = service.convertClient(model);

		Assert.assertTrue("Auto approve should be positive", dcd.isAutoApprove(expected));
	}

	@Test
	public void testIsAutoApproveNegative()
	{
		final String expected = "abc";

		final OAuthClientDetailsModel model = new OAuthClientDetailsModel();
		model.setAutoApprove(Sets.newHashSet(expected));
		final ClientDetails dcd = service.convertClient(model);

		Assert.assertFalse("Auto approve should be negative", dcd.isAutoApprove("ab"));
	}

	@Test
	public void testIsAutoApproveEmpty()
	{
		final OAuthClientDetailsModel model = new OAuthClientDetailsModel();
		final ClientDetails dcd = service.convertClient(model);

		Assert.assertFalse("Auto approve should be negative", dcd.isAutoApprove("ab"));
	}
}
