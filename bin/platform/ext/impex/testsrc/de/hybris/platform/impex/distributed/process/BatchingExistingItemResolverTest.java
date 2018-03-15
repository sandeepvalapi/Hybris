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
package de.hybris.platform.impex.distributed.process;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.impex.distributed.batch.impl.BatchData;
import de.hybris.platform.impex.distributed.batch.impl.BatchingExistingItemResolver;
import de.hybris.platform.impex.distributed.batch.impl.ImportBatchParser;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.google.common.collect.Sets;


@IntegrationTest
public class BatchingExistingItemResolverTest extends ServicelayerBaseTest
{
	FlexibleSearchService flexibleSearchService;

	@Resource
	ModelService modelService;

	@Resource
	I18NService i18nService;

	@Resource
	CommonI18NService commonI18NService;

	@Resource
	ImportBatchParser importBatchParser;

	private final PropertyConfigSwitcher queryForEachLineSwitch = new PropertyConfigSwitcher(
			"impex.distributed.query.for.each.line");

	private String existingUG1Code;
	private String existingUG2Code;
	private String notExistingUGCode;

	@Before
	public void prepareMocks()
	{
		flexibleSearchService = mock(FlexibleSearchService.class);
		final SearchResult result = mock(SearchResult.class);
		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(result);
	}

	@Before
	public void prepareTestData()
	{
		existingUG1Code = "EXISTING_UG_1" + UUID.randomUUID();
		existingUG2Code = "EXISTING_UG_2" + UUID.randomUUID();
		notExistingUGCode = "NOT_EXISTING_UG_" + UUID.randomUUID();

		Stream.of(existingUG1Code, existingUG2Code).forEach(c -> {
			final EnumerationValueModel userPriceGroup = modelService.create(UserPriceGroup._TYPECODE);

			userPriceGroup.setCode(c);
			userPriceGroup.setName(c);
		});

		modelService.saveAll();
	}

	@Before
	public void turnOffQueryForEachLineSwitch()
	{
		queryForEachLineSwitch.switchToValue("false");
	}

	@After
	public void restoreQueryForEachLineSwitch()
	{
		queryForEachLineSwitch.switchBackToDefault();
	}

	@Test
	public void shouldQueryAllItemsInOneGoWhenThereAreNoEmptyAndMissingReferencesAndQueryForEachLineSwitchIsTurnedOff()
	{
		readFully("INSERT_UPDATE PriceRow;ug(code)[unique=true];productId[unique=true];price", //
				impExRow(existingUG1Code, "ala", "23.34"), //
				impExRow(existingUG2Code, "ola", "23.34"), //
				impExRow(existingUG1Code, "ela", "23.34"));


		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code, existingUG2Code, "ala", "ola", "ela"));

		verifyNoMoreInteractions(flexibleSearchService);
	}

	@Test
	public void shouldQueryAllItemsInOneGoWhenThereAreNoEmptyAndMissingReferencesAndQueryForEachLineSwitchIsTurnedOn()
	{
		turnOnQueryForEachLineSwitch();

		readFully("INSERT_UPDATE PriceRow;ug(code)[unique=true];productId[unique=true];price", //
				impExRow(existingUG1Code, "ala", "23.34"), //
				impExRow(existingUG2Code, "ola", "23.34"), //
				impExRow(existingUG1Code, "ela", "23.34"));


		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code, "ala"));
		verify(flexibleSearchService).search(withQueryParameters(existingUG2Code, "ola"));
		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code, "ela"));

		verifyNoMoreInteractions(flexibleSearchService);
	}

	@Test
	public void shouldQueryLineByLineWhenThereAreMissingReferencesAndQueryForEachLineSwitchIsTurnedOff()
	{
		readFully("INSERT_UPDATE PriceRow;ug(code)[unique=true];productId[unique=true];price", //
				impExRow(existingUG1Code, "ala", "23.34"), //
				impExRow(notExistingUGCode, "ola", "23.34"), //
				impExRow(existingUG1Code, "ela", "23.34"));


		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code, "ala"));
		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code, "ela"));

		verifyNoMoreInteractions(flexibleSearchService);
	}

	@Test
	public void shouldQueryLineByLineWhenThereAreEmptyReferencesAndQueryForEachLineSwitchIsTurnedOff()
	{
		readFully("INSERT_UPDATE PriceRow;ug(code)[unique=true];productId[unique=true];price", //
				impExRow(existingUG1Code, "ala", "23.34"), //
				impExRow(notExistingUGCode, "", "23.34"), //
				impExRow(existingUG1Code, "ela", "23.34"));


		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code, "ala"));
		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code, "ela"));

		verifyNoMoreInteractions(flexibleSearchService);
	}

	@Test
	public void shouldQueryLineByLineWhenThereAreEmptyAndMissingReferencesAndQueryForEachLineSwitchIsTurnedOff()
	{
		readFully("INSERT_UPDATE PriceRow;ug(code)[unique=true];productId[unique=true];price", //
				impExRow(existingUG1Code, "ala", "23.34"), //
				impExRow(notExistingUGCode, "ola", "23.34"), //
				impExRow(existingUG1Code, "", "23.34"));


		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code, "ala"));
		verify(flexibleSearchService).search(withQueryParameters(existingUG1Code));

		verifyNoMoreInteractions(flexibleSearchService);
	}

	@Test
	public void shouldNotQueryAnyLineWhenAllReferenceAreMissing()
	{
		readFully("INSERT_UPDATE PriceRow;ug(code)[unique=true];productId[unique=true];price", //
				impExRow(notExistingUGCode, "ala", "23.34"), //
				impExRow(notExistingUGCode, "ola", "23.34"), //
				impExRow(notExistingUGCode, "ela", "23.34"));

		verifyZeroInteractions(flexibleSearchService);
	}

	private void turnOnQueryForEachLineSwitch()
	{
		queryForEachLineSwitch.switchToValue("true");
	}

	private String impExRow(final String... cells)
	{
		return Stream.concat(Stream.of(""), Stream.of(cells)).collect(Collectors.joining(";"));
	}

	private void readFully(final String... lines)
	{
		final String input = Stream.of(lines).collect(Collectors.joining("\n"));

		final Importer importer = new Importer(input, ImportMetadata.empty(), ImportMetadata.empty());

		final BatchData batchData = importBatchParser.parse(importer);

		final BatchingExistingItemResolver resolver = new BatchingExistingItemResolver(flexibleSearchService, batchData,
				i18nService, commonI18NService);

		batchData.getImportData().forEach(resolver::findExisting);
	}

	private FlexibleSearchQuery withQueryParameters(final String... params)
	{
		return argThat(containsParams(params));
	}

	private Matcher<FlexibleSearchQuery> containsParams(final String... expectedParams)
	{
		return new ArgumentMatcher<FlexibleSearchQuery>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				final Set<String> queryParams = query.getQueryParameters().values().stream().map(Objects::toString)
						.collect(Collectors.toSet());
				return queryParams.equals(Sets.newHashSet(expectedParams));
			}

			@Override
			public void describeTo(final Description description)
			{
				final String allParams = Stream.of(expectedParams).map(Objects::toString).collect(Collectors.joining(", "));
				description.appendText("Query with the following parameters: {" + allParams + "}");
			}
		};
	}

}
