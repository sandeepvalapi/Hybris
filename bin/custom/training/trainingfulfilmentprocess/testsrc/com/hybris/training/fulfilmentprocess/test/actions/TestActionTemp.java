/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test.actions;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


public class TestActionTemp<T extends BusinessProcessModel> extends AbstractAction<T>
{
	private static final Logger LOG = Logger.getLogger(TestActionTemp.class);

	private String result = "OK";
	private boolean throwException = false;

	public String getResult()
	{
		return result;
	}

	public void setResult(final String result)
	{
		this.result = result;
	}

	public void setThrowException(final boolean throwException)
	{
		this.throwException = throwException;
	}

	@Override
	public String execute(final T process) throws Exception
	{
		// This call actually puts -this- into a queue.
		if (throwException)
		{
			throw new IllegalStateException("Error");
		}

		LOG.info(result);
		return result;
	}

	@Override
	public Set<String> getTransitions()
	{
		final Set<String> res = new HashSet<String>();
		res.add(result);
		return res;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
	}
}
