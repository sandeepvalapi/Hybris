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
package de.hybris.platform.servicelayer.user.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.PasswordEncoderConstants;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.PasswordPolicyService;
import de.hybris.platform.servicelayer.user.UserConstants;
import de.hybris.platform.servicelayer.user.daos.TitleDao;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultUserDao;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


/**
 * Unit tests for {@link DefaultUserService}.
 */
@UnitTest
public class DefaultUserServiceTest
{
	private static final String TEST_UID = "ateam";

	private DefaultUserService userService;

	private UserGroupDao userGroupDao;

	private UserDao userDao;

	private TitleDao titleDao;

	private PasswordEncoderService passwordEncoderService;

	private PasswordPolicyService passwordPolicyService;

	@Before
	public void setUp()
	{
		// mock user dao
		userDao = Mockito.mock(DefaultUserDao.class);

		// mock user group dao
		userGroupDao = Mockito.mock(UserGroupDao.class);

		// mock title dao
		titleDao = Mockito.mock(TitleDao.class);

		passwordEncoderService = Mockito.mock(PasswordEncoderService.class);
		passwordPolicyService = Mockito.mock(PasswordPolicyService.class);

		final SessionService sessionService = Mockito.mock(SessionService.class);

		final SearchRestrictionService srs = Mockito.mock(SearchRestrictionService.class);
		Mockito.doNothing().when(srs).clearSessionSearchRestrictions();

		// create user service
		userService = new DefaultUserService();
		userService.setUserDao(userDao);
		userService.setUserGroupDao(userGroupDao);
		userService.setTitleDao(titleDao);
		userService.setSessionService(sessionService);
		userService.setSearchRestrictionService(srs);
		userService.setPasswordEncoderService(passwordEncoderService);
		userService.setPasswordPolicyService(passwordPolicyService);

		Mockito.when(sessionService.executeInLocalView(Mockito.any(SessionExecutionBody.class))).thenAnswer(new Answer<Object>()
		{
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable
			{
				final SessionExecutionBody args = (SessionExecutionBody) invocation.getArguments()[0];
				return args.execute();
			}
		});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUserForID()
	{
		userService.getUserForUID(null);
		fail("NPE expected");
	}

	@Test
	public void testFindAUser()
	{
		final UserModel user = new UserModel();
		user.setUid("anon");
		Mockito.when(userDao.findUserByUID(user.getUid())).thenReturn(user);

		assertEquals("not the same user", user, userService.getUserForUID(user.getUid()));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testFindNoUser()
	{
		Mockito.when(userDao.findUserByUID("something")).thenReturn(null);
		userService.getUserForUID("something");
		fail("UnknownIdentifierException expected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUserByIdAndClassWithNull()
	{
		userService.getUserForUID("something", null);
		fail("NPE expected");
	}

	@Test
	public void testGetUserByIdAndEmployeeClassOK()
	{
		final UserModel adm = new EmployeeModel();
		adm.setUid("adm");
		Mockito.when(userDao.findUserByUID(adm.getUid())).thenReturn(adm);

		assertEquals("not the same user", adm, userService.getUserForUID(adm.getUid(), EmployeeModel.class));
	}

	@Test(expected = ClassMismatchException.class)
	public void testGetUserByIdAndCustomerClassFails()
	{
		final UserModel adm = new EmployeeModel();
		adm.setUid("adm");
		Mockito.when(userDao.findUserByUID(adm.getUid())).thenReturn(adm);

		assertEquals("not the same user", adm, userService.getUserForUID(adm.getUid(), CustomerModel.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsUserExistingWithNull() throws Exception
	{
		userService.isUserExisting(null);
		fail("IllegalArgumentException expected");
	}

	@Test
	public void testIsUserExisting() throws Exception
	{
		final UserModel adm = new EmployeeModel();
		adm.setUid("usr");

		Mockito.when(userDao.findUserByUID(adm.getUid())).thenReturn(adm);

		assertTrue("not expected user", userService.isUserExisting("usr"));
	}

	@Test
	public void testIsNotUserExisting() throws Exception
	{
		final UserModel adm = new EmployeeModel();
		adm.setUid("usr");

		Mockito.when(userDao.findUserByUID(adm.getUid())).thenReturn(adm);

		assertFalse("not expected user", userService.isUserExisting("usr2"));
	}

	@Test
	public void testGetUserGroupForUID()
	{
		final UserGroupModel group = createUserGroup(TEST_UID, "A Team");

		Mockito.when(userGroupDao.findUserGroupByUid(TEST_UID)).thenReturn(group);

		Assert.assertEquals(group, userService.getUserGroupForUID(TEST_UID));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetUserGroupForUIDNotFound()
	{
		Mockito.when(userGroupDao.findUserGroupByUid(TEST_UID)).thenReturn(null);

		userService.getUserGroupForUID(TEST_UID);
		fail("UnknownIdentifierException expected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUserGroupForUIDWithNull()
	{
		final UserGroupModel group = createUserGroup(TEST_UID, "A Team");

		Mockito.when(userGroupDao.findUserGroupByUid(TEST_UID)).thenReturn(group);

		userService.getUserGroupForUID(null);
		fail("UnknownIdentifierException expected");
	}

	@Test
	public void testGetUserGroupForUIDWithType()
	{
		final TestUserGroupModel group = createTestUserGroup(TEST_UID, "A Team");

		Mockito.when(userGroupDao.findUserGroupByUid(TEST_UID)).thenReturn(group);

		assertEquals("not expected group", group, userService.getUserGroupForUID(TEST_UID, TestUserGroupModel.class));
	}

	@Test(expected = ClassMismatchException.class)
	public void testGetUserGroupForUIDWithWrongType()
	{
		final UserGroupModel group = createUserGroup(TEST_UID, "A Team");

		Mockito.when(userGroupDao.findUserGroupByUid(TEST_UID)).thenReturn(group);

		userService.getUserGroupForUID(TEST_UID, TestUserGroupModel.class);
		fail("TypeMismatchException expected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUserGroupForUIDWithNullType()
	{
		final UserGroupModel group = createUserGroup(TEST_UID, "A Team");

		Mockito.when(userGroupDao.findUserGroupByUid(TEST_UID)).thenReturn(group);

		userService.getUserGroupForUID(TEST_UID, null);
		fail("IllegalArgumentException expected");
	}

	@Test
	public void testGetAdminUserGroup()
	{
		final UserGroupModel adminGroup = createUserGroup(UserConstants.ADMIN_USERGROUP_UID, "Admin Group");

		Mockito.when(userGroupDao.findUserGroupByUid(UserConstants.ADMIN_USERGROUP_UID)).thenReturn(adminGroup);

		Assert.assertEquals(adminGroup, userService.getAdminUserGroup());
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetAdminUserGroupNotFound()
	{
		Mockito.when(userGroupDao.findUserGroupByUid(UserConstants.ADMIN_USERGROUP_UID)).thenReturn(null);

		userService.getAdminUserGroup();
		fail("UnknownIdentifierException expected");
	}

	@Test
	public void testIsNotMemberOf() throws Exception
	{
		final UserGroupModel group = createUserGroup(TEST_UID, "A Team");

		final UserModel user = createUser("anon");

		Assert.assertFalse(userService.isMemberOfGroup(user, group));
	}

	@Test
	public void testIsNotMemberOfRecursive() throws Exception
	{
		final UserGroupModel group1 = createUserGroup(TEST_UID, "A Team");

		final UserGroupModel group2 = createUserGroup("tst", "Test");

		final UserModel user = createUser("anon");
		addToGroup(user, group2);

		Assert.assertFalse(userService.isMemberOfGroup(user, group1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsMemberOfWithGroupNull() throws Exception
	{
		userService.isMemberOfGroup(createUser("anon"), null);
		fail("IllegalArgumentException expected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsMemberOfWithUserNull() throws Exception
	{
		userService.isMemberOfGroup((UserModel) null, createUserGroup(TEST_UID, "A Team"));
		fail("IllegalArgumentException expected");
	}

	@Test
	public void testIsMemberOf() throws Exception
	{
		final UserGroupModel group = createUserGroup(TEST_UID, "A Team");

		final UserModel user = createUser("anon");
		addToGroup(user, group);

		Assert.assertTrue(userService.isMemberOfGroup(user, group));
	}

	@Test
	public void testIsMemberOfRecursive() throws Exception
	{
		final UserGroupModel group1 = createUserGroup("all", "All users");

		final UserGroupModel group2 = createUserGroup(TEST_UID, "A Team");
		addToGroup(group2, group1);

		final UserModel user = createUser("anon");
		addToGroup(user, group2);

		Assert.assertTrue(userService.isMemberOfGroup(user, group1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGroupIsMemberOfWithGroupNull() throws Exception
	{
		userService.isMemberOfGroup((UserGroupModel) null, createUserGroup(TEST_UID, "A Team"));
		fail("IllegalArgumentException expected");
	}

	@Test
	public void testGroupIsNotMemberOf() throws Exception
	{
		final UserGroupModel group = createUserGroup(TEST_UID, "A Team");

		final UserGroupModel userGroup = createUserGroup("a", "test");

		Assert.assertFalse(userService.isMemberOfGroup(userGroup, group));
	}

	@Test
	public void testGroupIsNotMemberOfRecursive() throws Exception
	{
		final UserGroupModel group1 = createUserGroup(TEST_UID, "A Team");

		final UserGroupModel group2 = createUserGroup("tst", "Test");

		final UserGroupModel userGroup = createUserGroup("a", "test");
		addToGroup(userGroup, group2);

		Assert.assertFalse(userService.isMemberOfGroup(userGroup, group1));
	}

	@Test
	public void testGroupIsMemberOf() throws Exception
	{
		final UserGroupModel group = createUserGroup(TEST_UID, "A Team");

		final UserGroupModel userGroup = createUserGroup("a", "test");
		addToGroup(userGroup, group);

		Assert.assertTrue(userService.isMemberOfGroup(userGroup, group));
	}

	@Test
	public void testGroupIsMemberOfRecursive() throws Exception
	{
		final UserGroupModel group1 = createUserGroup("all", "All users");

		final UserGroupModel group2 = createUserGroup(TEST_UID, "A Team");
		addToGroup(group2, group1);

		final UserGroupModel userGroup = createUserGroup("a", "test");
		addToGroup(userGroup, group2);

		Assert.assertTrue(userService.isMemberOfGroup(userGroup, group1));
	}

	@Test
	public void testGetAllUserGroupsForUser() throws Exception
	{
		final UserGroupModel group1 = createUserGroup("all", "All users");

		final UserGroupModel group2 = createUserGroup(TEST_UID, "A Team");
		addToGroup(group2, group1);

		final UserModel user = createUser("a");
		addToGroup(user, group2);

		final Set<UserGroupModel> groups = userService.getAllUserGroupsForUser(user);
		assertNotNull("groups was null", groups);
		assertEquals("not expected size", 2, groups.size());
		assertTrue("does not containg group1", groups.contains(group1));
		assertTrue("does not containg group2", groups.contains(group2));
	}

	@Test
	public void testGetAllUserGroupsForUserWithType() throws Exception
	{
		final TestUserGroupModel group1 = createTestUserGroup("all", "All users");

		final UserGroupModel group2 = createUserGroup(TEST_UID, "A Team");
		addToGroup(group2, group1);

		final UserModel user = createUser("a");
		addToGroup(user, group2);

		final Set<TestUserGroupModel> groups = userService.getAllUserGroupsForUser(user, TestUserGroupModel.class);
		assertNotNull("groups was null", groups);
		assertEquals("not expected size", 1, groups.size());
		assertTrue("does not contain group1", groups.contains(group1));
		assertFalse("does contain group2", groups.contains(group2));
	}

	@Test
	public void testGetAllUserGroupsForUserIsEmpty() throws Exception
	{
		final UserModel user = createUser("a");

		final Set<UserGroupModel> groups = userService.getAllUserGroupsForUser(user);
		assertNotNull("groups was null", groups);
		assertEquals("not expected size", 0, groups.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAllUserGroupsForUserWithNull() throws Exception
	{
		userService.getAllUserGroupsForUser(null);
		fail("IllegalArgumentException expected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAllUserGroupsForUserWithTypeNull() throws Exception
	{
		final TestUserGroupModel group1 = createTestUserGroup("all", "All users");

		final UserModel user = createUser("test");
		addToGroup(user, group1);

		userService.getAllUserGroupsForUser(user, null);
		fail("IllegalArgumentException expected");
	}

	@Test
	public void testGetAllUserGroupsForUserWithCycle() throws Exception
	{
		final UserGroupModel group1 = createUserGroup("all", "All users");

		final UserGroupModel group2 = createTestUserGroup(TEST_UID, "A Team");

		// set up a cycle
		addToGroup(group2, group1);
		addToGroup(group1, group2);

		final UserModel user = createUser("a");
		addToGroup(user, group2);

		final Set<UserGroupModel> groups = userService.getAllUserGroupsForUser(user);
		assertNotNull("groups was null", groups);
		assertEquals("not expected size", 2, groups.size());
		assertTrue("does not containg group1", groups.contains(group1));
		assertTrue("does not containg group2", groups.contains(group2));
	}

	@Test
	public void testGetAllTitles()
	{
		final TitleModel title1 = createTitle("Dr");
		final TitleModel title2 = createTitle("Prof");

		Mockito.when(titleDao.findTitles()).thenReturn(Arrays.asList(title1, title2));

		final Collection<TitleModel> titles = userService.getAllTitles();

		assertNotNull("titles was null", titles);
		assertEquals("not expected size", 2, titles.size());
		assertEquals("titles differs.", new HashSet<TitleModel>(titles), new HashSet<TitleModel>(Arrays.asList(title1, title2)));
	}

	@Test
	public void testGetTitleForCode()
	{
		final String code = "Dr";
		final TitleModel title = createTitle(code);

		Mockito.when(titleDao.findTitleByCode(code)).thenReturn(title);

		assertEquals("not expected title", title, userService.getTitleForCode(code));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetTitleForWrongCode()
	{
		final String code = "Dr";

		Mockito.when(titleDao.findTitleByCode(code)).thenReturn(null);

		userService.getTitleForCode(code);
		fail("UnknownIdentifierException expected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetTitleForCodeWithNull()
	{
		userService.getTitleForCode(null);
		fail("IllegalArgumentException expected");
	}

	private void addToGroup(final PrincipalModel user, final UserGroupModel group)
	{
		// add group to user
		Set<PrincipalGroupModel> groups = user.getGroups();
		if (groups == null)
		{
			groups = new HashSet<PrincipalGroupModel>();
		}
		groups.add(group);
		user.setGroups(groups);

		// add user as member to group
		Set<PrincipalModel> members = group.getMembers();
		if (members == null)
		{
			members = new HashSet<PrincipalModel>();
		}
		members.add(user);
		group.setMembers(members);
	}

	private UserModel createUser(final String uid)
	{
		final UserModel user = new UserModel();
		user.setUid(uid);

		return user;
	}

	private TitleModel createTitle(final String code)
	{
		final TitleModel title = new TitleModel();
		title.setCode(code);

		return title;
	}

	private UserGroupModel createUserGroup(final String uid, final String name)
	{
		final UserGroupModel group = new UserGroupModel();
		group.setUid(uid);
		group.setName(name);

		return group;
	}

	private TestUserGroupModel createTestUserGroup(final String uid, final String name)
	{
		final TestUserGroupModel group = new TestUserGroupModel();
		group.setUid(uid);
		group.setName(name);

		return group;
	}

	// test sub class of user group 
	private class TestUserGroupModel extends UserGroupModel
	{
		//
	}

	@Test
	public void testAdminUser()
	{
		final EmployeeModel realAdmin = new EmployeeModel();
		realAdmin.setUid(UserConstants.ADMIN_EMPLOYEE_UID);
		final EmployeeModel fakeAdmin = new EmployeeModel();
		fakeAdmin.setUid(UserConstants.ADMIN_EMPLOYEE_UID);

		final UserGroupModel adminGroup = createUserGroup(UserConstants.ADMIN_USERGROUP_UID, "Admin Group");

		Mockito.when(userDao.findUserByUID(UserConstants.ADMIN_EMPLOYEE_UID)).thenReturn(realAdmin);
		Mockito.when(userGroupDao.findUserGroupByUid(UserConstants.ADMIN_USERGROUP_UID)).thenReturn(adminGroup);

		assertFalse("was true, not expected", userService.isAdmin(fakeAdmin));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsNullAdmin()
	{
		userService.isAdmin(null);
		fail("expected IllegalArgumentException");
	}

	@Test
	public void testIsAdminWithAdmin() throws Exception
	{
		final EmployeeModel admin = new EmployeeModel();
		admin.setUid(UserConstants.ADMIN_EMPLOYEE_UID);

		Mockito.when(userDao.findUserByUID(UserConstants.ADMIN_EMPLOYEE_UID)).thenReturn(admin);

		assertTrue("was false", userService.isAdmin(admin));
	}

	@Test
	public void testIsAdminWithAdminGroup() throws Exception
	{
		final EmployeeModel admin = new EmployeeModel();
		admin.setUid(UserConstants.ADMIN_EMPLOYEE_UID);

		final UserModel otherAdmin = createUser("tstAdmin");

		final UserGroupModel adminGroup = createUserGroup(UserConstants.ADMIN_USERGROUP_UID, "adminGroup");
		addToGroup(otherAdmin, adminGroup);

		Mockito.when(userDao.findUserByUID(UserConstants.ADMIN_EMPLOYEE_UID)).thenReturn(admin);
		Mockito.when(userGroupDao.findUserGroupByUid(UserConstants.ADMIN_USERGROUP_UID)).thenReturn(adminGroup);

		assertTrue("was false", userService.isAdmin(otherAdmin));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsAdminWithNull() throws Exception
	{
		final EmployeeModel admin = new EmployeeModel();
		admin.setUid(UserConstants.ADMIN_EMPLOYEE_UID);

		Mockito.when(userDao.findUserByUID(UserConstants.ADMIN_EMPLOYEE_UID)).thenReturn(admin);

		assertTrue("was false", userService.isAdmin(null));
	}

	@Test
	public void testAnonymousUser()
	{
		final CustomerModel realAnon = new CustomerModel();
		realAnon.setUid(UserConstants.ANONYMOUS_CUSTOMER_UID);
		final CustomerModel fakeAnon = new CustomerModel();
		fakeAnon.setUid(UserConstants.ANONYMOUS_CUSTOMER_UID);

		Mockito.when(userDao.findUserByUID(UserConstants.ANONYMOUS_CUSTOMER_UID)).thenReturn(realAnon);

		assertFalse("was true", userService.isAnonymousUser(fakeAnon));
		assertFalse("was true", userService.isAnonymousUser(null));
	}

	@Test
	public void testGetPassword()
	{
		final EmployeeModel admin = new EmployeeModel();
		admin.setUid(UserConstants.ADMIN_EMPLOYEE_UID);
		admin.setEncodedPassword("nimda");

		Mockito.when(userDao.findUserByUID(UserConstants.ADMIN_EMPLOYEE_UID)).thenReturn(admin);
		Mockito.when(passwordEncoderService.decode(admin)).thenReturn(admin.getEncodedPassword());

		assertEquals("nimda", userService.getPassword(admin));
		assertEquals("nimda", userService.getPassword(admin.getUid()));
	}

	@Test
	public void testSetPassword()
	{
		final EmployeeModel user = new EmployeeModel();

		Mockito.when(passwordEncoderService.encode(user, "plainPassword", PasswordEncoderConstants.DEFAULT_ENCODING)).thenReturn(
				"xxx");
		Mockito.when(passwordPolicyService.verifyPassword(user, "plainPassword", PasswordEncoderConstants.DEFAULT_ENCODING))
				.thenReturn(Collections.emptyList());

		userService.setPasswordWithDefaultEncoding(user, "plainPassword");

		assertEquals(PasswordEncoderConstants.DEFAULT_ENCODING, user.getPasswordEncoding());
		assertEquals("xxx", user.getEncodedPassword());
	}

}
