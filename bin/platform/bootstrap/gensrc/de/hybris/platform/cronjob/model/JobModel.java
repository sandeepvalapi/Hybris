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
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobSearchRestrictionModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type Job first defined at extension processing.
 */
@SuppressWarnings("all")
public class JobModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Job";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.code</code> attribute defined at extension <code>processing</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.nodeID</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEID = "nodeID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.nodeGroup</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEGROUP = "nodeGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.errorMode</code> attribute defined at extension <code>processing</code>. */
	public static final String ERRORMODE = "errorMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.logToFile</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGTOFILE = "logToFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.logToDatabase</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGTODATABASE = "logToDatabase";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.logLevelFile</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGLEVELFILE = "logLevelFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.logLevelDatabase</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGLEVELDATABASE = "logLevelDatabase";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.sessionUser</code> attribute defined at extension <code>processing</code>. */
	public static final String SESSIONUSER = "sessionUser";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.sessionLanguage</code> attribute defined at extension <code>processing</code>. */
	public static final String SESSIONLANGUAGE = "sessionLanguage";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.sessionCurrency</code> attribute defined at extension <code>processing</code>. */
	public static final String SESSIONCURRENCY = "sessionCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.sessionContextValues</code> attribute defined at extension <code>processing</code>. */
	public static final String SESSIONCONTEXTVALUES = "sessionContextValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.active</code> attribute defined at extension <code>processing</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.retry</code> attribute defined at extension <code>processing</code>. */
	public static final String RETRY = "retry";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.singleExecutable</code> attribute defined at extension <code>processing</code>. */
	public static final String SINGLEEXECUTABLE = "singleExecutable";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.emailAddress</code> attribute defined at extension <code>processing</code>. */
	public static final String EMAILADDRESS = "emailAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.sendEmail</code> attribute defined at extension <code>processing</code>. */
	public static final String SENDEMAIL = "sendEmail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.changeRecordingEnabled</code> attribute defined at extension <code>processing</code>. */
	public static final String CHANGERECORDINGENABLED = "changeRecordingEnabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.requestAbort</code> attribute defined at extension <code>processing</code>. */
	public static final String REQUESTABORT = "requestAbort";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.requestAbortStep</code> attribute defined at extension <code>processing</code>. */
	public static final String REQUESTABORTSTEP = "requestAbortStep";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.priority</code> attribute defined at extension <code>processing</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.removeOnExit</code> attribute defined at extension <code>processing</code>. */
	public static final String REMOVEONEXIT = "removeOnExit";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.emailNotificationTemplate</code> attribute defined at extension <code>processing</code>. */
	public static final String EMAILNOTIFICATIONTEMPLATE = "emailNotificationTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.alternativeDataSourceID</code> attribute defined at extension <code>processing</code>. */
	public static final String ALTERNATIVEDATASOURCEID = "alternativeDataSourceID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.restrictions</code> attribute defined at extension <code>processing</code>. */
	public static final String RESTRICTIONS = "restrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.triggers</code> attribute defined at extension <code>processing</code>. */
	public static final String TRIGGERS = "triggers";
	
	/** <i>Generated constant</i> - Attribute key of <code>Job.cronJobs</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOBS = "cronJobs";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 */
	@Deprecated
	public JobModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public JobModel(final String _code, final Integer _nodeID, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.active</code> attribute defined at extension <code>processing</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.alternativeDataSourceID</code> attribute defined at extension <code>processing</code>. 
	 * @return the alternativeDataSourceID
	 */
	@Accessor(qualifier = "alternativeDataSourceID", type = Accessor.Type.GETTER)
	public String getAlternativeDataSourceID()
	{
		return getPersistenceContext().getPropertyValue(ALTERNATIVEDATASOURCEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.changeRecordingEnabled</code> attribute defined at extension <code>processing</code>. 
	 * @return the changeRecordingEnabled
	 */
	@Accessor(qualifier = "changeRecordingEnabled", type = Accessor.Type.GETTER)
	public Boolean getChangeRecordingEnabled()
	{
		return getPersistenceContext().getPropertyValue(CHANGERECORDINGENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.code</code> attribute defined at extension <code>processing</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.cronJobs</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cronJobs - CronJobs the job is assigned to
	 */
	@Accessor(qualifier = "cronJobs", type = Accessor.Type.GETTER)
	public Collection<CronJobModel> getCronJobs()
	{
		return getPersistenceContext().getPropertyValue(CRONJOBS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.emailAddress</code> attribute defined at extension <code>processing</code>. 
	 * @return the emailAddress
	 */
	@Accessor(qualifier = "emailAddress", type = Accessor.Type.GETTER)
	public String getEmailAddress()
	{
		return getPersistenceContext().getPropertyValue(EMAILADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.emailNotificationTemplate</code> attribute defined at extension <code>processing</code>. 
	 * @return the emailNotificationTemplate
	 */
	@Accessor(qualifier = "emailNotificationTemplate", type = Accessor.Type.GETTER)
	public RendererTemplateModel getEmailNotificationTemplate()
	{
		return getPersistenceContext().getPropertyValue(EMAILNOTIFICATIONTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.errorMode</code> attribute defined at extension <code>processing</code>. 
	 * @return the errorMode - the error mode. @since 2.10
	 */
	@Accessor(qualifier = "errorMode", type = Accessor.Type.GETTER)
	public ErrorMode getErrorMode()
	{
		return getPersistenceContext().getPropertyValue(ERRORMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.logLevelDatabase</code> attribute defined at extension <code>processing</code>. 
	 * @return the logLevelDatabase
	 */
	@Accessor(qualifier = "logLevelDatabase", type = Accessor.Type.GETTER)
	public JobLogLevel getLogLevelDatabase()
	{
		return getPersistenceContext().getPropertyValue(LOGLEVELDATABASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.logLevelFile</code> attribute defined at extension <code>processing</code>. 
	 * @return the logLevelFile
	 */
	@Accessor(qualifier = "logLevelFile", type = Accessor.Type.GETTER)
	public JobLogLevel getLogLevelFile()
	{
		return getPersistenceContext().getPropertyValue(LOGLEVELFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.logToDatabase</code> attribute defined at extension <code>processing</code>. 
	 * @return the logToDatabase
	 */
	@Accessor(qualifier = "logToDatabase", type = Accessor.Type.GETTER)
	public Boolean getLogToDatabase()
	{
		return getPersistenceContext().getPropertyValue(LOGTODATABASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.logToFile</code> attribute defined at extension <code>processing</code>. 
	 * @return the logToFile
	 */
	@Accessor(qualifier = "logToFile", type = Accessor.Type.GETTER)
	public Boolean getLogToFile()
	{
		return getPersistenceContext().getPropertyValue(LOGTOFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
	public String getNodeGroup()
	{
		return getPersistenceContext().getPropertyValue(NODEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.nodeID</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeID - Node ID
	 */
	@Accessor(qualifier = "nodeID", type = Accessor.Type.GETTER)
	public Integer getNodeID()
	{
		return getPersistenceContext().getPropertyValue(NODEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.priority</code> attribute defined at extension <code>processing</code>. 
	 * @return the priority - the priority. @since 2.10
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.removeOnExit</code> attribute defined at extension <code>processing</code>. 
	 * @return the removeOnExit
	 */
	@Accessor(qualifier = "removeOnExit", type = Accessor.Type.GETTER)
	public Boolean getRemoveOnExit()
	{
		return getPersistenceContext().getPropertyValue(REMOVEONEXIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.requestAbort</code> attribute defined at extension <code>processing</code>. 
	 * @return the requestAbort
	 */
	@Accessor(qualifier = "requestAbort", type = Accessor.Type.GETTER)
	public Boolean getRequestAbort()
	{
		return getPersistenceContext().getPropertyValue(REQUESTABORT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.requestAbortStep</code> attribute defined at extension <code>processing</code>. 
	 * @return the requestAbortStep
	 */
	@Accessor(qualifier = "requestAbortStep", type = Accessor.Type.GETTER)
	public Boolean getRequestAbortStep()
	{
		return getPersistenceContext().getPropertyValue(REQUESTABORTSTEP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.restrictions</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the restrictions - processed steps
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
	public List<JobSearchRestrictionModel> getRestrictions()
	{
		return getPersistenceContext().getPropertyValue(RESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.retry</code> attribute defined at extension <code>processing</code>. 
	 * @return the retry
	 */
	@Accessor(qualifier = "retry", type = Accessor.Type.GETTER)
	public Boolean getRetry()
	{
		return getPersistenceContext().getPropertyValue(RETRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.sendEmail</code> attribute defined at extension <code>processing</code>. 
	 * @return the sendEmail
	 */
	@Accessor(qualifier = "sendEmail", type = Accessor.Type.GETTER)
	public Boolean getSendEmail()
	{
		return getPersistenceContext().getPropertyValue(SENDEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.sessionCurrency</code> attribute defined at extension <code>processing</code>. 
	 * @return the sessionCurrency
	 */
	@Accessor(qualifier = "sessionCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getSessionCurrency()
	{
		return getPersistenceContext().getPropertyValue(SESSIONCURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.sessionLanguage</code> attribute defined at extension <code>processing</code>. 
	 * @return the sessionLanguage
	 */
	@Accessor(qualifier = "sessionLanguage", type = Accessor.Type.GETTER)
	public LanguageModel getSessionLanguage()
	{
		return getPersistenceContext().getPropertyValue(SESSIONLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.sessionUser</code> attribute defined at extension <code>processing</code>. 
	 * @return the sessionUser
	 */
	@Accessor(qualifier = "sessionUser", type = Accessor.Type.GETTER)
	public UserModel getSessionUser()
	{
		return getPersistenceContext().getPropertyValue(SESSIONUSER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.singleExecutable</code> attribute defined at extension <code>processing</code>. 
	 * @return the singleExecutable
	 */
	@Accessor(qualifier = "singleExecutable", type = Accessor.Type.GETTER)
	public Boolean getSingleExecutable()
	{
		return getPersistenceContext().getPropertyValue(SINGLEEXECUTABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Job.triggers</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the triggers - list of triggers
	 */
	@Accessor(qualifier = "triggers", type = Accessor.Type.GETTER)
	public List<TriggerModel> getTriggers()
	{
		return getPersistenceContext().getPropertyValue(TRIGGERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.active</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.alternativeDataSourceID</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the alternativeDataSourceID
	 */
	@Accessor(qualifier = "alternativeDataSourceID", type = Accessor.Type.SETTER)
	public void setAlternativeDataSourceID(final String value)
	{
		getPersistenceContext().setPropertyValue(ALTERNATIVEDATASOURCEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.changeRecordingEnabled</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the changeRecordingEnabled
	 */
	@Accessor(qualifier = "changeRecordingEnabled", type = Accessor.Type.SETTER)
	public void setChangeRecordingEnabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CHANGERECORDINGENABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.code</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.emailAddress</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the emailAddress
	 */
	@Accessor(qualifier = "emailAddress", type = Accessor.Type.SETTER)
	public void setEmailAddress(final String value)
	{
		getPersistenceContext().setPropertyValue(EMAILADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.emailNotificationTemplate</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the emailNotificationTemplate
	 */
	@Accessor(qualifier = "emailNotificationTemplate", type = Accessor.Type.SETTER)
	public void setEmailNotificationTemplate(final RendererTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(EMAILNOTIFICATIONTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.errorMode</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the errorMode - the error mode. @since 2.10
	 */
	@Accessor(qualifier = "errorMode", type = Accessor.Type.SETTER)
	public void setErrorMode(final ErrorMode value)
	{
		getPersistenceContext().setPropertyValue(ERRORMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.logLevelDatabase</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logLevelDatabase
	 */
	@Accessor(qualifier = "logLevelDatabase", type = Accessor.Type.SETTER)
	public void setLogLevelDatabase(final JobLogLevel value)
	{
		getPersistenceContext().setPropertyValue(LOGLEVELDATABASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.logLevelFile</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logLevelFile
	 */
	@Accessor(qualifier = "logLevelFile", type = Accessor.Type.SETTER)
	public void setLogLevelFile(final JobLogLevel value)
	{
		getPersistenceContext().setPropertyValue(LOGLEVELFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.logToDatabase</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logToDatabase
	 */
	@Accessor(qualifier = "logToDatabase", type = Accessor.Type.SETTER)
	public void setLogToDatabase(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOGTODATABASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.logToFile</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logToFile
	 */
	@Accessor(qualifier = "logToFile", type = Accessor.Type.SETTER)
	public void setLogToFile(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOGTOFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
	public void setNodeGroup(final String value)
	{
		getPersistenceContext().setPropertyValue(NODEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Job.nodeID</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the nodeID - Node ID
	 */
	@Accessor(qualifier = "nodeID", type = Accessor.Type.SETTER)
	public void setNodeID(final Integer value)
	{
		getPersistenceContext().setPropertyValue(NODEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.priority</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the priority - the priority. @since 2.10
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.removeOnExit</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the removeOnExit
	 */
	@Accessor(qualifier = "removeOnExit", type = Accessor.Type.SETTER)
	public void setRemoveOnExit(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REMOVEONEXIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.requestAbort</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the requestAbort
	 */
	@Accessor(qualifier = "requestAbort", type = Accessor.Type.SETTER)
	public void setRequestAbort(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REQUESTABORT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.requestAbortStep</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the requestAbortStep
	 */
	@Accessor(qualifier = "requestAbortStep", type = Accessor.Type.SETTER)
	public void setRequestAbortStep(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REQUESTABORTSTEP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.restrictions</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the restrictions - processed steps
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
	public void setRestrictions(final List<JobSearchRestrictionModel> value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.retry</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retry
	 */
	@Accessor(qualifier = "retry", type = Accessor.Type.SETTER)
	public void setRetry(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(RETRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.sendEmail</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the sendEmail
	 */
	@Accessor(qualifier = "sendEmail", type = Accessor.Type.SETTER)
	public void setSendEmail(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SENDEMAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.sessionCurrency</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the sessionCurrency
	 */
	@Accessor(qualifier = "sessionCurrency", type = Accessor.Type.SETTER)
	public void setSessionCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(SESSIONCURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.sessionLanguage</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the sessionLanguage
	 */
	@Accessor(qualifier = "sessionLanguage", type = Accessor.Type.SETTER)
	public void setSessionLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(SESSIONLANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.sessionUser</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the sessionUser
	 */
	@Accessor(qualifier = "sessionUser", type = Accessor.Type.SETTER)
	public void setSessionUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(SESSIONUSER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.singleExecutable</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the singleExecutable
	 */
	@Accessor(qualifier = "singleExecutable", type = Accessor.Type.SETTER)
	public void setSingleExecutable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SINGLEEXECUTABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Job.triggers</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the triggers - list of triggers
	 */
	@Accessor(qualifier = "triggers", type = Accessor.Type.SETTER)
	public void setTriggers(final List<TriggerModel> value)
	{
		getPersistenceContext().setPropertyValue(TRIGGERS, value);
	}
	
}
