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
package de.hybris.platform.category.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.internal.model.attribute.DynamicAttributesProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


/**
 * Mock tests for the {@link DefaultCategoryService}.
 */
@UnitTest
public class DefaultCategoryServiceTest
{
	private static final String MOCK_CATEGORY = "mock_category";
	@InjectMocks
	private final DefaultCategoryService categoryService = new DefaultCategoryService();
	@Mock
	private CategoryDao categoryDao;
	@Mock
	private DynamicAttributesProvider dynamicAttributesProvider;
	@Mock
	private SessionService sessionService;
	@Mock
	private CategoryModel categoryMock;
	@Mock
	private CategoryModel categoryMock1a;
	@Mock
	private CategoryModel categoryMock2a;
	@Mock
	private CategoryModel categoryMock1b;
	@Mock
	private CategoryModel categoryMock2b;
	@Mock
	private CategoryModel categoryMock3b;
	@Mock
	private CategoryModel categoryMock1c;
	@Mock
	private CategoryModel categoryMock2c;
	@Mock
	private CategoryModel categoryMock1d;
	@Mock
	private CategoryModel categoryMock2d;



	private final CategoryModel category = new CategoryModel();



	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		category.setCode(MOCK_CATEGORY);
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(category))
				.setDynamicAttributesProvider(dynamicAttributesProvider);
	}


	@Test
	public void shouldThrowIllegalArgumentExceptionWhenGettingAllSubcategoriesAndCategoryIsNull()
	{
		// given
		final CategoryModel category = null;

		try
		{
			// when
			categoryService.getAllSubcategoriesForCategory(category);
			fail("Should throw illegal argument exception");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Category is required to perform this operation, null given");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenGettingAllSupercategoriesAndCategoryIsNull()
	{
		// given
		final CategoryModel category = null;

		try
		{
			// when
			categoryService.getAllSupercategoriesForCategory(category);
			fail("Should throw illegal argument exception");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Category is required to perform this operation, null given");
		}
	}

	@Test
	public void shouldGetAllSubcategoriesForCategoryRecursively()
	{
		// given
		given(categoryMock.getCategories()).willReturn(Lists.newArrayList(categoryMock1a, categoryMock2a));
		given(categoryMock1a.getCategories()).willReturn(Lists.newArrayList(categoryMock1b, categoryMock2b, categoryMock3b));
		given(categoryMock2b.getCategories()).willReturn(Lists.newArrayList(categoryMock1c, categoryMock2c));
		given(categoryMock2c.getCategories()).willReturn(Lists.newArrayList(categoryMock1d, categoryMock2d));

		// when
		final Collection<CategoryModel> result = categoryService.getAllSubcategoriesForCategory(categoryMock);

		// then
		assertThat(result).hasSize(9);
		assertThat(result).containsOnly(categoryMock1a, categoryMock2a, categoryMock1b, categoryMock2b, categoryMock3b,
				categoryMock1c, categoryMock2c, categoryMock1d, categoryMock2d);
	}

	@Test
	public void shouldGetAllSupercategoriesForCategoryRecursively()
	{
		// given
		given(categoryMock.getSupercategories()).willReturn(Lists.newArrayList(categoryMock1a, categoryMock2a));
		given(categoryMock1a.getSupercategories()).willReturn(Lists.newArrayList(categoryMock1b, categoryMock2b, categoryMock3b));
		given(categoryMock2b.getSupercategories()).willReturn(Lists.newArrayList(categoryMock1c, categoryMock2c));
		given(categoryMock2c.getSupercategories()).willReturn(Lists.newArrayList(categoryMock1d, categoryMock2d));

		// when
		final Collection<CategoryModel> result = categoryService.getAllSupercategoriesForCategory(categoryMock);

		// then
		assertThat(result).hasSize(9);
		assertThat(result).containsOnly(categoryMock1a, categoryMock2a, categoryMock1b, categoryMock2b, categoryMock3b,
				categoryMock1c, categoryMock2c, categoryMock1d, categoryMock2d);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetCategoryForCode()
	{
		try
		{
			categoryService.getCategoryForCode(null);
			fail("IllegalArgumentException expected when category code is null.");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		when(categoryDao.findCategoriesByCode(category.getCode())).thenReturn(Collections.singletonList(category));
		assertEquals("not the same category", category, categoryService.getCategoryForCode(category.getCode()));

		final String nothing = "nothing";
		when(categoryDao.findCategoriesByCode(nothing)).thenReturn(Collections.EMPTY_LIST);
		assertNull(categoryService.getCategory(nothing));
	}

	@Test
	public void testGetCategory()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();

		try
		{
			categoryService.getCategoryForCode(null, MOCK_CATEGORY);
			fail("IllegalArgumentException expected when catalog version is null.");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		try
		{
			categoryService.getCategoryForCode(catalogVersion, null);
			fail("IllegalArgumentException expected when category code is null.");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		when(categoryDao.findCategoriesByCode(catalogVersion, MOCK_CATEGORY)).thenReturn(Collections.singletonList(category));
		assertEquals("not the same category", category, categoryService.getCategoryForCode(catalogVersion, category.getCode()));
	}

	@Test
	public void testGetPathForCategory()
	{
		final CategoryModel root = createRoot();
		final CategoryModel subCategory = createCategories(root, 90);
		final List<CategoryModel> path = categoryService.getPathForCategory(subCategory);
		assertEquals("should be 90 elements", 90, path.size());
		assertEquals("the first one(root) is 1", "category_1", path.get(0).getCode());
		Collections.reverse(path);
		assertEquals("the last one is 90", "category_90", path.get(0).getCode());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetPath()
	{
		final CategoryModel root = createRoot();
		final CategoryModel subCategory = createCategories(root, 90);
		final List<CategoryModel> path = categoryService.getPath(subCategory);
		assertEquals("should be 90 elements", 90, path.size());
		assertEquals("the first one is 90", "category_90", path.get(0).getCode());
		Collections.reverse(path);
		assertEquals("the last one(root) is 1", "category_1", path.get(0).getCode());
	}


	@Test
	public void shouldDisableSubcategoryRemovalCheck()
	{
		// when
		categoryService.disableSubcategoryRemovalCheck();

		// then
		verify(sessionService, times(1)).setAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK, Boolean.TRUE);
	}

	@Test
	public void shouldEnableSubcategoryRemovalCheck()
	{
		// when
		categoryService.enableSubcategoryRemovalCheck();

		// then
		verify(sessionService, times(1)).removeAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK);
	}


	@Test
	public void shouldReturnTrueWhenSubcategoryRemovalCheckIsSetOnTrueInTheSession()
	{
		// given
		given(sessionService.getAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK)).willReturn(Boolean.TRUE);

		// when
		final boolean result = categoryService.isSubcategoryRemovalCheckDisabled();

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldReturnFalseWhenSubcategoryRemovalCheckIsSetOnFalseInTheSession()
	{
		// given
		given(sessionService.getAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK)).willReturn(Boolean.FALSE);

		// when
		final boolean result = categoryService.isSubcategoryRemovalCheckDisabled();

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenSubcategoryRemovalCheckIsNullInTheSession()
	{
		// given
		given(sessionService.getAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK)).willReturn(null);

		// when
		final boolean result = categoryService.isSubcategoryRemovalCheckDisabled();

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenSubcategoryRemovalCheckIsSomeWeirdValueInTheSession()
	{
		// given
		given(sessionService.getAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK)).willReturn("some weird stuff");

		// when
		final boolean result = categoryService.isSubcategoryRemovalCheckDisabled();

		// then
		assertThat(result).isFalse();
	}

	private CategoryModel createRoot()
	{
		final CategoryModel root = new CategoryModel();
		root.setCode("category_1");
		root.setSupercategories(Collections.EMPTY_LIST);
		return root;
	}

	//return the last category
	private CategoryModel createCategories(final CategoryModel root, final int levels)
	{
		CategoryModel current = root;
		for (int i = 2; i <= levels; i++)
		{
			final CategoryModel category = new CategoryModel();
			category.setCode("category_" + i);
			category.setSupercategories(Collections.singletonList(current));
			current = category;
		}
		return current;
	}

}
