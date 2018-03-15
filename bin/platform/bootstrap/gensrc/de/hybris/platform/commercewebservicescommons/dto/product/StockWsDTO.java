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

public  class StockWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>StockWsDTO.stockLevelStatus</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String stockLevelStatus;

	/** <i>Generated property</i> for <code>StockWsDTO.stockLevel</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Long stockLevel;
	
	public StockWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setStockLevelStatus(final String stockLevelStatus)
	{
		this.stockLevelStatus = stockLevelStatus;
	}

		
	
	public String getStockLevelStatus() 
	{
		return stockLevelStatus;
	}
	
		
	
	public void setStockLevel(final Long stockLevel)
	{
		this.stockLevel = stockLevel;
	}

		
	
	public Long getStockLevel() 
	{
		return stockLevel;
	}
	


}
