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
package de.hybris.platform.product.interceptors;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


abstract class UpdateBaseProductModificationTime
{
	protected final VariantProductModel variantProductModel;
	protected final ProductModel currentProductModel;
	protected final ProductModel previosProductModel;
	protected final InterceptorContext interceptorContext;
	protected final ItemModelContext variantProductContext;
	protected final Collection<ProductModel> productsToModify;

	private UpdateBaseProductModificationTime(final VariantProductModel model, final InterceptorContext ctx)
	{
		variantProductModel = model;
		interceptorContext = ctx;
		currentProductModel = model.getBaseProduct();
		variantProductContext = ModelContextUtils.getItemModelContext(model);
		previosProductModel = getPreviousProductModel();
		productsToModify = new ArrayList<>(1);
		extractBaseProductToModify();
	}

	public static UpdateBaseProductModificationTime fromPrepareInterceptor(final VariantProductModel model,
			final InterceptorContext ctx)
	{
		return new UpdateBaseProductModificationTime(model, ctx)
		{
			@Override
			public void execute()
			{
				for (final ProductModel productToModify : productsToModify)
				{
					productToModify.setModifiedtime(new Date());
					interceptorContext.registerElement(productToModify, getProductSource(productToModify));
				}
			}
		};
	}

	public static UpdateBaseProductModificationTime fromRemoveInterceptor(final VariantProductModel model,
			final InterceptorContext ctx)
	{
		return new UpdateBaseProductModificationTime(model, ctx)
		{

			@Override
			protected void extractBaseProductToModify()
			{
				markKnownBaseProductsAsModified();
			}

			@Override
			public void execute()
			{
				for (final ProductModel productToModify : productsToModify)
				{
					productToModify.setModifiedtime(new Date());
				}
				interceptorContext.getModelService().saveAll(productsToModify);
			}
		};
	}

	public abstract void execute();

	private ProductModel getPreviousProductModel()
	{
		if (interceptorContext.isNew(variantProductModel)
				|| !interceptorContext.isModified(variantProductModel, VariantProductModel.BASEPRODUCT))
		{
			return null;
		}
		return variantProductContext.getOriginalValue(VariantProductModel.BASEPRODUCT);
	}

	protected void extractBaseProductToModify()
	{
		if (extractFormNewVaraintProductSucceeded())
		{
			return;
		}
		if (extractFormModifiedProductSucceeded())
		{
			return;
		}
	}

	private boolean extractFormNewVaraintProductSucceeded()
	{
		if (!interceptorContext.isNew(variantProductModel))
		{
			return false;
		}
		markKnownBaseProductsAsModified();
		return true;
	}

	private boolean extractFormModifiedProductSucceeded()
	{
		if (!interceptorContext.isModified(variantProductModel)
				|| !interceptorContext.isModified(variantProductModel, VariantProductModel.BASEPRODUCT))
		{
			return false;
		}
		markKnownBaseProductsAsModified();
		return true;
	}

	protected void markKnownBaseProductsAsModified()
	{
		if (canBeMarkedAsModified(previosProductModel))
		{
			productsToModify.add(previosProductModel);
		}
		if (canBeMarkedAsModified(currentProductModel))
		{
			productsToModify.add(currentProductModel);
		}
	}

	private boolean canBeMarkedAsModified(final ProductModel productModel)
	{
		return productModel != null && !interceptorContext.isRemoved(productModel);
	}

	protected Object getProductSource(final ProductModel model)
	{
		if (interceptorContext.isNew(model))
		{
			return null;
		}
		else
		{
			return interceptorContext.getModelService().getSource(model);
		}
	}
}
