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
package de.hybris.platform.catalog;


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.PreviewTicket;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.PreviewTicketModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PreviewTicketSLDTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;

	private final PropertyConfigSwitcher persistenceLegacyModeSwitch = new PropertyConfigSwitcher("persistence.legacy.mode");

	@Before
	public void enableDirectPersistence()
	{
		persistenceLegacyModeSwitch.switchToValue("false");
	}

	@After
	public void resetPersistence()
	{
		persistenceLegacyModeSwitch.switchBackToDefault();
	}

	@Test
	public void shouldGetTicketCode()
	{

		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);

		final UserModel user = modelService.create(UserModel.class);
		user.setUid(asUUID());
		modelService.saveAll();

		final PreviewTicketModel previewTicket = modelService.create(PreviewTicketModel.class);
		previewTicket.setPreviewCatalogVersion(catalogVersion);
		previewTicket.setCreatedBy(user);
		previewTicket.setValidTo(new Date());

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, previewTicket);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(previewTicket);

		assertThat(previewTicket.getTicketCode())
				.isEqualTo(PreviewTicket.PREVIEW_TICKET_PREFIX + previewTicket.getPk() + PreviewTicket.PREVIEW_TICKET_POSTFIX);
	}

	@Test
	public void shouldSetDefaultCreatedBy()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);
		modelService.saveAll();

		final PreviewTicketModel previewTicket = modelService.create(PreviewTicketModel.class);
		previewTicket.setPreviewCatalogVersion(catalogVersion);
		previewTicket.setValidTo(new Date());

		assertThat(previewTicket.getCreatedBy()).isNull();

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, previewTicket);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(previewTicket);

		assertThat(previewTicket.getCreatedBy()).isNotNull();
		assertThat(previewTicket.getCreatedBy()).isInstanceOf(UserModel.class);
	}


	private static String asUUID()
	{
		return UUID.randomUUID().toString();
	}
}
