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
package de.hybris.platform.media.storage.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;


public class IterableValueConverterTest
{
	private final IterableValueConverter converter = new IterableValueConverter();


	@Test
	public void shouldConvertCommaSeparatedStringIntoIterable()
	{
		// given
		final String input = "foo,bar,baz";

		// when
		final Iterable<String> converted = converter.convert(input);

		// then
		assertThat(converted).isNotNull().hasSize(3);
		assertThat(converted).contains("foo", "bar", "baz");
	}

	@Test
	public void shouldConvertCommaSeparatedStringIntoIterableSkippingEmptyElements()
	{
		// given
		final String input = "foo,,bar, ,baz";

		// when
		final Iterable<String> converted = converter.convert(input);

		// then
		assertThat(converted).isNotNull().hasSize(3);
		assertThat(converted).contains("foo", "bar", "baz");
	}

	@Test
	public void shouldConvertCommaSeparatedStringIntoIterableTrimingSpacesFromElements()
	{
		// given
		final String input = " foo, bar , baz ";

		// when
		final Iterable<String> converted = converter.convert(input);

		// then
		assertThat(converted).isNotNull().hasSize(3);
		assertThat(converted).contains("foo", "bar", "baz");
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenConversionInputIsNull()
	{
		// given
		final String input = null;

		try
		{
			// when
			converter.convert(input);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Conversion input cannot be null");
		}
	}
}
