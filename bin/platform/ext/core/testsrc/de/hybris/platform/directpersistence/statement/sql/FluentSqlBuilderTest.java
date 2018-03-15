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
package de.hybris.platform.directpersistence.statement.sql;

import static de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder.max;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;


public class FluentSqlBuilderTest
{
	private static final String TABLE_NAME = "foo";

	private final Set<Object> values = new LinkedHashSet<>();
	private final String val1 = "val1";
	private final String val2 = "val2";
	private final String val3 = "val3";


	@Test
	public void shouldCreateInsertStatementForThreeColumnsWithPlaceholders()
	{
		// given
		values.add(ColumnPayload.builder().declaredTypeClass(String.class).columnName("col1").value(val1).build());
		values.add(ColumnPayload.builder().declaredTypeClass(String.class).columnName("col2").value(val2).build());
		values.add(ColumnPayload.builder().declaredTypeClass(String.class).columnName("col3").value(val3).build());

		// when
		final String sql = builder().insert().into(TABLE_NAME).usingFields("col1", "col2", "col3").values(values).toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("INSERT INTO foo (col1,col2,col3) VALUES (?,?,?)");
	}

	@Test
	public void shouldCreateDeleteStatementForOneColumn()
	{
		// given
		final String colName = "col1";

		// when
		final String sql = builder().delete().from(TABLE_NAME).where().field(colName).isEqual().toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("DELETE FROM foo WHERE col1=?");
	}

	@Test
	public void shouldCreateDeleteStatementUsingWhereIN()
	{
		// given
		final String colName = "col1";
		values.add(val1);
		values.add(val2);
		values.add(val3);


		// when
		final String sql = builder().delete().from(TABLE_NAME).where().field(colName).in(values).toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("DELETE FROM foo WHERE col1 IN (?,?,?)");
	}

	@Test
	public void shouldCreateDeleteStatementForMoreThanOneColumn()
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";

		// when
		final String sql = builder().delete().from(TABLE_NAME).where().field(colName1).isEqual().and().field(colName2).isEqual()
				.toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("DELETE FROM foo WHERE col1=? AND col2=?");
	}

	@Test
	public void shouldCreateSelectStatementForAllColumns() throws Exception
	{
		// when
        final String sql = builder().selectAll().from(TABLE_NAME).toSql();

        // then
        assertThat(sql).isNotNull();
        assertThat(sql).isEqualTo("SELECT * FROM foo");
	}

	@Test
	public void shouldCreateSelectStatementForOneColumn()
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";

		// when
		final String sql = builder().select(colName1, colName2).from(TABLE_NAME).where().field(colName1).isEqual().toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1,col2 FROM foo WHERE col1=?");
	}

	@Test
	public void shouldCreateSelectStatementForMoreThanOneColumn()
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";

		// when
		final String sql = builder().select(colName1, colName2).from(TABLE_NAME).where().field(colName1).isEqual().and()
				.field(colName2).isEqual().toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1,col2 FROM foo WHERE col1=? AND col2=?");
	}


	@Test
	public void shouldCreateSelectDistinctStatement()
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";

		// when
		final String sql = builder().selectDistinct(colName1, colName2).from(TABLE_NAME).where().field(colName1).isEqual().toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT DISTINCT col1,col2 FROM foo WHERE col1=?");
	}

	@Test
	public void shouldCreateSelectStatementUsingWhereIN()
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";
		values.add(val1);
		values.add(val2);
		values.add(val3);

		// when
		final String sql = builder().select(colName1, colName2).from(TABLE_NAME).where().field(colName1).in(values).toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1,col2 FROM foo WHERE col1 IN (?,?,?)");
	}

	@Test
	public void shouldCreateSelectStatementForMoreThanOneColumnUsingEqualAndNotEqualOperands()
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";
		final String colName3 = "col3";

		// when
		final String sql = builder().select(colName1, colName2).from(TABLE_NAME).where().field(colName1).isEqual().and()
				.field(colName2).isEqual().and().field(colName3).isNotEqual().toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1,col2 FROM foo WHERE col1=? AND col2=? AND col3!=?");
	}

	@Test
	public void shouldCreateSelectStatementWithMaxFunctionOnOneOfTheColumns()
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";

		// when
		final String sql = builder().select(colName1, max(colName2)).from(TABLE_NAME).where().field(colName1).isEqual()
				.groupBy(colName2).toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1,max(col2) FROM foo WHERE col1=? GROUP BY col2");
	}

	@Test
	public void shouldCreateSelectStatementWithMaxFunctionOnOneOfTheColumnsUsinWhereIN()
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";
		values.add(val1);
		values.add(val2);
		values.add(val3);


		// when
		final String sql = builder().select(colName1, max(colName2)).from(TABLE_NAME).where().field(colName1).in(values)
				.groupBy(colName2).toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1,max(col2) FROM foo WHERE col1 IN (?,?,?) GROUP BY col2");
	}

	@Test
	public void shouldCreateUpdateStatement()
	{
		// given
		values.add(val1);
		values.add(val2);
		values.add(val3);

		// when
		final FluentSqlBuilder builder = builder().update(TABLE_NAME).set("col1", "col2", "col3").where().field("col1").isEqual();
		final String sql = builder.toSql();


		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("UPDATE foo SET col1=?,col2=?,col3=? WHERE col1=?");
	}

	@Test
	public void shouldCreateSelectStatementWithIsNullWhereClause() throws Exception
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";

		// when
		final FluentSqlBuilder builder = builder().select(colName1, colName2).from(TABLE_NAME).where().field(colName1).isNull()
				.and().field(colName2).isNotNull();
		final String sql = builder.toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1,col2 FROM foo WHERE col1 IS NULL  AND col2 IS NOT NULL");
	}

	@Test
	public void shouldUnionTwoBuildersWithStatements()
	{
		// given
		final FluentSqlBuilder builder1 = builder().select("foo", "bar", "baz").from(TABLE_NAME).where().field("foo").isEqual();
		final FluentSqlBuilder builder2 = builder().select("baz", "boom", "moo").from(TABLE_NAME).where().field("moo").isEqual();

		// when
		final FluentSqlBuilder builder = builder1.union(builder2);
		final String sql = builder.toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT foo,bar,baz FROM foo WHERE foo=? UNION SELECT baz,boom,moo FROM foo WHERE moo=?");
	}

	@Test
	public void shouldUnionAllTwoBuildersWithStatements()
	{
		// given
		final FluentSqlBuilder builder1 = builder().select("foo", "bar", "baz").from(TABLE_NAME).where().field("foo").isEqual();
		final FluentSqlBuilder builder2 = builder().select("baz", "boom", "moo").from(TABLE_NAME).where().field("moo").isEqual();

		// when
		final FluentSqlBuilder builder = builder1.unionAll(builder2);
		final String sql = builder.toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT foo,bar,baz FROM foo WHERE foo=? UNION ALL SELECT baz,boom,moo FROM foo WHERE moo=?");
	}

	@Test
	public void shouldCreateModifiedSelectStatementUsingINForOracleWhenINParamsAreGreaterThan1000() throws Exception
	{
		// given
		final List<String> listOfParams = getListOfParams(1200);
		final FluentSqlBuilder builder = FluentSqlBuilder.builder(Config.DatabaseName.ORACLE).select("foo") //
				.from(TABLE_NAME).where().field("bar").in(listOfParams);

		// when
		final String sql = builder.toSql();

		// then
		final String paramsString = buildOracleCompatibleInParams(listOfParams);
		assertThat(sql).isEqualTo("SELECT foo FROM " + TABLE_NAME + " WHERE (bar,'hy') IN (" + paramsString + ")");
	}

	@Test
	public void shouldCreateSelectStatementUsingINForOracleWhenINParamsAreLessOrEqualThan1000() throws Exception
	{
		// given
		final List<String> listOfParams = getListOfParams(1000);
		final FluentSqlBuilder builder = FluentSqlBuilder.builder(Config.DatabaseName.ORACLE).select("foo") //
				.from(TABLE_NAME).where().field("bar").in(listOfParams);

		// when
		final String sql = builder.toSql();

		// then
		final String paramsString = buildOracleCompatibleInParams(listOfParams);
		assertThat(sql).isEqualTo("SELECT foo FROM " + TABLE_NAME + " WHERE bar IN (" + paramsString + ")");
	}

	@Test
	public void shouldCreateSelectStatementUsingINForOracleInCombinedWhereClause() throws Exception
	{
		// given
		final List<String> listOfParams1 = getListOfParams(1000);
		final List<String> listOfParams2 = getListOfParams(1200);
		final List<String> listOfParams3 = getListOfParams(500);
		final FluentSqlBuilder builder = FluentSqlBuilder.builder(Config.DatabaseName.ORACLE).select("foo") //
				.from(TABLE_NAME).where().field("bar").in(listOfParams1).and().field("baz").in(listOfParams2) //
				.or().field("moo").in(listOfParams3);

		// when
		final String sql = builder.toSql();

		// then
		final String paramsString1 = buildOracleCompatibleInParams(listOfParams1);
		final String paramsString2 = buildOracleCompatibleInParams(listOfParams2);
		final String paramsString3 = buildOracleCompatibleInParams(listOfParams3);
		assertThat(sql).isEqualTo(
				"SELECT foo FROM " + TABLE_NAME + " WHERE bar IN (" + paramsString1 + ") AND (baz," + "'hy') IN (" + paramsString2
						+ ") OR moo IN (" + paramsString3 + ")");
	}

	@Test
	public void shouldCreateStatementWithOrderBy() throws Exception
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";

		// when
		final String sql = FluentSqlBuilder.genericBuilder().select(colName1, colName2).from(TABLE_NAME).where().field(colName2)
				.isEqual().orderBy(colName1).toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1,col2 FROM foo WHERE col2=? ORDER BY col1");

	}

	@Test
	public void shouldCreateStatementWithSubQuery() throws Exception
	{
		// given
		final String colName1 = "col1";
		final String colName2 = "col2";
		final FluentSqlBuilder subQuery = FluentSqlBuilder.genericBuilder().select(colName1, colName2).from(TABLE_NAME).where()
				.field(colName2).isEqual();

		// when
		final String sql = FluentSqlBuilder.genericBuilder().select(colName1).from(subQuery).toSql();

		// then
		assertThat(sql).isNotNull();
		assertThat(sql).isEqualTo("SELECT col1 FROM (SELECT col1,col2 FROM foo WHERE col2=?)");
	}

	private List<String> getListOfParams(final int num)
	{
		final List<String> result = new ArrayList<>(num);
		for (int i = 0; i < num; i++)
		{
			result.add("?");
		}
		return result;
	}

	private String buildOracleCompatibleInParams(final List<String> params)
	{
		if (params.size() > 1000)
		{
			return Joiner.on(',').join(Iterables.transform(params, new Function<String, Object>()
			{
				@Override
				public Object apply(final String str)
				{
					return "(" + str + ",'hy')";
				}
			}));
		}
		else
		{
			return Joiner.on(',').join(params);
		}
	}

	private FluentSqlBuilder builder()
	{
		return FluentSqlBuilder.builder(Config.DatabaseName.HSQLDB);
	}
}
