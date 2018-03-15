/*
 *  [y] hybris Platform
 *  
 *  Copyright (c) 2000-2017 SAP SE
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of SAP
 *  Hybris ("Confidential Information"). You shall not disclose such
 *  Confidential Information and shall use it only in accordance with the
 *  terms of the license agreement you entered into with SAP Hybris.
 */

package de.hybris.platform.audit.demo;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AuditConfigService;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.enums.PhoneContactInfoType;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteEntryModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.PhoneContactInfoModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@IntegrationTest
public class PlatformPersonalDataDemoTest extends ServicelayerBaseTest implements AuditableTest
{
	private static final String PLATFORM_SAMPLE_PERSONAL_DATA_JSON = "platformSamplePersonalData.json";

	private static final String[] TYPES = new String[]
	{ "User", "AbstractContactInfo", "Cart", //
			"CartEntry", "Country", "Currency", //
			"Order", "OrderEntry", "PaymentInfo", //
			"PaymentMode", "Product", "Quote", //
			"QuoteEntry", "Region", "Title", //
			"Unit", "UserGroup", "UserPasswordChangeAudit" };

	@Resource
	protected WriteAuditGateway writeAuditGateway;
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private AuditConfigService auditConfigService;
	@Resource
	private AuditViewService auditViewService;
	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;

	private AuditTestConfigManager auditTestConfigManager;

	private UserModel user;


	@Before
	public void setUp()
	{
		auditTestConfigManager = new AuditTestConfigManager(auditEnablementService);
		assumeAuditEnabled();

		auditTestConfigManager.enableAuditingForTypes(TYPES);
		Arrays.stream(TYPES).forEach(writeAuditGateway::removeAuditRecordsForType);

		user = createUserWithPlatformPersonalData();

	}

	@After
	public void cleanup()
	{
		auditTestConfigManager.resetAuditConfiguration();
		Arrays.stream(TYPES).forEach(writeAuditGateway::removeAuditRecordsForType);
	}


	@Test
	public void shouldReportBeTheSame() throws IOException
	{

		final Map<String, Object> expectedMap = loadPersonalDataJsonReport();

		final TypeAuditReportConfig typeReportConfig = TypeAuditReportConfig.builder().withConfig(loadConfigFromFile())
				.withRootTypePk(user.getPk()).build();

		// when
		final Optional<ReportView> report = auditViewService.getViewOn(typeReportConfig).findFirst();

		assertThat(report).isPresent();

		final Map<String, Object> actualMap = actualMapBasedOnConf(report.get());

		assertThat(actualMap).isEqualTo(expectedMap);
	}

	public Map<String, Object> actualMapBasedOnConf(final ReportView report) throws IOException
	{
		final ObjectMapper mapper = new ObjectMapper();
		final String jsonString = mapper.writeValueAsString(report.getPayload());

		final Map<String, Object> confMap = mapper.readValue(jsonString, Map.class);

		convertListsToSetsInMap(confMap);
		return confMap;
	}


	private void convertListsToSetsInMap(final Map<String, Object> map)
	{
		if (MapUtils.isEmpty(map))
		{
			return;
		}

		for (final Entry<String, Object> entry : map.entrySet())
		{
			if (isModelReferenceAttribute(entry.getValue()))
			{
				convertListsToSetsInMap(((Map<String, Object>) entry.getValue()));
			}
			else if (isCollectionAttribute(entry.getValue()))
			{
				final Collection collectionAttribute = (Collection) entry.getValue();

				for (final Object value : collectionAttribute)
				{
					if (!isModelReferenceAttribute(value))
					{
						break;
					}
					convertListsToSetsInMap((Map<String, Object>) value);
				}

				if (isNotASetType(collectionAttribute))
				{
					final Set<Object> set = new HashSet<Object>(collectionAttribute);
					entry.setValue(set);
				}
			}
		}
	}

	private boolean isNotASetType(final Collection collectionAttribute)
	{
		return !(collectionAttribute instanceof Set);
	}

	private boolean isCollectionAttribute(final Object value)
	{
		return value instanceof Collection;
	}

	private boolean isModelReferenceAttribute(final Object value)
	{
		return value instanceof Map;
	}

	private UserModel createUserWithPlatformPersonalData()
	{

		final CatalogVersionModel catalogVersion = createCatalogAndCatalogVersion();

		final UnitModel unitPieces = createUnit();

		final ProductModel productFirst = createProduct(catalogVersion, "first");

		final ProductModel productSecond = createProduct(catalogVersion, "second");

		//USER
		final CustomerModel user = createUser();

		user.setName("name_2");
		modelService.save(user);

		user.setName("name_3");
		modelService.save(user);


		//UserPasswordChangeAudit
		user.setPassword("password_2");
		user.setPasswordAnswer("passwordAnswer_2");
		user.setPasswordQuestion("passwordQuestion_2");

		modelService.save(user);


		//UserGroup
		createUserGroup(user);


		//AbstractContactInfo
		final PhoneContactInfoModel phoneContactInfoMobile = createPhoneContactInfo(user, "mobile");

		phoneContactInfoMobile.setPhoneNumber("phoneNumber_mobile_2");
		modelService.save(phoneContactInfoMobile);

		createPhoneContactInfo(user, "work");


		//Address
		final AddressModel addressPayment = createAddress(user, "payment");

		addressPayment.setAppartment("apartment_payment_2");
		modelService.save(addressPayment);


		addressPayment.setAppartment("apartment_payment_3");
		modelService.save(addressPayment);


		final AddressModel addressShipping = createAddress(user, "shipping");

		//Country
		final CountryModel country = createCountry();

		//Region
		final RegionModel region = createRegion(country);


		addressPayment.setCountry(country);
		addressPayment.setRegion(region);


		modelService.save(addressPayment);


		//Title
		final TitleModel title = createTitle();

		addressPayment.setTitle(title);

		modelService.save(addressPayment);

		user.setDefaultPaymentAddress(addressPayment);
		user.setDefaultShipmentAddress(addressShipping);

		modelService.save(user);

		//Currency
		final CurrencyModel currency = createCurrency();

		//PaymentInfo
		final CreditCardPaymentInfoModel creditCardPaymentInfo = createCreditCardPaymentInfo(user);


		final DebitPaymentInfoModel debitPaymentInfo = createDebitPaymentInfo(user);

		//PaymentMode
		final PaymentModeModel paymentModeCredit = createPaymentMode();

		//Cart
		createAbstractOrderWithEntries(CartModel.class, CartEntryModel.class, unitPieces, productFirst, productSecond, user,
				addressPayment, addressShipping, currency, creditCardPaymentInfo, paymentModeCredit);


		//Order
		createAbstractOrderWithEntries(OrderModel.class, OrderEntryModel.class, unitPieces, productFirst, productSecond, user,
				addressPayment, addressShipping, currency, debitPaymentInfo, paymentModeCredit);


		//Quote
		final QuoteModel quote = createAbstractOrderWithEntries(QuoteModel.class, QuoteEntryModel.class, unitPieces, productFirst,
				productSecond, user, addressPayment, addressShipping, currency, creditCardPaymentInfo, paymentModeCredit);

		quote.setState(QuoteState.ORDERED);

		modelService.save(quote);


		return user;
	}

	private <ORDER extends AbstractOrderModel, ENTRY extends AbstractOrderEntryModel> ORDER createAbstractOrderWithEntries(
			final Class<ORDER> clazz, final Class<ENTRY> entryClazz, final UnitModel unit, final ProductModel productFirst,
			final ProductModel productSecond, final CustomerModel user, final AddressModel addressPayment,
			final AddressModel addressShipping, final CurrencyModel currency, final PaymentInfoModel paymentInfo,
			final PaymentModeModel paymentMode)
	{
		final String instanceName = clazz.getSimpleName().toLowerCase().replace("Model", "");

		final ORDER order = modelService.create(clazz);
		order.setCalculated(Boolean.FALSE);
		order.setCode("code_" + instanceName + "_1");
		order.setDate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.of("UTC")).toInstant()));
		order.setDescription("description_" + instanceName + "_1");
		order.setGlobalDiscountValues(Lists.newArrayList(new DiscountValue("discount_" + instanceName + "_1", 20d, false, null)));
		order.setName("name_" + instanceName + "_1");
		order.setPaymentCost(1d);
		order.setTotalDiscounts(1d);
		order.setTotalTax(1d);
		order.setTotalTaxValues(Lists.newArrayList(new TaxValue("tax_" + instanceName + "_1", 10d, false, null)));
		order.setCurrency(currency);
		order.setDeliveryAddress(addressShipping);
		order.setPaymentAddress(addressPayment);
		order.setPaymentInfo(paymentInfo);
		order.setPaymentMode(paymentMode);

		order.setUser(user);
		modelService.save(order);

		createEntry(entryClazz, unit, productFirst, order, 1, "first");

		createEntry(entryClazz, unit, productSecond, order, 2, "second");

		return order;
	}

	private <ENTRY extends AbstractOrderEntryModel> void createEntry(final Class<ENTRY> clazz, final UnitModel unit,
			final ProductModel product, final AbstractOrderModel abstractOrder, final int value, final String instanceName)
	{
		final ENTRY entry = modelService.create(clazz);
		entry.setEntryGroupNumbers(Sets.newHashSet(1, 2, 3));
		entry.setInfo("info_" + instanceName + "_1");
		entry.setEntryNumber(Integer.valueOf(value));
		entry.setQuantity(Long.valueOf(value));
		entry.setTotalPrice(Double.valueOf(value));

		entry.setProduct(product);
		entry.setUnit(unit);

		entry.setOrder(abstractOrder);

		modelService.save(entry);
	}

	private PaymentModeModel createPaymentMode()
	{
		final PaymentModeModel paymentModeCredit = modelService.create(PaymentModeModel.class);
		paymentModeCredit.setActive(Boolean.TRUE);
		paymentModeCredit.setCode("code_credit_1");
		paymentModeCredit.setName("name_credit_1");
		paymentModeCredit.setPaymentInfoType(typeService.getComposedTypeForCode(Constants.TYPES.CreditCardTypeType));

		modelService.save(paymentModeCredit);
		return paymentModeCredit;
	}

	private DebitPaymentInfoModel createDebitPaymentInfo(final CustomerModel user)
	{
		final DebitPaymentInfoModel debitPaymentInfo = modelService.create(DebitPaymentInfoModel.class);
		debitPaymentInfo.setCode("code_debit_1");
		debitPaymentInfo.setDuplicate(Boolean.FALSE);
		debitPaymentInfo.setAccountNumber("accountNumber_debit_1");
		debitPaymentInfo.setBank("bank_debit_1");
		debitPaymentInfo.setBankIDNumber("bankIDNumber_debit_1");
		debitPaymentInfo.setBaOwner("baOwner_debit_1");

		debitPaymentInfo.setUser(user);
		modelService.save(debitPaymentInfo);
		return debitPaymentInfo;
	}

	private CreditCardPaymentInfoModel createCreditCardPaymentInfo(final CustomerModel user)
	{
		final CreditCardPaymentInfoModel creditCardPaymentInfo = modelService.create(CreditCardPaymentInfoModel.class);
		creditCardPaymentInfo.setCode("code_creditCard_1");
		creditCardPaymentInfo.setDuplicate(Boolean.FALSE);
		creditCardPaymentInfo.setCcOwner("ccOwner_creditCard_1");
		creditCardPaymentInfo.setNumber("4111 1111 1111 1111");
		creditCardPaymentInfo.setValidFromMonth("validFromMonth_creditCard_1");
		creditCardPaymentInfo.setValidFromYear("validFromYear_creditCard_1");
		creditCardPaymentInfo.setValidToMonth("validToMonth_creditCard_1");
		creditCardPaymentInfo.setValidToYear("validToYear_creditCard_1");
		creditCardPaymentInfo.setType(CreditCardType.VISA);

		creditCardPaymentInfo.setUser(user);
		modelService.save(creditCardPaymentInfo);
		return creditCardPaymentInfo;
	}

	private CurrencyModel createCurrency()
	{
		final CurrencyModel currency = modelService.create(CurrencyModel.class);
		currency.setIsocode("isocode_1");
		currency.setName("name_1");
		currency.setSymbol("symbol_1");

		modelService.save(currency);
		return currency;
	}

	private TitleModel createTitle()
	{
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode("code_1");
		title.setName("name_1");
		modelService.save(title);
		return title;
	}

	private RegionModel createRegion(final CountryModel country)
	{
		final RegionModel region = modelService.create(RegionModel.class);
		region.setIsocode("isocode_1");
		region.setName("name_1");
		region.setCountry(country);
		modelService.save(region);
		return region;
	}

	private CountryModel createCountry()
	{
		final CountryModel country = modelService.create(CountryModel.class);
		country.setIsocode("isocode_1");
		country.setName("name_1");
		modelService.save(country);
		return country;
	}

	private PhoneContactInfoModel createPhoneContactInfo(final CustomerModel user, final String instanceName)
	{
		final PhoneContactInfoModel phoneContactInfo = modelService.create(PhoneContactInfoModel.class);
		phoneContactInfo.setCode("code_" + instanceName + "_1");
		phoneContactInfo.setPhoneNumber("phoneNumber_" + instanceName + "_1");
		phoneContactInfo.setType(PhoneContactInfoType.valueOf(instanceName));

		phoneContactInfo.setUser(user);

		modelService.save(phoneContactInfo);
		return phoneContactInfo;
	}

	private AddressModel createAddress(final CustomerModel user, final String instanceName)
	{
		final AddressModel address = modelService.create(AddressModel.class);
		address.setAppartment("apartment_" + instanceName + "_1");
		address.setBuilding("building_" + instanceName + "_1");
		address.setCellphone("cellphone_" + instanceName + "_1");
		address.setCompany("company_" + instanceName + "_1");
		address.setDateOfBirth(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.of("UTC")).toInstant()));
		address.setDepartment("department_" + instanceName + "_1");
		address.setDistrict("district_" + instanceName + "_1");
		address.setDuplicate(Boolean.FALSE);
		address.setEmail("email_" + instanceName + "@sap.com_1");
		address.setFax("fax_" + instanceName + "_1");
		address.setFirstname("firstname_" + instanceName + "_1");
		address.setGender(Gender.MALE);
		address.setLastname("lastname_" + instanceName + "_1");
		address.setMiddlename("middlename_" + instanceName + "_1");
		address.setMiddlename2("middlename2_" + instanceName + "_1");
		address.setPhone1("phone1_" + instanceName + "_1");
		address.setPhone2("phone2_" + instanceName + "_2");
		address.setPobox("pobox_" + instanceName + "_1");
		address.setPostalcode("postalcode_" + instanceName + "_1");
		address.setStreetname("streetname_" + instanceName + "_1");
		address.setStreetnumber("streetnumber_" + instanceName + "_1");
		address.setTown("town_" + instanceName + "_1");

		address.setOwner(user);

		modelService.save(address);
		return address;
	}

	private UserGroupModel createUserGroup(final CustomerModel user)
	{
		final UserGroupModel userGroup = modelService.create(UserGroupModel.class);
		userGroup.setDescription("description_1");
		userGroup.setLocName("locName_1");
		userGroup.setName("name_1");
		userGroup.setUid("uid_usergroup_1");

		userGroup.setMembers(Collections.singleton(user));

		modelService.save(userGroup);

		return userGroup;
	}

	private CustomerModel createUser()
	{
		final CustomerModel user = modelService.create(CustomerModel.class);
		user.setDeactivationDate(Date.from(LocalDate.of(2100, 1, 1).atStartOfDay(ZoneId.of("UTC")).toInstant()));
		user.setDescription("description_1");
		user.setCustomerID("customerID_1");
		user.setName("name_1");
		user.setPasswordAnswer("passwordAnswer_1");
		user.setPassword("password_1");
		user.setPasswordQuestion("passwordQuestion_1");
		user.setUid("uid_user_1");
		modelService.save(user);
		return user;
	}

	private ProductModel createProduct(final CatalogVersionModel catalogVersion, final String instanceName)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("code_" + instanceName + "_1");
		product.setName("name_" + instanceName + "_1");
		product.setCatalogVersion(catalogVersion);
		modelService.save(product);
		return product;
	}

	private UnitModel createUnit()
	{
		final UnitModel unitPieces = modelService.create(UnitModel.class);
		unitPieces.setCode("code_pieces_1");
		unitPieces.setUnitType("unitType_pieces_1");
		modelService.save(unitPieces);
		return unitPieces;
	}

	private CatalogVersionModel createCatalogAndCatalogVersion()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(UUID.randomUUID().toString());

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(UUID.randomUUID().toString());
		catalogVersion.setCatalog(catalog);

		modelService.saveAll(catalog, catalogVersion);

		return catalogVersion;
	}

	private AuditReportConfig loadConfigFromFile()
	{
		try (InputStream resourceAsStream = PlatformPersonalDataDemoTest.class.getClassLoader()
				.getResourceAsStream("audit.test/audit-config.xml"))
		{
			final String xml = IOUtils.toString(resourceAsStream, UTF_8);
			auditConfigService.storeConfiguration("PersonalDataReport", xml);
			return auditConfigService.getConfigForName("PersonalDataReport");
		}
		catch (final IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	private Map<String, Object> loadPersonalDataJsonReport() throws IOException
	{
		final InputStream resourceAsStream = PlatformPersonalDataDemoTest.class.getClassLoader()
				.getResourceAsStream("audit.test/" + PLATFORM_SAMPLE_PERSONAL_DATA_JSON);
		final ObjectMapper mapper = new ObjectMapper();
		final Map<String, Object> loadedJsonMap = mapper.readValue(resourceAsStream, Map.class);
		convertListsToSetsInMap(loadedJsonMap);
		return loadedJsonMap;
	}
}