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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;
import de.hybris.platform.tx.InvalidationSet;
import de.hybris.platform.tx.InvalidationSet.Invalidation;
import de.hybris.platform.tx.InvalidationSet.InvalidationProcessor;
import de.hybris.platform.tx.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;


/**
 * Tests {@link InvalidationSet} functionality. That class is being crucial part of {@link Transaction} since it
 * 'records' and 'simulates' invalidations that are delayed until the end of a transaction.
 */
@UnitTest
public class InvalidationSetTest
{
	@Test
	public void testEmptySet()
	{
		final InvalidationSet set = InvalidationSet.EMPTY_SET;

		assertTrue(set.isEmpty());

		assertFalse(set.isInvalidated(asKey("a", "b", "c")));
		assertFalse(set.isInvalidated(asKey("a", "b")));
		assertFalse(set.isInvalidated(asKey("a")));

		// test operations that throw exceptions
		try
		{
			set.addInvalidation(asKey("a", "b", "c"), 2, 0);
			fail("UnsupportedOperationException expected");
		}
		catch (final UnsupportedOperationException e)
		{
			//fine
		}
		try
		{
			set.delayInvalidation(asKey("a", "b", "c"), 2, 0);
			fail("UnsupportedOperationException expected");
		}
		catch (final UnsupportedOperationException e)
		{
			//fine
		}
		try
		{
			set.delayRollbackInvalidation(asKey("a", "b", "c"), 2, 0);
			fail("UnsupportedOperationException expected");
		}
		catch (final UnsupportedOperationException e)
		{
			//fine
		}
		// now these are no-ops
		set.executeDelayedInvalidationsGlobally();
		set.executeDelayedInvalidationsLocally();
		set.executeDelayedRollbackInvalidationsLocally();
	}


	@Test
	public void testSimpleRecording()
	{
		final TestInvalidationTarget target = new TestInvalidationTarget();
		final PassThroughTestInvalidationProcessor processor = new PassThroughTestInvalidationProcessor(target);
		final InvalidationSet set = new InvalidationSet(processor);

		assertTrue(set.isEmpty());

		final Object[] key = asKey("a", "b", "c");
		// invalidate [a,b,c] -> *
		set.delayInvalidation(key, 2, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);
		assertEquals(Collections.EMPTY_LIST, target.recordeInvalidations);

		assertFalse(set.isEmpty());
		assertInvalidated(set, "a", "b", "c");
		assertInvalidated(set, "a", "b", "c", "d");

		assertNotInvalidated(set, "a", "b", "x");
		assertNotInvalidated(set, "a", "b");
		assertNotInvalidated(set, "a");

		set.executeDelayedInvalidationsGlobally();
		assertEquals(Arrays.asList(InvalidationSet.createInvalidation(key, -1, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED)),
				target.recordeInvalidations);
	}

	@Test
	public void testHJMPInvalidation()
	{
		final TestInvalidationTarget target = new TestInvalidationTarget();

		final TestInvalidationProcessor processor = new TestInvalidationProcessor(//
				target, //
				new DummyHJMPInvalidationListener(), new DummyFlexibleSearchInvalidationListener()//
		);
		final InvalidationSet set = new InvalidationSet(processor)
		{
			@Override
			protected InvalidationTarget getRealInvalidationTarget()
			{
				return target;
			}
		};

		final Object[] entityKey = asKey(Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, "1", PK.createFixedPK(1, 1));
		final Object[] queryKey = asKey(Cache.CACHEKEY_FLEXSEARCH, "1", new Object() /* some object - doesn't matter */);
		final Object[] otherQueryKey = asKey(Cache.CACHEKEY_FLEXSEARCH, "5", new Object() /* some object - doesn't matter */);

		set.delayInvalidation(entityKey, 3, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);

		assertEquals("invalidations have not been delayed", Collections.EMPTY_LIST, target.recordeInvalidations);
		assertInvalidated(set, entityKey);
		assertInvalidated(set, queryKey);
		assertNotInvalidated(set, otherQueryKey);

		final Object[] entityKey2 = asKey(Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, "1", PK.createFixedPK(1, 2));
		final Object[] queryKey2 = asKey(Cache.CACHEKEY_FLEXSEARCH, "1", new Object() /* some object - doesn't matter */);
		final Object[] otherQueryKey2 = asKey(Cache.CACHEKEY_FLEXSEARCH, "123", new Object() /*
																														  * some object - doesn't
																														  * matter
																														  */);

		set.delayInvalidation(entityKey2, 3, AbstractCacheUnit.INVALIDATIONTYPE_REMOVED);

		assertEquals("invalidations have not been delayed", Collections.EMPTY_LIST, target.recordeInvalidations);
		assertInvalidated(set, entityKey);
		assertInvalidated(set, queryKey);
		assertNotInvalidated(set, otherQueryKey);
		assertInvalidated(set, entityKey2);
		assertInvalidated(set, queryKey2);
		assertNotInvalidated(set, otherQueryKey2);


		set.executeDelayedInvalidationsGlobally();

		final List<Invalidation> expected = Arrays.asList(//
				InvalidationSet.createInvalidation(entityKey, -1, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED), //
				InvalidationSet.createInvalidation(Arrays.copyOf(queryKey, queryKey.length - 1), -1,
						AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED), //
				InvalidationSet.createInvalidation(entityKey2, -1, AbstractCacheUnit.INVALIDATIONTYPE_REMOVED), //
				InvalidationSet.createInvalidation(Arrays.copyOf(queryKey2, queryKey.length - 1), -1,
						AbstractCacheUnit.INVALIDATIONTYPE_REMOVED) //
				);

		assertEquals(expected, target.recordeInvalidations);
	}

	private void assertInvalidated(final InvalidationSet set, final Object... key)
	{
		final Object[] keys = asKey(key);
		assertTrue("key " + Arrays.deepToString(keys) + " is not invalidated", set.isInvalidated(keys));
	}

	private void assertNotInvalidated(final InvalidationSet set, final Object... key)
	{
		final Object[] keys = asKey(key);
		assertFalse("key " + Arrays.deepToString(keys) + " is wrongly invalidated", set.isInvalidated(keys));
	}

	private static class DummyHJMPInvalidationListener implements InvalidationListener
	{
		@Override
		public void keyInvalidated(final Object[] key, final int invalidationType, final InvalidationTarget target,
				final RemoteInvalidationSource remoteSrc)
		{
			target.invalidate(key, invalidationType);
		}
	}

	private static class DummyFlexibleSearchInvalidationListener implements InvalidationListener
	{
		@Override
		public void keyInvalidated(final Object[] key, final int invalidationType, final InvalidationTarget target,
				final RemoteInvalidationSource remoteSrc)
		{
			final Object[] fsKey = new Object[2];
			fsKey[0] = Cache.CACHEKEY_FLEXSEARCH;
			fsKey[1] = key[2]; //the qualified bean typecode
			target.invalidate(fsKey, invalidationType);
		}
	}

	private static class PassThroughTestInvalidationProcessor extends TestInvalidationProcessor
	{
		PassThroughTestInvalidationProcessor(final InvalidationTarget realTarget)
		{
			super(realTarget, new InvalidationListener()
			{
				@Override
				public void keyInvalidated(final Object[] key, final int invalidationType, final InvalidationTarget target,
						final RemoteInvalidationSource remoteSrc)
				{
					target.invalidate(key, invalidationType);
				}
			});
		}
	}

	private static class TestInvalidationTarget implements InvalidationTarget
	{
		private final List<Invalidation> recordeInvalidations = new ArrayList<Invalidation>();

		@Override
		public void invalidate(final Object[] key, final int invalidationType)
		{
			recordeInvalidations.add(InvalidationSet.createInvalidation(key, -1, invalidationType));
		}
	}

	private static class TestInvalidationProcessor implements InvalidationProcessor
	{
		private final List<Invalidation> recordeGlobalInvalidations = new ArrayList<Invalidation>();
		private final List<Invalidation> recordeLocalInvalidations = new ArrayList<Invalidation>();

		private final List<InvalidationListener> listeners = new ArrayList<InvalidationListener>();
		private final InvalidationTarget realTarget;

		TestInvalidationProcessor(final InvalidationTarget realTarget, final InvalidationListener... listeners)
		{
			this.realTarget = realTarget;
			if (!ArrayUtils.isEmpty(listeners))
			{
				for (final InvalidationListener l : listeners)
				{
					addListener(l);
				}
			}
		}

		@Override
		public void invalidateGlobally(final Invalidation inv)
		{
			recordeGlobalInvalidations.add(inv);
			processListeners(inv, realTarget);
		}

		@Override
		public void invalidateLocally(final Invalidation inv, final InvalidationTarget target)
		{
			recordeLocalInvalidations.add(inv);
			processListeners(inv, target);
		}

		private void processListeners(final Invalidation inv, final InvalidationTarget target)
		{
			for (final InvalidationListener l : listeners)
			{
				l.keyInvalidated(inv.getKey(), inv.getInvalidationType(), target, null);
			}
		}

		void addListener(final InvalidationListener invalidationListener)
		{
			listeners.add(invalidationListener);
		}

		void removeListener(final InvalidationListener invalidationListener)
		{
			listeners.remove(invalidationListener);
		}
	}

	private Object[] asKey(final Object... elements)
	{
		return elements;
	}
}
