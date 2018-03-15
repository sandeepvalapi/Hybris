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
package de.hybris.platform.licence.sap;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.licence.internal.LicenseFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.sap.security.core.server.likey.Admin;
import com.sap.security.core.server.likey.KeySystem;
import com.sap.security.core.server.likey.LogAndTrace;
import com.sap.security.core.server.likey.Persistence;


@IntegrationTest
public class SAPLicenseTest
{
	private final KeySystem keySystem = new DefaultKeySystem();
	private final LogAndTrace logAndTrace = new DefaultLogAndTrace();
	private Persistence persistence;
	private LicenseFactory factory;
	private static final String TEST_PRODUCT = "CPS_HDB";

	@Before
	public void setUp() throws Exception
	{
		persistence = new TestPersistence();
		factory = new LicenseFactory()
		{
			@Override
			protected Persistence getPersistence()
			{
				return persistence;
			}
		};
	}

	@Test
	public void shouldReturnValidSAPLicenseWhenItIsInstalled() throws Exception
	{
		// given
		installFirstTempLicenseForProduct(TEST_PRODUCT);

		// when
		final Licence license = factory.getCurrentLicense("sap");


		// then
		assertThat(license).isInstanceOf(SAPLicense.class);
	}

	@Test
	public void shouldReturnValidExpirationDate() throws Exception
	{
		// given
		installFirstTempLicenseForProduct(TEST_PRODUCT);

		// when
		final Licence license = factory.getCurrentLicense("sap");

		// then
		assertThat(license.getExpirationDate()).isNotNull();
		assertThat(license.getExpirationDate()).isEqualTo(getExpirationDateFromLicenseProperties(license));
	}

	private Date getExpirationDateFromLicenseProperties(final Licence licence) throws ParseException
	{
		return new SimpleDateFormat("dd/MMM/yyyy", Locale.US).parse(licence.getLicenceProperties()
				.getProperty("licence.expiration"));
	}

	@Test
	public void shouldReturnLicensePropertiesWithNoRestrictions() throws Exception
	{
		// given
		installFirstTempLicenseForProduct(TEST_PRODUCT);
		final Licence license = factory.getCurrentLicense("sap");

		// when
		final Properties props = license.getLicenceProperties();

		// then
		assertThat(props).isNotNull();
		assertThat(props.getProperty("licence.advancedsecurity")).isEqualTo("true");
		assertThat(props.getProperty("licence.highperformance")).isEqualTo("true");
		assertThat(props.getProperty("licence.version")).isEmpty();
		assertThat(props.getProperty("licence.email")).isEmpty();
		assertThat(props.getProperty("licence.name")).isEqualTo(((SAPLicense) license).getSource().getSwProduct());
		assertThat(props.getProperty("licence.eulaversion")).isEqualTo("2.0");
		assertThat(props.getProperty("licence.id")).isEqualTo(((SAPLicense) license).getSource().getSystemId());
		assertThat(props.getProperty("licence.date")).isEqualTo(((SAPLicense) license).getSource().getBeginDate());
		assertThat(props.getProperty("licence.expiration")).isEqualTo(((SAPLicense) license).getSource().getEndDate());
		assertThat(props.getProperty("licence.clustering")).isEqualTo("true");
		assertThat(props.getProperty("licence.endcustomer")).isEmpty();
	}

	@Test
	public void temporaryLicenseShouldBeTreatedAsDemoOrDevelopLicense() throws Exception
	{
		// given
		installFirstTempLicenseForProduct(TEST_PRODUCT);

		// when
		final Licence license = factory.getCurrentLicense("sap");

		// then
		assertThat(license.isDemoOrDevelopLicence()).isTrue();
	}

	private void installFirstTempLicenseForProduct(final String productName)
	{
		final Admin admin = new Admin(persistence, keySystem, logAndTrace);
		final boolean installResult = admin.installFirstTempLicense(productName, new Vector());

		assertThat(installResult).isTrue();
	}

}
