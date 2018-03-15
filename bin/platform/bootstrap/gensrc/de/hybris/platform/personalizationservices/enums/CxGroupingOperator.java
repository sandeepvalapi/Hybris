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
 * Generated enum CxGroupingOperator declared at extension personalizationservices.
 */
@SuppressWarnings("PMD")
public enum CxGroupingOperator implements HybrisEnumValue
{
	/**
	 * Generated enum value for CxGroupingOperator.AND declared at extension personalizationservices.
	 * <p/>
	 * Group by AND.
	 */
	AND("AND"),
	/**
	 * Generated enum value for CxGroupingOperator.OR declared at extension personalizationservices.
	 * <p/>
	 * Group by OR.
	 */
	OR("OR");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CxGroupingOperator";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CxGroupingOperator";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CxGroupingOperator(final String code)
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
