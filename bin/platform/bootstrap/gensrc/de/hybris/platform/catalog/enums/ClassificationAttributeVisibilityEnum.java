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
 * Generated enum ClassificationAttributeVisibilityEnum declared at extension catalog.
 */
@SuppressWarnings("PMD")
public enum ClassificationAttributeVisibilityEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for ClassificationAttributeVisibilityEnum.visible declared at extension catalog.
	 */
	VISIBLE("visible"),
	/**
	 * Generated enum value for ClassificationAttributeVisibilityEnum.not_visible declared at extension catalog.
	 */
	NOT_VISIBLE("not_visible"),
	/**
	 * Generated enum value for ClassificationAttributeVisibilityEnum.visible_in_base declared at extension catalog.
	 */
	VISIBLE_IN_BASE("visible_in_base"),
	/**
	 * Generated enum value for ClassificationAttributeVisibilityEnum.visible_in_variant declared at extension catalog.
	 */
	VISIBLE_IN_VARIANT("visible_in_variant");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ClassificationAttributeVisibilityEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ClassificationAttributeVisibilityEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ClassificationAttributeVisibilityEnum(final String code)
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
