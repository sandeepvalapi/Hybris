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
package de.hybris.platform.servicelayer.search;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.servicelayer.search.impl.LazyLoadModelList;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Collections;
import java.util.List;

import org.junit.Test;


/**
 * Tests {@link LazyLoadModelList}.
 */
@IntegrationTest
public class LazyLoadModelListTest extends HybrisJUnit4Test
{

	private final ItemObjectResolver resolverStub = new ItemObjectResolver()
	{
		@Override
		public Object resolve(final int expectedColumnIndex, final Object cachedIdentifier, final List expectedClassList)
		{
			return null;
		}

		@Override
		public Object resolve(final Object cachedIdentifier, final List expectedClassList)
		{
			return null;
		}

		@Override
		public Object unresolve(final Object model)
		{
			return null;
		}

		@Override
		public boolean preloadItems(final List list)
		{
			return false;
		}
	};

	@Test
	public void testLazyLoadOnRemovedItem() throws ConsistencyCheckException
	{
		final Country myCountry = C2LManager.getInstance().createCountry("thalerland");
		final LazyLoadItemList<Country> itemList = new LazyLoadItemList<Country>(null,
				Collections.singletonList(myCountry.getPK()), 2);
		final LazyLoadModelList list = new LazyLoadModelList(itemList, 2, Collections.EMPTY_LIST, resolverStub);
		assertEquals("List contains not exactly one element", 1, list.size());
		myCountry.remove();
		assertEquals("List contains not exactly one element", 1, list.size());
		assertNull("Item was removed before access to list, so List element must be null by contract but isn't", list.iterator()
				.next());
	}
}
