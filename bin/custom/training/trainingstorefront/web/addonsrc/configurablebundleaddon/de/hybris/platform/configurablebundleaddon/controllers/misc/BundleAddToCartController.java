/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleaddon.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.configurablebundleaddon.controllers.ConfigurablebundleaddonControllerConstants;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

import java.util.Collections;

/**
 * Controller for handling add product to bundle requests.
 */
@Controller
public class BundleAddToCartController extends AbstractController
{
	private static final Logger LOG = Logger.getLogger(BundleAddToCartController.class);
	
	private static final String QUANTITY_ATTR = "quantity";
	private static final String ERROR_MSG_TYPE = "errorMsg";

	@Resource(name = "bundleCartFacade")
	private BundleCartFacade bundleCartFacade;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@RequestMapping(value = "/cart/addBundle", method = RequestMethod.POST, produces = "application/json")
	public String addBundleToCart(@RequestParam("productCodeForBundle") final String code,
		@RequestParam("bundleTemplate") final String bundle, final Model model)
	{
		try
		{
			final CartModificationData cartModification = getBundleCartFacade().startBundle(bundle, code, 1);
			model.addAttribute(QUANTITY_ATTR, cartModification.getQuantityAdded());
			model.addAttribute("entry", cartModification.getEntry());
			model.addAttribute("cartCode", cartModification.getCartCode());

			if (cartModification.getQuantityAdded() == 0L)
			{
				model.addAttribute(ERROR_MSG_TYPE, "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
			}
		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.debug(ex);
			model.addAttribute(ERROR_MSG_TYPE, "basket.error.occurred");
			model.addAttribute(QUANTITY_ATTR, 0);
		}

		model.addAttribute("product", getProductFacade().getProductForCodeAndOptions(code, Collections.singletonList(ProductOption.BASIC)));

		return ConfigurablebundleaddonControllerConstants.Views.Fragments.Cart.AddToCartPopup;
	}

	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	public BundleCartFacade getBundleCartFacade()
	{
		return bundleCartFacade;
	}

}
