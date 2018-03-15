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
package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.directpersistence.record.ModificationRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;

import java.util.Locale;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


public class RecordAssert extends GenericAssert<RecordAssert, ModificationRecord>
{
	public RecordAssert(final ModificationRecord actual)
	{
		super(RecordAssert.class, actual);
	}

	public static RecordAssert assertThat(final ModificationRecord actual)
	{
		return new RecordAssert(actual);
	}

	public RecordAssert isTypeOf(final String type)
	{
		Assertions.assertThat(actual.getType()).isEqualTo(type);
		return this;
	}

	public RecordAssert hasChanges()
	{
		Assertions.assertThat(actual.getChanges()).isNotNull().isNotEmpty();
		return this;
	}

	public RecordAssert hasChangesOfSize(final int size)
	{
		hasChanges();
		Assertions.assertThat(actual.getChanges()).hasSize(size);
		return this;
	}

	public RecordAssert hasLocalizedChanges()
	{
		Assertions.assertThat(actual.getLocalizedChanges()).isNotNull().isNotEmpty();
		return this;
	}

	public RecordAssert hasLocalizedChangesOfSize(final int size)
	{
		hasLocalizedChanges();
		Assertions.assertThat(actual.getLocalizedChanges()).hasSize(size);
		return this;
	}

	public RecordAssert hasPropertyHolderWithNameAndValue(final String propertyName, final Object value)
	{
		final Optional<PropertyHolder> holder = getPropertyHolder(propertyName);
		Assertions.assertThat(holder.isPresent()).isTrue();
		Assertions.assertThat(holder.get().getValue()).isEqualTo(value);
		return this;
	}

	public RecordAssert hasPropertyHolderWithNameAndValue(final String propertyName, final Object value, final Locale locale)
	{
		final Optional<PropertyHolder> holder = getPropertyHolder(propertyName, locale);
		Assertions.assertThat(holder.isPresent()).isTrue();
		Assertions.assertThat(holder.get().getValue()).isEqualTo(value);
		return this;
	}

	private Optional<PropertyHolder> getPropertyHolder(final String propertyName)
	{
		return findHolder(actual.getChanges(), propertyName);
	}

	private Optional<PropertyHolder> getPropertyHolder(final String propertyName, final Locale locale)
	{
		final Set<PropertyHolder> holders = actual.getLocalizedChanges().get(locale);
		return findHolder(holders, propertyName);
	}

	private Optional<PropertyHolder> findHolder(final Iterable<PropertyHolder> holders, final String propertyName)
	{
		if (holders == null)
		{
			return Optional.<PropertyHolder> absent();
		}

		return Iterables.tryFind(holders, new Predicate<PropertyHolder>()
		{

			@Override
			public boolean apply(final PropertyHolder input)
			{
				return propertyName.equals(input.getName());
			}
		});
	}
}
