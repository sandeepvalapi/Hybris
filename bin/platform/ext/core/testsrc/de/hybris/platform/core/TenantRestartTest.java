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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.JaloSecurityException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;


@IntegrationTest
public class TenantRestartTest extends AbstractTenantInitializationTest
{

	private static final String TABLE_NAME = "booBar";
	private static final Logger LOG = Logger.getLogger(TenantRestartTest.class.getName());
	private static final String MESSAGE_LISTENER_DEF = ""// NOPMD
			+ "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n"//
			+ "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"//
			+ "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n"//
			+ "                            http://www.springframework.org/schema/beans/spring-beans-3.1.xsd\">\n" //

			+ "       <bean\n"//
			+ "             id=\"test.tenant.startup.bean\"\n"//
			+ "             class=\"" + TestAfterTenantStartupEventListener.class.getName() + "\"\n"//
			+ "           >\n"//
			+ "       </bean>\n"//
			+ "</beans>";
	private volatile GenericApplicationContext applicationContext;
	private TestAfterTenantStartupEventListener testListener;

	@Override
	protected Collection<String> getTenantIds()
	{
		return java.util.Collections.singleton("t1");
	}


	@Override
	@Before
	public void setUp() throws Exception
	{

		final ApplicationContext parent = Registry.getCoreApplicationContext();

		applicationContext = new GenericApplicationContext();
		applicationContext.setParent(parent);

		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(applicationContext);
		xmlReader.loadBeanDefinitions(new ByteArrayResource(MESSAGE_LISTENER_DEF.getBytes()));

		applicationContext.refresh();


		testListener = applicationContext.getBean("test.tenant.startup.bean", TestAfterTenantStartupEventListener.class);
		Assertions.assertThat(testListener).isInstanceOf(TestAfterTenantStartupEventListener.class);
		super.setUp();

	}


	@Override
	@After
	public void tearDown() throws Exception
	{
		applicationContext.destroy();
		applicationContext = null;

		super.tearDown();
	}

	@Test
	public void testJaloSessionObsoleteAfterTenantRestart() throws JaloSecurityException, ConsistencyCheckException
	{

		final SlaveTenant localTenant = Registry.getSlaveTenants().get("t1");
		Registry.setCurrentTenant(localTenant);

		final JaloSession session = JaloConnection.getInstance().createAnonymousCustomerSession();

		Assertions.assertThat(JaloSession.getCurrentSession(localTenant)).isSameAs(session);
		Assertions.assertThat(JaloSession.assureSessionNotStale(session)).isTrue();

		localTenant.doShutDownDuringInitialization();

		//TODO remove it
		//JaloSession.getCurrentSession(); //creating anonymous jalo session in case there was no other

		Assertions.assertThat(JaloSession.assureSessionNotStale(session))
				.overridingErrorMessage("Session should be stale is from before shutdown").isFalse();
		try
		{
			session.activate();
			Assert.fail("Should be not able to activate the stale session");
		}
		catch (final IllegalStateException se)
		{
			//fine here
		}

		localTenant.doStartUp();

		final JaloSession otherSession = JaloConnection.getInstance().createAnonymousCustomerSession();

		Assertions.assertThat(JaloSession.getCurrentSession(localTenant)).isSameAs(otherSession);
		Assertions.assertThat(JaloSession.assureSessionNotStale(otherSession))
				.overridingErrorMessage("Session can't be stale is fresh right after startup").isTrue();

		otherSession.activate(); //activate - no harm
	}

	@Test
	public void testAfterStartupMessageSendAfterRestart() throws JaloSecurityException, ConsistencyCheckException
	{
		Registry.unsetCurrentTenant();
		Registry.unregisterShutdownTenant("t1");

		Assertions.assertThat(testListener.getEventsRecoreded().isEmpty()).isTrue();

		final SlaveTenant localTenant = new TestSlaveTenantStub(Registry.getSlaveTenants().get("t1"), applicationContext);

		//here to activate injected tenant
		Registry.setCurrentTenantInternal(localTenant);
		localTenant.doStartUp(); //no event on  start up


		Registry.setCurrentTenant(localTenant);

		Registry.destroyAndForceStartup(); //event on restart

		Assertions.assertThat(testListener.getEventsRecoreded().isEmpty()).isFalse();
		Assertions.assertThat(testListener.getEventsRecoreded().size()).isEqualTo(1);

	}

	@Test
	public void testFetchAccurateHybrisDataSourceAfterTenantRestart() throws JaloSecurityException, ConsistencyCheckException
	{
		String tableName = null;
		final SlaveTenant localTenant = Registry.getSlaveTenants().get("t1");
		try
		{
			Registry.setCurrentTenant(localTenant);

			tableName = localTenant.getDatabaseTablePrefix() == null ? TABLE_NAME : localTenant.getDatabaseTablePrefix()
					+ TABLE_NAME;

			final DataSource springBasedDataSourceBefore = Registry.getCoreApplicationContext().getBean("dataSource", DataSource.class);
			Assert.assertSame("We should get the consistence data source vis spring factory  ", springBasedDataSourceBefore,
					Registry.getCoreApplicationContext().getBean("dataSource", DataSource.class));
			Assert.assertSame("We should get the same data source via spring or jalo ", springBasedDataSourceBefore, Registry
					.getCurrentTenant().getDataSource());

			final JdbcTemplate springTemplateBefore = new JdbcTemplate(springBasedDataSourceBefore);
			createTable(springTemplateBefore, tableName);

			assertTableExistsTimes(springBasedDataSourceBefore, tableName);
			assertTableExistsTimes(Registry.getCurrentTenant().getDataSource(), tableName);

			localTenant.doShutDownDuringInitialization();

			final DataSource springBasedDataSourceAfter = Registry.getCoreApplicationContext().getBean("dataSource", DataSource.class);
			Assert.assertSame("We should get the consistence data source vis spring factory  ", springBasedDataSourceAfter, Registry
					.getCoreApplicationContext().getBean("dataSource", DataSource.class));
			Assert.assertSame("We should get the same data source via spring or jalo ", springBasedDataSourceAfter, Registry
					.getCurrentTenant().getDataSource());

			assertTableExistsTimes(springBasedDataSourceBefore, tableName);
			assertTableExistsTimes(springBasedDataSourceAfter, tableName);

		}
		finally
		{
			if (tableName != null)
			{
				dropTable(localTenant.getDataSource(), tableName);
			}
		}

	}

	@Test
	public void testTheSameInvalidationListenersAfterRestart() throws JaloSecurityException, ConsistencyCheckException
	{

		final SlaveTenant localTenant = Registry.getSlaveTenants().get("t1");
		try
		{
			Registry.setCurrentTenant(localTenant);

			final InvalidationManager invManagerBefore = InvalidationManager.getInstance();

			final InvalidationTopic topicBefore = InvalidationManager.getInstance().getInvalidationTopic(new String[]
			{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY });


			localTenant.doShutDownDuringInitialization();
			localTenant.startUp();

			Assert.assertSame("invalidation manager should remain the same", invManagerBefore, InvalidationManager.getInstance());
			Assert.assertSame("invalidation topic should remain the same", topicBefore, InvalidationManager.getInstance()
					.getInvalidationTopic(new String[]
					{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY }));
			assertEqualListeners(topicBefore, InvalidationManager.getInstance().getInvalidationTopic(new String[]
			{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY }));
		}
		finally
		{
			//
		}

	}

	private void assertEqualListeners(final InvalidationTopic topicAfter, final InvalidationTopic topicBefore)
	{
		final List<InvalidationListener> listenersBefore = getListenersViaReflection(topicBefore);
		final List<InvalidationListener> listenersAfter = getListenersViaReflection(topicAfter);

		Assertions.assertThat(listenersAfter.size()).isEqualTo(listenersBefore.size());

		final List<InvalidationListener> copyBefore = new ArrayList<InvalidationListener>(listenersBefore);
		for (final InvalidationListener before : listenersBefore)
		{
			final int hashBefore = System.identityHashCode(before);
			for (final InvalidationListener after : listenersAfter)
			{
				final int hashAfter = System.identityHashCode(after);
				if (hashAfter == hashBefore)
				{
					copyBefore.remove(before);
					break;
				}
			}

		}

		Assert.assertTrue(copyBefore.isEmpty());
	}

	private List<InvalidationListener> getListenersViaReflection(final InvalidationTopic topic)
	{
		final Field listeners = ReflectionUtils.findField(InvalidationTopic.class, "listeners");
		Assertions.assertThat(listeners).isNotNull();
		listeners.setAccessible(true);
		return (List<InvalidationListener>) ReflectionUtils.getField(listeners, topic);
	}

	private void assertTableExistsTimes(final DataSource dataSource, final String tableName)
	{
		for (int i = 0; i < 100; i++)
		{
			assertTableExists(new JdbcTemplate(dataSource), tableName);
		}

	}

	private void assertTableExists(final JdbcTemplate template, final String tableName)
	{
		template.queryForList("SELECT ID  FROM " + tableName);

	}

	private void createTable(final JdbcTemplate template, final String tableName)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Create table " + tableName + " for tenant " + Registry.getCurrentTenantNoFallback());
		}
		template.execute("CREATE TABLE " + tableName + " ( ID VARCHAR(10))");
	}

	private void dropTable(final DataSource dataSource, final String tableName)
	{

		try
		{
			final JdbcTemplate create = new JdbcTemplate(dataSource);
			create.execute("DROP TABLE " + tableName);
		}
		catch (final DataAccessException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
		}
	}


	static class TestSlaveTenantStub extends SlaveTenant
	{
		//private final SlaveTenant wrapped;
		private final GenericApplicationContext localApplicationContext;

		TestSlaveTenantStub(final SlaveTenant wrapped, final GenericApplicationContext applicationContext)
		{
			super(wrapped.getTenantID(), MapUtils.toProperties(wrapped.getOwnConfig().getAllParameters()));
			this.localApplicationContext = applicationContext;
		}

		@Override
		List<TenantListener> getTenantListeners()
		{
			return Collections.EMPTY_LIST;
		}

		@Override
		GenericApplicationContext getApplicationContext()
		{
			return this.localApplicationContext;
		}

		//internal
		@Override
		GenericApplicationContext createCoreApplicationContext()
		{
			return this.localApplicationContext;
		}
	}

}
