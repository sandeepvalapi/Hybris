/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.storefront.web.mvc;

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UrlPathHelper;

/**
 * This implementation overrides the default implementation of Spring framework's {@link UrlPathHelper}
 * so that context path and servlet mapping tricks Spring MVC as if UrlEncoding is not present.
 */
public class AcceleratorUrlPathHelper extends UrlPathHelper
{
   @Override
	public String getContextPath(final HttpServletRequest request)
   {
	   final Object urlEncodingAttributes = request.getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
		return StringUtils.remove(super.getContextPath(request),
				urlEncodingAttributes != null ? urlEncodingAttributes.toString() : "");
	}


	@Override
	public String getPathWithinServletMapping(final HttpServletRequest request)
	{
		if ("".equalsIgnoreCase(super.getServletPath(request)))
		{
			return "/";
		}
		return super.getPathWithinServletMapping(request);
	}

}
