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

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;


/**
 * 
 * *
 * <table>
 * <th>core flag</th>
 * <th>method</th>
 * <tr>
 * <td>null</td>
 * <td>{@link #testCreationWhenCoreNoFlag()}</td>
 * </tr>
 * <tr>
 * <td>false</td>
 * <td>{@link #testCreationWhenCoreFlagFalse()}</td>
 * </tr>
 * <tr>
 * <td>true</td>-
 * <td>{@link #testCreationWhenCoreFlagTrue()}</td>
 * </tr>
 * <tr>
 * </table>
 * 
 * Tests core lazy init flag
 */
public class CoreScopeTenantIgnoreDocReaderTest extends ScopeTenantIgnoreDocReaderTest
{

	protected final static String UNDFINED = "undefined";

	protected String coreflagBefore = UNDFINED;

	@Override
	protected Class<? extends ScopeTenantIgnoreDocReader> getDocReaderClass()
	{
		return CoreScopeTenantIgnoringDocReader.class;
	}

	@Override
	public void doBefore()
	{
		coreflagBefore = Registry.getCurrentTenantNoFallback().getConfig()
				.getParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY);

		Registry.getCurrentTenantNoFallback().getConfig().clearCache();

	}


	@After
	public void unprepare()
	{

		if (UNDFINED != coreflagBefore)
		{
			Registry.getCurrentTenantNoFallback().getConfig()
					.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, coreflagBefore);
		}
	}

	/**
	 * global flag for core lazy-init is empty
	 */
	@Test
	public void testCreationWhenCoreNoFlag()
	{

		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, null);
		prepare();

		//

		assertBeanCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}

	/**
	 * global flag for core lazy-init is true
	 */
	@Test
	public void testCreationWhenCoreFlagTrue()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "true");
		prepare();

		assertBeanNotCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}


	/**
	 * global flag for core lazy-init is false
	 */
	@Test
	public void testCreationWhenCoreFlagFalse()
	{
		Registry.getCurrentTenantNoFallback().getConfig()
				.setParameter(HybrisContextLoaderListener.CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY, "false");
		prepare();

		assertBeanCreated(LAZY_INIT_DEFAULT_BEAN);
		assertBeanNotCreated(LAZY_INIT_TRUE_BEAN);
		assertBeanCreated(LAZY_INIT_FALSE_BEAN);
	}


	@Ignore
	@Override
	public void testCreation()
	{
		//
	}


}
