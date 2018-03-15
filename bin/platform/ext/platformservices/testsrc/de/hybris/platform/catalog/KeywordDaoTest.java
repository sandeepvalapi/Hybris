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
package de.hybris.platform.catalog;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.daos.KeywordDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test for {@link KeywordDao}
 */
@IntegrationTest
public class KeywordDaoTest extends ServicelayerTest
{
	@Resource
	private KeywordDao keywordDao;

	@Resource
	private ModelService modelService;

	private CatalogVersionModel catalogVersion1, catalogVersion2;
	private KeywordModel keyword1, keyword2, keyword3, keyword4;

	private final static String KEYWORD_STRING_1 = "keyword1";
	private final static String KEYWORD_STRING_2 = "keyword2";
	private final static String KEYWORD_STRING_3 = "keyword3";
	private final static String KEYWORD_STRING_4 = "keyword4";

	@Before
	public void setUp() throws Exception
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("catalog");
		modelService.save(catalog);

		catalogVersion1 = modelService.create(CatalogVersionModel.class);
		catalogVersion1.setCatalog(catalog);
		catalogVersion1.setVersion("catalogVersion1");
		modelService.save(catalogVersion1);

		catalogVersion2 = modelService.create(CatalogVersionModel.class);
		catalogVersion2.setCatalog(catalog);
		catalogVersion2.setVersion("catalogVersion2");
		modelService.save(catalogVersion2);

		final LanguageModel language = modelService.create(LanguageModel.class);
		language.setIsocode("MyLanguage");
		modelService.save(language);

		final LanguageModel language2 = modelService.create(LanguageModel.class);
		language2.setIsocode("MyLanguage2");
		modelService.save(language2);

		keyword1 = createKeyword(catalogVersion1, KEYWORD_STRING_1, language);
		keyword2 = createKeyword(catalogVersion1, KEYWORD_STRING_2, language);
		keyword3 = createKeyword(catalogVersion1, KEYWORD_STRING_3, language);
		keyword4 = createKeyword(catalogVersion1, KEYWORD_STRING_4, language2);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.daos.KeywordDao#getKeyword(de.hybris.platform.catalog.model.CatalogVersionModel, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetKeywordCatalogVersionModelString()
	{
		assertThat(keywordDao.getKeywords(catalogVersion1, KEYWORD_STRING_1)).containsOnly(keyword1);
		assertThat(keywordDao.getKeywords(catalogVersion1, KEYWORD_STRING_2)).containsOnly(keyword2);
		assertThat(keywordDao.getKeywords(catalogVersion2, KEYWORD_STRING_2)).isEmpty();
		assertThat(keywordDao.getKeywords(catalogVersion1, "code")).isEmpty();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.daos.KeywordDao#getKeyword(java.lang.String, de.hybris.platform.catalog.model.CatalogVersionModel, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetKeywordStringCatalogVersionModelString()
	{
		assertThat(keywordDao.getKeywords(KeywordModel._TYPECODE, catalogVersion1, KEYWORD_STRING_3)).containsOnly(keyword3);

		assertThat(keywordDao.getKeywords(KeywordModel._TYPECODE, catalogVersion1, KEYWORD_STRING_1)).containsOnly(keyword1);
		assertThat(keywordDao.getKeywords(KeywordModel._TYPECODE, catalogVersion1, KEYWORD_STRING_2)).containsOnly(keyword2);
		assertThat(keywordDao.getKeywords(KeywordModel._TYPECODE, catalogVersion2, KEYWORD_STRING_2)).isEmpty();
		assertThat(keywordDao.getKeywords(KeywordModel._TYPECODE, catalogVersion1, "code")).isEmpty();
	}

	private KeywordModel createKeyword(final CatalogVersionModel catalogVersion, final String keyword, final LanguageModel language)
	{
		final KeywordModel keywordModel = modelService.create(KeywordModel.class);
		keywordModel.setCatalogVersion(catalogVersion);
		keywordModel.setKeyword(keyword);
		keywordModel.setLanguage(language);
		modelService.save(keywordModel);
		return keywordModel;
	}





}
