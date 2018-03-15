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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class CatalogUnawareEurope1PricesTest extends CatalogUnawarePDTRowTest<PriceRowModel>
{
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Override
	protected PriceRowModel givenRowForProduct(final ProductModel product)
	{
		final PriceRowModel result = createNewPrice(123.45);
		result.setProduct(product);
		modelService.save(result);
		return result;
	}

	@Override
	protected PriceRowModel givenRowForProductId(final String productId)
	{
		final PriceRowModel result = createNewPrice(234.56);
		result.setProductId(productId);
		modelService.save(result);
		return result;
	}

	@Override
	protected PriceRowModel givenRowForAnyProduct()
	{
		final PriceRowModel result = createNewPrice(345.67);
		modelService.save(result);
		return result;
	}

	@Override
	protected List<PriceRowModel> getSynchronizedRows()
	{
		final String query = "select {PK} from {" + PriceRowModel._TYPECODE + "} where {" + PriceRowModel.CATALOGVERSION
				+ "} = ?catalogVersion";
		final ImmutableMap<String, Object> params = ImmutableMap.<String, Object> of("catalogVersion", otherCatalogVersion);
		return flexibleSearchService.<PriceRowModel> search(query, params).getResult();
	}

	@Override
	protected void assertThatTgtRowMatchSrcRow(final PriceRowModel tgtRow, final PriceRowModel srcRow)
	{
		assertThat(tgtRow).isNotNull();
		assertThat(srcRow).isNotNull();
		assertThat(tgtRow).isNotEqualTo(srcRow);
		assertThat(srcRow.getCurrency()).isEqualTo(tgtRow.getCurrency());
		assertThat(srcRow.getPrice()).isEqualTo(tgtRow.getPrice());
	}

	@Override
	protected Collection<PriceRowModel> getRowsFrom(final ProductModel product)
	{
		return product.getEurope1Prices();
	}

	private PriceRowModel createNewPrice(final double price)
	{
		final PriceRowModel result = modelService.create(PriceRowModel.class);
		result.setUnit(testUnit);
		result.setPrice(Double.valueOf(price));
		result.setCurrency(testCurrency);
		return result;
	}
}
