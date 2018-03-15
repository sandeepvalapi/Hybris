/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
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

public  class ProductSearchResultWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductSearchResultWsDTO.products</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private List<ProductWsDTO> products;

	/** <i>Generated property</i> for <code>ProductSearchResultWsDTO.pagination</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private PaginationWsDTO pagination;
	
	public ProductSearchResultWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setProducts(final List<ProductWsDTO> products)
	{
		this.products = products;
	}

		
	
	public List<ProductWsDTO> getProducts() 
	{
		return products;
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
