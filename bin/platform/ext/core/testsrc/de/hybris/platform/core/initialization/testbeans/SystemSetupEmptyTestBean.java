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
package de.hybris.platform.core.initialization.testbeans;

import de.hybris.platform.core.initialization.SystemSetup;


@SystemSetup(extension = SystemSetupEmptyTestBean.NOTHIN_TEST_EXTENSION)
public class SystemSetupEmptyTestBean
{
	public static final String NOTHIN_TEST_EXTENSION = "nothin_extension";

	public void doNothing()
	{
		//nope
	}
}
