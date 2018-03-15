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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CatalogsCollectorTest extends ServicelayerTransactionalBaseTest
{
	private CatalogsCollector collector;

	@Before
	public void setUp() throws Exception
	{
		collector = new CatalogsCollector();
	}

	@Test
	public void testCollectStatistics() throws Exception
	{
		// when
		final Map<String,Map<String,Object>> result = collector.collectStatistics();

		// then
		assertThat(result).isNotNull().isNotEmpty();
		assertThat(result.get("catalog")).isNotNull().isNotEmpty();
		assertThat(result.get("catalog").get("catalogSizes")).isInstanceOf(HashMap.class);
		assertThat((Map) result.get("catalog").get("catalogSizes")).hasSize(0);
	}
}
