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
package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

/**
 * Generated model class for type InboxView first defined at extension workflow.
 */
@SuppressWarnings("all")
public class InboxViewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "InboxView";
	
	/** <i>Generated constant</i> - Attribute key of <code>InboxView.action</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTION = "action";
	
	/** <i>Generated constant</i> - Attribute key of <code>InboxView.activated</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIVATED = "activated";
	
	/** <i>Generated constant</i> - Attribute key of <code>InboxView.actioncomment</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONCOMMENT = "actioncomment";
	
	/** <i>Generated constant</i> - Attribute key of <code>InboxView.workflow</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOW = "workflow";
	
	/** <i>Generated constant</i> - Attribute key of <code>InboxView.status</code> attribute defined at extension <code>workflow</code>. */
	public static final String STATUS = "status";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public InboxViewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public InboxViewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public InboxViewModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboxView.action</code> attribute defined at extension <code>workflow</code>. 
	 * @return the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.GETTER)
	public WorkflowActionModel getAction()
	{
		return getPersistenceContext().getPropertyValue(ACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboxView.actioncomment</code> attribute defined at extension <code>workflow</code>. 
	 * @return the actioncomment
	 */
	@Accessor(qualifier = "actioncomment", type = Accessor.Type.GETTER)
	public String getActioncomment()
	{
		return getPersistenceContext().getPropertyValue(ACTIONCOMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboxView.activated</code> attribute defined at extension <code>workflow</code>. 
	 * @return the activated
	 */
	@Accessor(qualifier = "activated", type = Accessor.Type.GETTER)
	public String getActivated()
	{
		return getPersistenceContext().getPropertyValue(ACTIVATED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboxView.status</code> attribute defined at extension <code>workflow</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public WorkflowActionStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboxView.workflow</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflow
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
	public WorkflowModel getWorkflow()
	{
		return getPersistenceContext().getPropertyValue(WORKFLOW);
	}
	
}
