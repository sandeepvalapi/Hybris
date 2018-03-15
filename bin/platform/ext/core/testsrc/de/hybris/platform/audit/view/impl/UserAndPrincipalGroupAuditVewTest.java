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

import static de.hybris.platform.audit.AuditTestHelper.noDuplicatedReportEntries;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.demo.AuditTestConfigManager;
import de.hybris.platform.audit.internal.config.AtomicAttribute;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.RelationAttribute;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@IntegrationTest
public class UserAndPrincipalGroupAuditVewTest extends ServicelayerTransactionalBaseTest implements AuditableTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private AuditViewService auditViewService;

	private AuditTestConfigManager auditTestConfigManager;

	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;

	@Before
	public void setUp() throws Exception
	{
		//assumeAuditEnabled();
		auditTestConfigManager = new AuditTestConfigManager(auditEnablementService);
		auditTestConfigManager.enableAuditingForTypes("User", "PrincipalGroupRelation", "UserGroup");
	}

	@Test
	public void shouldGenerateReportForActualDataWhenCreatingUserWithinGroup()
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setName("AdminGroup");
		group.setUid(UUID.randomUUID().toString());
		modelService.save(group);

		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("user1");
		user.setGroups(Sets.newHashSet(group));
		modelService.save(user);

		final List<ReportView> reportViews = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(createConfig()).withRootTypePk(user.getPk()).build())
				.collect(toList());

		assertThat(reportViews).hasSize(1);
		assertThat(reportViews).extracting(ReportView::getPayload).extracting("User").extracting("name").contains("user1");
		assertThat(reportViews).extracting(ReportView::getPayload).extracting("User").flatExtracting("UserGroup").extracting("name")
				.contains("AdminGroup");


	}

	@Test
	public void shouldGenerateReportForActualDataWhenReferencingManyGroups() throws Exception
	{
		final UserGroupModel group1 = modelService.create(UserGroupModel.class);
		group1.setName("Group1");
		group1.setUid(UUID.randomUUID().toString());
		modelService.save(group1);

		final UserGroupModel group2 = modelService.create(UserGroupModel.class);
		group2.setName("Group2");
		group2.setUid(UUID.randomUUID().toString());
		modelService.save(group2);

		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("user1");
		user.setGroups(Sets.newHashSet(group1, group2));
		modelService.save(user);

		final List<ReportView> reportViews = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(createConfig()).withRootTypePk(user.getPk()).build())
				.collect(toList());

		assertThat(reportViews).hasSize(1);
		assertThat(reportViews).extracting(ReportView::getPayload).extracting("User").extracting("name").contains("user1");
		assertThat(reportViews).extracting(ReportView::getPayload).extracting("User").flatExtracting("UserGroup").extracting("name")
				.contains("Group1", "Group2");
	}

	@Test
	public void shouldGenerateReportForActualDataWhenAddingUserToGroup()
	{

		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setName("AdminGroup");
		group.setUid(UUID.randomUUID().toString());
		modelService.save(group);

		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("user1");
		modelService.save(user);

		group.setMembers(Sets.newHashSet(user));
		modelService.save(group);

		final List<ReportView> reportViews = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(createConfig()).withRootTypePk(user.getPk()).build())
				.collect(toList());

		assertThat(reportViews).hasSize(1);
		assertThat(reportViews).extracting(ReportView::getPayload).extracting("User").extracting("name").contains("user1");
		assertThat(reportViews).extracting(ReportView::getPayload).extracting("User").flatExtracting("UserGroup").extracting("name")
				.contains("AdminGroup");


	}

	@Test
	public void shouldGenerateReportForActualDataWhenAddingGroupToUser()
	{

		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setName("AdminGroup");
		group.setUid(UUID.randomUUID().toString());
		modelService.save(group);

		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("user1");
		modelService.save(user);

		user.setGroups(Sets.newHashSet(group));
		modelService.save(user);

		final List<ReportView> reportViews = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(createConfig()).withRootTypePk(user.getPk()).build())
				.collect(toList());

		assertThat(reportViews).hasSize(1);
		assertThat(reportViews).extracting(ReportView::getPayload).extracting("User").extracting("name").contains("user1");
		assertThat(reportViews).extracting(ReportView::getPayload).extracting("User").flatExtracting("UserGroup").extracting("name")
				.contains("AdminGroup");


	}

	@Test
	public void shouldGenerateReportForAuditDataWhenAddingUserToGroup() throws Exception
	{


		final UserGroupModel group1 = modelService.create(UserGroupModel.class);
		group1.setName("Group1");
		group1.setUid(UUID.randomUUID().toString());
		modelService.save(group1);

		final UserGroupModel group2 = modelService.create(UserGroupModel.class);
		group2.setName("Group2");
		group2.setUid(UUID.randomUUID().toString());
		modelService.save(group2);

		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("user1");
		modelService.save(user);

		user.setGroups(Sets.newHashSet(group1, group2));
		modelService.save(user);

		final List<ReportView> reportViews = auditViewService.getViewOn(
				TypeAuditReportConfig.builder().withConfig(createConfig()).withFullReport().withRootTypePk(user.getPk()).build())
				.collect(toList());


		reportViews.forEach(reportView -> {
			System.out.println(reportView.getPayload() + " " + reportView.getTimestamp().getTime());
		});

		assertThat(reportViews).extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(
						o -> ((List<Map<String, Object>>) ((Map<String, Object>) o).getOrDefault("UserGroup", Collections.emptyList()))
								.stream().map(stringObjectMap -> stringObjectMap.get("name")).collect(toSet()))
				.containsSubsequence(Collections.emptySet(), Sets.newHashSet("Group2", "Group1"));
	}

	private AuditReportConfig createConfig()
	{
		final Type principalGroup = Type.builder().withCode("UserGroup") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
				) //
				.build();

		final Type user = Type.builder().withCode("User") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
				) //
				.withRelationAttributes( //
						RelationAttribute.builder().withRelation("principalGroupRelation").withTargetType(principalGroup)
								.withResolvesBy( //
										ResolvesBy.builder().withResolverBeanId("manyToManyReferencesResolver").build())
								.build() //
				).build();//

		final AuditReportConfig reportConfig = AuditReportConfig.builder() //
				.withGivenRootType(user) //
				.withName("testConfig") //
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, principalGroup) //
				.build();

		return reportConfig;
	}
}
