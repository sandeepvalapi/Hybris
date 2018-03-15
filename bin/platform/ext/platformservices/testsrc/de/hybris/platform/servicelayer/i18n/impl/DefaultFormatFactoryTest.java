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
package de.hybris.platform.servicelayer.i18n.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.testframework.runlistener.ResetMockitoRunListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test covering {@link FormatFactory} service.
 */
@UnitTest
@Ignore
public class DefaultFormatFactoryTest
{
	@Mock
	private I18NService i18nService;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private Session session;

	private DefaultFormatFactory factory;
	private SessionService sessionService;

	@Before
	public void setUp()
	{
		ResetMockitoRunListener.resetMockito();

		MockitoAnnotations.initMocks(this);

		factory = new DefaultFormatFactory();
		factory.setI18nService(i18nService);
		factory.setCommonI18NService(commonI18NService);

		sessionService = new TestDefaultSessionService(session);
	}

	@After
	public void tearDown()
	{
		ResetMockitoRunListener.resetMockito();
	}

	private void setEnv(final Locale currentLocale, final CurrencyModel currentCurrency)
	{
		Mockito.when(i18nService.getCurrentLocale()).thenReturn(currentLocale);
		Mockito.when(commonI18NService.getCurrentCurrency()).thenReturn(currentCurrency);
	}

	///Tests for a currency
	@Test
	public void testGetCurrencyFormat()
	{
		final Double number = Double.valueOf(10000000.00001);
		setEnv(Locale.getDefault(), null);

		assertSame("injecting mocks somehow failed", i18nService, factory.getI18nService());
		assertSame("injecting mocks somehow failed", commonI18NService, factory.getCommonI18NService());

		assertEquals("Strangely mockit returns different default locale than Locale.getDefault()", i18nService.getCurrentLocale(),
				Locale.getDefault());

		assertEquals(
				"getting number format using (default) locale from 18nservice is different than using Numberformat.getCurrencyInstance()",
				NumberFormat.getCurrencyInstance(i18nService.getCurrentLocale()), NumberFormat.getCurrencyInstance());

		final NumberFormat format = factory.createCurrencyFormat();

		assertEquals("factory returns different (default) format", format, NumberFormat.getCurrencyInstance());

		assertEquals("Factory currency format should  be equal with default " + Locale.getDefault() + " locale ",
				format.format(number), NumberFormat.getCurrencyInstance().format(number));
	}

	@Test
	public void testGetCurrencyFormatWithLocale()
	{
		final Double number = Double.valueOf(10000000.00001);

		final String defaultFormattedNumber = NumberFormat.getCurrencyInstance().format(number);
		final Locale chineseLocale = Locale.CHINA;

		setEnv(chineseLocale, null);

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				//execute factory with Chinese locale in the session
				final NumberFormat format = factory.createCurrencyFormat();
				final String customformattedNumber = format.format(number);
				Assert.assertEquals("Factory currency format should be with " + chineseLocale + " locales ", NumberFormat
						.getCurrencyInstance(chineseLocale).format(number), customformattedNumber);
				Assert.assertFalse("Factory currency format should not be equal with default  " + Locale.getDefault() + " locales ",
						defaultFormattedNumber.equals(customformattedNumber));
			}
		});
	}

	@Test
	public void testGetCurrencyFormatWithCurrencyWithoutFractionSymbol()
	{
		final Double number = Double.valueOf(10000000.00001);

		final CurrencyModel currencyModel = Mockito.mock(CurrencyModel.class);
		final Locale chineseLocale = Locale.CHINA;

		final String defaultFormattedNumber = NumberFormat.getCurrencyInstance().format(number);

		setEnv(chineseLocale, currencyModel);

		Mockito.when(currencyModel.getIsocode()).thenReturn("&iso&");
		Mockito.when(currencyModel.getSymbol()).thenReturn("&symbol&");
		Mockito.when(currencyModel.getDigits()).thenReturn(Integer.valueOf(0));

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				//execute factory with Chinese locale in the session
				i18nService.setCurrentLocale(chineseLocale);
				final NumberFormat format = factory.createCurrencyFormat();
				final String customformattedNumber = format.format(number);
				Assert.assertEquals("Factory currency format should be without fraction " + chineseLocale + " locales ", "&symbol&"
						+ "10,000,000", customformattedNumber);
				Assert.assertFalse("Factory currency format should not  be equal with default  " + Locale.getDefault() + " locales ",
						defaultFormattedNumber.equalsIgnoreCase(customformattedNumber));
			}
		});

		Mockito.verify(currencyModel, Mockito.times(2)).getDigits();
		Mockito.verify(currencyModel, Mockito.times(1)).getSymbol();

		Mockito.verify(i18nService, Mockito.times(1)).getCurrentLocale();
		Mockito.verify(commonI18NService, Mockito.times(1)).getCurrentCurrency();
	}

	@Test
	public void testGetCurrencyFormatWithCurrencyWithFractionAndSymbol()
	{
		final Double number = Double.valueOf(10000000.00001);

		final CurrencyModel currencyModel = Mockito.mock(CurrencyModel.class);
		final Locale chineseLocale = Locale.CHINA;

		final String defaultFormattedNumber = NumberFormat.getCurrencyInstance().format(number);

		setEnv(chineseLocale, currencyModel);

		Mockito.when(currencyModel.getIsocode()).thenReturn("&iso&");
		Mockito.when(currencyModel.getSymbol()).thenReturn("&symbol&");
		Mockito.when(currencyModel.getDigits()).thenReturn(Integer.valueOf(5));

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				//execute factory with Chinese locale in the session
				i18nService.setCurrentLocale(chineseLocale);
				final NumberFormat format = factory.createCurrencyFormat();
				final String customformattedNumber = format.format(number);
				Assert.assertEquals("Factory currency format should be with fraction and symbol" + chineseLocale + " locales ",
						"&symbol&10,000,000.00001", customformattedNumber);
				Assert.assertFalse("Factory currency format should not  be equal with default  " + Locale.getDefault() + " locales ",
						defaultFormattedNumber.equalsIgnoreCase(customformattedNumber));
			}
		});

		Mockito.verify(currencyModel, Mockito.times(2)).getDigits();
		Mockito.verify(currencyModel, Mockito.times(1)).getSymbol();
		Mockito.verify(currencyModel, Mockito.times(1)).getIsocode();

		Mockito.verify(i18nService, Mockito.times(1)).getCurrentLocale();
		Mockito.verify(commonI18NService, Mockito.times(1)).getCurrentCurrency();
	}

	//Tests for a number
	@Test
	public void testGetNumberFormat()
	{
		final Double number = Double.valueOf(10000000.00001);

		setEnv(Locale.getDefault(), null);

		final NumberFormat format = factory.createNumberFormat();

		Assert.assertEquals("Factory number format should  be equal with default " + Locale.getDefault() + " locale ",
				format.format(number), NumberFormat.getNumberInstance().format(number));
	}

	@Test
	public void testGetNumberFormatWithLocale()
	{
		final Double number = Double.valueOf(10000000.00001);
		final String defaultFormattedNumber = NumberFormat.getNumberInstance(Locale.ENGLISH).format(number);
		final Locale frenchLocale = Locale.FRENCH;

		setEnv(frenchLocale, null);

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				//execute factory with French locale in the session
				final NumberFormat format = factory.createNumberFormat();
				final String customformattedNumber = format.format(number);
				Assert.assertEquals("Factory number format should be equal with " + frenchLocale + " locales ", NumberFormat
						.getNumberInstance(frenchLocale).format(number), customformattedNumber);
				Assert.assertFalse("Factory number format should be not equal with default " + Locale.getDefault() + " locale ",
						customformattedNumber.equalsIgnoreCase(defaultFormattedNumber));
			}
		});
	}

	//Tests for a number
	@Test
	public void testGetIntegerFormat()
	{
		final Double number = Double.valueOf(10000000.00001);

		setEnv(Locale.getDefault(), null);

		final NumberFormat format = factory.createIntegerFormat();

		Assert.assertEquals("Factory integer format should  be equal with english " + Locale.ENGLISH + " locale ",
				format.format(number), NumberFormat.getIntegerInstance().format(number));
	}

	@Test
	public void testGetIntegerFormatWithLocale()
	{
		final Double number = Double.valueOf(10000000.00001);

		final String defaultFormattedNumber = NumberFormat.getIntegerInstance(Locale.ENGLISH).format(number);
		final Locale frenchLocale = Locale.FRENCH;

		setEnv(frenchLocale, null);

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				//execute factory with French locale in the session
				final NumberFormat format = factory.createIntegerFormat();
				final String customformattedNumber = format.format(number);
				Assert.assertEquals("Factory integer format should be with " + frenchLocale + " locales ", NumberFormat
						.getIntegerInstance(frenchLocale).format(number), customformattedNumber);
				Assert.assertFalse("Factory integer format should not be equal with english " + Locale.ENGLISH + " locale ", format
						.format(number).equalsIgnoreCase(defaultFormattedNumber));
			}
		});
	}

	//Tests for a number
	@Test
	public void testGetPercentFormat()
	{
		final Double number = Double.valueOf(10000000.00001);

		setEnv(Locale.getDefault(), null);

		final NumberFormat format = factory.createPercentFormat();

		Assert.assertEquals("Factory percent format should  be equal with default " + Locale.getDefault() + " locale ",
				format.format(number), NumberFormat.getPercentInstance().format(number));
	}

	@Test
	public void testGetPercentFormatWithLocale()
	{
		final Double number = Double.valueOf(10000000.00001);

		final String defaultFormattedNumber = NumberFormat.getPercentInstance(Locale.ENGLISH).format(number);
		final Locale frenchLocale = Locale.FRENCH;

		setEnv(frenchLocale, null);

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				//execute factory with French locale in the session
				final NumberFormat format = factory.createPercentFormat();
				final String customformattedPercent = format.format(number);
				Assert.assertEquals("Factory percent format should be with " + frenchLocale + " locales ", NumberFormat
						.getPercentInstance(frenchLocale).format(number), customformattedPercent);
				Assert.assertFalse("Factory percent format should not be equal with english " + Locale.ENGLISH + " locale ",
						customformattedPercent.equalsIgnoreCase(defaultFormattedNumber));
			}
		});
	}


	//Tests for a time/date
	@Test(expected = IllegalArgumentException.class)
	public void testGetTimeFormatInvalid()
	{
		final Long now = Long.valueOf(System.currentTimeMillis());

		setEnv(Locale.getDefault(), null);
		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());

		final DateFormat format = factory.createDateTimeFormat(-1, -1);

		Assert.assertEquals("Factory time format should  be equal with default " + Locale.getDefault() + " locale ",
				format.format(now), DateFormat.getTimeInstance(DateFormat.DEFAULT).format(now));
	}

	@Test
	public void testGetTimeFormat()
	{
		final Long now = Long.valueOf(System.currentTimeMillis());

		setEnv(Locale.getDefault(), null);
		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());

		//in case date style == -1 only time formatting is being used
		final DateFormat format = factory.createDateTimeFormat(-1, DateFormat.DEFAULT);

		Assert.assertEquals("Factory time format should  be equal with default " + Locale.getDefault() + " locale ",
				format.format(now), DateFormat.getTimeInstance(DateFormat.DEFAULT).format(now));
	}

	@Test
	public void testGetDateTimeFormat()
	{
		final Long now = Long.valueOf(System.currentTimeMillis());

		setEnv(Locale.getDefault(), null);
		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());

		final DateFormat format = factory.createDateTimeFormat(DateFormat.FULL, DateFormat.SHORT);

		Assert.assertEquals("Factory date time format should  be equal with default " + Locale.getDefault() + " locale ",
				format.format(now), DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(now));
	}

	@Test
	public void testGetTimeFormatWithLocale()
	{
		final Long now = Long.valueOf(System.currentTimeMillis());

		final String defaultFormattedNumber = DateFormat.getTimeInstance().format(now);
		final Locale chineseLocale = Locale.CHINA;

		setEnv(chineseLocale, null);
		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				//execute factory with Chinese locale in the session
				final DateFormat format = factory.createDateTimeFormat(-1, DateFormat.SHORT);
				final String customformattedNumber = format.format(now);
				Assert.assertEquals("Factory time format should be with " + chineseLocale + " locales ",
						DateFormat.getTimeInstance(DateFormat.SHORT, chineseLocale).format(now), customformattedNumber);
				Assert.assertFalse("Factory time format should not be equal with default " + Locale.getDefault() + " locale ", format
						.format(now).equalsIgnoreCase(defaultFormattedNumber));
			}
		});
	}

	@Test
	public void testGetDateTimeFormatWithLocale()
	{
		final Long now = Long.valueOf(System.currentTimeMillis());

		final String defaultFormattedNumber = DateFormat.getTimeInstance().format(now);

		final Locale chineseLocale = Locale.CHINA;

		setEnv(chineseLocale, null);
		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				//execute factory with Chinese locale in the session
				final DateFormat format = factory.createDateTimeFormat(DateFormat.LONG, DateFormat.SHORT);
				final String customformattedNumber = format.format(now);
				Assert.assertEquals("Factory time format should be with " + chineseLocale + " locales ", DateFormat
						.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, chineseLocale).format(now), customformattedNumber);
				Assert.assertFalse("Factory time format should not be equal with default " + Locale.getDefault() + " locale ", format
						.format(now).equalsIgnoreCase(defaultFormattedNumber));
			}
		});
	}

	/**
	 * Example of use {@link FormatFactory} to apply pattern to {@link DateFormat}.
	 */
	@Test
	public void testSimpleDateFormat()
	{
		final Long now = Long.valueOf(System.currentTimeMillis());

		setEnv(Locale.getDefault(), null);
		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());

		//in case date style == -1 only date formatting
		final DateFormat format = factory.createDateTimeFormat(DateFormat.DEFAULT, -1);
		((SimpleDateFormat) format).applyPattern("yyyy-mm-dd");

		Assert.assertEquals("Factory time format should  be equal with default " + Locale.getDefault() + " locale ",
				new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(now), format.format(now));
	}

	/**
	 * Example of use {@link FormatFactory} to apply pattern to {@link DateFormat} with custom {@link Locale}.
	 */
	@Test
	public void testSimpleDateFormatWithLocale()
	{
		final Long now = Long.valueOf(System.currentTimeMillis());
		final Locale chineseLocale = Locale.CHINA;

		setEnv(chineseLocale, null);
		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{

				final DateFormat format = factory.createDateTimeFormat(DateFormat.DEFAULT, -1);
				((SimpleDateFormat) format).applyPattern("yyyy-mm-dd E");

				Assert.assertEquals("Factory time format should  be equal with chinese " + chineseLocale + " locale ",
						new SimpleDateFormat("yyyy-mm-dd E", chineseLocale).format(now), format.format(now));

				Assert.assertFalse("Factory time format should be  not equal with default " + Locale.getDefault() + " locale ",
						new SimpleDateFormat("yyyy-mm-dd E", Locale.getDefault()).format(now).equals(format.format(now)));
			}
		});
		//in case time style == -1 only date formatting is being used
		final DateFormat format = factory.createDateTimeFormat(DateFormat.DEFAULT, -1);
		((SimpleDateFormat) format).applyPattern("yyyy-mm-dd");

		Assert.assertEquals("Factory time format should  be equal with default " + Locale.getDefault() + " locale ",
				new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(now), format.format(now));
	}

	@Test
	public void shouldCreateSimpleDateFormatObjectOfGivenFormatForCurrentLocale()
	{
		// given
		final String format = "yyyy-mm-dd";

		given(i18nService.getCurrentLocale()).willReturn(new Locale("en"));

		// when
		final DateFormat result = factory.createDateTimeFormat(format);

		// then
		assertThat(result).isNotNull();
		assertThat(result).isInstanceOf(SimpleDateFormat.class);
		verify(i18nService, times(1)).getCurrentLocale();
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenCreateSimpleDateFormatAndFormatIsNull()
	{
		// given
		final String format = null;

		try
		{
			// when
			factory.createDateTimeFormat(format);
			fail();
		}
		catch (final NullPointerException e)
		{
			// then OK
		}
	}

	//Stub session service
	class TestDefaultSessionService extends MockSessionService
	{
		private final Session sessionMock;

		TestDefaultSessionService(final Session sessionMock)
		{
			super();
			this.sessionMock = sessionMock;
		}

		@Override
		public Session getCurrentSession()
		{
			return sessionMock;
		}
	}

}
