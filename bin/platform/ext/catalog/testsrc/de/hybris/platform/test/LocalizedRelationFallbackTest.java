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
package de.hybris.platform.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * See PLA-10952 and PLA-8589 - localization fallback does not work for any collection ( mostly relation ) based
 * attribute !
 */
@IntegrationTest
public class LocalizedRelationFallbackTest extends ServicelayerBaseTest
{

	@Resource
	I18NService i18nService;

	@Resource
	CommonI18NService commonI18NService;

	@Resource
	ModelService modelService;

	Product productJalo;
	ProductModel product;

	Keyword keyDE1Jalo, keyDE2Jalo, keyENJalo;
	KeywordModel keyDE1, keyDE2, keyEN;

	Language deJalo, enJalo, de_DE_Jalo;
	LanguageModel de, en, de_DE;
	Locale l_de, l_en, l_de_DE;

	@Before
	public void setUp()
	{
		de = modelService.get(deJalo = getOrCreateLanguage("de"));
		en = modelService.get(enJalo = getOrCreateLanguage("en"));
		de_DE = modelService.get(de_DE_Jalo = getOrCreateLanguage("de_DE"));

		l_de = commonI18NService.getLocaleForLanguage(de);
		l_de_DE = commonI18NService.getLocaleForLanguage(de_DE);
		l_en = commonI18NService.getLocaleForLanguage(en);

		de_DE.setFallbackLanguages(Collections.singletonList(de));
		modelService.save(de_DE);

		final CatalogModel c = modelService.create(CatalogModel.class);
		c.setId("cat");
		modelService.save(c);

		final CatalogVersionModel cv = modelService.create(CatalogVersionModel.class);
		cv.setCatalog(c);
		cv.setVersion("ver");
		modelService.save(cv);

		keyDE1 = modelService.create(KeywordModel.class);
		keyDE1.setCatalogVersion(cv);
		keyDE1.setKeyword("keyDE1");
		keyDE1.setLanguage(de);

		keyDE2 = modelService.create(KeywordModel.class);
		keyDE2.setCatalogVersion(cv);
		keyDE2.setKeyword("keyDE2");
		keyDE2.setLanguage(de);

		keyEN = modelService.create(KeywordModel.class);
		keyEN.setCatalogVersion(cv);
		keyEN.setKeyword("keyEN");
		keyEN.setLanguage(en);

		modelService.saveAll(keyDE1, keyDE2, keyEN);

		keyDE1Jalo = modelService.getSource(keyDE1);
		keyDE2Jalo = modelService.getSource(keyDE2);
		keyENJalo = modelService.getSource(keyEN);

		product = modelService.create(ProductModel.class);
		product.setCode("product");
		product.setCatalogVersion(cv);
		product.setName("nameDE", l_de);
		product.setName("nameEN", l_en);
		product.setKeywords(Arrays.asList(keyDE1, keyDE2), l_de);
		product.setKeywords(Arrays.asList(keyEN), l_en);

		modelService.save(product);

		productJalo = modelService.getSource(product);
	}

	@Test
	public void testFallbackForLocalizedRelationsJalo()
	{
		// built on the assumption that Product<--->Keyword is a localized relation

		final Object before = jaloSession.getSessionContext().getAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED);
		try
		{
			final SessionContext ctxDE = jaloSession.createSessionContext();
			ctxDE.setLanguage(deJalo);
			final SessionContext ctxDE_DE = jaloSession.createSessionContext();
			ctxDE_DE.setLanguage(de_DE_Jalo);
			final SessionContext ctxDE_DE_FALLBACK = jaloSession.createSessionContext();
			ctxDE_DE_FALLBACK.setLanguage(de_DE_Jalo);
			ctxDE_DE_FALLBACK.setAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
			final SessionContext ctxEN = jaloSession.createSessionContext();
			ctxEN.setLanguage(enJalo);

			final CatalogManager cm = CatalogManager.getInstance();

			assertEquals("nameDE", productJalo.getName(ctxDE));
			assertEquals("nameEN", productJalo.getName(ctxEN));
			assertNull(productJalo.getName(ctxDE_DE));
			assertEquals(productJalo.getName(ctxDE), productJalo.getName(ctxDE_DE_FALLBACK));

			assertEquals(Arrays.asList(keyDE1Jalo, keyDE2Jalo), cm.getKeywords(ctxDE, productJalo));
			assertEquals(Arrays.asList(keyENJalo), cm.getKeywords(ctxEN, productJalo));
			assertEquals(Collections.EMPTY_LIST, cm.getKeywords(ctxDE_DE, productJalo));
			assertEquals(cm.getKeywords(ctxDE, productJalo), cm.getKeywords(ctxDE_DE_FALLBACK, productJalo));
		}
		finally
		{
			jaloSession.getSessionContext().setAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED, before);
		}
	}

	@Test
	public void testFallbackForLocalizedRelationsServiceLayer()
	{
		// built on the assumption that Product<--->Keyword is a localized relation

		final boolean before = i18nService.isLocalizationFallbackEnabled();
		try
		{
			assertFalse(before);

			assertEquals("nameDE", product.getName(l_de));
			assertEquals("nameEN", product.getName(l_en));
			assertNull(product.getName(l_de_DE));

			i18nService.setLocalizationFallbackEnabled(true);
			assertEquals(product.getName(l_de), product.getName(l_de_DE));
			i18nService.setLocalizationFallbackEnabled(false);

			assertEquals(Arrays.asList(keyDE1, keyDE2), product.getKeywords(l_de));
			assertEquals(Arrays.asList(keyEN), product.getKeywords(l_en));
			assertEquals(Collections.EMPTY_LIST, product.getKeywords(l_de_DE));

			i18nService.setLocalizationFallbackEnabled(true);
			assertEquals(product.getKeywords(l_de), product.getKeywords(l_de_DE));
			i18nService.setLocalizationFallbackEnabled(false);
		}
		finally
		{
			i18nService.setLocalizationFallbackEnabled(before);
		}
	}
}
