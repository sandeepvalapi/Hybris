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
package com.hybris.training.storefront.security.evaluator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface SecurityTraitEvaluator
{
	/**
	 * Evaluates a security trait.
	 *
	 * @return true if security trait needs to be enforced.
	 */
	boolean evaluate(final HttpServletRequest request, final HttpServletResponse response);
}
