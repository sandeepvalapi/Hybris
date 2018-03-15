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
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.daos.WorkflowTemplateDao;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultWorkflowDaoUnitTest
{
	private final DefaultWorkflowDao dao = new DefaultWorkflowDao(null);

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private UserService userService;

	@Mock
	private WorkflowTemplateDao workflowTemplateDao;

	private final List<WorkflowTemplateModel> resultList = new ArrayList<WorkflowTemplateModel>(1);

	@Mock
	private UserModel userModel;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
		dao.setUserService(userService);
		dao.setWorkflowTemplateDao(workflowTemplateDao);


		resultList.add(Mockito.mock(WorkflowTemplateModel.class));
		final SearchResult result = Mockito.mock(SearchResult.class);
		Mockito.when(result.getResult()).thenReturn(resultList);
		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		Mockito.when(userService.getCurrentUser()).thenReturn(userModel);

	}

	@Test
	public void testAllAdhocWorkflowsNoAdhocTemplate()
	{

		Mockito.when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Collections.EMPTY_LIST);

		final Date dateFrom = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
		final Date dateTo = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Assert.assertEquals(Collections.EMPTY_LIST, dao.findAllAdhocWorkflows(dateFrom, dateTo));

		Mockito.verifyZeroInteractions(flexibleSearchService);

	}

	@Test
	public void testAllAdhocWorkflowsNotNullDates()
	{
		final WorkflowTemplateModel adhocTemplate = Mockito.mock(WorkflowTemplateModel.class);

		Mockito.when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Collections.singletonList(adhocTemplate));

		final Date dateFrom = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
		final Date dateTo = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Assert.assertEquals(resultList, dao.findAllAdhocWorkflows(dateFrom, dateTo));

		final Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("adhocWorkflowTemplate", adhocTemplate);
		params.put("dateTo", dateTo);
		params.put("dateFrom", dateFrom);
		params.put("user", userModel);

		Mockito
				.verify(flexibleSearchService)
				.search(
						Mockito
								.eq("SELECT {pk} FROM {Workflow} WHERE {owner} = ?user AND {job} = ?adhocWorkflowTemplate AND {modifiedtime} >= ?dateFrom AND {modifiedtime} <= ?dateTo"),
						Mockito.eq(params));

	}


	@Test
	public void testAllAdhocWorkflowsNotNullToDate()
	{
		final WorkflowTemplateModel adhocTemplate = Mockito.mock(WorkflowTemplateModel.class);

		Mockito.when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Collections.singletonList(adhocTemplate));

		//final Date dateFrom = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
		final Date dateTo = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Assert.assertEquals(resultList, dao.findAllAdhocWorkflows(null, dateTo));

		final Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("adhocWorkflowTemplate", adhocTemplate);
		params.put("user", userModel);
		params.put("dateTo", dateTo);

		Mockito.verify(flexibleSearchService).search(
				Mockito.eq("SELECT {pk} FROM {Workflow} WHERE {owner} = ?user AND {job} = ?adhocWorkflowTemplate AND {modifiedtime} <= ?dateTo"),
				Mockito.eq(params));

	}

	@Test
	public void testAllAdhocWorkflowsNotNullFromDate()
	{
		final WorkflowTemplateModel adhocTemplate = Mockito.mock(WorkflowTemplateModel.class);

		Mockito.when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Collections.singletonList(adhocTemplate));

		final Date dateFrom = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
		//final Date dateTo = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Assert.assertEquals(resultList, dao.findAllAdhocWorkflows(dateFrom, null));

		final Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("adhocWorkflowTemplate", adhocTemplate);
		params.put("user", userModel);
		params.put("dateFrom", dateFrom);

		Mockito.verify(flexibleSearchService).search(
				Mockito.eq("SELECT {pk} FROM {Workflow} WHERE {owner} = ?user AND {job} = ?adhocWorkflowTemplate AND {modifiedtime} >= ?dateFrom"),
				Mockito.eq(params));

	}

	/**
	 * Note that dateFrom in in future and dateTo in the past
	 */
	@Test
	public void testAllAdhocWorkflowsIncorrectPerdiodLimits()
	{
		final WorkflowTemplateModel adhocTemplate = Mockito.mock(WorkflowTemplateModel.class);

		Mockito.when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Collections.singletonList(adhocTemplate));

		final Date dateTo = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
		final Date dateFrom = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Assert.assertEquals(resultList, dao.findAllAdhocWorkflows(dateFrom, dateTo));

		final Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("adhocWorkflowTemplate", adhocTemplate);
		params.put("user", userModel);

		Mockito.verify(flexibleSearchService).search(
				Mockito.eq("SELECT {pk} FROM {Workflow} WHERE {owner} = ?user AND {job} = ?adhocWorkflowTemplate"),
				Mockito.eq(params));

	}

}
