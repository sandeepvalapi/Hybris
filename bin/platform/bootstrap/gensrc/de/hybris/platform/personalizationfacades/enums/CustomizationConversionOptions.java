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
package de.hybris.platform.personalizationfacades.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CustomizationConversionOptions declared at extension personalizationfacades.
 */
@SuppressWarnings("PMD")
public enum CustomizationConversionOptions implements HybrisEnumValue
{
	/**
	 * Generated enum value for CustomizationConversionOptions.BASE declared at extension personalizationfacades.
	 */
	BASE("BASE"),
	/**
	 * Generated enum value for CustomizationConversionOptions.FOR_VARIATION declared at extension personalizationfacades.
	 */
	FOR_VARIATION("FOR_VARIATION"),
	/**
	 * Generated enum value for CustomizationConversionOptions.FULL declared at extension personalizationfacades.
	 */
	FULL("FULL");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CustomizationConversionOptions";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CustomizationConversionOptions";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CustomizationConversionOptions(final String code)
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
