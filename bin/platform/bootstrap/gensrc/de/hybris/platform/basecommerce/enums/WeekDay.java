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
package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum WeekDay declared at extension basecommerce.
 */
@SuppressWarnings("PMD")
public enum WeekDay implements HybrisEnumValue
{
	/**
	 * Generated enum value for WeekDay.SUNDAY declared at extension basecommerce.
	 */
	SUNDAY("SUNDAY"),
	/**
	 * Generated enum value for WeekDay.MONDAY declared at extension basecommerce.
	 */
	MONDAY("MONDAY"),
	/**
	 * Generated enum value for WeekDay.TUESDAY declared at extension basecommerce.
	 */
	TUESDAY("TUESDAY"),
	/**
	 * Generated enum value for WeekDay.WEDNESDAY declared at extension basecommerce.
	 */
	WEDNESDAY("WEDNESDAY"),
	/**
	 * Generated enum value for WeekDay.THURSDAY declared at extension basecommerce.
	 */
	THURSDAY("THURSDAY"),
	/**
	 * Generated enum value for WeekDay.FRIDAY declared at extension basecommerce.
	 */
	FRIDAY("FRIDAY"),
	/**
	 * Generated enum value for WeekDay.SATURDAY declared at extension basecommerce.
	 */
	SATURDAY("SATURDAY");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WeekDay";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "WeekDay";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private WeekDay(final String code)
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
