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
package de.hybris.platform.persistence.audit;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.read.SLDItemDAO;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.testframework.seed.TestDataCreator;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AuditableSaverTest extends ServicelayerBaseTest implements AuditableTest
{
	@Resource
	private AuditableSaver auditableSaver;

	@Resource
	private SLDItemDAO sldItemDAO;


	@Resource
	private ModelService modelService;

	@Resource
	private ReadAuditGateway readAuditGateway;

	@Resource
	private WriteAuditGateway writeAuditGateway;

	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;

	private final PropertyConfigSwitcher auditAllTypesEnabledProperty = new PropertyConfigSwitcher("auditing.alltypes.enabled");
	private final PropertyConfigSwitcher auditBatchSizeProperty = new PropertyConfigSwitcher("audit.write.jdbc.batch.size");
	private static final int BATCH_SIZE_FOR_TEST = 20;

	@Before
	public void setUp() throws Exception
	{
		assumeAuditEnabled();
		auditAllTypesEnabledProperty.switchToValue("false");
		auditBatchSizeProperty.switchToValue(String.valueOf(BATCH_SIZE_FOR_TEST));
		prepareAuditingForTypes(false, "user", "Title");
	}

	@After
	public void tearDown() throws Exception
	{
		auditAllTypesEnabledProperty.switchBackToDefault();
		auditBatchSizeProperty.switchBackToDefault();
	}

	@Test
	public void testCreateAuditChange()
	{

		//given
		final TestDataCreator testDataCreator = new TestDataCreator(modelService);

		final UserModel u = testDataCreator.createUser(UUID.randomUUID().toString(), "Kowalski");
		List<AuditRecord> unsortedAuditRecords = getAuditRecordsForType(UserModel._TYPECODE, u.getPk());
		assertThat(unsortedAuditRecords).hasSize(0);
		final SLDDataContainer dataBefore = null;
		final SLDDataContainer dataAfter = sldItemDAO.load(u.getPk());

		final AuditableChange auditChange = new AuditableChange(dataBefore, dataAfter);
		final List<AuditableChange> auditChangeList = new ArrayList<>();
		auditChangeList.add(auditChange);
		//when
		auditableSaver.storeAudit(auditChangeList);

		//then
		unsortedAuditRecords = getAuditRecordsForType(UserModel._TYPECODE, u.getPk());
		final List<AuditRecord> records = AuditTestHelper.sortRecords(unsortedAuditRecords);

		assertThat(records) //
				.extracting(AuditRecord::getAuditType, AuditRecord::getType,
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("name")) //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.CREATION, UserModel._TYPECODE, "Kowalski"));

	}

	@Test
	public void testCreateModifyAuditChangeForManyItemsBatchUsage()
	{
		assertThat(Config.getInt("audit.write.jdbc.batch.size", 50)).isEqualTo(BATCH_SIZE_FOR_TEST);
		//given
		final TestDataCreator testDataCreator = new TestDataCreator(modelService);
		final List<UserModel> userModelList = new ArrayList<>();
		final List<TitleModel> titleModelList = new ArrayList<>();

		for (int i = 0; i < BATCH_SIZE_FOR_TEST + 1; i++)
		{
			final String randomUID = UUID.randomUUID().toString();
			final UserModel u = testDataCreator.createUser(randomUID, randomUID);
			userModelList.add(u);
			final List<AuditRecord> unsortedAuditRecords = getAuditRecordsForType(UserModel._TYPECODE, u.getPk());
			assertThat(unsortedAuditRecords).hasSize(0);
			final String randomUIDTitle = UUID.randomUUID().toString();

			final TitleModel t = testDataCreator.createTitle(randomUIDTitle, randomUIDTitle);
			titleModelList.add(t);
			final List<AuditRecord> unsortedAuditRecordsTitle = getAuditRecordsForType(TitleModel._TYPECODE, t.getPk());
			assertThat(unsortedAuditRecordsTitle).hasSize(0);
		}

		final List<AuditableChange> auditChangeList = new ArrayList<>();
		for (final UserModel um : userModelList)
		{
			auditChangeList.add(createAuditableChange(um.getPk()));
		}

		for (final TitleModel um : titleModelList)
		{
			auditChangeList.add(createAuditableChange(um.getPk()));
		}

		//when
		auditableSaver.storeAudit(auditChangeList);

		int totalAuditSize = 0;
		//then
		for (final UserModel um : userModelList)
		{
			final List<AuditRecord> unsortedAuditRecords = getAuditRecordsForType(UserModel._TYPECODE, um.getPk());
			totalAuditSize += unsortedAuditRecords.size();
		}
		for (final TitleModel um : titleModelList)
		{
			final List<AuditRecord> unsortedAuditRecords = getAuditRecordsForType(TitleModel._TYPECODE, um.getPk());
			totalAuditSize += unsortedAuditRecords.size();
		}
		assertThat(totalAuditSize).isEqualTo(2 * (BATCH_SIZE_FOR_TEST + 1));

	}

	private AuditableChange createAuditableChange(final PK pk)
	{
		final SLDDataContainer dataBefore = null;
		final SLDDataContainer dataAfter = sldItemDAO.load(pk);
		return new AuditableChange(dataBefore, dataAfter);
	}

	@Test
	public void testModifyAuditChange()
	{
		//given
		final TestDataCreator testDataCreator = new TestDataCreator(modelService);

		final UserModel u = testDataCreator.createUser(UUID.randomUUID().toString(), "Nowak");
		final SLDDataContainer dataBefore = sldItemDAO.load(u.getPk());

		u.setName("Nowak 2");
		modelService.save(u);
		final SLDDataContainer dataAfter = sldItemDAO.load(u.getPk());

		final AuditableChange auditChange = new AuditableChange(dataBefore, dataAfter);

		final List<AuditableChange> auditChangeList = new ArrayList<>();
		auditChangeList.add(auditChange);

		//when
		auditableSaver.storeAudit(auditChangeList);

		//then
		final List<AuditRecord> unsortedAuditRecords = getAuditRecordsForType(UserModel._TYPECODE, u.getPk());
		final List<AuditRecord> records = AuditTestHelper.sortRecords(unsortedAuditRecords);
		assertThat(records) //
				.extracting(AuditRecord::getAuditType, AuditRecord::getType,
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("name")) //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.MODIFICATION, UserModel._TYPECODE, "Nowak 2"));
	}

	@Test
	public void testDeleteAuditChange()
	{
		//given
		final TestDataCreator testDataCreator = new TestDataCreator(modelService);

		final UserModel u = testDataCreator.createUser(UUID.randomUUID().toString(), "Kowalski");
		final SLDDataContainer dataBefore = sldItemDAO.load(u.getPk());
		modelService.remove(u.getPk());
		final SLDDataContainer dataAfter = sldItemDAO.load(u.getPk());

		final AuditableChange auditChange = new AuditableChange(dataBefore, dataAfter);

		final List<AuditableChange> auditChangeList = new ArrayList<>();
		auditChangeList.add(auditChange);
		//when
		auditableSaver.storeAudit(auditChangeList);

		//then
		final List<AuditRecord> unsortedAuditRecords = getAuditRecordsForType(UserModel._TYPECODE, u.getPk());
		final List<AuditRecord> records = AuditTestHelper.sortRecords(unsortedAuditRecords);
		assertThat(records) //
				.extracting(AuditRecord::getAuditType, AuditRecord::getType,
						ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("name")) //
				.containsExactlyInAnyOrder( //
						tuple(AuditType.DELETION, UserModel._TYPECODE, "Kowalski"));

	}


	private List<AuditRecord> getAuditRecordsForType(final String type, final PK pk)
	{
		return readAuditGateway.search(AuditSearchQuery.forType(type).withPkSearchRules(pk).build()).collect(toList());
	}


	private void prepareAuditingForTypes(final boolean enabled, final String... types)
	{
		for (final String type : types)
		{
			writeAuditGateway.removeAuditRecordsForType(type);
			final PropertyConfigSwitcher switcher = new PropertyConfigSwitcher("audit." + type + ".enabled");
			switcher.switchToValue(BooleanUtils.toStringTrueFalse(enabled));
		}

		// We need to re-read config
		auditEnablementService.refreshConfiguredAuditTypes();
	}

}
