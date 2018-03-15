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
package de.hybris.platform.personalizationservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CxItemStatus declared at extension personalizationservices.
 */
@SuppressWarnings("PMD")
public enum CxItemStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for CxItemStatus.ENABLED declared at extension personalizationservices.
	 * <p/>
	 * Enabled state.
	 */
	ENABLED("ENABLED"),
	/**
	 * Generated enum value for CxItemStatus.DISABLED declared at extension personalizationservices.
	 * <p/>
	 * Disabled state.
	 */
	DISABLED("DISABLED"),
	/**
	 * Generated enum value for CxItemStatus.DELETED declared at extension personalizationservices.
	 * <p/>
	 * Soft deleted state.
	 */
	DELETED("DELETED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CxItemStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CxItemStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CxItemStatus(final String code)
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
