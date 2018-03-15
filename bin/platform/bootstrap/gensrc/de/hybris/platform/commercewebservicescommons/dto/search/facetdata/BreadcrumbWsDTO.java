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
package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.search.SearchStateWsDTO;

/**
 * POJO representing a section of the Breadcrumb.
 */
public  class BreadcrumbWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>BreadcrumbWsDTO.facetCode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String facetCode;

	/** <i>Generated property</i> for <code>BreadcrumbWsDTO.facetName</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String facetName;

	/** <i>Generated property</i> for <code>BreadcrumbWsDTO.facetValueCode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String facetValueCode;

	/** <i>Generated property</i> for <code>BreadcrumbWsDTO.facetValueName</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String facetValueName;

	/** <i>Generated property</i> for <code>BreadcrumbWsDTO.removeQuery</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private SearchStateWsDTO removeQuery;

	/** <i>Generated property</i> for <code>BreadcrumbWsDTO.truncateQuery</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private SearchStateWsDTO truncateQuery;
	
	public BreadcrumbWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setFacetCode(final String facetCode)
	{
		this.facetCode = facetCode;
	}

		
	
	public String getFacetCode() 
	{
		return facetCode;
	}
	
		
	
	public void setFacetName(final String facetName)
	{
		this.facetName = facetName;
	}

		
	
	public String getFacetName() 
	{
		return facetName;
	}
	
		
	
	public void setFacetValueCode(final String facetValueCode)
	{
		this.facetValueCode = facetValueCode;
	}

		
	
	public String getFacetValueCode() 
	{
		return facetValueCode;
	}
	
		
	
	public void setFacetValueName(final String facetValueName)
	{
		this.facetValueName = facetValueName;
	}

		
	
	public String getFacetValueName() 
	{
		return facetValueName;
	}
	
		
	
	public void setRemoveQuery(final SearchStateWsDTO removeQuery)
	{
		this.removeQuery = removeQuery;
	}

		
	
	public SearchStateWsDTO getRemoveQuery() 
	{
		return removeQuery;
	}
	
		
	
	public void setTruncateQuery(final SearchStateWsDTO truncateQuery)
	{
		this.truncateQuery = truncateQuery;
	}

		
	
	public SearchStateWsDTO getTruncateQuery() 
	{
		return truncateQuery;
	}
	


}
