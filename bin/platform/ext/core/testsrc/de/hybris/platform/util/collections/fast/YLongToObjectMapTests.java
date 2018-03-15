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
package de.hybris.platform.util.collections.fast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.collections.fast.procedures.YLongAndObjectProcedure;
import de.hybris.platform.util.collections.fast.procedures.YLongProcedure;

import org.junit.Test;


@UnitTest
public class YLongToObjectMapTests
{
	@Test
	public void shouldBeAbleToCreateByDefaultConstructor()
	{
		new YLongToObjectMap<>();
	}

	@Test
	public void shouldBeAbleToCreateWithInitialCapacity()
	{
		new YLongToObjectMap<>(1234);
	}

	@Test
	public void shouldBeEmptyAfterClear()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		assertTrue(map.isEmpty());
		map.put(12, new Object());
		assertFalse(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void shouldBeAbleToPutValuesForTheSameKey()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		map.put(10, new Object());
		map.put(10, new Object());
	}

	@Test
	public void shouldNotFailOnGettingNonExistingKey()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		map.get(12);
	}

	@Test
	public void shouldBeAbleToGetExistingValue()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		final Object expected = new Object();
		map.put(12, expected);
		assertTrue(expected == map.get(12));
	}

	@Test
	public void shouldReturnNullWhenGettingNotExistingValue()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		assertNull(map.get(12));
	}

	@Test
	public void shouldReturnNullWhenPuttingKeyForTheFirstTime()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		assertNull(map.put(12, new Object()));
	}

	@Test
	public void shouldReturnPreviousValueWhenExistingKeyIsBeingReplaced()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		final Object expected = new Object();
		map.put(12, expected);
		assertTrue(expected == map.put(12, new Object()));
	}

	@Test
	public void shouldReturnNullWhenRemovingNotExistingKey()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		assertNull(map.remove(12));
	}

	@Test
	public void shouldReturnPreviousValueWhenRemovingExistingKey()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		final Object expected = new Object();
		map.put(12, expected);
		assertTrue(expected == map.remove(12));
	}

	@Test
	public void shouldBeAbleToPutNullValue()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		map.put(12, null);
	}

	@Test
	public void sizeShouldBeZeroWhenSetIsEmpty()
	{
		assertEquals(0, new YLongToObjectMap<>().size());
	}

	@Test
	public void sizeShouldBeProperWhenSetIsNotEmpty()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		map.put(12, new Object());
		assertEquals(1, map.size());
		map.put(12, new Object());
		assertEquals(1, map.size());
		map.put(13, new Object());
		assertEquals(2, map.size());
		map.remove(12);
		assertEquals(1, map.size());
		map.clear();
		assertEquals(0, map.size());
	}

	@Test
	public void shouldBeEmptyWhenThereAreNoElements()
	{
		assertTrue(new YLongToObjectMap<>().isEmpty());
	}

	@Test
	public void shouldNotBeEmptyWhenTherIsAnyElement()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		map.put(12, new Object());
		assertFalse(map.isEmpty());
		map.put(12, new Object());
		assertFalse(map.isEmpty());
	}

	@Test
	public void shouldContainsExistingKey()
	{
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		assertFalse(map.containsKey(12));
		map.put(12, new Object());
		assertTrue(map.containsKey(12));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenNullEntryProcedureHasBeenPassed()
	{
		new YLongToObjectMap<>().forEachEntry(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenNullKeyProcedureHasBeenPassed()
	{
		new YLongToObjectMap<>().forEachKey(null);
	}

	@Test
	public void procedureShouldNotBeCalledForMapWithoutEntries()
	{
		final YLongAndObjectProcedure procedure = mock(YLongAndObjectProcedure.class);
		new YLongToObjectMap<>().forEachEntry(procedure);
		verify(procedure, never()).execute(anyLong(), anyObject());
	}

	@Test
	public void procedureShouldNotBeCalledForMapWithoutKeys()
	{
		final YLongProcedure procedure = mock(YLongProcedure.class);
		new YLongToObjectMap<>().forEachKey(procedure);
		verify(procedure, never()).execute(anyLong());
	}

	@Test
	public void procedureShouldBeCalledForMapWithEntries()
	{
		final YLongAndObjectProcedure procedure = mock(YLongAndObjectProcedure.class);
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		final Object obj1 = new Object();
		final Object obj2 = new Object();
		map.put(1, obj1);
		map.put(2, obj2);
		map.forEachEntry(procedure);
		verify(procedure, times(2)).execute(anyLong(), anyObject());
		verify(procedure, times(1)).execute(1, obj1);
		verify(procedure, times(1)).execute(2, obj2);
	}

	@Test
	public void procedureShouldBeCalledForMapWithKeys()
	{
		final YLongProcedure procedure = mock(YLongProcedure.class);
		final YLongToObjectMap<Object> map = new YLongToObjectMap<>();
		final Object obj1 = new Object();
		final Object obj2 = new Object();
		map.put(1, obj1);
		map.put(2, obj2);
		map.forEachKey(procedure);
		verify(procedure, times(2)).execute(anyLong());
		verify(procedure, times(1)).execute(1);
		verify(procedure, times(1)).execute(2);
	}
}
