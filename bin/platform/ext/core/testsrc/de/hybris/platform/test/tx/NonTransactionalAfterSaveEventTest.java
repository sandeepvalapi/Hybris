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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.Transaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


/**
 * Tests AfterSaveEvent when no explicit transactions are running.
 */
@IntegrationTest
public class NonTransactionalAfterSaveEventTest extends AbstractAfterSaveEventTest
{
	@Test
	public void testSynNonTransactionalPropertyUpdate() throws Exception
	{
		final TestAfterSaveListenerRegistry registry = TestAfterSaveListenerRegistry.createSyncDeliveryRegistry();

		final Title title = UserManager.getInstance().createTitle("title" + Math.random() * 10000);

		try
		{
			final Transaction newTx = TestAfterSaveEventTransaction.install(registry);
			{
				assertSame(newTx, Transaction.current());
				title.setCode(title.getCode() + System.nanoTime());
				assertNotSame(newTx, Transaction.current());
			}
			assertEquals(1, registry.getEvents().size());
			assertEquals(title.getPK(), registry.getEvents().get(0).getPk());
			assertEquals(AfterSaveEvent.UPDATE, registry.getEvents().get(0).getType());
			registry.clearEvents();

			{
				title.setCode(title.getCode() + System.nanoTime());
			}
			assertEquals(1, registry.getEvents().size());
			assertEquals(title.getPK(), registry.getEvents().get(0).getPk());
			assertEquals(AfterSaveEvent.UPDATE, registry.getEvents().get(0).getType());
		}
		finally
		{
			TestAfterSaveEventTransaction.uninstall();
		}
	}

	@Test
	public void testSynNonTransactionalComplexUpdates() throws Exception
	{
		final UserGroup ggg = UserManager.getInstance().createUserGroup("GGG");
		final Customer ccc1 = UserManager.getInstance().createCustomer("CCC1");
		final Customer ccc2 = UserManager.getInstance().createCustomer("CCC2");
		final Customer ccc3 = UserManager.getInstance().createCustomer("CCC3");
		final Customer ccc4 = UserManager.getInstance().createCustomer("CCC4");

		// We have to wait at least one second to make sure subsequent modification time
		// updates are really hitting the database. If the time between creation and update
		// is too short the HJMP entity will detect no change (especially on MySQL which is
		// storing timestamps only exact to the second!)
		//		Thread.sleep(1010);

		final TestAfterSaveListenerRegistry registry = TestAfterSaveListenerRegistry.createSyncDeliveryRegistry();

		try
		{
			final TestAfterSaveEventTransaction newTx = TestAfterSaveEventTransaction.install(registry);
			assertSame(newTx, Transaction.current());

			ggg.setMembers(new LinkedHashSet(Arrays.asList(ccc1, ccc2, ccc3, ccc4)));
			assertNotSame(newTx, Transaction.current());

			assertEvent(ggg, AfterSaveEvent.UPDATE, 1, registry.getEvents());
			assertEvent(ccc1, AfterSaveEvent.UPDATE, 1, registry.getEvents());
			assertEvent(ccc2, AfterSaveEvent.UPDATE, 1, registry.getEvents());
			assertEvent(ccc3, AfterSaveEvent.UPDATE, 1, registry.getEvents());
			assertEvent(ccc4, AfterSaveEvent.UPDATE, 1, registry.getEvents());

			assertEvent(getLink(CoreConstants.Relations.PRINCIPALGROUPRELATION, ccc1, ggg), AfterSaveEvent.CREATE, 1,
					registry.getEvents());
			assertEvent(getLink(CoreConstants.Relations.PRINCIPALGROUPRELATION, ccc2, ggg), AfterSaveEvent.CREATE, 1,
					registry.getEvents());
			assertEvent(getLink(CoreConstants.Relations.PRINCIPALGROUPRELATION, ccc3, ggg), AfterSaveEvent.CREATE, 1,
					registry.getEvents());
			assertEvent(getLink(CoreConstants.Relations.PRINCIPALGROUPRELATION, ccc4, ggg), AfterSaveEvent.CREATE, 1,
					registry.getEvents());
		}
		finally
		{
			TestAfterSaveEventTransaction.uninstall();
		}
	}

	@Test
	public void testSynNonTransactionalRemove() throws Exception
	{
		final Title title = UserManager.getInstance().createTitle("title" + Math.random() * 10000);

		final TestAfterSaveListenerRegistry registry = TestAfterSaveListenerRegistry.createSyncDeliveryRegistry();

		try
		{
			final TestAfterSaveEventTransaction newTx = TestAfterSaveEventTransaction.install(registry);
			newTx.activateAsCurrentTransaction();
			assertSame(newTx, Transaction.current());

			title.remove();
			assertNotSame(newTx, Transaction.current());

			assertEquals(1, registry.getEvents().size());
			assertEquals(title.getPK(), registry.getEvents().get(0).getPk());
			assertEquals(AfterSaveEvent.REMOVE, registry.getEvents().get(0).getType());
		}
		finally
		{
			TestAfterSaveEventTransaction.uninstall();
		}
	}

	@Test
	public void testAsynNonTransactionalConcurrentCreation() throws Exception
	{
		doNonTransactionalConcurrentCreation(30, 300, 2);
	}

	private void doNonTransactionalConcurrentCreation(final int maxThreads, final int rounds, final int waitingMinutes)
			throws Exception
	{

		TestAfterSaveListenerRegistry reg = null;
		ExecutorService executor = null;
		try
		{
			reg = TestAfterSaveListenerRegistry.createAsyncDeliveryRegistry();
			executor = createTenantAwareExecutorService(maxThreads);

			final TestAfterSaveListenerRegistry registry = reg;
			for (int i = 1; i <= rounds; i++)
			{
				final int titleNr = i;
				executor.execute(new Runnable()
				{
					@Override
					public void run()
					{
						JaloSession.getCurrentSession().getSessionContext()
								.setAttribute(SessionContext.TRANSACTION_IN_CREATE_DISABLED, Boolean.TRUE);
						try
						{
							TestAfterSaveEventTransaction.install(registry);
							//one CREATE, and one UPDATE
							createTitle("title-" + titleNr, "name-" + System.nanoTime());
						}
						catch (final Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							TestAfterSaveEventTransaction.uninstall();
						}
					}
				});
			}

			executor.shutdown();
			assertTrue(executor.awaitTermination(waitingMinutes, TimeUnit.MINUTES));
			final long maxWait = System.currentTimeMillis() + (maxThreads * 1000);
			for (int[] cnt = countEvents(reg); (cnt[0] < rounds || cnt[1] < rounds) && System.currentTimeMillis() < maxWait; cnt = countEvents(reg))
			{
				Thread.yield();
			}

			final int[] cnt = countEvents(reg);
			final int creates = cnt[0];
			final int updates = cnt[1];

			// for sure we expect #rounds CREATEs
			assertEquals(rounds, creates);
			// updates may be fewer due to publisher thread merging CREATE + UPDATE upon the same item!
			assertTrue(rounds >= updates);
		}
		finally
		{
			if (executor != null)
			{
				executor.shutdownNow();
				executor.awaitTermination(30, TimeUnit.SECONDS);
			}
			if (reg != null)
			{
				TestAfterSaveListenerRegistry.destroyRegistry(reg);
			}
		}
	}

	int[] countEvents(final TestAfterSaveListenerRegistry registry)
	{
		int creates = 0;
		int updates = 0;
		for (final AfterSaveEvent e : registry.getEvents())
		{
			switch (e.getType())
			{
				case AfterSaveEvent.CREATE:
					creates++;
					break;
				case AfterSaveEvent.UPDATE:
					updates++;
					break;
				default:
					fail("illegal event " + e);

			}
		}
		return new int[]
		{ creates, updates };
	}

	Title createTitle(final String code, final String name) throws ConsistencyCheckException
	{
		try
		{
			final Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put(Title.CODE, code);
			attributes.put(Title.NAME, name);
			return (Title) TypeManager.getInstance().getComposedType(Title.class).newInstance(attributes);
		}
		catch (final Exception e)
		{
			throw new JaloSystemException(e);
		}
	}

}
