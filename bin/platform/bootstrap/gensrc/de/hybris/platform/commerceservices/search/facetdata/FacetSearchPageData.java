/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:39 PM
 * ----------------------------------------------------------------
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
 */
package de.hybris.platform.commerceservices.search.facetdata;

import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import java.util.List;

/**
 * POJO that includes all necessary data for creating proper result in facet search.
 */
public  class FacetSearchPageData<STATE, RESULT> extends SearchPageData<RESULT> 
{

 

	/** <i>Generated property</i> for <code>FacetSearchPageData<STATE, RESULT>.currentQuery</code> property defined at extension <code>commerceservices</code>. */
		
	private STATE currentQuery;

	/** <i>Generated property</i> for <code>FacetSearchPageData<STATE, RESULT>.breadcrumbs</code> property defined at extension <code>commerceservices</code>. */
		
	private List<BreadcrumbData<STATE>> breadcrumbs;

	/** <i>Generated property</i> for <code>FacetSearchPageData<STATE, RESULT>.facets</code> property defined at extension <code>commerceservices</code>. */
		
	private List<FacetData<STATE>> facets;
	
	public FacetSearchPageData()
	{
		// default constructor
	}
	
		
	
	public void setCurrentQuery(final STATE currentQuery)
	{
		this.currentQuery = currentQuery;
	}

		
	
	public STATE getCurrentQuery() 
	{
		return currentQuery;
	}
	
		
	
	public void setBreadcrumbs(final List<BreadcrumbData<STATE>> breadcrumbs)
	{
		this.breadcrumbs = breadcrumbs;
	}

		
	
	public List<BreadcrumbData<STATE>> getBreadcrumbs() 
	{
		return breadcrumbs;
	}
	
		
	
	public void setFacets(final List<FacetData<STATE>> facets)
	{
		this.facets = facets;
	}

		
	
	public List<FacetData<STATE>> getFacets() 
	{
		return facets;
	}
	


}
