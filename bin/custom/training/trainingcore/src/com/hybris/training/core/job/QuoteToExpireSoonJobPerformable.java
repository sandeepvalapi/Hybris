/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.job;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commerceservices.enums.QuoteNotificationType;
import de.hybris.platform.commerceservices.event.QuoteToExpireSoonEvent;
import de.hybris.platform.commerceservices.order.dao.CommerceQuoteDao;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.time.TimeService;


/**
 * The job finds quotes that qualify for {@link QuoteNotificationType#EXPIRING_SOON} email notification. We can specify
 * when to send the notification by specifying the number of days left to quote expiry
 * {@link QuoteToExpireSoonJobPerformable#DAYS_TO_EXPIRE}. A quote qualifies for the notification if it is in
 * {@link QuoteState#BUYER_OFFER} state and expires in DAYS_TO_EXPIRE days from the current date (Set on
 * {@link QuoteModel#EXPIRATIONTIME}).
 * 
 * @since 6.4
 */
public class QuoteToExpireSoonJobPerformable extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(QuoteToExpireSoonJobPerformable.class);

	protected static final String DAYS_TO_EXPIRE = "quotetoexpiresoonjob.daystoexpire";

	protected static final int DEFAULT_DAYS_TO_EXPIRE = 3;

	private ConfigurationService configurationService;

	private Set<QuoteState> supportedQuoteStatuses;

	private CommerceQuoteDao commerceQuoteDao;

	private EventService eventService;

	private TimeService timeService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final LocalDateTime currentDateTime = getCurrentDateTime();
		final Date expiredAfter = toDate(currentDateTime);

		final int daysToExpire = getConfigurationService().getConfiguration().getInt(DAYS_TO_EXPIRE, DEFAULT_DAYS_TO_EXPIRE);
		final Date expiredBy = toDate(currentDateTime.plus(daysToExpire, ChronoUnit.DAYS));

		final SearchResult<QuoteModel> searchResult = getCommerceQuoteDao().findQuotesSoonToExpire(expiredAfter, expiredBy,
				QuoteNotificationType.EXPIRING_SOON, getSupportedQuoteStatuses());

		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Quotes to expire by %s: %s", expiredBy,
					searchResult.getResult().stream().map(AbstractOrderModel::getCode).collect(Collectors.joining(", "))));
		}

		searchResult.getResult().stream().forEach(this::publishQuoteToExpireSoonEvent);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected void publishQuoteToExpireSoonEvent(final QuoteModel quoteModel)
	{
		final QuoteToExpireSoonEvent quoteToExpireSoonEvent = new QuoteToExpireSoonEvent(quoteModel);

		getEventService().publishEvent(quoteToExpireSoonEvent);
	}

	protected Date toDate(final LocalDateTime localDateTime)
	{
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	protected LocalDateTime getCurrentDateTime()
	{
		final Date currentDate = getTimeService().getCurrentTime();
		return LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
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

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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
