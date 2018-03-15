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
package de.hybris.platform.task;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;
import java.util.Date;

/**
 * Generated model class for type TaskCondition first defined at extension processing.
 */
@SuppressWarnings("all")
public class TaskConditionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "TaskCondition";
	
	/**<i>Generated relation code constant for relation <code>TaskConditionRelation</code> defining source attribute <code>task</code> in extension <code>processing</code>.</i>*/
	public static final String _TASKCONDITIONRELATION = "TaskConditionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.uniqueID</code> attribute defined at extension <code>processing</code>. */
	public static final String UNIQUEID = "uniqueID";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.expirationDate</code> attribute defined at extension <code>processing</code>. */
	public static final String EXPIRATIONDATE = "expirationDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. */
	public static final String EXPIRATIONTIMEMILLIS = "expirationTimeMillis";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.processedDate</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESSEDDATE = "processedDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.fulfilled</code> attribute defined at extension <code>processing</code>. */
	public static final String FULFILLED = "fulfilled";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.consumed</code> attribute defined at extension <code>processing</code>. */
	public static final String CONSUMED = "consumed";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.choice</code> attribute defined at extension <code>processing</code>. */
	public static final String CHOICE = "choice";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.counter</code> attribute defined at extension <code>processing</code>. */
	public static final String COUNTER = "counter";
	
	/** <i>Generated constant</i> - Attribute key of <code>TaskCondition.task</code> attribute defined at extension <code>processing</code>. */
	public static final String TASK = "task";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TaskConditionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TaskConditionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uniqueID initial attribute declared by type <code>TaskCondition</code> at extension <code>processing</code>
	 */
	@Deprecated
	public TaskConditionModel(final String _uniqueID)
	{
		super();
		setUniqueID(_uniqueID);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uniqueID initial attribute declared by type <code>TaskCondition</code> at extension <code>processing</code>
	 */
	@Deprecated
	public TaskConditionModel(final ItemModel _owner, final String _uniqueID)
	{
		super();
		setOwner(_owner);
		setUniqueID(_uniqueID);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.choice</code> attribute defined at extension <code>processing</code>. 
	 * @return the choice - Additional value which can be provided by the user when triggering an event
	 */
	@Accessor(qualifier = "choice", type = Accessor.Type.GETTER)
	public String getChoice()
	{
		return getPersistenceContext().getPropertyValue(CHOICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.consumed</code> attribute defined at extension <code>processing</code>. 
	 * @return the consumed
	 */
	@Accessor(qualifier = "consumed", type = Accessor.Type.GETTER)
	public Boolean getConsumed()
	{
		return getPersistenceContext().getPropertyValue(CONSUMED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.counter</code> attribute defined at extension <code>processing</code>. 
	 * @return the counter - Counter which if set to value greater than 0 prevent fulfilling the condition. Counter is decremented every time when an event is triggered.
	 */
	@Accessor(qualifier = "counter", type = Accessor.Type.GETTER)
	public Integer getCounter()
	{
		return getPersistenceContext().getPropertyValue(COUNTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.expirationDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 * @return the expirationDate - The maximum allowed time to wait for completion
	 */
	@Accessor(qualifier = "expirationDate", type = Accessor.Type.GETTER)
	public Date getExpirationDate()
	{
		return getPersistenceContext().getDynamicValue(this,EXPIRATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 * @return the expirationTimeMillis - The maximum allowed time to wait for completion
	 */
	@Accessor(qualifier = "expirationTimeMillis", type = Accessor.Type.GETTER)
	public Long getExpirationTimeMillis()
	{
		return getPersistenceContext().getPropertyValue(EXPIRATIONTIMEMILLIS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.fulfilled</code> attribute defined at extension <code>processing</code>. 
	 * @return the fulfilled
	 */
	@Accessor(qualifier = "fulfilled", type = Accessor.Type.GETTER)
	public Boolean getFulfilled()
	{
		return getPersistenceContext().getPropertyValue(FULFILLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.processedDate</code> attribute defined at extension <code>processing</code>. 
	 * @return the processedDate - The date when this condition has been processed.
	 *                         Then state is switched to FULFILLED or REJECTED.
	 */
	@Accessor(qualifier = "processedDate", type = Accessor.Type.GETTER)
	public Date getProcessedDate()
	{
		return getPersistenceContext().getPropertyValue(PROCESSEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.task</code> attribute defined at extension <code>processing</code>. 
	 * @return the task
	 */
	@Accessor(qualifier = "task", type = Accessor.Type.GETTER)
	public TaskModel getTask()
	{
		return getPersistenceContext().getPropertyValue(TASK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TaskCondition.uniqueID</code> attribute defined at extension <code>processing</code>. 
	 * @return the uniqueID - The unique id of this condition - may be created by number series !?
	 */
	@Accessor(qualifier = "uniqueID", type = Accessor.Type.GETTER)
	public String getUniqueID()
	{
		return getPersistenceContext().getPropertyValue(UNIQUEID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.choice</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the choice - Additional value which can be provided by the user when triggering an event
	 */
	@Accessor(qualifier = "choice", type = Accessor.Type.SETTER)
	public void setChoice(final String value)
	{
		getPersistenceContext().setPropertyValue(CHOICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.consumed</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the consumed
	 */
	@Accessor(qualifier = "consumed", type = Accessor.Type.SETTER)
	public void setConsumed(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CONSUMED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.counter</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the counter - Counter which if set to value greater than 0 prevent fulfilling the condition. Counter is decremented every time when an event is triggered.
	 */
	@Accessor(qualifier = "counter", type = Accessor.Type.SETTER)
	public void setCounter(final Integer value)
	{
		getPersistenceContext().setPropertyValue(COUNTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.expirationDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the expirationDate - The maximum allowed time to wait for completion
	 */
	@Accessor(qualifier = "expirationDate", type = Accessor.Type.SETTER)
	public void setExpirationDate(final Date value)
	{
		getPersistenceContext().setDynamicValue(this,EXPIRATIONDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the expirationTimeMillis - The maximum allowed time to wait for completion
	 */
	@Accessor(qualifier = "expirationTimeMillis", type = Accessor.Type.SETTER)
	public void setExpirationTimeMillis(final Long value)
	{
		getPersistenceContext().setPropertyValue(EXPIRATIONTIMEMILLIS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.fulfilled</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the fulfilled
	 */
	@Accessor(qualifier = "fulfilled", type = Accessor.Type.SETTER)
	public void setFulfilled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(FULFILLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.processedDate</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the processedDate - The date when this condition has been processed.
	 *                         Then state is switched to FULFILLED or REJECTED.
	 */
	@Accessor(qualifier = "processedDate", type = Accessor.Type.SETTER)
	public void setProcessedDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(PROCESSEDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.task</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the task
	 */
	@Accessor(qualifier = "task", type = Accessor.Type.SETTER)
	public void setTask(final TaskModel value)
	{
		getPersistenceContext().setPropertyValue(TASK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TaskCondition.uniqueID</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the uniqueID - The unique id of this condition - may be created by number series !?
	 */
	@Accessor(qualifier = "uniqueID", type = Accessor.Type.SETTER)
	public void setUniqueID(final String value)
	{
		getPersistenceContext().setPropertyValue(UNIQUEID, value);
	}
	
}
