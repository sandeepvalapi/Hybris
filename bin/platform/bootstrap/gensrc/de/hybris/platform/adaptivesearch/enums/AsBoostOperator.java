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
package de.hybris.platform.adaptivesearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum AsBoostOperator declared at extension adaptivesearch.
 */
@SuppressWarnings("PMD")
public enum AsBoostOperator implements HybrisEnumValue
{
	/**
	 * Generated enum value for AsBoostOperator.EQUAL declared at extension adaptivesearch.
	 */
	EQUAL("EQUAL"),
	/**
	 * Generated enum value for AsBoostOperator.NOT_EQUAL declared at extension adaptivesearch.
	 */
	NOT_EQUAL("NOT_EQUAL"),
	/**
	 * Generated enum value for AsBoostOperator.GREATER_THAN declared at extension adaptivesearch.
	 */
	GREATER_THAN("GREATER_THAN"),
	/**
	 * Generated enum value for AsBoostOperator.GREATER_THAN_OR_EQUAL declared at extension adaptivesearch.
	 */
	GREATER_THAN_OR_EQUAL("GREATER_THAN_OR_EQUAL"),
	/**
	 * Generated enum value for AsBoostOperator.LESS_THAN declared at extension adaptivesearch.
	 */
	LESS_THAN("LESS_THAN"),
	/**
	 * Generated enum value for AsBoostOperator.LESS_THAN_OR_EQUAL declared at extension adaptivesearch.
	 */
	LESS_THAN_OR_EQUAL("LESS_THAN_OR_EQUAL"),
	/**
	 * Generated enum value for AsBoostOperator.MATCH declared at extension adaptivesearch.
	 */
	MATCH("MATCH");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AsBoostOperator";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "AsBoostOperator";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private AsBoostOperator(final String code)
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
