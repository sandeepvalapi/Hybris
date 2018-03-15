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
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type DistributedProcess first defined at extension processing.
 */
@SuppressWarnings("all")
public class DistributedProcessModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DistributedProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.code</code> attribute defined at extension <code>processing</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.handlerBeanId</code> attribute defined at extension <code>processing</code>. */
	public static final String HANDLERBEANID = "handlerBeanId";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.currentExecutionId</code> attribute defined at extension <code>processing</code>. */
	public static final String CURRENTEXECUTIONID = "currentExecutionId";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.state</code> attribute defined at extension <code>processing</code>. */
	public static final String STATE = "state";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.stopRequested</code> attribute defined at extension <code>processing</code>. */
	public static final String STOPREQUESTED = "stopRequested";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.nodeGroup</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEGROUP = "nodeGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.status</code> attribute defined at extension <code>processing</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.extendedStatus</code> attribute defined at extension <code>processing</code>. */
	public static final String EXTENDEDSTATUS = "extendedStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.progress</code> attribute defined at extension <code>processing</code>. */
	public static final String PROGRESS = "progress";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedProcess.batches</code> attribute defined at extension <code>processing</code>. */
	public static final String BATCHES = "batches";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DistributedProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DistributedProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _currentExecutionId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _state initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public DistributedProcessModel(final String _code, final String _currentExecutionId, final DistributedProcessState _state)
	{
		super();
		setCode(_code);
		setCurrentExecutionId(_currentExecutionId);
		setState(_state);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _currentExecutionId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _handlerBeanId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _nodeGroup initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _state initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public DistributedProcessModel(final String _code, final String _currentExecutionId, final String _handlerBeanId, final String _nodeGroup, final ItemModel _owner, final DistributedProcessState _state)
	{
		super();
		setCode(_code);
		setCurrentExecutionId(_currentExecutionId);
		setHandlerBeanId(_handlerBeanId);
		setNodeGroup(_nodeGroup);
		setOwner(_owner);
		setState(_state);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.batches</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the batches
	 */
	@Accessor(qualifier = "batches", type = Accessor.Type.GETTER)
	public Collection<BatchModel> getBatches()
	{
		return getPersistenceContext().getPropertyValue(BATCHES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.code</code> attribute defined at extension <code>processing</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.currentExecutionId</code> attribute defined at extension <code>processing</code>. 
	 * @return the currentExecutionId
	 */
	@Accessor(qualifier = "currentExecutionId", type = Accessor.Type.GETTER)
	public String getCurrentExecutionId()
	{
		return getPersistenceContext().getPropertyValue(CURRENTEXECUTIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.extendedStatus</code> attribute defined at extension <code>processing</code>. 
	 * @return the extendedStatus
	 */
	@Accessor(qualifier = "extendedStatus", type = Accessor.Type.GETTER)
	public String getExtendedStatus()
	{
		return getPersistenceContext().getPropertyValue(EXTENDEDSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.handlerBeanId</code> attribute defined at extension <code>processing</code>. 
	 * @return the handlerBeanId
	 */
	@Accessor(qualifier = "handlerBeanId", type = Accessor.Type.GETTER)
	public String getHandlerBeanId()
	{
		return getPersistenceContext().getPropertyValue(HANDLERBEANID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
	public String getNodeGroup()
	{
		return getPersistenceContext().getPropertyValue(NODEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.progress</code> attribute defined at extension <code>processing</code>. 
	 * @return the progress
	 */
	@Accessor(qualifier = "progress", type = Accessor.Type.GETTER)
	public Double getProgress()
	{
		return getPersistenceContext().getPropertyValue(PROGRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.state</code> attribute defined at extension <code>processing</code>. 
	 * @return the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.GETTER)
	public DistributedProcessState getState()
	{
		return getPersistenceContext().getPropertyValue(STATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.status</code> attribute defined at extension <code>processing</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public String getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedProcess.stopRequested</code> attribute defined at extension <code>processing</code>. 
	 * @return the stopRequested
	 */
	@Accessor(qualifier = "stopRequested", type = Accessor.Type.GETTER)
	public boolean isStopRequested()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(STOPREQUESTED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DistributedProcess.batches</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the batches
	 */
	@Accessor(qualifier = "batches", type = Accessor.Type.SETTER)
	public void setBatches(final Collection<BatchModel> value)
	{
		getPersistenceContext().setPropertyValue(BATCHES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DistributedProcess.code</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DistributedProcess.currentExecutionId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the currentExecutionId
	 */
	@Accessor(qualifier = "currentExecutionId", type = Accessor.Type.SETTER)
	public void setCurrentExecutionId(final String value)
	{
		getPersistenceContext().setPropertyValue(CURRENTEXECUTIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DistributedProcess.extendedStatus</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the extendedStatus
	 */
	@Accessor(qualifier = "extendedStatus", type = Accessor.Type.SETTER)
	public void setExtendedStatus(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTENDEDSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DistributedProcess.handlerBeanId</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the handlerBeanId
	 */
	@Accessor(qualifier = "handlerBeanId", type = Accessor.Type.SETTER)
	public void setHandlerBeanId(final String value)
	{
		getPersistenceContext().setPropertyValue(HANDLERBEANID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DistributedProcess.nodeGroup</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
	public void setNodeGroup(final String value)
	{
		getPersistenceContext().setPropertyValue(NODEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DistributedProcess.progress</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the progress
	 */
	@Accessor(qualifier = "progress", type = Accessor.Type.SETTER)
	public void setProgress(final Double value)
	{
		getPersistenceContext().setPropertyValue(PROGRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DistributedProcess.state</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.SETTER)
	public void setState(final DistributedProcessState value)
	{
		getPersistenceContext().setPropertyValue(STATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DistributedProcess.status</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final String value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DistributedProcess.stopRequested</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the stopRequested
	 */
	@Accessor(qualifier = "stopRequested", type = Accessor.Type.SETTER)
	public void setStopRequested(final boolean value)
	{
		getPersistenceContext().setPropertyValue(STOPREQUESTED, toObject(value));
	}
	
}
