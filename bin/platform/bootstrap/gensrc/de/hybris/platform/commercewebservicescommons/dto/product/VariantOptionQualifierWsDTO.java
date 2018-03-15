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
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;

public  class VariantOptionQualifierWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VariantOptionQualifierWsDTO.qualifier</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String qualifier;

	/** <i>Generated property</i> for <code>VariantOptionQualifierWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>VariantOptionQualifierWsDTO.value</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String value;

	/** <i>Generated property</i> for <code>VariantOptionQualifierWsDTO.image</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private ImageWsDTO image;
	
	public VariantOptionQualifierWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

		
	
	public String getQualifier() 
	{
		return qualifier;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setValue(final String value)
	{
		this.value = value;
	}

		
	
	public String getValue() 
	{
		return value;
	}
	
		
	
	public void setImage(final ImageWsDTO image)
	{
		this.image = image;
	}

		
	
	public ImageWsDTO getImage() 
	{
		return image;
	}
	


}
