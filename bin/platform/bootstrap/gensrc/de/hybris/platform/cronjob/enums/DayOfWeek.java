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
 * Generated enum DayOfWeek declared at extension processing.
 */
@SuppressWarnings("PMD")
public enum DayOfWeek implements HybrisEnumValue
{
	/**
	 * Generated enum value for DayOfWeek.SUNDAY declared at extension processing.
	 */
	SUNDAY("SUNDAY"),
	/**
	 * Generated enum value for DayOfWeek.MONDAY declared at extension processing.
	 */
	MONDAY("MONDAY"),
	/**
	 * Generated enum value for DayOfWeek.TUESDAY declared at extension processing.
	 */
	TUESDAY("TUESDAY"),
	/**
	 * Generated enum value for DayOfWeek.WEDNESDAY declared at extension processing.
	 */
	WEDNESDAY("WEDNESDAY"),
	/**
	 * Generated enum value for DayOfWeek.THURSDAY declared at extension processing.
	 */
	THURSDAY("THURSDAY"),
	/**
	 * Generated enum value for DayOfWeek.FRIDAY declared at extension processing.
	 */
	FRIDAY("FRIDAY"),
	/**
	 * Generated enum value for DayOfWeek.SATURDAY declared at extension processing.
	 */
	SATURDAY("SATURDAY");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DayOfWeek";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "DayOfWeek";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private DayOfWeek(final String code)
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
