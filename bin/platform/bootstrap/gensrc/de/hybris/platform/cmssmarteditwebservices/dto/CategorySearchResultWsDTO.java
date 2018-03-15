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
package de.hybris.platform.cmssmarteditwebservices.dto;

import java.io.Serializable;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import java.util.List;

/**
 * DTO which serves as a wrapper object that contains a list of CategoryData
 */
public  class CategorySearchResultWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CategorySearchResultWsDTO.productCategories</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private List<CategoryWsDTO> productCategories;

	/** <i>Generated property</i> for <code>CategorySearchResultWsDTO.pagination</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private PaginationWsDTO pagination;
	
	public CategorySearchResultWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setProductCategories(final List<CategoryWsDTO> productCategories)
	{
		this.productCategories = productCategories;
	}

		
	
	public List<CategoryWsDTO> getProductCategories() 
	{
		return productCategories;
	}
	
		
	
	public void setPagination(final PaginationWsDTO pagination)
	{
		this.pagination = pagination;
	}

		
	
	public PaginationWsDTO getPagination() 
	{
		return pagination;
	}
	


}
