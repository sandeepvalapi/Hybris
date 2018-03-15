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
package de.hybris.platform.order.interceptors;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.ZoneDeliveryModeService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test for {@link ZoneDeliveryModeValueValidator}.
 */
@IntegrationTest
public class ZoneDeliveryModeValueValidatorTest extends ServicelayerTransactionalTest
{

	@Resource
	private ZoneDeliveryModeValueValidator zoneDeliveryModeValueValidator;
	@Resource
	private ModelService modelService;
	@Resource
	private ZoneDeliveryModeService zoneDeliveryModeService;
	@Resource
	private CommonI18NService commonI18NService;

	/**
	 * Creates the core data, and necessary data for delivery modes.
	 */
	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/servicelayer/test/testDeliveryMode.csv", "windows-1252");
	}

	/**
	 * Tries to create the {@link ZoneDeliveryModeValueModel} with modelService, and tests the
	 * {@link ZoneDeliveryModeValueValidator}.
	 * <ul>
	 * <li>creates a new {@link ZoneDeliveryModeValueModel} in the duplicate_zone, and gets an InterceptorException,</li>
	 * <li>creates a new {@link ZoneDeliveryModeValueModel} in the zone Germany with existed minimum value 0 Euro, and
	 * gets an InterceptorException,</li>
	 * <li>creates a new {@link ZoneDeliveryModeValueModel} in the zone Germany with minimum value 10 Euro successfully.</li>
	 * </ul>
	 */
	@Test
	public void testZoneDeliveryModeValueValidator()
	{
		final ZoneDeliveryModeModel dhlZoneDeliveryMode = (ZoneDeliveryModeModel) zoneDeliveryModeService
				.getDeliveryModeForCode("dhl");
		final CurrencyModel eurCurrency = commonI18NService.getCurrency("EUR");
		Double min = Double.valueOf(0);
		final Double value = Double.valueOf(5);
		final ZoneModel duplicateZone = zoneDeliveryModeService.getZoneForCode("duplicate_zone");
		final ZoneModel deZone = zoneDeliveryModeService.getZoneForCode("de");

		testZoneDeliveryModeValueValidator(eurCurrency, min, value, duplicateZone, dhlZoneDeliveryMode, true);
		testZoneDeliveryModeValueValidator(eurCurrency, min, value, deZone, dhlZoneDeliveryMode, true);
		min = Double.valueOf(10);
		testZoneDeliveryModeValueValidator(eurCurrency, min, value, deZone, dhlZoneDeliveryMode, false);
	}

	private void testZoneDeliveryModeValueValidator(final CurrencyModel currency, final Double min, final Double value,
			final ZoneModel zone, final ZoneDeliveryModeModel zoneDeliveryMode, final boolean expectException)
	{
		final ZoneDeliveryModeValueModel zoneDeliveryModeValue = modelService.create(ZoneDeliveryModeValueModel.class);
		zoneDeliveryModeValue.setCurrency(currency);
		zoneDeliveryModeValue.setMinimum(min);
		zoneDeliveryModeValue.setValue(value);
		zoneDeliveryModeValue.setZone(zone);
		zoneDeliveryModeValue.setDeliveryMode(zoneDeliveryMode);

		try
		{
			zoneDeliveryModeValueValidator.onValidate(zoneDeliveryModeValue, null);
			if (expectException)
			{
				fail("InterceptorException must be thrown.");
			}
		}
		catch (final InterceptorException ie)
		{
			if (expectException)
			{ //NOPMD
			  //all right, expected
			}
			else
			{
				fail("ZoneDeliveryModeValue was not created: " + ie.getMessage());
			}
		}
	}

}
