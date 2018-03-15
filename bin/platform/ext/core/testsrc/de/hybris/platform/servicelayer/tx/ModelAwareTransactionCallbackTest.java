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

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionException;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.transaction.TransactionStatus;


@UnitTest
public class ModelAwareTransactionCallbackTest
{

	@Test
	public void testCaseSampleStubCheck()
	{
		final ItemModel item = createMock(ItemModel.class);
		final Transaction transaction = createMock(Transaction.class);
		final ModelService service = createMock(ModelService.class);
		final TransactionStatus status = createMock(TransactionStatus.class);
		final ModelAwareTransactionCallback callBack = new ModelAwareTransactionCallback()
		{

			@Override
			protected ItemModel doInModelAwareTransaction(final TransactionStatus localstatus)
			{
				Assert.assertEquals(status, localstatus);
				return item;
			}

			@Override
			protected ModelService getModelService()
			{
				return service;
			}

			@Override
			protected boolean isEnableDelayedStore()
			{
				return false;
			}

			@Override
			protected Transaction prepareTransaction()
			{
				return transaction;
			}

		};
		callBack.doInTransaction(status);
	}

	/**
	 * Successfully call
	 */
	@Test
	public void testCaseSuccessfulCase()
	{
		final ItemModel item = createMock(ItemModel.class);
		final Transaction transaction = createMock(Transaction.class);
		final ModelService service = createMock(ModelService.class);
		final TransactionStatus status = createMock(TransactionStatus.class);
		final ModelAwareTransactionCallback callBack = new ModelAwareTransactionCallback()
		{

			@Override
			protected ItemModel doInModelAwareTransaction(final TransactionStatus localstatus)
			{
				Assert.assertEquals(status, localstatus);
				return item;
			}

			@Override
			protected ModelService getModelService()
			{
				return service;
			}

			@Override
			protected boolean isEnableDelayedStore()
			{
				return false;
			}

			@Override
			protected Transaction prepareTransaction()
			{
				return transaction;
			}
		};

		service.saveAll();
		expectLastCall().once();

		transaction.enableDelayedStore(false);
		expectLastCall().once();

		replay(transaction, service);
		callBack.doInTransaction(status);
		verify(transaction, service);
	}

	@Test(expected = ModelSavingException.class)
	public void testCaseModelSavingException()
	{

		final Exception expectedMessage = new ModelSavingException("some message");

		final ItemModel item = createMock(ItemModel.class);

		final Transaction transaction = createMock(Transaction.class);

		final ModelService service = createMock(ModelService.class);

		final TransactionStatus status = createMock(TransactionStatus.class);

		final ModelAwareTransactionCallback callBack = new ModelAwareTransactionCallback()
		{

			@Override
			protected ItemModel doInModelAwareTransaction(final TransactionStatus localstatus)
			{
				Assert.assertEquals(status, localstatus);
				return item;
			}

			@Override
			protected ModelService getModelService()
			{
				return service;
			}

			@Override
			protected boolean isEnableDelayedStore()
			{
				return false;
			}

			@Override
			protected Transaction prepareTransaction()
			{
				return transaction;
			}


		};

		service.saveAll();
		expectLastCall().andThrow(expectedMessage);

		transaction.enableDelayedStore(false);
		expectLastCall().once();

		replay(transaction, service);
		callBack.doInTransaction(status);
		verify(transaction, service);
	}


	@Test(expected = TransactionException.class)
	public void testCaseComitTxException()
	{

		final Exception expectedMessage = new TransactionException("some message");
		final ItemModel item = createMock(ItemModel.class);
		final Transaction transaction = createMock(Transaction.class);
		final ModelService service = createMock(ModelService.class);
		final TransactionStatus status = createMock(TransactionStatus.class);
		final ModelAwareTransactionCallback callBack = new ModelAwareTransactionCallback()
		{

			@Override
			protected ItemModel doInModelAwareTransaction(final TransactionStatus localstatus)
			{
				Assert.assertEquals(status, localstatus);
				return item;
			}

			@Override
			protected ModelService getModelService()
			{
				return service;
			}

			@Override
			protected boolean isEnableDelayedStore()
			{
				return false;
			}

			@Override
			protected Transaction prepareTransaction()
			{
				return transaction;
			}

		};

		transaction.enableDelayedStore(false);
		expectLastCall().once();


		service.saveAll();
		expectLastCall().andThrow(expectedMessage);


		replay(transaction, service);
		callBack.doInTransaction(status);
		verify(transaction, service);

	}
}
