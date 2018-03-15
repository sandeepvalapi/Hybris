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

import de.hybris.platform.catalog.model.AgreementModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import de.hybris.platform.util.Config;
import de.hybris.platform.util.persistence.PersistenceUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class AgreementSLDTest extends ServicelayerBaseTest
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
	public void shouldSaveViaDirectPersistence()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel version1 = modelService.create(CatalogVersionModel.class);
		version1.setCatalog(defaultCatalog);
		version1.setVersion(asUUID());

		modelService.saveAll();

		final AgreementModel agreement = modelService.create(AgreementModel.class);
		agreement.setId(asUUID());
		agreement.setEnddate(new Date());
		agreement.setCatalogVersion(version1);

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, agreement);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(agreement);

		final Object assignedAgreement = agreement.getProperty(AgreementModel.CATALOG);

		assertThat(assignedAgreement).isNotNull();
		assertThat(assignedAgreement).isInstanceOf(CatalogModel.class);
	}

	private static String asUUID()
	{
		return UUID.randomUUID().toString();
	}
}
