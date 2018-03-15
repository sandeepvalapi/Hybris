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
package de.hybris.platform.cache;

import de.hybris.platform.util.collections.YFastFIFOMap;


public class BaseCacheYFastFIFOMapStub<K> extends YFastFIFOMap<K, AbstractCacheUnit>
{
	private final CacheBaseStub cacheBase;

	public BaseCacheYFastFIFOMapStub(final CacheBaseStub cacheBase, final int max)
	{
		super(max);
		this.cacheBase = cacheBase;
	}

	@Override
	public void processDisplacedEntry(final Object key, final AbstractCacheUnit value)
	{
		cacheBase.removeUnitFromNestedMapOnly(value);

	}

}
