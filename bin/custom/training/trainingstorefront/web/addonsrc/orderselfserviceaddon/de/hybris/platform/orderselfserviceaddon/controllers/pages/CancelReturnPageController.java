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
 *
 */
package de.hybris.platform.orderselfserviceaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordermanagementfacades.returns.OmsReturnFacade;
import de.hybris.platform.ordermanagementfacades.returns.data.CancelReturnRequestData;
import de.hybris.platform.ordermanagementfacades.returns.data.ReturnEntryData;
import de.hybris.platform.ordermanagementfacades.returns.data.ReturnRequestData;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import javax.annotation.Resource;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for cancel return page
 */
@Controller
@RequestMapping(value = "/my-account/returns")
public class CancelReturnPageController extends AbstractSearchPageController
{
	protected static final String BREADCRUMBS_ATTR = "breadcrumbs";
	protected static final String MY_ACCOUNT_RETURNS = "/my-account/returns/";
	protected static final String REDIRECT_TO_RETURNS_HISTORY_PAGE = REDIRECT_PREFIX + MY_ACCOUNT_RETURNS;
	protected static final String CANCEL_RETURN_CMS_PAGE = "cancel-return";
	protected static final String RETURN_HISTORY_LABEL = "text.account.returnHistory";
	protected static final String RETURN_REQUEST_LABEL = "Return Request {0}";
	protected static final String RETURN_BREADCRUMB_LABEL = "text.account.returns.returnBreadcrumb";
	protected static final String CANCEL_RETURN_LABEL = "text.account.return.cancelReturn";

	private static final Logger LOG = Logger.getLogger(CancelReturnPageController.class);

	@Resource(name = "omsReturnFacade")
	private OmsReturnFacade omsReturnFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	/**
	 * Displays the cancel return page
	 *
	 * @param returnCode
	 * 		the {@link de.hybris.platform.returns.model.ReturnRequestModel#CODE}
	 * @param model
	 * 		the {@link Model} to which page attributes are added
	 * @param redirectModel
	 * 		the {@link RedirectAttributes}
	 * @return View String
	 * @throws CMSItemNotFoundException
	 * 		if the CMS content is not found
	 */
	@RequireHardLogIn
	@RequestMapping(value = "{returnCode:.*}/cancel", method = { RequestMethod.POST, RequestMethod.GET })
	public String showCancelReturnPage(@PathVariable(value = "returnCode") final String returnCode, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException // NOSONAR
	{
		final ReturnRequestData returnRequestDetails = getOmsReturnFacade().getReturnForReturnCode(returnCode);

		if (isUserValidForOrder(returnRequestDetails.getOrder()))
		{
			try
			{

				returnRequestDetails.getReturnEntries().forEach(this::populateReturnEntry);
				model.addAttribute("returnRequestData", returnRequestDetails);

				final List<Breadcrumb> breadcrumbs = getAccountBreadcrumbBuilder().getBreadcrumbs(null);
				breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_RETURNS,
						getMessageSource().getMessage(RETURN_HISTORY_LABEL, null, getI18nService().getCurrentLocale()), null));
				breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_RETURNS + returnCode, getMessageSource()
						.getMessage(RETURN_BREADCRUMB_LABEL, new Object[] { returnRequestDetails.getCode() }, RETURN_REQUEST_LABEL,
								getI18nService().getCurrentLocale()), null));
				breadcrumbs.add(new Breadcrumb("#",
						getMessageSource().getMessage(CANCEL_RETURN_LABEL, null, getI18nService().getCurrentLocale()), null));
				model.addAttribute(BREADCRUMBS_ATTR, breadcrumbs);
			}
			catch (final UnknownIdentifierException e) //NOSONAR
			{
				LOG.warn("Attempted to load a order that does not exist or is not visible");
				GlobalMessages
						.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "system.error.page.not.found", null);
				return REDIRECT_TO_RETURNS_HISTORY_PAGE;
			}

			storeCmsPageInModel(model, getContentPageForLabelOrId(CANCEL_RETURN_CMS_PAGE));
			model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CANCEL_RETURN_CMS_PAGE));
			return getViewForPage(model);
		}
		else
		{
			LOG.warn("Attempted to load a cancel request that is not linked to the requesting user");
			GlobalMessages
					.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "text.account.return.cancel.fail.generic");
			return REDIRECT_TO_RETURNS_HISTORY_PAGE;

		}
	}

	/**
	 * Submits the return cancellation request
	 *
	 * @param returnCode
	 * 		the {@link de.hybris.platform.returns.model.ReturnRequestModel#CODE}
	 * 		the {@link Model} to which page attributes are added
	 * @param redirectModel
	 * 		the {@link RedirectAttributes}
	 * @return View String
	 * @throws CMSItemNotFoundException
	 * 		if the CMS content is not found
	 */
	@RequireHardLogIn
	@RequestMapping(value = "/{returnCode:.*}/cancel/submit", method = RequestMethod.POST)
	public String submitCancelReturnPage(@PathVariable(value = "returnCode") final String returnCode, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final ReturnRequestData returnRequestDetails = getOmsReturnFacade().getReturnForReturnCode(returnCode);
		if (isUserValidForOrder(returnRequestDetails.getOrder()))
		{
			final CancelReturnRequestData cancelReturnRequestData = new CancelReturnRequestData();
			cancelReturnRequestData.setCode(returnCode);
			cancelReturnRequestData.setCancelReason(CancelReason.CUSTOMERREQUEST);

			try
			{
				getOmsReturnFacade().cancelReturnRequest(cancelReturnRequestData);
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER,
						getMessageSource().getMessage("text.account.return.cancel.success", null, getI18nService().getCurrentLocale()),
						null);
				return REDIRECT_TO_RETURNS_HISTORY_PAGE;
			}
			catch (final UnknownIdentifierException | IllegalArgumentException e) //NOSONAR
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"text.account.return.cancel.fail.generic");
				return REDIRECT_TO_RETURNS_HISTORY_PAGE;
			}
		}
		else
		{
			LOG.warn("Attempted to submit a cancel that is not linked to the requesting user");
			GlobalMessages
					.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "text.account.return.cancel.fail.generic");
			return REDIRECT_TO_RETURNS_HISTORY_PAGE;

		}
	}

	/**
	 * Populates the given {@link ReturnEntryData} with its associated {@link ProductData}
	 *
	 * @param returnEntryData
	 * 		the {@link ReturnEntryData} which is to be populated
	 */
	protected void populateReturnEntry(final ReturnEntryData returnEntryData)
	{
		final ProductModel product = getProductService().getProductForCode(returnEntryData.getOrderEntry().getProduct().getCode());
		final ProductData productData = getProductConverter().convert(product);
		returnEntryData.getOrderEntry().setProduct(productData);
	}

	/**
	 * Validates if the current {@link CustomerData} matches the user in the {@link OrderData} and that it is part of the same {@link BaseStoreModel}
	 *
	 * @param orderData
	 * 		the {@link OrderData}
	 * @return true if the order is tied to the current customer and the current base store.
	 */
	protected boolean isUserValidForOrder(final OrderData orderData)
	{
		return orderData.getUser().getUid().equals(getUser().getUid()) && orderData.getStore()
				.equals(getBaseStoreService().getCurrentBaseStore().getUid());
	}

	protected OmsReturnFacade getOmsReturnFacade()
	{
		return omsReturnFacade;
	}

	protected ResourceBreadcrumbBuilder getAccountBreadcrumbBuilder()
	{
		return accountBreadcrumbBuilder;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}
}
