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
package de.hybris.platform.util.collections;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.util.collections.fast.YLongToObjectMap;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;


/**
 * Performance comparison of different Maps
 */
@PerformanceTest
public class MapBenchmarkTest
{

	@Before
	public void setUp() throws Exception
	{
		System.out.println("\n1st line: time used(s)\n2nd line: heap memory used so far(MB)");
	}

	@Test
	public void testJavaMap()
	{
		final int n = 10000000;
		System.out.println("\n===== Java's built-in HashMap =====");

		long startTime = System.nanoTime();
		final long startHeapSize = Runtime.getRuntime().freeMemory();

		// BEGIN: benchmark for Java's built-in hashmap
		final HashMap jLongObjectMap = new HashMap();

		System.out.println("\n-- " + n + " puts(key, value) --");
		startTime = System.nanoTime();
		for (long i = 0; i < n; i++)
		{
			jLongObjectMap.put(i, new float[]
			{ 0f, 1f, 2f, 3f, 4f });
		}
		printResults(startTime, startHeapSize);

		System.out.println("\n-- " + n + " gets(key) --");
		startTime = System.nanoTime();
		for (int i = 0; i < n; i++)
		{
			jLongObjectMap.get(i);
		}
		printResults(startTime, startHeapSize);

		System.out.println("\n-- " + n + " containsKey(key) --");
		startTime = System.nanoTime();
		for (int i = 0; i < n; i++)
		{
			jLongObjectMap.containsKey(i);
		}
		printResults(startTime, startHeapSize);

	}

	@Test
	public void testFastUtilMap()
	{
		final int n = 10000000;
		System.out.println("\n===== FastUtil's YLongToObjectMap =====");

		long startTime = System.nanoTime();
		final long startHeapSize = Runtime.getRuntime().freeMemory();
		// BEGIN: benchmark for FastUtil's  YLongToObjectMap
		final YLongToObjectMap fLongObjectMap = new YLongToObjectMap();

		System.out.println("\n-- " + n + " puts(key, value) --");
		startTime = System.nanoTime();
		for (long i = 0; i < n; i++)
		{
			fLongObjectMap.put(i, new float[]
			{ 0f, 1f, 2f, 3f, 4f });
		}
		printResults(startTime, startHeapSize);

		System.out.println("\n-- " + n + " gets(key) --");
		startTime = System.nanoTime();
		for (int i = 0; i < n; i++)
		{
			fLongObjectMap.get(i);
		}
		printResults(startTime, startHeapSize);

		System.out.println("\n-- " + n + " containsKey(key) --");
		startTime = System.nanoTime();
		for (int i = 0; i < n; i++)
		{
			fLongObjectMap.containsKey(i);
		}
		printResults(startTime, startHeapSize);
	}

	// Because pf license reason, we cannot ship the trove library wih our platform. 
	//	To test it locally(ONLY, don't commit!) - you have include proper jar file first (e.g. trove-2.0.4.jar
	//ONLY FOR LOCAL TESTS!
	//
	//	@Test
	//	public void testTroveMap()
	//	{
	//		final int n = 10000000;
	//		System.out.println("\n===== Trove's TLongToObjectMap =====");
	//
	//		long startTime = System.nanoTime();
	//		final long startHeapSize = Runtime.getRuntime().freeMemory();
	//		// BEGIN: benchmark for Trove's TLongToObjectMap
	//		final TLongObjectHashMap tLongObjectMap = new TLongObjectHashMap();
	//
	//		System.out.println("\n-- " + n + " puts(key, value) --");
	//		startTime = System.nanoTime();
	//		for (long i = 0; i < n; i++)
	//		{
	//			tLongObjectMap.put(i, new float[]
	//			{ 0f, 1f, 2f, 3f, 4f });
	//		}
	//		printResults(startTime, startHeapSize);
	//
	//		System.out.println("\n-- " + n + " gets(key) --");
	//		startTime = System.nanoTime();
	//		for (int i = 0; i < n; i++)
	//		{
	//			tLongObjectMap.get(i);
	//		}
	//		printResults(startTime, startHeapSize);
	//
	//		System.out.println("\n-- " + n + " containsKey(key) --");
	//		startTime = System.nanoTime();
	//		for (int i = 0; i < n; i++)
	//		{
	//			tLongObjectMap.containsKey(i);
	//		}
	//		printResults(startTime, startHeapSize);
	//	}


	// 1st line: time used(s). 
	//2nd line: heap memory used so far(MB)
	private void printResults(final long startTime, final long startHeapSize)
	{
		System.out.println((System.nanoTime() - startTime) / 1000000000.0);
		System.out.println((startHeapSize - Runtime.getRuntime().freeMemory()) / 1048576.0);
	}
}
