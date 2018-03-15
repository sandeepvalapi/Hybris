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

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Perf;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.Test;


@PerformanceTest
public class ConcurrentPerformanceTest extends HybrisJUnit4Test
{
	ProductManager productManager;

	private static final Logger LOG = Logger.getLogger(ConcurrentPerformanceTest.class);

	@Test
	public void testParallel1() throws Exception
	{
		Unit testunit = null;
		Perf perf = null;
		try
		{
			testunit = ProductManager.getInstance().createUnit("$$test$$", "$$");
			final Unit unit = testunit;
			final int MS_PER_RUN = 1000;

			perf = new Perf(200)
			{
				@Override
				public void body() throws Exception
				{
					final int random = (int) (Math.random() * 1000000);
					unit.setProperty("test", "bla" + random);
				}
			};

			//for(int jjj=0;jjj<20;jjj++)
			{
				for (int i = 10; i <= 200; i += 10)
				{

					perf.go(MS_PER_RUN, i);
					final long executions = perf.getExecutions();
					perf.reset();
					LOG.info(i + " threads: " + executions + " executions, "
							+ Registry.getCurrentTenant().getDataSource().getNumPhysicalOpen() + " phys. DB conns open, "
							+ Registry.getCurrentTenant().getDataSource().getMillisWaitedForConnection() + " ms waited for conns");
				}
			}
		}
		finally
		{
			if (perf != null)
			{
				perf.close();
			}
			if (testunit != null)
			{
				testunit.remove();
			}
		}
	}

	@Test
	public void testParallel2() throws Exception
	{
		final int CNT = 200;
		final Unit[] unit = new Unit[CNT];
		Perf perf = null;
		try
		{
			perf = new Perf(210)
			{
				@Override
				public void body() throws Exception
				{
					final int rnd = (int) (Math.random() * CNT);
					unit[rnd].setProperty("test", "bla" + getExecutions());
				}
			};

			for (int i = 0; i < CNT; i++)
			{
				unit[i] = ProductManager.getInstance().createUnit("$$test$$", "$$" + i);
			}


			final int MS_PER_RUN = 1000;
			for (int i = 10; i <= 210; i += 50)
			{
				perf.go(MS_PER_RUN, i);
				final long executions = perf.getExecutions();
				perf.reset();
				LOG.info(i + " threads: " + executions + " executions, "
						+ Registry.getCurrentTenant().getDataSource().getNumPhysicalOpen() + " phys. DB conns open, "
						+ Registry.getCurrentTenant().getDataSource().getMillisWaitedForConnection() + " ms waited for conns");
				//LOG.info("poolsize:"+DataSourceImpl.cnt);
			}
		}
		finally
		{
			if (perf != null)
			{
				perf.close();
			}
			for (int i = 0; i < CNT; i++)
			{
				unit[i].remove();
			}

		}
	}

	// test with genericItems
	@Test
	public void testParallel3() throws Exception
	{
		final int CNT = 200;
		Perf perf = null;
		final MediaContainer[] mediaContainer = new MediaContainer[CNT];
		try
		{
			for (int i = 0; i < CNT; i++)
			{
				mediaContainer[i] = MediaManager.getInstance().createMediaContainer("$$" + i);
			}

			final int THREADS = 50;
			perf = new Perf(THREADS)
			{
				@Override
				public void body() throws Exception
				{
					final int rnd = (int) (Math.random() * CNT);
					mediaContainer[rnd].setQualifier("bla");
				}
			};
			perf.go(2000);
			LOG.info("GenericItem: setting same qualifier: " + perf.getExecutions());
			perf.close();



			perf = new Perf(THREADS)
			{
				@Override
				public void body() throws Exception
				{
					final int rnd = (int) (Math.random() * CNT);
					mediaContainer[rnd].setQualifier("bla" + getExecutions());
				}
			};
			perf.go(2000);
			LOG.info("GenericItem: setting rnd qualifier: " + perf.getExecutions());
			perf.close();


			perf = new Perf(THREADS)
			{
				@Override
				public void body() throws Exception
				{
					final int rnd = (int) (Math.random() * CNT);
					mediaContainer[rnd].getQualifier();
				}
			};
			perf.go(2000);
			LOG.info("GenericItem: getting qualifier: " + perf.getExecutions());
			perf.close();

		}
		finally
		{
			for (int i = 0; i < CNT; i++)
			{
				if (mediaContainer[i] != null)
				{
					mediaContainer[i].remove();
				}
			}

		}
	}

	// test with genericItems
	@Test
	public void testParallelCache() throws Exception
	{
		final int CNT = 1; //only one for jalo cache test
		final SavedQuery[] savedQuery = new SavedQuery[1];
		Perf perf = null;
		try
		{
			for (int i = 0; i < CNT; i++)
			{
				savedQuery[i] = FlexibleSearch.getInstance().createSavedQuery("$$" + i,
						TypeManager.getInstance().getComposedType(Product.class), "SELECT {PK} FROM {Product}", new HashMap());
			}

			final Cache cache = Registry.getCurrentTenant().getCache();
			cache.clear();
			savedQuery[0].getCode();


			final long conngets = Registry.getCurrentTenant().getDataSource().totalGets();
			final int THREADS = 50;
			perf = new Perf(THREADS)
			{
				@Override
				public void body() throws Exception
				{
					savedQuery[0].getCode();
				}
			};
			perf.go(10000);

			LOG.info("Getting same cache entry: " + perf.getExecutions());
			LOG.info("Cache stat: Gets:" + cache.getGetCount() + ",Adds:" + cache.getAddCount());
			LOG.info("DB stat: getConns():" + (Registry.getCurrentTenant().getDataSource().totalGets() - conngets));
			perf.close();

		}
		finally
		{
			for (int i = 0; i < CNT; i++)
			{
				if (savedQuery[i] != null)
				{
					savedQuery[i].remove();
				}
			}

		}
	}
}
