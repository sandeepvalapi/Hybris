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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class UserTypeTest extends HybrisJUnit4TransactionalTest
{
	ComposedType userType, employeeType;
	Employee employee;
	TypeManager typeManager;

	@Before
	public void setUp() throws Exception
	{
		typeManager = jaloSession.getTypeManager();
		userType = typeManager.getRootComposedType(Constants.TC.User);
		employeeType = typeManager.getComposedType(Employee.class);
		assertNotNull(employee = jaloSession.getUserManager().createEmployee("usertypetest"));
	}

	@Test
	public void testCodes()
	{
		assertNotNull(userType);
		assertNotNull(employeeType);
		// TODO: this is not future safe
		assertTrue(userType.getCode().indexOf("User") >= 0);
		// TODO: this only worked with generated type codes!
		//assertTrue( employeeType.getCode().indexOf( Constants.TYPES.Employee )>=0 );
		assertEquals(Constants.TC.User, userType.getItemTypeCode());
		assertEquals(Constants.TC.User, employeeType.getItemTypeCode());
		assertEquals(Registry.getPersistenceManager().getJNDIName(Constants.TC.User), userType.getJNDIName());
		assertEquals(Registry.getPersistenceManager().getJNDIName(Constants.TC.User), employeeType.getJNDIName());
		assertEquals(User.class, userType.getJaloClass());
		assertEquals(Employee.class, employeeType.getJaloClass());
		// TODO: this only worked with generated type codes!
		//assertTrue( employeeType.getCode().indexOf( String.valueOf(Constants.TYPES.Employee) )>=0 );
	}

	@Test
	public void testHierarchy()
	{
		assertEquals(userType, employeeType.getSuperType());
		assertTrue(userType.getSubTypes().contains(employeeType));
	}

	@Test
	public void testInstance()
	{
		assertTrue(userType.isInstance(employee));
		assertEquals(employeeType, employee.getComposedType());
		assertTrue(employeeType.isInstance(employee));
	}
}
