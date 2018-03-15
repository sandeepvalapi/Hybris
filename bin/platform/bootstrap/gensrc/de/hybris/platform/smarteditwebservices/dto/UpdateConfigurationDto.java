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
package de.hybris.platform.smarteditwebservices.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Configuration data used for update
 */
@ApiModel(value="configurationData", description="Configuration data used for update")
public  class UpdateConfigurationDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** The uid of the configuration data<br/><br/><i>Generated property</i> for <code>UpdateConfigurationDto.uid</code> property defined at extension <code>smarteditwebservices</code>. */
	@ApiModelProperty(name="uid", value="The uid of the configuration data", required=true) 	
	private String uid;

	/** The configuration data key<br/><br/><i>Generated property</i> for <code>UpdateConfigurationDto.key</code> property defined at extension <code>smarteditwebservices</code>. */
	@ApiModelProperty(name="key", value="The configuration data key", required=true) 	
	private String key;

	/** The configuration data value<br/><br/><i>Generated property</i> for <code>UpdateConfigurationDto.value</code> property defined at extension <code>smarteditwebservices</code>. */
	@ApiModelProperty(name="value", value="The configuration data value", required=true) 	
	private String value;
	
	public UpdateConfigurationDto()
	{
		// default constructor
	}
	
		
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

		
	
	public String getUid() 
	{
		return uid;
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
