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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.deliveryzone.jalo.Zone;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryModeManager;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class ZoneTest extends HybrisJUnit4Test
{
	Zone zone;
	Country country;

	ZoneDeliveryModeManager manager;

	@Before
	public void setUp() throws Exception
	{
		manager = ZoneDeliveryModeManager.getInstance();
		assertNotNull(zone = manager.createZone("z"));
		assertNotNull(country = C2LManager.getInstance().createCountry("c"));
	}

	@Test
	public void testTransaction()
	{
		Collection countries;

		final Transaction tx = Transaction.current();
		tx.begin();
		try
		{
			countries = zone.getCountries();
			assertTrue("expected [] but got " + countries, countries.isEmpty());
			zone.addToCountries(country);
			countries = zone.getCountries();
			assertTrue("expected [" + country + "]b ut got " + countries, countries.size() == 1 && countries.contains(country));
		}
		finally
		{
			tx.rollback();
		}


		countries = zone.getCountries();
		assertTrue("expected [] but got " + countries, countries.isEmpty());
	}
}
