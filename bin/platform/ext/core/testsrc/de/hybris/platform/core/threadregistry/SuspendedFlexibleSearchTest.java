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
package de.hybris.platform.core.threadregistry;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.internal.FlexibleSearchExecutor;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.impl.DefaultFlexibleSearchService;
import de.hybris.platform.servicelayer.search.internal.preprocessor.QueryPreprocessorRegistry;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;


@SuppressWarnings("deprecation")
@IntegrationTest
public class SuspendedFlexibleSearchTest extends ServicelayerBaseTest
{
	@Resource
	private SessionService sessionService;

	@Resource
	private QueryPreprocessorRegistry queryPreprocessorRegistry;

	@Resource
	private ModelService modelService;

	@Test
	public void shouldThrowSystemIsSuspendedExceptionFromFlexibleSearchWhenSystemIsSuspended()
	{
		final FlexibleSearch fs = givenSuspendedFlexibleSearch();

		assertThatExceptionOfType(SystemIsSuspendedException.class)
				.isThrownBy(() -> fs.search("select {PK} from {Title}", List.class)).withNoCause().withMessage("Expected");
	}

	@Test
	public void shouldThrowSystemIsSuspendedExceptionFromDefaultFlexibleSearchServiceWhenSystemIsSuspended()
	{
		final FlexibleSearchService fs = givenSuspendedFlexibleSearchService();
		assertThatExceptionOfType(SystemIsSuspendedException.class).isThrownBy(() -> fs.search("select {PK} from {Title}"))
				.withNoCause().withMessage("Expected");
	}

	private FlexibleSearchService givenSuspendedFlexibleSearchService()
	{
		final DefaultFlexibleSearchService fs = new DefaultFlexibleSearchService()
		{
			@Override
			protected FlexibleSearch getFlexibleSearchInstance()
			{
				return givenSuspendedFlexibleSearch();
			}

			@Override
			public QueryPreprocessorRegistry lookupQueryPreprocessorRegistry()
			{
				return queryPreprocessorRegistry;
			}
		};
		fs.setSessionService(sessionService);
		fs.setModelService(modelService);
		return fs;
	}

	private FlexibleSearch givenSuspendedFlexibleSearch()
	{
		return new SuspendedFlexibleSearch();
	}

	private static class SuspendedFlexibleSearch extends FlexibleSearch
	{
		public SuspendedFlexibleSearch()
		{
			super(new FlexibleSearchExecutor(Registry.getCurrentTenantNoFallback())
			{
				@Override
				public SearchResult execute(final int start, final int count, final boolean dontNeedTotal,
						final TranslatedQuery translatedQuery, final java.util.List<Class<?>> resultClasses, final Map values,
						final PK languagePK, final int prefetchSize, final Set<PK> prefetchLanguages)
				{
					throw new SystemIsSuspendedException("Expected");
				}
			});
		}
	}
}
