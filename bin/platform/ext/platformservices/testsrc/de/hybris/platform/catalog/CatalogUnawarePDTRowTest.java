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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.fest.assertions.Condition;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
public abstract class CatalogUnawarePDTRowTest<T extends PDTRowModel> extends ServicelayerBaseTest
{
	@Resource
	protected ModelService modelService;

	protected CatalogVersionModel testCatalogVersion;
	protected CatalogVersionModel otherCatalogVersion;
	protected ProductModel testProduct;
	protected ProductModel otherProduct;
	protected UnitModel testUnit;
	protected CurrencyModel testCurrency;

	@Before
	public void setUp()
	{
		testUnit = modelService.create(UnitModel.class);
		testUnit.setCode("ly");
		testUnit.setName("light-year");
		testUnit.setUnitType("length");

		testCurrency = modelService.create(CurrencyModel.class);
		testCurrency.setName("BTC");
		testCurrency.setIsocode("BTC");
		testCurrency.setSymbol("BTC");

		final CatalogModel testCatalog = modelService.create(CatalogModel.class);
		testCatalog.setId("TEST_CATALOG");

		testCatalogVersion = modelService.create(CatalogVersionModel.class);
		testCatalogVersion.setCatalog(testCatalog);
		testCatalogVersion.setVersion("TEST_VERSION");

		otherCatalogVersion = modelService.create(CatalogVersionModel.class);
		otherCatalogVersion.setCatalog(testCatalog);
		otherCatalogVersion.setVersion("OTHER_VERSION");

		testProduct = modelService.create(ProductModel.class);
		testProduct.setCode("TEST_PRODUCT");
		testProduct.setCatalogVersion(testCatalogVersion);
		testProduct.setUnit(testUnit);

		otherProduct = modelService.create(ProductModel.class);
		otherProduct.setCode("OTHER_PRODUCT");
		otherProduct.setCatalogVersion(testCatalogVersion);
		otherProduct.setUnit(testUnit);

		modelService.saveAll();
	}

	@Test
	public void shouldGetRowForAnyProduct()
	{
		final T row = givenRowForAnyProduct();

		modelService.refresh(testProduct);

		assertThat(getRowsFrom(testProduct)).containsOnly(row);
	}

	@Test
	public void shouldGetRowByProductCode()
	{
		final T row = givenRowForProductId(testProduct.getCode());

		modelService.refresh(testProduct);

		assertThat(getRowsFrom(testProduct)).containsOnly(row);
	}

	@Test
	public void shouldGetRowByProduct()
	{
		final T row = givenRowForProduct(testProduct);

		modelService.refresh(testProduct);

		assertThat(getRowsFrom(testProduct)).containsOnly(row);
	}


	@Test
	public void shouldNotFindRowForDifferentProductCode()
	{
		givenRowForProductId(otherProduct.getCode());

		modelService.refresh(testProduct);

		assertThat(getRowsFrom(testProduct)).isEmpty();
	}

	@Test
	public void shouldNotFindRowForDifferentProduct()
	{
		givenRowForProduct(otherProduct);

		modelService.refresh(testProduct);

		assertThat(getRowsFrom(testProduct)).isEmpty();
	}

	@Test
	public void shouldFindOnlyRowsForProductCode()
	{
		givenRowForProductId(otherProduct.getCode());
		final T expectedRow = givenRowForProductId(testProduct.getCode());

		modelService.refresh(testProduct);

		assertThat(getRowsFrom(testProduct)).containsOnly(expectedRow);
	}

	@Test
	public void shouldFindOnlyRowsForProduct()
	{
		givenRowForProduct(otherProduct);
		final T expectedRow = givenRowForProduct(testProduct);

		modelService.refresh(testProduct);

		assertThat(getRowsFrom(testProduct)).containsOnly(expectedRow);
	}

	@Test
	public void shouldFindMultipleRows()
	{
		final T anyProductRow = givenRowForAnyProduct();
		final T byCodeRow = givenRowForProductId(testProduct.getCode());
		final T byProductRow = givenRowForProduct(testProduct);

		modelService.refresh(testProduct);

		assertThat(getRowsFrom(testProduct)).containsOnly(anyProductRow, byCodeRow, byProductRow);
	}

	@Test
	public void shouldNotRemoveRowForAnyProduct()
	{
		final T anyProductRow = givenRowForAnyProduct();

		modelService.remove(testProduct);

		assertThat(modelService.isRemoved(anyProductRow)).isFalse();
	}

	@Test
	public void shouldNotRemoveRowForProductCode()
	{
		final T byCodeRow = givenRowForProductId(testProduct.getCode());

		modelService.remove(testProduct);

		assertThat(modelService.isRemoved(byCodeRow)).isFalse();
	}

	@Test
	public void shouldRemoveRowForProduct()
	{
		final T byProductRow = givenRowForProduct(testProduct);

		modelService.remove(testProduct);

		assertThat(modelService.isRemoved(byProductRow)).isTrue();
	}

	@Test
	public void shouldRemoveOnlyRowForProduct()
	{
		final T anyProductRow = givenRowForAnyProduct();
		final T byCodeRow = givenRowForProductId(testProduct.getCode());
		final T byProductRow = givenRowForProduct(testProduct);

		modelService.remove(testProduct);

		assertThat(modelService.isRemoved(anyProductRow)).isFalse();
		assertThat(modelService.isRemoved(byCodeRow)).isFalse();
		assertThat(modelService.isRemoved(byProductRow)).isTrue();
	}

	@Test
	public void shouldSynchronizeRowForProduct()
	{
		final T srcRow = givenRowForProduct(testProduct);

		performTestCatalogSynchronization();

		final List<T> syncRows = getSynchronizedRows();
		assertThat(syncRows).hasSize(1).excludes(srcRow);
		final T tgtRow = syncRows.get(0);
		assertThat(tgtRow).satisfies(isSynchronizedVersionOf(srcRow));
	}

	private Condition<Object> isSynchronizedVersionOf(final T srcRow)
	{
		return new Condition<Object>()
		{
			@Override
			public boolean matches(final Object value)
			{
				final T tgtRow = (T) value;
				assertThatTgtRowMatchSrcRow(tgtRow, srcRow);
				return true;
			}
		};
	}

	@Test
	public void shouldNotSynchronizeAnyProductRow()
	{
		givenRowForAnyProduct();

		performTestCatalogSynchronization();

		final List<T> syncRows = getSynchronizedRows();
		assertThat(syncRows).isEmpty();
	}

	@Test
	public void shouldNotSynchronizeRowForProductCode()
	{
		givenRowForProductId(testProduct.getCode());

		performTestCatalogSynchronization();

		final List<T> syncRows = getSynchronizedRows();
		assertThat(syncRows).isEmpty();
	}

	protected abstract T givenRowForProduct(final ProductModel product);

	protected abstract T givenRowForProductId(final String productId);

	protected abstract T givenRowForAnyProduct();

	protected abstract List<T> getSynchronizedRows();

	protected abstract void assertThatTgtRowMatchSrcRow(final T tgtRow, final T srcRow);

	protected abstract Collection<T> getRowsFrom(ProductModel product);

	protected void performTestCatalogSynchronization()
	{
		final SynchronizationTestHelper helper = SynchronizationTestHelper.builder(testCatalogVersion, otherCatalogVersion).build();
		helper.performSynchronization();
	}
}
