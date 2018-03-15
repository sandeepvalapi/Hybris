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

package de.hybris.platform.audit.demo;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.groups.Tuple.tuple;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AuditConfigService;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.impl.DefaultAuditableSaver;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.testframework.seed.TestDataCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;


@IntegrationTest
public class UserAuditDemoTest extends ServicelayerBaseTest implements AuditableTest
{
	public static final String NAME = "name";
	public static final String APPARTMENT = "appartment";
	public static final String USER = "User";
	public static final String GROUPS = "groups";
	public static final String OWNED_ADDRESSES = "owned addresses";
	public static final String DESCRIPTION = "description";
	public static final String CUSTOMER_ID = "customerID";
	public static final String LOGIN_DISABLED = "loginDisabled";
	public static final String HMC_LOGIN_DISABLED = "hmcLoginDisabled";
	public static final String ENCODED_PASSWORD = "encodedPassword";
	public static final String PASSWORD_QUESTION = "passwordQuestion";
	public static final String PASSWORD_ANSWER = "passwordAnswer";
	public static final String UID = "uid";
	public static final String LOC_NAME = "locName";

	public static final String MGR = "mgr";
	public static final String SIR = "sir";
	public static final String MADAME = "madame";
	public static final String PHD = "phd";
	public static final String GMBH = "gmbh";

	@Resource
	protected WriteAuditGateway writeAuditGateway;
	@Resource
	protected ReadAuditGateway readAuditGateway;
	@Resource
	private ModelService modelService;
	@Resource
	private AuditConfigService auditConfigService;
	@Resource
	private AuditViewService auditViewService;
	private AuditTestConfigManager auditTestConfigManager;
	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;
	private AuditTestHelper auditTestHelper;
	@Resource
	private SessionService sessionService;

	@Before
	public void setUp() throws Exception
	{
		auditTestConfigManager = new AuditTestConfigManager(auditEnablementService);
		auditTestHelper = new AuditTestHelper();
		assumeAuditEnabled();
		auditTestConfigManager.enableAuditingForTypes(USER, "Title", "Address", "UserGroup", "Country", "Region");

		auditTestHelper.clearAuditDataForTypes(USER, "Title", "Address", "UserGroup", "Country", "Region");
	}

	@After
	public void cleanup()
	{
		auditTestConfigManager.resetAuditConfiguration();
		auditTestHelper.clearAuditDataForTypes(USER, "Title", "Address", "UserGroup", "Country", "Region");
		auditTestHelper.removeCreatedItems();
	}

	@Test
	public void shouldGenerateReportForTypeWhichDoesntHaveAnyAtomicAttributes() throws Exception
	{
		// given
		final TestDataCreator testDataCreator = new TestDataCreator(modelService);
		final UserModel user = testDataCreator.createUser("henio", "Henio Walczak");
		final AddressModel address = testDataCreator.createAddress("Sosnowiec", "Piłsudskiego", user);
		user.setDefaultPaymentAddress(address);
		modelService.save(user);
		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-just-with-references-audit.xml", "UserReport");
		final TypeAuditReportConfig typeReportConfig = TypeAuditReportConfig.builder().withConfig(testConfig)
				.withRootTypePk(user.getPk()).withFullReport().build();

		// when
		final List<ReportView> report = auditViewService.getViewOn(typeReportConfig).collect(Collectors.toList());

		// then
		assertThat(report).isNotEmpty();
		assertThat((Map<String, Object>) report.get(0).getPayload().get("User")).isEmpty();
		assertThat((Map<String, Object>) report.get(1).getPayload().get("User")).hasSize(1);
		assertThat(
				((Map<String, Object>) ((Map<String, Object>) report.get(1).getPayload().get("User")).get("defaultPaymentAddress"))
						.get("street")).isEqualTo("Piłsudskiego");
	}

	@Test
	public void shouldAuditE2EUserData()
	{
		final UserModel u2 = auditTestHelper.createItem(UserModel.class);
		u2.setUid(UUID.randomUUID().toString());
		u2.setName("Name");
		u2.setDescription("My user description");
		modelService.save(u2);

		u2.setName("Thomas");
		modelService.save(u2);

		u2.setName("Jack");
		modelService.save(u2);


		final UserModel u = auditTestHelper.createItem(UserModel.class);
		u.setUid(UUID.randomUUID().toString());
		u.setName("u1name_1");
		u.setDescription("u1description_1");

		u.setLoginDisabled(false);
		u.setHmcLoginDisabled(Boolean.FALSE);

		modelService.save(u);

		u.setName("u1name_2");
		u.setPassword("u1password_2");
		u.setPasswordQuestion("u1passwordQuestion_2");
		u.setPasswordAnswer("u1passwordAnswer_2");
		u.setLoginDisabled(true);
		u.setHmcLoginDisabled(Boolean.TRUE);

		modelService.save(u);

		final UserModel u3 = auditTestHelper.createItem(UserModel.class);
		u3.setUid(UUID.randomUUID().toString());
		u3.setName("Jenny");
		u3.setDescription("Jenny Account");

		u.setName("u1name_3");
		u.setDescription("u1description_3");

		u2.setDescription("Description");
		u2.setName("Sherlock");

		modelService.saveAll(u3, u, u2);

		final List<AuditRecord> unsortedRecords = readAuditGateway
				.search(AuditSearchQuery.forType(USER).withPkSearchRules(u.getPk()).build()).collect(toList());
		final List<AuditRecord> user = AuditTestHelper.sortRecords(unsortedRecords);

		final Stream<Object> names = user.stream()
				.map(auditRecord -> AuditTestHelper.getAuditRecordsAttributes(auditRecord).get(NAME));
		assertThat(names).containsSubsequence("u1name_1", "u1name_2");

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-audit.xml", "testConfig");
		final List<ReportView> u1ReportView = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(u.getPk()).withFullReport().build())
				.collect(toList());


		assertThat(u1ReportView.stream().map(reportView1 -> reportView1.getPayload().get(USER))) //
				.extracting(UID, NAME, DESCRIPTION, LOGIN_DISABLED, HMC_LOGIN_DISABLED, ENCODED_PASSWORD, PASSWORD_QUESTION,
						PASSWORD_ANSWER)
				.containsSubsequence( //
						tuple(u.getUid(), "u1name_1", "u1description_1", Boolean.FALSE, Boolean.FALSE, null, null, null), //
						tuple(u.getUid(), "u1name_2", "u1description_1", Boolean.TRUE, Boolean.TRUE, "u1password_2",
								"u1passwordQuestion_2", "u1passwordAnswer_2"), //
						tuple(u.getUid(), "u1name_3", "u1description_3", Boolean.TRUE, Boolean.TRUE, "u1password_2",
								"u1passwordQuestion_2", "u1passwordAnswer_2") //
		);
	}

	@Test
	public void shouldAuditCustomerData()
	{
		final CustomerModel customer = auditTestHelper.createItem(CustomerModel.class);
		customer.setUid(UUID.randomUUID().toString());
		final String name_1 = "name_1";
		customer.setName(name_1);
		final String description_1 = "description_1";
		customer.setDescription(description_1);
		final String customerId_1 = "customerId_1";
		customer.setCustomerID(customerId_1);

		modelService.save(customer);

		final String name_2 = "name_2";
		customer.setName(name_2);
		final String description_2 = "description_2";
		customer.setDescription(description_2);
		final String customerId_2 = "customerId_2";
		customer.setCustomerID(customerId_2);
		modelService.save(customer);

		final List<AuditRecord> unsortedRecords = readAuditGateway
				.search(AuditSearchQuery.forType(USER).withPkSearchRules(customer.getPk()).build()).collect(toList());
		final List<AuditRecord> user = AuditTestHelper.sortRecords(unsortedRecords);

		assertThat(AuditTestHelper.getAuditRecordsAttributes(user.get(0)).get(NAME)).isEqualTo(name_1);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/customer-audit.xml", "testConfig");
		final List<ReportView> u1ReportView = auditViewService.getViewOn(
				TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(customer.getPk()).withFullReport().build())
				.collect(toList());

		assertThat(u1ReportView.stream().map(reportView -> reportView.getPayload().get(USER))) //
				.extracting(NAME, DESCRIPTION, CUSTOMER_ID) //
				.containsSubsequence( //
						tuple(name_1, description_1, customerId_1), //
						tuple(name_2, description_2, customerId_2) //
		);
	}


	@Test
	public void shouldAuditUserAndPrincipalGroup()
	{
		final UserModel u1 = auditTestHelper.createItem(UserModel.class);
		u1.setUid(UUID.randomUUID().toString());
		u1.setName("name1");
		u1.setDescription("description1");
		modelService.save(u1);

		final UserGroupModel userGroup1 = auditTestHelper.createItem(UserGroupModel.class);
		userGroup1.setUid(UUID.randomUUID().toString());
		userGroup1.setLocName("group1_1");
		modelService.save(userGroup1);

		final UserGroupModel userGroup2 = auditTestHelper.createItem(UserGroupModel.class);
		userGroup2.setUid(UUID.randomUUID().toString());
		userGroup2.setLocName("group2_1");
		modelService.save(userGroup2);

		final Set<PrincipalGroupModel> groups = new HashSet<>();
		groups.add(userGroup1);
		u1.setGroups(groups);

		modelService.save(u1);


		final Set<PrincipalGroupModel> groups2 = new HashSet<>();
		groups2.add(userGroup1);
		groups2.add(userGroup2);
		u1.setGroups(groups2);

		modelService.save(u1);

		userGroup1.setLocName("group1_2");
		modelService.save(userGroup1);

		userGroup1.setLocName("group1_3");
		modelService.save(userGroup1);

		u1.setName("name2");
		modelService.save(u1);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-groups-audit.xml", "UserReport");
		final List<ReportView> u1ReportView = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(u1.getPk()).withFullReport().build())
				.collect(toList());

		assertThat(u1ReportView.stream().map(reportView -> reportView.getPayload().get(USER))) //
				.extracting(NAME) //
				.containsSubsequence("name1", "name2");

		assertThat(u1ReportView.stream().map(reportView -> reportView.getPayload().get(USER))) //
				.flatExtracting(o -> {
					final List<String> groups1 = (List<String>) ((Map<String, Object>) o).get("groups");
					return groups1 == null ? Collections.emptyList() : groups1;
				}) //
				.extracting("locName") //
				.extracting("en") //
				.contains("group1_1", "group1_2", "group1_3");
	}

	@Test
	public void shouldDisallowAbstractTypesInConfiguration()
	{
		final UserModel u1 = auditTestHelper.createItem(UserModel.class);
		u1.setUid(UUID.randomUUID().toString());
		u1.setName("name1");
		u1.setDescription("description1");
		modelService.save(u1);

		final UserGroupModel userGroup1 = auditTestHelper.createItem(UserGroupModel.class);
		userGroup1.setUid(UUID.randomUUID().toString());
		userGroup1.setLocName("group1_1");
		modelService.save(userGroup1);

		final UserGroupModel userGroup2 = auditTestHelper.createItem(UserGroupModel.class);
		userGroup2.setUid(UUID.randomUUID().toString());
		userGroup2.setLocName("group2_1");
		modelService.save(userGroup2);

		final Set<PrincipalGroupModel> groups = new HashSet<>();
		groups.add(userGroup1);
		u1.setGroups(groups);

		modelService.save(u1);

		// type PrincipalGroup is Abstract
		try
		{
			final AuditReportConfig testConfig = loadConfigFromFile("audit.test/validation-abstract-type.xml", "PRODUCT");
			auditViewService
					.getViewOn(
							TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(u1.getPk()).withFullReport().build())
					.collect(toList());
			fail("SystemException was expected but not thrown");
		}
		catch (final Exception exc)
		{
			assertThat(exc).isInstanceOf(SystemException.class)
					.hasMessage("Type " + PrincipalGroupModel._TYPECODE + " has no audit table.");
			//ok here
		}
	}


	@Test
	public void shouldCorrectlyRemoveMany2ManyRelation()
	{
		final GroupsProvider groupsProvider = new GroupsProvider();

		final UserModel u1 = modelService.create(UserModel.class);
		u1.setUid(UUID.randomUUID().toString());
		u1.setName("name1");
		u1.setDescription("description1");
		modelService.save(u1);

		u1.setGroups(groupsProvider.getGroups("group1_1", "group2_1"));
		modelService.save(u1);

		u1.setGroups(groupsProvider.getGroups("group1_1"));
		modelService.save(u1);

		u1.setGroups(groupsProvider.getGroups("a", "b", "c", "d"));
		modelService.save(u1);

		u1.setGroups(groupsProvider.getGroups("a", "d"));
		modelService.save(u1);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-groups-audit.xml", "UserReport");
		final List<ReportView> u1ReportView = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(u1.getPk()).withFullReport().build())
				.collect(toList());

		final List<List> groups = extractGroupsFromReport(u1ReportView);
		final List<Set<String>> groupNamesSnapshots = extractEnglishGroupNames(groups);

		assertThat(groupNamesSnapshots).containsSubsequence(Sets.newSet("group1_1", "group2_1"), Sets.newSet("group1_1"),
				Sets.newSet("a", "b", "c", "d"), Sets.newSet("a", "d"));
	}

	private List<Set<String>> extractEnglishGroupNames(final List<List> groups)
	{
		final List<Set<String>> groupNamesSnapshots = new ArrayList<>();
		for (final List group : groups)
		{
			final Set<String> groupNames = (Set<String>) group.stream().map(i -> ((Map) i).get(LOC_NAME))
					.map(i -> ((Map) i).get("en")).map(String.class::cast).collect(Collectors.toSet());
			groupNamesSnapshots.add(groupNames);
		}
		return groupNamesSnapshots;
	}

	private List<List> extractGroupsFromReport(final List<ReportView> reportView)
	{
		return reportView.stream().map(i -> (Map) i.getPayload().get(USER)).map(i -> i.get(GROUPS)).map(List.class::cast)
				.filter(i -> !i.isEmpty()).collect(Collectors.toList());
	}

	private class GroupsProvider
	{
		private final Map<String, UserGroupModel> cachedGroups = new HashMap<>();

		public Set<PrincipalGroupModel> getGroups(final String... groups)
		{
			final Set<PrincipalGroupModel> resultGroups = new HashSet<>();

			for (final String group : groups)
			{
				final UserGroupModel userGroupModel = cachedGroups.getOrDefault(group, build(group));
				cachedGroups.put(group, userGroupModel);

				resultGroups.add(userGroupModel);
			}

			return resultGroups;
		}

		private UserGroupModel build(final String groupName)
		{
			final UserGroupModel userGroup = modelService.create(UserGroupModel.class);
			userGroup.setUid(UUID.randomUUID().toString());
			userGroup.setLocName(groupName);
			modelService.save(userGroup);

			return userGroup;
		}
	}

	@Test
	public void shouldAuditUserAndAddresses()
	{
		final UserModel u1 = auditTestHelper.createItem(UserModel.class);
		u1.setUid(UUID.randomUUID().toString());
		final String name1 = "name1";
		u1.setName(name1);
		final String description1 = "description1";
		u1.setDescription(description1);
		modelService.save(u1);

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		final String apartment_1 = "apartment_1";
		address.setAppartment(apartment_1);
		address.setOwner(u1);

		modelService.save(address);

		final String apartment_2 = "apartment_2";
		address.setAppartment(apartment_2);
		modelService.save(address);


		final String apartment_3 = "apartment_3";
		address.setAppartment(apartment_3);
		modelService.save(address);

		final String name2 = "name2";
		u1.setName(name2);
		modelService.save(u1);

		final String apartment_4 = "apartment_4";
		address.setAppartment(apartment_4);
		modelService.save(address);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-address-audit.xml", "UserReport");
		final List<ReportView> u1ReportView = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(u1.getPk()).withFullReport().build())
				.collect(toList());

		assertThat(u1ReportView).hasSize(6);

		final Map<String, Object> userReport1 = (Map<String, Object>) u1ReportView.get(0).getPayload().get(USER);
		assertThat(userReport1.get(NAME)).isEqualTo(name1);
		assertThat(userReport1.containsKey(DESCRIPTION)).isFalse();
		assertThat((List) userReport1.get(OWNED_ADDRESSES)).hasSize(0);

		final Map<String, Object> userReport2 = (Map) u1ReportView.get(1).getPayload().get(USER);
		assertThat(userReport2.get(NAME)).isEqualTo(name1);
		assertThat((List) userReport2.get(OWNED_ADDRESSES)).hasSize(1);
		assertThat(((List<Map<String, Object>>) userReport2.get(OWNED_ADDRESSES)).get(0).get(APPARTMENT)).isEqualTo(apartment_1);

		final Map<String, Object> userReport3 = (Map) u1ReportView.get(2).getPayload().get(USER);
		assertThat(userReport3.get(NAME)).isEqualTo(name1);
		assertThat((List) userReport3.get(OWNED_ADDRESSES)).hasSize(1);
		assertThat(((List<Map<String, Object>>) userReport3.get(OWNED_ADDRESSES)).get(0).get(APPARTMENT)).isEqualTo(apartment_2);

		final Map<String, Object> userReport4 = (Map) u1ReportView.get(3).getPayload().get(USER);
		assertThat(userReport4.get(NAME)).isEqualTo(name1);
		assertThat((List) userReport4.get(OWNED_ADDRESSES)).hasSize(1);
		assertThat(((List<Map<String, Object>>) userReport4.get(OWNED_ADDRESSES)).get(0).get(APPARTMENT)).isEqualTo(apartment_3);

		final Map<String, Object> userReport5 = (Map) u1ReportView.get(4).getPayload().get(USER);
		assertThat(userReport5.get(NAME)).isEqualTo(name2);
		assertThat((List) userReport5.get(OWNED_ADDRESSES)).hasSize(1);
		assertThat(((List<Map<String, Object>>) userReport5.get(OWNED_ADDRESSES)).get(0).get(APPARTMENT)).isEqualTo(apartment_3);

		final Map<String, Object> userReport6 = (Map) u1ReportView.get(5).getPayload().get(USER);
		assertThat(userReport6.get(NAME)).isEqualTo(name2);
		assertThat((List) userReport6.get(OWNED_ADDRESSES)).hasSize(1);
		assertThat(((List<Map<String, Object>>) userReport6.get(OWNED_ADDRESSES)).get(0).get(APPARTMENT)).isEqualTo(apartment_4);
	}


	@Test
	public void shouldAuditUserAndAddressesAndTitles()
	{
		final TitleModel universityTitle = auditTestHelper.createItem(TitleModel.class);
		universityTitle.setCode(MGR);
		universityTitle.setName(MGR);

		final TitleModel personalTitle = auditTestHelper.createItem(TitleModel.class);

		personalTitle.setCode(SIR);
		personalTitle.setName(SIR);

		modelService.saveAll(universityTitle, personalTitle);

		personalTitle.setCode(MADAME);
		personalTitle.setName(MADAME);
		modelService.save(personalTitle);

		final UserModel u1 = auditTestHelper.createItem(UserModel.class);
		u1.setUid(UUID.randomUUID().toString());
		final String name1 = "name1";
		u1.setName(name1);
		modelService.save(u1);

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		final String apartment_1 = "apartment_1";
		address.setAppartment(apartment_1);
		address.setOwner(u1);

		address.setTitle(universityTitle);

		modelService.save(address);

		universityTitle.setCode(PHD);
		universityTitle.setName(PHD);
		modelService.save(universityTitle);

		address.setTitle(personalTitle);
		modelService.save(address);

		personalTitle.setCode(GMBH);
		personalTitle.setName(GMBH);
		modelService.save(personalTitle);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-address-audit.xml", "UserReport");
		final List<ReportView> u1ReportView = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(u1.getPk()).withFullReport().build())
				.collect(toList());

		final Map<String, Object> userAudit1 = getUser(u1ReportView.get(0));

		assertThat(userAudit1.get(UserModel.NAME)).isEqualTo("name1");
		assertThat(userAudit1.get("owned addresses")).isInstanceOf(List.class);
		assertThat((List) userAudit1.get("owned addresses")).isEmpty();

		final Map<String, Object> userAudit2 = getUser(u1ReportView.get(1));
		assertThat(userAudit2.get("owned addresses")).isInstanceOf(List.class);
		assertThat((List) userAudit2.get("owned addresses")).hasSize(1);

		final Map<String, Object> addressAudit2 = getAddress(u1ReportView.get(1), 0);
		assertThat(addressAudit2.get(AddressModel.APPARTMENT)).isEqualTo("apartment_1");
		assertThat(addressAudit2.get("street")).isEqualTo("street");

		final Map<String, Object> titleAudit2 = getAddressTitle(u1ReportView.get(1), 0);
		assertThat(titleAudit2).isNotNull();

		final Stream<Tuple> titlesCodeAndNameEn = u1ReportView.stream() //
				.map(reportView -> extractAddressTitleAttributes(reportView, 0, Arrays.asList("title", TitleModel.CODE),
						Arrays.asList("title", TitleModel.NAME, "en")));
		assertThat(titlesCodeAndNameEn).containsSubsequence(tuple(MGR, MGR), tuple(PHD, PHD), tuple(MADAME, MADAME),
				tuple(GMBH, GMBH));
	}

	@SafeVarargs
	private final Tuple extractAddressTitleAttributes(final ReportView reportView, final int idx,
			final List<String>... attributePaths)
	{
		final Map<String, Object> user = getUser(reportView);

		final List userAddresses = (List) user.get("owned addresses");
		if (userAddresses.size() <= idx)
		{
			return tuple(new Object[attributePaths.length]);
		}

		return AuditTestHelper.extractRecursiveMapAttributes((Map<String, Object>) userAddresses.get(idx), attributePaths);
	}

	@Test
	public void shouldAuditCountryRegions()
	{
		final CountryModel poland = auditTestHelper.createItem(CountryModel.class);
		poland.setIsocode(UUID.randomUUID().toString());
		poland.setName("poland");


		final RegionModel silesia = auditTestHelper.createItem(RegionModel.class);
		silesia.setIsocode(UUID.randomUUID().toString());
		silesia.setName("silesia");
		silesia.setCountry(poland);

		final RegionModel lesserPoland = auditTestHelper.createItem(RegionModel.class);
		lesserPoland.setIsocode(UUID.randomUUID().toString());
		lesserPoland.setName("lesser poland");
		lesserPoland.setCountry(poland);

		modelService.saveAll(poland, silesia, lesserPoland);


		final UserModel u1 = auditTestHelper.createItem(UserModel.class);
		u1.setUid(UUID.randomUUID().toString());
		u1.setName("name1");
		modelService.save(u1);

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setOwner(u1);
		address.setCountry(poland);
		address.setRegion(silesia);

		modelService.save(address);

		address.setRegion(lesserPoland);
		modelService.save(address);


		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-address-country.xml", "userAddressCountry");
		final List<ReportView> u1ReportView = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(u1.getPk()).withFullReport().build())
				.collect(toList());

		final Map<String, Object> region1 = getRegion(u1ReportView.get(1), 0);
		final Map<String, Object> country1 = getCountry(u1ReportView.get(1), 0);

		assertThat((Map) region1.get("name")).containsEntry("en", "silesia");
		assertThat((Map) country1.get("name")).containsEntry("en", "poland");

		assertThat(country1.get("regions")).isNotNull();
		assertThat((ArrayList) country1.get("regions")).hasSize(2);

		final Map<String, Object> region2 = getRegion(u1ReportView.get(2), 0);
		final Map<String, Object> country2 = getCountry(u1ReportView.get(2), 0);

		assertThat((Map) region2.get("name")).containsEntry("en", "lesser poland");
		assertThat((Map) country2.get("name")).containsEntry("en", "poland");

		assertThat(country2.get("regions")).isNotNull();
		assertThat((ArrayList) country2.get("regions")).hasSize(2);
	}

	private Map<String, Object> getUser(final ReportView reportView)
	{
		return (Map<String, Object>) reportView.getPayload().get("User");
	}

	private Map<String, Object> getAddress(final ReportView reportView, final int idx)
	{
		final Map<String, Object> user = getUser(reportView);

		final List userAddresses = (List) user.get("owned addresses");
		return (Map<String, Object>) userAddresses.get(idx);
	}

	private Map<String, Object> getAddressTitle(final ReportView reportView, final int idx)
	{
		final Map<String, Object> address = getAddress(reportView, idx);
		return (Map<String, Object>) address.get("title");
	}

	private Map<String, Object> getRegion(final ReportView reportView, final int idx)
	{
		final Map<String, Object> address = getAddress(reportView, idx);
		return (Map<String, Object>) address.get("region");
	}

	private Map<String, Object> getCountry(final ReportView reportView, final int idx)
	{
		final Map<String, Object> address = getAddress(reportView, idx);
		return (Map<String, Object>) address.get("country");
	}


	@Test
	public void shouldFailIfUserReportIsExecutedForCatalog()
	{
		final CatalogModel catalog = auditTestHelper.createItem(CatalogModel.class);
		catalog.setId(UUID.randomUUID().toString());

		modelService.save(catalog);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-audit.xml", "testConfig");

		try
		{
			auditViewService.getViewOn(
					TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(catalog.getPk()).withFullReport().build());
			fail("SystemException was expected but not thrown!");
		}
		catch (final Exception ex)
		{
			assertThat(ex).isInstanceOf(SystemException.class);
			assertThat(ex).hasMessageContaining("Failed to execute");
		}
	}

	@Test
	public void shouldGenerateAuditForDeletedUser()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		final String name = "someName";
		user.setName(name);
		modelService.save(user);
		modelService.remove(user);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-audit.xml", "testConfig");
		final List<ReportView> reportView = auditViewService
				.getViewOn(
						TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(user.getPk()).withFullReport().build())
				.collect(toList());

		assertThat(reportView.size()).isEqualTo(2);

		final Map<String, Object> userMap = (Map<String, Object>) reportView.get(0).getPayload().get(USER);
		assertThat(userMap.get(NAME)).isEqualTo(name);
		final String userDeleted = (String) reportView.get(1).getPayload().get(USER);
		assertThat(userDeleted).contains("Deleted at");
	}

	@Test
	public void shouldGenerateAuditForUserUsingTypeCodeInsteadOfNotExistingDisplayKey()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		final String name = "testName";
		user.setName(name);
		modelService.save(user);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-with-displaykey-audit.xml", "ItemsConfig");
		final Type rootType = testConfig.getGivenRootType();
		assertThat(rootType.getTypeName()).isEqualTo(rootType.getCode());

		final List<ReportView> reportView = auditViewService
				.getViewOn(
						TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(user.getPk()).withFullReport().build())
				.collect(toList());

		assertThat(reportView.size()).isEqualTo(1);

		final Map<String, Object> userMap = (Map<String, Object>) reportView.get(0).getPayload().get(USER);
		assertThat(userMap.get(NAME)).isEqualTo(name);
	}


	@Test
	public void shouldGenerateAuditForUserUsingExactDisplayNames()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("testName");
		user.setDescription("description");
		modelService.save(user);

		final AddressModel defaultPaymentAddress = auditTestHelper.createItem(AddressModel.class);
		defaultPaymentAddress.setOwner(user);
		defaultPaymentAddress.setStreetname("some street name");
		defaultPaymentAddress.setAppartment("some apartment");
		modelService.save(defaultPaymentAddress);

		final UserGroupModel group = auditTestHelper.createItem(UserGroupModel.class);
		group.setUid(UUID.randomUUID().toString());
		group.setLocName("groupName");
		group.setMembers(Sets.newSet(user));
		modelService.save(group);

		user.setDefaultPaymentAddress(defaultPaymentAddress);
		modelService.save(user);


		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-with-displayname-audit.xml",
				"UserWithDisplayNameConfig");
		final Type rootType = testConfig.getGivenRootType();
		assertThat(rootType.getTypeName()).isEqualTo(rootType.getCode());

		final List<ReportView> reportViews = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(user.getPk()).build())
				.collect(toList());

		assertThat(reportViews.size()).isEqualTo(1);
		final ReportView reportView = reportViews.get(0);

		assertThat(reportView.getPayload()).containsOnlyKeys(USER);
		final Map<String, Object> userPayload = (Map<String, Object>) reportView.getPayload().get(USER);
		assertThat(userPayload).containsOnlyKeys("uid", "nAmE", "userDescription", "defaultPaymentAddress", "owned Addresses",
				"User_Groups");

		final Map<String, Object> addressRelationPayload = (Map<String, Object>) userPayload.get("defaultPaymentAddress");
		assertThat(addressRelationPayload).containsOnlyKeys("streeT", "aPpartment");

		final Collection<Map<String, Object>> addressVirtualPayload = (Collection<Map<String, Object>>) userPayload
				.get("owned Addresses");
		assertThat(addressVirtualPayload).hasSize(1);
		assertThat(addressVirtualPayload.iterator().next()).containsOnlyKeys("streeT", "aPpartment");

		final Collection<Map<String, Object>> groupsPayload = (Collection<Map<String, Object>>) userPayload.get("User_Groups");
		assertThat(groupsPayload).hasSize(1);
		assertThat(groupsPayload.iterator().next()).containsOnlyKeys("lOcName");

	}

	@Test
	public void shouldGenerateAuditForChangesDoneByActingUser()
	{
		final String actingUserSessionAttr = "ACTING_USER_UID";
		String currentActingUser = sessionService.getAttribute(actingUserSessionAttr);
		assertThat(currentActingUser).isNull();
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		final String name = "someName";
		user.setName(name);
		modelService.save(user);

		currentActingUser = "testActingUser";
		sessionService.setAttribute(actingUserSessionAttr, currentActingUser);

		final String nameChangedByActingUser = "nameChangedByActingUser";
		user.setName(nameChangedByActingUser);

		modelService.save(user);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/user-audit.xml", "testConfig");
		final List<ReportView> reportView = auditViewService
				.getViewOn(
						TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(user.getPk()).withFullReport().build())
				.collect(toList());

		assertThat(reportView.size()).isEqualTo(2);

		final Map<String, Object> userMap1 = (Map<String, Object>) reportView.get(0).getPayload().get(USER);
		assertThat(userMap1.get(NAME)).isEqualTo(name);
		assertThat(reportView.get(0).getContext()).doesNotContainKey(actingUserSessionAttr);

		final Map<String, Object> userMap2 = (Map<String, Object>) reportView.get(1).getPayload().get(USER);
		assertThat(userMap2.get(NAME)).isEqualTo(nameChangedByActingUser);
		assertThat(reportView.get(1).getContext()).containsKey(DefaultAuditableSaver.ACTING_USER);
		assertThat(reportView.get(1).getContext().get(DefaultAuditableSaver.ACTING_USER)).isEqualTo(currentActingUser);


	}

	private AuditReportConfig loadConfigFromFile(final String file, final String configName)
	{
		return auditTestHelper.loadConfigFromFile(file, configName);
	}
}
