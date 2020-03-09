/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.actions.quote;

import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;

import java.util.HashSet;
import java.util.Set;


/**
 * Extends AbstractAction to include Quote Specific Transition states.
 *
 * @param <T>
 */
public abstract class AbstractQuoteDecisionAction<T extends BusinessProcessModel> extends AbstractAction<T>
{
	public enum Transition
	{
		OK, NOK, ERROR;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();

			for (final Transition t : Transition.values())
			{
				res.add(t.toString());
			}
			return res;
		}
	}

	@Override
	public final String execute(final T process) throws RetryLaterException, Exception
	{
		return executeAction(process).toString();
	}


	/**
	 * Executes this <code>Action</code>'s business logic working on the given {@link BusinessProcessModel}.
	 *
	 * @param process
	 *           The process context to work on.
	 * @return Transition
	 * @throws RetryLaterException
	 * @throws Exception
	 */
	public abstract Transition executeAction(T process) throws RetryLaterException, Exception;

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}
