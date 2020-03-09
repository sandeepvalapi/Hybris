/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.breadcrumb;

/**
 * Breadcrumb piece data object.
 */
public class Breadcrumb
{
	private String url;
	private String name;
	private String linkClass;

	private String categoryCode;

	public Breadcrumb(final String url, final String name, final String linkClass)
	{
		this.url = url;
		this.name = name;
		this.linkClass = linkClass;
	}

	public Breadcrumb(final String url, final String name, final String linkClass, final String categoryCode)
	{
		this(url, name, linkClass);

		this.categoryCode = categoryCode;
	}


	public String getUrl()
	{
		return url;
	}

	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getLinkClass()
	{
		return linkClass;
	}

	public void setLinkClass(final String linkClass)
	{
		this.linkClass = linkClass;
	}

	public String getCategoryCode()
	{
		return categoryCode;
	}

	public void setCategoryCode(final String categoryCode)
	{
		this.categoryCode = categoryCode;
	}
}
