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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.BusinessException;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCreditCardNumberHelperTest
{
	private static final String VISA_PROPERTY_KEY = "cardnumber.pattern.visa";
	private static final String MASTER_PROPERTY_KEY = "cardnumber.pattern.master";
	private static final String AMEX_PROPERTY_KEY = "cardnumber.pattern.amex";
	private static final String DINERS_PROPERTY_KEY = "cardnumber.pattern.diners";

	private static final String VISA_VALID_REGEXP = "^4[0-9]{12}(?:[0-9]{3})?$";
	private static final String DINERS_VALID_REGEXP = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
	private static final String AMEX_VALID_REGEXP = "^3[47][0-9]{13}$";
	private static final String MASTER_VALID_REGEXP = "^5[1-5][0-9]{14}$";

	@InjectMocks
	private final DefaultCreditCardNumberHelper helper = new DefaultCreditCardNumberHelper();
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;

	@Before
	public void createDefaultCreditCardNumberHelper() throws Exception // NOPMD
	{
		given(configurationService.getConfiguration()).willReturn(configuration);
	}

	@Test
	public void shouldNotValidateCardNumberWhenInputCardNumberIsImproperlyFormatted() throws BusinessException
	{
		// given
		final String cardNumber = "4111 1 1 1 1 1 1 1 1 1 1 1 1";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(null);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.VISA);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateCardNumberWhenThereIsNoRegexpConfiguredForCardType() throws BusinessException
	{
		// given
		final String invalidVisaCardButValidLuhnGenerated = "5000000000000009";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(null);

		// when
		final boolean valid = helper.isValidCardNumber(invalidVisaCardButValidLuhnGenerated, CreditCardType.VISA);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldValidateValid16DigitVISACard() throws BusinessException
	{
		// given
		final String cardNumber = "4111 1111 1111 1111";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(VISA_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.VISA);

		// then
		assertThat(valid).isTrue();
	}

	@Test
	public void shouldValidateValid13DigitOldVISACard() throws BusinessException
	{
		// given
		final String cardNumber = "4 9291 2312 3123";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(VISA_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.VISA);

		// then
		assertThat(valid).isTrue();
	}

	@Test
	public void shouldNotValidateVISACardWithBadStart() throws BusinessException
	{
		// given
		final String cardNumber = "5111 1111 1111 1111";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(VISA_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.VISA);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooLongVISACard() throws BusinessException
	{
		// given
		final String cardNumber = "4111 1111 1111 1111 1";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(VISA_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.VISA);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooShortVISACard() throws BusinessException
	{
		// given
		final String cardNumber = "4111 1111 1111 111";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(VISA_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.VISA);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooShortOldVISACard() throws BusinessException
	{
		// given
		final String cardNumber = "4 9291 2312 312";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(VISA_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.VISA);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooLongOldVISACard() throws BusinessException
	{
		// given
		final String cardNumber = "4 9291 2312 3123 1";
		given(configuration.getString(VISA_PROPERTY_KEY, null)).willReturn(VISA_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.VISA);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldValidateValidDINERSCard() throws BusinessException
	{
		// given
		final String cardNumbe1 = "3000 0000 0000 04";
		final String cardNumbe2 = "3852 0000 0232 37";
		final String cardNumbe3 = "3056 9309 0259 04";
		given(configuration.getString(DINERS_PROPERTY_KEY, null)).willReturn(DINERS_VALID_REGEXP);

		// when
		final boolean valid1 = helper.isValidCardNumber(cardNumbe1, CreditCardType.DINERS);
		final boolean valid2 = helper.isValidCardNumber(cardNumbe2, CreditCardType.DINERS);
		final boolean valid3 = helper.isValidCardNumber(cardNumbe3, CreditCardType.DINERS);

		// then
		assertThat(valid1).isTrue();
		assertThat(valid2).isTrue();
		assertThat(valid3).isTrue();
	}

	@Test
	public void shouldNotValidateDINERSCardWithBadStart() throws BusinessException
	{
		// given
		final String cardNumber = "4000 0000 0000 04";
		given(configuration.getString(DINERS_PROPERTY_KEY, null)).willReturn(DINERS_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.DINERS);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooLongDINERSCard() throws BusinessException
	{
		// given
		final String cardNumber = "4000 0000 0000 04 1";
		given(configuration.getString(DINERS_PROPERTY_KEY, null)).willReturn(DINERS_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.DINERS);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooShortDINERSCard() throws BusinessException
	{
		// given
		final String cardNumber = "4000 0000 0000 0";
		given(configuration.getString(DINERS_PROPERTY_KEY, null)).willReturn(DINERS_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.DINERS);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldValidateValidAMEXCard() throws BusinessException
	{
		// given
		final String cardNumber1 = "3400 0000 0000 009";
		final String cardNumber2 = "3700 0000 0000 002";
		given(configuration.getString(AMEX_PROPERTY_KEY, null)).willReturn(AMEX_VALID_REGEXP);

		// when
		final boolean valid1 = helper.isValidCardNumber(cardNumber1, CreditCardType.AMEX);
		final boolean valid2 = helper.isValidCardNumber(cardNumber2, CreditCardType.AMEX);

		// then
		assertThat(valid1).isTrue();
		assertThat(valid2).isTrue();
	}

	@Test
	public void shouldNotValidateTooLongAMEXCard() throws BusinessException
	{
		// given
		final String cardNumber = "3400 0000 0000 0009";
		given(configuration.getString(AMEX_PROPERTY_KEY, null)).willReturn(AMEX_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.AMEX);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooShortAMEXCard() throws BusinessException
	{
		// given
		final String cardNumber = "3400 0000 000 009";
		given(configuration.getString(AMEX_PROPERTY_KEY, null)).willReturn(AMEX_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.AMEX);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldValidateValidMASTERCard() throws BusinessException
	{
		// given
		final String cardNumber1 = "5100 0000 0000 0008";
		final String cardNumber2 = "5200 0000 0000 0007";
		final String cardNumber3 = "5300 0000 0000 0006";
		final String cardNumber4 = "5400 0000 0000 0005";
		final String cardNumber5 = "5500 0000 0000 0004";
		given(configuration.getString(MASTER_PROPERTY_KEY, null)).willReturn(MASTER_VALID_REGEXP);

		// when
		final boolean valid1 = helper.isValidCardNumber(cardNumber1, CreditCardType.MASTER);
		final boolean valid2 = helper.isValidCardNumber(cardNumber2, CreditCardType.MASTER);
		final boolean valid3 = helper.isValidCardNumber(cardNumber3, CreditCardType.MASTER);
		final boolean valid4 = helper.isValidCardNumber(cardNumber4, CreditCardType.MASTER);
		final boolean valid5 = helper.isValidCardNumber(cardNumber5, CreditCardType.MASTER);

		// then
		assertThat(valid1).isTrue();
		assertThat(valid2).isTrue();
		assertThat(valid3).isTrue();
		assertThat(valid4).isTrue();
		assertThat(valid5).isTrue();
	}

	@Test
	public void shouldNotValidateMASTERCardWithBadStart() throws BusinessException
	{
		// given
		final String cardNumber = "5600 0000 0000 0003";
		given(configuration.getString(MASTER_PROPERTY_KEY, null)).willReturn(MASTER_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.MASTER);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooShortMASTERCard() throws BusinessException
	{
		// given
		final String cardNumber = "5500 0000 000 0004";
		given(configuration.getString(MASTER_PROPERTY_KEY, null)).willReturn(MASTER_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.MASTER);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotValidateTooLongMASTERCard() throws BusinessException
	{
		// given
		final String cardNumber = "5500 0000 0001 0004";
		given(configuration.getString(MASTER_PROPERTY_KEY, null)).willReturn(MASTER_VALID_REGEXP);

		// when
		final boolean valid = helper.isValidCardNumber(cardNumber, CreditCardType.MASTER);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldMaskValidCreditCardNumber()
	{
		// given
		final String cardNumber = "5100 0000 0000 0008";

		// when
		final String maskedCardNumber = helper.maskCreditCardNumber(cardNumber);

		// then
		assertThat(maskedCardNumber).isNotNull().isNotEmpty();
		assertThat(maskedCardNumber).isEqualTo("************0008");
	}

	@Test
	public void shouldNotMaskShorterThan13DigitsNormalizedCreditCardNumberAndReturnNull()
	{
		// given
		final String cardNumber = "5100 0000";

		// when
		final String maskedCardNumber = helper.maskCreditCardNumber(cardNumber);

		// then
		assertThat(maskedCardNumber).isNull();
	}

	@Test
	public void shouldNotMaskLongerThan19DigitsNormalizedCreditCardNumberAndReturnNull()
	{
		// given
		final String cardNumber = "5100 0000 0000 0008 0000";

		// when
		final String maskedCardNumber = helper.maskCreditCardNumber(cardNumber);

		// then
		assertThat(maskedCardNumber).isNull();
	}

	@Test
	public void shouldNormalizeHumanReadableCardNumberToOnlyDigits()
	{
		// given
		final String cardNumber = "5100 0000 0000 0008";

		// when
		final String normalizedCardNumber = helper.normalizeCreditCardNumber(cardNumber);

		// then
		assertThat(normalizedCardNumber).isNotNull().isNotEmpty();
		assertThat(normalizedCardNumber).isEqualTo("5100000000000008");
	}
}
