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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.util.SerializationUtils;


/**
 *
 */
public final class SessionCloneTestUtils
{

	private SessionCloneTestUtils()
	{
		// don't allow object creation
	}

	public static void assertClonedContextAttributesEqual(final Map<String, Object> original, final Map<String, Object> serialized)
	{
		// we cannot assume that all values can compared via equals - therefore we must check more carefully
		for (final Map.Entry<String, Object> originalEntry : original.entrySet())
		{
			final String originalKey = originalEntry.getKey();
			final Object originalValue = originalEntry.getValue();

			assertTrue(serialized.containsKey(originalKey));

			final Object clonedValue = serialized.get(originalKey);

			// if we find two values that are not equal we must at least assure that
			// these two have the same class and there is no equals() method other than
			// the one from Object - in that case they cannot be equal since serialization
			// always creates a new instance!
			if (!isSameOrEqual(originalValue, clonedValue))
			{
				assertSameClassNoEquals(originalValue, clonedValue);
			}
		}
	}

	// test of two objects have the same class and there is equals() method declared on it
	// except the one on java.lang.Object
	public static void assertSameClassNoEquals(final Object o1, final Object o2)
	{
		assertNotNull(o1);
		assertNotNull(o2);

		assertEquals(o1.getClass(), o2.getClass());

		try
		{
			final Method equalsMethod = o1.getClass().getMethod("equals", Object.class);
			assertEquals(Object.class, equalsMethod.getDeclaringClass());
		}
		catch (final SecurityException e)
		{
			fail(e.getMessage());
		}
		catch (final NoSuchMethodException e)
		{
			fail(e.getMessage());
		}
	}


	public static boolean isSameOrEqual(final Object o1, final Object o2)
	{
		return o1 == o2 || o1 != null && o1.equals(o2);
	}

	public static <T extends Object> T cloneViaSerialization(final T source)
	{
		return (T) deserialize(serialize(source));
	}

	public static byte[] serialize(final Object source)
	{
		return SerializationUtils.serialize(source);
	}

	public static Object deserialize(final byte[] bytes)
	{
		return SerializationUtils.deserialize(bytes);
	}
}
