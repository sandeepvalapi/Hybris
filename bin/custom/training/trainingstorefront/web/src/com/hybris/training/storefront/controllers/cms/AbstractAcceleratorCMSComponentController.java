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
package com.hybris.training.storefront.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import com.hybris.training.storefront.controllers.ControllerConstants;

import org.apache.commons.lang.StringUtils;


/**
 * Abstract accelerator CMS component controller providing a common implementation for the getView method.
 */
public abstract class AbstractAcceleratorCMSComponentController<T extends AbstractCMSComponentModel> extends
		AbstractCMSComponentController<T>
{
	@Override
	protected String getView(final T component)
	{
		// build a jsp response based on the component type
		return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(getTypeCode(component));
	}

}
