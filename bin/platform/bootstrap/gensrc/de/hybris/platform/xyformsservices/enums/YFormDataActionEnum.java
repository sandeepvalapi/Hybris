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
package de.hybris.platform.xyformsservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum YFormDataActionEnum declared at extension xyformsservices.
 */
@SuppressWarnings("PMD")
public enum YFormDataActionEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for YFormDataActionEnum.VIEW declared at extension xyformsservices.
	 */
	VIEW("VIEW"),
	/**
	 * Generated enum value for YFormDataActionEnum.EDIT declared at extension xyformsservices.
	 */
	EDIT("EDIT"),
	/**
	 * Generated enum value for YFormDataActionEnum.NEW declared at extension xyformsservices.
	 */
	NEW("NEW");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "YFormDataActionEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "YFormDataActionEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private YFormDataActionEnum(final String code)
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
