/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type WorkflowItemAttachment first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowItemAttachmentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WorkflowItemAttachment";
	
	/**<i>Generated relation code constant for relation <code>WorkflowItemAttachmentRelation</code> defining source attribute <code>workflow</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWITEMATTACHMENTRELATION = "WorkflowItemAttachmentRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionItemAttachmentRelation</code> defining source attribute <code>actions</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONITEMATTACHMENTRELATION = "WorkflowActionItemAttachmentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.code</code> attribute defined at extension <code>workflow</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.comment</code> attribute defined at extension <code>workflow</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.item</code> attribute defined at extension <code>workflow</code>. */
	public static final String ITEM = "item";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.typeOfItem</code> attribute defined at extension <code>workflow</code>. */
	public static final String TYPEOFITEM = "typeOfItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.actionStr</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONSTR = "actionStr";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.workflowPOS</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOWPOS = "workflowPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.workflow</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOW = "workflow";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.actions</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONS = "actions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowItemAttachmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowItemAttachmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _item initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowItemAttachmentModel(final ItemModel _item, final WorkflowModel _workflow)
	{
		super();
		setItem(_item);
		setWorkflow(_workflow);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 * @param _item initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowItemAttachmentModel(final String _code, final ItemModel _item, final ItemModel _owner, final WorkflowModel _workflow)
	{
		super();
		setCode(_code);
		setItem(_item);
		setOwner(_owner);
		setWorkflow(_workflow);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.actions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the actions - part of WorkflowActionItemAttachmentRelation; references specific actions of referenced workflow for which attachment is relevant for processing
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
	public Collection<WorkflowActionModel> getActions()
	{
		return getPersistenceContext().getPropertyValue(ACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.actionStr</code> attribute defined at extension <code>workflow</code>. 
	 * @return the actionStr
	 */
	@Accessor(qualifier = "actionStr", type = Accessor.Type.GETTER)
	public String getActionStr()
	{
		return getPersistenceContext().getPropertyValue(ACTIONSTR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.code</code> attribute defined at extension <code>workflow</code>. 
	 * @return the code - identifier of this attachment; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.comment</code> attribute defined at extension <code>workflow</code>. 
	 * @return the comment - a comment containing some notes either by creator or processor
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public String getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.item</code> attribute defined at extension <code>workflow</code>. 
	 * @return the item - the item this attachment references
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.GETTER)
	public ItemModel getItem()
	{
		return getPersistenceContext().getPropertyValue(ITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. 
	 * @return the name - name of the attachment
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. 
	 * @param loc the value localization key 
	 * @return the name - name of the attachment
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.typeOfItem</code> attribute defined at extension <code>workflow</code>. 
	 * @return the typeOfItem
	 */
	@Accessor(qualifier = "typeOfItem", type = Accessor.Type.GETTER)
	public ComposedTypeModel getTypeOfItem()
	{
		return getPersistenceContext().getPropertyValue(TYPEOFITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.workflow</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflow - 1-part of WorkflowItemAttachmentRelation; references the related workflow this attachment belongs to
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
	public WorkflowModel getWorkflow()
	{
		return getPersistenceContext().getPropertyValue(WORKFLOW);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.actions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the actions - part of WorkflowActionItemAttachmentRelation; references specific actions of referenced workflow for which attachment is relevant for processing
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
	public void setActions(final Collection<WorkflowActionModel> value)
	{
		getPersistenceContext().setPropertyValue(ACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowItemAttachment.code</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - identifier of this attachment; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.comment</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the comment - a comment containing some notes either by creator or processor
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.item</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the item - the item this attachment references
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.SETTER)
	public void setItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(ITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the attachment
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the attachment
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowItemAttachment.workflow</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the workflow - 1-part of WorkflowItemAttachmentRelation; references the related workflow this attachment belongs to
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
	public void setWorkflow(final WorkflowModel value)
	{
		getPersistenceContext().setPropertyValue(WORKFLOW, value);
	}
	
}
