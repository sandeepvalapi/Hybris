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
package de.hybris.platform.order.daos;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests {@link ZoneDeliveryModeValueDao}
 */
@IntegrationTest
public class ZoneDeliveryModeValueDaoTest extends ServicelayerTest
{

	@Resource
	private ZoneDeliveryModeValueDao zoneDeliveryModeValueDao;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;

	private CurrencyModel pln;
	private CountryModel poland;
	private ZoneModel polandZone;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/servicelayer/test/testDeliveryMode.csv", "windows-1252");

		pln = modelService.create(CurrencyModel.class);
		pln.setIsocode("PLN");
		pln.setConversion(Double.valueOf(0.76d));
		pln.setSymbol("PLN");
		pln.setDigits(Integer.valueOf(3));
		pln.setName("polish zloty");

		poland = modelService.create(CountryModel.class);
		poland.setIsocode("PL");
		poland.setName("Poland");

		polandZone = modelService.create(ZoneModel.class);
		polandZone.setCode("poland");
		polandZone.setCountries(Collections.singleton(poland));

		modelService.saveAll(pln, poland, polandZone);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.order.daos.ZoneDeliveryModeValueDao#findZoneDeliveryModeValuesByCurrency(de.hybris.platform.core.model.c2l.CurrencyModel)}
	 * .
	 */
	@Test
	public void testFindZoneDeliveryModeValuesByCurrency()
	{
		final CurrencyModel gbpTemplate = new CurrencyModel();
		gbpTemplate.setIsocode("GBP");
		final CurrencyModel gbp = flexibleSearchService.getModelByExample(gbpTemplate);

		final Collection<ZoneDeliveryModeValueModel> gbpResult = zoneDeliveryModeValueDao.findZoneDeliveryModeValuesByCurrency(gbp);
		Assert.assertEquals("unexpected returned collection size", 1, gbpResult.size());

		final CurrencyModel usdTemplate = new CurrencyModel();
		usdTemplate.setIsocode("USD");
		final CurrencyModel usd = flexibleSearchService.getModelByExample(usdTemplate);

		final Collection<ZoneDeliveryModeValueModel> usdResult = zoneDeliveryModeValueDao.findZoneDeliveryModeValuesByCurrency(usd);
		Assert.assertEquals("unexpected returned collection size", 3, usdResult.size());


		final Collection<ZoneDeliveryModeValueModel> plnResult = zoneDeliveryModeValueDao.findZoneDeliveryModeValuesByCurrency(pln);
		Assert.assertEquals("unexpected returned collection size", 0, plnResult.size());

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.order.daos.ZoneDeliveryModeValueDao#findZoneDeliveryModeValuesByZone(de.hybris.platform.deliveryzone.model.ZoneModel)}
	 * .
	 */
	@Test
	public void testFindZoneDeliveryModeValuesByZone()
	{
		final ZoneModel europeTemplate = new ZoneModel();
		europeTemplate.setCode("europe");
		final ZoneModel europe = flexibleSearchService.getModelByExample(europeTemplate);

		final Collection<ZoneDeliveryModeValueModel> europeResult = zoneDeliveryModeValueDao
				.findZoneDeliveryModeValuesByZone(europe);
		Assert.assertEquals("unexpected returned collection size", 8, europeResult.size());

		final ZoneModel worldTemplate = new ZoneModel();
		worldTemplate.setCode("world");
		final ZoneModel world = flexibleSearchService.getModelByExample(worldTemplate);

		final Collection<ZoneDeliveryModeValueModel> worldResult = zoneDeliveryModeValueDao.findZoneDeliveryModeValuesByZone(world);
		Assert.assertEquals("unexpected returned collection size", 6, worldResult.size());

		final Collection<ZoneDeliveryModeValueModel> polandResult = zoneDeliveryModeValueDao
				.findZoneDeliveryModeValuesByZone(polandZone);
		Assert.assertEquals("unexpected returned collection size", 0, polandResult.size());
	}

}
