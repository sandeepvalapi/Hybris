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
package de.hybris.platform.jobs;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CleanupDynamicProcessDefinitionsCronJobModel;
import de.hybris.platform.jobs.maintenance.impl.CleanupDynamicProcessDefinitionsStrategy;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.task.utils.NeedsTaskEngine;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.fest.assertions.Condition;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


@IntegrationTest
@NeedsTaskEngine
public class CleanupDynamicProcessDefinitionsStrategyIntegrationTest extends ServicelayerBaseTest
{
	private static final String FIRST_DEFINITION = "DEFINITION1";
	private static final String SECOND_DEFINITION = "DEFINITION2";
	private static final int SAVE_DELAY_SECONDS = 5;

	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private BusinessProcessService businessProcessService;

	private CleanupDynamicProcessDefinitionsStrategy cleanupStrategy;

	@Before
	public void setUp() throws TimeoutException, InterruptedException
	{
		cleanupStrategy = new CleanupDynamicProcessDefinitionsStrategy();
		cleanupStrategy.setModelService(modelService);
		createTestData();
	}

	@Test
	public void shouldReturnResultWithoutGivenThresholds()
	{
		final List<DynamicProcessDefinitionModel> definitionsToRemove = useCleanupStrategyWith(versionThreshold(null),
				timeThreshold(null));

		assertThat(definitionsToRemove).hasSize(5).satisfies(containsOnlyHistorical())
				.satisfies(containsDefinition(FIRST_DEFINITION, 1)).satisfies(containsDefinition(FIRST_DEFINITION, 2))
				.satisfies(containsDefinition(SECOND_DEFINITION, 1)).satisfies(containsDefinition(SECOND_DEFINITION, 2))
				.satisfies(containsDefinition(SECOND_DEFINITION, 3));
	}

	@Test
	public void shouldReturnResultWithGivenVersionThreshold()
	{
		final List<DynamicProcessDefinitionModel> definitionsToRemove = useCleanupStrategyWith(
				versionThreshold(Integer.valueOf(1)), timeThreshold(null));

		assertThat(definitionsToRemove).hasSize(4).satisfies(containsOnlyHistorical())
				.satisfies(containsDefinition(SECOND_DEFINITION, 3)).satisfies(containsDefinition(SECOND_DEFINITION, 2))
				.satisfies(containsDefinition(SECOND_DEFINITION, 1)).satisfies(containsDefinition(FIRST_DEFINITION, 1));
	}

	@Test
	public void shouldReturnResultWithGivenTimeThreshold()
	{
		final List<DynamicProcessDefinitionModel> definitionsToRemove = useCleanupStrategyWith(versionThreshold(null),
				timeThreshold(getTestTimeThreshold()));

		assertThat(definitionsToRemove).hasSize(3).satisfies(containsOnlyHistorical())
				.satisfies(containsDefinition(SECOND_DEFINITION, 1)).satisfies(containsDefinition(FIRST_DEFINITION, 2))
				.satisfies(containsDefinition(FIRST_DEFINITION, 1));
	}

	@Test
	public void shouldReturnResultWithGivenTimeAndVersionThresholds()
	{
		final List<DynamicProcessDefinitionModel> definitionsToRemove = useCleanupStrategyWith(
				versionThreshold(Integer.valueOf(1)), timeThreshold(getTestTimeThreshold()));

		assertThat(definitionsToRemove).hasSize(2).satisfies(containsOnlyHistorical())
				.satisfies(containsDefinition(SECOND_DEFINITION, 1)).satisfies(containsDefinition(FIRST_DEFINITION, 1));
	}

	@Test
	public void shouldRemoveRequestedDefinitions()
	{
		final String query = "select {PK} from {" + DynamicProcessDefinitionModel._TYPECODE + "} where {"
				+ DynamicProcessDefinitionModel.CODE + "}=?" + DynamicProcessDefinitionModel.CODE;
		final ImmutableMap<String, Object> params = ImmutableMap.<String, Object> of(DynamicProcessDefinitionModel.CODE,
				SECOND_DEFINITION);

		final List<DynamicProcessDefinitionModel> definitionsToRemove = flexibleSearchService
				.<DynamicProcessDefinitionModel> search(query, params).getResult();

		assertThat(definitionsToRemove).isNotEmpty();
		cleanupStrategy.process(definitionsToRemove);

		final List<DynamicProcessDefinitionModel> checkDefinitions = flexibleSearchService.<DynamicProcessDefinitionModel> search(
				query, params).getResult();
		assertThat(checkDefinitions).isEmpty();
	}

	private Integer getTestTimeThreshold()
	{
		final long reference = getReferenceDate().getTime() / 1000;
		return Integer.valueOf((int) ((new Date().getTime() / 1000) - reference + (SAVE_DELAY_SECONDS / 2) + 1));
	}

	private Date getReferenceDate()
	{
		final String query = "select {PK} from {DynamicProcessDefinition} where {code}=?code and {version}=?version";
		final Map<String, Object> params = ImmutableMap.<String, Object> of("code", SECOND_DEFINITION, "version",
				Integer.valueOf(2));

		final List<DynamicProcessDefinitionModel> result = flexibleSearchService.<DynamicProcessDefinitionModel> search(query,
				params).getResult();
		assertThat(result).isNotNull().hasSize(1);

		return result.get(0).getModifiedtime();
	}

	private void createTestData() throws TimeoutException, InterruptedException
	{
		createTestDefinitionsAndProcesses(FIRST_DEFINITION);
		final DynamicProcessDefinitionModel definition2 = createTestDefinitionsAndProcesses(SECOND_DEFINITION);
		definition2.setContent(definition2.getContent() + " ");
		modelService.save(definition2);
		modelService.remove(definition2);
	}

	private DynamicProcessDefinitionModel createTestDefinitionsAndProcesses(final String definitionName) throws TimeoutException,
			InterruptedException
	{
		final DynamicProcessDefinitionModel definition = modelService.create(DynamicProcessDefinitionModel.class);

		definition.setContent(getTestProcessDefinitionContent(definitionName));
		modelService.save(definition);
		Thread.sleep(SAVE_DELAY_SECONDS * 1000);

		markAsRunning(businessProcessService.createProcess(definitionName + "V0created", definitionName), definition);
		waitFor(businessProcessService.startProcess(definitionName + "V0finished", definitionName));

		definition.setContent(definition.getContent() + " ");
		modelService.save(definition);
		Thread.sleep(SAVE_DELAY_SECONDS * 1000);

		waitFor(businessProcessService.startProcess(definitionName + "V1finished", definitionName));

		definition.setContent(definition.getContent() + " ");
		modelService.save(definition);
		Thread.sleep(SAVE_DELAY_SECONDS * 1000);

		definition.setContent(definition.getContent() + " ");
		modelService.save(definition);
		Thread.sleep(SAVE_DELAY_SECONDS * 1000);

		waitFor(businessProcessService.startProcess(definitionName + "V3finished", definitionName));

		return definition;
	}

	private List<DynamicProcessDefinitionModel> useCleanupStrategyWith(final Integer versionThreshold, final Integer timeThreshold)
	{
		final CleanupDynamicProcessDefinitionsCronJobModel cronJob = modelService
				.create(CleanupDynamicProcessDefinitionsCronJobModel.class);
		cronJob.setVersionThreshold(versionThreshold);
		cronJob.setTimeThreshold(timeThreshold);
		final FlexibleSearchQuery query = cleanupStrategy.createFetchQuery(cronJob);
		final List<Object> queryResult = flexibleSearchService.search(query).getResult();
		return Lists.newLinkedList(Iterables.filter(queryResult, DynamicProcessDefinitionModel.class));
	}

	private Integer versionThreshold(final Integer threshold)
	{
		return threshold;
	}

	private Integer timeThreshold(final Integer threshold)
	{
		return threshold;
	}

	private static String getTestProcessDefinitionContent(final String definitionCode)
	{
		final StringBuilder result = new StringBuilder();

		result.append("<?xml version='1.0' encoding='utf-8'?>");
		result.append("<process xmlns='http://www.hybris.de/xsd/processdefinition' start='action0' name='").append(definitionCode)
				.append("'>");
		result.append("	<scriptAction id='action0'>");
		result.append("		<script type='javascript'>(function() { return 'itworks' })()</script>");
		result.append("		<transition name='itworks' to='success'/>");
		result.append("	</scriptAction>");
		result.append("	<end id='success' state='SUCCEEDED'>Everything was fine</end>");
		result.append("</process>");

		return result.toString();
	}

	private void waitFor(final BusinessProcessModel model) throws TimeoutException
	{
		final long maxWaitTime = System.currentTimeMillis() + 2 * 60 * 1000;
		while (model.getState() == ProcessState.RUNNING)
		{
			if (System.currentTimeMillis() > maxWaitTime)
			{
				throw new TimeoutException("Wait time exceeded for " + model.getCode());
			}
			modelService.refresh(model);
		}
		assertThat(model.getState()).isEqualTo(ProcessState.SUCCEEDED);
	}

	private void markAsRunning(final BusinessProcessModel process, final DynamicProcessDefinitionModel definition)
	{
		process.setState(ProcessState.RUNNING);
		process.setProcessDefinitionVersion(definition.getVersion().toString());
		modelService.save(process);
	}

	private Condition<List<?>> containsOnlyHistorical()
	{
		return new Condition<List<?>>("Must contains only historical definitions")
		{
			@Override
			public boolean matches(final List<?> definitions)
			{
				for (final Object d : definitions)
				{
					final DynamicProcessDefinitionModel definition = (DynamicProcessDefinitionModel) d;
					if (definition != null && Boolean.TRUE.equals(definition.getActive()))
					{
						return false;
					}
				}
				return true;
			}
		};
	}

	private Condition<List<?>> containsDefinition(final String code, final int version)
	{
		return new Condition<List<?>>("Must contains definition with code: " + code + " and version: " + version)
		{
			@Override
			public boolean matches(final List<?> definitions)
			{
				for (final Object d : definitions)
				{
					final DynamicProcessDefinitionModel definition = (DynamicProcessDefinitionModel) d;
					if (/* definition != null && */definition.getCode().equals(code)
							&& definition.getVersion().equals(Long.valueOf(version)))
					{
						return true;
					}
				}
				return false;
			}
		};
	}
}
