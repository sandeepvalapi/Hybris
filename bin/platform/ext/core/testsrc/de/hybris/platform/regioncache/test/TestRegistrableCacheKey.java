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

import de.hybris.platform.regioncache.key.AbstractRegistrableCacheKey;

import java.util.Arrays;


public class TestRegistrableCacheKey extends AbstractRegistrableCacheKey
{
	private final Object keyName;


	public Object getKeyName()
	{
		return keyName;
	}

	public TestRegistrableCacheKey(final Object keyName, final String[] dependentTypes)
	{
		super("master", dependentTypes);
		this.keyName = keyName;

	}

	public TestRegistrableCacheKey(final Object keyName, final String tenant, final String[] dependentTypes)
	{
		super(tenant, dependentTypes);
		this.keyName = keyName;

	}

	@Override
	public String toString()
	{
		return "TestRegistrableCacheKey [keyName=" + keyName + ", dependentTypes=" + Arrays.toString(getDependentTypes())
				+ ", typeCode=" + typeCode + "]";
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
		final TestRegistrableCacheKey other = (TestRegistrableCacheKey) obj;
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
}
