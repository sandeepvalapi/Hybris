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

import static java.util.stream.Collectors.toList;

import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.persistence.audit.AuditableOperationsTestSupport.RunnableWithException;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.tx.BeforeCommitNotification;
import de.hybris.platform.tx.BeforeRollbackNotification;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.springframework.transaction.support.TransactionTemplate;



public class TransactionAwareAuditTest extends ServicelayerBaseTest implements AuditableTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private ReadAuditGateway readAuditGateway;
	@Resource
	private TransactionTemplate transactionTemplate;

	final PropertyConfigSwitcher persistanceLegacyModeSwitcher = new PropertyConfigSwitcher(
			PersistenceUtils.PERSISTENCE_LEGACY_MODE);

	final PropertyConfigSwitcher allTypesEnabledSwitcher = new PropertyConfigSwitcher("auditing.alltypes.enabled");


	@After
	public void restoreConfiguration()
	{
		persistanceLegacyModeSwitcher.switchBackToDefault();
		allTypesEnabledSwitcher.switchBackToDefault();
	}



	protected void warmUp()
	{
		final TitleModel title = modelService.create(TitleModel.class);

		title.setCode("TEST_" + UUID.randomUUID());
		title.setName("WARM_UP");

		modelService.save(title);

		modelService.refresh(title);

		modelService.remove(title);

	}

	@Test
	public void testInternalWithExplicitTransactionWithRollbackAndCommit() throws Exception
	{

		AuditableOperationsTestSupport.executeWithTransactionalHandler(
				AuditableOperationsTestSupport.getMockitoSpyTransactionalHandler(), new RunnableWithException()
				{

					@Override
					public void run() throws Exception
					{
						Transaction.current().begin();

						final TitleModel title1 = modelService.create(TitleModel.class);
						final TitleModel title2 = modelService.create(TitleModel.class);

						title1.setCode("TEST_1_" + UUID.randomUUID());
						title2.setCode("TEST_1_" + UUID.randomUUID());

						modelService.saveAll();

						final AuditableOperationHandler firstTransactionalHandler = Transaction.current()
								.getAttached(AuditableOperationHandler.class);

						AuditableOperationsTestSupport.verifyIfMethodBeforeCommitWasCalledOnCommitXTimes(
								(BeforeCommitNotification) firstTransactionalHandler, 0);
						AuditableOperationsTestSupport.verifyIfMethodBeforeRollbackWasCalledOnRollbacktXTimes(
								(BeforeRollbackNotification) firstTransactionalHandler, 1);
						Transaction.current().rollback();

						Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, title1.getPk())).isEmpty();

						Transaction.current().begin();

						final TitleModel title3 = modelService.create(TitleModel.class);
						final TitleModel title4 = modelService.create(TitleModel.class);
						title3.setCode("TEST_1_" + UUID.randomUUID());
						title4.setCode("TEST_1_" + UUID.randomUUID());

						modelService.saveAll();

						final AuditableOperationHandler secondTransactionalHandler = Transaction.current()
								.getAttached(AuditableOperationHandler.class);
						Assertions.assertThat(firstTransactionalHandler).isNotEqualTo(secondTransactionalHandler);
						//transactionalSpyHandler should be released on first transaction commit
						AuditableOperationsTestSupport.verifyIfMethodBeforeCommitWasCalledOnCommitXTimes(
								(BeforeCommitNotification) secondTransactionalHandler, 1);
						AuditableOperationsTestSupport.verifyIfMethodBeforeRollbackWasCalledOnRollbacktXTimes(
								(BeforeRollbackNotification) secondTransactionalHandler, 0);

						Transaction.current().commit();

						Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, title1.getPk())).isEmpty();
						Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, title2.getPk())).isEmpty();
						Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, title3.getPk())).isNotEmpty();
						Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, title4.getPk())).isNotEmpty();
					}

				});

	}

	@Test
	public void testInternalWithExplicitTransaction() throws Exception
	{

		AuditableOperationsTestSupport.executeWithTransactionalHandler(
				AuditableOperationsTestSupport.getMockitoSpyTransactionalHandler(), new RunnableWithException()
				{

					@Override
					public void run() throws Exception
					{
						Transaction.current().begin();


						final TitleModel title1 = modelService.create(TitleModel.class);
						final TitleModel title2 = modelService.create(TitleModel.class);

						title1.setCode("TEST_1_" + UUID.randomUUID());
						title2.setCode("TEST_1_" + UUID.randomUUID());

						modelService.saveAll();
						final AuditableOperationHandler firstTransactionalHandler = Transaction.current()
								.getAttached(AuditableOperationHandler.class);
						AuditableOperationsTestSupport.verifyIfMethodBeforeCommitWasCalledOnCommitXTimes(
								(BeforeCommitNotification) firstTransactionalHandler, 1);

						Transaction.current().commit();

						Transaction.current().begin();

						title1.setName("Changed");
						title2.setName("Changed");
						modelService.saveAll();
						//
						final AuditableOperationHandler secondTransactionalHandler = Transaction.current()
								.getAttached(AuditableOperationHandler.class);

						Assertions.assertThat(firstTransactionalHandler).isNotEqualTo(secondTransactionalHandler);

						AuditableOperationsTestSupport.verifyIfMethodBeforeCommitWasCalledOnCommitXTimes(
								(BeforeCommitNotification) secondTransactionalHandler, 1);
						Transaction.current().commit();
						Assertions.assertThat(!Transaction.current().isRunning());
					}

				});
	}


	@Test
	public void testInternalWithExplicitNestedTransaction() throws Exception
	{

		AuditableOperationsTestSupport.executeWithTransactionalHandler(
				AuditableOperationsTestSupport.getMockitoSpyTransactionalHandler(), new RunnableWithException()
				{

					@Override
					public void run() throws Exception
					{

						Transaction.current().begin();
						final TitleModel title1 = modelService.create(TitleModel.class);
						Transaction.current().begin();
						final TitleModel title2 = modelService.create(TitleModel.class);

						title1.setCode("TEST_1_" + UUID.randomUUID());
						title2.setCode("TEST_1_" + UUID.randomUUID());

						modelService.saveAll();
						final AuditableOperationHandler transactionalHandler = Transaction.current()
								.getAttached(AuditableOperationHandler.class);

						AuditableOperationsTestSupport
								.verifyIfMethodBeforeCommitWasCalledOnCommitXTimes((BeforeCommitNotification) transactionalHandler, 0);
						Transaction.current().commit();
						AuditableOperationsTestSupport
								.verifyIfMethodBeforeCommitWasCalledOnCommitXTimes((BeforeCommitNotification) transactionalHandler, 1);
						Transaction.current().commit();
					}

				});
	}

	@Test
	public void shouldNotSaveItemAndAuditWithExplicitTransaction() throws Exception
	{
		//given
		Transaction.current().begin();
		final TitleModel title1 = modelService.create(TitleModel.class);
		title1.setCode("TEST_1_" + UUID.randomUUID());
		modelService.saveAll();

		//when
		try
		{
			Transaction.current().attach(new BeforeCommitNotification()
			{
				@Override
				public void beforeCommit()
				{
					assertIfItemAndAuditIsSaved(title1.getPk());
					throw new RuntimeException("Items and audit records should not be stored");
				}
			});

			Transaction.current().commit();
			Assertions.fail("Runtime exception shuld be thrown");
		}
		catch (final Exception e)
		{
			Assertions.assertThat(e).hasMessage("Items and audit records should not be stored");
		}
		finally
		{
			assertIfItemAndAuditIsNotSaved(title1.getPk());
		}
	}


	@Test
	public void shouldSaveItemAndAuditWithExcplicitTransaction() throws Exception
	{

		Transaction.current().begin();

		final TitleModel title1 = modelService.create(TitleModel.class);
		title1.setCode("TEST_1_" + UUID.randomUUID());

		modelService.saveAll();

		//
		Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, title1.getPk())).isEmpty();
		Assertions.assertThat(checkIfItemModelExist(title1.getPk())).isTrue();

		Transaction.current().attach(checkIfAuditForItemModelExist(TitleModel._TYPECODE, title1.getPk()));

		Transaction.current().commit();

		assertIfItemAndAuditIsSaved(title1.getPk());

	}


	@Test
	public void shouldSaveItemAndAuditWithExplicitNestedTransaction() throws Exception
	{
		Assertions.assertThat(Transaction.current().isRunning()).isFalse();

		Transaction.current().begin();

		final TitleModel title1 = modelService.create(TitleModel.class);
		title1.setCode("TEST_1_" + UUID.randomUUID());

		Transaction.current().begin();

		modelService.saveAll();

		Assertions.assertThat(checkIfItemModelExist(title1.getPk())).isTrue();

		Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, title1.getPk())).isEmpty();

		Transaction.current().commit();

		Transaction.current().attach(checkIfAuditForItemModelExist(TitleModel._TYPECODE, title1.getPk()));

		Transaction.current().commit();

		assertIfItemAndAuditIsSaved(title1.getPk());
		Assertions.assertThat(Transaction.current().isRunning()).isFalse();
	}

	@Test
	public void shouldNotSaveItemAndAuditWithExplicitNestedTransactionAfterExceptionIsThrown() throws Exception
	{
		Assertions.assertThat(Transaction.current().isRunning()).isFalse();

		Transaction.current().begin();

		final TitleModel title1 = modelService.create(TitleModel.class);
		title1.setCode("TEST_1_" + UUID.randomUUID());

		Transaction.current().begin();

		modelService.saveAll();

		Assertions.assertThat(checkIfItemModelExist(title1.getPk())).isTrue();
		Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, title1.getPk())).isEmpty();


		Transaction.current().commit();

		Transaction.current().attach(checkIfAuditForItemModelExist(TitleModel._TYPECODE, title1.getPk()));

		Transaction.current().attach(new BeforeCommitNotification()
		{
			@Override
			public void beforeCommit()
			{
				assertIfItemAndAuditIsSaved(title1.getPk());
				throw new IllegalStateException("Items and audit records should not be stored");
			}
		});

		try
		{
			Transaction.current().commit();
			Assertions.fail("Runtime exception shuld be thrown");
		}
		catch (final IllegalStateException e)
		{
			//fine
		}
		finally
		{
			assertIfItemAndAuditIsNotSaved(title1.getPk());
		}
		Assertions.assertThat(Transaction.current().isRunning()).isFalse();
	}

	private List<AuditRecord> getAuditRecordsForType(final String type, final PK pk)
	{
		return readAuditGateway.search(AuditSearchQuery.forType(type).withPkSearchRules(pk).build()).collect(toList());
	}

	@Test
	public void shouldNotSaveItemAndAuditWithExplicitNestedTransactionAfterExceptionIsThrown2() throws Exception
	{
		Assertions.assertThat(Transaction.current().isRunning()).isFalse();

		Transaction.current().begin();

		final TitleModel title1 = modelService.create(TitleModel.class);
		title1.setCode("TEST_1_" + UUID.randomUUID());
		modelService.saveAll();
		Transaction.current().commit();

		try
		{

			Transaction.current().begin();

			Transaction.current().begin();
			title1.setCode("CHANGE_1_" + UUID.randomUUID());
			modelService.saveAll();

			Transaction.current().commit();

			Transaction.current().begin();
			throw new IllegalStateException();

		}
		catch (final IllegalStateException e)
		{
			//rollback  nested transaction
			Transaction.current().rollback();
			//rollback outer transaction
			Transaction.current().rollback();
		}

		modelService.refresh(title1);

		Assertions.assertThat(title1.getCode()).startsWith("TEST_1_");
		final List<AuditRecord> recList = getAuditRecordsForType(TitleModel._TYPECODE, title1.getPk());
		Assertions.assertThat(recList).isNotEmpty();
		Assertions.assertThat(recList).hasSize(1);
		Assertions.assertThat(recList.get(0).getAuditType()).isEqualTo(AuditType.CREATION);


		Assertions.assertThat(Transaction.current().isRunning()).isFalse();
	}

	@Test
	public void shouldNotStoreItemAndAuditWithSpringTransaction()
	{
		Assertions.assertThat(Transaction.current().isRunning()).isFalse();

		Transaction.current().begin();
		final TitleModel title1 = modelService.create(TitleModel.class);
		title1.setCode("TEST_1_" + UUID.randomUUID());
		modelService.saveAll();

		Transaction.current().commit();
		modelService.refresh(title1);

		try
		{
			final TitleModel title = transactionTemplate.execute(status -> {

				title1.setCode("NEW_CODE_" + UUID.randomUUID());
				modelService.saveAll();
				Assertions.assertThat(Transaction.current().isRunning()).isTrue();

				Transaction.current().attach(new BeforeCommitNotification()
				{
					@Override
					public void beforeCommit()
					{
						throw new RuntimeException("Items and audit records should not be stored");
					}
				});

				return title1;
			});

		}
		catch (final Exception e)
		{
			//fine
		}
		final TitleModel title = modelService.get(title1.getPk());

		Assertions.assertThat(title.getCode()).startsWith("TEST_1_");
		final List<AuditRecord> recList = getAuditRecordsForType(TitleModel._TYPECODE, title.getPk());
		Assertions.assertThat(recList).isNotEmpty();
		Assertions.assertThat(recList).hasSize(1);
		Assertions.assertThat(recList.get(0).getAuditType()).isEqualTo(AuditType.CREATION);

		Assertions.assertThat(Transaction.current().isRunning()).isFalse();
	}

	private void assertIfItemAndAuditIsNotSaved(final PK pk)
	{
		Assertions.assertThat(checkIfItemModelExist(pk)).isFalse();
		Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, pk)).isEmpty();
	}

	private void assertIfItemAndAuditIsSaved(final PK pk)
	{
		Assertions.assertThat(checkIfItemModelExist(pk)).isTrue();
		Assertions.assertThat(getAuditRecordsForType(TitleModel._TYPECODE, pk)).isNotEmpty();
	}

	private boolean checkIfItemModelExist(final PK pk)
	{
		try
		{
			modelService.get(pk);
			return true;
		}
		catch (final ModelLoadingException e)
		{
			return false;
		}
	}

	private BeforeCommitNotification checkIfAuditForItemModelExist(final String type, final PK pk)
	{
		return new BeforeCommitNotification()
		{
			@Override
			public void beforeCommit()
			{
				checkIfItemModelExist(pk);
				Assertions.assertThat(getAuditRecordsForType(type, pk)).hasSize(1);
			}
		};
	}

}
