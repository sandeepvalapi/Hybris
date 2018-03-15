/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
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
package de.hybris.platform.commercewebservicescommons.dto.search.pagedata;

import java.io.Serializable;

/**
 * POJO representation of search query pagination.
 *
 * @deprecated Since version 6.5 use de.hybris.platform.webservicescommons.dto.PaginationWsDTO instead
 */
@Deprecated
public  class PageableWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** The number of results per page. A page may have less results if there are less than a full page
				of results, only on the last page in the results.
			<br/><br/><i>Generated property</i> for <code>PageableWsDTO.pageSize</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer pageSize;

	/** The current page number. The first page is number zero (0), the second page is number one (1),
				and so on.
			<br/><br/><i>Generated property</i> for <code>PageableWsDTO.currentPage</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer currentPage;

	/** The selected sort code.<br/><br/><i>Generated property</i> for <code>PageableWsDTO.sort</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String sort;
	
	public PageableWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setPageSize(final Integer pageSize)
	{
		this.pageSize = pageSize;
	}

		
	
	public Integer getPageSize() 
	{
		return pageSize;
	}
	
		
	
	public void setCurrentPage(final Integer currentPage)
	{
		this.currentPage = currentPage;
	}

		
	
	public Integer getCurrentPage() 
	{
		return currentPage;
	}
	
		
	
	public void setSort(final String sort)
	{
		this.sort = sort;
	}

		
	
	public String getSort() 
	{
		return sort;
	}
	


}
