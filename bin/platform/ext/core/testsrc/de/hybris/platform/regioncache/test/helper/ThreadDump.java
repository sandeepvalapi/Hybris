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

import java.io.PrintStream;
import java.util.Map;


public class ThreadDump
{

	public static void dumpThreads(final PrintStream writer)
	{
		DeadlockDetector.printDeadlocks(writer);
		final Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
		for (final Thread thread : traces.keySet())
		{
			writer.println(String.format("\nThread %s@%d: (state = %s)", thread.getName(), Long.valueOf(thread.getId()),
					thread.getState()));
			for (final StackTraceElement stackTraceElement : traces.get(thread))
			{
				writer.println(" - " + stackTraceElement);
			}
		}
	}

}
