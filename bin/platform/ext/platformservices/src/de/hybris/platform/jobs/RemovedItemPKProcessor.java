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

import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.model.RemoveItemsCronJobModel;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Iterator like processor for traversing through PK's from {@link RemoveItemsCronJobModel#getItemPKs()} data stream.
 * 
 * @since 4.3
 */
public class RemovedItemPKProcessor implements DisposableRemovedItemPKProcessor<RemoveItemsCronJobModel>
{

	private static final Logger LOG = Logger.getLogger(RemoveItemsJobPerformable.class.getName());

	private BufferedReader bufferedReader;

	private int counter = 1;

	private int toSkip;

	private MediaService mediaService;

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	private void skipProcessedEntries()
	{
		while (counter <= toSkip)
		{
			if (hasNext())
			{
				next();
			}
			else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Underlying has no more than " + counter + " entries and starting index seem to exceed this");
				}
				break;
			}
		}
	}

	@Override
	public boolean hasNext()
	{
		try
		{
			return bufferedReader.ready();
		}
		catch (final IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public PK next()
	{
		String line;
		try
		{
			line = bufferedReader.readLine();
		}
		catch (final IOException e)
		{
			throw new IllegalStateException(e);
		}
		counter++;
		return de.hybris.platform.core.PK.parse(line);

	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public void init(final RemoveItemsCronJobModel cronJob)
	{
		final InputStream inputStream = mediaService.getStreamFromMedia(cronJob.getItemPKs());

		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		final int deleted = cronJob.getItemsDeleted() == null ? 0 : cronJob.getItemsDeleted().intValue();
		final int refused = cronJob.getItemsRefused() == null ? 0 : cronJob.getItemsRefused().intValue();

		toSkip = deleted + refused;

		skipProcessedEntries();
	}


	@Override
	public void dispose()
	{
		if (bufferedReader != null)
		{
			try
			{
				bufferedReader.close();
			}
			catch (final IOException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
			}
		}
	}


}
