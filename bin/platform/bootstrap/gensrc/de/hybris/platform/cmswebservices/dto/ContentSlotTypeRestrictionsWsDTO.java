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
package de.hybris.platform.cmswebservices.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Content slot type restrictions DTO
 */
public  class ContentSlotTypeRestrictionsWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ContentSlotTypeRestrictionsWsDTO.contentSlotUid</code> property defined at extension <code>cmswebservices</code>. */
		
	private String contentSlotUid;

	/** <i>Generated property</i> for <code>ContentSlotTypeRestrictionsWsDTO.validComponentTypes</code> property defined at extension <code>cmswebservices</code>. */
		
	private List<String> validComponentTypes;
	
	public ContentSlotTypeRestrictionsWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setContentSlotUid(final String contentSlotUid)
	{
		this.contentSlotUid = contentSlotUid;
	}

		
	
	public String getContentSlotUid() 
	{
		return contentSlotUid;
	}
	
		
	
	public void setValidComponentTypes(final List<String> validComponentTypes)
	{
		this.validComponentTypes = validComponentTypes;
	}

		
	
	public List<String> getValidComponentTypes() 
	{
		return validComponentTypes;
	}
	


}
