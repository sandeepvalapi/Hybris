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
package de.hybris.platform.catalog.references.impl;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.references.ProductReferenceService;
import de.hybris.platform.catalog.references.daos.ProductReferencesDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link ProductReferenceService}
 */
public class DefaultProductReferenceService extends AbstractBusinessService implements ProductReferenceService
{
	private ProductReferencesDao productReferencesDao;

	@Override
	public Collection<ProductReferenceModel> getProductReferences(final String qualifier, final ProductModel source,
			final ProductModel target, final boolean activeOnly)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);
		if (getModelService().isNew(source) || getModelService().isNew(target))
		{
			return Collections.EMPTY_LIST;
		}
		return productReferencesDao.findProductReferences(qualifier, source, target, null, activeOnly ? Boolean.TRUE : null);
	}

	@Override
	public Collection<ProductReferenceModel> getProductReferencesForSourceAndTarget(final ProductModel source,
			final ProductModel target, final boolean activeOnly)
	{
		return getProductReferences(null, source, target, activeOnly);
	}

	@Override
	public Collection<ProductReferenceModel> getProductReferencesForSourceProduct(final ProductModel source,
			final ProductReferenceTypeEnum referenceType, final boolean activeOnly)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		if (getModelService().isNew(source))
		{
			return Collections.EMPTY_LIST;
		}
		return productReferencesDao.findProductReferences(null, source, null, referenceType, activeOnly ? Boolean.TRUE : null);
	}

	@Override
	public Collection<ProductReferenceModel> getProductReferencesForTargetProduct(final ProductModel target,
			final ProductReferenceTypeEnum referenceType, final boolean activeOnly)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);
		if (getModelService().isNew(target))
		{
			return Collections.EMPTY_LIST;
		}
		return productReferencesDao.findProductReferences(null, null, target, referenceType, activeOnly ? Boolean.TRUE : null);
	}

	@Required
	public void setProductReferencesDao(final ProductReferencesDao productReferencesDao)
	{
		this.productReferencesDao = productReferencesDao;
	}

}
