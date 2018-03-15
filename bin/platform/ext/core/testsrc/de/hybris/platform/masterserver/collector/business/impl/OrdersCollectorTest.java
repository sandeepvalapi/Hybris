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
package de.hybris.platform.masterserver.collector.business.impl;

import static org.fest.assertions.Assertions.assertThat;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OrdersCollectorTest extends ServicelayerTransactionalBaseTest
{
	private OrdersCollector collector;

	@Before
	public void setUp() throws Exception
	{
		collector = new OrdersCollector();
	}

	@Test
	public void testCollectStatistics() throws Exception
	{
		// when
		final Map<String, Map<String, Object>> result = collector.collectStatistics();

		// then
		assertThat(result).isNotNull().isNotEmpty();
		assertThat(result.get("orders")).isNotNull().isNotEmpty();
		assertThat(result.get("orders").get("numOrders")).overridingErrorMessage("On test systems there are no orders").isEqualTo
				  (Integer.valueOf(0));
	}

}
