/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
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
