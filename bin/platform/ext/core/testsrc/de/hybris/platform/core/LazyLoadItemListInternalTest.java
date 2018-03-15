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

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * UnitTest for checking internals of the {@link LazyLoadItemList} working correctly.
 * 
 */
@UnitTest
public class LazyLoadItemListInternalTest
{
	/**
	 * Test case for checking page switching/caching mechanism.
	 */
	@Test
	public void testLazyLoadItemListPaging()
	{
		final int ALL_ITEMS = 500;
		final int PAGE_SIZE = 50;
		final List<PK> allPks = new ArrayList<PK>(ALL_ITEMS);
		for (int i = 0; i < ALL_ITEMS; i++)
		{
			allPks.add(PK.createFixedCounterPK(1, i));
		}

		final LazyLoadItemList<Long> testListMock = Mockito.spy(new TestPKLazyLoadItemList(allPks, PAGE_SIZE));

		Mockito.reset(testListMock);//reset



		testListMock.get(0);//first access fill in cache
		Mockito.verify(testListMock, Mockito.times(1)).switchPage(0);
		Mockito.reset(testListMock);

		testListMock.get(0);//fetch again - already in cache
		Mockito.verify(testListMock, Mockito.never()).switchPage(0);
		Mockito.reset(testListMock);

		testListMock.get(PAGE_SIZE - 1);//fetch from the same page
		Mockito.verify(testListMock, Mockito.never()).switchPage(0);
		Mockito.reset(testListMock);

		testListMock.get(PAGE_SIZE + 1);//next page first access
		Mockito.verify(testListMock, Mockito.times(1)).switchPage(PAGE_SIZE + 1);
		Mockito.reset(testListMock);

		testListMock.get(PAGE_SIZE + 1);//next page first access
		Mockito.verify(testListMock, Mockito.never()).switchPage(PAGE_SIZE + 1);
		Mockito.reset(testListMock);

		testListMock.get(2 * PAGE_SIZE - 1);//last item on that page
		Mockito.verify(testListMock, Mockito.never()).switchPage(1);
		Mockito.reset(testListMock);


		testListMock.get((2 * PAGE_SIZE) + 1);//third page first access
		Mockito.verify(testListMock, Mockito.times(1)).switchPage(2 * PAGE_SIZE + 1);
		Mockito.reset(testListMock);

		testListMock.get((2 * PAGE_SIZE) + 1);//this access won't need switch
		Mockito.verify(testListMock, Mockito.never()).switchPage(2 * PAGE_SIZE + 1);
		Mockito.reset(testListMock);

		testListMock.get(0);//but fetch for the element from some other page will
		Mockito.verify(testListMock, Mockito.times(1)).switchPage(0);
		Mockito.reset(testListMock);

		testListMock.get(ALL_ITEMS - 1);//last item access
		Mockito.verify(testListMock, Mockito.times(1)).switchPage(ALL_ITEMS - 1);
		Mockito.reset(testListMock);

		testListMock.get(ALL_ITEMS - 1);//last item access
		Mockito.verify(testListMock, Mockito.never()).switchPage(10);
		Mockito.reset(testListMock);

		try
		{
			testListMock.get(ALL_ITEMS);//last item access
			Assert.fail("Should throw IndexOutOfBoundsException ");
		}
		catch (final IndexOutOfBoundsException ibe)
		{
			//ok here
		}
		try
		{
			testListMock.get(ALL_ITEMS + 1);
			Assert.fail("Should throw IndexOutOfBoundsException ");
		}
		catch (final IndexOutOfBoundsException ibe)
		{
			//ok here
		}
	}

	public static class TestPKLazyLoadItemList extends LazyLoadItemList<Long>
	{
		public TestPKLazyLoadItemList(final List<PK> pks, final int prefetchSize)
		{
			super(null, pks, prefetchSize);
		}

		@Override
		protected List<Long> loadPage(final List<PK> pks)
		{
			//the list has as values for keys consecutive long values
			final List<Long> page = new ArrayList<Long>(pks.size());
			for (final PK pk : pks)
			{
				page.add(Long.valueOf(pk.getCounter()));
			}
			return page;
		}
	}
}
