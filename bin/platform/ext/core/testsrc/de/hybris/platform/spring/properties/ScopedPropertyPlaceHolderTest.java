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
package de.hybris.platform.spring.properties;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 *
 */
@UnitTest
public class ScopedPropertyPlaceHolderTest
{

	private static final String GLOBAL_BEAN = "globalBean";
	private static final String APP_BEAN = "appBean";

	@Test
	public void testEvaluateGlobalProperty()
	{

		final ApplicationContext globalCtx = new ClassPathXmlApplicationContext("classpath:test/global-placeholder-test-spring.xml");

		Assert.assertNotNull(globalCtx.getBean(GLOBAL_BEAN, de.hybris.platform.spring.properties.beans.PlaceholderBean.class));

		final de.hybris.platform.spring.properties.beans.PlaceholderBean bean = globalCtx.getBean(GLOBAL_BEAN,
				de.hybris.platform.spring.properties.beans.PlaceholderBean.class);

		Assert.assertEquals("global-bar", bean.getValue());

	}


	@Test
	public void testEvaluateChildProperty()
	{

		final ApplicationContext globalCtx = new ClassPathXmlApplicationContext("classpath:test/global-placeholder-test-spring.xml");

		final ApplicationContext childCtx = new ClassPathXmlApplicationContext(new String[]
		{ "classpath:test/application-placeholder-test-spring.xml" }, globalCtx);

		Assert.assertNotNull(childCtx.getBean(APP_BEAN, de.hybris.platform.spring.properties.beans.PlaceholderBean.class));

		final de.hybris.platform.spring.properties.beans.PlaceholderBean bean = childCtx.getBean(APP_BEAN,
				de.hybris.platform.spring.properties.beans.PlaceholderBean.class);

		Assert.assertEquals("app-bar", bean.getValue());

	}


	@Test
	public void testShouldFailOnUnresolvedProperty()
	{

		final ApplicationContext globalCtx = new ClassPathXmlApplicationContext("classpath:test/global-placeholder-test-spring.xml");

		Boolean error = null;
		try
		{
			new ClassPathXmlApplicationContext(new String[]
			{ "classpath:test/application-placeholder-fail-test-spring.xml" }, globalCtx);

		}
		catch (final BeanDefinitionStoreException e)
		{
			error = Boolean.TRUE;
		}

		Assert.assertEquals("should fail on unresolved property", Boolean.TRUE, error);

	}

}
