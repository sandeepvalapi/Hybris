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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.StopWatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class PKTest extends HybrisJUnit4Test
{
	static final Logger LOG = Logger.getLogger(PKTest.class.getName());

	private static final int TC_BITS_COUNT = 15;
	private static final long MARKER_BIT_AT_43 = 0x0000080000000000l;


	@Test
	public void testBit43PreservationPLA11587()
	{
		// one counter leading to a long having bit 43 set to 1
		final long counter1 = (MARKER_BIT_AT_43 >> TC_BITS_COUNT) + 1;
		// and one counter leading to a long having bit 43 set to 0
		final long counter2 = 1;

		final PK pk1 = PK.createFixedCounterPK(1, counter1);
		final PK pk2 = PK.createFixedCounterPK(1, counter2);

		assertEquals(1, pk1.getTypeCode());
		assertEquals(1, pk2.getTypeCode());

		assertEquals(counter1, pk1.getCounter());
		assertEquals(counter2, pk2.getCounter());

		assertFalse(pk1.equals(pk2));
	}

	@Test
	public void testGeneralFormat() throws Exception
	{
		final PK pk = PK.createUUIDPK(10);
		LOG.info("Registry.getPreferredClusterID(): " + Integer.valueOf(Registry.getPreferredClusterID()));
		LOG.info("pk.getClusterID(): " + Integer.valueOf(pk.getClusterID()));
		LOG.info("Registry.isStandaloneMode(): " + Registry.isStandaloneMode());
	}

	@Test
	public void testHash() throws Exception
	{
		final int COUNT = 1500;

		final Map<PK, String> pkMap = new HashMap<PK, String>();
		final List<PK> pkList = new ArrayList<PK>(COUNT);

		createPKs(pkMap, pkList, COUNT);

		final StopWatch w = new StopWatch("hash");
		for (int j = 0; j < 100; j++)
		{
			for (int i = 0; i < COUNT; i++)
			{
				pkMap.get(pkList.get(i));
			}
		}
		w.stop();
	}

	@Test
	public void samePK() throws Exception
	{
		final HashSet s = new HashSet();
		for (int i = 0; i < 200000; i++)
		{
			final PK p = PK.createUUIDPK(101);
			if (!s.add(p))
			{
				fail("PK " + p + " was already in the map");
			}
		}
	}

	@Test
	public void testManyPKs() throws Exception
	{
		final int CNT = 100000;
		for (int i = 0; i < CNT; i++)
		{
			PK.createUUIDPK(111);
		}

		LOG.info("PK.getCurrentTimeOffsetInMillis(): " + Long.valueOf(PK.getCurrentTimeOffsetInMillis()));
	}

	@Test
	public void testMultiThreadedCreation()
	{
		// run several times to increase error probability without hitting OOM
		for (int i = 0; i < 10; i++)
		{
			testMultiThreadedCreation(100, 10, 2 * 1000 * 1000);
		}
	}

	private void testMultiThreadedCreation(final int THREADS, final int MAX_RUN_TIME_SECONDS, final int MAX_PKS)
	{

		final TestThreadsHolder<PKCreationRunner> threads = new TestThreadsHolder<PKTest.PKCreationRunner>(THREADS, false)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				final int myMaxPKs = MAX_PKS / THREADS;
				return new PKCreationRunner(myMaxPKs);
			}
		};

		threads.startAll();

		// 1. wait for threads to finish on their own 
		if (!threads.waitForAll(MAX_RUN_TIME_SECONDS, TimeUnit.SECONDS))
		{
			// if this did not happen stop them directly ( via thread interrupt )
			threads.stopAndDestroy(10);
		}

		assertAllCreatedPKsUnique(threads.getRunners());
	}

	private void assertAllCreatedPKsUnique(final Collection<PKCreationRunner> runners)
	{
		final HashSet<PK> uniqueSet = new HashSet<PK>();
		int duplicates = 0;
		for (final PKCreationRunner r : runners)
		{
			if (CollectionUtils.isNotEmpty(r.creaedPKs))
			{
				for (final PK pk : r.creaedPKs)
				{
					if (!uniqueSet.add(pk))
					{
						duplicates++;
					}
				}
			}
		}
		assertEquals("created duplicate PKs using " + runners.size() + " runners", 0, duplicates);
	}

	static class PKCreationRunner implements Runnable
	{
		volatile List<PK> creaedPKs;
		final int maxPKs;
		final Tenant tenant;

		// if max < 0 this runner will created unlimited PKs
		PKCreationRunner(final int maxPKs)
		{
			this.maxPKs = maxPKs;
			this.tenant = Registry.getCurrentTenantNoFallback();
		}

		@Override
		public void run()
		{
			Registry.setCurrentTenant(tenant);
			final Thread thread = Thread.currentThread();

			final List<PK> newOnes = new LinkedList<PK>();
			int count = 0;

			while (!thread.isInterrupted() && (maxPKs < 0 || count < maxPKs))
			{
				try
				{
					newOnes.add(PK.createCounterPK(5555));
				}
				catch (final IllegalStateException e)
				{
					if (e.getCause() instanceof InterruptedException)
					{
						// fine
						Assert.assertTrue(thread.isInterrupted());
					}
					else
					{
						throw e;
					}
				}
				count++;
			}

			creaedPKs = newOnes; // volatile write at the end !!!
		}
	}


	private void createPKs(final Map<PK, String> pkMap, final List<PK> pkList, final int count)
	{
		for (int i = 0; i < count; i++)
		{
			final PK pk = PK.createUUIDPK(1);
			//pk.hash=(int)((Math.random()*100000000)+1);
			pkMap.put(pk, "1");
			pkList.add(i, pk);
		}
	}
}
