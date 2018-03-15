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
package de.hybris.platform.acceleratorstorefrontcommons.forms.validation;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.commerceservices.util.QuoteExpirationTimeUtils;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;


/**
 * Validator for quote expiration time
 */
public class QuoteExpirationTimeValidator implements ConstraintValidator<QuoteExpirationTime, Object>
{
	public static final String DATE_FORMAT_KEY = "text.quote.dateformat";

	@Resource(name = "i18nService")
	private I18NService i18nService;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "timeService")
	private TimeService timeService;

	@Override
	public void initialize(final QuoteExpirationTime quoteExpirationTime)
	{
		//empty
	}

	@Override
	public boolean isValid(final Object expirationTime, final ConstraintValidatorContext constraintValidatorContext)
	{
		if (StringUtils.isEmpty((String) expirationTime))
		{
			return true;
		}

		try
		{
			final Locale currentLocale = getI18nService().getCurrentLocale();
			final String dateParsingFormat = getMessageSource().getMessage(DATE_FORMAT_KEY, null, currentLocale);
			final SimpleDateFormat dateFormat = new SimpleDateFormat(dateParsingFormat, currentLocale);
			dateFormat.setLenient(false);

			final Date expirationDate = QuoteExpirationTimeUtils.getEndOfDay(dateFormat.parse((String) expirationTime));

			return QuoteExpirationTimeUtils.isExpirationTimeValid(expirationDate,
					getTimeService().getCurrentDateWithTimeNormalized());
		}
		catch (final ParseException e)
		{
			return false;
		}
	}

	protected MessageSource getMessageSource()
	{
		return messageSource;
	}

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}
}
