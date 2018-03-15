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
package de.hybris.platform.catalog.jalo;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Test for illustrating HOR-705 problem, wityh localized links e.g {@link Keyword}'s for a product.
 */
@Ignore("PLA-8589")
@IntegrationTest
public class LocalizedKeywordTest extends HybrisJUnit4TransactionalTest
{
	@Test
	@Ignore("PLA-8589")
	public void testLocalizabeleLinks() throws ConsistencyCheckException
	{
		final CatalogManager cmanager = CatalogManager.getInstance();
		//


		final CatalogVersion version = cmanager.createCatalogVersion(cmanager.createCatalog("sampleLocalizedKeywordCatalog"),
				"sampleCatalogVersion", jaloSession.getSessionContext().getLanguage());

		// prepare langugaes and fallbacks 
		final Language polish = jaloSession.getC2LManager().createLanguage("polski");
		final Language czech = jaloSession.getC2LManager().createLanguage("czeski");
		final Language japan = jaloSession.getC2LManager().createLanguage("japan");
		polish.setFallbackLanguages(czech);

		final Product emptyPolishKeywordProduct = jaloSession.getProductManager().createProduct("someSampleProductWithAllLanguage");
		emptyPolishKeywordProduct.setCode("newcode1");

		final Product emptyJapanKeywordProduct = jaloSession.getProductManager().createProduct("someSampleProductWithJapan");
		emptyJapanKeywordProduct.setCode("newcode2");

		final Keyword polishKeyword = CatalogManager.getInstance().createKeyword(version, "polish_keyword", polish);

		final Keyword czechKeyword = CatalogManager.getInstance().createKeyword(version, "czech_keyword", czech);

		final Keyword japanKeyword = CatalogManager.getInstance().createKeyword(version, "japan_keyword", japan);

		//create keywords for polish
		jaloSession.getSessionContext().setLanguage(polish);

		cmanager.setKeywords(emptyPolishKeywordProduct, Arrays.asList(new Keyword[] {}));

		cmanager.setKeywords(emptyJapanKeywordProduct, Arrays.asList(new Keyword[]
		{ polishKeyword }));

		//create keywords for czech
		jaloSession.getSessionContext().setLanguage(czech);

		cmanager.setKeywords(emptyPolishKeywordProduct, Arrays.asList(new Keyword[]
		{ czechKeyword, polishKeyword }));

		cmanager.setKeywords(emptyJapanKeywordProduct, Arrays.asList(new Keyword[]
		{ czechKeyword }));

		//create keywrods for japan
		jaloSession.getSessionContext().setLanguage(japan);

		cmanager.setKeywords(emptyPolishKeywordProduct, Arrays.asList(new Keyword[]
		{ japanKeyword }));

		cmanager.setKeywords(emptyJapanKeywordProduct, Arrays.asList(new Keyword[] {}));

		//polish language
		jaloSession.getSessionContext().setLanguage(polish);

		//fallback to czech
		Assert.assertEquals("should use czech keywords list", 2, cmanager.getKeywords(emptyPolishKeywordProduct).size());
		Assert.assertEquals("should be one czech keyword", "czech_keyword", cmanager.getKeywords(emptyPolishKeywordProduct).get(0)
				.getKeyword());
		Assert.assertEquals("should be one polish keyword", "polish_keyword", cmanager.getKeywords(emptyPolishKeywordProduct)
				.get(1).getKeyword());

		Assert.assertEquals("should be one polish keyword", 1, cmanager.getKeywords(emptyJapanKeywordProduct).size());
		Assert.assertEquals("should be one polish keyword", "polish_keyword", cmanager.getKeywords(emptyJapanKeywordProduct).get(0)
				.getKeyword());

		//czech language
		jaloSession.getSessionContext().setLanguage(czech);

		Assert.assertEquals("should use czech keywords list", 2, cmanager.getKeywords(emptyPolishKeywordProduct).size());
		Assert.assertEquals("should be one czech keyword", "czech_keyword", cmanager.getKeywords(emptyPolishKeywordProduct).get(0)
				.getKeyword());
		Assert.assertEquals("should be one polish keyword", "polish_keyword", cmanager.getKeywords(emptyPolishKeywordProduct)
				.get(1).getKeyword());

		Assert.assertEquals("should be one czech keyword", 1, cmanager.getKeywords(emptyJapanKeywordProduct).size());
		Assert.assertEquals("should be one czech keyword", "czech_keyword", cmanager.getKeywords(emptyJapanKeywordProduct).get(0)
				.getKeyword());


		//japan language
		jaloSession.getSessionContext().setLanguage(japan);
		Assert.assertEquals("should be one japan keyword", 1, cmanager.getKeywords(emptyPolishKeywordProduct).size());
		Assert.assertEquals("should be one czech keyword", "japan_keyword", cmanager.getKeywords(emptyPolishKeywordProduct).get(0)
				.getKeyword());

		//nofallback
		Assert.assertEquals("should be none japan keyword", 0, cmanager.getKeywords(emptyJapanKeywordProduct).size());

	}
}
