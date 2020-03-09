/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.warehouse;

import de.hybris.platform.commerceservices.enums.WarehouseConsignmentState;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehouse.Warehouse2ProcessAdapter;
import de.hybris.platform.warehouse.WarehouseConsignmentStatus;
import com.hybris.training.fulfilmentprocess.constants.TrainingFulfilmentProcessConstants;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


public class DefaultWarehouse2ProcessAdapter implements Warehouse2ProcessAdapter
{
	private Map<WarehouseConsignmentStatus, WarehouseConsignmentState> statusMap;
	private ModelService modelService;

	private BusinessProcessService businessProcessService;

	@Override
	public void receiveConsignmentStatus(final ConsignmentModel consignment, final WarehouseConsignmentStatus status)
	{
		for (final ConsignmentProcessModel process : consignment.getConsignmentProcesses())
		{
			final WarehouseConsignmentState state = getStatusMap().get(status);
			if (state == null)
			{
				throw new IllegalStateException("No mapping for WarehouseConsignmentStatus: " + status);
			}
			process.setWarehouseConsignmentState(state);
			getModelService().save(process);
			getBusinessProcessService().triggerEvent(
					process.getCode() + "_" + TrainingFulfilmentProcessConstants.WAIT_FOR_WAREHOUSE);
		}
	}

	protected Map<WarehouseConsignmentStatus, WarehouseConsignmentState> getStatusMap()
	{
		return statusMap;
	}

	@Required
	public void setStatusMap(final Map<WarehouseConsignmentStatus, WarehouseConsignmentState> statusMap)
	{
		this.statusMap = statusMap;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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
