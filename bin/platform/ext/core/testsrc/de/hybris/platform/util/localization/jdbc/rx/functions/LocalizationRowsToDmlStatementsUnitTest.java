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
package de.hybris.platform.util.localization.jdbc.rx.functions;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.util.localization.jdbc.LocalizableRowWithName;
import de.hybris.platform.util.localization.jdbc.LocalizableRowWithNameAndDescription;
import de.hybris.platform.util.localization.jdbc.LocalizationInfo;
import de.hybris.platform.util.localization.jdbc.StatementWithParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LocalizationRowsToDmlStatementsUnitTest
{
	private static final String ITEM_WITH_NAME_TABLE_NAME = "ITEM_WITH_NAME";
	private static final long ITEM_WITH_NAME_TYPE_PK = 123456789;
	private static final String ITEM_WITH_NAME_AND_DESCRIPTION_TABLE_NAME = "ITEM_WITH_NAME_AND_DESCRIPTION";
	private static final long ITEM_WITH_NAME_AND_DESCRIPTION_TYPE_PK = 987654321;
	private static final int NUMBER_OF_KNOWN_LANGUAGES = 3;
	private static final PK PL_LANGUAGE_PK = PK.fromLong(123);
	private static final PK EN_LANGUAGE_PK = PK.fromLong(456);
	private static final PK DE_LANGUAGE_PK = PK.fromLong(789);
	private static final PK UNKNOWN_LANGUAGE_PK = PK.fromLong(666);
	private static final String KNOWN_NAME_KEY = "known.name";
	private static final String KNOWN_NAME_VALUE_PL = "Karetka";
	private static final String KNOWN_NAME_VALUE_EN = "Ambulance";
	private static final String KNOWN_NAME_VALUE_DE = "Krankenwagen";
	private static final String KNOWN_DESCRIPTION_KEY = "known.description";
	private static final String KNOWN_DESCRIPTION_VALUE_PL = "To jest karetka";
	private static final String KNOWN_DESCRIPTION_VALUE_EN = "This is the ambulance";
	private static final String KNOWN_DESCRIPTION_VALUE_DE = "Dies ist der Krankenwagen";
	private static final String UNKNOWN_NAME_KEY = "unknown.name";
	private static final String UNKNOWN_DESCRIPTION_KEY = "unknown.description";

	@Mock
	private LocalizationInfo localizationInfo;

	private LocalizationRowsToDmlStatements localizationRowsToDmlStatements;

	@Before
	public void setUp()
	{
		when(localizationInfo.getLanguagePKs()).thenReturn(ImmutableSet.of(PL_LANGUAGE_PK, EN_LANGUAGE_PK, DE_LANGUAGE_PK));
		when(localizationInfo.getLocalizedProperty(PL_LANGUAGE_PK, KNOWN_NAME_KEY)).thenReturn(KNOWN_NAME_VALUE_PL);
		when(localizationInfo.getLocalizedProperty(EN_LANGUAGE_PK, KNOWN_NAME_KEY)).thenReturn(KNOWN_NAME_VALUE_EN);
		when(localizationInfo.getLocalizedProperty(DE_LANGUAGE_PK, KNOWN_NAME_KEY)).thenReturn(KNOWN_NAME_VALUE_DE);
		when(localizationInfo.getLocalizedProperty(PL_LANGUAGE_PK, KNOWN_DESCRIPTION_KEY)).thenReturn(KNOWN_DESCRIPTION_VALUE_PL);
		when(localizationInfo.getLocalizedProperty(EN_LANGUAGE_PK, KNOWN_DESCRIPTION_KEY)).thenReturn(KNOWN_DESCRIPTION_VALUE_EN);
		when(localizationInfo.getLocalizedProperty(DE_LANGUAGE_PK, KNOWN_DESCRIPTION_KEY)).thenReturn(KNOWN_DESCRIPTION_VALUE_DE);
		when(localizationInfo.getLocalizedProperty((PK) notNull(), eq(UNKNOWN_NAME_KEY))).thenReturn(null);
		when(localizationInfo.getLocalizedProperty((PK) notNull(), eq(UNKNOWN_DESCRIPTION_KEY))).thenReturn(null);

		localizationRowsToDmlStatements = new LocalizationRowsToDmlStatements(localizationInfo);
	}

	@Test
	public void shouldGenerateUpdateStatementForRowWithNameAndGivenLanguage()
	{
		//given
		final LocalizableRowWithName row = testRow(1, PL_LANGUAGE_PK, KNOWN_NAME_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withName().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		final StatementWithParams pl_statement = getStatementForLanguage(statements, PL_LANGUAGE_PK);
		assertThat(pl_statement.getStatement().toLowerCase()).startsWith("update");
	}

	@Test
	public void shouldGenerateInsertStatementForRowWithNameAndGivenLanguage()
	{
		//given
		final LocalizableRowWithName pl_row = testRow(1, PL_LANGUAGE_PK, KNOWN_NAME_KEY);
		final LocalizableRowWithName en_row = testRow(1, EN_LANGUAGE_PK, KNOWN_NAME_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withName().call(
				ImmutableList.of(pl_row, en_row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		final StatementWithParams de_statement = getStatementForLanguage(statements, DE_LANGUAGE_PK);
		assertThat(de_statement.getStatement().toLowerCase()).startsWith("insert");
	}

	@Test
	public void shouldGenerateInsertStatementsForRowWithNameAndWithoutGivenLanguage()
	{
		//given
		final LocalizableRowWithName row = testRow(1, null, KNOWN_NAME_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withName().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		final StatementWithParams pl_statement = getStatementForLanguage(statements, PL_LANGUAGE_PK);
		final StatementWithParams en_statement = getStatementForLanguage(statements, EN_LANGUAGE_PK);
		final StatementWithParams de_statement = getStatementForLanguage(statements, DE_LANGUAGE_PK);
		assertThat(pl_statement.getStatement().toLowerCase()).startsWith("insert");
		assertThat(en_statement.getStatement().toLowerCase()).startsWith("insert");
		assertThat(de_statement.getStatement().toLowerCase()).startsWith("insert");
	}

	@Test
	public void shouldNotGenerateStatementsForRowWithNameAndUnknownLanguage()
	{
		//given
		final LocalizableRowWithName row = testRow(1, UNKNOWN_LANGUAGE_PK, KNOWN_NAME_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withName().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		final StatementWithParams unknownLangStatement = getStatementForLanguage(statements, UNKNOWN_LANGUAGE_PK);

		assertThat(unknownLangStatement.isEmpty()).isTrue();
	}

	@Test
	public void shouldGenerateUpdateStatementForRowWithNameDescriptionAndGivenLanguage()
	{
		//given
		final LocalizableRowWithNameAndDescription row = testRow(1, PL_LANGUAGE_PK, KNOWN_NAME_KEY, KNOWN_DESCRIPTION_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withNameAndDescription().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		final StatementWithParams pl_statement = getStatementForLanguage(statements, PL_LANGUAGE_PK);
		assertThat(pl_statement.getStatement().toLowerCase()).startsWith("update");
	}

	@Test
	public void shouldGenerateInsertStatementForRowWithNameDescriptionAndGivenLanguage()
	{
		//given
		final LocalizableRowWithNameAndDescription pl_row = testRow(1, PL_LANGUAGE_PK, KNOWN_NAME_KEY, KNOWN_DESCRIPTION_KEY);
		final LocalizableRowWithNameAndDescription en_row = testRow(1, EN_LANGUAGE_PK, KNOWN_NAME_KEY, KNOWN_DESCRIPTION_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withNameAndDescription().call(
				ImmutableList.of(pl_row, en_row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		final StatementWithParams de_statement = getStatementForLanguage(statements, DE_LANGUAGE_PK);
		assertThat(de_statement.getStatement().toLowerCase()).startsWith("insert");
	}

	@Test
	public void shouldGenerateInsertStatementsForRowWithNameDescriptionAndWithoutGivenLanguage()
	{
		//given
		final LocalizableRowWithNameAndDescription row = testRow(1, null, KNOWN_NAME_KEY, KNOWN_DESCRIPTION_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withNameAndDescription().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		final StatementWithParams pl_statement = getStatementForLanguage(statements, PL_LANGUAGE_PK);
		final StatementWithParams en_statement = getStatementForLanguage(statements, EN_LANGUAGE_PK);
		final StatementWithParams de_statement = getStatementForLanguage(statements, DE_LANGUAGE_PK);
		assertThat(pl_statement.getStatement().toLowerCase()).startsWith("insert");
		assertThat(en_statement.getStatement().toLowerCase()).startsWith("insert");
		assertThat(de_statement.getStatement().toLowerCase()).startsWith("insert");
	}

	@Test
	public void shouldNotGenerateStatementsForRowWithNamedescriptionAndUnknownLanguage()
	{
		//given
		final LocalizableRowWithNameAndDescription row = testRow(1, UNKNOWN_LANGUAGE_PK, KNOWN_NAME_KEY, KNOWN_DESCRIPTION_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withNameAndDescription().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		final StatementWithParams unknownLangStatement = getStatementForLanguage(statements, UNKNOWN_LANGUAGE_PK);

		assertThat(unknownLangStatement.isEmpty()).isTrue();
	}

	@Test
	public void shouldGenerateEmptyStatementsWhenNameIsntKnownForRowWithName()
	{
		//given
		final LocalizableRowWithName row = testRow(1, PL_LANGUAGE_PK, UNKNOWN_NAME_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withName().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		assertThat(statements).containsOnly(StatementWithParams.NONE);
	}

	@Test
	public void shouldGenerateEmptyStatementsWhenNameAndDescriptionArentKnownForRowWithNameAndDescription()
	{
		//given
		final LocalizableRowWithNameAndDescription row = testRow(1, PL_LANGUAGE_PK, UNKNOWN_NAME_KEY, UNKNOWN_DESCRIPTION_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withNameAndDescription().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);
		assertThat(statements).containsOnly(StatementWithParams.NONE);
	}

	@Test
	public void shouldGenerateStatementsForRowWithNameAndDescriptionWhenNameAndDescriptionAreKnown()
	{
		//given
		final LocalizableRowWithNameAndDescription row = testRow(1, PL_LANGUAGE_PK, KNOWN_NAME_KEY, KNOWN_DESCRIPTION_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withNameAndDescription().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);

		final StatementWithParams pl_statement = getStatementForLanguage(statements, PL_LANGUAGE_PK);
		final StatementWithParams en_statement = getStatementForLanguage(statements, EN_LANGUAGE_PK);
		final StatementWithParams de_statement = getStatementForLanguage(statements, DE_LANGUAGE_PK);

		assertThat(pl_statement.isEmpty()).isFalse();
		assertThat(en_statement.isEmpty()).isFalse();
		assertThat(de_statement.isEmpty()).isFalse();
	}

	@Test
	public void shouldGenerateStatementsForRowWithNameAndDescriptionWhenNameIsKnownAndDescriptionIsntKnown()
	{
		//given
		final LocalizableRowWithNameAndDescription row = testRow(1, PL_LANGUAGE_PK, KNOWN_NAME_KEY, UNKNOWN_DESCRIPTION_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withNameAndDescription().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);

		final StatementWithParams pl_statement = getStatementForLanguage(statements, PL_LANGUAGE_PK);
		final StatementWithParams en_statement = getStatementForLanguage(statements, EN_LANGUAGE_PK);
		final StatementWithParams de_statement = getStatementForLanguage(statements, DE_LANGUAGE_PK);

		assertThat(pl_statement.isEmpty()).isFalse();
		assertThat(en_statement.isEmpty()).isFalse();
		assertThat(de_statement.isEmpty()).isFalse();
	}

	@Test
	public void shouldGenerateStatementsForRowWithNameAndDescriptionWhenNameIsntKnownAndDescriptionIsKnown()
	{
		//given
		final LocalizableRowWithNameAndDescription row = testRow(1, PL_LANGUAGE_PK, UNKNOWN_NAME_KEY, KNOWN_DESCRIPTION_KEY);

		//when
		final Observable<StatementWithParams> observableStatements = localizationRowsToDmlStatements.withNameAndDescription().call(
				ImmutableList.of(row));

		//then
		final Iterable<StatementWithParams> statements = getStatements(observableStatements);
		assertThat(statements).isNotNull();
		assertThat(statements).hasSize(NUMBER_OF_KNOWN_LANGUAGES);

		final StatementWithParams pl_statement = getStatementForLanguage(statements, PL_LANGUAGE_PK);
		final StatementWithParams en_statement = getStatementForLanguage(statements, EN_LANGUAGE_PK);
		final StatementWithParams de_statement = getStatementForLanguage(statements, DE_LANGUAGE_PK);

		assertThat(pl_statement.isEmpty()).isFalse();
		assertThat(en_statement.isEmpty()).isFalse();
		assertThat(de_statement.isEmpty()).isFalse();
	}

	private Iterable<StatementWithParams> getStatements(final Observable<StatementWithParams> observableStatements)
	{
		return observableStatements.toBlocking().toIterable();
	}

	private StatementWithParams getStatementForLanguage(final Iterable<StatementWithParams> statements, final PK langPK)
	{
		for (final StatementWithParams statement : statements)
		{
			if (Sets.newHashSet(statement.getParams()).contains(langPK.getLong()))
			{
				return statement;
			}
		}
		return StatementWithParams.NONE;
	}

	private LocalizableRowWithName testRow(final long itemPKValue, final PK langPK, final String namePropertyKey)
	{
		return new LocalizableRowWithName(ITEM_WITH_NAME_TABLE_NAME, itemPKValue, ITEM_WITH_NAME_TYPE_PK, langPK == null ? null
				: langPK.getLong())
		{

			@Override
			public String getNamePropertyKey()
			{
				return namePropertyKey;
			}
		};
	}

	private LocalizableRowWithNameAndDescription testRow(final long itemPK, final PK langPK, final String namePropertyKey,
			final String descriptionPropertyKey)
	{
		return new LocalizableRowWithNameAndDescription(ITEM_WITH_NAME_AND_DESCRIPTION_TABLE_NAME, itemPK,
				ITEM_WITH_NAME_AND_DESCRIPTION_TYPE_PK, langPK == null ? null : langPK.getLong())
		{

			@Override
			public String getNamePropertyKey()
			{
				return namePropertyKey;
			}

			@Override
			public String getDescriptionPropertyKey()
			{
				return descriptionPropertyKey;
			}
		};
	}
}
