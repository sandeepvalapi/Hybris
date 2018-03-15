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
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;


public class PDTRowRemoveInterceptor implements RemoveInterceptor<PDTRowModel>
{

	private PDTProductModificationMarker pdtProductModificationMarker;

	@Override
	public void onRemove(final PDTRowModel pdtRowModel, final InterceptorContext ctx) throws InterceptorException
	{
		pdtProductModificationMarker.markProductAsModifiedIfFlagSet(pdtRowModel, ctx);
	}

	@Required
	public void setPdtProductModificationMarker(final PDTProductModificationMarker pdtProductModificationMarker)
	{
		this.pdtProductModificationMarker = pdtProductModificationMarker;
	}

}
