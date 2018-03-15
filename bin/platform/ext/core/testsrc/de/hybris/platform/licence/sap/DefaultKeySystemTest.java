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
package de.hybris.platform.licence.sap;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.Config;

import java.util.Properties;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultKeySystemTest
{
	private static final String HW_ID = "Y4989890650";

	private DefaultKeySystem defaultKeySystem;

	@Before
	public void setUp() throws Exception
	{
		defaultKeySystem = new DefaultKeySystem();
	}

	@Test
	public void testInit() throws Exception
	{
		assertThat(defaultKeySystem.init()).isTrue();
	}

	@Test
	public void testGetSystemId() throws Exception
	{
		assertThat(defaultKeySystem.getSystemId()).hasSize(3).isEqualTo(Config.getString("license.sap.sapsystem", "CPS"));
	}

	@Test
	public void testGetSwProducts() throws Exception
	{
		assertThat(defaultKeySystem.getSwProducts()).containsOnly("CPS_HDB", "CPS_SQL", "CPS_ORA", "CPS_MSS", "CPS_MYS", "CPS_POS");
	}

	@Test
	public void shouldContainProductNamesWithConfiguredDbPostfixes() throws Exception
	{
		// given
		final TestKeySystemWithOverridenDbCodes keySystem = new TestKeySystemWithOverridenDbCodes();

		// when
		final Vector swProducts = keySystem.getSwProducts();

		// then
		assertThat(swProducts).containsOnly("CPS_SAPTEST", "CPS_MYSQLTEST", "CPS_ORACLETEST", "CPS_SQLSERVERTEST", "CPS_HSQLDBTEST",
				"CPS_POSTGRESQLTEST");
	}

	@Test
	public void testGetBasisRelease() throws Exception
	{
		assertThat(defaultKeySystem.getBasisRelease()).isEqualTo("hybris");
	}

	@Test
	public void testGetHwId() throws Exception
	{
		assertThat(defaultKeySystem.getHwId()).hasSize(11).isEqualTo(HW_ID);
	}

	@Test
	public void testGetCmdPrefix() throws Exception
	{
		final String cmdPrefix = defaultKeySystem.getCmdPrefix();
		if (System.getProperty("os.name").equalsIgnoreCase("windows"))
		{
			assertThat(cmdPrefix).isEqualTo("license.bat");
		}
		else
		{
			assertThat(cmdPrefix).isEqualTo("license.sh");
		}
	}

	private static class TestKeySystemWithOverridenDbCodes extends DefaultKeySystem
	{
		@Override
		protected Properties loadPlatformProperties()
		{
			final Properties properties = new Properties();
			properties.put("license.db.code.sap", "SAPTEST");
			properties.put("license.db.code.hsqldb", "HSQLDBTEST");
			properties.put("license.db.code.mysql", "MYSQLTEST");
			properties.put("license.db.code.oracle", "ORACLETEST");
			properties.put("license.db.code.sqlserver", "SQLSERVERTEST");
			properties.put("license.db.code.postgresql", "POSTGRESQLTEST");

			return properties;
		}
	}

}
