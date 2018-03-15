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
package de.hybris.platform.servicelayer.internal.model.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.ModelPersister;
import de.hybris.platform.servicelayer.internal.model.extractor.ChangeSetBuilder;
import de.hybris.platform.servicelayer.internal.model.extractor.ModelExtractor;
import de.hybris.platform.servicelayer.internal.model.extractor.PersistenceTypeService;
import de.hybris.platform.servicelayer.locking.impl.DefaultItemLockingService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


@UnitTest
public class DefaultModelServiceTest
{
	@Spy
	@InjectMocks
	private final DefaultModelService modelService = new DefaultModelService();
	@Mock
	private TransactionTemplate transaction;
	@Mock
	private ItemModel model1;
	@Mock
	private ItemModel model2;
	@Mock
	private InterceptorRegistry interceptorRegistry;
	@Mock
	private ConverterRegistry converterRegistry;
	@Mock
	private SessionService sessionService;
	@Mock
	private ModelConverter modelConverter;
	//gets injected at modelService automatically
	@SuppressWarnings("unused")
	@Mock
	private ModelPersister modelPersister;
	//gets injected at modelService automatically
	@SuppressWarnings("unused")
	@Mock
	private ModelContext modelContext;
	@Mock
	private ModelExtractor modelExtractor;
	@Mock
	private PersistenceTypeService persistenceTypeService;
	@Mock
	private ChangeSetBuilder changeSetBuilder;
	@Mock
	private CacheInvalidator cacheInvalidator;
	@Mock
	private EventService eventService;
	@Mock
	private DefaultItemLockingService defaultItemLockingService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void shouldEnableTransactionsLocally()
	{
		// given
		doReturn(sessionService).when(modelService).lookupSessionService();

		// when
		modelService.enableTransactions();

		// then
		verify(sessionService, times(1)).setAttribute(DefaultModelService.ENABLE_TRANSACTIONAL_SAVES, Boolean.TRUE);
	}

	@Test
	public void shouldDisableTransactionsLocally()
	{
		// given
		doReturn(sessionService).when(modelService).lookupSessionService();

		// when
		modelService.disableTransactions();

		// then
		verify(sessionService, times(1)).setAttribute(DefaultModelService.ENABLE_TRANSACTIONAL_SAVES, Boolean.FALSE);
	}

	@Test
	public void shouldClearTransactionSettings()
	{
		// given
		doReturn(sessionService).when(modelService).lookupSessionService();

		// when
		modelService.clearTransactionsSettings();

		// then
		verify(sessionService, times(1)).removeAttribute(DefaultModelService.ENABLE_TRANSACTIONAL_SAVES);
	}

	@Test
	public void shouldExecuteSaveVaragsObjectsInTransactionWhenGlobalTransactionFlagIsTrue() throws Exception
	{
		// given
		modelService.setTransactional(true);
		doReturn(sessionService).when(modelService).lookupSessionService();


		// when (execute all 4 save* methods)
		modelService.save(model1);
		modelService.saveAll(model1, model2);
		modelService.saveAll(Collections.singletonList(model1));
		modelService.saveAll();

		// then (should be 4 interactions with transaction mock)
		verify(transaction, times(4)).execute((TransactionCallback) anyObject());
	}

	@Test
	public void shouldExecuteSaveVaragsObjectsInTransactionWhenGlobalTransactionFlagIsFalseButLocallyTransactionsAreEnabled()
			throws Exception
	{
		// given
		modelService.setTransactional(false);
		doReturn(sessionService).when(modelService).lookupSessionService();
		given(sessionService.getAttribute(DefaultModelService.ENABLE_TRANSACTIONAL_SAVES)).willReturn(Boolean.TRUE);

		// when (execute all 4 save* methods)
		modelService.save(model1);
		modelService.saveAll(model1, model2);
		modelService.saveAll(Collections.singletonList(model1));
		modelService.saveAll();

		// then (should be 4 interactions with transaction mock)
		verify(transaction, times(4)).execute((TransactionCallback) anyObject());
	}

	@Test
	public void shouldExecuteSaveVaragsObjectsWithoutTransactionWhenGlobalTransactionFlagIsFalse() throws Exception
	{
		// given
		modelService.setTransactional(false);
		doReturn(sessionService).when(modelService).lookupSessionService();
		doReturn(interceptorRegistry).when(modelService).lookupInterceptorRegistry();
		doReturn(converterRegistry).when(modelService).lookupConverterRegistry();
		given(converterRegistry.getModelConverterByModelType((Class) anyObject())).willReturn(modelConverter);
		given(modelConverter.getType(model1)).willReturn("ItemModel");
		given(converterRegistry.getModelConverterByModel(model1)).willReturn(modelConverter);
		given(converterRegistry.getModelConverterByModel(model2)).willReturn(modelConverter);

		// when (execute all 4 save* methods)
		modelService.save(model1);
		modelService.saveAll(model1, model2);
		modelService.saveAll(Collections.singletonList(model1));
		modelService.saveAll();

		// then (should be 0 interactions with transaction mock)
		verify(transaction, times(0)).execute((TransactionCallback) anyObject());
	}

	@Test
	public void shouldExecuteSaveVaragsObjectsWithoutTransactionWhenGlobalTransactionFlagIsTrueButLocallyTransactionsAreDisabled()
			throws Exception
	{
		// given
		modelService.setTransactional(true);
		doReturn(sessionService).when(modelService).lookupSessionService();
		doReturn(interceptorRegistry).when(modelService).lookupInterceptorRegistry();
		doReturn(converterRegistry).when(modelService).lookupConverterRegistry();
		given(converterRegistry.getModelConverterByModelType((Class) anyObject())).willReturn(modelConverter);
		given(modelConverter.getType(model1)).willReturn("ItemModel");
		given(sessionService.getAttribute(DefaultModelService.ENABLE_TRANSACTIONAL_SAVES)).willReturn(Boolean.FALSE);
		given(converterRegistry.getModelConverterByModel(model1)).willReturn(modelConverter);
		given(converterRegistry.getModelConverterByModel(model2)).willReturn(modelConverter);

		// when (execute all 4 save* methods)
		modelService.save(model1);
		modelService.saveAll(model1, model2);
		modelService.saveAll(Collections.singletonList(model1));
		modelService.saveAll();

		// then (should be 0 interactions with transaction mock)
		verify(transaction, times(0)).execute((TransactionCallback) anyObject());
	}

	@Test
	public void removeAllWithTransaction() throws Exception
	{
		// given
		doReturn(sessionService).when(modelService).lookupSessionService();
		doReturn(interceptorRegistry).when(modelService).lookupInterceptorRegistry();
		doReturn(converterRegistry).when(modelService).lookupConverterRegistry();
		given(converterRegistry.getModelConverterByModelType((Class) anyObject())).willReturn(modelConverter);
		given(modelConverter.getType(model1)).willReturn("ItemModel");
		given(sessionService.getAttribute(DefaultModelService.ENABLE_TRANSACTIONAL_SAVES)).willReturn(Boolean.TRUE);

		// when (execute 2 remove* methods)
		modelService.remove(model1);
		modelService.removeAll(Arrays.asList(model1, model2));

		// then (should be 2 interactions with transaction mock)
		verify(transaction, times(2)).execute((TransactionCallback) anyObject());
	}

	@Test
	public void removeAllWithoutTransaction() throws Exception
	{
		// given
		doReturn(sessionService).when(modelService).lookupSessionService();
		doReturn(interceptorRegistry).when(modelService).lookupInterceptorRegistry();
		doReturn(converterRegistry).when(modelService).lookupConverterRegistry();

		given(converterRegistry.getModelConverterByModelType((Class) anyObject())).willReturn(modelConverter);
		given(modelConverter.getType(model1)).willReturn("ItemModel");
		given(sessionService.getAttribute(DefaultModelService.ENABLE_TRANSACTIONAL_SAVES)).willReturn(Boolean.FALSE);
		given(converterRegistry.getModelConverterByModel(model1)).willReturn(modelConverter);
		given(converterRegistry.getModelConverterByModel(model2)).willReturn(modelConverter);

		// when (execute 2 remove* methods)
		modelService.remove(model1);
		modelService.removeAll(Arrays.asList(model1, model2));

		// then (should be 0 interactions with transaction mock)
		verify(transaction, times(0)).execute((TransactionCallback) anyObject());
	}




}
