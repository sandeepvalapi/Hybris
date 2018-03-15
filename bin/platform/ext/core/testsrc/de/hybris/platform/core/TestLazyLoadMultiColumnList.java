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

import java.util.List;
import java.util.Set;


class TestLazyLoadMultiColumnList extends LazyLoadMultiColumnList
{

	public TestLazyLoadMultiColumnList(final List<List<Object>> originalRows, final List<Class> signature,
			final Set<PK> prefetchLanguages, final int prefetchSize, final boolean mustWrapObjectsToo)
	{
		super(originalRows, signature, prefetchLanguages, prefetchSize, mustWrapObjectsToo);

	}

	@Override
	protected Object wrapObject(final Object original)
	{
		return original;
	}


	@Override
	protected LazyLoadItemList createItemList(final Set<PK> prefetchLanguages, final List<PK> itemPKs, final int prefetchSize)
	{
		return new LazyLoadItemList(prefetchLanguages, itemPKs, prefetchSize)
		{
			@Override
			protected List loadPage(final List pks)
			{
				return pks;
			}
		};
	}

}
