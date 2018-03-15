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
package de.hybris.platform.persistence.security;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class GenericSaltedPasswordEncoderTest extends HybrisJUnit4Test
{

	private PasswordEncoderFactory passwordEncoderFactory;

	@Before
	public void prepareTest()
	{
		passwordEncoderFactory = Registry.getApplicationContext().getBean("core.passwordEncoderFactory",
				PasswordEncoderFactory.class);
	}

	@Test(expected = RuntimeException.class)
	public void nonExistingAlgorithmShouldThrowException()
	{
		DigestCalculator.getInstance("noneSuch");
	}

	@Test
	public void testDigestCalculator()
	{
		final PasswordEncoder deprecatedEncoder = passwordEncoderFactory.getEncoder("md5");
		final PasswordEncoder genericEncoder = Registry.getApplicationContext()
				.getBean("md5PasswordEncoder", PasswordEncoder.class);

		assertThat(deprecatedEncoder.encode("uid", "foo")).isEqualTo(genericEncoder.encode("uid", "foo"));
	}
}
