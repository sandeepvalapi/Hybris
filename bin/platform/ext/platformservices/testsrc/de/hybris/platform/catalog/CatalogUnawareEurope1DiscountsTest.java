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
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class CatalogUnawareEurope1DiscountsTest extends CatalogUnawarePDTRowTest<DiscountRowModel>
{
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private DiscountModel testDiscount;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		testDiscount = modelService.create(DiscountModel.class);
		testDiscount.setCode("DSC");
		modelService.saveAll();
	}

	@Override
	protected DiscountRowModel givenRowForProduct(final ProductModel product)
	{
		final DiscountRowModel result = createNewDiscount();
		result.setProduct(product);
		modelService.save(result);
		return result;
	}

	@Override
	protected DiscountRowModel givenRowForProductId(final String productId)
	{
		final DiscountRowModel result = createNewDiscount();
		result.setProductId(productId);
		modelService.save(result);
		return result;
	}

	@Override
	protected DiscountRowModel givenRowForAnyProduct()
	{
		final DiscountRowModel result = createNewDiscount();
		modelService.save(result);
		return result;
	}

	@Override
	protected List<DiscountRowModel> getSynchronizedRows()
	{
		final String query = "select {PK} from {" + DiscountRowModel._TYPECODE + "} where {" + DiscountRowModel.CATALOGVERSION
				+ "} = ?catalogVersion";
		final ImmutableMap<String, Object> params = ImmutableMap.<String, Object> of("catalogVersion", otherCatalogVersion);
		return flexibleSearchService.<DiscountRowModel> search(query, params).getResult();
	}

	@Override
	protected void assertThatTgtRowMatchSrcRow(final DiscountRowModel tgtRow, final DiscountRowModel srcRow)
	{
		assertThat(tgtRow).isNotNull();
		assertThat(srcRow).isNotNull();
		assertThat(tgtRow).isNotEqualTo(srcRow);
		assertThat(srcRow.getCurrency()).isEqualTo(tgtRow.getCurrency());
		assertThat(srcRow.getDiscount()).isEqualTo(tgtRow.getDiscount());
	}

	@Override
	protected Collection<DiscountRowModel> getRowsFrom(final ProductModel product)
	{
		return product.getEurope1Discounts();
	}

	private DiscountRowModel createNewDiscount()
	{
		final DiscountRowModel result = modelService.create(DiscountRowModel.class);
		result.setCurrency(testCurrency);
		result.setDiscount(testDiscount);
		return result;
	}
}
