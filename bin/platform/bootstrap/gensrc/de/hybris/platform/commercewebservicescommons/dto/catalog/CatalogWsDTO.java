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
package de.hybris.platform.commercewebservicescommons.dto.catalog;

import de.hybris.platform.commercewebservicescommons.dto.catalog.AbstractCatalogItemWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.catalog.CatalogVersionWsDTO;
import java.util.Collection;

public  class CatalogWsDTO extends AbstractCatalogItemWsDTO 
{

 

	/** <i>Generated property</i> for <code>CatalogWsDTO.catalogVersions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<CatalogVersionWsDTO> catalogVersions;
	
	public CatalogWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setCatalogVersions(final Collection<CatalogVersionWsDTO> catalogVersions)
	{
		this.catalogVersions = catalogVersions;
	}

		
	
	public Collection<CatalogVersionWsDTO> getCatalogVersions() 
	{
		return catalogVersions;
	}
	


}
