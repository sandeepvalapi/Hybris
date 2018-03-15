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
package com.hybris.yprofile.dto;

import com.hybris.yprofile.dto.AbstractProfileEvent;

public  class User extends AbstractProfileEvent 
{

 

	/** <i>Generated property</i> for <code>User.channelRef</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String channelRef;

	/** <i>Generated property</i> for <code>User.date</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String date;

	/** <i>Generated property</i> for <code>User.type</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>User.sessionId</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String sessionId;

	/** <i>Generated property</i> for <code>User.body</code> property defined at extension <code>yprofilecommons</code>. */
		
	private UserBody body;
	
	public User()
	{
		// default constructor
	}
	
		
	
	public void setChannelRef(final String channelRef)
	{
		this.channelRef = channelRef;
	}

		
	
	public String getChannelRef() 
	{
		return channelRef;
	}
	
		
	
	public void setDate(final String date)
	{
		this.date = date;
	}

		
	
	public String getDate() 
	{
		return date;
	}
	
		
	
	public void setType(final String type)
	{
		this.type = type;
	}

		
	
	public String getType() 
	{
		return type;
	}
	
		
	
	public void setSessionId(final String sessionId)
	{
		this.sessionId = sessionId;
	}

		
	
	public String getSessionId() 
	{
		return sessionId;
	}
	
		
	
	public void setBody(final UserBody body)
	{
		this.body = body;
	}

		
	
	public UserBody getBody() 
	{
		return body;
	}
	


}
