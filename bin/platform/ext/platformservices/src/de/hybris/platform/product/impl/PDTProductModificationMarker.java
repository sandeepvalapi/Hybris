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

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.Model;

import static de.hybris.platform.servicelayer.interceptor.PersistenceOperation.SAVE;


/**
 * Marks PDTRow product as modified if pdtrow.mark.product.modified is enabled in session/configuration. Used by
 * {@link PDTRowPrepareInterceptor} and {@link PDTRowRemoveInterceptor}
 */
public class PDTProductModificationMarker
{
	private SessionService sessionService;

	public void markProductAsModifiedIfFlagSet(final PDTRowModel pdtRowModel, final InterceptorContext ctx)
	{
		final Session currentSession = sessionService.getCurrentSession();
		final boolean markProductModifiedSession = Boolean.TRUE
				.equals(currentSession.getAttribute(Europe1Constants.PDTROW_MARK_PRODUCT_MODIFIED));

		final boolean markProductModified = Config.getBoolean(Europe1Constants.PDTROW_MARK_PRODUCT_MODIFIED, false);
		if (markProductModifiedSession || markProductModified)
		{
			final ProductModel product = pdtRowModel.getProduct();

			if (product != null)
			{
				product.setModifiedtime(new Date());

				if (!ctx.contains(product, PersistenceOperation.SAVE))
				{
					ctx.registerElementFor(product, PersistenceOperation.SAVE);
				}
			}
		}
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
