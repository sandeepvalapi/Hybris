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
package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.cronjob.jalo.CronJob.CronJobResult;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;

import java.util.HashMap;
import java.util.Map;


/**
 * A job for testing custom attributes list
 */
public class CustomAttributesTriggerableJob extends Job implements TriggerableJob
{
	public static final String ATTRIBUTE_CRON_JOB_THREE_ID = "attributeCronJobThree";
	public static final String ATTRIBUTE_CRON_JOB_TWO_ID = "attributeCronJobTwo";
	public static final String ATTRIBUTE_CRON_JOB_ONE_ID = "attributeCronJobOne";
	public static final String ATTRIBUTE_THREE_JOB_ID = "attributteThree";
	public static final String ATTRIBUTE_TWO_JOB_ID = "attributteTwo";
	public static final String ATTRIBUTE_ONE_JOB_ID = "attributteOne";
	public static String STATICCJOBCODE = "CustomAttributesTriggerableJob";
	public static String STATICCRONJOBCODE = "CustomAttributesTriggerableCronJob";

	@Override
	protected CronJobResult performCronJob(final CronJob cronJob) throws AbortCronJobException
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (final Exception e1)
		{
			return cronJob.getFinishedResult(false);
		}
		return cronJob.getFinishedResult(true);
	}

	@Override
	public CronJob newExecution()
	{
		final ComposedType type = (ComposedType) TypeManager.getInstance().getType(STATICCRONJOBCODE);
		try
		{
			final Map map = new HashMap();
			map.put(CronJob.JOB, this);
			//map.put(CronJob.CODE, code);
			//map.put(CronJob.ACTIVE, Boolean.valueOf(active));
			//map.put(CronJob.NODEID, Integer.valueOf(node));
			return (CronJob) type.newInstance(map);
		}
		catch (final JaloGenericCreationException e)
		{
			// YTODO Auto-generated catch block
			throw new JaloSystemException(e);
		}
		catch (final JaloAbstractTypeException e)
		{
			// YTODO Auto-generated catch block
			throw new JaloSystemException(e);
		}
	}




	/**
	 * custom mapping for
	 */
	@Override
    @ForceJALO(reason = ForceJALO.SOMETHING_ELSE)
	protected Map<String, String> getConfigAttributes(final CronJob cronjob)
	{
		final Map<String, String> jobParams = super.getConfigAttributes(cronjob);
		jobParams.put(ATTRIBUTE_ONE_JOB_ID, ATTRIBUTE_CRON_JOB_ONE_ID);
		jobParams.put(ATTRIBUTE_TWO_JOB_ID, ATTRIBUTE_CRON_JOB_TWO_ID);
		jobParams.put(ATTRIBUTE_THREE_JOB_ID, ATTRIBUTE_CRON_JOB_THREE_ID);
		return jobParams;
	}
}
