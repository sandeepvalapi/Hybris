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
package de.hybris.platform.order.strategies.paymentinfo.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.order.strategies.paymentinfo.CreditCardNumberHelper;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.BusinessException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultCreditCardNumberHelper implements CreditCardNumberHelper
{

	private static final Logger LOG = Logger.getLogger(DefaultCreditCardNumberHelper.class);
	private static final String PREFIX = "cardnumber.pattern.";
	private static final String LEADING_NUMERIC_PATTERN = "[^0-9]";
	private static final String NUMERIC_PATTERN = "[0-9]";
	// according to "Anatomy of credid card numbers" (http://www.merriampark.com/anatomycc.htm)
	private static final int MAX_CREDIT_CARD_LENGTH = 19;
	private static final int MIN_CREDIT_CARD_LENGTH = 13;
	private final Map<String, Pattern> patternCache = new HashMap<String, Pattern>();

	private ConfigurationService configurationService;

	@Override
	public boolean isValidCardNumber(final String cardNumber, final CreditCardType type) throws BusinessException
	{
		final String normalizedCardNumber = normalizeCreditCardNumber(cardNumber);

		if (hasValidLength(normalizedCardNumber))
		{
			final String patternForCard = findPatternForCardType(type);
			return checkCardNumberAgainstRegexp(patternForCard, normalizedCardNumber) && luhnTest(normalizedCardNumber);
		}
		else
		{
			return false;
		}
	}

	private boolean hasValidLength(final String cardNumber)
	{
		return cardNumber != null && cardNumber.length() >= MIN_CREDIT_CARD_LENGTH && cardNumber.length() <= MAX_CREDIT_CARD_LENGTH;
	}

	protected String findPatternForCardType(final CreditCardType cardType)
	{
		validateParameterNotNullStandardMessage("cardType", cardType);
		return configurationService.getConfiguration().getString(PREFIX + cardType.getCode(), null);
	}

	private boolean checkCardNumberAgainstRegexp(final String patternForCard, final String cardNumber)
	{
		if (GenericValidator.isBlankOrNull(patternForCard) || GenericValidator.isBlankOrNull(cardNumber))
		{
			return false;
		}
		else
		{
			final Pattern compiledPattern = getOrCreateCachedCardPattern(patternForCard);
			return compiledPattern.matcher(cardNumber).matches();
		}
	}

	private boolean luhnTest(final String cardNumber) throws BusinessException
	{
		if (cardNumber == null)
		{
			return false;
		}

		int len = cardNumber.length();
		final int digits[] = new int[len];
		for (int i = 0; i < len; i++)
		{
			try
			{
				digits[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
			}
			catch (final NumberFormatException e)
			{
				LOG.error(e.getMessage());
				throw new BusinessException(e.getMessage(), e);
			}
		}
		int sum = 0;
		while (len > 0)
		{
			sum += digits[len - 1];
			len--;
			if (len > 0)
			{
				final int digit = 2 * digits[len - 1];
				sum += (digit > 9) ? digit - 9 : digit;
				len--;
			}
		}
		return (sum % 10 == 0);
	}

	@Override
	public String normalizeCreditCardNumber(final String creditCardNumber)
	{
		if (creditCardNumber == null)
		{
			return creditCardNumber;
		}
		final Pattern pattern = getOrCreateCachedCardPattern(LEADING_NUMERIC_PATTERN);
		final Matcher matcher = pattern.matcher(creditCardNumber);
		return matcher.replaceAll("");
	}

	@Override
	public String maskCreditCardNumber(final String creditCardNumber)
	{
		final String _creditCardNumber = normalizeCreditCardNumber(creditCardNumber);

		if (hasValidLength(_creditCardNumber))
		{
			final int len = _creditCardNumber.length();

			final Pattern pattern = getOrCreateCachedCardPattern(NUMERIC_PATTERN);
			final Matcher matcher = pattern.matcher(_creditCardNumber.substring(0, len - 4));

			final String _cn = matcher.replaceAll("*");
			return _cn + _creditCardNumber.substring(len - 4, len);
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Invalid length of the submitted credit card number!");
			}
			return null;
		}
	}

	private Pattern getOrCreateCachedCardPattern(final String key)
	{
		Pattern compiledPattern = null;
		if (patternCache.containsKey(key))
		{
			compiledPattern = patternCache.get(key);
		}
		else
		{
			synchronized (patternCache)
			{
				compiledPattern = Pattern.compile(key, Pattern.CASE_INSENSITIVE);
				patternCache.put(key, compiledPattern);
			}
		}
		return compiledPattern;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	@SystemSetup(extension = CoreConstants.EXTENSIONNAME, type = Type.ALL)
	public void clearPatternCache()
	{
		if (patternCache != null)
		{
			patternCache.clear();
		}
	}
}
