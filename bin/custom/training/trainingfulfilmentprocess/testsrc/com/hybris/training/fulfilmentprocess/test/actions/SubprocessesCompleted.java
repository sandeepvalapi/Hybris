/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test.actions;


import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;

import org.apache.log4j.Logger;


/**
 *
 */
public class SubprocessesCompleted extends TestActionTemp<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(SubprocessesCompleted.class);

	@Override
	public String execute(final OrderProcessModel process) throws Exception
	{
		for (final ConsignmentProcessModel subProcess : process.getConsignmentProcesses())
		{
			modelService.refresh(subProcess);
			if (!subProcess.isDone())
			{

				LOG.info("Process: " + process.getCode() + " found  subprocess " + subProcess.getCode()
						+ " incomplete -> wait again!");
				//getQueueService().actionExecuted(process, this);
				return "NOK";
			}
		}
		//getQueueService().actionExecuted(process, this);
		return "OK";
	}


}
