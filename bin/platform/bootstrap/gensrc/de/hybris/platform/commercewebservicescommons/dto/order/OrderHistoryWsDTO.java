/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
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
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import java.util.Date;

public  class OrderHistoryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderHistoryWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>OrderHistoryWsDTO.status</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>OrderHistoryWsDTO.statusDisplay</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String statusDisplay;

	/** <i>Generated property</i> for <code>OrderHistoryWsDTO.placed</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Date placed;

	/** <i>Generated property</i> for <code>OrderHistoryWsDTO.guid</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String guid;

	/** <i>Generated property</i> for <code>OrderHistoryWsDTO.total</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO total;
	
	public OrderHistoryWsDTO()
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
	
		
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

		
	
	public String getStatus() 
	{
		return status;
	}
	
		
	
	public void setStatusDisplay(final String statusDisplay)
	{
		this.statusDisplay = statusDisplay;
	}

		
	
	public String getStatusDisplay() 
	{
		return statusDisplay;
	}
	
		
	
	public void setPlaced(final Date placed)
	{
		this.placed = placed;
	}

		
	
	public Date getPlaced() 
	{
		return placed;
	}
	
		
	
	public void setGuid(final String guid)
	{
		this.guid = guid;
	}

		
	
	public String getGuid() 
	{
		return guid;
	}
	
		
	
	public void setTotal(final PriceWsDTO total)
	{
		this.total = total;
	}

		
	
	public PriceWsDTO getTotal() 
	{
		return total;
	}
	


}
