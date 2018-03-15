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
package de.hybris.platform.workflow.integration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.constants.WorkflowConstants;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.jalo.WorkflowAction;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for extension Workflow.
 */
@DemoTest
public class WorkflowTest extends ServicelayerTransactionalBaseTest
{
	final private static Logger LOG = Logger.getLogger(WorkflowTest.class);
	/**
	 * Reference to the manager instance.
	 */
	//protected WorkflowManager manager;
	@Resource(name = "newestWorkflowService")
	protected WorkflowService workflowService;
	@Resource
	protected ModelService modelService;
	@Resource
	protected UserService userService;
	@Resource
	protected TypeService typeService;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	@Resource
	protected CatalogVersionService catalogVersionService;

	@Resource
	protected WorkflowActionService workflowActionService;

	@Resource
	protected WorkflowProcessingService workflowProcessingService;

	/**
	 * Template instance created at each set up.
	 */
	protected WorkflowTemplateModel testTemplate = null;

	/**
	 * workflow instance created at each set up.
	 */
	protected WorkflowModel testWorkflow = null;

	protected static enum ACTIONCODES
	{
		ACTION1, ACTION2, ACTION3, ACTION4, ACTION5, ACTION6
	}

	protected static enum DECISIONCODES
	{
		DECISION1, DECISION2, DECISION3, DECISION4, DECISION5, DECISION6, DECISION7
	}


	@Before
	public void setUp() throws Exception
	{


		final CatalogModel testCatalog = modelService.create(CatalogModel.class);
		testCatalog.setId("DefaultTestCatalog");
		modelService.save(testCatalog);

		final CatalogVersionModel testCv = modelService.create(CatalogVersionModel.class);
		testCv.setVersion("Online");
		testCv.setCatalog(testCatalog);
		modelService.save(testCv);

		catalogVersionService.addSessionCatalogVersion(testCv);
		//manager = WorkflowManager.getInstance();
		final UserModel testUser = createUser("TestUser");
		testTemplate = createWorkflowTemplate(testUser);

		userService.setCurrentUser(testUser);

		testWorkflow = workflowService.createWorkflow(testTemplate, testUser);
		workflowProcessingService.toggleActions(testWorkflow);
		modelService.saveAll();
		assertNotNull("Workflow not null", testWorkflow);

		// start workflow
		workflowProcessingService.startWorkflow(testWorkflow);
	}

	/**
	 * check actions template size
	 */
	@Test
	public void testTemplateActionsSize()
	{
		final Collection<WorkflowActionTemplateModel> actionTemplates = testTemplate.getActions();
		assertEquals("Expected number of action templates", 6, actionTemplates.size());
	}

	/**
	 * check actions size
	 */
	@Test
	public void testActionsSize()
	{
		final Collection<WorkflowActionModel> actions = testWorkflow.getActions();
		assertEquals("Expected number of actions", 6, actions.size());
	}

	/**
	 * Creates a workflow template with given user assigned.
	 * 
	 * @param user
	 *           user instance to use for template
	 * @return new created template instance
	 */
	protected WorkflowTemplateModel createWorkflowTemplate(final UserModel user)
	{
		final WorkflowTemplateModel template = createWorkflowTemplate(user, "Test Template", "Test Template Descr");
		final WorkflowActionTemplateModel templateAction1 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION1.name(),
				WorkflowActionType.START, template);
		final WorkflowActionTemplateModel templateAction2 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION2.name(),
				WorkflowActionType.START, template);
		final WorkflowActionTemplateModel templateAction3 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION3.name(),
				WorkflowActionType.NORMAL, template);
		final WorkflowActionTemplateModel templateAction4 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION4.name(),
				WorkflowActionType.NORMAL, template);
		final WorkflowActionTemplateModel templateAction5 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION5.name(),
				WorkflowActionType.NORMAL, template);
		final WorkflowActionTemplateModel templateAction6 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION6.name(),
				WorkflowActionType.END, template);

		final WorkflowDecisionTemplateModel templateDecision1 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION1.name(),
				templateAction1);
		final WorkflowDecisionTemplateModel templateDecision2 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION2.name(),
				templateAction1);
		final WorkflowDecisionTemplateModel templateDecision3 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION3.name(),
				templateAction2);
		final WorkflowDecisionTemplateModel templateDecision4 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION4.name(),
				templateAction2);
		final WorkflowDecisionTemplateModel templateDecision5 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION5.name(),
				templateAction3);
		final WorkflowDecisionTemplateModel templateDecision6 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION6.name(),
				templateAction4);
		final WorkflowDecisionTemplateModel templateDecision7 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION7.name(),
				templateAction5);

		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision1, templateAction3, Boolean.TRUE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision2, templateAction4, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision3, templateAction3, Boolean.TRUE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision4, templateAction4, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision4, templateAction5, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision5, templateAction6, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision6, templateAction6, Boolean.TRUE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision7, templateAction6, Boolean.TRUE);

		return template;
	}

	/**
	 * Creates a user instance using given uid.
	 * 
	 * @param userName
	 *           user id used for user creation
	 * @return created user
	 */
	protected UserModel createUser(final String userName)
	{
		User user = null;
		try
		{
			user = UserManager.getInstance().createCustomer(userName);
			final UserRight readRight = AccessManager.getInstance().getOrCreateUserRightByCode(AccessManager.READ);
			assertNotNull("UserRight should not be null", readRight);
			TypeManager.getInstance().getComposedType(WorkflowAction.class).addPositivePermission(user, readRight);
			assertNotNull("User should not be null", user);
		}
		catch (final ConsistencyCheckException e)
		{
			fail("Can not create user caused by: " + e.getMessage());
		}
		return modelService.get(user);
	}

	protected void clearCache()
	{
		// restart
		Registry.getCurrentTenant().getCache().clear();
	}

	/**
	 * Creates new workflow template using given user, code, and description.
	 * 
	 * @param owner
	 *           user assigned to new template
	 * @param code
	 *           code of new template
	 * @param desc
	 *           description assigned to template
	 * @return created template
	 */
	protected WorkflowTemplateModel createWorkflowTemplate(final UserModel owner, final String code, final String desc)
	{
		return createWorkflowTemplate(owner, code, desc, null);
	}

	/**
	 * Creates new workflow template using given user, code, description and activation script.
	 * 
	 * @param owner
	 *           user assigned to new template
	 * @param code
	 *           code of new template
	 * @param desc
	 *           description assigned to template
	 * @param activationScript
	 *           code of the activation script
	 * @return created template
	 */
	protected WorkflowTemplateModel createWorkflowTemplate(final UserModel owner, final String code, final String desc,
			final String activationScript)
	{
		final WorkflowTemplateModel template = modelService.create(WorkflowTemplateModel.class);
		template.setOwner(owner);
		template.setCode(code);
		template.setDescription(desc);
		template.setActivationScript(activationScript);

		modelService.save(template);

		assertNotNull("Template should not be null", template);
		assertThat(modelService.isUpToDate(template)).isTrue();
		return template;
	}

	/**
	 * Creates new action template.
	 * 
	 * @param user
	 *           user assigned to template
	 * @param code
	 *           code of template
	 * @param workflow
	 *           workflow assigned to action template
	 * @return created action template
	 */
	protected WorkflowActionTemplateModel createWorkflowActionTemplateModel(final UserModel user, final String code,
			final WorkflowActionType actionType, final WorkflowTemplateModel workflow)
	{
		final WorkflowActionTemplateModel action = modelService.create(WorkflowActionTemplateModel.class);
		action.setPrincipalAssigned(user);
		action.setWorkflow(workflow);
		action.setCode(code);
		action.setSendEmail(Boolean.FALSE);
		action.setActionType(actionType);
		modelService.save(action);

		final List<WorkflowActionTemplateModel> coll = new ArrayList(workflow.getActions());
		coll.add(action);
		workflow.setActions(coll);
		modelService.save(workflow);

		assertNotNull("Action Template should not be null", action);
		return action;
	}

	/**
	 * Creates new automated action template.
	 * 
	 * @param user
	 *           user assigned to template
	 * @param code
	 *           code of template
	 * @param workflow
	 *           workflow assigned to action template
	 * @return created action template
	 */
	protected AutomatedWorkflowActionTemplateModel createAutomatedWorkflowActionTemplateModel(final UserModel user,
			final String code, final WorkflowActionType actionType, final WorkflowTemplateModel workflow, final Class jobClass)
	{
		final AutomatedWorkflowActionTemplateModel action = modelService.create(AutomatedWorkflowActionTemplateModel.class);
		action.setPrincipalAssigned(user);
		action.setWorkflow(workflow);
		action.setCode(code);
		action.setSendEmail(Boolean.FALSE);
		action.setActionType(actionType);
		action.setJobClass(jobClass);
		modelService.save(action);

		final List<WorkflowActionTemplateModel> coll = new ArrayList(workflow.getActions());
		coll.add(action);
		workflow.setActions(coll);
		modelService.save(workflow);

		assertNotNull("Automated Action Template should not be null", action);
		return action;
	}

	/**
	 * Creates new decision template.
	 * 
	 * @param code
	 *           code of template
	 * @param actionTemplate
	 *           action template assigned to decision template
	 * @return created decision template
	 */
	protected WorkflowDecisionTemplateModel createWorkflowDecisionTemplate(final String code,
			final WorkflowActionTemplateModel actionTemplate)
	{
		final WorkflowDecisionTemplateModel decision = modelService.create(WorkflowDecisionTemplateModel.class);
		decision.setCode(code);
		decision.setActionTemplate(actionTemplate);
		modelService.save(decision);


		final Collection<WorkflowDecisionTemplateModel> coll = new ArrayList(actionTemplate.getDecisionTemplates());
		coll.add(decision);
		actionTemplate.setDecisionTemplates(coll);
		modelService.save(actionTemplate);

		assertNotNull("The decision should not be null", decision);
		assertThat(modelService.isUpToDate(decision)).isTrue();
		return decision;
	}

	/**
	 * 
	 * @param decisionTemplate
	 * @param toAction
	 * @param hasAndConnection
	 */
	protected void createWorkflowActionTemplateModelLinkTemplateRelation(final WorkflowDecisionTemplateModel decisionTemplate,
			final WorkflowActionTemplateModel toAction, final Boolean hasAndConnection)
	{
		final List<WorkflowActionTemplateModel> toTemplateActions = new ArrayList<WorkflowActionTemplateModel>(
				decisionTemplate.getToTemplateActions());
		toTemplateActions.add(toAction);
		decisionTemplate.setToTemplateActions(toTemplateActions);
		modelService.save(decisionTemplate);

		assertThat(modelService.isUpToDate(decisionTemplate)).isTrue();

		final Collection<LinkModel> incomingLinkList = getLinkTemplates(decisionTemplate, toAction);

		for (final LinkModel link : incomingLinkList)
		{
			setAndConnectionTemplate(link, hasAndConnection);
		}

	}

	private Collection<LinkModel> getLinkTemplates(final AbstractWorkflowDecisionModel decision,
			final AbstractWorkflowActionModel action)
	{
		SearchResult<LinkModel> results;
		final Map params = new HashMap();
		params.put("desc", decision);
		params.put("act", action);

		if (decision == null && action == null)
		{
			throw new NullPointerException("Decision and action cannot both be null");
		}
		else if (action == null)
		{
			results = flexibleSearchService.search("SELECT {" + ItemModel.PK + "} from {"
					+ "WorkflowActionTemplateLinkTemplateRelation" + "} where {" + LinkModel.SOURCE + "}=?desc", params);
		}
		else if (decision == null)
		{
			results = flexibleSearchService.search("SELECT {" + ItemModel.PK + "} from {"
					+ "WorkflowActionTemplateLinkTemplateRelation" + "} where {" + LinkModel.TARGET + "}=?act", params);
		}
		else
		{
			results = flexibleSearchService.search("SELECT {" + ItemModel.PK + "} from {"
					+ "WorkflowActionTemplateLinkTemplateRelation" + "} where {" + LinkModel.SOURCE + "}=?desc AND {"
					+ LinkModel.TARGET + "}=?act", params);
			if (results.getTotalCount() != 1)
			{
				// if source and target are specified only an Item can be returned.
				LOG.error("There is more than one WorkflowActionTemplateLinkTemplateRelation for source '" + decision.getCode()
						+ "' and target '" + action.getCode() + "'");
			}
		}
		return results.getResult();
	}

	private void setAndConnectionTemplate(final LinkModel item, final Boolean value)
	{
		final Link itemSource = modelService.getSource(item);
		itemSource
				.setProperty(WorkflowConstants.Attributes.WorkflowActionTemplateLinkTemplateRelation.ANDCONNECTIONTEMPLATE, value);
	}

	/**
	 * Gets the action with given code from test workflow instance.
	 * 
	 * @param code
	 *           code of needed action
	 * @return action of test workflow with given code
	 */
	protected WorkflowActionModel getAction(final String code)
	{
		final Collection<WorkflowActionModel> actions = testWorkflow.getActions();
		for (final WorkflowActionModel action : actions)
		{
			if (action.getTemplate().getCode().equals(code))
			{
				return action;
			}
		}
		fail("Action " + code + "can not be found");
		return null;
	}

	/**
	 * Gets the decision with given code from test workflow instance.
	 * 
	 * @param code
	 *           code of needed decision
	 * @return decision of test workflow with given code
	 */
	protected WorkflowDecisionModel getDecision(final String code, final WorkflowActionModel action)
	{
		final Collection<WorkflowDecisionModel> decisions = action.getDecisions();
		for (final WorkflowDecisionModel decision : decisions)
		{
			if (decision.getCode().equals(code))
			{
				return decision;
			}
		}
		fail("Decision " + code + "can not be found");
		return null;
	}

	/**
	 * Gets the action template with given code from test workflow template.
	 * 
	 * @param code
	 *           code of needed action
	 * @return action template of test workflow template with given code
	 */
	protected WorkflowActionTemplateModel getActionTemplate(final String code)
	{
		final Collection<WorkflowActionTemplateModel> actions = testTemplate.getActions();
		for (final WorkflowActionTemplateModel action : actions)
		{
			if (action.getCode().equals(code))
			{
				return action;
			}
		}
		fail("ActionTemplate " + code + "can not be found");
		return null;
	}
}
