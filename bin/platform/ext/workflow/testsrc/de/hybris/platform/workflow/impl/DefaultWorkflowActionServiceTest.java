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
package de.hybris.platform.workflow.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants.Attributes.Product;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.daos.WorkflowActionDao;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultWorkflowActionServiceTest
{
	DefaultWorkflowActionService workflowActionService;

	@Mock
	private ModelService modelService;
	@Mock
	private UserService userService;
	@Mock
	private WorkflowActionDao workflowActionDao;
	@Mock
	private TypeService typeService;
	@Mock
	private WorkflowModel workflow;

	@Mock
	private I18NService i18n;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		workflowActionService = new DefaultWorkflowActionService();
		workflowActionService.setModelService(modelService);
		workflowActionService.setUserService(userService);
		workflowActionService.setWorkflowActionDao(workflowActionDao);
		workflowActionService.setTypeService(typeService);
		workflowActionService.setI18nService(i18n);
	}

	@Test
	public void testGetEndWorkflowActions()
	{
		//given
		final List<WorkflowActionModel> actions = new ArrayList<WorkflowActionModel>();
		actions.add(mock(WorkflowActionModel.class));
		when(workflowActionDao.findEndWorkflowActions(workflow)).thenReturn(actions);

		//when
		final List<WorkflowActionModel> endWorkflowActions = workflowActionService.getEndWorkflowActions(workflow);

		//then

		assertThat(endWorkflowActions).isSameAs(actions);
	}

	@Test
	public void testGetNormalWorkflowActions()
	{
		//given
		final List<WorkflowActionModel> actions = new ArrayList<WorkflowActionModel>();
		actions.add(mock(WorkflowActionModel.class));
		when(workflowActionDao.findNormalWorkflowActions(workflow)).thenReturn(actions);

		//when
		final List<WorkflowActionModel> endWorkflowActions = workflowActionService.getNormalWorkflowActions(workflow);

		//then

		assertThat(endWorkflowActions).isSameAs(actions);
	}

	@Test
	public void testGetStartWorkflowActions()
	{
		//given
		final List<WorkflowActionModel> actions = new ArrayList<WorkflowActionModel>();
		actions.add(mock(WorkflowActionModel.class));
		when(workflowActionDao.findStartWorkflowActions(workflow)).thenReturn(actions);

		//when
		final List<WorkflowActionModel> endWorkflowActions = workflowActionService.getStartWorkflowActions(workflow);

		//then

		assertThat(endWorkflowActions).isSameAs(actions);
	}

	@Test
	public void testGetAllUserWorkflowActionsWithAttachments()
	{
		//given
		final List<WorkflowActionModel> actions = new ArrayList<WorkflowActionModel>();
		actions.add(mock(WorkflowActionModel.class));
		final ComposedTypeModel mockProductComposedType = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForCode(ProductModel._TYPECODE)).thenReturn(mockProductComposedType);
		when(
				workflowActionDao.findWorkflowActionsByStatusAndAttachmentType(Collections.singletonList(mockProductComposedType),
						Collections.singletonList(WorkflowActionStatus.IN_PROGRESS))).thenReturn(actions);

		final List<String> attachmentTypes = new ArrayList<String>();
		attachmentTypes.add(ProductModel._TYPECODE);

		//when
		final List<WorkflowActionModel> endWorkflowActions = workflowActionService
				.getAllUserWorkflowActionsWithAttachments(attachmentTypes);

		//then
		assertThat(endWorkflowActions).isSameAs(actions);
	}

	@Test
	public void testGetAllUserWorkflowActionsWithAttachmentsWhenUnknownClass()
	{
		//given
		final String COMPOSED_TYPE_CODE = "unknownClass";
		final List<WorkflowActionModel> actions = new ArrayList<WorkflowActionModel>();
		actions.add(mock(WorkflowActionModel.class));
		final ComposedTypeModel mockProductComposedType = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForCode(COMPOSED_TYPE_CODE)).thenThrow(new UnknownIdentifierException(""));
		when(typeService.getComposedTypeForClass(ProductModel.class)).thenReturn(mockProductComposedType);
		when(
				workflowActionDao.findWorkflowActionsByStatusAndAttachmentType(Collections.singletonList(mockProductComposedType),
						Collections.singletonList(WorkflowActionStatus.IN_PROGRESS))).thenReturn(actions);

		final List<String> attachmentTypes = new ArrayList<String>();
		attachmentTypes.add(COMPOSED_TYPE_CODE);

		//when
		final List<WorkflowActionModel> endWorkflowActions = workflowActionService
				.getAllUserWorkflowActionsWithAttachments(attachmentTypes);

		//then
		assertThat(endWorkflowActions).isEmpty();
	}

	@Test
	public void testGetAllUserWorkflowActionsWithAttachmentForString()
	{
		//given
		final List<WorkflowActionModel> actions = new ArrayList<WorkflowActionModel>();
		actions.add(mock(WorkflowActionModel.class));
		final ComposedTypeModel mockProductComposedType = mock(ComposedTypeModel.class);
		when(mockProductComposedType.getJaloclass()).thenReturn(Product.class);
		when(typeService.getComposedTypeForCode(ProductModel._TYPECODE)).thenReturn(mockProductComposedType);
		when(
				workflowActionDao.findWorkflowActionsByStatusAndAttachmentType(Collections.singletonList(mockProductComposedType),
						Collections.singletonList(WorkflowActionStatus.IN_PROGRESS))).thenReturn(actions);

		//when
		final List<WorkflowActionModel> endWorkflowActions = workflowActionService
				.getAllUserWorkflowActionsWithAttachment(ProductModel._TYPECODE);

		//then
		assertThat(endWorkflowActions).isSameAs(actions);
	}

	@Test
	public void testGetAllUserWorkflowActionsWithAttachmentForComposedType()
	{
		//given
		final List<WorkflowActionModel> actions = new ArrayList<WorkflowActionModel>();
		actions.add(mock(WorkflowActionModel.class));
		final ComposedTypeModel mockProductComposedType = mock(ComposedTypeModel.class);

		when(mockProductComposedType.getJaloclass()).thenReturn(Product.class);
		when(typeService.getComposedTypeForCode(ProductModel._TYPECODE)).thenReturn(mockProductComposedType);
		when(
				workflowActionDao.findWorkflowActionsByStatusAndAttachmentType(Collections.singletonList(mockProductComposedType),
						Collections.singletonList(WorkflowActionStatus.IN_PROGRESS))).thenReturn(actions);

		//when
		final List<WorkflowActionModel> endWorkflowActions = workflowActionService
				.getAllUserWorkflowActionsWithAttachment(mockProductComposedType);

		//then
		assertThat(endWorkflowActions).isSameAs(actions);
	}

	@Test
	public void testCheckStates()
	{
		//given
		final WorkflowActionModel mockAction1 = getActionWithStatus(WorkflowActionStatus.IN_PROGRESS);
		final WorkflowActionModel mockAction2 = getActionWithStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		final WorkflowActionModel mockAction3 = getActionWithStatus(WorkflowActionStatus.COMPLETED);
		final WorkflowActionModel mockAction4 = getActionWithStatus(WorkflowActionStatus.DISABLED);
		final WorkflowActionModel mockAction5 = getActionWithStatus(WorkflowActionStatus.PAUSED);
		final WorkflowActionModel mockAction6 = getActionWithStatus(WorkflowActionStatus.PENDING);
		final WorkflowActionModel mockAction7 = getActionWithStatus(WorkflowActionStatus.TERMINATED);

		//when
		final boolean isActive = workflowActionService.isActive(mockAction1);
		final boolean isEndedByWorkflow = workflowActionService.isEndedByWorkflow(mockAction2);
		final boolean isCompleted = workflowActionService.isCompleted(mockAction3);
		final boolean isDisabled = workflowActionService.isDisabled(mockAction4);
		final boolean isCompleted2 = workflowActionService.isDisabled(mockAction4);
		final boolean isNotActive = workflowActionService.isActive(mockAction5);
		final boolean isNotCompleted = workflowActionService.isCompleted(mockAction6);
		final boolean isNotDisabled = workflowActionService.isDisabled(mockAction7);

		//then
		assertThat(isActive).isTrue();
		assertThat(isEndedByWorkflow).isTrue();
		assertThat(isCompleted).isTrue();
		assertThat(isDisabled).isTrue();
		assertThat(isCompleted2).isTrue();
		assertThat(isNotActive).isFalse();
		assertThat(isNotCompleted).isFalse();
		assertThat(isNotDisabled).isFalse();
	}

	private WorkflowActionModel getActionWithStatus(final WorkflowActionStatus status)
	{
		final WorkflowActionModel mockAction1 = mock(WorkflowActionModel.class);
		when(mockAction1.getStatus()).thenReturn(status);
		return mockAction1;
	}

	@Test
	public void testDisableAction()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);

		//when
		final WorkflowActionModel disabledAction = workflowActionService.disable(mockAction);

		//then
		verify(disabledAction).setStatus(WorkflowActionStatus.DISABLED);
	}

	@Test
	public void testCompleteAction()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);

		//when
		final WorkflowActionModel compledAction = workflowActionService.complete(mockAction);

		//then
		verify(compledAction).setStatus(WorkflowActionStatus.COMPLETED);
	}

	@Test
	public void testIdleAction()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);

		//when
		final WorkflowActionModel idledAction = workflowActionService.idle(mockAction);

		//then
		verify(idledAction).setStatus(WorkflowActionStatus.PENDING);
	}

	@Test
	public void testGetWorkflowActionsByType()
	{
		//given
		final List<WorkflowActionModel> mockActions = new ArrayList<WorkflowActionModel>();
		when(workflowActionDao.findWorkflowActionsByType(WorkflowActionType.END, workflow)).thenReturn(mockActions);

		//when
		final List<WorkflowActionModel> actions = workflowActionService.getWorkflowActionsByType(WorkflowActionType.END, workflow);

		//then
		assertThat(actions).isSameAs(mockActions);
	}

	@Test
	public void testIsUserAssignedPrincipalForAdmin()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		final UserModel mockUser = mock(UserModel.class);
		when(Boolean.valueOf(userService.isAdmin(mockUser))).thenReturn(Boolean.TRUE);
		when(userService.getCurrentUser()).thenReturn(mockUser);

		//when
		final boolean isAssigned = workflowActionService.isUserAssignedPrincipal(mockAction);

		//then
		assertThat(isAssigned).isTrue();
	}

	@Test
	public void testIsUserAssignedPrincipalForSameUser()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		final UserModel mockUser = mock(UserModel.class);
		when(mockAction.getPrincipalAssigned()).thenReturn(mockUser);
		when(userService.getCurrentUser()).thenReturn(mockUser);

		//when
		final boolean isAssigned = workflowActionService.isUserAssignedPrincipal(mockAction);

		//then
		assertThat(isAssigned).isTrue();
	}

	@Test
	public void testIsUserAssignedPrincipalForSameGroup()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		final UserModel mockUser = mock(UserModel.class);
		final UserGroupModel mockGroup = mock(UserGroupModel.class);
		when(mockAction.getPrincipalAssigned()).thenReturn(mockGroup);
		when(userService.getCurrentUser()).thenReturn(mockUser);
		when(mockUser.getAllGroups()).thenReturn((Set) Collections.singleton(mockGroup));

		//when
		final boolean isAssigned = workflowActionService.isUserAssignedPrincipal(mockAction);

		//then
		assertThat(isAssigned).isTrue();
	}

	@Test
	public void testIsUserAssignedPrincipalFail()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		final UserModel mockUser = mock(UserModel.class);
		when(userService.getCurrentUser()).thenReturn(mockUser);

		//when
		final boolean isAssigned = workflowActionService.isUserAssignedPrincipal(mockAction);

		//then
		assertThat(isAssigned).isFalse();
	}

	@Test
	public void testCreateWorkflowAction()
	{
		//given
		final WorkflowActionTemplateModel template = mock(WorkflowActionTemplateModel.class);
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(modelService.create(WorkflowActionModel.class)).thenReturn(mockAction);
		final ComposedTypeModel composedTypeModel = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForCode(AbstractWorkflowActionModel._TYPECODE)).thenReturn(composedTypeModel);
		when(composedTypeModel.getDeclaredattributedescriptors()).thenReturn(new ArrayList<AttributeDescriptorModel>());
		//when
		final WorkflowActionModel action = workflowActionService.createWorkflowAction(template, workflow);

		//then
		assertThat(action).isNotNull();
		assertThat(action).isSameAs(mockAction);
	}

}
