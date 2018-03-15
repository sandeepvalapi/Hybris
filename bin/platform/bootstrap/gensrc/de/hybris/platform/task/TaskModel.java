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
import de.hybris.platform.task.TaskConditionModel;
import java.util.Date;
import java.util.Set;

/**
 * Generated model class for type Task first defined at extension processing.
 */
@SuppressWarnings("all")
public class TaskModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Task";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.runnerBean</code> attribute defined at extension <code>processing</code>. */
	public static final String RUNNERBEAN = "runnerBean";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.executionDate</code> attribute defined at extension <code>processing</code>. */
	public static final String EXECUTIONDATE = "executionDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.executionTimeMillis</code> attribute defined at extension <code>processing</code>. */
	public static final String EXECUTIONTIMEMILLIS = "executionTimeMillis";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.executionHourMillis</code> attribute defined at extension <code>processing</code>. */
	public static final String EXECUTIONHOURMILLIS = "executionHourMillis";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.failed</code> attribute defined at extension <code>processing</code>. */
	public static final String FAILED = "failed";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.expirationDate</code> attribute defined at extension <code>processing</code>. */
	public static final String EXPIRATIONDATE = "expirationDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. */
	public static final String EXPIRATIONTIMEMILLIS = "expirationTimeMillis";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.context</code> attribute defined at extension <code>processing</code>. */
	public static final String CONTEXT = "context";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.contextItem</code> attribute defined at extension <code>processing</code>. */
	public static final String CONTEXTITEM = "contextItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.nodeId</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEID = "nodeId";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.nodeGroup</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEGROUP = "nodeGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.retry</code> attribute defined at extension <code>processing</code>. */
	public static final String RETRY = "retry";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. */
	public static final String RUNNINGONCLUSTERNODE = "runningOnClusterNode";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.conditions</code> attribute defined at extension <code>processing</code>. */
	public static final String CONDITIONS = "conditions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TaskModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TaskModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _runnerBean initial attribute declared by type <code>Task</code> at extension <code>processing</code>
	 */
	@Deprecated
	public TaskModel(final String _runnerBean)
	{
		super();
		setRunnerBean(_runnerBean);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _runnerBean initial attribute declared by type <code>Task</code> at extension <code>processing</code>
	 */
	@Deprecated
	public TaskModel(final ItemModel _owner, final String _runnerBean)
	{
		super();
		setOwner(_owner);
		setRunnerBean(_runnerBean);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.conditions</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the conditions
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
	public Set<TaskConditionModel> getConditions()
	{
		return getPersistenceContext().getPropertyValue(CONDITIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.context</code> attribute defined at extension <code>processing</code>. 
	 * @return the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.GETTER)
	public Object getContext()
	{
		return getPersistenceContext().getPropertyValue(CONTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.contextItem</code> attribute defined at extension <code>processing</code>. 
	 * @return the contextItem
	 */
	@Accessor(qualifier = "contextItem", type = Accessor.Type.GETTER)
	public ItemModel getContextItem()
	{
		return getPersistenceContext().getPropertyValue(CONTEXTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.executionDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 * @return the executionDate - Date after this task is to be executed
	 */
	@Accessor(qualifier = "executionDate", type = Accessor.Type.GETTER)
	public Date getExecutionDate()
	{
		return getPersistenceContext().getDynamicValue(this,EXECUTIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.executionTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 * @return the executionTimeMillis - Internal representation to overcome database limitations!
	 */
	@Accessor(qualifier = "executionTimeMillis", type = Accessor.Type.GETTER)
	public Long getExecutionTimeMillis()
	{
		return getPersistenceContext().getPropertyValue(EXECUTIONTIMEMILLIS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.expirationDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 * @return the expirationDate
	 */
	@Accessor(qualifier = "expirationDate", type = Accessor.Type.GETTER)
	public Date getExpirationDate()
	{
		return getPersistenceContext().getDynamicValue(this,EXPIRATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 * @return the expirationTimeMillis - Date when this task is to be executed
	 */
	@Accessor(qualifier = "expirationTimeMillis", type = Accessor.Type.GETTER)
	public Long getExpirationTimeMillis()
	{
		return getPersistenceContext().getPropertyValue(EXPIRATIONTIMEMILLIS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.failed</code> attribute defined at extension <code>processing</code>. 
	 * @return the failed
	 */
	@Accessor(qualifier = "failed", type = Accessor.Type.GETTER)
	public Boolean getFailed()
	{
		return getPersistenceContext().getPropertyValue(FAILED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
	public String getNodeGroup()
	{
		return getPersistenceContext().getPropertyValue(NODEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.nodeId</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeId
	 */
	@Accessor(qualifier = "nodeId", type = Accessor.Type.GETTER)
	public Integer getNodeId()
	{
		return getPersistenceContext().getPropertyValue(NODEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.retry</code> attribute defined at extension <code>processing</code>. 
	 * @return the retry
	 */
	@Accessor(qualifier = "retry", type = Accessor.Type.GETTER)
	public Integer getRetry()
	{
		return getPersistenceContext().getPropertyValue(RETRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.runnerBean</code> attribute defined at extension <code>processing</code>. 
	 * @return the runnerBean
	 */
	@Accessor(qualifier = "runnerBean", type = Accessor.Type.GETTER)
	public String getRunnerBean()
	{
		return getPersistenceContext().getPropertyValue(RUNNERBEAN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. 
	 * @return the runningOnClusterNode
	 */
	@Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.GETTER)
	public Integer getRunningOnClusterNode()
	{
		return getPersistenceContext().getPropertyValue(RUNNINGONCLUSTERNODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.conditions</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the conditions
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
	public void setConditions(final Set<TaskConditionModel> value)
	{
		getPersistenceContext().setPropertyValue(CONDITIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.context</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.SETTER)
	public void setContext(final Object value)
	{
		getPersistenceContext().setPropertyValue(CONTEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.contextItem</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the contextItem
	 */
	@Accessor(qualifier = "contextItem", type = Accessor.Type.SETTER)
	public void setContextItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(CONTEXTITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.executionDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the executionDate - Date after this task is to be executed
	 */
	@Accessor(qualifier = "executionDate", type = Accessor.Type.SETTER)
	public void setExecutionDate(final Date value)
	{
		getPersistenceContext().setDynamicValue(this,EXECUTIONDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.executionHourMillis</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the executionHourMillis - Internal representation to overcome database limitations!
	 */
	@Accessor(qualifier = "executionHourMillis", type = Accessor.Type.SETTER)
	public void setExecutionHourMillis(final Long value)
	{
		getPersistenceContext().setPropertyValue(EXECUTIONHOURMILLIS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.executionTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the executionTimeMillis - Internal representation to overcome database limitations!
	 */
	@Accessor(qualifier = "executionTimeMillis", type = Accessor.Type.SETTER)
	public void setExecutionTimeMillis(final Long value)
	{
		getPersistenceContext().setPropertyValue(EXECUTIONTIMEMILLIS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.expirationDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the expirationDate
	 */
	@Accessor(qualifier = "expirationDate", type = Accessor.Type.SETTER)
	public void setExpirationDate(final Date value)
	{
		getPersistenceContext().setDynamicValue(this,EXPIRATIONDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the expirationTimeMillis - Date when this task is to be executed
	 */
	@Accessor(qualifier = "expirationTimeMillis", type = Accessor.Type.SETTER)
	public void setExpirationTimeMillis(final Long value)
	{
		getPersistenceContext().setPropertyValue(EXPIRATIONTIMEMILLIS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.failed</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the failed
	 */
	@Accessor(qualifier = "failed", type = Accessor.Type.SETTER)
	public void setFailed(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(FAILED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
	public void setNodeGroup(final String value)
	{
		getPersistenceContext().setPropertyValue(NODEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.nodeId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the nodeId
	 */
	@Accessor(qualifier = "nodeId", type = Accessor.Type.SETTER)
	public void setNodeId(final Integer value)
	{
		getPersistenceContext().setPropertyValue(NODEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.retry</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retry
	 */
	@Accessor(qualifier = "retry", type = Accessor.Type.SETTER)
	public void setRetry(final Integer value)
	{
		getPersistenceContext().setPropertyValue(RETRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.runnerBean</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the runnerBean
	 */
	@Accessor(qualifier = "runnerBean", type = Accessor.Type.SETTER)
	public void setRunnerBean(final String value)
	{
		getPersistenceContext().setPropertyValue(RUNNERBEAN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the runningOnClusterNode
	 */
	@Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.SETTER)
	public void setRunningOnClusterNode(final Integer value)
	{
		getPersistenceContext().setPropertyValue(RUNNINGONCLUSTERNODE, value);
	}
	
}
