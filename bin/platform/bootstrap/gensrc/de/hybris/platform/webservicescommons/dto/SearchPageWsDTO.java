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
package de.hybris.platform.webservicescommons.dto;

import java.io.Serializable;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * Includes all necessary data for creating proper result in refine search
 */
@ApiModel(value="searchPage", description="Includes all necessary data for creating proper result in refine search")
public  class SearchPageWsDTO<RESULT>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** Result list<br/><br/><i>Generated property</i> for <code>SearchPageWsDTO<RESULT>.results</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="results", value="Result list") 	
	private List<RESULT> results;

	/** <i>Generated property</i> for <code>SearchPageWsDTO<RESULT>.sorts</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="sorts") 	
	private List<SortWsDTO> sorts;

	/** Pagination info<br/><br/><i>Generated property</i> for <code>SearchPageWsDTO<RESULT>.pagination</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="pagination", value="Pagination info") 	
	private PaginationWsDTO pagination;
	
	public SearchPageWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setResults(final List<RESULT> results)
	{
		this.results = results;
	}

		
	
	public List<RESULT> getResults() 
	{
		return results;
	}
	
		
	
	public void setSorts(final List<SortWsDTO> sorts)
	{
		this.sorts = sorts;
	}

		
	
	public List<SortWsDTO> getSorts() 
	{
		return sorts;
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
