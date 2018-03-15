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
package de.hybris.platform.test.tx;

import de.hybris.platform.tx.AfterSaveListenerRegistry;
import de.hybris.platform.tx.DefaultTransaction;
import de.hybris.platform.tx.Transaction;


final class TestAfterSaveEventTransaction extends DefaultTransaction
{

	static TestAfterSaveEventTransaction install(final TestAfterSaveListenerRegistry registry)
	{
		Transaction.setTransactionFactory(new TestTxFactory(registry));
		return (TestAfterSaveEventTransaction) Transaction.current();
	}

	static void uninstall()
	{
		Transaction.unsetTransactionFactory();
		new CleanTx().activateAsCurrentTransaction();
	}

	private final AfterSaveListenerRegistry reg;

	private TestAfterSaveEventTransaction(final AfterSaveListenerRegistry reg)
	{
		this.reg = reg;
	}

	@Override
	protected AfterSaveListenerRegistry getAfterSaveEventListenerRegistry()
	{
		return reg;
	}

	private static class TestTxFactory implements TransactionFactory
	{
		private final AfterSaveListenerRegistry reg;

		TestTxFactory(final AfterSaveListenerRegistry reg)
		{
			this.reg = reg;
		}

		@Override
		public Transaction newCurrent()
		{
			return new TestAfterSaveEventTransaction(reg);
		}
	}

	private static class CleanTx extends DefaultTransaction
	{
		//
	}
}
