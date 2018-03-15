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
package de.hybris.platform.masterserver.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.masterserver.StatisticsGatewayFactory;

import org.junit.Before;
import org.junit.Test;

import com.hybris.statistics.StatisticsGateway;


@IntegrationTest
public class DefaultStatisticsGatewayFactoryTest
{

	private StatisticsGatewayFactory factory;

	@Before
	public void setUp() throws Exception
	{
		factory = DefaultStatisticsGatewayFactory.getInstance();
		assertThat(factory).isNotNull();
	}

	@Test
	public void shouldGetOrCreateStatisticsGatewayObject()
	{
		// given
		final StatisticsGateway statisticsGateway1 = factory.getOrCreateStatisticsGateway();
		final StatisticsGateway statisticsGateway2 = factory.getOrCreateStatisticsGateway();

		// then
		assertThat(statisticsGateway1).isNotNull();
		assertThat(statisticsGateway2).isNotNull();
		assertThat(statisticsGateway1).isSameAs(statisticsGateway2);
	}

}
