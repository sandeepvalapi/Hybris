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
package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CompositeEntryModel;
import de.hybris.platform.cronjob.model.MoveMediaJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class CompositeJobPerformableIntegrationTest extends ServicelayerBaseTest
{

	@Resource
	private ModelService modelService;

	@Test
	public void testWrongCompositeJobAssignment()
	{
		final MoveMediaJobModel jobModel = modelService.create(MoveMediaJobModel.class);
		jobModel.setCode("someCode");

		modelService.save(jobModel);

		final CompositeEntryModel compositeEntryModel = modelService.create(CompositeEntryModel.class);
		compositeEntryModel.setCode("wrapperCronJOb");
		//compositeCronJob.setCompositeEntries(null);
		compositeEntryModel.setTriggerableJob(jobModel);

		try
		{
			modelService.save(compositeEntryModel);
			Assert.fail("Should not be able to save a composite");
		}
		catch (final ModelSavingException mse)
		{
			//ok here
		}

	}

	@Test
	public void testCorrectCompositeJobAssignment()
	{
		final ServicelayerJobModel jobModel = modelService.create(ServicelayerJobModel.class);
		jobModel.setCode("someCode");
		jobModel.setSpringId("moveMediaJob");

		modelService.save(jobModel);

		final CompositeEntryModel compositeEntryModel = modelService.create(CompositeEntryModel.class);
		compositeEntryModel.setCode("wrapperCronJOb");
		//compositeCronJob.setCompositeEntries(null);
		compositeEntryModel.setTriggerableJob(jobModel);

		modelService.save(compositeEntryModel);

	}

	@Test
	public void testWrongTriggerJobAssignment()
	{
		final MoveMediaJobModel jobModel = modelService.create(MoveMediaJobModel.class);
		jobModel.setCode("someCode");

		modelService.save(jobModel);

		final TriggerModel triggerModel = modelService.create(TriggerModel.class);
		//triggerModel.setCode("wrapperCronJOb");
		//compositeCronJob.setCompositeEntries(null);
		triggerModel.setJob(jobModel);

		try
		{
			modelService.save(triggerModel);
			Assert.fail("Should not be able to save a trigger");
		}
		catch (final ModelSavingException mse)
		{
			//ok here
		}

	}

	@Test
	public void testCorrectTriggerJobAssignment()
	{
		final ServicelayerJobModel jobModel = modelService.create(ServicelayerJobModel.class);
		jobModel.setCode("someCode");
		jobModel.setSpringId("moveMediaJob");

		modelService.save(jobModel);

		final TriggerModel triggerModel = modelService.create(TriggerModel.class);
		//triggerModel.setCode("wrapperCronJOb");
		//compositeCronJob.setCompositeEntries(null);
		triggerModel.setJob(jobModel);

		modelService.save(triggerModel);

	}


}
