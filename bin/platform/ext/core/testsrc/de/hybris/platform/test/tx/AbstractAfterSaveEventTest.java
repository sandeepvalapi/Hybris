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
package de.hybris.platform.test.tx;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.AfterSaveEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.junit.Ignore;


@Ignore
public abstract class AbstractAfterSaveEventTest extends HybrisJUnit4Test
{

	ExecutorService createTenantAwareExecutorService(final int threads)
	{
		final Tenant tenant = Registry.getCurrentTenantNoFallback();

		return Executors.newFixedThreadPool(threads, new ThreadFactory()
		{
			@Override
			public Thread newThread(final Runnable runable)
			{
				return new RegistrableThread(runable)
				{
					@Override
					public void internalRun()
					{
						try
						{
							prepareThread();
							super.internalRun();
						}
						finally
						{
							unprepareThread();
						}
					}

					void prepareThread()
					{
						Registry.setCurrentTenant(tenant);
					}

					void unprepareThread()
					{
						JaloSession.deactivate();
						Registry.unsetCurrentTenant();
					}
				};
			}
		});
	}

	void assertEvent(final Item item, final int type, final int amount, final List<AfterSaveEvent> allEvents)
	{
		final PK pk = item.getPK();
		assertEvent(pk, type, amount, allEvents);
	}

	void assertEvent(final PK pk, final int type, final int amount, final List<AfterSaveEvent> allEvents)
	{
		int counter = 0;
		for (final AfterSaveEvent e : allEvents)
		{
			if (pk.equals(e.getPk()) && type == e.getType())
			{
				counter++;
			}
		}
		assertEquals(amount, counter);
	}

	List<AfterSaveEvent> getEventsForItem(final Item item, final List<AfterSaveEvent> allEvents)
	{
		final PK pk = item.getPK();
		final List<AfterSaveEvent> ret = new ArrayList<AfterSaveEvent>();
		for (final AfterSaveEvent e : allEvents)
		{
			if (pk.equals(e.getPk()))
			{
				ret.add(e);
			}
		}
		return ret;
	}

	Link getLink(final String qualifier, final Item src, final Item tgt)
	{
		final Collection<Link> links = LinkManager.getInstance().getLinks(qualifier, src, tgt);
		return links != null && links.size() == 1 ? links.iterator().next() : null;
	}

}
