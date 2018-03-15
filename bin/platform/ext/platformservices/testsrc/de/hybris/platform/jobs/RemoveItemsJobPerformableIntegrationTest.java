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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.RemoveItemsCronJobModel;
import de.hybris.platform.cronjob.model.RemoveItemsJobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * {@link RemoveItemsJobModel} integrity test.
 */
@IntegrationTest
public class RemoveItemsJobPerformableIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private MediaService mediaService;

	@Resource
	private ModelService modelService;

	@Resource
	private CronJobService cronJobService;

	@Resource
	private SessionService sessionService;

	@Resource
	private UserService userService;

	private List<PK> allModelsList;

	private RemoveItemsCronJobModel cronJob;

	@Before
	public void setUp()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("FunnyToonzCatalog");
		catalog.setName("FunnyToonzCatalog");

		modelService.save(catalog);

		final CatalogVersionModel version = modelService.create(CatalogVersionModel.class);
		version.setCatalog(catalog);
		version.setVersion("1.0");

		modelService.save(version);

		allModelsList = Arrays.asList(//
				createSingleModel("pitch"), createSingleModel("potch"),//
				createSingleModel("connie"), createSingleModel("slimPig"),//
				createSingleModel("mio"), createSingleModel("mao")//
				);

		final MediaModel media = modelService.create(MediaModel.class);
		media.setCode("ToonzWeDon'tWantAnyMoreList");
		media.setMime("text/plain");
		media.setRealfilename(media.getCode() + ".txt");
		media.setCatalogVersion(version);
		modelService.save(media);

		mediaService.setStreamForMedia(media, new DataInputStream(new ItemListInputStream(allModelsList)));

		final JobModel job = createJob();

		modelService.save(job);

		cronJob = modelService.create(RemoveItemsCronJobModel.class);
		cronJob.setCode("TestDeleteToonzCronJob");
		cronJob.setJob(job);

		cronJob.setItemPKs(media);
		cronJob.setLogToFile(Boolean.FALSE);
		cronJob.setLogToDatabase(Boolean.FALSE);
		cronJob.setActive(Boolean.TRUE);
		cronJob.setSessionUser(userService.getAdminUser());

		modelService.save(cronJob);
	}


	@Test
	public void testRemoveAllItemsWithoutSufficientRights()
	{
		cronJob.setSessionUser(userService.getAnonymousUser());
		modelService.save(cronJob);

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				cronJobService.performCronJob(cronJob, true);
			}
		});

		modelService.refresh(cronJob);

		Assert.assertNotNull(cronJob.getItemsDeleted());
		Assert.assertEquals("Should not  be able to delete all items ", 0, cronJob.getItemsDeleted().intValue());
		Assert.assertNotNull(cronJob.getItemsRefused());
		Assert.assertEquals("Should  be there all refused disposals ", allModelsList.size(), cronJob.getItemsRefused().intValue());
	}


	@Test
	public void testRemoveAllItems()
	{
		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				cronJobService.performCronJob(cronJob, true);
			}
		});

		modelService.refresh(cronJob);

		Assert.assertNotNull(cronJob.getItemsDeleted());
		Assert.assertEquals("Should be able to delete all items ", allModelsList.size(), cronJob.getItemsDeleted().intValue());
		Assert.assertNotNull(cronJob.getItemsRefused());
		Assert.assertEquals("Should not be there any refused disposals ", 0, cronJob.getItemsRefused().intValue());
	}


	@Test
	public void testRemoveAlmostAllItems()
	{
		modelService.remove(allModelsList.get(2)); //we want connie
		modelService.remove(allModelsList.get(4)); //we want mio

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				cronJobService.performCronJob(cronJob, true);
			}
		});

		modelService.refresh(cronJob);

		Assert.assertNotNull(cronJob.getItemsDeleted());
		Assert.assertEquals("Should be able to delete all items ", 4, cronJob.getItemsDeleted().intValue());
		Assert.assertNotNull(cronJob.getItemsRefused());
		Assert.assertEquals("Should not be there any refused disposals ", 2, cronJob.getItemsRefused().intValue());
	}

	private PK createSingleModel(final String name)
	{
		final TitleModel tmodel = modelService.create(TitleModel.class);
		tmodel.setCode(name);
		tmodel.setName(name);

		modelService.save(tmodel);

		return tmodel.getPk();
	}



	private class ItemListInputStream extends InputStream
	{
		private Iterator<PK> iter = null;
		private byte[] buffer = {};
		private int pos = -1;
		private final String newline = System.getProperty("line.separator");

		/**
		 * Constructor.
		 * 
		 * @param itemList
		 *           A list of items containing PK Strings or Item objects.
		 */
		public ItemListInputStream(final List<PK> itemList)
		{
			super();
			this.iter = itemList.iterator();
		}

		@Override
		public int available() throws IOException
		{
			return (pos > buffer.length) ? 0 : 1;
		}

		@Override
		public int read() throws IOException
		{
			if (++pos == buffer.length && iter.hasNext())
			{
				final PK pk = iter.next();
				buffer = (pk.toString() + newline).getBytes();
				pos = 0;
			}
			final int result = (pos < buffer.length) ? buffer[pos] : -1;
			return result;
		}
	}


	private JobModel createJob()
	{
		//final RemoveItemsJobModel job = modelService.create(RemoveItemsJobModel.class);
		final ServicelayerJobModel job = modelService.create(ServicelayerJobModel.class);
		job.setCode("TestDeleteToonzJob");
		job.setSpringId("removeItemsJob");
		return job;
	}

}
