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
package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum FlashQuality declared at extension cms2lib.
 */
@SuppressWarnings("PMD")
public enum FlashQuality implements HybrisEnumValue
{
	/**
	 * Generated enum value for FlashQuality.low declared at extension cms2lib.
	 */
	LOW("low"),
	/**
	 * Generated enum value for FlashQuality.autolow declared at extension cms2lib.
	 */
	AUTOLOW("autolow"),
	/**
	 * Generated enum value for FlashQuality.autohigh declared at extension cms2lib.
	 */
	AUTOHIGH("autohigh"),
	/**
	 * Generated enum value for FlashQuality.medium declared at extension cms2lib.
	 */
	MEDIUM("medium"),
	/**
	 * Generated enum value for FlashQuality.high declared at extension cms2lib.
	 */
	HIGH("high"),
	/**
	 * Generated enum value for FlashQuality.best declared at extension cms2lib.
	 */
	BEST("best");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "FlashQuality";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "FlashQuality";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private FlashQuality(final String code)
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
