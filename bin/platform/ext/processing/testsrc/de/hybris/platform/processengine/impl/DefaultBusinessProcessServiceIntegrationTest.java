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
package de.hybris.platform.processengine.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.task.TaskConditionModel;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.junit.Test;


@IntegrationTest
public class DefaultBusinessProcessServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private DefaultBusinessProcessService defaultBusinessProcessService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;

	@Test
	public void triggerEventTest()
	{
		final String eventName = "anEvent";

		defaultBusinessProcessService.triggerEvent(eventName);

		final TaskConditionModel taskCondition = modelService.create(TaskConditionModel.class);
		taskCondition.setUniqueID(eventName);
		final TaskConditionModel taskConditionResult = flexibleSearchService.getModelByExample(taskCondition);

		Assertions.assertThat(taskConditionResult).isNotNull();
		Assertions.assertThat(taskConditionResult.getUniqueID()).isNotNull();
		Assertions.assertThat(taskConditionResult.getUniqueID()).isEqualTo(eventName);
	}

	@Test
	public void triggerEventTestWithExpirationDate()
	{
		final String eventName = "anEvent";
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 10); // add 10 days  

		final Date expirationDate = cal.getTime();

		defaultBusinessProcessService.triggerEvent(eventName, expirationDate);

		final TaskConditionModel taskCondition = modelService.create(TaskConditionModel.class);
		taskCondition.setUniqueID(eventName);
		final TaskConditionModel taskConditionResult = flexibleSearchService.getModelByExample(taskCondition);

		Assertions.assertThat(taskConditionResult).isNotNull();
		Assertions.assertThat(taskConditionResult.getUniqueID()).isNotNull();
		Assertions.assertThat(taskConditionResult.getUniqueID()).isEqualTo(eventName);
		Assertions.assertThat(taskConditionResult.getExpirationDate()).isEqualTo(expirationDate);
	}
}
