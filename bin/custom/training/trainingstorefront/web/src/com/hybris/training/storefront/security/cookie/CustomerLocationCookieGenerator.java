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
package com.hybris.training.storefront.security.cookie;

import de.hybris.platform.site.BaseSiteService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class CustomerLocationCookieGenerator extends EnhancedCookieGenerator
{
	public static final String LOCATION_SEPARATOR = "|";
	public static final String LATITUDE_LONGITUDE_SEPARATOR = ",";

	private BaseSiteService baseSiteService;

	@Override
	public String getCookieName()
	{
		return StringUtils.deleteWhitespace(getBaseSiteService().getCurrentBaseSite().getUid()) + "-customerLocation";
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

}
