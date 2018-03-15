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
package de.hybris.platform.servicelayer.web;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.GenericFilterBean;


/**
 * DynamicCatalogVersionActivationFilter takes care of activating the catalog versions at runtime.
 */
public class DynamicCatalogVersionActivationFilter extends GenericFilterBean
{
	private static final Logger LOG = Logger.getLogger(DynamicCatalogVersionActivationFilter.class.getName());

	private CatalogVersionService catalogVersionService;

	private CatalogService catalogService;

	/**
	 * Before the filterChain gets processed, DynamicCatalogVersionActivationFilter takes care of activating the catalog
	 * versions properly
	 * 
	 * @param request
	 *           the request
	 * @param response
	 *           the response
	 * @param filterChain
	 *           the filterChain
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException
	{
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
		{
			throw new ServletException("DynamicCatalogVersionActivationFilter just supports HTTP requests");
		}
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final HttpSession httpSession = httpRequest.getSession();

		// skip if catalog versions are already assigned
		final Collection<CatalogVersionModel> catVersions = catalogVersionService.getSessionCatalogVersions();
		if (catVersions.isEmpty())
		{
			final Collection<CatalogVersionModel> versionsToSet = filterByURL(httpRequest,
					catalogVersionService.getAllCatalogVersions());

			assignCatalogVersions(httpRequest, httpSession, versionsToSet);
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Current session already got its catalog versions " + catVersions);
			}
		}

		filterChain.doFilter(httpRequest, httpResponse);
	}

	@Override
	public String toString()
	{
		return "DynamicCatalogVersionProcessor[ hashCode: " + this.hashCode() + " ]";
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	@Required
	public void setCatalogService(final CatalogService catalogService)
	{
		this.catalogService = catalogService;
	}

	private void assignCatalogVersions(final HttpServletRequest request, final HttpSession httpSession,
			final Collection<CatalogVersionModel> versions)
	{
		if (versions == null || versions.isEmpty())
		{
			final CatalogModel def = catalogService.getDefaultCatalog();
			CatalogVersionModel defCatVer = null;
			if (def != null)
			{
				defCatVer = def.getActiveCatalogVersion();
			}
			//try to set the defaultCatalogVersion
			catalogVersionService.setSessionCatalogVersions(defCatVer == null ? Collections.EMPTY_LIST : Collections
					.singletonList(defCatVer));

			if (LOG.isDebugEnabled())
			{
				final String reqURI = request == null ? "<n/a>" : request.getRequestURI();
				LOG.debug("No active versions available for request " + reqURI + "!");
			}
		}
		else
		{
			catalogVersionService.setSessionCatalogVersions(versions);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Active versions now are " + versions);
			}
		}
	}


	private Collection<CatalogVersionModel> filterByURL(final HttpServletRequest request,
			final Collection<CatalogVersionModel> allActiveVersions)
	{
		final Collection<CatalogVersionModel> ret = new ArrayList(allActiveVersions);
		// CORE-3383
		final String requestURL = request.getRequestURL().toString();
		final String requestQuery = request.getQueryString();
		final String requestStr = requestURL + (requestQuery != null && requestQuery.length() > 0 ? "?" + requestQuery : "");
		//
		for (final Iterator it = ret.iterator(); it.hasNext();)
		{
			final CatalogVersionModel catalogVersion = (CatalogVersionModel) it.next();
			final CatalogModel catalog = catalogVersion.getCatalog();
			// filter by URL ?
			final Collection patterns = catalog.getUrlPatterns();
			if (patterns != null && !patterns.isEmpty())
			{
				boolean matched = false;
				for (final Iterator urlIt = patterns.iterator(); !matched && urlIt.hasNext();)
				{
					final String expr = (String) urlIt.next();
					try
					{
						matched = Pattern.matches(expr, requestStr);
					}
					catch (final PatternSyntaxException e)
					{
						LOG.error("Illegal catalog pattern '" + expr + "'");
						matched = false;
					}
				}
				if (!matched)
				{
					it.remove();
				}
				if (LOG.isDebugEnabled())
				{
					if (matched)
					{
						LOG.debug("Request " + requestStr + " matched url patterns " + patterns + ". Added version " + catalogVersion
								+ ".");
					}
					else
					{
						LOG.debug("Request " + requestStr + " did not match url patterns " + patterns + ". Filtered version "
								+ catalogVersion + ".");
					}
				}
			}
			// no, then always add version
			else
			{
				it.remove();
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Hiding catalogversion: " + catalogVersion + " since it is not restricted by URL pattern.");
				}
			}
		}
		return ret;
	}
}
