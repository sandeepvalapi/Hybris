/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CreditCardType declared at extension core.
 */
@SuppressWarnings("PMD")
public enum CreditCardType implements HybrisEnumValue
{
	/**
	 * Generated enum value for CreditCardType.amex declared at extension core.
	 */
	AMEX("amex"),
	/**
	 * Generated enum value for CreditCardType.maestro declared at extension payment.
	 */
	MAESTRO("maestro"),
	/**
	 * Generated enum value for CreditCardType.switch declared at extension payment.
	 */
	SWITCH("switch"),
	/**
	 * Generated enum value for CreditCardType.visa declared at extension core.
	 */
	VISA("visa"),
	/**
	 * Generated enum value for CreditCardType.master declared at extension core.
	 */
	MASTER("master"),
	/**
	 * Generated enum value for CreditCardType.mastercard_eurocard declared at extension payment.
	 */
	MASTERCARD_EUROCARD("mastercard_eurocard"),
	/**
	 * Generated enum value for CreditCardType.diners declared at extension core.
	 */
	DINERS("diners");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CreditCardType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CreditCardType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CreditCardType(final String code)
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
		return SIMPLE_CLASSNAME;
	}
	
}
