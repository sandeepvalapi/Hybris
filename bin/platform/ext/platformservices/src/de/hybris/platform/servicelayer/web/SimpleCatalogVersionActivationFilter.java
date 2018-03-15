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

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.filter.GenericFilterBean;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;


/**
 * SimpleCatalogVersionActivationFilter assures that the configured catalog versions are set as session catalog
 * versions. <br/>
 * <b>Configuration parameters:</b><br/>
 * 
 * <li><b>activeCatalogVersions:</b> String specifying the catalog version which should be set as session
 * catalogversion, format is : catalogId:catalogVersion,catalogId2:catalogVersion2.
 * (E.g.hwcatalog:Online,clothescatalog:Online in order to activate the online versions of HWCatalog and ClothesCatalog)
 * <br/> <li><b>onlySetOnce:</b> Set to true if order to set the session catalogVersions only once for a http session.
 */
public class SimpleCatalogVersionActivationFilter extends GenericFilterBean
{
	private static final Logger LOG = Logger.getLogger(SimpleCatalogVersionActivationFilter.class.getName());

	private CatalogVersionService catalogVersionService;

	private String activeCatalogVersions;

	private Map<String, String> sessionCatalogVersionMap = null;
	private boolean onlySetOnce;

	public static final String SESSION_CATALOG_VERSIONS_CONFIGURED = "SimpleCatalogVersionActivationFilter.executed";

	/**
	 * default constructor
	 */
	public SimpleCatalogVersionActivationFilter()
	{
		this.onlySetOnce = true;
	}

	/**
	 * Stores the configured catalogVersions locally. At this point no activation yet.
	 */
	@Override
	public void afterPropertiesSet() throws ServletException
	{
		super.afterPropertiesSet();
		final String sessionCatalogVersions = this.activeCatalogVersions;

		if (!Strings.isNullOrEmpty(sessionCatalogVersions))
		{
			sessionCatalogVersionMap = new HashMap();
			for (final String catalogVersionSplit : Splitter.on(",").split(sessionCatalogVersions))
			{
				final Iterator<String> iterator = Splitter.on(":").split(catalogVersionSplit).iterator();
				final String catalogId = iterator.next();
				final String catalogVersionName = iterator.next();

				sessionCatalogVersionMap.put(catalogId, catalogVersionName);
			}
		}

	}

	/**
	 * Before the filterChain gets processed,
	 * {@link SimpleCatalogVersionActivationFilter#activateCatalogVersions(HttpSession)} gets called.
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
			throw new ServletException("SimpleCatalogVersionActivationFilter just supports HTTP requests");
		}
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;

		activateCatalogVersions(httpRequest.getSession());

		filterChain.doFilter(httpRequest, httpResponse);
	}

	/**
	 * Sets the configured CatalogVersions as session catalogversions for the specified httpSession. Before trying to set
	 * the configured session catalogversions this methods checks if there is at least one catalogversion configured and
	 * if the onlySetOnce flag is set to true, if the catalogversions have not been set for this httpSession.
	 * 
	 * @param httpSession
	 *           the httpSession which is used to set the marker for the onlySetOnce flag to ensure the session
	 *           catalogversions are only set once for the specified httpSession
	 * 
	 */
	protected void activateCatalogVersions(final HttpSession httpSession)
	{
		//if there are configured session catalogversions which need to be set configured
		if (!sessionCatalogVersionMap.isEmpty()
				&& (!onlySetOnce || onlySetOnce && httpSession.getAttribute(SESSION_CATALOG_VERSIONS_CONFIGURED) == null))
		{
			final Set<CatalogVersionModel> catalogVersions = new HashSet();
			for (final Map.Entry<String, String> catalogVersionEntry : sessionCatalogVersionMap.entrySet())
			{
				try
				{
					final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogVersionEntry.getKey(),
							catalogVersionEntry.getValue());
					catalogVersions.add(catalogVersion);
				}
				catch (final Exception exp)
				{
					LOG.error(
							"Error while getting CatalogVersion '" + catalogVersionEntry.getKey() + ":" + catalogVersionEntry.getValue()
									+ "'!", exp);
				}
			}
			catalogVersionService.setSessionCatalogVersions(catalogVersions);
			if (onlySetOnce)
			{
				httpSession.setAttribute(SESSION_CATALOG_VERSIONS_CONFIGURED, "true");
			}
		}
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public void setOnlySetOnce(final boolean onlySetOnce)
	{
		this.onlySetOnce = onlySetOnce;
	}

	public void setActiveCatalogVersions(final String activeCatalogVersions)
	{
		this.activeCatalogVersions = activeCatalogVersions;
	}

	@Override
	public String toString()
	{
		return "SimpleCatalogVersionProcessor[ onlySetOnce: " + onlySetOnce + ", session catalog versions: "
				+ sessionCatalogVersionMap + ", hashCode: " + this.hashCode() + " ]";
	}

}
