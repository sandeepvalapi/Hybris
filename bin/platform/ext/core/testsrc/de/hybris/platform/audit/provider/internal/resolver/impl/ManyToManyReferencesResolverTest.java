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
package de.hybris.platform.audit.provider.internal.resolver.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.demo.AuditTestConfigManager;
import de.hybris.platform.audit.internal.config.AtomicAttribute;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.RelationAttribute;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.provider.AuditRecordsProvider;
import de.hybris.platform.constants.GeneratedCoreConstants.Relations;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;


@Ignore
public class ManyToManyReferencesResolverTest extends ServicelayerBaseTest implements AuditableTest
{

	@Resource
	protected ModelService modelService;

	private AuditTestConfigManager auditTestConfigManager;

	@Resource
	protected AuditRecordsProvider auditRecordsProvider;

	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;

	private AuditTestHelper auditTestHelper;

	private final PropertyConfigSwitcher auditAllTypesEnabledProperty = new PropertyConfigSwitcher("auditing.alltypes.enabled");

	private final PropertyConfigSwitcher persistenceLegacyModeSwitcher = new PropertyConfigSwitcher(
			PersistenceUtils.PERSISTENCE_LEGACY_MODE);

	@Before
	public void setUp() throws Exception
	{
		assumeAuditEnabled();
		auditTestHelper = new AuditTestHelper();
		auditTestHelper.clearAuditDataForTypes(UserModel._TYPECODE, UserGroupModel._TYPECODE, Relations.PRINCIPALGROUPRELATION);

		auditTestConfigManager = new AuditTestConfigManager(auditEnablementService);

	}

	protected void switchPersistenceLegacyMode(final boolean value)
	{
		persistenceLegacyModeSwitcher.switchToValue(Boolean.valueOf(value).toString());
	}

	@After
	public void tearDown() throws Exception
	{
		auditTestHelper.removeCreatedItems();
		auditTestHelper.clearAuditDataForTypes(UserModel._TYPECODE, UserGroupModel._TYPECODE, Relations.PRINCIPALGROUPRELATION);
		auditTestConfigManager.resetAuditConfiguration();
		auditAllTypesEnabledProperty.switchBackToDefault();
		persistenceLegacyModeSwitcher.switchBackToDefault();
	}


	@Test
	public void shouldProvideAuditRecordsForUserBasedOnCurrentModel() throws Exception
	{
		auditAllTypesEnabledProperty.switchToValue("false");
		disableAuditing(UserModel._TYPECODE, UserGroupModel._TYPECODE, Relations.PRINCIPALGROUPRELATION);

		final UserModel user = createUser("user1a", "user1");
		final UserGroupModel userGroup1 = createUserGroup("userGroup1a", "userGroup1");

		user.setGroups(Sets.newHashSet(userGroup1));
		modelService.save(user);

		final List<AuditRecord> auditRecords = auditRecordsProvider.getRecords(TypeAuditReportConfig.builder()
				.withConfig(createConfigForUserWithManyToManyAttribute()).withRootTypePk(user.getPk()).build())
				.collect(Collectors.toList());


		assertThat(auditRecords.stream().filter(AuditRecord::isLink)) //
				.extracting("auditType", "parentPk", "childPk")
				.containsExactlyInAnyOrder(tuple(AuditType.CURRENT, user.getPk(), userGroup1.getPk()));

		assertThat(auditRecords.stream().filter(auditRecord -> !auditRecord.isLink())) //
				.extracting("auditType", "type", "pk") //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CURRENT, UserModel._TYPECODE, user.getPk()), //
						tuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup1.getPk()));

	}


	@Test
	public void shouldProvideAuditRecordsForUserWithDeleteBasedOnCurrentModel() throws Exception
	{
		auditAllTypesEnabledProperty.switchToValue("false");
		disableAuditing(UserModel._TYPECODE, UserGroupModel._TYPECODE, Relations.PRINCIPALGROUPRELATION);

		final UserModel user = createUser("user1b", "user1");
		final UserGroupModel userGroup1 = createUserGroup("userGroup1b", "userGroup1");
		final UserGroupModel userGroup2 = createUserGroup("userGroup2b", "userGroup2");

		user.setGroups(Sets.newHashSet(userGroup1, userGroup2));
		modelService.save(user);

		user.setGroups(Sets.newHashSet(userGroup1));
		modelService.save(user);

		final List<AuditRecord> collect = auditRecordsProvider.getRecords(TypeAuditReportConfig.builder()
				.withConfig(createConfigForUserWithManyToManyAttribute()).withRootTypePk(user.getPk()).build())
				.collect(Collectors.toList());

		assertThat(collect.stream()).filteredOn(AuditRecord::isLink) //
				.extracting("auditType", "parentPk", "childPk") //
				.containsExactlyInAnyOrder(tuple(AuditType.CURRENT, user.getPk(), userGroup1.getPk()));

		assertThat(collect.stream()).filteredOn(auditRecord -> !auditRecord.isLink()) //
				.extracting("auditType", "type", "pk") //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CURRENT, UserModel._TYPECODE, user.getPk()), //
						tuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup1.getPk()));
	}


	@Test
	public void shouldProvideAuditRecordsForGroupWithDeleteBasedOnCurrentModel() throws Exception
	{
		auditAllTypesEnabledProperty.switchToValue("false");
		disableAuditing(UserModel._TYPECODE, UserGroupModel._TYPECODE, Relations.PRINCIPALGROUPRELATION);

		final UserModel user = createUser("user1c", "user1");
		final UserGroupModel userGroup1 = createUserGroup("userGroup1c", "userGroup1");
		final UserGroupModel userGroup2 = createUserGroup("userGroup2c", "userGroup2");

		user.setGroups(Sets.newHashSet(userGroup1, userGroup2));
		modelService.save(user);

		user.setGroups(Sets.newHashSet(userGroup1));
		modelService.save(user);

		final List<AuditRecord> collect = auditRecordsProvider.getRecords(TypeAuditReportConfig.builder()
				.withConfig(createConfigForGroupWithManyToManyAttribute()).withRootTypePk(userGroup1.getPk()).build())
				.collect(Collectors.toList());

		assertThat(collect.stream()).filteredOn(AuditRecord::isLink) //
				.extracting("auditType", "parentPk", "childPk") //
				.containsExactlyInAnyOrder(tuple(AuditType.CURRENT, userGroup1.getPk(), user.getPk()));

		assertThat(collect.stream()).filteredOn(auditRecord -> !auditRecord.isLink()) //
				.extracting("auditType", "type", "pk") //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CURRENT, UserModel._TYPECODE, user.getPk()), //
						tuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup1.getPk()));
	}

	@Test
	public void shouldProvideAuditRecordsForDeletedUser() throws Exception
	{
		enableAuditing(UserModel._TYPECODE, UserGroupModel._TYPECODE, "PrincipalGroupRelation");

		final UserModel user = createUser("user1g", "user1");
		// CREATE user
		final UserGroupModel userGroup1 = createUserGroup("userGroup1g", "userGroup1");
		// CREATE group1
		final UserGroupModel userGroup2 = createUserGroup("userGroup2g", "userGroup2");
		//CREATE group2

		user.setGroups(Sets.newHashSet(userGroup1, userGroup2));
		modelService.save(user);
		//CREATE link user->group1, CREATE link user->group2, MODIFY user

		user.setGroups(Sets.newHashSet(userGroup1));
		modelService.save(user);
		//DELETE user->group2, MODIFY user

		modelService.remove(user);
		//DELETE user, user->group1,

		//still in DB (CURRENT): group1, group2

		final List<AuditRecord> collect = auditRecordsProvider.getRecords(TypeAuditReportConfig.builder()
				.withConfig(createConfigForUserWithManyToManyAttribute()).withRootTypePk(user.getPk()).build())
				.collect(Collectors.toList());


		assertThat(collect.stream()).filteredOn(AuditRecord::isLink) //
				.extracting("auditType", "parentPk", "childPk") //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CREATION, user.getPk(), userGroup1.getPk()), //
						tuple(AuditType.CREATION, user.getPk(), userGroup2.getPk()), //
						tuple(AuditType.DELETION, user.getPk(), userGroup2.getPk()), //
						tuple(AuditType.DELETION, user.getPk(), userGroup1.getPk()) //
		);

		assertThat(collect.stream()).filteredOn(auditRecord -> !auditRecord.isLink()) //
				.extracting("auditType", "type", "pk") //
				.containsExactlyInAnyOrder( //
						TuplesBuilder.tuplesBuilder() //
								.addTuple(AuditType.CREATION, UserModel._TYPECODE, user.getPk()) //
								.addTuple(AuditType.CREATION, UserGroupModel._TYPECODE, userGroup1.getPk()) //
								.addTuple(AuditType.CREATION, UserGroupModel._TYPECODE, userGroup2.getPk()) //
								.addTuple(AuditType.DELETION, UserModel._TYPECODE, user.getPk()) //
								.addTuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup1.getPk()) //
								.addTuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup2.getPk()) //
								.build()

		);
	}

	@Test
	public void shouldProvideAuditRecordsForUserWithDeleteBasedOnCurrentModelAndAudit() throws Exception
	{
		enableAuditing(UserModel._TYPECODE, UserGroupModel._TYPECODE, "PrincipalGroupRelation");

		final UserModel user = createUser("user1f", "user1");
		// CREATE user
		final UserGroupModel userGroup1 = createUserGroup("userGroup1f", "userGroup1");
		// CREATE group1
		final UserGroupModel userGroup2 = createUserGroup("userGroup2f", "userGroup2");
		//CREATE group2

		user.setGroups(Sets.newHashSet(userGroup1, userGroup2));
		modelService.save(user);
		//CREATE link user->group1, CREATE link user->group2, MODIFY user

		user.setGroups(Sets.newHashSet(userGroup1));
		modelService.save(user);
		//DELETE user->group2, MODIFY user

		//still in DB (CURRENT): user, link user->group1, group1, group2

		final List<AuditRecord> collect = auditRecordsProvider.getRecords(TypeAuditReportConfig.builder()
				.withConfig(createConfigForUserWithManyToManyAttribute()).withRootTypePk(user.getPk()).build())
				.collect(Collectors.toList());


		assertThat(collect.stream()).filteredOn(AuditRecord::isLink) //
				.extracting("auditType", "parentPk", "childPk") //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CREATION, user.getPk(), userGroup1.getPk()),
						tuple(AuditType.CURRENT, user.getPk(), userGroup1.getPk()),
						tuple(AuditType.CREATION, user.getPk(), userGroup2.getPk()),
						tuple(AuditType.DELETION, user.getPk(), userGroup2.getPk()));

		assertThat(collect.stream()).filteredOn(auditRecord -> !auditRecord.isLink()) //
				.extracting("auditType", "type", "pk") //
				.containsExactlyInAnyOrder( //
						TuplesBuilder.tuplesBuilder() //
								.addTuple(AuditType.CREATION, UserModel._TYPECODE, user.getPk()) //
								.addTuple(AuditType.CREATION, UserGroupModel._TYPECODE, userGroup1.getPk()) //
								.addTuple(AuditType.CREATION, UserGroupModel._TYPECODE, userGroup2.getPk()) //
								.addTuple(AuditType.CURRENT, UserModel._TYPECODE, user.getPk()) //
								.addTuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup1.getPk()) //
								.addTuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup2.getPk()) //
								.build()

		);
	}

	@Test
	public void shouldProvideAuditRecordsForUserBasedOnCurrentModelAndAudit() throws Exception
	{
		enableAuditing(UserModel._TYPECODE, UserGroupModel._TYPECODE);

		final UserModel user = createUser("user1d", "user1");
		final UserGroupModel userGroup1 = createUserGroup("userGroup1d", "userGroup1");

		user.setGroups(Sets.newHashSet(userGroup1));
		modelService.save(user);

		final List<AuditRecord> auditRecords = auditRecordsProvider.getRecords(TypeAuditReportConfig.builder()
				.withConfig(createConfigForUserWithManyToManyAttribute()).withRootTypePk(user.getPk()).build())
				.collect(Collectors.toList());


		assertThat(auditRecords.stream()).filteredOn(AuditRecord::isLink) //
				.extracting("auditType", "parentPk", "childPk") //
				.containsExactlyInAnyOrder(tuple(AuditType.CREATION, user.getPk(), userGroup1.getPk()), //
						tuple(AuditType.CURRENT, user.getPk(), userGroup1.getPk()));

		assertThat(auditRecords.stream()).filteredOn(auditRecord -> !auditRecord.isLink()) //
				.extracting("auditType", "type", "pk") //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CREATION, UserModel._TYPECODE, user.getPk()),
						tuple(AuditType.CREATION, UserGroupModel._TYPECODE, userGroup1.getPk()),
						tuple(AuditType.CURRENT, UserModel._TYPECODE, user.getPk()), //
						tuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup1.getPk()) //
		);

	}

	@Test
	public void shouldProvideAuditRecordsForGroupBasedOnCurrentModelAndAudit() throws Exception
	{
		enableAuditing(UserModel._TYPECODE, UserGroupModel._TYPECODE, "PrincipalGroupRelation");

		final UserModel user = createUser("user1e", "user1");
		final UserGroupModel userGroup = createUserGroup("userGroup1e", "userGroup1");

		user.setGroups(Sets.newHashSet(userGroup));
		modelService.save(user);

		final List<AuditRecord> auditRecords = auditRecordsProvider.getRecords(TypeAuditReportConfig.builder()
				.withConfig(createConfigForGroupWithManyToManyAttribute()).withRootTypePk(userGroup.getPk()).build())
				.collect(Collectors.toList());


		assertThat(auditRecords.stream()).filteredOn(AuditRecord::isLink) //
				.extracting("auditType", "parentPk", "childPk") //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CURRENT, userGroup.getPk(), user.getPk()), //
						tuple(AuditType.CREATION, userGroup.getPk(), user.getPk()) //
		);

		assertThat(auditRecords.stream()).filteredOn(auditRecord -> !auditRecord.isLink()) //
				.extracting("auditType", "type", "pk") //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CREATION, UserModel._TYPECODE, user.getPk()),
						tuple(AuditType.CREATION, UserGroupModel._TYPECODE, userGroup.getPk()),
						tuple(AuditType.CURRENT, UserModel._TYPECODE, user.getPk()), //
						tuple(AuditType.CURRENT, UserGroupModel._TYPECODE, userGroup.getPk()) //
		);
	}


	protected void enableAuditing(final String... types)
	{
		auditTestConfigManager.enableAuditingForTypes(types);
	}

	private void disableAuditing(final String... types)
	{
		auditTestConfigManager.disableAuditingForTypes(types);
	}


	protected UserGroupModel createUserGroup(final String userGroupUid, final String userGroupName)
	{
		final UserGroupModel userGroup = auditTestHelper.createItem(UserGroupModel.class);
		userGroup.setUid(userGroupUid);
		userGroup.setName(userGroupName);
		modelService.save(userGroup);
		return userGroup;
	}

	protected UserModel createUser(final String userUid, final String userName)
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid(userUid);
		user.setName(userName);
		modelService.save(user);
		return user;
	}



	protected AuditReportConfig createConfigForUserWithManyToManyAttribute()
	{

		final Type userGroups = Type.builder().withCode(UserGroupModel._TYPECODE) //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier(UserGroupModel.UID).build(), //
						AtomicAttribute.builder().withQualifier(UserGroupModel.NAME).build()) //
				.build();


		final Type user = Type.builder().withCode(UserModel._TYPECODE) //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier(UserModel.UID).build(), //
						AtomicAttribute.builder().withQualifier(UserModel.NAME).build()) //
				.withRelationAttributes( //
						RelationAttribute.builder() //
								.withRelation("principalGroupRelation") //
								.withTargetType(userGroups) //
								.withResolvesBy( //
										ResolvesBy.builder() //
												.withResolverBeanId("manyToManyReferencesResolver") //
												.build() //
								) //
								.build() //
				).build();

		return AuditReportConfig.builder().withGivenRootType(user).withName("testConfig")
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, userGroups).build();
	}

	protected AuditReportConfig createConfigForGroupWithManyToManyAttribute()
	{

		final Type user = Type.builder().withCode(UserModel._TYPECODE) //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier(UserModel.UID).build(), //
						AtomicAttribute.builder().withQualifier(UserModel.NAME).build() //
				) //
				.build();

		final Type userGroups = Type.builder().withCode(UserGroupModel._TYPECODE) //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier(UserGroupModel.UID).build(), //
						AtomicAttribute.builder().withQualifier(UserGroupModel.NAME).build() //
				) //
				.withRelationAttributes( //
						RelationAttribute.builder() //
								.withRelation("principalGroupRelation") //
								.withTargetType(user) //
								.withResolvesBy( //
										ResolvesBy.builder() //
												.withResolverBeanId("manyToManyReferencesResolver") //
												.build() //
								) //
								.build() //
				) //
				.build();



		return AuditReportConfig.builder().withGivenRootType(userGroups).withName("testConfig")
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, userGroups).build();
	}


	protected static class TuplesBuilder
	{
		private final ImmutableList.Builder<Tuple> tuples = ImmutableList.builder();

		private TuplesBuilder()
		{
		}

		public TuplesBuilder addTupleMultiplied(final int multiplier, final Object... tupleParams)
		{
			this.tuples.addAll(Stream.generate(() -> tuple(tupleParams)).limit(multiplier).collect(Collectors.toList()));
			return this;
		}

		public TuplesBuilder addTuple(final Object... tupleParams)
		{
			this.tuples.add(tuple(tupleParams));
			return this;
		}

		public Tuple[] build()
		{
			final ImmutableList<Tuple> list = tuples.build();
			return list.toArray(new Tuple[list.size()]);
		}

		public static TuplesBuilder tuplesBuilder()
		{
			return new TuplesBuilder();
		}
	}

}

