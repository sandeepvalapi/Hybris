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
package de.hybris.platform.persistence.audit.internal;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AuditEnablementServiceTest extends ServicelayerBaseTest
{
	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;
	@Resource
	private TypeService typeService;
	private final PropertyConfigSwitcher auditingEnabledFlag = getConfigSwitcherForAuditingEnabledFlag();
	private final PropertyConfigSwitcher flagForAllTyes = getConfigSwitcherForAllTypesFlag();
	private final PropertyConfigSwitcher flagForUserType = getConfigSwitcherForType("User");
	private final PropertyConfigSwitcher flagForBlacklistedType = getConfigSwitcherForType("ItemSyncTimestamp");
	private final PropertyConfigSwitcher flagForItem = getConfigSwitcherForType("Item");
	private final PropertyConfigSwitcher flagForProcessTask = getConfigSwitcherForType("ProcessTask");
	private PK typePkForUser;
	private PK typePkForEmployee;

	@Before
	public void setUp() throws Exception
	{
		typePkForUser = typeService.getComposedTypeForCode("User").getPk();
		typePkForEmployee = typeService.getComposedTypeForCode("Employee").getPk();
	}

	@After
	public void tearDown() throws Exception
	{
		auditingEnabledFlag.switchBackToDefault();
		flagForUserType.switchBackToDefault();
		flagForBlacklistedType.switchBackToDefault();
		flagForItem.switchBackToDefault();
		flagForProcessTask.switchBackToDefault();
		flagForAllTyes.switchBackToDefault();

		auditEnablementService.refreshConfiguredAuditTypes();
	}

	@Test
	public void shouldHaveAuditEnabledForTypeWhichIsNotConfiguredDirectlyButFlagForAllTypesIsEnabled() throws Exception
	{
		// given
        auditingEnabledFlag.switchToValue("true");
        flagForAllTyes.switchToValue("true");
        final PK mediaTypePk = typeService.getComposedTypeForCode("Media").getPk();

		// when
        final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(mediaTypePk);

        // then
        assertThat(auditEnabled).isTrue();
	}

	@Test
	public void shouldHaveAuditDisabledWhenGlobalFlagIsSetToFalse() throws Exception
	{
		// given
		auditingEnabledFlag.switchToValue("false");

		// when
		final boolean enabledGlobally = auditEnablementService.isAuditEnabledGlobally();

		// then
		assertThat(enabledGlobally).isFalse();
	}

	@Test
	public void shouldHaveAuditEnabledWhenGlobalFlagIsSetToTrue() throws Exception
	{
		// given
		auditingEnabledFlag.switchToValue("true");

		// when
		final boolean enabledGlobally = auditEnablementService.isAuditEnabledGlobally();

		// then
		assertThat(enabledGlobally).isTrue();
	}

	@Test
	public void shouldHaveAuditDisabledForAnyTypeIfAuditIsDisabledGlobally() throws Exception
	{
		// given
		auditingEnabledFlag.switchToValue("false");

		// when
		final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(typePkForUser);

		// then
		assertThat(auditEnabled).isFalse();
	}

	@Test
	public void shouldHaveAuditDisabledForTypeWhichIsNotConfigured() throws Exception
	{
		// given
		auditingEnabledFlag.switchToValue("true");
		flagForAllTyes.switchToValue("false");
		final PK mediaTypePk = typeService.getComposedTypeForCode("Media").getPk();

		// when
		final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(mediaTypePk);

		// then
		assertThat(auditEnabled).isFalse();
	}

	@Test
	public void shouldHaveAuditDisabledForBlacklistedTypeEvenItIsConfiguredByUser() throws Exception
	{
		// given
		flagForAllTyes.switchToValue("false");
		auditingEnabledFlag.switchToValue("true");
		flagForBlacklistedType.switchToValue("true");
		auditEnablementService.refreshConfiguredAuditTypes();
		final PK typePk = typeService.getComposedTypeForCode("ItemSyncTimestamp").getPk();

		// when
		final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(typePk);

		// then
		assertThat(auditEnabled).isFalse();
	}

	@Test
	public void shouldHaveAuditDisabledForBlacklistedTypeEvenIfAuditIsEnabledForAllTypes() throws Exception
	{
		// given
		flagForAllTyes.switchToValue("true");
		auditingEnabledFlag.switchToValue("true");
		final PK typePk = typeService.getComposedTypeForCode("ItemSyncTimestamp").getPk();

		// when
		final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(typePk);

		// then
		assertThat(auditEnabled).isFalse();
	}

	@Test
	public void shouldHaveAuditDisabledForBlacklistedTypeEvenIfAuditIsEnabledForSuperTypeOfBlacklistedOne() throws Exception
	{
		// given
		flagForAllTyes.switchToValue("true");
		auditingEnabledFlag.switchToValue("true");
		flagForItem.switchToValue("true");
		auditEnablementService.refreshConfiguredAuditTypes();
		final PK typePk = typeService.getComposedTypeForCode("ItemSyncTimestamp").getPk();

		// when
		final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(typePk);

		// then
		assertThat(auditEnabled).isFalse();
	}

	@Test
	public void shouldHaveAuditDisabledForTypeIfAuditrSuperTypeIsBlacklisted() throws Exception
	{
		// given
		flagForAllTyes.switchToValue("true");
		auditingEnabledFlag.switchToValue("true");
		flagForProcessTask.switchToValue("true");
		auditEnablementService.refreshConfiguredAuditTypes();
		final PK typePk = typeService.getComposedTypeForCode("ProcessTask").getPk();

		// when
		final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(typePk);

		// then
		assertThat(auditEnabled).isFalse();
	}

	@Test
	public void shouldHaveAuditEnabledForUserTypeWhenItIsSetInConfigurationAndGlobalFlagIsEnabled() throws Exception
	{
		// given
		flagForAllTyes.switchToValue("false");
		auditingEnabledFlag.switchToValue("true");
		flagForUserType.switchToValue("true");
		auditEnablementService.refreshConfiguredAuditTypes();

		// when
		final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(typePkForUser);

		// then
		assertThat(auditEnabled).isTrue();
	}

	@Test
	public void shouldHaveAuditEnabledForSubTypeWhenSupertpeIsSetInConfigurationAndGlobalFlagIsEnabled() throws Exception
	{
		// given
		flagForAllTyes.switchToValue("false");
		auditingEnabledFlag.switchToValue("true");
		flagForUserType.switchToValue("true");
		auditEnablementService.refreshConfiguredAuditTypes();

		// when
		final boolean auditEnabled = auditEnablementService.isAuditEnabledForType(typePkForEmployee);

		// then
		assertThat(auditEnabled).isTrue();
	}

	private PropertyConfigSwitcher getConfigSwitcherForAllTypesFlag()
	{
		return new PropertyConfigSwitcher(AuditEnablementService.AUDITING_FOR_ALL_TYPES);
	}

	private PropertyConfigSwitcher getConfigSwitcherForAuditingEnabledFlag()
	{
		return new PropertyConfigSwitcher(AuditEnablementService.AUDITING_ENABLED);
	}

	private PropertyConfigSwitcher getConfigSwitcherForType(final String type)
	{
		return new PropertyConfigSwitcher("audit." + type + ".enabled");
	}
}
