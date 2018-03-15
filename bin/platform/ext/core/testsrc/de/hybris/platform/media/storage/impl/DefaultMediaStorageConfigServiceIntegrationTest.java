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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;
import de.hybris.platform.media.storage.MediaStorageConfigService.GlobalMediaStorageConfig;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultMediaStorageConfigServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource(name = "mediaStorageConfigService")
	private DefaultMediaStorageConfigService storageConfigService;
	private final Map<String, String> backupDefaults = new HashMap<String, String>();

	@Before
	public void setUp() throws Exception
	{
		addToBackup("media.folder.foo.hashing.depth");
		addToBackup("media.folder.bar.hashing.depth");
		addToBackup("media.globalSettings.s3MediaStorageStrategy.accessKeyId");
		addToBackup("media.globalSettings.s3MediaStorageStrategy.bucketId");
		addToBackup("media.folder.bar.accessKey");
		addToBackup("media.folder.bar.storage.strategy");
		addToBackup("media.default.storage.strategy");
		addToBackup("media.globalSettings.localFileMediaStorageStrategy.newKey");
		addToBackup("media.folder.images.secured");
		addToBackup("media.folder.privatemedia.secured");

		Config.setParameter("media.folder.foo.hashing.depth", "0");
		Config.setParameter("media.folder.bar.hashing.depth", "1");
		Config.setParameter("media.globalSettings.s3MediaStorageStrategy.accessKeyId", "1234");
		Config.setParameter("media.globalSettings.s3MediaStorageStrategy.bucketId", "fooBar");
		Config.setParameter("media.folder.bar.accessKey", "5678");
		Config.setParameter("media.folder.bar.storage.strategy", "s3MediaStorageStrategy");
		Config.setParameter("media.default.storage.strategy", "localFileMediaStorageStrategy");
		Config.setParameter("media.globalSettings.localFileMediaStorageStrategy.newKey", null);
		Config.setParameter("media.folder.images.secured", "false");
		Config.setParameter("media.folder.privatemedia.secured", "true");
	}

	private void addToBackup(final String key)
	{
		backupDefaults.put(key, Config.getParameter(key));
	}

	@After
	public void tearDown() throws Exception
	{
		for (final Map.Entry<String, String> entry : backupDefaults.entrySet())
		{
			Config.setParameter(entry.getKey(), entry.getValue());
		}
	}

	@Test
	public void shouldUpdateGlobalStrategySettingsOnConfigChange()
	{
		// given
		final String strategyId = storageConfigService.getDefaultStrategyId();
		assertThat(strategyId).isNotNull();
		GlobalMediaStorageConfig settings = storageConfigService.getGlobalSettingsForStrategy(strategyId);
		assertThat(settings).isNotNull();
		assertThat(settings.getParameter("newKey")).isNull();
		Config.setParameter("media.globalSettings.localFileMediaStorageStrategy.newKey", "fooBar");

		// when
		settings = storageConfigService.getGlobalSettingsForStrategy(strategyId);

		// then
		assertThat(settings).isNotNull();
		assertThat(settings.getParameter("newKey")).isEqualTo("fooBar");
	}

	@Test
	public void shouldUpdateDefaultSettingsOnConfigChange()
	{
		// given
		String strategyId = storageConfigService.getDefaultStrategyId();
		assertThat(strategyId).isEqualTo("localFileMediaStorageStrategy");
		Config.setParameter("media.default.storage.strategy", "fooBarStrategy");

		// when
		strategyId = storageConfigService.getDefaultStrategyId();

		// then
		assertThat(strategyId).isEqualTo("fooBarStrategy");
	}

	@Test
	public void folderDepthForFooFolderShouldBeEqual0()
	{
		// given
		final String folderQualifier = "foo";
		final MediaFolderConfig configForFolder = storageConfigService.getConfigForFolder(folderQualifier);

		// when
		final int value = configForFolder.getHashingDepth();

		// then
		assertThat(value).isEqualTo(0);
	}

	@Test
	public void folderDepthForBarFolderShouldBeEqual1()
	{
		// given
		final String folderQualifier = "bar";
		final MediaFolderConfig configForFolder = storageConfigService.getConfigForFolder(folderQualifier);

		// when
		final int value = configForFolder.getHashingDepth();

		// then
		assertThat(value).isEqualTo(1);
	}

	@Test
	public void folderDepthForNonExistentFolderConfigurationShouldBeDefaultTwo()
	{
		// given
		final String folderQualifier = "zoo";
		final MediaFolderConfig configForFolder = storageConfigService.getConfigForFolder(folderQualifier);

		// when
		final int value = configForFolder.getHashingDepth();

		// then
		assertThat(value).isEqualTo(2);
	}

	@Test
	public void shouldGetGlobalSettingIfFolderDoesNotHaveSpecificConfig()
	{
		// given
		final String folderQualifier = "bar";
		final String key = "bucketId";
		final MediaFolderConfig configForFolder = storageConfigService.getConfigForFolder(folderQualifier);

		// when
		final String value = configForFolder.getParameter(key);

		// then
		assertThat(value).isNotNull().isEqualTo("fooBar");
	}

	@Test
	public void shouldGetSpecificSettingIfFolderDoesHaveSpecificConfig()
	{
		// given
		final String folderQualifier = "bar";
		final String key = "accessKey";
		final MediaFolderConfig configForFolder = storageConfigService.getConfigForFolder(folderQualifier);

		// when
		final String value = configForFolder.getParameter(key);

		// then
		assertThat(value).isNotNull().isEqualTo("5678");
	}

	@Test
	public void shouldGetSecuredFoldersFromProperties()
	{
		// when
		final Collection<String> securedFolders = storageConfigService.getSecuredFoldersFromProperties();

		// then
		assertThat(securedFolders).contains("privatemedia");
		assertThat(securedFolders).excludes("images");
	}
}
