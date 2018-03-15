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
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.Collection;

/**
 * Generated model class for type WorkflowDecision first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowDecisionModel extends AbstractWorkflowDecisionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WorkflowDecision";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionDecisionsRelation</code> defining source attribute <code>action</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWACTIONDECISIONSRELATION = "WorkflowActionDecisionsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowDecision.action</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTION = "action";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowDecision.toActions</code> attribute defined at extension <code>workflow</code>. */
	public static final String TOACTIONS = "toActions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowDecisionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowDecisionModel(final ItemModelContext ctx)
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
	public WorkflowDecisionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowDecision.action</code> attribute defined at extension <code>workflow</code>. 
	 * @return the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.GETTER)
	public WorkflowActionModel getAction()
	{
		return getPersistenceContext().getPropertyValue(ACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowDecision.toActions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the toActions
	 */
	@Accessor(qualifier = "toActions", type = Accessor.Type.GETTER)
	public Collection<WorkflowActionModel> getToActions()
	{
		return getPersistenceContext().getPropertyValue(TOACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowDecision.action</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.SETTER)
	public void setAction(final WorkflowActionModel value)
	{
		getPersistenceContext().setPropertyValue(ACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowDecision.toActions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the toActions
	 */
	@Accessor(qualifier = "toActions", type = Accessor.Type.SETTER)
	public void setToActions(final Collection<WorkflowActionModel> value)
	{
		getPersistenceContext().setPropertyValue(TOACTIONS, value);
	}
	
}
