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
package com.hybris.training.cockpits.cmscockpit.browser.filters;

import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.util.localization.Localization;



public class DesktopUiExperienceBrowserFilter extends AbstractUiExperienceFilter
{
	private static final String DESKTOP_UI_EXPERIENCE_LABEL_KEY = "desktop.ui.experience.label.key";

	@Override
	public boolean exclude(final Object item)
	{

		return false;
	}

	@Override
	public void filterQuery(final Query query)
	{
		//empty because DESKTOP pages are displayed as default 
	}


	@Override
	public String getLabel()
	{
		return Localization.getLocalizedString(DESKTOP_UI_EXPERIENCE_LABEL_KEY);
	}

}
