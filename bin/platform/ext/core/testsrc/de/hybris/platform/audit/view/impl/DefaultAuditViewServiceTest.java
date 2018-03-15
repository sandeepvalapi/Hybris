package de.hybris.platform.audit.view.impl;

import static com.google.common.collect.Lists.newArrayList;
import static de.hybris.platform.audit.AuditTestHelper.noDuplicatedReportEntries;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AtomicAttribute;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.internal.config.VirtualAttribute;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.testframework.seed.TestDataCreator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;


@IntegrationTest
public class DefaultAuditViewServiceTest extends ServicelayerBaseTest
{
	@Resource
	private AuditViewService auditViewService;
	@Resource
	private ModelService modelService;
	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;

	private AuditTestHelper auditTestHelper;
	private final PropertyConfigSwitcher userAuditConfigSwitcher = new PropertyConfigSwitcher("audit.user.enabled");
	private final PropertyConfigSwitcher allTypesAuditConfigSwitcher = new PropertyConfigSwitcher("auditing.alltypes.enabled");

	@Before
	public void setUp() throws Exception
	{
		auditTestHelper = new AuditTestHelper();
	}

	@After
	public void tearDown() throws Exception
	{
		userAuditConfigSwitcher.switchBackToDefault();
		allTypesAuditConfigSwitcher.switchBackToDefault();
	}

	@Test
	public void shouldProduceReportViewWithoutDuplicatedEntries() throws Exception
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("user1");
		user.setDescription("user description");
		modelService.save(user);

		user.setName("user2");
		modelService.save(user);

		user.setDescription("user description 2");
		modelService.save(user);


		final TypeAuditReportConfig config = TypeAuditReportConfig.builder() //
				.withConfig(auditTestHelper.createTestConfigForIntegrationTest()) //
				.withFullReport() //
				.withRootTypePk(user.getPk()) //
				.build();
		final List<ReportView> reportViews = auditViewService.getViewOn(config).collect(Collectors.toList());


		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries());
	}

	@Test
	public void shouldProduceReportViewForConfig() throws Exception
	{
		// given
		final AuditReportConfig testAuditReportConfig = auditTestHelper.createTestConfigForIntegrationTest();
		final UserModel user = auditTestHelper.prepareTestDataForIntegrationTest();

		// when
		final List<ReportView> reportView = auditViewService.getViewOn(TypeAuditReportConfig.builder()
				.withConfig(testAuditReportConfig).withRootTypePk(user.getPk()).withFullReport().build()).collect(toList());

		// then
		assertThat(reportView.stream().map(reportView1 -> ((Map<String, Object>) reportView1.getPayload().get("User"))))
				.extracting(AuditTestHelper.extractingRecursiveMapAttribute("name"), //
						AuditTestHelper.extractingRecursiveMapAttribute("defaultPaymentAddress", "streetname"), //
						AuditTestHelper.extractingRecursiveMapAttribute("defaultShipmentAddress", "streetname"), //
						AuditTestHelper.extractingRecursiveMapAttribute("profilepicture", "code") //
		) //
				.containsSubsequence( //
						tuple("Adam", null, null, null), //
						tuple("Adam", "Moniuszki", null, null), //
						tuple("Adam", "Moniuszki", "55th St.", "nice profile picture of me"), //
						tuple("SomeBetterNameForUser", "Moniuszki", "55th St.", "nice profile picture of me"), //
						tuple("SomeBetterNameForUser", "Chopina", "55th St.", "ugly picture of me") //
		);
	}

	@Test
	public void shouldProduceReportViewForConfigWithVirtualAttribute() throws Exception
	{
		// given
		final AuditReportConfig testAuditReportConfig = auditTestHelper.createTestConfigWithVirtualAttributeForIntegrationTest();
		final UserModel user = auditTestHelper.prepareTestDataForIntegrationTest();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig)
				.withRootTypePk(user.getPk()).withFullReport().build();


		// when
		final List<ReportView> reportView = auditViewService.getViewOn(config).collect(toList());


		// then
		assertThat(reportView.stream().map(reportView1 -> ((Map<String, Object>) reportView1.getPayload().get("User"))))
				.extracting(AuditTestHelper.extractingRecursiveMapAttribute("name"), //
						stringObjectMap -> ((List<Map<String, Object>>) stringObjectMap.getOrDefault("ownedAddresses",
								Collections.emptyList())).stream().map(address -> address.getOrDefault("streetname", null)).sorted()
										.collect(toList()),
						AuditTestHelper.extractingRecursiveMapAttribute("profilepicture", "code") //
		) //
				.containsSubsequence( //
						tuple("Adam", Collections.emptyList(), null), //
						tuple("Adam", newArrayList("55th St.", "Konichiwa", "Moniuszki"), null), //
						tuple("Adam", newArrayList("55th St.", "Konichiwa", "Moniuszki"), "nice profile picture of me"), //
						tuple("SomeBetterNameForUser", newArrayList("55th St.", "Konichiwa", "Moniuszki"),
								"nice profile picture of me"), //
						tuple("SomeBetterNameForUser", newArrayList("55th St.", "Chopina", "Konichiwa"), "nice profile picture of me"), //
						tuple("SomeBetterNameForUser", newArrayList("55th St.", "Chopina", "Konichiwa"), "ugly picture of me"), //
						tuple("SomeBetterNameForUser", newArrayList("55th St.", "Chopina", "Konichiwa", "Rynek"), "ugly picture of me"), //
						tuple("SomeBetterNameForUser", newArrayList("55th St.", "Chopina", "Konichiwa"), "ugly picture of me"), //
						tuple("SomeBetterNameForUser", newArrayList("Chopina", "Konichiwa"), "ugly picture of me") //
		);
	}


	@Test
	public void shouldProduceReportViewWithCyclicConfiguration()
	{

		final TestDataCreator testDataCreator = new TestDataCreator(modelService);
		final UserModel user = testDataCreator.createUser("cyclicUser", "Cyprian");
		final AddressModel address = testDataCreator.createAddress("Krakow", "Norwida", user);
		final TitleModel title = testDataCreator.createTitle("mr", "Mr");

		address.setTitle(title);
		user.setDefaultPaymentAddress(address);
		user.setDefaultShipmentAddress(address);

		modelService.saveAll(title, address, user);

		final TypeAuditReportConfig config = TypeAuditReportConfig.builder()
				.withConfig(auditTestHelper.loadConfigFromFile("audit.test/user-address-cyclic-audit.xml", "ItemsConfig"))
				.withRootTypePk(user.getPk()).withFullReport().build();
		final Stream<ReportView> reportView = auditViewService.getViewOn(config);

		final List<ReportView> reportViews = reportView.collect(Collectors.toList());

		assertThat(reportViews).isNotEmpty();
	}

	private String toJson(final Object input)
	{
		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			mapper.setDateFormat(new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ss.SSSZ"));
			return mapper.writeValueAsString(input);
		}
		catch (final IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Test
	public void shouldProduceReportViewForTypeWhichIsNotConfiguredForAuditingAndWasNeverModifiedWithCorrectCreationDate()
			throws Exception
	{
		// given
		allTypesAuditConfigSwitcher.switchToValue("false");
		userAuditConfigSwitcher.switchToValue("false");
		auditEnablementService.refreshConfiguredAuditTypes();
		final TestDataCreator testDataCreator = new TestDataCreator(modelService);
		final UserModel user = testDataCreator.createUser("henio", "Henio");
		final AuditReportConfig testAuditReportConfig = auditTestHelper.createTestConfigWithVirtualAttributeForIntegrationTest();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig)
				.withRootTypePk(user.getPk()).withFullReport().build();

		// when
		final List<ReportView> reportView = auditViewService.getViewOn(config).collect(toList());

		// then
		assertThat(reportView).isNotEmpty().hasSize(1);
		assertThat(reportView.get(0).getTimestamp()).isEqualTo(user.getCreationtime());
	}

	@Test
	public void shouldProduceReportViewForTypeWhichIsNotConfiguredForAuditingAndWasModifiedWithCorrectModificationDate()
			throws Exception
	{
		// given
		allTypesAuditConfigSwitcher.switchToValue("false");
		userAuditConfigSwitcher.switchToValue("false");
		auditEnablementService.refreshConfiguredAuditTypes();
		final TestDataCreator testDataCreator = new TestDataCreator(modelService);
		final UserModel user = testDataCreator.createUser("henio", "Henio");
		final AuditReportConfig testAuditReportConfig = auditTestHelper.createTestConfigWithVirtualAttributeForIntegrationTest();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig)
				.withRootTypePk(user.getPk()).withFullReport().build();
		user.setName("Henio Walczak");
		modelService.save(user);

		// when
		final List<ReportView> reportView = auditViewService.getViewOn(config).collect(toList());

		// then
		assertThat(reportView).isNotEmpty().hasSize(1);
		assertThat(reportView.get(0).getTimestamp()).isNotEqualTo(user.getCreationtime());
		assertThat(reportView.get(0).getTimestamp()).isEqualTo(user.getModifiedtime());
	}


	@Test
	public void shouldProduceReportViewForTypeWithVirtualAttributesWithInvalidCaseInAttributeExpression() throws Exception
	{

		final Type addressConfig = Type.builder() //
				.withCode("Address") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("town").build(), //
						AtomicAttribute.builder().withQualifier("streetname").build() //
		).build();

		final Type userConfig = Type.builder().withCode(UserModel._TYPECODE) //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
		) //
				.withVirtualAttributes( //
						VirtualAttribute.builder().withExpression("ownedAddresses").withDisplayName("addresses").withMany(Boolean.TRUE)
								.withType(addressConfig).withResolvesBy( //
										ResolvesBy.builder().withExpression("oWnEr").withResolverBeanId("virtualReferencesResolver").build() //
		).build() //
		) //
				.build();

		final AuditReportConfig reportConfig = AuditReportConfig.builder() //
				.withGivenRootType(userConfig) //
				.withName("testConfig") //
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(userConfig, addressConfig) //
				.build();

		final TestDataCreator testDataCreator = new TestDataCreator(modelService);
		final UserModel user = testDataCreator.createUser("henio", "Henio");
		final AddressModel address = testDataCreator.createAddress("Town", "Street", user);

		final List<ReportView> reportView = auditViewService
				.getViewOn(TypeAuditReportConfig.builder().withConfig(reportConfig).withRootTypePk(user.getPk()).build())
				.collect(toList());


		assertThat(reportView).isNotEmpty();


		assertThat(reportView.stream().map(reportView1 -> reportView1.getPayload().get("User"))).flatExtracting("addresses")
				.isNotNull().extracting("town", "streetname").containsOnly(tuple(address.getTown(), address.getStreetname()));


	}
}


