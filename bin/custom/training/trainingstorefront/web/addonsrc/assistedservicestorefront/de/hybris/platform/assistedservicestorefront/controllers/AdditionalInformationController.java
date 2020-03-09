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

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedservicefacades.customer360.AdditionalInformationFrameworkFacade;
import de.hybris.platform.assistedservicefacades.customer360.Fragment;
import de.hybris.platform.assistedservicestorefront.constants.AssistedservicestorefrontConstants;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import de.hybris.platform.util.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.commons.lang.StringUtils;



/**
 *
 * Controller to AIF requests for ASM
 *
 */
@Controller
@RequestMapping(value = "/assisted-service-aif")
public class AdditionalInformationController
{

	private static final String CUSTOMER_360_PAGE = "addon:/assistedservicestorefront/pages/customer360";
	private static final String CUSTOMER_360_FRAGMENT_404_PAGE = "addon:/assistedservicestorefront/fragments/customer360/fragment404";
	private static final String CUSTOMER_360_SECTION = "addon:/assistedservicestorefront/pages/section";
	private static final String SECTION_ID_PARAM_NAME = "sectionId";
	private static final String FRAGMENT_ID_PARAM_NAME = "fragmentId";
	private static final String AIF_AJAX_TIMEOUT = "aif_ajax_timeout";

	@Resource(name = "additionalInformationFrameworkFacade")
	private AdditionalInformationFrameworkFacade additionalInformationFrameworkFacade;

	@Resource
	private AssistedServiceFacade assistedServiceFacade;

	/***
	 * returns list of sections we have configured to display them on frontend, sections are not loaded just section
	 * title
	 *
	 * @param model
	 * @return list of sections to display
	 */
	@RequestMapping(value = "/customer360", method = RequestMethod.GET)
	public String getCustomer360(final Model model, final HttpServletResponse response)
	{
		if (!assistedServiceFacade.isAssistedServiceAgentLoggedIn())
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return CUSTOMER_360_FRAGMENT_404_PAGE;
		}

		model.addAttribute("sections", additionalInformationFrameworkFacade.getSections());
		return CUSTOMER_360_PAGE;
	}

	/***
	 * get the section's fragments and display fragments meta info only like title
	 *
	 * @param model
	 * @param sectionId
	 *           section id to retrieve fragments for
	 * @return section info along with its fragments
	 */
	@RequestMapping(value = "/customer360section", method = RequestMethod.GET)
	public String getCustomer360Section(final Model model,
		@RequestParam("sectionId") final String sectionId, final HttpServletResponse response)
	{
		if (!assistedServiceFacade.isAssistedServiceAgentLoggedIn())
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return CUSTOMER_360_FRAGMENT_404_PAGE;
		}

		model.addAttribute("section", additionalInformationFrameworkFacade.getSection(sectionId));

		model.addAttribute(AIF_AJAX_TIMEOUT, Integer.valueOf(Config.getInt(AssistedservicestorefrontConstants.AIF_TIMEOUT,
				AssistedservicestorefrontConstants.AIF_DEFAULT_TIMEOUT)));

		return CUSTOMER_360_SECTION;
	}

	/**
	 * Method for getting fragment's details, data and JSP renderer as response on GET or POSTs request
	 *
	 * @param model
	 * @param allRequestParams
	 *           all request parameters
	 * @return fragment with populated data and renderer
	 */
	@RequestMapping(value = "/customer360Fragment", method = { RequestMethod.POST, RequestMethod.GET })
	public String getCustomer360Fragment(final Model model,
		 @RequestParam final Map<String, String> allRequestParams, final HttpServletResponse response)
	{
		if (!assistedServiceFacade.isAssistedServiceAgentLoggedIn())
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return CUSTOMER_360_FRAGMENT_404_PAGE;
		}

		final String sectionId = allRequestParams.get(SECTION_ID_PARAM_NAME);
		final String fragmentId = allRequestParams.get(FRAGMENT_ID_PARAM_NAME);

		if (StringUtils.isBlank(fragmentId))
		{
			return CUSTOMER_360_FRAGMENT_404_PAGE;
		}

		final Fragment populatedFragment = additionalInformationFrameworkFacade.getFragment(sectionId, fragmentId,
				allRequestParams);

		if (populatedFragment == null)
		{
			return CUSTOMER_360_FRAGMENT_404_PAGE;
		}

		model.addAttribute("fragmentData", populatedFragment.getData());

		return populatedFragment.getJspPath();
	}
}
