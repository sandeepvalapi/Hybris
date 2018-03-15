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
package de.hybris.platform.commercewebservicescommons.dto.user;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.user.UserGroupWsDTO;
import java.util.List;

public  class UserGroupListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UserGroupListWsDTO.userGroups</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<UserGroupWsDTO> userGroups;

	/** <i>Generated property</i> for <code>UserGroupListWsDTO.totalNumber</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer totalNumber;

	/** <i>Generated property</i> for <code>UserGroupListWsDTO.pageSize</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer pageSize;

	/** <i>Generated property</i> for <code>UserGroupListWsDTO.numberOfPages</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer numberOfPages;

	/** <i>Generated property</i> for <code>UserGroupListWsDTO.currentPage</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer currentPage;
	
	public UserGroupListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setUserGroups(final List<UserGroupWsDTO> userGroups)
	{
		this.userGroups = userGroups;
	}

		
	
	public List<UserGroupWsDTO> getUserGroups() 
	{
		return userGroups;
	}
	
		
	
	public void setTotalNumber(final Integer totalNumber)
	{
		this.totalNumber = totalNumber;
	}

		
	
	public Integer getTotalNumber() 
	{
		return totalNumber;
	}
	
		
	
	public void setPageSize(final Integer pageSize)
	{
		this.pageSize = pageSize;
	}

		
	
	public Integer getPageSize() 
	{
		return pageSize;
	}
	
		
	
	public void setNumberOfPages(final Integer numberOfPages)
	{
		this.numberOfPages = numberOfPages;
	}

		
	
	public Integer getNumberOfPages() 
	{
		return numberOfPages;
	}
	
		
	
	public void setCurrentPage(final Integer currentPage)
	{
		this.currentPage = currentPage;
	}

		
	
	public Integer getCurrentPage() 
	{
		return currentPage;
	}
	


}
