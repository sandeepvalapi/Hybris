/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.tags;

import de.hybris.platform.acceleratorcms.component.slot.CMSPageSlotComponentService;
import de.hybris.platform.acceleratorcms.data.CmsPageRequestContextData;
import de.hybris.platform.acceleratorcms.services.CMSPageContextService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.enums.PickupInStoreMode;
import de.hybris.platform.commerceservices.enums.QuoteUserType;
import de.hybris.platform.commerceservices.order.strategies.QuoteUserIdentificationStrategy;
import de.hybris.platform.commerceservices.order.strategies.QuoteUserTypeIdentificationStrategy;
import de.hybris.platform.commerceservices.strategies.PickupStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.JavaScriptUtils;

import com.sap.security.core.server.csi.XSSEncoder;


/**
 * JSP EL Functions. This file contains static methods that are used by JSP EL.
 */
public class Functions
{
	private static final Logger LOG = Logger.getLogger(Functions.class);
	public static final String DEFAULT_HOMEPAGE_URL = "/";

	/**
	 * JSP EL Function to get a primary Image for a Product in a specific format
	 *
	 * @param product
	 *           the product
	 * @param format
	 *           the desired format
	 * @return the image
	 */
	public static ImageData getPrimaryImageForProductAndFormat(final ProductData product, final String format)
	{
		if (product != null && format != null)
		{
			final Collection<ImageData> images = product.getImages();
			if (images != null && !images.isEmpty())
			{
				return getPrimaryImageForTypeAndFormat(images, format);
			}
		}
		return null;
	}

	protected static ImageData getPrimaryImageForTypeAndFormat(final Collection<ImageData> images, final String format)
	{
		for (final ImageData image : images)
		{
			if (ImageDataType.PRIMARY.equals(image.getImageType()) && format.equals(image.getFormat()))
			{
				return image;
			}
		}
		return null;
	}

	/**
	 * JSP EL Function to get an Image for a Store in a specific format
	 *
	 * @param store
	 *           the store
	 * @param format
	 *           the desired image format
	 * @return the image
	 */
	public static ImageData getImageForStoreAndFormat(final PointOfServiceData store, final String format)
	{
		if (store != null && format != null)
		{
			final Collection<ImageData> images = store.getStoreImages();
			if (images != null && !images.isEmpty())
			{
				return getImageForFormat(images, format);
			}
		}
		return null;
	}

	protected static ImageData getImageForFormat(final Collection<ImageData> images, final String format)
	{
		for (final ImageData image : images)
		{
			if (format.equals(image.getFormat()))
			{
				return image;
			}
		}
		return null;
	}

	/**
	 * JSP EL Function to get the URL for a CMSLinkComponent
	 *
	 * @param component
	 *           The Link Component
	 * @return The URL
	 */
	public static String getUrlForCMSLinkComponent(final CMSLinkComponentModel component)
	{
		return getUrlForCMSLinkComponent(component, null, null);
	}

	public static String getUrlForCMSLinkComponent(final CMSLinkComponentModel component,
			final Converter<ProductModel, ProductData> productUrlConverter,
			final Converter<CategoryModel, CategoryData> categoryUrlConverter)
	{
		// Try to get the label for the content page
		final ContentPageModel contentPage = component.getContentPage();
		if (contentPage != null)
		{
			if (contentPage.isHomepage())
			{
				return DEFAULT_HOMEPAGE_URL;
			}
			else
			{
				return contentPage.getLabel();
			}
		}

		final String categoryUrl = getCategoryUrl(component, categoryUrlConverter);
		if (categoryUrl != null)
		{
			return categoryUrl;
		}

		// Try to get the product and build a URL to the product
		final ProductModel product = component.getProduct();
		if (product != null)
		{
			return convertWithProperConverter(productUrlConverter, product);
		}

		// Try to get the URL from the component
		final String url = component.getUrl();
		if (url != null && !url.isEmpty())
		{
			return url;
		}

		return null;
	}

	protected static String getCategoryUrl(final CMSLinkComponentModel component,
			final Converter<CategoryModel, CategoryData> categoryUrlConverter)
	{
		// Try to get the category and build a URL to the category
		final CategoryModel category = component.getCategory();
		if (category != null)
		{
			String categoryUrl;
			if (categoryUrlConverter != null)
			{
				categoryUrl = categoryUrlConverter.convert(category).getUrl();
			}
			else
			{
				categoryUrl = getCategoryUrlConverter(getCurrentRequest()).convert(category).getUrl();
			}
			// append the query string if available in the url on the component
			if (StringUtils.isNotBlank(component.getUrl()) && component.getUrl().indexOf("?") != -1)
			{
				categoryUrl += component.getUrl().substring(component.getUrl().indexOf("?"), component.getUrl().length());
			}
			return categoryUrl;
		}
		return null;
	}

	protected static String convertWithProperConverter(final Converter<ProductModel, ProductData> productUrlConverter,
			final ProductModel product)
	{
		if (productUrlConverter != null)
		{
			return productUrlConverter.convert(product).getUrl();
		}
		else
		{
			return getProductUrlConverter(getCurrentRequest()).convert(product).getUrl();
		}
	}

	public static boolean evaluateRestrictions(final AbstractCMSComponentModel model)
	{
		final HttpServletRequest request = getCurrentRequest();
		final CmsPageRequestContextData cmsPageRequestContextData = getCMSPageContextService(request)
				.getCmsPageRequestContextData(request);
		return getCMSPageSlotComponentService(request).isComponentVisible(cmsPageRequestContextData, model, true);
	}

	protected static Converter<ProductModel, ProductData> getProductUrlConverter(final HttpServletRequest httpRequest)
	{
		return getSpringBean(httpRequest, "productUrlConverter", Converter.class);
	}

	protected static Converter<CategoryModel, CategoryData> getCategoryUrlConverter(final HttpServletRequest httpRequest)
	{
		return getSpringBean(httpRequest, "categoryUrlConverter", Converter.class);
	}

	protected static CMSPageContextService getCMSPageContextService(final HttpServletRequest httpRequest)
	{
		return getSpringBean(httpRequest, "cmsPageContextService", CMSPageContextService.class);
	}

	protected static CMSPageSlotComponentService getCMSPageSlotComponentService(final HttpServletRequest httpRequest)
	{
		return getSpringBean(httpRequest, "cmsPageSlotComponentService", CMSPageSlotComponentService.class);
	}

	/**
	 * Returns the Spring bean with name <code>beanName</code> and of type <code>beanClass</code>.
	 *
	 * @param <T>
	 *           type of the bean
	 * @param httpRequest
	 *           the http request
	 * @param beanName
	 *           name of the bean
	 * @param beanClass
	 *           expected type of the bean
	 * @return the bean matching the given arguments or <code>null</code> if no bean could be resolved
	 */
	public static <T> T getSpringBean(final HttpServletRequest httpRequest, final String beanName, final Class<T> beanClass)
	{
		return RequestContextUtils.findWebApplicationContext(httpRequest, httpRequest.getSession().getServletContext())
				.getBean(beanName, beanClass);
	}

	/**
	 * Test if entry or grouped entry belongs to consumed entry
	 *
	 * @param consumed
	 * @param entry
	 * @return true if consumed entry and entry/grouped entry corresponds to each other otherwise false
	 */
	public static boolean isConsumedByEntry(final PromotionOrderEntryConsumedData consumed, final OrderEntryData entry)
	{
		final Integer consumendEntryNumber = consumed.getOrderEntryNumber();
		if (CollectionUtils.isEmpty(entry.getEntries()))
		{
			return consumendEntryNumber.equals(entry.getEntryNumber());
		}
		else
		{
			return entry.getEntries().stream().anyMatch(e -> e.getEntryNumber().equals(consumendEntryNumber));
		}
	}

	/**
	 * Test if a cart has an applied promotion for the specified entry number.
	 *
	 * @param cart
	 *           the cart
	 * @param entryNumber
	 *           the entry number
	 * @return true if there is an applied promotion for the entry number
	 */
	public static boolean doesAppliedPromotionExistForOrderEntry(final CartData cart, final int entryNumber)
	{
		return cart != null && doesPromotionExistForOrderEntry(cart.getAppliedProductPromotions(), entryNumber);
	}

	/**
	 * Test if a cart has an applied promotion for the specified entry or grouped entry.
	 *
	 * @param cart
	 *           the cart
	 * @param entry
	 *           the order entry
	 * @return true if there is an applied promotion for the entry number
	 */
	public static boolean doesAppliedPromotionExistForOrderEntryOrOrderEntryGroup(final CartData cart, final OrderEntryData entry)
	{
		if (CollectionUtils.isEmpty(entry.getEntries()))
		{
			return doesAppliedPromotionExistForOrderEntry(cart, entry.getEntryNumber().intValue());
		}
		else
		{
			return entry.getEntries().stream()
					.anyMatch(e -> doesAppliedPromotionExistForOrderEntry(cart, e.getEntryNumber().intValue()));
		}
	}

	/**
	 * Test if a cart has an potential promotion for the specified entry number.
	 *
	 * @param cart
	 *           the cart
	 * @param entryNumber
	 *           the entry number
	 * @return true if there is an potential promotion for the entry number
	 */
	public static boolean doesPotentialPromotionExistForOrderEntry(final CartData cart, final int entryNumber)
	{
		return cart != null && doesPromotionExistForOrderEntry(cart.getPotentialProductPromotions(), entryNumber);
	}

	/**
	 * Test if a cart has an potential promotion for the specified entry or entry group.
	 *
	 * @param cart
	 *           the cart
	 * @param entry
	 *           the entry
	 * @return true if there is an potential promotion for the entry or entry group
	 */
	public static boolean doesPotentialPromotionExistForOrderEntryOrOrderEntryGroup(final CartData cart,
			final OrderEntryData entry)
	{
		if (CollectionUtils.isEmpty(entry.getEntries()))
		{
			return doesPotentialPromotionExistForOrderEntry(cart, entry.getEntryNumber().intValue());
		}
		else
		{
			return entry.getEntries().stream()
					.anyMatch(e -> doesPotentialPromotionExistForOrderEntry(cart, e.getEntryNumber().intValue()));
		}
	}

	public static boolean doesPromotionExistForOrderEntry(final List<PromotionResultData> productPromotions, final int entryNumber)
	{
		if (productPromotions != null && !productPromotions.isEmpty())
		{
			final Integer entryNumberToFind = Integer.valueOf(entryNumber);

			//Iterate over all promotion results and return true if any results match the entryNumber
			//Only exit the loop in the event of a match or all results returning false
			for (final PromotionResultData productPromotion : productPromotions)
			{
				if (StringUtils.isNotBlank(productPromotion.getDescription())
						&& doesPromotionExistForOrderEntry(entryNumberToFind, productPromotion))
				{
					return true;
				}
			}
		}
		return false;
	}

	protected static boolean doesPromotionExistForOrderEntry(final Integer entryNumberToFind,
			final PromotionResultData productPromotion)
	{
		final List<PromotionOrderEntryConsumedData> consumedEntries = productPromotion.getConsumedEntries();
		if (consumedEntries != null && !consumedEntries.isEmpty())
		{
			for (final PromotionOrderEntryConsumedData consumedEntry : consumedEntries)
			{
				if (entryNumberToFind.equals(consumedEntry.getOrderEntryNumber()))
				{
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Utility method that encodes given URL
	 *
	 * @param url
	 *           the url to encode
	 * @return encoded URL
	 */
	public static String encodeUrl(final String url)
	{
		try
		{
			return XSSEncoder.encodeURL(url);
		}
		catch (final UnsupportedEncodingException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
			return url;
		}
	}

	/**
	 * Utility method that encodes the given input as HTML
	 *
	 * @param valueToBeEncoded
	 *           the value to encode
	 * @return value encoded as HTML
	 */
	public static String encodeHTML(final String valueToBeEncoded)
	{
		try
		{
			return XSSEncoder.encodeHTML(valueToBeEncoded);
		}
		catch (final UnsupportedEncodingException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
			return valueToBeEncoded;
		}
	}

	/**
	 * Validates that tag is a valid HTML tag name
	 *
	 * @param tag
	 *           HTML tag name
	 * @return tag if valid, div if it is not valid.
	 */
	public static String sanitizeHtmlTagName(final String tag)
	{
		final String defaultTag = "div";
		if (StringUtils.isNotBlank(tag))
		{
			final Pattern pattern = Pattern.compile("[A-Za-z0-9-_:]+");
			final Matcher matcher = pattern.matcher(tag);
			if (matcher.matches())
			{
				return tag;
			}
		}
		return defaultTag;
	}

	/**
	 * Utility method that encodes the given input as HTML
	 *
	 * @param valueToBeEncoded
	 *           the value to encode
	 * @return value encoded as JavaScript
	 */
	public static String encodeJavaScript(final String valueToBeEncoded)
	{
		return JavaScriptUtils.javaScriptEscape(valueToBeEncoded);
	}

	/**
	 * Utility tag function to check if Pickup mode is enabled for the current base store.
	 *
	 * @return boolean
	 */
	public static boolean checkIfPickupEnabledForStore()
	{
		final PickupStrategy pickupStrategy = getSpringBean(getCurrentRequest(), "pickupStrategy", PickupStrategy.class);
		return !PickupInStoreMode.DISABLED.equals(pickupStrategy.getPickupInStoreMode());
	}

	protected static HttpServletRequest getCurrentRequest()
	{
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	public static boolean isExtensionInstalled(final String extensionName)
	{
		return ExtensionManager.getInstance().isExtensionInstalled(extensionName);
	}

	public static boolean isQuoteUserSalesRep()
	{
		final UserModel userModel = getQuoteUserIdentificationStrategy(getCurrentRequest()).getCurrentQuoteUser();
		final Optional<QuoteUserType> quoteUserTypeOptional = getQuoteUserTypeIdentificationStrategy(getCurrentRequest())
				.getCurrentQuoteUserType(userModel);
		return quoteUserTypeOptional.isPresent() && QuoteUserType.SELLER.equals(quoteUserTypeOptional.get());
	}

	public static boolean isQuoteUserSellerApprover()
	{
		final UserModel userModel = getQuoteUserIdentificationStrategy(getCurrentRequest()).getCurrentQuoteUser();
		final Optional<QuoteUserType> quoteUserTypeOptional = getQuoteUserTypeIdentificationStrategy(getCurrentRequest())
				.getCurrentQuoteUserType(userModel);
		return quoteUserTypeOptional.isPresent() && QuoteUserType.SELLERAPPROVER.equals(quoteUserTypeOptional.get());
	}

	protected static QuoteUserTypeIdentificationStrategy getQuoteUserTypeIdentificationStrategy(
			final HttpServletRequest httpRequest)
	{
		return getSpringBean(httpRequest, "quoteUserTypeIdentificationStrategy", QuoteUserTypeIdentificationStrategy.class);
	}

	protected static QuoteUserIdentificationStrategy getQuoteUserIdentificationStrategy(final HttpServletRequest httpRequest)
	{
		return getSpringBean(httpRequest, "quoteUserIdentificationStrategy", QuoteUserIdentificationStrategy.class);
	}

	/**
	 * Returns normalized code for selection data code
	 *
	 * @param code
	 * @return code normalized according to replace condition
	 */
	public static String normalizedCode(final String code)
	{
		return StringUtils.isEmpty(code) ? "" : code.replaceAll("\\W", "_");
	}

	/**
	 * JSP EL Function to get an image for a Product in a specific format based on the product code.
	 *
	 * @param product
	 *           The product.
	 * @param productCode
	 *           The desired product code.
	 * @param format
	 *           The desired format.
	 * @return The image.
	 */
	public static ImageData getImageForProductCode(final ProductData product, final String productCode, final String format)
	{
		if (product != null && productCode != null && format != null)
		{
			final ImageData image = getImageData(product, productCode, format);
			if (image != null)
			{
				return image;
			}
		}
		return null;
	}

	protected static ImageData getImageData(final ProductData product, final String productCode, final String format)
	{
		int selectedIndex = 0;

		for (int i = 1; i <= product.getCategories().size(); i++)
		{
			int j = 0;
			final List<VariantMatrixElementData> theMatrix;

			if (i == 1)
			{
				theMatrix = product.getVariantMatrix();
			}
			else
			{
				theMatrix = product.getVariantMatrix().get(selectedIndex).getElements();
				selectedIndex = 0;
			}

			if (CollectionUtils.isNotEmpty(theMatrix)
					&& theMatrix.get(selectedIndex).getParentVariantCategory().getHasImage().booleanValue())
			{
				for (final VariantMatrixElementData matrixElementData : theMatrix)
				{
					if (matrixElementData.getVariantOption().getVariantOptionQualifiers() != null
							&& productCode.equals(matrixElementData.getVariantOption().getCode()))
					{
						for (final VariantOptionQualifierData variantOption : matrixElementData.getVariantOption()
								.getVariantOptionQualifiers())
						{
							if (format.equals(variantOption.getImage().getFormat()))
							{
								return variantOption.getImage();
							}
						}
						selectedIndex = j;
					}

					j++;
				}
			}
		}
		return null;
	}

	/**
	 * Check if array contains instance.
	 *
	 * @param stringArr
	 *           the string array
	 * @param string
	 *           the string instance
	 * @return true, if contains
	 */
	public static boolean arrayContainsInstance(final String[] stringArr, final String string)
	{
		return ArrayUtils.contains(stringArr, string);
	}
}
