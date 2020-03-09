/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationaddon.controllers;

/**
 */
public interface StocknotificationaddonControllerConstants
{
	String ADDON_PREFIX = "addon:/stocknotificationaddon/";

	interface Pages
	{
		String AddNotificationPage = ADDON_PREFIX + "pages/addStockNotification";
		String AddNotificationFromInterestPage = ADDON_PREFIX + "pages/addStockNotificationFromInterest";
	}
}
