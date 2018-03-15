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
package de.hybris.platform.servicelayer.tx;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;


/**
 *
 */
public class MockTransactionManager extends AbstractPlatformTransactionManager
{
	@Override
	protected void doBegin(final Object transaction, final TransactionDefinition definition) throws TransactionException
	{
		//empty
	}

	@Override
	protected void doCommit(final DefaultTransactionStatus status) throws TransactionException
	{
		//empty
	}

	@Override
	protected Object doGetTransaction() throws TransactionException
	{
		return new Object();
	}

	@Override
	protected void doRollback(final DefaultTransactionStatus status) throws TransactionException
	{
		//empty
	}

}
