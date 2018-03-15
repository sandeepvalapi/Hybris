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
package com.hybris.training.fulfilmentprocess.test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import com.hybris.training.fulfilmentprocess.actions.order.SetOrderExpirationTimeAction;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class SetOrderExpirationTimeActionTest
{
	@InjectMocks
	private final SetOrderExpirationTimeAction action = new SetOrderExpirationTimeAction();

	@Mock
	private ModelService modelService;
	@Mock
	private TimeService timeService;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldSetOrderExpirationTime()
	{
		final OrderProcessModel businessProcessModel = mock(OrderProcessModel.class);
		final OrderModel order = mock(OrderModel.class);
		final Date expireDate = mock(Date.class);
		given(businessProcessModel.getOrder()).willReturn(order);
		given(timeService.getCurrentTime()).willReturn(expireDate);

		action.executeAction(businessProcessModel);
		verify(order).setExpirationTime(expireDate);
		verify(modelService).save(order);
	}
}
