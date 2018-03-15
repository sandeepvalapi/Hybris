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
package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum KeywordRedirectMatchType declared at extension solrfacetsearch.
 */
@SuppressWarnings("PMD")
public enum KeywordRedirectMatchType implements HybrisEnumValue
{
	/**
	 * Generated enum value for KeywordRedirectMatchType.EXACT declared at extension solrfacetsearch.
	 */
	EXACT("EXACT"),
	/**
	 * Generated enum value for KeywordRedirectMatchType.STARTS_WITH declared at extension solrfacetsearch.
	 */
	STARTS_WITH("STARTS_WITH"),
	/**
	 * Generated enum value for KeywordRedirectMatchType.ENDS_WITH declared at extension solrfacetsearch.
	 */
	ENDS_WITH("ENDS_WITH"),
	/**
	 * Generated enum value for KeywordRedirectMatchType.CONTAINS declared at extension solrfacetsearch.
	 */
	CONTAINS("CONTAINS"),
	/**
	 * Generated enum value for KeywordRedirectMatchType.REGEX declared at extension solrfacetsearch.
	 */
	REGEX("REGEX");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "KeywordRedirectMatchType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "KeywordRedirectMatchType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private KeywordRedirectMatchType(final String code)
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
