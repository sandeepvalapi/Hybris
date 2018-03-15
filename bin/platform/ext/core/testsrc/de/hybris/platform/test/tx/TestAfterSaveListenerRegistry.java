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

import static junit.framework.Assert.fail;

import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;
import de.hybris.platform.tx.DefaultAfterSaveListenerRegistry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


final class TestAfterSaveListenerRegistry extends DefaultAfterSaveListenerRegistry
{

	static TestAfterSaveListenerRegistry createSyncDeliveryRegistry()
	{
		return new TestAfterSaveListenerRegistry(false);
	}

	static TestAfterSaveListenerRegistry createAsyncDeliveryRegistry()
	{
		final TestAfterSaveListenerRegistry fakeRegistry = new TestAfterSaveListenerRegistry(true);

		fakeRegistry.addListener(new AfterSaveListener()
		{
			@Override
			public void afterSave(final Collection<AfterSaveEvent> collection)
			{
				fakeRegistry.getEvents().addAll(collection);
			}
		});

		startupRegistry(fakeRegistry);

		return fakeRegistry;
	}

	private static void startupRegistry(final TestAfterSaveListenerRegistry fakeRegistry)
	{
		try
		{
			fakeRegistry.afterPropertiesSet();
		}
		catch (final Exception e1)
		{
			e1.printStackTrace();
			fail(e1.getMessage());
		}
	}

	static void destroyRegistry(final TestAfterSaveListenerRegistry fakeRegistry)
	{
		try
		{
			fakeRegistry.destroy();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}


	private final List<AfterSaveEvent> events;
	private final boolean async;

	public TestAfterSaveListenerRegistry(final boolean async)
	{
		super();
		this.events = new CopyOnWriteArrayList<AfterSaveEvent>();
		this.async = async;
	}

	@Override
	protected void notifyListenersSynchronously(final Collection<AfterSaveEvent> collection)
	{
		if (async)
		{
			throw new IllegalStateException("notifyListenersSynchronously() must not be called for asnc delivery");
		}
		events.addAll(collection);
	}

	@Override
	protected boolean isAsyncParam()
	{
		return async;
	}


	List<AfterSaveEvent> getEvents()
	{
		return this.events;
	}

	void clearEvents()
	{
		this.events.clear();
	}
}
