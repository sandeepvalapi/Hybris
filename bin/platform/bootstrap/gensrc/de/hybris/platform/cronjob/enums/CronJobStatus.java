/*
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
package de.hybris.platform.cronjob.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CronJobStatus declared at extension processing.
 * <p/>
 * The status indicates the condition which the CronJob is in.
 */
@SuppressWarnings("PMD")
public enum CronJobStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for CronJobStatus.RUNNINGRESTART declared at extension processing.
	 * <p/>
	 * The CronJob's execution has previously been canceled, so it has been restarted and is
	 *                     currently still running.
	 */
	RUNNINGRESTART("RUNNINGRESTART"),
	/**
	 * Generated enum value for CronJobStatus.RUNNING declared at extension processing.
	 * <p/>
	 * The CronJob is being executed right now.
	 */
	RUNNING("RUNNING"),
	/**
	 * Generated enum value for CronJobStatus.PAUSED declared at extension processing.
	 * <p/>
	 * The CronJob's execution has been interrupted in its execution, either automatically by a
	 *                     piece of business logic or manually by user intervention. You will need to resume the CronJob's
	 *                     execution if you want it to finish its run.
	 */
	PAUSED("PAUSED"),
	/**
	 * Generated enum value for CronJobStatus.FINISHED declared at extension processing.
	 * <p/>
	 * The CronJob has completed its run.
	 */
	FINISHED("FINISHED"),
	/**
	 * Generated enum value for CronJobStatus.ABORTED declared at extension processing.
	 * <p/>
	 * The CronJob's execution has been cancelled - either due to an error or due to user
	 *                     intervention. Check the CronJob's log for details.
	 */
	ABORTED("ABORTED"),
	/**
	 * Generated enum value for CronJobStatus.UNKNOWN declared at extension processing.
	 * <p/>
	 * Most probably, a CronJob in this state has never been run before.
	 */
	UNKNOWN("UNKNOWN");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CronJobStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CronJobStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CronJobStatus(final String code)
	{
		this.code = code.intern();
	}
	
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
}
