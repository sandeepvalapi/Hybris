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
package de.hybris.platform.assistedservicepromotionfacades.customer360;

import java.io.Serializable;

public  class CSACouponData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CSACouponData.code</code> property defined at extension <code>assistedservicepromotionfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>CSACouponData.name</code> property defined at extension <code>assistedservicepromotionfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CSACouponData.couponApplied</code> property defined at extension <code>assistedservicepromotionfacades</code>. */
		
	private Boolean couponApplied;
	
	public CSACouponData()
	{
		// default constructor
	}
	
		
	
	public void setCode(final String code)
	{
		this.code = code;
	}

		
	
	public String getCode() 
	{
		return code;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setCouponApplied(final Boolean couponApplied)
	{
		this.couponApplied = couponApplied;
	}

		
	
	public Boolean getCouponApplied() 
	{
		return couponApplied;
	}
	


}
