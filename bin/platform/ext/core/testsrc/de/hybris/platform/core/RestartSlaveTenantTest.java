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

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.meta.MetaInformationManager;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.testframework.runlistener.ItemCreationListener;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Config.SystemSpecificParams;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


/**
 * Test checks that system in the most crucial low level features
 * <ul>
 * <li>flexible search</li>
 * <li>jalo persistence</li>
 * <li>metadata persistence</li>
 * <li>tenant loading mechanism</li>
 * </ul>
 * 
 * re-think the concept of creating tenants in the tests
 */
@ManualTest
public class RestartSlaveTenantTest extends BaseSlaveTenantTest
{
	private static final Logger LOG = Logger.getLogger(RestartSlaveTenantTest.class.getName());


	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;


	private ItemCreationListener testListener;


	@Override
	@Before
	public void before() throws Exception
	{
		super.before();
		testListener = new ItemCreationListener();
		testListener.testStarted(null);

	}

	@Override
	@After
	public void after() throws Exception
	{
		super.after();
		testListener.testFinished(null);
	}

	@Test
	public void testCanQueryFlexibleSearchAfterRestart()
	{

		final TitleModel titleModel = modelService.create(TitleModel.class);
		titleModel.setCode("tm");
		titleModel.setName("foo");

		modelService.save(titleModel);

		SearchResult<TitleModel> result = flexibleSearchService.search("SELECT {PK} from {" + TitleModel._TYPECODE + "} WHERE {"
				+ TitleModel.CODE + "}  = ?code", Collections.singletonMap("code", "tm"));

		Assert.assertNotNull(result);
		Assert.assertTrue(CollectionUtils.isNotEmpty(result.getResult()));
		Assert.assertEquals(1, result.getResult().size());

		destroyAndForceStartupCurrentTenant();

		result = flexibleSearchService.search("SELECT {PK} from {" + TitleModel._TYPECODE + "} WHERE {" + TitleModel.CODE
				+ "}  = ?code", Collections.singletonMap("code", "tm"));

		Assert.assertNotNull(result);
		Assert.assertTrue(CollectionUtils.isNotEmpty(result.getResult()));
		Assert.assertEquals(1, result.getResult().size());
	}



	@Test
	public void testJaloPersistenceAfterRestart() throws ConsistencyCheckException
	{
		final Language language = getOrCreateLang("dummy");

		final SessionContext localCtx = jaloSession.createLocalSessionContext();
		try
		{
			localCtx.setLanguage(language);

			final UserGroup boo = UserManager.getInstance().createUserGroup("boo");
			boo.setLocalizedProperty(localCtx, "boo wicked", "boo value");

			final Principal foo = UserManager.getInstance().createCustomer("foo");
			foo.setName("foo value");
			foo.setLocalizedProperty(localCtx, "foo wicked", "foo value");

			final Principal bar = UserManager.getInstance().createCustomer("bar");
			bar.setName("bar value");

			boo.setMembers(Sets.newHashSet(foo, bar));
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}



		destroyAndForceStartupCurrentTenant();
		final SessionContext localCtxAfter = jaloSession.createLocalSessionContext();
		try
		{
			localCtxAfter.setLanguage(language);

			final UserGroup boo = UserManager.getInstance().getUserGroupByGroupID("boo");
			Assert.assertNotNull(boo);
			Assert.assertEquals("boo value", boo.getLocalizedProperty(localCtxAfter, "boo wicked"));

			Assert.assertEquals(2, boo.getMembersCount());

			final Principal foo = UserManager.getInstance().getUserByLogin("foo");
			Assert.assertNotNull(foo);
			Assert.assertEquals("foo value", foo.getName());
			Assert.assertEquals("foo value", foo.getLocalizedProperty("foo wicked"));

			final Principal bar = UserManager.getInstance().getUserByLogin("bar");
			Assert.assertNotNull(foo);

			Assert.assertEquals("bar value", bar.getName());

		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}

	/**
	 * 
	 */
	private void destroyAndForceStartupCurrentTenant()
	{
		final Tenant before = Registry.getCurrentTenantNoFallback();
		Registry.destroyAndForceStartup();
		Assert.assertSame(before, Registry.getCurrentTenantNoFallback());
	}


	@Test
	public void testTenantsAfterRestart()
	{


		final Map<String, Properties> decoratedProperties = new HashMap<String, Properties>();
		final Properties commonProps = new Properties();
		commonProps.setProperty("cluster.id", "0");
		decoratedProperties.put("foo", commonProps);
		decoratedProperties.put("bar", commonProps);

		try
		{

			final MasterTenant before = Registry.getMasterTenant();
			final Map<String, SlaveTenant> beforeMap = Registry.getSlaveTenants();

			destroyAndForceStartupCurrentTenant();

			Assert.assertSame(before, Registry.getMasterTenant());
			Assert.assertEquals(beforeMap.size(), Registry.getSlaveTenants().size());

			final Map<String, SlaveTenant> afterMap = Registry.getSlaveTenants();

			Assert.assertTrue(CollectionUtils.disjunction(beforeMap.keySet(), afterMap.keySet()).isEmpty());
			Assert.assertTrue(CollectionUtils.disjunction(afterMap.keySet(), beforeMap.keySet()).isEmpty());
			for (final Map.Entry<String, SlaveTenant> afterMapEntries : afterMap.entrySet())
			{
				final SlaveTenant slaveAfter = afterMapEntries.getValue();
				final SlaveTenant slaveBefore = beforeMap.get(afterMapEntries.getKey());

				Assert.assertSame(slaveAfter, slaveBefore);// compare values
			}
		}
		finally
		{
			//
		}
	}

	@Test
	public void testPropertyPersistenceAfterRestart() throws ConsistencyCheckException
	{

		MetaInformationManager.getInstance().setAttribute("attribute", "some value");
		MetaInformationManager.getInstance().setProperty("property", "some value");

		destroyAndForceStartupCurrentTenant();

		Assert.assertEquals("some value", MetaInformationManager.getInstance().getAttribute("attribute"));
		Assert.assertEquals("some value", MetaInformationManager.getInstance().getProperty("property"));


	}


	//@Ignore("Should be called onnky manually in standalone mode")
	@Test
	public void testAssignSlaveDataSourceParamsFromMaster() throws Exception
	{
		final String fromJndi = StringUtils.isBlank(Registry.getMasterTenant().getConfig()
				.getParameter(SystemSpecificParams.DB_POOL_FROMJNDI)) ? null : Registry.getMasterTenant().getConfig()
				.getParameter(SystemSpecificParams.DB_POOL_FROMJNDI);
		//		final String prefix = Registry.getMasterTenant().getConfig().getParameter(SystemSpecificParams.DB_TABLEPREFIX);
		final String url = Registry.getMasterTenant().getConfig().getParameter(SystemSpecificParams.DB_URL);
		final String user = Registry.getMasterTenant().getConfig().getParameter(SystemSpecificParams.DB_USERNAME);


		try
		{
			//this might throw some exception 

			final Collection<String> loadedAltIds = fooTenant.getAllAlternativeMasterDataSourceIDs();

			Assert.assertTrue(loadedAltIds.contains("alt1"));
			Assert.assertTrue(loadedAltIds.contains("alt2"));

			for (final HybrisDataSource slaveDataSource : fooTenant.getAllAlternativeMasterDataSources())
			{
				LOG.info(slaveDataSource.getID());
				Assert.assertEquals(fromJndi, slaveDataSource.getJNDIName());
				LOG.info(slaveDataSource.getDatabaseName());
				Assert.assertEquals(url, slaveDataSource.getDatabaseURL());
				//db user is highly db provider dependent
				if (Config.isHSQLDBUsed())
				{
					Assert.assertTrue(user.equalsIgnoreCase(slaveDataSource.getDatabaseUser()));
				}
				else
				{
					Assert.assertTrue(slaveDataSource.getDatabaseUser().startsWith(user));
				}
				LOG.info(slaveDataSource.getDatabaseVersion());
			}


			final Collection<String> loadedSlaveIds = fooTenant.getAllSlaveDataSourceIDs();

			Assert.assertEquals(3, loadedSlaveIds.size());
			Assert.assertTrue(loadedSlaveIds.contains("a"));
			Assert.assertTrue(loadedSlaveIds.contains("b"));
			Assert.assertTrue(loadedSlaveIds.contains("c"));

			for (final HybrisDataSource altDataSource : fooTenant.getAllSlaveDataSources())
			{
				LOG.info(altDataSource.getID());
				Assert.assertEquals(fromJndi, altDataSource.getJNDIName());
				LOG.info(altDataSource.getDatabaseName());
				Assert.assertEquals(url, altDataSource.getDatabaseURL());
				//db user is highly db provider dependent
				if (Config.isHSQLDBUsed())
				{
					Assert.assertTrue(user.equalsIgnoreCase(altDataSource.getDatabaseUser()));
				}
				else
				{
					Assert.assertTrue(altDataSource.getDatabaseUser().startsWith(user));
				}
				LOG.info(altDataSource.getDatabaseVersion());
			}
		}
		finally
		{
			//
		}
	}

	private Language getOrCreateLang(final String isoCode) throws ConsistencyCheckException
	{
		try
		{
			return jaloSession.getC2LManager().getLanguageByIsoCode(isoCode);
		}
		catch (final JaloItemNotFoundException jnfe)
		{
			return jaloSession.getC2LManager().createLanguage(isoCode);
		}
	}

}
