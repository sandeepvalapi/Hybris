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
package de.hybris.platform.servicelayer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.SingletonMap;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests saving maps by model service
 */
@IntegrationTest
public class ModelServiceMapTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18nService;

	@Resource
	private CommonI18NService commonI18NService;

	private ProductModel product;
	private Map<ArticleStatus, String> testMap;

	@Before
	public void setUp()
	{
		//setup test product
		i18nService.setCurrentLocale(Locale.ENGLISH);

		product = modelService.create(ProductModel.class);
		product.setCode("testproduct");
		product.setName("enName", Locale.ENGLISH);

		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("testCatalog");
		final CatalogVersionModel catver = modelService.create(CatalogVersionModel.class);
		catver.setVersion("testCatalogVersion");
		catver.setCatalog(catalog);
		product.setCatalogVersion(catver);

		modelService.save(product);

		//test correct creation of test  product
		assertEquals("no catalog", catalog, product.getCatalogVersion().getCatalog());
		assertTrue("catver was not saved", modelService.isUpToDate(product.getCatalogVersion()));
		assertFalse("catalog was not saved", modelService.isNew(catalog));
		assertEquals("set name normal fails", "enName", product.getName());

		//additional test data
		testMap = new HashMap<ArticleStatus, String>();
		testMap.put(ArticleStatus.BARGAIN, "that's a bargain no doubt!");

	}

	@Test
	public void testSetArticleStatusMapNormalWay()
	{
		product.setArticleStatus(testMap);
		modelService.save(product);

		final Map<ArticleStatus, String> returnMap = product.getArticleStatus();
		assertEquals("map does not contain one single element", 1, returnMap.size());
		assertTrue("map does not contain expected key", returnMap.containsKey(ArticleStatus.BARGAIN));
		assertEquals("map does not contain expected value", "that's a bargain no doubt!", returnMap.get(ArticleStatus.BARGAIN));
	}

	@Test
	public void testSetArticleStatusGenericWay()
	{
		modelService.setAttributeValue(product, ProductModel.ARTICLESTATUS, new SingletonMap(commonI18NService.getLanguage("en"),
				testMap));
		modelService.save(product);

		final Map<ArticleStatus, String> returnMap = product.getArticleStatus();
		assertEquals("map does not contain one single element", 1, returnMap.size());
		assertTrue("map does not contain expected key", returnMap.containsKey(ArticleStatus.BARGAIN));
		assertEquals("map does not contain expected value", "that's a bargain no doubt!", returnMap.get(ArticleStatus.BARGAIN));
	}

	@Test
	public void testSetLocalizedNameGenericWayWithLocales()
	{
		final Map<Locale, String> localeMap = new HashMap<Locale, String>(1);
		localeMap.put(Locale.ENGLISH, "newLocaleName");

		modelService.setAttributeValue(product, ProductModel.NAME, localeMap);
		modelService.save(product);

		assertEquals("false new locale name", "newLocaleName", product.getName());
	}

	@Test
	public void testSetLocalizedNameGenericWayWithLanguages()
	{
		final LanguageModel lang = commonI18NService.getLanguage("en");
		assertNotNull("didn't find en language", lang);

		final Map<LanguageModel, String> langMap = new HashMap<LanguageModel, String>(1);
		langMap.put(lang, "newLanguageName");

		modelService.setAttributeValue(product, ProductModel.NAME, langMap);
		modelService.save(product);

		assertEquals("false new language name", "newLanguageName", product.getName());
	}
}
