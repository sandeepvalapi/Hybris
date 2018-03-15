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
package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.order.AbstractOrderWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.PrincipalWsDTO;
import java.util.Date;
import java.util.List;

public  class CartWsDTO extends AbstractOrderWsDTO 
{

 

	/** <i>Generated property</i> for <code>CartWsDTO.totalUnitCount</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer totalUnitCount;

	/** <i>Generated property</i> for <code>CartWsDTO.potentialOrderPromotions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<PromotionResultWsDTO> potentialOrderPromotions;

	/** <i>Generated property</i> for <code>CartWsDTO.potentialProductPromotions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<PromotionResultWsDTO> potentialProductPromotions;

	/** <i>Generated property</i> for <code>CartWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CartWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>CartWsDTO.expirationTime</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Date expirationTime;

	/** <i>Generated property</i> for <code>CartWsDTO.saveTime</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Date saveTime;

	/** <i>Generated property</i> for <code>CartWsDTO.savedBy</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PrincipalWsDTO savedBy;
	
	public CartWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setTotalUnitCount(final Integer totalUnitCount)
	{
		this.totalUnitCount = totalUnitCount;
	}

		
	
	public Integer getTotalUnitCount() 
	{
		return totalUnitCount;
	}
	
		
	
	public void setPotentialOrderPromotions(final List<PromotionResultWsDTO> potentialOrderPromotions)
	{
		this.potentialOrderPromotions = potentialOrderPromotions;
	}

		
	
	public List<PromotionResultWsDTO> getPotentialOrderPromotions() 
	{
		return potentialOrderPromotions;
	}
	
		
	
	public void setPotentialProductPromotions(final List<PromotionResultWsDTO> potentialProductPromotions)
	{
		this.potentialProductPromotions = potentialProductPromotions;
	}

		
	
	public List<PromotionResultWsDTO> getPotentialProductPromotions() 
	{
		return potentialProductPromotions;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

		
	
	public String getDescription() 
	{
		return description;
	}
	
		
	
	public void setExpirationTime(final Date expirationTime)
	{
		this.expirationTime = expirationTime;
	}

		
	
	public Date getExpirationTime() 
	{
		return expirationTime;
	}
	
		
	
	public void setSaveTime(final Date saveTime)
	{
		this.saveTime = saveTime;
	}

		
	
	public Date getSaveTime() 
	{
		return saveTime;
	}
	
		
	
	public void setSavedBy(final PrincipalWsDTO savedBy)
	{
		this.savedBy = savedBy;
	}

		
	
	public PrincipalWsDTO getSavedBy() 
	{
		return savedBy;
	}
	


}
