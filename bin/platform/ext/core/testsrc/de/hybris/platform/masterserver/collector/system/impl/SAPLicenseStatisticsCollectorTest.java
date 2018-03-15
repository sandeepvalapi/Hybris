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
package de.hybris.platform.masterserver.collector.system.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.licence.sap.DefaultKeySystem;
import de.hybris.platform.licence.sap.DefaultLogAndTrace;
import de.hybris.platform.licence.sap.TestPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.hybris.statistics.collector.StatisticsCollector;
import com.sap.security.core.server.likey.Admin;
import com.sap.security.core.server.likey.KeySystem;
import com.sap.security.core.server.likey.LicenseChecker;
import com.sap.security.core.server.likey.LicenseKey;
import com.sap.security.core.server.likey.Persistence;


@UnitTest
public class SAPLicenseStatisticsCollectorTest
{
	private final Persistence persistence = new TestPersistence();
	private final StatisticsCollector<Map<String, List<Map<String, String>>>> collector = new SAPLicenseStatisticsCollector()
	{
		@Override
		Persistence getPersistence()
		{
			return persistence;
		}
	};
	private final KeySystem keySystem = new DefaultKeySystem();
	private final Admin admin = new Admin(persistence, keySystem, new DefaultLogAndTrace());

	@Before
	public void setUp() throws Exception
	{
		admin.installFirstTempLicense("TestProduct", new Vector());
	}

	@After
	public void tearDown() throws Exception
	{
		admin.deleteLicenses(keySystem.getSystemId(), keySystem.getHwId(), "TestProduct", new Vector());
	}

	@Test
	public void shouldCollectStatisticsFromInstalledSAPLicenses() throws Exception
	{
		// when
		final Map<String, List<Map<String, String>>> result = collector.collectStatistics();

		// then
		assertThat(result).isNotNull();
		assertThat(result.get("sap licenses")).isNotEmpty().hasSize(1);
		final Map<String, String> stats = result.get("sap licenses").get(0);
		final LicenseKey licenseKey = getLicenseKeyForProduct("TestProduct");
		assertThat(licenseKey).isNotNull();
		assertThat(stats.get("System")).isEqualTo(licenseKey.getSystemId());
		assertThat(stats.get("Hardware Key")).isEqualTo(licenseKey.getHwKey());
		assertThat(stats.get("SW Product")).isEqualTo(licenseKey.getSwProduct());
		assertThat(stats.get("SW Product Limit")).isEqualTo(licenseKey.getSwProductLimit());
		assertThat(stats.get("Begin date")).isEqualTo(licenseKey.getBeginDate());
		assertThat(stats.get("Expiration date")).isEqualTo(licenseKey.getEndDate());
		assertThat(stats.get("License key type")).isEqualTo(licenseKey.getType());
		assertThat(stats.get("System No.")).isEqualTo(licenseKey.getSysNo());
		assertThat(stats.get("Validity")).isEqualTo("valid");
		assertThat(stats.get("Installation Number")).isEqualTo(licenseKey.getInstNo());
	}

	private LicenseKey getLicenseKeyForProduct(final String productName)
	{
		final List<LicenseKey> allLicenses = new ArrayList<LicenseKey>(LicenseChecker.getAllLicenses(persistence,
				new DefaultLogAndTrace()));

		final Optional<LicenseKey> license = Iterables.tryFind(allLicenses, new Predicate<LicenseKey>()
		{
			@Override
			public boolean apply(final LicenseKey key)
			{
				return productName.equals(key.getSwProduct());
			}
		});

		return license.isPresent() ? license.get() : null;
	}

}
