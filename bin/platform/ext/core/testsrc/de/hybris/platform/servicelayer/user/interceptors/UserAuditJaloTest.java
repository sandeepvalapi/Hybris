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
import de.hybris.platform.core.model.user.AbstractUserAuditModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.model.user.UserPasswordChangeAuditModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.daos.UserAuditDao;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class UserAuditJaloTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	ModelService modelService;

	@Resource
	UserService userService;

	@Resource
	UserAuditDao userAuditDao;

	@Resource
	FlexibleSearchService flexibleSearchService;

	private final PropertyConfigSwitcher userAuditSwitcher = new PropertyConfigSwitcher("user.audit.enabled");

	@Before
	public void enableAudit()
	{
		userAuditSwitcher.switchToValue("true");
	}

	@After
	public void revertAuditSettings()
	{
		userAuditSwitcher.switchBackToDefault();
	}


	@Test
	public void shouldRecordPasswordChanges() throws ConsistencyCheckException
	{
		final User user = UserManager.getInstance().createUser(UUID.randomUUID().toString());

		user.setPassword("foo", "plain");
		user.setPassword("bar", "plain");

		final UserModel userModel = modelService.get(user.getPK());

		final List<AbstractUserAuditModel> userAudits = userService.getUserAudits(userModel);
		assertThat(userAudits).hasSize(1);
		assertThat(userAudits).onProperty(UserPasswordChangeAuditModel.ENCODEDPASSWORD).containsOnly("foo");
	}

	@Test
	public void shouldNotAuditIfDisabled() throws ConsistencyCheckException
	{
		userAuditSwitcher.switchToValue("false");
		final User user = UserManager.getInstance().createUser(UUID.randomUUID().toString());

		user.setPassword("foo", "plain");
		user.setPassword("bar", "plain");

		final UserModel userModel = modelService.get(user.getPK());
		final List<AbstractUserAuditModel> userAudit = userService.getUserAudits(userModel);
		assertThat(userAudit).isEmpty();
	}
}
