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
package de.hybris.platform.order.strategies.paymentinfo;

import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Credit card number - oriented helper.
 */
public interface CreditCardNumberHelper
{

	/**
	 * Returns a string representation of masked credit card number. After 'normalizing' the submitted credit card
	 * number, all characters, except for the last 4, will be replaced by '*'
	 * <p/>
	 * <b>Sample:</b> 4111-1111-1111-1111 -> 4111111111111111 -> ************1111
	 * 
	 * @param cn
	 */
	String maskCreditCardNumber(String cn);

	/**
	 * Validates Credit card number according to the given card type. I.e, when {@link CreditCardType#MASTER} is given as
	 * 
	 * <code>type<code>, the method checks if the submitted MASTER number is valid and performs a luhn test. Note: The submitted credit
	 * card number will be normalized at first!
	 * 
	 * @param cardNumber
	 *           card Number to validate
	 * @param type
	 *           one of {@link CreditCardType} enum value
	 */
	boolean isValidCardNumber(String cardNumber, CreditCardType type) throws BusinessException;

	/**
	 * Removes all non-numeric characters <b>Sample:</b> 4111-1111-1111-1111 -> 4111111111111111.
	 * 
	 * @param cardNumber
	 *           the credit card number
	 * @return normalized credit card number
	 */
	String normalizeCreditCardNumber(final String cardNumber);
}
