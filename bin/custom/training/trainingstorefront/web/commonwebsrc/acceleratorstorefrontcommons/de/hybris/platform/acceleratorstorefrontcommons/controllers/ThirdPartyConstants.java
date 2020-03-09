/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.controllers;

public interface ThirdPartyConstants
{
	interface Google // NOSONAR
	{
		String API_KEY_ID = "googleApiKey";
		String ANALYTICS_TRACKING_ID = "google.analytics.tracking.id";
	}

	@Deprecated
	interface Jirafe // NOSONAR
	{
		String API_URL = "jirafe.api.url";
		String API_TOKEN = "jirafe.api.token";
		String APPLICATION_ID = "jirafe.app.id";
		String VERSION = "jirafe.version";
		String DATA_URL = "jirafe.data.url";
		String SITE_ID = "jirafe.site.id";
	}

	interface SeoRobots // NOSONAR
	{
		String META_ROBOTS = "metaRobots";
		String INDEX_FOLLOW = "index,follow";
		String INDEX_NOFOLLOW = "index,nofollow";
		String NOINDEX_FOLLOW = "noindex,follow";
		String NOINDEX_NOFOLLOW = "noindex,nofollow";
	}
}
