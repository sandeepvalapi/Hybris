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
package de.hybris.platform.media.services.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.media.exceptions.MediaInvalidLocationException;
import de.hybris.platform.testframework.TestUtils;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultMediaLocationHashServiceTest
{
	private static final String SECURED_LOCATION_SALT = "33201938958234904813001891048234022402340";
	private DefaultMediaLocationHashService hashService;

	@Before
	public void createDefaultMediaLocationHashService() throws Exception
	{
		hashService = new DefaultMediaLocationHashService();
		hashService.setSalt(SECURED_LOCATION_SALT);
	}

	@Test
	public void shouldGenerateDifferentLocationHashKeysForDifferentFolderQualifiersAndLocations()
	{
		// given
		final String folderQualifier = "foo";
		final String location = "bar/baz/some.jpg";

		// when
		final String hashForLocation = hashService.createHashForLocation(folderQualifier, location);
		final String hashForTrickedLocation = hashService.createHashForLocation("bar", location);

		// then
		assertThat(hashForLocation).isNotEqualTo(hashForTrickedLocation);
	}

	@Test
	public void shouldValidateValidLocationHash()
	{
		// given
		TestUtils.disableFileAnalyzer("error expected");
		final String validHash = "5831c5fd4587a3d2ad631647834256f7ab3f767b8f9e77b6b95a90c4dd676341";

		try
		{
			// when
			hashService.verifyHashForLocation(validHash, "root", "hd5/ha8/8796136538142.jpg");
		}
		catch (final MediaInvalidLocationException e)
		{
			// then
			fail("Shouldn't throw an exception");
		}
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void shouldThrowMediaInvalidLocationExceptionWhenHashIsNotValid()
	{
		// given
		TestUtils.disableFileAnalyzer("error expected");
		final String notValidHash = "5831c5fd4587a3d2ad631647834256f7ab3f767b8f9e77b6b95a90c4dd676341AAA";

		try
		{
			// when
			hashService.verifyHashForLocation(notValidHash, "root", "hd5/ha8/8796136538142.jpg");
			fail("Should throw MediaInvalidLocationException");
		}
		catch (final MediaInvalidLocationException e)
		{
			// then
			assertThat(e).hasMessage("Given location: hd5/ha8/8796136538142.jpg does not belong to provided folder qualifier: root");
		}
		TestUtils.enableFileAnalyzer();
	}

}
