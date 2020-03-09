/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.CMSTabParagraphContainerModel;
import com.hybris.training.storefront.controllers.ControllerConstants;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller for CMSTabParagraphContainer. This controller is used for displaying the container own page
 */
@Controller("CMSTabParagraphContainerController")
@RequestMapping(value = ControllerConstants.Actions.Cms.CMSTabParagraphContainer)
public class CMSTabParagraphContainerController extends AbstractAcceleratorCMSComponentController<CMSTabParagraphContainerModel>
{
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final CMSTabParagraphContainerModel component)
	{
		model.addAttribute("components", component.getSimpleCMSComponents());
	}
}
