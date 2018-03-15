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
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type WorkflowActionTemplate first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowActionTemplateModel extends AbstractWorkflowActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WorkflowActionTemplate";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionOrderingRelation</code> defining source attribute <code>predecessors</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONORDERINGRELATION = "WorkflowActionOrderingRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowTemplateActionTemplateRelation</code> defining source attribute <code>workflow</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWTEMPLATEACTIONTEMPLATERELATION = "WorkflowTemplateActionTemplateRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionTemplateLinkTemplateRelation</code> defining source attribute <code>incomingTemplateDecisions</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION = "WorkflowActionTemplateLinkTemplateRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.incomingLinkTemplates</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGLINKTEMPLATES = "incomingLinkTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.incomingLinkTemplatesStr</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGLINKTEMPLATESSTR = "incomingLinkTemplatesStr";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.creationType</code> attribute defined at extension <code>workflow</code>. */
	public static final String CREATIONTYPE = "creationType";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.workflowPOS</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOWPOS = "workflowPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.workflow</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOW = "workflow";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.decisionTemplates</code> attribute defined at extension <code>workflow</code>. */
	public static final String DECISIONTEMPLATES = "decisionTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.incomingTemplateDecisions</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGTEMPLATEDECISIONS = "incomingTemplateDecisions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowActionTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowActionTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowActionTemplate</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionTemplateModel(final WorkflowActionType _actionType, final PrincipalModel _principalAssigned, final WorkflowTemplateModel _workflow)
	{
		super();
		setActionType(_actionType);
		setPrincipalAssigned(_principalAssigned);
		setWorkflow(_workflow);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _code initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowActionTemplate</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionTemplateModel(final WorkflowActionType _actionType, final String _code, final UserModel _owner, final PrincipalModel _principalAssigned, final WorkflowTemplateModel _workflow)
	{
		super();
		setActionType(_actionType);
		setCode(_code);
		setOwner(_owner);
		setPrincipalAssigned(_principalAssigned);
		setWorkflow(_workflow);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.creationType</code> attribute defined at extension <code>workflow</code>. 
	 * @return the creationType
	 */
	@Accessor(qualifier = "creationType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getCreationType()
	{
		return getPersistenceContext().getPropertyValue(CREATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.decisionTemplates</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the decisionTemplates - list of all decision templates of the action template
	 */
	@Accessor(qualifier = "decisionTemplates", type = Accessor.Type.GETTER)
	public Collection<WorkflowDecisionTemplateModel> getDecisionTemplates()
	{
		return getPersistenceContext().getPropertyValue(DECISIONTEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.incomingLinkTemplates</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingLinkTemplates
	 */
	@Accessor(qualifier = "incomingLinkTemplates", type = Accessor.Type.GETTER)
	public List<LinkModel> getIncomingLinkTemplates()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGLINKTEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.incomingLinkTemplatesStr</code> attribute defined at extension <code>workflow</code>. 
	 * @return the incomingLinkTemplatesStr
	 */
	@Accessor(qualifier = "incomingLinkTemplatesStr", type = Accessor.Type.GETTER)
	public String getIncomingLinkTemplatesStr()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGLINKTEMPLATESSTR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.incomingTemplateDecisions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingTemplateDecisions
	 */
	@Accessor(qualifier = "incomingTemplateDecisions", type = Accessor.Type.GETTER)
	public Collection<WorkflowDecisionTemplateModel> getIncomingTemplateDecisions()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGTEMPLATEDECISIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.workflow</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflow - workflow template to which the action template belongs
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
	public WorkflowTemplateModel getWorkflow()
	{
		return getPersistenceContext().getPropertyValue(WORKFLOW);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionTemplate.creationType</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the creationType
	 */
	@Accessor(qualifier = "creationType", type = Accessor.Type.SETTER)
	public void setCreationType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(CREATIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionTemplate.decisionTemplates</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the decisionTemplates - list of all decision templates of the action template
	 */
	@Accessor(qualifier = "decisionTemplates", type = Accessor.Type.SETTER)
	public void setDecisionTemplates(final Collection<WorkflowDecisionTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(DECISIONTEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionTemplate.incomingTemplateDecisions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the incomingTemplateDecisions
	 */
	@Accessor(qualifier = "incomingTemplateDecisions", type = Accessor.Type.SETTER)
	public void setIncomingTemplateDecisions(final Collection<WorkflowDecisionTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(INCOMINGTEMPLATEDECISIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowActionTemplate.workflow</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the workflow - workflow template to which the action template belongs
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
	public void setWorkflow(final WorkflowTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(WORKFLOW, value);
	}
	
}
