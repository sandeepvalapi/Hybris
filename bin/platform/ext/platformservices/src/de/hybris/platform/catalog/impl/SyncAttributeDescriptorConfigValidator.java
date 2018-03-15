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
package de.hybris.platform.catalog.impl;

import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * {@link ValidateInterceptor} for the {@link SyncAttributeDescriptorConfigModel}.
 */
public class SyncAttributeDescriptorConfigValidator implements ValidateInterceptor
{
	private FlexibleSearchService flexibleSearchService;

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof SyncAttributeDescriptorConfigModel)
		{
			final SyncAttributeDescriptorConfigModel modelImpl = (SyncAttributeDescriptorConfigModel) model;
			final SyncItemJobModel jobModel = modelImpl.getSyncJob();
			final AttributeDescriptorModel attributeDescriptorModel = modelImpl.getAttributeDescriptor();

			if (isInheritedImpl(attributeDescriptorModel))
			{
				throw new InterceptorException("Attribute " + attributeDescriptorModel + " is inherited - use the declared one.");
			}

			if (ctx.isNew(model) && //the config must be not saved yet / new
					jobModel != null && !ctx.isNew(jobModel) // syncjob must be persisted/existing in the db 
					&& verifyConfigAlreadyExists(jobModel, attributeDescriptorModel))
			{
				throw new InterceptorException(modelImpl.getItemtype() + " for job " + jobModel + " and attribute "
						+ attributeDescriptorModel + " already exists");
			}
		}
	}

	/**
	 * @param attributeDesc
	 * @return true/false depending if it is inherited or not
	 */
	private boolean isInheritedImpl(final AttributeDescriptorModel attributeDesc)
	{
		return attributeDesc != null && attributeDesc.getModifiers() != null
				&& (attributeDesc.getModifiers().intValue() & AttributeDescriptor.INHERITED_FLAG) != 0;
	}

	private boolean verifyConfigAlreadyExists(final SyncItemJobModel job, final AttributeDescriptorModel attributeDesc)
			throws InterceptorException
	{
		final Map params = new HashMap();
		params.put("me", job);
		params.put("ad", attributeDesc);

		final SearchResult<SyncAttributeDescriptorConfigModel> result = flexibleSearchService.search("SELECT {" + Item.PK
				+ "} FROM {" + SyncAttributeDescriptorConfigModel._TYPECODE + "} " + "WHERE {"
				+ SyncAttributeDescriptorConfigModel.ATTRIBUTEDESCRIPTOR + "}=?ad and {" + SyncAttributeDescriptorConfigModel.SYNCJOB
				+ "}=?me ", params);
		return !(result.getResult().isEmpty());
	}

	/**
	 * @param attributeDesc
	 * @return true/false depending if it is inherited or not
	 * @deprecated since 4.4
	 */
	@Deprecated
	protected boolean isInherited(final AttributeDescriptorModel attributeDesc)
	{
		return isInheritedImpl(attributeDesc);
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService service)
	{
		this.flexibleSearchService = service;
	}
}
