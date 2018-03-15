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
package com.hybris.training.storefront.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;


public class DefaultRequestDataProcessor implements RequestDataValueProcessor
{
	@Override
	public String processAction(final HttpServletRequest request, final String action, final String httpMethod )
	{
		return action;
	}

	@Override
	public String processFormFieldValue(final HttpServletRequest request, final String name, final String value, final String type)
	{
		return value;
	}

	@Override
	public Map<String, String> getExtraHiddenFields(final HttpServletRequest request)
	{
		return new HashMap<String, String>();
	}

	@Override
	public String processUrl(final HttpServletRequest request, final String url)
	{
		return url;
	}

}
