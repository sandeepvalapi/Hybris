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
 * Generated enum VariationConversionOptions declared at extension personalizationfacades.
 */
@SuppressWarnings("PMD")
public enum VariationConversionOptions implements HybrisEnumValue
{
	/**
	 * Generated enum value for VariationConversionOptions.BASE declared at extension personalizationfacades.
	 */
	BASE("BASE"),
	/**
	 * Generated enum value for VariationConversionOptions.FOR_ACTION declared at extension personalizationfacades.
	 */
	FOR_ACTION("FOR_ACTION"),
	/**
	 * Generated enum value for VariationConversionOptions.FOR_CUSTOMIZATION declared at extension personalizationfacades.
	 */
	FOR_CUSTOMIZATION("FOR_CUSTOMIZATION"),
	/**
	 * Generated enum value for VariationConversionOptions.FOR_TRIGGER declared at extension personalizationfacades.
	 */
	FOR_TRIGGER("FOR_TRIGGER"),
	/**
	 * Generated enum value for VariationConversionOptions.FULL declared at extension personalizationfacades.
	 */
	FULL("FULL");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "VariationConversionOptions";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "VariationConversionOptions";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private VariationConversionOptions(final String code)
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
