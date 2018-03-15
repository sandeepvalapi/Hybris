/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:37 PM
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
package de.hybris.platform.commercewebservicescommons.dto.storesession;

import java.io.Serializable;

public  class CurrencyWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CurrencyWsDTO.isocode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String isocode;

	/** <i>Generated property</i> for <code>CurrencyWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CurrencyWsDTO.active</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean active;

	/** <i>Generated property</i> for <code>CurrencyWsDTO.symbol</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String symbol;
	
	public CurrencyWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setIsocode(final String isocode)
	{
		this.isocode = isocode;
	}

		
	
	public String getIsocode() 
	{
		return isocode;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setActive(final Boolean active)
	{
		this.active = active;
	}

		
	
	public Boolean getActive() 
	{
		return active;
	}
	
		
	
	public void setSymbol(final String symbol)
	{
		this.symbol = symbol;
	}

		
	
	public String getSymbol() 
	{
		return symbol;
	}
	


}
