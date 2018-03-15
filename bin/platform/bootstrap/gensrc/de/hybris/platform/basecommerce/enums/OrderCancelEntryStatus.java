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
 * Generated enum OrderCancelEntryStatus declared at extension basecommerce.
 */
@SuppressWarnings("PMD")
public enum OrderCancelEntryStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for OrderCancelEntryStatus.FULL declared at extension basecommerce.
	 */
	FULL("FULL"),
	/**
	 * Generated enum value for OrderCancelEntryStatus.PARTIAL declared at extension basecommerce.
	 */
	PARTIAL("PARTIAL"),
	/**
	 * Generated enum value for OrderCancelEntryStatus.DENIED declared at extension basecommerce.
	 */
	DENIED("DENIED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "OrderCancelEntryStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "OrderCancelEntryStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private OrderCancelEntryStatus(final String code)
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
