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
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;


/**
 * see newgroups hybris.jakarta thread &quot;Finder in Transaktionen&quot;
 * 
 * 
 * 
 */
@IntegrationTest
public class FindUncommittedTest extends HybrisJUnit4TransactionalTest
{

	ProductManager pm;

	@Before
	public void setUp() throws Exception
	{
		pm = jaloSession.getProductManager();
	}

	@Test
	public void testFind() throws Exception
	{
		//if ( !isRunningInServer() ||  !isCoreDevelopMode() ) return;
		if (!Config.itemCacheIsolationActivated())
		{
			return;
		}


		Collection found;
		Product product = null;
		final Transaction t = Transaction.current();
		boolean success = true;
		try
		{
			t.begin();

			success = false;

			try
			{
				assertNotNull(product = pm.createProduct("finduncommitted"));
				assertEquals("finduncommitted", product.getCode());
				found = pm.getProductsByCode("finduncommitted");
				assertEquals(1, found.size());
				assertEquals(product, found.iterator().next());
				product.setCode("othercode");
				assertEquals("othercode", product.getCode());
				found = pm.getProductsByCode("othercode");
				assertEquals(1, found.size());
				assertEquals(product, found.iterator().next());
				found = pm.getProductsByCode("finduncommitted");
				assertEquals(0, found.size());

				success = true;

			}
			catch (final Throwable e)
			{
				e.printStackTrace(System.err);
				throw e;
			}
			finally
			{
				if (success)
				{
					t.commit();
				}
				else
				{
					t.rollback();
				}
			}
		}
		catch (final Throwable e)
		{
			if (success)
			{
				//Exception in begin() or commit()...
				e.printStackTrace(System.err);
			}

			if (product != null)
			{
				assertNotNull(product);// should be removed by rollback
			}
			fail("error : " + e);
		}
		found = pm.getProductsByCode("othercode");
		assertEquals(1, found.size());
		assertEquals(product, found.iterator().next());
	}

}
