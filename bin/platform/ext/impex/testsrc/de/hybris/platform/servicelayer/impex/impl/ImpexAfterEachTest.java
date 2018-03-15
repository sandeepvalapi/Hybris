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

package de.hybris.platform.servicelayer.impex.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ImpexAfterEachTest extends ServicelayerTest
{
	@Resource
	private ImportService importService;
	@Resource
	private ModelService modelService;

	private final PropertyConfigSwitcher distributedImpexFlag = new PropertyConfigSwitcher(
			ImportService.DISTRIBUTED_IMPEX_GLOBAL_FLAG);

	private final String INCORRECT_AFTER_EACH_IMPEX = "INSERT_UPDATE Product; code[unique = true];catalogversion(catalog(id), version)[unique = true, default = myCatalog:myCatVersion]\n"
			+ "\"#% afterEach: script with error\"\n" //
			+ ";1\n" //
			+ ";2\n" //
			+ ";3\n" //
			+ "#%afterEach:end";

	private final String CORRECT_IMPEX = "INSERT_UPDATE Product; code[unique = true];catalogversion(catalog(id), version)[unique = true, default = myCatalog:myCatVersion]\n"
			+ ";1\n" //
			+ ";2\n" //
			+ ";3";

	@Before
	public void setUp() throws Exception
	{
		final CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId("myCatalog");

		final CatalogVersionModel catVersion = modelService.create(CatalogVersionModel.class);
		catVersion.setCatalog(cat);
		catVersion.setVersion("myCatVersion");

		modelService.saveAll();
		distributedImpexFlag.switchToValue("false");
	}

	@After
	public void after()
	{
		distributedImpexFlag.switchBackToDefault();
	}

	@Test
	public void shouldImportScriptWithoutAfterEachInSingleThreadedMode()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "false");

		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(CORRECT_IMPEX.getBytes()),
				CSVConstants.HYBRIS_ENCODING);

		final ImportConfig config = configure(mediaRes);
		config.setMaxThreads(1);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
	}

	@Test
	public void shouldEndWithErrorIfAfterEachFailsInSingleThreadedMode()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "false");

		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(INCORRECT_AFTER_EACH_IMPEX.getBytes()),
				CSVConstants.HYBRIS_ENCODING);

		final ImportConfig config = configure(mediaRes);
		config.setMaxThreads(1);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isTrue();
	}

	@Test
	public void shouldEndWithErrorIfAfterEachFailsInMultiThreadedMode()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "false");

		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(INCORRECT_AFTER_EACH_IMPEX.getBytes()),
				CSVConstants.HYBRIS_ENCODING);

		final ImportConfig config = configure(mediaRes);
		config.setMaxThreads(2);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isTrue();
	}

	private ImportConfig configure(final ImpExResource mediaRes)
	{
		final ImportConfig config = new ImportConfig();
		config.setLegacyMode(Boolean.FALSE);
		config.setSynchronous(true);
		config.setFailOnError(true);
		config.setScript(mediaRes);
		config.setRemoveOnSuccess(false);
		return config;
	}
}
