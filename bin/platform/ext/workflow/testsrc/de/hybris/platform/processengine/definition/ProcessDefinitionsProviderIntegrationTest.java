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
package de.hybris.platform.processengine.definition;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class ProcessDefinitionsProviderIntegrationTest extends ServicelayerBaseTest
{
	private static final String TEST_DEFINITION_CODE = "TEST_DEFINITION";
	private static final ProcessDefinitionId TEST_ACTIVE_ID = new ProcessDefinitionId(TEST_DEFINITION_CODE);
	private static final String INITIAL_CONTET;
	private static final String UPDATED_CONTENT;

	@Resource
	private ProcessDefinitionsProvider processDefinitionsProvider;

	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ProcessDefinitionsCache processDefinitionsCache;

	@After
	public void tearDown()
	{
		processDefinitionsCache.clear();
	}

	@Test
	public void shouldNotFindLatestDefinitionIdWhenDynamicDefinitionDoesntExist()
	{
		assertThat(processDefinitionsProvider.getLatestDefinitionIdFor(TEST_ACTIVE_ID)).isNull();
	}

	@Test
	public void shouldNotFindLatestDefinitionWhenDynamicDefinitionDoesntExist()
	{
		assertThat(processDefinitionsProvider.getDefinition(TEST_ACTIVE_ID)).isNull();
	}

	@Test
	public void shouldNotFindHistricalDefinitionWhenDynamicDefinitionDoesntExist()
	{
		final ProcessDefinitionId historicalId = new ProcessDefinitionId(TEST_DEFINITION_CODE, "not existing version");
		assertThat(processDefinitionsProvider.getDefinition(historicalId)).isNull();
	}

	@Test
	public void shouldFindLatestDefinitionId()
	{
		final DynamicProcessDefinitionModel dynamicDefinition = givenProcessDefinition();

		final ProcessDefinitionId latestId = processDefinitionsProvider.getLatestDefinitionIdFor(TEST_ACTIVE_ID);

		assertThat(latestId).isNotNull();
		assertThat(latestId.getName()).isEqualTo(TEST_DEFINITION_CODE);
		assertThat(latestId.getVersion()).isEqualTo(dynamicDefinition.getVersion().toString());
	}

	@Test
	public void shouldFindLatestDefinitionById()
	{
		final DynamicProcessDefinitionModel dynamicDefinition = givenProcessDefinition();

		final ProcessDefinitionId latestId = processDefinitionsProvider.getLatestDefinitionIdFor(TEST_ACTIVE_ID);
		final ProcessDefinition definition = processDefinitionsProvider.getDefinition(latestId);

		assertThat(definition).isNotNull();
		assertThat(definition.getName()).isEqualTo(TEST_DEFINITION_CODE);
		assertThat(definition.getVersion()).isEqualTo(dynamicDefinition.getVersion().toString());
	}

	@Test
	public void shouldFindLatestDefinitionIdForDynamicDefinitionWithHistoricalDefinition()
	{
		final DynamicProcessDefinitionModel dynamicDefinition = givenProcessDefinitionWithHistoricalDefinition();

		final ProcessDefinitionId latestId = processDefinitionsProvider.getLatestDefinitionIdFor(TEST_ACTIVE_ID);

		assertThat(latestId).isNotNull();
		assertThat(latestId.getName()).isEqualTo(TEST_DEFINITION_CODE);
		assertThat(latestId.getVersion()).isEqualTo(dynamicDefinition.getVersion().toString());
	}

	@Test
	public void shouldFindHistoricalDefinition()
	{
		final DynamicProcessDefinitionModel historicalDefinition = givenHistoricalProcessDefinition();
		final ProcessDefinitionId historicalId = new ProcessDefinitionId(historicalDefinition.getCode(), historicalDefinition
				.getVersion().toString());

		final ProcessDefinition historicalProcessDefinition = processDefinitionsProvider.getDefinition(historicalId);

		assertThat(historicalProcessDefinition).isNotNull();
		assertThat(historicalProcessDefinition.getName()).isEqualTo(TEST_DEFINITION_CODE);
		assertThat(historicalProcessDefinition.getVersion()).isEqualTo(historicalDefinition.getVersion().toString());
	}

	private DynamicProcessDefinitionModel givenHistoricalProcessDefinition()
	{
		return (DynamicProcessDefinitionModel) findPredecessorFor(givenProcessDefinitionWithHistoricalDefinition());
	}

	private DynamicProcessDefinitionModel givenProcessDefinitionWithHistoricalDefinition()
	{
		final DynamicProcessDefinitionModel result = givenProcessDefinition();
		result.setContent(UPDATED_CONTENT);
		modelService.save(result);
		return result;
	}

	private AbstractDynamicContentModel findPredecessorFor(final DynamicProcessDefinitionModel definition)
	{
		final SearchResult<DynamicProcessDefinitionModel> queryResult = flexibleSearchService
				.<DynamicProcessDefinitionModel> search("select {PK} from {" + DynamicProcessDefinitionModel._TYPECODE
						+ "} where {code}=?code and {version}<?version order by {version} desc",
						ImmutableMap.<String, Object> of("code", definition.getCode(), "version", definition.getVersion()));

		final List<DynamicProcessDefinitionModel> result = queryResult.getResult();

		return result.isEmpty() ? null : result.get(0);
	}

	private DynamicProcessDefinitionModel givenProcessDefinition()
	{
		final DynamicProcessDefinitionModel result = modelService.create(DynamicProcessDefinitionModel.class);
		result.setContent(INITIAL_CONTET);
		result.setCode(TEST_DEFINITION_CODE);
		modelService.save(result);
		return result;
	}

	static
	{
		INITIAL_CONTET = String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + //
				"<process xmlns=\"http://www.hybris.de/xsd/processdefinition\" start=\"success\" name=\"%s\">\n" + //
				"  <end id=\"success\" state=\"SUCCEEDED\">Everything was fine</end>\n" + //
				"</process>", TEST_DEFINITION_CODE);

		UPDATED_CONTENT = String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + //
				"<process xmlns=\"http://www.hybris.de/xsd/processdefinition\" start=\"UPDATED_success\" name=\"%s\">\n" + //
				"  <end id=\"UPDATED_success\" state=\"SUCCEEDED\">Everything was fine</end>\n" + //
				"</process>", TEST_DEFINITION_CODE);
	}
}
