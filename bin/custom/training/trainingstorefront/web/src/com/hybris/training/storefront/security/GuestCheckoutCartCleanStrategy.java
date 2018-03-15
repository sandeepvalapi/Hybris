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
package com.hybris.training.storefront.security;

import javax.servlet.http.HttpServletRequest;


/**
 * A strategy for clearing unwanted saved data from the cart for guest checkout.
 *
 */
public interface GuestCheckoutCartCleanStrategy
{

	/**
	 * Checks whether the request's page is to be skipped (e.g. checkout URL).
	 *
	 */
	boolean checkWhetherURLContainsSkipPattern(final HttpServletRequest request);

	/**
	 * Removes the delivery address, delivery mode, payment info from the session cart, if the guest user moves away from
	 * checkout pages.
	 *
	 */
	void cleanGuestCart(final HttpServletRequest request);
}
