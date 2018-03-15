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

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;


@UnitTest
public class YLongToIntMapTests
{
	@Test
	public void shouldBeAbleToCreateByDefaultConstructor()
	{
		new YLongToIntMap();
	}

	@Test
	public void shouldBeAbleToCreateWithInitialCapacity()
	{
		new YLongToIntMap(123);
	}

	@Test
	public void shouldBeAbleToPutValuesForTheSameKey()
	{
		final YLongToIntMap map = new YLongToIntMap();
		map.put(10, 64);
		map.put(10, 128);
	}

	@Test
	public void shouldNotFailOnGettingNonExistingKey()
	{
		final YLongToIntMap map = new YLongToIntMap();
		map.get(12);
	}

	@Test
	public void shouldBeAbleToGetExistingValue()
	{
		final YLongToIntMap map = new YLongToIntMap();
		final int expected = 123;
		map.put(12, expected);
		assertEquals(expected, map.get(12));
	}

	@Test
	public void shouldReturnEmptyValueWhenGettingNotExistingValue()
	{
		final YLongToIntMap map = new YLongToIntMap();
		assertEquals(map.getEmptyValue(), map.get(12));
	}

	@Test
	public void shouldReturnEmptyValueWhenPuttingKeyForTheFirstTime()
	{
		final YLongToIntMap map = new YLongToIntMap();
		assertEquals(map.getEmptyValue(), map.put(12, 42));
	}

	@Test
	public void shouldReturnPreviousValueWhenExistingKeyIsBeeingReplaced()
	{
		final YLongToIntMap map = new YLongToIntMap();
		final int expected = 123;
		map.put(12, expected);
		assertEquals(expected, map.put(12, 210));
	}

	@Test
	public void shouldReturnEmptyValueWhenRemovingNotExistingKey()
	{
		final YLongToIntMap map = new YLongToIntMap();
		assertEquals(map.getEmptyValue(), map.remove(12));
	}

	@Test
	public void shouldReturnPreviousValueWhenRemovingExistingKey()
	{
		final YLongToIntMap map = new YLongToIntMap();
		final int expected = 1234;
		map.put(12, expected);
		assertEquals(expected, map.remove(12));
	}

	@Test
	public void shouldBeAbleToPutEmptyValue()
	{
		final YLongToIntMap map = new YLongToIntMap();
		map.put(12, map.getEmptyValue());
	}
}
