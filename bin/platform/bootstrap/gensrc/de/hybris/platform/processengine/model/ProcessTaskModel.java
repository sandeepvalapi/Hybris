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
package de.hybris.platform.processengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

/**
 * Generated model class for type ProcessTask first defined at extension processing.
 */
@SuppressWarnings("all")
public class ProcessTaskModel extends TaskModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProcessTask";
	
	/**<i>Generated relation code constant for relation <code>Process2TaskRelation</code> defining source attribute <code>process</code> in extension <code>processing</code>.</i>*/
	public static final String _PROCESS2TASKRELATION = "Process2TaskRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTask.action</code> attribute defined at extension <code>processing</code>. */
	public static final String ACTION = "action";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTask.process</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESS = "process";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProcessTaskModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProcessTaskModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _action initial attribute declared by type <code>ProcessTask</code> at extension <code>processing</code>
	 * @param _process initial attribute declared by type <code>ProcessTask</code> at extension <code>processing</code>
	 * @param _runnerBean initial attribute declared by type <code>Task</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ProcessTaskModel(final String _action, final BusinessProcessModel _process, final String _runnerBean)
	{
		super();
		setAction(_action);
		setProcess(_process);
		setRunnerBean(_runnerBean);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _action initial attribute declared by type <code>ProcessTask</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _process initial attribute declared by type <code>ProcessTask</code> at extension <code>processing</code>
	 * @param _runnerBean initial attribute declared by type <code>Task</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ProcessTaskModel(final String _action, final ItemModel _owner, final BusinessProcessModel _process, final String _runnerBean)
	{
		super();
		setAction(_action);
		setOwner(_owner);
		setProcess(_process);
		setRunnerBean(_runnerBean);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTask.action</code> attribute defined at extension <code>processing</code>. 
	 * @return the action - Name of the action to perform.
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.GETTER)
	public String getAction()
	{
		return getPersistenceContext().getPropertyValue(ACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTask.process</code> attribute defined at extension <code>processing</code>. 
	 * @return the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.GETTER)
	public BusinessProcessModel getProcess()
	{
		return getPersistenceContext().getPropertyValue(PROCESS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProcessTask.action</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the action - Name of the action to perform.
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.SETTER)
	public void setAction(final String value)
	{
		getPersistenceContext().setPropertyValue(ACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTask.process</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.SETTER)
	public void setProcess(final BusinessProcessModel value)
	{
		getPersistenceContext().setPropertyValue(PROCESS, value);
	}
	
}
