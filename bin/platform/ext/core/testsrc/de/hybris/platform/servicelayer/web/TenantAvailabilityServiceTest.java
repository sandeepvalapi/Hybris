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
package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.system.InitializationLockDao;
import de.hybris.platform.core.system.InitializationLockInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TenantAvailabilityServiceTest
{

	private static final String FOO_TENANT = "foo";
	private static final String BAR_TENANT = "bar";

	@Mock
	InitializationLockDao lockDao;


	private InitializationLockInfo createLockInfo(final String tenant, final boolean locked)
	{
		return new InitializationLockInfo(tenant, 1, new Date(), locked, Initialization.SYSTEM_INITIALIZATION_OP_NAME, 1L);
	}

	private TenantAvailabilityService createInitDetectionServiceSpy(final InitializationLockDao initLockDao) {
		final TenantAvailabilityService tenantAvailabilityService = spy(new TenantAvailabilityService(initLockDao));
		when(tenantAvailabilityService.isMasterTenantRunning()).thenReturn(true);
		when(tenantAvailabilityService.isSystemInitialized()).thenReturn(true);
		return tenantAvailabilityService;
	}

	@Test
	public void tenantIsAvailableIfNotLocked()
	{
		when(lockDao.readLockInfo()).thenReturn(createLockInfo(FOO_TENANT, false));
		final TenantAvailabilityService tenantAvailabilityService = createInitDetectionServiceSpy(lockDao);

		final boolean result = tenantAvailabilityService.isTenantAvailable(FOO_TENANT);

		assertThat(result).isTrue();
	}

	@Test
	public void tenantIsNotAvailableDuringInit()
	{
		when(lockDao.readLockInfo()).thenReturn(createLockInfo(FOO_TENANT, true));
		final TenantAvailabilityService tenantAvailabilityService = createInitDetectionServiceSpy(lockDao);

		final boolean result = tenantAvailabilityService.isTenantAvailable(FOO_TENANT);

		assertThat(result).isFalse();
	}

	@Test
	public void tenantIsNotAvailableDuringMasterInit()
	{
		when(lockDao.readLockInfo()).thenReturn(createLockInfo(MasterTenant.MASTERTENANT_ID, true));
		final TenantAvailabilityService tenantAvailabilityService = createInitDetectionServiceSpy(lockDao);

		final boolean result = tenantAvailabilityService.isTenantAvailable(FOO_TENANT);

		assertThat(result).isFalse();
	}


	@Test
	public void tenantIsAvailableDuringOtherInit()
	{
		when(lockDao.readLockInfo()).thenReturn(createLockInfo(BAR_TENANT, true));
		final TenantAvailabilityService tenantAvailabilityService = createInitDetectionServiceSpy(lockDao);

		final boolean result = tenantAvailabilityService.isTenantAvailable(FOO_TENANT);

		assertThat(result).isTrue();
	}
}
