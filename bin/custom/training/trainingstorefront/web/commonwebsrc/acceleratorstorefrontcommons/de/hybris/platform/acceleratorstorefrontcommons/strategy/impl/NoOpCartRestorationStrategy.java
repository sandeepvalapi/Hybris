/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.strategy.impl;

import de.hybris.platform.acceleratorstorefrontcommons.strategy.CartRestorationStrategy;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * Strategy for skipping cart restoration.
 */
public class NoOpCartRestorationStrategy implements CartRestorationStrategy
{
	private static final Logger LOG = Logger.getLogger(NoOpCartRestorationStrategy.class);

	@Override
	public void restoreCart(final HttpServletRequest request)
	{
		LOG.info("Skipping cart restoration");
	}
}
