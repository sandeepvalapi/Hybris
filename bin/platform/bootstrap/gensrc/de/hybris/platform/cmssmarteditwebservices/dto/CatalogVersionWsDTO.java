/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
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
import de.hybris.platform.cmsfacades.data.DisplayConditionData;
import java.util.List;

public  class CatalogVersionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CatalogVersionWsDTO.active</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private Boolean active;

	/** <i>Generated property</i> for <code>CatalogVersionWsDTO.pageDisplayConditions</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private List<DisplayConditionData> pageDisplayConditions;

	/** <i>Generated property</i> for <code>CatalogVersionWsDTO.thumbnailUrl</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String thumbnailUrl;

	/** <i>Generated property</i> for <code>CatalogVersionWsDTO.version</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String version;

	/** <i>Generated property</i> for <code>CatalogVersionWsDTO.uuid</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String uuid;
	
	public CatalogVersionWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setActive(final Boolean active)
	{
		this.active = active;
	}

		
	
	public Boolean getActive() 
	{
		return active;
	}
	
		
	
	public void setPageDisplayConditions(final List<DisplayConditionData> pageDisplayConditions)
	{
		this.pageDisplayConditions = pageDisplayConditions;
	}

		
	
	public List<DisplayConditionData> getPageDisplayConditions() 
	{
		return pageDisplayConditions;
	}
	
		
	
	public void setThumbnailUrl(final String thumbnailUrl)
	{
		this.thumbnailUrl = thumbnailUrl;
	}

		
	
	public String getThumbnailUrl() 
	{
		return thumbnailUrl;
	}
	
		
	
	public void setVersion(final String version)
	{
		this.version = version;
	}

		
	
	public String getVersion() 
	{
		return version;
	}
	
		
	
	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

		
	
	public String getUuid() 
	{
		return uuid;
	}
	


}
