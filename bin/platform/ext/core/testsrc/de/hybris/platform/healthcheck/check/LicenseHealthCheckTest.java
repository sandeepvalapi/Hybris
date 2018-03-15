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
package de.hybris.platform.healthcheck.check;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Map;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.junit.Test;


@UnitTest
public class LicenseHealthCheckTest
{
	private final LicenseHealthCheck licenseHealthCheck = new LicenseHealthCheck();

	@Test
	public void shouldReturnResultOfLicenseValidation() throws Exception
	{
		// when
		final Map<MultiKey, Object> result = licenseHealthCheck.perform();

		// then
		assertThat(result).isNotNull();
        assertThat(result.get(new MultiKey("license check", "validation", "status"))).isEqualTo("VALID");
        assertThat(result.get(new MultiKey("license check", "validation", "full result"))).isNotNull();
	}
}
