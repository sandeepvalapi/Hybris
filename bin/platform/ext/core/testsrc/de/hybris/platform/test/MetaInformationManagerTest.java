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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.meta.MetaInformationManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class MetaInformationManagerTest extends HybrisJUnit4TransactionalTest
{
	MetaInformationManager manager;

	private final static byte[] BLOB_ARRAY;

	static
	{
		BLOB_ARRAY = new byte[20000];
		new Random().nextBytes(BLOB_ARRAY);
	}

	@Before
	public void setUp() throws Exception
	{
		manager = jaloSession.getMetaInformationManager();
	}

	@Test
	public void testProperties()
	{
		final String propertyName = "metainfmanager.test";
		try
		{
			final ArrayList propertyValue = new ArrayList();
			assertEquals(null, manager.getProperty(propertyName));
			assertEquals(null, manager.setProperty(propertyName, propertyValue));
			assertEquals(propertyValue, manager.getProperty(propertyName));

			propertyValue.add("entry1");
			assertEquals("hint: set copy-by-value to false", Collections.EMPTY_LIST, manager.getProperty(propertyName));
			assertTrue(propertyValue.containsAll((List) manager.getProperty(propertyName)));
			assertFalse(propertyValue.equals(manager.getProperty(propertyName)));
			assertEquals(Collections.EMPTY_LIST, manager.setProperty(propertyName, propertyValue));
			assertEquals(propertyValue, manager.getProperty(propertyName));

			propertyValue.add("entry2");
			assertFalse(propertyValue.equals(manager.getProperty(propertyName)));
			assertEquals(Arrays.asList(new String[]
			{ "entry1" }), manager.setProperty(propertyName, propertyValue));
			assertEquals(propertyValue, manager.getProperty(propertyName));

			for (int i = 3; i < 10; i++)
			{
				propertyValue.add("entry" + i);
				assertFalse(propertyValue.equals(manager.getProperty(propertyName)));
				manager.setProperty(propertyName, propertyValue);
				assertEquals(propertyValue, manager.getProperty(propertyName));
			}

			assertTrue(manager.getPropertyNames().contains(propertyName));
			assertEquals(propertyValue, manager.setProperty(propertyName, null));
			assertFalse(manager.getPropertyNames().contains(propertyName));
			assertEquals(null, manager.getProperty(propertyName));

			// blob test
			manager.setProperty(propertyName, BLOB_ARRAY);
			assertEquals(BLOB_ARRAY.length, ((byte[]) manager.getProperty(propertyName)).length);
		}
		finally
		{
			try
			{
				manager.setProperty(propertyName, null);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
	}


}
