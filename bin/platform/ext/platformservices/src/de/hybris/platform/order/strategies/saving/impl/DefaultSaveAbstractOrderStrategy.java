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
package de.hybris.platform.order.strategies.saving.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.strategies.saving.SaveAbstractOrderStrategy;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Implements the {@link SaveAbstractOrderStrategy} on the generic, {@link AbstractOrderModel} type level.
 */
public class DefaultSaveAbstractOrderStrategy extends AbstractBusinessService implements
		SaveAbstractOrderStrategy<AbstractOrderModel>
{

	private static final Logger LOG = Logger.getLogger(DefaultSaveAbstractOrderStrategy.class);

	private TransactionTemplate transactionTemplate;

	@Override
	public AbstractOrderModel saveOrder(final AbstractOrderModel order)
	{
		if (order == null)
		{
			throw new IllegalArgumentException("Order cannot be null");
		}
		transactionTemplate.execute(new SaveOrderTransactionCallback(order));

		return order;
	}

	@Required
	public void setTransactionTemplate(final TransactionTemplate transactionTemplate)
	{
		this.transactionTemplate = transactionTemplate;
	}

	private class SaveOrderTransactionCallback implements TransactionCallback
	{
		private final AbstractOrderModel order;

		public SaveOrderTransactionCallback(final AbstractOrderModel order)
		{
			super();
			this.order = order;
		}

		@Override
		public Object doInTransaction(final TransactionStatus status)
		{
			saveOrderEntries(order);
			getModelService().save(order);
			getModelService().refresh(order);
			return order;
		}

	}

	private void saveOrderEntries(final AbstractOrderModel order)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("saving entries for order [" + order + "]");
			}
			final ListIterator listIterator = entries.listIterator(entries.size());
			while (listIterator.hasPrevious())
			{
				final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) listIterator.previous();
				final Integer requested = entry.getEntryNumber();
				LOG.debug("saving entry " + entry.getEntryNumber() + " for order [" + order + "]");
				getModelService().save(entry);
				if (!requested.equals(entry.getEntryNumber()))
				{
					entry.setEntryNumber(requested);
					getModelService().save(entry);
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("saved with entry " + entry.getEntryNumber());
				}
			}

		}
	}

}
