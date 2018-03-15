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
package de.hybris.platform.retention.impl;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultExtensibleRemoveCleanupActionIntegrTest extends ServicelayerBaseTest
{

	@Resource
	protected ModelService modelService;
	@Resource
	protected ReadAuditGateway readAuditGateway;
	@Resource
	protected WriteAuditGateway writeAuditGateway;
	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;
	@Resource
	private DefaultExtensibleRemoveCleanupAction defaultExtensibleRemoveCleanupAction;

	private final Map<String, PropertyConfigSwitcher> auditedTypes = new HashMap<>();
	private final PropertyConfigSwitcher auditForAllTypes = new PropertyConfigSwitcher("auditing.alltypes.enabled");

	@Before
	public void setUp() throws Exception
	{
		prepareAuditingForTypes(true, "user");
	}

	@After
	public void tearDown() throws Exception
	{

		resetAuditConfiguration();
		auditForAllTypes.switchBackToDefault();
		final PropertyConfigSwitcher switcher = new PropertyConfigSwitcher(PersistenceUtils.PERSISTENCE_LEGACY_MODE);
		switcher.switchBackToDefault();
	}

	@Test
	public void shuldRemoveItemAndAuditsWithoutCreatingAdditionAudits()
	{
		//given
		final UserModel user = modelService.create(UserModel.class);
		final String uid = UUID.randomUUID().toString();
		user.setUid(uid);
		user.setName(uid);
		modelService.save(user);

		//when
		Transaction.current().begin();
		defaultExtensibleRemoveCleanupAction.cleanup(null, null,
				ItemToCleanup.builder().withItemType(UserModel._TYPECODE).withPK(user.getPk()).build());
		Transaction.current().commit();

		//then
		assertThat(modelService.isRemoved(user)).isTrue();
		assertThat(readAuditGateway.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build())
				.collect(toList())).hasSize(0);
	}


	private void prepareAuditingForTypes(final boolean enabled, final String... types)
	{
		for (final String type : types)
		{
			writeAuditGateway.removeAuditRecordsForType(type);
			final PropertyConfigSwitcher switcher = new PropertyConfigSwitcher("audit." + type + ".enabled");
			switcher.switchToValue(BooleanUtils.toStringTrueFalse(enabled));
			auditedTypes.put(type, switcher);
		}

		// We need to re-read config
		auditEnablementService.refreshConfiguredAuditTypes();
	}

	private void resetAuditConfiguration()
	{
		for (final Map.Entry<String, PropertyConfigSwitcher> entry : auditedTypes.entrySet())
		{
			entry.getValue().switchBackToDefault();
		}
	}
}
