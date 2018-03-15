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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.daos.WorkflowDao;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.joda.time.DateMidnight;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integrational tests for {@link WorkflowDao}
 */
@IntegrationTest
public class DefaultWorkflowDaoTest extends ServicelayerTransactionalTest
{
	private static final String WORKFLOW_TEST_USER = "workflowTestUser";
	private static final int DEFAULT_PAGE_SIZE = 8;
	private static final int DEFAULT_START_INDEX = 0;
	private static final String WORKFLOW_1_CODE = "workflow1";
	private static final String WORKFLOW_2_CODE = "workflow2";
	private static final String WORKFLOW_3_CODE = "workflow3";
	private static final String WORKFLOW_TEMPLATE_CODE = "SampleWorkflowPredecessor";
	//dates
	protected final DateMidnight today = new DateMidnight();
	protected final DateMidnight yesterday = today.minusDays(1);
	protected final DateMidnight tomorrow = today.plusDays(1);
	protected final DateMidnight oneWeekBeforeNow = today.minusDays(7);
	protected final DateMidnight oneWeekAfrerNow = today.plusDays(7);

	protected WorkflowModel adhocWorkflow = null;


	@Resource
	private WorkflowDao defaultWorkflowDao;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private UserService userService;

	@Resource
	private ModelService modelService;

	@Resource(name = "newestWorkflowService")
	private WorkflowService workflowService;

	@Resource
	private WorkflowTemplateService workflowTemplateService;

	@Resource
	private UserGroupDao userGroupDao;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
		importCsv("/workflow/testdata/workflow_test_data.csv", "windows-1252");
		addAnotherUserAndAssignAllTypesOfWorkflows();
		addActionToAdhocWorkflow();
	}

	private void addActionToAdhocWorkflow()
	{
		final List<WorkflowModel> workflowList = defaultWorkflowDao.findWorkflowsByCode(WORKFLOW_3_CODE);
		assertThat(workflowList).hasSize(1);

		adhocWorkflow = workflowList.get(0);

		//INSERT_UPDATE WorkflowActionTemplate;code[unique=true];name[lang=de];name[lang=en];description[lang=de];description[lang=en];principalAssigned(uid);workflow(code)[unique=true];sendEmail;emailAddress;predecessors(code);rendererTemplate(code)
		//;SampleActionPre1;Kategorisierung;Categorization;Bitte ordnen Sie das angehangte Produkt in den Kategoriebaum ein.;Please assign the attached product to the category structure.;productmanager_wf1;SampleWorkflowPredecessor;false;wf1@hybris.de;;
		final WorkflowActionTemplateModel adhocActionTemplate = modelService.create(WorkflowActionTemplateModel.class);
		adhocActionTemplate.setPrincipalAssigned(userService.getUserForUID("productmanager_wf1"));
		adhocActionTemplate.setWorkflow(workflowTemplateService.getAdhocWorkflowTemplate());
		modelService.save(adhocActionTemplate);

		final WorkflowActionTemplateModel adhocActionTemplate2 = modelService.create(WorkflowActionTemplateModel.class);
		adhocActionTemplate2.setPrincipalAssigned(userService.getUserForUID("productmanager_wf1"));
		adhocActionTemplate2.setWorkflow(workflowTemplateService.getAdhocWorkflowTemplate());
		modelService.save(adhocActionTemplate2);

		final WorkflowActionModel actionStart = modelService.create(WorkflowActionModel.class);
		actionStart.setWorkflow(adhocWorkflow);
		actionStart.setActionType(WorkflowActionType.START);
		actionStart.setTemplate(adhocActionTemplate);
		actionStart.setStatus(WorkflowActionStatus.IN_PROGRESS);
		actionStart.setPrincipalAssigned(userService.getUserForUID(WORKFLOW_TEST_USER));
		modelService.save(actionStart);

		final WorkflowActionModel actionNormal1 = modelService.create(WorkflowActionModel.class);
		actionNormal1.setWorkflow(adhocWorkflow);
		actionNormal1.setActionType(WorkflowActionType.NORMAL);
		actionNormal1.setTemplate(adhocActionTemplate);
		actionNormal1.setStatus(WorkflowActionStatus.IN_PROGRESS);
		actionNormal1.setPrincipalAssigned(userService.getUserForUID(WORKFLOW_TEST_USER));
		modelService.save(actionNormal1);

		final WorkflowActionModel actionNormal2 = modelService.create(WorkflowActionModel.class);
		actionNormal2.setWorkflow(adhocWorkflow);
		actionNormal2.setActionType(WorkflowActionType.NORMAL);
		actionNormal2.setTemplate(adhocActionTemplate);
		actionNormal2.setStatus(WorkflowActionStatus.IN_PROGRESS);
		actionNormal2.setPrincipalAssigned(userService.getUserForUID(WORKFLOW_TEST_USER));
		modelService.save(actionNormal2);

		final WorkflowActionModel actionEnd = modelService.create(WorkflowActionModel.class);
		actionEnd.setWorkflow(adhocWorkflow);
		actionEnd.setActionType(WorkflowActionType.END);
		actionEnd.setTemplate(adhocActionTemplate);
		actionEnd.setStatus(WorkflowActionStatus.IN_PROGRESS);
		actionEnd.setPrincipalAssigned(userService.getUserForUID(WORKFLOW_TEST_USER));
		modelService.save(actionEnd);
	}

	private void addAnotherUserAndAssignAllTypesOfWorkflows()
	{
		final UserModel anotherWorkflowTestUser = modelService.create(UserModel.class);
		anotherWorkflowTestUser.setUid("anotherWorkflowTestUser");
		final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();

		groups.add(userGroupDao.findUserGroupByUid("productmanagergroup"));
		groups.add(userGroupDao.findUserGroupByUid("employeegroup"));
		anotherWorkflowTestUser.setGroups(groups);
		modelService.save(anotherWorkflowTestUser);

		final WorkflowTemplateModel workflowTemplate = workflowTemplateService.getWorkflowTemplateForCode(WORKFLOW_TEMPLATE_CODE);
		final WorkflowModel workflowTerminated = workflowService.createWorkflow(workflowTemplate, anotherWorkflowTestUser);
		final WorkflowModel workflowPlanned = workflowService.createWorkflow(workflowTemplate, anotherWorkflowTestUser);
		final WorkflowModel workflowRunning = workflowService.createWorkflow(workflowTemplate, anotherWorkflowTestUser);
		final WorkflowModel workflowFinished = workflowService.createWorkflow(workflowTemplate, anotherWorkflowTestUser);

		WorkflowActionModel action = workflowTerminated.getActions().get(0);
		action.setStatus(WorkflowActionStatus.TERMINATED);
		modelService.save(action);

		for (final WorkflowActionModel act : workflowPlanned.getActions())
		{
			act.setStatus(WorkflowActionStatus.PENDING);
			modelService.save(act);
		}

		action = workflowRunning.getActions().get(0);
		action.setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(action);

		action = workflowFinished.getActions().get(0);
		action.setStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		modelService.save(action);
	}

	@Test
	public void testFindAllWorkflowsWithoutTimerange()
	{
		//given
		setSessionUser(WORKFLOW_TEST_USER);

		//when
		final List<WorkflowModel> findAllWorkflows = defaultWorkflowDao.findAllWorkflows(null, null);

		//then
		assertThat(findAllWorkflows).hasSize(2);
	}

	@Test
	public void testFindAllWorkflowsWithTimerangeSpecified()
	{
		setSessionUser(WORKFLOW_TEST_USER);

		//when
		final List<WorkflowModel> findAllWorkflows = defaultWorkflowDao.findAllWorkflows(new DateMidnight(2010, 10, 1).toDate(),
				new DateMidnight(2010, 10, 29).toDate());

		//then
		//TODO: problem with modified time !
		assertThat(findAllWorkflows).isEmpty();
	}

	@Test
	public void testFindAllWorkflowsForUserThatDoesNotHaveWorkflows()
	{
		//given
		setSessionUser("anonymous");

		//when
		final List<WorkflowModel> findAllWorkflows = defaultWorkflowDao.findAllWorkflows(null, null);

		//then
		//TODO: problem with modified time !
		assertThat(findAllWorkflows).isEmpty();
	}

	@Test
	public void testFindAllAdhocWorkflowsWithGivenPeriod()
	{
		//given
		setSessionUser(WORKFLOW_TEST_USER);

		//when
		final List<WorkflowModel> findAllWorkflows = defaultWorkflowDao.findAllAdhocWorkflows(
				new DateMidnight(2010, 10, 1).toDate(), new DateMidnight(2010, 10, 29).toDate());

		//then
		//TODO: problem with modified time !
		assertThat(findAllWorkflows).isEmpty();
	}

	@Test
	public void testFindAllAdhocWorkflows()
	{
		//given
		setSessionUser(WORKFLOW_TEST_USER);

		//when
		final List<WorkflowModel> findAllWorkflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null);

		//then
		assertThat(findAllWorkflows).hasSize(1);
	}

	@Test
	public void testFindAllAdhocWorkflowsAndNoResults()
	{
		//given
		setSessionUser(WORKFLOW_TEST_USER);
		final List<WorkflowModel> findAllWorkflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null);
		assertThat(findAllWorkflows).hasSize(1);
		final WorkflowModel adhocWorkflow = findAllWorkflows.get(0);
		//TODO: to change
		modelService.remove(adhocWorkflow);

		//when
		final List<WorkflowModel> newFindAllWorkflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null);

		//then
		//TODO: problem with modified time !
		assertThat(newFindAllWorkflows).isEmpty();
	}

	@Test
	public void testFindWorkflowsByUserAndTemplate()
	{
		//given
		final WorkflowTemplateModel wtm = (WorkflowTemplateModel) flexibleSearchService
				.search("SELECT {PK} FROM {WorkflowTemplate} WHERE {code}=?code",
						Collections.singletonMap("code", "SampleWorkflowPredecessor")).getResult().get(0);

		final UserModel sessionUser = setSessionUser(WORKFLOW_TEST_USER);


		//when
		final List<WorkflowModel> findAllWorkflows = defaultWorkflowDao.findWorkflowsByUserAndTemplate(sessionUser, wtm);

		//then
		assertThat(findAllWorkflows).hasSize(2);
	}

	@Test
	public void testFindWorkflowsByUserAndTemplateWhenUserHasNoWorkflows()
	{
		//given
		final WorkflowTemplateModel wtm = (WorkflowTemplateModel) flexibleSearchService
				.search("SELECT {PK} FROM {WorkflowTemplate} WHERE {code}=?code",
						Collections.singletonMap("code", "SampleWorkflowPredecessor")).getResult().get(0);

		final UserModel sessionUser = setSessionUser("anonymous");


		//when
		final List<WorkflowModel> findAllWorkflows = defaultWorkflowDao.findWorkflowsByUserAndTemplate(sessionUser, wtm);

		//then
		assertThat(findAllWorkflows).isEmpty();
	}

	@Test
	public void testFindWorkflowsByCode()
	{
		//when
		final List<WorkflowModel> findedWorkflows = defaultWorkflowDao.findWorkflowsByCode(WORKFLOW_1_CODE);

		//then 
		assertThat(findedWorkflows).hasSize(1);
	}

	@Test
	public void testFindWorkflowsByCodeWhenWorkflowDoesNotExists()
	{
		//when
		final List<WorkflowModel> findedWorkflows = defaultWorkflowDao.findWorkflowsByCode("unknown");

		//then 
		assertThat(findedWorkflows).isEmpty();
	}

	@Test
	public void testPaginationIncorrectParams()
	{
		TestUtils.disableFileAnalyzer("Exceptions are expected");
		try
		{
			defaultWorkflowDao.findAllWorkflows(null, null, null, -1, 50);
			Assert.fail("findAllWorkflows with negative page number should throw exception");
		}
		catch (final IllegalStateException ex)//NOPMD
		{
			//expected behavior
		}
		try
		{
			defaultWorkflowDao.findAllWorkflows(null, null, null, 0, -10);
			Assert.fail("findAllWorkflows with negative page size should throw exception");
		}
		catch (final IllegalStateException ex)//NOPMD
		{
			//expected behavior
		}

		try
		{
			defaultWorkflowDao.findAllWorkflows(null, null, null, 0, 0);
			Assert.fail("findAllWorkflows with page size = 0 should throw exception");
		}
		catch (final IllegalStateException ex)//NOPMD
		{
			//expected behavior
		}
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void testPaginationAdhocIncorrectParams()
	{
		TestUtils.disableFileAnalyzer("Exceptions are expected");
		try
		{
			defaultWorkflowDao.findAllAdhocWorkflows(null, null, null, -1, 50);
			Assert.fail("findAllWorkflows with negative page number should throw exception");
		}
		catch (final IllegalStateException ex)//NOPMD
		{
			//expected behavior
		}
		try
		{
			defaultWorkflowDao.findAllAdhocWorkflows(null, null, null, 0, -10);
			Assert.fail("findAllWorkflows with negative page size should throw exception");
		}
		catch (final IllegalStateException ex)//NOPMD
		{
			//expected behavior
		}

		try
		{
			defaultWorkflowDao.findAllAdhocWorkflows(null, null, null, 0, 0);
			Assert.fail("findAllWorkflows with page size = 0 should throw exception");
		}
		catch (final IllegalStateException ex)//NOPMD
		{
			//expected behavior
		}
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void testPaginationWhenNoWorkflowStatusSelected()
	{
		setSessionUser(WORKFLOW_TEST_USER);
		SearchResult<WorkflowModel> workflowsWhenStatusSetIsNull = defaultWorkflowDao.findAllWorkflows(null, null, null,
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		SearchResult<WorkflowModel> workflowsWhenStatusSetIsEmpty = defaultWorkflowDao.findAllWorkflows(null, null,
				EnumSet.noneOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		List<WorkflowModel> workflowsWithoutPaging = defaultWorkflowDao.findAllWorkflows(null, null);

		assertThat(workflowsWithoutPaging).hasSize(2);
		assertThat(workflowsWhenStatusSetIsNull.getResult()).isEqualTo(workflowsWithoutPaging);
		assertThat(workflowsWhenStatusSetIsEmpty.getResult()).isEqualTo(workflowsWithoutPaging);

		//check if date params are taken into account: currently all workflows has set modified time to current time

		// date range: yesterday - tomorrow
		workflowsWithoutPaging = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate());
		workflowsWhenStatusSetIsNull = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(), null,
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		workflowsWhenStatusSetIsEmpty = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.noneOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);

		assertThat(workflowsWithoutPaging).hasSize(2);
		assertThat(workflowsWhenStatusSetIsNull.getResult()).isEqualTo(workflowsWithoutPaging);
		assertThat(workflowsWhenStatusSetIsEmpty.getResult()).isEqualTo(workflowsWithoutPaging);
		// date range: oneWeekBeforeNow - yesterday
		workflowsWhenStatusSetIsNull = defaultWorkflowDao.findAllWorkflows(oneWeekBeforeNow.toDate(), yesterday.toDate(), null,
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		workflowsWhenStatusSetIsEmpty = defaultWorkflowDao.findAllWorkflows(oneWeekBeforeNow.toDate(), yesterday.toDate(),
				EnumSet.noneOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsWhenStatusSetIsNull.getResult()).hasSize(0);
		assertThat(workflowsWhenStatusSetIsEmpty.getResult()).hasSize(0);
		// date range: tomorrow - oneWeekAfterNow 
		workflowsWhenStatusSetIsNull = defaultWorkflowDao.findAllWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(), null,
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		workflowsWhenStatusSetIsEmpty = defaultWorkflowDao.findAllWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.noneOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsWhenStatusSetIsNull.getResult()).hasSize(0);
		assertThat(workflowsWhenStatusSetIsEmpty.getResult()).hasSize(0);
	}

	@Test
	public void testPaginationAdhocWhenNoWorkflowStatusSelected()
	{
		setSessionUser(WORKFLOW_TEST_USER);
		SearchResult<WorkflowModel> workflowsWhenStatusSetIsNull = defaultWorkflowDao.findAllAdhocWorkflows(null, null, null,
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		SearchResult<WorkflowModel> workflowsWhenStatusSetIsEmpty = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.noneOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		List<WorkflowModel> workflowsWithoutPaging = defaultWorkflowDao.findAllAdhocWorkflows(null, null);

		assertThat(workflowsWithoutPaging).hasSize(1);
		assertThat(workflowsWhenStatusSetIsNull.getResult()).isEqualTo(workflowsWithoutPaging);
		assertThat(workflowsWhenStatusSetIsEmpty.getResult()).isEqualTo(workflowsWithoutPaging);

		//check if date params are taken into account: currently all workflows has set modified time to current time

		// date range: yesterday - tomorrow
		workflowsWithoutPaging = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate());
		workflowsWhenStatusSetIsNull = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(), null,
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		workflowsWhenStatusSetIsEmpty = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.noneOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);

		assertThat(workflowsWithoutPaging).hasSize(1);
		assertThat(workflowsWhenStatusSetIsNull.getResult()).isEqualTo(workflowsWithoutPaging);
		assertThat(workflowsWhenStatusSetIsEmpty.getResult()).isEqualTo(workflowsWithoutPaging);
		// date range: oneWeekBeforeNow - yesterday
		workflowsWhenStatusSetIsNull = defaultWorkflowDao.findAllAdhocWorkflows(oneWeekBeforeNow.toDate(), yesterday.toDate(),
				null, DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		workflowsWhenStatusSetIsEmpty = defaultWorkflowDao.findAllAdhocWorkflows(oneWeekBeforeNow.toDate(), yesterday.toDate(),
				EnumSet.noneOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsWhenStatusSetIsNull.getResult()).hasSize(0);
		assertThat(workflowsWhenStatusSetIsEmpty.getResult()).hasSize(0);
		// date range: tomorrow - oneWeekAfterNow 
		workflowsWhenStatusSetIsNull = defaultWorkflowDao.findAllAdhocWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(), null,
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		workflowsWhenStatusSetIsEmpty = defaultWorkflowDao.findAllAdhocWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.noneOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsWhenStatusSetIsNull.getResult()).hasSize(0);
		assertThat(workflowsWhenStatusSetIsEmpty.getResult()).hasSize(0);
	}

	/**
	 * Test check if any workflow action is terminated, workflow should also has 'TERMINATED'status
	 */
	@Test
	public void testPaginationWhenWorkflowStatusIsTerminated()
	{

		setSessionUser(WORKFLOW_TEST_USER);
		final WorkflowModel testWorkflow1 = getTestWorkflow1AndAssertInitialState();
		SearchResult<WorkflowModel> workflowsFromPaginationMethod = defaultWorkflowDao.findAllWorkflows(null, null,
				EnumSet.of(WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsFromPaginationMethod.getResult()).hasSize(0);

		testWorkflow1.getActions().get(0).setStatus(WorkflowActionStatus.TERMINATED);
		modelService.save(testWorkflow1.getActions().get(0));

		workflowsFromPaginationMethod = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.TERMINATED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsFromPaginationMethod.getResult()).hasSize(1);
		assertThat(workflowsFromPaginationMethod.getResult().get(0).getCode()).isEqualTo(WORKFLOW_1_CODE);
		//check if date are taken into account
		workflowsFromPaginationMethod = defaultWorkflowDao.findAllWorkflows(oneWeekBeforeNow.toDate(), yesterday.toDate(),
				EnumSet.of(WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsFromPaginationMethod.getResult()).hasSize(0);

		workflowsFromPaginationMethod = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsFromPaginationMethod.getResult()).hasSize(1);
		assertThat(workflowsFromPaginationMethod.getResult().get(0).getCode()).isEqualTo(WORKFLOW_1_CODE);
	}

	@Test
	public void testPaginationAdhocWhenWorkflowStatusIsTerminated()
	{
		setSessionUser(WORKFLOW_TEST_USER);
		SearchResult<WorkflowModel> workflowsFromPaginationMethod = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsFromPaginationMethod.getResult()).hasSize(0);

		adhocWorkflow.getActions().get(0).setStatus(WorkflowActionStatus.TERMINATED);
		modelService.save(adhocWorkflow.getActions().get(0));

		workflowsFromPaginationMethod = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.TERMINATED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsFromPaginationMethod.getResult()).hasSize(1);
		//check if date are taken into account
		workflowsFromPaginationMethod = defaultWorkflowDao.findAllAdhocWorkflows(oneWeekBeforeNow.toDate(), yesterday.toDate(),
				EnumSet.of(WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsFromPaginationMethod.getResult()).hasSize(0);

		workflowsFromPaginationMethod = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflowsFromPaginationMethod.getResult()).hasSize(1);
	}


	@Test
	public void testPaginationWhenWorkflowStatusIsFinished()
	{
		// workflow is finished when:
		// 1. every action has state: DISABLED or COMPLETED
		// 2. OR one or more action has state ENDED_THROUGH_END_OF_WORKFLOW

		setSessionUser(WORKFLOW_TEST_USER);
		final WorkflowModel workflow = getTestWorkflow1AndAssertInitialState();

		SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllWorkflows(null, null,
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult().get(0).getCode()).isEqualTo(WORKFLOW_2_CODE);
		//all DISABLED
		for (final WorkflowActionModel workflowAction : workflow.getActions())
		{
			workflowAction.setStatus(WorkflowActionStatus.DISABLED);
			modelService.save(workflowAction);
		}
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(), EnumSet.of(WorkflowStatus.FINISHED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		//only one IN_PROGRESS
		workflow.getActions().get(0).setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(workflow.getActions().get(0));
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult().get(0).getCode()).isEqualTo(WORKFLOW_2_CODE);
		workflows = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(), EnumSet.of(WorkflowStatus.FINISHED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult().get(0).getCode()).isEqualTo(WORKFLOW_2_CODE);
		//all COMPLETED
		for (final WorkflowActionModel workflowAction : workflow.getActions())
		{
			workflowAction.setStatus(WorkflowActionStatus.COMPLETED);
			modelService.save(workflowAction);
		}
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(), EnumSet.of(WorkflowStatus.FINISHED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		//only one IN_PROGRESS
		workflow.getActions().get(0).setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(workflow.getActions().get(0));
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult().get(0).getCode()).isEqualTo(WORKFLOW_2_CODE);

		//half DISABLED, half COMPLETED
		for (int i = 0; i < workflow.getActions().size(); i++)
		{
			if (i % 2 == 0)
			{
				workflow.getActions().get(i).setStatus(WorkflowActionStatus.DISABLED);
			}
			else
			{
				workflow.getActions().get(i).setStatus(WorkflowActionStatus.COMPLETED);
			}
			modelService.save(workflow.getActions().get(i));
		}
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(), EnumSet.of(WorkflowStatus.FINISHED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);


		//only one ENDED_THROUGH_END_OF_WORKFLOW, rest IN_PROGRESS
		for (final WorkflowActionModel action : workflow.getActions())
		{
			action.setStatus(WorkflowActionStatus.IN_PROGRESS);
			modelService.save(action);
		}
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult().get(0).getCode()).isEqualTo(WORKFLOW_2_CODE);

		workflow.getActions().get(0).setStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		modelService.save(workflow.getActions().get(0));
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(), EnumSet.of(WorkflowStatus.FINISHED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
	}

	@Test
	public void testPaginationAdhocWhenWorkflowStatusIsFinished()
	{
		// workflow is finished when:
		// 1. every action has state: DISABLED or COMPLETED
		// 2. OR one or more action has state ENDED_THROUGH_END_OF_WORKFLOW

		setSessionUser(WORKFLOW_TEST_USER);

		SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
		//all DISABLED
		for (final WorkflowActionModel workflowAction : adhocWorkflow.getActions())
		{
			workflowAction.setStatus(WorkflowActionStatus.DISABLED);
			modelService.save(workflowAction);
		}
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		//only one IN_PROGRESS
		adhocWorkflow.getActions().get(0).setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(adhocWorkflow.getActions().get(0));
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
		//all COMPLETED
		for (final WorkflowActionModel workflowAction : adhocWorkflow.getActions())
		{
			workflowAction.setStatus(WorkflowActionStatus.COMPLETED);
			modelService.save(workflowAction);
		}
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		//only one IN_PROGRESS
		adhocWorkflow.getActions().get(0).setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(adhocWorkflow.getActions().get(0));
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		//half DISABLED, half COMPLETED
		for (int i = 0; i < adhocWorkflow.getActions().size(); i++)
		{
			if (i % 2 == 0)
			{
				adhocWorkflow.getActions().get(i).setStatus(WorkflowActionStatus.DISABLED);
			}
			else
			{
				adhocWorkflow.getActions().get(i).setStatus(WorkflowActionStatus.COMPLETED);
			}
			modelService.save(adhocWorkflow.getActions().get(i));
		}
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);


		//only one ENDED_THROUGH_END_OF_WORKFLOW, rest IN_PROGRESS
		for (final WorkflowActionModel action : adhocWorkflow.getActions())
		{
			action.setStatus(WorkflowActionStatus.IN_PROGRESS);
			modelService.save(action);
		}
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		adhocWorkflow.getActions().get(0).setStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		modelService.save(adhocWorkflow.getActions().get(0));
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
	}

	@Test
	public void testPaginationWhenWorkflowStatusIsPlanned()
	{
		// workflow is planned when all actions has state PENDING
		setSessionUser(WORKFLOW_TEST_USER);
		final WorkflowModel workflow = getTestWorkflow1AndAssertInitialState();
		SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult().get(0).getCode()).isEqualTo(WORKFLOW_2_CODE);


		workflow.getActions().get(0).setStatus(WorkflowActionStatus.PENDING);
		modelService.save(workflow.getActions().get(0));
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult().get(0).getCode()).isEqualTo(WORKFLOW_2_CODE);

		for (final WorkflowActionModel action : workflow.getActions())
		{
			action.setStatus(WorkflowActionStatus.PENDING);
			modelService.save(action);
		}
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(), EnumSet.of(WorkflowStatus.PLANNED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		workflows = defaultWorkflowDao.findAllWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
	}

	@Test
	public void testPaginationAdhocWhenWorkflowStatusIsPlanned()
	{
		// workflow is planned when all actions has state PENDING
		setSessionUser(WORKFLOW_TEST_USER);
		SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		adhocWorkflow.getActions().get(0).setStatus(WorkflowActionStatus.PENDING);
		modelService.save(adhocWorkflow.getActions().get(0));
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		for (final WorkflowActionModel action : adhocWorkflow.getActions())
		{
			action.setStatus(WorkflowActionStatus.PENDING);
			modelService.save(action);
		}
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
	}


	@Test
	public void testPaginationWhenWorkflowStatusIsRunning()
	{
		//workflow has starus running when even only one action has status: IN_PROGRESS
		setSessionUser(WORKFLOW_TEST_USER);
		final WorkflowModel workflow = getTestWorkflow1AndAssertInitialState();

		for (final WorkflowActionModel action : workflow.getActions())
		{
			action.setStatus(WorkflowActionStatus.PAUSED);
			modelService.save(action);
		}
		SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.RUNNING),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		workflow.getActions().get(0).setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(workflow.getActions().get(0));

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(), EnumSet.of(WorkflowStatus.RUNNING),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.RUNNING), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
	}

	@Test
	public void testPaginationAdhocWhenWorkflowStatusIsRunning()
	{
		//workflow has starus running when even only one action has status: IN_PROGRESS
		setSessionUser(WORKFLOW_TEST_USER);

		for (final WorkflowActionModel action : adhocWorkflow.getActions())
		{
			action.setStatus(WorkflowActionStatus.PAUSED);
			modelService.save(action);
		}
		SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.RUNNING), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		adhocWorkflow.getActions().get(0).setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(adhocWorkflow.getActions().get(0));

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.RUNNING), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(tomorrow.toDate(), oneWeekAfrerNow.toDate(),
				EnumSet.of(WorkflowStatus.RUNNING), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);
	}

	@Test
	public void testPaginationWhenManyStatuses()
	{
		final UserModel owner = setSessionUser(WORKFLOW_TEST_USER);
		final WorkflowTemplateModel workflowTemplate = workflowTemplateService.getWorkflowTemplateForCode(WORKFLOW_TEMPLATE_CODE);
		final WorkflowModel[] tabNewWorkflows = new WorkflowModel[4];
		tabNewWorkflows[0] = workflowService.createWorkflow(workflowTemplate, owner);
		tabNewWorkflows[1] = workflowService.createWorkflow(workflowTemplate, owner);
		tabNewWorkflows[2] = workflowService.createWorkflow(workflowTemplate, owner);
		tabNewWorkflows[3] = workflowService.createWorkflow(workflowTemplate, owner);

		final WorkflowModel workflow1 = workflowService.getWorkflowForCode(WORKFLOW_1_CODE);
		// all new created workflows has 10 action with pending status
		// change to pending status action for workflow1
		for (final WorkflowActionModel action : workflow1.getActions())
		{
			action.setStatus(WorkflowActionStatus.PENDING);
			modelService.save(action);
		}
		// currently all workflow should be in status PLANNED
		final List<String> expectedWorkflowIds = new ArrayList<String>();
		expectedWorkflowIds.add(WORKFLOW_1_CODE);
		expectedWorkflowIds.add(WORKFLOW_2_CODE);
		for (int i = 0; i < tabNewWorkflows.length; i++)
		{
			expectedWorkflowIds.add(tabNewWorkflows[i].getCode());
		}

		SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllWorkflows(null, null,
				EnumSet.allOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		// should return just created workflows + workflow1
		assertThat(workflows.getResult()).hasSize(expectedWorkflowIds.size());
		assertThat(workflows.getResult()).onProperty("code").containsOnly(expectedWorkflowIds.toArray());

		workflows = defaultWorkflowDao.findAllWorkflows(null, null,
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.RUNNING, WorkflowStatus.TERMINATED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);

		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult().get(0).getCode()).isEqualTo(WORKFLOW_2_CODE);

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(expectedWorkflowIds.size());
		assertThat(workflows.getResult()).onProperty("code").containsOnly(expectedWorkflowIds.toArray());

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED, WorkflowStatus.TERMINATED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(expectedWorkflowIds.size());
		assertThat(workflows.getResult()).onProperty("code").containsOnly(expectedWorkflowIds.toArray());

		// change one workflow to running status
		WorkflowActionModel action = tabNewWorkflows[0].getActions().get(0);
		action.setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(action);

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewWorkflows[0].getCode());

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.RUNNING, WorkflowStatus.TERMINATED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewWorkflows[0].getCode());

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(5);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(WORKFLOW_1_CODE, WORKFLOW_2_CODE,
				tabNewWorkflows[1].getCode(), tabNewWorkflows[2].getCode(), tabNewWorkflows[3].getCode());


		// change one workflow to terminated
		action = tabNewWorkflows[1].getActions().get(0);
		action.setStatus(WorkflowActionStatus.TERMINATED);
		modelService.save(action);

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.TERMINATED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewWorkflows[1].getCode());

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.RUNNING, WorkflowStatus.TERMINATED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewWorkflows[0].getCode(),
				tabNewWorkflows[1].getCode());

		//change one workflow to finished, case: (all actions are in state DISABLED or COMPLETED)
		for (final WorkflowActionModel act : tabNewWorkflows[2].getActions())
		{
			act.setStatus(WorkflowActionStatus.COMPLETED);
			modelService.save(act);
		}
		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(WORKFLOW_2_CODE, tabNewWorkflows[2].getCode());

		workflows = defaultWorkflowDao.findAllWorkflows(null, null,
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.TERMINATED, WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(4);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(WORKFLOW_2_CODE, tabNewWorkflows[0].getCode(),
				tabNewWorkflows[1].getCode(), tabNewWorkflows[2].getCode());
		//change one workflow to finished, case: one action in state ENDED_THROUGH_END_OF_WORKFLOW
		action = tabNewWorkflows[3].getActions().get(0);
		action.setStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		modelService.save(action);

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(3);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(WORKFLOW_2_CODE, tabNewWorkflows[2].getCode(),
				tabNewWorkflows[3].getCode());

		workflows = defaultWorkflowDao.findAllWorkflows(null, null,
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.TERMINATED, WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(5);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(WORKFLOW_2_CODE, tabNewWorkflows[0].getCode(),
				tabNewWorkflows[1].getCode(), tabNewWorkflows[2].getCode(), tabNewWorkflows[3].getCode());
		//check if dates are taken into account
		workflows = defaultWorkflowDao.findAllWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.TERMINATED, WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(5);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(WORKFLOW_2_CODE, tabNewWorkflows[0].getCode(),
				tabNewWorkflows[1].getCode(), tabNewWorkflows[2].getCode(), tabNewWorkflows[3].getCode());

		workflows = defaultWorkflowDao.findAllWorkflows(oneWeekBeforeNow.toDate(), yesterday.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.TERMINATED, WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		workflows = defaultWorkflowDao.findAllWorkflows(null, null, EnumSet.allOf(WorkflowStatus.class), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(expectedWorkflowIds.size());
		assertThat(workflows.getResult()).onProperty("code").containsOnly(expectedWorkflowIds.toArray());
	}


	@Test
	public void testPaginationAdhocWhenManyStatuses()
	{
		final UserModel owner = setSessionUser(WORKFLOW_TEST_USER);
		final WorkflowTemplateModel workflowTemplate = workflowTemplateService.getAdhocWorkflowTemplate();
		final WorkflowModel[] tabNewAdhocWorkflows = new WorkflowModel[4];
		tabNewAdhocWorkflows[0] = workflowService.createWorkflow(workflowTemplate, owner);
		tabNewAdhocWorkflows[1] = workflowService.createWorkflow(workflowTemplate, owner);
		tabNewAdhocWorkflows[2] = workflowService.createWorkflow(workflowTemplate, owner);
		tabNewAdhocWorkflows[3] = workflowService.createWorkflow(workflowTemplate, owner);

		// all new created workflows has 2 action with pending status
		// change to pending status action for workflow1
		for (final WorkflowActionModel action : adhocWorkflow.getActions())
		{
			action.setStatus(WorkflowActionStatus.PENDING);
			modelService.save(action);
		}
		// currently all workflow should be in status PLANNED
		final List<String> adhocWorkflowsId = new ArrayList<String>();
		adhocWorkflowsId.add(adhocWorkflow.getCode());
		for (int i = 0; i < tabNewAdhocWorkflows.length; i++)
		{
			adhocWorkflowsId.add(tabNewAdhocWorkflows[i].getCode());
		}

		SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.allOf(WorkflowStatus.class), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		// should return just created workflows + workflow1
		assertThat(workflows.getResult()).hasSize(5);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(adhocWorkflowsId.toArray());

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.RUNNING, WorkflowStatus.TERMINATED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);

		assertThat(workflows.getResult()).hasSize(0);

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(5);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(adhocWorkflowsId.toArray());

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.PLANNED, WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(5);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(adhocWorkflowsId.toArray());

		// change one workflow to running status
		WorkflowActionModel action = tabNewAdhocWorkflows[0].getActions().get(0);
		action.setStatus(WorkflowActionStatus.IN_PROGRESS);
		modelService.save(action);

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[0].getCode());

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.RUNNING, WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[0].getCode());

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.PLANNED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(4);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(adhocWorkflow.getCode(),
				tabNewAdhocWorkflows[1].getCode(), tabNewAdhocWorkflows[2].getCode(), tabNewAdhocWorkflows[3].getCode());


		// change one workflow to terminated
		action = tabNewAdhocWorkflows[1].getActions().get(0);
		action.setStatus(WorkflowActionStatus.TERMINATED);
		modelService.save(action);

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.TERMINATED),
				DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[1].getCode());

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.RUNNING, WorkflowStatus.TERMINATED), DEFAULT_START_INDEX, DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[0].getCode(),
				tabNewAdhocWorkflows[1].getCode());

		//change one workflow to finished, case: (all actions are in state DISABLED or COMPLETED)
		for (final WorkflowActionModel act : tabNewAdhocWorkflows[2].getActions())
		{
			act.setStatus(WorkflowActionStatus.COMPLETED);
			modelService.save(act);
		}
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(1);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[2].getCode());

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.TERMINATED, WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(3);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[0].getCode(),
				tabNewAdhocWorkflows[1].getCode(), tabNewAdhocWorkflows[2].getCode());
		//change one workflow to finished, case: one action in state ENDED_THROUGH_END_OF_WORKFLOW
		action = tabNewAdhocWorkflows[3].getActions().get(0);
		action.setStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		modelService.save(action);

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.of(WorkflowStatus.FINISHED), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(2);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[2].getCode(),
				tabNewAdhocWorkflows[3].getCode());

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null,
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.TERMINATED, WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(4);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[0].getCode(),
				tabNewAdhocWorkflows[1].getCode(), tabNewAdhocWorkflows[2].getCode(), tabNewAdhocWorkflows[3].getCode());
		//check if dates are taken into account
		workflows = defaultWorkflowDao.findAllAdhocWorkflows(yesterday.toDate(), tomorrow.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.TERMINATED, WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(4);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(tabNewAdhocWorkflows[0].getCode(),
				tabNewAdhocWorkflows[1].getCode(), tabNewAdhocWorkflows[2].getCode(), tabNewAdhocWorkflows[3].getCode());

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(oneWeekBeforeNow.toDate(), yesterday.toDate(),
				EnumSet.of(WorkflowStatus.FINISHED, WorkflowStatus.TERMINATED, WorkflowStatus.RUNNING), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(0);

		workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, EnumSet.allOf(WorkflowStatus.class), DEFAULT_START_INDEX,
				DEFAULT_PAGE_SIZE);
		assertThat(workflows.getResult()).hasSize(5);
		assertThat(workflows.getResult()).onProperty("code").containsOnly(adhocWorkflowsId.toArray());
	}


	@Test
	public void testPaginationPageParams()
	{
		final UserModel owner = setSessionUser(WORKFLOW_TEST_USER);
		final WorkflowTemplateModel workflowTemplate = workflowTemplateService.getWorkflowTemplateForCode(WORKFLOW_TEMPLATE_CODE);
		final int NUMBER_OF_NEW_WORKFLOWS = 20;
		final WorkflowModel[] tabNewWorkflows = new WorkflowModel[NUMBER_OF_NEW_WORKFLOWS];

		final Set<String> createdWorkflowsCodeSet = new HashSet<String>();
		final Set<String> returnedWorkflowsCodeSet = new HashSet<String>();

		createdWorkflowsCodeSet.add(WORKFLOW_1_CODE);
		createdWorkflowsCodeSet.add(WORKFLOW_2_CODE);
		for (int i = 0; i < NUMBER_OF_NEW_WORKFLOWS; i++)
		{
			tabNewWorkflows[i] = workflowService.createWorkflow(workflowTemplate, owner);
			createdWorkflowsCodeSet.add(tabNewWorkflows[i].getCode());
		}


		int numberOfFetchedWorkflows = 0;
		int startIndex = 0;
		final int pageSize = 3;
		final int expectedTotalCount = NUMBER_OF_NEW_WORKFLOWS + 2;

		while (numberOfFetchedWorkflows < expectedTotalCount)
		{
			final SearchResult<WorkflowModel> workflows = defaultWorkflowDao
					.findAllWorkflows(null, null, null, startIndex, pageSize);
			assertThat(workflows.getTotalCount()).isEqualTo(expectedTotalCount);
			for (final WorkflowModel workflow : workflows.getResult())
			{
				final boolean added = returnedWorkflowsCodeSet.add(workflow.getCode());
				Assert.assertTrue("Workflow id should be unique", added);
			}
			startIndex += pageSize;
			assertThat(workflows.getCount()).isEqualTo(workflows.getResult().size());
			assertThat(workflows.getCount()).isEqualTo(Math.min(pageSize, expectedTotalCount - numberOfFetchedWorkflows));
			numberOfFetchedWorkflows += workflows.getResult().size();

		}

		assertThat(Integer.valueOf(numberOfFetchedWorkflows)).isEqualTo(expectedTotalCount);
		assertThat(returnedWorkflowsCodeSet).isEqualTo(createdWorkflowsCodeSet);
	}

	@Test
	public void testPaginationAdhocPageParams()
	{
		final UserModel owner = setSessionUser(WORKFLOW_TEST_USER);
		final WorkflowTemplateModel workflowTemplate = workflowTemplateService.getAdhocWorkflowTemplate();
		final int NUMBER_OF_NEW_WORKFLOWS = 20;
		final WorkflowModel[] tabNewWorkflows = new WorkflowModel[NUMBER_OF_NEW_WORKFLOWS];

		final Set<String> createdWorkflowsCodeSet = new HashSet<String>();
		final Set<String> returnedWorkflowsCodeSet = new HashSet<String>();

		createdWorkflowsCodeSet.add(WORKFLOW_3_CODE);
		for (int i = 0; i < NUMBER_OF_NEW_WORKFLOWS; i++)
		{
			tabNewWorkflows[i] = workflowService.createWorkflow(workflowTemplate, owner);
			createdWorkflowsCodeSet.add(tabNewWorkflows[i].getCode());
		}


		int numberOfFetchedWorkflows = 0;
		int startIndex = 0;
		final int pageSize = 3;
		final int expectedTotalCount = NUMBER_OF_NEW_WORKFLOWS + 1;

		while (numberOfFetchedWorkflows < expectedTotalCount)
		{
			final SearchResult<WorkflowModel> workflows = defaultWorkflowDao.findAllAdhocWorkflows(null, null, null, startIndex,
					pageSize);
			assertThat(workflows.getTotalCount()).isEqualTo(expectedTotalCount);
			for (final WorkflowModel workflow : workflows.getResult())
			{
				final boolean added = returnedWorkflowsCodeSet.add(workflow.getCode());
				Assert.assertTrue("Workflow id should be unique", added);
			}
			startIndex += pageSize;
			assertThat(workflows.getCount()).isEqualTo(workflows.getResult().size());
			assertThat(workflows.getCount()).isEqualTo(Math.min(pageSize, expectedTotalCount - numberOfFetchedWorkflows));
			numberOfFetchedWorkflows += workflows.getResult().size();

		}

		assertThat(Integer.valueOf(numberOfFetchedWorkflows)).isEqualTo(expectedTotalCount);
		assertThat(returnedWorkflowsCodeSet).isEqualTo(createdWorkflowsCodeSet);
	}

	private UserModel setSessionUser(final String uid)
	{
		final UserModel sessionUser = userService.getUserForUID(uid);
		userService.setCurrentUser(sessionUser);
		return sessionUser;
	}

	/**
	 * Return workflow with code {@value #WORKFLOW_1_CODE} and check that all 4 actions are IN_PROGRESS
	 * 
	 */
	private WorkflowModel getTestWorkflow1AndAssertInitialState()
	{
		final List<WorkflowModel> workflow1List = defaultWorkflowDao.findWorkflowsByCode(WORKFLOW_1_CODE);
		assertThat(workflow1List).hasSize(1);
		final WorkflowModel workflow1 = workflow1List.get(0);
		final List<WorkflowActionModel> actions = workflow1.getActions();
		assertThat(actions).hasSize(4);
		for (final WorkflowActionModel action : actions)
		{
			assertThat(action.getStatus()).isEqualTo(WorkflowActionStatus.IN_PROGRESS);
		}
		return workflow1;
	}
}
