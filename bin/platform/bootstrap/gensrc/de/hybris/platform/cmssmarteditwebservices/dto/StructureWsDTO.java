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
import de.hybris.platform.cmssmarteditwebservices.dto.StructureAttributeWsDTO;
import java.util.List;

public  class StructureWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>StructureWsDTO.code</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>StructureWsDTO.category</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String category;

	/** <i>Generated property</i> for <code>StructureWsDTO.name</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>StructureWsDTO.i18nKey</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String i18nKey;

	/** <i>Generated property</i> for <code>StructureWsDTO.type</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>StructureWsDTO.attributes</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private List<StructureAttributeWsDTO> attributes;
	
	public StructureWsDTO()
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
	
		
	
	public void setCategory(final String category)
	{
		this.category = category;
	}

		
	
	public String getCategory() 
	{
		return category;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setI18nKey(final String i18nKey)
	{
		this.i18nKey = i18nKey;
	}

		
	
	public String getI18nKey() 
	{
		return i18nKey;
	}
	
		
	
	public void setType(final String type)
	{
		this.type = type;
	}

		
	
	public String getType() 
	{
		return type;
	}
	
		
	
	public void setAttributes(final List<StructureAttributeWsDTO> attributes)
	{
		this.attributes = attributes;
	}

		
	
	public List<StructureAttributeWsDTO> getAttributes() 
	{
		return attributes;
	}
	


}
