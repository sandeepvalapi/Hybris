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
package de.hybris.platform.processing.distributed.simple;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.distributed.simple.data.CollectionBasedCreationData;
import de.hybris.platform.processing.distributed.simple.data.QueryBasedCreationData;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.testframework.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


public class SimpleDistributedProcessIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private DistributedProcessService distributedProcessService;
	private List<PK> titles;

	@Before
	public void setUp() throws Exception
	{
		titles = new ArrayList<>();
		IntStream.range(0, 1050).forEach(this::createTitle);

		assertThat(titles).hasSize(1050);
	}

	private void createTitle(final int i)
	{
		createTitle(i + "_" + UUID.randomUUID().toString());
	}

	private void createTitle(final String code)
	{
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode(code);
		modelService.save(title);
		titles.add(title.getPk());
	}

	@After
	public void tearDown() throws Exception
	{
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void shouldFinishProcessSuccessfully() throws Exception
	{
		// given
		final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
				.withQuery("SELECT {PK} FROM {Title}") //
				.withHandlerId("testSimpleDistributedProcessHandler") //
				.build();

        // when
        final DistributedProcessModel process = startProcess(testProcessData);

        // then
        assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
        assertThat(getNumTitles()).isEqualTo(0);
    }

    @Test
    public void shouldFinishProcessSuccessfullyUsingDatabasePaging() throws Exception
    {
    	// given
        final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
                .withQuery("SELECT {PK} FROM {Title} ORDER BY {code}") //
                .useDatabasePaging()
                .withHandlerId("testSimpleDistributedProcessHandler") //
                .build();

        // when
        final DistributedProcessModel process = startProcess(testProcessData);

        // then
        assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
        assertThat(getNumTitles()).isEqualTo(0);
    }

	@Test
	public void shouldFailWholeProcessWhenEachBatchConstantlyThrowsAnException() throws Exception
	{
		// given
		TestUtils.disableFileAnalyzer("Expecting exceptions");
		final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
				.withQuery("SELECT {PK} FROM {Title}") //
				.withHandlerId("completelyFailingTestSimpleDistributedProcessHandler") //
				.build();

		// when
		final DistributedProcessModel process = startProcess(testProcessData);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.FAILED);
		assertThat(getNumTitles()).isEqualTo(1050);
	}

	@Test
	public void shouldFinishPossibleToDoBatchesAndTryToRetryThoseWhichAreFailing() throws Exception
	{
		// given
		TestUtils.disableFileAnalyzer("Expecting exceptions");
		final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
				.withQuery("SELECT {PK} FROM {Title}") //
				.withHandlerId("sometimesFailingTestSimpleDistributedProcessHandler") //
				.build();

		// when
		final DistributedProcessModel process = startProcess(testProcessData);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.FAILED);
		assertThat(getNumTitles()).isEqualTo(50);
	}

	@Test
	public void shouldFinishProcessSuccessfullyForMultiValueQuery() throws Exception
	{
		// given
		final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
				.withQuery("SELECT {PK}, {code} FROM {Title}") //
				.withHandlerId("multiValueTestSimpleDistributedProcessHandler") //
				.withResultClasses(ImmutableList.of(PK.class, String.class)) //
				.build();

		// when
		final DistributedProcessModel process = startProcess(testProcessData);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
		assertThat(getNumTitles()).isEqualTo(0);
	}


	@Test
	public void shouldFinishProcessSuccesfullyOnlyForItemsMatchesTheQuery() throws Exception
	{
		// given
		final List<String> titleCodes = createThousandSpecificTitles();
		final String searchQuery = "SELECT {PK} FROM {Title} WHERE {code} IN (?params)";
		final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
				.withQuery(searchQuery) //
				.withQueryParams(ImmutableMap.of("params", titleCodes)) //
				.withHandlerId("testSimpleDistributedProcessHandler") //
				.build();

		// when
		final DistributedProcessModel process = startProcess(testProcessData);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
		assertThat(getNumTitles()).isEqualTo(1050); // those from setUp method should stay
	}

	@Test
	public void shouldFinishProcessSuccesfullyUsingScriptingBasedProcessor() throws Exception
	{
		// given
		final String scriptCode = createScriptingProcessor();
		final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
				.withQuery("SELECT {PK} FROM {Title}") //
				.withScriptCode(scriptCode)//
				.build();

		// when
		final DistributedProcessModel process = startProcess(testProcessData);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
		assertThat(getNumTitles()).isEqualTo(0);
	}

	@Test
	public void shouldFinishProcessSuccessfullyWithUseOfFullyBlownFlexibleSearchQueryObject() throws Exception
	{
		// given
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Title} ORDER BY {code}");
		fQuery.setDisableCaching(true);
		fQuery.setResultClassList(ImmutableList.of(PK.class));
		final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
				.withFlexibleSearchQuery(fQuery) //
				.withHandlerId("testSimpleDistributedProcessHandler") //
				.build();

		// when
		final DistributedProcessModel process = startProcess(testProcessData);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
		assertThat(getNumTitles()).isEqualTo(0);
	}

	@Test
	public void shouldAllowToSetHookBeforeQueryWhenProducingBatches() throws Exception
	{
		// given
		final HookChecker hookChecker = new HookChecker();
		assertThat(hookChecker.isHookSet()).isFalse();
		final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
				.withQuery("SELECT {PK} FROM {Title} ORDER BY {code}") //
				.withHandlerId("testSimpleDistributedProcessHandler") //
				.withBeforeQueryHook(hookChecker::setHook).build();

		// when
		final DistributedProcessModel process = startProcess(testProcessData);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
		assertThat(getNumTitles()).isEqualTo(0);
		assertThat(hookChecker.isHookSet()).isTrue();
	}

	@Test
	public void shouldThrowAnExceptionWhenQueryIsNull() throws Exception
	{
		try
		{
			// given
			final QueryBasedCreationData testProcessData = QueryBasedCreationData.builder() //
					.withHandlerId("testSimpleDistributedProcessHandler") //
					.build();

			// when
			startProcess(testProcessData);
            fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldFinishProcessSuccessfullyUsingCollectionBasedCreationData() throws Exception
	{
		final CollectionBasedCreationData testProcessData = CollectionBasedCreationData.builder() //
				.withElements(titles) //
				.withHandlerId("testSimpleDistributedProcessHandler") //
				.build();

		// when
		final DistributedProcessModel process = startProcess(testProcessData);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
		assertThat(getNumTitles()).isEqualTo(0);

	}

	private String createScriptingProcessor()
	{
		final String scriptContent = "import de.hybris.platform.core.PK\n" + //
				"import de.hybris.platform.processing.model.SimpleBatchModel\n" + //
				"import de.hybris.platform.processing.distributed.simple.SimpleBatchProcessor\n" + //
				"\n" + //
				"public class TestBatchProcessor implements SimpleBatchProcessor\n" + //
				"{\n" + //
				"    def modelService\n" + //
				"\n" + //
				"    @Override\n" + //
				"    public void process(final SimpleBatchModel inputBatch)\n" + //
				"    {\n" + //
				"        inputBatch.getContext().each { modelService.remove(it) }\n" + //
				"    }\n" + //
				"}\n" + //
				"\n" + //
				"new TestBatchProcessor(modelService: modelService)"; //
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setScriptType(ScriptType.GROOVY);
		script.setCode(UUID.randomUUID().toString());
		script.setContent(scriptContent);
		modelService.save(script);

		return script.getCode();
	}

	private List<String> createThousandSpecificTitles()
	{
		return IntStream.range(0, 1000).mapToObj(i -> {
			final String code = "specific_" + i;
			createTitle(code);
			return code;
		}).collect(Collectors.toList());
	}

	private long getNumTitles()
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT count({PK}) FROM {Title}");
		fQuery.setResultClassList(ImmutableList.of(Long.class));

		final Long count = flexibleSearchService.<Long> search(fQuery).getResult().get(0);

		return count.longValue();
	}

	private DistributedProcessModel startProcess(final ProcessCreationData testProcessData) throws InterruptedException
	{
		final DistributedProcessModel process = distributedProcessService.create(testProcessData);
		distributedProcessService.start(process.getCode());
		distributedProcessService.wait(process.getCode(), 100);
		return process;
	}

	private class HookChecker
	{
		private boolean hook;

		public void setHook()
		{
			hook = true;
		}

		public boolean isHookSet()
		{
			return hook;
		}
	}

}
