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
package de.hybris.platform.servicelayer.user.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultUserGroupDaoTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private UserGroupDao userGroupDao;

	@Test
	public void testFindUserGroupByUid()
	{
		createUserGroup("tst2", "Test Group2");
		createUserGroup("tst", "Test Group");

		modelService.saveAll();

		final UserGroupModel userGroup = userGroupDao.findUserGroupByUid("tst");
		assertNotNull(userGroup);

		assertUserGroupEquals(createUserGroup("tst", "Test Group"), userGroup);
	}

	@Test
	public void testFindUserGroupByMissingUid()
	{
		createUserGroup("tst2", "Test Group2");
		createUserGroup("tst3", "Test Group3");

		modelService.saveAll();

		final UserGroupModel userGroup = userGroupDao.findUserGroupByUid("tst");
		assertNull(userGroup);
	}

	private void assertUserGroupEquals(final UserGroupModel expected, final UserGroupModel actual)
	{
		assertEquals(expected.getUid(), actual.getUid());
		assertEquals(expected.getName(), actual.getName());
	}

	private UserGroupModel createUserGroup(final String uid, final String name)
	{
		final UserGroupModel grp = modelService.create(UserGroupModel.class);
		grp.setUid(uid);
		grp.setName(name);

		return grp;
	}
}
