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
package com.hybris.training.cockpits.cmscockpit.session.impl;

import de.hybris.platform.cmscockpit.session.impl.CatalogBrowserArea;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import org.apache.log4j.Logger;


/**
 *
 *
 *
 */
public class DefaultCatalogBrowserArea extends CatalogBrowserArea
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultCatalogBrowserArea.class);


	@Override
	public void update()
	{
		super.update();
		if (getPerspective().getActiveItem() != null)
		{
			final BrowserModel browserModel = getFocusedBrowser();
			if (browserModel instanceof DefaultCmsPageBrowserModel)
			{
				final TypedObject associatedPageTypeObject = ((DefaultCmsPageBrowserModel) browserModel).getCurrentPageObject();
				((DefaultCmsCockpitPerspective) getPerspective()).activateItemInEditorFallback(associatedPageTypeObject);
			}
		}
	}


}
