/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.controllers;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.commercefacades.customer.CustomerListFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.CustomerListData;
import de.hybris.platform.commercefacades.user.data.UserGroupData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * Controller to handle querying requests for ASM and handling customer lists implementations
 *
 */
@Controller
@RequestMapping("/assisted-service-querying")
public class CustomerListController extends AbstractSearchPageController
{
	private static final Logger LOG = Logger.getLogger(CustomerListController.class);

	private static final String DEFAULT_CUSTOMER_LIST = "defaultList";
	private static final String AVAILABLE_CUSTOMER_LIST = "availableLists";
	private static final String CUSTOMER_LIST_DATA = "customerListData";
	private static final String QUERY = "query";

	@Resource(name = "customerListFacade")
	private CustomerListFacade customerListFacade;

	@Resource(name = "assistedServiceFacade")
	private AssistedServiceFacade assistedServiceFacade;


	/**
	 * Method responsible for getting available customer list for agent and return a popup with the data
	 *
	 * @param model
	 *           model to hold the populated data
	 * @return the popup with list of customers list populated
	 */
	@RequestMapping(value = "/availableCustomerLists", method = RequestMethod.GET)
	public String getCustomersListPopup(final Model model, final HttpServletResponse response)
	{
		if (!assistedServiceFacade.isAssistedServiceAgentLoggedIn())
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			assistedServiceFacade.getAsmSession().setFlashErrorMessage("asm.emulate.error.agent_missed");
			return null;
		}

		final List<UserGroupData> customerLists = customerListFacade.getCustomerListsForEmployee(assistedServiceFacade
				.getAsmSession().getAgent().getUid());

		// Handle paged search results
		if (!CollectionUtils.isEmpty(customerLists))
		{
			model.addAttribute(AVAILABLE_CUSTOMER_LIST, customerLists);
			model.addAttribute(DEFAULT_CUSTOMER_LIST, customerLists.get(0).getUid());
		}

		return AssistedservicestorefrontControllerConstants.Views.Fragments.CustomerListComponent.ASM_CUSTOMER_LIST_POPUP;
	}

	/**
	 * Responsible for getting list of customers based on a customer List UId and handle pagination and sorting of this
	 * list as well
	 *
	 * @param model
	 *           to hold populated data
	 * @param page
	 *           page number in case we have more than 1 page of data
	 * @param showMode
	 *           either to show all or to show pages (default is page)
	 * @param sortCode
	 *           the sort code for the list of customers
	 * @param customerListUid
	 *           the customer list UId to get customers for
	 * @param query
	 *           the query provided by the user to filter the results
	 * @return paginated view with customer data
	 */
	@RequestMapping(value = "/listCustomers", method = RequestMethod.GET)
	public String listPaginatedCustomers(final Model model, @RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "customerListUId", required = false) final String customerListUid,
			@RequestParam(value = "query", required = false) final String query)
	{
		try
		{
			if (!StringUtils.isBlank(customerListUid))
			{
				// Populate customer list data
				final String agentUid = assistedServiceFacade.getAsmSession().getAgent().getUid();
				final CustomerListData customerListData = customerListFacade.getCustomerListForUid(customerListUid,
						agentUid);
				model.addAttribute(CUSTOMER_LIST_DATA, customerListData);
				final Map<String, Object> parametersMap = new HashMap<>();

				// Handle query if enabled
				if (customerListData.isSearchBoxEnabled())
				{
					parametersMap.put(QUERY, query);
					model.addAttribute(QUERY, query);
				}

				// Handle paged search results
				final PageableData pageableData = createPageableData(page, 5, sortCode, showMode);
				final SearchPageData<CustomerData> searchPageData = customerListFacade.getPagedCustomersForCustomerListUID(
						customerListUid, agentUid, pageableData, parametersMap);

				populateModel(model, searchPageData, showMode);
				model.addAttribute(DEFAULT_CUSTOMER_LIST, customerListUid);
			}
			else
			{
				throw new IllegalArgumentException("customerListUid can not be empty!");
			}
		}
		catch (final Exception exp)
		{
			LOG.error(exp, exp);
		}
		return AssistedservicestorefrontControllerConstants.Views.Fragments.CustomerListComponent.ASM_CUSTOMER_LIST_TABLE;
	}

	@Override
	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		model.addAttribute("numberPagesShown", Integer.valueOf(3));
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("isShowAllAllowed", calculateShowAll(searchPageData, showMode));
		model.addAttribute("isShowPageAllowed", calculateShowPaged(searchPageData, showMode));
	}

	@Override
	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(100);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	@Override
	protected Boolean calculateShowAll(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf((showMode != ShowMode.All && //
				searchPageData.getPagination().getTotalNumberOfResults() > searchPageData.getPagination().getPageSize())
				&& isShowAllAllowed(searchPageData));
	}

	@Override
	protected Boolean calculateShowPaged(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean
				.valueOf(showMode == ShowMode.All
						&& (searchPageData.getPagination().getNumberOfPages() > 1 || searchPageData.getPagination().getPageSize() == getMaxSearchPageSize()));
	}
}
