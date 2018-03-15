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
package de.hybris.platform.audit.provider.impl;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.provider.AuditRecordsProvider;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AuditRecordsProviderIntegrationTests extends ServicelayerBaseTest implements AuditableTest
{
	private final PropertyConfigSwitcher persistenceLegacyModeSwitch = new PropertyConfigSwitcher("persistence.legacy.mode");
	@Resource
	protected ModelService modelService;
	@Resource
	protected UserService userService;
	@Resource
	private AuditRecordsProvider auditRecordsProvider;
	private AuditTestHelper helper;

	@Before
	public void setUp() throws Exception
	{
		assumeAuditEnabled();
		helper = new AuditTestHelper();

		persistenceLegacyModeSwitch.switchToValue("true");
	}

	@After
	public void cleanup()
	{
		persistenceLegacyModeSwitch.switchBackToDefault();

		helper.removeCreatedItems();
		helper.clearAuditDataForTypes(TitleModel._TYPECODE, AddressModel._TYPECODE, UserModel._TYPECODE, MediaModel._TYPECODE);
	}

	@Test
	public void shouldReturnAuditRecordsForConfig() throws Exception
	{
		// given
		final AuditReportConfig testAuditReportConfig = helper.createTestConfigForIntegrationTest();
		final UserModel user = helper.prepareTestDataForIntegrationTest();
		final PK userPk = user.getPk();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig).withFullReport()
				.withRootTypePk(userPk).build();

		// when
		final List<AuditRecord> records = auditRecordsProvider.getRecords(config).collect(toList());


		assertThat(records).isNotNull().extracting( //
				AuditRecord::getType, //
				AuditRecord::getAuditType) //
				.containsSubsequence( //
						tuple(UserModel._TYPECODE, AuditType.MODIFICATION), //
						tuple(AddressModel._TYPECODE, AuditType.MODIFICATION), //
						tuple(MediaModel._TYPECODE, AuditType.MODIFICATION), //
						tuple(AddressModel._TYPECODE, AuditType.DELETION) //
				) //
				.containsOnlyOnce( // in any order
						tuple(UserModel._TYPECODE, AuditType.CURRENT), //
						tuple(AddressModel._TYPECODE, AuditType.CURRENT), //
						tuple(TitleModel._TYPECODE, AuditType.CURRENT), //
						tuple(MediaModel._TYPECODE, AuditType.CURRENT) //
		);



		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(TitleModel._TYPECODE)) //
				.extracting( //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), //
						AuditRecord::getAuditType //
				) //
				.containsSubsequence( //
						tuple("Mr", AuditType.CURRENT) //
		);


		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(AddressModel._TYPECODE)) //
				.extracting( //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("town"), //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("streetname"), //
						AuditRecord::getAuditType //
				) //
				.containsSubsequence( //
						tuple("Sosnowiec", "Chopina", AuditType.MODIFICATION), //
						tuple("New York", "55th St.", AuditType.DELETION), //
						tuple("Sosnowiec", "Chopina", AuditType.CURRENT) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(MediaModel._TYPECODE)) //
				.extracting( //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), //
						AuditRecord::getAuditType //
				) //
				.containsSubsequence( //
						tuple("ugly picture of me", AuditType.MODIFICATION), //
						tuple("ugly picture of me", AuditType.CURRENT) //
		);
	}


	@Test
	public void shouldReturnAuditRecordsForConfigWithVirtualAttribute() throws Exception
	{
		// given
		final AuditReportConfig testAuditReportConfig = helper.createTestConfigWithVirtualAttributeForIntegrationTest();
		final UserModel user = helper.prepareTestDataForIntegrationTest();
		final PK userPk = user.getPk();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig)
				.withRootTypePk(userPk).build();

		// when
		final List<AuditRecord> records = auditRecordsProvider.getRecords(config).collect(toList());

		assertThat(records).isNotNull().extracting( //
				AuditRecord::getType, //
				AuditRecord::getAuditType) //
				.containsSubsequence( //
						tuple(UserModel._TYPECODE, AuditType.MODIFICATION), //
						tuple(AddressModel._TYPECODE, AuditType.MODIFICATION), //
						tuple(MediaModel._TYPECODE, AuditType.MODIFICATION), //
						tuple(TitleModel._TYPECODE, AuditType.CREATION), //
						tuple(AddressModel._TYPECODE, AuditType.CREATION), //
						tuple(TitleModel._TYPECODE, AuditType.DELETION), //
						tuple(AddressModel._TYPECODE, AuditType.DELETION), //
						tuple(AddressModel._TYPECODE, AuditType.DELETION) //
				) //
				.contains( // in any order
						tuple(UserModel._TYPECODE, AuditType.CURRENT), //
						tuple(AddressModel._TYPECODE, AuditType.CURRENT), // address1
						tuple(AddressModel._TYPECODE, AuditType.CURRENT), // address2
						tuple(TitleModel._TYPECODE, AuditType.CURRENT), // title1
						tuple(TitleModel._TYPECODE, AuditType.CURRENT), // title2
						tuple(MediaModel._TYPECODE, AuditType.CURRENT) //
		);



		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(TitleModel._TYPECODE)) //
				.extracting( //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), //
						AuditRecord::getAuditType //
				) //
				.containsSubsequence( //
						tuple("Sir", AuditType.CREATION), //
						tuple("Sir", AuditType.DELETION) //
				) //
				.contains( //
						tuple("Engr.", AuditType.CURRENT), //
						tuple("Mr", AuditType.CURRENT) //
		);


		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(AddressModel._TYPECODE)) //
				.extracting( //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("town"), //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("streetname"), //
						AuditRecord::getAuditType //
				) //
				.containsSubsequence( //
						tuple("Sosnowiec", "Chopina", AuditType.MODIFICATION), //
						tuple("Krakow", "Rynek", AuditType.CREATION), //
						tuple("Krakow", "Rynek", AuditType.DELETION), //
						tuple("New York", "55th St.", AuditType.DELETION) //

				) //
				.contains( //
						tuple("Sosnowiec", "Chopina", AuditType.CURRENT) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(MediaModel._TYPECODE)) //
				.extracting( //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), //
						AuditRecord::getAuditType //
				) //
				.containsSubsequence( //
						tuple("ugly picture of me", AuditType.MODIFICATION), //
						tuple("ugly picture of me", AuditType.CURRENT) //
		);
	}

	public void assertContainsAddress(final ArrayList addresses, final String streetname)
	{
		boolean contains = false;
		for (final Object address : addresses)
		{
			if (((Map<String, Object>) address).get("streetname").equals(streetname))
			{
				contains = true;
				break;
			}
		}

		assertThat(contains).isTrue();
	}
}
