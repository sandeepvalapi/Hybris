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
package de.hybris.platform.smarteditwebservices.dto;

import java.io.Serializable;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;

/**
 * List of configurations
 */
@ApiModel(value="ConfigurationDataListWsDto", description="List of configurations")
public  class ConfigurationDataListWsDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** The list of configuration data<br/><br/><i>Generated property</i> for <code>ConfigurationDataListWsDto.configurations</code> property defined at extension <code>smarteditwebservices</code>. */
	@ApiModelProperty(name="configurations", value="The list of configuration data", required=true) 	
	private Collection<ConfigurationData> configurations;
	
	public ConfigurationDataListWsDto()
	{
		// default constructor
	}
	
		
	
	public void setConfigurations(final Collection<ConfigurationData> configurations)
	{
		this.configurations = configurations;
	}

		
	
	public Collection<ConfigurationData> getConfigurations() 
	{
		return configurations;
	}
	


}
