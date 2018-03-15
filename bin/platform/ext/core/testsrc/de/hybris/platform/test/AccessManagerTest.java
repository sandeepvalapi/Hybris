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
package de.hybris.platform.test;

import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.PermissionContainer;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AccessManagerTest extends HybrisJUnit4TransactionalTest
{
	AccessManager accessManager;
	UserManager userManager;
	Product product;
	UserGroup admins;
	UserGroup somebodyElse;
	Employee admin;
	User user1, user2, user3;
	User[] usersGroup;
	UserRight[] usersRights;
	static private final int MAX_USERS = 10;
	static private final int MAX_RIGHTS = 10;

	@Before
	public void setUp() throws Exception
	{
		accessManager = jaloSession.getAccessManager();
		userManager = jaloSession.getUserManager();
		assertNotNull(product = jaloSession.getProductManager().createProduct("PRODUCT"));
		admins = jaloSession.getUserManager().getAdminUserGroup();
		assertNotNull(somebodyElse = jaloSession.getUserManager().createUserGroup("SOMEBODY_ELSE"));
		admin = jaloSession.getUserManager().getEmployeeByLogin(Constants.USER.ADMIN_EMPLOYEE);
		assertNotNull(user1 = userManager.createEmployee("empl1"));
		assertNotNull(user2 = userManager.createEmployee("empl2"));
		assertNotNull(user3 = userManager.createEmployee("empl3"));
		usersGroup = new User[MAX_USERS];
		usersRights = new UserRight[MAX_RIGHTS];
		for (int i = 0; i < MAX_USERS; i++)
		{
			assertNotNull(usersGroup[i] = userManager.createEmployee("USER_" + i));
		}
		for (int i = 0; i < MAX_RIGHTS; i++)
		{
			assertNotNull(usersRights[i] = accessManager.createUserRight("USERR_" + i));
		}
	}

	@Test
	public void shouldReturnAllUserRightsInTheSystem() throws Exception
	{
		// when
		final Collection allUserRights = accessManager.getAllUserRights();

		// then
		assertThat(allUserRights).hasSize(MAX_RIGHTS);
	}

	@Test
	public void testGetRestrictedPrincipals() throws ConsistencyCheckException
	{
		UserRight permission = null;
		assertNotNull(permission = accessManager.createUserRight("test.p"));
		// test not-find
		assertCollection(Collections.EMPTY_LIST, accessManager.getGlobalRestrictedPrincipals(permission));
		// set permissions
		// u1+, u3-, u3+
		user1.addGlobalPositivePermission(permission);
		user3.addGlobalPositivePermission(permission);
		user3.addGlobalNegativePermission(permission);
		// test find
		assertCollection(Arrays.asList(new Object[]
		{ user1, user3 }), accessManager.getGlobalRestrictedPrincipals(permission));
	}

	@Test
	public void testAddNegativePermissionOn() throws Exception
	{
		final UserRight userRight;
		assertNotNull(userRight = jaloSession.getAccessManager().createUserRight("USERRIGHT"));
		assertNotNull(jaloSession.getAccessManager().getUserRightByCode("USERRIGHT"));
		userRight.remove();
		assertNotNull(userRight);
		assertNull(jaloSession.getAccessManager().getUserRightByCode("USERRIGHT"));
		try
		{
			jaloSession.getAccessManager().addNegativePermissionOn(product, admins, somebodyElse,
					jaloSession.getAccessManager().getUserRightByCode("USERRIGHT"));
			fail("Bad parameter given");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetRestricedPrincipals()
	{
		User user1 = null;
		User user2 = null;
		User user3 = null;
		UserGroup userGroup = null;
		UserRight userRight = null;
		try
		{
			assertNotNull(userRight = accessManager.createUserRight("blahblubb"));
			assertNotNull(user1 = userManager.createEmployee("e1"));
			assertNotNull(user2 = userManager.createEmployee("e2"));
			assertNotNull(user3 = userManager.createCustomer("c1"));
			assertNotNull(userGroup = userManager.createUserGroup("g"));
			userGroup.addMember(user2);
			// ( u1 + )
			userRight.addPositivePermission(user1, userRight);
			// ( u3 - )
			userRight.addNegativePermission(user3, userRight);
			// ( g - )
			userRight.addNegativePermission(userGroup, userRight);
			// there is no way to find principals by item permissions any more!
			// just searching by global permission is possible
			assertCollection(Collections.EMPTY_LIST, accessManager.getGlobalRestrictedPrincipals(userRight));
			// Collection principals = jaloSession.getAccessManager().getRestrictedPrincipals( r );
			//
			// assertTrue(
			// "expected ["+u1+","+u3+","+g+"] but got "+principals,
			// principals != null && !principals.isEmpty() && principals.size() == 3 &&
			// principals.contains( u1 ) && principals.contains( u3 ) && principals.contains( g )
			// );
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testAddGlobalPermissions() throws Exception
	{
		UserRight perm = null;
		assertNotNull(perm = accessManager.createUserRight("USERRIGHT_3"));
		assertFalse(user1.checkPermission(user2, perm));
		assertFalse(user1.checkPermission(user3, perm));
		final Collection permis = Arrays
				.asList(new PermissionContainer[]
				{ new PermissionContainer(user3.getPK(), perm.getPK(), false),
						new PermissionContainer(user2.getPK(), perm.getPK(), false) });
		accessManager.addGlobalPermissions(admin, permis);
		assertTrue(accessManager.checkPermission(user2, perm));
		assertTrue(accessManager.checkPermission(user3, perm));
	}

	@Test
	public void testAddGlobalPermissionsPerformance() throws Exception
	{
		StopWatch stopWatch = new StopWatch("Creating global permission not in batch ...");
		for (int i = 0; i < MAX_USERS; i++)
		{
			for (int j = 0; j < MAX_RIGHTS; j++)
			{
				usersGroup[i].addGlobalPositivePermission(usersRights[j]);
				usersGroup[i].addGlobalNegativePermission(usersRights[j]);
			}
		}
		stopWatch.stop();
		stopWatch = new StopWatch("Creating global permission batch ...");
		final Collection permis = new ArrayList();
		for (int i = 0; i < MAX_USERS; i++)
		{
			for (int j = 0; j < MAX_RIGHTS; j++)
			{
				permis.add(new PermissionContainer(usersGroup[i].getPK(), usersRights[j].getPK(), true));
				permis.add(new PermissionContainer(usersGroup[i].getPK(), usersRights[j].getPK(), false));
			}
		}
		accessManager.addGlobalPermissions(admin, permis);
		stopWatch.stop();
	}
}
