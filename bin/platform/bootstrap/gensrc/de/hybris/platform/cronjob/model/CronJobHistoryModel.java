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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type CronJobHistory first defined at extension processing.
 */
@SuppressWarnings("all")
public class CronJobHistoryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CronJobHistory";
	
	/**<i>Generated relation code constant for relation <code>CronJobHistoryRelation</code> defining source attribute <code>cronJob</code> in extension <code>processing</code>.</i>*/
	public static final String _CRONJOBHISTORYRELATION = "CronJobHistoryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.cronJobCode</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOBCODE = "cronJobCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.jobCode</code> attribute defined at extension <code>processing</code>. */
	public static final String JOBCODE = "jobCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.startTime</code> attribute defined at extension <code>processing</code>. */
	public static final String STARTTIME = "startTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.endTime</code> attribute defined at extension <code>processing</code>. */
	public static final String ENDTIME = "endTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.nodeID</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEID = "nodeID";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.scheduled</code> attribute defined at extension <code>processing</code>. */
	public static final String SCHEDULED = "scheduled";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.userUid</code> attribute defined at extension <code>processing</code>. */
	public static final String USERUID = "userUid";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.status</code> attribute defined at extension <code>processing</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.result</code> attribute defined at extension <code>processing</code>. */
	public static final String RESULT = "result";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.failureMessage</code> attribute defined at extension <code>processing</code>. */
	public static final String FAILUREMESSAGE = "failureMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.progress</code> attribute defined at extension <code>processing</code>. */
	public static final String PROGRESS = "progress";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.statusLine</code> attribute defined at extension <code>processing</code>. */
	public static final String STATUSLINE = "statusLine";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.cronJobPOS</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOBPOS = "cronJobPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJobHistory.cronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOB = "cronJob";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CronJobHistoryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CronJobHistoryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJobCode initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _jobCode initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _startTime initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CronJobHistoryModel(final String _cronJobCode, final String _jobCode, final int _nodeID, final Date _startTime)
	{
		super();
		setCronJobCode(_cronJobCode);
		setJobCode(_jobCode);
		setNodeID(_nodeID);
		setStartTime(_startTime);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJobCode initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _jobCode initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _startTime initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CronJobHistoryModel(final String _cronJobCode, final String _jobCode, final int _nodeID, final ItemModel _owner, final Date _startTime)
	{
		super();
		setCronJobCode(_cronJobCode);
		setJobCode(_jobCode);
		setNodeID(_nodeID);
		setOwner(_owner);
		setStartTime(_startTime);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.cronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the cronJob - cronjob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
	public CronJobModel getCronJob()
	{
		return getPersistenceContext().getPropertyValue(CRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.cronJobCode</code> attribute defined at extension <code>processing</code>. 
	 * @return the cronJobCode
	 */
	@Accessor(qualifier = "cronJobCode", type = Accessor.Type.GETTER)
	public String getCronJobCode()
	{
		return getPersistenceContext().getPropertyValue(CRONJOBCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.endTime</code> attribute defined at extension <code>processing</code>. 
	 * @return the endTime
	 */
	@Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
	public Date getEndTime()
	{
		return getPersistenceContext().getPropertyValue(ENDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.failureMessage</code> attribute defined at extension <code>processing</code>. 
	 * @return the failureMessage
	 */
	@Accessor(qualifier = "failureMessage", type = Accessor.Type.GETTER)
	public String getFailureMessage()
	{
		return getPersistenceContext().getPropertyValue(FAILUREMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.jobCode</code> attribute defined at extension <code>processing</code>. 
	 * @return the jobCode
	 */
	@Accessor(qualifier = "jobCode", type = Accessor.Type.GETTER)
	public String getJobCode()
	{
		return getPersistenceContext().getPropertyValue(JOBCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.nodeID</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeID
	 */
	@Accessor(qualifier = "nodeID", type = Accessor.Type.GETTER)
	public int getNodeID()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(NODEID));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.progress</code> attribute defined at extension <code>processing</code>. 
	 * @return the progress
	 */
	@Accessor(qualifier = "progress", type = Accessor.Type.GETTER)
	public Double getProgress()
	{
		return getPersistenceContext().getPropertyValue(PROGRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.result</code> attribute defined at extension <code>processing</code>. 
	 * @return the result
	 */
	@Accessor(qualifier = "result", type = Accessor.Type.GETTER)
	public CronJobResult getResult()
	{
		return getPersistenceContext().getPropertyValue(RESULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.scheduled</code> attribute defined at extension <code>processing</code>. 
	 * @return the scheduled
	 */
	@Accessor(qualifier = "scheduled", type = Accessor.Type.GETTER)
	public Boolean getScheduled()
	{
		return getPersistenceContext().getPropertyValue(SCHEDULED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.startTime</code> attribute defined at extension <code>processing</code>. 
	 * @return the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
	public Date getStartTime()
	{
		return getPersistenceContext().getPropertyValue(STARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.status</code> attribute defined at extension <code>processing</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public CronJobStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.statusLine</code> attribute defined at extension <code>processing</code>. 
	 * @return the statusLine
	 */
	@Accessor(qualifier = "statusLine", type = Accessor.Type.GETTER)
	public String getStatusLine()
	{
		return getPersistenceContext().getPropertyValue(STATUSLINE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJobHistory.userUid</code> attribute defined at extension <code>processing</code>. 
	 * @return the userUid
	 */
	@Accessor(qualifier = "userUid", type = Accessor.Type.GETTER)
	public String getUserUid()
	{
		return getPersistenceContext().getPropertyValue(USERUID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.cronJob</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the cronJob - cronjob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
	public void setCronJob(final CronJobModel value)
	{
		getPersistenceContext().setPropertyValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJobHistory.cronJobCode</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronJobCode
	 */
	@Accessor(qualifier = "cronJobCode", type = Accessor.Type.SETTER)
	public void setCronJobCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CRONJOBCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.endTime</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the endTime
	 */
	@Accessor(qualifier = "endTime", type = Accessor.Type.SETTER)
	public void setEndTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.failureMessage</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the failureMessage
	 */
	@Accessor(qualifier = "failureMessage", type = Accessor.Type.SETTER)
	public void setFailureMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(FAILUREMESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJobHistory.jobCode</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the jobCode
	 */
	@Accessor(qualifier = "jobCode", type = Accessor.Type.SETTER)
	public void setJobCode(final String value)
	{
		getPersistenceContext().setPropertyValue(JOBCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.nodeID</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the nodeID
	 */
	@Accessor(qualifier = "nodeID", type = Accessor.Type.SETTER)
	public void setNodeID(final int value)
	{
		getPersistenceContext().setPropertyValue(NODEID, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.progress</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the progress
	 */
	@Accessor(qualifier = "progress", type = Accessor.Type.SETTER)
	public void setProgress(final Double value)
	{
		getPersistenceContext().setPropertyValue(PROGRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.result</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the result
	 */
	@Accessor(qualifier = "result", type = Accessor.Type.SETTER)
	public void setResult(final CronJobResult value)
	{
		getPersistenceContext().setPropertyValue(RESULT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.scheduled</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the scheduled
	 */
	@Accessor(qualifier = "scheduled", type = Accessor.Type.SETTER)
	public void setScheduled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SCHEDULED, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJobHistory.startTime</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
	public void setStartTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.status</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final CronJobStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.statusLine</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the statusLine
	 */
	@Accessor(qualifier = "statusLine", type = Accessor.Type.SETTER)
	public void setStatusLine(final String value)
	{
		getPersistenceContext().setPropertyValue(STATUSLINE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJobHistory.userUid</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the userUid
	 */
	@Accessor(qualifier = "userUid", type = Accessor.Type.SETTER)
	public void setUserUid(final String value)
	{
		getPersistenceContext().setPropertyValue(USERUID, value);
	}
	
}
