/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.customerinterestsaddon.controllers.pages;

import de.hybris.platform.acceleratorfacades.futurestock.FutureStockFacade;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestRelationData;
import de.hybris.platform.customerinterestsfacades.productinterest.ProductInterestFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Scope("tenant")
@RequestMapping("/my-account/my-interests")
public class MyInterestsPageController extends AbstractSearchPageController
{
	private static final String MY_INTERESTS_CMS_PAGE = "my-interests";
	private static final String BREADCRUMBS_ATTR = "breadcrumbs";
	private static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "{productCode:.*}";
	private static final String NOTIFICATION_TYPE_PATH_VARIABLE_PATTERN = "{notificationType:.*}";
	private static final String NOTIFICATION_URL_PREFIX = "notification.url.";
	private static final String SORT_BY_NAME_ASC = "byNameAsc";
	private static final String SORT_BY_NAME_DESC = "byNameDesc";

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "productInterestFacade")
	private ProductInterestFacade productInterestFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "mediaService")
	private MediaService mediaService;

	@Resource(name = "futureStockFacade")
	private FutureStockFacade futureStockFacade;

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String getCustomerInterests(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "byNameAsc", required = false) final String sortCode, final Model model)
					throws CMSItemNotFoundException
	{
		final PageableData pageableData = createPageableData(page, 5, sortCode, showMode);
		final List<ProductInterestRelationData> sortedProductInterests = getProductInterestFacade()
				.getProductsByCustomerInterests(pageableData);

		sortedProductInterests.stream().forEach(productInterestRelation->{
			final ProductData product = productInterestRelation.getProduct();
			product.setFutureStocks(getFutureStockFacade().getFutureAvailability(product.getCode()));
		});

		final int total = getProductInterestFacade().getProductsCountByCustomerInterests(pageableData);
		final SearchPageData<ProductInterestRelationData> pagedProductInterestRelationData = new SearchPageData<ProductInterestRelationData>();
		pagedProductInterestRelationData.setResults(sortedProductInterests);
		pagedProductInterestRelationData.setPagination(createPagination(pageableData, total));
		final List<SortData> result = buildSorts(sortCode);

		pagedProductInterestRelationData.setSorts(result);
		populateModel(model, pagedProductInterestRelationData, showMode);

		storeCmsPageInModel(model, getContentPageForLabelOrId(MY_INTERESTS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_INTERESTS_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs("text.account.myInterests"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/show/" + NOTIFICATION_TYPE_PATH_VARIABLE_PATTERN + "/"
			+ PRODUCT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String show(@PathVariable("productCode") final String encodedProductCode,
			@PathVariable("notificationType") final String notificationType, final HttpServletRequest request,
			final HttpServletResponse response, final Model model) throws IOException
	{
		final String urlKey = NOTIFICATION_URL_PREFIX + notificationType;
		return REDIRECT_PREFIX + getConfigurationService().getConfiguration().getString(urlKey) + "/"
				+ decodeWithScheme(encodedProductCode, UTF_8);
	}

	@RequestMapping(value = "/removeall/" + PRODUCT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public void removeAllInterestForProduct(@PathVariable("productCode") final String encodedProductCode,
			final HttpServletRequest request, final HttpServletResponse response, final Model model)
					throws IOException, CMSItemNotFoundException
	{
		getProductInterestFacade().removeProductInterestByProduct(decodeWithScheme(encodedProductCode, UTF_8));
	}

	protected ResourceBreadcrumbBuilder getAccountBreadcrumbBuilder()
	{
		return accountBreadcrumbBuilder;
	}

	protected ProductInterestFacade getProductInterestFacade()
	{
		return productInterestFacade;
	}

	@Override
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	protected FutureStockFacade getFutureStockFacade()
	{
		return futureStockFacade;
	}

	protected PaginationData createPagination(final PageableData pageableData, final int total)
	{
		final PaginationData paginationData = new PaginationData();
		paginationData.setPageSize(pageableData.getPageSize());
		paginationData.setSort(pageableData.getSort());
		paginationData.setTotalNumberOfResults(total);

		// Calculate the number of pages
		paginationData.setNumberOfPages(
				(int) Math.ceil(((double) paginationData.getTotalNumberOfResults()) / paginationData.getPageSize()));

		// Work out the current page, fixing any invalid page values
		paginationData.setCurrentPage(Math.max(0, Math.min(paginationData.getNumberOfPages(), pageableData.getCurrentPage())));

		return paginationData;
	}


	protected List<SortData> buildSorts(final String sortCode)
	{
		final List<SortData> result = new ArrayList<SortData>(1);
		final SortData sortDataByNameAsc = new SortData();
		sortDataByNameAsc.setCode(SORT_BY_NAME_ASC);
		sortDataByNameAsc.setSelected(SORT_BY_NAME_ASC.equals(sortCode) ? true : false);
		final SortData sortDataByNameDesc = new SortData();
		sortDataByNameDesc.setCode(SORT_BY_NAME_DESC);
		sortDataByNameDesc.setSelected(SORT_BY_NAME_DESC.equals(sortCode) ? true : false);
		result.add(sortDataByNameDesc);
		result.add(sortDataByNameAsc);
		return result;
	}

}
