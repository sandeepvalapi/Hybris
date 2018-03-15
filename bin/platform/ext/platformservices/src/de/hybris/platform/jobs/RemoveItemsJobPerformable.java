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
import de.hybris.platform.core.PK.PKException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.RemoveItemsCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @since 4.3
 * 
 * @spring.bean removeItemsJob
 */
public class RemoveItemsJobPerformable extends AbstractJobPerformable<RemoveItemsCronJobModel>
{

	private static final Logger LOG = Logger.getLogger(RemoveItemsJobPerformable.class.getName());

	// update the status fields every 10 seconds
	private static final long STATUS_UPDATE_INTERVAL = 10L * 1000L;

	private static final String IS_HMC_SESSION = "is.hmc.session";

	private PermissionCheckingService permissionCheckingService;

	private MediaService mediaService;

	@Required
	public void setPermissionCheckingService(final PermissionCheckingService permissionCheckingService)
	{
		this.permissionCheckingService = permissionCheckingService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	@Override
	public PerformResult perform(final RemoveItemsCronJobModel cronJob)
	{
		final DisposableRemovedItemPKProcessor disposableIterator = new RemovedItemPKProcessor();
		((RemovedItemPKProcessor) disposableIterator).setMediaService(mediaService);
		int deleted = cronJob.getItemsDeleted() == null ? 0 : cronJob.getItemsDeleted().intValue();
		int refused = cronJob.getItemsRefused() == null ? 0 : cronJob.getItemsRefused().intValue();

		boolean error = false;
		long timestamp = System.currentTimeMillis();

		try
		{
			if (cronJob.getItemPKs() == null)
			{
				throw new IllegalStateException("ItemPKs media is null ");
			}
			if (cronJob.getCreateSavedValues() != null && !cronJob.getCreateSavedValues().booleanValue())
			{
				//TODO remove it since we deprecate HMC
				sessionService.removeAttribute(IS_HMC_SESSION);
			}
			disposableIterator.init(cronJob);

			for (final Iterator<PK> iter = disposableIterator; iter.hasNext();)
			{
				if (tryToDeleteItem(iter.next()))
				{
					deleted++;
				}
				else
				{
					refused++;
				}
				// change status fields every update interval
				if (isUpdateProgressNeeded(timestamp))
				{
					cronJob.setItemsDeleted(Integer.valueOf(deleted));
					cronJob.setItemsRefused(Integer.valueOf(refused));
					modelService.save(cronJob);
					timestamp = System.currentTimeMillis();
				}
			}
		}
		catch (final PKException pke)
		{
			LOG.error("Incorrect PK in the stream  : " + pke.getMessage() + ", to get detail information set debug log level");
			if (LOG.isDebugEnabled())
			{
				LOG.debug(pke.getMessage(), pke);
			}
			error = true;
		}
		catch (final Exception e)
		{
			LOG.error("Other error : " + e.getMessage() + ", to get detail information set debug log level");
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			error = true;
		}
		finally
		{
			cronJob.setItemsDeleted(Integer.valueOf(deleted));
			cronJob.setItemsRefused(Integer.valueOf(refused));
			cronJob.setItemsFound(Integer.valueOf(refused + deleted));
			modelService.save(cronJob);
			disposableIterator.dispose();
		}
		return new PerformResult((!error && refused == 0) ? CronJobResult.SUCCESS : CronJobResult.FAILURE, CronJobStatus.FINISHED);
	}

	/**
	 * Returs true if update progress in needed
	 */
	protected boolean isUpdateProgressNeeded(final long timestamp)
	{
		return (System.currentTimeMillis() - timestamp) > STATUS_UPDATE_INTERVAL;
	}

	/**
	 *
	 */
	private boolean tryToDeleteItem(final PK parsedPK)
	{
		try
		{
			PermissionCheckResult permission = null;
			final ItemModel model = modelService.get(parsedPK);
			if (model != null)
			{
				permission = permissionCheckingService.checkItemPermission(model, PermissionsConstants.REMOVE);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Permission result is a " + permission + " for the " + model);
				}
				if (permission != null && PermissionCheckValue.ALLOWED.equals(permission.getCheckValue()))
				{
					modelService.remove(parsedPK);
				}
				else
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Permission result is a " + permission + " for the " + model);
					}
					return false;
				}
			}
			else
			{
				LOG.warn("Failed to remove item "
						+ parsedPK
						+ " , for user \""
						+ ((sessionService.getAttribute("user") != null) ? ((UserModel) sessionService.getAttribute("user")).getUid()
								: "unknown") + "\" has no right to delete item or right has been denied (" + permission + ")");
				return false;
			}
		}
		catch (final ModelLoadingException mle)
		{
			LOG.warn("Failed to load item model " + parsedPK + ", with message :" + mle.getMessage()
					+ " for more details set log level as debug");
			if (LOG.isDebugEnabled())
			{
				LOG.debug(mle.getMessage(), mle);
			}
			return false;
		}
		catch (final ModelRemovalException mre)
		{

			LOG.warn("Failed to remove item model " + parsedPK + ", with message :" + mre.getMessage()
					+ " for more details set log level as debug");
			if (LOG.isDebugEnabled())
			{
				LOG.debug(mre.getMessage(), mre);
			}
			return false;
		}
		return true;
	}


}
