/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.returns;

import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;


/**
 * Check whether the return request is an instore or an online request and redirects it to the appropriate step.
 */
public class InitialReturnAction extends AbstractAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(InitialReturnAction.class);

	protected enum Transition
	{
		ONLINE, INSTORE;

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
	public String execute(final ReturnProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();

		final String transition = returnRequest.getReturnEntries().stream().allMatch(entry -> entry.getAction().equals(
				ReturnAction.IMMEDIATE)) ? Transition.INSTORE.toString() : Transition.ONLINE.toString();

		LOG.debug("Process: " + process.getCode() + " transitions to " + transition);

		return transition;
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

}
