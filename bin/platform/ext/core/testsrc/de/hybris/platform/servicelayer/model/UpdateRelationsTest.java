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
package de.hybris.platform.servicelayer.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;


/**
 * Tests to add new relation (next element) for existing model using setters.
 */
@IntegrationTest
public class UpdateRelationsTest extends ServicelayerTransactionalBaseTest
{

	private static final String USER1_UID = "user1";
	private static final String USER2_UID = "user2";
	private static final String GROUP1_UID = "group1";
	private static final String GROUP2_UID = "group2";

	@Resource
	private ModelService modelService;

	private final UserManager userManager = UserManager.getInstance();


	/**
	 * Tests to add next user (member) to user group which is updated using setter setMembers.
	 */
	@Test
	public void testAddNextUserToExistingGroupWithSetter()
	{
		final UserModel user1 = createUser(USER1_UID);

		final UserGroupModel userGroup = new UserGroupModel();
		modelService.initDefaults(userGroup);
		userGroup.setUid(GROUP1_UID);
		// It will be better that setter setMembers passes list with type: List<? extends PrincipalModel> value
		// We will avoid not necessary cast like in this example casting UserModel to PrincipalModel
		userGroup.setMembers(Collections.singleton((PrincipalModel) user1));
		modelService.save(userGroup);

		assertThat(Integer.valueOf(userGroup.getMembers().size()), is(equalTo(Integer.valueOf(1))));

		// add next user to group which was created earlier
		final UserGroupModel existingUserGroup = findUserGroup(GROUP1_UID);

		assertThat(existingUserGroup, is(notNullValue()));
		assertThat(Integer.valueOf(existingUserGroup.getMembers().size()), is(equalTo(Integer.valueOf(1))));

		final UserModel user2 = createUser(USER2_UID);

		final Set<PrincipalModel> members = existingUserGroup.getMembers();
		assertThat(Integer.valueOf(members.size()), is(equalTo(Integer.valueOf(1))));

		final Set<PrincipalModel> newMembers = new LinkedHashSet<PrincipalModel>();
		newMembers.addAll(members);
		newMembers.add(user2);

		existingUserGroup.setMembers(newMembers);
		modelService.save(existingUserGroup);

		assertThat(Integer.valueOf(existingUserGroup.getMembers().size()), is(equalTo(Integer.valueOf(2))));
	}


	/**
	 * Test to add next group to existing user which is updated using setter setGroups and was created before - without
	 * finding user once again.
	 */
	@Test
	public void testAddNextGroupToExistingUserWithSetter()
	{
		final UserModel user1 = new UserModel();
		modelService.initDefaults(user1);
		user1.setUid(USER1_UID);
		final UserGroupModel group1 = createUserGroup(GROUP1_UID);
		user1.setGroups(Collections.singleton((PrincipalGroupModel) group1));
		modelService.save(user1);
		assertThat(Integer.valueOf(user1.getGroups().size()), is(equalTo(Integer.valueOf(1))));

		final UserGroupModel group2 = createUserGroup(GROUP2_UID);

		final Set<PrincipalGroupModel> groups = user1.getGroups();

		final Set<PrincipalGroupModel> newGroups = new LinkedHashSet<PrincipalGroupModel>();
		newGroups.addAll(groups);
		newGroups.add(group2);

		user1.setGroups(newGroups);
		modelService.save(user1);

		assertThat("Number of groups for user1", Integer.valueOf(user1.getGroups().size()), is(equalTo(Integer.valueOf(2))));

	}

	/**
	 * Test to add first group to existing user
	 * 
	 * My result passed
	 */
	@Test
	public void testAddGroupToExistingUserWithoutGroup()
	{
		createUser(USER1_UID);
		final UserModel user1 = findUser(USER1_UID);
		final UserGroupModel group1 = createUserGroup(GROUP1_UID);
		user1.setGroups(Collections.singleton((PrincipalGroupModel) group1));
		modelService.save(user1);
		assertThat(Integer.valueOf(user1.getGroups().size()), is(equalTo(Integer.valueOf(1))));

	}

	/**
	 * Test to add next group to existing user which is updated using setter setGroups and was created before - with
	 * finding user once again after each update .
	 */
	@Test
	public void testAddNextGroupToExistingUserAlwaysSearchBeforeAddGroup()
	{
		createUser(USER1_UID);
		UserModel user1 = findUser(USER1_UID);
		final UserGroupModel group1 = createUserGroup(GROUP1_UID);
		user1.setGroups(Collections.singleton((PrincipalGroupModel) group1));
		modelService.save(user1);
		assertThat(Integer.valueOf(user1.getGroups().size()), is(equalTo(Integer.valueOf(1))));

		user1 = findUser(USER1_UID);
		final UserGroupModel group2 = createUserGroup(GROUP2_UID);
		final Set<PrincipalGroupModel> groups = user1.getGroups();

		final Set<PrincipalGroupModel> newGroups = new LinkedHashSet<PrincipalGroupModel>();
		newGroups.addAll(groups);
		newGroups.add(group2);

		user1.setGroups(newGroups);
		modelService.save(user1);

		user1 = findUser(USER1_UID);
		assertThat("Number of groups for user1", Integer.valueOf(user1.getGroups().size()), is(equalTo(Integer.valueOf(2))));

	}

	private UserModel createUser(final String uid)
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(uid);
		modelService.save(user);
		assertThat(user.getPk(), is(notNullValue()));
		return user;
	}

	private UserGroupModel createUserGroup(final String uid)
	{
		final UserGroupModel userGroup = modelService.create(UserGroupModel.class);
		userGroup.setUid(uid);
		modelService.save(userGroup);
		assertThat(userGroup.getPk(), is(notNullValue()));
		return userGroup;
	}

	private UserGroupModel findUserGroup(final String groupId)
	{
		final UserGroup userGroupItem = userManager.getUserGroupByGroupID(groupId);
		return modelService.get(userGroupItem);
	}

	private UserModel findUser(final String uid)
	{
		final User userItem = userManager.getUserByLogin(uid);
		return modelService.get(userItem);
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
