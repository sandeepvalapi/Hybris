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
package de.hybris.platform.util.localization.jdbc.rx.operators;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.localization.jdbc.LocalizableRow;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;


@UnitTest
public class GroupByItemPKValueUnitTest
{
	private GroupByItemPKValue<LocalizableRow> groupByItemPKOperator;

	@Before
	public void setUp()
	{
		groupByItemPKOperator = new GroupByItemPKValue<LocalizableRow>();
	}

	@Test
	public void shouldCompleteWithEmptyGroupWhenThereIsNoRows()
	{
		//given
		final Observable<LocalizableRow> rows = Observable.empty();

		//when
		final List<Iterable<LocalizableRow>> groups = rows.lift(groupByItemPKOperator).toList().toBlocking().single();

		//then
		assertThat(groups).isNotNull();
		assertThat(groups).isEmpty();
	}

	@Test
	public void shouldCompleteWithOneGroupWhenThereIsOnlyOneRow()
	{
		//given
		final LocalizableRow singleRow = testRow(123456789l);
		final Observable<LocalizableRow> rows = Observable.just(singleRow);

		//when
		final List<Iterable<LocalizableRow>> groups = rows.lift(groupByItemPKOperator).toList().toBlocking().single();

		//then
		assertThat(groups).isNotNull();
		assertThat(groups).hasSize(1);
		final Iterable<LocalizableRow> firstGroup = groups.get(0);
		assertThat(firstGroup).containsOnly(singleRow);
	}

	@Test
	public void shouldCompleteWithOneGroupWhenThereIsMoreThanOneRows()
	{
		//given
		final LocalizableRow row1_1 = testRow(1);
		final LocalizableRow row1_2 = testRow(1);
		final LocalizableRow row2_1 = testRow(2);
		final LocalizableRow row3_1 = testRow(3);
		final LocalizableRow row3_2 = testRow(3);
		final LocalizableRow row3_3 = testRow(3);
		final LocalizableRow row4_1 = testRow(4);
		final Observable<LocalizableRow> rows = Observable.just(row1_1, row1_2, row2_1, row3_1, row3_2, row3_3, row4_1);

		//when
		final List<Iterable<LocalizableRow>> groups = rows.lift(groupByItemPKOperator).toList().toBlocking().single();

		//then
		assertThat(groups).isNotNull();
		assertThat(groups).hasSize(4);

		final Iterable<LocalizableRow> group1 = groups.get(0);
		assertThat(group1).containsOnly(row1_1, row1_2);

		final Iterable<LocalizableRow> group2 = groups.get(1);
		assertThat(group2).containsOnly(row2_1);

		final Iterable<LocalizableRow> group3 = groups.get(2);
		assertThat(group3).containsOnly(row3_1, row3_2, row3_3);

		final Iterable<LocalizableRow> group4 = groups.get(3);
		assertThat(group4).containsOnly(row4_1);
	}

	private LocalizableRow testRow(final long rowPKValue)
	{
		return new LocalizableRow("TEST_TABLE", rowPKValue, 123l, null);
	}
}
