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

import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * tests behaviour of Customer
 * 
 * 
 * 
 */
@IntegrationTest
public class CustomerTest extends HybrisJUnit4TransactionalTest
{
	private Customer customer;

	@Before
	public void setUp() throws Exception
	{
		customer = jaloSession.getUserManager().createCustomer("jalo.customer/Customer");
	}

	@After
	public void tearDown() throws Exception
	{
		customer.remove();
	}

	@Test
	public void testCreationDate() throws ConsistencyCheckException
	{
		Customer otherCustomer = null;
		try
		{
			otherCustomer = jaloSession.getUserManager().createCustomer("jalo.customer/OtherCustomer");
			assertTrue("customer or otherCustomer was null (" + customer + "," + otherCustomer + ")", customer != null
					&& otherCustomer != null);
			final Date d1 = customer.getCreationTime();
			final Date d2 = otherCustomer.getCreationTime();
			assertTrue("creation dates were not correct ( d1 = " + d1.getTime() + ", d2 = " + d2.getTime() + " )",
					d2.getTime() >= d1.getTime());
		}
		finally
		{
			if (otherCustomer != null)
			{
				otherCustomer.remove();
			}
		}
	}

}
