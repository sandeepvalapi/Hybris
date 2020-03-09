/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.job;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commerceservices.enums.QuoteNotificationType;
import de.hybris.platform.commerceservices.event.QuoteExpiredEvent;
import de.hybris.platform.commerceservices.order.dao.CommerceQuoteDao;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.time.TimeService;


/**
 * The job finds quotes that qualify for {@link QuoteNotificationType#EXPIRED} email notification. The notification is
 * sent for all quotes in {@link QuoteState#BUYER_OFFER} state, which have had expired by the time the job runs. The
 * notification is then marked on the {@link QuoteModel} so that it is not sent again once the job runs next time.
 *
 * @since 6.4
 */
public class QuoteExpiredJobPerformable extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(QuoteExpiredJobPerformable.class);

	private Set<QuoteState> supportedQuoteStatuses;

	private CommerceQuoteDao commerceQuoteDao;

	private EventService eventService;

	private TimeService timeService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final Date currentDate = getTimeService().getCurrentTime();

		final SearchResult<QuoteModel> searchResult = getCommerceQuoteDao().findQuotesExpired(currentDate,
				QuoteNotificationType.EXPIRED, getSupportedQuoteStatuses());

		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Quotes expired as of %s: %s", currentDate,
					searchResult.getResult().stream().map(AbstractOrderModel::getCode).collect(Collectors.joining(", "))));
		}

		searchResult.getResult().stream().forEach(this::publishQuoteExpiredEvent);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected void publishQuoteExpiredEvent(final QuoteModel quoteModel)
	{
		final QuoteExpiredEvent quoteExpiredEvent = new QuoteExpiredEvent(quoteModel);

		getEventService().publishEvent(quoteExpiredEvent);
	}

	protected Set<QuoteState> getSupportedQuoteStatuses()
	{
		return supportedQuoteStatuses;
	}

	@Required
	public void setSupportedQuoteStatuses(final Set<QuoteState> supportedQuoteStatuses)
	{
		this.supportedQuoteStatuses = supportedQuoteStatuses;
	}

	protected CommerceQuoteDao getCommerceQuoteDao()
	{
		return commerceQuoteDao;
	}

	@Required
	public void setCommerceQuoteDao(final CommerceQuoteDao commerceQuoteDao)
	{
		this.commerceQuoteDao = commerceQuoteDao;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}
}
