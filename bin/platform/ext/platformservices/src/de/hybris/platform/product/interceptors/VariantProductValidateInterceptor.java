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
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Interceptor, which makes some operations (checking base product and base variant type) on variant product, which is
 * being created.
 * <p>
 * Interceptor is being performed only if the given {@link VariantProductModel} is newly created or it's
 * {@link VariantProductModel#BASEPRODUCT} is modified.
 * 
 */
public class VariantProductValidateInterceptor implements ValidateInterceptor
{

	private TypeService typeService;

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof VariantProductModel && (ctx.isNew(model) || ctx.isModified(model, VariantProductModel.BASEPRODUCT)))
		{
			final VariantProductModel variantProductModel = (VariantProductModel) model;
			final ProductModel base = variantProductModel.getBaseProduct();

			if (base != null) //mandatory attribute, if it is null an exception will be thrown by MandatoryAttributesValidator
			{
				final VariantTypeModel baseVariantType = base.getVariantType();
				if (baseVariantType == null)
				{
					throw new InterceptorException("Proposed base product " + base + " got no variant type - cannot be base product");
				}
				final TypeModel productType = typeService.getComposedTypeForCode(VariantProductModel._TYPECODE);
				if (!typeService.isAssignableFrom(productType, baseVariantType))
				{
					throw new InterceptorException("Base variant type " + baseVariantType + " of base product " + base
							+ " is incompatible to variant type " + productType);
				}
			}
		}
	}
}
