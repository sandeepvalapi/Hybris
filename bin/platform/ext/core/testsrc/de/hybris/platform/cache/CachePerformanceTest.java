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
package de.hybris.platform.cache;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.config.ConfigIntf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bethecoder.ascii_table.ASCIITable;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;


/**
 * RegionCache performance tests
 */
@PerformanceTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/EHCacheRegionPerformanceTest-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CachePerformanceTest
{
	private static final String REGION_CACHE_NAME = "testRegionCache";
	private static final String REGION_CACHE_KEY_NAME = "testRegionCacheKey";
	private static final String OLD_CACHE_KEY_NAME = "testOldCacheKey";
	private static final String CACHE_KEY_NAME = "testCacheKey";

	private static final int THREADS = 30;
	private static final int TOTALHITS = THREADS * 500 * 1000;
	private static final int MAX_WAIT_SECONDS = 240;
	private static final int FILL = 10000;

	@Mock
	Tenant tenantMock;

	@Mock
	ConfigIntf configMock;

	@After
	public void clean()
	{
		CacheManager.getInstance().clearAll();
		System.gc();
	}


	@Test
	public void testOldCache()
	{
		final BaseCacheYFastFIFOMapStub oldCacheMap = createOldCacheMap();

		fillOldCacheMap(oldCacheMap, FILL);
		assertEquals(FILL, oldCacheMap.getMaxReachedSize());

		writeResultTable("old cache", 100, FILL, TOTALHITS, THREADS,
				executeMultithreadedCacheAccess(oldCacheMap, FILL, true, THREADS, TOTALHITS, MAX_WAIT_SECONDS));

		writeResultTable("old cache", 0, FILL, TOTALHITS, THREADS,
				executeMultithreadedCacheAccess(oldCacheMap, FILL, false, THREADS, TOTALHITS, MAX_WAIT_SECONDS));
	}

	@Test
	public void testRegionCachePerformance()
	{
		final Cache regionCacheMap = createRegionCacheMap();

		fillRegionCacheMap(regionCacheMap, FILL);
		assertEquals(FILL, regionCacheMap.getKeys().size());

		writeResultTable("region cache", 100, FILL, TOTALHITS, THREADS,
				executeMultithreadedCacheAccess(regionCacheMap, FILL, true, THREADS, TOTALHITS, MAX_WAIT_SECONDS));

		writeResultTable("region cache", 0, FILL, TOTALHITS, THREADS,
				executeMultithreadedCacheAccess(regionCacheMap, FILL, false, THREADS, TOTALHITS, MAX_WAIT_SECONDS));
	}


	@Test
	public void testConcurrentHashMap()
	{
		final ConcurrentHashMap<String, Object> chMap = new ConcurrentHashMap<String, Object>(FILL * 2, 0.75f, 8);

		fillCacheMap(chMap, FILL);
		assertEquals(FILL, chMap.size());

		writeResultTable("ConcurrentHashMap", 100, FILL, TOTALHITS, THREADS,
				executeMultithreadedCacheAccess(chMap, FILL, true, THREADS, TOTALHITS, MAX_WAIT_SECONDS));

		writeResultTable("ConcurrentHashMap", 0, FILL, TOTALHITS, THREADS,
				executeMultithreadedCacheAccess(chMap, FILL, false, THREADS, TOTALHITS, MAX_WAIT_SECONDS));
	}

	@Test
	public void testConcurrentLinkedHashMap()
	{
		final ConcurrentMap<String, Object> chMap = new ConcurrentLinkedHashMap.Builder<String, Object>()
				.maximumWeightedCapacity(FILL).concurrencyLevel(8).initialCapacity(FILL).build();

		fillCacheMap(chMap, FILL);
		assertEquals(FILL, chMap.size());

		writeResultTable("ConcurrentLinkedHashMap", 100, FILL, TOTALHITS, THREADS,
				executeMultithreadedCacheAccess(chMap, FILL, true, THREADS, TOTALHITS, MAX_WAIT_SECONDS));

		writeResultTable("ConcurrentLinkedHashMap", 0, FILL, TOTALHITS, THREADS,
				executeMultithreadedCacheAccess(chMap, FILL, false, THREADS, TOTALHITS, MAX_WAIT_SECONDS));
	}


	static void fillRegionCacheMap(final Cache regionCacheMap, final int size)
	{
		for (int i = 0; i < size; i++)
		{
			regionCacheMap.putQuiet(new Element(REGION_CACHE_KEY_NAME + i, "RegionCacheValue" + i));
		}
	}

	static void fillOldCacheMap(final BaseCacheYFastFIFOMapStub oldCacheMap, final int size)
	{
		for (int i = 0; i < size; i++)
		{
			oldCacheMap.put(OLD_CACHE_KEY_NAME + i, "OldCacheValue" + i);
		}
	}

	private void fillCacheMap(final Map<String, Object> cacheMap, final int size)
	{
		for (int i = 0; i < size; i++)
		{
			cacheMap.put(CACHE_KEY_NAME + i, "CacheValue" + i);
		}
	}

	BaseCacheYFastFIFOMapStub createOldCacheMap()
	{
		MockitoAnnotations.initMocks(this);

		when(tenantMock.getConfig()).thenReturn(configMock);
		when(tenantMock.getTenantID()).thenReturn("master");
		when(configMock.getParameter("cache.main.map")).thenReturn("de.hybris.platform.cache.BaseCacheYFastFIFOMapStub");
		when(configMock.getParameter("cache.experimental.cachemap")).thenReturn(
				"de.hybris.platform.cache.BaseCacheYFastFIFOMapStub");
		//
		when(configMock.getParameter("cache.evictionpolicy")).thenReturn("FIFO");
		when(Integer.valueOf(configMock.getInt("cache.concurrency.level", 8))).thenReturn(Integer.valueOf(8));

		//init oldCacheMap
		final CacheBaseStub cacheBase = new CacheBaseStub(tenantMock, FILL + 1000);
		return new BaseCacheYFastFIFOMapStub(cacheBase, FILL + 1000);
	}

	static Cache createRegionCacheMap()
	{

		//init regionCacheMap
		final CacheManager manager = CacheManager.getInstance();
		if (manager.cacheExists(REGION_CACHE_NAME))
		{
			manager.removeCache(REGION_CACHE_NAME);
		}
		final Cache regionCacheMap = new Cache(createRegionCacheConfiguration());

		manager.addCache(regionCacheMap);
		assertNotNull(regionCacheMap);

		return regionCacheMap;
	}


	private static CacheConfiguration createRegionCacheConfiguration()
	{
		final CacheConfiguration config = new CacheConfiguration();
		config.setStatistics(false);
		config.setMemoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU.toString());
		config.setMaxEntriesLocalHeap(FILL + 1000);
		config.setCopyOnRead(false);
		config.setCopyOnWrite(false);
		config.setName(REGION_CACHE_NAME);
		config.overflowToDisk(false);
		config.setEternal(true);
		config.setLogging(false);

		return config;
	}

	private void writeResultTable(final String cacheType, final int hitRate, final int fill, final int totalhits,
			final int threads, final long duration)
	{
		final String[] header =
		{ "cache type", "hit rate", "fill", "totalhits", "threads", "duration" };
		ASCIITable.getInstance().printTable(header, new String[][]
		{
		{ cacheType, hitRate + " %", String.valueOf(fill), String.valueOf(totalhits), //
				String.valueOf(threads), duration + " ms" } });
	}

	private long executeMultithreadedCacheAccess(final Object cacheMap, final int cacheFill, final boolean hitAlways,
			final int numberOfThreads, final long hitcount, final int maxWaitSeconds)
	{
		final long hitsPerThread = hitcount / numberOfThreads;
		final long hitsRemainder = hitcount % numberOfThreads;

		final de.hybris.platform.test.RunnerCreator<Runnable> runnerCreator = new de.hybris.platform.test.RunnerCreator<Runnable>()
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				final long hits = threadNumber < hitsRemainder ? hitsPerThread + 1 : hitsPerThread;
				if (cacheMap instanceof Cache)
				{
					return new RegionCacheReader(threadNumber, hits, cacheFill, hitAlways, (Cache) cacheMap);
				}
				else if (cacheMap instanceof BaseCacheYFastFIFOMapStub)
				{
					return new OldCacheReader(threadNumber, hits, cacheFill, hitAlways, (BaseCacheYFastFIFOMapStub) cacheMap);
				}
				else if (cacheMap instanceof Map)
				{
					return new PlainMapReader(threadNumber, hits, cacheFill, hitAlways, (Map<String, Object>) cacheMap);
				}
				else
				{
					throw new IllegalArgumentException("unknown cache map type " + cacheMap);
				}
			}
		};

		final TestThreadsHolder workerThreads = new TestThreadsHolder<Runnable>(numberOfThreads, runnerCreator);

		workerThreads.startAll();
		assertTrue("not all workers finished after " + maxWaitSeconds + " seconds", workerThreads.waitAndDestroy(maxWaitSeconds));

		return workerThreads.getStartToFinishMillis();
	}

	private static abstract class AbstractCacheReader implements Runnable
	{
		final int number;
		final long hits;
		final int cacheFill;
		final int sleepMs;
		final boolean hitAlways;

		public AbstractCacheReader(final int number, final long hits, final int cacheFill, final boolean hitAlways,
				final int sleepMs)
		{
			this.number = number;
			this.hits = hits;
			this.cacheFill = cacheFill;
			this.hitAlways = hitAlways;
			this.sleepMs = sleepMs;
		}

		@Override
		final public void run()
		{
			for (int h = 0; h < hits && !Thread.currentThread().isInterrupted(); h++)
			{
				if (hitAlways)
				{
					doHitAlways();
				}
				else
				{
					doHitNever();
				}
				if (sleepMs > 0)
				{
					try
					{
						Thread.sleep(sleepMs);
					}
					catch (final InterruptedException e)
					{
						break;
					}
				}
			}
		}

		abstract void doHitAlways();

		abstract void doHitNever();
	}

	private static class RegionCacheReader extends AbstractCacheReader
	{
		private final Cache cacheMap;

		public RegionCacheReader(final int number, final long hits, final int cacheFill, final boolean hitAlways,
				final Cache cacheMap)
		{
			super(number, hits, cacheFill, hitAlways, 0);
			this.cacheMap = cacheMap;
		}

		@Override
		void doHitAlways()
		{
			final Element elem = cacheMap.get(REGION_CACHE_KEY_NAME + RandomUtils.nextInt(cacheFill));
			assertNotNull(elem);
		}

		@Override
		void doHitNever()
		{
			final Element notExistingElem = cacheMap.get("RegionCacheKeyNotInMap");
			assertNull(notExistingElem);
		}
	}


	static class OldCacheReader extends AbstractCacheReader
	{
		final BaseCacheYFastFIFOMapStub cacheMap;

		OldCacheReader(final int number, final long hits, final int cacheFill, final boolean hitAlways,
				final BaseCacheYFastFIFOMapStub cacheMap)
		{
			super(number, hits, cacheFill, hitAlways, 0);
			this.cacheMap = cacheMap;
		}

		@Override
		void doHitAlways()
		{
			final Object elem = cacheMap.get(OLD_CACHE_KEY_NAME + RandomUtils.nextInt(cacheFill));
			assertNotNull(elem);
		}

		@Override
		void doHitNever()
		{
			final Object notExistingElem = cacheMap.get("OldCacheKeyNotInMap");
			assertNull(notExistingElem);
		}
	}

	static class PlainMapReader extends AbstractCacheReader
	{
		final Map<String, Object> cacheMap;

		PlainMapReader(final int number, final long hits, final int cacheFill, final boolean hitAlways,
				final Map<String, Object> cacheMap)
		{
			super(number, hits, cacheFill, hitAlways, 0);
			this.cacheMap = cacheMap;
		}

		@Override
		void doHitAlways()
		{
			final Object elem = cacheMap.get(CACHE_KEY_NAME + RandomUtils.nextInt(cacheFill));
			assertNotNull(elem);
		}

		@Override
		void doHitNever()
		{
			final Object notExistingElem = cacheMap.get("CHMCacheKeyNotInMap");
			assertNull(notExistingElem);
		}
	}

}
