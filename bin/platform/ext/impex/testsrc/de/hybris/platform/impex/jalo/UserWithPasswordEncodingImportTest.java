/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *
 */
package de.hybris.platform.impex.jalo;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * This test class contains all tests considering the import process of the impex extension.
 */
@IntegrationTest
public class UserWithPasswordEncodingImportTest extends ServicelayerBaseTest
{

	@Resource
	private ImportService importService;

	@Resource
	private DefaultUserService defaultUserService;

	@Before
	public void setUp() throws Exception
	{
		defaultUserService.setDefaultPasswordEncoding("sha-512");
	}

	@After
	public void tearDown() throws Exception
	{

		defaultUserService.setDefaultPasswordEncoding(Config.getParameter("default.password.encoding"));

	}

	@Test
	public void shouldImportUserWithSHA512PasswordEncoding() throws InterruptedException
	{
		final String script = "REMOVE Customer;uid[unique=true]\n" + ";test\n" + "\n"
				+ "INSERT_UPDATE Customer;uid[unique=true];password;name\n" + ";test;test;test";

		for (int i = 0; i < 20; i++)
		{

			final ImportConfig config = new ImportConfig();
			final ImpExResource resource = new StreamBasedImpExResource(new ByteArrayInputStream(script.getBytes()),
					CSVConstants.HYBRIS_ENCODING);
			config.setScript(resource);
			config.setFailOnError(true);
			config.setValidationMode(ImportConfig.ValidationMode.STRICT);

			final ImportResult importResult = importService.importData(config);

			assertThat(importResult.isFinished()).isTrue();
			assertThat(importService.collectImportErrors(importResult)).isEmpty();
		}
	}
}
