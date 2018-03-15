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
package de.hybris.platform.util;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.fest.assertions.Assertions;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;


/**
 *
 */
@UnitTest
public class CoreUtilitiesTest
{

	private static final String EXISTING_MANAGER_CLASS_NAME = de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager.class
			.getName();

	private static final Logger LOG = Logger.getLogger(CoreUtilitiesTest.class.getName());

	private CoreUtilities utils = null;

	@Test
	public void testGetAllConfiguredExtensionNames()
	{
		utils = new CoreUtilities(ConfigUtil.getPlatformConfig(CoreUtilitiesTest.class), true, 7)
		{

			@Override
			boolean isCorePropertiesNotLoaded()
			{
				return true;
			}

			@Override
			Map<String, Class> getInstalledExtensionClassMapping()
			{
				return getInstalledExtensionClassMappingNoCache();
			}

			@Override
			public List<String> getAllConfiguredExtensionNames() throws IllegalStateException
			{
				return Lists.newArrayList(getInstalledExtensionClassMappingNoCache().keySet());
			}

			@Override
			Properties loadPlatformPropertiesOnce(final PlatformConfig config)
			{

				final Properties props = new Properties();
				props.putAll(Collections.singletonMap("extension.envs", "foo," + EXISTING_MANAGER_CLASS_NAME + ";" + //
						"bar," + EXISTING_MANAGER_CLASS_NAME + ";" + //
						"baz," + EXISTING_MANAGER_CLASS_NAME + ";" + //
						"fyie," + EXISTING_MANAGER_CLASS_NAME + ";"//

				));
				return props;
			}
		};

		LOG.info(Joiner.on(";").join("Extensions :", utils.getAllConfiguredExtensionNames()));


		Assertions.assertThat(utils.getAllConfiguredExtensionNames()).containsOnly("foo", "bar", "baz", "fyie");

	}


}
