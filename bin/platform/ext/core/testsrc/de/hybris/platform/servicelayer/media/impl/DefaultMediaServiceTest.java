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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.media.MediaFormat;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.File;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultMediaServiceTest
{
	private DefaultMediaService mediaService;

	@Mock
	private ModelService mockModelService;
	@Mock
	private SessionService mockSessionService;
	@Mock
	private MediaDao mockMediaDao;
	@Mock
	private MediaContainer mockMediaContainer;
	@Mock
	private MediaFormat mockMediaFormat;
	@Mock
	private MediaModel mockMediaModel;
	@Mock
	private Media mockMedia;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		mediaService = new DefaultMediaService();
		mediaService.setModelService(mockModelService);
		mediaService.setMediaDao(mockMediaDao);
		mediaService.setSessionService(mockSessionService);
	}

	@Test
	public void testModelNotFoundExceptionWhenMediaContainerModelIsNull()
	{
		// Given
		final MediaContainerModel mockMediaContainerModel = mock(MediaContainerModel.class);
		final MediaFormatModel mockMediaFormatModel = mock(MediaFormatModel.class);
		given(mockModelService.getSource(mockMediaContainerModel)).willReturn(mockMediaContainer);
		given(mockModelService.getSource(mockMediaFormatModel)).willReturn(mockMediaFormat);
		given(mockModelService.get(mockMedia)).willReturn(mockMediaModel);
		given(mockMediaContainer.getMedia(mockMediaFormat)).willReturn(mockMedia);

		// When
		try
		{
			mediaService.getMediaByFormat(mockMediaContainerModel, mockMediaFormatModel);
		}
		catch (final ModelNotFoundException e)
		{
			// OK
		}
	}

	@Test
	public void testModelNotFoundExceptionWhenMediaFormatModelIsNull()
	{
		// Given
		final MediaContainerModel mockMediaContainerModel = mock(MediaContainerModel.class);
		final MediaFormatModel mockMediaFormatModel = mock(MediaFormatModel.class);
		given(mockModelService.getSource(mockMediaContainerModel)).willReturn(mockMediaContainer);
		given(mockModelService.getSource(mockMediaFormatModel)).willReturn(mockMediaFormat);
		given(mockModelService.get(mockMedia)).willReturn(mockMediaModel);
		given(mockMediaContainer.getMedia(mockMediaFormat)).willReturn(mockMedia);

		// When
		try
		{
			mediaService.getMediaByFormat(mockMediaContainerModel, mockMediaFormatModel);
		}
		catch (final ModelNotFoundException e)
		{
			// OK
		}
	}

	@Test
	public void testModelNotFoundExceptionWhenMediaFormatNotFoundInContainer()
	{
		// Given
		final MediaContainerModel mockMediaContainerModel = mock(MediaContainerModel.class);
		final MediaFormatModel mockMediaFormatModel = mock(MediaFormatModel.class);
		given(mockModelService.getSource(mockMediaContainerModel)).willReturn(mockMediaContainer);
		given(mockModelService.getSource(mockMediaFormatModel)).willReturn(mockMediaFormat);
		given(mockModelService.get(mockMedia)).willReturn(null);
		given(mockMediaContainer.getMedia(mockMediaFormat)).willReturn(mockMedia);

		// When
		try
		{
			mediaService.getMediaByFormat(mockMediaContainerModel, mockMediaFormatModel);
		}
		catch (final ModelNotFoundException e)
		{
			// OK
		}
	}

	@Test
	public void testGetFilesWithMediaModel()
	{
		// Given
		final Collection<File> mockCollection = mock(Collection.class);
		given(mockModelService.getSource(mockMediaModel)).willReturn(mockMedia);
		given(mockMedia.getFiles()).willReturn(mockCollection);

		// When
		final Collection<File> result = mediaService.getFiles(mockMediaModel);

		Assert.assertNotNull(result);
	}

	@Test
	public void testNoDataAvailableExceptionWithNullMediaModel()
	{
		// Given
		final MediaModel mediaModel = null;

		// When
		try
		{
			mediaService.getFiles(mediaModel);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			// OK
		}
	}
}
