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
package de.hybris.platform.catalog.jalo.synchronization;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import org.junit.After;
import org.junit.Test;


@IntegrationTest
public class CatalogVersionSyncJobTest extends HybrisJUnit4Test
{
	private final String dbName = Registry.getCurrentTenantNoFallback().getDataSource().getDatabaseName().toLowerCase();
	private final PropertyConfigSwitcher dbRelatedNumberOfThreads = new PropertyConfigSwitcher(
			CatalogConstants.Config.SYNC_WORKERS + "." + dbName);
	private final PropertyConfigSwitcher generalNumberOfThreads = new PropertyConfigSwitcher(CatalogConstants.Config.SYNC_WORKERS);

	@After
	public void tearDown()
	{
		dbRelatedNumberOfThreads.switchBackToDefault();
		generalNumberOfThreads.switchBackToDefault();
	}

	@Test
	public void shouldUseDbSpecificSettingForDefaultThreadsNumber()
	{
		dbRelatedNumberOfThreads.switchToValue("#cores * 2");
		generalNumberOfThreads.switchToValue("#cores * 3");

		final int numberOfThreads = CatalogVersionSyncJob.getDefaultMaxThreads(currentTenant());

		assertThat(numberOfThreads).isEqualTo(numberOfCores() * 2);
	}

	@Test
	public void shouldUseGeneratSettingForDefaultThreadsNumber()
	{
		dbRelatedNumberOfThreads.switchToValue("");
		generalNumberOfThreads.switchToValue("#cores * 3");

		final int numberOfThreads = CatalogVersionSyncJob.getDefaultMaxThreads(currentTenant());

		assertThat(numberOfThreads).isEqualTo(numberOfCores() * 3);
	}

	@Test
	public void shouldUseDefaultSettingForDefaultThreadsNumber()
	{
		dbRelatedNumberOfThreads.switchToValue("");
		generalNumberOfThreads.switchToValue("");

		final int numberOfThreads = CatalogVersionSyncJob.getDefaultMaxThreads(currentTenant());

		assertThat(numberOfThreads).isEqualTo(2);
	}

	private Tenant currentTenant()
	{
		return Registry.getCurrentTenantNoFallback();
	}

	private int numberOfCores()
	{
		return Runtime.getRuntime().availableProcessors();
	}
}
