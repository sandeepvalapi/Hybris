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
package de.hybris.platform.category.interceptors;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CategoryRemovalValidatorIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;
	@Resource
	CategoryService categoryService;
	@Resource
	CategoryDao categoryDao;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createHardwareCatalog();
	}

	@Test
	public void shouldThrowInterceptorExceptionWhenCategoryHasSubcategories()
	{
		// given
		final Collection<CategoryModel> categories = categoryDao.findCategoriesByCode("HW1000");
		final CategoryModel category = categories.iterator().next();

		try
		{
			// when
			modelService.remove(category);
			fail("should throw InterceptorException");
		}
		catch (final ModelRemovalException e)
		{
			// then
			assertThat(e.getMessage()).contains("since this category still has sub-categories");
			assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
		}
	}

	@Test
	public void shouldNotThrowInterceptorExceptionWhenCategoryIsLeaf()
	{
		// given
		final Collection<CategoryModel> categories = categoryDao.findCategoriesByCode("HW1240");
		final CategoryModel category = categories.iterator().next();

		try
		{
			// when
			modelService.remove(category);
		}
		catch (final ModelSavingException e)
		{
			// then
			fail("should NOT throw InterceptorException");
		}
	}

	@Test
	public void shouldNotThrowInterceptorExceptionWhenCategoryHasSubcategoriesButSessionKeyIsTrue()
	{
		// given
		categoryService.disableSubcategoryRemovalCheck();
		final Collection<CategoryModel> categories = categoryDao.findCategoriesByCode("HW1200");
		final CategoryModel category = categories.iterator().next();

		try
		{
			// when
			modelService.remove(category);
		}
		catch (final ModelSavingException e)
		{
			// then
			fail("should NOT throw InterceptorException");
		}
	}
}
