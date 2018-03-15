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
package de.hybris.platform.jalo;

import de.hybris.bootstrap.annotations.PerformanceTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.base.Joiner;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Test checking if the ContextMap fulfills contract of thread safety.
 */
@PerformanceTest
public class ContextMapUnitTest
{

	/**
	 * 
	 */
	private static final String CHILD_KEY_2 = "C2";
	/**
	 * 
	 */
	private static final String CHILD_KEY_1 = "C1";
	private static final String GLOBAL_KEY_2 = "P2";
	private static final String GLOBAL_KEY_1 = "P1";


	private final ContextMap<String, String> parentMap = new GlobalContextMap<String, String>();

	private final Map<String, String> givenAttributes = new HashMap<String, String>((int) (10 / 0.75f) + 1);

	@Before
	public void prepare()
	{


		parentMap.put(GLOBAL_KEY_1, "foo one");
		parentMap.put(GLOBAL_KEY_2, "foo two");



		givenAttributes.put(CHILD_KEY_1, "bar one");
		givenAttributes.put(CHILD_KEY_2, "bar two");

	}



	@Test
	public void testEmptyAsGlobal()
	{
		Assert.assertTrue(new GlobalContextMap<String, String>().isEmpty());
	}

	@Test
	public void testEmptyAsLocal()
	{

		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		Assert.assertFalse(ctxMap.isEmpty());

		ctxMap.putAll(givenAttributes);

		Assert.assertFalse(ctxMap.isEmpty());

		ctxMap.remove(CHILD_KEY_1);
		ctxMap.remove(CHILD_KEY_2);
		ctxMap.remove(GLOBAL_KEY_1);
		ctxMap.remove(GLOBAL_KEY_2);
		Assert.assertTrue(ctxMap.isEmpty());
	}

	@Test
	public void testClearAsGlobal()
	{

		final Map<String, String> empty = new GlobalContextMap<String, String>();
		Assert.assertTrue(empty.isEmpty());
		empty.clear();
		Assert.assertTrue(empty.isEmpty());
	}

	/**
	 * assures no modification calls on parent map
	 */
	@Test
	public void testClearAsLocal()
	{
		final ContextMap<String, String> parentMapSpy = Mockito.spy(parentMap);

		Mockito.doThrow(new RuntimeException()).when(parentMapSpy).clear();
		Mockito.doThrow(new RuntimeException()).when(parentMapSpy).put(Mockito.anyString(), Mockito.anyString());
		Mockito.doThrow(new RuntimeException()).when(parentMapSpy).remove(Mockito.anyString());

		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMapSpy);

		Assert.assertFalse(parentMapSpy.isEmpty());
		Assert.assertFalse(ctxMap.isEmpty());

		ctxMap.putAll(givenAttributes);


		Assert.assertFalse(ctxMap.isEmpty());

		ctxMap.clear();
		Assert.assertTrue(ctxMap.isEmpty());
		Assert.assertFalse(parentMapSpy.isEmpty());

		Mockito.verify(parentMapSpy, Mockito.times(1)).keySet();
	}


	@Test
	public void testPutAllAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.putAll(givenAttributes);

		Assert.assertEquals(4, ctxMap.size());

		Assert.assertTrue(ctxMap.containsKey(GLOBAL_KEY_1));
		Assert.assertTrue(ctxMap.containsKey(GLOBAL_KEY_2));
		Assert.assertTrue(ctxMap.containsKey(CHILD_KEY_1));
		Assert.assertTrue(ctxMap.containsKey(CHILD_KEY_2));
	}

	@Test
	public void testPutAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.put("L1", "loo one");

		Assert.assertEquals(3, ctxMap.size());

		Assert.assertTrue(ctxMap.containsKey(GLOBAL_KEY_1));
		Assert.assertTrue(ctxMap.containsKey(GLOBAL_KEY_2));
		Assert.assertTrue(ctxMap.containsKey("L1"));
	}

	@Test
	public void testPutNullValueAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.put("L1", null);

		Assert.assertEquals(3, ctxMap.size());

		Assert.assertTrue(ctxMap.containsKey("L1"));
	}

	@Test
	public void testRemoveAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		Assert.assertEquals(null, ctxMap.remove("L1"));

		Assert.assertEquals(2, ctxMap.size());

		Assert.assertTrue(ctxMap.containsKey(GLOBAL_KEY_1));
		Assert.assertTrue(ctxMap.containsKey(GLOBAL_KEY_2));

		ctxMap.putAll(givenAttributes);

		Assert.assertEquals("bar one", ctxMap.remove(CHILD_KEY_1));

		Assert.assertTrue(ctxMap.containsKey(GLOBAL_KEY_1));
		Assert.assertTrue(ctxMap.containsKey(GLOBAL_KEY_2));
		Assert.assertFalse(ctxMap.containsKey(CHILD_KEY_1));
		Assert.assertTrue(ctxMap.containsKey(CHILD_KEY_2));

	}


	@Test
	public void testRemoveAsGlobal()
	{
		final Map<String, String> ctxMap = new GlobalContextMap<String, String>();

		ctxMap.putAll(givenAttributes);

		Assert.assertEquals("bar one", ctxMap.remove(CHILD_KEY_1));
		Assert.assertEquals(null, ctxMap.remove(CHILD_KEY_1));
	}

	@Test
	public void testRemoveRemovedAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.putAll(givenAttributes);

		Assert.assertEquals("bar one", ctxMap.remove(CHILD_KEY_1));

		Assert.assertEquals(null, ctxMap.remove(CHILD_KEY_1));
	}


	@Test
	public void testRemoveAfterCleanAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.putAll(givenAttributes);

		ctxMap.clear();

		Assert.assertEquals(null, ctxMap.remove(CHILD_KEY_1));

	}

	@Test
	public void testContainsAfterCleanAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.putAll(givenAttributes);

		ctxMap.clear();

		Assert.assertEquals(false, ctxMap.containsKey(CHILD_KEY_1));

	}

	@Test
	public void testContainsAfterCleanFromParentAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.putAll(givenAttributes);

		ctxMap.clear();

		Assert.assertEquals(false, ctxMap.containsKey(GLOBAL_KEY_1));

	}

	@Test
	public void testRemoveFromParentAfterCleanAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.putAll(givenAttributes);

		ctxMap.clear();

		Assert.assertEquals(null, ctxMap.get(GLOBAL_KEY_1));

		Assert.assertEquals(null, ctxMap.remove(GLOBAL_KEY_1));
	}


	@Test
	public void testRemoveRemovedFromParentAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.putAll(givenAttributes);

		Assert.assertEquals("foo one", ctxMap.remove(GLOBAL_KEY_1));

		Assert.assertEquals(null, ctxMap.get(GLOBAL_KEY_1));

		Assert.assertEquals(null, ctxMap.remove(GLOBAL_KEY_1));
	}

	@Test
	public void testContainsValueAsGlobal()
	{
		final Map<String, String> ctxMap = new GlobalContextMap<String, String>();

		ctxMap.putAll(givenAttributes);

		Assert.assertEquals(true, ctxMap.containsValue("bar one"));
		Assert.assertEquals(false, ctxMap.containsValue("bar zillion"));
	}

	@Test
	public void testContainsValueAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.putAll(givenAttributes);

		Assert.assertEquals(true, ctxMap.containsValue("bar one"));
		Assert.assertEquals(false, ctxMap.containsValue("bar zillion"));
		Assert.assertEquals(true, ctxMap.containsValue("foo one"));
		Assert.assertEquals(false, ctxMap.containsValue("foo zillion"));
	}

	@Test
	public void testContainsNullValueAsLocal()
	{
		final Map<String, String> ctxMap = new LocalContextMap<String, String>(parentMap);

		ctxMap.put(CHILD_KEY_1, null);

		Assert.assertEquals(true, ctxMap.containsValue(null));
		ctxMap.clear();
		Assert.assertEquals(false, ctxMap.containsValue(null));

	}


	@Test
	public void testMadlyNestedMaps()
	{

		final int DEPTH = 50;
		final ContextMap<String, String> root = new GlobalContextMap<String, String>();

		ContextMap<String, String> current = root;

		for (int i = 0; i < DEPTH; i++)
		{
			current = new LocalContextMap<String, String>(current);
			current.put("foo-" + i, String.valueOf(i));
		}


		for (int i = 0; i < DEPTH; i++)
		{
			Assert.assertTrue(current.containsKey("foo-" + i));
			Assert.assertTrue(current.containsValue(String.valueOf(i)));
		}
	}

	@Test
	public void testMadlyNestedMapsWithAllocationIssue()
	{

		final int DEPTH = 50;
		final ContextMap<String, String> root = new GlobalContextMap<String, String>();

		for (int i = 0; i < DEPTH; i++)
		{
			root.put("root-" + i, "root-" + i);
		}

		ContextMap<String, String> current = root;

		for (int i = 0; i < DEPTH; i++)
		{
			current = new LocalContextMap<String, String>(current);
			current.put("foo-" + i, String.valueOf(i));
		}


		for (int i = 0; i < DEPTH; i++)
		{
			Assert.assertTrue(current.containsKey("foo-" + i));
			Assert.assertTrue(current.containsValue(String.valueOf(i)));
		}
	}

	@Test
	public void testNestedMaps()
	{
		final ContextMap<String, String> root = new GlobalContextMap<String, String>();

		root.put("foo-root", "0");
		root.put("bar-root", "0");
		root.put("roo-root", "0");

		final ContextMap<String, String> firstLevel = new LocalContextMap<String, String>(root);

		firstLevel.put("foo-1", "1");

		final ContextMap<String, String> secondLevel = new LocalContextMap<String, String>(firstLevel);

		secondLevel.put("foo-2", "2");
		secondLevel.remove("bar-root");
		secondLevel.put("foo-root", "2");

		final ContextMap<String, String> thirdLevel = new LocalContextMap<String, String>(secondLevel);

		thirdLevel.put("foo-3", "3");
		thirdLevel.remove("bar-root");
		thirdLevel.remove("foo-root");
		thirdLevel.remove("foo-2");

        assertThat(root.values()).containsOnly("0");
		assertThat(root.values()).hasSize(3);
		assertThat(root.keySet()).containsOnly("foo-root", "bar-root", "roo-root");

		assertThat(firstLevel.values()).containsOnly("1", "0");
		assertThat(firstLevel.values()).hasSize(2);
		assertThat(firstLevel.keySet()).containsOnly("foo-root", "bar-root", "roo-root", "foo-1");

		assertThat(secondLevel.values()).containsOnly("2", "1", "0");
		assertThat(secondLevel.values()).hasSize(3);
		assertThat(secondLevel.keySet()).containsOnly("foo-root", "roo-root", "foo-1", "foo-2");

		assertThat(thirdLevel.values()).containsOnly("3", "1", "0");
		assertThat(thirdLevel.values()).hasSize(3);
		assertThat(thirdLevel.keySet()).containsOnly("roo-root", "foo-3", "foo-1");
	}



	/**
	 * 
	 */
	private void assertSetsEqual(final Set<String> given, final String... expected)
	{
		org.junit.Assert.assertEquals("Given set [" + Joiner.on(",").join(given) + "] size doesn't match the ["
				+ Joiner.on(",").join(expected) + "]", expected.length, given.size());
		for (final String expectOne : expected)
		{
			Assert.assertTrue("Given set [" + Joiner.on(",").join(given) + "] does not contain [" + expectOne + "]",
					given.contains(expectOne));
		}
		//org.junit.Assert.assertEquals(Sets.newHashSet("foo-root", "bar-root", "roo-root"), new ArrayList(root.keySet()));
	}


}
