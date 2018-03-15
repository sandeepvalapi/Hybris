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
package de.hybris.platform.cmswebservices.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * Category DTO
 */
public  class CategoryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CategoryWsDTO.code</code> property defined at extension <code>cmswebservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>CategoryWsDTO.name</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String,String> name;

	/** <i>Generated property</i> for <code>CategoryWsDTO.description</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String,String> description;

	/** <i>Generated property</i> for <code>CategoryWsDTO.thumbnailMediaCode</code> property defined at extension <code>cmswebservices</code>. */
		
	private String thumbnailMediaCode;

	/** <i>Generated property</i> for <code>CategoryWsDTO.catalogId</code> property defined at extension <code>cmswebservices</code>. */
		
	private String catalogId;

	/** <i>Generated property</i> for <code>CategoryWsDTO.catalogVersion</code> property defined at extension <code>cmswebservices</code>. */
		
	private String catalogVersion;
	
	public CategoryWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setCode(final String code)
	{
		this.code = code;
	}

		
	
	public String getCode() 
	{
		return code;
	}
	
		
	
	public void setName(final Map<String,String> name)
	{
		this.name = name;
	}

		
	
	public Map<String,String> getName() 
	{
		return name;
	}
	
		
	
	public void setDescription(final Map<String,String> description)
	{
		this.description = description;
	}

		
	
	public Map<String,String> getDescription() 
	{
		return description;
	}
	
		
	
	public void setThumbnailMediaCode(final String thumbnailMediaCode)
	{
		this.thumbnailMediaCode = thumbnailMediaCode;
	}

		
	
	public String getThumbnailMediaCode() 
	{
		return thumbnailMediaCode;
	}
	
		
	
	public void setCatalogId(final String catalogId)
	{
		this.catalogId = catalogId;
	}

		
	
	public String getCatalogId() 
	{
		return catalogId;
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
