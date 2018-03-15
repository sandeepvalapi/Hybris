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
package de.hybris.platform.regioncache.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/EHCacheRegionExclusiveComputationTest-context.xml" }, inheritLocations = false)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class EHCacheRegionExclusiveComputationTest extends AbstractCacheControllerOneRegionTest
{
	/**
	 * Tests if: cache loads values
	 */
	@Test
	public void concurrentValueLoadTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		Assert.assertNotNull(controller);
		final AtomicLong loadCounter = new AtomicLong();
		final String[] keyNames = new String[]
		{ "A", "B", "C", "D", "E" };
		final TestThreadsHolder<Runnable> randomAccessHolder = new TestThreadsHolder<Runnable>(keyNames.length * 7,
				new RunnerCreator<Runnable>()
				{
					@Override
					public Runnable newRunner(final int threadNumber)
					{
						final CacheKey key = new TestCacheKey(keyNames[threadNumber % keyNames.length]);
						return new Runnable()
						{
							@Override
							public void run()
							{
								final int cnt[] = new int[]
								{ 0 };
								for (int i = 0; i < 1000; ++i)
								{
									try
									{
										controller.getWithLoader(key, new CacheValueLoader()
										{
											@Override
											public Object load(final CacheKey key) throws CacheValueLoadException
											{
												++cnt[0];
												return key.getTypeCode() + ":" + keyNames[threadNumber % keyNames.length];
											}
										}); // , region.getName());
									}
									catch (final Exception e)
									{
										throw new Error(e);
									}
								}
								loadCounter.addAndGet(cnt[0]);
							}
						};
					}
				});

		randomAccessHolder.startAll();
		Assert.assertTrue(randomAccessHolder.waitForAll(60, TimeUnit.SECONDS));

		Assert.assertEquals(keyNames.length, loadCounter.longValue());

		final CacheStatistics stats = region.getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		Assert.assertEquals(keyNames.length, stats.getMisses());
		Assert.assertEquals(keyNames.length * 7 * 1000 - keyNames.length, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

		final Object type = new TestCacheKey("").getTypeCode();
		Assert.assertEquals(keyNames.length, stats.getMisses(type));
		Assert.assertEquals(keyNames.length * 7 * 1000 - keyNames.length, stats.getHits(type));
		Assert.assertEquals(0, stats.getEvictions(type));
	}
}
