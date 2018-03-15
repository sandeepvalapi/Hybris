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
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;

public  class PriceRangeWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PriceRangeWsDTO.maxPrice</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO maxPrice;

	/** <i>Generated property</i> for <code>PriceRangeWsDTO.minPrice</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO minPrice;
	
	public PriceRangeWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setMaxPrice(final PriceWsDTO maxPrice)
	{
		this.maxPrice = maxPrice;
	}

		
	
	public PriceWsDTO getMaxPrice() 
	{
		return maxPrice;
	}
	
		
	
	public void setMinPrice(final PriceWsDTO minPrice)
	{
		this.minPrice = minPrice;
	}

		
	
	public PriceWsDTO getMinPrice() 
	{
		return minPrice;
	}
	


}
