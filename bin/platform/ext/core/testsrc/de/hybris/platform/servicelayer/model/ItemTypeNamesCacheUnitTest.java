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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class ItemTypeNamesCacheUnitTest
{
	@Before
	public void setUp()
	{
		MockModel._TYPECODE = "MockType";
	}

	@Test
	public void shouldReturnItemTypeCodeFromModelClass()
	{
		final ItemTypeNamesCache cache = givenItemTypeNamesCache();

		final String typeName = cache.getItemTypeName(MockModel.class);

		assertThat(typeName).isNotNull().isEqualTo("MockType");
	}

	@Test
	public void shouldCacheItemTypeNameForGivenClass()
	{
		final ItemTypeNamesCache cache = givenItemTypeNamesCache();

		final String firstCallItemTypeName = cache.getItemTypeName(MockModel.class);
		MockModel._TYPECODE = "Changed";
		final String secondCallItemTypeName = cache.getItemTypeName(MockModel.class);

		assertThat(firstCallItemTypeName).isNotNull().isEqualTo("MockType");
		assertThat(secondCallItemTypeName).isNotNull().isEqualTo("MockType");
	}

	@Test
	public void shouldTryToExtractTypeNameFormClassNameWhenClassDoesntContainTYPECODEField()
	{
		final ItemTypeNamesCache cache = givenItemTypeNamesCache();

		final String typeName = cache.getItemTypeName(ModelWithoutTYPECODE.class);

		assertThat(typeName).isNotNull().isEqualTo("ModelWithoutTYPECODE");
	}

	@Test
	public void shouldPreserveConventionAndRemoveTrailingModelWordFromClassNameWhenTYPECODEIsNotGiven()
	{
		final ItemTypeNamesCache cache = givenItemTypeNamesCache();

		final String typeName = cache.getItemTypeName(TestModel.class);

		assertThat(typeName).isNotNull().isEqualTo("Test");
	}

	@Test
	public void shouldFailWithIllegalStateExceptionWhenClassDeclaresTYPECODEFieldWhichIsNotAccessible()
	{
		final ItemTypeNamesCache cache = givenItemTypeNamesCache();

		try
		{
			cache.getItemTypeName(ModelWithPrivateTYPECODE.class);
		}
		catch (final IllegalStateException e)
		{
			assertThat(e.getCause()).isNotNull().isInstanceOf(IllegalAccessException.class);
			assertThat(e.getMessage()).contains("_TYPECODE");
			return;
		}

		fail("IllegalStateException was expected");
	}

	private ItemTypeNamesCache givenItemTypeNamesCache()
	{
		return new ItemTypeNamesCache();
	}
}

class TestModel extends AbstractItemModel
{
	// doesn't declare _TYPECODE
}

class ModelWithoutTYPECODE extends AbstractItemModel
{
	// doesn't declare _TYPECODE
}

class ModelWithPrivateTYPECODE extends AbstractItemModel
{
	@SuppressWarnings("unused")
	private static String _TYPECODE = "Private";
}

class MockModel extends AbstractItemModel
{
	public static String _TYPECODE = "MockType";
}
