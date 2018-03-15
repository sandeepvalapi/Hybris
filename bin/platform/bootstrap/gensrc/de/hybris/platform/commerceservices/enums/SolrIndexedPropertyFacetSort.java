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
package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SolrIndexedPropertyFacetSort declared at extension commerceservices.
 * <p/>
 * This enumeration defines sorting order of the facet values within a facet.
 * 				Count: The facet values can be sorted by facet value count, with the highest count first.
 * 				Alpha: Sorted lexically by facet display name (A to Z)
 * 				Custom: A custom sort must be provided via the SolrIndexedProperty.customFacetSortProvider.
 */
@SuppressWarnings("PMD")
public enum SolrIndexedPropertyFacetSort implements HybrisEnumValue
{
	/**
	 * Generated enum value for SolrIndexedPropertyFacetSort.Count declared at extension commerceservices.
	 */
	COUNT("Count"),
	/**
	 * Generated enum value for SolrIndexedPropertyFacetSort.Alpha declared at extension commerceservices.
	 */
	ALPHA("Alpha"),
	/**
	 * Generated enum value for SolrIndexedPropertyFacetSort.Custom declared at extension commerceservices.
	 */
	CUSTOM("Custom");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrIndexedPropertyFacetSort";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SolrIndexedPropertyFacetSort";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SolrIndexedPropertyFacetSort(final String code)
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
