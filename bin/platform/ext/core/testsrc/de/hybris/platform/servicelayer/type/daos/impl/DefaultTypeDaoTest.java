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
package de.hybris.platform.servicelayer.type.daos.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.TestImportCsvUtil;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.daos.TypeDao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultTypeDaoTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private TypeDao typeDao;


	@Resource(name = "testImportCsvUtil")
	private TestImportCsvUtil importCsvUtil;


	@Test
	public void shouldFindComposedTypeByCode() throws Exception
	{
		// given 
		importCsvUtil.importCsv("/servicelayer/test/testTypeDao.csv", "UTF-8");

		// when
		final ComposedTypeModel actualModel = typeDao.findComposedTypeByCode("TestComposedTypeChild");

		// then
		assertThat(actualModel).isNotNull();
		assertThat(actualModel.getCode()).isEqualTo("TestComposedTypeChild");
		assertThat(actualModel.getSuperType().getCode()).isEqualTo("TestComposedTypeParent");
	}

	@Test
	public void shouldNotFindComposedTypeByNotExistingCode() throws Exception
	{
		// given 

		// when
		try
		{
			typeDao.findComposedTypeByCode("ThisIsReallyNotExistingComposedTypeCode");
			fail("Not existing type found");
		}
		catch (final UnknownIdentifierException e)
		{
			// then Excteption shoud be thrown

		}

	}

	@Test
	public void shouldNotFindComposedTypeByCollectionTypeCode() throws Exception
	{
		// given 
		importCsvUtil.importCsv("/servicelayer/test/testTypeDao.csv", "UTF-8");

		// when
		try
		{
			typeDao.findComposedTypeByCode("TestCollectionType");
			fail("Wrong type found (CollectionType is not ComposedType");
		}
		catch (final UnknownIdentifierException e)
		{
			// then Excteption shoud be thrown

		}

	}

	@Test
	public void shouldFindTypeByCollectionTypeCode() throws Exception
	{
		// given 
		importCsvUtil.importCsv("/servicelayer/test/testTypeDao.csv", "UTF-8");

		// when
		final TypeModel actualModel = typeDao.findTypeByCode("TestCollectionType");

		// then
		assertThat(actualModel).isNotNull();
		assertThat(actualModel.getCode()).isEqualTo("TestCollectionType");
	}

	@Test
	public void shouldFindTypeByTypeCode() throws Exception
	{
		// given 
		importCsvUtil.importCsv("/servicelayer/test/testTypeDao.csv", "UTF-8");

		// when
		final TypeModel actualModel = typeDao.findTypeByCode("TestComposedTypeParent");

		// then
		assertThat(actualModel).isNotNull();
		assertThat(actualModel.getCode()).isEqualTo("TestComposedTypeParent");
	}

	@Test
	public void shouldNotFindTypeByNotExistingCode() throws Exception
	{
		// given 

		// when
		try
		{
			typeDao.findComposedTypeByCode("ThisIsReallyNotExistingTypeCode");
			fail("Not existing type found");
		}
		catch (final UnknownIdentifierException e)
		{
			// then Excteption shoud be thrown

		}

	}

	@Test
	public void shouldFindAtomicTypeByCode() throws Exception
	{
		// given that system is initialized with atomic type java.lang.String

		// when
		final AtomicTypeModel actualModel = typeDao.findAtomicTypeByCode("java.lang.String");

		// then
		assertNotNull("Atomic type can't be null", actualModel);
		assertEquals("Different object found", "java.lang.String", actualModel.getCode());
	}

	@Test
	public void shouldNotFindAtomicTypeForNotExistingCode() throws Exception
	{
		// given 

		// when
		try
		{
			typeDao.findAtomicTypeByCode("java.lang.SSSSStttttrrrrriiiinnngggg");
			Assert.fail("Not existing type found");
		}
		catch (final UnknownIdentifierException e)
		{
			// then Excteption shoud be thrown

		}

	}

}
