/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.job;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.enums.QuoteNotificationType;
import de.hybris.platform.commerceservices.order.dao.CommerceQuoteDao;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.time.TimeService;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class QuoteExpiredJobPerformableTest
{
	@Mock
	protected Set<QuoteState> supportedQuoteStatuses;

	@Mock
	protected CommerceQuoteDao commerceQuoteDao;

	@Mock
	private EventService eventService;

	@Mock
	private ModelService modelService;

	@Mock
	private TimeService timeService;

	@Spy
	@InjectMocks
	private final QuoteExpiredJobPerformable job = new QuoteExpiredJobPerformable();

	@Test
	public void testPerform()
	{
		final Date date1 = new GregorianCalendar(2017, 1, 25, 10, 0, 0).getTime();
		final Date date2 = new GregorianCalendar(2017, 1, 25, 18, 0, 0).getTime(); // current date

		// Mock current date time
		doReturn(date2).when(timeService).getCurrentTime();

		// Mock search results
		final SearchResult<QuoteModel> searchResult = mock(SearchResult.class);
		final QuoteModel quote1 = buildQuoteModel(date1);
		final QuoteModel quote2 = buildQuoteModel(date2);
		doReturn(Arrays.asList(quote1, quote2)).when(searchResult).getResult();
		doReturn(searchResult).when(commerceQuoteDao).findQuotesExpired(eq(date2), any(QuoteNotificationType.class), anySet());

		// Mock cron job
		final CronJobModel cronJob = mock(CronJobModel.class);

		job.perform(cronJob);

		verify(commerceQuoteDao).findQuotesExpired(eq(date2), eq(QuoteNotificationType.EXPIRED), eq(supportedQuoteStatuses));

		searchResult.getResult().stream()
				.forEach(quoteModel -> verify(eventService).publishEvent(argThat(hasProperty("quote", sameInstance(quoteModel)))));
	}

	private QuoteModel buildQuoteModel(final Date expiryTime)
	{
		final QuoteModel quoteModel = mock(QuoteModel.class);
		doReturn(expiryTime).when(quoteModel).getExpirationTime();
		return quoteModel;
	}
}
