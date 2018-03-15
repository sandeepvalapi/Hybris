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
package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public  class AfterSessionAttributeChangeEvent  extends AbstractEvent {


	/** <i>Generated property</i> for <code>AfterSessionAttributeChangeEvent.attributeName</code> property defined at extension <code>core</code>. */
		
	private String attributeName;

	/** <i>Generated property</i> for <code>AfterSessionAttributeChangeEvent.value</code> property defined at extension <code>core</code>. */
		
	private Object value;
	
	public AfterSessionAttributeChangeEvent()
	{
		super();
	}

	public AfterSessionAttributeChangeEvent(final Serializable source)
	{
		super(source);
	}
	
	
	
	public void setAttributeName(final String attributeName)
	{
		this.attributeName = attributeName;
	}
	
	
	
	public String getAttributeName() 
	{
		return attributeName;
	}
	
	
	
	public void setValue(final Object value)
	{
		this.value = value;
	}
	
	
	
	public Object getValue() 
	{
		return value;
	}
	


}