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
 * Generated enum SolrIndexedPropertyFacetType declared at extension solrfacetsearch.
 * <p/>
 * This enumeration defines the type of solr facets. The facet type determines behaviour of the facet filer.
 * 				Refine: the typical drill down facet, where only one facet value can be selected within a facet.
 * 				Multi-select allows multiple facet values to be selected within a facet.
 * 				MultiSelectAnd: causes the multiple facet values to be ANDed together, where the product results must match
 * 				all of the selected values.
 * 				MultiSelectOr: causes the multiple facet values to be ORed together, where the product results must mach
 * 				any of the selected values.
 */
@SuppressWarnings("PMD")
public enum SolrIndexedPropertyFacetType implements HybrisEnumValue
{
	/**
	 * Generated enum value for SolrIndexedPropertyFacetType.Refine declared at extension solrfacetsearch.
	 */
	REFINE("Refine"),
	/**
	 * Generated enum value for SolrIndexedPropertyFacetType.MultiSelectAnd declared at extension solrfacetsearch.
	 */
	MULTISELECTAND("MultiSelectAnd"),
	/**
	 * Generated enum value for SolrIndexedPropertyFacetType.MultiSelectOr declared at extension solrfacetsearch.
	 */
	MULTISELECTOR("MultiSelectOr");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrIndexedPropertyFacetType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SolrIndexedPropertyFacetType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SolrIndexedPropertyFacetType(final String code)
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
