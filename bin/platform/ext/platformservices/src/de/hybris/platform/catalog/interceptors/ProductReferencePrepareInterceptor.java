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
package de.hybris.platform.catalog.interceptors;

import de.hybris.platform.catalog.jalo.ProductReference;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;


public class ProductReferencePrepareInterceptor implements PrepareInterceptor<ProductReferenceModel>
{
	@Override
	public void onPrepare(final ProductReferenceModel productReferenceModel, final InterceptorContext ctx)
			throws InterceptorException
	{
		if (ctx.isModified(productReferenceModel))
		{
			final ProductModel source = productReferenceModel.getSource();

			if (source != null)
			{
				if (ctx.isNew(productReferenceModel))
				{
					modifyAndRegisterProduct(ctx, source);
					return;
				}

				final ItemModelContextImpl itemModelCtx = (ItemModelContextImpl) productReferenceModel.getItemModelContext();

				final ModelValueHistory history = itemModelCtx.getValueHistory();

				if (checkAndHandleUnlocalizedAttributesForChanges(ctx, itemModelCtx, history)
						|| checkLocalizedAttributesForChanges(itemModelCtx, history))
				{
					modifyAndRegisterProduct(ctx, source);
				}
			}
		}
	}

	private void modifyAndRegisterProduct(final InterceptorContext ctx, final ProductModel productModel)
	{
		productModel.setModifiedtime(new Date());

		if (!ctx.contains(productModel, PersistenceOperation.SAVE))
		{
			ctx.registerElementFor(productModel, PersistenceOperation.SAVE);
		}
	}

	private boolean checkAndHandleUnlocalizedAttributesForChanges(final InterceptorContext ctx,
			final ItemModelContextImpl itemModelContext, final ModelValueHistory history)
	{
		final Set<String> attributes = Sets.newHashSet(history.getDirtyAttributes());
		if (attributes.contains(ProductReference.SOURCE))
		{
			final Object originalValue = getOriginalValueForAttribute(itemModelContext, null, ProductReference.SOURCE);
			final Object currentValue = itemModelContext.getRawPropertyValue(ProductReference.SOURCE);
			if (originalValue != null && !originalValue.equals(currentValue))
			{
				modifyAndRegisterProduct(ctx, (ProductModel) originalValue);
				return true;
			}
			attributes.remove(ProductReference.SOURCE);
		}

		for (final String attr : attributes)
		{
			final Object originalValue = getOriginalValueForAttribute(itemModelContext, null, attr);
			final Object currentValue = itemModelContext.getRawPropertyValue(attr);

			if (!Objects.equals(originalValue, currentValue))
			{
				return true;
			}
		}
		return false;
	}

	private boolean checkLocalizedAttributesForChanges(final ItemModelContextImpl itemModelContext,
			final ModelValueHistory history)
	{
		for (final Map.Entry<Locale, Set<String>> entry : history.getDirtyLocalizedAttributes().entrySet())
		{
			for (final String attr : entry.getValue())
			{
				final Object originalValue = getOriginalValueForAttribute(itemModelContext, entry.getKey(), attr);
				final Object currentValue = itemModelContext.getCombinedLocalizedValuesMap().get(attr).get(entry.getKey());

				if (!Objects.equals(originalValue, currentValue))
				{
					return true;
				}
			}
		}
		return false;
	}


	private Object getOriginalValueForAttribute(final ItemModelContextImpl ctx, final Locale locale, final String attr)
	{
		final Object value;
		if (locale == null)
		{
			value = ctx.isLoaded(attr) ? ctx.getOriginalValue(attr) : ctx.loadOriginalValue(attr);
		}
		else
		{
			value = ctx.isLoaded(attr, locale) ? ctx.getOriginalValue(attr, locale) : ctx.loadOriginalValue(attr, locale);
		}
		return value;
	}
}
