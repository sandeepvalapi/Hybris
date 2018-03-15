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
package de.hybris.platform.couponservices.rao;

import java.io.Serializable;

public  class CouponRAO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CouponRAO.couponId</code> property defined at extension <code>couponservices</code>. */
		
	private String couponId;

	/** <i>Generated property</i> for <code>CouponRAO.couponCode</code> property defined at extension <code>couponservices</code>. */
		
	private String couponCode;
	
	public CouponRAO()
	{
		// default constructor
	}
	
		
	
	public void setCouponId(final String couponId)
	{
		this.couponId = couponId;
	}

		
	
	public String getCouponId() 
	{
		return couponId;
	}
	
		
	
	public void setCouponCode(final String couponCode)
	{
		this.couponCode = couponCode;
	}

		
	
	public String getCouponCode() 
	{
		return couponCode;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final CouponRAO other = (CouponRAO) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getCouponId(), other.getCouponId()) 
			.append(getCouponCode(), other.getCouponCode()) 
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
		.append(getCouponId()) 
		.append(getCouponCode()) 
		.toHashCode();
	}

}
