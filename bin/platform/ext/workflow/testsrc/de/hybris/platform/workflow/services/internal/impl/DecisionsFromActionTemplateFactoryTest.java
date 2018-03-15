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
package de.hybris.platform.workflow.services.internal.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.workflow.daos.WorkflowLinksDao;
import de.hybris.platform.workflow.daos.WorkflowLinksTemplateDao;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DecisionsFromActionTemplateFactoryTest
{
	private final DecisionsFromActionTemplateFactory factory = new DecisionsFromActionTemplateFactory();

	@Mock
	private ModelService modelService;

	@Mock
	private WorkflowLinksDao workflowLinksDao;


	@Mock
	private WorkflowLinksTemplateDao workflowLinksTemplateDao;

	@Mock
	private DecisionFromDecisionTemplateFactory decisionFactory;

	@Mock
	private TypeService typeService;

	@Mock
	private I18NService i18nService;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		factory.setModelService(modelService);
		factory.setWorkflowLinksDao(workflowLinksDao);
		factory.setWorkflowLinksTemplateDao(workflowLinksTemplateDao);
		factory.setDecisionFactory(decisionFactory);
		factory.setTypeService(typeService);
		factory.setI18nService(i18nService);
	}

	@Test
	public void testEmptyDecisionsListNoMatchingAction()
	{
		final WorkflowModel workFlow = Mockito.mock(WorkflowModel.class);
		final WorkflowActionTemplateModel templateAction = Mockito.mock(WorkflowActionTemplateModel.class);

		try
		{
			factory.create(workFlow, templateAction);
			Assert.fail("Should have a NPE when no action found, by matching WorkflowActionTemplateModel??");
		}
		catch (final NullPointerException npe)
		{
			//ok here
		}
	}

	@Test
	public void testEmptyDecisionsListMatchingActionTemplate()
	{
		final WorkflowActionTemplateModel matchingTemplateAction = Mockito.mock(WorkflowActionTemplateModel.class);

		final WorkflowActionModel workFlowActionOne = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(workFlowActionOne.getTemplate()).thenReturn(new WorkflowActionTemplateModel());
		final WorkflowActionModel workFlowActionTwo = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(workFlowActionTwo.getTemplate()).thenReturn(matchingTemplateAction);

		final WorkflowModel workFlow = Mockito.mock(WorkflowModel.class);
		Mockito.when(workFlow.getActions()).thenReturn(Arrays.asList(workFlowActionOne, workFlowActionTwo));

		Assert.assertEquals(Collections.EMPTY_LIST, factory.create(workFlow, matchingTemplateAction));

	}

	@Test
	public void testDecisionsListWithNoMatchingAction()
	{
		final WorkflowModel workFlow = Mockito.mock(WorkflowModel.class);

		final WorkflowDecisionModel decisionOne = Mockito.mock(WorkflowDecisionModel.class);
		final WorkflowDecisionModel decisionTwo = Mockito.mock(WorkflowDecisionModel.class);

		final WorkflowDecisionTemplateModel decisionTemplateOne = Mockito.mock(WorkflowDecisionTemplateModel.class);
		Mockito.when(decisionTemplateOne.getCode()).thenReturn("someCodeOne");
		Mockito.when(decisionTemplateOne.getActionTemplate()).thenReturn(new WorkflowActionTemplateModel());
		Mockito.when(decisionTemplateOne.getName()).thenReturn("nameOne");
		Mockito.when(decisionTemplateOne.getDescription()).thenReturn("descriptionOne");

		final WorkflowDecisionTemplateModel decisionTemplateTwo = Mockito.mock(WorkflowDecisionTemplateModel.class);
		Mockito.when(decisionTemplateTwo.getCode()).thenReturn("someCodeTwo");
		Mockito.when(decisionTemplateTwo.getActionTemplate()).thenReturn(null);
		Mockito.when(decisionTemplateTwo.getName()).thenReturn("nameTwo");
		Mockito.when(decisionTemplateTwo.getDescription()).thenReturn("descriptionTwo");

		final WorkflowActionTemplateModel templateAction = Mockito.mock(WorkflowActionTemplateModel.class);
		Mockito.when(templateAction.getDecisionTemplates()).thenReturn(Arrays.asList(decisionTemplateOne, decisionTemplateTwo));

		Mockito.when(decisionFactory.create(workFlow, decisionTemplateOne)).thenReturn(decisionOne);
		Mockito.when(decisionFactory.create(workFlow, decisionTemplateTwo)).thenReturn(decisionTwo);
		try
		{
			factory.create(workFlow, templateAction);
			Assert.fail("Should have a NPE when no action found, by matching WorkflowActionTemplateModel??");
		}
		catch (final NullPointerException npe)
		{
			//ok here
		}
	}


	@Test
	public void testDecisionsListWithMatchingActionTemplate()
	{

		final WorkflowActionModel workFlowActionOne = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(workFlowActionOne.getTemplate()).thenReturn(new WorkflowActionTemplateModel());
		final WorkflowActionModel workFlowActionTwo = Mockito.mock(WorkflowActionModel.class);
		//Mockito.when(workFlowActionTwo.getTemplate()).thenReturn(matchingTemplateAction);

		final WorkflowModel workFlow = Mockito.mock(WorkflowModel.class);
		Mockito.when(workFlow.getActions()).thenReturn(Arrays.asList(workFlowActionOne, workFlowActionTwo));


		final WorkflowDecisionModel decisionOne = Mockito.mock(WorkflowDecisionModel.class);
		final WorkflowDecisionModel decisionTwo = Mockito.mock(WorkflowDecisionModel.class);

		final WorkflowDecisionTemplateModel decisionTemplateOne = Mockito.mock(WorkflowDecisionTemplateModel.class);
		Mockito.when(decisionTemplateOne.getCode()).thenReturn("someCodeOne");
		Mockito.when(decisionTemplateOne.getActionTemplate()).thenReturn(new WorkflowActionTemplateModel());
		Mockito.when(decisionTemplateOne.getName()).thenReturn("nameOne");
		Mockito.when(decisionTemplateOne.getDescription()).thenReturn("descriptionOne");

		final WorkflowDecisionTemplateModel decisionTemplateTwo = Mockito.mock(WorkflowDecisionTemplateModel.class);
		Mockito.when(decisionTemplateTwo.getCode()).thenReturn("someCodeTwo");
		Mockito.when(decisionTemplateTwo.getActionTemplate()).thenReturn(null);
		Mockito.when(decisionTemplateTwo.getName()).thenReturn("nameTwo");
		Mockito.when(decisionTemplateTwo.getDescription()).thenReturn("descriptionTwo");

		final WorkflowActionTemplateModel matchingTemplateAction = Mockito.mock(WorkflowActionTemplateModel.class);
		Mockito.when(matchingTemplateAction.getDecisionTemplates()).thenReturn(
				Arrays.asList(decisionTemplateOne, decisionTemplateTwo));
		Mockito.when(workFlowActionTwo.getTemplate()).thenReturn(matchingTemplateAction);

		Mockito.when(decisionFactory.create(workFlow, decisionTemplateOne)).thenReturn(decisionOne);
		Mockito.when(decisionFactory.create(workFlow, decisionTemplateTwo)).thenReturn(decisionTwo);

		final ComposedTypeModel composedTypeModel = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForCode(AbstractWorkflowDecisionModel._TYPECODE)).thenReturn(composedTypeModel);
		when(composedTypeModel.getDeclaredattributedescriptors()).thenReturn(new ArrayList<AttributeDescriptorModel>());

		Assert.assertEquals(Arrays.asList(decisionOne, decisionTwo), factory.create(workFlow, matchingTemplateAction));

	}

	@Test
	public void testDecisionsListWithMatchingActionTemplateAndDecisionAction()
	{
		final WorkflowActionTemplateModel decisionMatchingActionTemplate = new WorkflowActionTemplateModel();

		final WorkflowActionModel workFlowActionOne = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(workFlowActionOne.getTemplate()).thenReturn(decisionMatchingActionTemplate);
		final WorkflowActionModel workFlowActionTwo = Mockito.mock(WorkflowActionModel.class);

		final WorkflowModel workFlow = Mockito.mock(WorkflowModel.class);
		Mockito.when(workFlow.getActions()).thenReturn(Arrays.asList(workFlowActionOne, workFlowActionTwo));


		final WorkflowDecisionModel decisionOne = Mockito.mock(WorkflowDecisionModel.class);
		final WorkflowDecisionModel decisionTwo = Mockito.mock(WorkflowDecisionModel.class);

		final WorkflowDecisionTemplateModel decisionTemplateOne = Mockito.mock(WorkflowDecisionTemplateModel.class);
		Mockito.when(decisionTemplateOne.getCode()).thenReturn("someCodeOne");
		Mockito.when(decisionTemplateOne.getActionTemplate()).thenReturn(new WorkflowActionTemplateModel());
		Mockito.when(decisionTemplateOne.getName()).thenReturn("nameOne");
		Mockito.when(decisionTemplateOne.getDescription()).thenReturn("descriptionOne");

		final WorkflowDecisionTemplateModel decisionTemplateTwo = Mockito.mock(WorkflowDecisionTemplateModel.class);
		Mockito.when(decisionTemplateTwo.getCode()).thenReturn("someCodeTwo");
		Mockito.when(decisionTemplateTwo.getActionTemplate()).thenReturn(decisionMatchingActionTemplate);
		Mockito.when(decisionTemplateTwo.getName()).thenReturn("nameTwo");
		Mockito.when(decisionTemplateTwo.getDescription()).thenReturn("descriptionTwo");

		final WorkflowActionTemplateModel matchingTemplateAction = Mockito.mock(WorkflowActionTemplateModel.class);
		Mockito.when(matchingTemplateAction.getDecisionTemplates()).thenReturn(
				Arrays.asList(decisionTemplateOne, decisionTemplateTwo));
		Mockito.when(workFlowActionTwo.getTemplate()).thenReturn(matchingTemplateAction);

		Mockito.when(decisionFactory.create(workFlow, decisionTemplateOne)).thenReturn(decisionOne);
		Mockito.when(decisionFactory.create(workFlow, decisionTemplateTwo)).thenReturn(decisionTwo);

		final ComposedTypeModel composedTypeModel = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForCode(AbstractWorkflowDecisionModel._TYPECODE)).thenReturn(composedTypeModel);
		when(composedTypeModel.getDeclaredattributedescriptors()).thenReturn(new ArrayList<AttributeDescriptorModel>());

		Assert.assertEquals(Arrays.asList(decisionOne, decisionTwo), factory.create(workFlow, matchingTemplateAction));

	}


	@Test
	public void testDecisionsListWithMatchingActionTemplateCopyLinks() throws Exception
	{


		final WorkflowActionModel workFlowAction = Mockito.mock(WorkflowActionModel.class);
		//Mockito.when(workFlowActionTwo.getTemplate()).thenReturn(matchingTemplateAction);

		final WorkflowModel workFlow = Mockito.mock(WorkflowModel.class);
		Mockito.when(workFlow.getActions()).thenReturn(Arrays.asList(workFlowAction));

		final WorkflowDecisionModel decisionOne = Mockito.mock(WorkflowDecisionModel.class);

		final WorkflowDecisionTemplateModel decisionTemplateOne = Mockito.mock(WorkflowDecisionTemplateModel.class);
		Mockito.when(decisionTemplateOne.getCode()).thenReturn("someCodeOne");
		Mockito.when(decisionTemplateOne.getActionTemplate()).thenReturn(new WorkflowActionTemplateModel());
		Mockito.when(decisionTemplateOne.getName()).thenReturn("nameOne");
		Mockito.when(decisionTemplateOne.getDescription()).thenReturn("descriptionOne");

		final WorkflowActionTemplateModel matchingTemplateAction = Mockito.mock(WorkflowActionTemplateModel.class);
		Mockito.when(matchingTemplateAction.getDecisionTemplates()).thenReturn(Arrays.asList(decisionTemplateOne));
		Mockito.when(workFlowAction.getTemplate()).thenReturn(matchingTemplateAction);

		Mockito.when(decisionFactory.create(workFlow, decisionTemplateOne)).thenReturn(decisionOne);


		final LinkModel linkTemplateOne = Mockito.mock(LinkModel.class);
		Mockito.when(linkTemplateOne.getTarget()).thenReturn(new WorkflowActionTemplateModel());
		final LinkModel linkTemplateTwo = Mockito.mock(LinkModel.class);
		Mockito.when(linkTemplateTwo.getTarget()).thenReturn(matchingTemplateAction);//here it is a link matching action template 

		Mockito.when(workflowLinksTemplateDao.findLinksByDecisionAndAction(decisionTemplateOne, null)).thenReturn(
				Arrays.asList(linkTemplateOne, linkTemplateTwo));

		final LinkModel matchingTemplateLink = Mockito.mock(LinkModel.class);

		Mockito.when(workflowLinksDao.findLinksByDecisionAndAction(decisionOne, workFlowAction)).thenReturn(
				Collections.singleton(matchingTemplateLink));

		//record link setAttributeForLink 
		final Link targetLink = Mockito.mock(Link.class);//
		final Link sourceLink = Mockito.mock(Link.class);//

		Mockito.when(modelService.getSource(linkTemplateTwo)).thenReturn(sourceLink);//source
		Mockito.when(modelService.getSource(matchingTemplateLink)).thenReturn(targetLink);
		Mockito.when(sourceLink.getAttribute("andConnectionTemplate")).thenReturn("fencyTemplateFlag");

		final ComposedTypeModel composedTypeModel = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForCode(AbstractWorkflowDecisionModel._TYPECODE)).thenReturn(composedTypeModel);
		when(composedTypeModel.getDeclaredattributedescriptors()).thenReturn(new ArrayList<AttributeDescriptorModel>());

		//method
		Assert.assertEquals(Arrays.asList(decisionOne), factory.create(workFlow, matchingTemplateAction));

		//verify link setAttributeForLink 	

		Mockito.verify(sourceLink).getAttribute("andConnectionTemplate");
		Mockito.verify(targetLink).setAttribute("andconnection", "fencyTemplateFlag");
	}
}
