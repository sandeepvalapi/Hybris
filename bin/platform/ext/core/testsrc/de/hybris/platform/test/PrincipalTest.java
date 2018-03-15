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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PrincipalTest extends HybrisJUnit4Test
{
	UserManager userManager;
	UserGroup userGroup;
	Principal principal;

	@Before
	public void setUp() throws Exception
	{
		assertNotNull(userGroup = (userManager = jaloSession.getUserManager()).createUserGroup("principaltest"));
		assertNotNull(principal = userManager.createEmployee("principaltest"));
	}

	@Test
	public void testTransaction() throws Exception
	{
		final Transaction transaction = Transaction.current();
		Set groupsBefore = null;

		assertTrue(principal.isAlive());
		assertTrue(userGroup.isAlive());

		transaction.begin();
		try
		{
			assertTrue(principal.isAlive());
			assertTrue(userGroup.isAlive());

			groupsBefore = new HashSet(principal.getGroups());
			assertFalse(groupsBefore.contains(userGroup));

			principal.addToGroup(userGroup);

			//will fail here if 'normal' transaction is used, because cache
			//is cleared, data is got from the database but will return an empty collection,
			//because transaction was not yet committed
			//
			assertTrue(principal.getGroups().contains(userGroup));
			assertTrue(principal.getGroups().containsAll(groupsBefore));
			assertEquals(Collections.singleton(principal), userGroup.getMembers());

		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
		finally
		{
			transaction.rollback();
		}

		assertCollection(Collections.EMPTY_SET, userGroup.getMembers());
		assertCollection(groupsBefore, principal.getGroups());
	}
}
