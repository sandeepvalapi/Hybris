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

import static de.hybris.platform.jalo.type.AttributeDescriptor.PROPERTY_FLAG;
import static de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG;
import static de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.webservicescommons.jalo.OAuthAccessToken;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


@SuppressWarnings("deprecation")
@IntegrationTest
public class CleanupAccessTokenTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private CleanupAccessToken cleanupAccessToken;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	private TypeManager typeManager;
	private ComposedType type;

	@Before
	public void setUp() throws Exception
	{
		typeManager = TypeManager.getInstance();
		type = typeManager.getComposedType(OAuthAccessTokenModel._TYPECODE);
	}

	@Test
	public void testRemoveOnly1Record() throws Exception
	{
		if (type.hasAttribute("clientId"))
		{
			type.removeProperty("clientId");
		}
		type.createAttributeDescriptor("clientId", typeManager.getRootAtomicType(String.class),
				WRITE_FLAG | READ_FLAG | PROPERTY_FLAG);
		try
		{
			type.newInstance(ImmutableMap.of("clientId", "client", OAuthAccessTokenModel.TOKENID, "token",
					OAuthAccessTokenModel.AUTHENTICATIONID, "testAuthId_1"));
			type.newInstance(
					ImmutableMap.of(OAuthAccessTokenModel.TOKENID, "token2", OAuthAccessTokenModel.AUTHENTICATIONID, "testAuthId_2"));

			cleanupAccessToken.clearAccessTokensFrom60Version();

			final List<Object> result = flexibleSearchService
					.search(String.format("select {pk} from {%s}", OAuthAccessTokenModel._TYPECODE)).getResult();
			assertThat(result.size()).isEqualTo(1);
			assertThat(((OAuthAccessTokenModel) result.get(0)).getTokenId()).isEqualTo("token2");
		}
		finally
		{
			type.removeProperty("clientId");
		}
	}

	@Test
	public void testPagedRemoval() throws Exception
	{
		if (type.hasAttribute("clientId"))
		{
			type.removeProperty("clientId");
		}
		type.createAttributeDescriptor("clientId", typeManager.getRootAtomicType(String.class),
				WRITE_FLAG | READ_FLAG | PROPERTY_FLAG);
		try
		{
			type.newInstance(ImmutableMap.of("clientId", "client", OAuthAccessTokenModel.AUTHENTICATIONID, "testAuthId_1",
					OAuthAccessTokenModel.TOKENID, "token"));
			type.newInstance(ImmutableMap.of("clientId", "client", OAuthAccessTokenModel.AUTHENTICATIONID, "testAuthId_2",
					OAuthAccessTokenModel.TOKENID, "token2"));
			cleanupAccessToken.setMaxRows(1);
			cleanupAccessToken.clearAccessTokensFrom60Version();

			final List<Object> result = flexibleSearchService
					.search(String.format("select {pk} from {%s}", OAuthAccessTokenModel._TYPECODE)).getResult();
			assertThat(result.size()).isEqualTo(0);
		}
		finally
		{
			type.removeProperty("clientId");
		}
	}

	@Test
	public void testRemoveNoRecord() throws Exception
	{
		if (type.hasAttribute("clientId"))
		{
			type.removeProperty("clientId");
		}
		type.createAttributeDescriptor("clientId", typeManager.getRootAtomicType(String.class),
				WRITE_FLAG | READ_FLAG | PROPERTY_FLAG);
		try
		{
			type.newInstance(ImmutableMap.of(OAuthAccessToken.TOKENID, "token2"));

			cleanupAccessToken.clearAccessTokensFrom60Version();

			final List<Object> result = flexibleSearchService
					.search(String.format("select {pk} from {%s}", OAuthAccessTokenModel._TYPECODE)).getResult();
			assertThat(result.size()).isEqualTo(1);
			assertThat(((OAuthAccessTokenModel) result.get(0)).getTokenId()).isEqualTo("token2");
		}
		finally
		{
			type.removeProperty("clientId");
		}
	}

	@Test
	public void test61schema() throws Exception
	{
		type.newInstance(ImmutableMap.of(OAuthAccessToken.TOKENID, "token2"));
		cleanupAccessToken.clearAccessTokensFrom60Version();
		final List<Object> result = flexibleSearchService
				.search(String.format("select {pk} from {%s}", OAuthAccessTokenModel._TYPECODE)).getResult();
		assertThat(result.size()).isEqualTo(1);
		assertThat(((OAuthAccessTokenModel) result.get(0)).getTokenId()).isEqualTo("token2");
	}
}
