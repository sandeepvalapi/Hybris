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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.TriggerService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AfterCronJobCrashAbortEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;
import de.hybris.platform.servicelayer.event.events.BeforeCronJobStartEvent;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationListener;

/**
 *
 */
@IntegrationTest
public class CronJobEventTest extends ServicelayerBaseTest
{
	@Resource
	EventService eventService;
	
	@Resource
	CronJobService cronJobService;
	
	@Resource
	TriggerService triggerService;
	
	@Resource
	TaskService taskService;
	
	@Resource
	ModelService modelService;

	TestStartListener startListener;
	TestEndListener endListener;
	TestCrashListener crashListener;
	
	@Before
	public void setupListeners()
	{
		startListener = new TestStartListener();
		endListener = new TestEndListener();
		crashListener = new TestCrashListener();
		
		eventService.registerEventListener(startListener);
		eventService.registerEventListener(endListener);
		eventService.registerEventListener(crashListener);
	}

	@After
	public void unregisterListeners()
	{
		eventService.unregisterEventListener(startListener);
		eventService.unregisterEventListener(endListener);
		eventService.unregisterEventListener(crashListener);
	}

	@Test
	public void testAfterCrashAbortEvent()
	{
		// given
		final ScriptModel script = createSuccessScript();
		final CronJobModel cronJob = createCronJob(script);
		final ScriptingJobModel job = (ScriptingJobModel) cronJob.getJob();
		
		cronJob.setRunningOnClusterNode(Integer.valueOf(99));
		cronJob.setStatus(CronJobStatus.RUNNING);
		modelService.save(cronJob);

		// when
		CronJobManager.getInstance().abortRunningCronJobsForClusterNode(99);
		
		// then
		assertEquals(CronJobStatus.ABORTED, cronJob.getStatus());

		assertEquals(1, crashListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<AfterCronJobCrashAbortEvent> crash = crashListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(crash.isPresent());
		
		assertCronJobMatchesCrashEvent(cronJob, job, crash.get());
	}

	
	@Test
	public void testEventsOnSuccessfulCronJob()
	{
		// given
		final ScriptModel script = createSuccessScript();
		final CronJobModel cronJob = createCronJob(script);
		final ScriptingJobModel job = (ScriptingJobModel) cronJob.getJob();

		// when
		cronJobService.performCronJob(cronJob, true);
		modelService.refresh(cronJob);
		
		// then
		assertEquals(CronJobResult.SUCCESS, cronJob.getResult());
		assertEquals(CronJobStatus.FINISHED, cronJob.getStatus());

		assertEquals(1, startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<BeforeCronJobStartEvent> start = startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(start.isPresent());
		
		assertCronJobMatchesStartEvent(cronJob, job, start.get(), true, false, null);

		assertEquals(1, endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<AfterCronJobFinishedEvent> end = endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(end.isPresent());
		
		assertEndEventMatchesCronJob(cronJob, job, end.get(), true, false, null);
	}

	@Test
	public void testEventsOnSuccessfulCronJobAsync()
	{
		// given
		final ScriptModel script = createSuccessScript();
		final CronJobModel cronJob = createCronJob(script);
		final ScriptingJobModel job = (ScriptingJobModel) cronJob.getJob();

		// when
		cronJobService.performCronJob(cronJob, false);
		modelService.refresh(cronJob);
		assertTrue( "cronjob "+cronJob.getCode()+" didn't finish in time", waitForCronJobFinished(cronJob, 30));
		assertTrue( "end event did not arrive in time", waitForEndEventReceived(cronJob, 10));
		
		// then
		assertEquals(CronJobResult.SUCCESS, cronJob.getResult());
		assertEquals(CronJobStatus.FINISHED, cronJob.getStatus());

		assertEquals(1, startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<BeforeCronJobStartEvent> start = startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(start.isPresent());
		
		assertCronJobMatchesStartEvent(cronJob, job, start.get(), false, false, null);

		assertEquals(1, endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<AfterCronJobFinishedEvent> end = endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(end.isPresent());
		
		assertEndEventMatchesCronJob(cronJob, job, end.get(), false, false, null);
	}

	
	@Test
	public void testEventsOnSuccessfulCronJobViaTrigger()
	{
		boolean runningBefore = taskService.getEngine().isRunning();
		try
		{
			if( runningBefore )
			{
				taskService.getEngine().stop();
			}
		
   		// given
   		final ScriptModel script = createSuccessScript();
   		final CronJobModel cronJob = createCronJob(script);
   		final ScriptingJobModel job = (ScriptingJobModel) cronJob.getJob();
   		
   		final TriggerModel trigger = modelService.create(TriggerModel.class);
   		trigger.setCronJob(cronJob);
   		trigger.setActivationTime(new Date());
   		trigger.setActive(Boolean.TRUE);
   		modelService.save(trigger);
   
   		// when
   		triggerService.activate(trigger);
   		modelService.refresh(cronJob);
   		assertTrue( "cronjob "+cronJob.getCode()+" didn't finish in time", waitForCronJobFinished(cronJob, 30));
   		assertTrue( "end event did not arrive in time", waitForEndEventReceived(cronJob, 10));
   		
   		// then
   		assertEquals(CronJobResult.SUCCESS, cronJob.getResult());
   		assertEquals(CronJobStatus.FINISHED, cronJob.getStatus());
   
   		assertEquals(1, startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
   		Optional<BeforeCronJobStartEvent> start = startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
   		assertTrue(start.isPresent());
   		
   		assertCronJobMatchesStartEvent(cronJob, job, start.get(), false, true, trigger.getPk());
   
   		assertEquals(1, endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
   		Optional<AfterCronJobFinishedEvent> end = endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
   		assertTrue(end.isPresent());
   		
   		assertEndEventMatchesCronJob(cronJob, job, end.get(), false, true, trigger.getPk());
		}
		finally
		{
			if( runningBefore )
			{
				taskService.getEngine().start();
			}
		}
	}
	
	@Test
	public void testEventsOnRegularFailingCronJob()
	{
		// given
		final ScriptModel script = createRegularFailScript();
		final CronJobModel cronJob = createCronJob(script);
		final ScriptingJobModel job = (ScriptingJobModel) cronJob.getJob();

		// when
		cronJobService.performCronJob(cronJob, true);
		modelService.refresh(cronJob);
		
		// then
		assertEquals(CronJobResult.FAILURE, cronJob.getResult());
		assertEquals(CronJobStatus.FINISHED, cronJob.getStatus());

		assertEquals(1, startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<BeforeCronJobStartEvent> start = startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(start.isPresent());
		assertCronJobMatchesStartEvent(cronJob, job, start.get(), true, false, null);

		assertEquals(1, endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<AfterCronJobFinishedEvent> end = endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(end.isPresent());
		
		assertEndEventMatchesCronJob(cronJob, job, end.get(), true, false, null);
	}

	@Test
	public void testEventsOnRegularFailingCronJobAsync()
	{
		// given
		final ScriptModel script = createRegularFailScript();
		final CronJobModel cronJob = createCronJob(script);
		final ScriptingJobModel job = (ScriptingJobModel) cronJob.getJob();

		// when
		cronJobService.performCronJob(cronJob, false);
		modelService.refresh(cronJob);
		assertTrue( "cronjob "+cronJob.getCode()+" didn't finish in time", waitForCronJobFinished(cronJob, 30));
		assertTrue( "end event did not arrive in time", waitForEndEventReceived(cronJob, 10));

		// then
		assertEquals(CronJobResult.FAILURE, cronJob.getResult());
		assertEquals(CronJobStatus.FINISHED, cronJob.getStatus());

		assertEquals(1, startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<BeforeCronJobStartEvent> start = startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(start.isPresent());
		assertCronJobMatchesStartEvent(cronJob, job, start.get(), false, false, null);

		assertEquals(1, endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
		Optional<AfterCronJobFinishedEvent> end = endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
		assertTrue(end.isPresent());
		
		assertEndEventMatchesCronJob(cronJob, job, end.get(), false, false, null);
	}

	@Test
	public void testEventsRegularFailingCronJobViaTrigger()
	{
		boolean runningBefore = taskService.getEngine().isRunning();
		try
		{
			if( runningBefore )
			{
				taskService.getEngine().stop();
			}

   		// given
   		final ScriptModel script = createRegularFailScript();
   		final CronJobModel cronJob = createCronJob(script);
   		final ScriptingJobModel job = (ScriptingJobModel) cronJob.getJob();
   
   		final TriggerModel trigger = modelService.create(TriggerModel.class);
   		trigger.setCronJob(cronJob);
   		trigger.setActivationTime(new Date());
   		trigger.setActive(Boolean.TRUE);
   		modelService.save(trigger);
   
   		// when
   		triggerService.activate(trigger);
   		modelService.refresh(cronJob);
   		assertTrue( "cronjob "+cronJob.getCode()+" didn't finish in time", waitForCronJobFinished(cronJob, 30));
   		assertTrue( "end event did not arrive in time", waitForEndEventReceived(cronJob, 10));
   		
   		// then
   		assertEquals(CronJobResult.FAILURE, cronJob.getResult());
   		assertEquals(CronJobStatus.FINISHED, cronJob.getStatus());
   
   		assertEquals(1, startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
   		Optional<BeforeCronJobStartEvent> start = startListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
   		assertTrue(start.isPresent());
   		assertCronJobMatchesStartEvent(cronJob, job, start.get(), false, true, trigger.getPk());
   
   		assertEquals(1, endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).count());
   		Optional<AfterCronJobFinishedEvent> end = endListener.events.stream().filter(evt -> evt.getCronJob().equals(cronJob.getCode())).findAny();
   		assertTrue(end.isPresent());
   		
   		assertEndEventMatchesCronJob(cronJob, job, end.get(), false, true, trigger.getPk());
		}
		finally
		{
			if( runningBefore )
			{
				taskService.getEngine().start();
			}
		}

	}



	boolean waitForEndEventReceived(CronJobModel cronJob, int timeoutSeconds)
	{
		final long waitUntilMaxTime = System.currentTimeMillis() + (timeoutSeconds * 1000);
		while( endListener.events.stream().noneMatch(evt -> evt.getCronJob().equals(cronJob.getCode())) && System.currentTimeMillis() <= waitUntilMaxTime )
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				Thread.interrupted();
				return false;
			}
		}
		return endListener.events.stream().anyMatch(evt -> evt.getCronJob().equals(cronJob.getCode()));
	}

	boolean waitForCronJobFinished( CronJobModel cronJob, int timeoutSeconds)
	{
		final long waitUntilMaxTime = System.currentTimeMillis() + (timeoutSeconds * 1000);
		
		while( !cronJobService.isFinished(cronJob) && System.currentTimeMillis() <= waitUntilMaxTime )
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				Thread.interrupted();
				return false;
			}
			modelService.refresh(cronJob);
		}
		return cronJobService.isFinished(cronJob); 
	}
	
	void assertCronJobMatchesCrashEvent(final CronJobModel cronJob, final ScriptingJobModel job,
			AfterCronJobCrashAbortEvent crash )
	{
		assertEquals(cronJob.getPk(), crash.getCronJobPK());
		assertEquals(cronJob.getItemtype(), crash.getCronJobType());
		assertEquals(job.getCode(), crash.getJob());
		assertEquals(job.getItemtype(), crash.getJobType());
	}
	
	void assertCronJobMatchesStartEvent(final CronJobModel cronJob, final ScriptingJobModel job,
			BeforeCronJobStartEvent start, boolean synchronous, boolean scheduled, PK triggerPK)
	{
		assertEquals(cronJob.getPk(), start.getCronJobPK());
		assertEquals(cronJob.getItemtype(), start.getCronJobType());
		assertEquals(job.getCode(), start.getJob());
		assertEquals(job.getItemtype(), start.getJobType());
		assertEquals(synchronous, start.isSynchronous());
		assertEquals(scheduled, start.isScheduled());
		assertEquals(triggerPK, start.getScheduledByTriggerPk());
	}

	void assertEndEventMatchesCronJob(final CronJobModel cronJob, final ScriptingJobModel job,
			AfterCronJobFinishedEvent end, boolean synchronous, boolean scheduled, PK triggerPK)
	{
		assertEquals(cronJob.getResult(), end.getResult());
		assertEquals(cronJob.getStatus(), end.getStatus());
		assertEquals(cronJob.getPk(), end.getCronJobPK());
		assertEquals(cronJob.getItemtype(), end.getCronJobType());
		assertEquals(job.getCode(), end.getJob());
		assertEquals(job.getItemtype(), end.getJobType());
		assertEquals(synchronous, end.isSynchronous());
		assertEquals(scheduled, end.isScheduled());
		assertEquals(triggerPK, end.getScheduledByTriggerPk());
	}

	static class TestCrashListener implements ApplicationListener<AfterCronJobCrashAbortEvent>
	{
		final List<AfterCronJobCrashAbortEvent> events = new ArrayList<>();
		
		@Override
		public void onApplicationEvent(AfterCronJobCrashAbortEvent arg0)
		{
			events.add(arg0);
		}
	}

	static class TestStartListener implements ApplicationListener<BeforeCronJobStartEvent>
	{
		final List<BeforeCronJobStartEvent> events = new ArrayList<>();
		
		@Override
		public void onApplicationEvent(BeforeCronJobStartEvent arg0)
		{
			events.add(arg0);
		}
	}
	
	static class TestEndListener implements ApplicationListener<AfterCronJobFinishedEvent>
	{
		final List<AfterCronJobFinishedEvent> events = new ArrayList<>();

		@Override
		public void onApplicationEvent(AfterCronJobFinishedEvent arg0)
		{
			events.add(arg0);
		}
	}

	CronJobModel createCronJob( ScriptModel script)
	{
		final ScriptingJobModel job = modelService.create(ScriptingJobModel.class);
		job.setCode("TestJob");
		job.setScriptURI("model://"+script.getCode());
		
		final CronJobModel cronJob = modelService.create(CronJobModel.class);
		cronJob.setCode("TestCronJob");
		cronJob.setJob(job);
		modelService.saveAll(job,cronJob);
		
		return cronJob;
	}
	
	ScriptModel createSuccessScript()
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setActive(Boolean.TRUE);
		script.setCode("script1");
		script.setScriptType(ScriptType.GROOVY);
		script.setContent(
			"import de.hybris.platform.servicelayer.cronjob.PerformResult \n"+
			"import de.hybris.platform.cronjob.enums.CronJobResult \n"+
			"import de.hybris.platform.cronjob.enums.CronJobStatus \n"+
			"\n"+
			"println 'successful test job';\n"+
			"return new PerformResult(CronJobResult.SUCCESS,CronJobStatus.FINISHED)"
		);
		modelService.save(script);
		
		return script;
	}

	ScriptModel createRegularFailScript()
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setActive(Boolean.TRUE);
		script.setCode("script1");
		script.setScriptType(ScriptType.GROOVY);
		script.setContent(
			"import de.hybris.platform.servicelayer.cronjob.PerformResult \n"+
			"import de.hybris.platform.cronjob.enums.CronJobResult \n"+
			"import de.hybris.platform.cronjob.enums.CronJobStatus \n"+
			"\n"+
			"println 'failing test job';\n"+
			"return new PerformResult(CronJobResult.FAILURE,CronJobStatus.FINISHED)"
		);
		modelService.save(script);

		return script;
	}

	ScriptModel createExceptionFailScript()
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setActive(Boolean.TRUE);
		script.setCode("script1");
		script.setScriptType(ScriptType.GROOVY);
		script.setContent(
			"import de.hybris.platform.servicelayer.cronjob.PerformResult \n"+
			"import de.hybris.platform.cronjob.enums.CronJobResult \n"+
			"import de.hybris.platform.cronjob.enums.CronJobStatus \n"+
			"\n"+
			"println 'failing via exception test job';\n"+
			"throw new RuntimeException('fail this cron job!!!')"
		);
		modelService.save(script);

		return script;
	}

}
