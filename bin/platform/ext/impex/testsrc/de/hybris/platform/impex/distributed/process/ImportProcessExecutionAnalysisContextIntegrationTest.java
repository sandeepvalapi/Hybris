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
package de.hybris.platform.impex.distributed.process;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class ImportProcessExecutionAnalysisContextIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Test
	public void shouldSucceedAfterFirstTurnWhenThereIsOnlyOneGroupAndAllBatchesHaveBeenFullyProcessed()
	{
		final ProcessExecutionId gr0 = ProcessExecutionId.INITIAL;
		final DistributedProcessModel process = givenProcess((gr0), //
				initial("b1", 123, 0), initial("b2", 12, 0), initial("b3", 3, 0), //
				input("b1", 123, gr0), input("b2", 12, gr0), input("b3", 3, gr0), //
				result("b1", 0, gr0), result("b2", 0, gr0), result("b3", 0, gr0) //
		);

		final ImportProcessExecutionAnalysisContext ctx = givenAnalysisContext(process);

		assertThat(ctx.processFailed()).isFalse();
		assertThat(ctx.processSucceeded()).isTrue();
	}

	@Test
	public void shouldSucceedAfterFirstTurnWhenThereManyGroupsAndAllBatchesHaveBeenFullyProcessed()
	{
		final ProcessExecutionId gr0 = ProcessExecutionId.INITIAL;
		final ProcessExecutionId gr1 = gr0.nextGroup();
		final DistributedProcessModel process = givenProcess((gr1), //
				initial("b1", 123, 0), initial("b2", 12, 0), initial("b3", 3, 0), initial("b4", 7, 1), //
				input("b1", 123, gr0), input("b2", 12, gr0), input("b3", 3, gr0), //
				result("b1", 0, gr0), result("b2", 0, gr0), result("b3", 0, gr0), //
				input("b4", 7, gr1), //
				result("b4", 0, gr1) //
		);

		final ImportProcessExecutionAnalysisContext ctx = givenAnalysisContext(process);

		assertThat(ctx.processFailed()).isFalse();
		assertThat(ctx.processSucceeded()).isTrue();
	}

	@Test
	public void shouldContinueExecutingOfNextGroupForFirstTurn()
	{
		final ProcessExecutionId gr0 = ProcessExecutionId.INITIAL;
		final DistributedProcessModel process = givenProcess((gr0), //
				initial("b1", 123, 0), initial("b2", 12, 0), initial("b3", 3, 0), initial("b4", 7, 1), //
				input("b1", 123, gr0), input("b2", 12, gr0), input("b3", 3, gr0), //
				result("b1", 0, gr0), result("b3", 1, gr0) //
		);

		final ImportProcessExecutionAnalysisContext ctx = givenAnalysisContext(process);

		assertThat(ctx.processFailed()).isFalse();
		assertThat(ctx.processSucceeded()).isFalse();
		final DistributedProcessModel changedProcess = ctx.prepareProcessForNextExecution().getModel();
		assertThat(changedProcess.getCurrentExecutionId()).isEqualTo(gr0.nextGroup().toString());

		final Map<String, ImportBatchModel> batches = nextInputBatches(ctx);
		assertThat(batches.keySet()).hasSize(1).contains("b4");
	}

	@Test
	public void shouldContinueExecutingSucce()
	{
		final ProcessExecutionId gr0 = ProcessExecutionId.INITIAL;
		final ProcessExecutionId gr1 = gr0.nextGroup();
		final DistributedProcessModel process = givenProcess((gr1), //
				initial("b1", 123, 0), initial("b2", 12, 0), initial("b3", 3, 0), initial("b4", 7, 1), //
				input("b1", 123, gr0), input("b2", 12, gr0), input("b3", 3, gr0), //
				result("b1", 0, gr0), result("b3", 1, gr0), //
				input("b4", 7, gr1), //
				result("b4", 3, gr1) //
		);

		final ImportProcessExecutionAnalysisContext ctx = givenAnalysisContext(process);

		assertThat(ctx.processFailed()).isFalse();
		assertThat(ctx.processSucceeded()).isFalse();
		final DistributedProcessModel changedProcess = ctx.prepareProcessForNextExecution().getModel();
		assertThat(changedProcess.getCurrentExecutionId()).isEqualTo(gr1.nextTurn().toString());

		final Map<String, ImportBatchModel> batches = nextInputBatches(ctx);
		assertThat(batches.keySet()).hasSize(3).contains("b2", "b3", "b4");
	}

	@Test
	public void shouldSucceedAfterSecondTurnWhenWhenAllBatchesHaveBeenFullyProcessed()
	{
		final ProcessExecutionId gr0 = ProcessExecutionId.INITIAL;
		final ProcessExecutionId gr1 = gr0.nextGroup();
		final ProcessExecutionId t2 = gr0.nextTurn();
		final DistributedProcessModel process = givenProcess((t2), //
				initial("b1", 123, 0), initial("b2", 12, 0), initial("b3", 3, 0), initial("b4", 7, 1), //
				input("b1", 123, gr0), input("b2", 12, gr0), input("b3", 3, gr0), //
				result("b1", 0, gr0), result("b3", 1, gr0), //
				input("b4", 7, gr1), //
				result("b4", 3, gr1), //
				input("b2", 12, t2), input("b3", 1, t2), input("b4", 3, t2), //
				result("b2", 0, t2), result("b3", 0, t2), result("b4", 0, t2) //
		);

		final ImportProcessExecutionAnalysisContext ctx = givenAnalysisContext(process);

		assertThat(ctx.processFailed()).isFalse();
		assertThat(ctx.processSucceeded()).isTrue();
	}

	@Test
	public void shouldContinueExecutionOfNextTurnWithLeftoversFromPreviousTurn()
	{
		final ProcessExecutionId gr0 = ProcessExecutionId.INITIAL;
		final ProcessExecutionId gr1 = gr0.nextGroup();
		final ProcessExecutionId t2 = gr0.nextTurn();
		final DistributedProcessModel process = givenProcess((t2), //
				initial("b1", 123, 0), initial("b2", 12, 0), initial("b3", 3, 0), initial("b4", 7, 1), //
				input("b1", 123, gr0), input("b2", 12, gr0), input("b3", 3, gr0), //
				result("b1", 0, gr0), result("b3", 1, gr0), //
				input("b4", 7, gr1), //
				result("b4", 3, gr1), //
				input("b2", 12, t2), input("b3", 1, t2), input("b4", 3, t2), //
				result("b2", 1, t2), result("b3", 0, t2) //
		);

		final ImportProcessExecutionAnalysisContext ctx = givenAnalysisContext(process);

		assertThat(ctx.processFailed()).isFalse();
		assertThat(ctx.processSucceeded()).isFalse();
		final DistributedProcessModel changedProcess = ctx.prepareProcessForNextExecution().getModel();
		assertThat(changedProcess.getCurrentExecutionId()).isEqualTo(t2.nextTurn().toString());

		final Map<String, ImportBatchModel> batches = nextInputBatches(ctx);
		assertThat(batches.keySet()).hasSize(2).contains("b2", "b4");
	}

	@Test
	public void shouldFailIfThereIsNoPrograssionInThreeLatestTurns()
	{
		final ProcessExecutionId gr0 = ProcessExecutionId.INITIAL;
		final ProcessExecutionId gr1 = gr0.nextGroup();
		final ProcessExecutionId t2 = gr0.nextTurn();
		final ProcessExecutionId t3 = t2.nextTurn();
		final ProcessExecutionId t4 = t3.nextTurn();
		final ProcessExecutionId t5 = t4.nextTurn();
		final DistributedProcessModel process = givenProcess((t5), //
				initial("b1", 123, 0), initial("b2", 12, 0), initial("b3", 3, 0), initial("b4", 7, 1), //
				input("b1", 123, gr0), input("b2", 12, gr0), input("b3", 3, gr0), //
				result("b1", 0, gr0), result("b3", 1, gr0), //
				input("b4", 7, gr1), //
				result("b4", 3, gr1), //
				input("b2", 12, t2), input("b3", 1, t2), input("b4", 3, t2), //
				result("b2", 1, t2), result("b3", 0, t2), //
				input("b2", 1, t3), input("b4", 3, t3), //
				input("b2", 1, t4), input("b4", 3, t4), //
				input("b2", 1, t5), input("b4", 3, t5) //
		);

		final ImportProcessExecutionAnalysisContext ctx = givenAnalysisContext(process);

		assertThat(ctx.processFailed()).isTrue();
		assertThat(ctx.processSucceeded()).isFalse();
	}

	private Map<String, ImportBatchModel> nextInputBatches(final ImportProcessExecutionAnalysisContext ctx)
	{
		return ctx.nextExecutionInputBatches().map(b -> (ImportBatchModel) b.getModel())
				.collect(Collectors.toMap(b -> b.getId(), b -> b));
	}

	private ImportBatchModel result(final String id, final long remainingWorkLoad, final ProcessExecutionId executionId)
	{
		return createBatch(id, remainingWorkLoad, executionId.toString(), 0, BatchType.RESULT, id + "_content");
	}

	private ImportBatchModel input(final String id, final long remainingWorkLoad, final ProcessExecutionId executionId)
	{
		return createBatch(id, remainingWorkLoad, executionId.toString(), 0, BatchType.INPUT, id + "_content");
	}

	private ImportBatchModel initial(final String id, final long remainingWorkLoad, final int group)
	{
		return createBatch(id, remainingWorkLoad, "INIT", group, BatchType.INITIAL, id + "_content");
	}

	private DistributedProcessModel givenProcess(final ProcessExecutionId executionId, final ImportBatchModel... batches)
	{
		final DistributedProcessModel process = modelService.create(DistributedProcessModel.class);

		process.setCode(UUID.randomUUID().toString());
		process.setState(DistributedProcessState.WAITING_FOR_EXECUTION);
		process.setCurrentExecutionId(executionId.toString());

		for (final ImportBatchModel batch : batches)
		{
			batch.setProcess(process);
		}

		modelService.saveAll();

		return process;
	}

	private ImportBatchModel createBatch(final String id, final long remainingWorkLoad, final String executionId, final int group,
                                         final BatchType type, final String contentCode)
	{
		final ImportBatchModel batch = modelService.create(ImportBatchModel.class);

		batch.setId(id);
		batch.setRemainingWorkLoad(remainingWorkLoad);
		batch.setExecutionId(executionId);
		batch.setGroup(group);
		batch.setType(type);
		batch.setImportContentCode(contentCode);

		return batch;
	}

	private ImportProcessExecutionAnalysisContext givenAnalysisContext(final DistributedProcessModel process)
	{
		return new ImportProcessExecutionAnalysisContext(process, modelService, flexibleSearchService);
	}

}
