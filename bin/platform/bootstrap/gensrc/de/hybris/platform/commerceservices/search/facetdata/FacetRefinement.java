/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:41 PM
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

import java.io.Serializable;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import java.util.List;

/**
 * POJO representing a facet refinement.
 */
public  class FacetRefinement<STATE>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>FacetRefinement<STATE>.facets</code> property defined at extension <code>commerceservices</code>. */
		
	private List<FacetData<STATE>> facets;

	/** <i>Generated property</i> for <code>FacetRefinement<STATE>.breadcrumbs</code> property defined at extension <code>commerceservices</code>. */
		
	private List<BreadcrumbData<STATE>> breadcrumbs;

	/** <i>Generated property</i> for <code>FacetRefinement<STATE>.count</code> property defined at extension <code>commerceservices</code>. */
		
	private long count;
	
	public FacetRefinement()
	{
		// default constructor
	}
	
		
	
	public void setFacets(final List<FacetData<STATE>> facets)
	{
		this.facets = facets;
	}

		
	
	public List<FacetData<STATE>> getFacets() 
	{
		return facets;
	}
	
		
	
	public void setBreadcrumbs(final List<BreadcrumbData<STATE>> breadcrumbs)
	{
		this.breadcrumbs = breadcrumbs;
	}

		
	
	public List<BreadcrumbData<STATE>> getBreadcrumbs() 
	{
		return breadcrumbs;
	}
	
		
	
	public void setCount(final long count)
	{
		this.count = count;
	}

		
	
	public long getCount() 
	{
		return count;
	}
	


}
