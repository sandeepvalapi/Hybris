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
package de.hybris.eventtracking.model.events;

import java.io.Serializable;

import de.hybris.eventtracking.model.events.AbstractProductAwareTrackingEvent;

public  class ProductMediaViewEvent extends AbstractProductAwareTrackingEvent {


	/** <i>Generated property</i> for <code>ProductMediaViewEvent.productMediaType</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String productMediaType;
	
	public ProductMediaViewEvent()
	{
		super();
	}

	public ProductMediaViewEvent(final Serializable source)
	{
		super(source);
	}
	
	
	
	public void setProductMediaType(final String productMediaType)
	{
		this.productMediaType = productMediaType;
	}
	
	
	
	public String getProductMediaType() 
	{
		return productMediaType;
	}
	


}