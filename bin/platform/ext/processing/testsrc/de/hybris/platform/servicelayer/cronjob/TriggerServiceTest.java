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
package de.hybris.platform.servicelayer.cronjob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.cronjob.impl.DefaultTriggerService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.StandardDateRange;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TriggerServiceTest extends ServicelayerTransactionalBaseTest
{
	private static final Logger LOG = Logger.getLogger(TriggerServiceTest.class);

	private ServicelayerJobModel job;

	protected final static long SECOND = 1000;
	protected final static long MINUTE = 60 * SECOND; //     60000
	protected final static long HOUR = 60 * MINUTE; //   3600000
	protected final static long DAY = 24 * HOUR; //  86400000
	protected final static long WEEK = 7 * DAY; // 604800000

	@Resource
	private TriggerService triggerService;

	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18nService;

	@Resource
	private EnumerationService enumerationService;

	@Before
	public void setUp() throws Exception
	{
		job = modelService.create(ServicelayerJobModel.class);
		job.setCode("cleanUpJob");
		job.setSpringId("cleanUpJobPerformable");
		modelService.save(job);
	}

	/**
	 * Configures a new <code>TriggerModel</code> without saving.
	 *
	 * @param job
	 *           the <code>JobModel</code> the <code>TriggerModel</code> should be assigned to
	 * @param second
	 *           the second at which the <code>TriggerModel</code> should fire (0-59 or -1 for every second)
	 * @param minute
	 *           the minute at which the <code>TriggerModel</code> should fire (0-59 or -1 for every minute)
	 * @param hour
	 *           hour at which the <code>TriggerModel</code> should fire (0-23 or -1 for every hour)
	 * @param day
	 *           the day of month the <code>TriggerModel</code> should fire (0-31 or -1 for every day)
	 * @param month
	 *           the month at which the <code>TriggerModel</code> should fire (0-11 or -1 for every month)
	 * @param year
	 *           the year when the <code>TriggerModel</code> should fire (-1 for every year)
	 * @param daysOfWeek
	 *           a <code>List</code> of
	 * @param relative
	 *           true if time values should be considered relative to each other or false if not
	 * @return the TriggerModel created
	 */
	private TriggerModel configureNewTrigger(final JobModel job, final int second, final int minute, final int hour,
			final int day, final int month, final int year, final List daysOfWeek, final boolean relative)
	{
		final TriggerModel trigger = modelService.create(TriggerModel.class);

		trigger.setJob(job);
		trigger.setSecond(Integer.valueOf(second));
		trigger.setMinute(Integer.valueOf(minute));
		trigger.setHour(Integer.valueOf(hour));
		trigger.setDay(Integer.valueOf(day));
		trigger.setMonth(Integer.valueOf(month));
		trigger.setYear(Integer.valueOf(year));
		trigger.setDaysOfWeek(daysOfWeek);
		trigger.setRelative(Boolean.valueOf(relative));
		trigger.setActivationTime(triggerService.getNextTime(trigger,
				Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale())).getTime());
		// Set the default value to -1, which means ignore
		trigger.setMaxAcceptableDelay(Integer.valueOf(-1));

		return trigger;
	}

	@Test
	public void testEverySecond()
	{
		//creates a trigger who triggers every second
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, -1, -1, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 61 seconds
		for (int i = 0; i < 60; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 second
			assertEquals("Time not equal!", SECOND, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testSeconds()
	{
		// creates a trigger who triggers every minute and 30 seconds
		final TriggerModel trigger = configureNewTrigger(job, 30, -1, -1, -1, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 5 loops
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 minute
			assertEquals("Time not equal!", MINUTE, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testSecondsCronExpression()
	{
		// creates a trigger who triggers every minute and 30 seconds
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronExpression("30 * * * * ? *");
		trigger.setJob(job);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 5 loops
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 minute
			assertEquals("Time not equal!", MINUTE, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testMinutes()
	{
		// creates a trigger who triggers every minute
		final TriggerModel trigger = configureNewTrigger(job, -1, 1, -1, -1, -1, -1, null, true);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 5 minutes
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 minute
			assertEquals("Time not equal!", MINUTE, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testHours()
	{
		// create a trigger who triggers every hour at 51 minutes
		final TriggerModel trigger = configureNewTrigger(job, -1, 51, -1, -1, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// seconds are not considered
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// set current time to 2 minutes
		now.set(Calendar.MINUTE, 2);
		long time = now.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		// expected difference is 49 minutes = 2940000
		final long expected = (51 - 2) * MINUTE;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);
		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 5 hours
		// Note: THIS WILL FAIL ON DAYLIGHT SAVING (sommerzeit/winterzeitumstellung)
		//
		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 hour
			assertEquals("Time not equal!", HOUR, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testDays()
	{
		// create a trigger who triggers every day at 5:51h
		final TriggerModel trigger = configureNewTrigger(job, -1, 51, 5, -1, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		// set current time to 3:02h
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);

		long time = now.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		// expected difference is 2 hours and 49 minutes = 10140000
		final long expected = (51 - 2) * MINUTE + HOUR * (5 - 3);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// run for 5 days
		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 day
			assertEquals("Time not equal!", DAY, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testMinutelessDays()
	{
		// create a trigger who triggers every day at 5:00h
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, 5, -1, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
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
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		// expected difference is 2 hours - 2 minutes = 7080000
		final long expected = (-2) * MINUTE + HOUR * (5 - 3);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// run for 5 days
		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 day
			assertEquals("Time not equal!", DAY, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testWeekly()
	{
		// create a trigger who triggers every monday at 5:00h
		final DayOfWeek monday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.MONDAY.getCode());
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, 5, -1, -1, -1, Arrays.asList(monday), false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// set current time to 3:02h and to tuesday (third day of week)
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);

		long time = now.getTimeInMillis();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		// expected difference is 6 days and 2 hours - 2 minutes = 525480000
		final long expected = (-2) * MINUTE + HOUR * (5 - 3) + DAY * 6;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();

		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 week
			assertEquals("Time not equal!", WEEK, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testWeekly2()
	{
		// create a trigger who triggers every tuesday at 5:03h
		final DayOfWeek tuesday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.TUESDAY.getCode());
		final TriggerModel trigger = configureNewTrigger(job, -1, 3, 5, -1, -1, -1, Arrays.asList(tuesday), false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// set current time to 3:02h and wednesday (fourth day of week)
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, 4);

		long time = now.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		// expected difference is 6 days and 2 hours and 1 minute = 525660000
		final long expected = (1) * MINUTE + HOUR * (5 - 3) + DAY * 6;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();

		for (int i = 0; i < 5; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 week
			assertEquals("Time not equal!", WEEK, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testDayAMonth()
	{
		// create a trigger who triggers every second day a month at 5:03h
		final TriggerModel trigger = configureNewTrigger(job, -1, 3, 5, 2, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// set current time to 3:02h and first day of month
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_MONTH, 1);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);

		long time = now.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		final long delta = next - time;
		// expected difference is 1 day and 2 hours and 1 minute = 93660000
		final long expected = (1) * MINUTE + HOUR * (5 - 3) + DAY * 1;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);

		int month = now.get(Calendar.MONTH);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 12 months
		for (int i = 0; i < 12; i++)
		{
			month = (month + 1) % 12;
			now.setTime(new Date(next));

			if (LOG.isDebugEnabled())
			{
				LOG.debug("day/Month h:min " + now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + " "
						+ now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
			}

			// Minute
			assertEquals("Time not equal!", 3, now.get(Calendar.MINUTE));
			// Hour of day
			assertEquals("Time not equal!", 5, now.get(Calendar.HOUR_OF_DAY));
			// Day of month
			assertEquals("Time not equal!", 2, now.get(Calendar.DAY_OF_MONTH));
			// Month - NOTE for december 0 is returned
			assertEquals("Time not equal!", month, now.get(Calendar.MONTH));

			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testYearly()
	{
		// create a trigger who triggers yearly every april
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, -1, -1, 4, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// set current time to 3:02h and first of march
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.set(Calendar.MONTH, 3);

		long time = now.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		final long delta = next - time;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
		}
		now.setTime(new Date(next));

		// Minute
		assertEquals("Time not equal!", 0, now.get(Calendar.MINUTE));
		// Hour of Day
		assertEquals("Time not equal!", 0, now.get(Calendar.HOUR_OF_DAY));
		// Day of month
		assertEquals("Time not equal!", 1, now.get(Calendar.DAY_OF_MONTH));
		// Month
		assertEquals("Time not equal!", 4, now.get(Calendar.MONTH));

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 5 years
		for (int i = 0; i < 5; i++)
		{
			now.setTime(new Date(next));

			if (LOG.isDebugEnabled())
			{
				LOG.debug("year day/Month h:min " + now.get(Calendar.YEAR) + " " + now.get(Calendar.DAY_OF_MONTH) + "/"
						+ now.get(Calendar.MONTH) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
			}

			// Minute
			assertEquals("Time not equal!", 0, now.get(Calendar.MINUTE));
			// Hour of Day
			assertEquals("Time not equal!", 0, now.get(Calendar.HOUR_OF_DAY));
			// Day of month
			assertEquals("Time not equal!", 1, now.get(Calendar.DAY_OF_MONTH));
			// Month
			assertEquals("Time not equal!", 4, now.get(Calendar.MONTH));

			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testOneYear()
	{
		// create a trigger who triggers only once per year at 01.01.2020 00:00:00
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, -1, -1, -1, 2020, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar cal = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// set current time to 03:02:00 01.03.2000
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 2);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 3);
		cal.set(Calendar.YEAR, 2000);

		long relativeTo = cal.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(relativeTo)).getTimeInMillis();
		assertTrue("Wrong time!", next > -1);
		final long delta = next - relativeTo;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + relativeTo + " next=" + next + " delta=" + delta);
		}
		cal.setTimeInMillis(next);
		// second
		assertEquals("Time not equal!", 0, cal.get(Calendar.SECOND));
		// Minute
		assertEquals("Time not equal!", 0, cal.get(Calendar.MINUTE));
		// Hour of Day
		assertEquals("Time not equal!", 0, cal.get(Calendar.HOUR_OF_DAY));
		// Day of month
		assertEquals("Time not equal!", 1, cal.get(Calendar.DAY_OF_MONTH));
		// month
		assertEquals("Time not equal!", Calendar.JANUARY, cal.get(Calendar.MONTH));
		// year
		assertEquals("Time not equal!", 2020, cal.get(Calendar.YEAR));

		relativeTo = next;
		next = triggerService.getNextTime(trigger, getCalendar(relativeTo)).getTimeInMillis();
		// year bail out
		assertEquals("Time not equal!", -1, next);
	}

	@Test
	public void testRelativeValues()
	{
		// creates a trigger who triggers every 30 seconds
		final TriggerModel trigger = configureNewTrigger(job, 30, -1, -1, -1, -1, -1, null, true);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// seconds are not considered
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);

		// current time in millis
		long time = now.getTime().getTime();
		// next update time in millis
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 5 loops
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 30 seconds
			assertEquals("Time not equal!", 30 * SECOND, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
		// creates a trigger who triggers every minute
		trigger.setSecond(Integer.valueOf(-1));
		trigger.setMinute(Integer.valueOf(1));
		modelService.save(trigger);
		time = now.getTime().getTime();
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 50 minutes
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 minute
			assertEquals("Time not equal!", MINUTE, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// creates a trigger who triggers every 10 minutes
		trigger.setMinute(Integer.valueOf(10));
		modelService.save(trigger);
		time = now.getTime().getTime();
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 50 minutes
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 10 minute
			assertEquals("Time not equal!", 10 * MINUTE, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// creates a trigger who triggers every 2 hours
		trigger.setMinute(Integer.valueOf(0));
		trigger.setHour(Integer.valueOf(2));
		modelService.save(trigger);
		time = now.getTime().getTime();
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 10 hours
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 2 hours
			assertEquals("Time not equal!", 2 * HOUR, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// the same with minutes not set
		trigger.setMinute(Integer.valueOf(-1));
		modelService.save(trigger);
		time = now.getTime().getTime();
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 10 hours
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 2 hours
			assertEquals("Time not equal!", 2 * HOUR, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// creates a trigger who triggers every 10 hours and 30 minutes
		trigger.setMinute(Integer.valueOf(30));
		trigger.setHour(Integer.valueOf(10));
		modelService.save(trigger);
		time = now.getTime().getTime();
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 52 hours and 30 minutes
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 10 hours and 30 minutes
			assertEquals("Time not equal!", 10 * HOUR + 30 * MINUTE, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// modify the trigger to trigger every 5 days
		// NOTE: if daysOfWeek is set true, the interval flag is not considered
		trigger.setMinute(Integer.valueOf(0));
		trigger.setHour(Integer.valueOf(0));
		trigger.setDay(Integer.valueOf(5));
		modelService.save(trigger);
		time = now.getTime().getTime();
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 25 days
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 5 days
			assertEquals("Time not equal!", 5 * DAY, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// the same with minutes and hours not set
		trigger.setMinute(Integer.valueOf(-1));
		trigger.setHour(Integer.valueOf(-1));
		modelService.save(trigger);
		time = now.getTime().getTime();
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		// test for 25 days
		for (int i = 0; i < 5; i++)
		{
			final long delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 5 days
			assertEquals("Time not equal!", 5 * DAY, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testWeekInterval()
	{
		// create a trigger who triggers every monday at 5:00h
		final DayOfWeek monday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.MONDAY.getCode());
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, 5, -1, -1, -1, Arrays.asList(monday), false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// set current time to 3:02h and to tuesday (third day of week)
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, 3);

		long time = now.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		// expected difference is 6 days and 2 hours - 2 minutes = 525480000
		long expected = (-2) * MINUTE + HOUR * (5 - 3) + DAY * 6;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();

		// run for 3 weeks
		for (int i = 0; i < 3; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 week
			assertEquals("Time not equal!", WEEK, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// the same with attribute set
		trigger.setWeekInterval(Integer.valueOf(1));
		modelService.save(trigger);
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is 1 week as well
		expected = WEEK;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);
		// run for 3 weeks
		for (int i = 0; i < 3; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 week
			assertEquals("Time not equal!", WEEK, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// activate every second monday
		trigger.setWeekInterval(Integer.valueOf(2));
		modelService.save(trigger);
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is 2 weeks
		expected = 2 * WEEK;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);
		// run for 6 weeks
		for (int i = 0; i < 3; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 2 week
			assertEquals("Time not equal!", expected, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}

		// activate every second monday
		trigger.setWeekInterval(Integer.valueOf(3));
		modelService.save(trigger);
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is 3 weeks
		expected = 3 * WEEK;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);
		// run for 9 weeks
		for (int i = 0; i < 3; i++)
		{
			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 3 week
			assertEquals("Time not equal!", expected, delta);
			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
	}

	@Test
	public void testDateRange()
	{
		// create a trigger who triggers every day at 5:00h
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, 5, -1, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
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
		modelService.save(trigger);
		long time = now.getTime().getTime();
		// first day
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		final long expected = HOUR * (5 - 3);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		for (int i = 0; i < 3; i++)
		{
			final Calendar ctime = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
			ctime.setTimeInMillis(time);
			final Calendar cnext = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
			cnext.setTimeInMillis(next);
			final long daymillis = cnext.getTimeInMillis() - ctime.getTimeInMillis();

			delta = next - time;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 day
			assertEquals("Time not equal!", daymillis, delta);
			if (DAY != daymillis)
			{
				LOG.warn("seems to be a zeitumstellung coming..");
			}

			time = next;
			next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		}
		// fifth day should bail out
		assertEquals("Time not equal!", -1, next);
	}

	@Test
	public void testDaysAWeek()
	{
		// create a trigger who triggers only at weekend at 5:00h
		final DayOfWeek saturday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.SATURDAY.getCode());
		final DayOfWeek sunday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.SUNDAY.getCode());
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, 5, -1, -1, -1, Arrays.asList(saturday, sunday), false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// set current time to 3:02h and to tuesday (third day of week)
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, 3);

		long time = now.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		// expected difference is 4 days and 2 hours - 2 minutes
		long expected = (-2) * MINUTE + HOUR * (5 - 3) + DAY * (7 - 3);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is 1 day
		expected = DAY;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is 6 days
		expected = 6 * DAY;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals("Time not equal!", expected, delta);
	}

	@Test
	public void testDaysAWeekUnsorted()
	{
		// create a trigger who triggers only at 5:00h at different days not ordered
		final DayOfWeek monday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.MONDAY.getCode());
		final DayOfWeek tuesday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.TUESDAY.getCode());
		final DayOfWeek thursday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.THURSDAY.getCode());
		final DayOfWeek saturday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.SATURDAY.getCode());
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, 5, -1, -1, -1,
				Arrays.asList(tuesday, monday, saturday, thursday), false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		final Calendar now = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		// set current time to 3:02h and to tuesday (third day of week)
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		// NOTE: we set the month to april otherwise summer/winter time offset could force errors
		now.set(Calendar.MONTH, Calendar.APRIL);
		now.set(Calendar.MINUTE, 2);
		now.set(Calendar.HOUR_OF_DAY, 3);
		now.set(Calendar.DAY_OF_WEEK, 3);

		long time = now.getTime().getTime();
		long next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		long delta = next - time;
		// expected difference is 2 hours - 2 minutes (tuesday)
		long expected = (-2) * MINUTE + HOUR * (5 - 3);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		// Collect time at start
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is 2 days (thursday)
		expected = 2 * DAY;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is 2 days as well (saturday)
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is again 2 days as well (monday)
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals("Time not equal!", expected, delta);

		time = next;
		next = triggerService.getNextTime(trigger, getCalendar(time)).getTimeInMillis();
		delta = next - time;
		// expected difference is again one day (tuesday)
		expected = DAY;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals("Time not equal!", expected, delta);
	}

	@Test
	public void testDaysAWeekWithInterval() //NOPMD
	{
		Calendar cal = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setLenient(true);
		testDaysAWeekWithInterval(cal);

		cal = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setLenient(true);
		testDaysAWeekWithInterval(cal);
	}

	private void testDaysAWeekWithInterval(final Calendar now)
	{
		// create a trigger who triggers only every second weekend at 5:00h
		final DayOfWeek saturday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.SATURDAY.getCode());
		final DayOfWeek sunday = enumerationService.getEnumerationValue(DayOfWeek._TYPECODE, DayOfWeek.SUNDAY.getCode());
		final TriggerModel trigger = configureNewTrigger(job, -1, -1, 5, -1, -1, -1, Arrays.asList(saturday, sunday), false);
		trigger.setActive(Boolean.TRUE);
		trigger.setWeekInterval(Integer.valueOf(2));
		modelService.save(trigger);

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
		long next = triggerService.getNextTime(trigger, now).getTimeInMillis();
		long delta = next - time;
		// expected difference is 4 days and 2 hours - 2 minutes
		long expected = (-2 * MINUTE) + (HOUR * (5 - 3)) + (DAY * (Calendar.SATURDAY - Calendar.TUESDAY));
		if (LOG.isDebugEnabled())
		{
			LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
		}
		assertEquals("Time not equal!", expected, delta);

		if (now.getFirstDayOfWeek() == Calendar.SUNDAY)
		{
			time = next;
			now.setTimeInMillis(time);
			next = triggerService.getNextTime(trigger, now).getTimeInMillis();
			delta = next - time;
			// expected difference is now 8 days: SAT -> SUN next(!) week
			expected = 8 * DAY;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
			}
			assertEquals("Time not equal!", expected, delta);

			time = next;
			now.setTimeInMillis(time);
			next = triggerService.getNextTime(trigger, now).getTimeInMillis();
			delta = next - time;
			// expected difference is 6 days: SUN -> SAT same(!) week
			expected = 6 * DAY;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
			}
			assertEquals("Time not equal!", expected, delta);
		}
		else if (now.getFirstDayOfWeek() == Calendar.MONDAY)
		{
			time = next;
			now.setTimeInMillis(time);
			next = triggerService.getNextTime(trigger, now).getTimeInMillis();
			delta = next - time;
			// expected difference is now 1 day: SAT -> SUN
			expected = 1 * DAY;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
			}
			assertEquals("Time not equal!", expected, delta);

			time = next;
			now.setTimeInMillis(time);
			next = triggerService.getNextTime(trigger, now).getTimeInMillis();
			delta = next - time;
			// expected difference is 13 days: SUN -> SAT next week
			expected = 13 * DAY;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + time + " next=" + next + " delta=" + delta + " expected=" + expected);
			}
			assertEquals("Time not equal!", expected, delta);

		}
		else
		{
			fail("exepected one of " + Calendar.MONDAY + " and " + Calendar.SUNDAY + " as first day of week");
		}
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
		final TriggerModel trigger = configureNewTrigger(job, -1, 51, -1, -1, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		// Get now in milliseconds
		long now = System.currentTimeMillis();

		// Trigger time is still in the future, so should not run
		assertFalse("Activation works!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));

		// step 2 hours forward and the job should now activate, as we have no delay window
		now += twoHours;
		assertTrue("Activation fails!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		// step 2 hours forward and the job should again activate, as we have no delay window
		now += twoHours;
		assertTrue("Activation fails!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		// step 2 hours forward but set the MaxAcceptableDelay to 60 seconds and it should not run as we are too late
		now += twoHours;
		trigger.setMaxAcceptableDelay(Integer.valueOf(60));
		modelService.save(trigger);
		assertFalse("Activation works!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		trigger.setMaxAcceptableDelay(Integer.valueOf(-1));
		modelService.save(trigger);
		now += oneHour;
		// but should run again now as we have removed the delay window again
		assertTrue("Activation fails!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		// step 2 hours forward and the job should now activate, as we have no delay window
		now += twoHours;
		assertTrue("Activation fails!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));

	}

	@Test
	public void testMaxDelayTooSmall()
	{
		//The field MaxAcceptableDelay describes how long after the job was scehduled can it still be triggered.
		final int pulseSecs = triggerService.getPulseSeconds();
		final TriggerModel trigger = configureNewTrigger(job, -1, 51, -1, -1, -1, -1, null, false);
		modelService.save(trigger);

		// The default value should be -1, which means ignore this field
		assertTrue("Wrong max acceptable delay!", trigger.getMaxAcceptableDelay().equals(Integer.valueOf(-1)));
		// It should not be possible to set the value to something less than the regular "pulse" (checking period) of
		// the trigger engine, specified by cronjob.trigger.interval in project.properties
		trigger.setMaxAcceptableDelay(Integer.valueOf(pulseSecs - 1));
		modelService.save(trigger);
		assertTrue("Wrong max acceptable delay!", trigger.getMaxAcceptableDelay().equals(Integer.valueOf(-1)));
		// Negative values are also not allowed
		trigger.setMaxAcceptableDelay(Integer.valueOf(-123));
		modelService.save(trigger);
		assertTrue("Wrong max acceptable delay!", trigger.getMaxAcceptableDelay().equals(Integer.valueOf(-1)));
		// This delay is valid and should be allowed
		trigger.setMaxAcceptableDelay(Integer.valueOf(2 * pulseSecs));
		modelService.save(trigger);
		assertTrue("Wrong max acceptable delay!", trigger.getMaxAcceptableDelay().equals(Integer.valueOf(2 * pulseSecs)));
	}

	private Calendar getCalendar(final long miliseconds)
	{
		final Calendar calendar = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		calendar.setTimeInMillis(miliseconds);

		return calendar;
	}
}
