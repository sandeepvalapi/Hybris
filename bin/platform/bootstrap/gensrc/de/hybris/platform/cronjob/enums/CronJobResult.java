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
 * Generated enum CronJobResult declared at extension processing.
 * <p/>
 * The result reports on how the CronJob's last run was.
 */
@SuppressWarnings("PMD")
public enum CronJobResult implements HybrisEnumValue
{
	/**
	 * Generated enum value for CronJobResult.SUCCESS declared at extension processing.
	 * <p/>
	 * The CronJob was executed without running into any problems.
	 */
	SUCCESS("SUCCESS"),
	/**
	 * Generated enum value for CronJobResult.FAILURE declared at extension processing.
	 * <p/>
	 * The CronJob has completed its run on schedule, but it was not successful for some reason.
	 *                     Unlike the 'ERROR' result, this indicates a planned completion of the CronJob. Check the CronJob's
	 *                     log for details.
	 */
	FAILURE("FAILURE"),
	/**
	 * Generated enum value for CronJobResult.ERROR declared at extension processing.
	 * <p/>
	 * There was a problem during the CronJob's run (for example, the Java Virtual Machine was
	 *                     terminated or there was a system exception). Unlike the {literal}FAILURE{literal} status, this
	 *                     status indicates an uncontrolled termination of the CronJob. Check the CronJob's log for details.
	 */
	ERROR("ERROR"),
	/**
	 * Generated enum value for CronJobResult.UNKNOWN declared at extension processing.
	 * <p/>
	 * No result is available. Most probably the CronJob has never been started yet.
	 */
	UNKNOWN("UNKNOWN");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CronJobResult";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CronJobResult";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CronJobResult(final String code)
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
