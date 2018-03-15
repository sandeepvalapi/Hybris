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
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;

public  class ProductConsumedRAO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductConsumedRAO.orderEntry</code> property defined at extension <code>ruleengineservices</code>. */
		
	private OrderEntryRAO orderEntry;

	/** <i>Generated property</i> for <code>ProductConsumedRAO.availableQuantity</code> property defined at extension <code>ruleengineservices</code>. */
		
	private int availableQuantity;
	
	public ProductConsumedRAO()
	{
		// default constructor
	}
	
		
	
	public void setOrderEntry(final OrderEntryRAO orderEntry)
	{
		this.orderEntry = orderEntry;
	}

		
	
	public OrderEntryRAO getOrderEntry() 
	{
		return orderEntry;
	}
	
		
	
	public void setAvailableQuantity(final int availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}

		
	
	public int getAvailableQuantity() 
	{
		return availableQuantity;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final ProductConsumedRAO other = (ProductConsumedRAO) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getOrderEntry(), other.getOrderEntry()) 
			.isEquals();
		} 
		catch (ClassCastException c)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return new org.apache.commons.lang.builder.HashCodeBuilder()
		.append(getOrderEntry()) 
		.toHashCode();
	}

}
