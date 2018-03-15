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
package de.hybris.platform.servicelayer.cronjob;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.cronjob.model.CleanUpCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.MoveMediaCronJobModel;
import de.hybris.platform.cronjob.model.MoveMediaJobModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;


/**
 * Demo test class for {@link CronJobService}.
 */
@DemoTest
public class CronJobServiceDemoTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private CronJobService cronJobService;
	@Resource
	private ModelService modelService;

	@Test
	public void testCreate()
	{
		final CronJobModel cronJob = modelService.create("CronJob");
		assertNotNull("cronjob is null", cronJob);
		assertNull("job is not null", cronJob.getJob());
	}


	@Test
	public void testPerform() throws InterceptorException
	{
		//create a CronJobModel
		final MoveMediaCronJobModel cronJob = modelService.create(MoveMediaCronJobModel.class);
		cronJob.setCode("test");

		//create a MediaModel (ImpExMedia as it does not need a CatalogVersion)
		new CoreBasicDataCreator().createRootMediaFolder();
		final ImpExMediaModel media = modelService.create(ImpExMediaModel.class);
		media.setCode("testMedia");

		cronJob.setMedias((Collection) Collections.singletonList(media));
		modelService.save(media);

		//create a FolderModel
		final MediaFolderModel newFolder = modelService.create(MediaFolderModel.class);
		newFolder.setQualifier("testFolder");
		cronJob.setTargetFolder(newFolder);
		modelService.save(newFolder);

		//create a JobModel
		final MoveMediaJobModel job = new MoveMediaJobModel();
		job.setCode("test");
		cronJob.setJob(job);
		modelService.save(job);

		modelService.save(cronJob);

		//perform a CronJobModel
		cronJobService.performCronJob(cronJob, true);

		modelService.refresh(media);

		assertEquals("folders are not equal", newFolder, media.getFolder());

	}

    @Test
    public void testExistingCronJobFactory() throws Exception
    {
        // given
        final ServicelayerJobModel job = modelService.create(ServicelayerJobModel.class);
        job.setCode("cleanUpJob");
        job.setSpringId("cleanUpJobPerformable");
        job.setSpringIdCronJobFactory("testCronJobFactory");
        modelService.save(job);

        // when
        final CronJobFactory<CleanUpCronJobModel, JobModel> cronJobFactory = cronJobService.<CleanUpCronJobModel, JobModel>getCronJobFactory(job);
        final CleanUpCronJobModel cronJob = cronJobFactory.createCronJob(job);

        // then
        assertThat(cronJob).isNotNull();
    }

	@Test
	public void testGetGenericCronJobFactory()
	{
        // given
		final ServicelayerJobModel job = modelService.create(ServicelayerJobModel.class);
		job.setCode("cleanUpJob");
		job.setSpringId("cleanUpJobPerformable");
		modelService.save(job);

        // when
        final CronJobFactory<CleanUpCronJobModel, JobModel> cronJobFactory = cronJobService.<CleanUpCronJobModel, JobModel>getCronJobFactory(job);
        final CleanUpCronJobModel cronJob = cronJobFactory.createCronJob(job);

        // then
        assertThat(cronJob).isNotNull();
	}

	@Test
	public void testGetJobPerformable()
	{
		final ServicelayerJobModel job = modelService.create(ServicelayerJobModel.class);
		job.setCode("cleanUpJobPerformable");
		job.setSpringId("cleanUpJobPerformable");
		//an existing bean id

		try
		{
			modelService.save(job);
		}
		catch (final SystemException exception)
		{
			fail("We should be able to save an incorrect servicelayerjob");
		}
		try
		{
			cronJobService.getPerformable(job);

		}
		catch (final NoSuchBeanDefinitionException ses)
		{
			fail("Should not throw a NoSuchBeanDefinitionException");
		}
	}

	@Test
	public void testGetExistingCronJob()
	{

		final MoveMediaCronJobModel cronJob = new MoveMediaCronJobModel();
		cronJob.setCode("test");

		final MoveMediaJobModel job = new MoveMediaJobModel();
		job.setCode("test");
		modelService.save(job);

		cronJob.setJob(job);
		modelService.save(cronJob);

		Assert.assertEquals("PKs of the assigned CronJob should be the same ", cronJob.getPk(), cronJobService.getCronJob("test")
				.getPk());
	}

	@Test
	public void testGetExistingJob() throws BusinessException
	{

		final MoveMediaJobModel job = new MoveMediaJobModel();
		job.setCode("test");
		modelService.save(job);

		cronJobService.getJob("test");

		Assert.assertEquals("PKs of the assigned Job should be the same ", job.getPk(), cronJobService.getJob("test").getPk());
	}
}
