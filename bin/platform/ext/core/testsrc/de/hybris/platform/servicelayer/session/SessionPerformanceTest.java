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
package de.hybris.platform.servicelayer.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.StopWatch;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;


@PerformanceTest
public class SessionPerformanceTest extends ServicelayerBaseTest
{
	@Resource
	private SessionService sessionService;
	@Resource
	private UserService userService;
	@Resource
	ModelService modelService;

	private static final Logger LOGGER = Logger.getLogger(SessionPerformanceTest.class);

	@Test
	public void testAnonymousCreationPerformance() throws JaloSecurityException
	{
		final int cycles = 200 * 1000;
		final long time = createAnonymousSessions(cycles);
		LOGGER.info("creating " + cycles + " anonymous sessions took " + time + "ms");
	}

	protected long createAnonymousSessions(final int cycles) throws JaloSecurityException
	{
		final JaloConnection jaloConnection = JaloConnection.getInstance();
		final JaloSession[] all = new JaloSession[cycles];

		try
		{
			final long time1 = System.currentTimeMillis();
			for (int i = 0; i < cycles; i++)
			{
				all[i] = jaloConnection.createAnonymousCustomerSession();
			}
			return System.currentTimeMillis() - time1;
		}
		finally
		{
			for (int i = all.length - 1; i >= 0; i--)
			{
				if (all[i] != null)
				{
					all[i].close();
				}
			}
		}
	}

	@Test
	public void testAnonymousCreationMultipleThreads()
	{
		final int cycles = 200 * 1000;
		final int therads = 16;
		final long[] stats = createAnonymousSessions(cycles, therads, 120);
		LOGGER.info("creating " + cycles + " anonymous sessions using " + therads + " took " + stats[0] + "ms total ( average:"
				+ stats[1] + " min:" + stats[2] + " max:" + stats[3] + ")");
	}

	// [startToEnd, average, min, max]
	protected long[] createAnonymousSessions(final int cycles, final int threads, final int maxWaitSeconds)
	{
		final int cyclesPerThread = cycles / threads;
		final TestThreadsHolder<AnonSessionRunner> runners = new TestThreadsHolder<AnonSessionRunner>(threads, true)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new AnonSessionRunner(threadNumber == 0 ? cyclesPerThread + (cycles % threads) : cyclesPerThread);
			}
		};
		runners.startAll();
		assertTrue(runners.waitAndDestroy(maxWaitSeconds));
		assertEquals(Collections.EMPTY_MAP, runners.getErrors());

		final long startToEnd = runners.getStartToFinishMillis();
		long min = Long.MAX_VALUE;
		long max = -1;
		long sum = 0;
		int count = 0;
		for (final AnonSessionRunner r : runners.getRunners())
		{
			if (r.resultTime > -1)
			{
				min = Math.min(min, r.resultTime);
				max = Math.max(max, r.resultTime);
				sum += r.resultTime;
				count++;
			}
		}
		return new long[]
		{ startToEnd, sum / count, min, max };
	}

	class AnonSessionRunner implements Runnable
	{
		final int cycles;
		volatile long resultTime = -1;

		AnonSessionRunner(final int cycles)
		{
			this.cycles = cycles;
		}

		@Override
		public void run()
		{
			try
			{
				resultTime = createAnonymousSessions(cycles);
			}
			catch (final JaloSecurityException e)
			{
				throw new RuntimeException(e);
			}
		}
	}


	@Test
	public void testPerformanceAndMemory() throws Exception
	{
		//once to startup
		final JaloConnection jaloConnection = JaloConnection.getInstance();
		jaloConnection.createAnonymousCustomerSession();
		sessionService.createNewSession();


		final int CNT = 50000;
		StopWatch watch = new StopWatch("creating and closing JaloSession " + CNT + " times...");
		for (int i = 0; i < CNT; i++)
		{
			final JaloSession jaloSession = jaloConnection.createAnonymousCustomerSession();
			jaloSession.close();
		}
		watch.stop();

		watch = new StopWatch("creating and closing Servicelayer Session " + CNT + " times...");
		for (int i = 0; i < CNT; i++)
		{
			sessionService.createNewSession();
			sessionService.closeCurrentSession();
		}
		watch.stop();

	}


	@Test
	public void testExecuteInLocalViewValues()
	{
		final Session session = sessionService.createNewSession();
		assertSame(session, sessionService.getCurrentSession());
		session.setAttribute("key", "oldvalue");

		final UserModel currentUser = userService.getCurrentUser();
		final String sessionId = session.getSessionId();

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{

				final Session currentSession = sessionService.getCurrentSession();
				assertSame(session, currentSession);

				currentSession.setAttribute("key", "newvalue");
				userService.setCurrentUser(userService.getUserForUID("admin"));

				assertEquals(currentSession.getSessionId(), sessionId);
			}
		});

		assertSame(session, sessionService.getCurrentSession());
		assertEquals(session.getAttribute("key"), "oldvalue");
		assertEquals(currentUser, userService.getCurrentUser());
	}

	@Test
	public void testWrapping()
	{
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode("ttt");
		modelService.save(title);
		final Title titleJalo = modelService.getSource(title);

		// test write via SessionService
		sessionService.setAttribute("attr1", title);
		assertEquals(title, sessionService.getAttribute("attr1"));
		assertEquals(title, sessionService.getCurrentSession().getAttribute("attr1"));
		assertEquals(titleJalo, jaloSession.getAttribute("attr1"));

		// test write via Session
		sessionService.getCurrentSession().setAttribute("attr2", title);
		assertEquals(title, sessionService.getAttribute("attr2"));
		assertEquals(title, sessionService.getCurrentSession().getAttribute("attr2"));
		assertEquals(titleJalo, jaloSession.getAttribute("attr2"));

		// test write via JaloSession
		JaloSession.getCurrentSession().setAttribute("attr3", titleJalo);
		assertEquals(title, sessionService.getAttribute("attr3"));
		assertEquals(title, sessionService.getCurrentSession().getAttribute("attr3"));
		assertEquals(titleJalo, jaloSession.getAttribute("attr3"));
	}
}
