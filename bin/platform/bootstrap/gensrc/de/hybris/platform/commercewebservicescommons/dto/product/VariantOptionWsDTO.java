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
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.StockWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantOptionQualifierWsDTO;
import java.util.Collection;

public  class VariantOptionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VariantOptionWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>VariantOptionWsDTO.stock</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private StockWsDTO stock;

	/** <i>Generated property</i> for <code>VariantOptionWsDTO.url</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String url;

	/** <i>Generated property</i> for <code>VariantOptionWsDTO.priceData</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO priceData;

	/** <i>Generated property</i> for <code>VariantOptionWsDTO.variantOptionQualifiers</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<VariantOptionQualifierWsDTO> variantOptionQualifiers;
	
	public VariantOptionWsDTO()
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
	
		
	
	public void setStock(final StockWsDTO stock)
	{
		this.stock = stock;
	}

		
	
	public StockWsDTO getStock() 
	{
		return stock;
	}
	
		
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

		
	
	public String getUrl() 
	{
		return url;
	}
	
		
	
	public void setPriceData(final PriceWsDTO priceData)
	{
		this.priceData = priceData;
	}

		
	
	public PriceWsDTO getPriceData() 
	{
		return priceData;
	}
	
		
	
	public void setVariantOptionQualifiers(final Collection<VariantOptionQualifierWsDTO> variantOptionQualifiers)
	{
		this.variantOptionQualifiers = variantOptionQualifiers;
	}

		
	
	public Collection<VariantOptionQualifierWsDTO> getVariantOptionQualifiers() 
	{
		return variantOptionQualifiers;
	}
	


}
