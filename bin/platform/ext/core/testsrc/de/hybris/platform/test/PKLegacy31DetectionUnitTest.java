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
package de.hybris.platform.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;

import org.junit.Test;


@UnitTest
public class PKLegacy31DetectionUnitTest
{

	@Test
	public void testGetCounterWithoutTenant()
	{
		final PK dummy = PK.createFixedCounterPK(1, 1234);
		final Tenant currentTenant = Registry.getCurrentTenantNoFallback();
		try
		{
			Registry.unsetCurrentTenant();

			dummy.getCounter(); // boom
		}
		finally
		{
			if (currentTenant != null)
			{
				Registry.setCurrentTenant(currentTenant);
			}
		}
	}

}
