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
package de.hybris.platform.persistence.links.jdbc;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@IntegrationTest
public class JdbcLinkOperationFactoryIntegrationTest extends ServicelayerBaseTest
{
	private final PropertyConfigSwitcher optimisticLockingSwitch = new PropertyConfigSwitcher("link.jdbc.mode.enabled");

	@Mock
	private JdbcLinkOperationExecutor executor;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown()
	{
		optimisticLockingSwitch.switchBackToDefault();
	}

	@Test
	public void shoulbBeEnabledByDefault()
	{
		final JdbcLinkOperationFactory factory = new JdbcLinkOperationFactory(executor);

		optimisticLockingSwitch.switchToValue(null);

		assertThat(factory.isEnabled()).isTrue();
	}

	@Test
	public void shoulbBeEnabledWhenConfigFlagIsSetToTrue()
	{
		final JdbcLinkOperationFactory factory = new JdbcLinkOperationFactory(executor);

		optimisticLockingSwitch.switchToValue(Boolean.TRUE.toString());

		assertThat(factory.isEnabled()).isTrue();
	}

	@Test
	public void shoulbBeDisabledWhenConfigFlagIsSetToFalse()
	{
		final JdbcLinkOperationFactory factory = new JdbcLinkOperationFactory(executor);

		optimisticLockingSwitch.switchToValue(Boolean.FALSE.toString());

		assertThat(factory.isEnabled()).isFalse();
	}
}
