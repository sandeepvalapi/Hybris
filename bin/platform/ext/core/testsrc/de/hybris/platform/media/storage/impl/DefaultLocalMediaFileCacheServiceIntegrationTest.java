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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.media.storage.LocalMediaFileCacheService;
import de.hybris.platform.media.storage.LocalMediaFileCacheService.StreamGetter;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;
import de.hybris.platform.media.storage.impl.DefaultMediaStorageConfigService.DefaultSettingKeys;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.MediaUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.CountingInputStream;


@IntegrationTest
public class DefaultLocalMediaFileCacheServiceIntegrationTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(DefaultLocalMediaFileCacheServiceIntegrationTest.class);
	private static final String TENANT_ID = Registry.getCurrentTenantNoFallback().getTenantID();
	private static final String CACHE_FOLDER = "cache";
	private static final String FOLDER_QUALIFIER = "fooBar";
	private static final String FOLDER_PATH = FOLDER_QUALIFIER;
	private static final String MEDIA_LOCATION_IN_STORAGE = FOLDER_PATH + "/h94/h05/12345";

	@Resource(name = "defaultCacheController")
	private CacheController cacheController;
	@Resource(name = "localMediaFileCacheService")
	private LocalMediaFileCacheService localMediaFileCacheService;
	@Mock
	private MediaFolderConfig folderConfig;
	@Mock
	private Media media;
	@Mock
	private MediaFolder mediaFolder;
	@Mock
	private InputStream inputStream;
	@Mock
	private StreamGetter streamGetter;
    private final PropertyConfigSwitcher cacheSizeConfigSwitcher = new PropertyConfigSwitcher("media.default.local.cache.maxSize");

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		given(media.getFolder()).willReturn(mediaFolder);
		given(media.getDataPK()).willReturn(Long.valueOf(12345));
		given(folderConfig.getFolderQualifier()).willReturn(FOLDER_QUALIFIER);
		given(Integer.valueOf(folderConfig.getHashingDepth())).willReturn(Integer.valueOf(0));
		given(mediaFolder.getPath()).willReturn(FOLDER_QUALIFIER);
		given(Integer.valueOf(inputStream.read(any(byte[].class)))).willReturn(Integer.valueOf(1), Integer.valueOf(1),
				Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1),
				Integer.valueOf(-1));
		given(streamGetter.getStream(folderConfig, MEDIA_LOCATION_IN_STORAGE)).willReturn(inputStream);
		given(folderConfig.getParameter(DefaultSettingKeys.LOCAL_CACHE_ROOT_FOLDER_KEY.getKey(), String.class, CACHE_FOLDER))
				.willReturn(CACHE_FOLDER);
	}

	@After
	public void cleanUp()
	{
		try
		{
			final CacheRegion mediaCacheRegion = getMediaCacheRegion();
			final Collection<CacheKey> allKeys = mediaCacheRegion.getAllKeys();
			for (final CacheKey cacheKey : allKeys)
			{
				if (TENANT_ID.equalsIgnoreCase(cacheKey.getTenantId()))
				{
					mediaCacheRegion.remove(cacheKey, false);
				}
			}

			FileUtils.deleteDirectory(new File(MediaUtil.getLocalStorageDataDir() + "/cache"));
		}
		catch (final IOException e)
		{
			LOG.error("Cannot clean out testing cache directory");
		}
	}

	private CacheRegion getMediaCacheRegion()
	{
		final Optional<CacheRegion> mediaCacheRegion = Iterables.tryFind(cacheController.getRegions(), new Predicate<CacheRegion>()
		{

			@Override
			public boolean apply(@Nullable final CacheRegion input)
			{
				return (input instanceof MediaCacheRegion);
			}
		});
		assertThat(mediaCacheRegion.isPresent()).isTrue();
		return mediaCacheRegion.get();
	}

	@Test
	public void shouldCacheFileOnlyOnceWhenRequestingMediaWithTheSameLocationMultipleTimes() throws IOException
	{
		// given
		final StreamContainer container = new StreamContainer();

		try
		{
			// when
			runThreadsWithLatch(500, new CacheTestExecutor()
			{

				@Override
				public void execute()
				{
					final InputStream stream = localMediaFileCacheService.storeOrGetAsStream(folderConfig, MEDIA_LOCATION_IN_STORAGE,
							streamGetter);
					if (stream == null)
					{
						fail("Got null stream");
					}
					else
					{
						container.addStream(stream);
					}
				}
			});
		}
		finally
		{
			for (final InputStream stream : container.getStreams())
			{
				closeStream(stream);
			}
		}

		// then
		assertThat(
				getMediaCacheRegion().get(
						new DefaultLocalMediaFileCacheService.MediaCacheKey(TENANT_ID, CACHE_FOLDER, MEDIA_LOCATION_IN_STORAGE)))
				.isNotNull();
		CachedFileAssert.assertThat(MEDIA_LOCATION_IN_STORAGE).hasOneFileInCache();
	}

	@Test
	public void shouldNotRemoveFilesAfterInvalidationUntilAllClientsCloseTheStreams() throws IOException
	{
		// given
		final StreamContainer container = new StreamContainer();

		try
		{
			// given
			runThreadsWithLatch(500, new CacheTestExecutor()
			{

				@Override
				public void execute()
				{
					final InputStream stream = localMediaFileCacheService.storeOrGetAsStream(folderConfig, MEDIA_LOCATION_IN_STORAGE,
							streamGetter);
					if (stream == null)
					{
						fail("Got null stream");
					}
					else
					{
						container.addStream(stream);
					}
				}
			});
			localMediaFileCacheService.removeFromCache(folderConfig, MEDIA_LOCATION_IN_STORAGE);

			// then 
			assertThat(container.getStreams()).hasSize(500);
			assertThat(
					getMediaCacheRegion().get(
							new DefaultLocalMediaFileCacheService.MediaCacheKey(TENANT_ID, CACHE_FOLDER, MEDIA_LOCATION_IN_STORAGE)))
					.isNull();
			CachedFileAssert.assertThat(MEDIA_LOCATION_IN_STORAGE).hasOneFileInCache();
		}
		finally
		{
			for (final InputStream stream : container.getStreams())
			{
				closeStream(stream);
			}
		}

		// now file from disk cache should be removed (after closing all streams)
		CachedFileAssert.assertThat(MEDIA_LOCATION_IN_STORAGE).hasNoFileInCache();
	}

	private class StreamContainer
	{
		private final List<InputStream> streams = new ArrayList<InputStream>();

		public synchronized void addStream(final InputStream stream)
		{
			streams.add(stream);
		}

		public List<InputStream> getStreams()
		{
			return ImmutableList.<InputStream> copyOf(streams);
		}
	}

	@Test
	public void shouldStoreStreamInLocalCacheIfItDoesntExistsAndReturnItAsFileInputStream() throws IOException
	{
		// given
		InputStream stream = null;

		try
		{
			// when
			stream = localMediaFileCacheService.storeOrGetAsStream(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);

			// then
			verify(streamGetter, times(1)).getStream(folderConfig, MEDIA_LOCATION_IN_STORAGE);
			assertThat(stream).isNotNull();
			assertThat(getStreamNumBytesForStream(stream)).isEqualTo(
					getStreamNumBytesForStream(streamGetter.getStream(folderConfig, MEDIA_LOCATION_IN_STORAGE)));
		}
		finally
		{
			closeStream(stream);
		}
	}

	@Test
	public void shouldReturnFileInputStreamOfAlreadyCachedStream() throws IOException
	{
		// given (store initially)
		InputStream stream1 = null;
		InputStream stream2 = null;
		try
		{
			stream1 = localMediaFileCacheService.storeOrGetAsStream(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);

			// when
			stream2 = localMediaFileCacheService.storeOrGetAsStream(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);

			// then
			verify(streamGetter, times(1)).getStream(folderConfig, MEDIA_LOCATION_IN_STORAGE);
			assertThat(stream2).isNotNull();
			assertThat(getStreamNumBytesForStream(stream2)).isEqualTo(
					getStreamNumBytesForStream(streamGetter.getStream(folderConfig, MEDIA_LOCATION_IN_STORAGE)));
		}
		finally
		{
			closeStream(stream1);
			closeStream(stream2);
		}
	}

	@Test
	public void shouldStoreStreamInLocalCacheIfItDoesntExistsAndReturnItAsRegularFile()
	{
		// when
		final File file = localMediaFileCacheService.storeOrGetAsFile(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);

		// then
		verify(streamGetter, times(1)).getStream(folderConfig, MEDIA_LOCATION_IN_STORAGE);
		assertThat(file).isNotNull();
	}


	@Test
	public void shouldReturnRegularFileOfAlreadyCachedStream()
	{
		// given (store initially)
		localMediaFileCacheService.storeOrGetAsFile(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);

		// when
		final File file = localMediaFileCacheService.storeOrGetAsFile(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);

		// then
		verify(streamGetter, times(1)).getStream(folderConfig, MEDIA_LOCATION_IN_STORAGE);
		assertThat(file).isNotNull();
	}


	@Test
	public void shouldRemoveCachedFileFromDiskWhenDirectDeleteWasCalled() throws IOException
	{
		// given (store initially) 
		InputStream stream = null;
		try
		{
			stream = localMediaFileCacheService.storeOrGetAsStream(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);
		}
		catch (final IllegalStateException e)
		{
			fail("Stream from cached should be backed by existing file in disk cache");
		}
		finally
		{
			closeStream(stream);
		}

		// when
		localMediaFileCacheService.removeFromCache(folderConfig, MEDIA_LOCATION_IN_STORAGE);

		// then
		CachedFileAssert.assertThat(MEDIA_LOCATION_IN_STORAGE).hasNoFileInCache();
	}

    @Test
    public void shouldWriteEvictionMarkerFileForCachedFileTakenAsFileByTheClientInsteadRemovingFileOnEviction() throws Exception
    {
    	// given
        localMediaFileCacheService.storeOrGetAsFile(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);

    	// when
        localMediaFileCacheService.removeFromCache(folderConfig, MEDIA_LOCATION_IN_STORAGE);

        // then
        CachedFileAssert.assertThat(MEDIA_LOCATION_IN_STORAGE).hasOneFileInCache();
        CachedFileAssert.assertThat(MEDIA_LOCATION_IN_STORAGE).hasEvictionMarker();
    }

    @Test
    public void shouldEvictExistingCacheUnitWhenItsUnderlyingFileWasDeletedAccidentally() throws Exception
    {
        // given
        final File file = localMediaFileCacheService.storeOrGetAsFile(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);

        // when (Don't do this in real life)
        file.delete();

        // then
        final File newFile = localMediaFileCacheService.storeOrGetAsFile(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);
        assertThat(file.getAbsolutePath()).isNotEqualTo(newFile.getAbsolutePath());
    }

    @Test
    public void shouldNotStoreFileInTheCacheIfItIsBiggerThanEntireDeclaredCacheSize() throws Exception
    {
        // given
        given(Long.valueOf(streamGetter.getSize(folderConfig, MEDIA_LOCATION_IN_STORAGE))).willReturn(Long.valueOf(629145600));

        // when
        localMediaFileCacheService.storeOrGetAsFile(folderConfig, MEDIA_LOCATION_IN_STORAGE, streamGetter);
        final boolean isInCache = getMediaCacheRegion().containsKey(getMediaCacheKey());

        // then
        assertThat(isInCache).isFalse();
    }

    private CacheKey getMediaCacheKey()
    {
        return new DefaultLocalMediaFileCacheService.MediaCacheKey(TENANT_ID, CACHE_FOLDER, MEDIA_LOCATION_IN_STORAGE);
    }


	private static class CachedFileAssert extends GenericAssert<CachedFileAssert, String>
	{
		public CachedFileAssert(final String actual)
		{
			super(CachedFileAssert.class, actual);
		}

		public static CachedFileAssert assertThat(final String actual)
		{
			return new CachedFileAssert(actual);
		}

		public CachedFileAssert hasOneFileInCache()
		{
			final File[] files = findCachedFiles();
			Assertions.assertThat(files).hasSize(1);
			return this;
		}

		public CachedFileAssert hasNoFileInCache()
		{
			final File[] files = findCachedFiles();
			Assertions.assertThat(files).isEmpty();
			return this;
		}

        public CachedFileAssert hasEvictionMarker()
        {
            final File[] markers = findMarkersForCachedFile();
            Assertions.assertThat(markers).isNotEmpty();
            Assertions.assertThat(markers).hasSize(1);
            return this;
        }


		private File[] findCachedFiles()
		{
			final File dir = new File(MediaUtil.getLocalStorageDataDir(), "/cache/fooBar");
			final FileFilter fileFilter = new WildcardFileFilter(getMediaId(actual) + "*.bin");
			final File[] files = dir.listFiles(fileFilter);
			return files;
		}

        private File[] findMarkersForCachedFile()
        {
            final File dir = new File(MediaUtil.getLocalStorageDataDir(), "/cache/fooBar");
            final FileFilter fileFilter = new WildcardFileFilter(getMediaId(actual) + "*.bin.EVICTED");
            final File[] files = dir.listFiles(fileFilter);
            return files;
        }

		private String getMediaId(final String location)
		{
			return new String(Base64.encode(location.getBytes()));
		}
	}

	private long getStreamNumBytesForStream(final InputStream stream) throws IOException
	{
		CountingInputStream cis = null;
		try
		{
			cis = new CountingInputStream(stream);
			return cis.getCount();
		}
		finally
		{
			closeStream(cis);
		}
	}

	private void closeStream(final InputStream stream) throws IOException
	{
		if (stream != null)
		{
			IOUtils.closeQuietly(stream);
		}
	}

	private void runThreadsWithLatch(final int numThreads, final CacheTestExecutor executor)
	{
		try
		{
			final CountDownLatch latch = new CountDownLatch(numThreads);
			for (int i = 0; i < numThreads; i++)
			{
				new RegistrableThread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							executor.execute();
						}
						finally
						{
							latch.countDown();
						}
					}

				}).start();

			}
			latch.await(10, TimeUnit.SECONDS);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}

	private interface CacheTestExecutor
	{
		void execute();
	}
}
