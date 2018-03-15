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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.daos.KeywordDao;
import de.hybris.platform.catalog.impl.DefaultKeywordService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Arrays;
import java.util.Collections;

import org.fest.assertions.Assertions;
import org.fest.assertions.Fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * tests {@link DefaultKeywordService}
 */
@UnitTest
public class KeywordServiceTest
{
	private DefaultKeywordService keywordService;

	@Mock
	private KeywordDao keywordDao;


	String keyword = "keyword";
	String typecode = "Typecode";
	CatalogVersionModel catalogVersion = new CatalogVersionModel();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		keywordService = new DefaultKeywordService();
		keywordService.setKeywordDao(keywordDao);


	}

	@Test
	public void testGetKeyward()
	{
		final KeywordModel keywordModel = new KeywordModel();
		Mockito.when(keywordDao.getKeywords(catalogVersion, keyword)).thenReturn(Arrays.asList(keywordModel));

		Assertions.assertThat(keywordService.getKeyword(catalogVersion, keyword)).isSameAs(keywordModel);
	}


	@Test
	public void testGetKeywardFailToMany()
	{

		Mockito.when(keywordDao.getKeywords(catalogVersion, keyword)).thenReturn(
				Arrays.asList(new KeywordModel(), new KeywordModel()));

		try
		{
			keywordService.getKeyword(catalogVersion, keyword);
			Fail.fail();
		}
		catch (final AmbiguousIdentifierException e)
		{
			//ok
		}
	}

	@Test
	public void testGetKeywardFailNullArg()
	{
		try
		{
			keywordService.getKeyword(null, keyword);
			Fail.fail();
		}
		catch (final IllegalArgumentException e)
		{
			//ok
		}

		try
		{
			keywordService.getKeyword(catalogVersion, null);
			Fail.fail();
		}
		catch (final IllegalArgumentException e)
		{
			//ok
		}
	}

	@Test
	public void testGetKeywardFailEmpty()
	{

		Mockito.when(keywordDao.getKeywords(catalogVersion, keyword)).thenReturn(Collections.EMPTY_LIST);

		try
		{
			keywordService.getKeyword(catalogVersion, keyword);
			Fail.fail();
		}
		catch (final UnknownIdentifierException e)
		{
			//ok
		}
	}



	@Test
	public void testTypecodeGetKeyward()
	{
		final KeywordModel keywordModel = new KeywordModel();
		Mockito.when(keywordDao.getKeywords(typecode, catalogVersion, keyword)).thenReturn(Arrays.asList(keywordModel));

		Assertions.assertThat(keywordService.getKeyword(typecode, catalogVersion, keyword)).isSameAs(keywordModel);
	}


	@Test
	public void testTypecodeGetKeywardFailToMany()
	{

		Mockito.when(keywordDao.getKeywords(typecode, catalogVersion, keyword)).thenReturn(
				Arrays.asList(new KeywordModel(), new KeywordModel()));

		try
		{
			keywordService.getKeyword(typecode, catalogVersion, keyword);
			Fail.fail();
		}
		catch (final AmbiguousIdentifierException e)
		{
			//ok
		}
	}

	@Test
	public void testTypecodeGetKeywardFailEmpty()
	{

		Mockito.when(keywordDao.getKeywords(typecode, catalogVersion, keyword)).thenReturn(Collections.EMPTY_LIST);

		try
		{
			keywordService.getKeyword(typecode, catalogVersion, keyword);
			Fail.fail();
		}
		catch (final UnknownIdentifierException e)
		{
			//ok
		}
	}



	@Test
	public void testTypecodeGetKeywardFailNullArg()
	{
		try
		{
			keywordService.getKeyword(null, catalogVersion, keyword);
			Fail.fail();
		}
		catch (final IllegalArgumentException e)
		{
			//ok
		}


		try
		{
			keywordService.getKeyword(typecode, null, keyword);
			Fail.fail();
		}
		catch (final IllegalArgumentException e)
		{
			//ok
		}

		try
		{
			keywordService.getKeyword(typecode, catalogVersion, null);
			Fail.fail();
		}
		catch (final IllegalArgumentException e)
		{
			//ok
		}
	}
}
