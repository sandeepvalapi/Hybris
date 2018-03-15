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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.HybrisTransactionManager;
import de.hybris.platform.tx.DefaultTransaction;
import de.hybris.platform.tx.Transaction;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Tests correct translation of Jalo transaction errors into Spring counterparts (defined by
 * {@link PlatformTransactionManager}).
 * 
 * See PLA-11102 for reported bug.
 */
@IntegrationTest
public class HybrisTransactionManagerExceptionsTest extends ServicelayerBaseTest
{
	@Resource
	private UserService userService;

	@Resource
	private ModelService modelService;

	@Resource(name = "txManager")
	private HybrisTransactionManager hybrisTransactionManager;

	private TransactionTemplate template;

	@Before
	public void setUp()
	{
		template = new TransactionTemplate(hybrisTransactionManager);
	}

	@After
	public void tearDown()
	{
		while (Transaction.current().isRunning())
		{
			Transaction.current().rollback();
		}
	}

	@Test
	public void testCommitNoTxRunningExceptions()
	{
		//		 * @throws UnexpectedRollbackException in case of an unexpected rollback
		//		 * that the transaction coordinator initiated

		//		 * @throws HeuristicCompletionException in case of a transaction failure
		//		 * caused by a heuristic decision on the side of the transaction coordinator

		//		 * @throws TransactionSystemException in case of commit or system errors
		//		 * (typically caused by fundamental resource failures)

		//		 * @throws IllegalTransactionStateException if the given transaction
		//		 * is already completed (that is, committed or rolled back)

		final TestTransactionManager manager = new TestTransactionManager();

		// COMMIT -> no tx running

		final Transaction tx = (Transaction) manager.doGetTransaction();
		final DefaultTransactionStatus status = new DefaultTransactionStatus(tx, true, true, false, false, null);
		try
		{
			manager.doCommit(status);
		}
		catch (final IllegalTransactionStateException springEx)
		{
			// fine since this signals no tx running or tx already committed
		}
		catch (final TransactionException springEc)
		{
			// not actually correct, but bearable
			System.err
					.println("HybrisTransactionManager.doCommit() with not tx running: should throw IllegalTransactionStateException instead of TransactionException, should be improved");
		}
		catch (final Exception e)
		{
			fail("HybrisTransactionManager.doCommit() with not tx running: illegal exception " + e);
		}
	}

	@Test
	public void testCommitRollbackOnlyExceptions()
	{
		//		 * @throws UnexpectedRollbackException in case of an unexpected rollback
		//		 * that the transaction coordinator initiated

		//		 * @throws HeuristicCompletionException in case of a transaction failure
		//		 * caused by a heuristic decision on the side of the transaction coordinator

		//		 * @throws TransactionSystemException in case of commit or system errors
		//		 * (typically caused by fundamental resource failures)

		//		 * @throws IllegalTransactionStateException if the given transaction
		//		 * is already completed (that is, committed or rolled back)

		final TestTransactionManager manager = new TestTransactionManager();

		// COMMIT > rollback-only

		final Transaction tx = (Transaction) manager.doGetTransaction();
		final DefaultTransactionStatus status = new DefaultTransactionStatus(tx, true, true, false, false, null);
		manager.doBegin(tx, null);
		tx.setRollbackOnly();
		try
		{
			manager.doCommit(status);
		}
		catch (final UnexpectedRollbackException springEx)
		{
			// fine since this signals rollback due to underlying (not Spring) layer requested rollback
		}
		catch (final TransactionException e)
		{
			// not correct, but bearable
			System.err
					.println("HybrisTransactionManager.doCommit() + Jalo rollback-only: should throw UnexpectedRollbackException instead of TransactionException -> improve");
		}
		catch (final Exception e)
		{
			fail("HybrisTransactionManager.doCommit() + Jalo rollback-only: illegal exception " + e);
		}

		assertFalse(manager.isExistingTransaction(tx));
		assertFalse(Transaction.current().isRunning());

	}

	@Test
	public void testRollbackNoTxRunningExceptions()
	{
		//		 * @throws TransactionSystemException in case of rollback or system errors
		//		 * (typically caused by fundamental resource failures)

		//		 * @throws IllegalTransactionStateException if the given transaction
		//		 * is already completed (that is, committed or rolled back)

		final TestTransactionManager manager = new TestTransactionManager();

		// ROLLBACK -> no tx running

		final Transaction tx = (Transaction) manager.doGetTransaction();
		final DefaultTransactionStatus status = new DefaultTransactionStatus(tx, true, true, false, false, null);
		try
		{
			manager.doRollback(status);
		}
		catch (final IllegalTransactionStateException springEx)
		{
			// fine since this signals no tx running or tx already committed
		}
		catch (final TransactionException springEc)
		{
			// not actually correct, but bearable
			System.err
					.println("HybrisTransactionManager.doRollback() with not tx running: should throw IllegalTransactionStateException instead of TransactionException, should be improved");
		}
		catch (final Exception e)
		{
			fail("HybrisTransactionManager.doRollback() with not tx running: illegal exception " + e);
		}
	}

	@Test
	public void testRollbackSystemErrorExceptions()
	{
		//		 * @throws TransactionSystemException in case of rollback or system errors
		//		 * (typically caused by fundamental resource failures)

		//		 * @throws IllegalTransactionStateException if the given transaction
		//		 * is already completed (that is, committed or rolled back)

		final TestTransactionManager manager = new TestTransactionManager();


		// transaction instance that will throw a error during rollback 
		final Transaction specialTx = new DefaultTransaction()
		{
			@Override
			protected void rollbackConnection() throws SQLException
			{
				super.rollbackConnection();
				throw new RuntimeException("Foo");
			}
		};
		specialTx.activateAsCurrentTransaction();
		assertSame(specialTx, manager.doGetTransaction());
		final DefaultTransactionStatus status = new DefaultTransactionStatus(specialTx, true, true, false, false, null);
		manager.doBegin(specialTx, null);
		try
		{
			manager.doRollback(status);
		}
		catch (final TransactionSystemException e)
		{
			// fine since it's a system exception
		}
		catch (final TransactionException e)
		{
			// not correct, but bearable
			System.err
					.println("HybrisTransactionManager.doCommit() + Jalo rollback-only: should throw UnexpectedRollbackException instead of TransactionException -> improve");
		}
		catch (final Exception e)
		{
			fail("HybrisTransactionManager.doCommit() + Jalo rollback-only: illegal exception " + e);
		}

		assertFalse(manager.isExistingTransaction(specialTx));
		assertFalse(Transaction.current().isRunning());
	}


	@Test
	public void testTransactionTemplateRollbackOnly()
	{
		assertTxNotRunning();
		assertTitleNotExists("foo");

		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				assertTxRunning(1);
				createTitle("foo");
				assertTitleExists("foo");

				status.setRollbackOnly();
			}
		});

		assertTitleNotExists("foo");

	}

	// This is actually triggering the problem since Jalo Transaction.commit()
	// throws a exception that is not recognized by Spring tx handling and therefore
	// Transaction.rollback() is called which is illegal in that case.
	@Test
	public void testTransactionTemplateRollbackOnlyViaJalo()
	{
		assertTxNotRunning();
		assertTitleNotExists("foo");

		try
		{
			template.execute(new TransactionCallbackWithoutResult()
			{
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus status)
				{
					assertTxRunning(1);
					createTitle("foo");
					assertTitleExists("foo");

					Transaction.current().setRollbackOnly(); // this makes commit() throw a exception
				}
			});
		}
		catch (final TransactionException springTxException)
		{
			// would be fine
		}
		catch (final de.hybris.platform.tx.TransactionException jaloTxException)
		{
			assertTrue("wrong jalo tx exception " + jaloTxException, jaloTxException.getMessage().contains("rollback-only"));
		}

		assertTitleNotExists("foo");
	}

	private void createTitle(final String code)
	{
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode(code);
		modelService.save(title);
	}

	private void assertTitleExists(final String code)
	{
		try
		{
			assertEquals(code, userService.getTitleForCode(code).getCode());
		}
		catch (final UnknownIdentifierException e)
		{
			fail("title '" + code + "' did not exist");
		}
	}

	private void assertTitleNotExists(final String code)
	{
		try
		{
			userService.getTitleForCode(code);
			fail("title '" + code + "' unexpected exists");
		}
		catch (final UnknownIdentifierException e)
		{
			// fine
		}
	}

	private void assertTxRunning(final int nestedLevel)
	{
		final Transaction tx = Transaction.current();

		assertTrue(tx.isRunning());
		assertEquals(nestedLevel, tx.getOpenTransactionCount());

	}

	private void assertTxNotRunning()
	{
		final Transaction tx = Transaction.current();

		assertEquals(0, tx.getOpenTransactionCount());
		assertFalse(tx.isRunning());
	}

	private static class TestTransactionManager extends HybrisTransactionManager
	{
		@Override
		protected boolean isExistingTransaction(final Object transaction) throws TransactionException
		{
			return super.isExistingTransaction(transaction);
		}

		@Override
		protected Object doGetTransaction() throws TransactionException
		{
			return super.doGetTransaction();
		}

		@Override
		protected void doBegin(final Object transaction, final TransactionDefinition definition) throws TransactionException
		{
			super.doBegin(transaction, definition);
		}

		@Override
		protected void doCommit(final DefaultTransactionStatus status) throws TransactionException
		{
			super.doCommit(status);
		}

		@Override
		protected void doRollback(final DefaultTransactionStatus status) throws TransactionException
		{
			super.doRollback(status);
		}
	}

}
