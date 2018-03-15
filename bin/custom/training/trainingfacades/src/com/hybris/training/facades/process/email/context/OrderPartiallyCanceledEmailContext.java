/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
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
