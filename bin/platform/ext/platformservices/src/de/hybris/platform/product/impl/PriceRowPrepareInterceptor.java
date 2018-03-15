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
package de.hybris.platform.product.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * {@link PrepareInterceptor} for the {@link PriceRowModel}. Sets the fallback {@link UnitModel} if no unit is provided
 * for the pricerow. The fallback is the unit from {@link ProductModel#UNIT}. If the {@link PriceRowModel} AND
 * {@link ProductModel#UNIT} contains no unit a {@link InterceptorException} is thrown.
 * 
 * Besides - takes care of updating the matchValue depending on Product/Pg and User/Ug values. The PDtRow common logic
 * is executed as well - see ({@link PDTRowPrepareInterceptor}
 * 
 */
public class PriceRowPrepareInterceptor extends PDTRowPrepareInterceptor
{

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof PriceRowModel)
		{
			final PriceRowModel prModel = (PriceRowModel) model;

			if (prModel.getUnit() == null)
			{
				final UnitModel fallbackUnit = (prModel.getProduct() == null) ? null : prModel.getProduct().getUnit();
				if (fallbackUnit == null)
				{
					throw new InterceptorException("missing unit for price row ");
				}
				else
				{
					prModel.setUnit(fallbackUnit);
				}
			}

			super.onPrepare(prModel, ctx);

			if (ctx.isNew(model) || ctx.isModified(model, PriceRowModel.PRODUCT) || ctx.isModified(model, PriceRowModel.PG)
					|| ctx.isModified(model, PriceRowModel.USER) || ctx.isModified(model, PriceRowModel.UG)
					|| ctx.isModified(model, PriceRowModel.PRODUCTID))
			{
				updateMatchValue(prModel);
			}
		}
	}

	/**
	 * Value 9 : P + C 10 10 : 10 8 : PG + C 01 10 : 6 !!! 7 : P + CG 10 01 : 9 6 : PG + CG 01 01 : 5 !!! 5 : P + 10 00 :
	 * 8 4 : PG + 01 00 : 4 3 : + C 00 10 : 2 2 : + CG 00 01 : 1 1 : + 00 00 : 0
	 * 
	 * r1 > r2 -> value(r1) > value(r2)
	 */
	protected int calculateMatchValue(final PriceRowModel price)
	{
		final boolean _product = price.getProduct() != null || price.getProductId() != null;
		final boolean _productGroup = price.getPg() != null;
		final boolean _user = price.getUser() != null;
		final boolean _userGroup = price.getUg() != null;
		// ---
		int value = 0;
		if (_product)
		{
			if (_user)
			{
				value = 9;
			}
			else if (_userGroup)
			{
				value = 7;
			}
			else
			{
				value = 5;
			}
		}
		else if (_productGroup)
		{
			if (_user)
			{
				value = 8;
			}
			else if (_userGroup)
			{
				value = 6;
			}
			else
			{
				value = 4;
			}
		}
		else
		{
			if (_user)
			{
				value = 3;
			}
			else if (_userGroup)
			{
				value = 2;
			}
			else
			{
				value = 1;
			}
		}
		return value;
	}

	private void updateMatchValue(final PriceRowModel prModel)
	{
		prModel.setMatchValue(Integer.valueOf(calculateMatchValue(prModel)));
	}

	@Override
	protected void updateCatalogVersion(final PDTRowModel pdtModel)
	{
		CatalogVersionModel catver = ((PriceRowModel) pdtModel).getCatalogVersion();
		if (catver == null)
		{
			final ProductModel prod = pdtModel.getProduct();
			if (prod != null)
			{
				catver = getCatalogTypeService().getCatalogVersionForCatalogVersionAwareModel(prod);
				//
				if (catver != null)
				{
					((PriceRowModel) pdtModel).setCatalogVersion(catver);
				}
			}
		}
	}
}
