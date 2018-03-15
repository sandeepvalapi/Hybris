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
package de.hybris.platform.servicelayer.internal.model.attribute.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultDynamicAttributesProviderTest
{
	private DefaultDynamicAttributesProvider provider;
	@Mock
	private AbstractItemModel model;
	@Mock
	private DynamicAttributeHandler handler1;
	@Mock
	private DynamicAttributeHandler handler2;
	@Mock
	private DynamicAttributeHandler locHandler;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		final Map<String, DynamicAttributeHandler> dynamicAttributes = new HashMap<String, DynamicAttributeHandler>();
		dynamicAttributes.put("foo", handler1);
		dynamicAttributes.put("bar", handler2);
		dynamicAttributes.put("fooLoc", locHandler);
		dynamicAttributes.put("barLoc", handler1); //localized attribute with old (not localized) handler assigned - for backward compatibility tests

		provider = new DefaultDynamicAttributesProvider(dynamicAttributes);
	}

	@Test
	public void shouldThrowSystemExceptionWhenThereIsNoHandlerForAttributeOnGet()
	{
		// given
		final String attribute = "xxx";

		try
		{
			// when
			provider.get(model, attribute);
			fail("Should throw SystemException");
		}
		catch (final SystemException e)
		{
			// then
			assertThat(e.getMessage()).contains(
					"No registered attribute handler for attribute 'xxx'. Check your Spring configuration.");
		}
	}

	@Test
	public void shouldCallGetOnFoundHandlerForAttribute()
	{
		// given
		final String attribute1 = "foo";
		final String attribute2 = "bar";

		// when
		provider.get(model, attribute1);
		provider.get(model, attribute2);

		// then
		verify(handler1, times(1)).get(model);
		verify(handler2, times(1)).get(model);
	}

	@Test
	public void shouldCallGetOnFoundLocalizedAndNonLocalizedHandlerForAttribute()
	{
		// given
		final String attribute1 = "fooLoc";
		final String attribute2 = "barLoc";

		// when
		provider.getLocalized(model, attribute1, Locale.ENGLISH);
		provider.getLocalized(model, attribute2, Locale.ENGLISH);

		// then
		verify(handler1, times(1)).get(model);
		verify(locHandler, times(1)).get(model);
	}

	@Test
	public void shouldThrowSystemExceptionWhenThereIsNoHandlerForAttributeOnSet()
	{
		// given
		final String attribute = "xxx";
		final String value = "boo";

		try
		{
			// when
			provider.set(model, attribute, value);
			fail("Should throw SystemException");
		}
		catch (final SystemException e)
		{
			// then
			assertThat(e.getMessage()).contains(
					"No registered attribute handler for attribute 'xxx'. Check your Spring configuration.");
		}
	}

	@Test
	public void shouldCallSetOnFoundHandlerForAttribute()
	{
		// given
		final String attribute1 = "foo";
		final String attribute2 = "bar";
		final String value1 = "boo1";
		final String value2 = "boo2";

		// when
		provider.set(model, attribute1, value1);
		provider.set(model, attribute2, value2);

		// then
		verify(handler1, times(1)).set(model, value1);
		verify(handler2, times(1)).set(model, value2);
	}

	@Test
	public void shouldCallSetOnFoundLocalizedAndNotLocalizedHandlerForAttribute()
	{
		// given
		final String attribute1 = "fooLoc";
		final String attribute2 = "barLoc";
		final String value1 = "boo1Loc";
		final String value2 = "boo2Loc";

		// when
		provider.setLocalized(model, attribute1, value1, Locale.ENGLISH);
		provider.setLocalized(model, attribute2, value2, Locale.ENGLISH);

		// then
		verify(locHandler, times(1)).set(model, value1);
		verify(handler1, times(1)).set(model, value2);
	}

}
