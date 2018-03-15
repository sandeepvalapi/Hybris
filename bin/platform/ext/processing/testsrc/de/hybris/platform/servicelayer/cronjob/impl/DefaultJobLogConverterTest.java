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
package de.hybris.platform.servicelayer.cronjob.impl;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultFormatFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.Assert;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultJobLogConverterTest
{
	private DefaultJobLogConverter converter;

	private FormatFactory formatFactory;

	@Mock
	private I18NService i18nService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		converter = new DefaultJobLogConverter();

		formatFactory = Mockito.spy(new DefaultFormatFactory());
		((DefaultFormatFactory) formatFactory).setI18nService(i18nService);

		Mockito.when(i18nService.getCurrentLocale()).thenReturn(Locale.getDefault());
		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());

		converter.setFormatFactory(formatFactory);

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUnsuppportedConvert()
	{
		converter.convert(null, null);

		Mockito.verifyZeroInteractions(formatFactory);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidFormat()
	{
		converter.setDateFormatPattern("invalid format");
		converter.convert(null);

		Mockito.verifyZeroInteractions(formatFactory);
	}

	@Test
	public void testValidFormatEmptyLogs()
	{

		final String result = converter.convert(new ArrayList<JobLogModel>());

		Mockito.verify(formatFactory, Mockito.times(1)).createDateTimeFormat(DateFormat.DEFAULT, -1);

		Assert.assertTrue(StringUtils.isEmpty(result));
	}

	@Test
	public void testValidFormatLogs()
	{
		final String pattern = "dd HH";
		converter.setDateFormatPattern(pattern);

		final Date date = new Date();

		final JobLogModel logEntry = new JobLogModel();
		logEntry.setCreationtime(date);
		logEntry.setLevel(JobLogLevel.FATAL);

		final List<JobLogModel> entries = Arrays.asList(logEntry);

		final String result = converter.convert(entries);

		Mockito.verify(formatFactory, Mockito.times(1)).createDateTimeFormat(DateFormat.DEFAULT, -1);

		final SimpleDateFormat sdf = (SimpleDateFormat) formatFactory.createDateTimeFormat(DateFormat.DEFAULT, -1);
		sdf.applyPattern(pattern);

		Assert.assertEquals(result, String.format("%s: FATAL: <empty>" + CharUtils.LF, sdf.format(date)));
	}

	@Test
	public void testValidFormatLogsWithEntries()
	{
		final String pattern = "dd HH";
		converter.setDateFormatPattern(pattern);

		final StringBuilder messageBuilder = new StringBuilder(1000);
		for (int i = 0; i < 5; i++)
		{
			messageBuilder.append("message" + i + CharUtils.LF);
		}

		final Date date = new Date();

		final JobLogModel logEntry = new JobLogModel();
		logEntry.setCreationtime(date);
		logEntry.setLevel(JobLogLevel.FATAL);

		logEntry.setMessage(messageBuilder.toString());


		final List<JobLogModel> entries = Arrays.asList(logEntry);

		final String result = converter.convert(entries);

		Mockito.verify(formatFactory, Mockito.times(1)).createDateTimeFormat(DateFormat.DEFAULT, -1);

		final SimpleDateFormat sdf = (SimpleDateFormat) formatFactory.createDateTimeFormat(DateFormat.DEFAULT, -1);
		sdf.applyPattern(pattern);

		Assert.assertEquals(result, String.format("%s: FATAL: " + messageBuilder.toString(), sdf.format(date)));
	}

	@Test
	public void testValidFormatLogsWithEntriesTruncated()
	{
		final String pattern = "dd HH";
		converter.setDateFormatPattern(pattern);

		final StringBuilder messageFromLogs = new StringBuilder(1000);
		for (int i = 0; i < 15; i++)
		{
			messageFromLogs.append("message" + i + CharUtils.LF);
		}

		final StringBuilder messageExpected = new StringBuilder(1000);
		for (int i = 0; i < 10; i++)
		{
			messageExpected.append("message" + i + (i == 9 ? "" : String.valueOf(CharUtils.LF)));
		}
		messageExpected.append(" ..." + CharUtils.LF);

		final Date date = new Date();

		final JobLogModel logEntry = new JobLogModel();
		logEntry.setCreationtime(date);
		logEntry.setLevel(JobLogLevel.FATAL);

		logEntry.setMessage(messageFromLogs.toString());


		final List<JobLogModel> entries = Arrays.asList(logEntry);

		final String result = converter.convert(entries);

		Mockito.verify(formatFactory, Mockito.times(1)).createDateTimeFormat(DateFormat.DEFAULT, -1);

		final SimpleDateFormat sdf = (SimpleDateFormat) formatFactory.createDateTimeFormat(DateFormat.DEFAULT, -1);
		sdf.applyPattern(pattern);

		Assert.assertEquals(result, String.format("%s: FATAL: " + messageExpected.toString(), sdf.format(date)));
	}
}
