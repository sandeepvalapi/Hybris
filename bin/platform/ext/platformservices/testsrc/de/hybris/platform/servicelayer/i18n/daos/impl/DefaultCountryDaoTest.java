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
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCountryDaoTest extends ServicelayerTest
{

	private static final int CREATED_COUNT = 4;

	@Resource(name = "countryDao")
	private CountryDao countryDao;

	@Resource
	private ModelService modelService;

	private int initCount = 0;

	@Before
	public void prepare()
	{
		initCount = countryDao.findCountries().size();

		final CountryModel model0 = modelService.create(CountryModel.class);
		model0.setIsocode("tinyRed");

		final CountryModel model1 = modelService.create(CountryModel.class);
		model1.setIsocode("bigGrey");

		final CountryModel model2 = modelService.create(CountryModel.class);
		model2.setIsocode("smallWhite");

		final CountryModel model3 = modelService.create(CountryModel.class);
		model3.setIsocode("hugePink");

		modelService.saveAll();
		//
	}


	@Test
	public void testGetAllCountries()
	{
		final List<CountryModel> result = countryDao.findCountries();

		Assert.assertNotNull(result);
		Assert.assertEquals(CREATED_COUNT + initCount, result.size());
	}


	@Test
	public void testCountryByNotExistingCountry()
	{
		final List<CountryModel> result = countryDao.findCountriesByCode("otherCountry");

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}


	@Test
	public void testCountryByExistingCountry()
	{
		final List<CountryModel> result = countryDao.findCountriesByCode("bigGrey");

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCountryByNullCountry()
	{
		countryDao.findCountriesByCode(null);
	}

	@Test
	public void testCountryByEmptyCountry()
	{
		final List<CountryModel> result = countryDao.findCountriesByCode("");

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

}
