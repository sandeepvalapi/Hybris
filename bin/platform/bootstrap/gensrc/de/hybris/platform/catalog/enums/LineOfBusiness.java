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
package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum LineOfBusiness declared at extension catalog.
 */
@SuppressWarnings("PMD")
public enum LineOfBusiness implements HybrisEnumValue
{
	/**
	 * Generated enum value for LineOfBusiness.trade declared at extension catalog.
	 */
	TRADE("trade"),
	/**
	 * Generated enum value for LineOfBusiness.bank declared at extension catalog.
	 */
	BANK("bank"),
	/**
	 * Generated enum value for LineOfBusiness.industry declared at extension catalog.
	 */
	INDUSTRY("industry"),
	/**
	 * Generated enum value for LineOfBusiness.building declared at extension catalog.
	 */
	BUILDING("building"),
	/**
	 * Generated enum value for LineOfBusiness.government declared at extension catalog.
	 */
	GOVERNMENT("government"),
	/**
	 * Generated enum value for LineOfBusiness.service declared at extension catalog.
	 */
	SERVICE("service");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "LineOfBusiness";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "LineOfBusiness";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private LineOfBusiness(final String code)
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
