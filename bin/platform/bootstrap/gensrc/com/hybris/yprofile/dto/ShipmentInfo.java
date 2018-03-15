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
package com.hybris.yprofile.dto;

import java.io.Serializable;

public  class ShipmentInfo  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ShipmentInfo.address</code> property defined at extension <code>yprofilecommons</code>. */
		
	private Address address;

	/** <i>Generated property</i> for <code>ShipmentInfo.carrier</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String carrier;

	/** <i>Generated property</i> for <code>ShipmentInfo.trackingRef</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String trackingRef;

	/** <i>Generated property</i> for <code>ShipmentInfo.status</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String status;
	
	public ShipmentInfo()
	{
		// default constructor
	}
	
		
	
	public void setAddress(final Address address)
	{
		this.address = address;
	}

		
	
	public Address getAddress() 
	{
		return address;
	}
	
		
	
	public void setCarrier(final String carrier)
	{
		this.carrier = carrier;
	}

		
	
	public String getCarrier() 
	{
		return carrier;
	}
	
		
	
	public void setTrackingRef(final String trackingRef)
	{
		this.trackingRef = trackingRef;
	}

		
	
	public String getTrackingRef() 
	{
		return trackingRef;
	}
	
		
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

		
	
	public String getStatus() 
	{
		return status;
	}
	


}
