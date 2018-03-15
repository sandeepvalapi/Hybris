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
package de.hybris.platform.permissionswebservices.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;

/**
 * Permissions for principal
 */
@ApiModel(value="permissions", description="Permissions for principal")
public  class PermissionsWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** Principal identifier<br/><br/><i>Generated property</i> for <code>PermissionsWsDTO.id</code> property defined at extension <code>permissionswebservices</code>. */
	@ApiModelProperty(name="id", value="Principal identifier", required=true) 	
	private String id;

	/** Permissions map.<br/><br/><i>Generated property</i> for <code>PermissionsWsDTO.permissions</code> property defined at extension <code>permissionswebservices</code>. */
	@ApiModelProperty(name="permissions", value="Permissions map.") 	
	private Map<String, String> permissions;
	
	public PermissionsWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setId(final String id)
	{
		this.id = id;
	}

		
	
	public String getId() 
	{
		return id;
	}
	
		
	
	public void setPermissions(final Map<String, String> permissions)
	{
		this.permissions = permissions;
	}

		
	
	public Map<String, String> getPermissions() 
	{
		return permissions;
	}
	


}
