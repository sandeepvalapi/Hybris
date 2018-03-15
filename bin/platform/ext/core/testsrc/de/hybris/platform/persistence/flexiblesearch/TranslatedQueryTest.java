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
package de.hybris.platform.persistence.flexiblesearch;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery.ExecutableQuery;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;


@UnitTest
public class TranslatedQueryTest
{

	@Test
	public void shouldStripOutOrderByClauseFromQueryTemplateForCountQueryAndStripOutOrderByMarkersForSqlQuery()
	{
		// given
		final String queryTemplate = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo [--ORDER BY bar, baz DESC--]";
		final String strippedSql = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo ORDER BY bar, baz DESC";
		final String StrippedCountSql = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo ";
		final List<Object> valueMappings = Lists.newArrayList((Object) "foo");

		// when
		final ExecutableQuery executableQuery = new TranslatedQuery.ExecutableQuery(queryTemplate, valueMappings);


		// then
		assertThat(executableQuery).isNotNull();
		assertThat(executableQuery.getSQL()).isEqualTo(strippedSql);
		assertThat(executableQuery.getCountSQL()).isEqualTo(StrippedCountSql);
		assertThat(executableQuery.getCountValueList()).hasSize(1).isEqualTo(valueMappings);
		assertThat(executableQuery.getCountValueList()).isEqualTo(executableQuery.getValueList());
	}


	@Test
	public void shouldStripOutOrderByClauseFromQueryTemplateForCountQueryAndStripOutOrderByMarkersForSqlQueryAndReduceValueListForCountQuery()
	{
		// given
		final String queryTemplate = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo "
				+ "[--ORDER BY CASE WHEN z.foo = ?something AND z.moo = ?another THEN z.bar ELSE z.baz END --]";
		final String strippedSql = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo "
				+ "ORDER BY CASE WHEN z.foo = ?something AND z.moo = ?another THEN z.bar ELSE z.baz END ";
		final String StrippedCountSql = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo ";
		final List<Object> valueMappings = Lists.newArrayList((Object) "foo", (Object) "caseCondition", (Object) "caseCondition2");

		// when
		final ExecutableQuery executableQuery = new TranslatedQuery.ExecutableQuery(queryTemplate, valueMappings);

		// then
		assertThat(executableQuery).isNotNull();
		assertThat(executableQuery.getSQL()).isEqualTo(strippedSql);
		assertThat(executableQuery.getCountSQL()).isEqualTo(StrippedCountSql);
		assertThat(executableQuery.getCountValueList()).hasSize(1).containsOnly("foo");
	}

	@Test
	public void shouldStripOutMulitlineOrderByClauseFromQueryTemplateForCountQueryAndStripOutOrderByMarkersForSqlQuery()
	{
		// given
		final String queryTemplate = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo [--ORDER BY bar,\nbaz DESC--]";
		final String strippedSql = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo ORDER BY bar,\nbaz DESC";
		final String StrippedCountSql = "SELECT foo, bar FROM (SELECT baz FROM moo ORDER BY pk, moo ASC) AS z WHERE z.foo=?foo ";
		final List<Object> valueMappings = Lists.newArrayList((Object) "foo");

		// when
		final ExecutableQuery executableQuery = new TranslatedQuery.ExecutableQuery(queryTemplate, valueMappings);


		// then
		assertThat(executableQuery).isNotNull();
		assertThat(executableQuery.getSQL()).isEqualTo(strippedSql);
		assertThat(executableQuery.getCountSQL()).isEqualTo(StrippedCountSql);
		assertThat(executableQuery.getCountValueList()).hasSize(1).isEqualTo(valueMappings);
		assertThat(executableQuery.getCountValueList()).isEqualTo(executableQuery.getValueList());
	}
}
