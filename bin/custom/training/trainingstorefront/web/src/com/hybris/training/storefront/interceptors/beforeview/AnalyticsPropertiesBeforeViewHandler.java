/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorservices.config.HostConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.config.ConfigIntf;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;


public class AnalyticsPropertiesBeforeViewHandler implements BeforeViewHandler
{
	@Resource(name = "hostConfigService")
	private HostConfigService hostConfigService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	// Listener - listens to changes on the frontend and update the MapCache.
	private ConfigIntf.ConfigChangeListener cfgChangeListener;

	private static Map<String, String> analyticsPropertiesMapCache = new HashMap<>();

	private static final String ANALYTICS_TRACKING_ID = "googleAnalyticsTrackingId";
	private static final String GOOGLE_PREFIX = "googleAnalyticsTrackingId";

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		// Create the change listener and register it to listen when the config properties are changed in the platform
		if (cfgChangeListener == null)
		{
			registerConfigChangeLister();
		}
		final String serverName = request.getServerName();
		// Add config properties for google analytics
		addHostProperty(serverName, modelAndView, ThirdPartyConstants.Google.ANALYTICS_TRACKING_ID, ANALYTICS_TRACKING_ID);
	}

	protected class ConfigChangeListener implements ConfigIntf.ConfigChangeListener
	{
		@Override
		public void configChanged(final String key, final String newValue)
		{
			// Config Listener listen to changes on the platform config and updates the cache.
			if (key.startsWith(GOOGLE_PREFIX))
			{
				analyticsPropertiesMapCache.remove(key);
				analyticsPropertiesMapCache.put(key, newValue);
			}
		}
	}

	protected void registerConfigChangeLister()
	{
		final ConfigIntf config = Registry.getMasterTenant().getConfig();
		cfgChangeListener = new ConfigChangeListener();
		config.registerConfigChangeListener(cfgChangeListener);
	}

	protected void addHostProperty(final String serverName, final ModelAndView modelAndView, final String configKey,
			final String modelKey)
	{
		/*
		 * Changes made to cache the google analytics properties files in a HashMap. The first time the pages are accessed
		 * the values are read from the properties file & written on to a cache and the next time onwards it is accessed
		 * from the cache.
		 */
		if (analyticsPropertiesMapCache.get(configKey) == null)
		{
			analyticsPropertiesMapCache.put(configKey, hostConfigService.getProperty(configKey, serverName));
		}
		modelAndView.addObject(modelKey, analyticsPropertiesMapCache.get(configKey));
	}
}
