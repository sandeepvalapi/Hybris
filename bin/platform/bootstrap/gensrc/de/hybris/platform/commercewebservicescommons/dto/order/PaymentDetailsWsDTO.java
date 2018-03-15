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
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.CardTypeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;

public  class PaymentDetailsWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.id</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.accountHolderName</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String accountHolderName;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.cardType</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private CardTypeWsDTO cardType;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.cardNumber</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String cardNumber;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.startMonth</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String startMonth;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.startYear</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String startYear;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.expiryMonth</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String expiryMonth;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.expiryYear</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String expiryYear;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.issueNumber</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String issueNumber;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.subscriptionId</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String subscriptionId;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.saved</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean saved;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.defaultPayment</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean defaultPayment;

	/** <i>Generated property</i> for <code>PaymentDetailsWsDTO.billingAddress</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private AddressWsDTO billingAddress;
	
	public PaymentDetailsWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setId(final String id)
	{
		this.id = id;
	}

		
	
	public String getId() 
	{
		return id;
	}
	
		
	
	public void setAccountHolderName(final String accountHolderName)
	{
		this.accountHolderName = accountHolderName;
	}

		
	
	public String getAccountHolderName() 
	{
		return accountHolderName;
	}
	
		
	
	public void setCardType(final CardTypeWsDTO cardType)
	{
		this.cardType = cardType;
	}

		
	
	public CardTypeWsDTO getCardType() 
	{
		return cardType;
	}
	
		
	
	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

		
	
	public String getCardNumber() 
	{
		return cardNumber;
	}
	
		
	
	public void setStartMonth(final String startMonth)
	{
		this.startMonth = startMonth;
	}

		
	
	public String getStartMonth() 
	{
		return startMonth;
	}
	
		
	
	public void setStartYear(final String startYear)
	{
		this.startYear = startYear;
	}

		
	
	public String getStartYear() 
	{
		return startYear;
	}
	
		
	
	public void setExpiryMonth(final String expiryMonth)
	{
		this.expiryMonth = expiryMonth;
	}

		
	
	public String getExpiryMonth() 
	{
		return expiryMonth;
	}
	
		
	
	public void setExpiryYear(final String expiryYear)
	{
		this.expiryYear = expiryYear;
	}

		
	
	public String getExpiryYear() 
	{
		return expiryYear;
	}
	
		
	
	public void setIssueNumber(final String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

		
	
	public String getIssueNumber() 
	{
		return issueNumber;
	}
	
		
	
	public void setSubscriptionId(final String subscriptionId)
	{
		this.subscriptionId = subscriptionId;
	}

		
	
	public String getSubscriptionId() 
	{
		return subscriptionId;
	}
	
		
	
	public void setSaved(final Boolean saved)
	{
		this.saved = saved;
	}

		
	
	public Boolean getSaved() 
	{
		return saved;
	}
	
		
	
	public void setDefaultPayment(final Boolean defaultPayment)
	{
		this.defaultPayment = defaultPayment;
	}

		
	
	public Boolean getDefaultPayment() 
	{
		return defaultPayment;
	}
	
		
	
	public void setBillingAddress(final AddressWsDTO billingAddress)
	{
		this.billingAddress = billingAddress;
	}

		
	
	public AddressWsDTO getBillingAddress() 
	{
		return billingAddress;
	}
	


}
