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
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.ChangeDescriptorModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.LogFileModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.processengine.enums.BooleanOperator;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type CronJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class CronJobModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CronJob";
	
	/**<i>Generated relation code constant for relation <code>JobCronJobRelation</code> defining source attribute <code>job</code> in extension <code>processing</code>.</i>*/
	public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.code</code> attribute defined at extension <code>processing</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.errorMode</code> attribute defined at extension <code>processing</code>. */
	public static final String ERRORMODE = "errorMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logToFile</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGTOFILE = "logToFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logToDatabase</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGTODATABASE = "logToDatabase";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logLevelFile</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGLEVELFILE = "logLevelFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logLevelDatabase</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGLEVELDATABASE = "logLevelDatabase";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logFiles</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGFILES = "logFiles";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.sessionUser</code> attribute defined at extension <code>processing</code>. */
	public static final String SESSIONUSER = "sessionUser";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.sessionLanguage</code> attribute defined at extension <code>processing</code>. */
	public static final String SESSIONLANGUAGE = "sessionLanguage";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.sessionCurrency</code> attribute defined at extension <code>processing</code>. */
	public static final String SESSIONCURRENCY = "sessionCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.sessionContextValues</code> attribute defined at extension <code>processing</code>. */
	public static final String SESSIONCONTEXTVALUES = "sessionContextValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.active</code> attribute defined at extension <code>processing</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.retry</code> attribute defined at extension <code>processing</code>. */
	public static final String RETRY = "retry";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.singleExecutable</code> attribute defined at extension <code>processing</code>. */
	public static final String SINGLEEXECUTABLE = "singleExecutable";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.emailAddress</code> attribute defined at extension <code>processing</code>. */
	public static final String EMAILADDRESS = "emailAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.sendEmail</code> attribute defined at extension <code>processing</code>. */
	public static final String SENDEMAIL = "sendEmail";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.startTime</code> attribute defined at extension <code>processing</code>. */
	public static final String STARTTIME = "startTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.endTime</code> attribute defined at extension <code>processing</code>. */
	public static final String ENDTIME = "endTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.status</code> attribute defined at extension <code>processing</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.result</code> attribute defined at extension <code>processing</code>. */
	public static final String RESULT = "result";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logText</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGTEXT = "logText";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.nodeID</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEID = "nodeID";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.nodeGroup</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEGROUP = "nodeGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. */
	public static final String RUNNINGONCLUSTERNODE = "runningOnClusterNode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.currentStep</code> attribute defined at extension <code>processing</code>. */
	public static final String CURRENTSTEP = "currentStep";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.changeRecordingEnabled</code> attribute defined at extension <code>processing</code>. */
	public static final String CHANGERECORDINGENABLED = "changeRecordingEnabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.changes</code> attribute defined at extension <code>processing</code>. */
	public static final String CHANGES = "changes";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.requestAbort</code> attribute defined at extension <code>processing</code>. */
	public static final String REQUESTABORT = "requestAbort";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.requestAbortStep</code> attribute defined at extension <code>processing</code>. */
	public static final String REQUESTABORTSTEP = "requestAbortStep";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.timeTable</code> attribute defined at extension <code>processing</code>. */
	public static final String TIMETABLE = "timeTable";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.priority</code> attribute defined at extension <code>processing</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.removeOnExit</code> attribute defined at extension <code>processing</code>. */
	public static final String REMOVEONEXIT = "removeOnExit";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.emailNotificationTemplate</code> attribute defined at extension <code>processing</code>. */
	public static final String EMAILNOTIFICATIONTEMPLATE = "emailNotificationTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.alternativeDataSourceID</code> attribute defined at extension <code>processing</code>. */
	public static final String ALTERNATIVEDATASOURCEID = "alternativeDataSourceID";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logsDaysOld</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGSDAYSOLD = "logsDaysOld";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logsCount</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGSCOUNT = "logsCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logsOperator</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGSOPERATOR = "logsOperator";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.filesDaysOld</code> attribute defined at extension <code>processing</code>. */
	public static final String FILESDAYSOLD = "filesDaysOld";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.filesCount</code> attribute defined at extension <code>processing</code>. */
	public static final String FILESCOUNT = "filesCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.filesOperator</code> attribute defined at extension <code>processing</code>. */
	public static final String FILESOPERATOR = "filesOperator";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.queryCount</code> attribute defined at extension <code>processing</code>. */
	public static final String QUERYCOUNT = "queryCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.activeCronJobHistory</code> attribute defined at extension <code>processing</code>. */
	public static final String ACTIVECRONJOBHISTORY = "activeCronJobHistory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.processedSteps</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESSEDSTEPS = "processedSteps";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.pendingSteps</code> attribute defined at extension <code>processing</code>. */
	public static final String PENDINGSTEPS = "pendingSteps";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.logs</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGS = "logs";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.triggers</code> attribute defined at extension <code>processing</code>. */
	public static final String TRIGGERS = "triggers";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.job</code> attribute defined at extension <code>processing</code>. */
	public static final String JOB = "job";
	
	/** <i>Generated constant</i> - Attribute key of <code>CronJob.cronJobHistoryEntries</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOBHISTORYENTRIES = "cronJobHistoryEntries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.active</code> attribute defined at extension <code>processing</code>. 
	 * @return the active - Whether the CronJob is active (is run when its trigger is due). If set to false, the
	 *                         CronJob will not be executed if its trigger is due
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.activeCronJobHistory</code> attribute defined at extension <code>processing</code>. 
	 * @return the activeCronJobHistory - Active CronJobHistory entry
	 */
	@Accessor(qualifier = "activeCronJobHistory", type = Accessor.Type.GETTER)
	public CronJobHistoryModel getActiveCronJobHistory()
	{
		return getPersistenceContext().getPropertyValue(ACTIVECRONJOBHISTORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.alternativeDataSourceID</code> attribute defined at extension <code>processing</code>. 
	 * @return the alternativeDataSourceID
	 */
	@Accessor(qualifier = "alternativeDataSourceID", type = Accessor.Type.GETTER)
	public String getAlternativeDataSourceID()
	{
		return getPersistenceContext().getPropertyValue(ALTERNATIVEDATASOURCEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.changeRecordingEnabled</code> attribute defined at extension <code>processing</code>. 
	 * @return the changeRecordingEnabled
	 */
	@Accessor(qualifier = "changeRecordingEnabled", type = Accessor.Type.GETTER)
	public Boolean getChangeRecordingEnabled()
	{
		return getPersistenceContext().getPropertyValue(CHANGERECORDINGENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.changes</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the changes
	 */
	@Accessor(qualifier = "changes", type = Accessor.Type.GETTER)
	public Collection<ChangeDescriptorModel> getChanges()
	{
		return getPersistenceContext().getPropertyValue(CHANGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.code</code> attribute defined at extension <code>processing</code>. 
	 * @return the code - Identifier for the CronJob
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.cronJobHistoryEntries</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cronJobHistoryEntries - history entries
	 */
	@Accessor(qualifier = "cronJobHistoryEntries", type = Accessor.Type.GETTER)
	public List<CronJobHistoryModel> getCronJobHistoryEntries()
	{
		return getPersistenceContext().getPropertyValue(CRONJOBHISTORYENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.currentStep</code> attribute defined at extension <code>processing</code>. 
	 * @return the currentStep
	 */
	@Accessor(qualifier = "currentStep", type = Accessor.Type.GETTER)
	public StepModel getCurrentStep()
	{
		return getPersistenceContext().getPropertyValue(CURRENTSTEP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.emailAddress</code> attribute defined at extension <code>processing</code>. 
	 * @return the emailAddress - The e-mail address to which to send a notification on the CronJob's execution
	 */
	@Accessor(qualifier = "emailAddress", type = Accessor.Type.GETTER)
	public String getEmailAddress()
	{
		return getPersistenceContext().getPropertyValue(EMAILADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.emailNotificationTemplate</code> attribute defined at extension <code>processing</code>. 
	 * @return the emailNotificationTemplate
	 */
	@Accessor(qualifier = "emailNotificationTemplate", type = Accessor.Type.GETTER)
	public RendererTemplateModel getEmailNotificationTemplate()
	{
		return getPersistenceContext().getPropertyValue(EMAILNOTIFICATIONTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.endTime</code> attribute defined at extension <code>processing</code>. 
	 * @return the endTime
	 */
	@Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
	public Date getEndTime()
	{
		return getPersistenceContext().getPropertyValue(ENDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.errorMode</code> attribute defined at extension <code>processing</code>. 
	 * @return the errorMode - the error mode. @since 2.10
	 */
	@Accessor(qualifier = "errorMode", type = Accessor.Type.GETTER)
	public ErrorMode getErrorMode()
	{
		return getPersistenceContext().getPropertyValue(ERRORMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.filesCount</code> attribute defined at extension <code>processing</code>. 
	 * @return the filesCount - All LogFiles (LogFileModels) above this value will be removed
	 */
	@Accessor(qualifier = "filesCount", type = Accessor.Type.GETTER)
	public int getFilesCount()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(FILESCOUNT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.filesDaysOld</code> attribute defined at extension <code>processing</code>. 
	 * @return the filesDaysOld - All LogFiles (LogFileModels) older than this value in days will be removed
	 */
	@Accessor(qualifier = "filesDaysOld", type = Accessor.Type.GETTER)
	public int getFilesDaysOld()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(FILESDAYSOLD));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.filesOperator</code> attribute defined at extension <code>processing</code>. 
	 * @return the filesOperator - Operator
	 */
	@Accessor(qualifier = "filesOperator", type = Accessor.Type.GETTER)
	public BooleanOperator getFilesOperator()
	{
		return getPersistenceContext().getPropertyValue(FILESOPERATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.job</code> attribute defined at extension <code>processing</code>. 
	 * @return the job - References to the Job assigned to the CronJob
	 */
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public JobModel getJob()
	{
		return getPersistenceContext().getPropertyValue(JOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logFiles</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the logFiles - A list of log files related to the current CronJob
	 */
	@Accessor(qualifier = "logFiles", type = Accessor.Type.GETTER)
	public Collection<LogFileModel> getLogFiles()
	{
		return getPersistenceContext().getPropertyValue(LOGFILES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logLevelDatabase</code> attribute defined at extension <code>processing</code>. 
	 * @return the logLevelDatabase - Specifies the log level for logging to the database
	 */
	@Accessor(qualifier = "logLevelDatabase", type = Accessor.Type.GETTER)
	public JobLogLevel getLogLevelDatabase()
	{
		return getPersistenceContext().getPropertyValue(LOGLEVELDATABASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logLevelFile</code> attribute defined at extension <code>processing</code>. 
	 * @return the logLevelFile - Specifies the log level for logging to the file
	 */
	@Accessor(qualifier = "logLevelFile", type = Accessor.Type.GETTER)
	public JobLogLevel getLogLevelFile()
	{
		return getPersistenceContext().getPropertyValue(LOGLEVELFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logs</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the logs - list of jobLogs
	 */
	@Accessor(qualifier = "logs", type = Accessor.Type.GETTER)
	public List<JobLogModel> getLogs()
	{
		return getPersistenceContext().getPropertyValue(LOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logsCount</code> attribute defined at extension <code>processing</code>. 
	 * @return the logsCount - All JobLogs (JobLogModels) above this value will be removed
	 */
	@Accessor(qualifier = "logsCount", type = Accessor.Type.GETTER)
	public int getLogsCount()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(LOGSCOUNT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logsDaysOld</code> attribute defined at extension <code>processing</code>. 
	 * @return the logsDaysOld - All JobLogs (JobLogModels) older than this value in days will be removed
	 */
	@Accessor(qualifier = "logsDaysOld", type = Accessor.Type.GETTER)
	public int getLogsDaysOld()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(LOGSDAYSOLD));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logsOperator</code> attribute defined at extension <code>processing</code>. 
	 * @return the logsOperator - Operator
	 */
	@Accessor(qualifier = "logsOperator", type = Accessor.Type.GETTER)
	public BooleanOperator getLogsOperator()
	{
		return getPersistenceContext().getPropertyValue(LOGSOPERATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logText</code> attribute defined at extension <code>processing</code>. 
	 * @return the logText
	 */
	@Accessor(qualifier = "logText", type = Accessor.Type.GETTER)
	public String getLogText()
	{
		return getPersistenceContext().getPropertyValue(LOGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logToDatabase</code> attribute defined at extension <code>processing</code>. 
	 * @return the logToDatabase - Whether the CronJob's execution log is written to a log entry in the hybris Suite's
	 *                         database
	 */
	@Accessor(qualifier = "logToDatabase", type = Accessor.Type.GETTER)
	public Boolean getLogToDatabase()
	{
		return getPersistenceContext().getPropertyValue(LOGTODATABASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.logToFile</code> attribute defined at extension <code>processing</code>. 
	 * @return the logToFile - Whether the CronJob's execution log is written to a log file
	 */
	@Accessor(qualifier = "logToFile", type = Accessor.Type.GETTER)
	public Boolean getLogToFile()
	{
		return getPersistenceContext().getPropertyValue(LOGTOFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
	public String getNodeGroup()
	{
		return getPersistenceContext().getPropertyValue(NODEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.nodeID</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeID - The number of the cluster node on which the CronJob is to be run. This setting is
	 *                         relevant for clustered hybris Suite installations only.
	 */
	@Accessor(qualifier = "nodeID", type = Accessor.Type.GETTER)
	public Integer getNodeID()
	{
		return getPersistenceContext().getPropertyValue(NODEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.pendingSteps</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the pendingSteps - pending steps
	 */
	@Accessor(qualifier = "pendingSteps", type = Accessor.Type.GETTER)
	public List<StepModel> getPendingSteps()
	{
		return getPersistenceContext().getPropertyValue(PENDINGSTEPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.priority</code> attribute defined at extension <code>processing</code>. 
	 * @return the priority - the priority. @since 2.10
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.processedSteps</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the processedSteps - processed steps
	 */
	@Accessor(qualifier = "processedSteps", type = Accessor.Type.GETTER)
	public List<StepModel> getProcessedSteps()
	{
		return getPersistenceContext().getPropertyValue(PROCESSEDSTEPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.queryCount</code> attribute defined at extension <code>processing</code>. 
	 * @return the queryCount - Maximum number of cron jobs processed during one run while cleaning cron jobs' logs.
	 */
	@Accessor(qualifier = "queryCount", type = Accessor.Type.GETTER)
	public int getQueryCount()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(QUERYCOUNT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.removeOnExit</code> attribute defined at extension <code>processing</code>. 
	 * @return the removeOnExit - If set to true, the CronJob is removed from the hybris Suite after execution
	 */
	@Accessor(qualifier = "removeOnExit", type = Accessor.Type.GETTER)
	public Boolean getRemoveOnExit()
	{
		return getPersistenceContext().getPropertyValue(REMOVEONEXIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.requestAbort</code> attribute defined at extension <code>processing</code>. 
	 * @return the requestAbort
	 */
	@Accessor(qualifier = "requestAbort", type = Accessor.Type.GETTER)
	public Boolean getRequestAbort()
	{
		return getPersistenceContext().getPropertyValue(REQUESTABORT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.requestAbortStep</code> attribute defined at extension <code>processing</code>. 
	 * @return the requestAbortStep
	 */
	@Accessor(qualifier = "requestAbortStep", type = Accessor.Type.GETTER)
	public Boolean getRequestAbortStep()
	{
		return getPersistenceContext().getPropertyValue(REQUESTABORTSTEP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.result</code> attribute defined at extension <code>processing</code>. 
	 * @return the result - The CronJob's execution result
	 */
	@Accessor(qualifier = "result", type = Accessor.Type.GETTER)
	public CronJobResult getResult()
	{
		return getPersistenceContext().getPropertyValue(RESULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.retry</code> attribute defined at extension <code>processing</code>. 
	 * @return the retry - If an assigned job can't be started because of an already running job, a trigger will
	 *                         be created for the the cronjob instance with current time as activation time. This one will be
	 *                         triggerd on next poll repeatedly till the cronjob gets executed once
	 */
	@Accessor(qualifier = "retry", type = Accessor.Type.GETTER)
	public Boolean getRetry()
	{
		return getPersistenceContext().getPropertyValue(RETRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. 
	 * @return the runningOnClusterNode
	 */
	@Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.GETTER)
	public Integer getRunningOnClusterNode()
	{
		return getPersistenceContext().getPropertyValue(RUNNINGONCLUSTERNODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.sendEmail</code> attribute defined at extension <code>processing</code>. 
	 * @return the sendEmail - Whether a status e-mail is to be sent after the CronJob's execution
	 */
	@Accessor(qualifier = "sendEmail", type = Accessor.Type.GETTER)
	public Boolean getSendEmail()
	{
		return getPersistenceContext().getPropertyValue(SENDEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.sessionCurrency</code> attribute defined at extension <code>processing</code>. 
	 * @return the sessionCurrency - The system currency with which to execute the CronJob
	 */
	@Accessor(qualifier = "sessionCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getSessionCurrency()
	{
		return getPersistenceContext().getPropertyValue(SESSIONCURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.sessionLanguage</code> attribute defined at extension <code>processing</code>. 
	 * @return the sessionLanguage - The system language to execute the CronJob in
	 */
	@Accessor(qualifier = "sessionLanguage", type = Accessor.Type.GETTER)
	public LanguageModel getSessionLanguage()
	{
		return getPersistenceContext().getPropertyValue(SESSIONLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.sessionUser</code> attribute defined at extension <code>processing</code>. 
	 * @return the sessionUser - The user in whose context (access rights, restrictions, etc) the CronJob will be
	 *                         executed
	 */
	@Accessor(qualifier = "sessionUser", type = Accessor.Type.GETTER)
	public UserModel getSessionUser()
	{
		return getPersistenceContext().getPropertyValue(SESSIONUSER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.singleExecutable</code> attribute defined at extension <code>processing</code>. 
	 * @return the singleExecutable - If the assigned TriggerableJob instance can not be performed yet, a new Trigger for
	 *                         this CronJob will be created
	 */
	@Accessor(qualifier = "singleExecutable", type = Accessor.Type.GETTER)
	public Boolean getSingleExecutable()
	{
		return getPersistenceContext().getPropertyValue(SINGLEEXECUTABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.startTime</code> attribute defined at extension <code>processing</code>. 
	 * @return the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
	public Date getStartTime()
	{
		return getPersistenceContext().getPropertyValue(STARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.status</code> attribute defined at extension <code>processing</code>. 
	 * @return the status - The CronJob's execution status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public CronJobStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.timeTable</code> dynamic attribute defined at extension <code>processing</code>. 
	 * @return the timeTable
	 */
	@Accessor(qualifier = "timeTable", type = Accessor.Type.GETTER)
	public String getTimeTable()
	{
		return getPersistenceContext().getDynamicValue(this,TIMETABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.triggers</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the triggers - list of triggers
	 */
	@Accessor(qualifier = "triggers", type = Accessor.Type.GETTER)
	public List<TriggerModel> getTriggers()
	{
		return getPersistenceContext().getPropertyValue(TRIGGERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.active</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the active - Whether the CronJob is active (is run when its trigger is due). If set to false, the
	 *                         CronJob will not be executed if its trigger is due
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.activeCronJobHistory</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the activeCronJobHistory - Active CronJobHistory entry
	 */
	@Accessor(qualifier = "activeCronJobHistory", type = Accessor.Type.SETTER)
	public void setActiveCronJobHistory(final CronJobHistoryModel value)
	{
		getPersistenceContext().setPropertyValue(ACTIVECRONJOBHISTORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.alternativeDataSourceID</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the alternativeDataSourceID
	 */
	@Accessor(qualifier = "alternativeDataSourceID", type = Accessor.Type.SETTER)
	public void setAlternativeDataSourceID(final String value)
	{
		getPersistenceContext().setPropertyValue(ALTERNATIVEDATASOURCEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.changeRecordingEnabled</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the changeRecordingEnabled
	 */
	@Accessor(qualifier = "changeRecordingEnabled", type = Accessor.Type.SETTER)
	public void setChangeRecordingEnabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CHANGERECORDINGENABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.code</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the code - Identifier for the CronJob
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.cronJobHistoryEntries</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the cronJobHistoryEntries - history entries
	 */
	@Accessor(qualifier = "cronJobHistoryEntries", type = Accessor.Type.SETTER)
	public void setCronJobHistoryEntries(final List<CronJobHistoryModel> value)
	{
		getPersistenceContext().setPropertyValue(CRONJOBHISTORYENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.emailAddress</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the emailAddress - The e-mail address to which to send a notification on the CronJob's execution
	 */
	@Accessor(qualifier = "emailAddress", type = Accessor.Type.SETTER)
	public void setEmailAddress(final String value)
	{
		getPersistenceContext().setPropertyValue(EMAILADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.emailNotificationTemplate</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the emailNotificationTemplate
	 */
	@Accessor(qualifier = "emailNotificationTemplate", type = Accessor.Type.SETTER)
	public void setEmailNotificationTemplate(final RendererTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(EMAILNOTIFICATIONTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.endTime</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the endTime
	 */
	@Accessor(qualifier = "endTime", type = Accessor.Type.SETTER)
	public void setEndTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.errorMode</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the errorMode - the error mode. @since 2.10
	 */
	@Accessor(qualifier = "errorMode", type = Accessor.Type.SETTER)
	public void setErrorMode(final ErrorMode value)
	{
		getPersistenceContext().setPropertyValue(ERRORMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.filesCount</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the filesCount - All LogFiles (LogFileModels) above this value will be removed
	 */
	@Accessor(qualifier = "filesCount", type = Accessor.Type.SETTER)
	public void setFilesCount(final int value)
	{
		getPersistenceContext().setPropertyValue(FILESCOUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.filesDaysOld</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the filesDaysOld - All LogFiles (LogFileModels) older than this value in days will be removed
	 */
	@Accessor(qualifier = "filesDaysOld", type = Accessor.Type.SETTER)
	public void setFilesDaysOld(final int value)
	{
		getPersistenceContext().setPropertyValue(FILESDAYSOLD, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.filesOperator</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the filesOperator - Operator
	 */
	@Accessor(qualifier = "filesOperator", type = Accessor.Type.SETTER)
	public void setFilesOperator(final BooleanOperator value)
	{
		getPersistenceContext().setPropertyValue(FILESOPERATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJob.job</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the job - References to the Job assigned to the CronJob
	 */
	@Accessor(qualifier = "job", type = Accessor.Type.SETTER)
	public void setJob(final JobModel value)
	{
		getPersistenceContext().setPropertyValue(JOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.logFiles</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logFiles - A list of log files related to the current CronJob
	 */
	@Accessor(qualifier = "logFiles", type = Accessor.Type.SETTER)
	public void setLogFiles(final Collection<LogFileModel> value)
	{
		getPersistenceContext().setPropertyValue(LOGFILES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.logLevelDatabase</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logLevelDatabase - Specifies the log level for logging to the database
	 */
	@Accessor(qualifier = "logLevelDatabase", type = Accessor.Type.SETTER)
	public void setLogLevelDatabase(final JobLogLevel value)
	{
		getPersistenceContext().setPropertyValue(LOGLEVELDATABASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.logLevelFile</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logLevelFile - Specifies the log level for logging to the file
	 */
	@Accessor(qualifier = "logLevelFile", type = Accessor.Type.SETTER)
	public void setLogLevelFile(final JobLogLevel value)
	{
		getPersistenceContext().setPropertyValue(LOGLEVELFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.logsCount</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logsCount - All JobLogs (JobLogModels) above this value will be removed
	 */
	@Accessor(qualifier = "logsCount", type = Accessor.Type.SETTER)
	public void setLogsCount(final int value)
	{
		getPersistenceContext().setPropertyValue(LOGSCOUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.logsDaysOld</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logsDaysOld - All JobLogs (JobLogModels) older than this value in days will be removed
	 */
	@Accessor(qualifier = "logsDaysOld", type = Accessor.Type.SETTER)
	public void setLogsDaysOld(final int value)
	{
		getPersistenceContext().setPropertyValue(LOGSDAYSOLD, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.logsOperator</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logsOperator - Operator
	 */
	@Accessor(qualifier = "logsOperator", type = Accessor.Type.SETTER)
	public void setLogsOperator(final BooleanOperator value)
	{
		getPersistenceContext().setPropertyValue(LOGSOPERATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.logToDatabase</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logToDatabase - Whether the CronJob's execution log is written to a log entry in the hybris Suite's
	 *                         database
	 */
	@Accessor(qualifier = "logToDatabase", type = Accessor.Type.SETTER)
	public void setLogToDatabase(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOGTODATABASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.logToFile</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logToFile - Whether the CronJob's execution log is written to a log file
	 */
	@Accessor(qualifier = "logToFile", type = Accessor.Type.SETTER)
	public void setLogToFile(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOGTOFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
	public void setNodeGroup(final String value)
	{
		getPersistenceContext().setPropertyValue(NODEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.nodeID</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the nodeID - The number of the cluster node on which the CronJob is to be run. This setting is
	 *                         relevant for clustered hybris Suite installations only.
	 */
	@Accessor(qualifier = "nodeID", type = Accessor.Type.SETTER)
	public void setNodeID(final Integer value)
	{
		getPersistenceContext().setPropertyValue(NODEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.pendingSteps</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the pendingSteps - pending steps
	 */
	@Accessor(qualifier = "pendingSteps", type = Accessor.Type.SETTER)
	public void setPendingSteps(final List<StepModel> value)
	{
		getPersistenceContext().setPropertyValue(PENDINGSTEPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.priority</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the priority - the priority. @since 2.10
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.processedSteps</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the processedSteps - processed steps
	 */
	@Accessor(qualifier = "processedSteps", type = Accessor.Type.SETTER)
	public void setProcessedSteps(final List<StepModel> value)
	{
		getPersistenceContext().setPropertyValue(PROCESSEDSTEPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.queryCount</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the queryCount - Maximum number of cron jobs processed during one run while cleaning cron jobs' logs.
	 */
	@Accessor(qualifier = "queryCount", type = Accessor.Type.SETTER)
	public void setQueryCount(final int value)
	{
		getPersistenceContext().setPropertyValue(QUERYCOUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.removeOnExit</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the removeOnExit - If set to true, the CronJob is removed from the hybris Suite after execution
	 */
	@Accessor(qualifier = "removeOnExit", type = Accessor.Type.SETTER)
	public void setRemoveOnExit(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REMOVEONEXIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.requestAbort</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the requestAbort
	 */
	@Accessor(qualifier = "requestAbort", type = Accessor.Type.SETTER)
	public void setRequestAbort(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REQUESTABORT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.requestAbortStep</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the requestAbortStep
	 */
	@Accessor(qualifier = "requestAbortStep", type = Accessor.Type.SETTER)
	public void setRequestAbortStep(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REQUESTABORTSTEP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.result</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the result - The CronJob's execution result
	 */
	@Accessor(qualifier = "result", type = Accessor.Type.SETTER)
	public void setResult(final CronJobResult value)
	{
		getPersistenceContext().setPropertyValue(RESULT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.retry</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retry - If an assigned job can't be started because of an already running job, a trigger will
	 *                         be created for the the cronjob instance with current time as activation time. This one will be
	 *                         triggerd on next poll repeatedly till the cronjob gets executed once
	 */
	@Accessor(qualifier = "retry", type = Accessor.Type.SETTER)
	public void setRetry(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(RETRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the runningOnClusterNode
	 */
	@Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.SETTER)
	public void setRunningOnClusterNode(final Integer value)
	{
		getPersistenceContext().setPropertyValue(RUNNINGONCLUSTERNODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.sendEmail</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the sendEmail - Whether a status e-mail is to be sent after the CronJob's execution
	 */
	@Accessor(qualifier = "sendEmail", type = Accessor.Type.SETTER)
	public void setSendEmail(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SENDEMAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.sessionCurrency</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the sessionCurrency - The system currency with which to execute the CronJob
	 */
	@Accessor(qualifier = "sessionCurrency", type = Accessor.Type.SETTER)
	public void setSessionCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(SESSIONCURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.sessionLanguage</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the sessionLanguage - The system language to execute the CronJob in
	 */
	@Accessor(qualifier = "sessionLanguage", type = Accessor.Type.SETTER)
	public void setSessionLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(SESSIONLANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.sessionUser</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the sessionUser - The user in whose context (access rights, restrictions, etc) the CronJob will be
	 *                         executed
	 */
	@Accessor(qualifier = "sessionUser", type = Accessor.Type.SETTER)
	public void setSessionUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(SESSIONUSER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.singleExecutable</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the singleExecutable - If the assigned TriggerableJob instance can not be performed yet, a new Trigger for
	 *                         this CronJob will be created
	 */
	@Accessor(qualifier = "singleExecutable", type = Accessor.Type.SETTER)
	public void setSingleExecutable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SINGLEEXECUTABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.startTime</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
	public void setStartTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.status</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the status - The CronJob's execution status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final CronJobStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.triggers</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the triggers - list of triggers
	 */
	@Accessor(qualifier = "triggers", type = Accessor.Type.SETTER)
	public void setTriggers(final List<TriggerModel> value)
	{
		getPersistenceContext().setPropertyValue(TRIGGERS, value);
	}
	
}
