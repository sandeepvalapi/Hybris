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

import de.hybris.platform.product.daos.VariantTypeDao;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.VariantTypeModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Interceptor, which takes care of correct removing of given variant type.
 * 
 */
public class VariantTypeRemoveInterceptor implements RemoveInterceptor
{

	private SessionService sessionService;

	private VariantTypeDao variantTypeDao;

	private SearchRestrictionService searchRestrictionService;


	@Required
	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
	{
		this.searchRestrictionService = searchRestrictionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@Required
	public void setVariantTypeDao(final VariantTypeDao variantTypeDao)
	{
		this.variantTypeDao = variantTypeDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof VariantTypeModel)
		{
			final VariantTypeModel variantTypeModel = (VariantTypeModel) model;
			final int count = sessionService.<Integer> executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public Integer execute()
				{
					searchRestrictionService.disableSearchRestrictions();
					return Integer.valueOf(variantTypeDao.getBaseProductCount(variantTypeModel));
				}
			}).intValue();
			if (count > 0)
			{
				throw new InterceptorException("VariantType " + variantTypeModel.getCode()
						+ " can't be removed because it's still in use by at least one base product");
			}
		}
	}

}
