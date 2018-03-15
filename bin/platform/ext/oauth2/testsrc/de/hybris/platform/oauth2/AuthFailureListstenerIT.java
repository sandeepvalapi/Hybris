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

import static com.google.common.collect.ImmutableMap.of;
import static de.hybris.platform.oauth2.DefaultOauthUserAuditLoginStrategy.OAUTH2_MAX_AUTHENTICATION_ATTEMPTS;
import static java.lang.String.format;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.core.model.user.BruteForceOAuthDisabledAuditModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class AuthFailureListstenerIT extends ServicelayerTransactionalBaseTest
{
	public final static String testId = "testclient";
	private static final String PWD = "pwd";
	@Resource(name = "authFailureListener")
	private ApplicationListener listener;
	@Resource(name = "configurationService")
	private ConfigurationService config;
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService search;
	@Resource(name = "modelService")
	private ModelService model;
	@Resource(name = "oauthClientDetailsDao")
	private ClientDetailsDao clientDao;

	@Before
	public void setUp() throws Exception
	{
		config.getConfiguration().setProperty(OAUTH2_MAX_AUTHENTICATION_ATTEMPTS, "2");
		final OAuthClientDetailsModel client = model.create(OAuthClientDetailsModel.class);
		client.setClientId(testId);
		client.setClientSecret(PWD);
		model.save(client);
	}

	@Test
	public void createNewAttemptsRecord() throws Exception
	{
		listener.onApplicationEvent(new AuthenticationFailureBadCredentialsEvent(
				new UsernamePasswordAuthenticationToken(testId, PWD), new BadCredentialsException("")));
		assertThat(findAttempts(search, testId).getAttempts()).isEqualTo(1);
		assertThat(auditRecords().getResult()).isEmpty();
	}

	@Test
	public void createNewAuditRecord() throws Exception
	{
		createNewAttemptsRecord();
		listener.onApplicationEvent(new AuthenticationFailureBadCredentialsEvent(
				new UsernamePasswordAuthenticationToken(testId, PWD), new BadCredentialsException("")));
		assertThat(findAttempts(search, testId).getAttempts()).isEqualTo(0);
		final List<BruteForceOAuthDisabledAuditModel> result = auditRecords().getResult();
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getFailedOAuthAuthorizations()).isEqualTo(2);
		assertThat(clientDao.findClientById(testId).getDisabled()).isTrue();
	}

	public static BruteForceLoginAttemptsModel findAttempts(final FlexibleSearchService search, final String uid)
	{
		return search.searchUnique(new FlexibleSearchQuery(format("select {pk} from {%s} where {%s}= ?uid",
				BruteForceLoginAttemptsModel._TYPECODE, BruteForceLoginAttemptsModel.UID), of("uid", uid)));
	}

	private SearchResult<BruteForceOAuthDisabledAuditModel> auditRecords()
	{
		return search.search(format("select {a.pk} from {%s as a}  where {a.%s}=?uid", BruteForceOAuthDisabledAuditModel._TYPECODE,
				BruteForceOAuthDisabledAuditModel.UID), ImmutableMap.of("uid", testId));
	}
}
