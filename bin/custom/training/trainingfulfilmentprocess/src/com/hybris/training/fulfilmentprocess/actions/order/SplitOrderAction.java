/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.order;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.OrderSplittingService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import com.hybris.training.fulfilmentprocess.constants.TrainingFulfilmentProcessConstants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class SplitOrderAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(SplitOrderAction.class);

	private OrderSplittingService orderSplittingService;
	private BusinessProcessService businessProcessService;

	@Override
	public void executeAction(final OrderProcessModel process) throws Exception
	{
		if (LOG.isInfoEnabled())
		{
			LOG.info("Process: " + process.getCode() + " in step " + getClass());
		}

		// find the order's entries that are not already allocated to consignments
		final List<AbstractOrderEntryModel> entriesToSplit = new ArrayList<AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel entry : process.getOrder().getEntries())
		{
			if (entry.getConsignmentEntries() == null || entry.getConsignmentEntries().isEmpty())
			{
				entriesToSplit.add(entry);
			}
		}

		final List<ConsignmentModel> consignments = getOrderSplittingService().splitOrderForConsignment(process.getOrder(),
				entriesToSplit);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Splitting order into " + consignments.size() + " consignments.");
		}

		int index = 0;
		for (final ConsignmentModel consignment : consignments)
		{
			final ConsignmentProcessModel subProcess = getBusinessProcessService().<ConsignmentProcessModel> createProcess(

					process.getCode() + "_" + (++index), TrainingFulfilmentProcessConstants.CONSIGNMENT_SUBPROCESS_NAME);

			subProcess.setParentProcess(process);
			subProcess.setConsignment(consignment);
			save(subProcess);

			getBusinessProcessService().startProcess(subProcess);

		}
		setOrderStatus(process.getOrder(), OrderStatus.ORDER_SPLIT);
	}

	protected OrderSplittingService getOrderSplittingService()
	{
		return orderSplittingService;
	}

	@Required
	public void setOrderSplittingService(final OrderSplittingService orderSplittingService)
	{
		this.orderSplittingService = orderSplittingService;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}
}
