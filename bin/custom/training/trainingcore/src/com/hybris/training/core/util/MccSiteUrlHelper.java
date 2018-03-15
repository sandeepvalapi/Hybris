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
package com.hybris.training.core.util;

import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.Registry;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Helper bean for generating the MCC site links for the supported websites
 */
public class MccSiteUrlHelper
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MccSiteUrlHelper.class);

	private CMSSiteService cmsSiteService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	private SiteChannelValidationStrategy siteChannelValidationStrategy;

	// Called from BeanShell by MCC
	public static Map<String, String> getAllSitesAndUrls()
	{
		final MccSiteUrlHelper mccSiteUrlHelper = Registry.getApplicationContext().getBean("mccSiteUrlHelper",
				MccSiteUrlHelper.class);
		return mccSiteUrlHelper.getSitesAndUrls();
	}

	public Map<String, String> getSitesAndUrls()
	{
		final Map<String, String> siteToUrl = new TreeMap<String, String>();

		for (final CMSSiteModel cmsSiteModel : getCmsSiteService().getSites())
		{
			final String url = getSiteUrl(cmsSiteModel);
			if (url != null && !url.isEmpty() && getSiteChannelValidationStrategy().validateSiteChannel(cmsSiteModel.getChannel()))
			{
				siteToUrl.put(cmsSiteModel.getName(), url);
			}
		}

		return siteToUrl;
	}

	protected String getSiteUrl(final CMSSiteModel cmsSiteModel)
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(cmsSiteModel, false, "/");
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	@Required
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Required
	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	protected SiteChannelValidationStrategy getSiteChannelValidationStrategy()
	{
		return siteChannelValidationStrategy;
	}

	@Required
	public void setSiteChannelValidationStrategy(final SiteChannelValidationStrategy siteChannelValidationStrategy)
	{
		this.siteChannelValidationStrategy = siteChannelValidationStrategy;
	}
}
