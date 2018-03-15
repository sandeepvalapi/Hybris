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
package de.hybris.platform.core;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.SingletonCreator.Creator;

import org.junit.Test;


/**
 * Tests SingletonCreator via delegating methods like Registry.getSingleton(Creator).
 */
@IntegrationTest
public class SingletonCreatorTest extends HybrisJUnit4Test
{
	@Test
	public void testSingletonCreation()
	{
		final Object key = Long.toString(this.hashCode() * System.nanoTime());

		final Integer firstValue = Integer.valueOf((int) System.nanoTime());
		final Creator<Integer> firstCreator = createCreator(key, firstValue);
		assertEquals(firstValue, Registry.getSingleton(firstCreator));

		for (int i = 0; i < 1000; i++)
		{
			final Integer newValue = Integer.valueOf((int) System.nanoTime());
			final Creator<Integer> newCreator = createCreator(key, newValue);
			assertEquals(firstValue, Registry.getSingleton(newCreator));
		}
	}

	/**
	 * See also PLA-10874 which describes a error while replacing.
	 */
	@Test
	public void testReplace()
	{
		final Object ID = "id"; //NOPMD

		final Integer value1 = Integer.valueOf((int) System.nanoTime());

		final Creator<Integer> creator1 = createCreator(ID, value1);
		assertEquals(value1, Registry.getSingleton(creator1));

		final Integer value2 = Integer.valueOf((int) System.nanoTime());
		final Creator<Integer> creator2 = createCreator(ID, value2);
		assertEquals(value1, Registry.getSingleton(creator2)); // should still give value1 since we did not replace anything

		assertEquals(value2, Registry.replaceSingleton(createCreator(ID, value2)));
		assertEquals(value2, Registry.getSingleton(creator2));
		assertEquals(value2, Registry.getSingleton(creator1));
	}

	private static Creator<Integer> createCreator(final Object key, final Integer value)
	{
		return new SingletonCreator.Creator<Integer>()
		{
			@Override
			protected Object getID()
			{
				return key;
			}

			@Override
			protected Integer create() throws Exception
			{
				return value;
			}
		};

	}
}
