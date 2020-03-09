/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.order;

import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Optional;


/**
 *
 */
public class SubprocessesCompletedAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(SubprocessesCompletedAction.class);

	private static final String PROCESS_MSG = "Process: ";

	@Override
	public Transition executeAction(final OrderProcessModel process)
	{
		LOG.info(PROCESS_MSG + process.getCode() + " in step " + getClass());
		LOG.info(PROCESS_MSG + process.getCode() + " is checking for  " + process.getConsignmentProcesses().size()
				+ " subprocess results");

		final OrderModel order = process.getOrder();
		final Collection<ConsignmentProcessModel> consignmentProcesses = process.getConsignmentProcesses();

		if (CollectionUtils.isNotEmpty(consignmentProcesses))
		{
			final Optional<ConsignmentProcessModel> atleastOneConsProcessNotDone = consignmentProcesses.stream()
					.filter(consProcess -> !consProcess.isDone())
					.findFirst();
			final boolean allConsProcessNotDone = consignmentProcesses.stream().allMatch(consProcess -> !consProcess.isDone());

			if (allConsProcessNotDone)
			{
				LOG.info(PROCESS_MSG + process.getCode() + " found all subprocesses incomplete");
				order.setDeliveryStatus(DeliveryStatus.NOTSHIPPED);
				save(order);
				return Transition.NOK;
			}
			else if (atleastOneConsProcessNotDone.isPresent())
			{
				LOG.info(PROCESS_MSG + process.getCode() + " found subprocess " + atleastOneConsProcessNotDone.get().getCode()
						+ " incomplete -> wait again!");
				order.setDeliveryStatus(DeliveryStatus.PARTSHIPPED);
				save(order);
				return Transition.NOK;
			}
		}

		LOG.info(PROCESS_MSG + process.getCode() + " found all subprocesses complete");
		order.setDeliveryStatus(DeliveryStatus.SHIPPED);
		save(order);
		return Transition.OK;
	}
}
