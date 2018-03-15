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
 * Generated enum InStockStatus declared at extension basecommerce.
 * <p/>
 * Internal flag for stock status.
 */
@SuppressWarnings("PMD")
public enum InStockStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for InStockStatus.forceInStock declared at extension basecommerce.
	 */
	FORCEINSTOCK("forceInStock"),
	/**
	 * Generated enum value for InStockStatus.forceOutOfStock declared at extension basecommerce.
	 */
	FORCEOUTOFSTOCK("forceOutOfStock"),
	/**
	 * Generated enum value for InStockStatus.notSpecified declared at extension basecommerce.
	 */
	NOTSPECIFIED("notSpecified");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "InStockStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "InStockStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private InStockStatus(final String code)
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
