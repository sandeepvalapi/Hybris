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
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionRestrictionWsDTO;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public  class PromotionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PromotionWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>PromotionWsDTO.title</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String title;

	/** <i>Generated property</i> for <code>PromotionWsDTO.promotionType</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String promotionType;

	/** <i>Generated property</i> for <code>PromotionWsDTO.startDate</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Date startDate;

	/** <i>Generated property</i> for <code>PromotionWsDTO.endDate</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Date endDate;

	/** <i>Generated property</i> for <code>PromotionWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>PromotionWsDTO.couldFireMessages</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<String> couldFireMessages;

	/** <i>Generated property</i> for <code>PromotionWsDTO.firedMessages</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<String> firedMessages;

	/** <i>Generated property</i> for <code>PromotionWsDTO.productBanner</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private ImageWsDTO productBanner;

	/** <i>Generated property</i> for <code>PromotionWsDTO.enabled</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean enabled;

	/** <i>Generated property</i> for <code>PromotionWsDTO.priority</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer priority;

	/** <i>Generated property</i> for <code>PromotionWsDTO.promotionGroup</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String promotionGroup;

	/** <i>Generated property</i> for <code>PromotionWsDTO.restrictions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<PromotionRestrictionWsDTO> restrictions;
	
	public PromotionWsDTO()
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
	
		
	
	public void setTitle(final String title)
	{
		this.title = title;
	}

		
	
	public String getTitle() 
	{
		return title;
	}
	
		
	
	public void setPromotionType(final String promotionType)
	{
		this.promotionType = promotionType;
	}

		
	
	public String getPromotionType() 
	{
		return promotionType;
	}
	
		
	
	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

		
	
	public Date getStartDate() 
	{
		return startDate;
	}
	
		
	
	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

		
	
	public Date getEndDate() 
	{
		return endDate;
	}
	
		
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

		
	
	public String getDescription() 
	{
		return description;
	}
	
		
	
	public void setCouldFireMessages(final List<String> couldFireMessages)
	{
		this.couldFireMessages = couldFireMessages;
	}

		
	
	public List<String> getCouldFireMessages() 
	{
		return couldFireMessages;
	}
	
		
	
	public void setFiredMessages(final List<String> firedMessages)
	{
		this.firedMessages = firedMessages;
	}

		
	
	public List<String> getFiredMessages() 
	{
		return firedMessages;
	}
	
		
	
	public void setProductBanner(final ImageWsDTO productBanner)
	{
		this.productBanner = productBanner;
	}

		
	
	public ImageWsDTO getProductBanner() 
	{
		return productBanner;
	}
	
		
	
	public void setEnabled(final Boolean enabled)
	{
		this.enabled = enabled;
	}

		
	
	public Boolean getEnabled() 
	{
		return enabled;
	}
	
		
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

		
	
	public Integer getPriority() 
	{
		return priority;
	}
	
		
	
	public void setPromotionGroup(final String promotionGroup)
	{
		this.promotionGroup = promotionGroup;
	}

		
	
	public String getPromotionGroup() 
	{
		return promotionGroup;
	}
	
		
	
	public void setRestrictions(final Collection<PromotionRestrictionWsDTO> restrictions)
	{
		this.restrictions = restrictions;
	}

		
	
	public Collection<PromotionRestrictionWsDTO> getRestrictions() 
	{
		return restrictions;
	}
	


}
