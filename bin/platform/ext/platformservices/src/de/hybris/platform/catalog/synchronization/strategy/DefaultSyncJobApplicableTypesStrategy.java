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
package de.hybris.platform.catalog.synchronization.strategy;


import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultSyncJobApplicableTypesStrategy implements SyncJobApplicableTypesStrategy
{
	private TypeService typeService;
	private CatalogTypeService catalogTypeService;

	protected boolean isTypeInRootTypes(final String typeCode, final SyncItemJobModel syncItemJob)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("syncItemJob", syncItemJob);
		ServicesUtil.validateParameterNotNullStandardMessage("typeCode", typeCode);

		if (StringUtils.isNotBlank(typeCode) )
		{
   		final ComposedTypeModel givenComposedType = typeService.getComposedTypeForCode(typeCode);
   		return syncItemJob.getRootTypes().stream().anyMatch(rootType -> typeService.isAssignableFrom(rootType, givenComposedType));
		}
		return false;
	}

	@Override
	public boolean checkIfApplicable(final ItemModel theItem, final SyncItemJobModel syncItemJob)
	{
		if( catalogTypeService.isCatalogVersionAwareModel(theItem) )
		{
			final CatalogVersionModel myVersion = catalogTypeService.getCatalogVersionForCatalogVersionAwareModel(theItem);
			
			return ( ObjectUtils.equals(syncItemJob.getSourceVersion(), myVersion) || 
					ObjectUtils.equals(syncItemJob.getTargetVersion(), myVersion) ) &&
					isTypeInRootTypes(theItem.getItemtype(), syncItemJob);
		}
		return false;
	}

	@Required
	public void setCatalogTypeService(CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}

	@Required
	public void setTypeService(TypeService typeService)
	{
		this.typeService = typeService;
	}


}
