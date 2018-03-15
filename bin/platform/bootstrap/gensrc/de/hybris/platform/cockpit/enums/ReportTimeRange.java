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
 * Generated enum ReportTimeRange declared at extension cockpit.
 */
@SuppressWarnings("PMD")
public enum ReportTimeRange implements HybrisEnumValue
{
	/**
	 * Generated enum value for ReportTimeRange.LAST7DAYS declared at extension cockpit.
	 */
	LAST7DAYS("LAST7DAYS"),
	/**
	 * Generated enum value for ReportTimeRange.LAST4WEEKS declared at extension cockpit.
	 */
	LAST4WEEKS("LAST4WEEKS"),
	/**
	 * Generated enum value for ReportTimeRange.LASTMONTH declared at extension cockpit.
	 */
	LASTMONTH("LASTMONTH"),
	/**
	 * Generated enum value for ReportTimeRange.LAST52WEEKS declared at extension cockpit.
	 */
	LAST52WEEKS("LAST52WEEKS"),
	/**
	 * Generated enum value for ReportTimeRange.LAST12MONTHS declared at extension cockpit.
	 */
	LAST12MONTHS("LAST12MONTHS"),
	/**
	 * Generated enum value for ReportTimeRange.SPECIFICDAY declared at extension cockpit.
	 */
	SPECIFICDAY("SPECIFICDAY"),
	/**
	 * Generated enum value for ReportTimeRange.USERDEFINED declared at extension cockpit.
	 */
	USERDEFINED("USERDEFINED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ReportTimeRange";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ReportTimeRange";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ReportTimeRange(final String code)
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
