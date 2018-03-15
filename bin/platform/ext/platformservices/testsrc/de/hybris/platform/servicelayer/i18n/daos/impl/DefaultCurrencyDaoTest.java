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
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for CurrencyModel Dao.
 */
@IntegrationTest
public class DefaultCurrencyDaoTest extends ServicelayerTest
{
	@Resource(name = "currencyDao")
	private CurrencyDao currencyDao;

	@Resource
	private ModelService modelService;

	private static final int CREATED_COUNT = 3;

	private int initCurrencyCount = 0;
	private int initBaseCurrencyCount = 0;

	@Before
	public void prepare()
	{
		initCurrencyCount = currencyDao.findCurrencies().size();
		initBaseCurrencyCount = currencyDao.findBaseCurrencies().size();
		final CurrencyModel cModel1 = modelService.create(CurrencyModel.class);
		cModel1.setIsocode("EUR");
		cModel1.setSymbol("EUR");
		final CurrencyModel cModel2 = modelService.create(CurrencyModel.class);
		cModel2.setIsocode("PLN");
		cModel2.setSymbol("PLN");
		final CurrencyModel cModel3 = modelService.create(CurrencyModel.class);
		cModel3.setIsocode("CHF");
		cModel3.setSymbol("CHF");

		modelService.saveAll();
	}

	@Test
	public void testGetCurrencies()
	{
		final List<CurrencyModel> resultList = currencyDao.findCurrencies();
		Assert.assertNotNull(resultList);
		Assert.assertEquals(CREATED_COUNT + initCurrencyCount, resultList.size());
	}

	@Test
	public void testGetCurrencyByCodeExist()
	{
		final List<CurrencyModel> resultList = currencyDao.findCurrenciesByCode("EUR");
		Assert.assertEquals(1, resultList.size());
	}

	@Test
	public void testGetCurrencyByCodeNotExist()
	{
		final List<CurrencyModel> resultList = currencyDao.findCurrenciesByCode("testCur");
		Assert.assertEquals(0, resultList.size());
	}

	/**
	 * Test illustrates that there could be only one base currency
	 */
	@Test
	public void testGetBaseCurrencyExist()
	{
		final CurrencyModel wellnessCurrency = modelService.create(CurrencyModel.class);
		wellnessCurrency.setIsocode("GBP");
		wellnessCurrency.setSymbol("GBP");
		wellnessCurrency.setBase(Boolean.TRUE);
		modelService.save(wellnessCurrency);

		Assert.assertEquals("Expected base currency count (initially = " + initBaseCurrencyCount + ")", 1, currencyDao
				.findBaseCurrencies().size());
		Assert.assertSame(wellnessCurrency, currencyDao.findBaseCurrencies().get(0));

		final CurrencyModel japsCurrency = modelService.create(CurrencyModel.class);
		japsCurrency.setIsocode("JPN");
		japsCurrency.setSymbol("JPN");
		japsCurrency.setBase(Boolean.TRUE);
		modelService.save(japsCurrency);

		final List<CurrencyModel> resultList = currencyDao.findBaseCurrencies();
		Assert.assertEquals("Expected base currency count (initially = " + initBaseCurrencyCount + ")", 1, resultList.size());
		Assert.assertSame(japsCurrency, currencyDao.findBaseCurrencies().get(0));
	}

	/**
	 * A little bit naive test there always have to be a base currency.
	 */
	@Test
	public void testGetBaseCurrencyNotExist()
	{

		final List<CurrencyModel> resultList = currencyDao.findBaseCurrencies();
		Assert.assertEquals("Expected base currency count (initially = " + initBaseCurrencyCount + ")", 0 + initBaseCurrencyCount,
				resultList.size());
	}
}
