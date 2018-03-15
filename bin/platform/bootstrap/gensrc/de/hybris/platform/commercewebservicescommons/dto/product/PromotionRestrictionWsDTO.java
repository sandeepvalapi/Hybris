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
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;

public  class PromotionRestrictionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PromotionRestrictionWsDTO.restrictionType</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String restrictionType;

	/** <i>Generated property</i> for <code>PromotionRestrictionWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String description;
	
	public PromotionRestrictionWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setRestrictionType(final String restrictionType)
	{
		this.restrictionType = restrictionType;
	}

		
	
	public String getRestrictionType() 
	{
		return restrictionType;
	}
	
		
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

		
	
	public String getDescription() 
	{
		return description;
	}
	


}
