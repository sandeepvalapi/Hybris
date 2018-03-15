/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
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

public  class LanguageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>LanguageWsDTO.isocode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String isocode;

	/** <i>Generated property</i> for <code>LanguageWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>LanguageWsDTO.nativeName</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String nativeName;

	/** <i>Generated property</i> for <code>LanguageWsDTO.active</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean active;
	
	public LanguageWsDTO()
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
	
		
	
	public void setNativeName(final String nativeName)
	{
		this.nativeName = nativeName;
	}

		
	
	public String getNativeName() 
	{
		return nativeName;
	}
	
		
	
	public void setActive(final Boolean active)
	{
		this.active = active;
	}

		
	
	public Boolean getActive() 
	{
		return active;
	}
	


}
