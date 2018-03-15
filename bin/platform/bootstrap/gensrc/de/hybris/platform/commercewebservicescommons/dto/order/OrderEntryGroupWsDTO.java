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
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import java.util.Collection;

public  class OrderEntryGroupWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderEntryGroupWsDTO.totalPriceWithTax</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO totalPriceWithTax;

	/** <i>Generated property</i> for <code>OrderEntryGroupWsDTO.entries</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<OrderEntryWsDTO> entries;

	/** <i>Generated property</i> for <code>OrderEntryGroupWsDTO.quantity</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Long quantity;
	
	public OrderEntryGroupWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setTotalPriceWithTax(final PriceWsDTO totalPriceWithTax)
	{
		this.totalPriceWithTax = totalPriceWithTax;
	}

		
	
	public PriceWsDTO getTotalPriceWithTax() 
	{
		return totalPriceWithTax;
	}
	
		
	
	public void setEntries(final Collection<OrderEntryWsDTO> entries)
	{
		this.entries = entries;
	}

		
	
	public Collection<OrderEntryWsDTO> getEntries() 
	{
		return entries;
	}
	
		
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

		
	
	public Long getQuantity() 
	{
		return quantity;
	}
	


}
