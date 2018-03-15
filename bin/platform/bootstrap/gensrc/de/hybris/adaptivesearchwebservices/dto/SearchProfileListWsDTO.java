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
package de.hybris.adaptivesearchwebservices.dto;

import java.io.Serializable;
import de.hybris.adaptivesearchfacades.data.AsSearchProfileData;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * List of search profiles
 */
@ApiModel(value="searchProfileList", description="List of search profiles")
public  class SearchProfileListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SearchProfileListWsDTO.pagination</code> property defined at extension <code>adaptivesearchwebservices</code>. */
	@ApiModelProperty(name="pagination") 	
	private PaginationWsDTO pagination;

	/** <i>Generated property</i> for <code>SearchProfileListWsDTO.searchProfiles</code> property defined at extension <code>adaptivesearchwebservices</code>. */
	@ApiModelProperty(name="searchProfiles") 	
	private List<AsSearchProfileData> searchProfiles;
	
	public SearchProfileListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setPagination(final PaginationWsDTO pagination)
	{
		this.pagination = pagination;
	}

		
	
	public PaginationWsDTO getPagination() 
	{
		return pagination;
	}
	
		
	
	public void setSearchProfiles(final List<AsSearchProfileData> searchProfiles)
	{
		this.searchProfiles = searchProfiles;
	}

		
	
	public List<AsSearchProfileData> getSearchProfiles() 
	{
		return searchProfiles;
	}
	


}
