/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test.beans;

import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * 
 */
public class ActionExecution
{
	private final BusinessProcessModel process;
	private final AbstractAction action;

	public ActionExecution(final BusinessProcessModel process, final AbstractAction action)
	{
		this.process = process;
		this.action = action;
	}

	public AbstractAction getAction()
	{
		return action;
	}

	public BusinessProcessModel getProcess()
	{
		return process;
	}

}
