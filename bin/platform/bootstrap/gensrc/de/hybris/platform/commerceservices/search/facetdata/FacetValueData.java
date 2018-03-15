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

import java.io.Serializable;

/**
 * POJO representing a facet value.
 */
public  class FacetValueData<STATE>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.code</code> property defined at extension <code>commerceservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.name</code> property defined at extension <code>commerceservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.count</code> property defined at extension <code>commerceservices</code>. */
		
	private long count;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.query</code> property defined at extension <code>commerceservices</code>. */
		
	private STATE query;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.selected</code> property defined at extension <code>commerceservices</code>. */
		
	private boolean selected;
	
	public FacetValueData()
	{
		// default constructor
	}
	
		
	
	public void setCode(final String code)
	{
		this.code = code;
	}

		
	
	public String getCode() 
	{
		return code;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setCount(final long count)
	{
		this.count = count;
	}

		
	
	public long getCount() 
	{
		return count;
	}
	
		
	
	public void setQuery(final STATE query)
	{
		this.query = query;
	}

		
	
	public STATE getQuery() 
	{
		return query;
	}
	
		
	
	public void setSelected(final boolean selected)
	{
		this.selected = selected;
	}

		
	
	public boolean isSelected() 
	{
		return selected;
	}
	


}
