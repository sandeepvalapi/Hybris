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
package de.hybris.platform.jalo;

import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.bethecoder.ascii_table.ASCIITable;


/**
 *
 */
public class SessionContextMapTest extends HybrisJUnit4Test
{

	private static final int MAX_TRIES = 10000;

	private static final int MAX_ATTR_COUNT = 50;
	private static final int MAX_SESSION_COUNT = 50;

	private static final String CHANGED = "changed";
	private static final String SPECIAL = "special";

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SessionContextMapTest.class.getName());


	private final List<JaloSession> createdJaloSessions = new ArrayList<JaloSession>(MAX_ATTR_COUNT);

	private static Map ATTRIBS = new HashMap((int) (MAX_ATTR_COUNT / 0.75f) + 1);

	private final Random RAND = new Random(System.nanoTime());

	private final int[] sessionSeeds = new int[MAX_TRIES];


	@Before
	public void prepareSessions() throws JaloSecurityException
	{

		final StringBuffer buf = new StringBuffer(1000);
		for (int i = 0; i < MAX_ATTR_COUNT; i++)
		{
			buf.append("/KEY_VALUE[" + i + "]");
			ATTRIBS.put(buf.toString(), buf.toString());
		}
		ATTRIBS.put(SPECIAL, SPECIAL);

		for (int i = 0; i < MAX_SESSION_COUNT; i++)
		{
			JaloSession localSession = null;
			createdJaloSessions.add(localSession = jaloSession.getTenant().getJaloConnection().createAnonymousCustomerSession());
			localSession.getSessionContext().setAttributes(ATTRIBS);
		}


		for (int i = 0; i < MAX_TRIES; i++)
		{
			sessionSeeds[i] = Math.abs(RAND.nextInt(MAX_SESSION_COUNT));
		}


	}

	@Test
	public void testReadingLocalSesion() throws InterruptedException
	{
		long totalProcessTime = System.currentTimeMillis();
		for (int i = 0; i < MAX_TRIES; i++)
		{
			final JaloSession session = createdJaloSessions.get(sessionSeeds[i]);
			try
			{
				final SessionContext local = session.createLocalSessionContext();
				local.getAttribute(SPECIAL);
			}
			finally
			{
				JaloSession.getCurrentSession().removeLocalSessionContext();
			}
		}

		totalProcessTime = System.currentTimeMillis() - totalProcessTime;

		for (final JaloSession createdJaloSession : createdJaloSessions)
		{
			Assert.assertEquals(createdJaloSession.getSessionContext().getAttribute(SPECIAL), SPECIAL);
		}

		printResults("reading session context occasionally", MAX_TRIES, totalProcessTime);
	}

	@Test
	public void testModifingLocalSesion() throws InterruptedException
	{
		long totalProcessTime = System.currentTimeMillis();
		for (int i = 0; i < MAX_TRIES; i++)
		{
			final JaloSession session = createdJaloSessions.get(sessionSeeds[i]);
			try
			{
				final SessionContext local = session.createLocalSessionContext();
				local.setAttribute(SPECIAL, CHANGED);
			}
			finally
			{
				JaloSession.getCurrentSession().removeLocalSessionContext();
			}
		}

		totalProcessTime = System.currentTimeMillis() - totalProcessTime;

		for (final JaloSession createdJaloSession : createdJaloSessions)
		{
			Assert.assertEquals(createdJaloSession.getSessionContext().getAttribute(SPECIAL), SPECIAL);
		}

		printResults("modifing session context occasionally", MAX_TRIES, totalProcessTime);

	}


	private void printResults(final String dsc, final long totalCalls, final long totalProcessTime)
	{


		final String[] header =
		{ "description", "calls", "duration [ms]", "threads", "opers/ms" };
		ASCIITable.getInstance().printTable(
				header,
				new String[][]
				{
				{//
				dsc, String.valueOf(totalCalls), String.valueOf(totalProcessTime), String.valueOf(MAX_SESSION_COUNT),
						String.valueOf(((float) totalCalls / totalProcessTime)), // 
				} //
				});
	}
}
