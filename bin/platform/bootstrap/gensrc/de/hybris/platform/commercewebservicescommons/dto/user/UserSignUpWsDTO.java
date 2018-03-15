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
package de.hybris.platform.commercewebservicescommons.dto.user;

import java.io.Serializable;

public  class UserSignUpWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UserSignUpWsDTO.uid</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>UserSignUpWsDTO.firstName</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String firstName;

	/** <i>Generated property</i> for <code>UserSignUpWsDTO.lastName</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String lastName;

	/** <i>Generated property</i> for <code>UserSignUpWsDTO.titleCode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String titleCode;

	/** <i>Generated property</i> for <code>UserSignUpWsDTO.password</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String password;
	
	public UserSignUpWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

		
	
	public String getUid() 
	{
		return uid;
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
	
		
	
	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

		
	
	public String getTitleCode() 
	{
		return titleCode;
	}
	
		
	
	public void setPassword(final String password)
	{
		this.password = password;
	}

		
	
	public String getPassword() 
	{
		return password;
	}
	


}
