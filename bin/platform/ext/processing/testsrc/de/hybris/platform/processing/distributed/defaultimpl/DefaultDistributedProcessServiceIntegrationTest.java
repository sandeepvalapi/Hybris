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
import de.hybris.platform.processing.distributed.defaultimpl.TestDistributedProcessHandler.TestBatchCreationData;
import de.hybris.platform.processing.distributed.defaultimpl.TestDistributedProcessHandler.TestProcessCreationData;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;


@IntegrationTest
public class DefaultDistributedProcessServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private Controller distributedProcessController;

	private DefaultDistributedProcessService defaultDistributedProcessService;

	@Before
	public void setUp() throws Exception
	{
		defaultDistributedProcessService = new DefaultDistributedProcessService(modelService, flexibleSearchService,
				distributedProcessController);
	}

	@Test
	public void shouldCreateAndExecuteProcess() throws InterruptedException
	{
		final List<TestBatchCreationData> batches = ImmutableList.of(new TestBatchCreationData("b0", 0),
				new TestBatchCreationData("b1", 1), new TestBatchCreationData("b2", 2), new TestBatchCreationData("b3", 3));
		final TestProcessCreationData processData = new TestProcessCreationData(batches);

		DistributedProcessModel process = defaultDistributedProcessService.create(processData);

		assertThat(process).isNotNull();
		assertThat(process.getPk()).isNotNull();
		assertThat(process.getState()).isEqualTo(DistributedProcessState.CREATED);
		assertThat(batches(process)).hasSize(3).contains( //
				initial("b1", "CREATED", 1), initial("b2", "CREATED", 2), initial("b3", "CREATED", 3) //
		);

		process = defaultDistributedProcessService.start(process.getCode());

		assertThat(process.getState()).isNotEqualTo(DistributedProcessState.CREATED);

		process = defaultDistributedProcessService.wait(process.getCode(), 60);

		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
		assertThat(batches(process)).hasSize(15).contains( //
				initial("b3", "CREATED", 3), initial("b2", "CREATED", 2), initial("b1", "CREATED", 1), //
				input("b3", "0", 3), input("b2", "0", 2), input("b1", "0", 1), //
				result("b3", "0", 2), result("b2", "0", 1), result("b1", "0", 0), //
				input("b3", "1", 2), input("b2", "1", 1), //
				result("b3", "1", 1), result("b2", "1", 0), //
				input("b3", "2", 1), //
				result("b3", "2", 0) //
		);
	}

	private Set<BatchIdentity> batches(final DistributedProcessModel process)
	{
		return process.getBatches().stream().map(b -> BatchIdentity.from(b)).collect(Collectors.toSet());
	}

	private BatchIdentity initial(final String id, final String executionId, final long remainingWorkLoad)
	{
		return BatchIdentity.create(id, executionId, BatchType.INITIAL, remainingWorkLoad);
	}

	private BatchIdentity input(final String id, final String executionId, final long remainingWorkLoad)
	{
		return BatchIdentity.create(id, executionId, BatchType.INPUT, remainingWorkLoad);
	}

	private BatchIdentity result(final String id, final String executionId, final long remainingWorkLoad)
	{
		return BatchIdentity.create(id, executionId, BatchType.RESULT, remainingWorkLoad);
	}

	private static class BatchIdentity
	{
		private final String identityString;

		public static BatchIdentity from(final BatchModel batch)
		{
			return create(batch.getId(), batch.getExecutionId(), batch.getType(), batch.getRemainingWorkLoad());
		}

		public static BatchIdentity create(final String id, final String executionId, final BatchType type, final long remainingWorkLoad)
		{
			return new BatchIdentity(String.format("%s:%s:%s:%d", id, executionId, type, Long.valueOf(remainingWorkLoad)));
		}

		private BatchIdentity(final String identityString)
		{
			this.identityString = identityString;
		}

		@Override
		public boolean equals(final Object obj)
		{
			return identityString.equals(((BatchIdentity) obj).identityString);
		}

		@Override
		public int hashCode()
		{
			return identityString.hashCode();
		}

		@Override
		public String toString()
		{
			return identityString;
		}
	}

}
