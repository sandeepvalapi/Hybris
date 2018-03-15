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
package de.hybris.platform.personalizationservices;

import java.io.Serializable;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;

public  class CxContext  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CxContext.user</code> property defined at extension <code>personalizationservices</code>. */
		
	private UserModel user;

	/** <i>Generated property</i> for <code>CxContext.actionResult</code> property defined at extension <code>personalizationservices</code>. */
		
	private CxAbstractActionResult actionResult;
	
	public CxContext()
	{
		// default constructor
	}
	
		
	
	public void setUser(final UserModel user)
	{
		this.user = user;
	}

		
	
	public UserModel getUser() 
	{
		return user;
	}
	
		
	
	public void setActionResult(final CxAbstractActionResult actionResult)
	{
		this.actionResult = actionResult;
	}

		
	
	public CxAbstractActionResult getActionResult() 
	{
		return actionResult;
	}
	


}
