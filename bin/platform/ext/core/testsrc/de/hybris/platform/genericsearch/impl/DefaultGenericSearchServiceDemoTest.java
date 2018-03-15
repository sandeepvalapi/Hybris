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
package de.hybris.platform.genericsearch.impl;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * GenericSearchService demo tests.
 */
@DemoTest
public class DefaultGenericSearchServiceDemoTest extends ServicelayerTransactionalBaseTest
{

	private static final String TEST_CATALOG_ID = "testCatalog";

	private static final String TEST_CATALOGVERSION_ID = "testCatalogVersion";

	@Resource
	private ModelService modelService;
	@Resource
	private GenericSearchService genericSearchService;

	private ProductModel product1, product2;

	private CatalogModel catalogModel;

	private CatalogVersionModel catalogVersionModel;


	/**
	 * 
	 * Preparing two products which will be used in tests.
	 * <table border="1">
	 * <tr>
	 * <td><b>Product:</b></td>
	 * <td><b>product1</b></td>
	 * <td><b>product2</b></td>
	 * </tr>
	 * <tr>
	 * <td><b>Code:</b></td>
	 * <td>product1</td>
	 * <td>product2</td>
	 * </tr>
	 * <td><b>Ean:</b></td>
	 * <td>test</td>
	 * <td>ean</td></tr>
	 * <td><b>ManufacturerName:</b></td>
	 * <td colspan="2">testManufacturer</td> </tr>
	 * <table/>
	 * 
	 */
	@Before
	public void setUp()
	{
		// creating catalog model (required by catalogVersion)
		catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId(TEST_CATALOG_ID);
		modelService.save(catalogModel);
		// creating catalog version model (required by product)
		catalogVersionModel = modelService.create(CatalogVersionModel.class);
		catalogVersionModel.setCatalog(catalogModel);
		catalogVersionModel.setVersion(TEST_CATALOGVERSION_ID);
		modelService.save(catalogVersionModel);
		// creating product1
		product1 = modelService.create(ProductModel.class);
		product1.setEan("test");
		product1.setCode("product1");
		product1.setManufacturerName("testManufacturer");
		product1.setCatalogVersion(catalogVersionModel);
		modelService.save(product1);
		// creating product2
		product2 = modelService.create(ProductModel.class);
		product2.setEan("ean");
		product2.setCode("product2");
		product2.setManufacturerName("testManufacturer");
		product2.setCatalogVersion(catalogVersionModel);
		modelService.save(product2);
	}

	/**
	 * Search for products using GenericQuery.
	 */
	@Test
	public void searchUsingGenericQuery()
	{
		// search for all products
		final GenericQuery query = new GenericQuery(ProductModel._TYPECODE);
		final List<ProductModel> items = genericSearchService.<ProductModel> search(query).getResult();
		// 2 products expected
		assertEquals(2, items.size());
		assertTrue(items.contains(product1));
		assertTrue(items.contains(product2));
	}

	/**
	 * Search for products using GenericQuery with 'condition' and 'order by' clauses.
	 */
	@Test
	public void searchUsingGenericQueryWithConditionAndOrderBy()
	{
		// search for all products
		final GenericQuery query = new GenericQuery(ProductModel._TYPECODE);
		// where EAN equals "test"
		query.addCondition(GenericCondition.createConditionForValueComparison(new GenericSearchField(ProductModel.EAN),
				Operator.EQUAL, "test"));
		// ordered by CODE
		query.addOrderBy(new GenericSearchOrderBy(new GenericSearchField(ProductModel.CODE), true));

		final List<ProductModel> items = genericSearchService.<ProductModel> search(query).getResult();

		// 1 product expected
		assertEquals(1, items.size());
		assertEquals("test", items.get(0).getEan());
	}

	/**
	 * Search for products using GenericSearchQuery with default values.
	 */
	@Test
	public void searchUsingGenericSearchQuery()
	{
		// search for all products
		final GenericQuery query = new GenericQuery(ProductModel._TYPECODE);
		// where MANUFACTURERNAME equals "testManufacturer"
		query.addCondition(GenericCondition.createConditionForValueComparison(
				new GenericSearchField(ProductModel.MANUFACTURERNAME), Operator.EQUAL, "testManufacturer"));
		// ordered by CODE
		query.addOrderBy(new GenericSearchOrderBy(new GenericSearchField(ProductModel.CODE), true));

		final GenericSearchQuery genericSearchQuery = new GenericSearchQuery(query);
		final List<ProductModel> items = genericSearchService.<ProductModel> search(genericSearchQuery).getResult();

		// 2 products expected
		assertEquals(2, items.size());
	}

	/**
	 * Search for products using GenericSearchQuery with 'rangeStart' and 'rangeCount'.
	 */
	@Test
	public void searchUsingGenericSearchQueryWithRangeStartAndRangeCount()
	{
		// search for all products
		final GenericQuery query = new GenericQuery(ProductModel._TYPECODE);
		// where MANUFACTURERNAME equals "testManufacturer"
		query.addCondition(GenericCondition.createConditionForValueComparison(
				new GenericSearchField(ProductModel.MANUFACTURERNAME), Operator.EQUAL, "testManufacturer"));
		// ordered by CODE
		query.addOrderBy(new GenericSearchOrderBy(new GenericSearchField(ProductModel.CODE), true));

		final GenericSearchQuery genericSearchQuery = new GenericSearchQuery(query);
		// to get 2nd result from expected list, without fetching whole list we set rangeStart and rangeCount values.
		genericSearchQuery.setRangeStart(1);
		genericSearchQuery.setRangeCount(1);

		final List<ProductModel> items = genericSearchService.<ProductModel> search(genericSearchQuery).getResult();

		// 1 product expected
		assertEquals(1, items.size());
		assertEquals("product2", items.get(0).getCode());
	}
}
