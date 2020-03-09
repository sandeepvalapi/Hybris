/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;


public class AddToCartOrderForm
{

	private List<OrderEntryData> cartEntries;

	/**
	 * @return Return the cartEntries.
	 */
	public List<OrderEntryData> getCartEntries()
	{
		return cartEntries;
	}

	/**
	 * @param cartEntries
	 *           The cartEntries to set.
	 */
	public void setCartEntries(final List<OrderEntryData> cartEntries)
	{
		this.cartEntries = cartEntries;
	}
}
