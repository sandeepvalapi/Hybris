/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.orderselfserviceaddon.forms;


import java.util.Map;


/**
 * A Form object that is presented on the cancel and confirm cancel page on the store front
 */

public class OrderEntryCancelForm
{

	private Map<Integer, Integer> cancelEntryQuantityMap;

	public Map<Integer, Integer> getCancelEntryQuantityMap()
	{
		return cancelEntryQuantityMap;
	}

	public void setCancelEntryQuantityMap(final Map<Integer, Integer> cancelEntryQuantityMap)
	{
		this.cancelEntryQuantityMap = cancelEntryQuantityMap;
	}
}
