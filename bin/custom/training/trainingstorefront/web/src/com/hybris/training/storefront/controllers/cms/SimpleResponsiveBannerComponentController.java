/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import com.hybris.training.storefront.controllers.ControllerConstants;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller for CMS SimpleResponsiveBannerComponent
 */
@Controller("SimpleResponsiveBannerComponentController")
@RequestMapping(value = ControllerConstants.Actions.Cms.SimpleResponsiveBannerComponent)
public class SimpleResponsiveBannerComponentController extends
		AbstractAcceleratorCMSComponentController<SimpleResponsiveBannerComponentModel>
{
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final SimpleResponsiveBannerComponentModel component)
	{
		final List<ImageData> mediaDataList = responsiveMediaFacade.getImagesFromMediaContainer(component
				.getMedia(commerceCommonI18NService.getCurrentLocale()));
		model.addAttribute("medias", mediaDataList);
		model.addAttribute("urlLink", component.getUrlLink());
	}
}
