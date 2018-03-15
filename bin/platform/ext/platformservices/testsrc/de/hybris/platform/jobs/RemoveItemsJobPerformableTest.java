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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.RemoveItemsCronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.testframework.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

import junit.framework.Assert;
import org.apache.commons.lang.CharUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


/**
 * {@link RemoveItemsJobPerformable} covering test. TODO lack of test in case of result != PermissionCheckResult.ALLOWED
 */
@UnitTest
public class RemoveItemsJobPerformableTest
{

	@Mock
	private MediaService mediaService;

	@Mock
	private ModelService modelService;

	@Mock
	private SessionService sessionService;

	@Mock
	private PermissionCheckingService permissionCheckingService;


	@Mock
	private PermissionCheckResult permissionResult;

	//private RemovedItemPKProcessor removedItemPKIterator;

	private RemoveItemsJobPerformable performable;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		performable = Mockito.spy(new RemoveItemsJobPerformable());


		performable.setMediaService(mediaService);
		performable.setModelService(modelService);
		performable.setSessionService(sessionService);
		performable.setPermissionCheckingService(permissionCheckingService);

		Mockito.when(Boolean.valueOf(permissionResult.isGranted())).thenReturn(Boolean.TRUE);
		Mockito.when(Boolean.valueOf(permissionResult.isDenied())).thenReturn(Boolean.FALSE);
		Mockito.when(permissionResult.getCheckValue()).thenReturn(PermissionCheckValue.ALLOWED);

	}

	@Test
	public void testEmptyPkStreamPerformedJob()
	{
		final RemoveItemsCronJobModel cronJob = new RemoveItemsCronJobModel();
		cronJob.setItemPKs(null); //null item pk media
		cronJob.setCreateSavedValues(Boolean.TRUE);

        PerformResult result = null;
        try
        {
            TestUtils.disableFileAnalyzer("Expected error logs inside ");
            result = performable.perform(cronJob);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
        }
        //TODO uncomment when we operate on models only
		Assert.assertEquals(0, cronJob.getItemsRefused().intValue());
		Assert.assertEquals(0, cronJob.getItemsDeleted().intValue());

		Assert.assertEquals(CronJobResult.FAILURE, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(modelService, Mockito.never()).remove(Mockito.any(PK.class));
		Mockito.verify(sessionService, Mockito.never()).removeAttribute("is.hmc.session");
	}


	@Test
	public void testClearSavedValuesFalseStreamPerformedJob()
	{
		final MediaModel mediaPk = new MediaModel();
		final DataInputStream dis = new DataInputStream(buildUpStream("pika", "poka", "nuka"));

		Mockito.when(mediaService.getStreamFromMedia(mediaPk)).thenReturn(dis);

		final RemoveItemsCronJobModel cronJob = new RemoveItemsCronJobModel();
		cronJob.setItemPKs(mediaPk); //null item pk media
		cronJob.setCreateSavedValues(Boolean.FALSE);

        PerformResult result = null;
        try
        {
            TestUtils.disableFileAnalyzer("Expected error logs inside ");
            result = performable.perform(cronJob);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
        }

		//TODO uncomment when we operate on models only
		Assert.assertEquals(0, cronJob.getItemsRefused().intValue());
		Assert.assertEquals(0, cronJob.getItemsDeleted().intValue());
		Assert.assertEquals(CronJobResult.FAILURE, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(sessionService, Mockito.times(1)).removeAttribute("is.hmc.session");
		Mockito.verify(modelService, Mockito.never()).remove(Mockito.any(PK.class));
	}

	@Test
	public void testClearSavedValuesTrueStreamPerformedJob()
	{
		final MediaModel mediaPk = new MediaModel();
		final DataInputStream dis = new DataInputStream(buildUpStream("pika", "poka", "nuka"));

		Mockito.when(mediaService.getStreamFromMedia(mediaPk)).thenReturn(dis);

		final RemoveItemsCronJobModel cronJob = new RemoveItemsCronJobModel();
		cronJob.setItemPKs(mediaPk); //null item pk media
		cronJob.setCreateSavedValues(Boolean.TRUE);

        PerformResult result = null;
        try
        {
            TestUtils.disableFileAnalyzer("Expected error logs inside ");
            result = performable.perform(cronJob);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
        }

		Assert.assertEquals(0, cronJob.getItemsRefused().intValue());
		Assert.assertEquals(0, cronJob.getItemsDeleted().intValue());

		Assert.assertEquals(CronJobResult.FAILURE, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(sessionService, Mockito.never()).removeAttribute("is.hmc.session");
		Mockito.verify(modelService, Mockito.never()).remove(Mockito.any(PK.class));
	}


	@Test
	public void testNotEmptyPkStreamPerformedJob()
	{
		final MediaModel mediaPk = new MediaModel();
		final DataInputStream dis = new DataInputStream(buildUpStream("pika", "poka", "nuka"));

		Mockito.when(mediaService.getStreamFromMedia(mediaPk)).thenReturn(dis);

		final RemoveItemsCronJobModel cronJob = new RemoveItemsCronJobModel();
		cronJob.setItemPKs(mediaPk); //null item pk media

        PerformResult result = null;
        try
        {
            TestUtils.disableFileAnalyzer("Expected error logs inside ");
            result = performable.perform(cronJob);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
        }

		Assert.assertEquals("should not process not a PK formatted entries ", 0, cronJob.getItemsRefused().intValue());
		Assert.assertEquals("should not process not a PK formatted entries ", 0, cronJob.getItemsDeleted().intValue());
		Assert.assertEquals(CronJobResult.FAILURE, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(modelService, Mockito.never()).remove(Mockito.any(PK.class));
	}


	@Test
	public void testNotEmptyInvalidPkStreamPerformedJob()
	{
		final MediaModel mediaPk = new MediaModel();
		//media

		final DataInputStream dis = new DataInputStream(buildUpStream("pika", "poka", "nuka"));

		Mockito.when(mediaService.getStreamFromMedia(mediaPk)).thenReturn(dis);

		final RemoveItemsCronJobModel cronJob = new RemoveItemsCronJobModel();
		cronJob.setItemPKs(mediaPk);

        PerformResult result = null;
        try
        {
            TestUtils.disableFileAnalyzer("Expected error logs inside ");
            result = performable.perform(cronJob);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
        }

		Assert.assertEquals("should not process not a PK formatted entries ", 0, cronJob.getItemsRefused().intValue());
		Assert.assertEquals("should not process not a PK formatted entries ", 0, cronJob.getItemsDeleted().intValue());
		Assert.assertEquals(CronJobResult.FAILURE, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(modelService, Mockito.never()).remove(Mockito.any(PK.class));
	}


	@Test
	public void testNotEmptyValidPkStreamPerformedJob()
	{
		final ItemModel itemMock = Mockito.mock(ItemModel.class);

		final MediaModel mediaPk = new MediaModel();
		//media

		final PK one = PK.createFixedUUIDPK(102, 1);
		final PK two = PK.createFixedUUIDPK(102, 2);
		final PK three = PK.createFixedUUIDPK(102, 3);

		final DataInputStream dis = new DataInputStream(buildUpStream(one, two, three));

		Mockito.when((ItemModel) modelService.get(Mockito.any(PK.class))).thenReturn(itemMock);

		Mockito.when(mediaService.getStreamFromMedia(mediaPk)).thenReturn(dis);

		Mockito.when(permissionCheckingService.checkItemPermission(Mockito.any(ItemModel.class), Mockito.eq("remove"))).thenReturn(
				permissionResult);

		Mockito.doAnswer(new Answer<PK>()
		{

			@Override
			public PK answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				if (one.equals(args[0]))
				{
					throw new ModelRemovalException("It is one don't do this", null);
				}
				if (three.equals(args[0]))
				{
					throw new ModelRemovalException("It is three don't do this", null);
				}
				return null;
			}
		}).when(modelService).remove(Mockito.any(PK.class));


		final RemoveItemsCronJobModel cronJob = new RemoveItemsCronJobModel();
		cronJob.setItemPKs(mediaPk); //null item pk media

        PerformResult result = null;
        try
        {
            TestUtils.disableFileAnalyzer("Expected error logs inside ");
            result = performable.perform(cronJob);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
        }
        //TODO uncomment when we assigne the SL accessmanager
		Assert.assertEquals(2, cronJob.getItemsRefused().intValue());
		Assert.assertEquals(1, cronJob.getItemsDeleted().intValue());

		Assert.assertEquals(CronJobResult.FAILURE, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(modelService, Mockito.times(0 + 1)).save(cronJob); //one save in finally section
	}


	@Test
	public void testNotEmptyValidPkStreamWithPreviousCallPerformedJob()
	{
		final MediaModel mediaPk = new MediaModel();
		//media

		final RemoveItemsCronJobModel cronJob = Mockito.spy(new RemoveItemsCronJobModel());
		cronJob.setItemPKs(mediaPk);

		Mockito.when(cronJob.getItemsDeleted()).thenReturn(Integer.valueOf(2)); //previously deleted 2 items

		final PK one = PK.createFixedUUIDPK(102, 1);
		final PK two = PK.createFixedUUIDPK(102, 2);
		final PK three = PK.createFixedUUIDPK(102, 3);

		final DataInputStream dis = new DataInputStream(buildUpStream(one, two, three));

		Mockito.when(mediaService.getStreamFromMedia(mediaPk)).thenReturn(dis);
		Mockito.when(permissionCheckingService.checkItemPermission(Mockito.any(ItemModel.class), Mockito.eq("remove"))).thenReturn(
				permissionResult);
		Mockito.doAnswer(new Answer<PK>()
		{

			@Override
			public PK answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				//one or two should never been called - skipped because of the previous call
				if (one.equals(args[0]))
				{
					throw new ModelRemovalException("It is one don't do this", null);
				}
				if (two.equals(args[0]))
				{
					throw new ModelRemovalException("It is two don't do this", null);
				}

				return null;
			}
		}).when(modelService).remove(Mockito.any(PK.class));

		//this also be default resolves all AccessManager work
		Mockito.doAnswer(new Answer<ItemModel>()
		{

			@Override
			public ItemModel answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				//one or two should never been called - skipped because of the previous call
				if (three.equals(args[0]))
				{
					return cronJob;
				}
				return null;
			}
		}).when(modelService).get(Mockito.any(PK.class));

		final PerformResult result = performable.perform(cronJob);

		Assert.assertEquals("Refused items should be skipped due the delete init param ", 0, cronJob.getItemsRefused().intValue());

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(modelService, Mockito.times(1)).remove(Mockito.any(PK.class));
		Mockito.verify(modelService, Mockito.times(0 + 1)).save(cronJob); //one save in finally section
	}

	@Test
	public void testNotEmptyValidPkStreamWithSkipAllCallPerformedJob()
	{
		final MediaModel mediaPk = new MediaModel();
		//media

		final RemoveItemsCronJobModel cronJob = Mockito.spy(new RemoveItemsCronJobModel());
		cronJob.setItemPKs(mediaPk);

		Mockito.when(cronJob.getItemsDeleted()).thenReturn(Integer.valueOf(3)); //previously deleted 3 items

		final PK one = PK.createFixedUUIDPK(102, 1);
		final PK two = PK.createFixedUUIDPK(102, 2);
		final PK three = PK.createFixedUUIDPK(102, 3);

		final DataInputStream dis = new DataInputStream(buildUpStream(one, two, three));

		Mockito.when(mediaService.getStreamFromMedia(mediaPk)).thenReturn(dis);

		Mockito.doAnswer(new Answer<PK>()
		{

			@Override
			public PK answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
                final Object givenPK = args[0];
				//one or two should never been called - skipped because of the previous call
				if (one.equals(givenPK))
				{
					throw new ModelRemovalException("It is one don't do this", null);
				}
				if (two.equals(givenPK))
				{
					throw new ModelRemovalException("It is two don't do this", null);
				}

				if (three.equals(givenPK))
				{
					throw new ModelRemovalException("It is three don't do this", null);
				}

				return null;
			}
		}).when(modelService).remove(Mockito.any(PK.class));

		final PerformResult result = performable.perform(cronJob);

		Assert.assertEquals("Refused items should be skipped due the delete init param ", 0, cronJob.getItemsRefused().intValue());

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(modelService, Mockito.times(0)).remove(Mockito.any(PK.class));
		Mockito.verify(modelService, Mockito.times(0 + 1)).save(cronJob); //one save in finally section
	}


	@Test
	public void testNotEmptyValidPkStreamWithRefreshPerformedJob()
	{
		final ItemModel itemMock = Mockito.mock(ItemModel.class);
		final MediaModel mediaPk = new MediaModel();

		final PK one = PK.createFixedUUIDPK(102, 1);
		final PK two = PK.createFixedUUIDPK(102, 2);
		final PK three = PK.createFixedUUIDPK(102, 3);

		final DataInputStream dis = new DataInputStream(buildUpStream(one, two, three));
		//
		Mockito.when((ItemModel) modelService.get(Mockito.any(PK.class))).thenReturn(itemMock);

		Mockito.when(mediaService.getStreamFromMedia(mediaPk)).thenReturn(dis);

		//Mockito.when(Boolean.valueOf(accessManager.canRemoveInstance(Mockito.any(Item.class)))).thenReturn(Boolean.TRUE);
		Mockito.when(permissionCheckingService.checkItemPermission(Mockito.any(ItemModel.class), Mockito.eq("remove"))).thenReturn(
				permissionResult);

		Mockito.when(Boolean.valueOf(performable.isUpdateProgressNeeded(Mockito.anyLong()))).thenReturn(Boolean.TRUE);
		Mockito.doAnswer(new Answer<PK>()
		{

			@Override
			public PK answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				if (two.equals(args[0]))
				{
					throw new ModelRemovalException("It is two don't do this", null);
				}
				if (three.equals(args[0]))
				{
					throw new ModelRemovalException("It is three don't do this", null);
				}
				return null;
			}
		}).when(modelService).remove(Mockito.any(PK.class));

		final RemoveItemsCronJobModel cronJob = new RemoveItemsCronJobModel();
		cronJob.setItemPKs(mediaPk); //null item pk media

		final PerformResult result = performable.perform(cronJob);

		Assert.assertEquals(2, cronJob.getItemsRefused().intValue());
		Assert.assertEquals(1, cronJob.getItemsDeleted().intValue());

		Assert.assertEquals(CronJobResult.FAILURE, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(modelService, Mockito.times(3 + 1)).save(cronJob); //three partial saves + one save in finally section
	}


	private InputStream buildUpStream(final Object... args)
	{
		//ByteInputStream  bis = new ByteInputStream();
		final StringBuilder builder = new StringBuilder(1000);
		for (final Object arg : args)
		{
			builder.append(arg).append(CharUtils.CR);
		}
		return new ByteArrayInputStream(builder.toString().getBytes());
	}
}
