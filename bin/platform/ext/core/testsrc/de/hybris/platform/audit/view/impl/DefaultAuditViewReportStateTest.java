
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
package de.hybris.platform.audit.view.impl;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AtomicAttribute;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.internal.config.VirtualAttribute;
import de.hybris.platform.audit.provider.AuditRecordsProvider;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.seed.TestDataCreator;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;



public class DefaultAuditViewReportStateTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private AuditViewService auditViewService;

	@Resource
	protected ModelService modelService;

	@Resource
	protected UserService userService;

	@Resource
	private AuditRecordsProvider auditRecordsProvider;

	protected TestDataCreator dataCreator;
	protected UserModel userData;
	protected TestDataCreator creator;
	protected AddressModel address1;

	@Before
	public void setUpConfigAndData()
	{
		createTestData();
		createTestConfig();
	}

	private void createTestData()
	{
		creator = new TestDataCreator(modelService);
		userData = creator.createUser("userTest1", "Adam");
		address1 = creator.createAddress("Sosnowiec", "Moniuszki", userData);

	}

	private AuditReportConfig createTestConfig()
	{
		final Type address = Type.builder() //
				.withCode("Address") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("town").build(), //
						AtomicAttribute.builder().withQualifier("streetname").build() //
				).build();

		final Type user = Type.builder().withCode("User") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
				) //

				.withVirtualAttributes( //
						VirtualAttribute.builder().withExpression("ownedAddresses").withMany(Boolean.TRUE).withType(address)
								.withResolvesBy( //
										ResolvesBy.builder().withExpression("owner").withResolverBeanId("virtualReferencesResolver").build() //
								).build()).build();


		final AuditReportConfig reportConfig = AuditReportConfig.builder() //
				.withGivenRootType(user) //
				.withName("testConfig") //
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, address) //
				.build();

		return reportConfig;

	}

	@Test
	public void shouldReturnOnlyOneRecordInStream()
	{
		//given
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(createTestConfig())
				.withRootTypePk(userData.getPk()).build();
		//when
		final Stream<ReportView> reportView = auditViewService.getViewOn(config);
		//then
		assertThat(reportView.count()).isEqualTo(1);
	}

	@Test
	public void shouldReturnMoreThanOneRecordInStream()
	{
		//given
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(createTestConfig())
				.withRootTypePk(userData.getPk()).withFullReport().build();

		// when
		final List<AuditRecord> records = auditRecordsProvider.getRecords(config).collect(toList());
		final Stream<ReportView> reportViews = auditViewService.getViewOn(config);

		assertThat(records).hasSize(2);

		//then
		if (records.get(0).getTimestamp().equals(records.get(1).getTimestamp()) && records.get(0).getType().equals("Address"))
		{
			assertThat(reportViews).hasSize(1);
		}
		else
		{
			assertThat(reportViews).hasSize(2);
		}
	}
}
