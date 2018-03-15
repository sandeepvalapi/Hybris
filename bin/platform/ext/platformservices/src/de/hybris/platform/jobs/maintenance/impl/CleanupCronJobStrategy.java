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

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of the {@link MaintenanceCleanupStrategy} for cleaning up old CronJobModels. This impl searches for
 * CronJobModels of a certain age, status and result (default: 14 days old, successfull and finish).
 */
public class CleanupCronJobStrategy implements MaintenanceCleanupStrategy<CronJobModel, CronJobModel>
{
	private final static Logger LOG = Logger.getLogger(CleanupCronJobStrategy.class.getName());

	//static bean properties
	private ModelService modelService;
	private TypeService typeService;
	private Set<CronJobResult> result;
	private Set<CronJobStatus> status;
	private Set<String> excludedCronJobCodes;

	//dynamic job properties
	private int daysOld = 14;
	private String cronJobType = CronJobModel._TYPECODE;


	@Override
	public FlexibleSearchQuery createFetchQuery(final CronJobModel cjm)
	{
		checkJobParameters(cjm);

		final Map<String, Object> params = new HashMap<String, Object>();
		final StringBuilder builder = new StringBuilder();

		builder.append("SELECT {" + CronJobModel.PK + "} FROM {" + this.cronJobType + " AS c} ");
		builder.append("WHERE {c." + CronJobModel.PK + "} NOT IN (");
		builder.append(" {{SELECT {" + TriggerModel.CRONJOB + "} FROM {" + TriggerModel._TYPECODE + "} ");
		builder.append("WHERE {" + TriggerModel.CRONJOB + "} IS NOT NULL}}");
		builder.append(") ");

		if (!excludedCronJobCodes.isEmpty())
		{
			builder.append("AND {" + CronJobModel.CODE + "} NOT IN ( ?excludedCronJobCodes ) ");
			params.put("excludedCronJobCodes", excludedCronJobCodes);
		}

		builder.append("AND {" + CronJobModel.STATUS + "} IN ( ?status ) ");
		builder.append("AND {" + CronJobModel.RESULT + "} IN ( ?result ) ");
		builder.append("AND {" + CronJobModel.ENDTIME + "} < ?date");

		params.put("date", new Date(System.currentTimeMillis() - 1000L * 3600L * 24 * daysOld));
		params.put("result", this.result);
		params.put("status", this.status);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), params);
		query.setResultClassList(Arrays.asList(CronJobModel.class));
		return query;
	}

	@Override
	public void process(final List<CronJobModel> elements)
	{
		LOG.info("Removing " + elements.size() + " items from type " + this.cronJobType);
		modelService.removeAll(elements);
	}

	private void checkJobParameters(final CronJobModel cjm)
	{
		if (cjm.getJob() instanceof MaintenanceCleanupJobModel)
		{
			final MaintenanceCleanupJobModel job = (MaintenanceCleanupJobModel) cjm.getJob();
			if (job.getThreshold() != null)
			{
				LOG.info("MaintenanceCleanupJobModel contains a threeshold value, using " // 
						+ job.getThreshold() + " instead of " + daysOld);
				setDaysOld(job.getThreshold());
			}
			if (job.getSearchType() != null)
			{
				LOG.info("MaintenanceCleanupJobModel contains a composedtype value, using: " + job.getSearchType().getCode());
				setCronJobType(job.getSearchType().getCode());
			}
		}
	}

	private void setCronJobType(final String cronjobtype)
	{
		if (typeService.isAssignableFrom(this.cronJobType, cronjobtype))
		{
			//cronjobtype is not null and is a subtype, doesn't matter if it is the same.
			this.cronJobType = cronjobtype;
		}
		else
		{
			throw new IllegalArgumentException(cronjobtype + " must be a subtype of " + CronJobModel._TYPECODE);
		}
	}

	private void setDaysOld(final Integer daysold)
	{
		if (daysold.intValue() < 0)
		{
			throw new IllegalArgumentException("Cannot set negative value for daysold");
		}
		this.daysOld = daysold.intValue();
	}

	@Required
	public void setExcludedCronJobCodes(final Set<String> excludedCronjobCodes)
	{
		this.excludedCronJobCodes = excludedCronjobCodes;
	}

	@Required
	public void setResult(final Set<CronJobResult> result)
	{
		if (result == null || result.isEmpty())
		{
			throw new IllegalArgumentException("The CronJob result set must contains at least one value!");
		}
		this.result = result;
	}

	@Required
	public void setStatus(final Set<CronJobStatus> status)
	{
		if (status == null || status.isEmpty())
		{
			throw new IllegalArgumentException("The CronJob status set must contains at least one value!");
		}
		this.status = status;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

}
