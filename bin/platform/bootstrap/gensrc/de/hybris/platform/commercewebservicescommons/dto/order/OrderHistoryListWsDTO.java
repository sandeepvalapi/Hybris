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
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import java.util.List;

public  class OrderHistoryListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderHistoryListWsDTO.orders</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<OrderHistoryWsDTO> orders;

	/** <i>Generated property</i> for <code>OrderHistoryListWsDTO.sorts</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<SortWsDTO> sorts;

	/** <i>Generated property</i> for <code>OrderHistoryListWsDTO.pagination</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PaginationWsDTO pagination;
	
	public OrderHistoryListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setOrders(final List<OrderHistoryWsDTO> orders)
	{
		this.orders = orders;
	}

		
	
	public List<OrderHistoryWsDTO> getOrders() 
	{
		return orders;
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
