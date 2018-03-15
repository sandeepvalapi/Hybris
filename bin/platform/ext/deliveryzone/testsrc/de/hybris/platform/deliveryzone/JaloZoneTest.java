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
package de.hybris.platform.deliveryzone;


import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.deliveryzone.constants.ZoneDeliveryModeConstants;
import de.hybris.platform.deliveryzone.jalo.Zone;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryModeManager;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class JaloZoneTest extends HybrisJUnit4TransactionalTest
{
	ZoneDeliveryModeManager zoneDeliveryModeManager = null;

	@Before
	public void setUp() throws Exception
	{
		zoneDeliveryModeManager = (ZoneDeliveryModeManager) jaloSession.getExtensionManager().getExtension(ZoneDeliveryModeConstants.EXTENSIONNAME);
	}

	@Test
	public void testUniqueCode() throws Exception
	{
		assertNotNull(zoneDeliveryModeManager.createZone("test"));
		try
		{
			assertNotNull(zoneDeliveryModeManager.createZone("test"));
			fail("expected ConsistencyCheckException but second 'test' zone could be created");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine here
		}
	}

	@Test
	public void testBug1844() throws Exception
	{
		Zone zone;
		Country country1, country2;
		assertNotNull(zone = zoneDeliveryModeManager.createZone("test"));
		assertNotNull(country1 = jaloSession.getC2LManager().createCountry("c1"));
		assertNotNull(country2 = jaloSession.getC2LManager().createCountry("c2"));
		// z -> { c1 }
		zone.addToCountries(country1);
		assertCollection(Collections.singleton(country1), zone.getCountries());
		// z -> { c1, c2 }
		zone.addToCountries(country2);
		assertCollection(Arrays.asList(new Object[]
		{ country1, country2 }), zone.getCountries());
		// z -> { c1, c2 }
		zone.addToCountries(country1);
		// z -> { c1, c2 }
		zone.addToCountries(country2);
		assertCollection(Arrays.asList(new Object[]
		{ country1, country2 }), zone.getCountries());
		zone.setCountries(new LinkedHashSet(Arrays.asList(new Object[]
		{ country1, country1, country1, country1 })));
		assertCollection(Collections.singleton(country1), zone.getCountries());
	}
}
