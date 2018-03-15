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
package de.hybris.platform.jobs.maintenance.impl;

import de.hybris.platform.cronjob.model.CleanupDynamicProcessDefinitionsCronJobModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.jalo.DynamicProcessDefinition;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;


public class CleanupDynamicProcessDefinitionsStrategy implements
		MaintenanceCleanupStrategy<DynamicProcessDefinitionModel, CleanupDynamicProcessDefinitionsCronJobModel>
{
	private final static Logger LOG = Logger.getLogger(CleanupDynamicProcessDefinitionsStrategy.class);

	private static final String QUERY;
	private static final String VERSION_PART;
	private static final String TIME_PART;
	private ModelService modelService;

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public FlexibleSearchQuery createFetchQuery(final CleanupDynamicProcessDefinitionsCronJobModel cronJob)
	{
		final StringBuilder query = new StringBuilder(QUERY);
		final Builder<String, Object> paramsBuilder = ImmutableMap.<String, Object> builder();

		paramsBuilder.put("active", Boolean.TRUE);
		paramsBuilder.put("inactive", Boolean.FALSE);
		paramsBuilder.put("succeeded", ProcessState.SUCCEEDED);

		if (cronJob.getVersionThreshold() != null)
		{
			query.append(VERSION_PART);
			paramsBuilder.put("versionThreshold", cronJob.getVersionThreshold());
		}

		if (cronJob.getTimeThreshold() != null)
		{
			query.append(TIME_PART);
			final Date now = new Date();
			final Date thresholdDate = new Date(now.getTime() - cronJob.getTimeThreshold().intValue() * 1000);
			paramsBuilder.put("timeThreshold", thresholdDate);
		}

		LOG.info("Searching for inactive definitions with versionThreshold set to " + cronJob.getVersionThreshold()
				+ " and timeThreshold set to " + cronJob.getTimeThreshold() + "s.");

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query.toString(), paramsBuilder.build());
		flexibleSearchQuery.setResultClassList(Arrays.asList(DynamicProcessDefinition.class));
		return flexibleSearchQuery;
	}

	@Override
	public void process(final List<DynamicProcessDefinitionModel> elements)
	{
		modelService.removeAll(elements);
	}

	private static String processDefinitionVersion()
	{
		return Config.isPostgreSQLUsed() ? "CAST( { d:" + DynamicProcessDefinitionModel.VERSION + " } as varchar)" : "{ d:"
				+ DynamicProcessDefinitionModel.VERSION + " }";
	}

	static
	{
		QUERY = "select {d:PK} from {" + DynamicProcessDefinitionModel._TYPECODE + " as d left outer join "
				+ DynamicProcessDefinitionModel._TYPECODE + " as ad on {d:" + DynamicProcessDefinitionModel.CODE + "} = {ad:"
				+ DynamicProcessDefinitionModel.CODE + "} and {ad:" + DynamicProcessDefinitionModel.ACTIVE + "} = ?active} where {d:"
				+ DynamicProcessDefinitionModel.ACTIVE + "}=?inactive and not exists ({{select * from {"
				+ BusinessProcessModel._TYPECODE + " as bp} where {bp:" + BusinessProcessModel.PROCESSDEFINITIONNAME + "}={d:"
				+ DynamicProcessDefinitionModel.CODE + "} and {bp:" + BusinessProcessModel.PROCESSDEFINITIONVERSION + "}= "
				+ processDefinitionVersion() + " and {bp:" + BusinessProcessModel.STATE + "}<>?succeeded}})";

		VERSION_PART = "  and ({ad:" + DynamicProcessDefinitionModel.PK + "} is null or ({ad:"
				+ DynamicProcessDefinitionModel.VERSION + "} - {d:" + DynamicProcessDefinitionModel.VERSION
				+ "}) > ?versionThreshold)";

		TIME_PART = "  and (({d:" + DynamicProcessDefinitionModel.MODIFIEDTIME + "} is null and {d:"
				+ DynamicProcessDefinitionModel.CREATIONTIME + "} < ?timeThreshold) or ({d:"
				+ DynamicProcessDefinitionModel.MODIFIEDTIME + "} is not null and {d:" + DynamicProcessDefinitionModel.MODIFIEDTIME
				+ "} < ?timeThreshold))";

	}
}
