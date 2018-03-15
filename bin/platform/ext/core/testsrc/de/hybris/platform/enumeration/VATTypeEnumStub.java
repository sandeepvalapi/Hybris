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
package de.hybris.platform.enumeration;

import de.hybris.platform.core.HybrisEnumValue;


/**
 * ArticleApprovalStatusEnum - Stub
 */
public enum VATTypeEnumStub implements HybrisEnumValue
{

	/**
	 * enum value for VATType.FULL.
	 */
	FULL("FULL"),
	/**
	 * enum value for VATType.HALF.
	 */
	HALF("HALF"),
	/**
	 * enum value for VATType.NONE.
	 */
	NONE("NONE");


	private final String code;

	/**
	 * Creates a new enum value for this enum type.
	 * 
	 * @param code
	 *           the enum value code
	 */
	private VATTypeEnumStub(final String code)
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
		return "VATType";
	}

}
