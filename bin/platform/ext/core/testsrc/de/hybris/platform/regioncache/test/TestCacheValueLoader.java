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
package de.hybris.platform.regioncache.test;

import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;


public class TestCacheValueLoader implements CacheValueLoader
{
	private int loads = 0;

	private final Object val;

	public TestCacheValueLoader()
	{
		val = null;
	}

	public TestCacheValueLoader(final Object val)
	{
		this.val = val;
	}

	@Override
	public Object load(final CacheKey key) throws CacheValueLoadException
	{
		++loads;
		if (val == null)
		{
			return "Value for key " + key;
		}
		return val;
	}

	public int getLoads()
	{
		return loads;
	}

	/**
	 * 
	 */
	public Object getVal()
	{
		return val;
	}
}
