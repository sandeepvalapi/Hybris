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
package de.hybris.platform.commerceservices.order;

import java.io.Serializable;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import java.util.List;

/**
 * Represents the result of the restoration of a cart to a customer's session.
 */
public  class CommerceCartRestoration  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CommerceCartRestoration.modifications</code> property defined at extension <code>commerceservices</code>. */
		
	private List<CommerceCartModification> modifications;
	
	public CommerceCartRestoration()
	{
		// default constructor
	}
	
		
	
	public void setModifications(final List<CommerceCartModification> modifications)
	{
		this.modifications = modifications;
	}

		
	
	public List<CommerceCartModification> getModifications() 
	{
		return modifications;
	}
	


}
