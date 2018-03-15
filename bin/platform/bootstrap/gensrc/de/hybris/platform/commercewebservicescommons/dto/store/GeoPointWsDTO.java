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
package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;

public  class GeoPointWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>GeoPointWsDTO.latitude</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double latitude;

	/** <i>Generated property</i> for <code>GeoPointWsDTO.longitude</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double longitude;
	
	public GeoPointWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setLatitude(final Double latitude)
	{
		this.latitude = latitude;
	}

		
	
	public Double getLatitude() 
	{
		return latitude;
	}
	
		
	
	public void setLongitude(final Double longitude)
	{
		this.longitude = longitude;
	}

		
	
	public Double getLongitude() 
	{
		return longitude;
	}
	


}
