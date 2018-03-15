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
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductReferenceWsDTO;
import java.util.List;

public  class ProductReferenceListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductReferenceListWsDTO.references</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<ProductReferenceWsDTO> references;
	
	public ProductReferenceListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setReferences(final List<ProductReferenceWsDTO> references)
	{
		this.references = references;
	}

		
	
	public List<ProductReferenceWsDTO> getReferences() 
	{
		return references;
	}
	


}
