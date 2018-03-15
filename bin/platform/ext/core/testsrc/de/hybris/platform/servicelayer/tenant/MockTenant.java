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
package de.hybris.platform.servicelayer.tenant;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.numberseries.SerialNumberGenerator;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.threadpool.ThreadPool;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;


public class MockTenant implements Tenant
{
	@Override
	public void resetTenantRestartMarker()
	{
		throw new UnsupportedOperationException();
	}

	private final String tenantId;

	public MockTenant(final String tenantId)
	{
		this.tenantId = tenantId;
	}

	@Override
	public HybrisDataSource getDataSource(final String className)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Cache getCache()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ConfigIntf getConfig()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public HybrisDataSource getDataSource()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public InvalidationManager getInvalidationManager()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public JaloConnection getJaloConnection()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public PersistenceManager getPersistenceManager()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public PersistencePool getPersistencePool()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public SerialNumberGenerator getSerialNumberGenerator()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public SingletonCreator getSingletonCreator()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public SystemEJB getSystemEJB()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTenantID()
	{
		return tenantId;
	}

	@Override
	public List<String> getTenantSpecificExtensionNames()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Locale getTenantSpecificLocale()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public TimeZone getTenantSpecificTimeZone()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ThreadPool getThreadPool()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ThreadPool getWorkersThreadPool()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode()
	{
		return tenantId.hashCode();
	}

	@Override
	public boolean equals(final Object object)
	{
		if (object == null || !object.getClass().equals(this.getClass()))
		{
			return false;
		}
		return tenantId.equals(((MockTenant) object).tenantId);
	}

	@Override
	public JaloSession getActiveSession()
	{
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deactivateSlaveDataSource()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String activateSlaveDataSource()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void activateSlaveDataSource(final String id)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public HybrisDataSource getMasterDataSource()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSlaveDataSource()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAlternativeMasterDataSource()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<HybrisDataSource> getAllSlaveDataSources()
	{
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Set<String> getAllDataSourceIDs()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void forceMasterDataSource()
	{
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isForceMaster()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void activateAlternativeMasterDataSource(final String id)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void deactivateAlternativeDataSource()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getAllAlternativeMasterDataSourceIDs()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<HybrisDataSource> getAllAlternativeMasterDataSources()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getAllSlaveDataSourceIDs()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Thread createAndRegisterBackgroundThread(final Runnable payload, final ThreadFactory factory)
	{
		throw new UnsupportedOperationException();
	}
}
