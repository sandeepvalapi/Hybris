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
package de.hybris.platform.commercewebservicescommons.dto.queues;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.queues.ProductExpressUpdateElementWsDTO;
import java.util.List;

public  class ProductExpressUpdateElementListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductExpressUpdateElementListWsDTO.productExpressUpdateElements</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<ProductExpressUpdateElementWsDTO> productExpressUpdateElements;
	
	public ProductExpressUpdateElementListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setProductExpressUpdateElements(final List<ProductExpressUpdateElementWsDTO> productExpressUpdateElements)
	{
		this.productExpressUpdateElements = productExpressUpdateElements;
	}

		
	
	public List<ProductExpressUpdateElementWsDTO> getProductExpressUpdateElements() 
	{
		return productExpressUpdateElements;
	}
	


}
