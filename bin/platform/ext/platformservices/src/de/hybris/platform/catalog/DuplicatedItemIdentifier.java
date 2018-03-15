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
package de.hybris.platform.catalog;

/**
 * Value object representing count of duplicated item of given type.
 */
public class DuplicatedItemIdentifier
{
	private final String composedType;
	private final String code;
	private final int count;

	public DuplicatedItemIdentifier(final String composedType, final String code, final int count)
	{
		this.composedType = composedType;
		this.code = code;
		this.count = count;
	}

	public String getComposedType()
	{
		return composedType;
	}

	public String getCode()
	{
		return code;
	}

	public int getCount()
	{
		return count;
	}

	@Override
	public String toString()
	{
		return "DuplicatedItemIdentifier{" + "composedType='" + composedType + '\'' + ", code='" + code + '\'' + ", count=" + count
				+ '}';
	}
}
