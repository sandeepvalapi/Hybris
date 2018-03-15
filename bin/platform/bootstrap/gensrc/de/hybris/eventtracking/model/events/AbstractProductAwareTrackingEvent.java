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
package de.hybris.eventtracking.model.events;

import java.io.Serializable;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;

public  class AbstractProductAwareTrackingEvent extends AbstractTrackingEvent {


	/** <i>Generated property</i> for <code>AbstractProductAwareTrackingEvent.productId</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String productId;

	/** <i>Generated property</i> for <code>AbstractProductAwareTrackingEvent.productName</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String productName;

	/** <i>Generated property</i> for <code>AbstractProductAwareTrackingEvent.categoryId</code> property defined at extension <code>yprofileeventtrackingws</code>. */
		
	private String categoryId;

	/** <i>Generated property</i> for <code>AbstractProductAwareTrackingEvent.productPrice</code> property defined at extension <code>yprofileeventtrackingws</code>. */
		
	private String productPrice;
	
	public AbstractProductAwareTrackingEvent()
	{
		super();
	}

	public AbstractProductAwareTrackingEvent(final Serializable source)
	{
		super(source);
	}
	
	
	
	public void setProductId(final String productId)
	{
		this.productId = productId;
	}
	
	
	
	public String getProductId() 
	{
		return productId;
	}
	
	
	
	public void setProductName(final String productName)
	{
		this.productName = productName;
	}
	
	
	
	public String getProductName() 
	{
		return productName;
	}
	
	
	
	public void setCategoryId(final String categoryId)
	{
		this.categoryId = categoryId;
	}
	
	
	
	public String getCategoryId() 
	{
		return categoryId;
	}
	
	
	
	public void setProductPrice(final String productPrice)
	{
		this.productPrice = productPrice;
	}
	
	
	
	public String getProductPrice() 
	{
		return productPrice;
	}
	


}