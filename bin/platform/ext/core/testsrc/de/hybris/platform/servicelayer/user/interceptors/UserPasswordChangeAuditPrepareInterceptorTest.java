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
public class UserPasswordChangeAuditPrepareInterceptorTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	ModelService modelService;

	@Resource
	UserService userService;

	@Resource
	UserAuditDao userAuditDao;

	@Resource
	FlexibleSearchService flexibleSearchService;

	final String encoding = de.hybris.platform.persistence.SystemEJB.DEFAULT_ENCODING;
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
	public void shouldRecordPasswordChanges()
	{
		final UserModel user = givenUser();

		changePasswordInRange(user, 1, 5);

		final List<AbstractUserAuditModel> allAudit = getAllAuditData();
		final List<AbstractUserAuditModel> userAudit = userService.getUserAudits(user);

		assertThat(allAudit).onProperty(UserPasswordChangeAuditModel.ENCODEDPASSWORD).containsOnly("pass_no4", "pass_no3",
				"pass_no2", "pass_no1");
		assertThat(allAudit).onProperty(AbstractUserAuditModel.UID).containsOnly(user.getUid());
		assertThat(allAudit).onProperty(AbstractUserAuditModel.USERPK).containsOnly(user.getPk().getLong());

		assertThat(userAudit).onProperty(UserPasswordChangeAuditModel.ENCODEDPASSWORD).containsOnly("pass_no4", "pass_no3",
				"pass_no2", "pass_no1");
	}

	@Test
	public void shouldNotAuditIfDisabled()
	{
		userAuditSwitcher.switchToValue("false");
		final UserModel user = givenUser();

		changePasswordInRange(user, 1, 5);

		final List<AbstractUserAuditModel> userAudit = userService.getUserAudits(user);
		assertThat(userAudit).isEmpty();
	}

	@Test
	public void shouldKeepAuditDataAfterUserDeletion()
	{
		final UserModel user = givenUser();

		changePasswordInRange(user, 1, 2);
		modelService.remove(user);
		modelService.saveAll();

		final List<AbstractUserAuditModel> audit = getAllAuditData();

		assertThat(audit).onProperty(UserPasswordChangeAuditModel.ENCODEDPASSWORD).containsOnly("pass_no1");
	}

	@Test
	public void shouldCorrectlyAuditUserWithUidChange()
	{
		final UserModel user = givenUser();

		changePasswordInRange(user, 1, 2);

		user.setUid(UUID.randomUUID().toString());
		modelService.saveAll();

		changePasswordInRange(user, 3, 4);

		final List<AbstractUserAuditModel> userAuditData = userService.getUserAudits(user);

		assertThat(userAuditData).onProperty(UserPasswordChangeAuditModel.ENCODEDPASSWORD).containsOnly("pass_no3", "pass_no2",
				"pass_no1");
	}

	@Test
	public void shouldNotAuditChangeFromEmptyPassword()
	{
		final UserModel user = givenUser();
		modelService.save(user);
		userService.setPassword(user.getUid(), "pwd");

		final List<AbstractUserAuditModel> userAuditData = userService.getUserAudits(user);
		assertThat(userAuditData).isEmpty();
	}

	@Test
	public void shouldComparePasswordWithAudit()
	{
		final UserModel user = givenUser();
		final AbstractUserAuditModel audit = givenAudit(user, "pwd");

		assertThat(userService.isPasswordIdenticalToAudited(user, "pwd", (UserPasswordChangeAuditModel) audit)).isTrue();
		assertThat(userService.isPasswordIdenticalToAudited(user, "diff", (UserPasswordChangeAuditModel) audit)).isFalse();
	}

	private AbstractUserAuditModel givenAudit(final UserModel user, final String password)
	{
		userService.setPassword(user, "pwd");
		modelService.save(user);

		userService.setPassword(user, "not-used");
		modelService.save(user);

		final List<AbstractUserAuditModel> userAudits = userService.getUserAudits(user);
		assertThat(userAudits).hasSize(1);
		return userAudits.get(0);
	}

	private void changePasswordInRange(final UserModel user, final int start, final int end)
	{
		for (int i = start; i <= end; i++)
		{
			userService.setPassword(user, "pass_no" + i, encoding);
			modelService.save(user);
		}
	}

	private List<AbstractUserAuditModel> getAllAuditData()
	{
		return flexibleSearchService.<AbstractUserAuditModel>search(
				"SELECT {PK} FROM {" + AbstractUserAuditModel._TYPECODE + "} ORDER BY {" + AbstractUserAuditModel.CREATIONTIME +
						"} DESC")
				.getResult();
	}

	private UserModel givenUser()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		return user;
	}
}
