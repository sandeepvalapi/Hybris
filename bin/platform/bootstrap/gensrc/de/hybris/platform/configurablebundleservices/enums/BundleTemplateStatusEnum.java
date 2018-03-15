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
package de.hybris.platform.configurablebundleservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum BundleTemplateStatusEnum declared at extension configurablebundleservices.
 */
@SuppressWarnings("PMD")
public enum BundleTemplateStatusEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for BundleTemplateStatusEnum.check declared at extension configurablebundleservices.
	 */
	CHECK("check"),
	/**
	 * Generated enum value for BundleTemplateStatusEnum.unapproved declared at extension configurablebundleservices.
	 */
	UNAPPROVED("unapproved"),
	/**
	 * Generated enum value for BundleTemplateStatusEnum.approved declared at extension configurablebundleservices.
	 */
	APPROVED("approved"),
	/**
	 * Generated enum value for BundleTemplateStatusEnum.archived declared at extension configurablebundleservices.
	 */
	ARCHIVED("archived");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "BundleTemplateStatusEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "BundleTemplateStatusEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private BundleTemplateStatusEnum(final String code)
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
