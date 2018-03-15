/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
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
import java.util.Map;

public  class ProductWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductWsDTO.uid</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>ProductWsDTO.code</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>ProductWsDTO.name</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private Map<String,String> name;

	/** <i>Generated property</i> for <code>ProductWsDTO.description</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private Map<String,String> description;

	/** <i>Generated property</i> for <code>ProductWsDTO.thumbnail</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private MediaWsDTO thumbnail;

	/** <i>Generated property</i> for <code>ProductWsDTO.catalogId</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String catalogId;

	/** <i>Generated property</i> for <code>ProductWsDTO.catalogVersion</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String catalogVersion;
	
	public ProductWsDTO()
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
	
		
	
	public void setThumbnail(final MediaWsDTO thumbnail)
	{
		this.thumbnail = thumbnail;
	}

		
	
	public MediaWsDTO getThumbnail() 
	{
		return thumbnail;
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
