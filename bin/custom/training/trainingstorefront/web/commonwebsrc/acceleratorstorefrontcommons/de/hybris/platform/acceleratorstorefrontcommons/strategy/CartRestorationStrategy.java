/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.strategy;

import javax.servlet.http.HttpServletRequest;


/**
 * Strategy for cart restoration.
 *
 */
public interface CartRestorationStrategy
{

	/**
	 * Restore cart.
	 *
	 * @param request
	 *           the request
	 */
	void restoreCart(final HttpServletRequest request);
}
