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
package de.hybris.platform.category.attribute;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.CategoryTestUtils;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CategoryAllSupercategoriesTest extends ServicelayerTransactionalTest
{

	@Resource
	private CategoryAllSupercategories categoryAllSupercategories;
	@Resource
	private CategoryDao categoryDao;
	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		//		createHardwareCatalog();
		createDefaultCatalog();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.category.attribute.CategoryAllSupercategories#get(de.hybris.platform.category.model.CategoryModel)}
	 * .
	 */
	@Test
	public void shouldReturnEmptyCollectionWhereThereIsNoSupercategoriesForCategory()
	{
		// given
		final Collection<CategoryModel> categories = categoryDao.findCategoriesByCode("testCategory0");
		final CategoryModel category = categories.iterator().next();

		// when
		final Collection<CategoryModel> superCategoriesFromDynamicHandler = categoryAllSupercategories.get(category);

		// then
		assertThat(superCategoriesFromDynamicHandler).isEmpty();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.category.attribute.CategoryAllSupercategories#get(de.hybris.platform.category.model.CategoryModel)}
	 * .
	 */
	@Test
	public void shouldReturnAllSubcategoriesForCategory()
	{
		// given
		final Collection<CategoryModel> categories = categoryDao.findCategoriesByCode("testCategory3");
		final CategoryModel category = categories.iterator().next();
		final Category source = modelService.getSource(category);

		// when
		final Collection<CategoryModel> superCategoriesFromDynamicHandler = categoryAllSupercategories.get(category);
		final Collection<Category> superCategoriesFromItem = source.getAllSupercategories();

		// then
		assertThat(superCategoriesFromDynamicHandler).isNotEmpty();
		assertThat(superCategoriesFromItem).isNotEmpty();
		assertThat(superCategoriesFromDynamicHandler.size()).isEqualTo(superCategoriesFromItem.size());
		assertThat(CategoryTestUtils.convertNewCollectionToPk(superCategoriesFromDynamicHandler)).isEqualTo(
				CategoryTestUtils.convertOldCollectionToPk(superCategoriesFromItem));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.category.attribute.CategoryAllSupercategories#get(de.hybris.platform.category.model.CategoryModel)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenCategoryIsNull()
	{
		// given
		final CategoryModel category = null;

		try
		{
			// when
			categoryAllSupercategories.get(category);
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Category is required to perform this operation, null given");
		}
	}

}
