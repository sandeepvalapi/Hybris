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
package com.hybris.training.cockpits.cmscockpit.sitewizard;

import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;


/**
 * Wizard page for selecting the css theme.
 */
public class ThemeSelectorPage extends AbstractGenericItemPage
{
	@Override
	public Component createRepresentationItself()
	{
		final Component parent = getWizard().getFrameComponent().getFellow("contentFrame");
		String pageCmpURI = getComponentURI();
		if (StringUtils.isNotBlank(getWizard().getPageRoot()) && pageCmpURI.charAt(0) != '/')
		{
			pageCmpURI = getWizard().getPageRoot() + "/" + pageCmpURI; // NOPMD
		}
		return Executions.createComponents(pageCmpURI, parent, pageContent.getAttributes());
	}
}
