/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.facades.process.email.context;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;



/**
 * Velocity context for email about partially order cancellation.
 */
public class OrderPartiallyCanceledEmailContext extends OrderPartiallyModifiedEmailContext
{

	public List<OrderEntryData> getCanceledEntries()
	{
		return super.getModifiedEntries();
	}
}
