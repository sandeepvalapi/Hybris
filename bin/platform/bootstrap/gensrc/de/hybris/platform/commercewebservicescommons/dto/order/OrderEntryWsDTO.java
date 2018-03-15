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
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;

public  class OrderEntryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderEntryWsDTO.entryNumber</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer entryNumber;

	/** <i>Generated property</i> for <code>OrderEntryWsDTO.quantity</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Long quantity;

	/** <i>Generated property</i> for <code>OrderEntryWsDTO.basePrice</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO basePrice;

	/** <i>Generated property</i> for <code>OrderEntryWsDTO.totalPrice</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO totalPrice;

	/** <i>Generated property</i> for <code>OrderEntryWsDTO.product</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private ProductWsDTO product;

	/** <i>Generated property</i> for <code>OrderEntryWsDTO.updateable</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean updateable;

	/** <i>Generated property</i> for <code>OrderEntryWsDTO.deliveryMode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private DeliveryModeWsDTO deliveryMode;

	/** <i>Generated property</i> for <code>OrderEntryWsDTO.deliveryPointOfService</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PointOfServiceWsDTO deliveryPointOfService;
	
	public OrderEntryWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setEntryNumber(final Integer entryNumber)
	{
		this.entryNumber = entryNumber;
	}

		
	
	public Integer getEntryNumber() 
	{
		return entryNumber;
	}
	
		
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

		
	
	public Long getQuantity() 
	{
		return quantity;
	}
	
		
	
	public void setBasePrice(final PriceWsDTO basePrice)
	{
		this.basePrice = basePrice;
	}

		
	
	public PriceWsDTO getBasePrice() 
	{
		return basePrice;
	}
	
		
	
	public void setTotalPrice(final PriceWsDTO totalPrice)
	{
		this.totalPrice = totalPrice;
	}

		
	
	public PriceWsDTO getTotalPrice() 
	{
		return totalPrice;
	}
	
		
	
	public void setProduct(final ProductWsDTO product)
	{
		this.product = product;
	}

		
	
	public ProductWsDTO getProduct() 
	{
		return product;
	}
	
		
	
	public void setUpdateable(final Boolean updateable)
	{
		this.updateable = updateable;
	}

		
	
	public Boolean getUpdateable() 
	{
		return updateable;
	}
	
		
	
	public void setDeliveryMode(final DeliveryModeWsDTO deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

		
	
	public DeliveryModeWsDTO getDeliveryMode() 
	{
		return deliveryMode;
	}
	
		
	
	public void setDeliveryPointOfService(final PointOfServiceWsDTO deliveryPointOfService)
	{
		this.deliveryPointOfService = deliveryPointOfService;
	}

		
	
	public PointOfServiceWsDTO getDeliveryPointOfService() 
	{
		return deliveryPointOfService;
	}
	


}
