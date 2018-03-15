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
package de.hybris.platform.workflow.daos.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultWorkflowLinksDaoUnitTest
{
	private final DefaultWorkflowLinksDao dao = new DefaultWorkflowLinksDao();

	private static final String RELATIONNAME = WorkflowActionModel._WORKFLOWACTIONLINKRELATION;//"WorkflowActionLinkRelation";

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}

	@Test
	public void testFindWorkflowActionLinkRelationBySource()
	{

		final SearchResult result = Mockito.mock(SearchResult.class);
		Mockito.when(result.getResult()).thenReturn(null);

		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		Assert.assertNull(dao.findWorkflowActionLinkRelationBySource("sourceName"));
		Mockito.verify(flexibleSearchService).search(Mockito.eq("SELECT {pk} from {" + RELATIONNAME + "} where {source}=?desc"),
				Mockito.eq(Collections.singletonMap("desc", "sourceName")));
	}

	@Test
	public void testfindLinksByDecisionAndActionNullWorkflowNullDecision()
	{
		try
		{
			dao.findLinksByDecisionAndAction(null, null);
			Assert.fail("Should not be posssible to find links without workflow or decision");
		}
		catch (final IllegalArgumentException e)
		{
			//ok here
		}
		Mockito.verifyZeroInteractions(flexibleSearchService);

	}

	@Test
	public void testfindLinksByDecisionAndActionNullWorkflow()
	{

		final WorkflowDecisionModel decision = Mockito.mock(WorkflowDecisionModel.class);

		final SearchResult result = Mockito.mock(SearchResult.class);
		Mockito.when(result.getResult()).thenReturn(null);

		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		Assert.assertNull(dao.findLinksByDecisionAndAction(decision, null));
		Mockito.verify(flexibleSearchService).search(Mockito.eq("SELECT {pk} from {" + RELATIONNAME + "} where {source}=?desc"),
				Mockito.eq(Collections.singletonMap("desc", decision)));
	}

	@Test
	public void testfindLinksByDecisionAndActionNullDecision()
	{

		final WorkflowActionModel workflow = Mockito.mock(WorkflowActionModel.class);

		final SearchResult result = Mockito.mock(SearchResult.class);
		Mockito.when(result.getResult()).thenReturn(null);

		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);


		Assert.assertNull(dao.findLinksByDecisionAndAction(null, workflow));
		Mockito.verify(flexibleSearchService).search(Mockito.eq("SELECT {pk} from {" + RELATIONNAME + "} where {target}=?act"),
				Mockito.eq(Collections.singletonMap("act", workflow)));
	}

	@Test
	public void testfindLinksByDecisionAndAction()
	{
		final List<LinkModel> resultList = new ArrayList<LinkModel>(1);
		resultList.add(Mockito.mock(LinkModel.class));

		final WorkflowActionModel workflow = Mockito.mock(WorkflowActionModel.class);
		final WorkflowDecisionModel decision = Mockito.mock(WorkflowDecisionModel.class);

		final SearchResult result = Mockito.mock(SearchResult.class);
		Mockito.when(result.getResult()).thenReturn(resultList);

		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);
		final Map map = new HashMap();
		map.put("act", workflow);
		map.put("desc", decision);

		Assert.assertEquals(resultList, dao.findLinksByDecisionAndAction(decision, workflow));

		Mockito.verify(flexibleSearchService).search(
				Mockito.eq("SELECT {pk} from {" + RELATIONNAME + "} where {source}=?desc AND {target}=?act"), Mockito.eq(map));
	}

	@Test
	public void testfindLinksByDecisionAndActionTooLessResults()
	{
		final List<LinkModel> resultList = new ArrayList<LinkModel>(0);

		final WorkflowActionModel workflow = Mockito.mock(WorkflowActionModel.class);
		final WorkflowDecisionModel decision = Mockito.mock(WorkflowDecisionModel.class);

		final SearchResult result = Mockito.mock(SearchResult.class);
		Mockito.when(result.getResult()).thenReturn(resultList);

		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);
		final Map map = new HashMap();
		map.put("act", workflow);
		map.put("desc", decision);

		try
		{
			dao.findLinksByDecisionAndAction(decision, workflow);
			Assert.fail("Too less results ...");
		}
		catch (final UnknownIdentifierException e)
		{
			//
		}

		Mockito.verify(flexibleSearchService).search(
				Mockito.eq("SELECT {pk} from {" + RELATIONNAME + "} where {source}=?desc AND {target}=?act"), Mockito.eq(map));
	}

	@Test
	public void testfindLinksByDecisionAndActionTooMuchResults()
	{
		final List<LinkModel> resultList = new ArrayList<LinkModel>(2);
		resultList.add(Mockito.mock(LinkModel.class));
		resultList.add(Mockito.mock(LinkModel.class));

		final WorkflowActionModel workflow = Mockito.mock(WorkflowActionModel.class);
		final WorkflowDecisionModel decision = Mockito.mock(WorkflowDecisionModel.class);

		final SearchResult result = Mockito.mock(SearchResult.class);
		Mockito.when(result.getResult()).thenReturn(resultList);

		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);
		final Map map = new HashMap();
		map.put("act", workflow);
		map.put("desc", decision);

		try
		{
			dao.findLinksByDecisionAndAction(decision, workflow);
			Assert.fail("Too much results ...");
		}
		catch (final AmbiguousIdentifierException e)
		{
			// ok here
		}

		Mockito.verify(flexibleSearchService).search(
				Mockito.eq("SELECT {pk} from {" + RELATIONNAME + "} where {source}=?desc AND {target}=?act"), Mockito.eq(map));
	}
}
