/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:39 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.payment.dto;

/**
 * BasicCardInfo is a dto holding the basic information of a credit card
 */
public  class BasicCardInfo  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>BasicCardInfo.cardNumber</code> property defined at extension <code>payment</code>. */
		
	private String cardNumber;

	/** <i>Generated property</i> for <code>BasicCardInfo.expirationMonth</code> property defined at extension <code>payment</code>. */
		
	private Integer expirationMonth;

	/** <i>Generated property</i> for <code>BasicCardInfo.expirationYear</code> property defined at extension <code>payment</code>. */
		
	private Integer expirationYear;
	
	public BasicCardInfo()
	{
		// default constructor
	}
	
		
	
	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

		
	
	public String getCardNumber() 
	{
		return cardNumber;
	}
	
		
	
	public void setExpirationMonth(final Integer expirationMonth)
	{
		this.expirationMonth = expirationMonth;
	}

		
	
	public Integer getExpirationMonth() 
	{
		return expirationMonth;
	}
	
		
	
	public void setExpirationYear(final Integer expirationYear)
	{
		this.expirationYear = expirationYear;
	}

		
	
	public Integer getExpirationYear() 
	{
		return expirationYear;
	}
	

	/**
	 * Copy the values from another object
	 */
	public void copy(final BasicCardInfo orig)
	{
		try
		{
			final BasicCardInfo deepCopy = (BasicCardInfo) org.apache.commons.lang.SerializationUtils.clone(orig);
			org.apache.commons.beanutils.BeanUtils.copyProperties(this, deepCopy);
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Failed to copy BasicCardInfo", ex);
		}
	}




}
