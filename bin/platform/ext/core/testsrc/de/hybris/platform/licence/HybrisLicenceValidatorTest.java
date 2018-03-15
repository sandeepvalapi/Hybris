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
package de.hybris.platform.licence;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.licence.internal.HybrisLicenceDAO;
import de.hybris.platform.licence.internal.HybrisLicenceValidator;
import de.hybris.platform.licence.internal.ValidationResult;

import java.util.Date;
import java.util.Properties;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class HybrisLicenceValidatorTest
{
	public static final String TENANT_PREFIX = "junit_";
	private HybrisLicenceValidator validator;
	@Mock
	private Licence licence;
	@Mock
	private HybrisLicenceDAO hybrisLicenceDAO;
	@Mock
	private HybrisDataSource dataSource;
	@Mock
	private Properties properties;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		given(Boolean.valueOf(licence.isDemoOrDevelopLicence())).willReturn(Boolean.TRUE);
		given(dataSource.getTablePrefix()).willReturn(TENANT_PREFIX);

		setupValidatorInstances();
	}

	private void setupValidatorInstances()
	{
		validator = new HybrisLicenceValidator()
		{
			@Override
			protected HybrisLicenceDAO getHybrisLicenceDAO()
			{
				return hybrisLicenceDAO;
			}
		};
	}

	@Test
	public void licenceShouldNotBeValidWhenItIsNull() throws Exception
	{
		// given
		final Licence licence = null;

		// when
		final ValidationResult result = validator.checkLicence(licence);

		// then
		assertThat(result.isValid()).isFalse();
	}

	@Test
	public void licenceShouldNotBeValidWhenSignatureIsWrong() throws Exception
	{
		// given
		given(licence.getSignature()).willReturn("not-valid".getBytes());
		given(licence.getLicenceProperties()).willReturn(properties);

		// when
		final ValidationResult result = validator.checkLicence(licence);

		// then
		assertThat(result.isValid()).isFalse();
	}

	@Test
	public void licenceShouldNotBeValidWhenItIsExpired() throws Exception
	{
		// given
		given(licence.getSignature()).willReturn("not-valid".getBytes());
		given(licence.getLicenceProperties()).willReturn(properties);

		// when
		final ValidationResult result = validator.checkLicence(licence);

		// then
		assertThat(result.isValid()).isFalse();
	}

	@Test
	public void licenseShouldNotBeExpiredIfCurrentDateIsBeforeExpirationDate() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(10).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(dataSource)).willReturn(date);

		// when
		final boolean expired = validator.isLicenceExpiredIfDemoLicence(licence, dataSource);

		// then
		assertThat(expired).isFalse();
	}

	@Test
	public void licenseShouldNotBeExpiredIfCurrentDateIsThirtyDaysAfterStartingPoint() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(30).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(dataSource)).willReturn(date);

		// when
		final boolean expired = validator.isLicenceExpiredIfDemoLicence(licence, dataSource);

		// then
		assertThat(expired).isFalse();
	}

	@Test
	public void licenseShouldBeExpiredIfCurrentDateIsMoreThanThirtyDaysAfterStartingPoint() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(31).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(dataSource)).willReturn(date);

		// when
		final boolean expired = validator.isLicenceExpiredIfDemoLicence(licence, dataSource);

		// then
		assertThat(expired).isTrue();
	}

	@Test
	public void shouldReturnMinusOneDayLeftIfStartingPointDateIs31DaysBackFromNow() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(31).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(dataSource)).willReturn(date);

		// when
		final Integer daysLeft = validator.getDaysLeft(licence, dataSource);

		// then
		assertThat(daysLeft).isNotNull().isEqualTo(-1);
	}

	@Test
	public void shouldReturnZeroDaysLeftIfStartingPointDateIs30DaysBackFromNow() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(30).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance( dataSource)).willReturn(date);

		// when
		final Integer daysLeft = validator.getDaysLeft(licence, dataSource);

		// then
		assertThat(daysLeft).isNotNull().isEqualTo(0);
	}

	@Test
	public void shouldReturnOneDayLeftIfStartingPointDateIs29DaysBackFromNow() throws Exception
	{
		// given
		final Date date = DateTime.now().minusDays(29).toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance(dataSource)).willReturn(date);

		// when
		final Integer daysLeft = validator.getDaysLeft(licence, dataSource);

		// then
		assertThat(daysLeft).isNotNull().isEqualTo(1);
	}

	@Test
	public void shouldReturn30DaysLeftIfStartingPointDateIsNow() throws Exception
	{
		// given
		final Date date = DateTime.now().toDate();
		given(hybrisLicenceDAO.getStartingPointDateForPlatformInstance( dataSource)).willReturn(date);

		// when
		final Integer daysLeft = validator.getDaysLeft(licence, dataSource);

		// then
		assertThat(daysLeft).isNotNull().isEqualTo(30);
	}

	@Test
	public void shouldReturnNullAsDaysLeftIfPassedLicenceIsNotDemoOrDevelop() throws Exception
	{
		// given
		given(Boolean.valueOf(licence.isDemoOrDevelopLicence())).willReturn(Boolean.FALSE);

		// when
		final Integer daysLeft = validator.getDaysLeft(licence, dataSource);

		// then
		assertThat(daysLeft).isNull();
	}
}
