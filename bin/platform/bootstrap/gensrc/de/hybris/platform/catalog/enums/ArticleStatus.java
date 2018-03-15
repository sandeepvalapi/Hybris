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
 * Generated enum ArticleStatus declared at extension catalog.
 */
@SuppressWarnings("PMD")
public enum ArticleStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for ArticleStatus.bargain declared at extension catalog.
	 */
	BARGAIN("bargain"),
	/**
	 * Generated enum value for ArticleStatus.new_article declared at extension catalog.
	 */
	NEW_ARTICLE("new_article"),
	/**
	 * Generated enum value for ArticleStatus.old_article declared at extension catalog.
	 */
	OLD_ARTICLE("old_article"),
	/**
	 * Generated enum value for ArticleStatus.new declared at extension catalog.
	 */
	NEW("new"),
	/**
	 * Generated enum value for ArticleStatus.used declared at extension catalog.
	 */
	USED("used"),
	/**
	 * Generated enum value for ArticleStatus.refurbished declared at extension catalog.
	 */
	REFURBISHED("refurbished"),
	/**
	 * Generated enum value for ArticleStatus.core_article declared at extension catalog.
	 */
	CORE_ARTICLE("core_article"),
	/**
	 * Generated enum value for ArticleStatus.others declared at extension catalog.
	 */
	OTHERS("others");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ArticleStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ArticleStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ArticleStatus(final String code)
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
