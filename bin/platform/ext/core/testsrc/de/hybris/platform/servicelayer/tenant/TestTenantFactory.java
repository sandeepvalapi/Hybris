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
import de.hybris.platform.core.threadregistry.RegistrableThread;
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

import org.springframework.beans.factory.FactoryBean;


/**
 *
 */
public class TestTenantFactory implements FactoryBean<Tenant>
{
	private final Tenant tenant;

	public TestTenantFactory()
	{
		this("junit");
	}

	public TestTenantFactory(final String tenantId)
	{
		this.tenant = new Tenant()
		{

			@Override
			public void resetTenantRestartMarker()
			{
				// YTODO Auto-generated method stub			
			}

			@Override
			public Thread createAndRegisterBackgroundThread(final Runnable payload, final ThreadFactory factory)
			{
				return factory == null ? new RegistrableThread(payload) : factory.newThread(payload);
			}

			@Override
			public boolean isSlaveDataSource()
			{
				// YTODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isAlternativeMasterDataSource()
			{
				// YTODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isForceMaster()
			{
				// YTODO Auto-generated method stub
				return false;
			}

			@Override
			public HybrisDataSource getMasterDataSource()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public HybrisDataSource getDataSource()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public HybrisDataSource getDataSource(final String className)
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public Collection<HybrisDataSource> getAllSlaveDataSources()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public Set<String> getAllSlaveDataSourceIDs()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public Set<String> getAllDataSourceIDs()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public Collection<HybrisDataSource> getAllAlternativeMasterDataSources()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public Set<String> getAllAlternativeMasterDataSourceIDs()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public void forceMasterDataSource()
			{
				// YTODO Auto-generated method stub

			}

			@Override
			public void deactivateSlaveDataSource()
			{
				// YTODO Auto-generated method stub

			}

			@Override
			public void deactivateAlternativeDataSource()
			{
				// YTODO Auto-generated method stub

			}

			@Override
			public void activateSlaveDataSource(final String id)
			{
				// YTODO Auto-generated method stub

			}

			@Override
			public String activateSlaveDataSource()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public void activateAlternativeMasterDataSource(final String id)
			{
				// YTODO Auto-generated method stub

			}

			@Override
			public ThreadPool getWorkersThreadPool()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public ThreadPool getThreadPool()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public TimeZone getTenantSpecificTimeZone()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public Locale getTenantSpecificLocale()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getTenantSpecificExtensionNames()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public String getTenantID()
			{
				return tenantId;
			}

			@Override
			public SystemEJB getSystemEJB()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public SingletonCreator getSingletonCreator()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public SerialNumberGenerator getSerialNumberGenerator()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public PersistencePool getPersistencePool()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public PersistenceManager getPersistenceManager()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public JaloConnection getJaloConnection()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public InvalidationManager getInvalidationManager()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public ConfigIntf getConfig()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public Cache getCache()
			{
				// YTODO Auto-generated method stub
				return null;
			}

			@Override
			public JaloSession getActiveSession()
			{
				// YTODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	public Tenant getObject() throws Exception
	{
		return tenant;
	}

	@Override
	public Class<?> getObjectType()
	{
		return Tenant.class;
	}

	@Override
	public boolean isSingleton()
	{
		return false;
	}

}
