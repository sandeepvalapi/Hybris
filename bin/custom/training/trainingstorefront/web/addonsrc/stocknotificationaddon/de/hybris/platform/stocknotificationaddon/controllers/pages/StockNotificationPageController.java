/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationaddon.controllers.pages;

import de.hybris.platform.acceleratorfacades.futurestock.FutureStockFacade;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.FutureStockData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsfacades.productinterest.ProductInterestFacade;
import de.hybris.platform.notificationfacades.facades.NotificationPreferenceFacade;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.stocknotificationaddon.controllers.StocknotificationaddonControllerConstants;
import de.hybris.platform.stocknotificationaddon.forms.StockNotificationForm;
import de.hybris.platform.stocknotificationaddon.handlers.StockNotificationHandler;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@Scope("tenant")
@RequestMapping("/my-account/my-stocknotification")
public class StockNotificationPageController extends AbstractPageController
{
	private static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "{productCode:.*}";
	private static final String EXPIRY_DAY = "customerinterestsservices.expiryDay";
	private static final String PRODUCT_DETAIL_PAGE = "pdp";
	private static final String[] DISALLOWED_FIELDS = new String[] {};

	@Resource(name = "productInterestFacade")
	private ProductInterestFacade productInterestFacade;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "stockNotificationHandler")
	private StockNotificationHandler stockNotificationHandler;

	@Resource(name = "notificationPreferenceFacade")
	private NotificationPreferenceFacade notificationPreferenceFacade;

	@Resource(name = "futureStockFacade")
	private FutureStockFacade futureStockFacade;


	@InitBinder
	public void initBinder(final WebDataBinder binder) {
	    binder.setDisallowedFields(DISALLOWED_FIELDS);
	}

	@RequestMapping(value = "/open/" + PRODUCT_CODE_PATH_VARIABLE_PATTERN)
	@RequireHardLogIn
	public String getStockNotification(@PathVariable("productCode") final String encodedProductCode,
			@RequestParam(value = "channel", required = false) final String channel,
			final HttpServletRequest request,
			final HttpServletResponse response, final Model model)
	{

		final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
		final Optional<ProductInterestData> optional = productInterestFacade
				.getProductInterestDataForCurrentCustomer(productCode,
						NotificationType.BACK_IN_STOCK);

		if (optional.isPresent())
		{
			model.addAttribute("stockNotificationForm",
					stockNotificationHandler.prepareStockNotifcationFormByInterest(optional.get()));
			model.addAttribute("removeUrl",
					"/my-account/my-stocknotification/remove/" + productCode);
		}
		else
		{
			model.addAttribute("stockNotificationForm", stockNotificationHandler
					.prepareStockNotifcationFormByCustomer(notificationPreferenceFacade.getValidNotificationPreferences()));
		}
		model.addAttribute("action", "/my-account/my-stocknotification/add/" + productCode);
		model.addAttribute("expiryDay", Config.getParameter(EXPIRY_DAY));

		final List<FutureStockData> availabilities = futureStockFacade.getFutureAvailability(productCode);
		if (!CollectionUtils.isEmpty(availabilities))
		{
			availabilities.sort(Comparator.comparing(FutureStockData::getDate).reversed());
			model.addAttribute("expiryDate",availabilities.get(0));
		}

		if (PRODUCT_DETAIL_PAGE.equals(channel))
		{
			return StocknotificationaddonControllerConstants.Pages.AddNotificationPage;
		}
		return StocknotificationaddonControllerConstants.Pages.AddNotificationFromInterestPage;
	}


	@RequestMapping(value = "/add/" + PRODUCT_CODE_PATH_VARIABLE_PATTERN)
	@RequireHardLogIn
	public @ResponseBody String addStockNotification(@PathVariable final String productCode,
			@ModelAttribute("stockNotificationForm") final StockNotificationForm stockNotificationForm,
			final HttpServletResponse response, final Model model)
	{
		final Optional<ProductInterestData> optional = productInterestFacade
				.getProductInterestDataForCurrentCustomer(productCode, NotificationType.BACK_IN_STOCK);

		ProductInterestData productInterest = new ProductInterestData();

		final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
				Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.CATEGORIES));
		productInterest.setProduct(product);

		if (optional.isPresent())
		{
			productInterest = optional.get();
		}

		stockNotificationHandler.prepareInterestData(stockNotificationForm, productInterest);

		productInterestFacade.saveProductInterest(productInterest);

		return "success";
	}

	@RequestMapping(value = "/remove/" + PRODUCT_CODE_PATH_VARIABLE_PATTERN)
	@RequireHardLogIn
	public @ResponseBody String removeStockNotification(@PathVariable final String productCode, final HttpServletResponse response,
			final Model model)
	{

		final Optional<ProductInterestData> optional = productInterestFacade.getProductInterestDataForCurrentCustomer(productCode,
				NotificationType.BACK_IN_STOCK);

		optional.ifPresent(x -> productInterestFacade.removeProductInterest(x));

		return "success";
	}

}
