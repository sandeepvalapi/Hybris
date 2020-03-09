/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.consignment;


import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


public class ReceiveConsignmentStatusAction extends AbstractAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(ReceiveConsignmentStatusAction.class);

	public enum Transition
	{
		OK, CANCEL, ERROR;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();

			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(final ConsignmentProcessModel process)
	{
		Transition result = null;
		if (process.getWarehouseConsignmentState() == null)
		{
			LOG.error("Process has no warehouse consignment state");
			result = Transition.ERROR;
		}
		else
		{
			switch (process.getWarehouseConsignmentState())
			{
				case CANCEL:
					result = Transition.CANCEL;
					break;
				case COMPLETE:
					result = Transition.OK;
					break;
				default:
					LOG.error("Unexpected warehouse consignment state: " + process.getWarehouseConsignmentState());
					result = Transition.ERROR;
			}
		}
		process.setWaitingForConsignment(false);
		getModelService().save(process);
		return result.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}
