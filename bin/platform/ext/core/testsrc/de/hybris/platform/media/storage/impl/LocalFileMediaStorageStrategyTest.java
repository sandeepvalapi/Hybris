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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.exceptions.MediaStoreException;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.media.services.MediaStorageInitializer;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.media.storage.MediaMetaData;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.util.MediaUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@IntegrationTest
public class LocalFileMediaStorageStrategyTest extends ServicelayerBaseTest
{
	private static final String MEDIA_ID = "123456";
	private static final String REAL_FILENAME = "foo.jpg";
	private static final String MIME = "image/jpeg";
	private static final String FOLDER_PATH = "foo";
	private static final String FOLDER_QUALIFIER = FOLDER_PATH;
	private static final String PROPER_LOCATION = "foo/he6/hf4/";


	@Resource(name = "mediaStorageConfigService")
	private MediaStorageConfigService storageConfigService;
	@Resource(name = "localFileMediaStorageStrategy")
	private MediaStorageStrategy mediaStorageStrategy;
	@Resource(name = "localFileMediaStorageCleaner")
	private MediaStorageInitializer mediaStorageCleaner;
	@Resource
	private MimeService mimeService;
	@Resource
	MediaLocationHashService mediaLocationHashService;
	@Mock
	private InputStream inputStream;
	private final File tempStorage = MediaUtil.getLocalStorageDataDir();
	private MediaFolderConfig folderConfig;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		assertThat(mediaStorageStrategy).isNotNull();
		folderConfig = storageConfigService.getConfigForFolder(FOLDER_QUALIFIER);
	}

	@After
	public void cleanUp() throws Exception
	{
		mediaStorageCleaner.onInitialize();
	}

	@Test
	public void shouldStoreFileInAllReplicationDirs() throws IOException
	{
		final String tempDir = System.getProperty("java.io.tmpdir");

		final File dataDir = new File(tempDir, "_test_datadir");
		dataDir.mkdirs();
		final File replicationDir1 = new File(tempDir, "_test_replicationdir1");
		replicationDir1.mkdirs();
		final File replicationDir2 = new File(tempDir, "_test_replicationdir2");
		replicationDir2.mkdirs();

		final LocalFileMediaStorageStrategy lfStrategy = new LocalFileMediaStorageStrategy();
		lfStrategy.setMainDataDir(dataDir);
		// !!! despite the name 'setReplicationDirs()' must include the main data dir as well !!!
		lfStrategy.setReplicationDirs(Arrays.asList(dataDir, replicationDir1, replicationDir2));
		lfStrategy.setMimeService(mimeService);
		lfStrategy.setLocationHashService(mediaLocationHashService);


		// given
		final byte[] rawData = "AllReplicationDirsShouldGetTheSameDataTest!!!".getBytes();
		final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);

		final File expectedDataFile = new File(dataDir, PROPER_LOCATION + MEDIA_ID + ".jpg");
		final File expectedRepFile1 = new File(replicationDir1, PROPER_LOCATION + MEDIA_ID + ".jpg");
		final File expectedRepFile2 = new File(replicationDir2, PROPER_LOCATION + MEDIA_ID + ".jpg");
		try
		{

			// when
			final StoredMediaData storedMedia = lfStrategy
					.store(folderConfig, MEDIA_ID, metaData, new ByteArrayInputStream(rawData));

			// then
			assertThat(storedMedia).isNotNull();
			assertThat(storedMedia.getLocation()).isNotNull();
			assertThat(storedMedia.getLocation()).isEqualTo(PROPER_LOCATION + MEDIA_ID + ".jpg");

			assertThat(expectedDataFile.exists()).isTrue();
			assertThat(FileUtils.readFileToByteArray(expectedDataFile)).isEqualTo(rawData);

			assertThat(expectedRepFile1.exists()).isTrue();
			assertThat(FileUtils.readFileToByteArray(expectedRepFile1)).isEqualTo(rawData);

			assertThat(expectedRepFile2.exists()).isTrue();
			assertThat(FileUtils.readFileToByteArray(expectedRepFile2)).isEqualTo(rawData);
		}
		finally
		{
			FileUtils.deleteQuietly(expectedDataFile);
			FileUtils.deleteQuietly(expectedRepFile1);
			FileUtils.deleteQuietly(expectedRepFile2);
		}
	}

	@Test
	public void shouldStoreFileInLocalStorageAndReturnStoredMediaDataObjectWithStorageLocationAndSize() throws Exception
	{
		// given
		given(Integer.valueOf(inputStream.read(any(byte[].class)))).willReturn(Integer.valueOf(1), Integer.valueOf(0),
				Integer.valueOf(-1));
		final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);

		// when
		final StoredMediaData storedMedia = mediaStorageStrategy.store(folderConfig, MEDIA_ID, metaData, inputStream);

		// then
		assertThat(storedMedia).isNotNull();
		assertThat(storedMedia.getLocation()).isNotNull();
		assertThat(storedMedia.getLocation()).isEqualTo(PROPER_LOCATION + MEDIA_ID + ".jpg");
		assertThat(new File(tempStorage, PROPER_LOCATION + MEDIA_ID + ".jpg").exists()).isTrue();
	}

	@Test
	public void shouldThrowMediaStoreExceptionWhenFileWithTheSameNameAlreadyExist() throws Exception
	{
		// given
		given(Integer.valueOf(inputStream.read(any(byte[].class)))).willReturn(Integer.valueOf(1), Integer.valueOf(0),
				Integer.valueOf(-1));
		final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);

		try
		{
			// when
			mediaStorageStrategy.store(folderConfig, MEDIA_ID, metaData, inputStream);
			// try to write again the same file
			mediaStorageStrategy.store(folderConfig, MEDIA_ID, metaData, inputStream);
			fail("Shoud throw MediaStoreException");
		}
		catch (final MediaStoreException e)
		{
			final String mediaDirPath = new File(tempStorage, PROPER_LOCATION).getAbsolutePath();
			// then
			assertThat(e.getMessage()).startsWith("New media file already exists! (mediaId: 123456, file:");
			assertThat(e.getMessage()).endsWith(", dir: " + mediaDirPath + ")");
		}
	}

	@Test
	public void shouldRemoveMediaIfMediaExists() throws Exception
	{
		// given
		given(Integer.valueOf(inputStream.read(any(byte[].class)))).willReturn(Integer.valueOf(1), Integer.valueOf(0),
				Integer.valueOf(-1));
		final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);
		final StoredMediaData storeMedia = mediaStorageStrategy.store(folderConfig, MEDIA_ID, metaData, inputStream);
		assertThat(new File(tempStorage, PROPER_LOCATION + MEDIA_ID + ".jpg").exists()).isTrue();

		// when
		mediaStorageStrategy.delete(folderConfig, storeMedia.getLocation());

		// then
		assertThat(new File(tempStorage, PROPER_LOCATION + MEDIA_ID + ".jpg").exists()).isFalse();
	}

	@Test
	public void shouldGetMediaAsStream() throws Exception
	{
		// given
		given(Integer.valueOf(inputStream.read(any(byte[].class)))).willReturn(Integer.valueOf(1), Integer.valueOf(0),
				Integer.valueOf(-1));
		final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);
		final StoredMediaData storeMedia = mediaStorageStrategy.store(folderConfig, MEDIA_ID, metaData, inputStream);
		assertThat(new File(tempStorage, PROPER_LOCATION + MEDIA_ID + ".jpg").exists()).isTrue();
		InputStream mediaAsStream = null;

		try
		{
			// when
			mediaAsStream = mediaStorageStrategy.getAsStream(folderConfig, storeMedia.getLocation());

			// then
			assertThat(mediaAsStream).isNotNull();
		}
		finally
		{
			if (mediaAsStream != null)
			{
				IOUtils.closeQuietly(mediaAsStream);
			}
		}
	}

    @Test
    public void shouldReturnSizeOfAnObjectInStorage() throws Exception
    {
    	// given
        given(Integer.valueOf(inputStream.read(any(byte[].class)))).willReturn(Integer.valueOf(1), Integer.valueOf(0),
                Integer.valueOf(-1));
        final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);
        final StoredMediaData storeMedia = mediaStorageStrategy.store(folderConfig, MEDIA_ID, metaData, inputStream);
        assertThat(new File(tempStorage, PROPER_LOCATION + MEDIA_ID + ".jpg").exists()).isTrue();

    	// when
        final long size = mediaStorageStrategy.getSize(folderConfig, storeMedia.getLocation());

        // then
        assertThat(size).isEqualTo(1);
    }

    @Test
    public void shouldThrowMediaNotFoundExceptionWhenAskingForSizeForNonExistentObject() throws Exception
    {
        // given
        final String mediaLocation = "NON_EXISISTENT";

        try
        {
            // when
            mediaStorageStrategy.getSize(folderConfig, mediaLocation);
            fail("Should throw MediaNotFoundException");

        }
        catch (final MediaNotFoundException e)
        {
            // then fine
        }
    }

	@Test
	public void shouldGetMediaAsFile() throws Exception
	{
		// given
		given(Integer.valueOf(inputStream.read(any(byte[].class)))).willReturn(Integer.valueOf(1), Integer.valueOf(0),
				Integer.valueOf(-1));
		final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);
		final StoredMediaData storeMedia = mediaStorageStrategy.store(folderConfig, MEDIA_ID, metaData, inputStream);
		assertThat(new File(tempStorage, PROPER_LOCATION + MEDIA_ID + ".jpg").exists()).isTrue();

		// when
		final File mediaAsFile = mediaStorageStrategy.getAsFile(folderConfig, storeMedia.getLocation());

		// then
		assertThat(mediaAsFile).isNotNull();
	}

	private Map<String, Object> buildMediaMetaData(final String mime, final String originalName, final String folderPath)
	{
		final Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put(MediaMetaData.MIME, mime);
		metaData.put(MediaMetaData.FILE_NAME, originalName);
		metaData.put(MediaMetaData.FOLDER_PATH, folderPath);
		return metaData;
	}

}
