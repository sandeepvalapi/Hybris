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
package de.hybris.platform.order;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.daos.ZoneDeliveryModeDao;
import de.hybris.platform.order.daos.ZoneDeliveryModeValueDao;
import de.hybris.platform.order.exceptions.DeliveryModeInterceptorException;
import de.hybris.platform.order.impl.DefaultZoneDeliveryModeService;
import de.hybris.platform.order.strategies.deliveryzone.ZDMVConsistencyStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Tests the {@link DefaultZoneDeliveryModeService}.
 */
@UnitTest
public class DefaultZoneDeliveryModeTest
{
	private DefaultZoneDeliveryModeService zoneDeliveryModeService;

	@Mock
	private ZoneDeliveryModeDao zoneDeliveryModeDao;
	@Mock
	private ZoneDeliveryModeValueDao zoneDeliveryModeValueDao;
	@Mock
	private ZDMVConsistencyStrategy zdmvConsistencyStrategy;

	private final CurrencyModel currency = new CurrencyModel();
	private final ZoneModel zone = new ZoneModel();
	private final DeliveryModeModel deliveryMode = new ZoneDeliveryModeModel();
	private final Double min = Double.valueOf(10);
	private final Double value = Double.valueOf(15);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		zoneDeliveryModeService = new DefaultZoneDeliveryModeService();
		zoneDeliveryModeService.setZoneDeliveryModeDao(zoneDeliveryModeDao);
		zoneDeliveryModeService.setZoneDeliveryModeValueDao(zoneDeliveryModeValueDao);
		zoneDeliveryModeService.setZdmvConsistencyStrategy(zdmvConsistencyStrategy);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSupportedDeliveryModes()
	{
		zoneDeliveryModeService.getSupportedDeliveryModes(null);
		fail("should throw IllegalArgumentException.");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetZone()
	{
		zoneDeliveryModeService.getZoneForCode(null);
		fail("should throw IllegalArgumentException.");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetZonesForDeliveryMode()
	{
		zoneDeliveryModeService.getZonesForZoneDeliveryMode(null);
		fail("should throw IllegalArgumentException.");
	}

	@Test
	public void testGetCurrencies()
	{
		testGetCurrencies(null, deliveryMode);
		testGetCurrencies(zone, null);
	}

	private void testGetCurrencies(final ZoneModel zone, final DeliveryModeModel deliveryMode)
	{
		try
		{
			zoneDeliveryModeService.getCurrencies(zone, (ZoneDeliveryModeModel) deliveryMode);
			fail("should throw IllegalArgumentException.");
		}
		catch (final IllegalArgumentException ie)
		{
			//expected
		}
	}

	@Test
	public void testGetDeliveryMode()
	{
		try
		{
			zoneDeliveryModeService.getDeliveryModeForCode(null);
			fail("should throw IllegalArgumentException.");
		}
		catch (final IllegalArgumentException ie)
		{
			//expected
		}
	}

	@Test
	public void testSetDeliveryCost() throws DeliveryModeInterceptorException
	{
		testSetDeliveryCost(null, min, value, zone, deliveryMode);
		testSetDeliveryCost(currency, null, value, zone, deliveryMode);
		testSetDeliveryCost(currency, min, null, zone, deliveryMode);
		testSetDeliveryCost(currency, min, value, null, deliveryMode);
		testSetDeliveryCost(currency, min, value, zone, null);
	}

	private void testSetDeliveryCost(final CurrencyModel currency, final Double min, final Double value, final ZoneModel zone,
			final DeliveryModeModel deliveryMode) throws DeliveryModeInterceptorException
	{
		try
		{
			zoneDeliveryModeService.setDeliveryCost(currency, min, value, zone, (ZoneDeliveryModeModel) deliveryMode);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetDeliveryValue()
	{
		testGetDeliveryValue(null, currency, min, deliveryMode);
		testGetDeliveryValue(zone, null, min, deliveryMode);
		testGetDeliveryValue(zone, currency, null, deliveryMode);
		testGetDeliveryValue(zone, currency, min, null);
	}

	private void testGetDeliveryValue(final ZoneModel zone, final CurrencyModel currency, final Double min,
			final DeliveryModeModel deliveryMode)
	{
		try
		{
			zoneDeliveryModeService.getDeliveryValue(zone, currency, min, (ZoneDeliveryModeModel) deliveryMode);
			fail("should throw IllegalArgumentException.");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetDeliveryValues()
	{
		testGetDeliveryValues(null, zone, deliveryMode);
		testGetDeliveryValues(currency, null, deliveryMode);
		testGetDeliveryValues(currency, zone, null);
	}

	private void testGetDeliveryValues(final CurrencyModel currency, final ZoneModel zone, final DeliveryModeModel deliveryMode)
	{
		try
		{
			zoneDeliveryModeService.getDeliveryValues(currency, zone, (ZoneDeliveryModeModel) deliveryMode);
			fail("should throw IllegalArgumentException.");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testIsZoneAllowed()
	{
		testIsZoneAllowed(null, deliveryMode);
		testIsZoneAllowed(zone, null);
	}

	private void testIsZoneAllowed(final ZoneModel zone, final DeliveryModeModel deliveryMode)
	{
		try
		{
			zoneDeliveryModeService.isZoneAllowed(zone, (ZoneDeliveryModeModel) deliveryMode);
			fail("should throw IllegalArgumentException.");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testUsingPrice()
	{
		final ZoneDeliveryModeModel zoneDeliveryMode = new ZoneDeliveryModeModel();
		assertFalse("not using price", zoneDeliveryModeService.isUsingPrice(zoneDeliveryMode));
		zoneDeliveryModeService.setUsingPrice(zoneDeliveryMode);
		assertTrue("using price now", zoneDeliveryModeService.isUsingPrice(zoneDeliveryMode));
	}

}
