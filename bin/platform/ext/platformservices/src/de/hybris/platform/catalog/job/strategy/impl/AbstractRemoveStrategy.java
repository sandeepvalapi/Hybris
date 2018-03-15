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
package de.hybris.platform.catalog.job.strategy.impl;

import de.hybris.platform.catalog.job.callback.RemoveCallback;
import de.hybris.platform.catalog.job.util.CatalogVersionJobDao;
import de.hybris.platform.catalog.job.util.ImpexScriptGenerator;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVConstants;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.CharUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Common logic for removal strategies
 */
abstract public class AbstractRemoveStrategy
{
	private static final Logger LOG = Logger.getLogger(AbstractRemoveStrategy.class.getName());

	private static final int POLL_PERIOD = 1000;//1 s poll

	protected ModelService modelService;

	private ImpexScriptGenerator removeScriptGenerator;

	private ImportService importService;

	private RemoveCallback<Collection<CatalogVersionModel>> removeCallback;

	protected CatalogVersionJobDao catalogVersionDao;

	@Required
	public void setRemoveScriptGenerator(final ImpexScriptGenerator removeScriptConverter)
	{
		this.removeScriptGenerator = removeScriptConverter;
	}

	@Required
	public void setCatalogVersionJobDao(final CatalogVersionJobDao counter)
	{
		this.catalogVersionDao = counter;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}

	@Required
	public void setRemoveCallback(final RemoveCallback<Collection<CatalogVersionModel>> removeCallback)
	{
		this.removeCallback = removeCallback;
	}

	/**
	 * Starts catalog version removal , returns true if wait for started import job needed
	 */
	protected void removeCatalogVersionCollection(final Collection<CatalogVersionModel> catalogVersions,
			final RemoveCatalogVersionCronJobModel cronJob, final List<ComposedTypeModel> orderedComposedTypes)
	{

		ImportResult result = null;
		StringBuilder buffer = null;
		try
		{
			removeCallback.beforeRemove(cronJob, catalogVersions);

			for (final CatalogVersionModel catalogVersionModel : catalogVersions)
			{
				final int countBefore = catalogVersionDao.getItemInstanceCount(catalogVersionModel, orderedComposedTypes);
				if (countBefore > 0)
				{
					if (buffer == null)
					{
						buffer = new StringBuilder(1000);
					}
					buffer.append(removeScriptGenerator.generate(catalogVersionModel, orderedComposedTypes));

					if (LOG.isDebugEnabled())
					{
						LOG.debug("Generated script [" + CharUtils.LF + buffer.toString() + CharUtils.LF + "] for removing  "
								+ countBefore + " items from catalogversion " + catalogVersionModel);
					}
				}
			}
			if (buffer != null)
			{
				LOG.info("Starting impex based removing of the catalogversions " + catalogVersions);

				try
				{
					final ImportConfig config = getImpexConfig(buffer);

					result = importService.importData(config);
					//this code is because result seemed to be isRuninng  = false

					//Thread.sleep(POLL_PERIOD);//sleep

					LOG.info(" Import is running " + result.isRunning() + " finished " + result.isFinished() + " status "
							+ result.getCronJob().getStatus() + " cronjob " + result.getCronJob().getCode());
					while (result.isRunning() && result.getCronJob() != null)
					{
						//this is only for presenting the progress to outside
						removeCallback.doRemove(cronJob, catalogVersions, result);
						Thread.sleep(POLL_PERIOD);//sleep
						if (result.getCronJob() != null)
						{
							modelService.refresh(result.getCronJob());
						}
					}
				}
				catch (final InterruptedException e)
				{
					LOG.error("Current thread " + Thread.currentThread() + " was interrupted with message " + e.getMessage()
							+ ", please set log level to debug for more details");
					if (LOG.isDebugEnabled())
					{
						LOG.error(e.getMessage(), e);
					}
					Thread.currentThread().interrupt();
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage(), e);
				}
			}
		}
		finally
		{

			removeCallback.afterRemoved(cronJob, catalogVersions, result);
			if (result != null)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("After job finished  with result, unresolved lines " + result.hasUnresolvedLines() + ", is finished :"
							+ result.isFinished() + ", is running :" + result.isRunning());
				}
			}
		}
	}

	private ImportConfig getImpexConfig(final StringBuilder stream)
	{
		final ImpExResource mediaRes = createImpexResource(stream);
		final ImportConfig config = createImpexConfig();
		config.setRemoveOnSuccess(false);
		config.setScript(mediaRes);
		config.setSynchronous(true);//PLA-10759
		return config;
	}

	/**
	 * creates an {@link ImpExResource} upon a given {@link StringBuilder}.
	 */
	protected ImpExResource createImpexResource(final StringBuilder stream)
	{
		return new StreamBasedImpExResource(new ByteArrayInputStream(stream.toString().getBytes()), CSVConstants.HYBRIS_ENCODING);
	}


	protected ImportConfig createImpexConfig()
	{
		return new ImportConfig();
	}



}
