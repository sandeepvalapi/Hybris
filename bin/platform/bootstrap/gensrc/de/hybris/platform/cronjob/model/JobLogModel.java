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
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type JobLog first defined at extension processing.
 */
@SuppressWarnings("all")
public class JobLogModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "JobLog";
	
	/**<i>Generated relation code constant for relation <code>CronJobJobLogsRelation</code> defining source attribute <code>cronJob</code> in extension <code>processing</code>.</i>*/
	public static final String _CRONJOBJOBLOGSRELATION = "CronJobJobLogsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.step</code> attribute defined at extension <code>processing</code>. */
	public static final String STEP = "step";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.message</code> attribute defined at extension <code>processing</code>. */
	public static final String MESSAGE = "message";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.shortMessage</code> attribute defined at extension <code>processing</code>. */
	public static final String SHORTMESSAGE = "shortMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.level</code> attribute defined at extension <code>processing</code>. */
	public static final String LEVEL = "level";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobLog.cronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOB = "cronJob";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JobLogModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JobLogModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 * @param _level initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 */
	@Deprecated
	public JobLogModel(final CronJobModel _cronJob, final JobLogLevel _level)
	{
		super();
		setCronJob(_cronJob);
		setLevel(_level);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 * @param _level initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 * @param _message initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _step initial attribute declared by type <code>JobLog</code> at extension <code>processing</code>
	 */
	@Deprecated
	public JobLogModel(final CronJobModel _cronJob, final JobLogLevel _level, final String _message, final ItemModel _owner, final StepModel _step)
	{
		super();
		setCronJob(_cronJob);
		setLevel(_level);
		setMessage(_message);
		setOwner(_owner);
		setStep(_step);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.cronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the cronJob - assigned CronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
	public CronJobModel getCronJob()
	{
		return getPersistenceContext().getPropertyValue(CRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.level</code> attribute defined at extension <code>processing</code>. 
	 * @return the level
	 */
	@Accessor(qualifier = "level", type = Accessor.Type.GETTER)
	public JobLogLevel getLevel()
	{
		return getPersistenceContext().getPropertyValue(LEVEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.message</code> attribute defined at extension <code>processing</code>. 
	 * @return the message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.GETTER)
	public String getMessage()
	{
		return getPersistenceContext().getPropertyValue(MESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.shortMessage</code> attribute defined at extension <code>processing</code>. 
	 * @return the shortMessage
	 */
	@Accessor(qualifier = "shortMessage", type = Accessor.Type.GETTER)
	public String getShortMessage()
	{
		return getPersistenceContext().getPropertyValue(SHORTMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobLog.step</code> attribute defined at extension <code>processing</code>. 
	 * @return the step
	 */
	@Accessor(qualifier = "step", type = Accessor.Type.GETTER)
	public StepModel getStep()
	{
		return getPersistenceContext().getPropertyValue(STEP);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>JobLog.cronJob</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronJob - assigned CronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
	public void setCronJob(final CronJobModel value)
	{
		getPersistenceContext().setPropertyValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>JobLog.level</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the level
	 */
	@Accessor(qualifier = "level", type = Accessor.Type.SETTER)
	public void setLevel(final JobLogLevel value)
	{
		getPersistenceContext().setPropertyValue(LEVEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>JobLog.message</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.SETTER)
	public void setMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(MESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>JobLog.step</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the step
	 */
	@Accessor(qualifier = "step", type = Accessor.Type.SETTER)
	public void setStep(final StepModel value)
	{
		getPersistenceContext().setPropertyValue(STEP, value);
	}
	
}
