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
package com.hybris.yprofile.dto;

import java.io.Serializable;

public  class PaymentInfo  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PaymentInfo.paymentType</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String paymentType;

	/** <i>Generated property</i> for <code>PaymentInfo.status</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>PaymentInfo.address</code> property defined at extension <code>yprofilecommons</code>. */
		
	private Address address;
	
	public PaymentInfo()
	{
		// default constructor
	}
	
		
	
	public void setPaymentType(final String paymentType)
	{
		this.paymentType = paymentType;
	}

		
	
	public String getPaymentType() 
	{
		return paymentType;
	}
	
		
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

		
	
	public String getStatus() 
	{
		return status;
	}
	
		
	
	public void setAddress(final Address address)
	{
		this.address = address;
	}

		
	
	public Address getAddress() 
	{
		return address;
	}
	


}
