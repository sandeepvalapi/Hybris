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

import static junit.framework.Assert.*;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * It is now possible to trigger jobs directly from the trigger, without going via a cronJob. This class tests that such
 * a job can be triggered. The relationship between Trigger, Job and CronJob is now.. Job implements ITriggerableJob
 * O--------------------- O .................................| ................| | ...............................{XOR}
 * ..............| | .................................|.................| CronJob
 * O-----------------------------------------Trigger
 */
@IntegrationTest
public class TriggerableJobTest extends HybrisJUnit4Test
{
	private final TypeManager typeManager = TypeManager.getInstance();
	private final CronJobManager cronJobManager = CronJobManager.getInstance();

	private static final String JOB_NOT_CHANGEABLE = "attribute 'job' is not changeable";
	private static final String CRONJOB_NOT_CHANGEABLE = "attribute 'cronJob' is not changeable";
	private static final String MISSING_CRONJOB_AND_JOB = "Cannot create trigger! No value for CronJob OR Job is given. Need only one value!";

	private static int triggerPerformWaitSeconds = 10;

	@BeforeClass
	public static void setUpBeforeClass()
	{
		final double timeFactor = Math.max(1.0, Config.getDouble("platform.test.timefactor", 1.0));
		triggerPerformWaitSeconds = (int) (10 * timeFactor);
	}

	@Test
	public void testTriggerCanCreateAJob() throws Exception
	{
		// Assign a two second job to a trigger
		final Job job = createTwoSecondJob("job1");
		final Trigger trigger = makeTrigger(Trigger.ACTIVE, Boolean.TRUE, Trigger.ACTIVATIONTIME, nowMinusTSeconds(1), Trigger.JOB,
				job);

		// Job should not yet have a cronJob
		assertTrue("Job should have no cronjob", trigger.getJob().getCronJobs().size() == 0);

		// The trigger should not reference a cronJob directly
		assertNull("CronJob should be null", trigger.getCronJob());

		// Activate the trigger. This starts performing the job asynchronously.
		trigger.activate();

		assertEquals("Should have one cronJob", 1, trigger.getJob().getCronJobs().size());
		final CronJob cronJob = trigger.getJob().getCronJobs().iterator().next();

		assertFinished(cronJob);
	}

	@Test
	public void testTriggerNeedsAJobOrACronJobToBeInstantiated()
	{
		try
		{
			// Try to instantiate a trigger without a job or cronJob
			final Trigger trigger = makeTrigger(Trigger.ACTIVE, Boolean.TRUE, Trigger.ACTIVATIONTIME, nowMinusTSeconds(1));
			// This should throw an exception
			fail("Should not be able to reach here with " + trigger);
		}
		catch (final Exception e)
		{
			// The exception should contain this string..
			assertTrue(
					"Excepted.. " + MISSING_CRONJOB_AND_JOB + " but got " + e.getMessage() + " : "
							+ Utilities.getStackTraceAsString(e), e.getMessage().contains(MISSING_CRONJOB_AND_JOB));
		}
	}

	@Test
	public void testCanAssignCronJobToTriggerWithAJob()
	{
		try
		{
			final Job job = createTwoSecondJob("job1");
			final CronJob cronJob = cronJobManager.createCronJob(job, null, true);
			// Create a trigger with job
			final Trigger trigger = makeTrigger(Trigger.ACTIVE, Boolean.TRUE, Trigger.ACTIVATIONTIME, nowMinusTSeconds(1),
					Trigger.JOB, job);
			// .. and try to assign cronJob to it too
			trigger.setCronJob(cronJob);
			fail();
		}
		catch (final Exception e)
		{
			// The exception should contain this string..
			assertTrue(
					"Expected " + CRONJOB_NOT_CHANGEABLE + " but got " + e.getMessage() + " : " + Utilities.getStackTraceAsString(e),
					e.getMessage().contains(CRONJOB_NOT_CHANGEABLE));
		}
	}

	@Test
	public void testCanAssignJobToTriggerWithACronJob()
	{
		try
		{
			final Job job = createTwoSecondJob("job1");
			final CronJob cronJob = cronJobManager.createCronJob(job, null, true);
			// Create a trigger with cronJob
			final Trigger trigger = makeTrigger(Trigger.ACTIVE, Boolean.TRUE, Trigger.ACTIVATIONTIME, nowMinusTSeconds(1),
					Trigger.CRONJOB, cronJob);
			// .. and try to assign job to it too
			trigger.setJob(job);
			fail();
		}
		catch (final Exception e)
		{
			// The exception should contain this string..
			assertTrue("Expected " + JOB_NOT_CHANGEABLE + " but got " + e.getMessage() + " : " + Utilities.getStackTraceAsString(e),
					e.getMessage().contains(JOB_NOT_CHANGEABLE));
		}
	}

	private Trigger makeTrigger(final Object... args) throws Exception
	{
		final Map<String, Object> triggerParams = new HashMap<String, Object>();
		for (int i = 0; i < args.length; i += 2)
		{
			triggerParams.put((String) args[i], args[i + 1]);
		}
		return (Trigger) typeManager.getComposedType(Trigger.class).newInstance(triggerParams);
	}

	private Job createTwoSecondJob(final String jobName) throws Exception
	{
		final Map<String, Object> jobParams = new HashMap<String, Object>();
		jobParams.put(Job.CODE, jobName + new Date());
		final ComposedType job_ct = prepareComposedTypeIfNeeded(jobName, jobParams);
		final Job newJobInstance = (Job) job_ct.newInstance(jobParams);
		Assert.assertTrue("Created job instance should be triggerable or service layer job at least ",//
				ClassUtils.isAssignable(UnPerformableJob.class, TriggerableJob.class) //
						|| //
						ClassUtils.isAssignable(UnPerformableJob.class, ServicelayerJob.class)//
		);
		return newJobInstance;
	}

	/**
	 *
	 */
	private ComposedType prepareComposedTypeIfNeeded(final String jobName, final Map<String, Object> jobParams)
			throws JaloDuplicateCodeException
	{
		ComposedType job_ct = null;
		try
		{
			job_ct = typeManager.getComposedType("TriggerableTwoSecondJob");
		}
		catch (final JaloItemNotFoundException e)
		{
			job_ct = typeManager.createComposedType(typeManager.getComposedType(ServicelayerJob.class), "TriggerableTwoSecondJob");
		}
		job_ct.setJaloClass(TriggerableTwoSecondJob.class);
		return job_ct;
	}

	private Date nowMinusTSeconds(final int tSeconds)
	{
		return new Date(System.currentTimeMillis() - tSeconds * 1000);
	}


	@Test
	public void testPLA8101Explicity() throws Exception
	{
		final Job job = prepareCustomJobTypes("jobWithParams1");

		final CronJob createCronJob = job.createCronjob();

		assertEquals(job.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_ONE_JOB_ID),
				createCronJob.getAttribute("attributeCronJobOne"));
		assertEquals(job.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_TWO_JOB_ID),
				createCronJob.getAttribute("attributeCronJobTwo"));
		assertEquals(job.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_THREE_JOB_ID),
				createCronJob.getAttribute("attributeCronJobThree"));

	}

	@Test
	public void testPLA8101Triggered() throws Exception
	{
		// Assign a two second job to a trigger
		final Job job = prepareCustomJobTypes("jobWithParams1");
		final Trigger trigger = makeTrigger(Trigger.ACTIVE, Boolean.TRUE, Trigger.ACTIVATIONTIME, nowMinusTSeconds(1), Trigger.JOB,
				job);

		// Job should not yet have a cronJob
		assertTrue("Job should have no cronjob", trigger.getJob().getCronJobs().size() == 0);

		// The trigger should not reference a cronJob directly
		assertNull("CronJob should be null", trigger.getCronJob());

		// Activate the trigger. This causes the job to instantiate a cronJob
		trigger.activate();

		assertEquals("Should have one cronJob", 1, trigger.getJob().getCronJobs().size());
		final CronJob createCronJob = trigger.getJob().getCronJobs().iterator().next();

		assertFinished(createCronJob);

		assertEquals(job.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_ONE_JOB_ID),
				createCronJob.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_CRON_JOB_ONE_ID));

		assertEquals(job.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_TWO_JOB_ID),
				createCronJob.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_CRON_JOB_TWO_ID));

		assertEquals(job.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_THREE_JOB_ID),
				createCronJob.getAttribute(CustomAttributesTriggerableJob.ATTRIBUTE_CRON_JOB_THREE_ID));
	}


	private Job prepareCustomJobTypes(final String jobName) throws Exception
	{
		final Map<String, Object> jobParams = new HashMap<String, Object>();
		jobParams.put(Job.CODE, jobName + "[" + new Date() + "]");
		jobParams.put(CustomAttributesTriggerableJob.ATTRIBUTE_ONE_JOB_ID, "attributteOneValue");
		jobParams.put(CustomAttributesTriggerableJob.ATTRIBUTE_TWO_JOB_ID, "attributteTwoValue");
		jobParams.put(CustomAttributesTriggerableJob.ATTRIBUTE_THREE_JOB_ID, "attributteThreeValue");

		final ComposedType typeCronJob = typeManager.createComposedType(typeManager.getComposedType(CronJob.class),
				CustomAttributesTriggerableJob.STATICCRONJOBCODE);
		typeCronJob.createAttributeDescriptor(CustomAttributesTriggerableJob.ATTRIBUTE_CRON_JOB_ONE_ID,
				typeManager.getRootAtomicType(String.class), AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);
		typeCronJob.createAttributeDescriptor(CustomAttributesTriggerableJob.ATTRIBUTE_CRON_JOB_TWO_ID,
				typeManager.getRootAtomicType(String.class), AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);
		typeCronJob.createAttributeDescriptor(CustomAttributesTriggerableJob.ATTRIBUTE_CRON_JOB_THREE_ID,
				typeManager.getRootAtomicType(String.class), AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);

		final ComposedType type = typeManager.createComposedType(typeManager.getComposedType(Job.class),
				CustomAttributesTriggerableJob.STATICCJOBCODE);
		type.setJaloClass(CustomAttributesTriggerableJob.class);
		type.createAttributeDescriptor(CustomAttributesTriggerableJob.ATTRIBUTE_ONE_JOB_ID,
				typeManager.getRootAtomicType(String.class), AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);
		type.createAttributeDescriptor(CustomAttributesTriggerableJob.ATTRIBUTE_TWO_JOB_ID,
				typeManager.getRootAtomicType(String.class), AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);
		type.createAttributeDescriptor(CustomAttributesTriggerableJob.ATTRIBUTE_THREE_JOB_ID,
				typeManager.getRootAtomicType(String.class), AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);
		return (Job) type.newInstance(jobParams);
	}

	/*
	 * PLA-7933 If the cronjob is active and the trigger is activate the cronjob/job should be performed If the cronjob
	 * is NOT active the trigger should not perform the cronjob/job
	 */
	@Test
	public void testPLA7933TriggerAndInactiveCronJobs() throws Exception
	{
		testTriggerAndDeactivatedCronJob(true);
		testTriggerAndDeactivatedCronJob(false);

	}

	private void testTriggerAndDeactivatedCronJob(final boolean activecronjob) throws Exception
	{
		final Job job = createTwoSecondJob("job1");
		final CronJob cronJob = cronJobManager.createCronJob(job, job.getCode() + "xxx", true);
		cronJob.setActive(activecronjob);

		final Trigger trigger = makeTrigger(Trigger.ACTIVE, Boolean.TRUE, Trigger.ACTIVATIONTIME, nowMinusTSeconds(1),
				Trigger.CRONJOB, cronJob);

		assertNull(cronJob.getStartTime());

		trigger.activate();

		if (activecronjob)
		{
			assertFinished(cronJob);
			assertNotNull("cronjob(active=true) was not performed!", cronJob.getStartTime());
		}
		else
		{
			waitToFinish(cronJob); // we know it won't run but we must wait for sure
			assertNull("cronjob(active=" + activecronjob + ") was performed!", cronJob.getStartTime());
		}
	}

	/**
	 * PLA-7333 ,null attributes values
	 */
	@Test
	public void testTriggerForNullAttributtes() throws Exception
	{
		final Job job = createTwoSecondJob("job1");
		final CronJob cronJob = cronJobManager.createCronJob(job, job.getCode() + "xxx", true);
		cronJob.setActive(true);

		final Trigger trigger = makeTrigger(Trigger.ACTIVE, Boolean.TRUE, Trigger.ACTIVATIONTIME, nowMinusTSeconds(1),
				Trigger.CRONJOB, cronJob, Trigger.ACTIVATIONTIME, null, Trigger.ACTIVE, null, Trigger.DATERANGE, null, Trigger.DAY,
				null, Trigger.DAYSOFWEEK, null, Trigger.HOUR, null, /* Trigger.MAXACCEPTABLEDELAY, null */
				Trigger.MINUTE, null, Trigger.MONTH, null, Trigger.RELATIVE, null, Trigger.SECOND, null, /*
																																	   * Trigger.TIMETABLE
																																	   * , null,
																																	   */
				Trigger.WEEKINTERVAL, null, Trigger.YEAR, null);
		try
		{
			trigger.toString();
		}
		catch (final Exception e)
		{
			fail("Unable to perform toString() on trigger [" + e.getMessage() + "] " + trigger.getPK() + " : "
					+ Utilities.getStackTraceAsString(e));
		}

	}

	private void waitToFinish(final CronJob cronJob)
	{
		final Thread thread = Thread.currentThread();
		final long maxWaitTime = System.currentTimeMillis() + (triggerPerformWaitSeconds * 1000);
		while (!cronJob.isFinished() && !thread.isInterrupted() && System.currentTimeMillis() < maxWaitTime)
		{
			try
			{
				Thread.sleep(500);
			}
			catch (final InterruptedException e)
			{
				thread.interrupt();
			}
		}
	}

	private void assertFinished(final CronJob cronJob)
	{
		waitToFinish(cronJob);
		assertTrue(cronJob.isFinished());
	}

}
