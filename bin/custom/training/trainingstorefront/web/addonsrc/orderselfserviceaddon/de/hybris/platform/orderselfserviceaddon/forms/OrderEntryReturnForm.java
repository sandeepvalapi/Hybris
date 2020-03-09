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
 * A Form object that is presented on the return and confirm return page on the store front
 */
public class OrderEntryReturnForm
{

	private Map<Integer, Long> returnEntryQuantityMap;

	public Map<Integer, Long> getReturnEntryQuantityMap()
	{
		return returnEntryQuantityMap;
	}

	public void setReturnEntryQuantityMap(final Map<Integer, Long> returnEntryQuantityMap)
	{
		this.returnEntryQuantityMap = returnEntryQuantityMap;
	}
}
