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
package de.hybris.platform.servicelayer;

import static org.fest.assertions.Assertions.assertThat;
 
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.test.TestEmployeeModel;
import de.hybris.platform.core.model.test.TestUserGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
 
import javax.annotation.Resource;
 
import org.junit.Test;


@IntegrationTest
public class CircularReferencesPrefetchAllTest extends ServicelayerTest
{
 
	@Resource
	private ModelService modelService;

	@Test
	public void shouldReadUserWithGroupsInPrefetchAllMode() throws Exception
	{
		// given
		final EmployeeModel testUser = modelService.create(TestEmployeeModel.class);
		testUser.setUid("testUser");

		final UserGroupModel testUserGroup = modelService.create(TestUserGroupModel.class);
		testUserGroup.setUid("testUserGroup");
		testUserGroup.setName("test group");
		testUser.setGroups(Collections.singleton(testUserGroup));
		modelService.saveAll();
 
		// when
		final UserModel retrievedUser = modelService.get(testUser.getPk());
		// with servicelayer.prefetch=all, above line fails with StackOverFlow

		// then
		assertThat(retrievedUser.getGroups()).hasSize(1);
	}
 
}
