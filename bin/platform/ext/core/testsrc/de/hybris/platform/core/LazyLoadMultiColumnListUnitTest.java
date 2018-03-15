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

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.ItemPropertyValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import com.google.common.base.Joiner;


/**
 * 
 */
@UnitTest
public class LazyLoadMultiColumnListUnitTest
{
	private final static Logger LOG = Logger.getLogger(LazyLoadMultiColumnListUnitTest.class);

	private final PK someItemPK = PK.createFixedCounterPK(1, System.currentTimeMillis());

	private final int PEREFETCH_SIZE = 2;

	private final static int DURATION_SEC = 10;

	@Test
	public void testListDeserialize() throws IOException, ClassNotFoundException
	{


		final List<Object> oneRow = Arrays.asList(new Object[]
		{ "foo", Integer.valueOf(11), "bar", new ItemPropertyValue(someItemPK), new ItemPropertyValue(someItemPK) });

		final List<List<Object>> allRows = Arrays.asList(oneRow);

		final List<Class> signature = Arrays.asList(new Class[]
		{ String.class, Integer.class, Serializable.class, Product.class, ItemPropertyValue.class });

		final LazyLoadMultiColumnList list = new TestLazyLoadMultiColumnList(allRows, signature, Collections.EMPTY_SET,
				PEREFETCH_SIZE, true);

		for (final List<Object> row : list)
		{
			Assert.assertNotNull(row.get(0));
			Assert.assertEquals("foo", row.get(0));
			Assert.assertNotNull(row.get(1));
			Assert.assertEquals(Integer.valueOf(11), row.get(1));
			Assert.assertNotNull(row.get(2));
			Assert.assertEquals("bar", row.get(2));
			Assert.assertNotNull(row.get(3));
			Assert.assertEquals(new ItemPropertyValue(someItemPK), row.get(3));
			Assert.assertNotNull(row.get(4));
			Assert.assertEquals(someItemPK, row.get(4));
			LOG.info("row values ::" + Joiner.on("|").join(row));
		}

		for (final List<Object> row : list)
		{
			for (final Object value : row)
			{
				Assert.assertNotNull(value);
			}
		}


		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(bos);

		//write lists
		oos.writeObject(list);

		oos.close();

		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		//read them again
		final LazyLoadMultiColumnList deserializedList = (LazyLoadMultiColumnList) ois.readObject();

		for (final List<Object> row : deserializedList)
		{
			Assert.assertNotNull(row.get(0));
			Assert.assertEquals("foo", row.get(0));
			Assert.assertNotNull(row.get(1));
			Assert.assertEquals(Integer.valueOf(11), row.get(1));
			Assert.assertNotNull(row.get(2));
			Assert.assertEquals("bar", row.get(2));
			Assert.assertNotNull(row.get(3));
			Assert.assertEquals(new ItemPropertyValue(someItemPK), row.get(3));
			Assert.assertNotNull(row.get(4));
			Assert.assertEquals(someItemPK, row.get(4));
			LOG.info("row values ::" + Joiner.on("|").join(row));
		}

	}


	@Test
	public void testWrappedObejctsThreadSafety() throws IOException, ClassNotFoundException
	{
		final List<List<Object>> allRows = new ArrayList<List<Object>>(1001);

		for (int i = 0; i < 100; i++)
		{
			allRows.add(Arrays.asList(new Object[]
			{ "bar-" + i }));
		}

		final List<Class> signature = Arrays.asList(new Class[]
		{ Serializable.class });

		final LazyLoadMultiColumnList list = new TestLazyLoadMultiColumnList(allRows, signature, Collections.EMPTY_SET,
				PEREFETCH_SIZE, true);

		final TestThreadsHolder<Runnable> threads = new TestThreadsHolder<Runnable>(100, true)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new LazyLoadListFetcher(list, threadNumber);
			}
		};
		threads.startAll();
		try
		{
			Thread.sleep(DURATION_SEC * 1000);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		assertTrue(threads.stopAndDestroy(30));

		for (final Throwable runner : threads.getErrors().values())
		{
			runner.printStackTrace();
		}

		final Field field = ReflectionUtils.findField(LazyLoadMultiColumnList.class, "wrappedObjects");
		field.setAccessible(true);
		final Map<Integer, Object> wrapped = (Map<Integer, Object>) ReflectionUtils.getField(field, list);

		for (int i = 0; i < 100; i++)
		{
			Assert.assertEquals("bar-" + i, wrapped.get(Integer.valueOf(-(i + 1))));
		}

	}

	static class LazyLoadListFetcher implements Runnable
	{

		private final List<List<Object>> list;
		private final int idx;

		public LazyLoadListFetcher(final List<List<Object>> list, final int idx)
		{
			this.list = list;
			this.idx = idx;
		}

		@Override
		public void run()
		{
			list.get(idx).get(0);
		}
	}

}
