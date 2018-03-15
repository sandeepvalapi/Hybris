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
package de.hybris.platform.jalo.security;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Collection;

import org.junit.Test;


public class PrincipalGroupTest extends HybrisJUnit4Test
{
	@Test
	public void testPLA11892() throws ConsistencyCheckException
	{
		//test the null check in the protected (deprecated) method, see PLA-11892 for more infos
		final Collection nullColl = null;
		final UserGroup ug1 = JaloSession.getCurrentSession().getUserManager().createUserGroup("principaltest");
		assertNotNull("", ug1);
		ug1.setMembers(JaloSession.getCurrentSession().getSessionContext(), nullColl);
		assertTrue("", ug1.getMembers().isEmpty());
	}

	@Test
	public void testNullByPrincipalSetGroups() throws ConsistencyCheckException
	{
		final User user = JaloSession.getCurrentSession().getUserManager().createUser("moo");
		user.setGroups(JaloSession.getCurrentSession().getSessionContext(), null);
		assertTrue("", user.getGroups().isEmpty());
	}

}
