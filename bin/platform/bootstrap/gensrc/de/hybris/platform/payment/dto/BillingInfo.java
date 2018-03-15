/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
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
 * Billing address info
 */
public  class BillingInfo  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>BillingInfo.city</code> property defined at extension <code>payment</code>. */
		
	private String city;

	/** <i>Generated property</i> for <code>BillingInfo.country</code> property defined at extension <code>payment</code>. */
		
	private String country;

	/** <i>Generated property</i> for <code>BillingInfo.email</code> property defined at extension <code>payment</code>. */
		
	private String email;

	/** <i>Generated property</i> for <code>BillingInfo.firstName</code> property defined at extension <code>payment</code>. */
		
	private String firstName;

	/** <i>Generated property</i> for <code>BillingInfo.lastName</code> property defined at extension <code>payment</code>. */
		
	private String lastName;

	/** <i>Generated property</i> for <code>BillingInfo.phoneNumber</code> property defined at extension <code>payment</code>. */
		
	private String phoneNumber;

	/** <i>Generated property</i> for <code>BillingInfo.postalCode</code> property defined at extension <code>payment</code>. */
		
	private String postalCode;

	/** <i>Generated property</i> for <code>BillingInfo.state</code> property defined at extension <code>payment</code>. */
		
	private String state;

	/** <i>Generated property</i> for <code>BillingInfo.street1</code> property defined at extension <code>payment</code>. */
		
	private String street1;

	/** <i>Generated property</i> for <code>BillingInfo.street2</code> property defined at extension <code>payment</code>. */
		
	private String street2;

	/** <i>Generated property</i> for <code>BillingInfo.ipAddress</code> property defined at extension <code>payment</code>. */
		
	private String ipAddress;

	/** <i>Generated property</i> for <code>BillingInfo.region</code> property defined at extension <code>payment</code>. */
		
	private String region;
	
	public BillingInfo()
	{
		// default constructor
	}
	
		
	
	public void setCity(final String city)
	{
		this.city = city;
	}

		
	
	public String getCity() 
	{
		return city;
	}
	
		
	
	public void setCountry(final String country)
	{
		this.country = country;
	}

		
	
	public String getCountry() 
	{
		return country;
	}
	
		
	
	public void setEmail(final String email)
	{
		this.email = email;
	}

		
	
	public String getEmail() 
	{
		return email;
	}
	
		
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

		
	
	public String getFirstName() 
	{
		return firstName;
	}
	
		
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

		
	
	public String getLastName() 
	{
		return lastName;
	}
	
		
	
	public void setPhoneNumber(final String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

		
	
	public String getPhoneNumber() 
	{
		return phoneNumber;
	}
	
		
	
	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}

		
	
	public String getPostalCode() 
	{
		return postalCode;
	}
	
		
	
	public void setState(final String state)
	{
		this.state = state;
	}

		
	
	public String getState() 
	{
		return state;
	}
	
		
	
	public void setStreet1(final String street1)
	{
		this.street1 = street1;
	}

		
	
	public String getStreet1() 
	{
		return street1;
	}
	
		
	
	public void setStreet2(final String street2)
	{
		this.street2 = street2;
	}

		
	
	public String getStreet2() 
	{
		return street2;
	}
	
		
	
	public void setIpAddress(final String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

		
	
	public String getIpAddress() 
	{
		return ipAddress;
	}
	
		
	
	public void setRegion(final String region)
	{
		this.region = region;
	}

		
	
	public String getRegion() 
	{
		return region;
	}
	

	/**
	 * Copy the values from another object
	 */
	public void copy(final BillingInfo orig)
	{
		try
		{
			final BillingInfo deepCopy = (BillingInfo) org.apache.commons.lang.SerializationUtils.clone(orig);
			org.apache.commons.beanutils.BeanUtils.copyProperties(this, deepCopy);
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Failed to copy BillingInfo", ex);
		}
	}




}
