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

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.cronjob.model.LogFileModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.processengine.enums.BooleanOperator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;


/**
 * CleanUp JobLogs and LogFiles strategy.
 */
public class CleanUpLogsStrategy implements MaintenanceCleanupStrategy<CronJobModel, CronJobModel>
{

	public static final String JOB_LOGS_QUERY = "SELECT " + JobLogModel.PK + ",{" + JobLogModel.CREATIONTIME + "} FROM {"
			+ JobLogModel._TYPECODE + "} WHERE {" + JobLogModel.CRONJOB + "} in (?cronjob) ORDER BY {" + JobLogModel.CREATIONTIME
			+ "} ASC";
	public static final String LOG_FILES_QUERY = "SELECT " + LogFileModel.PK + " FROM {" + LogFileModel._TYPECODE + "} WHERE {"
			+ LogFileModel.OWNER + "} IN (?cronjob) ORDER BY {" + LogFileModel.CREATIONTIME + "} ASC";
	public static final String CRON_JOBS_QUERY = "SELECT {" + CronJobModel.PK + "} FROM {" + CronJobModel._TYPECODE + "}";

	private static final Logger LOG = Logger.getLogger(CleanUpLogsStrategy.class.getName());

	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;

	@Override
	public FlexibleSearchQuery createFetchQuery(final CronJobModel cjm)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(CRON_JOBS_QUERY);
		fsq.setResultClassList(Arrays.asList(CronJobModel.class));
		fsq.setStart(0);
		fsq.setCount(cjm.getQueryCount());
		return fsq;
	}

	@Override
	public void process(final List<CronJobModel> elements)
	{
		LOG.info("Searching " + elements.size() + " cron jobs for log entries eligible for deletion.");
		final ArrayList<ItemModel> toBeRemoved = new ArrayList<>();
		for (final CronJobModel cronJobModel : elements)
		{
			toBeRemoved.clear();
			final List<JobLogModel> jobLogModels = this.<JobLogModel> getLogModels(cronJobModel, JOB_LOGS_QUERY);
			toBeRemoved.addAll(findLogModels(jobLogModels, cronJobModel.getLogsOperator(), cronJobModel.getLogsDaysOld(),
					cronJobModel.getLogsCount()));

			final List<LogFileModel> logFileModels = this.<LogFileModel> getLogModels(cronJobModel, LOG_FILES_QUERY);
			toBeRemoved.addAll(findLogModels(logFileModels, cronJobModel.getFilesOperator(), cronJobModel.getFilesDaysOld(),
					cronJobModel.getFilesCount()));

			if (!toBeRemoved.isEmpty())
			{
				LOG.info("Removing " + toBeRemoved.size() + " log entries.");
				modelService.removeAll(toBeRemoved);
			}
		}
	}

	private <T> List<T> getLogModels(final CronJobModel cronJobModel, final String queryString)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(queryString);
		fsq.addQueryParameter("cronjob", cronJobModel.getPk().getLong());
		return flexibleSearchService.<T> search(fsq).getResult();
	}

	private List<ItemModel> findLogModels(final List<? extends ItemModel> logModels, final BooleanOperator operator,
			final Integer daysOld, final Integer maxCount)
	{
		int logModelsCount = logModels.size();
		final List<ItemModel> toBeRemoved = new ArrayList<>();
		for (final ItemModel logModel : logModels)
		{
			switch (operator)
			{
				case AND:
					if (isOlderThan(logModel.getCreationtime(), daysOld) && logModelsCount > maxCount)
					{
						logModelsCount--;
						toBeRemoved.add(logModel);
					}
					break;
				case OR:
					if (isOlderThan(logModel.getCreationtime(), daysOld) || logModelsCount > maxCount)
					{
						logModelsCount--;
						toBeRemoved.add(logModel);
					}
					break;
				default:
					throw new IllegalStateException("Unsupported operator: " + operator.getCode() + ".");
			}
		}
		return toBeRemoved;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	private boolean isOlderThan(final Date date, final int daysOld)
	{
		final Date before = DateTime.now().minusDays(daysOld).toDate();
		return date.before(before);
	}

}
