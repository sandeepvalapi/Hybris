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
package de.hybris.platform.persistence.links;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConfigurableLinkOperationFactoryUnitTest
{
	private static final String RELATION_QUALIFIER = "TEST_RELATION";

	@Mock
	private PluggableLinkOperationFactory enabled;

	@Mock
	private PluggableLinkOperationFactory disabled;

	@Mock
	private SessionContext sessionContext;

	@Mock
	private Item item;

	@Mock
	private List<Item> items;

	@Mock
	private Language language;

	@SuppressWarnings("boxing")
	@Before
	public void setUp()
	{
		when(enabled.isEnabled()).thenReturn(true);
		when(disabled.isEnabled()).thenReturn(false);
	}

	@Test
	public void shouldUseFirstEnabledFactoryAndDontAskOthers()
	{
		final ConfigurableLinkOperationFactory factory = givenFactory(enabled, disabled);

		factory.createRemoveOperation(sessionContext, item, true, RELATION_QUALIFIER, language, items, true, true, true);

		verify(enabled).isEnabled();
		verify(enabled).createRemoveOperation(sessionContext, item, true, RELATION_QUALIFIER, language, items, true, true, true);
		verifyNoMoreInteractions(enabled, disabled);
	}

	@Test
	public void shouldUseFirstEnabledFactoryAndSkipDisabled()
	{
		final ConfigurableLinkOperationFactory factory = givenFactory(disabled, enabled);

		factory.createRemoveOperation(sessionContext, item, true, RELATION_QUALIFIER, language, items, true, true, true);

		verify(disabled).isEnabled();
		verify(enabled).isEnabled();
		verify(enabled).createRemoveOperation(sessionContext, item, true, RELATION_QUALIFIER, language, items, true, true, true);
		verifyNoMoreInteractions(enabled, disabled);
	}

	@Test
	public void shouldThrowAnExceptionWhenThereIsNoFactories()
	{
		final ConfigurableLinkOperationFactory factory = givenFactory();
		try
		{
			factory.createRemoveOperation(sessionContext, item, true, RELATION_QUALIFIER, language, items, true, true, true);
		}
		catch (final IllegalStateException expected)
		{
			assertThat(expected).hasMessage("Can't find configured factory to use");
			return;
		}
		fail("IllegalStateException was expected");
	}

	@Test
	public void shouldThrowAnExceptionWhenThereIsNoEnabledFactory()
	{
		final ConfigurableLinkOperationFactory factory = givenFactory(disabled);
		try
		{
			factory.createRemoveOperation(sessionContext, item, true, RELATION_QUALIFIER, language, items, true, true, true);
		}
		catch (final IllegalStateException expected)
		{
			assertThat(expected).hasMessage("Can't find configured factory to use");
			return;
		}
		fail("IllegalStateException was expected");
	}

	private ConfigurableLinkOperationFactory givenFactory(final PluggableLinkOperationFactory... factories)
	{
		return new ConfigurableLinkOperationFactory(ImmutableList.copyOf(factories));
	}

}
