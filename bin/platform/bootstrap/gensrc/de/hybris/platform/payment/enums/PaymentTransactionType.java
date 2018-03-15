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
package de.hybris.platform.payment.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum PaymentTransactionType declared at extension payment.
 */
@SuppressWarnings("PMD")
public enum PaymentTransactionType implements HybrisEnumValue
{
	/**
	 * Generated enum value for PaymentTransactionType.AUTHORIZATION declared at extension payment.
	 */
	AUTHORIZATION("AUTHORIZATION"),
	/**
	 * Generated enum value for PaymentTransactionType.REVIEW_DECISION declared at extension commerceservices.
	 */
	REVIEW_DECISION("REVIEW_DECISION"),
	/**
	 * Generated enum value for PaymentTransactionType.CAPTURE declared at extension payment.
	 */
	CAPTURE("CAPTURE"),
	/**
	 * Generated enum value for PaymentTransactionType.PARTIAL_CAPTURE declared at extension payment.
	 */
	PARTIAL_CAPTURE("PARTIAL_CAPTURE"),
	/**
	 * Generated enum value for PaymentTransactionType.REFUND_FOLLOW_ON declared at extension payment.
	 */
	REFUND_FOLLOW_ON("REFUND_FOLLOW_ON"),
	/**
	 * Generated enum value for PaymentTransactionType.REFUND_STANDALONE declared at extension payment.
	 */
	REFUND_STANDALONE("REFUND_STANDALONE"),
	/**
	 * Generated enum value for PaymentTransactionType.CANCEL declared at extension payment.
	 */
	CANCEL("CANCEL"),
	/**
	 * Generated enum value for PaymentTransactionType.CREATE_SUBSCRIPTION declared at extension payment.
	 */
	CREATE_SUBSCRIPTION("CREATE_SUBSCRIPTION"),
	/**
	 * Generated enum value for PaymentTransactionType.UPDATE_SUBSCRIPTION declared at extension payment.
	 */
	UPDATE_SUBSCRIPTION("UPDATE_SUBSCRIPTION"),
	/**
	 * Generated enum value for PaymentTransactionType.GET_SUBSCRIPTION_DATA declared at extension payment.
	 */
	GET_SUBSCRIPTION_DATA("GET_SUBSCRIPTION_DATA"),
	/**
	 * Generated enum value for PaymentTransactionType.DELETE_SUBSCRIPTION declared at extension payment.
	 */
	DELETE_SUBSCRIPTION("DELETE_SUBSCRIPTION");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PaymentTransactionType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "PaymentTransactionType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private PaymentTransactionType(final String code)
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
