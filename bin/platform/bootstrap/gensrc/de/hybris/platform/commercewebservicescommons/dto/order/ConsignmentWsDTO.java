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

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.ConsignmentEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import java.util.Date;
import java.util.List;

public  class ConsignmentWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConsignmentWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>ConsignmentWsDTO.trackingID</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String trackingID;

	/** <i>Generated property</i> for <code>ConsignmentWsDTO.status</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>ConsignmentWsDTO.statusDate</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Date statusDate;

	/** <i>Generated property</i> for <code>ConsignmentWsDTO.entries</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<ConsignmentEntryWsDTO> entries;

	/** <i>Generated property</i> for <code>ConsignmentWsDTO.shippingAddress</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private AddressWsDTO shippingAddress;

	/** <i>Generated property</i> for <code>ConsignmentWsDTO.deliveryPointOfService</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PointOfServiceWsDTO deliveryPointOfService;
	
	public ConsignmentWsDTO()
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
	
		
	
	public void setTrackingID(final String trackingID)
	{
		this.trackingID = trackingID;
	}

		
	
	public String getTrackingID() 
	{
		return trackingID;
	}
	
		
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

		
	
	public String getStatus() 
	{
		return status;
	}
	
		
	
	public void setStatusDate(final Date statusDate)
	{
		this.statusDate = statusDate;
	}

		
	
	public Date getStatusDate() 
	{
		return statusDate;
	}
	
		
	
	public void setEntries(final List<ConsignmentEntryWsDTO> entries)
	{
		this.entries = entries;
	}

		
	
	public List<ConsignmentEntryWsDTO> getEntries() 
	{
		return entries;
	}
	
		
	
	public void setShippingAddress(final AddressWsDTO shippingAddress)
	{
		this.shippingAddress = shippingAddress;
	}

		
	
	public AddressWsDTO getShippingAddress() 
	{
		return shippingAddress;
	}
	
		
	
	public void setDeliveryPointOfService(final PointOfServiceWsDTO deliveryPointOfService)
	{
		this.deliveryPointOfService = deliveryPointOfService;
	}

		
	
	public PointOfServiceWsDTO getDeliveryPointOfService() 
	{
		return deliveryPointOfService;
	}
	


}
