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
package de.hybris.platform.task.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskEvent;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultTaskServiceEventTrigerringIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private DefaultTaskService defaultTaskService;

	@Test
	public void shouldNotFailWhenConditionDoesntExistAndFulfillingByRemoval()
	{
		final TaskEvent event = TaskEvent.fulfilByRemoval("NOT_EXISTING_" + UUID.randomUUID());

		final boolean removed = defaultTaskService.triggerEvent(event);

		assertThat(removed).isFalse();
	}

	@Test
	public void shouldRemoveConditionWhenFulfillingByRemoval()
	{
		final String id = "TEST_" + UUID.randomUUID();
		givenCondition(id);
		final TaskEvent event = TaskEvent.fulfilByRemoval(id);

		final boolean removed = defaultTaskService.triggerEvent(event);

		assertThat(removed).isTrue();
	}

	private TaskConditionModel givenCondition(final String id)
	{
		final TaskConditionModel condition = modelService.create(TaskConditionModel.class);

		condition.setUniqueID(id);

		modelService.save(condition);
		return condition;
	}


}
