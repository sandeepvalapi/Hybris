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

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Check if modified {@link CatalogModel#URLPATTERNS} collection is proper. Try to compile each and if there are errors
 * throw {@link InterceptorException}.
 */
public class CatalogURLPatternsValidator implements ValidateInterceptor
{

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CatalogModel)
		{
			final CatalogModel catalog = (CatalogModel) model;
			if (ctx.isModified(catalog, CatalogModel.URLPATTERNS) && catalog.getUrlPatterns() != null)
			{
				final Collection<String> value = catalog.getUrlPatterns();
				final Collection<String> illegal = new ArrayList<String>(value.size());

				for (final String pattern : value)
				{
					if (pattern != null)
					{
						try
						{
							Pattern.compile(pattern);
						}
						catch (final PatternSyntaxException e)
						{
							illegal.add(pattern);
						}
					}
				}
				if (!illegal.isEmpty())
				{
					throw new InterceptorException("illegal patterns found : " + illegal, this);
				}
			}
		}
	}

}
