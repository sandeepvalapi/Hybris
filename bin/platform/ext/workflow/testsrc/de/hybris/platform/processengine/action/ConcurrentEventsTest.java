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
package de.hybris.platform.processengine.action;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.constants.TaskConstants;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.core.io.ClassPathResource;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;


@IntegrationTest
public class ConcurrentEventsTest extends ServicelayerBaseTest
{
	private static final String TEST_DEFINITION_CODE = "battleshipsProcess";

	@Resource
	private ModelService modelService;
	@Resource
	private BusinessProcessService businessProcessService;
	@Resource
	private TaskService taskService;

	private String taskEngineIntervalToRestore;

	@Before
	public void setUp() throws IOException
	{
		taskEngineIntervalToRestore = Registry.getCurrentTenantNoFallback().getConfig()
				.getParameter(TaskConstants.Params.POLLING_INTERVAL);
		setTaskEngineInterval("1");
		final DynamicProcessDefinitionModel definition = modelService.create(DynamicProcessDefinitionModel.class);
		definition.setContent(Files.toString(new ClassPathResource("test/battleshipsProcess.xml").getFile(), Charsets.UTF_8));
		definition.setCode(TEST_DEFINITION_CODE);
		modelService.save(definition);
	}

	@After
	public void tearDown()
	{
		taskService.getEngine().stop();
		Registry.getCurrentTenantNoFallback().getConfig().setParameter(TaskConstants.Params.POLLING_INTERVAL, taskEngineIntervalToRestore);
	}

	@Test
	@Ignore("HORST-825")
	public void shouldFinishBattleshipsGameWithoutErrors() throws InterruptedException, IOException
	{
		final BusinessProcessModel playerOne = givenPlayerOne();
		final BusinessProcessModel playerTwo = givenPlayerTwo(playerOne);

		businessProcessService.startProcess(playerOne);
		businessProcessService.startProcess(playerTwo);
		waitFor(TimeUnit.SECONDS.toMillis(120), playerOne, playerTwo);

		assertThat(playerOne.getState()).isEqualTo(ProcessState.FAILED);
		assertThat(playerTwo.getState()).isEqualTo(ProcessState.SUCCEEDED);
	}

	BusinessProcessModel givenPlayerOne()
	{
		final Map<String, Object> params = ImmutableMap.<String, Object> of("board",
				"..........\n..........\n...###....\n..........\n..........");
		return businessProcessService.createProcess("playerOne", TEST_DEFINITION_CODE, params);
	}

	BusinessProcessModel givenPlayerTwo(final BusinessProcessModel playerOne)
	{
		final Map<String, Object> params = ImmutableMap.<String, Object> of("board",
				"..........\n...#......\n...#......\n...#......\n..........", "enemy", playerOne.getPk());
		return businessProcessService.createProcess("playerTwo", TEST_DEFINITION_CODE, params);
	}

	private void waitFor(final long millisToWait, final BusinessProcessModel playerOne, final BusinessProcessModel playerTwo)
			throws InterruptedException
	{
		final long expectedFinishTime = System.currentTimeMillis() + millisToWait;
		while (playerOne.getState() == ProcessState.RUNNING || playerTwo.getState() == ProcessState.RUNNING)
		{
			assertThat(expectedFinishTime).overridingErrorMessage("Test should finish in " + millisToWait + " ms.").isGreaterThan(
					System.currentTimeMillis());
			Thread.sleep(10 + (millisToWait / 100));
			modelService.refresh(playerOne);
			modelService.refresh(playerTwo);
		}
	}

	private void setTaskEngineInterval(final String interval)
	{
		Registry.getCurrentTenantNoFallback().getConfig().setParameter(TaskConstants.Params.POLLING_INTERVAL, interval);
		taskService.getEngine().stop();
		taskService.getEngine().start();
	}
}
