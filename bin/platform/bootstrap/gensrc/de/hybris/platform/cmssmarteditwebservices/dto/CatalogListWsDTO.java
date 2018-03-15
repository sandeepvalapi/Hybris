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
package de.hybris.platform.cmssmarteditwebservices.dto;

import java.io.Serializable;
import de.hybris.platform.cmssmarteditwebservices.dto.CatalogWsDTO;
import java.util.List;

/**
 * List of catalogs
 */
public  class CatalogListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CatalogListWsDTO.catalogs</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private List<CatalogWsDTO> catalogs;
	
	public CatalogListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setCatalogs(final List<CatalogWsDTO> catalogs)
	{
		this.catalogs = catalogs;
	}

		
	
	public List<CatalogWsDTO> getCatalogs() 
	{
		return catalogs;
	}
	


}
