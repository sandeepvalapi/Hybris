/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.controllers;



import de.hybris.platform.assistedservicestorefront.model.AsmDevicesUsedComponentModel;
import de.hybris.platform.assistedservicestorefront.model.AsmFavoriteColorsComponentModel;

@SuppressWarnings("squid:S1214")
public interface AssistedservicestorefrontControllerConstants
{
	String ADDON_PREFIX = "addon:/assistedservicestorefront/";

	// implement here controller constants used by this extension

	interface Views
	{

		interface Fragments
		{

			interface CustomerListComponent
			{
				String ASM_CUSTOMER_LIST_POPUP = ADDON_PREFIX + "fragments/asmCustomerListComponent";
				String ASM_CUSTOMER_LIST_TABLE = ADDON_PREFIX + "fragments/asmCustomerListTable";
			}

		}
	}

	interface Actions
	{
		interface Cms
		{
			String _Prefix = "/view/"; // NOSONAR
			String _Suffix = "Controller"; // NOSONAR

			String AsmDevicesUsedComponent = _Prefix + AsmDevicesUsedComponentModel._TYPECODE + _Suffix; // NOSONAR
			String AsmFavoriteColorsComponent = _Prefix + AsmFavoriteColorsComponentModel._TYPECODE + _Suffix; // NOSONAR
		}
	}
}
