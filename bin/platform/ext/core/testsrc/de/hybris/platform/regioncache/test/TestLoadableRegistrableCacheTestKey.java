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
import de.hybris.platform.regioncache.key.AbstractRegistrableCacheKey;
import de.hybris.platform.regioncache.key.CacheKey;

import java.util.Arrays;


/**
 *
 */
public class TestLoadableRegistrableCacheTestKey extends AbstractRegistrableCacheKey implements CacheValueLoader
{

	public TestLoadableRegistrableCacheTestKey(final String tenantId, final String[] dependentTypes)
	{
		super(tenantId, dependentTypes);
	}



	@Override
	public Object load(final CacheKey key) throws CacheValueLoadException
	{
		return this.getDependentTypes();
	}

	@Override
	public String toString()
	{
		return "RegistrableCacheTestKey [dependentTypes=" + Arrays.toString(getDependentTypes()) + ", typeCode=" + typeCode + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(getDependentTypes());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!super.equals(obj))
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final TestLoadableRegistrableCacheTestKey other = (TestLoadableRegistrableCacheTestKey) obj;
		if (!tenantId.equals(other.tenantId))
		{
			return false;
		}
		if (!Arrays.equals(getDependentTypes(), other.getDependentTypes()))
		{
			return false;
		}
		return true;
	}
}
