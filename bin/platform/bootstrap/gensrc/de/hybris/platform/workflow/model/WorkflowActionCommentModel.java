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
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;

/**
 * Generated model class for type WorkflowActionComment first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowActionCommentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WorkflowActionComment";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionCommentRelation</code> defining source attribute <code>workflowAction</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONCOMMENTRELATION = "WorkflowActionCommentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionComment.comment</code> attribute defined at extension <code>workflow</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionComment.user</code> attribute defined at extension <code>workflow</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionComment.workflowAction</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOWACTION = "workflowAction";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowActionCommentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowActionCommentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>WorkflowActionComment</code> at extension <code>workflow</code>
	 * @param _workflowAction initial attribute declared by type <code>WorkflowActionComment</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionCommentModel(final String _comment, final AbstractWorkflowActionModel _workflowAction)
	{
		super();
		setComment(_comment);
		setWorkflowAction(_workflowAction);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>WorkflowActionComment</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _workflowAction initial attribute declared by type <code>WorkflowActionComment</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionCommentModel(final String _comment, final ItemModel _owner, final AbstractWorkflowActionModel _workflowAction)
	{
		super();
		setComment(_comment);
		setOwner(_owner);
		setWorkflowAction(_workflowAction);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionComment.comment</code> attribute defined at extension <code>workflow</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public String getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionComment.user</code> attribute defined at extension <code>workflow</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionComment.workflowAction</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflowAction
	 */
	@Accessor(qualifier = "workflowAction", type = Accessor.Type.GETTER)
	public AbstractWorkflowActionModel getWorkflowAction()
	{
		return getPersistenceContext().getPropertyValue(WORKFLOWACTION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionComment.comment</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionComment.user</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionComment.workflowAction</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the workflowAction
	 */
	@Accessor(qualifier = "workflowAction", type = Accessor.Type.SETTER)
	public void setWorkflowAction(final AbstractWorkflowActionModel value)
	{
		getPersistenceContext().setPropertyValue(WORKFLOWACTION, value);
	}
	
}
