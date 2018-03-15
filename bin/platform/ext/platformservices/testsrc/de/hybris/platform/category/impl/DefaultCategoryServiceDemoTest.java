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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for the {@link DefaultCategoryService}.
 */
@DemoTest
public class DefaultCategoryServiceDemoTest extends ServicelayerTransactionalTest
{

	@Resource
	private CategoryService categoryService;
	@Resource
	private ProductService productService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private UserService userService;
	@Resource
	private SessionService sessionService; //NOPMD
	@Resource
	private ModelService modelService;

	private CategoryModel category0;
	private CategoryModel category2;
	private ClassificationClassModel clClass;
	private CatalogVersionModel catVersion;
	private ClassificationSystemVersionModel clSystemVersion;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		createHardwareCatalog();
	}


	@Test
	public void shouldGetCategoryPathForProduct()
	{
		// given
		final ProductModel product = productService.getProductForCode("HW2300-2356");

		// when
		final List<CategoryModel> categoryPathForProduct = categoryService.getCategoryPathForProduct(product);

		// then
		assertThat(categoryPathForProduct).isNotEmpty();
		assertThat(categoryPathForProduct).hasSize(2);
	}

	@Test
	public void shouldGetCategoryPathForProductWithCategoryClassOnly()
	{
		// given
		final ProductModel product = productService.getProductForCode("HW2300-2356");

		// when
		final List<CategoryModel> categoryPathForProduct = categoryService.getCategoryPathForProduct(product, CategoryModel.class);

		// then
		assertThat(categoryPathForProduct).isNotEmpty();
		assertThat(categoryPathForProduct).hasSize(2);
	}

	/**
	 * Tests the categoryService.getCategories(catalogVersion, product)
	 * <ul>
	 * <li>gets the categories that belong to hwcatalog.online and contain the product with code "HW2300-4121",</li>
	 * <li>gets the codes of the result, and checks whether they are expected.</li>
	 * </ul>
	 */
	@Test
	public void testGetCategories()
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online");
		final ProductModel product = productService.getProductForCode(catalogVersion, "HW2300-4121");
		final Collection<CategoryModel> categories = product.getSupercategories();
		final Set<String> expectedCategories = new HashSet<String>();
		expectedCategories.add("HW2300");
		expectedCategories.add("topseller");
		final Set<String> categoryCodes = new HashSet<String>();
		for (final CategoryModel category : categories)
		{
			categoryCodes.add(category.getCode());
		}
		//compare two sets which contain the category codes
		assertEquals(expectedCategories.size(), categoryCodes.size());
		assertTrue(expectedCategories.containsAll(categoryCodes));
		assertTrue(categoryCodes.containsAll(expectedCategories));
	}

	/**
	 * Checks that the category with code "testCategory0" exists.
	 */
	@Test
	public void testGetCategoryForCode() throws Exception
	{
		final String code = "testCategory0";
		final CategoryModel category = categoryService.getCategoryForCode(code);
		assertNotNull("Category", category);
		assertEquals("Category code", code, category.getCode());
	}

	/**
	 * Checks that the category with code "gibtesnicht" does NOT exist, and null is returned
	 */
	@SuppressWarnings("deprecated")
	@Test
	public void testGetNonExistingCategory() throws Exception
	{
		final String code = "gibtesnicht";
		try
		{
			categoryService.getCategoryForCode(code);
			fail("should throw UnknownIdentifierException");
		}
		catch (final UnknownIdentifierException e)
		{
			assertThat(e.getMessage()).contains("Category with code 'gibtesnicht' not found!");
		}
	}

	/**
	 * Checks that the category with code "gibtesnicht" does NOT exist, and UnknownIdentifierException is thrown.
	 */
	@Test
	public void testGetNonExistingCategoryForCode() throws Exception
	{
		final String code = "gibtesnicht";
		try
		{
			categoryService.getCategoryForCode(code);
			fail("should not find the category");
		}
		catch (final UnknownIdentifierException ue)
		{
			//expected
		}
	}

	/**
	 * Checks all paths of the specific category.
	 * <ul>
	 * <li>tests the category "HW1240" has two paths, and the detail of them,</li>
	 * <li>tests the category "HW2320" has two paths, and the detail of them.</li>
	 * </ul>
	 */
	@Test
	public void testGetPaths()
	{
		final List<List<String>> categoryCodes = new ArrayList<List<String>>();
		categoryCodes.add(Arrays.asList("HW1000", "HW1200", "HW1240"));
		categoryCodes.add(Arrays.asList("electronics", "hardware", "photography", "HW1200", "HW1240"));
		testGetPaths("HW1240", categoryCodes);
		categoryCodes.clear();
		categoryCodes.add(Arrays.asList("HW2000", "HW2300", "HW2320"));
		categoryCodes.add(Arrays.asList("electronics", "hardware", "graphics", "HW2300", "HW2320"));
		testGetPaths("HW2320", categoryCodes);
	}

	/**
	 * Tests whether the category is empty.
	 * <ul>
	 * <li>checks the category "HW2320" is not empty,</li>
	 * <li>creates a new category "flexible_category", and checks that is empty,</li>
	 * <li>creates another category "empty_category" and assign the "flexible_category" as its super-category,</li>
	 * <li>checks that the "flexible_category" is not empty now,</li>
	 * <li>removes the "empty_category", and checks that the "flexible_category" is empty again,</li>
	 * <li>creates a product and assign it to the "flexible_category",</li>
	 * <li>checks that the "flexible_category" is not empty,</li>
	 * <li>removes the product, and checks that the category "flexible_category" is empty again.</li>
	 * </ul>
	 */
	@Test
	public void testIsEmpty()
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online");
		final CategoryModel category = categoryService.getCategoryForCode(catalogVersion, "HW2320");
		assertFalse("should not be empty", categoryService.isEmpty(category));

		final CategoryModel flexibleCategory = modelService.create(CategoryModel.class);
		flexibleCategory.setCode("flexible_category");
		flexibleCategory.setSupercategories(Collections.singletonList(category));
		flexibleCategory.setCatalogVersion(catalogVersion);
		modelService.save(flexibleCategory);
		assertTrue("should be empty", categoryService.isEmpty(flexibleCategory));

		final CategoryModel emptyCategory = modelService.create(CategoryModel.class);
		emptyCategory.setCode("empty_category");
		emptyCategory.setSupercategories(Collections.singletonList(flexibleCategory));
		emptyCategory.setCatalogVersion(catalogVersion);
		modelService.save(emptyCategory);
		modelService.refresh(flexibleCategory);
		assertFalse("should not be empty now", categoryService.isEmpty(flexibleCategory));

		modelService.remove(emptyCategory);
		modelService.refresh(flexibleCategory);
		assertTrue("should be empty again", categoryService.isEmpty(flexibleCategory));

		final ProductModel product = productService.getProductForCode(catalogVersion, "HW2300-4121");
		flexibleCategory.setProducts(Collections.singletonList(product));
		modelService.save(flexibleCategory);
		assertFalse("should not be empty again", categoryService.isEmpty(flexibleCategory));
	}

	/**
	 * Tests whether the category is a root category.
	 * <ul>
	 * <li>checks the category "HW1000" is a root category,</li>
	 * <li>checks the category "HW1100" is not a root category.</li>
	 * </ul>
	 */
	@Test
	public void testIsRoot()
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online");
		final CategoryModel category1 = categoryService.getCategoryForCode(catalogVersion, "HW1000");
		final CategoryModel category2 = categoryService.getCategoryForCode(catalogVersion, "HW1100");
		assertTrue(categoryService.isRoot(category1));
		assertFalse(categoryService.isRoot(category2));
	}

	/**
	 * Tests the categoryService.getRootCategories(catalogVersion).
	 * <ul>
	 * <li>gets the root categories of testCatalog.Online, and tests that they don't have super categories,</li>
	 * <li>gets the root categories of a catalog version, and tests the result,</li>
	 * <li>gets the root categories of a classification system version, and tests the result.</li>
	 * </ul>
	 */
	@Test
	public void testRootCategories() throws Exception
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Collection<CategoryModel> categories = categoryService.getRootCategoriesForCatalogVersion(catalogVersion);
		assertNotNull("Categories", categories);
		assertFalse("Categories empty", categories.isEmpty());
		for (final CategoryModel cat : categories)
		{
			assertTrue("Root category must not have super category", cat.getSupercategories().isEmpty());
		}

		// see PLA-7609
		prepareDataForRootCategories();

		assertEquals(
				"catalog version", //
				new HashSet<CategoryModel>(Arrays.asList(category0, category2)),
				new HashSet<CategoryModel>(categoryService.getRootCategoriesForCatalogVersion(catVersion)));

		assertEquals(
				"classification system version", //
				new HashSet<CategoryModel>(Arrays.asList(clClass)),
				new HashSet<CategoryModel>(categoryService.getRootCategoriesForCatalogVersion(clSystemVersion)));
	}

	@Test
	public void shouldSetAllowedPrincipalsForCategoryAndRecursivelyToAllSubcategoriesAndSupercategories()
	{
		// given
		final CategoryModel category = categoryService.getCategoryForCode("HW2300");
		final PrincipalModel principal = userService.getAdminUser();

		// when
		categoryService.setAllowedPrincipalsForAllRelatedCategories(category, Collections.singletonList(principal));

		// then
		assertThat(category.getAllowedPrincipals()).containsOnly(principal);
		for (final CategoryModel subCategory : category.getAllSubcategories())
		{
			assertThat(subCategory.getAllowedPrincipals()).containsOnly(principal);
		}
		for (final CategoryModel superCategory : category.getAllSupercategories())
		{
			assertThat(superCategory.getAllowedPrincipals()).contains(principal);
		}
	}

	@Test
	public void shouldSetAllowedPrincipalsOnlyForPassedCategory()
	{
		// given
		final CategoryModel category = categoryService.getCategoryForCode("HW2300");
		final PrincipalModel principal = userService.getAdminUser();

		// when
		categoryService.setAllowedPrincipalsForCategory(category, Collections.singletonList(principal));

		// then
		assertThat(category.getAllowedPrincipals()).containsOnly(principal);
		for (final CategoryModel subCategory : category.getAllSubcategories())
		{
			assertThat(subCategory.getAllowedPrincipals()).hasSize(1);
			assertThat(principal).isNotIn(subCategory.getAllowedPrincipals());
		}
		for (final CategoryModel superCategory : category.getAllSupercategories())
		{
			assertThat(principal).isNotIn(superCategory.getAllowedPrincipals());
		}
	}

	/**
	 * Tests to add principals for unsaved category without exception.
	 */
	@Test
	public void testSetAllowedPrincipalsWitNotSavedModels()
	{
		final CategoryModel unsavedCategory = modelService.create(CategoryModel.class);
		unsavedCategory.setCatalogVersion(catVersion);
		unsavedCategory.setCode("unsavedCategory");
		unsavedCategory.setSupercategories(Collections.EMPTY_LIST);
		unsavedCategory.setCategories(Collections.EMPTY_LIST);
		categoryService.setAllowedPrincipalsForCategory(unsavedCategory, Collections.EMPTY_LIST);
	}

	@Test
	public void shouldReturnCategoryPathForProductWithRootAsFirstElementOfList()
	{
		// given
		//		final ProductModel product = productService.getProductForCode("HW2300-2356");

		// when
		//		final List<CategoryModel> categoryPathForProduct = categoryService.getCategoryPathForProduct(product);

		// then
		//		assertThat(categoryPathForProduct).isNotEmpty();
	}

	private void prepareDataForRootCategories()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("foo");
		modelService.save(catalog);

		catVersion = modelService.create(CatalogVersionModel.class);
		catVersion.setCatalog(catalog);
		catVersion.setVersion("bar");
		modelService.save(catVersion);

		final ClassificationSystemModel clSystem = modelService.create(ClassificationSystemModel.class);
		clSystem.setId("clSystem");
		modelService.save(clSystem);

		clSystemVersion = modelService.create(ClassificationSystemVersionModel.class);
		clSystemVersion.setCatalog(clSystem);
		clSystemVersion.setVersion("clVersion");
		clSystemVersion.setLanguages(Collections.EMPTY_LIST);
		modelService.save(clSystemVersion);

		//category structure:
		// clCl
		//   +-> c0
		//        +-> c1
		// c2
		clClass = modelService.create(ClassificationClassModel.class);
		clClass.setCode("clCl");
		clClass.setCatalogVersion(clSystemVersion);
		modelService.save(clClass);

		category0 = modelService.create(CategoryModel.class);
		category0.setCode("c0");
		category0.setCatalogVersion(catVersion);
		category0.setSupercategories(Collections.singletonList((CategoryModel) clClass));
		modelService.save(category0);

		final CategoryModel category1 = modelService.create(CategoryModel.class);
		category1.setCode("c1");
		category1.setCatalogVersion(catVersion);
		category1.setSupercategories(Collections.singletonList(category0));
		modelService.save(category1);

		category2 = modelService.create(CategoryModel.class);
		category2.setCode("c2");
		category2.setCatalogVersion(catVersion);
		modelService.save(category2);
	}

	private void testGetPaths(final String categoryCode, final List<List<String>> expectedCategoryCodes)
	{
		final CategoryModel category = categoryService.getCategoryForCode(categoryCode);
		final Collection<List<CategoryModel>> paths = categoryService.getPathsForCategory(category);
		final List<List<String>> categoryCodes = new ArrayList<List<String>>();
		for (final List<CategoryModel> path : paths)
		{
			final List<String> codes = new ArrayList<String>();
			for (final CategoryModel node : path)
			{
				codes.add(node.getCode());
			}
			categoryCodes.add(codes);
		}

		assertEquals(categoryCodes.size(), expectedCategoryCodes.size());

		final Set<Integer> consumedCodes = new HashSet<Integer>();
		for (final List<String> codes : categoryCodes)
		{
			boolean found = false;
			for (int i = 0; i < expectedCategoryCodes.size(); i++)
			{
				final List<String> expectedCodes = expectedCategoryCodes.get(i);

				for (int j = 0; j < codes.size(); j++)
				{
					if (expectedCodes.get(j).equals(codes.get(j)))
					{
						if (j == codes.size() - 1)
						{
							found = true;
						}
					}
					else
					{
						break;
					}
				}
				if (found)
				{
					consumedCodes.add(Integer.valueOf(i));
					break;
				}
			}
			assertTrue(found);
		}
		assertEquals(consumedCodes.size(), expectedCategoryCodes.size());
	}

}
