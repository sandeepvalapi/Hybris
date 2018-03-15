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

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 *
 */
@UnitTest
public class SlaveTenantTest
{

	@Mock
	private MasterTenant master;


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testLoadCustomExtensionListAddsCoreIfNeeded()
	{
		final Properties fooProps = new Properties();
		fooProps.put("cluster.id", "0");
		fooProps.put("cronjob.timertask.loadonstartup", "false");
		fooProps.put("allowed.extensions", "deliveryzone;commons;validation;europe1;catalog;");


		final SlaveTenant slaveTenant = new SlaveTenant("foo", fooProps);



		final Collection given = slaveTenant.getTenantSpecificExtensionNames();
		final Collection expected = Arrays.asList("core", "deliveryzone", "commons", "validation", "europe1", "catalog");
		Assert.assertTrue(CollectionUtils.subtract(expected, given).isEmpty());
		Assert.assertTrue(CollectionUtils.subtract(given, expected).isEmpty());

	}


	@Test
	public void testLoadDefaultExtensionList()
	{
		final Properties fooProps = new Properties();
		fooProps.put("cluster.id", "0");
		fooProps.put("cronjob.timertask.loadonstartup", "false");

		try
		{
			final SlaveTenant slaveTenant = new SlaveTenant("foo", fooProps)
			{
				@Override
				protected MasterTenant getMasterTenant()
				{
					return master;
				}
			};
			slaveTenant.getTenantSpecificExtensionNames();
			Assert.fail("no extensions given ");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
	}
}
