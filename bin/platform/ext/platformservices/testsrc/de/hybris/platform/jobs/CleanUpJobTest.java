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
package de.hybris.platform.jobs;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.constants.CronJobConstants;
import de.hybris.platform.cronjob.jalo.CleanUpCronJob;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.cronjob.jalo.Trigger;
import de.hybris.platform.cronjob.jalo.UnPerformableJob;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CleanUpJobTest extends ServicelayerTransactionalTest
{
	private final EnumerationManager enumman = EnumerationManager.getInstance();
	private final CronJobManager cronjobman = CronJobManager.getInstance();

	@Resource
	private I18NService i18nService;

	private ServicelayerJob cleanupjob = null;
	private ServicelayerJob dummyjob = null;

	private EnumerationValue status_finished = null;
	private EnumerationValue status_aborted = null;
	private EnumerationValue status_paused = null;
	private EnumerationValue status_running = null;
	private EnumerationValue status_runningrestart = null;
	private EnumerationValue status_unknown = null;
	private EnumerationValue result_error = null;
	private EnumerationValue result_failure = null;
	private EnumerationValue result_success = null;
	private EnumerationValue result_unknown = null;

	//verschiedene random cronjobs mit refs zu jobs anlegen
	//dabei START/END zeit setzen!!!!!

	//filter testen
	//status/result/exclude

	@Before
	public void setUp() throws Exception
	{
		final Map<String, Object> jobParams = new HashMap<String, Object>();
		jobParams.put(ServicelayerJob.CODE, "cleanUpJobPerformable");
		jobParams.put(ServicelayerJob.SPRINGID, "cleanUpJobPerformable");
		cleanupjob = CronJobManager.getInstance().createServicelayerJob(jobParams);

		jobParams.clear();
		jobParams.put(ServicelayerJob.CODE, "ysimpleJobPerformable");
		jobParams.put(ServicelayerJob.SPRINGID, "ysimpleJobPerformable");
		dummyjob = CronJobManager.getInstance().createServicelayerJob(jobParams);


		status_finished = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBSTATUS,
				CronJobConstants.Enumerations.CronJobStatus.FINISHED);
		status_aborted = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBSTATUS,
				CronJobConstants.Enumerations.CronJobStatus.ABORTED);
		status_paused = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBSTATUS,
				CronJobConstants.Enumerations.CronJobStatus.PAUSED);
		status_running = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBSTATUS,
				CronJobConstants.Enumerations.CronJobStatus.RUNNING);
		status_runningrestart = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBSTATUS,
				CronJobConstants.Enumerations.CronJobStatus.RUNNINGRESTART);
		status_unknown = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBSTATUS,
				CronJobConstants.Enumerations.CronJobStatus.UNKNOWN);

		result_error = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBRESULT,
				CronJobConstants.Enumerations.CronJobResult.ERROR);
		result_failure = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBRESULT,
				CronJobConstants.Enumerations.CronJobResult.FAILURE);
		result_success = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBRESULT,
				CronJobConstants.Enumerations.CronJobResult.SUCCESS);
		result_unknown = enumman.getEnumerationValue(CronJobConstants.TC.CRONJOBRESULT,
				CronJobConstants.Enumerations.CronJobResult.UNKNOWN);
	}


	@Test
	public void testSetUp()
	{
		assertNotNull(cleanupjob);
		assertEquals("cleanUpJobPerformable", cleanupjob.getCode());
		assertNotNull(dummyjob);
		assertEquals("ysimpleJobPerformable", dummyjob.getCode());

		assertNotNull(status_finished);
		assertNotNull(status_aborted);
		assertNotNull(status_paused);
		assertNotNull(status_running);
		assertNotNull(status_runningrestart);
		assertNotNull(status_unknown);
		assertNotNull(result_error);
		assertNotNull(result_failure);
		assertNotNull(result_success);
		assertNotNull(result_unknown);


	}

	@Test
	public void testCreationCleanUpCronJob()
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(CronJob.JOB, cleanupjob);
		final CleanUpCronJob cucj = cronjobman.createCleanUpCronJob(params);

		assertTrue(cucj.getResultcoll().contains(result_success));
		assertEquals(1, cucj.getResultcoll().size());
		assertTrue(cucj.getStatuscoll().contains(status_finished));
		assertEquals(1, cucj.getStatuscoll().size());
		assertEquals(14, cucj.getXDaysOldAsPrimitive());
		assertEquals(0, cucj.getExcludeCronJobs().size());
	}

	@Test
	public void testPLA8666() throws Exception //NOPMD
	{
		final ComposedType jobType = TypeManager.getInstance().createComposedType(
				TypeManager.getInstance().getComposedType(Job.class), "UnperformableJobType");
		jobType.setJaloClass(UnPerformableJob.class);

		final UnPerformableJob unperformable = (UnPerformableJob) jobType.newInstance(Collections.singletonMap(Job.CODE,
				"MyUnperformableJob"));

		final Map props = new HashMap();
		props.put(Trigger.JOB, unperformable);
		props.put(Trigger.ACTIVE, Boolean.TRUE);
		CronJobManager.getInstance().createTrigger(props);

		final Calendar cal1 = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal1.add(Calendar.DATE, -1 * 15);

		final CronJob delete1 = createDummyCronJob(cal1.getTime(), result_success, status_finished);
		assertNotNull(delete1);

		cleanupjob.perform(createCleanUpCronJob(), true);

		assertFalse(delete1.isAlive());
	}

	@Test
	public void testGeneralDelete()
	{
		final Calendar cal1 = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal1.add(Calendar.DATE, -1 * 15);

		final Calendar cal2 = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal2.add(Calendar.DATE, -1 * 11);

		final CronJob delete1 = createDummyCronJob(cal1.getTime(), result_success, status_finished);
		assertNotNull(delete1);

		final CronJob delete2 = createDummyCronJob(cal2.getTime(), result_success, status_finished);
		assertNotNull(delete2);

		cleanupjob.perform(createCleanUpCronJob(), true);

		assertFalse(delete1.isAlive());
		assertTrue(delete2.isAlive());

	}

	@Test
	public void testStatusFilter()
	{
		final Calendar cal = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal.add(Calendar.DATE, -1 * 55);

		final CronJob delete1 = createDummyCronJob(cal.getTime(), result_success, status_aborted);
		final CronJob delete2 = createDummyCronJob(cal.getTime(), result_success, status_finished);
		final CronJob delete3 = createDummyCronJob(cal.getTime(), result_success, status_paused);
		final CronJob delete4 = createDummyCronJob(cal.getTime(), result_success, status_running);

		final CleanUpCronJob cucj = createCleanUpCronJob();
		final Collection<EnumerationValue> statuscoll = new ArrayList<EnumerationValue>();
		statuscoll.add(status_aborted);
		statuscoll.add(status_paused);
		statuscoll.add(status_finished);
		cucj.setStatuscoll(statuscoll);
		cleanupjob.perform(cucj, true);

		assertFalse(delete1.isAlive());
		assertFalse(delete2.isAlive());
		assertFalse(delete3.isAlive());
		assertTrue(delete4.isAlive());
	}

	@Test
	public void testResultFilter()
	{
		final Calendar cal = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal.add(Calendar.DATE, -1 * 55);

		final CronJob delete1 = createDummyCronJob(cal.getTime(), result_error, status_finished);
		final CronJob delete2 = createDummyCronJob(cal.getTime(), result_success, status_finished);
		final CronJob delete3 = createDummyCronJob(cal.getTime(), result_failure, status_finished);
		final CronJob delete4 = createDummyCronJob(cal.getTime(), result_unknown, status_finished);

		final CleanUpCronJob cucj = createCleanUpCronJob();
		final Collection<EnumerationValue> resultcoll = new ArrayList<EnumerationValue>();
		resultcoll.add(result_error);
		resultcoll.add(result_failure);
		resultcoll.add(result_success);
		cucj.setResultcoll(resultcoll);
		cleanupjob.perform(cucj, true);

		assertFalse(delete1.isAlive());
		assertFalse(delete2.isAlive());
		assertFalse(delete3.isAlive());
		assertTrue(delete4.isAlive());
	}

	@Test
	public void testExcludeCronJobList()
	{
		final Calendar cal = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal.add(Calendar.DATE, -1 * 55);

		final CronJob delete1 = createDummyCronJob(cal.getTime(), result_success, status_finished);
		final CronJob delete2 = createDummyCronJob(cal.getTime(), result_success, status_finished);

		final CleanUpCronJob cucj = createCleanUpCronJob();
		cucj.setExcludeCronJobs(Collections.singletonList(delete1));
		cleanupjob.perform(cucj, true);

		assertTrue(delete1.isAlive());
		assertFalse(delete2.isAlive());
	}

	private CronJob createDummyCronJob(final Date endtime, final EnumerationValue result, final EnumerationValue status)
	{
		final Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put(CronJob.JOB, dummyjob);
		params1.put(CronJob.ENDTIME, endtime);
		params1.put(CronJob.RESULT, result);
		params1.put(CronJob.STATUS, status);
		return cronjobman.createCronJob(params1);
	}

	private CleanUpCronJob createCleanUpCronJob()
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(CronJob.JOB, cleanupjob);
		return cronjobman.createCleanUpCronJob(params);
	}

}
