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
 * Generated enum ClassificationAttributeTypeEnum declared at extension catalog.
 */
@SuppressWarnings("PMD")
public enum ClassificationAttributeTypeEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for ClassificationAttributeTypeEnum.string declared at extension catalog.
	 */
	STRING("string"),
	/**
	 * Generated enum value for ClassificationAttributeTypeEnum.number declared at extension catalog.
	 */
	NUMBER("number"),
	/**
	 * Generated enum value for ClassificationAttributeTypeEnum.boolean declared at extension catalog.
	 */
	BOOLEAN("boolean"),
	/**
	 * Generated enum value for ClassificationAttributeTypeEnum.enum declared at extension catalog.
	 */
	ENUM("enum"),
	/**
	 * Generated enum value for ClassificationAttributeTypeEnum.date declared at extension catalog.
	 */
	DATE("date");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ClassificationAttributeTypeEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ClassificationAttributeTypeEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ClassificationAttributeTypeEnum(final String code)
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
