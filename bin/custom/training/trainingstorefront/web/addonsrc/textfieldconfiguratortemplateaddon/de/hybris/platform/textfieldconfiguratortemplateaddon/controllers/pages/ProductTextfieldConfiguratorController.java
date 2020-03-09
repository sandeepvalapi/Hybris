/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.textfieldconfiguratortemplateaddon.controllers.pages;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ConfigureForm;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.QuoteFacade;
import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartParameterData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.quote.data.QuoteData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.textfieldconfiguratortemplateaddon.controllers.TextFieldConfigurationValidator;
import de.hybris.platform.textfieldconfiguratortemplateaddon.forms.TextFieldConfigurationForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProductTextfieldConfiguratorController extends AbstractPageController
{
	private static final String TYPE_MISMATCH_ERROR_CODE = "typeMismatch";
	public static final String PRODUCT_CONFIGURATOR_PAGE = "addon:/textfieldconfiguratortemplateaddon/pages/productConfiguratorPage";
	public static final String ENTRY_CONFIGURATOR_PAGE = "addon:/textfieldconfiguratortemplateaddon/pages/cartEntryConfiguratorPage";
	private static final String ENTRY_READ_ONLY_PAGE = "addon:/textfieldconfiguratortemplateaddon/pages/readOnlyEntryConfiguratorPage";
	public static final String TEXTFIELDCONFIGURATOR_TYPE = "TEXTFIELD";
	public static final String PAGE_LABEL = "configure" + TEXTFIELDCONFIGURATOR_TYPE;
	private static final String MODEL_ATTR_DOCUMENT_CODE = "documentCode";
	private static final String MODEL_ATTR_RETURN_DOCUMENT_TYPE = "returnDocumentType";
	private static final String MODEL_ATTR_ENTRY_NUMBER = "entryNumber";

	private static final Logger LOGGER = Logger.getLogger(ProductTextfieldConfiguratorController.class);

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "quoteFacade")
	private QuoteFacade quoteFacade;

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "saveCartFacade")
	private SaveCartFacade saveCartFacade;

	@Resource(name = "productBreadcrumbBuilder")
	private ProductBreadcrumbBuilder productBreadcrumbBuilder;

	@Resource(name = "textFieldConfigurationValidator")
	private TextFieldConfigurationValidator textFieldConfigurationValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder)
	{
		if (binder.getTarget() != null && TextFieldConfigurationForm.class.isAssignableFrom(binder.getTarget().getClass()))
		{
			binder.setValidator(getTextFieldConfigurationValidator());
		}
	}

	@RequestMapping(value = "/**/p/{productCode}/configuratorPage/" + TEXTFIELDCONFIGURATOR_TYPE, method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String productConfigurator(@PathVariable("productCode") final String encodedProductCode, final Model model,
			final ConfigureForm configureForm) throws CMSItemNotFoundException
	{
		final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
		storePageData(productCode, getProductFacade().getConfiguratorSettingsForCode(productCode), model);
		model.addAttribute("qty", configureForm.getQty());
		return PRODUCT_CONFIGURATOR_PAGE;
	}

	@RequestMapping(value = "/**/p/{productCode}/configure/" + TEXTFIELDCONFIGURATOR_TYPE, method = RequestMethod.POST)
	public String addToCart(@PathVariable("productCode") final String encodedProductCode, final Model model,
			@ModelAttribute("foo") @Valid final TextFieldConfigurationForm form, final BindingResult bindingErrors,
			final HttpServletRequest request, final RedirectAttributes redirectModel)
	{
		final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
		boolean err = false;
		if (bindingErrors.hasErrors())
		{
			bindingErrors.getAllErrors().forEach(
					error -> GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, error.getCode()));
			err = true;
		}
		else
		{
			final long qty = form.getQuantity();
			try
			{
				final CartModificationData cartModification = cartFacade.addToCart(productCode, qty);
				if (cartModification == null)
				{
					throw new CommerceCartModificationException("Null cart modification");
				}
				if (cartModification.getQuantityAdded() > 0)
				{
					cartFacade.updateCartEntry(enrichOrderEntryWithConfigurationData(form, cartModification.getEntry()));
					model.addAttribute("quantity", cartModification.getQuantityAdded());
					model.addAttribute("entry", cartModification.getEntry());
				}

				if (cartModification.getQuantityAdded() == 0L)
				{
					err = true;
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
				}
				else if (cartModification.getQuantityAdded() < qty)
				{
					err = true;
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
				}
			}
			catch (final CommerceCartModificationException ex)
			{
				LOGGER.error(ex.getMessage(), ex);
				err = true;
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "basket.error.occurred");
			}
		}

		if (err)
		{
			return getConfigurePageRedirectPath(productCode);
		}
		model.addAttribute("product",
				productFacade.getProductForCodeAndOptions(productCode, Collections.singletonList(ProductOption.BASIC)));
		return REDIRECT_PREFIX + "/cart";
	}

	@RequestMapping(value = "/cart/{entryNumber}/configuration/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String editConfigurationInEntry(@PathVariable(MODEL_ATTR_ENTRY_NUMBER) final int entryNumber, final Model model)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		final CartData cart = cartFacade.getSessionCart();
		final OrderEntryData entry = getAbstractOrderEntry(entryNumber, cart);
		model.addAttribute(MODEL_ATTR_ENTRY_NUMBER, entryNumber);
		storePageData(entry.getProduct().getCode(), entry.getConfigurationInfos(), model);
		return ENTRY_CONFIGURATOR_PAGE;
	}

	@RequestMapping(value = "/cart/{entryNumber}/configuration/" + TEXTFIELDCONFIGURATOR_TYPE, method = RequestMethod.POST)
	public String updateConfigurationInEntry(@PathVariable(MODEL_ATTR_ENTRY_NUMBER) final int entryNumber, final Model model,
			@ModelAttribute("foo") @Valid final TextFieldConfigurationForm form, final BindingResult bindingErrors,
			final HttpServletRequest request, final RedirectAttributes redirectModel) throws CommerceCartModificationException
	{
		if (bindingErrors.hasErrors())
		{
			bindingErrors.getAllErrors().forEach(
					error -> GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, error.getCode()));
			return REDIRECT_PREFIX + request.getServletPath();
		}
		final CartData cart = cartFacade.getSessionCart();
		final OrderEntryData entry = getAbstractOrderEntry(entryNumber, cart);
		cartFacade.updateCartEntry(enrichOrderEntryWithConfigurationData(form, entry));
		model.addAttribute("product", productFacade.getProductForCodeAndOptions(entry.getProduct().getCode(),
				Collections.singletonList(ProductOption.BASIC)));
		model.addAttribute("quantity", entry.getQuantity());
		model.addAttribute("entry", entry);
		return REDIRECT_PREFIX + "/cart";
	}

	@RequestMapping(value = "/my-account/my-quotes/{quoteCode}/{entryNumber}/configurationDisplay/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String displayConfigurationInQuoteEntry(@PathVariable("quoteCode") final String quoteCode,
			@PathVariable(MODEL_ATTR_ENTRY_NUMBER) final int entryNumber, final Model model)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		final QuoteData quote = quoteFacade.getQuoteForCode(quoteCode);
		final OrderEntryData entry = getAbstractOrderEntry(entryNumber, quote);
		model.addAttribute(MODEL_ATTR_ENTRY_NUMBER, entryNumber);
		model.addAttribute(MODEL_ATTR_DOCUMENT_CODE, quoteCode);
		model.addAttribute(MODEL_ATTR_RETURN_DOCUMENT_TYPE, "my-quotes");
		storePageData(entry.getProduct().getCode(), entry.getConfigurationInfos(), model);
		return ENTRY_READ_ONLY_PAGE;
	}

	@RequestMapping(value = "/my-account/order/{orderCode}/{entryNumber}/configurationDisplay/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String displayConfigurationInOrderEntry(@PathVariable("orderCode") final String orderCode,
			@PathVariable(MODEL_ATTR_ENTRY_NUMBER) final int entryNumber, final Model model)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		final OrderData order = orderFacade.getOrderDetailsForCode(orderCode);
		final OrderEntryData entry = getAbstractOrderEntry(entryNumber, order);
		model.addAttribute(MODEL_ATTR_ENTRY_NUMBER, entryNumber);
		model.addAttribute(MODEL_ATTR_DOCUMENT_CODE, orderCode);
		model.addAttribute(MODEL_ATTR_RETURN_DOCUMENT_TYPE, "order");
		storePageData(entry.getProduct().getCode(), entry.getConfigurationInfos(), model);
		return ENTRY_READ_ONLY_PAGE;
	}

	@RequestMapping(value = "/my-account/saved-carts/{cartCode}/{entryNumber}/configurationDisplay/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String displayConfigurationInSavedCartEntry(@PathVariable("cartCode") final String cartCode,
			@PathVariable(MODEL_ATTR_ENTRY_NUMBER) final int entryNumber, final Model model)
			throws CMSItemNotFoundException, CommerceCartModificationException, CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(cartCode);
		final CommerceSaveCartResultData commerceSaveCartResultData = saveCartFacade.getCartForCodeAndCurrentUser(parameters);
		final OrderEntryData entry = getAbstractOrderEntry(entryNumber, commerceSaveCartResultData.getSavedCartData());
		model.addAttribute(MODEL_ATTR_ENTRY_NUMBER, entryNumber);
		model.addAttribute(MODEL_ATTR_DOCUMENT_CODE, cartCode);
		model.addAttribute(MODEL_ATTR_RETURN_DOCUMENT_TYPE, "saved-carts");
		storePageData(entry.getProduct().getCode(), entry.getConfigurationInfos(), model);
		return ENTRY_READ_ONLY_PAGE;
	}


	protected void storePageData(final String productCode, final List<ConfigurationInfoData> configuration, final Model model)
			throws CMSItemNotFoundException
	{
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, productBreadcrumbBuilder.getBreadcrumbs(productCode));
		final Set<ProductOption> options = new HashSet<>(Arrays.asList(ProductOption.VARIANT_FIRST_VARIANT, ProductOption.BASIC,
				ProductOption.URL, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
				ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES, ProductOption.PRICE_RANGE,
				ProductOption.DELIVERY_MODE_AVAILABILITY));

		final ProductData productData = getProductFacade().getProductForCodeAndOptions(productCode, options);
		model.addAttribute("product", productData);
		model.addAttribute("pageType", PageType.PRODUCT.name());
		final ContentPageModel pageModel = getContentPageForLabelOrId(PAGE_LABEL);
		storeCmsPageInModel(model, pageModel);
		model.addAttribute("configurations", configuration);
	}

	protected OrderEntryData getAbstractOrderEntry(final int entryNumber, final AbstractOrderData abstractOrder)
			throws CommerceCartModificationException
	{
		final List<OrderEntryData> entries = abstractOrder.getEntries();
		if (entries == null)
		{
			throw new CommerceCartModificationException("Cart is empty");
		}
		try
		{
			return entries.stream().filter(e -> e != null).filter(e -> e.getEntryNumber() == entryNumber).findAny().get();
		}
		catch (final NoSuchElementException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw new CommerceCartModificationException("Cart entry #" + entryNumber + " does not exist");
		}
	}

	protected OrderEntryData enrichOrderEntryWithConfigurationData(final TextFieldConfigurationForm form,
			final OrderEntryData orderEntryData)
	{
		final List<ConfigurationInfoData> configurationInfoDataList = new ArrayList<>();
		if (form != null && form.getConfigurationsKeyValueMap() != null)
		{
			for (final Map.Entry<ConfiguratorType, Map<String, String>> item : form.getConfigurationsKeyValueMap().entrySet())
			{
				item.getValue().entrySet().stream().map(formEntry -> {
					final ConfigurationInfoData configurationInfoData = new ConfigurationInfoData();
					configurationInfoData.setConfigurationLabel(formEntry.getKey());
					configurationInfoData.setConfigurationValue(formEntry.getValue());
					configurationInfoData.setConfiguratorType(item.getKey());
					configurationInfoData.setStatus(ProductInfoStatus.SUCCESS);
					return configurationInfoData;
				}).forEach(configurationInfoDataList::add);
			}
		}
		orderEntryData.setConfigurationInfos(configurationInfoDataList);
		return orderEntryData;
	}

	protected String getConfigurePageRedirectPath(final String productCode)
	{
		return String.format("%s/p/%s/configuratorPage/%s", REDIRECT_PREFIX, urlEncode(productCode), TEXTFIELDCONFIGURATOR_TYPE);
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	protected TextFieldConfigurationValidator getTextFieldConfigurationValidator()
	{
		return textFieldConfigurationValidator;
	}
}
