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
import de.hybris.platform.commercewebservicescommons.dto.product.VariantCategoryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantMatrixElementWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantOptionWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantValueCategoryWsDTO;
import java.util.List;

public  class VariantMatrixElementWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VariantMatrixElementWsDTO.variantValueCategory</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private VariantValueCategoryWsDTO variantValueCategory;

	/** <i>Generated property</i> for <code>VariantMatrixElementWsDTO.parentVariantCategory</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private VariantCategoryWsDTO parentVariantCategory;

	/** <i>Generated property</i> for <code>VariantMatrixElementWsDTO.variantOption</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private VariantOptionWsDTO variantOption;

	/** <i>Generated property</i> for <code>VariantMatrixElementWsDTO.elements</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<VariantMatrixElementWsDTO> elements;

	/** <i>Generated property</i> for <code>VariantMatrixElementWsDTO.isLeaf</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean isLeaf;
	
	public VariantMatrixElementWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setVariantValueCategory(final VariantValueCategoryWsDTO variantValueCategory)
	{
		this.variantValueCategory = variantValueCategory;
	}

		
	
	public VariantValueCategoryWsDTO getVariantValueCategory() 
	{
		return variantValueCategory;
	}
	
		
	
	public void setParentVariantCategory(final VariantCategoryWsDTO parentVariantCategory)
	{
		this.parentVariantCategory = parentVariantCategory;
	}

		
	
	public VariantCategoryWsDTO getParentVariantCategory() 
	{
		return parentVariantCategory;
	}
	
		
	
	public void setVariantOption(final VariantOptionWsDTO variantOption)
	{
		this.variantOption = variantOption;
	}

		
	
	public VariantOptionWsDTO getVariantOption() 
	{
		return variantOption;
	}
	
		
	
	public void setElements(final List<VariantMatrixElementWsDTO> elements)
	{
		this.elements = elements;
	}

		
	
	public List<VariantMatrixElementWsDTO> getElements() 
	{
		return elements;
	}
	
		
	
	public void setIsLeaf(final Boolean isLeaf)
	{
		this.isLeaf = isLeaf;
	}

		
	
	public Boolean getIsLeaf() 
	{
		return isLeaf;
	}
	


}
