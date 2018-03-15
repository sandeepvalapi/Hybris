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
package de.hybris.platform.servicelayer.interceptor;

import de.hybris.platform.core.HybrisEnumValue;


/**
 * GenderEnum - Stub
 */
public enum GenderEnumStub implements HybrisEnumValue
{

	/**
	 * enum value for GenderEnumStub.MALE.
	 */
	MALE("MALE"),
	/**
	 * enum value for GenderEnumStub.FEMALE.
	 */
	FEMALE("FEMALE");

	private final String code;

	/**
	 * Creates a new enum value for this enum type.
	 * 
	 * @param code
	 *           the enum value code
	 */
	private GenderEnumStub(final String code)
	{
		this.code = code.intern();
	}


	/**
	 * Gets the code of this enum value.
	 * 
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}

	/**
	 * Gets the type this enum value belongs to.
	 * 
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return "Gender";
	}

}
