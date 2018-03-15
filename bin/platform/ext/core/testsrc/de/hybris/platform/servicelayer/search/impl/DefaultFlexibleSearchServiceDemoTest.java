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
package de.hybris.platform.servicelayer.search.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.TranslationResult;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


/**
 * Tests demonstrating usage of the flexible search service.
 */
@DemoTest
public class DefaultFlexibleSearchServiceDemoTest extends ServicelayerBaseTest
{

	private static final String DE_ISOCODE = "de";
	private static final String EN_ISOCODE = "en";
	private static final String THIRD_ISOCODE = "---";
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;
	@Resource
	private I18NService i18nService;
	@Resource
	private CommonI18NService commonI18NService;

	private Locale localeEN;

	@Before
	public void setUp()
	{
		final LanguageModel langEN = getOrCreateLanguageModel(EN_ISOCODE);
		langEN.setActive(Boolean.TRUE);
		modelService.save(langEN);
		final LanguageModel langDE = getOrCreateLanguageModel(DE_ISOCODE);
		langDE.setActive(Boolean.TRUE);
		modelService.save(langDE);
		final LanguageModel lang = getOrCreateLanguageModel(THIRD_ISOCODE);
		lang.setActive(Boolean.TRUE);
		modelService.save(lang);
		localeEN = new Locale(EN_ISOCODE);
		i18nService.setCurrentLocale(localeEN);
	}

	/**
	 * @param isocode
	 *           Language's isocde.
	 * @return existing or new LanguageModel for given isocode.
	 */
	private LanguageModel getOrCreateLanguageModel(final String isocode)
	{
		LanguageModel lang = null;
		try
		{
			lang = commonI18NService.getLanguage(isocode);
		}
		catch (final UnknownIdentifierException e)
		{
			lang = new LanguageModel();
			lang.setIsocode(isocode);
			modelService.attach(lang);
		}
		finally
		{
			modelService.save(lang);
		}
		return lang;
	}

	/**
	 * Demonstrates how to use the flexible search service to get any item model by example model.
	 * <p/>
	 * Test scenario:<br />
	 * <p/>
	 * - prepare new model as example<br />
	 * - search for any model with usage of method {@code FlexibleSearchService#getModelByExample(Object)}<br />
	 */
	@Test
	public void searchModelByExample()
	{
		// (given) prepare new model as example
		final LanguageModel example = modelService.create(LanguageModel.class);
		example.setIsocode("de");
		example.setActive(Boolean.TRUE);
		assertThat(modelService.isNew(example)).isTrue();

		// (when) search with usage of created example
		final LanguageModel foundModel = flexibleSearchService.getModelByExample(example);

		// (then) check results
		assertThat(foundModel).isNotNull();
		assertThat(foundModel).isInstanceOf(LanguageModel.class);
		assertThat(foundModel.getIsocode()).isEqualTo("de");
		assertThat(modelService.isNew(foundModel)).isFalse();
	}

	/**
	 * Demonstrates how to use the flexible search service to get list of item models by example model.
	 * <p/>
	 * Test scenario:<br />
	 * <p/>
	 * - prepare new model as example<br />
	 * - search for list of models with usage of method {@code FlexibleSearchService#getModelsByExample(Object)}<br />
	 */
	@Test
	public void searchModelsByExample()
	{
		// (given) prepare new model as example
		final LanguageModel example = modelService.create(LanguageModel.class);
		example.setIsocode("de");
		example.setActive(Boolean.TRUE);
		assertThat(modelService.isNew(example)).isTrue();

		// (when) search with usage of created example
		final List<LanguageModel> foundModels = flexibleSearchService.getModelsByExample(example);

		// (then) check results
		assertThat(foundModels).isNotNull();
		assertThat(foundModels).hasSize(1);
		assertThat(foundModels.get(0)).isInstanceOf(LanguageModel.class);
		assertThat(foundModels.get(0).getIsocode()).isEqualTo("de");
		assertThat(modelService.isNew(foundModels.get(0))).isFalse();
	}

	/**
	 * Demonstrates how to translate <code>FlexibleSearchQuery</code> object into <code>TranslationResult</code> which
	 * contains translated SQL query and list of parameters.
	 * <p/>
	 * Test scenario:<br />
	 * <p/>
	 * - prepare new model as example<br />
	 * - search for list of models with usage of method {@code FlexibleSearchService#getModelsByExample(Object)}<br />
	 */
	@Test
	public void translateFlexibleSearchQueryIntoTranslationResult()
	{
		final String expectedTranslatedQuery = "SELECT  item_t0.PK  FROM " + getTablePrefix()
				+ "languages item_t0 WHERE ( item_t0.p_active  = ?) AND (item_t0.TypePkString=? ) order by  item_t0.PK ";

		// (given) prepare FlexibleSearchQuery object with query in map of parameters.
		final String query = "SELECT {PK} FROM {Language AS l} WHERE {l.active} = ?value ORDER BY {PK}";
		final Map<String, ?> params = ImmutableMap.of("value", Boolean.TRUE);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query, params);

		// (when) translate FlexibleSearchQuery object
		final TranslationResult translationResult = flexibleSearchService.translate(flexibleSearchQuery);

		// (then) check results
		assertThat(translationResult).isNotNull();


		assertThat(translationResult.getSQLQuery()).isEqualToIgnoringCase(expectedTranslatedQuery);// intentional case
																																 // insensitive
		assertThat(translationResult.getSQLQueryParameters()).hasSize(2);
		assertThat(translationResult.getSQLQueryParameters().get(0)).isEqualTo(Boolean.TRUE);
	}

	/**
	 * Demonstrates how to do search with usage of <code>FlexibleSearchQuery</code> object.
	 * <p/>
	 * Test scenario:<br />
	 * <p/>
	 * - prepare flexible search query object with search criteria<br />
	 * - search for list of models with usage of method {@code FlexibleSearchService#search(FlexibleSearchQuery)}<br />
	 */
	@Test
	public void searchWithUsingFlexibleSearchQueryObject()
	{
		// (given) prepare FlexibleSearchQuery object with search criteria
		final String query = "SELECT {PK} FROM {Language AS l} WHERE {l.active} = ?value ORDER BY {PK}";
		final Map queryParams = ImmutableMap.of("value", Boolean.TRUE);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query, queryParams);

		// (when) search with usage of FlexibleSearchQuery object
		final SearchResult<LanguageModel> searchResult = flexibleSearchService.search(fQuery);

		// (then) check results
		assertThat(searchResult).isNotNull();
		assertThat(searchResult.getResult()).hasSize(3);
		assertThat(searchResult.getResult().get(0).getActive()).isTrue();
		assertThat(searchResult.getResult().get(1).getActive()).isTrue();
		assertThat(searchResult.getResult().get(2).getActive()).isTrue();
	}

	/**
	 * Demonstrates how to do search with usage of standard FlexibleSearch query string.<br />
	 * <p/>
	 * From security point of view this methods is not recommended when you have to use search parameters - in such case
	 * please use {@link FlexibleSearchService#search(String, Map)}.
	 * <p/>
	 * Test scenario:<br />
	 * <p/>
	 * - prepare FlexibleSearch query string<br />
	 * - search for list of models with usage of method {@code FlexibleSearchService#search(String)}<br />
	 */
	@Test
	public void searchWithStandardFlexibleSearchQueryString()
	{
		// (given) prepare FlexibleSearchQuery object with search criteria
		final String query = "SELECT {PK} FROM {Language AS l} ORDER BY {PK}";

		// (when) search with usage of FlexibleSearch query string
		final SearchResult<LanguageModel> searchResult = flexibleSearchService.search(query);

		// (then) check results
		assertThat(searchResult).isNotNull();
		assertThat(searchResult.getResult()).hasSize(3);
	}

	/**
	 * Demonstrates how to do search with usage of standard FlexibleSearch query string with map of search parameters.<br />
	 * <p/>
	 * Test scenario:<br />
	 * <p/>
	 * - prepare FlexibleSearch query string and map of parameters<br />
	 * - search for list of models with usage of method {@code FlexibleSearchService#search(String, Map)}<br />
	 */
	@Test
	public void searchWithStandardFlexibleSearchQueryStringAndMapOfParameters()
	{
		// (given) prepare FlexibleSearchQuery object with search criteria
		final String query = "SELECT {PK} FROM {Language AS l} WHERE {l.active} = ?value ORDER BY {PK}";
		final Map<String, ?> queryParams = ImmutableMap.of("value", Boolean.TRUE);

		// (when) search with usage of FlexibleSearch query string and map of parameters
		final SearchResult<LanguageModel> searchResult = flexibleSearchService.search(query, queryParams);

		// (then) check results
		assertThat(searchResult).isNotNull();
		assertThat(searchResult.getResult()).hasSize(3);
		assertThat(searchResult.getResult().get(0).getActive()).isTrue();
		assertThat(searchResult.getResult().get(1).getActive()).isTrue();
		assertThat(searchResult.getResult().get(2).getActive()).isTrue();
	}

	@Test
	public void searchWithUseOfFlexibleSearchQueryObjectAndCachingDisabled() throws Exception
	{
		// given
		final String query = "SELECT {PK} FROM {Language AS l} WHERE {l.active} = ?value ORDER BY {PK}";
		final Map queryParams = ImmutableMap.of("value", Boolean.TRUE);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query, queryParams);

		// fill cache here !!!
		flexibleSearchService.search(fQuery);

		// when
		fQuery.setDisableCaching(true);
		final SearchResult<LanguageModel> noCacheResult = flexibleSearchService.search(fQuery);

		fQuery.setDisableCaching(false);
		final SearchResult<LanguageModel> fromCacheResult = flexibleSearchService.search(fQuery);

		// then
		assertThat(noCacheResult).isNotNull();
		assertThat(noCacheResult).isInstanceOf(SearchResultImpl.class);
		assertThat(((SearchResultImpl) noCacheResult).isFromCache()).isFalse();
		assertThat(noCacheResult.getResult()).isNotEmpty();
		assertThat(noCacheResult.getResult().get(0).getActive()).isTrue();

		assertThat(fromCacheResult).isNotNull();
		assertThat(fromCacheResult).isInstanceOf(SearchResultImpl.class);
		assertThat(((SearchResultImpl) fromCacheResult).isFromCache()).isTrue();
		assertThat(fromCacheResult.getResult()).isNotEmpty();
		assertThat(fromCacheResult.getResult().get(0).getActive()).isTrue();
	}

	private String getTablePrefix()
	{
		return Registry.getCurrentTenantNoFallback().getDataSource().getTablePrefix();
	}
}
