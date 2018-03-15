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

import de.hybris.platform.audit.AbstractAuditTest;
import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.RelationAttribute;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.AuditType;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;


public class DefaultAuditRecordsProviderTest extends AbstractAuditTest
{

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		assumeAuditEnabled();

		creator.createLanguage("de", "German");
	}

	@Test
	public void shouldReturnAuditRecordsForConfigAndDataDefinedInMainSetup() throws Exception
	{
		// given
		final PK userPk = user1.getPk();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig)
				.withRootTypePk(userPk).build();

		// when
		final List<AuditRecord> records = auditRecordsProvider.getRecords(config).collect(toList());

		// then

		assertThat(records) //
				.extracting(AuditRecord::getPk, AuditRecord::getAuditType) //
				.containsSubsequence( //
						tuple(userPk, AuditType.CREATION), //
						tuple(title1.getPk(), AuditType.CREATION), //
						tuple(title2.getPk(), AuditType.CREATION), //
						tuple(address1.getPk(), AuditType.CREATION), //
						tuple(address2.getPk(), AuditType.CREATION), //
						tuple(address3.getPk(), AuditType.CREATION), //
						tuple(address1.getPk(), AuditType.MODIFICATION), //
						tuple(address2.getPk(), AuditType.MODIFICATION), //
						tuple(address3.getPk(), AuditType.MODIFICATION), //
						tuple(userPk, AuditType.MODIFICATION) //
				) //
				.containsOnlyOnce( // in any order
						tuple(userPk, AuditType.CURRENT), //
						tuple(title1.getPk(), AuditType.CURRENT), //
						tuple(title2.getPk(), AuditType.CURRENT), //
						tuple(address1.getPk(), AuditType.CURRENT), //
						tuple(address2.getPk(), AuditType.CURRENT), //
						tuple(address3.getPk(), AuditType.CURRENT) //
		);


		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(UserModel._TYPECODE)) //
				.allMatch(auditRecord -> auditRecord.getPk().equals(userPk)) //
				.extracting(AuditTestHelper::getAuditRecordsAttributes) //
				.extracting("uid", "name", "defaultpaymentaddress", "defaultshipmentaddress") //
				.containsSubsequence( //
						tuple("adam", "Adam", null, null), //
						tuple("adam", "Adam", address1.getPk(), address3.getPk()) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(AddressModel._TYPECODE)) //
				.filteredOn(auditRecord -> auditRecord.getPk().equals(address1.getPk())) //
				.extracting(AuditTestHelper::getAuditRecordsAttributes) //
				.extracting("town", "streetname", "title", "owner") //
				.containsSubsequence( //
						tuple("Sosnowiec", "Moniuszki", null, userPk), //
						tuple("Sosnowiec", "Moniuszki", title1.getPk(), userPk) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(AddressModel._TYPECODE)) //
				.filteredOn(auditRecord -> auditRecord.getPk().equals(address2.getPk())) //
				.extracting(AuditTestHelper::getAuditRecordsAttributes) //
				.extracting("town", "streetname", "title", "owner") //
				.containsSubsequence( //
						tuple("Tokyo", "Konnichiwa", null, userPk), //
						tuple("Tokyo", "Konnichiwa", title2.getPk(), userPk) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(AddressModel._TYPECODE)) //
				.filteredOn(auditRecord -> auditRecord.getPk().equals(address3.getPk())) //
				.extracting(AuditTestHelper::getAuditRecordsAttributes) //
				.extracting("town", "streetname", "title", "owner") //
				.containsSubsequence( //
						tuple("New York", "55th St.", null, userPk), //
						tuple("New York", "55th St.", title1.getPk(), userPk) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(TitleModel._TYPECODE)) //
				.extracting( //
						AuditRecord::getPk, //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), //
						ar -> ((Map) AuditTestHelper.getAuditRecordsAttributes(ar, "en").get("name")).get("en") //
				) //
				.containsOnly( //
						tuple(title1.getPk(), "Mr", "Mister"), tuple(title2.getPk(), "Engr.", "Engineer"));
	}

	@Test
	public void shouldReturnAuditRecordsForConfigAndDataDefinedInMainSetup_ModifiedAddressRefInUser() throws Exception
	{
		// given
		user1.setDefaultShipmentAddress(null);
		modelService.save(user1);
		final PK userPk = user1.getPk();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig)
				.withRootTypePk(userPk).build();

		// when
		final List<AuditRecord> records = auditRecordsProvider.getRecords(config).collect(toList());

		// then
		assertThat(records) //
				.extracting(AuditRecord::getPk, AuditRecord::getAuditType) //
				.containsSubsequence( //
						tuple(userPk, AuditType.CREATION), //
						tuple(title1.getPk(), AuditType.CREATION), //
						tuple(title2.getPk(), AuditType.CREATION), //
						tuple(address1.getPk(), AuditType.CREATION), //
						tuple(address2.getPk(), AuditType.CREATION), //
						tuple(address3.getPk(), AuditType.CREATION), //
						tuple(address1.getPk(), AuditType.MODIFICATION), //
						tuple(address2.getPk(), AuditType.MODIFICATION), //
						tuple(address3.getPk(), AuditType.MODIFICATION), //
						tuple(userPk, AuditType.MODIFICATION), //
						tuple(userPk, AuditType.MODIFICATION) //
				) //
				.containsOnlyOnce( // in any order
						tuple(userPk, AuditType.CURRENT), //
						tuple(title1.getPk(), AuditType.CURRENT), //
						tuple(title2.getPk(), AuditType.CURRENT), //
						tuple(address1.getPk(), AuditType.CURRENT), //
						tuple(address2.getPk(), AuditType.CURRENT), //
						tuple(address3.getPk(), AuditType.CURRENT) //
		);


		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(UserModel._TYPECODE)) //
				.allMatch(auditRecord -> auditRecord.getPk().equals(userPk)) //
				.extracting(AuditTestHelper::getAuditRecordsAttributes) //
				.extracting("uid", "name", "defaultpaymentaddress", "defaultshipmentaddress") //
				.containsSubsequence( //
						tuple("adam", "Adam", null, null), //
						tuple("adam", "Adam", address1.getPk(), address3.getPk()), //
						tuple("adam", "Adam", address1.getPk(), null) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(AddressModel._TYPECODE)) //
				.filteredOn(auditRecord -> auditRecord.getPk().equals(address1.getPk())) //
				.extracting(AuditTestHelper::getAuditRecordsAttributes) //
				.extracting("town", "streetname", "title", "owner") //
				.containsSubsequence( //
						tuple("Sosnowiec", "Moniuszki", null, userPk), //
						tuple("Sosnowiec", "Moniuszki", title1.getPk(), userPk) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(AddressModel._TYPECODE)) //
				.filteredOn(auditRecord -> auditRecord.getPk().equals(address2.getPk())) //
				.extracting(AuditTestHelper::getAuditRecordsAttributes) //
				.extracting("town", "streetname", "title", "owner") //
				.containsSubsequence( //
						tuple("Tokyo", "Konnichiwa", null, userPk), //
						tuple("Tokyo", "Konnichiwa", title2.getPk(), userPk) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(AddressModel._TYPECODE)) //
				.filteredOn(auditRecord -> auditRecord.getPk().equals(address3.getPk())) //
				.extracting(AuditTestHelper::getAuditRecordsAttributes) //
				.extracting("town", "streetname", "title", "owner") //
				.containsSubsequence( //
						tuple("New York", "55th St.", null, userPk), //
						tuple("New York", "55th St.", title1.getPk(), userPk) //
		);

		assertThat(records) //
				.filteredOn(ar -> ar.getType().equals(TitleModel._TYPECODE)) //
				.extracting( //
						AuditRecord::getPk, //
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), //
						ar -> ((Map) AuditTestHelper.getAuditRecordsAttributes(ar, "en").get("name")).get("en") //
				) //
				.containsOnly( //
						tuple(title1.getPk(), "Mr", "Mister"), tuple(title2.getPk(), "Engr.", "Engineer"));

	}

	@Test
	public void shouldProperlyHandleCyclicAuditConfiguration() throws Exception
	{
		// given
		final PK userPk = user1.getPk();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder()
				.withConfig(loadConfigFromFile("audit.test/user-address-cyclic-audit.xml", "ItemsConfig")).withRootTypePk(userPk).build();

		// when
		final List<AuditRecord> records = auditRecordsProvider.getRecords(config).collect(toList());

		// then
		assertThat(records).isNotEmpty();
	}

	@Test
	public void shouldNotStoreAuditRecordsWhenThereAreNoSignificantChanges() throws Exception
	{
		//given
		final UserModel user = creator.createUser(UUID.randomUUID().toString(), "user");
		final UserGroupModel group1 = creator.createUserGroup(UUID.randomUUID().toString(), "group1");
		final UserGroupModel group2 = creator.createUserGroup(UUID.randomUUID().toString(), "group2");
		modelService.save(group1);

		user.setGroups(Sets.newHashSet(group1, group2));
		modelService.saveAll(user, group1, group2);

		user.setGroups(Sets.newHashSet(group1));
		modelService.saveAll(user, group1, group2);

		final Type groupType = Type.builder().withCode(UserGroupModel._TYPECODE).build();
		final Type userType = Type.builder().withCode(UserModel._TYPECODE)
				.withRelationAttributes(RelationAttribute.builder().withRelation("principalGroupRelation").withTargetType(groupType)
						.withResolvesBy(ResolvesBy.builder().withResolverBeanId("manyToManyReferencesResolver").build()).build())
				.build();

		final AuditReportConfig reportConfig = AuditReportConfig.builder().withGivenRootType(userType).withName("testConfig")
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(userType).build();

		// when
		final List<AuditRecord> userRecords = auditRecordsProvider
				.getRecords(TypeAuditReportConfig.builder().withConfig(reportConfig).withRootTypePk(user.getPk()).build())
				.collect(toList());

		// then
		assertThat(userRecords).isNotEmpty();

		userRecords.stream().filter(auditRecord -> auditRecord.getAuditType() == AuditType.MODIFICATION).forEach(auditRecord -> {
			final JsonNode payloadBefore = toJsonAuditPayloadWithoutModifiedTime(auditRecord, "auditPayload");
			final JsonNode payloadAfter = toJsonAuditPayloadWithoutModifiedTime(auditRecord, "auditPayloadAfterOperation");

			assertThat(payloadBefore).isNotEqualTo(payloadAfter);
		});
	}

	@Test
	public void shouldNotStoreAuditRecordsWhenThereAreNoSignificantChangesWithLocalizedAttributes() throws Exception
	{
		// given
		final UserModel user = creator.createUser(UUID.randomUUID().toString(), "user");
		final UserGroupModel group1 = creator.createUserGroup(UUID.randomUUID().toString(), "group1");
		final UserGroupModel group2 = creator.createUserGroup(UUID.randomUUID().toString(), "group2");
		group1.setLocName("groupEN", Locale.ENGLISH);
		group1.setLocName("groupDE", Locale.GERMAN);
		modelService.save(group1);

		user.setGroups(Sets.newHashSet(group1, group2));
		modelService.saveAll(user, group1, group2);

		user.setGroups(Sets.newHashSet(group1));
		modelService.saveAll(user, group1, group2);

		final Type groupType = Type.builder().withCode(UserGroupModel._TYPECODE).build();
		final Type userType = Type.builder().withCode(UserModel._TYPECODE)
				.withRelationAttributes(RelationAttribute.builder().withRelation("principalGroupRelation").withTargetType(groupType)
						.withResolvesBy(ResolvesBy.builder().withResolverBeanId("manyToManyReferencesResolver").build()).build())
				.build();

		final AuditReportConfig reportConfig = AuditReportConfig.builder().withGivenRootType(groupType).withName("testConfig")
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(groupType).build();

		// when
		final List<AuditRecord> groupRecords = auditRecordsProvider
				.getRecords(TypeAuditReportConfig.builder().withConfig(reportConfig).withRootTypePk(group1.getPk()).build())
				.collect(toList());

		// then
		assertThat(groupRecords).isNotEmpty();

		groupRecords.stream().filter(auditRecord -> auditRecord.getAuditType() == AuditType.MODIFICATION).forEach(auditRecord -> {

			final JsonNode payloadBefore = toJsonAuditPayloadWithoutModifiedTime(auditRecord, "auditPayload");
			final JsonNode payloadAfter = toJsonAuditPayloadWithoutModifiedTime(auditRecord, "auditPayloadAfterOperation");

			assertThat(payloadBefore).isNotEqualTo(payloadAfter);
		});
	}

	private JsonNode toJsonAuditPayloadWithoutModifiedTime(final Object input, final String payloadAttributeName)
	{
		final JsonNode jsonNode = toJson(input);
		final JsonNode auditPayload = jsonNode.get(payloadAttributeName);
		((ObjectNode) auditPayload.get("attributes").get("modifiedtime")).put("value", 0);
		return auditPayload;
	}

	private JsonNode toJson(final Object input)
	{
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ss.SSSZ"));

		return mapper.valueToTree(input);
	}
}
