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
import static de.hybris.platform.testframework.Assert.assertCollectionElements;
import static junit.framework.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Utilities;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class TransactionJaloTest extends HybrisJUnit4Test
{

	private static final Logger LOG = Logger.getLogger(TransactionJaloTest.class.getName());

	@Test
	public void testCreate() throws Exception
	{
		Product p = null;


		boolean done = false;
		try
		{
			final Transaction tx = Transaction.current();
			tx.begin();

			try
			{
				assertCollection(Collections.EMPTY_SET, jaloSession.getProductManager().getProductsByCode("txtest"));
				p = jaloSession.getProductManager().createProduct("txtest");
				assertCollectionElements(jaloSession.getProductManager().getProductsByCode("txtest"), p);
				assertEquals("txtest", p.getCode());

				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals("txtest", p.getCode());
			assertCollectionElements(jaloSession.getProductManager().getProductsByCode("txtest"), p);
			assertEquals("txtest", p.getCode());

		}
		finally
		{
			if (done && p != null)
			{
				p.remove();
			}
		}

	}



	@Test(expected = IllegalStateException.class)
	public void testSwitchTenantCreate() throws Exception
	{

		final boolean isMasterTenantAsTestSystem = Utilities.isMasterTenantAsTestSystem();
		//test only if there is other tenant to switch to 
		if (!isMasterTenantAsTestSystem)
		{

			Product p = null;
			boolean done = false;
			try
			{
				final Transaction tx = Transaction.current();
				tx.begin();

				try
				{
					assertCollection(Collections.EMPTY_SET, jaloSession.getProductManager().getProductsByCode("txtest"));
					p = jaloSession.getProductManager().createProduct("txtest");
					assertCollectionElements(jaloSession.getProductManager().getProductsByCode("txtest"), p);
					assertEquals("txtest", p.getCode());

					Registry.setCurrentTenant(Registry.getMasterTenant()); //try to switch

					done = true;
				}
				finally
				{
					if (done)
					{
						tx.commit();
					}
					else
					{
						tx.rollback();
					}
				}

				assertEquals("txtest", p.getCode());
				assertCollectionElements(jaloSession.getProductManager().getProductsByCode("txtest"), p);
				assertEquals("txtest", p.getCode());

			}
			finally
			{
				if (done && p != null)
				{
					p.remove();
				}
			}

		}
		else
		{
			LOG.info("There is no tenant to switch to - please initialize system in at least two tenants configuration in order to run this test");
			throw new IllegalStateException("Dummy thrown exception ");
		}
	}



}
