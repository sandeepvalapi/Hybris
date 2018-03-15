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
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;

import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.ImmutableMap;


/**
 * Executes the dynamic languages scripts
 */
public class ScriptingJobPerformable extends AbstractJobPerformable<CronJobModel>
{
	private ScriptingLanguagesService scriptingLanguagesService;

	private final static Logger LOG = Logger.getLogger(ScriptingJobPerformable.class.getName());

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final ScriptingJobModel dynamicScriptingJob = (ScriptingJobModel) cronJob.getJob();
		final ScriptExecutable executable = scriptingLanguagesService.getExecutableByURI(dynamicScriptingJob.getScriptURI());

		LOG.info("### Starting executing script : " + dynamicScriptingJob.getScriptURI() + " ###");
		final Map<String, Object> params = ImmutableMap.<String, Object> builder()//
				.put("cronjob", cronJob) //
				.put("log", LOG) //
				.build();
		final ScriptExecutionResult result = executable.execute(params);

		final boolean hasPerformResult = hasPerformResultInScript(result);
		if (handleErrors(result) && !hasPerformResult)
		{
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}
		LOG.info(((StringWriter) result.getOutputWriter()).getBuffer().toString());
		LOG.info("### Finished executing script, returned script result = " + result.getScriptResult() + " ###");
		return hasPerformResult ? (PerformResult) result.getScriptResult() : new PerformResult(CronJobResult.SUCCESS,
				CronJobStatus.FINISHED);
	}

	private boolean handleErrors(final ScriptExecutionResult result)
	{
		boolean errorsFound = false;
		if (result == null)
		{
			LOG.error("### Executing script failed, probably script contains errors ###");
			errorsFound = true;
		}
		else
		{
			final StringBuffer buf = ((StringWriter) result.getErrorWriter()).getBuffer();
			if (buf.length() > 0)
			{
				LOG.error(buf.toString());
				errorsFound = true;
			}
		}
		return errorsFound;
	}

	private boolean hasPerformResultInScript(final ScriptExecutionResult executionResult)
	{
		if (executionResult != null && executionResult.getScriptResult() instanceof PerformResult)
		{
			return true;
		}
		return false;
	}

	@Required
	public void setScriptingLanguagesService(final ScriptingLanguagesService scriptingLanguagesService)
	{
		this.scriptingLanguagesService = scriptingLanguagesService;
	}

}
