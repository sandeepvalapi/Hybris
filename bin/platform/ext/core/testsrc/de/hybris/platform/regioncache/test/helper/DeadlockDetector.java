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
package de.hybris.platform.regioncache.test.helper;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;


/**
 * Detect Java-level deadlocks.
 * <p/>
 * Java 1.5 only supports finding monitor based deadlocks. 1.6's {@link ThreadMXBean} supports
 * {@link java.util.concurrent.locks.Lock} based deadlocks.
 */
public class DeadlockDetector
{

	private static final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

	public static void printDeadlocks(final PrintStream writer)
	{
		final List<ThreadInfo> deadlocks = findDeadlocks();
		if (deadlocks.isEmpty())
		{
			return;
		}
		print(writer, deadlocks);
	}

	private static void print(final PrintStream writer, final List<ThreadInfo> deadlocks)
	{
		writer.println("Deadlock detected\n=================\n");
		for (final ThreadInfo thread : deadlocks)
		{
			writer.println(format("\"%s\":", thread.getThreadName()));
			writer.println(format("  waiting to lock Monitor of %s ", thread.getLockName()));
			writer.println(format("  which is held by \"%s\"", thread.getLockOwnerName()));
			writer.println();
		}
	}

	private static List<ThreadInfo> findDeadlocks()
	{
		long[] result;
		if (mbean.isSynchronizerUsageSupported())
		{
			result = mbean.findDeadlockedThreads();
		}
		else
		{
			result = mbean.findMonitorDeadlockedThreads();
		}
		final long[] monitorDeadlockedThreads = result;
		if (monitorDeadlockedThreads == null)
		{
			return emptyList();
		}
		return asList(mbean.getThreadInfo(monitorDeadlockedThreads));
	}

}
