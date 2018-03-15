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
package de.hybris.platform.smartedit.dto;

import java.io.Serializable;

public  class ConfigurationData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConfigurationData.key</code> property defined at extension <code>smartedit</code>. */
		
	private String key;

	/** <i>Generated property</i> for <code>ConfigurationData.value</code> property defined at extension <code>smartedit</code>. */
		
	private String value;
	
	public ConfigurationData()
	{
		// default constructor
	}
	
		
	
	public void setKey(final String key)
	{
		this.key = key;
	}

		
	
	public String getKey() 
	{
		return key;
	}
	
		
	
	public void setValue(final String value)
	{
		this.value = value;
	}

		
	
	public String getValue() 
	{
		return value;
	}
	


}
