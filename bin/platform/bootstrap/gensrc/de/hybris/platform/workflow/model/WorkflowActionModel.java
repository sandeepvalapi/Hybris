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
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type WorkflowAction first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowActionModel extends AbstractWorkflowActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WorkflowAction";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionOrderingRelation</code> defining source attribute <code>predecessors</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONORDERINGRELATION = "WorkflowActionOrderingRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionRelation</code> defining source attribute <code>workflow</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONRELATION = "WorkflowActionRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionLinkRelation</code> defining source attribute <code>incomingDecisions</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONLINKRELATION = "WorkflowActionLinkRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.incomingLinks</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGLINKS = "incomingLinks";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.incomingLinksStr</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGLINKSSTR = "incomingLinksStr";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.selectedDecision</code> attribute defined at extension <code>workflow</code>. */
	public static final String SELECTEDDECISION = "selectedDecision";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.firstActivated</code> attribute defined at extension <code>workflow</code>. */
	public static final String FIRSTACTIVATED = "firstActivated";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.activated</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIVATED = "activated";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.comment</code> attribute defined at extension <code>workflow</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.status</code> attribute defined at extension <code>workflow</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.template</code> attribute defined at extension <code>workflow</code>. */
	public static final String TEMPLATE = "template";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.attachmentItems</code> attribute defined at extension <code>workflow</code>. */
	public static final String ATTACHMENTITEMS = "attachmentItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.workflowPOS</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOWPOS = "workflowPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.workflow</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOW = "workflow";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.decisions</code> attribute defined at extension <code>workflow</code>. */
	public static final String DECISIONS = "decisions";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.incomingDecisions</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGDECISIONS = "incomingDecisions";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.attachments</code> attribute defined at extension <code>workflow</code>. */
	public static final String ATTACHMENTS = "attachments";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _template initial attribute declared by type <code>WorkflowAction</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowAction</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionModel(final WorkflowActionType _actionType, final PrincipalModel _principalAssigned, final WorkflowActionTemplateModel _template, final WorkflowModel _workflow)
	{
		super();
		setActionType(_actionType);
		setPrincipalAssigned(_principalAssigned);
		setTemplate(_template);
		setWorkflow(_workflow);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _code initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _template initial attribute declared by type <code>WorkflowAction</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowAction</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionModel(final WorkflowActionType _actionType, final String _code, final UserModel _owner, final PrincipalModel _principalAssigned, final WorkflowActionTemplateModel _template, final WorkflowModel _workflow)
	{
		super();
		setActionType(_actionType);
		setCode(_code);
		setOwner(_owner);
		setPrincipalAssigned(_principalAssigned);
		setTemplate(_template);
		setWorkflow(_workflow);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.activated</code> attribute defined at extension <code>workflow</code>. 
	 * @return the activated - date of last activation
	 */
	@Accessor(qualifier = "activated", type = Accessor.Type.GETTER)
	public Date getActivated()
	{
		return getPersistenceContext().getPropertyValue(ACTIVATED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.attachmentItems</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the attachmentItems
	 */
	@Accessor(qualifier = "attachmentItems", type = Accessor.Type.GETTER)
	public List<ItemModel> getAttachmentItems()
	{
		return getPersistenceContext().getPropertyValue(ATTACHMENTITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.attachments</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the attachments - part of the WorkflowActionItemAttachmentRelation; associates a set of attachments set to the related workflow of this action
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
	public List<WorkflowItemAttachmentModel> getAttachments()
	{
		return getPersistenceContext().getPropertyValue(ATTACHMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.comment</code> attribute defined at extension <code>workflow</code>. 
	 * @return the comment - comment of the assigned principal on the status of the action
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public String getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.decisions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the decisions - set of all possible decisions of this action
	 */
	@Accessor(qualifier = "decisions", type = Accessor.Type.GETTER)
	public Collection<WorkflowDecisionModel> getDecisions()
	{
		return getPersistenceContext().getPropertyValue(DECISIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.firstActivated</code> attribute defined at extension <code>workflow</code>. 
	 * @return the firstActivated - date of first activation of the action (in case of a rejected action an action can be activated twice for example)
	 */
	@Accessor(qualifier = "firstActivated", type = Accessor.Type.GETTER)
	public Date getFirstActivated()
	{
		return getPersistenceContext().getPropertyValue(FIRSTACTIVATED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.incomingDecisions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingDecisions
	 */
	@Accessor(qualifier = "incomingDecisions", type = Accessor.Type.GETTER)
	public Collection<WorkflowDecisionModel> getIncomingDecisions()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGDECISIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.incomingLinks</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingLinks
	 */
	@Accessor(qualifier = "incomingLinks", type = Accessor.Type.GETTER)
	public List<LinkModel> getIncomingLinks()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGLINKS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.incomingLinksStr</code> attribute defined at extension <code>workflow</code>. 
	 * @return the incomingLinksStr
	 */
	@Accessor(qualifier = "incomingLinksStr", type = Accessor.Type.GETTER)
	public String getIncomingLinksStr()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGLINKSSTR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.selectedDecision</code> attribute defined at extension <code>workflow</code>. 
	 * @return the selectedDecision - the decision chosen when the action is processed
	 */
	@Accessor(qualifier = "selectedDecision", type = Accessor.Type.GETTER)
	public WorkflowDecisionModel getSelectedDecision()
	{
		return getPersistenceContext().getPropertyValue(SELECTEDDECISION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.status</code> attribute defined at extension <code>workflow</code>. 
	 * @return the status - the status of the action (pending, active, completed)
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public WorkflowActionStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.template</code> attribute defined at extension <code>workflow</code>. 
	 * @return the template - the action template this action was created by; template defines the perform method
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public WorkflowActionTemplateModel getTemplate()
	{
		return getPersistenceContext().getPropertyValue(TEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.workflow</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflow - workflow to which the action belongs
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
	public WorkflowModel getWorkflow()
	{
		return getPersistenceContext().getPropertyValue(WORKFLOW);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.activated</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the activated - date of last activation
	 */
	@Accessor(qualifier = "activated", type = Accessor.Type.SETTER)
	public void setActivated(final Date value)
	{
		getPersistenceContext().setPropertyValue(ACTIVATED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.attachments</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the attachments - part of the WorkflowActionItemAttachmentRelation; associates a set of attachments set to the related workflow of this action
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
	public void setAttachments(final List<WorkflowItemAttachmentModel> value)
	{
		getPersistenceContext().setPropertyValue(ATTACHMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.comment</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the comment - comment of the assigned principal on the status of the action
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.decisions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the decisions - set of all possible decisions of this action
	 */
	@Accessor(qualifier = "decisions", type = Accessor.Type.SETTER)
	public void setDecisions(final Collection<WorkflowDecisionModel> value)
	{
		getPersistenceContext().setPropertyValue(DECISIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.firstActivated</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the firstActivated - date of first activation of the action (in case of a rejected action an action can be activated twice for example)
	 */
	@Accessor(qualifier = "firstActivated", type = Accessor.Type.SETTER)
	public void setFirstActivated(final Date value)
	{
		getPersistenceContext().setPropertyValue(FIRSTACTIVATED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.incomingDecisions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the incomingDecisions
	 */
	@Accessor(qualifier = "incomingDecisions", type = Accessor.Type.SETTER)
	public void setIncomingDecisions(final Collection<WorkflowDecisionModel> value)
	{
		getPersistenceContext().setPropertyValue(INCOMINGDECISIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.selectedDecision</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the selectedDecision - the decision chosen when the action is processed
	 */
	@Accessor(qualifier = "selectedDecision", type = Accessor.Type.SETTER)
	public void setSelectedDecision(final WorkflowDecisionModel value)
	{
		getPersistenceContext().setPropertyValue(SELECTEDDECISION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.status</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the status - the status of the action (pending, active, completed)
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final WorkflowActionStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowAction.template</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the template - the action template this action was created by; template defines the perform method
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final WorkflowActionTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(TEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowAction.workflow</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the workflow - workflow to which the action belongs
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
	public void setWorkflow(final WorkflowModel value)
	{
		getPersistenceContext().setPropertyValue(WORKFLOW, value);
	}
	
}
