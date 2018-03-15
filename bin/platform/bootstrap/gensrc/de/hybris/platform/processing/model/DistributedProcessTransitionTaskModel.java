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
package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

/**
 * Generated model class for type DistributedProcessTransitionTask first defined at extension processing.
 */
@SuppressWarnings("all")
public class DistributedProcessTransitionTaskModel extends TaskModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DistributedProcessTransitionTask";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcessTransitionTask.state</code> attribute defined at extension <code>processing</code>. */
	public static final String STATE = "state";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DistributedProcessTransitionTaskModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DistributedProcessTransitionTaskModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _runnerBean initial attribute declared by type <code>Task</code> at extension <code>processing</code>
	 * @param _state initial attribute declared by type <code>DistributedProcessTransitionTask</code> at extension <code>processing</code>
	 */
	@Deprecated
	public DistributedProcessTransitionTaskModel(final String _runnerBean, final DistributedProcessState _state)
	{
		super();
		setRunnerBean(_runnerBean);
		setState(_state);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _runnerBean initial attribute declared by type <code>Task</code> at extension <code>processing</code>
	 * @param _state initial attribute declared by type <code>DistributedProcessTransitionTask</code> at extension <code>processing</code>
	 */
	@Deprecated
	public DistributedProcessTransitionTaskModel(final ItemModel _owner, final String _runnerBean, final DistributedProcessState _state)
	{
		super();
		setOwner(_owner);
		setRunnerBean(_runnerBean);
		setState(_state);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.contextItem</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>processing</code>. 
	 * @return the contextItem
	 */
	@Override
	@Accessor(qualifier = "contextItem", type = Accessor.Type.GETTER)
	public DistributedProcessModel getContextItem()
	{
		return (DistributedProcessModel) super.getContextItem();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcessTransitionTask.state</code> attribute defined at extension <code>processing</code>. 
	 * @return the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.GETTER)
	public DistributedProcessState getState()
	{
		return getPersistenceContext().getPropertyValue(STATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.contextItem</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>processing</code>. Will only accept values of type {@link de.hybris.platform.processing.model.DistributedProcessModel}. 
	 *  
	 * @param value the contextItem
	 */
	@Override
	@Accessor(qualifier = "contextItem", type = Accessor.Type.SETTER)
	public void setContextItem(final ItemModel value)
	{
		if( value == null || value instanceof DistributedProcessModel)
		{
			super.setContextItem(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.processing.model.DistributedProcessModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DistributedProcessTransitionTask.state</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.SETTER)
	public void setState(final DistributedProcessState value)
	{
		getPersistenceContext().setPropertyValue(STATE, value);
	}
	
}
