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
package de.hybris.platform.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.task.impl.DefaultScheduleAndTriggerStrategy;
import de.hybris.platform.task.impl.DefaultTaskService;
import de.hybris.platform.task.impl.ScheduleAndTriggerStrategy;
import de.hybris.platform.task.impl.TaskDAO;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * Tests task scheduling concurrency issues.
 */
@IntegrationTest
public class TaskSchedulingConcurrencyTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Test
	public void testSchedulingVsTriggerEvent()
	{
		testScheduleVsConcurrentTriggerEvent();
	}

	@Test
	public void testSchedulingVsTriggerEventTxRunning() throws Exception
	{
		final Transaction tx = Transaction.current();
		tx.execute(new TransactionBody()
		{

			@Override
			public Object execute() throws Exception
			{
				testScheduleVsConcurrentTriggerEvent();
				assertFalse(tx.isRollbackOnly());
				return null;
			}
		});
	}

	@Test
	public void testTriggerEventVsScheduling()
	{
		testTriggerEventVsScheduleTask();
	}

	@Test
	public void testTriggerEventVsSchedulingTxRunning() throws Exception
	{
		final Transaction tx = Transaction.current();
		tx.execute(new TransactionBody()
		{

			@Override
			public Object execute() throws Exception
			{
				testTriggerEventVsScheduleTask();
				assertFalse(tx.isRollbackOnly());
				return null;
			}
		});
	}

	private void testScheduleVsConcurrentTriggerEvent()
	{
		final String conditionUID = "Foo" + System.currentTimeMillis() + "_" + System.nanoTime();
		final TaskModel task = createDummyTaskWithCondition(conditionUID, new Date(12345));
		final TriggerEventWhileSchdulingTaskService taskService = new TriggerEventWhileSchdulingTaskService(modelService,
				flexibleSearchService);

		taskService.scheduleTask(task);

		assertTrue(taskService.retryCalled);
		assertNull(taskService.otherThreadError);
		assertTrue(modelService.isUpToDate(task));
		assertEquals(new Date(12345), task.getExecutionDate());
		final Set<TaskConditionModel> conditionsAfterSchedule = task.getConditions();
		assertNotNull(conditionsAfterSchedule);
		assertEquals(1, conditionsAfterSchedule.size());
		final TaskConditionModel condAfterSchedule = conditionsAfterSchedule.iterator().next();
		assertTrue(modelService.isUpToDate(condAfterSchedule));
		assertEquals(task, condAfterSchedule.getTask());
		assertEquals(conditionUID, condAfterSchedule.getUniqueID());
		assertEquals(Boolean.TRUE, condAfterSchedule.getFulfilled());

		final Task tItem = modelService.getSource(task);
		final TaskCondition cItem = modelService.getSource(condAfterSchedule);
		assertEquals(new Date(12345), tItem.getExecutionDate());
		assertTrue(tItem.isAlive());
		assertTrue(cItem.isAlive());
	}

	private TaskModel createDummyTaskWithCondition(final String conditionUID, final Date executionDate)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setRunnerBean("SomeBean");
		task.setExecutionDate(executionDate);
		final TaskConditionModel cond = modelService.create(TaskConditionModel.class);
		cond.setUniqueID(conditionUID);
		task.setConditions(Collections.singleton(cond));

		return task;
	}

	// Enforces this scenario:
	// caller -> schedule( task->cond )
	//              +-> insert task
	//              +-> try to match condition -> fails
	//   new thread---> insert condition via triggerEvent() -> commits successful
	// caller       +-> try to insert condition -> fails too
	//					 +-> try to match condition again -> now success
	//              +-> finalize task
	//
	private static class TriggerEventWhileSchdulingTaskService extends DefaultTaskService
	{
		boolean retryCalled = false;
		Exception otherThreadError = null;

		final FlexibleSearchService flexibleSearchService;

		TriggerEventWhileSchdulingTaskService(final ModelService ms, final FlexibleSearchService fs)
		{
			setModelService(ms);

			ScheduleAndTriggerStrategy strategy = null;

			setScheduleAndTriggerStrategy(strategy = new DefaultScheduleAndTriggerStrategy()
			{
				{
					setModelService(ms);
				}

				@Override
				protected void retryMatchConditionForSchedule(final TaskConditionModel cond, final PK taskPK,
						final ModelSavingException creationExcpetion)
				{
					super.retryMatchConditionForSchedule(cond, taskPK, creationExcpetion);
					retryCalled = true;
				}

				@Override
				protected void insertScheduleConditionOrRetryMatch(final TaskModel task, final PK taskPK,
						final TaskConditionModel cond)
				{
					triggerEventFromOtherThread(cond.getUniqueID());

					super.insertScheduleConditionOrRetryMatch(task, taskPK, cond);
				}

				private void triggerEventFromOtherThread(final String conditionUID)
				{
					final Tenant tenant = Registry.getCurrentTenantNoFallback();
					final AtomicReference<Exception> otherThreadError = new AtomicReference<Exception>();
					final Thread t = new RegistrableThread()
					{
						@Override
						public void internalRun()
						{
							Registry.setCurrentTenant(tenant);
							try
							{
								triggerEvent(conditionUID);

								final SearchResult<TaskConditionModel> sr = flexibleSearchService.search(//
										"SELECT {PK} " + //
												"FROM {" + TaskConditionModel._TYPECODE + "} " + //
												"WHERE {" + TaskConditionModel.UNIQUEID + "}=?uid", //
										Collections.singletonMap("uid", conditionUID) //
										);
								assertEquals(1, sr.getCount());
								final TaskConditionModel cond = sr.getResult().get(0);
								assertNull(cond.getTask());
								assertEquals(conditionUID, cond.getUniqueID());
								assertEquals(Boolean.TRUE, cond.getFulfilled());
							}
							catch (final Exception e)
							{
								otherThreadError.set(e);
							}
						}
					};
					t.start();
					try
					{
						t.join(60 * 1000);
					}
					catch (final InterruptedException e)
					{
						Thread.currentThread().interrupt();
					}

					TriggerEventWhileSchdulingTaskService.this.otherThreadError = otherThreadError.get();
				}

			});

			final TaskDAO dao = new TaskDAO(Registry.getCurrentTenantNoFallback());

			setTaskDao(dao);
			strategy.setTaskDao(dao);

			this.flexibleSearchService = fs;
		}

		@Override
		protected void checkTask(final TaskModel toBeScheduled)
		{
			// we dont need that
		}

		@Override
		public void triggerRepoll(final Integer nodeId, final String nodeGroupId)
		{
			// we dont need that
		}
	}

	private void testTriggerEventVsScheduleTask()
	{
		final String conditionUID = "Bar" + System.currentTimeMillis() + "_" + System.nanoTime();

		final ScheduleTaskWhileTriggerEventService taskService = new ScheduleTaskWhileTriggerEventService(modelService,
				flexibleSearchService);

		taskService.triggerEvent(conditionUID);

		assertTrue(taskService.retryCalled);
		assertNull(taskService.otherThreadError);
		assertNotNull(taskService.createdTask);
		final TaskModel task = modelService.get(taskService.createdTask);
		final Set<TaskConditionModel> conditions = task.getConditions();
		assertNotNull(conditions);
		assertEquals(1, conditions.size());
		final TaskConditionModel cond = conditions.iterator().next();
		assertEquals(conditionUID, cond.getUniqueID());
		assertEquals(Boolean.TRUE, cond.getFulfilled());
		assertEquals(task, cond.getTask());

		final Task tItem = modelService.getSource(task);
		final TaskCondition cItem = modelService.getSource(cond);
		assertEquals(new Date(5555), tItem.getExecutionDate());
		assertTrue(tItem.isAlive());
		assertTrue(cItem.isAlive());
	}

	// Enforces this scenario:
	// caller -> triggerEvent( uid )
	//              +-> try to match condition -> fails
	//   new thread---> schedule( task->cond(uid) )
	//              +-> insert task
	//              +-> try to match condition -> fails
	//              +-> insert condition -> success !
	//              +-> finalize task
	// caller       +-> try to insert condition -> fails too
	//					 +-> try to match condition again -> now success
	//
	private class ScheduleTaskWhileTriggerEventService extends DefaultTaskService
	{
		boolean retryCalled = false;
		Exception otherThreadError = null;
		PK createdTask;

		final FlexibleSearchService flexibleSearchService;

		ScheduleAndTriggerStrategy strategy = null;

		ScheduleTaskWhileTriggerEventService(final ModelService ms, final FlexibleSearchService fs)
		{
			setModelService(ms);
			setScheduleAndTriggerStrategy(strategy = new DefaultScheduleAndTriggerStrategy()
			{
				{
					setModelService(ms);
				}

				@Override
				protected boolean retryMatchConditionForEvent(final TaskEvent event)
				{
					final boolean result = super.retryMatchConditionForEvent(event);
					retryCalled = true;
					return result;
				}

				@Override
				protected boolean insertEventConditionOrRetryMatch(final TaskEvent event)
				{
					scheduleTaskFromOtherThread(event.getId());
					return super.insertEventConditionOrRetryMatch(event);
				}

				private void scheduleTaskFromOtherThread(final String conditionUID)
				{
					final Tenant tenant = Registry.getCurrentTenantNoFallback();
					final AtomicReference<Exception> otherThreadError = new AtomicReference<Exception>();
					final AtomicReference<PK> otherThreadTask = new AtomicReference<PK>();
					final Thread t = new RegistrableThread()
					{
						@Override
						public void internalRun()
						{
							Registry.setCurrentTenant(tenant);
							try
							{
								final TaskModel dummyTask = createDummyTaskWithCondition(conditionUID, new Date(5555));

								scheduleTask(dummyTask);

								final SearchResult<TaskConditionModel> sr = flexibleSearchService.search(//
										"SELECT {PK} " + //
												"FROM {" + TaskConditionModel._TYPECODE + "} " + //
												"WHERE {" + TaskConditionModel.UNIQUEID + "}=?uid", //
										Collections.singletonMap("uid", conditionUID) //
										);
								assertEquals(1, sr.getCount());
								final TaskConditionModel cond = sr.getResult().get(0);
								assertEquals(dummyTask, cond.getTask());
								assertEquals(conditionUID, cond.getUniqueID());
								assertEquals(Boolean.FALSE, cond.getFulfilled());

								otherThreadTask.set(dummyTask.getPk());
							}
							catch (final Exception e)
							{
								otherThreadError.set(e);
							}
						}
					};
					t.start();
					try
					{
						t.join(60 * 1000);
					}
					catch (final InterruptedException e)
					{
						Thread.currentThread().interrupt();
					}
					ScheduleTaskWhileTriggerEventService.this.otherThreadError = otherThreadError.get();
					ScheduleTaskWhileTriggerEventService.this.createdTask = otherThreadTask.get();
				}
			});

			final TaskDAO dao = new TaskDAO(Registry.getCurrentTenantNoFallback());

			setTaskDao(dao);
			strategy.setTaskDao(dao);

			this.flexibleSearchService = fs;
		}

		@Override
		protected void checkTask(final TaskModel toBeScheduled)
		{
			// we dont need that
		}

		@Override
		public void triggerRepoll(final Integer nodeId, final String nodeGroupId)
		{
			// we dont need that
		}
	}

}
