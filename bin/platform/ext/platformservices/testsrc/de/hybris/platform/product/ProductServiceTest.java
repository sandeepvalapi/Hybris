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
package de.hybris.platform.product;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductServiceTest
{
	private static final String PRODUCT_CODE = "PROD-001";

	private DefaultProductService productService;

	@Mock
	private SessionService mockSessionService;
	@Mock
	private ProductDao mockProductDao;

	@Mock
	private ModelService mockModelService;

	@Mock
	private ProductModel mockProduct;
	@Mock
	private CatalogVersionModel mockCatalogVersion;

	private CategoryModel mockCategory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		productService = new DefaultProductService();
		productService.setModelService(mockModelService);
		productService.setProductDao(mockProductDao);
		productService.setSessionService(mockSessionService);
	}

	@Test
	public void testReturnsProductForAGivenCode()
	{
		//given
		when(mockProductDao.findProductsByCode(PRODUCT_CODE)).thenReturn(Collections.singletonList(mockProduct));

		//when
		final ProductModel product = productService.getProductForCode(PRODUCT_CODE);

		//then
		Assert.assertNotNull(product);
		Assert.assertEquals(product, mockProduct);
	}

	@Test
	public void testThrowsIllegalArgumentExceptionWhenCodeIsNull()
	{
		try
		{
			productService.getProductForCode(null);
			Assert.fail("Should throw IllegalArgumentException when code is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
		catch (final Exception e)
		{
			Assert.fail("Should throw NullPointerException when code is null. Got exception " + e);
		}
	}

	@Test
	public void testThrowsUnknownIdentifierExceptionIfProductWithGivenCodeWasNotFound()
	{
		//given
		when(mockProductDao.findProductsByCode(PRODUCT_CODE)).thenReturn(Collections.EMPTY_LIST);

		//when
		try
		{
			productService.getProductForCode(PRODUCT_CODE);
			Assert.fail("Should throw UnknownIdentifierException when product was not found");
		}
		catch (final UnknownIdentifierException ex)
		{
			//OK
		}
	}

	@Test
	public void testThrowsAmbiguousIdentifierExceptionIfMoreThanOneProductWasFound()
	{
		//given
		final ProductModel p1 = new ProductModel();
		p1.setCode(PRODUCT_CODE);
		final ProductModel p2 = new ProductModel();
		p2.setCode(PRODUCT_CODE);
		when(mockProductDao.findProductsByCode(PRODUCT_CODE)).thenReturn(Arrays.asList(p1, p2));

		//when
		try
		{
			productService.getProductForCode(PRODUCT_CODE);
			Assert.fail("Should throw AmbiguousIdentifierException when more than one product was found");
		}
		catch (final AmbiguousIdentifierException ex)
		{
			//OK
		}
	}

	@Test
	public void testReturnsProductsForCategory()
	{
		//given
		final ProductModel secondMockProduct = Mockito.mock(ProductModel.class);
		final SearchResult<ProductModel> mockResultset = Mockito.mock(SearchResult.class);
		Mockito.when(mockResultset.getResult()).thenReturn(Arrays.asList(mockProduct, secondMockProduct));
		mockCategory = new CategoryModel();
		Mockito.when(mockProductDao.findProductsByCategory(mockCategory, 0, -1)).thenReturn(mockResultset);

		//when
		final List<ProductModel> products = productService.getProductsForCategory(mockCategory);

		//then
		Assert.assertNotNull(products);
		assertThat(Integer.valueOf(products.size()), equalTo(Integer.valueOf(2)));
	}

	@Test
	public void testGetOnlineProductsForCategoryWhenNullAsACategory()
	{
		try
		{
			productService.getOnlineProductsForCategory(null);
			Assert.fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

	}

	@Test
	public void testGetOnlineProductsForCategory()
	{
		//given
		mockCategory = new CategoryModel();
		Mockito.when(mockProductDao.findOnlineProductsByCategory(mockCategory)).thenReturn(Collections.singletonList(mockProduct));

		//when
		final List<ProductModel> products = productService.getOnlineProductsForCategory(mockCategory);

		//then
		assertEquals("products size", 1, products.size());
	}

	@Test
	public void testGetOfflineProductsForCategory()
	{
		//given
		mockCategory = new CategoryModel();
		Mockito.when(mockProductDao.findOfflineProductsByCategory(mockCategory)).thenReturn(Collections.singletonList(mockProduct));

		//when
		final List<ProductModel> products = productService.getOfflineProductsForCategory(mockCategory);

		//then
		assertEquals("products size", 1, products.size());
	}

	@Test
	public void testGetOfflineProductsForCategoryWhenNullAsACategory()
	{
		try
		{
			productService.getOfflineProductsForCategory(null);
			Assert.fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testThrowsIllegalArgumentExceptionWhenCategoryIsNull()
	{
		try
		{
			productService.getProductsForCategory(null);
			Assert.fail("Should throw IllegalArgumentException because category is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
		catch (final Exception e)
		{
			Assert.fail("Should throw IllegalArgumentException but got " + e);
		}
	}

	@Test
	public void testThrowsIllegalArgumentExceptionWhenCategoryIsNullForPaging()
	{
		try
		{
			productService.getProductsForCategory(null, 0, 10);
			Assert.fail("Should throw IllegalArgumentException because category is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
		catch (final Exception e)
		{
			Assert.fail("Should throw IllegalArgumentException but got " + e);
		}
	}

	@Test
	public void testReturnsProductsForCatalogVersionAndCode()
	{
		//given
		Mockito.when(mockProductDao.findProductsByCode(mockCatalogVersion, PRODUCT_CODE)).thenReturn(
				Collections.singletonList(mockProduct));
		final CatalogModel mockCatalog = Mockito.mock(CatalogModel.class);
		Mockito.when(mockCatalog.getId()).thenReturn(null);
		Mockito.when(mockCatalogVersion.getCatalog()).thenReturn(mockCatalog);

		//when
		final ProductModel product = productService.getProductForCode(mockCatalogVersion, PRODUCT_CODE);

		//then
		Assert.assertNotNull(product);

	}


	@Test
	public void testGetAllProductsCount()
	{
		// given
		final CategoryModel category = new CategoryModel();
		Mockito.when(mockProductDao.findAllProductsCountByCategory(category)).thenReturn(Integer.valueOf(5));

		//when
		final int noOfProducts = productService.getAllProductsCountForCategory(category).intValue();

		//then
		assertThat(noOfProducts).isEqualTo(5);
	}

	@Test
	public void testGetAllProductsCountWhenNullAsACategory()
	{
		try
		{
			//when
			productService.getAllProductsCountForCategory(null);
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testReturnsAllProductsForCatalogVersion()
	{
		//given
		Mockito.when(mockProductDao.findProductsByCatalogVersion(mockCatalogVersion))
				.thenReturn(Collections.singletonList(mockProduct));

		//when
		final List<ProductModel> products = productService.getAllProductsForCatalogVersion(mockCatalogVersion);

		//then
		assertThat(products).hasSize(1);
	}

}
