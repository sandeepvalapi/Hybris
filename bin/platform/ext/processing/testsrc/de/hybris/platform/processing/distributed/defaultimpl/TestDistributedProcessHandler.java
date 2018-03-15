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

import de.hybris.platform.processing.distributed.BatchCreationData;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


public class TestDistributedProcessHandler implements DistributedProcessHandler
{
	private final ModelService modelService;
	private final FlexibleSearchService flexibleSearchService;

	public TestDistributedProcessHandler(final ModelService modelService, final FlexibleSearchService flexibleSearchService)
	{
		this.modelService = modelService;
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public ProcessCreationContext createProcessCreationContext(final ProcessCreationData processData)
	{
		final TestProcessCreationData data = (TestProcessCreationData) processData;

		return new ProcessCreationContext()
		{
			@Override
			public ModelWithDependencies<DistributedProcessModel> createProcessModel()
			{
				final DistributedProcessModel process = modelService.create(DistributedProcessModel.class);
				process.setCode(UUID.randomUUID().toString());
				process.setHandlerBeanId(data.getHandlerBeanId());
				return ModelWithDependencies.singleModel(process);
			}

			@Override
			public Stream<ModelWithDependencies<BatchModel>> initialBatches()
			{
				return data.initialBatches().filter(b -> b.getInitialWorkToDo() > 0).map(b -> {
					final BatchModel batch = modelService.create(BatchModel.class);
					batch.setId(b.getId());
					batch.setRemainingWorkLoad(b.getInitialWorkToDo());
					return ModelWithDependencies.singleModel(batch);
				});
			}
		};
	}

	@Override
	public ProcessInitializationContext createProcessInitializationContext(final DistributedProcessModel process)
	{
		return new ProcessInitializationContext()
		{
			@Override
			public ModelWithDependencies<DistributedProcessModel> initializeProcess()
			{
				process.setCurrentExecutionId("0");
				return ModelWithDependencies.singleModel(process);
			}

			private static final String INITIAL_BATCHES_QUERY = "select {" + BatchModel.ID + "}, {" + BatchModel.REMAININGWORKLOAD
					+ "} from {" + BatchModel._TYPECODE + "} where {" + BatchModel.PROCESS + "}=?process and {" + BatchModel.TYPE
					+ "}=?type";

			@Override
			public Stream<ModelWithDependencies<BatchModel>> firstExecutionInputBatches()
			{
				final Map<String, Object> params = ImmutableMap.of("process", process, "type", BatchType.INITIAL);
				final List<Class<?>> resultClasses = ImmutableList.of(String.class, Integer.class);
				final FlexibleSearchQuery query = new FlexibleSearchQuery(INITIAL_BATCHES_QUERY, params);
				query.setResultClassList(resultClasses);

				final SearchResult<List> result = flexibleSearchService.search(query);

				return result.getResult().stream().map(row -> {
					final BatchModel batch = modelService.create(BatchModel.class);
					batch.setId((String) row.get(0));
					batch.setRemainingWorkLoad(((Integer) row.get(1)).intValue());
					return ModelWithDependencies.singleModel(batch);
				});
			}
		};
	}

	@Override
	public ProcessExecutionAnalysisContext createProcessExecutionAnalysisContext(final DistributedProcessModel process)
	{
		final String previousExecutionId = process.getCurrentExecutionId();
		return new ProcessExecutionAnalysisContext()
		{

			@Override
			public boolean processFailed()
			{
				return false;
			}

			@Override
			public boolean processSucceeded()
			{
				return false;
			}

			@Override
			public ModelWithDependencies<DistributedProcessModel> prepareProcessForNextExecution()
			{
				process.setCurrentExecutionId(Integer.toString(Integer.parseInt(previousExecutionId) + 1));
				return ModelWithDependencies.singleModel(process);
			}

			private static final String RESULT_BATCHES_QUERY = "select {" + BatchModel.ID + "}, {" + BatchModel.REMAININGWORKLOAD
					+ "} from {" + BatchModel._TYPECODE + "} where {" + BatchModel.PROCESS + "}=?process and {" + BatchModel.TYPE
					+ "}=?type and {" + BatchModel.EXECUTIONID + "}=?executionId and {" + BatchModel.REMAININGWORKLOAD + "}>0";

			@Override
			public Stream<ModelWithDependencies<BatchModel>> nextExecutionInputBatches()
			{
				final Map<String, Object> params = ImmutableMap.of("process", process, "type", BatchType.RESULT, "executionId",
						previousExecutionId);
				final List<Class<?>> resultClasses = ImmutableList.of(String.class, Integer.class);
				final FlexibleSearchQuery query = new FlexibleSearchQuery(RESULT_BATCHES_QUERY, params);
				query.setResultClassList(resultClasses);

				final SearchResult<List> result = flexibleSearchService.search(query);

				return result.getResult().stream().map(row -> {
					final BatchModel batch = modelService.create(BatchModel.class);
					batch.setId((String) row.get(0));
					batch.setRemainingWorkLoad(((Integer) row.get(1)).intValue());
					return ModelWithDependencies.singleModel(batch);
				});
			}
		};
	}

	@Override
	public ModelWithDependencies<BatchModel> createResultBatch(final BatchModel inputBatch)
	{
		final BatchModel result = modelService.create(BatchModel.class);

		result.setId(inputBatch.getId());
		result.setRemainingWorkLoad(inputBatch.getRemainingWorkLoad() - 1);

		return ModelWithDependencies.singleModel(result);
	}

	@Override
	public void onFinished(final DistributedProcessModel process)
	{
		return;
	}

	public static class TestProcessCreationData implements ProcessCreationData
	{
		private final List<TestBatchCreationData> batches;

		public TestProcessCreationData(final List<TestBatchCreationData> batches)
		{
			this.batches = ImmutableList.copyOf(batches);
		}

		@Override
		public Stream<TestBatchCreationData> initialBatches()
		{
			return batches.stream();
		}

		@Override
		public String getHandlerBeanId()
		{
			return "testDistributedProcessHandler";
		}

		@Override
		public String getNodeGroup()
		{
			return null;
		}
	}

	public static class TestBatchCreationData implements BatchCreationData
	{
		private final int initialWorkToDo;
		private final String id;

		public TestBatchCreationData(final String id, final int initialWorkToDo)
		{
			this.id = id;
			this.initialWorkToDo = initialWorkToDo;
		}

		public String getId()
		{
			return id;
		}

		public int getInitialWorkToDo()
		{
			return initialWorkToDo;
		}
	}

}
