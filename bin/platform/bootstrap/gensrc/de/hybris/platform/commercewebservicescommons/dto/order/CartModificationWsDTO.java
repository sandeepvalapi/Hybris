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
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;

public  class CartModificationWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CartModificationWsDTO.statusCode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String statusCode;

	/** <i>Generated property</i> for <code>CartModificationWsDTO.quantityAdded</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Long quantityAdded;

	/** <i>Generated property</i> for <code>CartModificationWsDTO.quantity</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Long quantity;

	/** <i>Generated property</i> for <code>CartModificationWsDTO.entry</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private OrderEntryWsDTO entry;

	/** <i>Generated property</i> for <code>CartModificationWsDTO.deliveryModeChanged</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean deliveryModeChanged;

	/** <i>Generated property</i> for <code>CartModificationWsDTO.statusMessage</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String statusMessage;
	
	public CartModificationWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setStatusCode(final String statusCode)
	{
		this.statusCode = statusCode;
	}

		
	
	public String getStatusCode() 
	{
		return statusCode;
	}
	
		
	
	public void setQuantityAdded(final Long quantityAdded)
	{
		this.quantityAdded = quantityAdded;
	}

		
	
	public Long getQuantityAdded() 
	{
		return quantityAdded;
	}
	
		
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

		
	
	public Long getQuantity() 
	{
		return quantity;
	}
	
		
	
	public void setEntry(final OrderEntryWsDTO entry)
	{
		this.entry = entry;
	}

		
	
	public OrderEntryWsDTO getEntry() 
	{
		return entry;
	}
	
		
	
	public void setDeliveryModeChanged(final Boolean deliveryModeChanged)
	{
		this.deliveryModeChanged = deliveryModeChanged;
	}

		
	
	public Boolean getDeliveryModeChanged() 
	{
		return deliveryModeChanged;
	}
	
		
	
	public void setStatusMessage(final String statusMessage)
	{
		this.statusMessage = statusMessage;
	}

		
	
	public String getStatusMessage() 
	{
		return statusMessage;
	}
	


}
