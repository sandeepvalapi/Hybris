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

import de.hybris.platform.regioncache.key.AbstractCacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;


public class TestCacheKey extends AbstractCacheKey
{

	private final Object keyName;

	public TestCacheKey(final Object keyName)
	{
		super("SIMPLE_TYPE", "master");
		this.keyName = keyName;
	}

	public TestCacheKey(final String keyName, final CacheUnitValueType unitType)
	{
		super(unitType, "SIMPLE_TYPE", "master");
		this.keyName = keyName;
	}

	public TestCacheKey(final Object keyName, final CacheUnitValueType unitType, final String typeCode)
	{
		super(unitType, typeCode, "master");
		this.keyName = keyName;
	}

	public TestCacheKey(final Object keyName, final String tenant, final CacheUnitValueType unitType, final String typeCode)
	{
		super(unitType, typeCode, tenant);
		this.keyName = keyName;
	}

	@Override
	public String toString()
	{
		return "TestCacheKey [keyName=" + keyName + ", typeCode=" + typeCode + "]";
	}

	@Override
	public int hashCode()
	{
		return ((keyName == null) ? 0 : keyName.hashCode());
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (this == obj)
		{
			return true;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final TestCacheKey other = (TestCacheKey) obj;
		if (!tenantId.equals(other.tenantId))
		{
			return false;
		}
		if (keyName == null)
		{
			if (other.keyName != null)
			{
				return false;
			}
		}
		else if (!keyName.equals(other.keyName))
		{
			return false;
		}
		return true;
	}

	/**
	 * @return the keyName
	 */
	public Object getKeyName()
	{
		return keyName;
	}
}
