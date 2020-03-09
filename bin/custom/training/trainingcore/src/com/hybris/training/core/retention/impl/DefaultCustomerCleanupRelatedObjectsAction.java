/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.retention.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.enums.RetentionState;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.impl.AbstractExtensibleRemoveCleanupAction;
import de.hybris.platform.retention.job.AfterRetentionCleanupJobPerformable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Cleanup the customer related objects and set customer retention state to PROCESSED
 *
 */
public class DefaultCustomerCleanupRelatedObjectsAction extends AbstractExtensibleRemoveCleanupAction
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerCleanupRelatedObjectsAction.class);

	@Override
	public void cleanup(final AfterRetentionCleanupJobPerformable retentionJob, final AbstractRetentionRuleModel rule,
			final ItemToCleanup item)
	{
		validateParameterNotNullStandardMessage("item to cleanup", item);

		final Object itemModel = getModelService().get(item.getPk());
		if (!(itemModel instanceof CustomerModel))
		{
			throw new IllegalStateException("Not instance of CustomerModel:" + item.getPk());
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cleaning up customer and its audit records: {}", item.getPk());
		}

		final CustomerModel customerModel = (CustomerModel) itemModel;
		cleanupRelatedObjects(customerModel);

		//set customer retention state, so not be processed again
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Set customer:{} retention state to processed", customerModel);
		}
		customerModel.setRetentionState(RetentionState.PROCESSED);
		getModelService().save(customerModel);
	}
}
