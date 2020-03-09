/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleaddon.controllers.pages;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.CommerceEntryGroupUtils;
import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller for select product as bundle component page.
 */
@Controller
@RequestMapping("/entrygroups")
public class BundlePageController extends AbstractSearchPageController
{
	private static final Logger LOG = Logger.getLogger(BundlePageController.class);

	protected static final String NO_RESULTS_CMS_PAGE_ID = "searchEmpty";
	protected static final String BUNDLE_PRODUCT_PAGE = "bundleProduct";

	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "customerLocationService")
	private CustomerLocationService customerLocationService;

	@Resource(name = "commerceEntryGroupUtils")
	private CommerceEntryGroupUtils commerceEntryGroupUtils;

	@Resource
	private CartFacade cartFacade;

	@Resource
	private BundleCartFacade bundleCartFacade;


	/**
	 * Select products for given bundle component.
	 *
	 * @param groupNumber
	 *           groupNumber of a group in cart. The group must be of type BundleGroup.
	 * @param request
	 *           http request
	 * @param model
	 *           page model
	 * @return jsp name
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping("/CONFIGURABLEBUNDLE/{groupNumber}")
	public String editEntryGroup(
			@PathVariable("groupNumber") final Integer groupNumber,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			final HttpServletRequest request, final Model model)
			throws CMSItemNotFoundException
	{
		model.addAttribute("entryGroupNumber", groupNumber);

		final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData;
		try
		{
			searchPageData = getBundleCartFacade().getAllowedProducts(groupNumber, searchQuery, pageableData);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Edit entry group failed", e);
			request.setAttribute("message", e.getMessage());
			return FORWARD_PREFIX + "/404";
		}

		if (searchPageData == null)
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}
		else if (searchPageData.getKeywordRedirectUrl() != null)
		{
			// if the search engine returns a redirect, just
			return REDIRECT_PREFIX + searchPageData.getKeywordRedirectUrl();
		}
		else if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
		{
			model.addAttribute("searchPageData", searchPageData);
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}
		else
		{
			storeContinueUrl(request);
			populateModel(model, searchPageData, ShowMode.Page);
			storeCmsPageInModel(model, getCmsPageService().getPageForId(BUNDLE_PRODUCT_PAGE));
		}
		model.addAttribute("userLocation", getCustomerLocationService().getUserLocation());
		getRequestContextData(request).setSearch(searchPageData);
		if (searchPageData != null)
		{
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getSearchBreadcrumbBuilder().getBreadcrumbs(null, "/CONFIGURABLEBUNDLES/" + groupNumber,
							CollectionUtils.isEmpty(searchPageData.getBreadcrumbs())));
		}

		model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());
		model.addAttribute("userLocation", getCustomerLocationService().getUserLocation());
		model.addAttribute("bundleNavigation", getLeafGroups(groupNumber));
		setUpPageTitle(getPageTitle(), model);

		return getViewForPage(model);
	}

	protected String getPageTitle()
	{
		return getMessageSource().getMessage("bundle.component.name.default", null,
				getI18nService().getCurrentLocale());
	}

	/**
	 * Find leaf nodes of the group tree, that contains group with given number.
	 *
	 * @param groupNumber
	 *           group number to identify tree in cart group trees
	 * @return leaf nodes
	 */
	protected List<EntryGroupData> getLeafGroups(final Integer groupNumber)
	{
		final CartData cart = getCartFacade().getSessionCart();
		final EntryGroupData group = getCommerceEntryGroupUtils().getGroup(cart, groupNumber);
		return getCommerceEntryGroupUtils().getLeaves(group.getRootGroup());
	}

	protected void setUpPageTitle(final String bundleName, final Model model)
	{
		storeContentPageTitleInModel(
				model,
				getPageTitleResolver().resolveContentPageTitle(bundleName));
	}

	protected BundleCartFacade getBundleCartFacade()
	{
		return bundleCartFacade;
	}

	protected SearchBreadcrumbBuilder getSearchBreadcrumbBuilder()
	{
		return searchBreadcrumbBuilder;
	}

	protected CustomerLocationService getCustomerLocationService()
	{
		return customerLocationService;
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	protected CommerceEntryGroupUtils getCommerceEntryGroupUtils()
	{
		return commerceEntryGroupUtils;
	}
}

