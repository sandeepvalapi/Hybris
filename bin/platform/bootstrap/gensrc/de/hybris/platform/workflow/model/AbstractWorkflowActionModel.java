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
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type AbstractWorkflowAction first defined at extension workflow.
 */
@SuppressWarnings("all")
public class AbstractWorkflowActionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractWorkflowAction";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionOrderingRelation</code> defining source attribute <code>predecessors</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONORDERINGRELATION = "WorkflowActionOrderingRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.actionType</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONTYPE = "actionType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.code</code> attribute defined at extension <code>workflow</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.name</code> attribute defined at extension <code>workflow</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.description</code> attribute defined at extension <code>workflow</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.principalAssigned</code> attribute defined at extension <code>workflow</code>. */
	public static final String PRINCIPALASSIGNED = "principalAssigned";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.sendEmail</code> attribute defined at extension <code>workflow</code>. */
	public static final String SENDEMAIL = "sendEmail";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.emailAddress</code> attribute defined at extension <code>workflow</code>. */
	public static final String EMAILADDRESS = "emailAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.predecessorsStr</code> attribute defined at extension <code>workflow</code>. */
	public static final String PREDECESSORSSTR = "predecessorsStr";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.rendererTemplate</code> attribute defined at extension <code>workflow</code>. */
	public static final String RENDERERTEMPLATE = "rendererTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.predecessors</code> attribute defined at extension <code>workflow</code>. */
	public static final String PREDECESSORS = "predecessors";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.successors</code> attribute defined at extension <code>workflow</code>. */
	public static final String SUCCESSORS = "successors";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractWorkflowAction.workflowActionComments</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOWACTIONCOMMENTS = "workflowActionComments";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractWorkflowActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractWorkflowActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public AbstractWorkflowActionModel(final WorkflowActionType _actionType, final PrincipalModel _principalAssigned)
	{
		super();
		setActionType(_actionType);
		setPrincipalAssigned(_principalAssigned);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _code initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public AbstractWorkflowActionModel(final WorkflowActionType _actionType, final String _code, final UserModel _owner, final PrincipalModel _principalAssigned)
	{
		super();
		setActionType(_actionType);
		setCode(_code);
		setOwner(_owner);
		setPrincipalAssigned(_principalAssigned);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.actionType</code> attribute defined at extension <code>workflow</code>. 
	 * @return the actionType - marker for the type of an action (start, end, normal)
	 */
	@Accessor(qualifier = "actionType", type = Accessor.Type.GETTER)
	public WorkflowActionType getActionType()
	{
		return getPersistenceContext().getPropertyValue(ACTIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.code</code> attribute defined at extension <code>workflow</code>. 
	 * @return the code - unique identifier of the action
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.description</code> attribute defined at extension <code>workflow</code>. 
	 * @return the description - description of the action; should contain the steps the assigned principal has to perform
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.description</code> attribute defined at extension <code>workflow</code>. 
	 * @param loc the value localization key 
	 * @return the description - description of the action; should contain the steps the assigned principal has to perform
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.emailAddress</code> attribute defined at extension <code>workflow</code>. 
	 * @return the emailAddress - e-mail address where notification e-mail will be sent to
	 */
	@Accessor(qualifier = "emailAddress", type = Accessor.Type.GETTER)
	public String getEmailAddress()
	{
		return getPersistenceContext().getPropertyValue(EMAILADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.name</code> attribute defined at extension <code>workflow</code>. 
	 * @return the name - name of the action
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.name</code> attribute defined at extension <code>workflow</code>. 
	 * @param loc the value localization key 
	 * @return the name - name of the action
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>workflow</code>. 
	 * @return the owner
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
	public UserModel getOwner()
	{
		return (UserModel) super.getOwner();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.predecessors</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the predecessors
	 */
	@Accessor(qualifier = "predecessors", type = Accessor.Type.GETTER)
	public List<AbstractWorkflowActionModel> getPredecessors()
	{
		return getPersistenceContext().getPropertyValue(PREDECESSORS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.predecessorsStr</code> attribute defined at extension <code>workflow</code>. 
	 * @return the predecessorsStr
	 */
	@Accessor(qualifier = "predecessorsStr", type = Accessor.Type.GETTER)
	public String getPredecessorsStr()
	{
		return getPersistenceContext().getPropertyValue(PREDECESSORSSTR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.principalAssigned</code> attribute defined at extension <code>workflow</code>. 
	 * @return the principalAssigned - assigned principal who has to assure the completion (can also be a principal group where any member of the group can process the action)
	 */
	@Accessor(qualifier = "principalAssigned", type = Accessor.Type.GETTER)
	public PrincipalModel getPrincipalAssigned()
	{
		return getPersistenceContext().getPropertyValue(PRINCIPALASSIGNED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.rendererTemplate</code> attribute defined at extension <code>workflow</code>. 
	 * @return the rendererTemplate
	 */
	@Accessor(qualifier = "rendererTemplate", type = Accessor.Type.GETTER)
	public RendererTemplateModel getRendererTemplate()
	{
		return getPersistenceContext().getPropertyValue(RENDERERTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.sendEmail</code> attribute defined at extension <code>workflow</code>. 
	 * @return the sendEmail - if activated a notification e-mail will be sent when action gets active
	 */
	@Accessor(qualifier = "sendEmail", type = Accessor.Type.GETTER)
	public Boolean getSendEmail()
	{
		return getPersistenceContext().getPropertyValue(SENDEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.successors</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the successors
	 */
	@Accessor(qualifier = "successors", type = Accessor.Type.GETTER)
	public List<AbstractWorkflowActionModel> getSuccessors()
	{
		return getPersistenceContext().getPropertyValue(SUCCESSORS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.workflowActionComments</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the workflowActionComments
	 */
	@Accessor(qualifier = "workflowActionComments", type = Accessor.Type.GETTER)
	public Collection<WorkflowActionCommentModel> getWorkflowActionComments()
	{
		return getPersistenceContext().getPropertyValue(WORKFLOWACTIONCOMMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.actionType</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the actionType - marker for the type of an action (start, end, normal)
	 */
	@Accessor(qualifier = "actionType", type = Accessor.Type.SETTER)
	public void setActionType(final WorkflowActionType value)
	{
		getPersistenceContext().setPropertyValue(ACTIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractWorkflowAction.code</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - unique identifier of the action
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.description</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the description - description of the action; should contain the steps the assigned principal has to perform
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.description</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the description - description of the action; should contain the steps the assigned principal has to perform
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.emailAddress</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the emailAddress - e-mail address where notification e-mail will be sent to
	 */
	@Accessor(qualifier = "emailAddress", type = Accessor.Type.SETTER)
	public void setEmailAddress(final String value)
	{
		getPersistenceContext().setPropertyValue(EMAILADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the action
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the action
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>workflow</code>. Will only accept values of type {@link de.hybris.platform.core.model.user.UserModel}. 
	 *  
	 * @param value the owner
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
	public void setOwner(final ItemModel value)
	{
		if( value == null || value instanceof UserModel)
		{
			super.setOwner(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.user.UserModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.predecessors</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the predecessors
	 */
	@Accessor(qualifier = "predecessors", type = Accessor.Type.SETTER)
	public void setPredecessors(final List<AbstractWorkflowActionModel> value)
	{
		getPersistenceContext().setPropertyValue(PREDECESSORS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.principalAssigned</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the principalAssigned - assigned principal who has to assure the completion (can also be a principal group where any member of the group can process the action)
	 */
	@Accessor(qualifier = "principalAssigned", type = Accessor.Type.SETTER)
	public void setPrincipalAssigned(final PrincipalModel value)
	{
		getPersistenceContext().setPropertyValue(PRINCIPALASSIGNED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.rendererTemplate</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the rendererTemplate
	 */
	@Accessor(qualifier = "rendererTemplate", type = Accessor.Type.SETTER)
	public void setRendererTemplate(final RendererTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(RENDERERTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.sendEmail</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the sendEmail - if activated a notification e-mail will be sent when action gets active
	 */
	@Accessor(qualifier = "sendEmail", type = Accessor.Type.SETTER)
	public void setSendEmail(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SENDEMAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.successors</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the successors
	 */
	@Accessor(qualifier = "successors", type = Accessor.Type.SETTER)
	public void setSuccessors(final List<AbstractWorkflowActionModel> value)
	{
		getPersistenceContext().setPropertyValue(SUCCESSORS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractWorkflowAction.workflowActionComments</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the workflowActionComments
	 */
	@Accessor(qualifier = "workflowActionComments", type = Accessor.Type.SETTER)
	public void setWorkflowActionComments(final Collection<WorkflowActionCommentModel> value)
	{
		getPersistenceContext().setPropertyValue(WORKFLOWACTIONCOMMENTS, value);
	}
	
}
