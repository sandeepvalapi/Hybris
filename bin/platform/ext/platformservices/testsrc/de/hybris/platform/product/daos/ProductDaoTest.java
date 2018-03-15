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
package de.hybris.platform.product.daos;

import static java.lang.String.format;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.variants.jalo.VariantType;
import de.hybris.platform.variants.jalo.VariantsManager;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ProductDaoTest extends ServicelayerTest
{
	@Resource
	private ProductDao productDao;
	@Resource
	private CategoryService categoryService;
	@Resource
	private VariantsService variantsService;
	@Resource
	private ModelService modelService;
	@Resource
	private TimeService timeService;
	@Resource
	protected CatalogVersionService catalogVersionService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
	}

	@After
	public void tearDown()
	{
		timeService.resetTimeOffset();
	}

	@Test
	public void testFindProduct() throws Exception
	{
		final List<ProductModel> products = productDao.findProductsByCode("testProduct0");
		assertThat(products).isNotNull();
		assertThat(products).isNotEmpty();
		assertEquals("Code", "testProduct0", products.get(0).getCode());
	}

	@Test
	public void testFindProductWhenCodeIsNull() throws Exception
	{
		//when
		try
		{
			productDao.findProductsByCode(null);
			Assert.fail("Should throw IllegalArgumentException when code is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
	}

	@Test
	public void testFindProductWhenCodeIsUnknown() throws Exception
	{
		//when
		final List<ProductModel> products = productDao.findProductsByCode("unknown");

		//then
		assertThat(products).isNotNull();
		assertThat(products).isEmpty();
	}

	@Test
	public void testGetProductsByCategory() throws Exception
	{
		final String categoryCode = "testCategory0";
		final CategoryModel cat = categoryService.getCategoryForCode(categoryCode);
		assertThat(cat).isNotNull();
		final SearchResult<ProductModel> searchResult = productDao.findProductsByCategory(cat, 0, -1);
		assertThat(searchResult).isNotNull();
		final List<ProductModel> products = searchResult.getResult();
		assertThat(products).isNotNull();
		assertFalse("No products found", products.isEmpty());
		for (final ProductModel product : products)
		{
			final Collection<CategoryModel> categories = product.getSupercategories();
			boolean found = false;
			for (final CategoryModel c : categories)
			{
				if (treeContainsCategoryWithCode(c, categoryCode))
				{
					found = true;
					break;
				}
			}
			assertTrue(format("Product %s not in category %s", product.getCode(), categoryCode), found);
		}

	}

	@Test
	public void testGetProductsWithStatus() throws Exception
	{
		createHardwareCatalog();
		final String testCode = "HW2320-1008";
		final CategoryModel category = categoryService.getCategoryForCode("HW2320");
		assertThat(category).isNotNull();
		assertThat(category.getCode()).isEqualTo("HW2320");
		//round one
		List<ProductModel> onlineProducts = productDao.findOnlineProductsByCategory(category);
		assertThat(onlineProducts).isNotNull();

		List<ProductModel> offlineProducts = productDao.findOfflineProductsByCategory(category);
		assertThat(offlineProducts).isNotNull();


		final Set<String> expectedCodes = new HashSet<String>();
		expectedCodes.add(testCode);
		expectedCodes.add("HW2320-1009");
		expectedCodes.add("HW2320-1010");
		expectedCodes.add("HW2320-1011");

		final Set<String> productCodes = new HashSet<String>();
		ProductModel offlineProduct = null;
		for (final ProductModel product : onlineProducts)
		{
			productCodes.add(product.getCode());
			//set the HW2320-1008 as offline product
			if (testCode.equals(product.getCode()))
			{
				final Date currentDate = new Date();
				product.setOnlineDate(new Date(currentDate.getTime() + (1 * 24 * 60 * 60 * 1000))); // + 1 days
				product.setOfflineDate(new Date(currentDate.getTime() + (4 * 24 * 60 * 60 * 1000))); // + 4 days
				modelService.save(product);

				offlineProduct = product;
				System.out.println("online date = " + offlineProduct.getOnlineDate().getTime());
				System.out.println("offline date = " + offlineProduct.getOfflineDate().getTime());
			}
		}
		assertEquals("different online size", expectedCodes.size(), productCodes.size());
		assertTrue(expectedCodes.containsAll(productCodes));
		assertTrue(productCodes.containsAll(expectedCodes));
		assertEquals("different offline size", 0, offlineProducts.size());

		//round two
		onlineProducts = productDao.findOnlineProductsByCategory(category);
		offlineProducts = productDao.findOfflineProductsByCategory(category);
		expectedCodes.remove(testCode);
		productCodes.clear();
		for (final ProductModel product : onlineProducts)
		{
			productCodes.add(product.getCode());
		}
		assertEquals("different online size", expectedCodes.size(), productCodes.size());
		assertTrue(expectedCodes.containsAll(productCodes));
		assertTrue(productCodes.containsAll(expectedCodes));
		assertEquals("different offline size", 1, offlineProducts.size());
		assertEquals("different code", testCode, offlineProducts.iterator().next().getCode());

		// round three: doing time-shift with TimeService 
		timeService.setCurrentTime(new Date(offlineProduct.getOnlineDate().getTime() + (1 * 24 * 60 * 60 * 1000))); //  + 1 after online date

		onlineProducts = productDao.findOnlineProductsByCategory(category);
		offlineProducts = productDao.findOfflineProductsByCategory(category);
		expectedCodes.add(testCode);
		productCodes.clear();
		for (final ProductModel product : onlineProducts)
		{
			productCodes.add(product.getCode());
		}
		assertEquals(Collections.EMPTY_LIST, offlineProducts);
		assertEquals("different online size", expectedCodes.size(), productCodes.size());
		assertTrue(expectedCodes.containsAll(productCodes));
		assertTrue(productCodes.containsAll(expectedCodes));

		// round four: doing time-shift with TimeService, but *after* offline date
		timeService.setCurrentTime(new Date(offlineProduct.getOfflineDate().getTime() + (1 * 24 * 60 * 60 * 1000)));

		onlineProducts = productDao.findOnlineProductsByCategory(category);
		offlineProducts = productDao.findOfflineProductsByCategory(category);
		expectedCodes.remove(testCode);
		productCodes.clear();
		for (final ProductModel product : onlineProducts)
		{
			productCodes.add(product.getCode());
		}
		assertEquals(1, offlineProducts.size());
		assertEquals(testCode, offlineProducts.get(0).getCode());
		assertEquals("different online size", expectedCodes.size(), productCodes.size());
		assertTrue(expectedCodes.containsAll(productCodes));
		assertTrue(productCodes.containsAll(expectedCodes));
	}

	@Test
	public void testGetProductsByCategoryWhenCategoryIsNull() throws Exception
	{
		//when
		try
		{
			productDao.findProductsByCategory(null, 0, -1);
			Assert.fail("Should throw IllegalArgumentException when category is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
	}

	@Test
	public void testGetProductsByCategoryWithLimit() throws Exception
	{
		final String categoryCode = "testCategory0";
		final CategoryModel cat = categoryService.getCategoryForCode(categoryCode);
		assertThat(cat).isNotNull();
		final SearchResult<ProductModel> result = productDao.findProductsByCategory(cat, 0, 1);
		assertThat(result).isNotNull();
		assertEquals("Result count", 1, result.getCount());
		assertEquals("Result start", 0, result.getRequestedStart());
	}

	@Test
	public void testVariants() throws JaloDuplicateQualifierException, JaloItemNotFoundException
	{
		final VariantType variantType = VariantsManager.getInstance().createVariantType("VTTest");
		variantType.createVariantAttributeDescriptor("color", TypeManager.getInstance().getType("java.lang.String"),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.OPTIONAL_FLAG
						| AttributeDescriptor.PROPERTY_FLAG);
		variantType.createVariantAttributeDescriptor("size", TypeManager.getInstance().getType("java.lang.String"),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.OPTIONAL_FLAG
						| AttributeDescriptor.PROPERTY_FLAG);
		variantType.createVariantAttributeDescriptor("ok", TypeManager.getInstance().getType("java.lang.Boolean"),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.OPTIONAL_FLAG
						| AttributeDescriptor.PROPERTY_FLAG);

		assertEquals(new HashSet<String>(Arrays.asList("color", "size", "ok")),
				variantsService.getVariantAttributes(variantType.getCode()));

		final ProductModel base = productDao.findProductsByCode("testProduct0").get(0);
		base.setVariantType((VariantTypeModel) modelService.get(variantType));

		modelService.save(base);

		assertEquals(Collections.EMPTY_LIST, base.getVariants());

		final VariantProductModel var1 = modelService.create(variantType.getCode());
		var1.setCode("var1");
		var1.setBaseProduct(base);
		var1.setCatalogVersion(base.getCatalogVersion());

		final VariantProductModel var2 = modelService.create(variantType.getCode());
		var2.setCode("var2");
		var2.setBaseProduct(base);
		var2.setCatalogVersion(base.getCatalogVersion());

		modelService.saveAll(Arrays.asList(var1, var2));

		assertEquals(variantType.getCode(), var1.getItemtype());
		assertEquals(variantType.getCode(), var2.getItemtype());

		assertEquals(Arrays.asList(var1, var2), base.getVariants());

		modelService.refresh(base);

		assertEquals(Arrays.asList(var1, var2), base.getVariants());

		assertNull(variantsService.getVariantAttributeValue(var1, "color"));
		assertNull(variantsService.getVariantAttributeValue(var1, "size"));
		assertNull(variantsService.getVariantAttributeValue(var1, "ok"));

		assertNull(variantsService.getVariantAttributeValue(var2, "color"));
		assertNull(variantsService.getVariantAttributeValue(var2, "size"));
		assertNull(variantsService.getVariantAttributeValue(var2, "ok"));

		variantsService.setVariantAttributeValue(var1, "color", "red");
		variantsService.setVariantAttributeValue(var1, "ok", Boolean.TRUE);
		variantsService.setVariantAttributeValue(var2, "size", "33");

		assertEquals("red", variantsService.getVariantAttributeValue(var1, "color"));
		assertNull(variantsService.getVariantAttributeValue(var1, "size"));
		assertEquals(Boolean.TRUE, variantsService.getVariantAttributeValue(var1, "ok"));

		assertNull(variantsService.getVariantAttributeValue(var2, "color"));
		assertEquals("33", variantsService.getVariantAttributeValue(var2, "size"));
		assertNull(variantsService.getVariantAttributeValue(var2, "ok"));
	}

	private boolean treeContainsCategoryWithCode(final CategoryModel categoryModel, final String categoryCode)
	{
		if (categoryCode.equals(categoryModel.getCode()))
		{
			return true;
		}
		final Collection<CategoryModel> supercategories = categoryModel.getSupercategories();
		for (final CategoryModel cat : supercategories)
		{
			if (treeContainsCategoryWithCode(cat, categoryCode))
			{
				return true;
			}
		}
		return false;
	}

	@Test
	public void testGetProductFromCatalogVersionWhenNoActiveCatalogVersionIsSet() throws Exception
	{
		catalogVersionService.setSessionCatalogVersions(Collections.EMPTY_LIST);
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		Collection<CatalogVersionModel> cvs = catalogVersionService.getSessionCatalogVersions();
		assertFalse(cvs.contains(catalogVersion));
		assertNotNull(productDao.findProductsByCode(catalogVersion, "testProduct0"));
		cvs = catalogVersionService.getSessionCatalogVersions();
		assertFalse(cvs.contains(catalogVersion));
	}

	@Test
	public void testFindProductWhenCatalogVersionIsNull() throws Exception
	{
		//when
		try
		{
			productDao.findProductsByCode(null, "testProduct0");
			Assert.fail("Should throw IllegalArgumentException when catalog version is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
	}

	@Test
	public void testFindProductWhenCodeIsNullAndCatalogVersionNotNull() throws Exception
	{
		//when
		try
		{
			final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
			productDao.findProductsByCode(catalogVersion, null);
			Assert.fail("Should throw IllegalArgumentException when code is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
	}

	@Test
	public void testFindProductWhenThereIsNoResults() throws Exception
	{
		//when
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final List<ProductModel> products = productDao.findProductsByCode(catalogVersion, "unknown");

		//then
		assertThat(products).isNotNull();
		Assert.assertTrue(products.isEmpty());
	}

	@Test
	public void testFindAllProductsCount() throws Exception
	{
		createHardwareCatalog();
		final String code = "HW1000";
		final CategoryModel category = categoryService.getCategoryForCode(code);
		assertThat(category).isNotNull();
		final Integer count = productDao.findAllProductsCountByCategory(category);
		assertEquals("should be 12 products", 12, count.intValue());
	}

	@Test
	public void testFindAllProductsCountByEmptyCategory()
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final CategoryModel empty = modelService.create(CategoryModel.class);
		empty.setCode("empty");
		empty.setCatalogVersion(catalogVersion);
		modelService.save(empty);

		final Integer count = productDao.findAllProductsCountByCategory(empty);
		assertEquals("should be 0 products", 0, count.intValue());
	}

	@Test
	public void testFindProductsCountForOneCategory()
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final CategoryModel bottomCat = modelService.create(CategoryModel.class);
		bottomCat.setCode("bottomCategory");
		bottomCat.setCatalogVersion(catalogVersion);

		modelService.save(bottomCat);

		Integer count = productDao.findProductsCountByCategory(bottomCat);
		assertThat(count).isEqualTo(0);

		final List<ProductModel> products = productDao.findProductsByCode("testProduct0");
		assertThat(products).isNotEmpty();

		bottomCat.setProducts(products);
		modelService.saveAll();

		count = productDao.findProductsCountByCategory(bottomCat);
		assertThat(count).isEqualTo(products.size());
	}

	@Test
	public void testFindProductsFromCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final List<ProductModel> allProducts = productDao.findProductsByCatalogVersion(catalogVersion);
		assertThat(allProducts).overridingErrorMessage("should be 10 products for a catalog version").hasSize(10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindProductsFromCatalogVersionWhenNull()
	{
		productDao.findProductsByCatalogVersion(null);
	}

}
