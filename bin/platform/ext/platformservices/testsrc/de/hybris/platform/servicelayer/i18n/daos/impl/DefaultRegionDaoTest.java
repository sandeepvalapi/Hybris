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
package de.hybris.platform.servicelayer.i18n.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.daos.RegionDao;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultRegionDaoTest extends ServicelayerTest
{

	private static final int CREATED_COUNT = 4;

	@Resource(name = "regionDao")
	private RegionDao regionDao;

	@Resource
	private ModelService modelService;

	private int initCount = 0;

	private CountryModel countryWithRegions = null;

	private CountryModel countryWithoutRegions = null;

	@Before
	public void prepare()
	{

		initCount = regionDao.findRegions().size();

		countryWithRegions = modelService.create(CountryModel.class);
		countryWithRegions.setIsocode("woobyland");


		final RegionModel model0 = modelService.create(RegionModel.class);
		model0.setIsocode("tinyRed");
		model0.setCountry(countryWithRegions);

		final RegionModel model1 = modelService.create(RegionModel.class);
		model1.setIsocode("bigGrey");
		model1.setCountry(countryWithRegions);

		final CountryModel otherCountry = modelService.create(CountryModel.class);
		otherCountry.setIsocode("neverland");
		//region with other country
		final RegionModel model2 = modelService.create(RegionModel.class);
		model2.setIsocode("smallWhite");
		model2.setCountry(otherCountry);

		final RegionModel model3 = modelService.create(RegionModel.class);
		model3.setIsocode("hugePink");
		model3.setCountry(countryWithRegions);

		countryWithoutRegions = modelService.create(CountryModel.class);
		countryWithoutRegions.setIsocode("glossolalia");


		modelService.saveAll();
		//
	}


	@Test
	public void testGetAllRegions()
	{
		final List<RegionModel> result = regionDao.findRegions();

		Assert.assertNotNull(result);
		Assert.assertEquals(CREATED_COUNT + initCount, result.size());
	}



	@Test(expected = IllegalArgumentException.class)
	public void testRegionByNullContryAndCode()
	{
		regionDao.findRegionsByCountryAndCode(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegionByExistingCountryAndNullCode()
	{
		regionDao.findRegionsByCountryAndCode(countryWithRegions, null);
	}

	@Test
	public void testRegionByExistingCountryAndCode1()
	{
		final List<RegionModel> result = regionDao.findRegionsByCountryAndCode(countryWithRegions, "bigGrey");

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
	}

	@Test
	public void testRegionByExistingCountryAndCode2()
	{
		final List<RegionModel> result = regionDao.findRegionsByCountryAndCode(countryWithRegions, "notExisting");

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testRegionByExistingCountryAndCode3()
	{
		final List<RegionModel> result = regionDao.findRegionsByCountryAndCode(countryWithRegions, "");

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

}
