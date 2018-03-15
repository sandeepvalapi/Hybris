/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:41 PM
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
package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.store.TimeWsDTO;

public  class OpeningDayWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OpeningDayWsDTO.openingTime</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private TimeWsDTO openingTime;

	/** <i>Generated property</i> for <code>OpeningDayWsDTO.closingTime</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private TimeWsDTO closingTime;
	
	public OpeningDayWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setOpeningTime(final TimeWsDTO openingTime)
	{
		this.openingTime = openingTime;
	}

		
	
	public TimeWsDTO getOpeningTime() 
	{
		return openingTime;
	}
	
		
	
	public void setClosingTime(final TimeWsDTO closingTime)
	{
		this.closingTime = closingTime;
	}

		
	
	public TimeWsDTO getClosingTime() 
	{
		return closingTime;
	}
	


}
