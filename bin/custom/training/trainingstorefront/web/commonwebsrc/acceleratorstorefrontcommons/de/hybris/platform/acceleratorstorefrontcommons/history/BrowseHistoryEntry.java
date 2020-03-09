/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.history;

import java.io.Serializable;


/**
 * Browse history entry data object
 */
public class BrowseHistoryEntry implements Serializable
{
	private String url;
	private String pageTitle;


	public BrowseHistoryEntry(final String url, final String pageTitle)
	{
		this.url = url;
		this.pageTitle = pageTitle;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getPageTitle()
	{
		return pageTitle;
	}

	public void setPageTitle(final String pageTitle)
	{
		this.pageTitle = pageTitle;
	}
}
