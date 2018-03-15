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
 * Generated enum RegexpFlag declared at extension validation.
 */
@SuppressWarnings("PMD")
public enum RegexpFlag implements HybrisEnumValue
{
	/**
	 * Generated enum value for RegexpFlag.UNIX_LINES declared at extension validation.
	 */
	UNIX_LINES("UNIX_LINES"),
	/**
	 * Generated enum value for RegexpFlag.CASE_INSENSITIVE declared at extension validation.
	 */
	CASE_INSENSITIVE("CASE_INSENSITIVE"),
	/**
	 * Generated enum value for RegexpFlag.COMMENTS declared at extension validation.
	 */
	COMMENTS("COMMENTS"),
	/**
	 * Generated enum value for RegexpFlag.MULTILINE declared at extension validation.
	 */
	MULTILINE("MULTILINE"),
	/**
	 * Generated enum value for RegexpFlag.DOTALL declared at extension validation.
	 */
	DOTALL("DOTALL"),
	/**
	 * Generated enum value for RegexpFlag.UNICODE_CASE declared at extension validation.
	 */
	UNICODE_CASE("UNICODE_CASE"),
	/**
	 * Generated enum value for RegexpFlag.CANON_EQ declared at extension validation.
	 */
	CANON_EQ("CANON_EQ");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RegexpFlag";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "RegexpFlag";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private RegexpFlag(final String code)
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
