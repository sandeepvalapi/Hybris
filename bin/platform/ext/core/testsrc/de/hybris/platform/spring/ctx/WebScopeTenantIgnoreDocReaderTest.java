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
package de.hybris.platform.spring.ctx;

import de.hybris.platform.core.Registry;
import de.hybris.platform.spring.HybrisContextLoaderListener;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;



/**
 * 
 * 
 * 
 * <table>
 * <th>core flag</th>
 * <th>web flag</th>
 * <th>method</th>
 * <tr>
 * <td>null</td>
 * <td>null</td>
 * <td>{@link #testCreationWhenCoreNoFlagWebNoFlag()}</td>
 * </tr>
 * <tr>
 * <td>false</td>
 * <td>null</td>
 * <td>{@link #testCreationWhenCoreFalseWebNoFlag()}</td>
 * </tr>
 * <tr>
 * <td>true</td>
 * <td>null</td>
 * <td>{@link #testCreationWhenCoreTrueWebNoFlag()}</td>
 * </tr>
 * <tr>
 * <td>null</td>
 * <td>false</td>
 * <td>{@link #testCreationWhenCoreNoFlagWebFalse()}</td>
 * </tr>
 * <tr>
 * <td>false</td>
 * <td>false</td>
 * <td>{@link #testCreationWhenCoreFalseWebFalse()}</td>
 * </tr>
 * <tr>
 * <td>true</td>
 * <td>false</td>
 * <td>{@link #testCreationWhenCoreTrueWebFalse()}</td>
 * </tr>
 * <tr>
 * <td>null</td>
 * <td>true</td>
 * <td>{@link #testCreationWhenCoreNoFlagWebTrue()}</td>
 * </tr>
 * <tr>
 * <td>false</td>
 * <td>true</td>
 * <td>{@link #testCreationWhenCoreFalseWebTrue()}</td>
 * </tr>
 * <tr>
 * <td>true</td>
 * <td>true</td>
 * <td>{@link #testCreationWhenCoreTrueWebTrue()}</td>
 * </tr>
 * </table>
 * 
 * tests web and core lazy init flag
 */
@Ignore
public class WebScopeTenantIgnoreDocReaderTest extends CoreScopeTenantIgnoreDocReaderTest
{


	private static final Logger LOG = Logger.getLogger(WebScopeTenantIgnoreDocReaderTest.class.getName());


	protected String webflagBefore = UNDFINED;

	@Override
	protected Class<? extends ScopeTenantIgnoreDocReader> getDocReaderClass()
	{
		return WebScopeTenantIgnoreDocReader.class;
	}

	@Override
	public void doBefore()
	{
		LOG.info("Flags before web : "
				+ Registry.getCurrentTenantNoFallback().getConfig()
						.getParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY)
				+ " core "
				+ Registry.getCurrentTenantNoFallback().getConfig()
						.getParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY));
		super.doBefore();
		webflagBefore = Registry.getCurrentTenantNoFallback().getConfig()
				.getParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY);

		Registry.getCurrentTenantNoFallback().getConfig().clearCache();

	}


	@Override
	@After
	public void unprepare()
	{
		super.unprepare();
		if (UNDFINED != webflagBefore)
		{
			Registry.getCurrentTenantNoFallback().getConfig()
					.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, webflagBefore);
		}

		LOG.info("Flags after web : "
				+ Registry.getCurrentTenantNoFallback().getConfig()
						.getParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY)
				+ " core "
				+ Registry.getCurrentTenantNoFallback().getConfig()
						.getParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY));
	}






	@Test
	public void testCreationWhenCoreNoFlagWebNoFlag()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, null);
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, null);
		prepare();

		//

		assertBeanNotCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}

	@Test
	public void testCreationWhenCoreTrueWebNoFlag()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "true");
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, null);
		prepare();

		//

		assertBeanNotCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}

	@Test
	public void testCreationWhenCoreFalseWebNoFlag()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "false");
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, null);
		prepare();

		//

		assertBeanNotCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}


	@Test
	public void testCreationWhenCoreNoFlagWebFalse()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, null);
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "false");
		prepare();

		//

		assertBeanCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}

	@Test
	public void testCreationWhenCoreFalseWebFalse()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "false");
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "false");
		prepare();

		//

		assertBeanCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}

	@Test
	public void testCreationWhenCoreTrueWebFalse()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "true");
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "false");
		prepare();

		//

		assertBeanNotCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}


	@Test
	public void testCreationWhenCoreNoFlagWebTrue()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, null);
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "true");
		prepare();

		//

		assertBeanNotCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}

	@Test
	public void testCreationWhenCoreFalseWebTrue()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "false");
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "true");
		prepare();

		//

		assertBeanNotCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}

	@Test
	public void testCreationWhenCoreTrueWebTrue()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "true");
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "true");
		prepare();

		//

		assertBeanNotCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}

	@Ignore
	@Override
	public void testCreation()
	{
		//
	}

	@Ignore
	@Override
	public void testCreationWhenCoreFlagFalse()
	{
		//
	}

	@Ignore
	@Override
	public void testCreationWhenCoreFlagTrue()
	{
		//
	}

	@Ignore
	@Override
	public void testCreationWhenCoreNoFlag()
	{
		//
	}
}
