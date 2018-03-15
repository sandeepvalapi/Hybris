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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.Assert;

import java.util.Arrays;

import org.junit.Test;


@UnitTest
public class YLongSetTests
{
	@Test
	public void shouldBeAbleToCreateByDefaultConstructor()
	{
		new YLongSet();
	}

	@Test
	public void shouldBeAbleToCreateWithInitialCapacity()
	{
		new YLongSet(123);
	}

	@Test
	public void shouldBeAbleToAddValue()
	{
		new YLongSet().add(10);
	}

	@Test
	public void shouldBeAbleToAddMultipleDistinctValues()
	{
		final YLongSet set = new YLongSet();
		set.add(Long.MIN_VALUE);
		set.add(-1);
		set.add(0);
		set.add(1);
		set.add(Long.MAX_VALUE);
	}

	@Test
	public void shouldBeAbleToAddTheSameValueMultipleTimes()
	{
		final YLongSet set = new YLongSet();
		set.add(1);
		set.add(1);
	}

	@Test
	public void addingNotExistingValueShouldModifySet()
	{
		final YLongSet set = new YLongSet();
		assertTrue(set.add(1));
		assertTrue(set.add(2));
	}

	@Test
	public void addingExistingValueShouldNotModifySet()
	{
		final YLongSet set = new YLongSet();
		set.add(1);
		assertFalse(set.add(1));
	}

	@Test
	public void containsShouldReturnTrueForExistingValue()
	{
		final YLongSet set = new YLongSet();
		set.add(10);
		assertTrue(set.contains(10));
	}

	@Test
	public void containsShouldReturnFalseForNotExistingValue()
	{
		assertFalse(new YLongSet().contains(11));
	}

	@Test
	public void setWithoutValuesShouldBeEmpty()
	{
		assertTrue(new YLongSet().isEmpty());
	}

	@Test
	public void setWithAnyValueShoulNotdBeEmpty()
	{
		final YLongSet set = new YLongSet();
		set.add(10);
		assertFalse(set.isEmpty());
	}

	@Test
	public void shouldBeAbleToRemoveNotExistingValue()
	{
		new YLongSet().remove(10);
	}

	@Test
	public void shouldBeAbleToRemoveExistingValue()
	{
		final YLongSet set = new YLongSet();
		set.add(10);
		set.remove(10);
		assertFalse(set.contains(10));
	}

	@Test
	public void removingOfNotExistingValueShouldNotModifySet()
	{
		assertFalse(new YLongSet().remove(123));
	}

	@Test
	public void removingOfExistingObjectShouldModifySet()
	{
		final YLongSet set = new YLongSet();
		set.add(10);
		assertTrue(set.remove(10));
	}

	@Test
	public void shouldReturnZeroSizeForEmptySet()
	{
		assertEquals(new YLongSet().size(), 0);
		assertEquals(new YLongSet(1234).size(), 0);
	}

	@Test
	public void shouldReturnProperSizeOfNonEmptySet()
	{
		final YLongSet list = new YLongSet();
		list.add(0);
		assertEquals(1, list.size());
		list.add(1);
		assertEquals(2, list.size());
	}

	@Test
	public void shouldReturnEmptyArrayForEmptySet()
	{
		assertEquals(new YLongSet().toArray().length, 0);
	}

	@Test
	public void shouldReturnArrayWithValues()
	{
		final long[] expected = new long[]
		{ 1, 2, 3, 4, 5 };
		final YLongSet set = new YLongSet();
		for (int i = 0; i < expected.length * 2; i++)
		{
			set.add(expected[(3 + i) % expected.length]);
		}
		final long[] result = set.toArray();
		Arrays.sort(result);
		assertArrayEquals(expected, result);
	}

	@Test
	public void shouldBeAbleToCompareTwoSets()
	{
		final YLongSet set1 = new YLongSet();
		final YLongSet set2 = new YLongSet();
		final long numberOfElements = 5;
		for (int i = 0; i < numberOfElements; i++)
		{
			set1.add(i);
			set2.add(numberOfElements - 1 - i);
		}
		Assert.assertEquals(set1, set2);
	}

	@Test
	public void shouldBeAbleToCompareTwoDifferentSets()
	{
		final YLongSet set1 = new YLongSet();
		final YLongSet set2 = new YLongSet();
		final long numberOfElements = 5;
		for (int i = 0; i < numberOfElements; i++)
		{
			set1.add(i);
			set2.add(numberOfElements + i);
		}
		Assert.assertNotEquals(set1, set2);
	}
}
