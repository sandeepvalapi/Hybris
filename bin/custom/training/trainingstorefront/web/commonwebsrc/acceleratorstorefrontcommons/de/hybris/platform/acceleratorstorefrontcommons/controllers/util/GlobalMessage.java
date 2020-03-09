/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.controllers.util;

import java.io.Serializable;
import java.util.Collection;


public class GlobalMessage implements Serializable
{
	private String code;
	private Collection<Object> attributes; //NOSONAR

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public Collection<Object> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(final Collection<Object> attributes)
	{
		this.attributes = attributes;
	}
}
