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
 * Generated enum SolrOptimizeMode declared at extension solrfacetsearch.
 */
@SuppressWarnings("PMD")
public enum SolrOptimizeMode implements HybrisEnumValue
{
	/**
	 * Generated enum value for SolrOptimizeMode.NEVER declared at extension solrfacetsearch.
	 */
	NEVER("NEVER"),
	/**
	 * Generated enum value for SolrOptimizeMode.AFTER_INDEX declared at extension solrfacetsearch.
	 */
	AFTER_INDEX("AFTER_INDEX"),
	/**
	 * Generated enum value for SolrOptimizeMode.AFTER_FULL_INDEX declared at extension solrfacetsearch.
	 */
	AFTER_FULL_INDEX("AFTER_FULL_INDEX");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrOptimizeMode";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SolrOptimizeMode";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SolrOptimizeMode(final String code)
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
