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
package de.hybris.platform.validation.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum ValidatorLanguage declared at extension validation.
 * <p/>
 * Language of dynamic validation.
 */
@SuppressWarnings("PMD")
public enum ValidatorLanguage implements HybrisEnumValue
{
	/**
	 * Generated enum value for ValidatorLanguage.BEANSHELL declared at extension validation.
	 */
	BEANSHELL("BEANSHELL");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ValidatorLanguage";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ValidatorLanguage";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ValidatorLanguage(final String code)
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
