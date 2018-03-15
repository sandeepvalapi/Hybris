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

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.CoreImpExConstants;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


@PerformanceTest
public class LinkPerformanceTest extends HybrisJUnit4Test
{
	@Test
	public void testSkipQueryLinksOption() throws ConsistencyCheckException
	{
		final UserManager userManager = UserManager.getInstance();

		final int cycles = 1000;

		final List<Title> newItems = new ArrayList<Title>(cycles);
		for (int i = 0; i < cycles; i++)
		{
			newItems.add(userManager.createTitle("T_" + i + "_" + System.nanoTime()));
		}

		final long msWithoutA = testSkipQueryLinksOption(newItems, false);
		final long msWithA = testSkipQueryLinksOption(newItems, true);
		final long msWithB = testSkipQueryLinksOption(newItems, true);
		final long msWithoutB = testSkipQueryLinksOption(newItems, false);

		final long msWithoutAvg = (msWithoutA + msWithoutB) / 2;
		final long msWithAvg = (msWithA + msWithB) / 2;

		System.out.println("linking " + cycles + " items without skip option took " + msWithoutAvg + "ms");
		System.out.println("linking " + cycles + " items with skip option took " + msWithAvg + "ms (difference "
				+ (msWithoutAvg - msWithAvg) + "ms " + (((msWithoutAvg - msWithAvg) * 100) / msWithoutAvg) + "%)");

		assertTrue(msWithoutAvg > msWithAvg);
	}

	private long testSkipQueryLinksOption(final List<Title> newItems, final boolean useOPtion) throws ConsistencyCheckException
	{
		final LinkManager linkManager = LinkManager.getInstance();

		final long time1 = System.currentTimeMillis();
		if (useOPtion)
		{
			jaloSession.getSessionContext().setAttribute(CoreImpExConstants.CTX_DONT_CHANGE_EXISTING_LINKS, Boolean.TRUE);
		}
		else
		{
			jaloSession.getSessionContext().removeAttribute(CoreImpExConstants.CTX_DONT_CHANGE_EXISTING_LINKS);
		}
		for (final Title src : newItems)
		{
			linkManager.setLinkedItems(src, true, "someDummyQualifier", null, getRandomElements(newItems, 10));
		}
		final long time2 = System.currentTimeMillis();

		return time2 - time1;
	}

	private List<Title> getRandomElements(final List<Title> all, final int count)
	{
		final List<Title> ret = new ArrayList<Title>(count);
		final int max = all.size();
		for (int i = 0; i < count; i++)
		{
			ret.add(all.get((int) (max * Math.random())));
		}
		return ret;
	}
}
