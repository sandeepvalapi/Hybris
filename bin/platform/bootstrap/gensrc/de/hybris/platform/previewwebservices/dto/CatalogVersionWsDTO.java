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
package de.hybris.platform.previewwebservices.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="catalogVersion")
public  class CatalogVersionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** Catalog ID<br/><br/><i>Generated property</i> for <code>CatalogVersionWsDTO.catalog</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="catalog", value="Catalog ID", required=true) 	
	private String catalog;

	/** Catalog version<br/><br/><i>Generated property</i> for <code>CatalogVersionWsDTO.catalogVersion</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="catalogVersion", value="Catalog version", required=true) 	
	private String catalogVersion;
	
	public CatalogVersionWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setCatalog(final String catalog)
	{
		this.catalog = catalog;
	}

		
	
	public String getCatalog() 
	{
		return catalog;
	}
	
		
	
	public void setCatalogVersion(final String catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}

		
	
	public String getCatalogVersion() 
	{
		return catalogVersion;
	}
	


}
