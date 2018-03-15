/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.order.events;

import java.io.Serializable;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import de.hybris.platform.core.model.order.OrderModel;

public class SubmitOrderEvent  extends AbstractEvent {

	/** <i>Generated property</i> for <code>SubmitOrderEvent.order</code> property defined at extension <code>platformservices</code>. */
	private OrderModel order;
	
	public SubmitOrderEvent()
	{
		super();
	}

	/**
	 * Attention: for backward compatibility this constructor invokes <pre>setOrder(source)</pre> in case the source object is a OrderModel!  
	 */
	public SubmitOrderEvent(final Serializable source)
	{
		super(source);
		
		// compatibility!
		if( source instanceof OrderModel )
		{
			setOrder((OrderModel)source);
		}
	}
	
		
	public void setOrder(final OrderModel order)
	{
		this.order = order;
	}
	
			
	public OrderModel getOrder() 
	{
		return order;
	}
		
}