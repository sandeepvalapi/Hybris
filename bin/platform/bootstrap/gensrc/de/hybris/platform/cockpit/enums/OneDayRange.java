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
package de.hybris.platform.cockpit.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum OneDayRange declared at extension cockpit.
 */
@SuppressWarnings("PMD")
public enum OneDayRange implements HybrisEnumValue
{
	/**
	 * Generated enum value for OneDayRange.TODAY declared at extension cockpit.
	 */
	TODAY("TODAY"),
	/**
	 * Generated enum value for OneDayRange.YESTERDAY declared at extension cockpit.
	 */
	YESTERDAY("YESTERDAY"),
	/**
	 * Generated enum value for OneDayRange.SPECIFICDAY declared at extension cockpit.
	 */
	SPECIFICDAY("SPECIFICDAY");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "OneDayRange";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "OneDayRange";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private OneDayRange(final String code)
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
