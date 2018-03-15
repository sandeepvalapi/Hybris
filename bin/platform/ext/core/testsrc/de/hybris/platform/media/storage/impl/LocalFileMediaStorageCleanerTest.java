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
package de.hybris.platform.media.storage.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.media.services.MediaStorageInitializer;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/MediaStorageStrategy-context.xml" })
public class LocalFileMediaStorageCleanerTest
{
	private static final Logger LOG = Logger.getLogger(LocalFileMediaStorageCleanerTest.class);

	@Resource(name = "localFileMediaStorageCleaner")
	private MediaStorageInitializer mediaStorageCleaner;
	private final File tempStorage = new File(System.getProperty("java.io.tmpdir") + "/hybrisMediaStorageTest");


	@Before
	public void setUp() throws Exception
	{
		mediaStorageCleaner.onInitialize();
	}

	@After
	public void tearDown() throws Exception
	{
		mediaStorageCleaner.onInitialize();
	}

	@Test
	public void shouldCleanStorageOnInitialization()
	{
		// given
		fillStorageWithTestFiles();
		assertThat(tempStorage.list()).hasSize(10);

		// when
		mediaStorageCleaner.onInitialize();

		// then
		assertThat(tempStorage.list()).hasSize(0);
	}

	private void fillStorageWithTestFiles()
	{
		createStorageFolder();

		for (int i = 0; i < 10; i++)
		{
			final String fileName = RandomStringUtils.randomAlphabetic(5);
			final File file = new File(tempStorage, fileName + ".mediaTestFile");
			try
			{
				file.createNewFile();
			}
			catch (final IOException e)
			{
				LOG.error(
						"Cannot create test file:  " + file.getName() + ", path: " + file.getAbsolutePath() + " (reason: "
								+ e.getMessage() + ")", e);
			}
		}
	}

	private void createStorageFolder()
	{
		if (!tempStorage.exists())
		{
			tempStorage.mkdirs();
		}
	}

}
