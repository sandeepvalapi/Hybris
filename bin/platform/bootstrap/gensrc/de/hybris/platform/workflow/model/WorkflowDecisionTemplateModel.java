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
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;

/**
 * Generated model class for type WorkflowDecisionTemplate first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowDecisionTemplateModel extends AbstractWorkflowDecisionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WorkflowDecisionTemplate";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionTemplateDecisionsTemplateRelation</code> defining source attribute <code>actionTemplate</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONTEMPLATEDECISIONSTEMPLATERELATION = "WorkflowActionTemplateDecisionsTemplateRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowDecisionTemplate.parentWorkflowTemplate</code> attribute defined at extension <code>workflow</code>. */
	public static final String PARENTWORKFLOWTEMPLATE = "parentWorkflowTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowDecisionTemplate.actionTemplate</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONTEMPLATE = "actionTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowDecisionTemplate.toTemplateActions</code> attribute defined at extension <code>workflow</code>. */
	public static final String TOTEMPLATEACTIONS = "toTemplateActions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowDecisionTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowDecisionTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractWorkflowDecision</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public WorkflowDecisionTemplateModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowDecisionTemplate.actionTemplate</code> attribute defined at extension <code>workflow</code>. 
	 * @return the actionTemplate - reference to the action template this decision belongs to
	 */
	@Accessor(qualifier = "actionTemplate", type = Accessor.Type.GETTER)
	public WorkflowActionTemplateModel getActionTemplate()
	{
		return getPersistenceContext().getPropertyValue(ACTIONTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowDecisionTemplate.parentWorkflowTemplate</code> attribute defined at extension <code>workflow</code>. 
	 * @return the parentWorkflowTemplate
	 */
	@Accessor(qualifier = "parentWorkflowTemplate", type = Accessor.Type.GETTER)
	public WorkflowTemplateModel getParentWorkflowTemplate()
	{
		return getPersistenceContext().getPropertyValue(PARENTWORKFLOWTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowDecisionTemplate.toTemplateActions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the toTemplateActions - list of actions that will be activated then the decision gets chosen
	 */
	@Accessor(qualifier = "toTemplateActions", type = Accessor.Type.GETTER)
	public Collection<WorkflowActionTemplateModel> getToTemplateActions()
	{
		return getPersistenceContext().getPropertyValue(TOTEMPLATEACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowDecisionTemplate.actionTemplate</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the actionTemplate - reference to the action template this decision belongs to
	 */
	@Accessor(qualifier = "actionTemplate", type = Accessor.Type.SETTER)
	public void setActionTemplate(final WorkflowActionTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(ACTIONTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowDecisionTemplate.toTemplateActions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the toTemplateActions - list of actions that will be activated then the decision gets chosen
	 */
	@Accessor(qualifier = "toTemplateActions", type = Accessor.Type.SETTER)
	public void setToTemplateActions(final Collection<WorkflowActionTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(TOTEMPLATEACTIONS, value);
	}
	
}
