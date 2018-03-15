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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.Ignore;


/**
 * Test AfterSaveEvent when explicit transactions are running.
 */
public class TransactionalAfterSaveEventTest extends AbstractAfterSaveEventTest
{

	private final int oneTxCount = 2000;
	private final int concurrentTxCount = 2000;

	//CREATE, UPDATE, and REMOVE an item in Transaction synchronously
	@Test
	public void testSynCreateUpdateRemoveInTx() throws Exception
	{
		final TestAfterSaveListenerRegistry registry = TestAfterSaveListenerRegistry.createSyncDeliveryRegistry();
		try
		{
			TestAfterSaveEventTransaction.install(registry);
			assertTrue(Transaction.current() instanceof TestAfterSaveEventTransaction);

			TransactionBody txBody = prepareTxBodyForCreate("title1");
			final Title title1 = ((Title[]) Transaction.current().execute(txBody))[0];
			List<AfterSaveEvent> events = registry.getEvents();
			List<AfterSaveEvent> titleEvents = filterEvents(events, title1);
			assertEvent(title1, AfterSaveEvent.CREATE, titleEvents);

			registry.clearEvents();
			txBody = prepareTxBodyForUpdate("code1", title1);
			Transaction.current().execute(txBody);
			events = registry.getEvents();
			titleEvents = filterEvents(events, title1);
			assertEvent(title1, AfterSaveEvent.UPDATE, titleEvents);

			registry.clearEvents();
			txBody = prepareTxBodyForRemove(title1);
			Transaction.current().execute(txBody);
			events = registry.getEvents();
			titleEvents = filterEvents(events, title1);
			assertEvent(title1, AfterSaveEvent.REMOVE, titleEvents);

			assertRollbackHidesEvents(txBody, registry);
		}
		finally
		{
			TestAfterSaveEventTransaction.uninstall();
		}
	}

	@Test
	public void testSynComplexUpdatesInTx() throws Exception
	{
		final TestAfterSaveListenerRegistry registry = TestAfterSaveListenerRegistry.createSyncDeliveryRegistry();
		try
		{
			final UserManager userManager = UserManager.getInstance();
			TestAfterSaveEventTransaction.install(registry);
			assertTrue(Transaction.current() instanceof TestAfterSaveEventTransaction);

			//creates one group
			TransactionBody txBody = new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					return new Object[]
					{ //
					userManager.createUserGroup("GGG") //
					};
				}
			};

			final Object[] items = (Object[]) Transaction.current().execute(txBody);
			List<AfterSaveEvent> events = registry.getEvents();
			assertEquals("got more events than expected : " + events, 1, events.size());

			final UserGroup ggg = (UserGroup) items[0];
			assertEvent(ggg, AfterSaveEvent.CREATE, 1, registry.getEvents());

			// create 4 customers
			registry.clearEvents();
			txBody = new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					return new Object[]
					{//
					userManager.createCustomer("CCC1"), //
							userManager.createCustomer("CCC2"), //
							userManager.createCustomer("CCC3"), //
							userManager.createCustomer("CCC4") //
					};
				}
			};
			final Object[] customers = (Object[]) Transaction.current().execute(txBody);
			events = registry.getEvents();
			final List<AfterSaveEvent> filteredCustomerEvents = filterEvents(events, customers);
			assertEquals("unexpected filtered customer creation event size -> " + filteredCustomerEvents, customers.length,
					filteredCustomerEvents.size());
			//			assertEquals("got more events than expected -> " + events, customers.length, events.size());

			for (final AfterSaveEvent event : filteredCustomerEvents)
			{
				assertEquals("must be CREATE event: ", AfterSaveEvent.CREATE, event.getType());
			}

			// We have to wait at least one second to make sure subsequent modification time
			// updates are really hitting the database. If the time between creation and update
			// is too short the HJMP entity will detect no change (especially on MySQL which is
			// storing timestamps only exact to the second!)
			//			Thread.sleep(1010);

			//creates the relations between the group and customers
			registry.clearEvents();
			txBody = new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					ggg.setMembers(new LinkedHashSet(Arrays.asList(customers)));
					return null;
				}
			};
			Transaction.current().execute(txBody);

			// 9 events in total ...
			events = registry.getEvents();
			assertEquals("unexpected filtered relation event size -> ", 9, events.size());

			// 4 x updates on all newly connected items
			assertEvent(ggg, AfterSaveEvent.UPDATE, 1, events);
			for (final AfterSaveEvent event : filteredCustomerEvents)
			{
				assertEvent(event.getPk(), AfterSaveEvent.UPDATE, 1, events);
			}

			// 5 x creates of the connecting Link items
			for (final Object customer : customers)
			{
				assertEvent(getLink(CoreConstants.Relations.PRINCIPALGROUPRELATION, (Customer) customer, ggg), //
						AfterSaveEvent.CREATE, 1, events);
			}
		}
		finally
		{
			TestAfterSaveEventTransaction.uninstall();
		}
	}

	private static List<AfterSaveEvent> filterEvents(final Collection<AfterSaveEvent> events, final Object... items)
	{
		final PK[] pks = new PK[items.length];
		int index = 0;
		for (final Object item : items)
		{
			pks[index++] = ((Item) item).getPK();
		}
		return filterEvents(events, pks);
	}

	private static List<AfterSaveEvent> filterEvents(final Collection<AfterSaveEvent> events, final PK... pks)
	{
		final List<AfterSaveEvent> ret = new ArrayList<AfterSaveEvent>();
		final Set<PK> pkSet = new HashSet<PK>(Arrays.asList(pks));
		for (final AfterSaveEvent e : events)
		{
			if (pkSet.contains(e.getPk()))
			{
				ret.add(e);
			}
		}
		return ret;
	}

	//CREATE, UPDATE, and REMOVE many items in one Transaction,
	//the asyn tag for the DefaultAfterSaveListenerRegistry is set to true
	@Test
	public void testAsynCreateUpdateRemoveInTx() throws Exception
	{
		final String[] titleNames = new String[this.oneTxCount];
		for (int i = 0; i < this.oneTxCount; i++)
		{
			titleNames[i] = "title" + i;
		}

		TestAfterSaveListenerRegistry registry = null;
		try
		{
			registry = TestAfterSaveListenerRegistry.createAsyncDeliveryRegistry();

			TestAfterSaveEventTransaction.install(registry);
			assertTrue(Transaction.current() instanceof TestAfterSaveEventTransaction);

			//CREATE
			TransactionBody txBody = prepareTxBodyForCreate(titleNames);
			final Title[] titles = (Title[]) Transaction.current().execute(txBody);
			busyWaiting(registry, 0);
			assertEquals(this.oneTxCount, titles.length);
			assertEvents(registry.getEvents(), titles, AfterSaveEvent.CREATE);

			//UPDATE
			registry.clearEvents();
			txBody = prepareTxBodyForUpdate("code1", titles);
			Transaction.current().execute(txBody);
			busyWaiting(registry, 1);
			assertEquals(this.oneTxCount, titles.length);
			assertEvents(registry.getEvents(), titles, AfterSaveEvent.UPDATE);

			//REMOVE
			registry.clearEvents();
			txBody = prepareTxBodyForRemove(titles);
			Transaction.current().execute(txBody);
			busyWaiting(registry, 2);
			assertEquals(this.oneTxCount, titles.length);
			assertEvents(registry.getEvents(), titles, AfterSaveEvent.REMOVE);
		}
		finally
		{
			TestAfterSaveEventTransaction.uninstall();
			if (registry != null)
			{
				TestAfterSaveListenerRegistry.destroyRegistry(registry);
			}
		}
	}

	//CREATE, UPDATE, and REMOVE an item in Transaction asynchronously and concurrently,
	//each thread will do the CREATE, UPDATE, and REMOVE in a different transaction
	@Test
	@Ignore("HORST-659")
	public void testAsynConcurrentCreateUpdateRemoveInTx() throws Exception
	{
		final String[] titleNames = new String[this.oneTxCount];
		for (int i = 0; i < this.oneTxCount; i++)
		{
			titleNames[i] = "title" + i;
		}

		final int threadCount = 100;
		TestAfterSaveListenerRegistry registry = null;
		ExecutorService executor = null;
		try
		{
			registry = TestAfterSaveListenerRegistry.createAsyncDeliveryRegistry();
			executor = createTenantAwareExecutorService(threadCount);

			final TestAfterSaveListenerRegistry reg = registry;

			for (int i = 0; i < this.concurrentTxCount; i++)
			{
				final int titleNr = i;
				executor.execute(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							TestAfterSaveEventTransaction.install(reg);
							//CREATE
							TransactionBody txBody = prepareTxBodyForCreate(titleNames[titleNr]);
							Title title = ((Title[]) Transaction.current().execute(txBody))[0];
							//UPDATE
							txBody = prepareTxBodyForUpdate("code" + titleNr, title);
							title = ((Title[]) Transaction.current().execute(txBody))[0];
							//REMOVE
							txBody = prepareTxBodyForRemove(title);
							Transaction.current().execute(txBody);
						}
						catch (final Exception e)
						{
							// TODO pass to test thread somehow
							e.printStackTrace();
						}
						finally
						{
							TestAfterSaveEventTransaction.uninstall();
						}
					}
				});
			}

			//busy waiting
			final long maxWait = System.currentTimeMillis() + (threadCount * 1000);
			for (int[] cnt = countEvents(registry); (cnt[0] < concurrentTxCount || cnt[2] < concurrentTxCount)
					&& System.currentTimeMillis() < maxWait; cnt = countEvents(registry))
			{
				Thread.yield();
			}

			final int[] eventCounts = countEvents(registry);
			assertEventCounts(eventCounts);
		}
		finally
		{
			if (executor != null)
			{
				executor.shutdownNow();
				executor.awaitTermination(30, TimeUnit.SECONDS);
			}
			if (registry != null)
			{
				TestAfterSaveListenerRegistry.destroyRegistry(registry);
			}
		}
	}

	private TransactionBody prepareTxBodyForCreate(final String... titles)
	{
		return new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				final Title[] tmpTitles = new Title[titles.length];
				final UserManager userManager = UserManager.getInstance();
				for (int i = 0; i < titles.length; i++)
				{
					final Title tmp = userManager.createTitle(titles[i]);
					tmpTitles[i] = tmp;
				}
				return tmpTitles;
			}
		};
	}

	private TransactionBody prepareTxBodyForUpdate(final String code, final Title... titles)
	{
		return new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				for (final Title title : titles)
				{
					title.setCode(code);
				}
				return titles;
			}
		};
	}

	private TransactionBody prepareTxBodyForRemove(final Title... titles)
	{
		return new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				for (final Title title : titles)
				{
					title.remove();
				}
				return null;
			}
		};
	}

	private void busyWaiting(final TestAfterSaveListenerRegistry registry, final int index)
	{
		final long maxWait = System.currentTimeMillis() + (oneTxCount * 50);
		for (int[] cnt = countEvents(registry); (cnt[index] < oneTxCount) && System.currentTimeMillis() < maxWait; cnt = countEvents(registry))
		{
			Thread.yield();
		}
	}

	private int[] countEvents(final TestAfterSaveListenerRegistry registry)
	{
		int creates = 0;
		int updates = 0;
		int removes = 0;
		for (final AfterSaveEvent event : registry.getEvents())
		{
			switch (event.getType())
			{
				case AfterSaveEvent.CREATE:
					creates++;
					break;
				case AfterSaveEvent.UPDATE:
					updates++;
					break;
				case AfterSaveEvent.REMOVE:
					removes++;
					break;
				default:
					fail("illegal event " + event);

			}
		}
		return new int[]
		{ creates, updates, removes };
	}

	private void assertEventCounts(final int[] eventCounts)
	{
		final int creates = eventCounts[0];
		final int updates = eventCounts[1];
		final int removes = eventCounts[2];

		assertEquals(concurrentTxCount, creates);
		assertEquals(concurrentTxCount, removes);
		assertTrue(concurrentTxCount >= updates);
	}

	private void assertEvent(final Title title, final int type, final List<AfterSaveEvent> events)
	{
		assertEquals(1, events.size());
		final AfterSaveEvent afterSaveEvent = events.get(0);
		assertEquals(title.getPK(), afterSaveEvent.getPk());
		assertEquals(type, afterSaveEvent.getType());
	}

	private void assertEvents(final List<AfterSaveEvent> events, final Title[] titles, final int type)
	{
		for (final AfterSaveEvent event : events)
		{
			assertEquals(type, event.getType());
			final PK pk = event.getPk();
			boolean found = false;
			for (final Title title : titles)
			{
				if (title.getPK().equals(pk))
				{
					found = true;
					break;
				}
			}
			assertTrue(found);
		}
	}

	void assertRollbackHidesEvents(final TransactionBody body, final TestAfterSaveListenerRegistry registry)
	{
		final String rollBackMessage = "Rollback please";
		registry.clearEvents();
		try
		{
			Transaction.current().execute(//
					new TransactionBody()
					{
						@Override
						public Object execute() throws Exception
						{
							body.execute();
							throw new RuntimeException(rollBackMessage);
						}
					}//
					);
		}
		catch (final Exception e)
		{
			if (e instanceof RuntimeException && rollBackMessage.equals(e.getMessage()))
			{ //NOPMD
			  //expected
			}
			else
			{
				fail("roll back test failed: " + e.getMessage());
			}
		}
		assertTrue(registry.getEvents().isEmpty());
	}

}
