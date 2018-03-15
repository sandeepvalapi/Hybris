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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.core.model.media.MediaFormatMappingModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.dao.MediaContainerDao;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * JUnit class that tests {@link DefaultMediaContainerService}
 */
@UnitTest
public class DefaultMediaContainerServiceTest
{
	private DefaultMediaContainerService mediaContainerService;
	private MediaContainerModel mediaContainerModel;
	private MediaModel mediaModel1;
	private MediaModel mediaModel2;
	private MediaFormatModel mediaFormatModel;
	private MediaContextModel mediaContextModel;
	private MediaFormatModel sourceMediaFormat;
	private MediaFormatModel targetMediaFormat;

	@Mock
	private ModelService modelService;

	@Mock
	private MediaService mediaService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		mediaContainerService = new DefaultMediaContainerService();
		mediaContainerService.setModelService(modelService);
		mediaContainerService.setMediaService(mediaService);

		mediaContainerModel = new MediaContainerModel();
		mediaContainerModel.setQualifier("container");
		mediaModel1 = mock(MediaModel.class);
		when(mediaModel1.getCode()).thenReturn("media1");
		mediaModel2 = mock(MediaModel.class);
		when(mediaModel2.getCode()).thenReturn("media2");

		mediaFormatModel = mock(MediaFormatModel.class);
		when(mediaModel1.getMediaFormat()).thenReturn(mediaFormatModel);
		final List<MediaModel> mediaModels = new ArrayList<MediaModel>();
		mediaModels.add(mediaModel1);
		mediaModels.add(mediaModel2);
		mediaContainerModel.setMedias(mediaModels);

		mediaContextModel = new MediaContextModel();
		final MediaFormatMappingModel mediaFormatMappingModel = new MediaFormatMappingModel();
		sourceMediaFormat = new MediaFormatModel();
		targetMediaFormat = new MediaFormatModel();
		mediaFormatMappingModel.setSource(sourceMediaFormat);
		mediaFormatMappingModel.setTarget(targetMediaFormat);
		mediaContextModel.setMappings(Collections.singletonList(mediaFormatMappingModel));
	}


	@Test
	public void testRemoveMediaFromContainer()
	{
		mediaContainerService.removeMediaFromContainer(mediaContainerModel, Collections.singletonList(mediaModel2));
		assertThat(mediaContainerModel.getMedias().size()).isEqualTo(1);
		assertThat(mediaContainerModel.getMedias().iterator().next().getCode()).isEqualTo("media1");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddMediaToContainerWithNullMediaContainer()
	{
		final List<MediaModel> mediaModels = new ArrayList<MediaModel>();

		mediaContainerService.addMediaToContainer(null, mediaModels);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddMediaToContainerWithNullMediaModels()
	{
		mediaContainerService.addMediaToContainer(mediaContainerModel, null);
	}

	/**
	 * Case :
	 * <p>
	 * There are already 2 medias fooMedia, barMedia with foo and bar format
	 * <p>
	 * Added 1 more medias gooMedia,rooMedia with goo format
	 * <p>
	 * Result : Should be all 3 medias fooMedia, bar Media and rooMedia
	 */
	@Test
	public void testAddMediaWithUniqueFormatToContainer()
	{

		final MediaFormatModel existingFooFormat = new MediaFormatModel();

		final MediaModel existingFooMedia = new MediaModel();
		existingFooMedia.setMediaFormat(existingFooFormat);
		existingFooMedia.setCode("fooMedia");
		final MediaFormatModel existingBarFormat = new MediaFormatModel();

		final MediaModel existingBarMedia = new MediaModel();
		existingBarMedia.setMediaFormat(existingBarFormat);
		existingBarMedia.setCode("barMedia");
		final MediaContainerModel existingContainer = new MediaContainerModel();
		existingContainer.setMedias(java.util.Arrays.asList(existingFooMedia, existingBarMedia));

		final MediaFormatModel newGooFormat = new MediaFormatModel();

		final MediaModel newGooMedia = new MediaModel();
		newGooMedia.setMediaFormat(newGooFormat);
		newGooMedia.setCode("gooMedia");

		final MediaContainerModel containerModel = new MediaContainerModel();
		containerModel.setMedias(Arrays.asList(existingBarMedia, existingFooMedia));

		mediaContainerService.addMediaToContainer(containerModel, Collections.singletonList(newGooMedia));

		Mockito.verify(modelService).save(Mockito.argThat(new ArgumentMatcher<MediaContainerModel>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				assertThat(argument instanceof MediaContainerModel);
				final MediaContainerModel container = (MediaContainerModel) argument;
				assertThat(container.getMedias() != null);
				assertThat(container.getMedias().size() == 3);
				assertThat(container.getMedias().contains(existingFooMedia));
				assertThat(container.getMedias().contains(existingBarMedia));
				assertThat(container.getMedias().contains(newGooMedia));
				return true;
			}
		}));

		assertThat(containerModel.getMedias().size()).isEqualTo(3);
	}

	/**
	 * Case :
	 * <p>
	 * There are already 2 medias fooMedia, barMedia with foo and bar format
	 * <p>
	 * Added 1 more media gooMedia with bar format
	 * <p>
	 * Result : Should be only 2 medias fooMedia (foo format) and gooMedia (bar format)
	 */
	@Test
	public void testAddMediaWithNonUniqueFormatToContainer()
	{

		final MediaFormatModel existingFooFormat = new MediaFormatModel();

		final MediaModel existingFooMedia = new MediaModel();
		existingFooMedia.setMediaFormat(existingFooFormat);
		existingFooMedia.setCode("fooMedia");
		final MediaFormatModel existingBarFormat = new MediaFormatModel();

		final MediaModel existingBarMedia = new MediaModel();
		existingBarMedia.setMediaFormat(existingBarFormat);
		existingBarMedia.setCode("barMedia");
		final MediaContainerModel existingContainer = new MediaContainerModel();
		existingContainer.setMedias(java.util.Arrays.asList(existingFooMedia, existingBarMedia));

		//final MediaFormatModel newGooFormat = new MediaFormatModel();

		final MediaModel newGooMedia = new MediaModel();
		newGooMedia.setMediaFormat(existingBarFormat);
		newGooMedia.setCode("gooMedia");

		final MediaContainerModel containerModel = new MediaContainerModel();
		containerModel.setMedias(Arrays.asList(existingBarMedia, existingFooMedia));

		mediaContainerService.addMediaToContainer(containerModel, Collections.singletonList(newGooMedia));

		Mockito.verify(modelService).save(Mockito.argThat(new ArgumentMatcher<MediaContainerModel>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				assertThat(argument instanceof MediaContainerModel);
				final MediaContainerModel container = (MediaContainerModel) argument;
				assertThat(container.getMedias() != null);
				assertThat(container.getMedias().size() == 2);
				assertThat(container.getMedias().contains(existingFooMedia));
				assertThat(!container.getMedias().contains(existingBarMedia));
				assertThat(container.getMedias().contains(newGooMedia));
				return true;
			}
		}));

		assertThat(containerModel.getMedias().size()).isEqualTo(2);
	}


	/**
	 * Case :
	 * <p>
	 * There are already 2 medias fooMedia, barMedia with foo and bar format
	 * <p>
	 * Added 2 more medias gooMedia,rooMedia with bar format
	 * <p>
	 * Result : Should be only 2 medias fooMedia (foo format) and rooMedia (bar format)
	 */
	@Test
	public void testAddMediaWithNonUniqueAsMediasFormatToContainer()
	{

		final MediaFormatModel existingFooFormat = new MediaFormatModel();

		final MediaModel existingFooMedia = new MediaModel();
		existingFooMedia.setMediaFormat(existingFooFormat);
		existingFooMedia.setCode("fooMedia");
		final MediaFormatModel existingBarFormat = new MediaFormatModel();

		final MediaModel existingBarMedia = new MediaModel();
		existingBarMedia.setMediaFormat(existingBarFormat);
		existingBarMedia.setCode("barMedia");
		final MediaContainerModel existingContainer = new MediaContainerModel();
		existingContainer.setMedias(java.util.Arrays.asList(existingFooMedia, existingBarMedia));

		//final MediaFormatModel newGooFormat = new MediaFormatModel();

		final MediaModel newGooMedia = new MediaModel();
		newGooMedia.setMediaFormat(existingBarFormat);
		newGooMedia.setCode("gooMedia");


		final MediaModel newRooMedia = new MediaModel();
		newRooMedia.setMediaFormat(existingBarFormat);
		newRooMedia.setCode("rooMedia");

		final MediaContainerModel containerModel = new MediaContainerModel();
		containerModel.setMedias(Arrays.asList(existingBarMedia, existingFooMedia));

		mediaContainerService.addMediaToContainer(containerModel, Arrays.asList(newGooMedia, newRooMedia));

		Mockito.verify(modelService).save(Mockito.argThat(new ArgumentMatcher<MediaContainerModel>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				assertThat(argument instanceof MediaContainerModel);
				final MediaContainerModel container = (MediaContainerModel) argument;
				assertThat(container.getMedias() != null);
				assertThat(container.getMedias().size() == 2);
				assertThat(container.getMedias().contains(existingFooMedia));
				assertThat(!container.getMedias().contains(existingBarMedia));
				assertThat(!container.getMedias().contains(newGooMedia));
				assertThat(!container.getMedias().contains(newRooMedia));
				return true;
			}
		}));

		assertThat(containerModel.getMedias().size()).isEqualTo(2);
	}



	@Test
	public void testAddMediaToContainer()
	{
		mediaContainerModel.setMedias(Collections.EMPTY_LIST);
		assertThat(mediaContainerModel.getMedias().size()).isEqualTo(0);

		final List<MediaModel> mediaModels = new ArrayList<MediaModel>();
		mediaModels.add(mediaModel1);
		mediaContainerService.addMediaToContainer(mediaContainerModel, mediaModels);
		assertThat(mediaContainerModel.getMedias().size()).isEqualTo(1);

		final MediaModel mediaModel3 = mock(MediaModel.class);
		when(mediaModel3.getMediaFormat()).thenReturn(mediaFormatModel);
		mediaContainerService.addMediaToContainer(mediaContainerModel, Collections.singletonList(mediaModel3));
		assertThat(mediaContainerModel.getMedias().size()).isEqualTo(1);
	}


	@Test
	public void testGetMediaFormatForSourceFormat()
	{
		final MediaFormatModel returnedFormat = mediaContainerService.getMediaFormatForSourceFormat(mediaContextModel,
				sourceMediaFormat);
		assertThat(returnedFormat).isEqualTo(targetMediaFormat);
	}


	@Test(expected = ModelNotFoundException.class)
	public void testGetMediaFormatForSourceFormatForNotExisting()
	{
		final MediaFormatModel existingFooFormat = new MediaFormatModel();
		final MediaFormatModel existingBarFormat = new MediaFormatModel();

		final MediaFormatMappingModel mappingModelFoo = new MediaFormatMappingModel();
		mappingModelFoo.setSource(existingFooFormat);

		final MediaFormatMappingModel mappingModelBar = new MediaFormatMappingModel();
		mappingModelBar.setSource(existingBarFormat);

		final MediaContextModel existingContext = new MediaContextModel();
		existingContext.setMappings(Arrays.asList(mappingModelBar, mappingModelFoo));

		final MediaFormatModel existingRooFormat = new MediaFormatModel();

		mediaContainerService.getMediaFormatForSourceFormat(existingContext, existingRooFormat);

	}


	@Test(expected = IllegalArgumentException.class)
	public void testGetMediaContainerForNullQualifier()
	{
		final MediaContainerDao dao = Mockito.mock(MediaContainerDao.class);
		mediaContainerService.setMediaContainerDao(dao);

		mediaContainerService.getMediaContainerForQualifier(null);

		Mockito.verifyZeroInteractions(dao);
	}

	@Test(expected = AmbiguousIdentifierException.class)
	public void testGetMediaContainerForAmbiguousQualifier()
	{
		final List<MediaContainerModel> result = Mockito.mock(List.class);
		Mockito.when(Integer.valueOf(result.size())).thenReturn(Integer.valueOf(100));

		final MediaContainerDao dao = Mockito.mock(MediaContainerDao.class);
		mediaContainerService.setMediaContainerDao(dao);

		Mockito.when(dao.findMediaContainersByQualifier(Mockito.eq("some foo qualifier"))).thenReturn(result);
		mediaContainerService.getMediaContainerForQualifier("some foo qualifier");

	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetMediaContainerForNotExistingQualifier()
	{
		final List<MediaContainerModel> result = Mockito.mock(List.class);
		Mockito.when(Integer.valueOf(result.size())).thenReturn(Integer.valueOf(0));
		Mockito.when(Boolean.valueOf(result.isEmpty())).thenReturn(Boolean.TRUE);

		final MediaContainerDao dao = Mockito.mock(MediaContainerDao.class);
		mediaContainerService.setMediaContainerDao(dao);

		Mockito.when(dao.findMediaContainersByQualifier(Mockito.eq("some foo qualifier"))).thenReturn(result);
		mediaContainerService.getMediaContainerForQualifier("some foo qualifier");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMediaContextForNullQualifier()
	{
		final MediaContainerDao dao = Mockito.mock(MediaContainerDao.class);
		mediaContainerService.setMediaContainerDao(dao);

		mediaContainerService.getMediaContextForQualifier(null);

		Mockito.verifyZeroInteractions(dao);
	}

	@Test(expected = AmbiguousIdentifierException.class)
	public void testGetMediaContextrForAmbiguousQualifier()
	{
		final List<MediaContextModel> result = Mockito.mock(List.class);
		Mockito.when(Integer.valueOf(result.size())).thenReturn(Integer.valueOf(100));

		final MediaContainerDao dao = Mockito.mock(MediaContainerDao.class);
		mediaContainerService.setMediaContainerDao(dao);

		Mockito.when(dao.findMediaContextByQualifier(Mockito.eq("some foo qualifier"))).thenReturn(result);
		mediaContainerService.getMediaContextForQualifier("some foo qualifier");

	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetMediaContextForNotExistingQualifier()
	{
		final List<MediaContextModel> result = Mockito.mock(List.class);
		Mockito.when(Integer.valueOf(result.size())).thenReturn(Integer.valueOf(0));
		Mockito.when(Boolean.valueOf(result.isEmpty())).thenReturn(Boolean.TRUE);

		final MediaContainerDao dao = Mockito.mock(MediaContainerDao.class);
		mediaContainerService.setMediaContainerDao(dao);

		Mockito.when(dao.findMediaContextByQualifier(Mockito.eq("some foo qualifier"))).thenReturn(result);
		mediaContainerService.getMediaContextForQualifier("some foo qualifier");

	}


}
