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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * tests collection methods for PKs
 * 
 * 
 * 
 */
@IntegrationTest
public class PKCollectionTest extends HybrisJUnit4TransactionalTest
{
	Item item1, item2, item3;
	PK pk1, pk2, pk3, failPK;

	private static final Logger LOG = Logger.getLogger(PKCollectionTest.class);

	@Before
	public void setUp() throws Exception
	{
		failPK = PK.createFixedUUIDPK(Constants.TC.User, 1);
		assertNotNull(item1 = jaloSession.getC2LManager().createCountry("testcountry"));
		assertNotNull(item2 = jaloSession.getOrderManager().createTax("testtax"));
		assertNotNull(item3 = jaloSession.getProductManager().createProduct("testproduct"));
	}

	@Test
	public void testCollections()
	{
		try
		{
			// test legal behavior
			Collection pkColl = new LinkedList();
			pkColl.add(item1.getPK());
			pkColl.add(item2.getPK());
			pkColl.add(item3.getPK());

			Collection items = jaloSession.getItems(pkColl);

			assertTrue("expected [" + item1 + "," + item2 + "," + item3 + "] but got " + items, items != null && !items.isEmpty()
					&& items.contains(item1) && items.contains(item2) && items.contains(item3));

			Collection pkStrings = new LinkedList();
			pkStrings.add(item3.getPK().toString());
			pkStrings.add(item1.getPK().toString());

			items = jaloSession.getItems(PK.parse(pkStrings));

			assertTrue("expected [" + item1 + "," + item3 + "] but got " + items,
					items != null && !items.isEmpty() && items.contains(item1) && items.contains(item3));

			// test illegal behavior
			pkColl = new LinkedList();
			pkColl.add(item1.getPK());
			pkColl.add(failPK);

			try
			{
				items = jaloSession.getItems(pkColl);
				fail("wrong pk did not throw JaloItemNotFoundException");
			}
			catch (final JaloItemNotFoundException e)
			{
				// fine
			}

			pkStrings = new LinkedList();
			pkStrings.add(item3.getPK().toString());
			pkStrings.add(failPK.toString());

			try
			{
				items = jaloSession.getItems(PK.parse(pkStrings));
				fail("wrong pk did not throw JaloItemNotFoundException");
			}
			catch (final JaloItemNotFoundException e)
			{
				// fine
			}

			// test PLA-5127
			if (Registry.getCurrentTenant().getDataSource().getMaxPreparedParameterCount() != -1)
			{
				final int max = Registry.getCurrentTenant().getDataSource().getMaxPreparedParameterCount();
				LOG.info("--------------------------------------------------------------------------------------------------------");
				LOG.info("--- database is limiting prepated statement parameters to " + max + " - testing JaloSession.getItems() ");
				LOG.info("--------------------------------------------------------------------------------------------------------");

				final PK[] pks = new PK[max * 3];
				for (int i = 0; i < max * 3; i++)
				{
					pks[i] = PK.createCounterPK(Constants.TC.Address);
				}

				final Collection coll = jaloSession.getItems(jaloSession.getSessionContext(), Arrays.asList(pks), true);
				assertTrue(coll.isEmpty());
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error : " + e);
		}
	}

}
