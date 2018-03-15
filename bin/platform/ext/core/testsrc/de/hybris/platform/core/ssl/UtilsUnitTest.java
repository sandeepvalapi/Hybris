/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.core.ssl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Objects;

import org.junit.Test;


@UnitTest
public class UtilsUnitTest
{
	@Test
	public void shouldThrowNullPointerExceptionWhenTryingToFindInstancesOfNullClass()
	{
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Utils.getFirstInstanceOfOrNull(null, "ala"));
	}

	@Test
	public void shouldReturnNullWhenTryingToGetInstanceAndThereAreNoElements()
	{
		final String res = Utils.getFirstInstanceOfOrNull(String.class);

		assertThat(res).isNull();
	}

	@Test
	public void shouldReturnNullWhenTryingToGetInstanceAndThereAreMatchingElements()
	{
		final String res = Utils.getFirstInstanceOfOrNull(String.class, Integer.valueOf(12));

		assertThat(res).isNull();
	}

	@Test
	public void shouldReturnNullWhenTryingToGetInstanceAndThereAreOnlyNullElements()
	{
		final String res = Utils.getFirstInstanceOfOrNull(String.class, null, null);

		assertThat(res).isNull();
	}

	@Test
	public void shouldReturnNullWhenTryingToGetInstanceAndThereAreOnlyNullOrNotMatchingElements()
	{
		final String res = Utils.getFirstInstanceOfOrNull(String.class, Integer.valueOf(12), null);

		assertThat(res).isNull();
	}

	@Test
	public void shouldReturnFirstInstanceOfAGivenClassWhenThereIsASingleElement()
	{
		final String e1 = "e1";

		final String res = Utils.getFirstInstanceOfOrNull(String.class, e1);

		assertThat(res).isNotNull().isSameAs(e1);
	}

	@Test
	public void shouldReturnFirstInstanceOfAGivenClassWhenThereAreMultipleMatchinElements()
	{
		final String e1 = "e1";
		final String e2 = "e2";

		final String res = Utils.getFirstInstanceOfOrNull(String.class, e1, e2);

		assertThat(res).isNotNull().isSameAs(e1);
	}

	@Test
	public void shouldReturnFirstInstanceOfAGivenClassWhenThereIsASingleMatchingElementAndMultipleNotMatchingOnes()
	{
		final String e1 = "e1";

		final String res = Utils.getFirstInstanceOfOrNull(String.class, Integer.valueOf(1), e1, Integer.valueOf(2));

		assertThat(res).isNotNull().isSameAs(e1);
	}

	@Test
	public void shouldReturnNullWhenThereAreNoElements()
	{
		final Object res = Utils.getFirstOrNull(TestItem::extract);

		assertThat(res).isNull();
	}

	@Test
	public void shouldReturnNullWhenThereAreOnlyNullElements()
	{
		final Object res = Utils.getFirstOrNull(TestItem::extract, null, null);

		assertThat(res).isNull();
	}

	@Test
	public void shouldReturnNullWhenThereAreOnlyElementsWithNullValues()
	{
		final Object res = Utils.getFirstOrNull(TestItem::extract, TestItem.empty());

		assertThat(res).isNull();
	}

	@Test
	public void shouldReturnFirstNonNullElementWhenThereAreNullElements()
	{
		final String expected = "expected";

		final Object res = Utils.getFirstOrNull(TestItem::extract, null, TestItem.of(expected));

		assertThat(res).isNotNull().isSameAs(expected);
	}

	@Test
	public void shouldReturnFirstNonNullElementWhenThereAreElementsWithNullValues()
	{
		final String expected = "expected";

		final Object res = Utils.getFirstOrNull(TestItem::extract, TestItem.empty(), TestItem.of(expected));

		assertThat(res).isNotNull().isSameAs(expected);
	}

	@Test
	public void shouldReturnFirstNonNullElementWhenThereAreNullElementsAndElementsWithNullValues()
	{
		final String expected = "expected";

		final Object res = Utils.getFirstOrNull(TestItem::extract, null, TestItem.empty(), TestItem.of(expected));

		assertThat(res).isNotNull().isSameAs(expected);
	}

	@Test
	public void shouldMergeNullElementsToAnEmptyArray()
	{
		final String[] result = Utils.mergeArrays(String.class, TestArray::extract, null, null);

		assertThat(result).isNotNull().isEmpty();
	}

	@Test
	public void shouldMergeEmptyArraysToAnEmptyArray()
	{
		final String[] result = Utils.mergeArrays(String.class, TestArray::extract, TestArray.of(), TestArray.of());

		assertThat(result).isNotNull().isEmpty();
	}

	@Test
	public void shouldMergeArraysAndGetRidOfDuplicatesAndPreserveOrder()
	{
		final String[] result = Utils.mergeArrays(String.class, TestArray::extract, null, TestArray.of("ala", null, "ela", "ala"),
				TestArray.of(), TestArray.of(null, "ala", "ola"));

		assertThat(result).isNotNull().isNotEmpty().hasSize(4).containsExactly("ala", null, "ela", "ola");
	}

	static class TestArray
	{
		private final String[] array;

		public static TestArray of(final String... strings)
		{
			return new TestArray(strings);
		}

		private TestArray(final String[] array)
		{
			this.array = array;
		}

		public String[] extract()
		{
			return array;
		}
	}


	static class TestItem
	{
		private final Object obj;

		public static TestItem of(final Object obj)
		{
			return new TestItem(Objects.requireNonNull(obj));
		}

		public static TestItem empty()
		{
			return new TestItem(null);
		}

		private TestItem(final Object obj)
		{
			this.obj = obj;
		}

		public Object extract()
		{
			return obj;
		}
	}

}
