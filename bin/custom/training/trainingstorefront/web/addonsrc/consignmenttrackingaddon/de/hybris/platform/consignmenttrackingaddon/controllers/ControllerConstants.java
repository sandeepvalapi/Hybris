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
package de.hybris.platform.consignmenttrackingaddon.controllers;

/**
 */
public interface ControllerConstants
{
	interface Views
	{
		String _AddonPrefix = "addon:/consignmenttrackingaddon/";

		interface Pages
		{

			interface Consignment
			{
				String TrackPackagePage = _AddonPrefix + "pages/consignment/trackPackage";
			}
		}
	}
}
