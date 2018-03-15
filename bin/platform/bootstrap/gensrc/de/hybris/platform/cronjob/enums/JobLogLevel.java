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
 * Generated enum JobLogLevel declared at extension processing.
 */
@SuppressWarnings("PMD")
public enum JobLogLevel implements HybrisEnumValue
{
	/**
	 * Generated enum value for JobLogLevel.DEBUG declared at extension processing.
	 */
	DEBUG("DEBUG"),
	/**
	 * Generated enum value for JobLogLevel.INFO declared at extension processing.
	 */
	INFO("INFO"),
	/**
	 * Generated enum value for JobLogLevel.WARNING declared at extension processing.
	 */
	WARNING("WARNING"),
	/**
	 * Generated enum value for JobLogLevel.ERROR declared at extension processing.
	 */
	ERROR("ERROR"),
	/**
	 * Generated enum value for JobLogLevel.FATAL declared at extension processing.
	 */
	FATAL("FATAL"),
	/**
	 * Generated enum value for JobLogLevel.UNKNOWN declared at extension processing.
	 */
	UNKNOWN("UNKNOWN");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "JobLogLevel";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "JobLogLevel";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private JobLogLevel(final String code)
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
