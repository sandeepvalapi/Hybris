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
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.media.storage.ConfigValueConverter;
import de.hybris.platform.media.storage.MediaStorageConfigService.GlobalMediaStorageConfig;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;
import de.hybris.platform.media.storage.impl.DefaultMediaStorageConfigService.DefaultSettingKeys;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultMediaStorageConfigServiceTest
{
	@Mock
	private ConfigValueMappingRegistry valueMappingRegistry;
	private DefaultMediaStorageConfigService service;
	private Map<String, String> properties;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		given(valueMappingRegistry.getMappings()).willReturn(prepareConverterMappings());

		service = new DefaultMediaStorageConfigService(valueMappingRegistry)
		{
			@Override
			Map<String, String> getPropertiesWithPrefix(final String prefix)
			{
				final Map<String, String> params = new HashMap<String, String>(properties);
				for (final String key : properties.keySet())
				{
					if (!key.startsWith(prefix))
					{
						params.remove(key);
					}
				}
				return params;
			}

			@Override
			void registerConfigChangeListener()
			{
				// no need to implement;
			}
		};

		properties = new HashMap<String, String>();
		properties.putAll(getDefaults());
		properties.putAll(getGlobalSettings());
		properties.putAll(getFoldersSettings());
		service.init();
	}

	private Map<String, ConfigValueConverter> prepareConverterMappings()
	{
		final Map<String, ConfigValueConverter> result = new HashMap<String, ConfigValueConverter>();
		result.put("url.strategy", new IterableValueConverter());
		result.put("secured", new BooleanValueConverter());
		result.put("local.cache", new BooleanValueConverter());
		result.put("hashing.depth", new IntegerValueConverter());
		return result;
	}


	@Test
	public void shouldCacheConfigForNotDirectlyConfiguredFolderWhenRequestedFirstTime()
	{
		// given
		final String folderQualifier = "notConfigured";
		final MediaFolderConfig config = service.getConfigForFolder(folderQualifier);

		// when
		final MediaFolderConfig config2 = service.getConfigForFolder(folderQualifier);

		// then
		assertThat(config).isSameAs(config2);
	}


	@Test
	public void shouldThrowIllegalStateExceptionWhenOneOfMandatoryDefaultSettingsIsMissed()
	{
		for (final DefaultSettingKeys defaultSettingKey : DefaultSettingKeys.values())
		{
			// given
			final String wholeKey = "media.default." + defaultSettingKey.getKey();
			final String backupValue = properties.get(wholeKey);
			properties.remove(wholeKey);

			try
			{
				// when
				service.init();
				fail("Should throw IllegalStateException");
			}
			catch (final IllegalStateException e)
			{
				// then
				assertThat(e).hasMessage(
						"Incorrect media storage configuration - mandatory default setting not present [key: "
								+ defaultSettingKey.getKey() + "]");
			}

			properties.put(wholeKey, backupValue);
		}
	}

	@Test
	public void shouldReturnConfigForDefaultStrategy()
	{
		// given
		final String defaultStrategyId = service.getDefaultStrategyId();

		// when
		final GlobalMediaStorageConfig storageConfig = service.getGlobalSettingsForStrategy(defaultStrategyId);

		// then
		assertThat(storageConfig.getKeys()).hasSize(4).containsOnly("bucketId", "accessKeyId", "secretAccessKey", "secured");
		assertThat(storageConfig.getParameter("bucketId")).isEqualTo("mybucket");
		assertThat(storageConfig.getParameter("accessKeyId")).isEqualTo("123456");
		assertThat(storageConfig.getParameter("secretAccessKey")).isEqualTo("abcdef");
	}


	@Test
	public void shouldReturnConfigForNotDirectlyConfiguredFolder()
	{
		// given
		final String folderQualifier = "notConfigured";

		// when
		final MediaFolderConfig config = service.getConfigForFolder(folderQualifier);

		// then
		assertThat(config).isNotNull();
		assertThat(config.getFolderQualifier()).isEqualTo(folderQualifier);
		assertThat(config.getStorageStrategyId()).isEqualTo("someStrategy");
		assertThat(config.getURLStrategyIds()).hasSize(3).containsOnly("someStrategy", "someOtherOne", "andAnother");
		assertThat(config.isSecured()).isTrue();
		assertThat(config.isLocalCacheEnabled()).isTrue();
		assertThat(config.getHashingDepth()).isEqualTo(2);
		assertThat(config.getParameter("secretAccessKey")).isEqualTo("abcdef");
		assertThat(config.getParameter("bucketId")).isEqualTo("mybucket");
		assertThat(config.getParameter("accessKeyId")).isEqualTo("123456");
	}

	@Test
	public void shouldBuildConfigForFolder()
	{
		// given
		final String folderQualifier = "folderOther";

		// when
		final MediaFolderConfig config = service.getConfigForFolder(folderQualifier);

		// then
		assertThat(config).isNotNull();
		assertThat(config.getFolderQualifier()).isEqualTo(folderQualifier);
		assertThat(config.getStorageStrategyId()).isEqualTo("someStrategy");
		assertThat(config.getURLStrategyIds()).hasSize(3).containsOnly("localStrategy", "someReallyFancyStrategy",
				"MyDummyStrategy");
		assertThat(config.isSecured()).isFalse();
		assertThat(config.isLocalCacheEnabled()).isTrue();
		assertThat(config.getHashingDepth()).isEqualTo(2);
		assertThat(config.getParameter("secretAccessKey")).isEqualTo("abcdef");
		assertThat(config.getParameter("bucketId")).isEqualTo("mybucket");
		assertThat(config.getParameter("accessKeyId")).isEqualTo("123456");
	}

	@Test
	public void shouldBuildConfigForFolderNameWithHyphen()
	{
		// given
		final String folderQualifier = "folder-with-hyphen";

		// when
		final MediaFolderConfig config = service.getConfigForFolder(folderQualifier);

		// then
		assertThat(config).isNotNull();
		assertThat(config.getFolderQualifier()).isEqualTo(folderQualifier);
		assertThat(config.getStorageStrategyId()).isEqualTo("someStrategy");
		assertThat(config.getURLStrategyIds()).hasSize(3).containsOnly("localStrategy", "someReallyFancyStrategy",
				"My-Dummy-Strategy");
		assertThat(config.isSecured()).isFalse();
		assertThat(config.isLocalCacheEnabled()).isTrue();
		assertThat(config.getHashingDepth()).isEqualTo(2);
		assertThat(config.getParameter("secretAccessKey")).isEqualTo("abcdef");
		assertThat(config.getParameter("bucketId")).isEqualTo("mybucket");
		assertThat(config.getParameter("accessKeyId")).isEqualTo("123456");
		assertThat(config.getParameter("my-key")).isEqualTo("my-value");
	}

	private Map<String, String> getDefaults()
	{
		final Map<String, String> result = new HashMap<String, String>();
		result.put("media.default.storage.strategy", "someStrategy");
		result.put("media.default.url.strategy", "someStrategy,someOtherOne,andAnother");
		result.put("media.default.local.cache", "true");
		result.put("media.default.local.cache.rootCacheFolder", "cache");
		result.put("media.default.hashing.depth", "2");
		result.put("media.default.storage.location.hash.salt", "123456789");
		result.put("media.default.secured", "false");

		return result;
	}

	private Map<String, String> getGlobalSettings()
	{
		final Map<String, String> result = new HashMap<String, String>();
		result.put("media.globalSettings.localFileMediaStorageStrategy.cleanOnInit", "true");
		result.put("media.globalSettings.someStrategy.bucketId", "mybucket");
		result.put("media.globalSettings.someStrategy.accessKeyId", "123456");
		result.put("media.globalSettings.someStrategy.secretAccessKey", "abcdef");
		result.put("media.globalSettings.someStrategy.secured", "true");

		return result;
	}

	private Map<String, String> getFoldersSettings()
	{
		final Map<String, String> result = new HashMap<String, String>();
		result.put("media.folder.folderFoo.cleanOnInit", "true");
		result.put("media.folder.folderDoom.storage.strategy", "doomStorageStrategy");
		result.put("media.folder.folderBar.bucketId", "mybucket");
		result.put("media.folder.folderFoo.accessKeyId", "123456");
		result.put("media.folder.folderOther.secretAccessKey", "abcdef");
		result.put("media.folder.folderOther.secured", "false");
		result.put("media.folder.folderOther.url.strategy", "localStrategy,someReallyFancyStrategy,,MyDummyStrategy");
		result.put("media.folder.folder-with-hyphen.secretAccessKey", "abcdef");
		result.put("media.folder.folder-with-hyphen.secured", "false");
		result.put("media.folder.folder-with-hyphen.url.strategy", "localStrategy,someReallyFancyStrategy,,My-Dummy-Strategy");
		result.put("media.folder.folder-with-hyphen.my-key", "my-value");

		return result;
	}
}
