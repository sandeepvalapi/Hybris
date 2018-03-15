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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.Date;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


/**
 * Utility class for tests. Allows to verify if the tested model is fully SLD compatible.
 */
public class PersistenceTestUtils
{


	/**
	 * Saves and asserts that a given item is saved through SLD instead of Jalo.
	 *
	 * @param modelService
	 *           model service for item saving
	 * @param item
	 *           item to save
	 */
	public static <T extends AbstractItemModel> void saveAndVerifyThatPersistedThroughSld(final ModelService modelService, final T item)
	{

		assertThat(modelService).isNotNull();
		assertThat(item).isNotNull();

		if (modelService == null || item == null)
		{
			return;
		}

		assumeFalse(PersistenceUtils.isPersistenceLagacyModeEnabledInConfig());

		final DefaultModelService modelServiceSpy = (DefaultModelService) Mockito.spy(modelService);
		final WritePersistenceGateway writePersistenceGatewaySpy = Mockito.spy(modelServiceSpy.getWritePersistenceGateway());

		doReturn(writePersistenceGatewaySpy).when(modelServiceSpy).getWritePersistenceGateway();
		modelServiceSpy.save(item);

		final ArgumentCaptor<ChangeSet> changeSet = ArgumentCaptor.forClass(ChangeSet.class);
		verify(writePersistenceGatewaySpy, times(1)).persist(changeSet.capture());

		assertThat(changeSet.getValue().getEntityRecords().stream().anyMatch(er -> er.getPK().equals(item.getPk()))).isTrue();
	}

	/**
	 * Asserts if a given item is backed by persistence object of SLD type.
	 *
	 * @param item
	 *           item to verify
	 */
	public static void verifyThatUnderlyingPersistenceObjectIsSld(final ItemModel item)
	{

		assertThat(item).isNotNull();

		if (item == null)
		{
			return;
		}

		assumeFalse(PersistenceUtils.isPersistenceLagacyModeEnabledInConfig());


		if (item.getItemModelContext() instanceof ItemModelContextImpl)
		{
			final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) item.getItemModelContext();

			assertThat(((PersistenceObjectInternal) itemModelContext.getPersistenceSource().getLatest()).isBackedByJaloItem())
					.isFalse();
		}

	}

	/**
	 * Simulates the effect of database storing a particular date.
	 * <p>
	 * On MySQL this means dropping all milliseconds!
	 */
	public static Date adjustToDB(final Date date)
	{
		if (Config.isMySQLUsed() && !Config.getBoolean("mysql.allow.fractional.seconds", true))
		{
			return new Date((date.getTime() / 1000) * 1000);
		}
		else
		{
			return date;
		}
	}
}
