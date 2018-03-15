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
package de.hybris.platform.assistedservicestorefront.customer360;

import java.io.Serializable;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import java.util.List;

public  class CustomerProfileData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CustomerProfileData.billingAddress</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private AddressData billingAddress;

	/** <i>Generated property</i> for <code>CustomerProfileData.deliveryAddress</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private AddressData deliveryAddress;

	/** <i>Generated property</i> for <code>CustomerProfileData.phone1</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private String phone1;

	/** <i>Generated property</i> for <code>CustomerProfileData.phone2</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private String phone2;

	/** <i>Generated property</i> for <code>CustomerProfileData.paymentInfoList</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private List<CCPaymentInfoData> paymentInfoList;
	
	public CustomerProfileData()
	{
		// default constructor
	}
	
		
	
	public void setBillingAddress(final AddressData billingAddress)
	{
		this.billingAddress = billingAddress;
	}

		
	
	public AddressData getBillingAddress() 
	{
		return billingAddress;
	}
	
		
	
	public void setDeliveryAddress(final AddressData deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
	}

		
	
	public AddressData getDeliveryAddress() 
	{
		return deliveryAddress;
	}
	
		
	
	public void setPhone1(final String phone1)
	{
		this.phone1 = phone1;
	}

		
	
	public String getPhone1() 
	{
		return phone1;
	}
	
		
	
	public void setPhone2(final String phone2)
	{
		this.phone2 = phone2;
	}

		
	
	public String getPhone2() 
	{
		return phone2;
	}
	
		
	
	public void setPaymentInfoList(final List<CCPaymentInfoData> paymentInfoList)
	{
		this.paymentInfoList = paymentInfoList;
	}

		
	
	public List<CCPaymentInfoData> getPaymentInfoList() 
	{
		return paymentInfoList;
	}
	


}
