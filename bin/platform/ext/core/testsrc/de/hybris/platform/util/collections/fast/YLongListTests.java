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
public class YLongListTests
{

	@Test
	public void shouldBeAbleToCreateByDefaultConstructor()
	{
		new YLongList();
	}

	@Test
	public void shouldBeAbleToCreateWithInitialCapacity()
	{
		new YLongList(123);
	}

	@Test
	public void shouldBeAbleToAddValues()
	{
		final YLongList list = new YLongList();
		list.add(Long.MAX_VALUE);
		list.add(-1);
		list.add(0);
		list.add(1);
		list.add(Long.MAX_VALUE);
	}

	@Test
	public void shouldReturnZeroSizeForEmptyList()
	{
		assertEquals(new YLongList().size(), 0);
		assertEquals(new YLongList(1234).size(), 0);
	}

	@Test
	public void shouldReturnProperSizeOfNonEmptyList()
	{
		final YLongList list = new YLongList();
		list.add(0);
		assertEquals(1, list.size());
		list.add(0);
		assertEquals(2, list.size());
	}

	@Test
	public void shouldBeAbleToGetExistingValueByOffset()
	{
		final YLongList list = new YLongList();
		list.add(1);
		assertEquals(list.get(0), 1);
		list.add(2);
		assertEquals(list.get(0), 1);
		assertEquals(list.get(1), 2);
		list.add(10);
		assertEquals(list.get(0), 1);
		assertEquals(list.get(1), 2);
		assertEquals(list.get(2), 10);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void shouldThrowExceptionWhenAccessingNotExistingOffset()
	{
		final YLongList list = new YLongList();
		list.get(0);
	}
}
