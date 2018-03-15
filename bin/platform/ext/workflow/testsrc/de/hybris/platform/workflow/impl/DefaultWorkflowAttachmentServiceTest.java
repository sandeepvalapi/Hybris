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
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultWorkflowAttachmentServiceTest
{
	DefaultWorkflowAttachmentService workflowAttachmentService;

	@Mock
	private ModelService modelService;

	@Mock
	private TypeService typeService;

	@Mock
	private WorkflowModel mockWorkflow;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		workflowAttachmentService = new DefaultWorkflowAttachmentService();
		workflowAttachmentService.setModelService(modelService);
		workflowAttachmentService.setTypeService(typeService);
	}

	@Test
	public void testAddItems()
	{
		//given
		final WorkflowItemAttachmentModel mockWIAttachment = mock(WorkflowItemAttachmentModel.class);
		when(modelService.create(WorkflowItemAttachmentModel.class)).thenReturn(mockWIAttachment);
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockWorkflow.getActions()).thenReturn(Collections.singletonList(mockAction));
		final List<ItemModel> itemsToAdd = new ArrayList<ItemModel>();
		itemsToAdd.add(mock(ItemModel.class));
		itemsToAdd.add(mock(ItemModel.class));

		//when
		final List<WorkflowItemAttachmentModel> workflowItemAttachments = workflowAttachmentService.addItems(mockWorkflow,
				itemsToAdd);

		//then
		assertThat(workflowItemAttachments).hasSize(2);
	}

	@Test
	public void testContainsItemAndFound()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockWorkflow.getActions()).thenReturn(Collections.singletonList(mockAction));
		final List<ItemModel> itemsToAdd = new ArrayList<ItemModel>();
		final ItemModel mockItem = mock(ItemModel.class);
		itemsToAdd.add(mockItem);
		itemsToAdd.add(mock(ItemModel.class));

		final WorkflowItemAttachmentModel mockWIAttachment = mock(WorkflowItemAttachmentModel.class);
		when(mockWIAttachment.getItem()).thenReturn(mockItem);
		when(mockWorkflow.getAttachments()).thenReturn(Collections.singletonList(mockWIAttachment));

		//when
		final ItemModel item = workflowAttachmentService.containsItem(mockWorkflow, itemsToAdd);

		//then
		assertThat(item).isNotNull();
	}

	@Test
	public void testContainsItemAndNotFound()
	{
		//when
		final ItemModel item = workflowAttachmentService.containsItem(mockWorkflow, Collections.EMPTY_LIST);

		//then
		assertThat(item).isNull();
	}

	@Test
	public void testGetAttachmentsForAction()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		final ItemModel mockItem = mock(ItemModel.class);
		when(mockItem.getItemtype()).thenReturn(ProductModel._TYPECODE);
		when(mockAction.getAttachmentItems()).thenReturn(Collections.singletonList(mockItem));
		when(Boolean.valueOf(typeService.isAssignableFrom(ProductModel._TYPECODE, ProductModel._TYPECODE)))
				.thenReturn(Boolean.TRUE);
		//when
		final List<ItemModel> items = workflowAttachmentService.getAttachmentsForAction(mockAction);

		//then
		assertThat(items).isNotEmpty();
	}

	@Test
	public void testGetAttachmentsForActionWithCollectionOfClassNames()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		final ItemModel mockItem = mock(ItemModel.class);
		when(mockItem.getItemtype()).thenReturn(ProductModel._TYPECODE);
		when(mockAction.getAttachmentItems()).thenReturn(Collections.singletonList(mockItem));
		when(Boolean.valueOf(typeService.isAssignableFrom(ProductModel._TYPECODE, ProductModel._TYPECODE)))
				.thenReturn(Boolean.TRUE);
		final ComposedTypeModel mockType = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForClass(ProductModel.class)).thenReturn(mockType);
		when(mockType.getCode()).thenReturn(ProductModel._TYPECODE);

		//when
		final List<ItemModel> items = workflowAttachmentService.getAttachmentsForAction(mockAction,
				Collections.singletonList(ProductModel.class.getName()));

		//then
		assertThat(items).isNotEmpty();
	}

	@Test
	public void testGetAttachmentsForActionWithSingleClassName()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		final ItemModel mockItem = mock(ItemModel.class);
		when(mockItem.getItemtype()).thenReturn(ProductModel._TYPECODE);
		when(mockAction.getAttachmentItems()).thenReturn(Collections.singletonList(mockItem));
		when(Boolean.valueOf(typeService.isAssignableFrom(ProductModel._TYPECODE, ProductModel._TYPECODE)))
				.thenReturn(Boolean.TRUE);
		final ComposedTypeModel mockType = mock(ComposedTypeModel.class);
		when(typeService.getComposedTypeForClass(ProductModel.class)).thenReturn(mockType);
		when(mockType.getCode()).thenReturn(ProductModel._TYPECODE);

		//when
		final List<ItemModel> items = workflowAttachmentService.getAttachmentsForAction(mockAction, ProductModel.class.getName());

		//then
		assertThat(items).isNotEmpty();
	}

    @Test
    public void testGetAttachmentsForActionForUnknownClassName()
    {
        try
        {
            TestUtils.disableFileAnalyzer(" expected unknownClass class ");
            //when
            final List<ItemModel> items = workflowAttachmentService.getAttachmentsForAction(null, "unknownClass");

            //then
            assertThat(items).isEmpty();
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
        }
    }
}
