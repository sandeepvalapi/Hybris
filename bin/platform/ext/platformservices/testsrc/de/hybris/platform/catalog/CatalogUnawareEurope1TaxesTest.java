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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class CatalogUnawareEurope1TaxesTest extends CatalogUnawarePDTRowTest<TaxRowModel>
{
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private TaxModel testTax;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		testTax = modelService.create(TaxModel.class);
		testTax.setCode("testTax");
		testTax.setName("testTax");
		testTax.setValue(Double.valueOf(7));
	}

	@Override
	protected TaxRowModel givenRowForProduct(final ProductModel product)
	{
		final TaxRowModel result = createNewTax();
		result.setProduct(product);
		modelService.save(result);
		return result;
	}

	@Override
	protected TaxRowModel givenRowForProductId(final String productId)
	{
		final TaxRowModel result = createNewTax();
		result.setProductId(productId);
		modelService.save(result);
		return result;
	}

	@Override
	protected TaxRowModel givenRowForAnyProduct()
	{
		final TaxRowModel result = createNewTax();
		modelService.save(result);
		return result;
	}

	@Override
	protected List<TaxRowModel> getSynchronizedRows()
	{
		final String query = "select {PK} from {" + TaxRowModel._TYPECODE + "} where {" + TaxRowModel.CATALOGVERSION
				+ "} = ?catalogVersion";
		final ImmutableMap<String, Object> params = ImmutableMap.<String, Object> of("catalogVersion", otherCatalogVersion);
		return flexibleSearchService.<TaxRowModel> search(query, params).getResult();
	}

	@Override
	protected void assertThatTgtRowMatchSrcRow(final TaxRowModel tgtRow, final TaxRowModel srcRow)
	{
		assertThat(tgtRow).isNotNull();
		assertThat(srcRow).isNotNull();
		assertThat(tgtRow).isNotEqualTo(srcRow);
		assertThat(srcRow.getCurrency()).isEqualTo(tgtRow.getCurrency());
		assertThat(srcRow.getTax()).isEqualTo(tgtRow.getTax());
	}

	@Override
	protected Collection<TaxRowModel> getRowsFrom(final ProductModel product)
	{
		return product.getEurope1Taxes();
	}

	private TaxRowModel createNewTax()
	{
		final TaxRowModel result = modelService.create(TaxRowModel.class);
		result.setTax(testTax);
		result.setCurrency(testCurrency);
		return result;
	}

}
