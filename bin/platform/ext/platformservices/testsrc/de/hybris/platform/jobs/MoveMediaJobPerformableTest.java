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
package de.hybris.platform.jobs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.MoveMediaCronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.media.MediaIOException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.testframework.TestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


@UnitTest
public class MoveMediaJobPerformableTest
{
	@Mock
	private MediaService mediaService;
	@Mock
	private ModelService modelService;
	@Mock
	private SessionService sessionService;

	private MoveMediaJobPerformable job;
	private MoveMediaCronJobModel cronJob;

	@Before
	public void setUp()
	{
		// Init mocks
		MockitoAnnotations.initMocks(this);

		job = new MoveMediaJobPerformable();
		job.setMediaService(mediaService);
		job.setModelService(modelService);
		job.setSessionService(sessionService);

		cronJob = new MoveMediaCronJobModel();
		cronJob.setCode("moveMediaTest");
		cronJob.setMedias(Collections.EMPTY_LIST);
	}

	@Test
	public void testMoveWithNoMedia()
	{
		final PerformResult result = job.perform(cronJob);

		assertTrue(result.getResult().equals(CronJobResult.SUCCESS));
		assertTrue(result.getStatus().equals(CronJobStatus.FINISHED));

		assertEquals(Integer.valueOf(0), cronJob.getMovedMediasCount());
	}

	@Test
	public void testMoveWithValidMedias()
	{
		cronJob.setMedias(createMedias(10, createFolder("start")));
		cronJob.setTargetFolder(createFolder("end"));

		final PerformResult result = job.perform(cronJob);

		assertTrue(result.getResult().equals(CronJobResult.SUCCESS));
		assertTrue(result.getStatus().equals(CronJobStatus.FINISHED));

		assertEquals(Integer.valueOf(10), cronJob.getMovedMediasCount());
		Mockito.verify(mediaService, Mockito.times(10)).setFolderForMedia(Mockito.any(MediaModel.class),
				Mockito.any(MediaFolderModel.class));
	}

	@Test
    public void testMoveWithIOException()
    {
        cronJob.setMedias(createMedias(10, createFolder("start")));
        cronJob.setTargetFolder(createFolder("end"));
        final MediaModel corruptMedia = ((List<MediaModel>) cronJob.getMedias()).get(3);

        Mockito.doThrow(new MediaIOException("bla")).when(mediaService)
                .setFolderForMedia(corruptMedia, cronJob.getTargetFolder());

        PerformResult result = null;
        try
        {
            TestUtils.disableFileAnalyzer("There might be an IOException inside.");
            result = job.perform(cronJob);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
        }

        assertTrue(result.getResult().equals(CronJobResult.ERROR));
        assertTrue(result.getStatus().equals(CronJobStatus.FINISHED));

        assertEquals(Integer.valueOf(9), cronJob.getMovedMediasCount());
    }

	@Test
	public void testMoveAbortWithValidMedias()
	{
		final MoveMediaCronJobModel mockedCronJob = Mockito.spy(cronJob);
		mockedCronJob.setMedias(createMedias(10, createFolder("start")));
		mockedCronJob.setTargetFolder(createFolder("end"));

		final Stack<Boolean> resultStack = new Stack<Boolean>();
		resultStack.push(Boolean.FALSE);
		resultStack.push(Boolean.FALSE);
		resultStack.push(Boolean.FALSE);
		resultStack.push(Boolean.FALSE);
		resultStack.push(Boolean.FALSE);
		resultStack.push(Boolean.FALSE);
		resultStack.push(Boolean.FALSE);
		resultStack.push(Boolean.FALSE);
		resultStack.push(Boolean.FALSE);

		Mockito.when(mockedCronJob.getRequestAbort()).thenAnswer(new Answer<Boolean>()
		{

			@Override
			public Boolean answer(final InvocationOnMock invocation) throws Throwable
			{
				try
				{
					return resultStack.pop();
				}
				catch (final EmptyStackException e)
				{
					return Boolean.TRUE;
				}
			}

		});

		final PerformResult result = job.perform(mockedCronJob);

		assertTrue(result.getResult().equals(CronJobResult.UNKNOWN));
		assertTrue(result.getStatus().equals(CronJobStatus.ABORTED));

		assertEquals(Integer.valueOf(9), mockedCronJob.getMovedMediasCount());
	}

	private Collection<MediaModel> createMedias(final int count, final MediaFolderModel folder)
	{
		final Collection<MediaModel> result = new ArrayList<MediaModel>();
		for (int i = 0; i < count; i++)
		{
			final MediaModel media = new MediaModel();
			media.setCode("bla" + i);
			media.setFolder(folder);
			result.add(media);
		}
		return result;
	}

	private MediaFolderModel createFolder(final String path)
	{
		final MediaFolderModel folder = new MediaFolderModel();
		folder.setQualifier(path);
		folder.setPath(path);
		return folder;
	}
}
