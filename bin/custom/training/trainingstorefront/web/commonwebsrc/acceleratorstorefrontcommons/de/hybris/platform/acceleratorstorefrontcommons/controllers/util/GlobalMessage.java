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
