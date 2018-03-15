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
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.impex.model.DistributedImportProcessModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler.ModelWithDependencies;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class ImportProcessInitializationContextIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;

	@Test
	public void shouldInitializeProcessExecutionId()
	{
		final DistributedImportProcessModel process = givenProcess(DistributedProcessState.CREATED, "EX_ID");
		final ImportProcessInitializationContext ctx = givenInitializationContext(process);

		final ModelWithDependencies<DistributedProcessModel> models = ctx.initializeProcess();

		assertThat(models).isNotNull();
		assertThat(models.getAllModels()).hasSize(1);
		assertThat(models.getModel()).isNotNull();
		assertThat(models.getModel().getCurrentExecutionId()).isEqualTo(ProcessExecutionId.INITIAL.toString());
	}

	@Test
	public void shouldTreatAllInitialBatchesAsInputWhenTheyBelongToTheSameGroup()
	{
		final int group = ProcessExecutionId.INITIAL.getGroup();
		final ImportBatchModel initialB1 = createBatch("b1", 12, "EX", group, BatchType.INITIAL, "b1Content");
		final ImportBatchModel initialiB2 = createBatch("b2", 23, "EX", group, BatchType.INITIAL, "b2Content");
		final DistributedImportProcessModel process = givenProcess(DistributedProcessState.CREATED, "EX_ID", initialB1, initialiB2);
		final ImportProcessInitializationContext ctx = givenInitializationContext(process);

		final List<ModelWithDependencies<BatchModel>> batches = ctx.firstExecutionInputBatches().collect(Collectors.toList());

		assertThat(batches).isNotNull().hasSize(2);

		assertThat(batches.get(0)).isNotNull();
		assertThat(batches.get(0).getAllModels()).isNotNull().hasSize(1);
		assertThat(batches.get(0).getModel()).isNotNull().isInstanceOf(ImportBatchModel.class);
		assertThat(batches.get(1)).isNotNull();
		assertThat(batches.get(1).getAllModels()).isNotNull().hasSize(1);
		assertThat(batches.get(1).getModel()).isNotNull().isInstanceOf(ImportBatchModel.class);


		final ImportBatchModel inputB1 = batches.stream().map(b -> (ImportBatchModel) b.getModel())
				.filter(b -> b.getId().equals("b1")).findAny().get();
		assertThat(inputB1.getType()).isEqualTo(BatchType.INPUT);
		assertThat(inputB1.getRemainingWorkLoad()).isEqualTo(12);
		assertThat(inputB1.getImportContentCode()).isEqualTo("b1Content");

		final ImportBatchModel inputB2 = batches.stream().map(b -> (ImportBatchModel) b.getModel())
				.filter(b -> b.getId().equals("b2")).findAny().get();
		assertThat(inputB2.getType()).isEqualTo(BatchType.INPUT);
		assertThat(inputB2.getRemainingWorkLoad()).isEqualTo(23);
		assertThat(inputB2.getImportContentCode()).isEqualTo("b2Content");
	}

	@Test
	public void shouldTreatAsInputOnlyInitialBatchesFromFirstGroup()
	{
		final int group = ProcessExecutionId.INITIAL.getGroup();
		final ImportBatchModel initialB1 = createBatch("b1", 12, "EX", group, BatchType.INITIAL, "b1Content");
		final ImportBatchModel initialiB2 = createBatch("b2", 23, "EX", group + 1, BatchType.INITIAL, "b2Content");
		final DistributedImportProcessModel process = givenProcess(DistributedProcessState.CREATED, "EX_ID", initialB1, initialiB2);
		final ImportProcessInitializationContext ctx = givenInitializationContext(process);

		final List<ModelWithDependencies<BatchModel>> batches = ctx.firstExecutionInputBatches().collect(Collectors.toList());

		assertThat(batches).isNotNull().hasSize(1);

		assertThat(batches.get(0)).isNotNull();
		assertThat(batches.get(0).getAllModels()).isNotNull().hasSize(1);
		assertThat(batches.get(0).getModel()).isNotNull().isInstanceOf(ImportBatchModel.class);
		final ImportBatchModel inputB1 = (ImportBatchModel) batches.get(0).getModel();
		assertThat(inputB1.getType()).isEqualTo(BatchType.INPUT);
		assertThat(inputB1.getRemainingWorkLoad()).isEqualTo(12);
		assertThat(inputB1.getImportContentCode()).isEqualTo("b1Content");
	}

	private DistributedImportProcessModel givenProcess(final DistributedProcessState state, final String executionId,
			final ImportBatchModel... batches)
	{
		final DistributedImportProcessModel process = modelService.create(DistributedImportProcessModel.class);

		process.setCode(UUID.randomUUID().toString());
		process.setState(state);
		process.setCurrentExecutionId(executionId);
		process.setImpExImportCronJob(prepareImportCronJob("test-distributed-impex-cronJob"));

		for (final ImportBatchModel batch : batches)
		{
			batch.setProcess(process);
		}

		modelService.saveAll();

		return process;
	}

	private ImpExImportCronJobModel prepareImportCronJob(final String processCode)
	{
		final ImpExImportJobModel job = modelService.create(ImpExImportJobModel.class);
		job.setCode(processCode);
		final ImpExImportCronJobModel cronJob = modelService.create(ImpExImportCronJobModel.class);
		cronJob.setCode(processCode);
		cronJob.setJob(job);
		cronJob.setStatus(CronJobStatus.PAUSED);

		return cronJob;
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

	private ImportProcessInitializationContext givenInitializationContext(final DistributedImportProcessModel process)
	{
		return new ImportProcessInitializationContext(flexibleSearchService, modelService, process);
	}
}
