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

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.impl.DefaultKeywordService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.fest.assertions.Fail;
import org.junit.Before;
import org.junit.Test;


/**
 * Test {@link de.hybris.platform.catalog.KeywordService}
 */
@DemoTest
public class KeywordDemoTest extends ServicelayerTransactionalTest
{
	@Resource
	private KeywordService keywordService;

	@Resource
	private ModelService modelService;


	private CatalogVersionModel catalogVersion1, catalogVersion2;
	private KeywordModel keyword1, keyword2;

	private final static String KEYWORD_STRING_1 = "keyword1";
	private final static String KEYWORD_STRING_2 = "keyword2";

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

		keyword1 = createKeyword(catalogVersion1, KEYWORD_STRING_1, language);
		keyword2 = createKeyword(catalogVersion1, KEYWORD_STRING_2, language);
	}

	/**
	 * Test method for
	 * {@link DefaultKeywordService#getKeyword(de.hybris.platform.catalog.model.CatalogVersionModel, java.lang.String)} .
	 * 
	 * Use {@link #createKeyword(CatalogVersionModel, String, LanguageModel)} in {@link #setUp()} function to creates
	 * sample data and than search for them.
	 */
	@Test
	public void testGetKeywordCatalogVersionModelString()
	{
		assertThat(keywordService.getKeyword(catalogVersion1, KEYWORD_STRING_1)).isEqualTo(keyword1);
		assertThat(keywordService.getKeyword(catalogVersion1, KEYWORD_STRING_2)).isEqualTo(keyword2);
		try
		{
			assertThat(keywordService.getKeyword(catalogVersion2, KEYWORD_STRING_2));
			Fail.fail();
		}
		catch (final UnknownIdentifierException e)
		{
			//keyword by given parameters could not be found - it not exist and the exception is correct.
		}
		try
		{
			assertThat(keywordService.getKeyword(catalogVersion1, "code"));
			Fail.fail();
		}
		catch (final UnknownIdentifierException e)
		{
			//keyword by given parameters could not be found - it not exist and the exception is correct.
		}
	}

	/**
	 * Test method for
	 * {@link DefaultKeywordService#getKeyword(java.lang.String, de.hybris.platform.catalog.model.CatalogVersionModel, java.lang.String)}
	 * . Use {@link #createKeyword(CatalogVersionModel, String, LanguageModel)} in {@link #setUp()} function to creates
	 * sample data and than search for them.
	 */
	@Test
	public void testGetKeywordStringCatalogVersionModelString()
	{
		assertThat(keywordService.getKeyword(KeywordModel._TYPECODE, catalogVersion1, KEYWORD_STRING_1)).isEqualTo(keyword1);
		assertThat(keywordService.getKeyword(KeywordModel._TYPECODE, catalogVersion1, KEYWORD_STRING_2)).isEqualTo(keyword2);

		try
		{
			assertThat(keywordService.getKeyword(KeywordModel._TYPECODE, catalogVersion2, KEYWORD_STRING_2));
			Fail.fail();
		}
		catch (final UnknownIdentifierException e)
		{
			//keyword by given parameters could not be found - it not exist and the exception is correct.
		}
		try
		{
			assertThat(keywordService.getKeyword(KeywordModel._TYPECODE, catalogVersion1, "code"));
			Fail.fail();
		}
		catch (final UnknownIdentifierException e)
		{
			//keyword by given parameters could not be found - it not exist and the exception is correct.
		}
	}

	/**
	 * Create keyword by given parameters.
	 */
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
