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
package de.hybris.platform.task.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

/**
 * Generated model class for type TriggerTask first defined at extension processing.
 */
@SuppressWarnings("all")
public class TriggerTaskModel extends TaskModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "TriggerTask";
	
	/** <i>Generated constant</i> - Attribute key of <code>TriggerTask.trigger</code> attribute defined at extension <code>processing</code>. */
	public static final String TRIGGER = "trigger";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TriggerTaskModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TriggerTaskModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _runnerBean initial attribute declared by type <code>TriggerTask</code> at extension <code>processing</code>
	 * @param _trigger initial attribute declared by type <code>TriggerTask</code> at extension <code>processing</code>
	 */
	@Deprecated
	public TriggerTaskModel(final String _runnerBean, final TriggerModel _trigger)
	{
		super();
		setRunnerBean(_runnerBean);
		setTrigger(_trigger);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _runnerBean initial attribute declared by type <code>TriggerTask</code> at extension <code>processing</code>
	 * @param _trigger initial attribute declared by type <code>TriggerTask</code> at extension <code>processing</code>
	 */
	@Deprecated
	public TriggerTaskModel(final ItemModel _owner, final String _runnerBean, final TriggerModel _trigger)
	{
		super();
		setOwner(_owner);
		setRunnerBean(_runnerBean);
		setTrigger(_trigger);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TriggerTask.trigger</code> attribute defined at extension <code>processing</code>. 
	 * @return the trigger
	 */
	@Accessor(qualifier = "trigger", type = Accessor.Type.GETTER)
	public TriggerModel getTrigger()
	{
		return getPersistenceContext().getPropertyValue(TRIGGER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TriggerTask.trigger</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the trigger
	 */
	@Accessor(qualifier = "trigger", type = Accessor.Type.SETTER)
	public void setTrigger(final TriggerModel value)
	{
		getPersistenceContext().setPropertyValue(TRIGGER, value);
	}
	
}
