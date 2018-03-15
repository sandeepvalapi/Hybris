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
import de.hybris.platform.ruleengineservices.rao.CartRAO;

public  class OrderConsumedRAO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderConsumedRAO.cart</code> property defined at extension <code>ruleengineservices</code>. */
		
	private CartRAO cart;

	/** <i>Generated property</i> for <code>OrderConsumedRAO.availableQuantity</code> property defined at extension <code>ruleengineservices</code>. */
		
	private int availableQuantity;
	
	public OrderConsumedRAO()
	{
		// default constructor
	}
	
		
	
	public void setCart(final CartRAO cart)
	{
		this.cart = cart;
	}

		
	
	public CartRAO getCart() 
	{
		return cart;
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
			final OrderConsumedRAO other = (OrderConsumedRAO) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getCart(), other.getCart()) 
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
		.append(getCart()) 
		.toHashCode();
	}

}
