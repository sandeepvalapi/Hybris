/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:41 PM
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
import java.math.BigDecimal;

public  class DiscountValueRAO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DiscountValueRAO.value</code> property defined at extension <code>ruleengineservices</code>. */
		
	private BigDecimal value;

	/** <i>Generated property</i> for <code>DiscountValueRAO.currencyIsoCode</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String currencyIsoCode;
	
	public DiscountValueRAO()
	{
		// default constructor
	}
	
		
	
	public void setValue(final BigDecimal value)
	{
		this.value = value;
	}

		
	
	public BigDecimal getValue() 
	{
		return value;
	}
	
		
	
	public void setCurrencyIsoCode(final String currencyIsoCode)
	{
		this.currencyIsoCode = currencyIsoCode;
	}

		
	
	public String getCurrencyIsoCode() 
	{
		return currencyIsoCode;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final DiscountValueRAO other = (DiscountValueRAO) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getValue(), other.getValue()) 
			.append(getCurrencyIsoCode(), other.getCurrencyIsoCode()) 
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
		.append(getValue()) 
		.append(getCurrencyIsoCode()) 
		.toHashCode();
	}

}
