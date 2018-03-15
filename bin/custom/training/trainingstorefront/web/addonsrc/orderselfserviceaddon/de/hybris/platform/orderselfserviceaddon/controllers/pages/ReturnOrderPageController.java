/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.orderselfserviceaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.ordermanagementfacades.returns.OmsReturnFacade;
import de.hybris.platform.ordermanagementfacades.returns.data.ReturnEntryData;
import de.hybris.platform.ordermanagementfacades.returns.data.ReturnRequestData;
import de.hybris.platform.orderselfserviceaddon.forms.OrderEntryReturnForm;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



/**
 * Controller for return order pages
 */
@Controller
@RequestMapping(value = "/my-account/order")
public class ReturnOrderPageController extends AbstractSearchPageController
{
	public static final Logger LOG = Logger.getLogger(ReturnOrderPageController.class);
	public static final String BREADCRUMBS_ATTR = "breadcrumbs";
	public static final String MY_ACCOUNT_ORDERS = "/my-account/orders";
	public static final String REDIRECT_TO_ORDERS_HISTORY_PAGE = REDIRECT_PREFIX + MY_ACCOUNT_ORDERS;
	public static final String RETURN_ORDER_CMS_PAGE = "return-order";
	public static final String RETRUN_CONFIRM_ORDER_CMS_PAGE = "confirm-return-order";
	public static final String MY_ACCOUNT_ORDER = "/my-account/order/";
	public static final String MY_ACCOUNT_RETURNS = "/my-account/returns";

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "omsReturnFacade")
	private OmsReturnFacade omsReturnFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	/*
	 * Display the return order page
	 */
	@RequireHardLogIn
	@RequestMapping(value = "/{orderCode:.*}/returns", method = { RequestMethod.POST, RequestMethod.GET })
	public String showReturnOrderPage(@PathVariable(value = "orderCode") final String orderCode, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException // NOSONAR
	{
		try
		{
			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
			model.addAttribute("orderData", orderDetails);
			model.addAttribute("orderEntryReturnForm", initializeForm(orderDetails));

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_RETURNS,
					getMessageSource().getMessage("text.account.orderHistory", null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_ORDER + orderCode, getMessageSource()
					.getMessage("text.account.order.orderBreadcrumb", new Object[] { orderDetails.getCode() }, "Order {0}",
							getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("#",
					getMessageSource().getMessage("text.account.returnOrder", null, getI18nService().getCurrentLocale()), null));
			model.addAttribute(BREADCRUMBS_ATTR, breadcrumbs);

		}
		catch (final UnknownIdentifierException e)
		{
			return redirectToOrdersHistory(redirectModel);
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(RETURN_ORDER_CMS_PAGE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETURN_ORDER_CMS_PAGE));
		return getViewForPage(model);
	}

	/*
	 * Display the confirm return order page
	 */
	@RequireHardLogIn
	@RequestMapping(value = "/{orderCode:.*}/returns/confirm", method = RequestMethod.POST)
	public String confirmReturnOrderPage(@PathVariable("orderCode") final String orderCode,
			@ModelAttribute("orderEntryReturnForm") final OrderEntryReturnForm orderEntryReturnForm, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		try
		{
			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
			orderEntryReturnForm.getReturnEntryQuantityMap()
					.forEach((entryNumber, qty) -> orderDetails.getEntries().forEach(orderEntryData ->
					{
						// Case of MultiD product
						if (isMultidimensionalEntry(orderEntryData))
						{
							orderEntryData.getEntries().stream()
									.filter(nestedOrderEntry -> nestedOrderEntry.getEntryNumber().equals(entryNumber))
									.forEach(nestedOrderEntryData -> setReturnedItemsPrice(qty, nestedOrderEntryData));
						}
						// Case of non MultiD product
						else
						{
							if (orderEntryData.getEntryNumber().equals(entryNumber))
							{
								setReturnedItemsPrice(qty, orderEntryData);
							}
						}
					}));
			model.addAttribute("orderData", orderDetails);
			model.addAttribute("orderEntryReturnForm", orderEntryReturnForm);
			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_ORDERS,
					getMessageSource().getMessage("text.account.orderHistory", null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_ORDER + orderCode, getMessageSource()
					.getMessage("text.account.order.orderBreadcrumb", new Object[] { orderDetails.getCode() }, "Order {0}",
							getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_ORDER + orderCode + "/returns",
					getMessageSource().getMessage("text.account.returnOrder", null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("#",
					getMessageSource().getMessage("text.account.confirm.returnOrder", null, getI18nService().getCurrentLocale()),
					null));
			model.addAttribute(BREADCRUMBS_ATTR, breadcrumbs);

		}
		catch (final UnknownIdentifierException e)
		{
			return redirectToOrdersHistory(redirectModel);
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(RETRUN_CONFIRM_ORDER_CMS_PAGE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETRUN_CONFIRM_ORDER_CMS_PAGE));
		return getViewForPage(model);
	}

	/*
	 * Submit the confirmed return items to be returned
	 */
	@RequireHardLogIn
	@RequestMapping(value = "/{orderCode:.*}/returns/submit", method = RequestMethod.POST)
	public String submitCancelOrderPage(@PathVariable("orderCode") final String orderCode,
			@ModelAttribute("orderEntryCancelForm") final OrderEntryReturnForm orderEntryReturnForm, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		try
		{
			final OrderData order = orderFacade.getOrderDetailsForCode(orderCode);
			omsReturnFacade.createReturnRequest(prepareReturnRequestData(order, orderEntryReturnForm));
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER,
					getMessageSource().getMessage("text.account.return.success", null, getI18nService().getCurrentLocale()), null);
			return REDIRECT_TO_ORDERS_HISTORY_PAGE;
		}
		catch (final Exception exception) //NOSONAR
		{
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "text.account.return.fail.generic");
			return REDIRECT_TO_ORDERS_HISTORY_PAGE;
		}
	}

	/**
	 * It prepares the {@link ReturnRequestData} object by taking the order and the map of orderentries {@link OrderEntryData} number and returned quantities
	 *
	 * @param order
	 * 		order {@link OrderData} which we want to return
	 * @param orderEntryReturnForm
	 * 		a {@link OrderEntryReturnForm} map of orderentries number and the returned quantities
	 * @return returnRequest {@link ReturnRequestData}
	 */
	protected ReturnRequestData prepareReturnRequestData(final OrderData order, final OrderEntryReturnForm orderEntryReturnForm)
	{
		final ReturnRequestData returnRequest = new ReturnRequestData();
		returnRequest.setOrder(order);
		final List<ReturnEntryData> returnEntries = new ArrayList<>();

		orderEntryReturnForm.getReturnEntryQuantityMap().forEach((key, value) ->
		{
			final ReturnEntryData returnEntry = new ReturnEntryData();
			final OrderEntryData oed = new OrderEntryData();
			oed.setEntryNumber(key);
			returnEntry.setOrderEntry(oed);
			returnEntry.setExpectedQuantity(value);
			returnEntry.setRefundReason(RefundReason.GOODWILL);
			returnEntry.setAction(ReturnAction.HOLD);
			returnEntries.add(returnEntry);
		});
		returnRequest.setReturnEntries(returnEntries);
		returnRequest.setRefundDeliveryCost(false);
		return returnRequest;
	}

	/**
	 * Confirms if the given {@link OrderEntryData} is for multidimensional product
	 *
	 * @param orderEntry
	 * 		the given {@link OrderEntryData}
	 * @return true, if the given {@link OrderEntryData} is for multidimensional product
	 */
	protected boolean isMultidimensionalEntry(final OrderEntryData orderEntry)
	{
		return orderEntry.getProduct().getMultidimensional() != null && orderEntry.getProduct().getMultidimensional() && !orderEntry
				.getEntries().isEmpty();
	}

	/**
	 * Initialize the input form and takes care of the multiD case
	 *
	 * @param orderData
	 * 		The given order{@link OrderData} to be returned
	 * @return initialized form {@link OrderEntryReturnForm} with initial values of 0
	 */
	protected OrderEntryReturnForm initializeForm(final OrderData orderData)
	{
		final OrderEntryReturnForm orderEntryReturnForm = new OrderEntryReturnForm();
		final Map<Integer, Long> returnEntryQuantityMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(orderData.getEntries()))
		{
			orderData.getEntries().stream().filter(this::isMultiD)
					.forEach(oed -> populateMapForNestedEntries(returnEntryQuantityMap, oed));
			orderData.getEntries().stream().filter(oed -> !isMultiD(oed))
					.forEach(oed -> returnEntryQuantityMap.put(oed.getEntryNumber(), 0L));
		}
		orderEntryReturnForm.setReturnEntryQuantityMap(returnEntryQuantityMap);
		return orderEntryReturnForm;
	}


	/**
	 * @param returnEntryQuantityMap
	 * 		The map to populate with entry number and quantity to return
	 * @param orderEntryData
	 * 		The order entry {@link OrderEntryData} that holds a MultiD product
	 */
	protected void populateMapForNestedEntries(final Map<Integer, Long> returnEntryQuantityMap,
			final OrderEntryData orderEntryData)
	{
		if (CollectionUtils.isNotEmpty(orderEntryData.getEntries()))
		{
			orderEntryData.getEntries().forEach(nestedOed -> returnEntryQuantityMap.put(nestedOed.getEntryNumber(), 0L));
		}
	}


	/**
	 * A method that checks if the product associated with this orderEntry is a multi dimensional product
	 *
	 * @param orderEntryData
	 * 		the order entry {@link OrderEntryData}
	 * @return true if the product in the orderEntryData is multiD and false if it is not set or it is not.
	 */
	protected boolean isMultiD(OrderEntryData orderEntryData)
	{
		return orderEntryData.getProduct().getMultidimensional() != null && orderEntryData.getProduct().getMultidimensional();
	}

	/**
	 * Updates the {@link OrderEntryData#returnedItemsPrice} for the given requested return quantity
	 *
	 * @param qty
	 * 		the quantity to be returned from the given {@link OrderEntryData}
	 * @param orderEntryData
	 * 		the {@link OrderEntryData}
	 */
	protected void setReturnedItemsPrice(final Long qty, final OrderEntryData orderEntryData)
	{
		final PriceData returnedItemsPriceData = priceDataFactory
				.create(PriceDataType.BUY, orderEntryData.getBasePrice().getValue().multiply(new BigDecimal(qty)),
						commonI18NService.getCurrentCurrency());
		orderEntryData.setReturnedItemsPrice(returnedItemsPriceData);
	}

	/**
	 * Redirects the flow to the Orders History page and notifies the user with the {@link RedirectAttributes}.
	 *
	 * @param redirectModel
	 * 		the {@link RedirectAttributes} used to display a message to the user
	 * @return View String
	 */
	protected String redirectToOrdersHistory(final RedirectAttributes redirectModel)
	{
		LOG.warn("Attempted to load a order that does not exist or is not visible");
		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "system.error.page.not.found", null);
		return REDIRECT_TO_ORDERS_HISTORY_PAGE;
	}
}
