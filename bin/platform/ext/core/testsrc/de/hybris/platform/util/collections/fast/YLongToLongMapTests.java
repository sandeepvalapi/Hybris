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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;


@UnitTest
public class YLongToLongMapTests
{
	@Test
	public void shouldBeAbleToCreateByDefaultConstructor()
	{
		new YLongToLongMap();
	}

	@Test
	public void shouldBeAbleToPutValuesForTheSameKey()
	{
		final YLongToLongMap map = new YLongToLongMap();
		map.put(10, 64);
		map.put(10, 128);
	}

	@Test
	public void shouldNotFailOnGettingNonExistingKey()
	{
		final YLongToLongMap map = new YLongToLongMap();
		map.get(12);
	}

	@Test
	public void shouldBeAbleToGetExistingValue()
	{
		final YLongToLongMap map = new YLongToLongMap();
		final int expected = 123;
		map.put(12, expected);
		assertEquals(expected, map.get(12));
	}

	@Test
	public void shouldReturnEmptyValueWhenGettingNotExistingValue()
	{
		final YLongToLongMap map = new YLongToLongMap();
		assertEquals(map.getEmptyValue(), map.get(12));
	}

	@Test
	public void shouldReturnEmptyValueWhenPuttingKeyForTheFirstTime()
	{
		final YLongToLongMap map = new YLongToLongMap();
		assertEquals(map.getEmptyValue(), map.put(12, 42));
	}

	@Test
	public void shouldReturnPreviousValueWhenExistingKeyIsBeingReplaced()
	{
		final YLongToLongMap map = new YLongToLongMap();
		final int expected = 123;
		map.put(12, expected);
		assertEquals(expected, map.put(12, 210));
	}

	@Test
	public void shouldNotContainNotExistingKey()
	{
		final YLongToLongMap map = new YLongToLongMap();
		assertFalse(map.containsKey(12));
	}

	@Test
	public void shouldContainExistingKey()
	{
		final YLongToLongMap map = new YLongToLongMap();
		map.put(12, 123);
		assertTrue(map.containsKey(12));
	}

	@Test
	public void shouldBeAbleToPutEmptyValue()
	{
		final YLongToLongMap map = new YLongToLongMap();
		map.put(12, map.getEmptyValue());
	}
}
