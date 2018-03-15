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
package de.hybris.platform.cronjob.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.constants.CronJobConstants;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.Utilities;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Test for Trigger
 */
@IntegrationTest
public class TriggerTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger log = Logger.getLogger(TriggerTest.class);
	private CronJobManager manager;
	private BatchJob batchJob;
	private CronJob cronJob;

	protected final static long SECOND = 1000;
	protected final static long MINUTE = 60 * SECOND; //     60000
	protected final static long HOUR = 60 * MINUTE; //   3600000
	protected final static long DAY = 24 * HOUR; //  86400000
	protected final static long WEEK = 7 * DAY; // 604800000

	@Before
	public void setUp() throws Exception
	{
		manager = (CronJobManager) jaloSession.getExtensionManager().getExtension(CronJobConstants.EXTENSIONNAME);
		batchJob = manager.createBatchJob("batchjob");
		assertNotNull(batchJob);
		cronJob = manager.createCronJob(batchJob, "cronJob", true);
		assertNotNull(cronJob);
	}

	@Test
	public void testEverySecond()
	{
		// creates a trigger who triggers every second
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, -1, -1, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = trigger.getNextTime(time);
		time = next;
		next = trigger.getNextTime(time);
		// test for 61 seconds
		for (int i = 0; i < 60; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 second
			assertEquals(SECOND, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testSeconds()
	{
		// creates a trigger who triggers every minute and 30 seconds
		final Trigger trigger = manager.createTrigger(cronJob, 30, -1, -1, -1, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = trigger.getNextTime(time);
		time = next;
		next = trigger.getNextTime(time);
		// test for 5 loops
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 minute
			assertEquals(MINUTE, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testMinutes()
	{
		// creates a trigger who triggers every minute
		final Trigger trigger = manager.createTrigger(cronJob, -1, 1, true);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// seconds are not considered
		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = trigger.getNextTime(time);
		time = next;
		next = trigger.getNextTime(time);
		// test for 5 minutes
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 minute
			assertEquals(MINUTE, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testHours()
	{
		// create a trigger who triggers every hour at 51 minutes
		final Trigger trigger = manager.createTrigger(cronJob, -1, 51, -1, -1, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// seconds are not considered
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// set current time to 2 minutes
		now.set(Calendar.MINUTE, 2);
		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		long delta = next - time;
		// expected difference is 49 minutes = 2940000
		final long expected = (51 - 2) * MINUTE;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);
		time = next;
		next = trigger.getNextTime(time);
		// test for 5 hours
		// Note: THIS WILL FAIL ON DAYLIGHT SAVING (sommerzeit/winterzeitumstellung)
		// 
		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 hour
			assertEquals(HOUR, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testDays()
	{
		// create a trigger who triggers every day at 5:51h
		final Trigger trigger = manager.createTrigger(cronJob, -1, 51, 5, -1, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors		
		now.set(Calendar.MONTH, Calendar.APRIL);
		// set current time to 3:02h
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		long delta = next - time;
		// expected difference is 2 hours and 49 minutes = 10140000
		final long expected = (51 - 2) * MINUTE + HOUR * (5 - 3);
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		// run for 5 days
		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 day
			assertEquals(DAY, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testMinutelessDays()
	{
		// create a trigger who triggers every day at 5:00h
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, 5, -1, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// seconds are not considered
		now.set(Calendar.SECOND, 0);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors		
		now.set(Calendar.MONTH, Calendar.APRIL);
		// set current time to 3:02h
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		long delta = next - time;
		// expected difference is 2 hours - 2 minutes = 7080000
		final long expected = (-2) * MINUTE + HOUR * (5 - 3);
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		// run for 5 days
		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 day
			assertEquals(DAY, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testWeekly()
	{
		// create a trigger who triggers every monday at 5:00h
		final EnumerationValue monday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.MONDAY);
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, 5, -1, -1, -1, Arrays.asList(new EnumerationValue[]
		{ monday }), false);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// set current time to 3:02h and to tuesday (third day of week)
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);

		long time = now.getTimeInMillis();
		long next = trigger.getNextTime(time);
		long delta = next - time;
		// expected difference is 6 days and 2 hours - 2 minutes = 525480000
		final long expected = (-2) * MINUTE + HOUR * (5 - 3) + DAY * 6;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);

		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 week
			assertEquals(WEEK, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testWeekly2()
	{
		// create a trigger who triggers every tuesday at 5:03h
		final EnumerationValue tuesday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.TUESDAY);
		final Trigger trigger = manager.createTrigger(cronJob, -1, 3, 5, -1, -1, -1, Arrays.asList(new EnumerationValue[]
		{ tuesday }), false);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// set current time to 3:02h and wednesday (fourth day of week)
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, 4);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		long delta = next - time;
		// expected difference is 6 days and 2 hours and 1 minute = 525660000
		final long expected = (1) * MINUTE + HOUR * (5 - 3) + DAY * 6;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);

		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 week
			assertEquals(WEEK, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testDayAMonth()
	{
		// create a trigger who triggers every second day a month at 5:03h
		final Trigger trigger = manager.createTrigger(cronJob, -1, 3, 5, 2, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// set current time to 3:02h and first day of month
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_MONTH, 1);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors		
		now.set(Calendar.MONTH, Calendar.APRIL);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		final long delta = next - time;
		// expected difference is 1 day and 2 hours and 1 minute = 93660000
		final long expected = (1) * MINUTE + HOUR * (5 - 3) + DAY * 1;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);

		int month = now.get(Calendar.MONTH);

		time = next;
		next = trigger.getNextTime(time);
		// test for 12 months
		for (int i = 0; i < 12; i++)
		{
			month = (month + 1) % 12;
			now.setTime(new Date(next));

			if (log.isDebugEnabled())
			{
				log.debug("day/Month h:min " + now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + " "
						+ now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
			}

			// Minute
			assertEquals(3, now.get(Calendar.MINUTE));
			// Hour of day
			assertEquals(5, now.get(Calendar.HOUR_OF_DAY));
			// Day of month
			assertEquals(2, now.get(Calendar.DAY_OF_MONTH));
			// Month - NOTE for december 0 is returned
			assertEquals(month, now.get(Calendar.MONTH));

			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testYearly()
	{
		// create a trigger who triggers yearly every april
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, -1, -1, 4, -1);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// set current time to 3:02h and first of march
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.set(Calendar.MONTH, 3);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		final long delta = next - time;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta);
		}
		now.setTime(new Date(next));

		// Minute
		assertEquals(0, now.get(Calendar.MINUTE));
		// Hour of Day
		assertEquals(0, now.get(Calendar.HOUR_OF_DAY));
		// Day of month
		assertEquals(1, now.get(Calendar.DAY_OF_MONTH));
		// Month
		assertEquals(4, now.get(Calendar.MONTH));

		time = next;
		next = trigger.getNextTime(time);
		// test for 5 years
		for (int i = 0; i < 5; i++)
		{
			now.setTime(new Date(next));

			if (log.isDebugEnabled())
			{
				log.debug("year day/Month h:min " + now.get(Calendar.YEAR) + " " + now.get(Calendar.DAY_OF_MONTH) + "/"
						+ now.get(Calendar.MONTH) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
			}

			// Minute
			assertEquals(0, now.get(Calendar.MINUTE));
			// Hour of Day
			assertEquals(0, now.get(Calendar.HOUR_OF_DAY));
			// Day of month
			assertEquals(1, now.get(Calendar.DAY_OF_MONTH));
			// Month
			assertEquals(4, now.get(Calendar.MONTH));

			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testOneYear()
	{
		// create a trigger who triggers only once per year at 01.01.2020 00:00:00
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, -1, -1, -1, 2020);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar cal = Utilities.getDefaultCalendar();
		// set current time to 03:02:00 01.03.2000
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 2);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 3);
		cal.set(Calendar.YEAR, 2000);

		long relativeTo = cal.getTime().getTime();
		long next = trigger.getNextTime(relativeTo);
		assertTrue(next > -1);
		final long delta = next - relativeTo;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + relativeTo + " next=" + next + " delta=" + delta);
		}
		cal.setTimeInMillis(next);
		// second
		assertEquals(0, cal.get(Calendar.SECOND));
		// Minute
		assertEquals(0, cal.get(Calendar.MINUTE));
		// Hour of Day
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
		// Day of month
		assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
		// month
		assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
		// year
		assertEquals(2020, cal.get(Calendar.YEAR));

		relativeTo = next;
		next = trigger.getNextTime(relativeTo);
		// year bail out
		assertEquals(-1, next);
	}

	@Test
	public void testRelativeValues()
	{
		// creates a trigger who triggers every 30 seconds
		final Trigger trigger = manager.createTrigger(cronJob, 30, true);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// seconds are not considered
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors		
		now.set(Calendar.MONTH, Calendar.APRIL);

		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = trigger.getNextTime(time);
		// test for 5 loops
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 30 seconds
			assertEquals(30 * SECOND, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
		// creates a trigger who triggers every minute
		trigger.setSecond(-1);
		trigger.setMinute(1);
		time = now.getTime().getTime();
		next = trigger.getNextTime(time);
		// test for 50 minutes
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 minute
			assertEquals(MINUTE, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// creates a trigger who triggers every 10 minutes
		trigger.setMinute(10);
		time = now.getTime().getTime();
		next = trigger.getNextTime(time);
		// test for 50 minutes
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 10 minute
			assertEquals(10 * MINUTE, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// creates a trigger who triggers every 2 hours
		trigger.setMinute(0);
		trigger.setHour(2);
		time = now.getTime().getTime();
		next = trigger.getNextTime(time);
		// test for 10 hours
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 2 hours
			assertEquals(2 * HOUR, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// the same with minutes not set
		trigger.setMinute(-1);
		time = now.getTime().getTime();
		next = trigger.getNextTime(time);
		// test for 10 hours
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 2 hours
			assertEquals(2 * HOUR, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// creates a trigger who triggers every 10 hours and 30 minutes
		trigger.setMinute(30);
		trigger.setHour(10);
		time = now.getTime().getTime();
		next = trigger.getNextTime(time);
		// test for 52 hours and 30 minutes
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 10 hours and 30 minutes
			assertEquals(10 * HOUR + 30 * MINUTE, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// modify the trigger to trigger every 5 days
		// NOTE: if daysOfWeek is set true, the interval flag is not considered
		trigger.setMinute(0);
		trigger.setHour(0);
		trigger.setDay(5);
		time = now.getTime().getTime();
		next = trigger.getNextTime(time);
		// test for 25 days
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 5 days
			assertEquals(5 * DAY, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// the same with minutes and hours not set
		trigger.setMinute(-1);
		trigger.setHour(-1);
		time = now.getTime().getTime();
		next = trigger.getNextTime(time);
		// test for 25 days
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 5 days
			assertEquals(5 * DAY, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testWeekInterval()
	{
		// create a trigger who triggers every monday at 5:00h
		final EnumerationValue monday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.MONDAY);
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, 5, -1, -1, -1, Arrays.asList(new EnumerationValue[]
		{ monday }), false);

		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// set current time to 3:02h and to tuesday (third day of week)
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, 3);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		long delta = next - time;
		// expected difference is 6 days and 2 hours - 2 minutes = 525480000
		long expected = (-2) * MINUTE + HOUR * (5 - 3) + DAY * 6;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);

		// run for 3 weeks
		for (int i = 0; i < 3; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 week
			assertEquals(WEEK, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// the same with attribute set
		trigger.setWeekInterval(1);
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is 1 week as well
		expected = WEEK;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);
		// run for 3 weeks
		for (int i = 0; i < 3; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 week
			assertEquals(WEEK, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// activate every second monday
		trigger.setWeekInterval(2);
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is 2 weeks
		expected = 2 * WEEK;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);
		// run for 6 weeks
		for (int i = 0; i < 3; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 2 week
			assertEquals(expected, delta);
			time = next;
			next = trigger.getNextTime(time);
		}

		// activate every second monday
		trigger.setWeekInterval(3);
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is 3 weeks
		expected = 3 * WEEK;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);
		// run for 9 weeks
		for (int i = 0; i < 3; i++)
		{
			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 3 week
			assertEquals(expected, delta);
			time = next;
			next = trigger.getNextTime(time);
		}
	}

	@Test
	public void testDateRange()
	{
		// create a trigger who triggers every day at 5:00h
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, 5, -1, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);

		final Calendar now = Utilities.getDefaultCalendar();
		// seconds are not considered
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// set current time to 3:02h
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.HOUR_OF_DAY, 3);
		//now.set( Calendar.DST_OFFSET, 0 );
		final Calendar end = (Calendar) now.clone();
		end.add(Calendar.DATE, 4);
		//end.add(Calendar.MONTH, 7);
		trigger.setDateRange(new StandardDateRange(now.getTime(), end.getTime()));
		long time = now.getTime().getTime();
		// first day
		long next = trigger.getNextTime(time);
		long delta = next - time;
		final long expected = HOUR * (5 - 3);
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		for (int i = 0; i < 3; i++)
		{
			final Calendar ctime = Utilities.getDefaultCalendar();
			ctime.setTimeInMillis(time);
			final Calendar cnext = Utilities.getDefaultCalendar();
			cnext.setTimeInMillis(next);
			final long daymillis = cnext.getTimeInMillis() - ctime.getTimeInMillis();

			delta = next - time;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 day
			assertEquals(daymillis, delta);
			if (DAY != daymillis)
			{
				log.warn("seems to be a zeitumstellung coming..");
			}

			time = next;
			next = trigger.getNextTime(time);
		}
		// fifth day should bail out
		assertEquals(-1, next);
	}

	@Test
	public void testDaysAWeek()
	{
		// create a trigger who triggers only at weekend at 5:00h
		final EnumerationValue saturday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.SATURDAY);
		final EnumerationValue sunday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.SUNDAY);
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, 5, -1, -1, -1, Arrays.asList(new EnumerationValue[]
		{ sunday, saturday }), false);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// set current time to 3:02h and to tuesday (third day of week)
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors		
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, 3);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		long delta = next - time;
		// expected difference is 4 days and 2 hours - 2 minutes
		long expected = (-2) * MINUTE + HOUR * (5 - 3) + DAY * (7 - 3);
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is 1 day
		expected = DAY;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is 6 days
		expected = 6 * DAY;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals(expected, delta);
	}

	@Test
	public void testDaysAWeekUnsorted()
	{
		// create a trigger who triggers only at 5:00h at different days not ordered
		final EnumerationValue monday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.MONDAY);
		final EnumerationValue tuesday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.TUESDAY);
		final EnumerationValue thursday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.THURSDAY);
		final EnumerationValue saturday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.SATURDAY);
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, 5, -1, -1, -1, Arrays.asList(new EnumerationValue[]
		{ tuesday, monday, saturday, thursday }), false);
		assertNotNull(trigger);
		trigger.setActive(true);
		final Calendar now = Utilities.getDefaultCalendar();
		// set current time to 3:02h and to tuesday (third day of week)
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors		
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, 3);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(time);
		long delta = next - time;
		// expected difference is 2 hours - 2 minutes (tuesday)
		long expected = (-2) * MINUTE + HOUR * (5 - 3);
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is 2 days (thursday)
		expected = 2 * DAY;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is 2 days as well (saturday)		
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is again 2 days as well (monday)
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals(expected, delta);

		time = next;
		next = trigger.getNextTime(time);
		delta = next - time;
		// expected difference is again one day (tuesday)
		expected = DAY;
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals(expected, delta);
	}

	@Test
	public void testDaysAWeekWithInterval()
	{
		Calendar cal = Utilities.getDefaultCalendar();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setLenient(true);
		testDaysAWeekWithInterval(cal);

		cal = Utilities.getDefaultCalendar();
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setLenient(true);
		testDaysAWeekWithInterval(cal);
	}

	private void testDaysAWeekWithInterval(final Calendar now)
	{
		// create a trigger who triggers only every second weekend at 5:00h
		final EnumerationValue saturday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.SATURDAY);
		final EnumerationValue sunday = jaloSession.getEnumerationManager().getEnumerationValue(CronJobConstants.TC.DAYOFWEEK,
				CronJobConstants.Enumerations.DayOfWeek.SUNDAY);
		final Trigger trigger = manager.createTrigger(cronJob, -1, -1, 5, -1, -1, -1, Arrays.asList(new EnumerationValue[]
		{ sunday, saturday }), false);
		assertNotNull(trigger);
		trigger.setActive(true);
		trigger.setWeekInterval(2);
		// set current time to 3:02h and to tuesday (third day of week)
		// NOTE: sunday is considered as the first day of the week
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors		
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		now.set(Calendar.MONTH, Calendar.APRIL);

		long time = now.getTime().getTime();
		long next = trigger.getNextTime(now);
		long delta = next - time;
		// expected difference is 4 days and 2 hours - 2 minutes
		long expected = (-2 * MINUTE) + (HOUR * (5 - 3)) + (DAY * (Calendar.SATURDAY - Calendar.TUESDAY));
		if (log.isDebugEnabled())
		{
			log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals(expected, delta);

		if (now.getFirstDayOfWeek() == Calendar.SUNDAY)
		{
			time = next;
			now.setTimeInMillis(time);
			next = trigger.getNextTime(now);
			delta = next - time;
			// expected difference is now 8 days: SAT -> SUN next(!) week
			expected = 8 * DAY;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
			}
			assertEquals(expected, delta);

			time = next;
			now.setTimeInMillis(time);
			next = trigger.getNextTime(now);
			delta = next - time;
			// expected difference is 6 days: SUN -> SAT same(!) week
			expected = 6 * DAY;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
			}
			assertEquals(expected, delta);
		}
		else if (now.getFirstDayOfWeek() == Calendar.MONDAY)
		{
			time = next;
			now.setTimeInMillis(time);
			next = trigger.getNextTime(now);
			delta = next - time;
			// expected difference is now 1 day: SAT -> SUN
			expected = 1 * DAY;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
			}
			assertEquals(expected, delta);

			time = next;
			now.setTimeInMillis(time);
			next = trigger.getNextTime(now);
			delta = next - time;
			// expected difference is 13 days: SUN -> SAT next week
			expected = 13 * DAY;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
			}
			assertEquals(expected, delta);

		}
		else
		{
			fail("exepected one of " + Calendar.MONDAY + " and " + Calendar.SUNDAY + " as first day of week");
		}
	}

	@Test
	public void testActivation()
	{
		// create a trigger who triggers every hour at 51 minutes
		final Trigger trigger = manager.createTrigger(cronJob, -1, 51, -1, -1, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);

		long now = System.currentTimeMillis();
		// get activation time already set
		final long activation = trigger.getActivationTime().getTime();
		// simulate activation		
		assertFalse(trigger.activateForTest(now));
		// get next activation time after activation
		long next = trigger.getActivationTime().getTime();
		// should be the same, because the trigger was not activated 
		long delta = next - activation;
		// step 1 hour forward

		now = next;
		assertTrue(trigger.activateForTest(now));
		next = trigger.getActivationTime().getTime();

		// test for 5 hours
		// Note: THIS WILL FAIL ON DAYLIGHT SAVING (sommerzeit/winterzeitumstellung)
		for (int i = 0; i < 5; i++)
		{
			delta = next - now;
			if (log.isDebugEnabled())
			{
				log.debug("time=" + now + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 hour
			assertEquals(HOUR, delta);
			now = next;
			assertTrue(trigger.activateForTest(now));
			next = trigger.getActivationTime().getTime();
		}
	}

	@Test
	public void testCreateTriggers()
	{
		final CronJobManager cronJobManager = CronJobManager.getInstance();
		final BatchJob batchJob = cronJobManager.createBatchJob("batchjob1");
		assertNotNull(batchJob);
		final CronJob cronJob1, cronJob2;
		assertNotNull(cronJob1 = cronJobManager.createCronJob(batchJob, "cronJob1", true));
		assertNotNull(cronJob2 = cronJobManager.createCronJob(batchJob, "cronJob2", true));

		final Trigger trigger1, trigger2, trigger3;
		assertNotNull(trigger1 = cronJobManager.createTrigger(cronJob1, -1, -1, -1, -1, -1, -1));
		assertNotNull(trigger2 = cronJobManager.createTrigger(cronJob2, -1, -1, -1, -1, -1, -1));
		assertNotNull(trigger3 = cronJobManager.createTrigger(cronJob1, -1, -1, -1, -1, -1, -1));

		assertTrue(trigger1.getCronJob().equals(cronJob1));
		assertTrue(trigger2.getCronJob().equals(cronJob2));
		assertTrue(trigger3.getCronJob().equals(cronJob1));
		assertTrue(cronJob1.getTriggers().size() == 2);
		assertTrue(cronJob2.getTriggers().size() == 1);
	}

	@Test
	public void testEndlessCycleBug()
	{
		final Trigger trigger;
		// every 1th per month at 2:30:00
		assertNotNull(trigger = manager.createTrigger(cronJob, 0, 30, 2, // 02:30:00 
				1, // every 01.
				-1, // month variable 
				false // not relative
				));
		trigger.setActive(false);

		final Calendar _cal = Utilities.getDefaultCalendar();
		_cal.set(Calendar.YEAR, 2006);
		_cal.set(Calendar.MONTH, Calendar.DECEMBER);
		_cal.set(Calendar.DAY_OF_MONTH, 1);
		_cal.set(Calendar.HOUR_OF_DAY, 2);
		_cal.set(Calendar.MINUTE, 30);
		_cal.set(Calendar.SECOND, 0);
		_cal.set(Calendar.MILLISECOND, 0);

		final Date lastTime = _cal.getTime();

		_cal.set(Calendar.DAY_OF_MONTH, 5);
		_cal.set(Calendar.HOUR_OF_DAY, 12);
		_cal.set(Calendar.MINUTE, 44);
		_cal.set(Calendar.SECOND, 23);
		_cal.set(Calendar.MILLISECOND, 0);

		final Date now = _cal.getTime();

		// set current activation time to 01.12.2006 02:30:00 0000
		trigger.setActivationTime(lastTime);

		assertEquals(lastTime, trigger.getActivationTime());

		try
		{
			assertTrue(trigger.activateForTest(now.getTime()));
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}

		_cal.set(Calendar.YEAR, 2007);
		_cal.set(Calendar.MONTH, Calendar.JANUARY);
		_cal.set(Calendar.DAY_OF_MONTH, 1);
		_cal.set(Calendar.HOUR_OF_DAY, 2);
		_cal.set(Calendar.MINUTE, 30);
		_cal.set(Calendar.SECOND, 0);
		_cal.set(Calendar.MILLISECOND, 0);

		final Date expected = _cal.getTime();

		// now activation time should be 01.01.2007 02:30:00 0000
		assertEquals(expected, trigger.getActivationTime());
	}

	@Test
	public void testMaxDelay()
	{
		final long oneHour = 60 * 60 * 1000;
		final long twoHours = 2 * oneHour;
		//The field MaxAcceptableDelay describes how long after the job was scheduled can it still be triggered.
		// If set to minus one, the value is ignored.
		// If set to <T>seconds, then this job can only be triggered up to <T>seconds after it should have run.
		// This is to prevent the following scenario:
		// a heavy job is scheduled to run at midnight, but the job engine crashes before hand. When the job 
		// engine restarts at say 9am the next day, the heavy job is then run.  By saying that this job can only
		// run up to (say) 1 hour after its scheduled time, we prevent the heavy job running in the day time.

		// Create a trigger who triggers every hour at 51 minutes
		final Trigger trigger = manager.createTrigger(cronJob, -1, 51, -1, -1, -1, -1);
		trigger.setActive(true);

		// Get now in milliseconds
		long now = System.currentTimeMillis();

		// Trigger time is still in the future, so should not run
		assertFalse(trigger.activateForTest(now));

		// step 2 hours forward and the job should now activate, as we have no delay window
		now += twoHours;
		assertTrue(trigger.activateForTest(now));
		// step 2 hours forward and the job should again activate, as we have no delay window
		now += twoHours;
		assertTrue(trigger.activateForTest(now));
		// step 2 hours forward but set the MaxAcceptableDelay to 60 seconds and it should not run as we are too late
		now += twoHours;
		trigger.setMaxAcceptableDelay(60);
		assertFalse(trigger.activateForTest(now));
		trigger.setMaxAcceptableDelay(-1);
		now += oneHour;
		// but should run again now as we hvae removed the delay window again
		assertTrue(trigger.activateForTest(now));
		// step 2 hours forward and the job should now activate, as we have no delay window
		now += twoHours;
		assertTrue(trigger.activateForTest(now));

	}

	@Test
	public void testMaxDelayTooSmall()
	{
		//The field MaxAcceptableDelay describes how long after the job was scehduled can it still be triggered.
		final int pulseSecs = Trigger.getPulseseconds();
		final Trigger trigger = manager.createTrigger(cronJob, -1, 51, -1, -1, -1, -1);
		// The default value should be -1, which means ignore this field
		assertTrue(trigger.getMaxAcceptableDelay().equals(Integer.valueOf(-1)));
		// It should not be possible to set the value to something less than the regular "pulse" (checking period) of
		// the trigger engine, specified by cronjob.trigger.interval in project.properties
		trigger.setMaxAcceptableDelay(pulseSecs - 1);
		assertTrue(trigger.getMaxAcceptableDelay().equals(Integer.valueOf(-1)));
		// Negative values are also not allowed
		trigger.setMaxAcceptableDelay(-123);
		assertTrue(trigger.getMaxAcceptableDelay().equals(Integer.valueOf(-1)));
		// This delay is valid and should be allowed
		trigger.setMaxAcceptableDelay(2 * pulseSecs);
		assertTrue(trigger.getMaxAcceptableDelay().equals(Integer.valueOf(2 * pulseSecs)));
	}

	@Test
	public void testAvoidACronJobsJobTwiceInParallel()
	{
		final long oneHour = 60 * 60 * 1000;
		final long twoHours = 2 * oneHour;
		// create a trigger who triggers every hour at 51 minutes
		final Trigger trigger = manager.createTrigger(cronJob, -1, 51, -1, -1, -1, -1);
		assertNotNull(trigger);
		trigger.setActive(true);

		// Get now in milliseconds
		long now = System.currentTimeMillis();
		// get activation time already set
		trigger.getActivationTime().getTime();
		// simulate activation		
		assertFalse(trigger.activateForTest(now));
		// get next activation time after activation

		// step 2 hours forward and the job should now activate
		now += twoHours;
		assertTrue(trigger.activateForTest(now));

		// step 2 hours forward but set the cronJob status to running 
		cronJob.setStatus(cronJob.getRunningStatus());
		now += twoHours;
		// now the job should not run, as the cronJob status is already running
		assertFalse(trigger.activateForTest(now));
		// Set the cronJob status to finished
		cronJob.setStatus(cronJob.getFinishedStatus());
		now += twoHours;
		// now the event should fire
		assertTrue(trigger.activateForTest(now));
	}


}
