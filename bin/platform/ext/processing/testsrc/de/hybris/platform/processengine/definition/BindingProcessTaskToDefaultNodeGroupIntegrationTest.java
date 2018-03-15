/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.processengine.definition;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


@IntegrationTest
public class BindingProcessTaskToDefaultNodeGroupIntegrationTest extends ServicelayerTransactionalBaseTest
{
	private final ProcessDefinitionId processWithDefaultGroup = new ProcessDefinitionId("nodeGroupBindingWithDefaultGroup");
	private final ProcessDefinitionId processWithoutDefaultGroup = new ProcessDefinitionId("nodeGroupBindingWithoutDefaultGroup");

	@Resource
	ProcessDefinitionFactory processDefinitionFactory;

	@Resource
	FlexibleSearchService flexibleSearchService;

	@Before
	public void setUp() throws IOException
	{
		processDefinitionFactory.add(new ClassPathResource("/processengine/test/nodeGroupBindingWithDefaultGroup.xml"));
		processDefinitionFactory.add(new ClassPathResource("/processengine/test/nodeGroupBindingWithoutDefaultGroup.xml"));
	}

	@After
	public void tearDown()
	{
		processDefinitionFactory.remove(processWithDefaultGroup);
		processDefinitionFactory.remove(processWithoutDefaultGroup);
	}

	@Test
	public void shouldUseDefaultGroupForActionWhenActionSpecificGroupOrNodeAreNotDefined() throws IOException
	{
		final Node node = givenProcessNode(processWithDefaultGroup, "actionWithoutClusterNodeOrGrouDefined");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isEqualTo("defaultGroup");
		assertThat(task.getNodeId()).isNull();
	}

	@Test
	public void shouldNotUseDefaultGroupForActionWhenOnlyActionSpecificNodeIsDefined() throws IOException
	{
		final Node node = givenProcessNode(processWithDefaultGroup, "actionWithClusterGroupDefined");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNotNull().isEqualTo("actionGroup");
		assertThat(task.getNodeId()).isNull();
	}

	@Test
	public void shouldUseNodeSpecificGroupForActionWhenOnlyActionSpecificGroupIsDefined() throws IOException
	{
		final Node node = givenProcessNode(processWithDefaultGroup, "actionWithClusterNodeDefined");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNull();
		assertThat(task.getNodeId()).isNotNull().isEqualTo(666);
	}

	@Test
	public void shouldUseNodeSpecificGroupForActionWhenActionSpecificGroupAndNodeAreDefined() throws IOException
	{
		final Node node = givenProcessNode(processWithDefaultGroup, "actionWithClusterGroupAndNodeDefined");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNull();
		assertThat(task.getNodeId()).isNotNull().isEqualTo(666);
	}

	@Test
	public void shouldUseDefaultGroupForTheWaitNode() throws IOException
	{
		final Node node = givenProcessNode(processWithDefaultGroup, "wait");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isEqualTo("defaultGroup");
		assertThat(task.getNodeId()).isNull();
	}

	@Test
	public void shouldUseDefaultGroupForTheNotifyNode() throws IOException
	{
		final Node node = givenProcessNode(processWithoutDefaultGroup, "notify");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithoutDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNull();
		assertThat(task.getNodeId()).isNull();
	}

	@Test
	public void shouldNotSetClusterGroupAndNodeForActionWhenThereIsNoDefaultGroupAndSpecificClusterSetingsAreNotDefined()
			throws IOException
	{
		final Node node = givenProcessNode(processWithoutDefaultGroup, "actionWithoutClusterNodeOrGrouDefined");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithoutDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNull();
		assertThat(task.getNodeId()).isNull();
	}

	@Test
	public void shouldSetClusterGroupWhenThereIsNoDefaultGroupAndSpecificClusterGroupIsDefined() throws IOException
	{
		final Node node = givenProcessNode(processWithoutDefaultGroup, "actionWithClusterGroupDefined");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithoutDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNotNull().isEqualTo("actionGroup");
		assertThat(task.getNodeId()).isNull();
	}

	@Test
	public void shouldSetClusterNodeWhenThereIsNoDefaultGroupAndSpecificClusterNodeIsDefined() throws IOException
	{
		final Node node = givenProcessNode(processWithoutDefaultGroup, "actionWithClusterNodeDefined");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithoutDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNull();
		assertThat(task.getNodeId()).isNotNull().isEqualTo(666);
	}

	@Test
	public void shouldSetClusterNodeWhenThereIsNoDefaultGroupAndSpecificClusterNodeAndGroupAreDefined() throws IOException
	{
		final Node node = givenProcessNode(processWithoutDefaultGroup, "actionWithClusterGroupAndNodeDefined");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithoutDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNull();
		assertThat(task.getNodeId()).isNotNull().isEqualTo(666);
	}

	@Test
	public void shouldNotSetClusterGroupAndNodeForWaitNodeWhenThereIsNoDefaultGroup() throws IOException
	{
		final Node node = givenProcessNode(processWithoutDefaultGroup, "wait");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithoutDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNull();
		assertThat(task.getNodeId()).isNull();
	}

	@Test
	public void shouldNotSetClusterGroupAndNodeForNotifyNodeWhenThereIsNoDefaultGroup() throws IOException
	{
		final Node node = givenProcessNode(processWithoutDefaultGroup, "notify");

		final ProcessTaskModel task = triggerTaskForGivenNode(processWithoutDefaultGroup, node);

		assertThat(task).isNotNull();
		assertThat(task.getNodeGroup()).isNull();
		assertThat(task.getNodeId()).isNull();
	}

	private Node givenProcessNode(final ProcessDefinitionId definitionId, final String nodeId)
	{
		final ProcessDefinition definition = processDefinitionFactory.getProcessDefinition(definitionId);
		assertThat(definition).isNotNull();

		final Node node = definition.retrieve(nodeId);
		assertThat(node).isNotNull();

		return node;
	}

	private ProcessTaskModel triggerTaskForGivenNode(final ProcessDefinitionId definitionId, final Node node)
	{
		node.trigger(giveMeFakeProcess(definitionId));

		return getTaskForNode(node);
	}

	private ProcessTaskModel getTaskForNode(final Node node)
	{
		final List<ProcessTaskModel> processes = flexibleSearchService.<ProcessTaskModel> search(
				"select {PK} from {" + ProcessTaskModel._TYPECODE + "} where {" + ProcessTaskModel.ACTION + "}=?action",
				Collections.singletonMap("action", node.getId())).getResult();

		assertThat(processes).isNotNull().hasSize(1).doesNotContainNull();

		return processes.get(0);
	}

	private BusinessProcessModel giveMeFakeProcess(final ProcessDefinitionId definitionId)
	{
		final BusinessProcessModel result = new BusinessProcessModel();

		result.setCode("FakeProcess");
		result.setProcessDefinitionName(definitionId.getName());

		return result;
	}

}
