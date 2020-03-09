/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.consignment;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.warehouse.Process2WarehouseAdapter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class SendConsignmentToWarehouseAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(SendConsignmentToWarehouseAction.class);

	private Process2WarehouseAdapter process2WarehouseAdapter;

	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		getProcess2WarehouseAdapter().prepareConsignment(process.getConsignment());
		process.setWaitingForConsignment(true);
		getModelService().save(process);
		LOG.info("Setting waitForConsignment to true");
	}

	@Required
	public void setProcess2WarehouseAdapter(final Process2WarehouseAdapter process2WarehouseAdapter)
	{
		this.process2WarehouseAdapter = process2WarehouseAdapter;
	}

	protected Process2WarehouseAdapter getProcess2WarehouseAdapter()
	{
		return process2WarehouseAdapter;
	}
}
