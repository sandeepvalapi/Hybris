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
package de.hybris.platform.masterserver.collector.business.impl;

import static org.fest.assertions.Assertions.assertThat;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

public class UsersCollectorTest extends ServicelayerBaseTest
{
	private UsersCollector usersCollector;
	@Resource
	private ModelService modelService;
	@Resource
	private CoreDataCreator userGroupsDataCreator;
	@Resource
	private UserService userService;

	@Before
	public void setUp() throws Exception
	{
		usersCollector = new UsersCollector();
		userGroupsDataCreator.populateDatabase();
		createTestUsers();
	}

	private void createTestUsers()
	{
		final EmployeeModel employee = modelService.create(EmployeeModel.class);
		employee.setUid("testEmployee1");
		employee.setName("testEmployee1");
		employee.setGroups(Sets.newHashSet(getEmployeeUserGroup()));
		modelService.save(employee);
		assertThat(modelService.isNew(employee)).isFalse();
	}

	private PrincipalGroupModel getEmployeeUserGroup()
	{
		final PrincipalGroupModel userGroup = userService.getUserGroupForUID(Constants.USER.EMPLOYEE_USERGROUP);
		assertThat(userGroup).isNotNull();
		return userGroup;
	}


	@Test
	public void shouldCollectNumEmployeesAndNumCustomers() throws Exception
	{
		// when
		final Map<String, Map<String, Object>> result = usersCollector.collectStatistics();

		// then
		assertThat(result).isNotNull().isNotEmpty();
		assertThat(result.get("users")).isNotNull().isNotEmpty();
		assertThat(result.get("users").get("employees")).isEqualTo(Integer.valueOf(2));
		assertThat(result.get("users").get("customers")).isEqualTo(Integer.valueOf(1));
	}
}
