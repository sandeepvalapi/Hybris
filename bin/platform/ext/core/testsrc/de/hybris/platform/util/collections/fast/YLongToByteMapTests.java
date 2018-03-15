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
public class YLongToByteMapTests
{

	@Test
	public void shouldBeAbleToCreateByDefaultConstructor()
	{
		new YLongToByteMap();
	}

	@Test
	public void shouldBeAbleToCreateWithInitialCapacity()
	{
		new YLongToByteMap(123);
	}

	@Test
	public void shouldBeAbleToPutValuesForTheSameKey()
	{
		final YLongToByteMap map = new YLongToByteMap();
		map.put(10L, (byte) 64);
		map.put(10L, (byte) 128);
	}

	@Test
	public void shouldNotFailOnGettingNonExistingKey()
	{
		final YLongToByteMap map = new YLongToByteMap();
		map.get(12);
	}

	@Test
	public void shouldBeAbleToGetExistingValue()
	{
		final YLongToByteMap map = new YLongToByteMap();
		final byte expected = 123;
		map.put(12, expected);
		assertEquals(expected, map.get(12));
	}

	@Test
	public void shouldReturnEmptyValueWhenGettingNotExistingValue()
	{
		final YLongToByteMap map = new YLongToByteMap();
		assertEquals(map.getEmptyValue(), map.get(12));
	}

	@Test
	public void shouldReturnEmptyValueWhenPuttingKeyForTheFirstTime()
	{
		final YLongToByteMap map = new YLongToByteMap();
		assertEquals(map.getEmptyValue(), map.put(12, (byte) 42));
	}

	@Test
	public void shouldReturnPreviousValueWhenExistingKeyIsBeeingReplaced()
	{
		final YLongToByteMap map = new YLongToByteMap();
		final byte expected = 123;
		map.put(12, expected);
		assertEquals(expected, map.put(12, (byte) 210));
	}

	@Test
	public void shouldBeAbleToPutEmptyValue()
	{
		final YLongToByteMap map = new YLongToByteMap();
		map.put(12, map.getEmptyValue());
	}
}
