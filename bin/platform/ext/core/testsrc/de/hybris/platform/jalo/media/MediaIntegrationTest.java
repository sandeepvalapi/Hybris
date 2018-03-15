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
package de.hybris.platform.jalo.media;


import static de.hybris.platform.testframework.assertions.InputStreamAssert.assertThat;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.testframework.assertions.MediaAssert;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Strings;


@SuppressWarnings("deprecation")
@IntegrationTest
public class MediaIntegrationTest extends HybrisJUnit4TransactionalTest
{
	private static final String CUSTOM_MIME = "image/jpeg";
	private static final String CUSTOM_FILE_NAME = "customName";
	private static final String DEFAULT_MIME = "application/octet-stream";
	private byte[] streamData;
	private Media media;

	@Before
	public void setUp() throws Exception
	{
		new CoreBasicDataCreator().createRootMediaFolder();
		streamData = Strings.repeat("1a", 1024).getBytes();
		media = createEmptyMedia();
	}

	private byte[] getRandomBytes()
	{
		return RandomStringUtils.randomAlphabetic(1024).getBytes();
	}

	@After
	public void cleanUp() throws Exception
	{
		removeMedia(media);
	}

	@Test
	public void shouldAllowUploadingDataAsInputStream() throws Exception
	{
		// given
		final InputStream inputStream = getSampleInputStream(streamData);

		// when
		media.setData(inputStream);

		// then
		assertThat(media.getDataFromStream()).hasSameDataAs(streamData);
		assertThat(media.getMime()).isEqualTo(DEFAULT_MIME);
		final Object attribute = media.getAttribute(Media.URL);
		assertThat(attribute).isNotNull();
	}


	@Test
	public void shouldAllowUploadingDataAsByteArray() throws Exception
	{
		// when
		media.setData(streamData);

		// then
		assertThat(media.getDataFromStream()).hasSameDataAs(streamData);
		assertThat(media.getMime()).isEqualTo(DEFAULT_MIME);
	}

	@Test
	public void shouldAllowUploadingDataAsZeroByteArray() throws Exception
	{
		// given
		final byte[] bytes = new byte[0];

		// when
		media.setData(bytes);

		// then
		assertThat(media.getDataFromStream()).hasSize(0);
		assertThat(media.getMime()).isEqualTo(DEFAULT_MIME);
	}

	@Test
	public void shouldAllowUploadDataAsInputStreamWithCustomNameAndMime() throws Exception
	{
		// given
		final InputStream inputStream = getSampleInputStream(streamData);

		// when
		media.setData(inputStream, CUSTOM_FILE_NAME, CUSTOM_MIME);

		// then
		assertThat(media.getDataFromStream()).hasSameDataAs(streamData);
		assertThat(media.getMime()).isEqualTo(CUSTOM_MIME);
		assertThat(media.getRealFileName()).isEqualTo(CUSTOM_FILE_NAME);
	}


	@Test
	@Ignore("HORST-354")
	public void shouldAllowUploadDataAsInputStreamWithCustomNameMimeAndFolder() throws Exception
	{
		// given
		final MediaFolder customFolder = createCustomMediaFolder("foo", "foo");
		final InputStream inputStream = getSampleInputStream(streamData);

		// when
		media.setData(inputStream, CUSTOM_FILE_NAME, CUSTOM_MIME, customFolder);

		// then
		assertThat(media.getDataFromStream()).hasSameDataAs(streamData);
		assertThat(media.getMime()).isEqualTo(CUSTOM_MIME);
		assertThat(media.getRealFileName()).isEqualTo(CUSTOM_FILE_NAME);
		assertThat(media.getFolder()).isEqualTo(customFolder);
	}

	@Test
	@Ignore("HORST-29")
	public void shouldAllowReSetInputStreamWithCustomNameMimeAndFolderToMediaWhichAlreadyHasData() throws Exception
	{
		// given
		final MediaFolder customFolder = createCustomMediaFolder("foo", "foo");
		final byte[] sampleData = getRandomBytes();
		final InputStream inputStream = getSampleInputStream(sampleData);
		media.setData(inputStream);
		final byte[] anotherStreamData = getRandomBytes();
		final InputStream anotherInputStream = getSampleInputStream(anotherStreamData);
		assertThat(sampleData).isNotEqualTo(anotherStreamData);

		// when
		media.setData(anotherInputStream, CUSTOM_FILE_NAME, CUSTOM_MIME, customFolder);

		// then
		assertThat(media.getDataFromStream()).hasSameDataAs(anotherStreamData);
		assertThat(media.getMime()).isEqualTo(CUSTOM_MIME);
		assertThat(media.getRealFileName()).isEqualTo(CUSTOM_FILE_NAME);
		assertThat(media.getFolder()).isEqualTo(customFolder);
	}


	@Test
	public void shouldAllowReUseDataFromAnotherMediaItem() throws Exception
	{
		// given
		media.setData(getSampleInputStream(streamData));
		final Media media2 = createEmptyMedia();

		// when
		media2.setData(media);

		// then
		assertThat(media2.getDataFromStream()).isNotNull();
		assertThat(media2.getDataFromStream()).hasSameDataAs(media.getDataFromStream());
		assertThat(media.getForeignDataOwners()).hasSize(1).containsOnly(media2);
		MediaAssert.assertThat(media2).hasSameMetaDataAs(media);
	}

	@Test
	public void shouldAllowReUseDataFromAnotherEmptyMediaItem() throws Exception
	{
		// given
		final Media media1 = createEmptyMedia();
		media1.setURL("http://some.domain/foo.jpg");
		final Media media2 = createEmptyMedia();

		// when
		media2.setData(media1);

		// then
		assertThat(media2.getDataFromStream()).isNull();
		MediaAssert.assertThat(media2).hasSameMetaDataAs(media1);
	}


	@Test
	public void shouldRemoveCurrentDataWhenAnotherInputStreamIsSetToMediaAndUseNewDataFromGivenStream()
			throws JaloBusinessException
	{
		// given
		final Media media1 = createEmptyMedia();
		media.setData(streamData);
		final String oldLocation = media1.getLocation();
		final Long oldDataPk = media1.getDataPK();
		assertThat(media.getDataFromStream()).hasSameDataAs(streamData);
		final byte[] newData = getRandomBytes();

		// when
		media.setData(newData);

		// then
		assertThat(media.hasData()).isTrue();
		assertThat(media.getLocation()).isNotNull().isNotEqualTo(oldLocation);
		assertThat(media.getDataPK()).isNotNull().isNotEqualTo(oldDataPk);
		assertThat(media.getDataFromStream()).hasSameDataAs(newData);
	}

	@Test
	public void shouldRemoveDataFromStorage() throws Exception
	{
		DataInputStream stream = null;
		try
		{
			// given
			media.setData(streamData);
			stream = media.getDataFromStream();
			assertThat(stream).isNotNull();
			assertThat(media.getSize()).isGreaterThan(0);
		}
		finally
		{
			if (stream != null)
			{
				IOUtils.closeQuietly(stream);
			}
		}

		// when
		media.removeData(true);

		// then
		assertThat(media.hasData()).isFalse();
		assertThat(media.getDataFromStream()).isNull();
		assertThat(media.getSize()).isNull();
		assertThat(media.getLocation()).isNull();
		assertThat(media.getDataPK()).isNull();
		assertThat(media.getMime()).isNull();
		assertThat(media.getDataFromInputStream()).isNull();
	}


	@Test
	public void shouldThrowMediaNotFoundExceptionWhenLocationIsSetAndUnderlyingStorageCannotFindMediaData()
	{
		// given
		media.setLocation("Some artificial location");

		try
		{
			// when
			media.getDataFromInputStream();
			fail("Should throw JaloBusinessException with MediaNotFoundException as cause");
		}
		catch (final JaloBusinessException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(MediaNotFoundException.class);
		}
	}

	@Test
	public void shouldGenerateDifferentDataPkOnEachUploadDataAttempt() throws Exception
	{
		// given
		media.setData(streamData);
		final Long dataPKAfterFirstUpload = media.getDataPK();

		// when
		media.setData(streamData);

		// then
		assertThat(dataPKAfterFirstUpload).isNotNull().isNotEqualTo(media.getDataPK());
	}

	@Test
	public void shouldMoveDataFromOneMediaToAnother() throws JaloBusinessException
	{
		// given
		final Media media1 = createEmptyMedia();
		final Media media2 = createEmptyMedia();
		media1.setData(streamData);
		final Long dataPK1 = media1.getDataPK();
		assertThat(media2.getDataPK()).isNotEqualTo(dataPK1);

		// when
		media1.moveData(media2);

		// then
		assertThat(media1.hasData()).isFalse();
		assertThat(media1.getDataPK()).isNull();
		assertThat(media2.hasData()).isTrue();
		assertThat(media2.getDataPK()).isEqualTo(dataPK1);

		removeMedia(media1, media2);
	}

	@Test
	public void shouldMoveMediaToAnotherFolder() throws JaloBusinessException
	{
		// given
		final MediaFolder folder = createCustomMediaFolder("test", "test");
		media.setData(streamData);
		assertThat(media.hasData()).isTrue();
		assertThat(media.getFolder().getQualifier()).isEqualTo("root");
		final Long dataPkBeforeMove = media.getDataPK();
		final String locationBeforeMove = media.getLocation();

		// when
		media.moveMedia(folder);

		// then
		assertThat(media.hasData()).isTrue();
		assertThat(media.getDataPK()).isNotEqualTo(dataPkBeforeMove);
		assertThat(media.getLocation()).isNotEqualTo(locationBeforeMove);
		assertThat(media.getFolder().getQualifier()).isEqualTo("test");
		assertThat(media.getDataFromInputStream()).hasSameDataAs(streamData);
	}

	@Test
	public void shouldCopyMediaToAnotherFolderOnMoveTryWhenAnotherMediaItemPointsToSameData() throws JaloBusinessException
	{
		// given
		final MediaFolder folder = createCustomMediaFolder("test", "test");
		media.setData(streamData);
		assertThat(media.hasData()).isTrue();
		assertThat(media.getFolder().getQualifier()).isEqualTo("root");
		final Long dataPkBeforeMove = media.getDataPK();
		final String locationBeforeMove = media.getLocation();
		final Media media2 = createEmptyMedia();
		media2.setData(media);
		final String media2Location = media2.getLocation();
		final Long media2DataPk = media.getDataPK();

		// when
		media.moveMedia(folder);

		// then
		assertThat(media.hasData()).isTrue();
		assertThat(media.getDataPK()).isNotEqualTo(dataPkBeforeMove).isNotEqualTo(media2DataPk);
		assertThat(media.getLocation()).isNotEqualTo(locationBeforeMove).isNotEqualTo(media2Location);
		assertThat(media.getFolder().getQualifier()).isEqualTo("test");
		assertThat(media.getDataFromInputStream()).hasSameDataAs(streamData);
		assertThat(media2.hasData()).isTrue();
		assertThat(media2.getLocation()).isEqualTo(media2Location);
		assertThat(media2.getDataPK()).isEqualTo(media2DataPk);
	}

	@Test
	public void shouldRelocateDataFromOneFolderToAnother() throws JaloBusinessException, IOException
	{
		// given
		final MediaFolder folder = createCustomMediaFolder("test", "test");
		final Media media = createEmptyMedia();
		media.setData(streamData);
		final String oldLocation = media.getLocation();

		// when
		final boolean isRelocated = media.relocateData(folder);

		// then
		assertThat(isRelocated).isTrue();
		assertThat(media.getFolder()).isNotNull().isEqualTo(folder);
		assertThat(media.getLocation()).isNotNull().isNotEqualTo(oldLocation);
	}

	@Test
	public void shouldRelocateDataFromOneFolderToAnotherAndUpdateMetadataForAllMediaItems() throws JaloBusinessException
	{
		// given
		final MediaFolder folder = createCustomMediaFolder("test", "test");
		final Media media1 = createEmptyMedia();
		media1.setData(streamData);
		final Media media2 = createEmptyMedia();
		media2.setData(media1);
		final String oldMedia1Location = media1.getLocation();
		final String oldMedia2Location = media2.getLocation();

		// when
		final boolean isRelocated = media1.relocateData(folder);

		// then
		assertThat(isRelocated).isTrue();
		assertThat(media1.getFolder()).isNotNull().isEqualTo(folder);
		assertThat(media2.getFolder()).isNotNull().isEqualTo(folder);
		assertThat(media1.getLocation()).isNotNull().isNotEqualTo(oldMedia1Location);
		assertThat(media2.getLocation()).isNotNull().isNotEqualTo(oldMedia2Location);
	}

	private Media createEmptyMedia()
	{
		return MediaManager.getInstance().createMedia(UUID.randomUUID().toString());
	}

	private void removeMedia(final Media... medias) throws JaloBusinessException
	{
		for (final Media media : medias)
		{
			if (media.isAlive() && media.hasData())
			{
				media.removeData(true);
			}
		}
	}

	private InputStream getSampleInputStream(final byte[] data)
	{
		return new DataInputStream(new ByteArrayInputStream(data));
	}

	private MediaFolder createCustomMediaFolder(final String qualifier, final String path)
	{
		return MediaManager.getInstance().createMediaFolder(qualifier, path);
	}


}
