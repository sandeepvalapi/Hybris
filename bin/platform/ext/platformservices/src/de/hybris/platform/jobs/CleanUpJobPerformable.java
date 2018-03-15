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
package de.hybris.platform.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CleanUpCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Removes successfully finished cronjobs of a certain age (default behaviour). All parameters can be changed, see
 * {@link CleanUpCronJobModel}.
 * 
 * @deprecated since ages - please use the{@link MaintenanceCleanupStrategy} / {@link GenericMaintenanceJobPerformable}
 */
@Deprecated
public class CleanUpJobPerformable extends AbstractJobPerformable<CleanUpCronJobModel>
{
	private final static Logger LOG = Logger.getLogger(CleanUpJobPerformable.class.getName());

	private I18NService i18nService;

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	@Override
	public PerformResult perform(final CleanUpCronJobModel cuCronJob)
	{
		if (cuCronJob == null)
		{
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		final List<CronJobModel> cronJobs = searchCronJobs(cuCronJob);
		LOG.info("Found " + cronJobs.size() + " CronJobs which are older than " + (cuCronJob.getXDaysOld() + " days."));

		final boolean success = deleteCronJobs(cronJobs);
		LOG.info("Finished deleting CronJobs");

		return new PerformResult(success ? CronJobResult.SUCCESS : CronJobResult.ERROR, CronJobStatus.FINISHED);
	}

	private List<CronJobModel> searchCronJobs(final CleanUpCronJobModel cuCronJob)
	{
		final Map<String, Object> params = new HashMap<String, Object>();

		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {").append(CronJobModel.PK).append("} ");
		builder.append("FROM {").append(CronJobModel._TYPECODE).append(" AS c} ");
		builder.append("WHERE {c.").append(CronJobModel.PK + "} NOT IN ({{");
		builder.append("  SELECT {").append(TriggerModel.CRONJOB).append("} ");
		builder.append("  FROM {").append(TriggerModel._TYPECODE).append("} ");
		builder.append("  WHERE {").append(TriggerModel.CRONJOB).append("} IS NOT NULL");
		builder.append("}}) ");

		if (!cuCronJob.getExcludeCronJobs().isEmpty())
		{
			builder.append("AND {c.").append(CronJobModel.PK).append("} NOT IN ( ?excludedCronJobs ) ");
			params.put("excludedCronJobs", cuCronJob.getExcludeCronJobs());
		}
		builder.append("AND {").append(CronJobModel.STATUS).append("} IN ( ?status ) ");
		builder.append("AND {").append(CronJobModel.RESULT).append("} IN ( ?result ) ");
		builder.append("AND {").append(CronJobModel.ENDTIME).append("} < ?date ");

		final Calendar cal = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		cal.add(Calendar.DATE, -1 * cuCronJob.getXDaysOld());
		params.put("date", cal.getTime());

		params.put("result", cuCronJob.getResultcoll());
		params.put("status", cuCronJob.getStatuscoll());

		final SearchResult<CronJobModel> searchres = flexibleSearchService.search(builder.toString(), params);
		return searchres.getResult();
	}

	private boolean deleteCronJobs(final List<CronJobModel> cronJobs)
	{
		boolean success = true;
		for (final CronJobModel cjm : cronJobs)
		{
			final String cjcode = cjm.getCode();
			try
			{
				modelService.remove(cjm);
				LOG.debug("Deleted cronjob \"" + cjcode + "\"");
			}
			catch (final ModelRemovalException e)
			{
				success = false;
				LOG.error("Could not delete CronJob \"" + cjcode + "\"", e);
			}
		}
		return success;
	}


}
