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
package de.hybris.platform.servicelayer.media.impl;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.NoDataAvailableException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * Test demonstrating usage of media related services
 */
@DemoTest
@IntegrationTest
public class MediaServiceDemoTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(MediaServiceDemoTest.class);
	/**
	 * Length of the sample file that is used in media items.
	 */
	private static final int TEST_DATA_LENGTH = 19;

	@Resource
	private MediaService mediaService;

	@Resource
	private UserService userService;

	@Resource
	private ModelService modelService;

	@Resource
	private MediaContainerService mediaContainerService;


	/**
	 * Prepares data for testing - sample media items, media folders, formats, containers, context and other data that
	 * will be used. Data is loaded from csv impex file.
	 */
	@Before
	public void setUp() throws Exception
	{
		// Create data for tests
		LOG.info("Creating data for media ..");
		userService.setCurrentUser(userService.getAdminUser());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		// importing test csv
		importCsv("/servicelayer/test/testMedias.csv", "utf-8");

		LOG.info("Finished data for media " + (System.currentTimeMillis() - startTime) + "ms");
	}


	/**
	 * Demonstrates how to use media service to retrieve input stream that represents file that media item contains.
	 * Media with code "media1" contains sample text file that has specific length and this length is checked for
	 * retrieved file.
	 */
	@Test
	public void testGetStreamFromMedia()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final InputStream inputStream = mediaService.getStreamFromMedia(model1);
		final byte[] buffer = new byte[100];
		try
		{
			final int read = inputStream.read(buffer);
			assertThat(read).isEqualTo(TEST_DATA_LENGTH);
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
	}


	/**
	 * Demonstrates how to use media service to retrieve byte array that contains data from file assigned to media item.
	 * Retrieved byte array is checked against number of bytes it contains.
	 */
	@Test
	public void testGetDataFromMedia()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final byte[] data = mediaService.getDataFromMedia(model1);
		assertThat(data.length).isEqualTo(TEST_DATA_LENGTH);
	}


	/**
	 * Demonstrates how to use media service to put byte array as data for underlying media file. New byte array is set
	 * for media item and then it is retrieved and its length is checked.
	 */
	@Test
	public void testSetDataForMedia()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final byte[] data = new byte[]
		{ 12, 13, 14, 15, 16 };
		mediaService.setDataForMedia(model1, data);
		final byte[] dataRead = mediaService.getDataFromMedia(model1);
		assertThat(dataRead.length).isEqualTo(data.length);
	}



	/**
	 * Test demonstrates how to use media service to set input stream into underlying media file. New byte array input
	 * stream is created and put into media item with new name and mime type. Then input stream is retrieved from media
	 * and its length is checked. Also mime type and new name of the file is checked.
	 */
	@Test
	public void testSetStreamForMedia()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final byte[] data = new byte[]
		{ 12, 13, 14, 15, 16 };
		final InputStream inputStream = new ByteArrayInputStream(data);
		mediaService.setStreamForMedia(model1, inputStream, "originalName", "text/plain");
		final InputStream readStream = mediaService.getStreamFromMedia(model1);
		final byte[] buffer = new byte[100];
		try
		{
			final int read = readStream.read(buffer);
			assertThat(read).isEqualTo(data.length);
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
		modelService.save(model1);
		assertThat(model1.getMime()).isEqualTo("text/plain");
		assertThat(model1.getRealfilename()).isEqualTo("originalName");
		modelService.remove(model1);
	}


	/**
	 * In this test we set new media folder for selected media. Media folder is created in set up process from impex file
	 * and is here assigned to the media item. Then media model is saved and checked if media folder was changed to the
	 * new one.
	 */
	@Test
	public void testSetFolderForMedia()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		assertThat(model1.getFolder().getQualifier()).isEqualTo("testFolder1");
		final MediaFolderModel folderModel = mediaService.getFolder("testFolder2");
		mediaService.setFolderForMedia(model1, folderModel);
		modelService.save(model1);
		modelService.refresh(model1);
		assertThat(model1.getFolder().getQualifier()).isEqualTo("testFolder2");
	}



	/**
	 * Test shows how to move data from one media object to another. New data (as byte array) is created and set for
	 * media object 2. Then we use media service to move this new data to the media object 1. Last step is to check if
	 * data was moved - model 2 should not contains this data anymore, and model 1 should contain this data.
	 */
	@Test
	public void testMoveData()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final MediaModel model2 = mediaService.getMedia("media2");
		final byte[] data = new byte[]
		{ 12, 13, 14, 15, 16 };
		mediaService.setDataForMedia(model2, data);
		mediaService.moveData(model2, model1);
		byte[] dataRead = mediaService.getDataFromMedia(model1);
		assertThat(dataRead.length).isEqualTo(data.length);
		try
		{
			dataRead = mediaService.getDataFromMedia(model2);
			fail("Should have no data!");
		}
		catch (final NoDataAvailableException ndae)
		{
			// expected behaviour
		}
	}


	/**
	 * This test demonstrates how to copy data from one media item to other one. New data is created (as byte array) and
	 * then put into media model 2. Then it is copied from model 2 to model 1 and then both models are checked if contain
	 * new data.
	 */
	@Test
	public void testCopyData()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final MediaModel model2 = mediaService.getMedia("media2");
		final byte[] data = new byte[]
		{ 12, 13, 14, 15, 16 };
		mediaService.setDataForMedia(model2, data);
		mediaService.copyData(model2, model1);
		byte[] dataRead = mediaService.getDataFromMedia(model2);
		assertThat(dataRead.length).isEqualTo(data.length);
		dataRead = mediaService.getDataFromMedia(model1);
		assertThat(dataRead.length).isEqualTo(data.length);
	}



	/**
	 * Here we demonstrate how to duplicate underlying media data. New data is created and put into model 2. Then this
	 * data is duplicated into model 1. The difference between copying and duplicating is that copying just make in the
	 * model new reference to existing file, and duplicating creates new file and assigns it to the target model.
	 */
	@Test
	public void testDuplicateData()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final MediaModel model2 = mediaService.getMedia("media2");
		final byte[] data = new byte[]
		{ 12, 13, 14, 15, 16 };
		mediaService.setDataForMedia(model2, data);
		mediaService.duplicateData(model2, model1);
		byte[] dataRead = mediaService.getDataFromMedia(model2);
		assertThat(dataRead.length).isEqualTo(data.length);
		dataRead = mediaService.getDataFromMedia(model1);
		assertThat(dataRead.length).isEqualTo(data.length);
		assertThat(model1.getURL()).isNotEqualTo(model2.getURL());
	}


	/**
	 * Here we show how to get corresponding media item byt its context. In the system there is created test context item
	 * that contains mappings between two media formats. Now, if we want to retrieve media object in the corresponding
	 * format we call this method and get proper media if the context contains mapping where current media format is
	 * denoted as source. Then corresponding media in target format will be returned from the media container.
	 */
	@Test
	public void testGetMediaByContext()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final MediaContextModel contextModel = mediaContainerService.getMediaContextForQualifier("testContext1");
		final MediaModel model2 = mediaService.getMediaByContext(model1, contextModel);
		assertThat(model2.getCode()).isEqualTo("media2");
	}


	/**
	 * Here we have other situation with media container. We have media container obeject that contains medias in
	 * different formats. If we want to retrieve some media in the specified format we can call this method giving as
	 * arguments media container and required media format, and media model in specified format will be returned.
	 */
	@Test
	public void testGetMediaByFormat()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final MediaFormatModel mediaFormatModel = mediaService.getFormat("testFormat2");
		MediaModel resultModel = mediaService.getMediaByFormat(model1, mediaFormatModel);
		assertThat(resultModel.getCode()).isEqualTo("media2");

		final MediaContainerModel mediaContainerModel = mediaContainerService.getMediaContainerForQualifier("testContainer");
		resultModel = mediaService.getMediaByFormat(mediaContainerModel, mediaFormatModel);
		assertThat(resultModel.getCode()).isEqualTo("media2");
	}


	/**
	 * Here we have a test that demostrates usage of the method getFiles. Using this method we retrieve all files from
	 * all replicated directories (if there is more than one). In the example we have one file in the media and we check
	 * if the file is our sample file.
	 */
	@Test
	public void testGetFiles()
	{
		final MediaModel model1 = mediaService.getMedia("media1");
		final Collection<File> files = mediaService.getFiles(model1);
		assertThat(files.size()).isEqualTo(1);
		assertThat(files.iterator().next().length()).isEqualTo(TEST_DATA_LENGTH);
	}


	/**
	 * Here we show how to add new media model to the existing container. We just use proper method that takes container
	 * and list of elements to add as arguments and check container content before and after add operation. If the
	 * container already contains media with the format that is present in the list this media will be replaced with new
	 * one, so all medias in the container have different formats.
	 */
	@Test
	public void testAddMediaToContainer()
	{
		final MediaContainerModel mediaContainerModel = mediaContainerService.getMediaContainerForQualifier("testContainer");
		assertThat(mediaContainerModel.getMedias().size()).isEqualTo(2);
		mediaContainerService.addMediaToContainer(mediaContainerModel, Collections.singletonList(mediaService.getMedia("media3")));
		assertThat(mediaContainerModel.getMedias().size()).isEqualTo(3);
	}



	/**
	 * Here we present very simple method that removes media from container. We remove existing media and check container
	 * for amount of medias before and after removal.
	 */
	@Test
	public void testRemoveMediaFromContainer()
	{
		final MediaContainerModel mediaContainerModel = mediaContainerService.getMediaContainerForQualifier("testContainer");
		assertThat(mediaContainerModel.getMedias().size()).isEqualTo(2);
		mediaContainerService.removeMediaFromContainer(mediaContainerModel, Collections.singletonList(mediaService
				.getMedia("media1")));
		assertThat(mediaContainerModel.getMedias().size()).isEqualTo(1);
	}


	/**
	 * This method demonstrates usage of the method that returns to us target media format for the given source format
	 * from the specified media context. Necessary mapping is placed in media context with impex file and we retrieve
	 * target format and check if we got expected one.
	 */
	@Test
	public void testGetMediaFormatForSourceFormat()
	{
		final MediaFormatModel mediaFormatModel = mediaService.getFormat("testFormat1");
		final MediaContextModel contextModel = mediaContainerService.getMediaContextForQualifier("testContext1");
		final MediaFormatModel targetModel = mediaContainerService.getMediaFormatForSourceFormat(contextModel, mediaFormatModel);
		assertThat(targetModel).isNotNull();
		assertThat(targetModel.getQualifier()).isEqualTo("testFormat2");
	}

}
