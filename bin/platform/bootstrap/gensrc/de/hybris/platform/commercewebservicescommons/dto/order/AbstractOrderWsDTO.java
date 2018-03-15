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

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryOrderEntryGroupWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PickupOrderEntryGroupWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.PrincipalWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.voucher.VoucherWsDTO;
import java.util.List;

public  class AbstractOrderWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.net</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean net;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.totalPriceWithTax</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO totalPriceWithTax;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.totalPrice</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO totalPrice;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.totalTax</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO totalTax;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.subTotal</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO subTotal;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.deliveryCost</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO deliveryCost;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.entries</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<OrderEntryWsDTO> entries;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.totalItems</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer totalItems;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.deliveryMode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private DeliveryModeWsDTO deliveryMode;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.deliveryAddress</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private AddressWsDTO deliveryAddress;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.paymentInfo</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PaymentDetailsWsDTO paymentInfo;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.appliedOrderPromotions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<PromotionResultWsDTO> appliedOrderPromotions;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.appliedProductPromotions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<PromotionResultWsDTO> appliedProductPromotions;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.productDiscounts</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO productDiscounts;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.orderDiscounts</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO orderDiscounts;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.totalDiscounts</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO totalDiscounts;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.site</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String site;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.store</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String store;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.guid</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String guid;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.calculated</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean calculated;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.appliedVouchers</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<VoucherWsDTO> appliedVouchers;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.user</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PrincipalWsDTO user;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.pickupOrderGroups</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<PickupOrderEntryGroupWsDTO> pickupOrderGroups;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.deliveryOrderGroups</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<DeliveryOrderEntryGroupWsDTO> deliveryOrderGroups;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.pickupItemsQuantity</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Long pickupItemsQuantity;

	/** <i>Generated property</i> for <code>AbstractOrderWsDTO.deliveryItemsQuantity</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Long deliveryItemsQuantity;
	
	public AbstractOrderWsDTO()
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
	
		
	
	public void setNet(final Boolean net)
	{
		this.net = net;
	}

		
	
	public Boolean getNet() 
	{
		return net;
	}
	
		
	
	public void setTotalPriceWithTax(final PriceWsDTO totalPriceWithTax)
	{
		this.totalPriceWithTax = totalPriceWithTax;
	}

		
	
	public PriceWsDTO getTotalPriceWithTax() 
	{
		return totalPriceWithTax;
	}
	
		
	
	public void setTotalPrice(final PriceWsDTO totalPrice)
	{
		this.totalPrice = totalPrice;
	}

		
	
	public PriceWsDTO getTotalPrice() 
	{
		return totalPrice;
	}
	
		
	
	public void setTotalTax(final PriceWsDTO totalTax)
	{
		this.totalTax = totalTax;
	}

		
	
	public PriceWsDTO getTotalTax() 
	{
		return totalTax;
	}
	
		
	
	public void setSubTotal(final PriceWsDTO subTotal)
	{
		this.subTotal = subTotal;
	}

		
	
	public PriceWsDTO getSubTotal() 
	{
		return subTotal;
	}
	
		
	
	public void setDeliveryCost(final PriceWsDTO deliveryCost)
	{
		this.deliveryCost = deliveryCost;
	}

		
	
	public PriceWsDTO getDeliveryCost() 
	{
		return deliveryCost;
	}
	
		
	
	public void setEntries(final List<OrderEntryWsDTO> entries)
	{
		this.entries = entries;
	}

		
	
	public List<OrderEntryWsDTO> getEntries() 
	{
		return entries;
	}
	
		
	
	public void setTotalItems(final Integer totalItems)
	{
		this.totalItems = totalItems;
	}

		
	
	public Integer getTotalItems() 
	{
		return totalItems;
	}
	
		
	
	public void setDeliveryMode(final DeliveryModeWsDTO deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

		
	
	public DeliveryModeWsDTO getDeliveryMode() 
	{
		return deliveryMode;
	}
	
		
	
	public void setDeliveryAddress(final AddressWsDTO deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
	}

		
	
	public AddressWsDTO getDeliveryAddress() 
	{
		return deliveryAddress;
	}
	
		
	
	public void setPaymentInfo(final PaymentDetailsWsDTO paymentInfo)
	{
		this.paymentInfo = paymentInfo;
	}

		
	
	public PaymentDetailsWsDTO getPaymentInfo() 
	{
		return paymentInfo;
	}
	
		
	
	public void setAppliedOrderPromotions(final List<PromotionResultWsDTO> appliedOrderPromotions)
	{
		this.appliedOrderPromotions = appliedOrderPromotions;
	}

		
	
	public List<PromotionResultWsDTO> getAppliedOrderPromotions() 
	{
		return appliedOrderPromotions;
	}
	
		
	
	public void setAppliedProductPromotions(final List<PromotionResultWsDTO> appliedProductPromotions)
	{
		this.appliedProductPromotions = appliedProductPromotions;
	}

		
	
	public List<PromotionResultWsDTO> getAppliedProductPromotions() 
	{
		return appliedProductPromotions;
	}
	
		
	
	public void setProductDiscounts(final PriceWsDTO productDiscounts)
	{
		this.productDiscounts = productDiscounts;
	}

		
	
	public PriceWsDTO getProductDiscounts() 
	{
		return productDiscounts;
	}
	
		
	
	public void setOrderDiscounts(final PriceWsDTO orderDiscounts)
	{
		this.orderDiscounts = orderDiscounts;
	}

		
	
	public PriceWsDTO getOrderDiscounts() 
	{
		return orderDiscounts;
	}
	
		
	
	public void setTotalDiscounts(final PriceWsDTO totalDiscounts)
	{
		this.totalDiscounts = totalDiscounts;
	}

		
	
	public PriceWsDTO getTotalDiscounts() 
	{
		return totalDiscounts;
	}
	
		
	
	public void setSite(final String site)
	{
		this.site = site;
	}

		
	
	public String getSite() 
	{
		return site;
	}
	
		
	
	public void setStore(final String store)
	{
		this.store = store;
	}

		
	
	public String getStore() 
	{
		return store;
	}
	
		
	
	public void setGuid(final String guid)
	{
		this.guid = guid;
	}

		
	
	public String getGuid() 
	{
		return guid;
	}
	
		
	
	public void setCalculated(final Boolean calculated)
	{
		this.calculated = calculated;
	}

		
	
	public Boolean getCalculated() 
	{
		return calculated;
	}
	
		
	
	public void setAppliedVouchers(final List<VoucherWsDTO> appliedVouchers)
	{
		this.appliedVouchers = appliedVouchers;
	}

		
	
	public List<VoucherWsDTO> getAppliedVouchers() 
	{
		return appliedVouchers;
	}
	
		
	
	public void setUser(final PrincipalWsDTO user)
	{
		this.user = user;
	}

		
	
	public PrincipalWsDTO getUser() 
	{
		return user;
	}
	
		
	
	public void setPickupOrderGroups(final List<PickupOrderEntryGroupWsDTO> pickupOrderGroups)
	{
		this.pickupOrderGroups = pickupOrderGroups;
	}

		
	
	public List<PickupOrderEntryGroupWsDTO> getPickupOrderGroups() 
	{
		return pickupOrderGroups;
	}
	
		
	
	public void setDeliveryOrderGroups(final List<DeliveryOrderEntryGroupWsDTO> deliveryOrderGroups)
	{
		this.deliveryOrderGroups = deliveryOrderGroups;
	}

		
	
	public List<DeliveryOrderEntryGroupWsDTO> getDeliveryOrderGroups() 
	{
		return deliveryOrderGroups;
	}
	
		
	
	public void setPickupItemsQuantity(final Long pickupItemsQuantity)
	{
		this.pickupItemsQuantity = pickupItemsQuantity;
	}

		
	
	public Long getPickupItemsQuantity() 
	{
		return pickupItemsQuantity;
	}
	
		
	
	public void setDeliveryItemsQuantity(final Long deliveryItemsQuantity)
	{
		this.deliveryItemsQuantity = deliveryItemsQuantity;
	}

		
	
	public Long getDeliveryItemsQuantity() 
	{
		return deliveryItemsQuantity;
	}
	


}
