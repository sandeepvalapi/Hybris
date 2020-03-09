/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
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
