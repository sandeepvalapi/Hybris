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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.exceptions.DeliveryModeInterceptorException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


@DemoTest
public class ZoneDeliveryModeServiceTest extends ServicelayerTransactionalTest
{

	@Resource
	private ZoneDeliveryModeService zoneDeliveryModeService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private PaymentModeService paymentModeService;
	@Resource
	private ModelService modelService;

	private static final String dhlDeliveryModeCode = "dhl";
	private static final String fedexDeliveryModeCode = "fedex";
	private static final String upsDeliveryModeCode = "ups";

	private static final String deZoneCode = "de";
	private static final String europeZoneCode = "europe";
	private static final String worldZoneCode = "world";

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
	 * Tries to search for the delivery mode with code:
	 * <ul>
	 * <li>successful with "courier",</li>
	 * <li>caught UnknownIdentifierException with "No_Such_DeliveryMode",</li>
	 * <li>found all delivery modes, whose size is 3.</li>
	 * </ul>
	 */
	@Test
	public void testGetDeliveryMode()
	{
		String deliveryModeCode = "courier";
		DeliveryModeModel deliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(deliveryModeCode);
		assertNotNull(deliveryMode);
		assertEquals(deliveryModeCode, deliveryMode.getCode());

		deliveryModeCode = "No_Such_DeliveryMode";
		try
		{
			deliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(deliveryModeCode);
			fail("the delivery mode code [" + deliveryMode + "] should NOT be found.");
		}
		catch (final UnknownIdentifierException ue)
		{
			//expected
		}

		final String[] expectedDeliveryModes =
		{ "collect", "courier", "dhl", "fedex", "post", "postService", "ups" };
		final Collection<DeliveryModeModel> deliveryModes = zoneDeliveryModeService.getAllDeliveryModes();
		assertEquals(expectedDeliveryModes.length, deliveryModes.size());
		final List<String> _expectedDeliveryModes = Arrays.asList(expectedDeliveryModes);
		final List<String> _deliveryModes = new ArrayList<String>();
		for (final DeliveryModeModel mode : deliveryModes)
		{
			_deliveryModes.add(mode.getCode());
		}
		final boolean same = CollectionUtils.isEqualCollection(_expectedDeliveryModes, _deliveryModes);
		assertTrue(same);
	}

	/**
	 * Tries to search for the supported delivery modes with the payment mode:
	 * <ul>
	 * <li>found payment mode with "creditcard",</li>
	 * <li>found all supported delivery modes for this payment mode, which is 5</li>
	 * </ul>
	 */
	@Test
	public void testGetSupportedDeliveryModes()
	{
		final PaymentModeModel creditCartPaymentMode = paymentModeService.getPaymentModeForCode("creditcard");
		assertNotNull(creditCartPaymentMode);
		final Collection<DeliveryModeModel> deliveryModes = zoneDeliveryModeService
				.getSupportedDeliveryModes(creditCartPaymentMode);
		//2 DeliveryMode and 3 ZoneDeliveryMode
		assertEquals(5, deliveryModes.size());
	}

	/**
	 * Tries to search for the zone with code:
	 * <ul>
	 * <li>successful with "europe",</li>
	 * <li>caught UnknownIdentifierException with "No_Such_Zone",</li>
	 * <li>found all zones, whose size is 3.</li>
	 * </ul>
	 */
	@Test
	public void testGetZone()
	{
		String zoneCode = europeZoneCode;
		ZoneModel zone = zoneDeliveryModeService.getZoneForCode(zoneCode);
		assertNotNull(zone);
		assertEquals(zoneCode, zone.getCode());

		zoneCode = "No_Such_Zone";
		try
		{
			zone = zoneDeliveryModeService.getZoneForCode(zoneCode);
			fail("the zone code [" + zone + "] should NOT be found.");
		}
		catch (final UnknownIdentifierException ue)
		{
			//expected
		}

		final Collection<ZoneModel> zones = zoneDeliveryModeService.getAllZones();
		assertEquals(4, zones.size());
	}

	/**
	 * Tries to search for the zones with the specific {@link ZoneDeliveryModeModel}:
	 * <ul>
	 * <li>found 3 zones for "dhl".</li>
	 * <li>found 2 zones for "fedex".</li>
	 * <li>found 3 zones for "ups".</li>
	 * </ul>
	 */
	@Test
	public void testGetZonesForDeliveryMode()
	{
		String deliveryModeCode = dhlDeliveryModeCode;
		testGetZonesForDeliveryMode(deliveryModeCode, 3);
		deliveryModeCode = fedexDeliveryModeCode;
		testGetZonesForDeliveryMode(deliveryModeCode, 2);
		deliveryModeCode = upsDeliveryModeCode;
		testGetZonesForDeliveryMode(deliveryModeCode, 3);
	}

	private void testGetZonesForDeliveryMode(final String deliveryModeCode, final int zoneSize)
	{
		final DeliveryModeModel deliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(deliveryModeCode);
		assertNotNull(deliveryMode);
		assertEquals(deliveryModeCode, deliveryMode.getCode());
		final Collection<ZoneModel> zones = zoneDeliveryModeService
				.getZonesForZoneDeliveryMode((ZoneDeliveryModeModel) deliveryMode);
		assertEquals(zoneSize, zones.size());
	}

	/**
	 * Tries to search for the supported currencies in the {@link ZoneModel} and with the {@link ZoneDeliveryModeModel}:
	 * <ul>
	 * <li>retrieves {@link ZoneDeliveryModeModel} with code "dhl",</li>
	 * <li>retrieves {@link ZoneModel} with code "de",</li>
	 * <li>tests there is one supported currency for the found {@link ZoneModel} and {@link ZoneDeliveryModeModel}</li>
	 * <li>retrieves {@link ZoneDeliveryModeModel} with code "ups",</li>
	 * <li>retrieves {@link ZoneModel} with code "world",</li>
	 * <li>tests there are 2 supported currencies for the found {@link ZoneModel} and {@link ZoneDeliveryModeModel}</li>
	 * </ul>
	 */
	@Test
	public void testGetCurrencies()
	{
		testGetCurrencies(dhlDeliveryModeCode, deZoneCode, 1);
		testGetCurrencies(upsDeliveryModeCode, worldZoneCode, 2);
	}

	private void testGetCurrencies(final String deliveryModeCode, final String zoneCode, final int amount)
	{
		final DeliveryModeModel deliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(deliveryModeCode);
		final ZoneModel zone = zoneDeliveryModeService.getZoneForCode(zoneCode);
		final Collection<CurrencyModel> currencies = zoneDeliveryModeService.getCurrencies(zone,
				(ZoneDeliveryModeModel) deliveryMode);
		assertEquals(amount, currencies.size());
	}

	/**
	 * Tries to set the delivery value with the currency and minimum value in the zone and with the specific delivery
	 * mode.
	 * <ul>
	 * <li>tests that it costs 10 Euro in Germany, with dhl and with minimum value 0 Euro,</li>
	 * <li>tests that there are 3(unchanged) delivery values after the setter.</li>
	 * <li>tests that it costs 5 Euro in Germany, with dhl and with minimum value 10 Euro,</li>
	 * <li>tests that there are 4(one more) delivery values after the setter.</li>
	 * <li>tests that another zone delivery mode value with the same minimum value cannot be created.</li>
	 * </ul>
	 */
	@Test
	public void testSetDeliveryCost()
	{
		//YXX test null parameters
		final String euroCurrency = "EUR";
		Double min = Double.valueOf(0);
		Double deliveryValue = Double.valueOf(10);

		testSetDeliveryCost(euroCurrency, deZoneCode, dhlDeliveryModeCode, min, deliveryValue, 3, false);
		min = Double.valueOf(10);
		deliveryValue = Double.valueOf(5);
		testSetDeliveryCost(euroCurrency, deZoneCode, dhlDeliveryModeCode, min, deliveryValue, 4, false);
		deliveryValue = Double.valueOf(15);
		testSetDeliveryCost(euroCurrency, deZoneCode, dhlDeliveryModeCode, min, deliveryValue, 4, true);
	}

	private void testSetDeliveryCost(final String currencyCode, final String zoneCode, final String deliveryModeCode,
			final Double min, final Double value, final int afterSetterAmount, final boolean newModel)
	{
		final CurrencyModel currency = commonI18NService.getCurrency(currencyCode);
		final ZoneModel zone = zoneDeliveryModeService.getZoneForCode(zoneCode);
		final DeliveryModeModel deliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(deliveryModeCode);
		if (newModel)
		{
			final ZoneDeliveryModeValueModel zoneDeliveryModeValue = modelService.create(ZoneDeliveryModeValueModel.class);
			zoneDeliveryModeValue.setDeliveryMode((ZoneDeliveryModeModel) deliveryMode);
			zoneDeliveryModeValue.setCurrency(currency);
			zoneDeliveryModeValue.setZone(zone);
			zoneDeliveryModeValue.setMinimum(min);
			zoneDeliveryModeValue.setValue(value);
			try
			{
				modelService.save(zoneDeliveryModeValue);
				fail("a new zone delivery mode value should not be creatd");
			}
			catch (final ModelSavingException e)
			{
				//expected
			}
		}
		else
		{
			try
			{
				final ZoneDeliveryModeValueModel zoneDeliveryModeValue = zoneDeliveryModeService.setDeliveryCost(currency, min,
						value, zone, (ZoneDeliveryModeModel) deliveryMode);
				modelService.save(zoneDeliveryModeValue);
				assertEquals(value.doubleValue(), zoneDeliveryModeValue.getValue().doubleValue(), 0.01);
			}
			catch (final DeliveryModeInterceptorException dme)
			{
				fail(dme.getMessage());
			}
			final Map<Double, Double> deliveryValues = zoneDeliveryModeService.getDeliveryValues(currency, zone,
					(ZoneDeliveryModeModel) deliveryMode);
			assertEquals(afterSetterAmount, deliveryValues.size());
		}
	}

	/**
	 * Tries to search for the delivery value with the currency in the zone and with the specific delivery mode.
	 * <ul>
	 * <li>tests that it costs 6 Euro in Germany, with dhl and with minimum value 0 Euro,</li>
	 * <li>tests that it costs 4 Euro in Germany, with dhl and with minimum value 20 Euro,</li>
	 * <li>tests that an UnknownIdentifierException is thrown in Germany, with dhl and with minimum value 10 Euro,</li>
	 * <li>tests that it is free shipping in Germany, with dhl and with minimum value 50 Euro.</li>
	 * <li>tests that it costs 15 USD in Europe, with dhl and with minimum value 0 USD.</li>
	 * </ul>
	 */
	@Test
	public void testGetDeliveryValue()
	{
		final String euroCurrency = "EUR";
		final String usdCurrency = "USD";

		Double min = Double.valueOf(0);
		Double deliveryValue = Double.valueOf(6);
		testGetDeliveryValue(euroCurrency, deZoneCode, dhlDeliveryModeCode, min, deliveryValue, null);
		min = Double.valueOf(20);
		deliveryValue = Double.valueOf(4);
		testGetDeliveryValue(euroCurrency, deZoneCode, dhlDeliveryModeCode, min, deliveryValue, null);
		min = Double.valueOf(10);
		testGetDeliveryValue(euroCurrency, deZoneCode, dhlDeliveryModeCode, min, deliveryValue, UnknownIdentifierException.class);
		min = Double.valueOf(50);
		deliveryValue = Double.valueOf(0);
		testGetDeliveryValue(euroCurrency, deZoneCode, dhlDeliveryModeCode, min, deliveryValue, null);
		min = Double.valueOf(0);
		deliveryValue = Double.valueOf(15);
		testGetDeliveryValue(usdCurrency, europeZoneCode, dhlDeliveryModeCode, min, deliveryValue, null);
	}

	private void testGetDeliveryValue(final String currencyCode, final String zoneCode, final String deliveryModeCode,
			final Double min, final Double expectedDeliveryValue, final Class expectedExceptionClass)
	{
		final CurrencyModel currency = commonI18NService.getCurrency(currencyCode);
		final ZoneModel zone = zoneDeliveryModeService.getZoneForCode(zoneCode);
		final DeliveryModeModel deliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(deliveryModeCode);
		try
		{
			final ZoneDeliveryModeValueModel zoneDeliveryModeValue = zoneDeliveryModeService.getDeliveryValue(zone, currency, min,
					(ZoneDeliveryModeModel) deliveryMode);
			assertEquals(expectedDeliveryValue.doubleValue(), zoneDeliveryModeValue.getValue().doubleValue(), 0.01);
		}
		catch (final UnknownIdentifierException e)
		{
			if (e.getClass().equals(expectedExceptionClass))
			{ //NOPMD
			  //expected
			}
			else
			{
				throw e;
			}
		}
	}

	/**
	 * Tries to search for the delivery values with the currency in the zone and with the specific delivery mode.
	 * <ul>
	 * <li>tests there are 3 delivery cost values for EURO in Germany and with dhl,</li>
	 * <li>tests there are 2 delivery cost values for EURO in Europe and with dhl,</li>
	 * <li>tests there are 2 delivery cost values for EURO worldwide and with dhl.</li>
	 * <li>tests there is no delivery cost values for USD in Germany and with dhl,</li>
	 * <li>tests there is exactly one delivery cost value for USD worldwide and with dhl.</li>
	 * </ul>
	 */
	@Test
	public void testGetDeliveryValues()
	{
		final Map<Double, Double> deliveryValues = new HashMap<Double, Double>();
		final String euroCurrency = "EUR";
		final String usdCurrency = "USD";

		deliveryValues.put(Double.valueOf(0), Double.valueOf(6));
		deliveryValues.put(Double.valueOf(20), Double.valueOf(4));
		deliveryValues.put(Double.valueOf(50), Double.valueOf(0));
		testGetDeliveryValues(euroCurrency, deZoneCode, dhlDeliveryModeCode, deliveryValues);

		deliveryValues.clear();
		deliveryValues.put(Double.valueOf(0), Double.valueOf(10));
		deliveryValues.put(Double.valueOf(100), Double.valueOf(8));
		testGetDeliveryValues(euroCurrency, europeZoneCode, dhlDeliveryModeCode, deliveryValues);

		deliveryValues.clear();
		deliveryValues.put(Double.valueOf(0), Double.valueOf(15));
		deliveryValues.put(Double.valueOf(100), Double.valueOf(5));
		testGetDeliveryValues(euroCurrency, worldZoneCode, dhlDeliveryModeCode, deliveryValues);

		deliveryValues.clear();
		testGetDeliveryValues(usdCurrency, deZoneCode, dhlDeliveryModeCode, deliveryValues);

		deliveryValues.clear();
		deliveryValues.put(Double.valueOf(0), Double.valueOf(25));
		testGetDeliveryValues(usdCurrency, worldZoneCode, dhlDeliveryModeCode, deliveryValues);
	}

	private void testGetDeliveryValues(final String currencyCode, final String zoneCode, final String deliveryModeCode,
			final Map<Double, Double> expectedDeliveryValues)
	{
		final CurrencyModel currency = commonI18NService.getCurrency(currencyCode);
		final ZoneModel zone = zoneDeliveryModeService.getZoneForCode(zoneCode);
		final DeliveryModeModel deliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(deliveryModeCode);
		final Map<Double, Double> deliveryValues = zoneDeliveryModeService.getDeliveryValues(currency, zone,
				(ZoneDeliveryModeModel) deliveryMode);
		final boolean sameMap = compareMaps(expectedDeliveryValues, deliveryValues);
		assertTrue(sameMap);
	}

	private <K, V> boolean compareMaps(final Map<K, V> src, final Map<K, V> dest)
	{
		if (src.size() != dest.size())
		{
			return false;
		}
		for (final K key : src.keySet())
		{
			if (!src.get(key).equals(dest.get(key)))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Tests whether the specific zone is allowed for the delivery mode.
	 * <ul>
	 * <li>retrieves two delivery modes, "dhl" and "fedex"</li>
	 * <li>retrieves three zones, de, europe and world</li>
	 * <li>creates a new zone "mid_europe" including Germany and Austria</li>
	 * <li>tests the three old zones are allowed for both delivery modes</li>
	 * <li>tests the new zone is not allowed for the either of the delivery modes</li>
	 * </ul>
	 */
	@Test
	public void testIsZoneAllowed()
	{
		final DeliveryModeModel dhlDeliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(dhlDeliveryModeCode);
		final DeliveryModeModel fedexDeliveryMode = zoneDeliveryModeService.getDeliveryModeForCode(fedexDeliveryModeCode);
		final ZoneModel europeGermany = zoneDeliveryModeService.getZoneForCode(deZoneCode);
		final ZoneModel europeZone = zoneDeliveryModeService.getZoneForCode(europeZoneCode);
		final ZoneModel worldZone = zoneDeliveryModeService.getZoneForCode(worldZoneCode);

		final List<DeliveryModeModel> deliveryModes = new ArrayList<DeliveryModeModel>();
		deliveryModes.add(dhlDeliveryMode);
		deliveryModes.add(fedexDeliveryMode);

		final List<ZoneModel> zones = new ArrayList<ZoneModel>();
		zones.add(europeGermany);
		zones.add(europeZone);
		zones.add(worldZone);

		final ZoneModel ambiguousZone = createAmbiguousZone();

		for (final DeliveryModeModel _deliveryMode : deliveryModes)
		{
			for (final ZoneModel _zone : zones)
			{
				assertTrue(zoneDeliveryModeService.isZoneAllowed(_zone, (ZoneDeliveryModeModel) _deliveryMode));
			}
			assertFalse(zoneDeliveryModeService.isZoneAllowed(ambiguousZone, (ZoneDeliveryModeModel) _deliveryMode));
		}
	}

	/**
	 * Creates an ambiguous zone which includes Germany and Austria.
	 */
	private ZoneModel createAmbiguousZone()
	{
		final CountryModel countryGermany = commonI18NService.getCountry("DE");
		final CountryModel countryAustria = commonI18NService.getCountry("AT");
		final Set<CountryModel> countries = new HashSet<CountryModel>();
		countries.add(countryGermany);
		countries.add(countryAustria);
		final ZoneModel zone = modelService.create(ZoneModel.class);
		zone.setCode("mid_europe");
		zone.setCountries(countries);
		modelService.save(zone);
		return zone;
	}

}
