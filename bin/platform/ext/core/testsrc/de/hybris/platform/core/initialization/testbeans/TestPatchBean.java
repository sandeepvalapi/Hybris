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

import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.core.initialization.SystemSetup;


@SystemSetup(extension = CoreConstants.EXTENSIONNAME)
public class TestPatchBean
{
	@SystemSetup(patch = true)
	public void requiredPatch()
	{
	}

	@SystemSetup(patch = true)
	public void optionalPatch()
	{
	}
}
