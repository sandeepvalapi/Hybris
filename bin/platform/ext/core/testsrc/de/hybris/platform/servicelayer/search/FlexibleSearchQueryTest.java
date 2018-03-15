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
package de.hybris.platform.servicelayer.search;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;


@UnitTest
public class FlexibleSearchQueryTest
{


	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery#addQueryParameter(java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenValueIsNull()
	{
		// given
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final String value = null;

		try
		{
			// when
			fQuery.addQueryParameter("foo", value);
			fail("IllegalArgumentException should be thrown");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Value is required, null given for key: foo");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery#addQueryParameter(java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenValueIsEmptyCollection()
	{
		// given
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final Collection value = Collections.EMPTY_LIST;

		try
		{
			// when
			fQuery.addQueryParameter("foo", value);
			fail("IllegalArgumentException should be thrown");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Value is instanceof Collection but cannot be empty collection");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery#addQueryParameter(java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldAddKeyAndValueAsQueryParams()
	{
		// given
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final String value = "foo";

		// when
		fQuery.addQueryParameter("foo", value);

		// then
		assertThat(fQuery.getQueryParameters()).isNotEmpty();
		assertThat(fQuery.getQueryParameters()).hasSize(1);
		assertThat(fQuery.getQueryParameters().get("foo")).isEqualTo("foo");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery#addQueryParameters(java.util.Map)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenOneOfValuesInParamsMapIsNull()
	{
		// given
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("foo", "foo");
		params.put("bar", null);

		try
		{
			// when
			fQuery.addQueryParameters(params);
			fail("IllegalArgumentException should be thrown");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Value is required, null given for key: bar");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery#addQueryParameters(java.util.Map)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenOneOfValuesInParamsMapIsEmptyCollection()
	{
		// given
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("foo", "foo");
		params.put("bar", Collections.EMPTY_LIST);

		try
		{
			// when
			fQuery.addQueryParameters(params);
			fail("IllegalArgumentException should be thrown");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Value is instanceof Collection but cannot be empty collection for key: bar");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery#addQueryParameters(java.util.Map)}.
	 */
	@Test
	public void shouldAddMapOfParamsAsQueryParameters()
	{
		// given
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("foo", "foo");
		params.put("bar", "bar");

		// when
		assertThat(fQuery.getQueryParameters()).isEmpty();
		fQuery.addQueryParameters(params);

		// then
		assertThat(fQuery.getQueryParameters()).isNotEmpty();
		assertThat(fQuery.getQueryParameters()).hasSize(2);
	}

	/**
	 * Test method for {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery#equals(Object)}
	 */
	@Test
	public void shouldBeEqual()
	{
		// given
		final FlexibleSearchQuery fQuery1 = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final FlexibleSearchQuery fQuery2 = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");

		// when
		fQuery1.addQueryParameter("foo", "foo");
		fQuery1.addQueryParameter("bar", Long.valueOf(100));
		fQuery2.addQueryParameter("foo", "fo" + "o");
		fQuery2.addQueryParameter("bar", Long.valueOf(50 + 50));

		// then
		assertThat(fQuery1.equals(fQuery2)).isTrue();
		assertThat(fQuery1.equals(fQuery1)).isTrue();
		assertThat(fQuery1.hashCode()).isEqualTo(fQuery2.hashCode());

	}

	/**
	 * Test method for {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery#equals(Object)}
	 */
	@Test
	public void shouldNotBeEqual()
	{
		// given
		// order of parameters in query3 is different
		final FlexibleSearchQuery fQuery1 = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final FlexibleSearchQuery fQuery2 = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {foo}=?foo AND {bar}=?bar");
		final FlexibleSearchQuery fQuery3 = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {bar}=?bar AND {foo}=?foo");

		// when
		// query2 has different value of parameter bar
		fQuery1.addQueryParameter("foo", "foo");
		fQuery1.addQueryParameter("bar", Long.valueOf(100));

		fQuery2.addQueryParameter("foo", "foo");
		fQuery2.addQueryParameter("bar", Long.valueOf(50));

		fQuery3.addQueryParameter("foo", "foo");
		fQuery3.addQueryParameter("bar", Long.valueOf(100));

		// then
		assertThat(fQuery1.equals(fQuery2)).isFalse();
		assertThat(fQuery1.equals(fQuery3)).isFalse();

	}

}
