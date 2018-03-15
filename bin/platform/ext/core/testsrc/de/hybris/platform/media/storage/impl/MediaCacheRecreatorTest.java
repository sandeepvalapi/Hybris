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
import static org.mockito.BDDMockito.given;

import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;
import de.hybris.platform.media.storage.impl.DefaultMediaStorageConfigService.DefaultSettingKeys;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.region.CacheRegion;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/MediaCacheRegion-context.xml" }, inheritLocations = false)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MediaCacheRecreatorTest
{
	@Resource
	private CacheController cacheController;
	@Resource
	private String defaultCacheFolderName;
	@Resource
	private String config2CacheFolderName;
	@Resource
	private MediaCacheRecreator cacheRecreator;

	@Mock
	private MediaFolderConfig config1, config2;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		createRandomCachedFiles(10, defaultCacheFolderName);
		createRandomCachedFiles(5, config2CacheFolderName);
		assertThat(cacheController.getRegions()).isNotNull().hasSize(1);
	}

	private void createRandomCachedFiles(final int num, final String folderName) throws IOException
	{
		for (int i = 0; i < num; i++)
		{
			final String location = RandomStringUtils.randomAlphabetic(10);
			Files.createTempFile(Paths.get(System.getProperty("java.io.tmpdir"), folderName),
					new String(Base64.encode(location.getBytes())) + DefaultLocalMediaFileCacheService.CACHE_FILE_NAME_DELIM, ".bin");
		}
	}


	@After
	public void tearDown() throws Exception
	{
		cleanCacheFolder(defaultCacheFolderName);
		cleanCacheFolder(config2CacheFolderName);
		getMediaCacheRegion().clearCache();
	}

	private void cleanCacheFolder(final String folderName) throws IOException
	{
		Files.walkFileTree(Paths.get(System.getProperty("java.io.tmpdir"), folderName), new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException
			{
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException
			{
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	@Test
	public void shouldRecreateCacheFromExistingCachedFileEntriesUsingDefaultCacheFolder()
	{
		// given
		final List<MediaFolderConfig> configs = Collections.EMPTY_LIST;

		// when
		cacheRecreator.recreateCache(defaultCacheFolderName, configs);

		// then
		assertThat(getMediaCacheRegion().getMaxReachedSize()).isEqualTo(10);
	}


	@Test
	public void shouldRecreateCacheFromExistingCachedFileEntriesUsingDefaultCacheFolderAndConfiguredFolder()
	{
		// given
		given(Boolean.valueOf(config1.isLocalCacheEnabled())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(config2.isLocalCacheEnabled())).willReturn(Boolean.TRUE);
		given(config1.getParameter(DefaultSettingKeys.LOCAL_CACHE_ROOT_FOLDER_KEY.getKey())).willReturn(defaultCacheFolderName);
		given(config2.getParameter(DefaultSettingKeys.LOCAL_CACHE_ROOT_FOLDER_KEY.getKey())).willReturn(config2CacheFolderName);

		// when
		cacheRecreator.recreateCache(defaultCacheFolderName, Lists.newArrayList(config1, config2));

		// then
		assertThat(getMediaCacheRegion().getMaxReachedSize()).isEqualTo(15);
	}

	private CacheRegion getMediaCacheRegion()
	{
		final Optional<CacheRegion> found = Iterables.tryFind(cacheController.getRegions(), new Predicate<CacheRegion>()
		{

			@Override
			public boolean apply(@Nullable final CacheRegion input)
			{
				return (input instanceof MediaCacheRegion);
			}
		});


		assertThat(found.isPresent()).overridingErrorMessage("Media cache region not found via controller").isTrue();

		return found.get();
	}
}
