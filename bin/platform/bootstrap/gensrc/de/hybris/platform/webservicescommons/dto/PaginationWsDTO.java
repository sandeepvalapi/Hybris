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
package de.hybris.platform.webservicescommons.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Pagination info
 */
@ApiModel(value="pagination", description="Pagination info")
public  class PaginationWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** Number of elements on this page<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.count</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="count", value="Number of elements on this page") 	
	private Integer count;

	/** Total number of elements<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.totalCount</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="totalCount", value="Total number of elements") 	
	private Long totalCount;

	/** Current page number<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.page</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="page", value="Current page number") 	
	private Integer page;

	/** Total number of pages<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.totalPages</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="totalPages", value="Total number of pages") 	
	private Integer totalPages;
	
	public PaginationWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setCount(final Integer count)
	{
		this.count = count;
	}

		
	
	public Integer getCount() 
	{
		return count;
	}
	
		
	
	public void setTotalCount(final Long totalCount)
	{
		this.totalCount = totalCount;
	}

		
	
	public Long getTotalCount() 
	{
		return totalCount;
	}
	
		
	
	public void setPage(final Integer page)
	{
		this.page = page;
	}

		
	
	public Integer getPage() 
	{
		return page;
	}
	
		
	
	public void setTotalPages(final Integer totalPages)
	{
		this.totalPages = totalPages;
	}

		
	
	public Integer getTotalPages() 
	{
		return totalPages;
	}
	


}
