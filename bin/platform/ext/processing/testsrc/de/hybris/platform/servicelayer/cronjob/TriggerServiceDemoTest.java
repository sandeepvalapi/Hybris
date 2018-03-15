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

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.cronjob.jalo.TestJob;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloTypeException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.impl.DefaultTriggerService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@DemoTest
@SuppressWarnings("deprecation")
public class TriggerServiceDemoTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(TriggerServiceDemoTest.class);

	protected final static long SECOND = 1000;
	protected final static long MINUTE = 60 * SECOND; //     60000
	protected final static long HOUR = 60 * MINUTE; //   3600000

	@Resource
	private TriggerService triggerService;

	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18NService;

	private ServicelayerJobModel job;

	@Before
	public void setUp() throws Exception
	{
		job = modelService.create(ServicelayerJobModel.class);
		job.setCode("cleanUpJob");
		job.setSpringId("cleanUpJobPerformable");
		modelService.save(job);
	}

	@After
	public void cleanUp()
	{
		if (!modelService.isNew(job))
		{
			modelService.remove(job);
		}
	}

	/**
	 * Create a new Jalo type and register it with the type system. If a type with the same type code and Jalo class
	 * already exists, it is returned instead.
	 *
	 * @param parentJaloClass
	 *           The Jalo class of the parent type.
	 * @param typeCode
	 *           Type code of the new type. Must be unique.
	 * @param jaloClass
	 *           The Jalo class of the new type.
	 * @return The new type instance.
	 * @throws JaloTypeException
	 *            Thrown if the code is not unique in the type system.
	 * @throws IllegalStateException
	 *            If a type with the same type code and a different Jalo class already exists in the type system.
	 */
	protected ComposedType createType(final Class<? extends Item> parentJaloClass, final String typeCode,
			final Class<? extends Item> jaloClass) throws JaloTypeException
	{
		final TypeManager manager = TypeManager.getInstance();
		ComposedType type;
		try
		{
			//Check if requested type already exists
			type = manager.getComposedType(typeCode);
			if (type.getJaloClass() == null)
			{
				type.setJaloClass(jaloClass);
			}
			else if (type.getJaloClass() != jaloClass)
			{
				throw new IllegalStateException(
						String.format("Type %s already exists with JALO class %s, cannot create with JALO class %s", typeCode,
								type.getJaloClass().getName(), jaloClass.getName()));
			}
		}
		catch (final JaloItemNotFoundException e)
		{
			//Type does not exist, create new composed type
			type = manager.createComposedType(manager.getComposedType(parentJaloClass), typeCode);
			type.setJaloClass(jaloClass);
		}
		return type;
	}

	/**
	 * Set the writable status of a type attribute.
	 *
	 * @param typeCode
	 *           The type code.
	 * @param modelClass
	 *           Model class for the type.
	 * @param attributeQualifier
	 *           Qualifier to identify the attribute.
	 * @param writable
	 *           New writable status.
	 * @return Original writable status of the given attribute.
	 */
	protected boolean setTypeAttributeWritable(final String typeCode, final Class<? extends AbstractItemModel> modelClass,
			final String attributeQualifier, final boolean writable)
	{
		final ComposedType cronJobType = TypeManager.getInstance().getComposedType(typeCode);
		final boolean originalState = cronJobType.getAttributeDescriptor(attributeQualifier).isWritable();
		cronJobType.getAttributeDescriptor(attributeQualifier).setWritable(writable);
		return originalState;
	}

	/**
	 * Mapping of cron job to cron job model may not be possible when some attributes of the cron job model are not
	 * writable. This test validates that the corresponding exception is caught (and logged) and does not cause failure
	 * of the cron job creation and execution.
	 */
	@Test
	public void testJobCreationWithNonWritableJobAttribute() throws JaloTypeException, ConsistencyCheckException
	{
		//Create a type for the TestJob class
		final ComposedType type = createType(Job.class, "TestJob", TestJob.class);

		//Set attribute of cron job type non-writable to cause exception during cron job configuration
		final boolean originalState = setTypeAttributeWritable("CronJob", CronJobModel.class, Job.CHANGERECORDINGENABLED, false);

		try
		{
			//Create a new job
			final Map attributes = new HashMap<>();
			attributes.put(Job.CODE, getClass().getName() + "TestJob");
			attributes.put(Job.ACTIVE, true);
			final TestJob testJob = (TestJob) type.newInstance(attributes);
			testJob.setPerformable(cronJob -> cronJob.getFinishedResult(true));

			//Try to create and execute a cron job from the job
			final JobModel jobModel = modelService.get(testJob);
			final TriggerModel trigger = configureNewTrigger(jobModel, -1, -1, -1, -1, -1, -1, null, false);
			final long now = trigger.getActivationTime().getTime();

			assertTrue("Cron job not executed!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		}
		finally
		{
			//Reset writable state of cron job type attribute
			setTypeAttributeWritable("CronJob", CronJobModel.class, Job.CHANGERECORDINGENABLED, originalState);
		}
	}

	@Test
	public void testActivationWithCronExpression()
	{
		// create a trigger who triggers every hour at 51 minutes
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronExpression("0 51 * * * ? *");
		trigger.setJob(job);
		trigger.setActive(Boolean.TRUE);
		// the interceptor will set the activationTime
		modelService.save(trigger);

		long now = System.currentTimeMillis();
		// get activation time already set
		final long activation = trigger.getActivationTime().getTime();
		// simulate activation
		assertFalse("Activation works!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		// get next activation time after activation
		long next = trigger.getActivationTime().getTime();
		// should be the same, because the trigger was not activated
		long delta = next - activation;
		// step 1 hour forward
		assertEquals("Time not equal!", HOUR, delta);

		now = next;
		assertTrue("Activation fails!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		next = trigger.getActivationTime().getTime();

		// test for 5 hours
		// Note: THIS WILL FAIL ON DAYLIGHT SAVING (sommerzeit/winterzeitumstellung)
		for (int i = 0; i < 5; i++)
		{
			delta = next - now;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + now + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 hour
			assertEquals("Time not equal!", HOUR, delta);
			now = next;
			assertTrue("Activation fails!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
			next = trigger.getActivationTime().getTime();
		}
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
				Calendar.getInstance(i18NService.getCurrentTimeZone(), i18NService.getCurrentLocale())).getTime());
		// Set the default value to -1, which means ignore
		trigger.setMaxAcceptableDelay(Integer.valueOf(-1));
		return trigger;
	}

	@Test
	public void testActivationOldWay()
	{
		// create a trigger who triggers every hour at 51 minutes
		// here we have to set the activationTime by our own
		final TriggerModel trigger = configureNewTrigger(job, -1, 51, -1, -1, -1, -1, null, false);
		trigger.setActive(Boolean.TRUE);
		modelService.save(trigger);

		long now = System.currentTimeMillis();
		// get activation time already set
		final long activation = trigger.getActivationTime().getTime();
		// simulate activation
		assertFalse("Activation works!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		// get next activation time after activation
		long next = trigger.getActivationTime().getTime();
		// should be the same, because the trigger was not activated
		long delta = next - activation;
		// step 1 hour forward
		assertEquals("Time not equal!", HOUR, delta);

		now = next;
		assertTrue("Activation fails!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
		next = trigger.getActivationTime().getTime();

		// test for 5 hours
		// Note: THIS WILL FAIL ON DAYLIGHT SAVING (sommerzeit/winterzeitumstellung)
		for (int i = 0; i < 5; i++)
		{
			delta = next - now;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("time=" + now + " next=" + next + " delta=" + delta);
			}
			// Time increments of 1 hour
			assertEquals("Time not equal!", HOUR, delta);
			now = next;
			assertTrue("Activation fails!", ((DefaultTriggerService) triggerService).activateForTest(trigger, now));
			next = trigger.getActivationTime().getTime();
		}
	}
}
