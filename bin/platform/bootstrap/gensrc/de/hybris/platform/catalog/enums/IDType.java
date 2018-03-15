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
 * Generated enum IDType declared at extension catalog.
 */
@SuppressWarnings("PMD")
public enum IDType implements HybrisEnumValue
{
	/**
	 * Generated enum value for IDType.duns declared at extension catalog.
	 */
	DUNS("duns"),
	/**
	 * Generated enum value for IDType.iln declared at extension catalog.
	 */
	ILN("iln"),
	/**
	 * Generated enum value for IDType.supplier_specific declared at extension catalog.
	 */
	SUPPLIER_SPECIFIC("supplier_specific"),
	/**
	 * Generated enum value for IDType.buyer_specific declared at extension catalog.
	 */
	BUYER_SPECIFIC("buyer_specific"),
	/**
	 * Generated enum value for IDType.unspecified declared at extension catalog.
	 */
	UNSPECIFIED("unspecified");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "IDType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "IDType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private IDType(final String code)
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
