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
package de.hybris.platform.processengine;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.impl.BusinessProcessServiceDao;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class RestartBusinessProcessTest extends ServicelayerBaseTest
{
	private static final String WAIT_NODE = "waitForever";
	private static final String START_NODE = "start";

	private static final long WAIT_TIMEOUT = TimeUnit.SECONDS.toMillis(60);

	@Resource
	private BusinessProcessService businessProcessService;

	@Resource
	private PreWaitAction preWaitAction;

	@Resource
	private PostWaitAction postWaitAction;

	@Resource
	private ModelService modelService;

	@Resource
	private BusinessProcessServiceDao businessProcessServiceDao;

	/**
	 * HORST-1336 Repair business process duplicated TaskCondition problem
	 */
	@Test
	public void restartingProcessStoppedOnWaitNodeShouldNotFail() throws InterruptedException, TimeoutException
	{
		// given
		final String uuid = UUID.randomUUID().toString();
		final BusinessProcessModel processModel = businessProcessService.createProcess(uuid, "repairBusinessProcessDefinition");

		// when
		businessProcessService.startProcess(processModel);
		waitForAction(processModel, WAIT_NODE);

		// then preWait action was invoked and we're at wait node
		assertThatActionsWereInvokedNTimes(1, 0);

		// and when
		businessProcessService.restartProcess(processModel, START_NODE);
		waitForAction(processModel, WAIT_NODE);

		// then preWait action was once again invoked and we're at wait node
		assertThatActionsWereInvokedNTimes(2, 0);

		// and when
		businessProcessService.triggerEvent(BusinessProcessEvent.newEvent(uuid + "_SomethingToHappen"));
		waitForState(processModel, ProcessState.SUCCEEDED);

		// then postWait
		assertThatActionsWereInvokedNTimes(2, 1);
	}

	private void assertThatActionsWereInvokedNTimes(final int slowActionInvocations, final int afterWaitActionInvocations)
	{
		assertThat(preWaitAction.getInvocations().get()).isEqualTo(slowActionInvocations);
		assertThat(postWaitAction.getInvocations().get()).isEqualTo(afterWaitActionInvocations);
	}

	private void waitForAction(final BusinessProcessModel model, final String state) throws InterruptedException, TimeoutException
	{
		waitForCondition(r -> {

			try
			{
				final List<String> actions = businessProcessServiceDao.findBusinessProcessTaskActions(model.getPk());
				return Boolean.valueOf(actions.iterator().next().equals(state));
			}
			catch (final NoSuchElementException ex)
			{
				return Boolean.FALSE;
			}
		});
	}

	private void waitForState(final BusinessProcessModel model, final ProcessState state)
			throws InterruptedException, TimeoutException
	{
		waitForCondition(r -> {
			modelService.refresh(model);
			return Boolean.valueOf(model.getProcessState().equals(state));
		});
	}

	private void waitForCondition(final Function<Long, Boolean> condition) throws TimeoutException, InterruptedException
	{
		final long start = System.currentTimeMillis();
		long round = 1;
		while (!condition.apply(Long.valueOf(round)).booleanValue())
		{
			if (System.currentTimeMillis() - start > WAIT_TIMEOUT)
			{
				throw new TimeoutException();
			}
			round++;
			Thread.sleep(Math.min(100, WAIT_TIMEOUT / 100));
		}
	}

	public static class PreWaitAction extends AbstractProceduralAction
	{
		private final AtomicLong invocations = new AtomicLong(0);

		@Override
		public void executeAction(final BusinessProcessModel process) throws RetryLaterException, InterruptedException
		{
			Thread.sleep(500);
			invocations.incrementAndGet();
		}

		public AtomicLong getInvocations()
		{
			return invocations;
		}
	}

	public static class PostWaitAction extends AbstractProceduralAction
	{
		private final AtomicLong invocations = new AtomicLong(0);

		@Override
		public void executeAction(final BusinessProcessModel process) throws Exception
		{
			invocations.incrementAndGet();
		}

		public AtomicLong getInvocations()
		{
			return invocations;
		}
	}

}
