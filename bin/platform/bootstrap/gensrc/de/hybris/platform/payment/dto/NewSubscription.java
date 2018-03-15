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
package de.hybris.platform.payment.dto;

import java.io.Serializable;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

/**
 * Informations about payment card
 */
public  class NewSubscription  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>NewSubscription.subscriptionID</code> property defined at extension <code>payment</code>. */
		
	private String subscriptionID;

	/** <i>Generated property</i> for <code>NewSubscription.transactionEntry</code> property defined at extension <code>payment</code>. */
		
	private PaymentTransactionEntryModel transactionEntry;
	
	public NewSubscription()
	{
		// default constructor
	}
	
		
	
	public void setSubscriptionID(final String subscriptionID)
	{
		this.subscriptionID = subscriptionID;
	}

		
	
	public String getSubscriptionID() 
	{
		return subscriptionID;
	}
	
		
	
	public void setTransactionEntry(final PaymentTransactionEntryModel transactionEntry)
	{
		this.transactionEntry = transactionEntry;
	}

		
	
	public PaymentTransactionEntryModel getTransactionEntry() 
	{
		return transactionEntry;
	}
	


}
