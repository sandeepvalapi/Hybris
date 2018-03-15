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

import static junit.framework.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


/**
 * Simulate save event for a country, especially saving a region there.
 * 
 * 
 */
@IntegrationTest
public class SaveCountryTest extends HybrisJUnit4TransactionalTest
{
	Country country;
	Region region;

	@Before
	public void setUp() throws Exception
	{
		country = jaloSession.getC2LManager().createCountry("aCountry");
		assertNotNull(country);
		region = country.addNewRegion("aRegion");
		assertNotNull(region);
	}

	@Test
	public void testSave() throws Exception
	{
		// this is, what GenericItemChip.performSave does
		final Map attributeValues = new HashMap();
		attributeValues.put("active", Boolean.FALSE);
		attributeValues.put("isocode", "aCountry");
		attributeValues.put("regions", Arrays.asList(new Object[]
		{ region }));

		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(null);

		for (final Iterator i = attributeValues.entrySet().iterator(); i.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) i.next();
			country.setAttribute(ctx, (String) entry.getKey(), (Serializable) entry.getValue());
		}
	}

}
