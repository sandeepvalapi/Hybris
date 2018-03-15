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
package de.hybris.platform.directpersistence.audit.impl;


import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.constants.GeneratedCoreConstants.Relations;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecord;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.impl.DefaultAuditableSaver;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.testframework.seed.TestDataCreator;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Sets;


@Ignore
public class SaveAuditRecordsTest extends ServicelayerBaseTest
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
	private TypeService typeService;
	@Resource
	private TransactionTemplate transactionTemplate;

	private TestDataCreator creator;

	private final Map<String, PropertyConfigSwitcher> auditedTypes = new HashMap<>();
	private final PropertyConfigSwitcher auditForAllTypes = new PropertyConfigSwitcher("auditing.alltypes.enabled");
	private final PropertyConfigSwitcher persistenceLegacyMode = new PropertyConfigSwitcher(
			PersistenceUtils.PERSISTENCE_LEGACY_MODE);


	private TestDataCreator testDataCreator()
	{
		return new TestDataCreator(modelService);
	}


	@Before
	public void setUp() throws Exception
	{
		enableAuditingForTypes(UserModel._TYPECODE, Relations.PRINCIPALGROUPRELATION);
		disableAuditingForTypes(TitleModel._TYPECODE);
		creator = new TestDataCreator(modelService);
	}

	@After
	public void tearDown() throws Exception
	{

		resetAuditConfiguration();
		auditForAllTypes.switchBackToDefault();
		persistenceLegacyMode.switchBackToDefault();
	}


	@Test
	public void shouldRemoveAuditRecordsForTypeAndPk() throws Exception
	{
		//given
		final UserModel ronald = creator.createUser("ronald", "Ronald Reagan");
		final UserModel margaret = creator.createUser("margaret", "Margaret Thatcher");
		assertThat(readAuditGateway.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(ronald.getPk()).build())
				.collect(toList())).hasSize(1);
		assertThat(readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(margaret.getPk()).build()).collect(toList()))
						.hasSize(1);

		// when
		writeAuditGateway.removeAuditRecordsForType(UserModel._TYPECODE, ronald.getPk());

		// then
		assertThat(readAuditGateway.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(ronald.getPk()).build())
				.collect(toList())).isEmpty();
		assertThat(readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(margaret.getPk()).build()).collect(toList()))
						.hasSize(1);
	}

	@Test
	public void shouldNotCreateLogsForRollback()
	{
		Transaction.current().begin();
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		Transaction.current().rollback();
		final List<AuditRecord> records = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		assertThat(records).hasSize(0);
	}

	@Test
	public void shouldCreateTwoRecordsOneInNoTransactionOneInTransactionScope() throws Exception
	{
		//given
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		Transaction.current().begin();

		final User userJalo = JaloSession.getCurrentSession().getItem(user.getPk());
		userJalo.setName("Changed1");
		modelService.refresh(user);
		userJalo.setName("Changed2");
		user.setName("Changed3Transaction");
		modelService.save(user);
		Transaction.current().commit();


		//when
		final List<AuditRecord> unsortedAuditRecords = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		final List<AuditRecord> records = AuditTestHelper.sortRecords(unsortedAuditRecords);

		//then
		assertThat(records) //
				.extracting(AuditRecord::getAuditType, AuditRecord::getType,
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("name")) //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CREATION, UserModel._TYPECODE, "Ronald Reagan"), //
						tuple(AuditType.MODIFICATION, UserModel._TYPECODE, "Changed3Transaction"));
	}

	@Test
	public void shouldCreatedAuditForCreation() throws Exception
	{
		// given
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");

		// when
		final List<AuditRecord> records = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());

		// then
		assertThat(records).hasSize(1);

		final AuditRecord record1 = records.get(0);
		assertThat(record1.getPk()).isEqualTo(user.getPk());
		assertThat(record1.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record1.getType()).isEqualTo("User");
		assertThat(record1.getAuditType()).isEqualTo(AuditType.CREATION);
		assertThat(record1.getAttributesBeforeOperation()).isEmpty();
		assertThat(record1.getAttributesAfterOperation()).isNotEmpty();
		assertThat(record1.getChangingUser()).isNotEmpty();
		assertThat(record1.getContext()).doesNotContainKey(DefaultAuditableSaver.ACTING_USER);
	}

	@Test
	public void shouldNotAuditItemWhichIsNotConfigured() throws Exception
	{
		// given
		auditForAllTypes.switchToValue("false");
		final TitleModel title = creator.createTitle("st", "Some Title");

		// when
		final List<AuditRecord> records = readAuditGateway
				.search(AuditSearchQuery.forType(TitleModel._TYPECODE).withPkSearchRules(title.getPk()).build()).collect(toList());

		// then
		assertThat(records).isEmpty();
	}

	@Test
	public void shouldOnlyAuditItemsThatAreConfigured() throws Exception
	{
		// given
		Transaction.current().begin();
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		//final TitleModel title = creator.createTitle("st", "Some Title");
		Transaction.current().commit();

		// when
		final List<AuditRecord> records = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());

		// then
		assertThat(records).hasSize(1);

		final AuditRecord record1 = records.get(0);
		assertThat(record1.getPk()).isEqualTo(user.getPk());
		assertThat(record1.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record1.getType()).isEqualTo("User");
		assertThat(record1.getAuditType()).isEqualTo(AuditType.CREATION);
		assertThat(record1.getAttributesBeforeOperation()).isEmpty();
		assertThat(record1.getAttributesAfterOperation()).isNotEmpty();
	}

	@Test
	public void shouldCreateAuditForCreationAndModification() throws Exception
	{
		// given
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		user.setDescription("President of United States");
		modelService.save(user);
		// when
		final List<AuditRecord> unsortedRecords = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		final List<AuditRecord> records = AuditTestHelper.sortRecords(unsortedRecords);

		// then
		assertThat(records).hasSize(2);

		final AuditRecord record1 = records.get(0);
		assertThat(record1.getPk()).isEqualTo(user.getPk());
		assertThat(record1.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record1.getType()).isEqualTo("User");
		assertThat(record1.getAuditType()).isEqualTo(AuditType.CREATION);
		assertThat(record1.getAttributesBeforeOperation()).isEmpty();
		assertThat(record1.getAttributesAfterOperation()).isNotEmpty();

		final AuditRecord record2 = records.get(1);
		assertThat(record2.getPk()).isEqualTo(user.getPk());
		assertThat(record2.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record2.getType()).isEqualTo("User");
		assertThat(record2.getAuditType()).isEqualTo(AuditType.MODIFICATION);
		assertThat(record2.getAttributesBeforeOperation()).isNotEmpty();
		assertThat(record2.getAttributesAfterOperation()).isNotEmpty();
	}

	@Test
	public void shouldCreateAuditForCreationModificationAndDeletion() throws Exception
	{
		// given
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		user.setDescription("President of United States");
		modelService.save(user);
		modelService.remove(user);
		// when
		final List<AuditRecord> unsortedRecords = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		final List<AuditRecord> records = AuditTestHelper.sortRecords(unsortedRecords);

		// then
		assertThat(records).hasSize(3);
		records.sort(Comparator.comparing(AuditRecord::getTimestamp));

		final AuditRecord record1 = records.get(0);
		assertThat(record1.getPk()).isEqualTo(user.getPk());
		assertThat(record1.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record1.getType()).isEqualTo("User");
		assertThat(record1.getAuditType()).isEqualTo(AuditType.CREATION);
		assertThat(record1.getAttributesBeforeOperation()).isEmpty();
		assertThat(record1.getAttributesAfterOperation()).isNotEmpty();

		final AuditRecord record2 = records.get(1);
		assertThat(record2.getPk()).isEqualTo(user.getPk());
		assertThat(record2.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record2.getType()).isEqualTo("User");
		assertThat(record2.getAuditType()).isEqualTo(AuditType.MODIFICATION);
		assertThat(record2.getAttributesBeforeOperation()).isNotEmpty();
		assertThat(record2.getAttributesAfterOperation()).isNotEmpty();

		final AuditRecord record3 = records.get(2);
		assertThat(record3.getPk()).isEqualTo(user.getPk());
		assertThat(record3.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record3.getType()).isEqualTo("User");
		assertThat(record3.getAuditType()).isEqualTo(AuditType.DELETION);
		assertThat(record3.getAttributesBeforeOperation()).isNotEmpty();
		assertThat(record3.getAttributesAfterOperation()).isEmpty();
	}

	@Test
	public void shouldCreateOneAuditRecordInTransaction() throws Exception
	{
		// given
		Transaction.current().begin();
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		user.setDescription("President of United States");
		modelService.save(user);
		Transaction.current().commit();
		// when
		final List<AuditRecord> records = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		// then
		assertThat(records).hasSize(1);

	}

	@Test
	public void shouldCreateTwoRecordWithAndWithoutTransactions() throws Exception
	{
		// given
		Transaction.current().begin();
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		user.setDescription("President of United States");
		Transaction.current().begin();
		modelService.save(user);
		Transaction.current().begin();
		user.setDescription("President of Russia");
		modelService.save(user);
		Transaction.current().commit();
		Transaction.current().commit();
		Transaction.current().commit();
		modelService.remove(user);

		// when
		final List<AuditRecord> unsortedRecords = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		final List<AuditRecord> records = AuditTestHelper.sortRecords(unsortedRecords);

		// then
		assertThat(records).hasSize(2);
		final AuditRecord record1 = records.get(0);
		assertThat(record1.getPk()).isEqualTo(user.getPk());
		assertThat(record1.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record1.getType()).isEqualTo("User");
		assertThat(record1.getAuditType()).isEqualTo(AuditType.CREATION);
		assertThat(record1.getAttributesBeforeOperation()).isEmpty();
		assertThat(record1.getAttributesAfterOperation()).isNotEmpty();
		assertThat(record1.getAttributesAfterOperation().get("description")).isEqualTo("President of Russia");

		final AuditRecord record2 = records.get(1);
		assertThat(record2.getPk()).isEqualTo(user.getPk());
		assertThat(record2.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record2.getType()).isEqualTo("User");
		assertThat(record2.getAuditType()).isEqualTo(AuditType.DELETION);
		assertThat(record2.getAttributesBeforeOperation()).isNotEmpty();
		assertThat(record2.getAttributesAfterOperation()).isEmpty();

	}

	@Test
	public void shouldCreateTwoRecordWithAndWithoutTransactions2() throws Exception
	{
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		// given
		Transaction.current().begin();
		user.setDescription("President of United States");
		modelService.save(user);
		user.setDescription("President of Russia");
		modelService.save(user);
		modelService.remove(user);

		Transaction.current().commit();

		// when
		final List<AuditRecord> unsortedRecords = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		final List<AuditRecord> records = AuditTestHelper.sortRecords(unsortedRecords);

		// then
		assertThat(records).hasSize(2);
		final AuditRecord record1 = records.get(0);
		assertThat(record1.getPk()).isEqualTo(user.getPk());
		assertThat(record1.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record1.getType()).isEqualTo("User");
		assertThat(record1.getAuditType()).isEqualTo(AuditType.CREATION);
		assertThat(record1.getAttributesBeforeOperation()).isEmpty();
		assertThat(record1.getAttributesAfterOperation()).isNotEmpty();

		final AuditRecord record2 = records.get(1);
		assertThat(record2.getPk()).isEqualTo(user.getPk());
		assertThat(record2.getTypePk()).isEqualTo(typeService.getComposedTypeForCode("User").getPk());
		assertThat(record2.getType()).isEqualTo("User");
		assertThat(record2.getAuditType()).isEqualTo(AuditType.DELETION);
		assertThat(record2.getAttributesBeforeOperation()).isNotEmpty();
		assertThat(record2.getAttributesAfterOperation()).isEmpty();
	}

	@Test
	public void shouldNotCreateRecordForNestedTransactions() throws Exception
	{
		// given
		Transaction.current().begin();
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");
		user.setDescription("President of United States");
		Transaction.current().begin();
		modelService.save(user);
		Transaction.current().begin();
		modelService.remove(user);
		Transaction.current().commit();
		Transaction.current().commit();
		Transaction.current().commit();
		// when
		final List<AuditRecord> records = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		// then
		assertThat(records).hasSize(0);

	}

	@Test
	public void shouldRemoveRecordsFromCacheAndNotCreateAuditRecord() throws Exception
	{
		final UserModel user = creator.createUser("ronald", "Ronald Reagan");

		Transaction.current().begin();
		modelService.remove(user);

		writeAuditGateway.removeAuditRecordsForType(UserModel._TYPECODE, user.getPk());
		Transaction.current().commit();
		// when
		final List<AuditRecord> records = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(user.getPk()).build()).collect(toList());
		// then
		assertThat(records).hasSize(0);
	}


	@Test
	public void shouldCreateAuditForLink() throws Exception
	{
		//given
		final UserModel user = creator.createUser("user1", "user1");
		final UserGroupModel userGroup1 = creator.createUserGroup("userGroup1", "userGroup1");
		final UserGroupModel userGroup2 = creator.createUserGroup("userGroup2", "userGroup2");
		user.setGroups(Sets.newHashSet(userGroup1, userGroup2));
		modelService.save(user);
		user.setGroups(Sets.newHashSet(userGroup1));
		modelService.save(user);

		//when
		final List<LinkAuditRecord> unsortedRecords = readAuditGateway
				.<LinkAuditRecord> search(AuditSearchQuery.forLink("PrincipalGroupRelation").withPk(user.getPk()).buildForSource())
				.collect(toList());
		final List<LinkAuditRecord> records = AuditTestHelper.sortRecords(unsortedRecords);

		//then
		assertThat(records).hasSize(3);
		assertThat(records) //
				.extracting(LinkAuditRecord::getAuditType, LinkAuditRecord::getSourcePk, LinkAuditRecord::getTargetPk) //
				.contains( //
						tuple(AuditType.CREATION, user.getPk(), userGroup1.getPk()),
						tuple(AuditType.CREATION, user.getPk(), userGroup2.getPk()),
						tuple(AuditType.DELETION, user.getPk(), userGroup2.getPk()));
	}

	@Test
	public void shouldCreateAuditForLinkWithRemoveAllLinks() throws Exception
	{
		//given
		final UserModel user = creator.createUser("user1", "user1");
		final UserGroupModel userGroup1 = creator.createUserGroup("userGroup1", "userGroup1");
		final UserGroupModel userGroup2 = creator.createUserGroup("userGroup2", "userGroup2");
		user.setGroups(Sets.newHashSet(userGroup1, userGroup2));
		modelService.save(user);
		user.setGroups(null);
		modelService.save(user);

		//when
		final List<LinkAuditRecord> unsortedRecords = readAuditGateway
				.<LinkAuditRecord> search(AuditSearchQuery.forLink("PrincipalGroupRelation").withPk(user.getPk()).buildForSource())
				.collect(toList());
		final List<LinkAuditRecord> records = AuditTestHelper.sortRecords(unsortedRecords);

		//then
		assertThat(records).hasSize(4);
		assertThat(records) //
				.extracting(LinkAuditRecord::getAuditType, LinkAuditRecord::getSourcePk, LinkAuditRecord::getTargetPk) //
				.contains( //
						tuple(AuditType.CREATION, user.getPk(), userGroup1.getPk()),
						tuple(AuditType.CREATION, user.getPk(), userGroup2.getPk()),
						tuple(AuditType.DELETION, user.getPk(), userGroup2.getPk()),
						tuple(AuditType.DELETION, user.getPk(), userGroup1.getPk()));
	}


	@Test
	public void shouldSetValuesBeforeFromStartOfTransaction() throws Exception
	{
		final UserModel user = testDataCreator().createUser("user", "Ronald Reagan");

		final UserModel ronald = transactionTemplate.execute(status -> {
			user.setDescription("foo");
			modelService.save(user);
			user.setDescription("baz");
			modelService.save(user);
			user.setDescription("bar");
			modelService.save(user);

			return user;
		});

		// when
		final List<AuditRecord> unsortedAuditRecords = readAuditGateway
				.search(AuditSearchQuery.forType(UserModel._TYPECODE).withPkSearchRules(ronald.getPk()).build()).collect(toList());
		final List<AuditRecord> auditRecords = AuditTestHelper.sortRecords(unsortedAuditRecords);

		// then
		assertThat(auditRecords).hasSize(2);
		assertThat(auditRecords) //
				.extracting(AuditRecord::getAuditType, ar -> ar.getAttributeBeforeOperation("description"))
				.contains(tuple(AuditType.CREATION, null), tuple(AuditType.MODIFICATION, null));

	}

	protected void enableAuditingForTypes(final String... types)
	{
		prepareAuditingForTypes(true, types);
	}

	private void disableAuditingForTypes(final String... types)
	{
		prepareAuditingForTypes(false, types);
	}

	protected void switchPersistenceLegacyMode(final boolean value)
	{
		persistenceLegacyMode.switchToValue(Boolean.valueOf(value).toString());
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
