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

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;

public  class AddCouponRAO extends AbstractRuleActionRAO 
{

 

	/** <i>Generated property</i> for <code>AddCouponRAO.couponId</code> property defined at extension <code>couponservices</code>. */
		
	private String couponId;
	
	public AddCouponRAO()
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
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final AddCouponRAO other = (AddCouponRAO) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getCouponId(), other.getCouponId()) 
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
		.toHashCode();
	}

}
