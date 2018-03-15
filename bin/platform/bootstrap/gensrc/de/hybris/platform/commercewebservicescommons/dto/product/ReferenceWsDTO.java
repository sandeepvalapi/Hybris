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
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

public  class ReferenceWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ReferenceWsDTO.referenceType</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String referenceType;

	/** <i>Generated property</i> for <code>ReferenceWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>ReferenceWsDTO.quantity</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer quantity;

	/** <i>Generated property</i> for <code>ReferenceWsDTO.target</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private ProductWsDTO target;
	
	public ReferenceWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setReferenceType(final String referenceType)
	{
		this.referenceType = referenceType;
	}

		
	
	public String getReferenceType() 
	{
		return referenceType;
	}
	
		
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

		
	
	public String getDescription() 
	{
		return description;
	}
	
		
	
	public void setQuantity(final Integer quantity)
	{
		this.quantity = quantity;
	}

		
	
	public Integer getQuantity() 
	{
		return quantity;
	}
	
		
	
	public void setTarget(final ProductWsDTO target)
	{
		this.target = target;
	}

		
	
	public ProductWsDTO getTarget() 
	{
		return target;
	}
	


}
