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
package de.hybris.platform.tx;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction.TransactionFactory;

import org.junit.Assert;
import org.junit.Test;


/**
 * PLA-13802
 */
@IntegrationTest
public class UnsetTransactionAfterErrorTest extends HybrisJUnit4Test
{

	@Test
	public void testTxUnsetAfterTenantMissingDuringCommit() throws ConsistencyCheckException
	{
		final TransactionFactory f = new TransactionFactory()
		{
			@Override
			public Transaction newCurrent()
			{
				return new TransactionWithErrorBeforeUnset();
			}
		};
		Transaction.setTransactionFactory(f);
		try
		{
			final Transaction tx = Transaction.current();
			tx.begin();

			final Tenant t = Registry.getCurrentTenantNoFallback();
			try
			{
				// dummy write
				UserManager.getInstance().createTitle("Foo" + System.nanoTime());
				// now simulate error
				Registry.unsetCurrentTenant();
				Exception ex = null;
				try
				{
					tx.commit();
				}
				catch (final Exception e)
				{
					ex = e;
				}
				if (ex != null)
				{
					System.out.println("tx " + tx.getObjectID() + " got expected exception " + ex);

					Assert.assertNotSame(tx, Transaction.current());
					Assert.assertFalse("Transaction must not be running after commit() error", tx.isRunning());
				}
			}
			finally
			{
				Registry.setCurrentTenant(t);
			}
		}
		finally
		{
			Transaction.unsetTransactionFactory();
		}
	}

	@Test
	public void testTxUnsetAfterTenantMissingDuringRollback() throws ConsistencyCheckException
	{
		final TransactionFactory f = new TransactionFactory()
		{
			@Override
			public Transaction newCurrent()
			{
				return new TransactionWithErrorBeforeUnset();
			}
		};
		Transaction.setTransactionFactory(f);
		try
		{
			final Transaction tx = Transaction.current();

			tx.begin();

			final Tenant t = Registry.getCurrentTenantNoFallback();
			try
			{
				// dummy write
				UserManager.getInstance().createTitle("Foo" + System.nanoTime());
				// now simulate error
				Registry.unsetCurrentTenant();
				Exception ex = null;
				try
				{
					tx.rollback();
				}
				catch (final Exception e)
				{
					ex = e;
				}
				if (ex != null)
				{
					System.out.println("tx " + tx.getObjectID() + " got expected exception " + ex);

					Assert.assertNotSame(tx, Transaction.current());
					Assert.assertFalse("Transaction must not be running after commit() error", tx.isRunning());
				}
			}
			finally
			{
				Registry.setCurrentTenant(t);
			}
		}
		finally
		{
			Transaction.unsetTransactionFactory();
		}
	}

	static class TransactionWithErrorBeforeUnset extends DefaultTransaction
	{
		public TransactionWithErrorBeforeUnset()
		{
			super();
			System.err.println("created TransactionWithErrorBeforeUnset tx with id " + getObjectID());
		}

		@Override
		protected AfterSaveListenerRegistry getAfterSaveEventListenerRegistry()
		{
			throw new RuntimeException("Boom from " + getObjectID());
		}
	}
}
