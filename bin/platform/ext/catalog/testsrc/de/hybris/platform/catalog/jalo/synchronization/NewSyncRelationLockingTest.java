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
package de.hybris.platform.catalog.jalo.synchronization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.jalo.synchronization.ArrayBasedRelationLockHolder.RelationAttributeInfo;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.testframework.HybrisJUnit4ClassRunner;
import de.hybris.platform.testframework.RunListeners;
import de.hybris.platform.testframework.runlistener.LogRunListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(HybrisJUnit4ClassRunner.class)
@RunListeners(
{ LogRunListener.class })
public class NewSyncRelationLockingTest
{
	private static final int ITEMS_SIZE = 20;
	private static final int WORKER_SIZE = 6;

	List<PK> itemPKs;
	ArrayBasedRelationLockHolder lockHolder;
	PK fakeRelationPK;

	@Before
	public void setUp() throws ConsistencyCheckException
	{
		this.itemPKs = new ArrayList<PK>(ITEMS_SIZE);
		for (int i = 0; i < ITEMS_SIZE; i++)
		{
			itemPKs.add(PK.createUUIDPK(Constants.TC.Title));
		}
		this.fakeRelationPK = PK.createFixedUUIDPK(Constants.TC.Link, 22);

		this.lockHolder = new ArrayBasedRelationLockHolder(WORKER_SIZE);
	}

	@Test
	public void testNoBlock()
	{
		final PK itemPK1 = itemPKs.get(0);
		final PK itemPK2 = itemPKs.get(1);

		final Set<PK> item1Refs = new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(2), itemPKs.get(3)));
		final Set<PK> item2Refs = new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(4), itemPKs.get(5)));

		// same relation, same end, no overlapping references
		final RelationAttributeInfo info1 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info2 = new TestInfo(fakeRelationPK, true, itemPK2, item2Refs);

		assertBothLockableAndRelease(info1, info2);

		// different relation, everything else is same
		final RelationAttributeInfo info3 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info4 = new TestInfo(PK.createFixedUUIDPK(Constants.TC.Link, 23), true, itemPK1, item1Refs);

		assertBothLockableAndRelease(info3, info4);

		// same relation, different ends, no direct connection, no overlapping references
		final RelationAttributeInfo info5 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info6 = new TestInfo(fakeRelationPK, false, itemPK2, item2Refs);

		assertBothLockableAndRelease(info5, info6);
	}

	@Test
	public void testWaitOnOposedAttributes()
	{
		// create pk0 -> ( pk2, pk1 ) and pk1 -> (pk0, pk3, pk4 )

		final PK itemPK1 = itemPKs.get(0);
		final PK itemPK2 = itemPKs.get(1);

		final Set<PK> item1Refs = new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(2), itemPK2));
		final Set<PK> item2Refs = new LinkedHashSet<PK>(Arrays.asList(itemPK1, itemPKs.get(3), itemPKs.get(4)));

		// opposed attributes: one is source and one target and both reference each other 
		final RelationAttributeInfo info1 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info2 = new TestInfo(fakeRelationPK, false, itemPK2, item2Refs);

		assertMustWait(info1, info2);

		// opposed attributes: one is source and one target and only one references the other (actually not common but possible)
		final Set<PK> item4Refs = new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(3), itemPKs.get(4)));
		final RelationAttributeInfo info3 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info4 = new TestInfo(fakeRelationPK, false, itemPK2, item4Refs);

		assertMustWait(info3, info4);
	}

	@Test
	public void testWaitOnSameSideAttributes()
	{
		// create pk0 -> ( pk2, pk3 ) and pk1 -> (pk4, pk3, pk5 )

		final PK itemPK1 = itemPKs.get(0);
		final PK itemPK2 = itemPKs.get(1);

		final Set<PK> item1Refs = new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(2), itemPKs.get(3)));
		final Set<PK> item2Refs = new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(4), itemPKs.get(3), itemPKs.get(5)));

		final RelationAttributeInfo info1 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info2 = new TestInfo(fakeRelationPK, true, itemPK2, item2Refs);

		assertMustWait(info1, info2);

		final RelationAttributeInfo info3 = new TestInfo(fakeRelationPK, false, itemPK1, item1Refs);
		final RelationAttributeInfo info4 = new TestInfo(fakeRelationPK, false, itemPK2, item2Refs);

		assertMustWait(info3, info4);
	}

	@Test
	public void testNoLockOnEmptyReferences()
	{
		// create pk0 -> ( ) and pk1 -> ()

		final PK itemPK1 = itemPKs.get(0);
		final PK itemPK2 = itemPKs.get(1);

		final Set<PK> item1Refs = Collections.EMPTY_SET;
		final Set<PK> item2Refs = Collections.EMPTY_SET;

		// same side attributes
		final RelationAttributeInfo info1 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info2 = new TestInfo(fakeRelationPK, true, itemPK2, item2Refs);

		assertBothLockableAndRelease(info1, info2);

		// opposed attributes
		final RelationAttributeInfo info3 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info4 = new TestInfo(fakeRelationPK, false, itemPK2, item2Refs);

		assertBothLockableAndRelease(info3, info4);

		// now one attribute has a non-empty reference set
		final Set<PK> nonEmptyRefs = new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(2), itemPKs.get(3), itemPKs.get(4)));

		// same side attributes
		final RelationAttributeInfo info5 = new TestInfo(fakeRelationPK, true, itemPK1, nonEmptyRefs);
		final RelationAttributeInfo info6 = new TestInfo(fakeRelationPK, true, itemPK2, item2Refs);

		assertBothLockableAndRelease(info5, info6);

		// opposed attributes
		final RelationAttributeInfo info7 = new TestInfo(fakeRelationPK, true, itemPK1, item1Refs);
		final RelationAttributeInfo info8 = new TestInfo(fakeRelationPK, false, itemPK2, nonEmptyRefs);

		assertBothLockableAndRelease(info7, info8);
	}

	@Test
	public void testBlockOnWait() throws InterruptedException
	{
		// a:b  pk0->(pk1,pk2) 
		final TestInfo testInfo = new TestInfo(fakeRelationPK, true, itemPKs.get(0), new LinkedHashSet<PK>(Arrays.asList(
				itemPKs.get(1), itemPKs.get(2))));
		// b:a  pk1<-(pk0,pk3)
		final TestInfo testInfo2 = new TestInfo(fakeRelationPK, false, itemPKs.get(1), new LinkedHashSet<PK>(Arrays.asList(itemPKs
				.get(0))));

		final int workerNumber = 0;

		assertTrue(lockHolder.lockRelationAttribute(workerNumber, testInfo, false));

		final AtomicBoolean threadLockresult = new AtomicBoolean(false);

		final Thread thread = new RegistrableThread("NewSyncRelationLockingTestThread")
		{
			final int workerNumber = 1;

			@Override
			public void internalRun()
			{
				// this should block until worker 0 is released
				final boolean gotLock;
				threadLockresult.set(gotLock = lockHolder.lockRelationAttribute(workerNumber, testInfo2, true));
				if (gotLock)
				{
					lockHolder.releaseAttributeLock(workerNumber);
				}
			}
		};
		thread.start();

		// wait for 2 seconds
		Thread.sleep(2000);

		// check that thread did not get the lock by now
		assertFalse(threadLockresult.get());
		assertTrue(thread.isAlive());

		// now release lock -> this should make t continue
		lockHolder.releaseAttributeLock(workerNumber);

		// wait at most 5 seconds for locking thread to die
		thread.join(5000);

		assertFalse("locking thread has not finished", thread.isAlive());
		assertTrue("locking thread did not get lock", threadLockresult.get());
	}

	@Test
	public void testConcurrentLocking() throws InterruptedException
	{
		// All attributes are locking at least one other. Therefore no two
		// threads must be holding a lock at the same time. We check that by
		// 

		final RelationAttributeInfo[] attributeInfos = new RelationAttributeInfo[5];
		final AtomicInteger[] stackCounters = new AtomicInteger[attributeInfos.length];

		// a:b:d  pk0->(pk1,pk2) 
		attributeInfos[0] = new TestInfo(fakeRelationPK, true, itemPKs.get(0), new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(1),
				itemPKs.get(2))));
		// b:a:d  pk1<-(pk0,pk3,pk4)
		attributeInfos[1] = new TestInfo(fakeRelationPK, false, itemPKs.get(1), new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(0),
				itemPKs.get(3), itemPKs.get(4))));
		// c:e  pk4->(pk5,pk6,pk7)
		attributeInfos[2] = new TestInfo(fakeRelationPK, true, itemPKs.get(4), new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(5),
				itemPKs.get(6), itemPKs.get(7))));
		// d:a:b  pk8->(pk9,pk2,pk1)
		attributeInfos[3] = new TestInfo(fakeRelationPK, true, itemPKs.get(8), new LinkedHashSet<PK>(Arrays.asList(itemPKs.get(9),
				itemPKs.get(2), itemPKs.get(1))));
		// e:c  pk7<-(pk10,pk4)
		attributeInfos[4] = new TestInfo(fakeRelationPK, false, itemPKs.get(7), new LinkedHashSet<PK>(Arrays.asList(
				itemPKs.get(10), itemPKs.get(4))));

		// group a:b:d
		stackCounters[0] = stackCounters[1] = stackCounters[3] = new AtomicInteger(0);
		// group c:e
		stackCounters[2] = stackCounters[4] = new AtomicInteger(0);

		assertMustWait(attributeInfos[0], attributeInfos[1]);
		assertMustWait(attributeInfos[0], attributeInfos[3]);
		assertMustWait(attributeInfos[1], attributeInfos[3]);
		assertMustWait(attributeInfos[2], attributeInfos[4]);

		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch end = new CountDownLatch(attributeInfos.length);
		final AtomicBoolean finished = new AtomicBoolean(false);

		final AtomicInteger errorCounter = new AtomicInteger(0);

		for (int i = 0; i < attributeInfos.length; i++)
		{
			final int workerNumber = i;
			new RegistrableThread("NewSyncRelationLockingTestThread-" + i)
			{
				@Override
				public void internalRun()
				{
					try
					{
						start.await();

						do
						{
							if (lockHolder.lockRelationAttribute(workerNumber, attributeInfos[workerNumber], false))
							{
								// add one to stack counter -> should be 1 now
								final int current = stackCounters[workerNumber].incrementAndGet();
								// check for error -> increase error counter if we found one
								if (current != 1)
								{
									errorCounter.incrementAndGet();
								}
								// pretend to do something and let others run
								Thread.yield();
								// remove one from stack counter -> should be 0 now
								final int after = stackCounters[workerNumber].decrementAndGet();
								// check for error again -> increase error counter if we found one
								if (current == 1 && after != 0)
								{
									errorCounter.incrementAndGet();
								}
								// release lock again
								lockHolder.releaseAttributeLock(workerNumber);
							}
							else
							{
								Thread.yield();
							}

						}
						while (!finished.get());
					}
					catch (final InterruptedException e)
					{
						e.printStackTrace();
					}
					finally
					{
						end.countDown();
					}
				}
			}.start();
		}
		// let all threads start at once
		start.countDown();
		// wait for some seconds
		Thread.sleep(10 * 1000);
		// signal end
		finished.set(true);
		// wait for threads
		end.await();
		for (int i = 0; i < stackCounters.length; i++)
		{
			assertEquals("worker " + i + " stack count is not zero after threads have died", 0, stackCounters[i].get());
		}
		assertEquals("threads found " + errorCounter.get() + " errors", 0, errorCounter.get());
	}

	private void assertMustWait(final RelationAttributeInfo info1, final RelationAttributeInfo info2)
	{
		try
		{
			// worker 0 should be able to lock attribute1
			assertTrue(lockHolder.lockRelationAttribute(0, info1, false));
			// worker 1 must not be able to lock attribute2
			assertFalse(lockHolder.lockRelationAttribute(1, info2, false));

			// release attribute1
			lockHolder.releaseAttributeLock(0);

			// now worker 1 should be able to lock attribute2
			assertTrue(lockHolder.lockRelationAttribute(1, info2, false));

			// worker 2 must now not be able to lock attribute1 since attribute2 has the lock 
			assertFalse(lockHolder.lockRelationAttribute(0, info1, false));
		}
		finally
		{
			lockHolder.releaseAttributeLock(0);
			lockHolder.releaseAttributeLock(1);
		}
	}


	private void assertBothLockableAndRelease(final RelationAttributeInfo info1, final RelationAttributeInfo info2)
	{
		try
		{
			// worker 0 should be able to lock attribute1
			assertTrue(lockHolder.lockRelationAttribute(0, info1, false));
			// worker 1 should be able to lock attribute2
			assertTrue(lockHolder.lockRelationAttribute(1, info2, false));
		}
		finally
		{
			lockHolder.releaseAttributeLock(0);
			lockHolder.releaseAttributeLock(1);
		}
	}

	private class TestInfo implements ArrayBasedRelationLockHolder.RelationAttributeInfo
	{
		private final PK relPK;
		private final boolean source;
		private final PK parentPK;
		private final Set<PK> referencePKs;

		TestInfo(final PK relationPK, final boolean source, final PK parentPK, final Set<PK> referncePKs)
		{
			this.relPK = relationPK;
			this.source = source;
			this.parentPK = parentPK;
			this.referencePKs = referncePKs;
		}

		@Override
		public PK getParentPK()
		{
			return parentPK;
		}

		@Override
		public Set<PK> getReferencedPKs()
		{
			return referencePKs;
		}

		@Override
		public PK getRelationTypePK()
		{
			return relPK;
		}

		@Override
		public boolean isSource()
		{
			return source;
		}
	}
}
