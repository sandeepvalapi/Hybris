/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
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

import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.payment.dto.BasicCardInfo;
import de.hybris.platform.payment.dto.BillingInfo;

/**
 * Informations about payment card
 */
public  class CardInfo extends BasicCardInfo 
{


	/** <i>Generated property</i> for <code>CardInfo.cardHolderFullName</code> property defined at extension <code>payment</code>. */
		
	private String cardHolderFullName;

	/** <i>Generated property</i> for <code>CardInfo.issueNumber</code> property defined at extension <code>payment</code>. */
		
	private String issueNumber;

	/** <i>Generated property</i> for <code>CardInfo.issueMonth</code> property defined at extension <code>payment</code>. */
		
	private Integer issueMonth;

	/** <i>Generated property</i> for <code>CardInfo.issueYear</code> property defined at extension <code>payment</code>. */
		
	private Integer issueYear;

	/** <i>Generated property</i> for <code>CardInfo.cardType</code> property defined at extension <code>payment</code>. */
		
	private CreditCardType cardType;

	/** <i>Generated property</i> for <code>CardInfo.billingInfo</code> property defined at extension <code>payment</code>. */
		
	private BillingInfo billingInfo;

	/** <i>Generated property</i> for <code>CardInfo.cv2Number</code> property defined at extension <code>payment</code>. */
		
	private String cv2Number;
	
	public CardInfo()
	{
		// default constructor
	}
	
		
	
	public void setCardHolderFullName(final String cardHolderFullName)
	{
		this.cardHolderFullName = cardHolderFullName;
	}

		
	
	public String getCardHolderFullName() 
	{
		return cardHolderFullName;
	}
	
		
	
	public void setIssueNumber(final String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

		
	
	public String getIssueNumber() 
	{
		return issueNumber;
	}
	
		
	
	public void setIssueMonth(final Integer issueMonth)
	{
		this.issueMonth = issueMonth;
	}

		
	
	public Integer getIssueMonth() 
	{
		return issueMonth;
	}
	
		
	
	public void setIssueYear(final Integer issueYear)
	{
		this.issueYear = issueYear;
	}

		
	
	public Integer getIssueYear() 
	{
		return issueYear;
	}
	
		
	
	public void setCardType(final CreditCardType cardType)
	{
		this.cardType = cardType;
	}

		
	
	public CreditCardType getCardType() 
	{
		return cardType;
	}
	
		
	
	public void setBillingInfo(final BillingInfo billingInfo)
	{
		this.billingInfo = billingInfo;
	}

		
	
	public BillingInfo getBillingInfo() 
	{
		return billingInfo;
	}
	
		
	
	public void setCv2Number(final String cv2Number)
	{
		this.cv2Number = cv2Number;
	}

		
	
	public String getCv2Number() 
	{
		return cv2Number;
	}
	

	/**
	 * Copy the values from another object
	 */
	public void copy(final CardInfo orig)
	{
		try
		{
			final CardInfo deepCopy = (CardInfo) org.apache.commons.lang.SerializationUtils.clone(orig);
			org.apache.commons.beanutils.BeanUtils.copyProperties(this, deepCopy);
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Failed to copy CardInfo", ex);
		}
	}




}
