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
import java.util.Date;

/**
 * Generated model class for type ProcessTaskLog first defined at extension processing.
 */
@SuppressWarnings("all")
public class ProcessTaskLogModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProcessTaskLog";
	
	/**<i>Generated relation code constant for relation <code>Process2TaskLogRelation</code> defining source attribute <code>process</code> in extension <code>processing</code>.</i>*/
	public static final String _PROCESS2TASKLOGRELATION = "Process2TaskLogRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.returnCode</code> attribute defined at extension <code>processing</code>. */
	public static final String RETURNCODE = "returnCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.startDate</code> attribute defined at extension <code>processing</code>. */
	public static final String STARTDATE = "startDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.endDate</code> attribute defined at extension <code>processing</code>. */
	public static final String ENDDATE = "endDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.actionId</code> attribute defined at extension <code>processing</code>. */
	public static final String ACTIONID = "actionId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.clusterId</code> attribute defined at extension <code>processing</code>. */
	public static final String CLUSTERID = "clusterId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.logMessages</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGMESSAGES = "logMessages";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.process</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESS = "process";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProcessTaskLogModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProcessTaskLogModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionId initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 * @param _clusterId initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 * @param _process initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ProcessTaskLogModel(final String _actionId, final Integer _clusterId, final BusinessProcessModel _process)
	{
		super();
		setActionId(_actionId);
		setClusterId(_clusterId);
		setProcess(_process);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionId initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 * @param _clusterId initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _process initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ProcessTaskLogModel(final String _actionId, final Integer _clusterId, final ItemModel _owner, final BusinessProcessModel _process)
	{
		super();
		setActionId(_actionId);
		setClusterId(_clusterId);
		setOwner(_owner);
		setProcess(_process);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.actionId</code> attribute defined at extension <code>processing</code>. 
	 * @return the actionId - ID of the action performed
	 */
	@Accessor(qualifier = "actionId", type = Accessor.Type.GETTER)
	public String getActionId()
	{
		return getPersistenceContext().getPropertyValue(ACTIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.clusterId</code> attribute defined at extension <code>processing</code>. 
	 * @return the clusterId - ID of the cluster where action performed
	 */
	@Accessor(qualifier = "clusterId", type = Accessor.Type.GETTER)
	public Integer getClusterId()
	{
		return getPersistenceContext().getPropertyValue(CLUSTERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.endDate</code> attribute defined at extension <code>processing</code>. 
	 * @return the endDate - Date when task was ended
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
	public Date getEndDate()
	{
		return getPersistenceContext().getPropertyValue(ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.logMessages</code> attribute defined at extension <code>processing</code>. 
	 * @return the logMessages - Messages given during the process.
	 */
	@Accessor(qualifier = "logMessages", type = Accessor.Type.GETTER)
	public String getLogMessages()
	{
		return getPersistenceContext().getPropertyValue(LOGMESSAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.process</code> attribute defined at extension <code>processing</code>. 
	 * @return the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.GETTER)
	public BusinessProcessModel getProcess()
	{
		return getPersistenceContext().getPropertyValue(PROCESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.returnCode</code> attribute defined at extension <code>processing</code>. 
	 * @return the returnCode - Return code of the task.
	 */
	@Accessor(qualifier = "returnCode", type = Accessor.Type.GETTER)
	public String getReturnCode()
	{
		return getPersistenceContext().getPropertyValue(RETURNCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.startDate</code> attribute defined at extension <code>processing</code>. 
	 * @return the startDate - Date when task was started
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
	public Date getStartDate()
	{
		return getPersistenceContext().getPropertyValue(STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.actionId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the actionId - ID of the action performed
	 */
	@Accessor(qualifier = "actionId", type = Accessor.Type.SETTER)
	public void setActionId(final String value)
	{
		getPersistenceContext().setPropertyValue(ACTIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.clusterId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the clusterId - ID of the cluster where action performed
	 */
	@Accessor(qualifier = "clusterId", type = Accessor.Type.SETTER)
	public void setClusterId(final Integer value)
	{
		getPersistenceContext().setPropertyValue(CLUSTERID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.endDate</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the endDate - Date when task was ended
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
	public void setEndDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.logMessages</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logMessages - Messages given during the process.
	 */
	@Accessor(qualifier = "logMessages", type = Accessor.Type.SETTER)
	public void setLogMessages(final String value)
	{
		getPersistenceContext().setPropertyValue(LOGMESSAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.process</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.SETTER)
	public void setProcess(final BusinessProcessModel value)
	{
		getPersistenceContext().setPropertyValue(PROCESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.returnCode</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the returnCode - Return code of the task.
	 */
	@Accessor(qualifier = "returnCode", type = Accessor.Type.SETTER)
	public void setReturnCode(final String value)
	{
		getPersistenceContext().setPropertyValue(RETURNCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.startDate</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the startDate - Date when task was started
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
	public void setStartDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATE, value);
	}
	
}
