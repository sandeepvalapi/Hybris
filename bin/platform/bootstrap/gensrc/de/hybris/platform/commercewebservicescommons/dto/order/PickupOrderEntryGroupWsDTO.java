/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
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

import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryGroupWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;

public  class PickupOrderEntryGroupWsDTO extends OrderEntryGroupWsDTO 
{

 

	/** <i>Generated property</i> for <code>PickupOrderEntryGroupWsDTO.deliveryPointOfService</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PointOfServiceWsDTO deliveryPointOfService;

	/** <i>Generated property</i> for <code>PickupOrderEntryGroupWsDTO.distance</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double distance;
	
	public PickupOrderEntryGroupWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setDeliveryPointOfService(final PointOfServiceWsDTO deliveryPointOfService)
	{
		this.deliveryPointOfService = deliveryPointOfService;
	}

		
	
	public PointOfServiceWsDTO getDeliveryPointOfService() 
	{
		return deliveryPointOfService;
	}
	
		
	
	public void setDistance(final Double distance)
	{
		this.distance = distance;
	}

		
	
	public Double getDistance() 
	{
		return distance;
	}
	


}
