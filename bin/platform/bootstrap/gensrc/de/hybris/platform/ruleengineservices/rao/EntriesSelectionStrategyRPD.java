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
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.util.List;

public  class EntriesSelectionStrategyRPD  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>EntriesSelectionStrategyRPD.orderEntries</code> property defined at extension <code>ruleengineservices</code>. */
		
	private List<OrderEntryRAO> orderEntries;

	/** <i>Generated property</i> for <code>EntriesSelectionStrategyRPD.selectionStrategy</code> property defined at extension <code>ruleengineservices</code>. */
		
	private OrderEntrySelectionStrategy selectionStrategy;

	/** <i>Generated property</i> for <code>EntriesSelectionStrategyRPD.quantity</code> property defined at extension <code>ruleengineservices</code>. */
		
	private int quantity;

	/** <i>Generated property</i> for <code>EntriesSelectionStrategyRPD.targetOfAction</code> property defined at extension <code>ruleengineservices</code>. */
		
	private boolean targetOfAction;
	
	public EntriesSelectionStrategyRPD()
	{
		// default constructor
	}
	
		
	
	public void setOrderEntries(final List<OrderEntryRAO> orderEntries)
	{
		this.orderEntries = orderEntries;
	}

		
	
	public List<OrderEntryRAO> getOrderEntries() 
	{
		return orderEntries;
	}
	
		
	
	public void setSelectionStrategy(final OrderEntrySelectionStrategy selectionStrategy)
	{
		this.selectionStrategy = selectionStrategy;
	}

		
	
	public OrderEntrySelectionStrategy getSelectionStrategy() 
	{
		return selectionStrategy;
	}
	
		
	
	public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}

		
	
	public int getQuantity() 
	{
		return quantity;
	}
	
		
	
	public void setTargetOfAction(final boolean targetOfAction)
	{
		this.targetOfAction = targetOfAction;
	}

		
	
	public boolean isTargetOfAction() 
	{
		return targetOfAction;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final EntriesSelectionStrategyRPD other = (EntriesSelectionStrategyRPD) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getOrderEntries(), other.getOrderEntries()) 
			.append(getSelectionStrategy(), other.getSelectionStrategy()) 
			.append(getQuantity(), other.getQuantity()) 
			.append(isTargetOfAction(), other.isTargetOfAction())
			.isEquals();
		} 
		catch (ClassCastException c)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return new org.apache.commons.lang.builder.HashCodeBuilder()
		.append(getOrderEntries()) 
		.append(getSelectionStrategy()) 
		.append(getQuantity()) 
		.append(isTargetOfAction())
		.toHashCode();
	}

}
