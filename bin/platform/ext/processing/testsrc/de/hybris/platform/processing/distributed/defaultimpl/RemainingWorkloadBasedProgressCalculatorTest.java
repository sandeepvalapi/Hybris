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
package de.hybris.platform.processing.distributed.defaultimpl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class RemainingWorkloadBasedProgressCalculatorTest extends ServicelayerBaseTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;


	@Test
	public void shouldReturnZeroWhenProcessIsNew()
	{
		final DistributedProcessModel process = givenNewProcess();
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isZero();
	}

	@Test
	public void shouldReturnZeroWhenProcessIsInCREATEDstate()
	{
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.CREATED);
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isZero();
	}

	@Test
	public void shouldReturnZeroWhenThereAreNoInitialBatches()
	{
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.WAITING_FOR_EXECUTION);
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isZero();
	}

	@Test
	public void shouldReturnZeroWhenThereAre()
	{
		final DistributedProcessModel process = givenProcessWithBatches( //
				initial("b1", 40), initial("b2", 60) //
		);
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isZero();
	}

	@Test
	public void shouldReturnZeroWhenFirstTurnWasOnlyScheduled()
	{
		final DistributedProcessModel process = givenProcessWithBatches( //
				initial("b1", 40), initial("b2", 60), //
				input("b1", 40, "FIRST_TURN"), input("b2", 60, "FIRST_TURN"));
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isZero();
	}

	@Test
	public void shouldReturnValidProgressWhenFirstTurnCompletedAllBatches()
	{
		final DistributedProcessModel process = givenProcessWithBatches( //
				initial("b1", 40), initial("b2", 60), //
				input("b1", 40, "FIRST_TURN"), input("b2", 60, "FIRST_TURN"), //
				result("b1", 0, "FIRST_TURN"), result("b2", 0, "FIRST_TURN") //
		);
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isEqualTo(100.0);
	}

	@Test
	public void shouldReturnValidProgressWhenFirstTurnCompletedPartially()
	{
		final DistributedProcessModel process = givenProcessWithBatches( //
				initial("b1", 40), initial("b2", 60), //
				input("b1", 40, "FIRST_TURN"), input("b2", 60, "FIRST_TURN"), //
				result("b1", 20, "FIRST_TURN") //
		);
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isEqualTo(20.0);
	}

	@Test
	public void shouldReturnValidProgressWhenSecondTurnCompletedPartially()
	{
		final DistributedProcessModel process = givenProcessWithBatches( //
				initial("b1", 40), initial("b2", 60), //
				input("b1", 40, "FIRST_TURN"), input("b2", 60, "FIRST_TURN"), //
				result("b1", 20, "FIRST_TURN"), //
				input("b1", 20, "SECOND_TURN"), input("b2", 60, "SECOND_TURN"), //
				result("b2", 30, "SECOND_TURN") //
		);
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isEqualTo(50.0);
	}

	@Test
	public void shouldReturnValidProgressWhenSecondCompletedAllBatches()
	{
		final DistributedProcessModel process = givenProcessWithBatches( //
				initial("b1", 40), initial("b2", 60), //
				input("b1", 40, "FIRST_TURN"), input("b2", 60, "FIRST_TURN"), //
				result("b1", 20, "FIRST_TURN"), //
				input("b1", 20, "SECOND_TURN"), input("b2", 60, "SECOND_TURN"), //
				result("b1", 0, "SECOND_TURN"), result("b2", 0, "SECOND_TURN") //
		);
		final RemainingWorkloadBasedProgressCalculator calculator = givenCalculator(process);

		final double progress = calculator.calculateProgress();

		assertThat(progress).isEqualTo(100.0);
	}

	private RemainingWorkloadBasedProgressCalculator givenCalculator(final DistributedProcessModel process)
	{
		return new RemainingWorkloadBasedProgressCalculator(process, flexibleSearchService);
	}

	private DistributedProcessModel givenProcessWithBatches(final BatchModel... batches)
	{
		final DistributedProcessModel process = givenNewProcess();
		process.setCode(UUID.randomUUID().toString());
		process.setCurrentExecutionId(UUID.randomUUID().toString());
		process.setState(DistributedProcessState.WAITING_FOR_EXECUTION);

		Arrays.asList(batches).forEach(b -> b.setProcess(process));

		final List<ItemModel> modelsToSave = new LinkedList<>();
		modelsToSave.addAll(Arrays.asList(batches));
		modelsToSave.add(process);

		modelService.saveAll(modelsToSave);
		return process;
	}

	private DistributedProcessModel givenProcessInState(final DistributedProcessState state)
	{
		final DistributedProcessModel process = givenNewProcess();
		process.setCode(UUID.randomUUID().toString());
		process.setCurrentExecutionId(UUID.randomUUID().toString());
		process.setState(state);
		modelService.save(process);
		return process;
	}

	private DistributedProcessModel givenNewProcess()
	{
		return modelService.create(DistributedProcessModel.class);
	}

	private BatchModel result(final String id, final long remainingWorkload, final String turnId)
	{
		final BatchModel model = modelService.create(BatchModel.class);
		model.setId(id);
		model.setRemainingWorkLoad(remainingWorkload);
		model.setExecutionId(turnId);
		model.setType(BatchType.RESULT);
		return model;
	}

	private BatchModel input(final String id, final long remainingWorkload, final String turnId)
	{
		final BatchModel model = modelService.create(BatchModel.class);
		model.setId(id);
		model.setRemainingWorkLoad(remainingWorkload);
		model.setExecutionId(turnId);
		model.setType(BatchType.INPUT);
		return model;
	}

	private BatchModel initial(final String id, final long remainingWorkload)
	{
		final BatchModel model = modelService.create(BatchModel.class);
		model.setId(id);
		model.setRemainingWorkLoad(remainingWorkload);
		model.setExecutionId("Initial");
		model.setType(BatchType.INITIAL);
		return model;
	}
}
