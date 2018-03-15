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
package de.hybris.platform.test;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.security.SaltEncodingPolicy;
import de.hybris.platform.persistence.security.SaltedMD5PasswordEncoder;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Test class of {@link SaltedMD5PasswordEncoder}.
 */
@IntegrationTest
public class SaltedMD5PasswordEncoderTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(SaltedMD5PasswordEncoderTest.class.getName());

	private static final String UID = "test";
	private static final String PASSWORD = "foobar";
	private static final String SALT_ENCODING_POLICY = "saltEncodingPolicy";

	/**
	 * Checks password encoding and checking
	 */
	@Test
	public void testEncoding()
	{
		final SaltedMD5PasswordEncoder encoder = new SaltedMD5PasswordEncoder();
		final SaltEncodingPolicy saltPolicy = Registry.getApplicationContext().getBean(SALT_ENCODING_POLICY,
				SaltEncodingPolicy.class);
		encoder.setSaltEncodingPolicy(saltPolicy);
		final String hashedPassword = encoder.encode(UID, PASSWORD);

		LOG.info("UID: " + UID);
		LOG.info("Password: " + PASSWORD);
		LOG.info("Hashed password: " + hashedPassword);

		assertTrue("Password check failed!", encoder.check(UID, hashedPassword, PASSWORD));

	}
}
