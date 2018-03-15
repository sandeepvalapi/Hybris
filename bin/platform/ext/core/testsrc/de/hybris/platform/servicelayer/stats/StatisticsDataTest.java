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
package de.hybris.platform.servicelayer.stats;

import de.hybris.bootstrap.annotations.UnitTest;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "classpath:/servicelayer/test/statistisdatatest-spring.xml" })
@UnitTest
public class StatisticsDataTest
{
	@Resource(name = "defaultStatisticsData")
	private DefaultStatisticsData data;
	private static String COLLECTOR = "testcollector";

	@Before
	public void beofre()
	{
		data.removeDataCollector(COLLECTOR);
		data.addDataCollector(COLLECTOR);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.stats.DefaultStatisticsData#addDataCollector(java.lang.String)}.
	 */
	@Test
	public void testAddDataCollector()
	{
		data.removeDataCollector(COLLECTOR);
		Assert.assertTrue(data.addDataCollector(COLLECTOR));
		Assert.assertFalse(data.addDataCollector(COLLECTOR));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.stats.DefaultStatisticsData#removeDataCollector(java.lang.String)}.
	 */
	@Test
	public void testRemoveDataCollector()
	{
		Assert.assertTrue(data.removeDataCollector(COLLECTOR));
		Assert.assertFalse(data.removeDataCollector(COLLECTOR));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.stats.DefaultStatisticsData#containsDataCollector(java.lang.String)}.
	 */
	@Test
	public void testContainsDataCollector()
	{
		Assert.assertTrue(data.containsDataCollector(COLLECTOR));
		data.removeDataCollector(COLLECTOR);
		Assert.assertFalse(data.containsDataCollector(COLLECTOR));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.stats.DefaultStatisticsData#putData(java.lang.String, long, float)}.
	 */
	@Test
	public void testPutData()
	{
		Assert.assertTrue(data.putData(COLLECTOR, System.currentTimeMillis(), 1));
		data.removeDataCollector(COLLECTOR);
		Assert.assertFalse(data.putData(COLLECTOR, System.currentTimeMillis(), 1));
	}

	//	/**
	//	 * Test method for
	//	 * {@link de.hybris.platform.servicelayer.stats.DefaultStatisticsData#getTimePeriodData(java.lang.String, long, long, long)}
	//	 * .
	//	 */
	//	@Test
	//	public void testGetTimePeriodData()
	//	{
	//		System.out.println("*---- testGetTimePeriodData-test ----*");
	//		fillData(20000);
	//		final Object[][] ticks = data.getTimePeriodData(COLLECTOR, 50000, 100000, 0);
	//		Assert.assertEquals(50000, (((Long) ticks[ticks.length - 1][0]).longValue() - ((Long) ticks[0][0]).longValue()));
	//
	//				data.removeDataCollector(COLLECTOR);
	//				data.addDataCollector(COLLECTOR);
	//				fillData(5000000l);
	//				ticks = data.getTimePeriodData(COLLECTOR, 0, 5000000000l, 0);
	//				Assert.assertTrue(ticks.length <= 15000);
	//				System.out.println("ticks-size: " + ticks.length);
	//				System.out.println("*----            ----*");
	//	}

	//	/**
	//	 * Test method for
	//	 * {@link de.hybris.platform.servicelayer.stats.DefaultStatisticsData#getAllData(java.lang.String, int, long)}.
	//	 */
	//	@Test
	//	public void testGetAllData()
	//	{
	//		fillData(1000);
	//		Object[][] ticks = data.getAllData(COLLECTOR, 3000, 0);
	//		Assert.assertEquals(1000, ticks.length);
	//		Assert.assertEquals((1000 * 1000), ((Long) ticks[ticks.length - 1][0]).longValue());
	//		data.removeDataCollector(COLLECTOR);
	//		data.addDataCollector(COLLECTOR);
	//		fillData(1000000l);
	//		ticks = data.getAllData(COLLECTOR, 3000, 0);
	//		Assert.assertTrue(ticks.length <= 3000);
	//	}
	//
	//	/**
	//	 * Test method for
	//	 * {@link de.hybris.platform.servicelayer.stats.DefaultStatisticsData#getTickAsArray(java.lang.String, long, int, long)}
	//	 * .
	//	 */
	//	@Test
	//	public void testGetTickAsArray()
	//	{
	//		fillData(20000);
	//		final Object[][] ticks = data.getTickAsArray(COLLECTOR, 10000, 10, 0);
	//		Assert.assertTrue(ticks.length <= 10);
	//		Assert.assertTrue((((Long) ticks[ticks.length - 1][0]).longValue() - ((Long) ticks[0][0]).longValue()) <= 10000);
	//		Assert.assertEquals((20000 * 1000), ((Long) ticks[ticks.length - 1][0]).longValue());
	//	}
	//
	//	@Test
	//	public void testPutMaxAmount()
	//	{
	//		System.out.println("*---- testPutMaxAmount-test ----*");
	//
	//		//DefaultStatisticsData should keep with default-settings 12 hours + 7 days + 31 days data.
	//		//=> that makes 12 * 60 * 60 + 7 * 24 * 60 * 60 + 31 * 24 * 60 * 60 = 3326400 seconds.
	//
	//		fillData(3326400l);
	//
	//		Assert.assertEquals(130464, data.getCurrentSize(COLLECTOR));
	//		data.removeDataCollector(COLLECTOR);
	//		data.addDataCollector(COLLECTOR);
	//
	//		//When putting more data, it should drop the ticks that get over it.
	//		fillData(4000000l);
	//
	//		System.out.println("Collector-size: " + data.getCurrentSize(COLLECTOR));
	//		Assert.assertTrue(data.getCurrentSize(COLLECTOR) <= 130470);
	//		data.removeDataCollector(COLLECTOR);
	//		data.addDataCollector(COLLECTOR);
	//
	//		//Even if much much more time elapses.
	//		fillData(10000000l);
	//
	//		Assert.assertTrue(data.getCurrentSize(COLLECTOR) <= 130470);
	//
	//		System.out.println("*----            ----*");
	//	}
	//
	//	@Test
	//	public void testMultiThreadingAccess()
	//	{
	//		final Thread t1 = new Thread(new WriteData());
	//		final Thread t2 = new Thread(new ReadData());
	//		final Thread t3 = new Thread(new ReadData());
	//
	//		t1.run();
	//		t2.run();
	//		t3.run();
	//	}

	public void fillData(final long amount)
	{
		for (long i = 1; i <= amount; i++)
		{
			data.putData(COLLECTOR, (i * 1000l), i);
		}
	}

	class ReadData implements Runnable
	{
		@Override
		public void run()
		{
			Object[][] ticks;
			for (int i = 0; i < 50; i++)
			{
				ticks = data.getTickAsArray(COLLECTOR, 43200000l, 60, 60000);
				Assert.assertTrue(ticks != null);
				Assert.assertTrue(ticks.length > 0);
				Assert.assertTrue(ticks.length < 60);
			}
		}
	}

	class WriteData implements Runnable
	{
		@Override
		public void run()
		{
			for (long i = 0; i < 5000000l; i++)
			{
				data.putData(COLLECTOR, i * 1000l, i);
			}
		}
	}
}
