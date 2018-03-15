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
package de.hybris.platform.flexiblesearch;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.persistence.flexiblesearch.oracle.InParametersQueryTranslator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class InParametersQueryTranslatorTest extends ServicelayerBaseTest
{
	// SELECT {pk} FROM {Product} WHERE {name} in (?names)
	private final String selectProductByNameInQry = "SELECT  lp_t0.ITEMPK  FROM junit_productslp lp_t0 WHERE ((lp_t0.LANGPK =?0 ) AND ( lp_t0.p_name  in (?names) )) AND (lp_t0.ITEMTYPEPK IN  (?11,?12) )";

	// SELECT {pk} FROM {Product} WHERE {name} in ('bar', 'baz', ?names, 'fuu')
	private final String selectProductByNameWithLiterals = "SELECT  lp_t0.ITEMPK  FROM junit_productslp lp_t0 WHERE ((lp_t0.LANGPK =?0 ) AND ( lp_t0.p_name  in ('doh', 'dah', ?names, 'deh') )) AND (lp_t0.ITEMTYPEPK IN  (?11,?12) )";

	// "SELECT {pk} FROM {Product} WHERE {name} in (?names) AND {code} in (?codes)"
	private final String selectProductByNameAndCode = "SELECT  item_t0.PK  FROM junit_products item_t0 JOIN junit_productslp lp_t0 ON item_t0.PK = lp_t0.ITEMPK AND lp_t0.LANGPK =?0  WHERE ( lp_t0.p_name  in (?names) AND  item_t0.p_code  in (?codes)) AND (item_t0.TypePkString IN  (?12,?13) )";

	private final String selectWithOverlappingTokens = "SELECT  item_t0.PK  FROM junit_products item_t0 JOIN junit_productslp lp_t0 ON item_t0.PK = lp_t0.ITEMPK AND lp_t0.LANGPK =?0  WHERE ( lp_t0.p_name  in (?foo) AND  item_t0.p_code  in (?foobar)) AND (item_t0.TypePkString IN  (?12,?13) )";


	private final String selectWithNoMatchingParam = "SELECT  item_t0.PK  FROM junit_products item_t0 JOIN junit_productslp lp_t0 ON item_t0.PK = lp_t0.ITEMPK AND lp_t0.LANGPK =?0  WHERE ( lp_t0.p_name  in (?foo_0) AND  item_t0.p_code  in (?foobar)) AND (item_t0.TypePkString IN  (?12,?13) )";


	// SELECT {pk} FROM {Product} WHERE {name} in ('bar', 'baz', ?names, 'fuu')
	private final String manyDifferentLiterals = "SELECT  lp_t0.ITEMPK  FROM junit_productslp lp_t0 WHERE ((lp_t0.LANGPK =?0 ) AND ( (lp_t0.p_name  in ('doh', 'dah', ?names))) OR (lp_t0.p_name  in ('doh', ?names  ))) AND (lp_t0.ITEMTYPEPK IN  (?11,?12) )";

	private final String manySameLiterals = "SELECT  lp_t0.ITEMPK  FROM junit_productslp lp_t0 WHERE ((lp_t0.LANGPK =?0 ) AND ( (lp_t0.p_name  in ('doh', ?names))) OR (lp_t0.p_name  in ('doh', ?names  ))) AND (lp_t0.ITEMTYPEPK IN  (?11,?12) )";

	private final String sameArgumentUsedTwice = "SELECT  lp_t0.ITEMPK  FROM junit_productslp lp_t0 WHERE ((lp_t0.LANGPK =?0 ) AND ( (lp_t0.p_name  in ( ?names    ))) OR (lp_t0.p_name  in (?names))) AND (lp_t0.ITEMTYPEPK IN  (?11,?12) )";


	private InParametersQueryTranslator createQueryTranslator(final int inParametersLimit)

	{
		return new InParametersQueryTranslator(inParametersLimit)
		{
			@Override
			protected boolean isOracle()
			{
				return true;
			}
		};
	}

	@Test
	public void shouldAnalyzeAndGenerateWithForInClause()
	{
		// given
		final InParametersQueryTranslator translator = createQueryTranslator(3);
		final Map<String, List<String>> belowMaxParams = ImmutableMap.of("names", ImmutableList.of("foo", "bar", "baz"));
		final Map<String, List<String>> overMaxParams = ImmutableMap.of("names", ImmutableList.of("foo", "bar", "baz", "bazinga"));

		// when
		final List<InParametersQueryTranslator.ExceedingParameter> noExceedingParams = translator
				.analyzeQuery(selectProductByNameInQry, belowMaxParams);
		final List<InParametersQueryTranslator.ExceedingParameter> exceedingParams = translator
				.analyzeQuery(selectProductByNameInQry, overMaxParams);

		// then
		assertThat(noExceedingParams).isEmpty();
		assertThat(exceedingParams).hasSize(1);
		assertThat(exceedingParams.get(0).getParam()).isEqualTo("names");
		assertThat(exceedingParams.get(0).getQryFragments().get(0)).isEqualTo("(?names)");
		assertThat(exceedingParams.get(0).getExtractedLiterals()).isEmpty();
	}

	@Test
	public void shouldAnalyzeInClauseWithLiterals()
	{
		// given
		final InParametersQueryTranslator translator = createQueryTranslator(3);
		final Map<String, List<String>> overMaxParams = ImmutableMap.of("names", ImmutableList.of("foo", "bar", "baz", "bazinga"));

		// when
		final List<InParametersQueryTranslator.ExceedingParameter> exceedingParams = translator
				.analyzeQuery(selectProductByNameWithLiterals, overMaxParams);

		// then
		assertThat(exceedingParams).hasSize(1);
		assertThat(exceedingParams.get(0).getParam()).isEqualTo("names");
		assertThat(exceedingParams.get(0).getQryFragments().get(0)).isEqualTo("('doh', 'dah', ?names, 'deh')");
		assertThat(exceedingParams.get(0).getExtractedLiterals()).containsOnly("'doh'", "'dah'", "'deh'");

		// and when
		final String withQuery = translator.generateWithClause(exceedingParams);

		// then
		assertThat(withQuery).isEqualTo("with with_tbl_names1 (id) as (select 'foo' from dual UNION \n"
				+ "select 'bar' from dual UNION \n" + "select 'baz' from dual UNION \n" + "select 'bazinga' from dual UNION \n"
				+ "select 'doh' from dual UNION \n" + "select 'dah' from dual UNION \n" + "select 'deh' from dual)");
	}


	@Test
	public void shouldAnalyzeInClauseWithMultipleLiterals()
	{
		// given
		final InParametersQueryTranslator translator = createQueryTranslator(3);
		final Map<String, List<String>> overMaxParams = ImmutableMap.of("names", ImmutableList.of("foo", "bar", "baz", "bazinga"));

		// when
		final List<InParametersQueryTranslator.ExceedingParameter> exceedingParams = translator
				.analyzeQuery(manyDifferentLiterals, overMaxParams);

		// then
		assertThat(exceedingParams).hasSize(2);
		assertThat(exceedingParams.get(0).getParam()).isEqualTo("names");
		assertThat(exceedingParams.get(0).getQryFragments().get(0)).isEqualTo("('doh', 'dah', ?names)");
		assertThat(exceedingParams.get(0).getExtractedLiterals()).containsOnly("'doh'", "'dah'");

		assertThat(exceedingParams.get(1).getParam()).isEqualTo("names");
		assertThat(exceedingParams.get(1).getQryFragments().get(0)).isEqualTo("('doh', ?names  )");
		assertThat(exceedingParams.get(1).getExtractedLiterals()).containsOnly("'doh'");

		// and when
		final String withQuery = translator.generateWithClause(exceedingParams);

		// then
		assertThat(withQuery).isEqualTo("with with_tbl_names1 (id) as (select 'foo' from dual UNION \n" +
				"select 'bar' from dual UNION \n" +
				"select 'baz' from dual UNION \n" +
				"select 'bazinga' from dual UNION \n" +
				"select 'doh' from dual UNION \n" +
				"select 'dah' from dual), with_tbl_names2 (id) as (select 'foo' from dual UNION \n" +
				"select 'bar' from dual UNION \n" +
				"select 'baz' from dual UNION \n" +
				"select 'bazinga' from dual UNION \n" +
				"select 'doh' from dual)");
	}

	@Test
	public void shouldAnalyzeInClauseWithMultipleSameLiterals()
	{
		// given
		final InParametersQueryTranslator translator = createQueryTranslator(3);
		final Map<String, List<String>> overMaxParams = ImmutableMap.of("names", ImmutableList.of("foo", "bar", "baz", "bazinga"));

		// when
		final List<InParametersQueryTranslator.ExceedingParameter> exceedingParams = translator
				.analyzeQuery(manySameLiterals, overMaxParams);

		// then
		assertThat(exceedingParams).hasSize(1);
		assertThat(exceedingParams.get(0).getParam()).isEqualTo("names");
		assertThat(exceedingParams.get(0).getQryFragments()).containsOnly("('doh', ?names)", "('doh', ?names  )");
		assertThat(exceedingParams.get(0).getExtractedLiterals()).containsOnly("'doh'");

		// and when
		final String withQuery = translator.generateWithClause(exceedingParams);

		// then
		assertThat(withQuery).isEqualTo("with with_tbl_names1 (id) as (select 'foo' from dual UNION \n" +
				"select 'bar' from dual UNION \n" +
				"select 'baz' from dual UNION \n" +
				"select 'bazinga' from dual UNION \n" +
				"select 'doh' from dual)");
	}


	@Test
	public void shouldAnalyzeInClauseWithSameArguementUsedTwice()
	{
		// given
		final InParametersQueryTranslator translator = createQueryTranslator(3);
		final Map<String, List<String>> overMaxParams = ImmutableMap.of("names", ImmutableList.of("foo", "bar", "baz", "bazinga"));

		// when
		final List<InParametersQueryTranslator.ExceedingParameter> exceedingParams = translator
				.analyzeQuery(sameArgumentUsedTwice, overMaxParams);

		// then
		assertThat(exceedingParams).hasSize(1);
		assertThat(exceedingParams.get(0).getParam()).isEqualTo("names");
		assertThat(exceedingParams.get(0).getQryFragments()).containsOnly("( ?names    )", "(?names)");
		assertThat(exceedingParams.get(0).getExtractedLiterals()).isEmpty();

		// and when
		final String withQuery = translator.generateWithClause(exceedingParams);

		// then
		assertThat(withQuery).isEqualTo("with with_tbl_names1 (id) as (select 'foo' from dual UNION \n" +
				"select 'bar' from dual UNION \n" +
				"select 'baz' from dual UNION \n" +
				"select 'bazinga' from dual)");
	}


	@Test
	public void shouldNotGenerateIfNoExceedingParams()
	{
		final InParametersQueryTranslator translator = createQueryTranslator(3);

		final String withClause = translator.generateWithClause(Collections.emptyList());

		assertThat(withClause).isEmpty();
	}

	@Test
	public void shouldAnalyzeAndGenerateClauseForTwoExceedingInClauses()
	{
		// given
		final InParametersQueryTranslator translator = createQueryTranslator(2);
		final Map<String, List<String>> overMaxParams = ImmutableMap.of("names", ImmutableList.of("foo", "bar", "baz"), "codes",
				ImmutableList.of("cfoo", "cbar", "cbaz"));

		// when
		final List<InParametersQueryTranslator.ExceedingParameter> exceedingParams = translator
				.analyzeQuery(selectProductByNameAndCode, overMaxParams);

		// then
		assertThat(exceedingParams).hasSize(2);
		assertThat(exceedingParams.get(0).getParam()).isEqualTo("names");
		assertThat(exceedingParams.get(0).getQryFragments().get(0)).isEqualTo("(?names)");
		assertThat(exceedingParams.get(0).getExtractedLiterals()).isEmpty();

		assertThat(exceedingParams.get(1).getParam()).isEqualTo("codes");
		assertThat(exceedingParams.get(1).getQryFragments().get(0)).isEqualTo("(?codes)");
		assertThat(exceedingParams.get(1).getExtractedLiterals()).isEmpty();

		// and when
		final String withQuery = translator.generateWithClause(exceedingParams);

		// then
		assertThat(withQuery)
				.isEqualTo("with with_tbl_names1 (id) as (select 'foo' from dual UNION \n" + "select 'bar' from dual UNION \n"
						+ "select 'baz' from dual), with_tbl_codes1 (id) as (select 'cfoo' from dual UNION \n"
						+ "select 'cbar' from dual UNION \n" + "select 'cbaz' from dual)");
	}


	@Test
	public void shouldIdentifyQueryParamThatIsASubstringOfAnother()
	{
		// given
		final InParametersQueryTranslator translator = createQueryTranslator(2);
		final Map<String, List<String>> overMaxParams = ImmutableMap.of("foo", ImmutableList.of("foo", "bar", "baz"), "foobar",
				ImmutableList.of("cfoo", "cbar", "cbaz"));

		// when
		final List<InParametersQueryTranslator.ExceedingParameter> exceedingParams = translator
				.analyzeQuery(selectWithOverlappingTokens, overMaxParams);

		// then
		assertThat(exceedingParams).hasSize(2);
		assertThat(exceedingParams.get(0).getParam()).isEqualTo("foo");
		assertThat(exceedingParams.get(0).getQryFragments().get(0)).isEqualTo("(?foo)");
		assertThat(exceedingParams.get(0).getExtractedLiterals()).isEmpty();

		assertThat(exceedingParams.get(1).getParam()).isEqualTo("foobar");
		assertThat(exceedingParams.get(1).getQryFragments().get(0)).isEqualTo("(?foobar)");
	}

}
