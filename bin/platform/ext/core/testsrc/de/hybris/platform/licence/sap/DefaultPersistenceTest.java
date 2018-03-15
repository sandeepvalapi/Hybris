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

import java.util.Properties;

import org.junit.After;
import org.junit.Test;

import com.sap.security.core.server.likey.Persistence;


@IntegrationTest
public class DefaultPersistenceTest
{
	private final Persistence persistence = new PropertyBasedTestPersistence();

	@After
	public void tearDown() throws Exception
	{
		((DefaultPersistence) persistence).getPropertyFile().delete();
	}

	@Test
	public void shouldInitializePersistence() throws Exception
	{
		// when
		final boolean initialized = persistence.init();

		// then
		assertThat(initialized).isTrue();
	}

	@Test
	public void shouldInsertNewEntryWithKeyAndValueIfSuchEntryDoesNotExists() throws Exception
	{
		// given
		final String key = "foo";
		final String value = "bar";

		// when
		final boolean result = persistence.insertKey(key, value);

		// then
		assertThat(result).isTrue();
		assertThat(persistence.getKey(key)).isEqualTo(value);
	}

	@Test
	public void shouldNotInsertEntryWithKeyAndValueIfSuchEntryAlreadyExists() throws Exception
	{
		// given
		final String key = "foo";
		final String value = "bar";
		assertThat(persistence.insertKey(key, value)).isTrue();

		// when
		final boolean result = persistence.insertKey(key, value);

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldUpdateExistingValueForGivenKey() throws Exception
	{
		// given
		final String key = "foo";
		final String value = "bar";
		final String newValue = "bar2";
		persistence.insertKey(key, value);
		assertThat(persistence.getKey(key)).isEqualTo(value);

		// when
		final boolean result = persistence.updateKey(key, newValue);

		// then
		assertThat(result).isTrue();
		assertThat(persistence.getKey(key)).isEqualTo(newValue);
	}

	@Test
	public void shouldReturnFalseWhenTryingToUpdateNonExistentEntry() throws Exception
	{
		// given
		final String key = "nonExistent";
		final String value = "bar";

		// when
		final boolean result = persistence.updateKey(key, value);

		// then
		assertThat(result).isFalse();
		assertThat(persistence.getKey(key)).isNull();
	}

	@Test
	public void shouldDeleteEntryForGivenExistingKey() throws Exception
	{
		// given
		final String key = "foo";
		final String value = "bar";
		persistence.insertKey(key, value);
		assertThat(persistence.getKey(key)).isEqualTo(value);

		// when
		final boolean result = persistence.deleteKey(key);

		// then
		assertThat(result).isTrue();
		assertThat(persistence.getKey(key)).isNull();
	}

	@Test
	public void shouldReturnFalseWhenTryingToDeleteNonExistentEntry() throws Exception
	{
		// given
		final String key = "nonExistent";

		// when
		final boolean result = persistence.deleteKey(key);

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldGetAValueForGivenKey() throws Exception
	{
		// given
		final String key = "foo";
		final String value = "bar";
		persistence.insertKey(key, value);

		// when
		final String result = persistence.getKey(key);

		// then
		assertThat(result).isEqualTo(value);
	}

	@Test
	public void shouldReturnNullWhenTryingToGetAValueForNonExistentKey() throws Exception
	{
		// given
		final String key = "nonExistent";

		// when
		final String value = persistence.getKey(key);

		// then
		assertThat(value).isNull();
	}

	@Test
	public void shouldReturnAllEntriesAsPropertiesObject() throws Exception
	{
		// given
		final String key1 = "foo";
		final String value1 = "bar";
		final String key2 = "baz";
		final String value2 = "bam";
		persistence.insertKey(key1, value1);
		persistence.insertKey(key2, value2);

		// when
		final Properties result = persistence.getKeys();

		// then
		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.getProperty(key1)).isEqualTo(value1);
		assertThat(result.getProperty(key2)).isEqualTo(value2);
	}
}
