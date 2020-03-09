/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import com.hybris.training.fulfilmentprocess.constants.TrainingFulfilmentProcessConstants;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * CronJob periodically send CleanUpEvent for <b>order-process</b> processes which are in action <b>waitForCleanUp</b>
 */
public class CleanUpFraudOrderJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CleanUpFraudOrderJob.class);
	private BusinessProcessService businessProcessService;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final String processDefinitionName = TrainingFulfilmentProcessConstants.ORDER_PROCESS_NAME;
		final String processCurrentAction = "waitForCleanUp";
		final List<BusinessProcessModel> processes = getAllProcessByDefinitionAndCurrentAction(processDefinitionName,
				processCurrentAction);

		final String eventNameSuffix = "_CleanUpEvent";
		for (final BusinessProcessModel bpm : processes)
		{
			//${process.code}_CleanUpEvent
			final String eventName = bpm.getCode() + eventNameSuffix;
			businessProcessService.triggerEvent(eventName);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected List<BusinessProcessModel> getAllProcessByDefinitionAndCurrentAction(final String processDefinitionName,
			final String processCurrentAction)
	{
		final String query = "select {bp.PK} " + "from {BusinessProcess AS bp  JOIN ProcessTask AS pt ON {bp.pk} = {pt.process} } "
				+ "WHERE {bp.processDefinitionName} = ?processDefinitionName and {pt.action} = ?processCurrentAction";

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		searchQuery.addQueryParameter("processDefinitionName", processDefinitionName);
		searchQuery.addQueryParameter("processCurrentAction", processCurrentAction);
		final SearchResult<BusinessProcessModel> processes = flexibleSearchService.search(searchQuery);
		return processes.getResult();
	}
}
