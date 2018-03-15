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
package com.hybris.training.cockpits.cmscockpit.services.impl;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSPreviewService;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * This service class overrides the clonePreviewData method as the default implementation has issues in cloning by
 * not copying the complete set of attributes for PreviewDataModel
 */
public class DefaultCMSCockpitLivePreviewService extends DefaultCMSPreviewService
{
	@Override
	public PreviewDataModel clonePreviewData(final PreviewDataModel original)
	{
		if (original == null)
		{
			return null;
		}

		//refreshing
		final ModelService modelService = getModelService();
		if (!modelService.isNew(original) && !modelService.isRemoved(original))
		{
			modelService.refresh(original);
		}

		return modelService.clone(original);
	}
}
