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
package de.hybris.platform.ruleengineservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum FactContextType declared at extension ruleengineservices.
 */
@SuppressWarnings("PMD")
public enum FactContextType implements HybrisEnumValue
{
	/**
	 * Generated enum value for FactContextType.PROMOTION_ORDER declared at extension promotionengineservices.
	 */
	PROMOTION_ORDER("PROMOTION_ORDER"),
	/**
	 * Generated enum value for FactContextType.RULE_GROUP declared at extension ruleengineservices.
	 */
	RULE_GROUP("RULE_GROUP"),
	/**
	 * Generated enum value for FactContextType.PROMOTION_PRODUCT declared at extension promotionengineservices.
	 */
	PROMOTION_PRODUCT("PROMOTION_PRODUCT"),
	/**
	 * Generated enum value for FactContextType.RULE_CONFIGURATION declared at extension ruleengineservices.
	 */
	RULE_CONFIGURATION("RULE_CONFIGURATION");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "FactContextType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "FactContextType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private FactContextType(final String code)
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
