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
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import java.util.List;

public  class CartListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CartListWsDTO.carts</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<CartWsDTO> carts;
	
	public CartListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setCarts(final List<CartWsDTO> carts)
	{
		this.carts = carts;
	}

		
	
	public List<CartWsDTO> getCarts() 
	{
		return carts;
	}
	


}
