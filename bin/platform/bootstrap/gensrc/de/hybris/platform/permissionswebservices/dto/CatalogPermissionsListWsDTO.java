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
package de.hybris.platform.permissionswebservices.dto;

import java.io.Serializable;
import de.hybris.platform.permissionswebservices.dto.CatalogPermissionsWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * Catalog permissions list
 */
@ApiModel(value="catalogPermissionsList", description="Catalog permissions list")
public  class CatalogPermissionsListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CatalogPermissionsListWsDTO.permissionsList</code> property defined at extension <code>permissionswebservices</code>. */
	@ApiModelProperty(name="permissionsList") 	
	private List<CatalogPermissionsWsDTO> permissionsList;
	
	public CatalogPermissionsListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setPermissionsList(final List<CatalogPermissionsWsDTO> permissionsList)
	{
		this.permissionsList = permissionsList;
	}

		
	
	public List<CatalogPermissionsWsDTO> getPermissionsList() 
	{
		return permissionsList;
	}
	


}
