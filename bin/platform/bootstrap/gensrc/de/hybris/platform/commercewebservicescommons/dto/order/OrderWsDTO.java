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
package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.order.AbstractOrderWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.ConsignmentWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import java.util.Date;
import java.util.List;

public  class OrderWsDTO extends AbstractOrderWsDTO 
{

 

	/** <i>Generated property</i> for <code>OrderWsDTO.created</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Date created;

	/** <i>Generated property</i> for <code>OrderWsDTO.status</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>OrderWsDTO.statusDisplay</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String statusDisplay;

	/** <i>Generated property</i> for <code>OrderWsDTO.guestCustomer</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean guestCustomer;

	/** <i>Generated property</i> for <code>OrderWsDTO.consignments</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<ConsignmentWsDTO> consignments;

	/** <i>Generated property</i> for <code>OrderWsDTO.deliveryStatus</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String deliveryStatus;

	/** <i>Generated property</i> for <code>OrderWsDTO.deliveryStatusDisplay</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String deliveryStatusDisplay;

	/** <i>Generated property</i> for <code>OrderWsDTO.unconsignedEntries</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<OrderEntryWsDTO> unconsignedEntries;
	
	public OrderWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setCreated(final Date created)
	{
		this.created = created;
	}

		
	
	public Date getCreated() 
	{
		return created;
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
	
		
	
	public void setGuestCustomer(final Boolean guestCustomer)
	{
		this.guestCustomer = guestCustomer;
	}

		
	
	public Boolean getGuestCustomer() 
	{
		return guestCustomer;
	}
	
		
	
	public void setConsignments(final List<ConsignmentWsDTO> consignments)
	{
		this.consignments = consignments;
	}

		
	
	public List<ConsignmentWsDTO> getConsignments() 
	{
		return consignments;
	}
	
		
	
	public void setDeliveryStatus(final String deliveryStatus)
	{
		this.deliveryStatus = deliveryStatus;
	}

		
	
	public String getDeliveryStatus() 
	{
		return deliveryStatus;
	}
	
		
	
	public void setDeliveryStatusDisplay(final String deliveryStatusDisplay)
	{
		this.deliveryStatusDisplay = deliveryStatusDisplay;
	}

		
	
	public String getDeliveryStatusDisplay() 
	{
		return deliveryStatusDisplay;
	}
	
		
	
	public void setUnconsignedEntries(final List<OrderEntryWsDTO> unconsignedEntries)
	{
		this.unconsignedEntries = unconsignedEntries;
	}

		
	
	public List<OrderEntryWsDTO> getUnconsignedEntries() 
	{
		return unconsignedEntries;
	}
	


}
