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
package de.hybris.platform.servicelayer.impex;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.impl.ImportCronJobResult;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVConstants;

import java.io.ByteArrayInputStream;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests importing conflicting items - see PLA-13426.
 */
@IntegrationTest
public class ConflictingItemsImportTest extends ServicelayerTest
{

	private static final String ISOCODE_EN = "en";
	private static final String TEST_CODE = "testCode";
	private static final String TEST_NAME = "testName";
	private static final String OTHER_NAME = "otherName";
	@Resource
	private ModelService modelService;

	@Resource
	private ImportService importService;

	private TitleModel title;
	private Language language;

	@Before
	public void setUp()
	{
		language = getOrCreateLanguage(ISOCODE_EN);
		title = new TitleModel();
		title.setCode(TEST_CODE);
		title.setName(TEST_NAME, language.getLocale());
		modelService.save(title);
	}

	@After
	public void tearDown()
	{
		modelService.remove(title);
	}

	@Test
	public void shouldNotUpdateWhenInsertingConflictingItemsSingleThreaded()
	{
		final ImportConfig config = prepareImportConfig();
		config.setMaxThreads(1);
		final ImportCronJobResult result = (ImportCronJobResult) importService.importData(config);
		Assert.assertTrue(result.isError());
		modelService.refresh(title);
		Assert.assertEquals(TEST_NAME, title.getName(language.getLocale()));
	}

	@Test
	public void shouldNotUpdateWhenInsertingConflictingItemsMultiThreaded()
	{
		final ImportConfig config = prepareImportConfig();
		config.setMaxThreads(2);
		final ImportCronJobResult result = (ImportCronJobResult) importService.importData(config);
		Assert.assertTrue(result.isError());
		Assert.assertTrue(result.hasUnresolvedLines());
		modelService.refresh(title);
		Assert.assertEquals(TEST_NAME, title.getName(language.getLocale()));
	}

	private ImportConfig prepareImportConfig()
	{
		final ImportConfig config = new ImportConfig();
		final ImpExResource script = new StreamBasedImpExResource(new ByteArrayInputStream(("INSERT Title;code[unique=true];name["
				+ ISOCODE_EN + "]\n;" + TEST_CODE + ";" + OTHER_NAME).getBytes()), CSVConstants.HYBRIS_ENCODING);
		config.setScript(script);
		config.setFailOnError(true);
		config.setDumpingEnabled(true);
		return config;
	}
}
