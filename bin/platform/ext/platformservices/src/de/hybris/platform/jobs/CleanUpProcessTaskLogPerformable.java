/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.jobs;

import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.ProcessTaskLogMaintenanceJobModel;
import de.hybris.platform.processengine.model.ProcessTaskLogModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * This class offers functionality to remove old ProcessTaskLog instances.
 */
public class CleanUpProcessTaskLogPerformable extends AbstractJobPerformable<CronJobModel>
{
	private static final String QUERY = "SELECT {" + ProcessTaskLogModel.PK + "} FROM {" + ProcessTaskLogModel._TYPECODE
			+ " as log} WHERE {log." + ProcessTaskLogModel.STARTDATE + "} < (?age) ORDER BY {log." + ProcessTaskLogModel.STARTDATE
			+ "} DESC";

	private static final Logger LOG = Logger.getLogger(CleanUpProcessTaskLogPerformable.class.getName());


	public FlexibleSearchQuery createFetchQuery(final ProcessTaskLogMaintenanceJobModel model)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(QUERY);

		final LocalDateTime localDateTimeBefore = LocalDateTime.now().minusDays(model.getAge());
		final Date dateBafore = Date.from(localDateTimeBefore.toInstant(ZoneOffset.UTC));
		fsq.setStart(0);
		fsq.setCount(model.getQueryCount());
		fsq.addQueryParameter("age", dateBafore);
		return fsq;
	}

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		boolean caughtExeption = false;
		try
		{
			final ProcessTaskLogMaintenanceJobModel model = (ProcessTaskLogMaintenanceJobModel) cronJob.getJob();
			final FlexibleSearchQuery query = createFetchQuery(model);

			query.setResultClassList(Arrays.asList(PK.class));

			final SearchResult<PK> searchRes = flexibleSearchService.search(query);
			final List<PK> result = searchRes.getResult();
			final int numberOfItemsToRemove = result.size() >= model.getNumberOfLogs() ? result.size() - model.getNumberOfLogs() : 0;

			LOG.info("Number of logs to be removed: " + numberOfItemsToRemove);

			final List<PK> subList = searchRes.getResult().subList(0, numberOfItemsToRemove);

			for (final PK pk : subList)
			{
				modelService.remove(pk);
			}
		}
		catch (final Exception e)
		{
			caughtExeption = true;
			LOG.error("Exception caught:", e);
		}

		return new PerformResult(caughtExeption ? CronJobResult.FAILURE : CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
}
