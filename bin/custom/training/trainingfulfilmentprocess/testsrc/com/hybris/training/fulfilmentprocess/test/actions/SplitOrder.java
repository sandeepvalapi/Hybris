/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test.actions;

import de.hybris.platform.core.Registry;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import com.hybris.training.fulfilmentprocess.constants.TrainingFulfilmentProcessConstants;

import java.util.Arrays;

import org.apache.log4j.Logger;


/**
 *
 */
public class SplitOrder extends TestActionTemp
{
	private static final Logger LOG = Logger.getLogger(SplitOrder.class);

	private int subprocessCount = 1;

	public void setSubprocessCount(final int subprocessCount)
	{
		this.subprocessCount = subprocessCount;
	}

	@Override
	public String execute(final BusinessProcessModel process) throws Exception
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass());

		final BusinessProcessParameterModel warehouseCounter = new BusinessProcessParameterModel();
		warehouseCounter.setName(TrainingFulfilmentProcessConstants.CONSIGNMENT_COUNTER);
		warehouseCounter.setProcess(process);
		warehouseCounter.setValue(Integer.valueOf(subprocessCount));
		save(warehouseCounter);

		final BusinessProcessParameterModel params = new BusinessProcessParameterModel();
		params.setName(TrainingFulfilmentProcessConstants.PARENT_PROCESS);
		params.setValue(process.getCode());


		for (int i = 0; i < subprocessCount; i++)
		{
			final ConsignmentProcessModel consProcess = modelService.create(ConsignmentProcessModel.class);
			consProcess.setParentProcess((OrderProcessModel) process);
			consProcess.setCode(process.getCode() + "_" + i);
			consProcess.setProcessDefinitionName("consignment-process-test");
			params.setProcess(consProcess);
			consProcess.setContextParameters(Arrays.asList(params));
			consProcess.setState(ProcessState.CREATED);
			modelService.save(consProcess);
			getBusinessProcessService().startProcess(consProcess);
			LOG.info("Subprocess: " + process.getCode() + "_" + i + " started");
		}

		//getQueueService().actionExecuted(process, this);
		return "OK";
	}



	/**
	 * @return the businessProcessService
	 */
	@Override
	public BusinessProcessService getBusinessProcessService()
	{
		return (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
	}

}
