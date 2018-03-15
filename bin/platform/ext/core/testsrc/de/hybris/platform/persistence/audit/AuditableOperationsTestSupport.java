/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.persistence.audit;

import de.hybris.platform.tx.BeforeCommitNotification;
import de.hybris.platform.tx.BeforeRollbackNotification;
import de.hybris.platform.tx.Transaction;

import java.util.function.Supplier;

import org.mockito.Mockito;


public final class AuditableOperationsTestSupport
{
	private static final ThreadLocal<AuditableOperationHandler> handlerToUse = new ThreadLocal<>();
	private static final ThreadLocal<Supplier<AuditableOperationHandler>> transactionalHandler = new ThreadLocal<>();

	public static void executeWithHandlerFactory(final AuditableOperationHandler handler, final RunnableWithException operation)
			throws Exception
	{
		handlerToUse.set(handler);
		final Supplier<AuditableOperationHandler> currentFactory = AuditableOperations.getHandlerFactory();
		final ThreadAwareHandlerFactory newFactory = new ThreadAwareHandlerFactory(currentFactory);
		AuditableOperations.setHandlerFactory(newFactory);
		try
		{
			operation.run();
		}
		finally
		{
			handlerToUse.remove();
			AuditableOperations.setHandlerFactory(currentFactory);
		}
	}

	public static void executeWithTransactionalHandler(final Supplier<AuditableOperationHandler> handler,
			final RunnableWithException operation) throws Exception
	{
		transactionalHandler.set(handler);
		final Supplier<AuditableOperationHandler> currentFactory = AuditableOperations.getTransactionalHandlerFactory();
		final ThreadAwareTransactionalHandlerFactory newHandler = new ThreadAwareTransactionalHandlerFactory(currentFactory);
		AuditableOperations.setTransactionalHandlerFactory(newHandler);
		try
		{
			operation.run();
		}
		finally
		{
			transactionalHandler.remove();
			AuditableOperations.setTransactionalHandlerFactory(currentFactory);
		}
	}

	private static class ThreadAwareTransactionalHandlerFactory implements Supplier<AuditableOperationHandler>
	{
		private final Supplier<AuditableOperationHandler> delegate;

		public ThreadAwareTransactionalHandlerFactory(final Supplier<AuditableOperationHandler> delegate)
		{
			this.delegate = delegate;
		}

		@Override
		public AuditableOperationHandler get()
		{
			final AuditableOperationHandler fromThread = transactionalHandler.get().get();
			if (fromThread != null)
			{
				return fromThread;
			}
			return delegate.get();
		}
	}

	private static class ThreadAwareHandlerFactory implements Supplier<AuditableOperationHandler>
	{
		private final Supplier<AuditableOperationHandler> delegate;

		public ThreadAwareHandlerFactory(final Supplier<AuditableOperationHandler> delegate)
		{
			this.delegate = delegate;
		}

		@Override
		public AuditableOperationHandler get()
		{
			final AuditableOperationHandler fromThread = handlerToUse.get();
			if (fromThread != null)
			{
				return fromThread;
			}
			return delegate.get();
		}
	}

	public interface RunnableWithException
	{
		void run() throws Exception;
	}


	public static void verifyIfMethodBeforeCommitWasCalledOnCommitXTimes(final BeforeCommitNotification beforeCommitToSpy,
			final int numberOfTimes)
	{

		final BeforeCommitNotification beforeCommitVeryficator = Transaction.current()
				.getAttached(BeforeCommitNotificationVeryficator.class);

		if (beforeCommitVeryficator != null)
		{
			Transaction.current().dettach(beforeCommitVeryficator);
		}

		Transaction.current().attach(new BeforeCommitNotificationVeryficator(beforeCommitToSpy, numberOfTimes));

	}

	public static void verifyIfMethodBeforeRollbackWasCalledOnRollbacktXTimes(final BeforeRollbackNotification beforeCommitToSpy,
			final int numberOfTimes)
	{

		final BeforeRollbackNotificationVeryficator beforeRollbackVeryficator = Transaction.current()
				.getAttached(BeforeRollbackNotificationVeryficator.class);

		if (beforeRollbackVeryficator != null)
		{
			Transaction.current().dettach(beforeRollbackVeryficator);
		}

		Transaction.current().attach(new BeforeRollbackNotificationVeryficator(beforeCommitToSpy, numberOfTimes));

	}


	public static Supplier<AuditableOperationHandler> getMockitoSpyTransactionalHandler()
	{
		return new Supplier<AuditableOperationHandler>()
		{
			@Override
			public AuditableOperationHandler get()
			{
				return Mockito.spy(AuditableOperations.getTransactionalHandlerFromSpringContext());
			}
		};
	}

	public static Supplier<AuditableOperationHandler> getMockitoSpyNonTransactionalHandler()
	{
		return new Supplier<AuditableOperationHandler>()
		{
			@Override
			public AuditableOperationHandler get()
			{
				return Mockito.spy(AuditableOperations.getNonTransactionalHandlerFromSpringContext());
			}
		};
	}

	private static class BeforeCommitNotificationVeryficator implements BeforeCommitNotification
	{
		final BeforeCommitNotification opHandlerSpy;
		final int timesNumber;

		public BeforeCommitNotificationVeryficator(final BeforeCommitNotification opHandlerSpy, final int timesNumber)
		{
			this.opHandlerSpy = opHandlerSpy;
			this.timesNumber = timesNumber;
		}

		@Override
		public void beforeCommit()
		{
			Mockito.verify(opHandlerSpy, Mockito.times(timesNumber)).beforeCommit();
		}

	}


	private static class BeforeRollbackNotificationVeryficator implements BeforeRollbackNotification
	{
		final BeforeRollbackNotification opHandlerSpy;
		final int timesNumber;

		public BeforeRollbackNotificationVeryficator(final BeforeRollbackNotification opHandlerSpy, final int timesNumber)
		{
			this.opHandlerSpy = opHandlerSpy;
			this.timesNumber = timesNumber;
		}

		@Override
		public void beforeRollback()
		{
			Mockito.verify(opHandlerSpy, Mockito.times(timesNumber)).beforeRollback();
		}

	}
}

