/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleaddon.controllers.cms;

import de.hybris.platform.configurablebundleservices.model.components.BundleCarouselComponentModel;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import de.hybris.platform.core.model.product.ProductModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller for CMS BundleCarouselComponentController.
 */
@Controller("BundleCarouselComponentController")
@RequestMapping("/view/BundleCarouselComponentController")
public class BundleCarouselComponentController extends AbstractCMSAddOnComponentController<BundleCarouselComponentModel>
{
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.STARTING_BUNDLES);

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final BundleCarouselComponentModel component)
	{
		final ProductModel currentProduct = getRequestContextData(request).getProduct();

		if (currentProduct != null)
		{
			final ProductData productData = getProductFacade().getProductForCodeAndOptions(currentProduct.getCode(), PRODUCT_OPTIONS);
			model.addAttribute("product", productData);
		}
	}

	private ProductFacade getProductFacade()
	{
		return productFacade;
	}
}
