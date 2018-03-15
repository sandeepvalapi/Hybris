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
import de.hybris.platform.util.collections.CaseInsensitiveStringMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.junit.Assert;
import org.junit.Test;

import com.bethecoder.ascii_table.ASCIITable;


@UnitTest
public class CaseInsensitiveStringMapTest
{
	@Test
	public void testCaseInsensitivity()
	{
		final Map<String, String> testMap = new CaseInsensitiveStringMap<String>();

		testMap.put("FOO", "bar");
		Assert.assertEquals("bar", testMap.get("FOO"));
		Assert.assertEquals("bar", testMap.get("fOO"));
		Assert.assertEquals("bar", testMap.get("foo"));
		Assert.assertEquals("bar", testMap.get("FOo"));
		Assert.assertNull(testMap.get("bar"));
		Assert.assertNull(testMap.get("FOOO"));

		testMap.put("", "xxx");
		Assert.assertEquals("xxx", testMap.get(""));

		testMap.put("\u00df", "sz");
		Assert.assertEquals("sz", testMap.get("\u00df"));

		testMap.put("\u00dc", "UE");
		Assert.assertEquals("UE", testMap.get("\u00dc")); // upper UE
		Assert.assertEquals("UE", testMap.get("\u00fc")); // lower UE

		Assert.assertTrue(testMap.containsKey("FOO"));
		Assert.assertTrue(testMap.containsKey("foo"));
		Assert.assertFalse(testMap.containsKey("FOOO"));
		Assert.assertTrue(testMap.containsKey(""));
		Assert.assertTrue(testMap.containsKey("\u00df"));
		Assert.assertTrue(testMap.containsKey("\u00dc"));
		Assert.assertTrue(testMap.containsKey("\u00fc"));
	}

	@Test
	public void testKeySet()
	{
		final Set<String> keys = new HashSet<String>(//
				Arrays.asList("AAA", "BBB", "CCC", "ddd", "x", "y", "\u00df", "\u00dc", ""));

		final Set<String> wrongKeys = new HashSet<String>(//
				Arrays.asList("aaa", "bbb", "ccc", "ddd", "x", "y", "\u00df", "\u00dc", ""));

		final Map<String, String> testMap = new CaseInsensitiveStringMap<String>();

		for (final String key : keys)
		{
			testMap.put(key, key);
		}

		final Set<String> keySet = testMap.keySet();
		Assert.assertEquals(keys, keySet);
		Assert.assertFalse(wrongKeys.equals(keySet));
		for (final String key : keys)
		{
			Assert.assertTrue(keySet.contains(key));
			Assert.assertTrue("didn't match '" + key.toLowerCase() + "' within " + keySet, keySet.contains(key.toLowerCase()));
			if (!"\u00df".equals(key)) // upper case ZS becomes SS which isn't working any way
			{
				Assert.assertTrue("didn't match '" + key.toUpperCase() + "' within " + keySet, keySet.contains(key.toUpperCase()));
			}
		}
	}

	@Test
	public void testValues()
	{
		final Set<String> values = new HashSet<String>(//
				Arrays.asList("AAA", "BBB", "CCC", "ddd", "x", "y", "\u00df", "\u00dc", ""));

		final Set<String> wrongValues = new HashSet<String>(//
				Arrays.asList("aaa", "bbb", "ccc", "ddd", "x", "y", "\u00df", "\u00dc", ""));

		final Map<String, String> testMap = new CaseInsensitiveStringMap<String>();

		for (final String v : values)
		{
			testMap.put(v, v);
		}

		Assert.assertTrue(testMap.containsValue("AAA"));
		Assert.assertFalse(testMap.containsValue("aaa"));

		Assert.assertEquals(values, new HashSet<String>(testMap.values()));
		Assert.assertFalse(wrongValues.equals(new HashSet<String>(testMap.values())));
	}

	@Test
	public void testEntrySet()
	{
		final Set<String> keys = new HashSet<String>(//
				Arrays.asList("AAA", "BBB", "CCC", "ddd", "x", "y", "\u00df", "\u00dc", ""));

		final Map<String, String> testMap = new CaseInsensitiveStringMap<String>();

		for (final String key : keys)
		{
			testMap.put(key, key);
		}

		final Set<Entry<String, String>> entrySet = testMap.entrySet();
		Assert.assertEquals(keys.size(), entrySet.size());

		for (final Map.Entry<String, String> e : entrySet)
		{
			Assert.assertTrue(keys.contains(e.getKey()));
			Assert.assertTrue(e.getKey().equals(e.getValue()));
		}
	}




	@Test
	public void testReadPerformance()
	{
		final int size = 1000;
		final int gets = 10 * 1000 * size;
		final List<String> keys = generateKeys(size);
		final List<String> keysCaseShuffled = shuffleCase(keys);
		final long seed = System.nanoTime();

		final CaseInsensitiveMap map1 = new CaseInsensitiveMap(size);
		fillMap(map1, keys);

		final long time1 = testReadPerformance(keysCaseShuffled, map1, gets, seed);
		writeResults(1, gets, time1, map1.getClass().toString());

		final CaseInsensitiveStringMap<String> map2 = new CaseInsensitiveStringMap<String>(size);
		fillMap(map2, keys);

		final long time2 = testReadPerformance(keysCaseShuffled, map2, gets, seed);
		writeResults(1, gets, time2, map2.getClass().toString());
	}

	@Test
	public void testThreadLocalVsConcat()
	{
		final int size = 1000;
		final int gets = 10 * 1000 * size;
		final long seed = System.nanoTime();
		final List<String> keys = generateKeys(size);
		final List<String> keysCaseShuffled = shuffleCase(keys);

		final long timeConcat = testLowerCase(keys, keysCaseShuffled, size, gets, seed);
		writeResults(1, gets, timeConcat, "Lower Case");

		final long timeTL = testThreadLocalApproach(keys, keysCaseShuffled, size, gets, seed);
		writeResults(1, gets, timeTL, "ThreadLocal Approach");
	}

	private long testLowerCase(final List<String> keys, final List<String> keysForLookup, final int size, final int gets,
			final long seed)
	{
		final Map<String, String> map = new HashMap<String, String>(size);
		for (final String k : keys)
		{
			map.put(k.toLowerCase(), k);
		}
		final Random random = new Random(seed);

        final int[] indexes = new int[gets];
        for (int i = 0; i < gets; i++)
        {
            indexes[i] = random.nextInt(size);
        }
        final long time1 = System.currentTimeMillis();
        for (int i = 0; i < gets; i++)
		{
			final String key = keysForLookup.get(indexes[i]);
			map.get(key.toLowerCase()).hashCode();
		}
		final long time2 = System.currentTimeMillis();

		return time2 - time1;

	}

	private long testThreadLocalApproach(final List<String> keys, final List<String> keysForLookup, final int size,
			final int gets, final long seed)
	{
		final Map<Key, Object> map = new HashMap<Key, Object>(size);
		final MagicKeys magicKeys = new MagicKeys();
		for (final String k : keys)
		{
			map.put(magicKeys.getForPut(k), k);
		}
		final Random random = new Random(seed);
        final int[] indexes = new int[gets];
        for (int i = 0; i < gets; i++)
        {
            indexes[i] = random.nextInt(size);
        }
        final long time1 = System.currentTimeMillis();
		for (int i = 0; i < gets; i++)
		{
			map.get(magicKeys.getForLookup(keysForLookup.get(indexes[i]))).hashCode();
		}
		final long time2 = System.currentTimeMillis();

		return time2 - time1;
	}

	static class MagicKeys
	{
		final ThreadLocal<Key> keyForThread = new ThreadLocal<Key>()
		{
			@Override
			protected Key initialValue()
			{
				return new Key();
			}
		};

		Key getForLookup(final String string)
		{
			final Key key = keyForThread.get();
			key.set(string);
			return key;
		}

		Key getForPut(final String string)
		{
			final Key key = new Key();
			key.set(string);
			return key;
		}
	}

	static class Key
	{
		private int hash = 0;
		private String string1;

		void set(final String string1)
		{
			this.string1 = string1;
			this.hash = 0;
		}

		@Override
		public int hashCode()
		{
			if (hash == 0)
			{
				int hashHolder = 0;
				final int len = string1.length();
				if (len > 0)
				{
					for (int i = 0; i < len; i++)
					{
						hashHolder = 31 * hashHolder + Character.toLowerCase(string1.charAt(i));
					}
				}
				hash = hashHolder;
			}
			return hash;
		}

		@Override
		public boolean equals(final Object obj)
		{
			return super.equals(obj) || (obj != null && ((Key) obj).string1.equalsIgnoreCase(string1));
		}
	}

	private long testReadPerformance(final List<String> keys, final Map<String, String> map, final int cycles, final long seed)
	{
		final int size = keys.size();
		final Random random = new Random(seed);

		final long time1 = System.currentTimeMillis();
		for (int i = 0; i < cycles; i++)
		{
			final String key = keys.get(random.nextInt(size));
			map.get(key).hashCode();
		}
		final long time2 = System.currentTimeMillis();

		return time2 - time1;
	}

	final char[] CHARS =
	{ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	private List<String> shuffleCase(final List<String> keys)
	{
		final List<String> shuffled = new ArrayList<String>(keys.size());
		for (final String key : keys)
		{
			final char[] chars = key.toCharArray();
			for (int i = 0; i < chars.length; i++)
			{
				final char character = chars[i];
				if (Character.isUpperCase(character))
				{
					chars[i] = Character.toLowerCase(character);
				}
				else if (Character.isLowerCase(character))
				{
					chars[i] = Character.toUpperCase(character);
				}
				shuffled.add(new String(chars));
			}
		}
		return shuffled;
	}


	private List<String> generateKeys(final int size)
	{
		final Random random = new Random(System.nanoTime());

		final List<String> keys = new ArrayList<String>(size);

		for (int i = 0; i < size; i++)
		{
			final int length = 3 + random.nextInt(7);
			final StringBuilder stringBuilder = new StringBuilder(length);
			for (int j = 0; j < length; j++)
			{
				stringBuilder.append(CHARS[random.nextInt(CHARS.length)]);
			}
			final String key = stringBuilder.toString();
			keys.add(key);
		}
		return keys;
	}

	private void fillMap(final Map<String, String> map, final List<String> keys)
	{
		for (final String key : keys)
		{
			map.put(key, key);
		}
	}

	private void writeResults(final int threads, final int gets, final long milliSeconds, final String comment)
	{
		final String[] header =
		{ "threads", "gets", "time", "comment" };
		ASCIITable.getInstance().printTable(header, new String[][]
		{
		{ Integer.toString(threads), Integer.toString(threads * gets), Long.toString(milliSeconds) + " ms", comment } });

	}

}
