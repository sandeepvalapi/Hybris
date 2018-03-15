/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
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
import java.util.List;

public  class UserBody  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UserBody.date</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String date;

	/** <i>Generated property</i> for <code>UserBody.identity</code> property defined at extension <code>yprofilecommons</code>. */
		
	private Consumer identity;

	/** <i>Generated property</i> for <code>UserBody.identities</code> property defined at extension <code>yprofilecommons</code>. */
		
	private List<Consumer> identities;

	/** <i>Generated property</i> for <code>UserBody.type</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>UserBody.masterData</code> property defined at extension <code>yprofilecommons</code>. */
		
	private UserMasterData masterData;
	
	public UserBody()
	{
		// default constructor
	}
	
		
	
	public void setDate(final String date)
	{
		this.date = date;
	}

		
	
	public String getDate() 
	{
		return date;
	}
	
		
	
	public void setIdentity(final Consumer identity)
	{
		this.identity = identity;
	}

		
	
	public Consumer getIdentity() 
	{
		return identity;
	}
	
		
	
	public void setIdentities(final List<Consumer> identities)
	{
		this.identities = identities;
	}

		
	
	public List<Consumer> getIdentities() 
	{
		return identities;
	}
	
		
	
	public void setType(final String type)
	{
		this.type = type;
	}

		
	
	public String getType() 
	{
		return type;
	}
	
		
	
	public void setMasterData(final UserMasterData masterData)
	{
		this.masterData = masterData;
	}

		
	
	public UserMasterData getMasterData() 
	{
		return masterData;
	}
	


}
