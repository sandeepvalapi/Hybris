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
package de.hybris.platform.servicelayer.user;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class UserServiceNonTxTest extends ServicelayerBaseTest
{
	@Resource
	private UserService userService;

	@Resource
	private ModelService modelService;

	@Test
	public void testSetAndGetSesionUser()
	{
		final PrincipalModel user = userService.getUserForUID("anonymous");
		assertNotNull("User", user);
		userService.setCurrentUser((UserModel) user);
		final UserModel gotUser = userService.getCurrentUser();
		assertEquals("User", user, gotUser);
		final User userItem = JaloSession.getCurrentSession().getUser();
		assertEquals("User PK", userItem.getPK(), user.getPk());
	}

	@Test
	public void testGetUserByLogin()
	{
		final UserModel user = userService.getUserForUID("anonymous");
		assertNotNull("User", user);
		assertEquals("Login", "anonymous", user.getUid());
	}

	@Test
	public void testGetAllGroups() throws Exception
	{
		final CoreBasicDataCreator creator = new CoreBasicDataCreator();
		creator.createBasicC2L();
		creator.createBasicUserGroups();

		final User jaloUser = UserManager.getInstance().getAnonymousCustomer();

		final UserModel user = userService.getUserForUID("anonymous");

		assertEquals(jaloUser.getPK(), user.getPk());
		assertNotNull("User", user);

		final Collection<UserGroupModel> groups = userService.getAllUserGroupsForUser(user);
		assertNotNull("Groups", groups);

		assertFalse(modelService.isModified(user));
		assertTrue(ModelContextUtils.getItemModelContext(user).isLoaded(UserModel.GROUPS));
		assertFalse(ModelContextUtils.getItemModelContext(user).isDirty(UserModel.GROUPS));

		final Set<PrincipalGroupModel> groupsDirect = user.getGroups();
		assertTrue(groups.containsAll(groupsDirect));

		final UserGroup ug = UserManager.getInstance().getUserGroupByGroupID("customergroup");
		assertNotNull(ug);
		assertTrue(jaloUser.isMemberOf(ug));
		assertTrue(jaloUser.getGroups().contains(ug));
		assertFalse("Groups is empty(Jalo)", jaloUser.getGroups().isEmpty());
		assertFalse("Groups is empty", groups.isEmpty());
	}
}
