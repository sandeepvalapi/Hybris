/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:39 PM
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
package com.hybris.yprofile.dto;

import java.io.Serializable;
import java.util.List;

public  class OrderBody  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderBody.lineItems</code> property defined at extension <code>yprofilecommons</code>. */
		
	private List<OrderLineItem> lineItems;

	/** <i>Generated property</i> for <code>OrderBody.promotionInfo</code> property defined at extension <code>yprofilecommons</code>. */
		
	private List<Promotion> promotionInfo;

	/** <i>Generated property</i> for <code>OrderBody.paymentInfo</code> property defined at extension <code>yprofilecommons</code>. */
		
	private PaymentInfo paymentInfo;

	/** <i>Generated property</i> for <code>OrderBody.status</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>OrderBody.date</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String date;

	/** <i>Generated property</i> for <code>OrderBody.orderId</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String orderId;

	/** <i>Generated property</i> for <code>OrderBody.cartId</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String cartId;

	/** <i>Generated property</i> for <code>OrderBody.shipmentInfo</code> property defined at extension <code>yprofilecommons</code>. */
		
	private ShipmentInfo shipmentInfo;

	/** <i>Generated property</i> for <code>OrderBody.orderValue</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String orderValue;

	/** <i>Generated property</i> for <code>OrderBody.currency</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String currency;
	
	public OrderBody()
	{
		// default constructor
	}
	
		
	
	public void setLineItems(final List<OrderLineItem> lineItems)
	{
		this.lineItems = lineItems;
	}

		
	
	public List<OrderLineItem> getLineItems() 
	{
		return lineItems;
	}
	
		
	
	public void setPromotionInfo(final List<Promotion> promotionInfo)
	{
		this.promotionInfo = promotionInfo;
	}

		
	
	public List<Promotion> getPromotionInfo() 
	{
		return promotionInfo;
	}
	
		
	
	public void setPaymentInfo(final PaymentInfo paymentInfo)
	{
		this.paymentInfo = paymentInfo;
	}

		
	
	public PaymentInfo getPaymentInfo() 
	{
		return paymentInfo;
	}
	
		
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

		
	
	public String getStatus() 
	{
		return status;
	}
	
		
	
	public void setDate(final String date)
	{
		this.date = date;
	}

		
	
	public String getDate() 
	{
		return date;
	}
	
		
	
	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

		
	
	public String getOrderId() 
	{
		return orderId;
	}
	
		
	
	public void setCartId(final String cartId)
	{
		this.cartId = cartId;
	}

		
	
	public String getCartId() 
	{
		return cartId;
	}
	
		
	
	public void setShipmentInfo(final ShipmentInfo shipmentInfo)
	{
		this.shipmentInfo = shipmentInfo;
	}

		
	
	public ShipmentInfo getShipmentInfo() 
	{
		return shipmentInfo;
	}
	
		
	
	public void setOrderValue(final String orderValue)
	{
		this.orderValue = orderValue;
	}

		
	
	public String getOrderValue() 
	{
		return orderValue;
	}
	
		
	
	public void setCurrency(final String currency)
	{
		this.currency = currency;
	}

		
	
	public String getCurrency() 
	{
		return currency;
	}
	


}
