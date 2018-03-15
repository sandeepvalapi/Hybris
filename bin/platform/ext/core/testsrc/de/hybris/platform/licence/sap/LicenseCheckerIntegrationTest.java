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

import de.hybris.bootstrap.annotations.IntegrationTest;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.sap.security.core.server.likey.Admin;
import com.sap.security.core.server.likey.KeySystem;
import com.sap.security.core.server.likey.LicenseChecker;
import com.sap.security.core.server.likey.LogAndTrace;
import com.sap.security.core.server.likey.Persistence;


@IntegrationTest
public class LicenseCheckerIntegrationTest
{
	private final KeySystem keySystem = new DefaultKeySystem();
	private final LogAndTrace logAndTrace = new DefaultLogAndTrace();
	private Persistence persistence;
	private LicenseChecker licenseChecker;

	@Before
	public void setUp() throws Exception
	{
		persistence = new TestPersistence();
		licenseChecker = new LicenseChecker(persistence, keySystem, logAndTrace);
	}

	@Test
	public void shouldSuccessfullyCheckAndValidateFirstInstalledTempLicense() throws Exception
	{
		// given
		final String swProductName = "CPS_SQL";
        installFirstTempLicenseForProduct(swProductName);

		// when
		final boolean result = licenseChecker.check(swProductName);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldCheckLicenseWithInvalidProductName() throws Exception
	{
		final String swProductName = "CPS_SQL";
		final String wrongProductName = "WrongProductName";
        installFirstTempLicenseForProduct(swProductName);

		// when
		final boolean result = licenseChecker.check(wrongProductName);

		// then
		assertThat(result).isFalse();
	}

	private void installFirstTempLicenseForProduct(final String productName)
	{
		final Admin admin = new Admin(persistence, keySystem, logAndTrace);
        final boolean installResult = admin.installFirstTempLicense(productName, new Vector());

        assertThat(installResult).isTrue();
    }

}
