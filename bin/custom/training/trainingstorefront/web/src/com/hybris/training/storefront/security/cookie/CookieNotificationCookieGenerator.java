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
package com.hybris.training.storefront.security.cookie;


/**
 * CookieGenerator for CookieNotification overlay.
 *
 */
public class CookieNotificationCookieGenerator extends EnhancedCookieGenerator
{
	private static final String COOKIE_NOTIFICATION = "cookie-notification";

	@Override
	public String getCookieName()
	{
		return COOKIE_NOTIFICATION;
	}
}
