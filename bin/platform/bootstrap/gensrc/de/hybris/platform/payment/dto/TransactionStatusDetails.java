/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
 * ----------------------------------------------------------------
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
 */
package de.hybris.platform.payment.dto;

public enum TransactionStatusDetails
{

	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.AMOUNTS_MUST_MATCH</code> value defined at extension <code>payment</code>. */
	AMOUNTS_MUST_MATCH , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.AUTHORIZATION_ALREADY_REVERSED</code> value defined at extension <code>payment</code>. */
	AUTHORIZATION_ALREADY_REVERSED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.AUTHORIZATION_ALREADY_SETTLED</code> value defined at extension <code>payment</code>. */
	AUTHORIZATION_ALREADY_SETTLED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.AUTHORIZATION_REJECTED_BY_PSP</code> value defined at extension <code>payment</code>. */
	AUTHORIZATION_REJECTED_BY_PSP , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.AUTHORIZED_AMOUNT_EXCEEDED</code> value defined at extension <code>payment</code>. */
	AUTHORIZED_AMOUNT_EXCEEDED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.BANK_DECLINE</code> value defined at extension <code>payment</code>. */
	BANK_DECLINE , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.CARD_USED_TOO_RECENTLY</code> value defined at extension <code>payment</code>. */
	CARD_USED_TOO_RECENTLY , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.COMMUNICATION_PROBLEM</code> value defined at extension <code>payment</code>. */
	COMMUNICATION_PROBLEM , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.CREDIT_FOR_VOIDED_CAPTURE</code> value defined at extension <code>payment</code>. */
	CREDIT_FOR_VOIDED_CAPTURE , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.CREDIT_LIMIT_REACHED</code> value defined at extension <code>payment</code>. */
	CREDIT_LIMIT_REACHED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.CUSTOMER_BLACKLISTED</code> value defined at extension <code>payment</code>. */
	CUSTOMER_BLACKLISTED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.DUPLICATE_TRANSACTION</code> value defined at extension <code>payment</code>. */
	DUPLICATE_TRANSACTION , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.GENERAL_SYSTEM_ERROR</code> value defined at extension <code>payment</code>. */
	GENERAL_SYSTEM_ERROR , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INACTIVE_OR_INVALID_CARD</code> value defined at extension <code>payment</code>. */
	INACTIVE_OR_INVALID_CARD , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INCORRECT_CARD_NUMBER_OR_TYPE</code> value defined at extension <code>payment</code>. */
	INCORRECT_CARD_NUMBER_OR_TYPE , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INSUFFICIENT_FUNDS</code> value defined at extension <code>payment</code>. */
	INSUFFICIENT_FUNDS , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INVALID_ACCOUNT_NUMBER</code> value defined at extension <code>payment</code>. */
	INVALID_ACCOUNT_NUMBER , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INVALID_CARD_EXPIRATION_DATE</code> value defined at extension <code>payment</code>. */
	INVALID_CARD_EXPIRATION_DATE , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INVALID_CARD_TYPE</code> value defined at extension <code>payment</code>. */
	INVALID_CARD_TYPE , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INVALID_CVN</code> value defined at extension <code>payment</code>. */
	INVALID_CVN , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INVALID_REQUEST</code> value defined at extension <code>payment</code>. */
	INVALID_REQUEST , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INVALID_REQUEST_ID</code> value defined at extension <code>payment</code>. */
	INVALID_REQUEST_ID , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.NO_AUTHORIZATION_FOR_SETTLEMENT</code> value defined at extension <code>payment</code>. */
	NO_AUTHORIZATION_FOR_SETTLEMENT , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.NOT_VOIDABLE</code> value defined at extension <code>payment</code>. */
	NOT_VOIDABLE , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.PROCESSOR_DECLINE</code> value defined at extension <code>payment</code>. */
	PROCESSOR_DECLINE , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.PSP_CONFIGURATION_PROBLEM</code> value defined at extension <code>payment</code>. */
	PSP_CONFIGURATION_PROBLEM , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.REVIEW_NEEDED</code> value defined at extension <code>payment</code>. */
	REVIEW_NEEDED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.STOLEN_OR_LOST_CARD</code> value defined at extension <code>payment</code>. */
	STOLEN_OR_LOST_CARD , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.SUCCESFULL</code> value defined at extension <code>payment</code>. */
	SUCCESFULL , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.THREE_D_SECURE_AUTHENTICATION_REQUIRED</code> value defined at extension <code>payment</code>. */
	THREE_D_SECURE_AUTHENTICATION_REQUIRED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.THREE_D_SECURE_NOT_SUPPORTED</code> value defined at extension <code>payment</code>. */
	THREE_D_SECURE_NOT_SUPPORTED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.TIMEOUT</code> value defined at extension <code>payment</code>. */
	TIMEOUT , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.TRANSACTION_ALREADY_SETTLED_OR_REVERSED</code> value defined at extension <code>payment</code>. */
	TRANSACTION_ALREADY_SETTLED_OR_REVERSED , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.UNKNOWN_CODE</code> value defined at extension <code>payment</code>. */
	UNKNOWN_CODE , 
	/** <i>Generated enum value</i> for <code>TransactionStatusDetails.INVALID_SUBSCRIPTION_ID</code> value defined at extension <code>payment</code>. */
	INVALID_SUBSCRIPTION_ID  

}