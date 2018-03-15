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
package de.hybris.platform.test;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.GlobalDiscountRow;
import de.hybris.platform.europe1.model.GlobalDiscountRowModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@IntegrationTest
public class UserSldTest extends ServicelayerBaseTest
{

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Resource
	ModelService modelService;

	@Resource
	UserService userService;

	@Resource
	AddressService addressService;

	@Resource
	CommonI18NService commonI18NService;

	@Resource
	SessionService sessionService;

	@Resource
	TypeService typeService;


	private static final PropertyConfigSwitcher persistenceLegacyMode = new PropertyConfigSwitcher("persistence.legacy.mode");

	@Before
	public void setUp()
	{
		persistenceLegacyMode.switchToValue("false");
	}

	@After
	public void tearDown()
	{
		persistenceLegacyMode.switchBackToDefault();
	}

	private UserModel createUser(final String login)
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(login);

		return user;
	}

	private UserGroupModel createUserGroup(final String groupName)
	{
		final UserGroupModel userGroup = modelService.create(UserGroupModel.class);
		userGroup.setUid(groupName);

		return userGroup;
	}

	private AddressModel createAddress(final String department)
	{
		final AddressModel address = modelService.create(AddressModel.class);
		address.setDepartment(department);

		return address;
	}

	@Test
	public void shouldPersistAndReadThroughSld()
	{
		final UserModel testUser = createUser("fooLogin");

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, testUser);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(testUser);

		final UserModel persistedUser = userService.getUserForUID(testUser.getUid());

		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(persistedUser);
	}

	@Test
	public void shouldPersistAndRemoveUserWithAddresses()
	{
		//given
		final String testUserId = "testUser9";
		final UserModel testUser = createUser(testUserId);

		final UserGroupModel testUserGroup = createUserGroup("testUserGroup9");

		testUser.setGroups(Collections.singleton(testUserGroup));


		final AddressModel address1 = createAddress("department1");
		address1.setOwner(testUser);

		final AddressModel address2 = createAddress("department2");
		address2.setOwner(testUser);
		address2.setOriginal(address1);

		testUser.setAddresses(Sets.newHashSet(address1, address2));

		modelService.save(testUser);

		final UserModel persistedUser = userService.getUserForUID(testUserId);

		assertThat(persistedUser.getAddresses()).hasSize(1);

		assertThat(modelService.isNew(address1)).isFalse();
		assertThat(modelService.isNew(address2)).isFalse();

		//when
		modelService.remove(persistedUser);

		//then
		assertThat(modelService.isRemoved(persistedUser)).isTrue();
		assertThat(modelService.isRemoved(address1)).isTrue();

		assertThat(modelService.isRemoved(address2)).isFalse();
	}

	@Test
	public void shouldGetAllGroupsReturnSameValueBeforeAndAfterBeingSaved()
	{
		//given
		final UserGroupModel userGroup = createUserGroup("userGroup");

		final UserGroupModel superGroup = createUserGroup("superGroup");
		userGroup.setGroups(Collections.singleton(superGroup));

		final UserModel user = createUser("user");
		user.setGroups(Sets.newHashSet(userGroup));

		//when
		assertThat(user.getGroups()).hasSize(1);
		assertThat(user.getAllGroups()).hasSize(2);

		modelService.saveAll();

		//then
		assertThat(user.getGroups()).hasSize(1);
		assertThat(user.getAllGroups()).hasSize(2);

		modelService.refresh(user);

		assertThat(user.getGroups()).hasSize(1);
		assertThat(user.getAllGroups()).hasSize(2);
	}

	@Test
	public void shouldPasswordBeTheSameForItemAndModel() throws ConsistencyCheckException
	{
		//given
		final String password = "Password123_xyz";

		final User userItem = UserManager.getInstance().createUser("userItem");

		//when
		userItem.setPassword(password);


		final UserModel userModel = createUser("userModel");
		userModel.setPassword(password);

		//then
		assertThat(userItem.getEncodedPassword()).isNotEmpty();
		assertThat(userItem.getPasswordEncoding()).isNotEmpty();
		assertThat(userItem.getEncodedPassword()).isEqualTo(userModel.getEncodedPassword());
		assertThat(userItem.getPasswordEncoding()).isEqualTo(userModel.getPasswordEncoding());

		modelService.save(userModel);

		assertThat(userItem.getEncodedPassword()).isEqualTo(userModel.getEncodedPassword());
		assertThat(userItem.getPasswordEncoding()).isEqualTo(userModel.getPasswordEncoding());
	}

	@Test
	public void shouldThrowExceptionWhenRemovingUserWithOrders()
	{
		//given
		final UserModel testUser = createUser("UserWithOrders");

		modelService.save(testUser);

		final OrderModel testOrder = modelService.create(OrderModel.class);

		testOrder.setCode("order calc test");
		testOrder.setUser(testUser);
		testOrder.setCurrency(commonI18NService.getBaseCurrency());
		testOrder.setDate(new Date());
		testOrder.setNet(Boolean.FALSE);

		//when
		modelService.save(testOrder);


		assertThat(testUser.getOrders()).containsOnly(testOrder);

		//then
		thrown.expect(ModelRemovalException.class);

		modelService.remove(testUser);
	}

	@Test
	public void shouldSetCurrentDateToPresentDay()
	{
		final UserModel testUser = createUser("user");

		final Date currentDateWithoutTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);

		assertThat(testUser.getCurrentDate()).isEqualTo(currentDateWithoutTime);
	}

	@Test
	public void shouldRemoveCartsWhenCartsSwitchUserAndThatUserIsRemoved()
	{
		//given
		final CartModel cart = modelService.create(CartModel.class);
		cart.setCurrency(commonI18NService.getCurrentCurrency());
		cart.setDate(new Date());
		cart.setNet(Boolean.TRUE);

		final UserModel userWhichCreateCarts = createUser("userWhichCreateCarts19");
		cart.setUser(userWhichCreateCarts);

		userWhichCreateCarts.setCarts(Sets.newHashSet(cart));

		modelService.save(userWhichCreateCarts);

		assertThat(cart.getUser()).isEqualTo(userWhichCreateCarts);

		final UserModel userWhichTakesCarts = createUser("userWhichTakesCarts19");

		//when
		userWhichTakesCarts.setCarts(Sets.newHashSet(cart));

		modelService.save(userWhichTakesCarts);

		modelService.refresh(cart);


		assertThat(cart.getUser()).isEqualTo(userWhichTakesCarts);

		modelService.remove(userWhichCreateCarts);

		//then
		assertThat(modelService.isRemoved(userWhichCreateCarts)).isTrue();
		assertThat(modelService.isRemoved(cart)).isFalse();

		modelService.remove(userWhichTakesCarts);

		assertThat(modelService.isRemoved(userWhichTakesCarts)).isTrue();
		assertThat(modelService.isRemoved(cart)).isTrue();
	}


	@Test
	public void shouldAddAllWritableCatalogVersionsToReadableAndRemoveNonUniqueOnesOnSave()
	{
		//given
		final UserModel userWithCatalogs = createUser("userWithCatalogs");

		final CatalogModel catalogModel = modelService.create(CatalogModel.class);

		catalogModel.setId("catalogId");
		catalogModel.setName("catalogName");


		final CatalogVersionModel catalogVersionWrite1 = modelService.create(CatalogVersionModel.class);

		catalogVersionWrite1.setVersion("version_write1");
		catalogVersionWrite1.setCatalog(catalogModel);

		final CatalogVersionModel catalogVersionWrite2 = modelService.create(CatalogVersionModel.class);

		catalogVersionWrite2.setVersion("version_write2");
		catalogVersionWrite2.setCatalog(catalogModel);

		final CatalogVersionModel catalogVersionRead1 = modelService.create(CatalogVersionModel.class);

		catalogVersionRead1.setVersion("version_read1");
		catalogVersionRead1.setCatalog(catalogModel);

		final CatalogVersionModel catalogVersionRead2 = modelService.create(CatalogVersionModel.class);

		catalogVersionRead2.setVersion("version_read2");
		catalogVersionRead2.setCatalog(catalogModel);

		modelService.saveAll();

		//when
		userWithCatalogs.setWritableCatalogVersions(Lists.newArrayList(catalogVersionWrite1, catalogVersionWrite2));

		userWithCatalogs.setReadableCatalogVersions(Lists.newArrayList(catalogVersionRead1, catalogVersionRead2,
				catalogVersionRead1));

		modelService.save(userWithCatalogs);

		//then
		assertThat(userWithCatalogs.getWritableCatalogVersions()).containsExactly(catalogVersionWrite1, catalogVersionWrite2);
		assertThat(userWithCatalogs.getReadableCatalogVersions()).containsExactly(catalogVersionRead1, catalogVersionRead2,
				catalogVersionWrite1, catalogVersionWrite2);
	}

	@Test
	public void shouldGetDiscountRowsUsingSldAndRemoveOnlyOnesThatBelongToCustomer() throws ImpExException,
			ConsistencyCheckException, InterruptedException
	{

		try
		{
			commonI18NService.getCurrency("EUR");
		}
		catch (final UnknownIdentifierException e)
		{
			final CurrencyModel model = modelService.create(CurrencyModel.class);
			model.setIsocode("EUR");
			modelService.save(model);
		}

		//given
		final String impexcode = "INSERT_UPDATE Discount;code[unique=true];value;priority;global;currency(isocode);name[lang=de];name[lang=en]\n"
				+ ";XXX;20;0;true;EUR;20e;20e\n"
				+ ";YYY;30;0;true;EUR;30e;30e\n"
				+ ";ZZZ;40;0;true;EUR;40e;40e\n"
				+ "INSERT GlobalDiscountRow;discount(code);currency(isocode);value;userMatchQualifier\n"
				+ ";ZZZ;EUR;50;"
				+ PK.NULL_PK
				+ "\n"
				+ "INSERT_UPDATE Customer;uid[unique=true];name;europe1Discounts[translator=de.hybris.platform.europe1.jalo.impex.Europe1UserDiscountsTranslator]\n"
				+ ";custxx1;customer XX 1;20 EUR XXX, 10 % YYY\n";

		final ImpExImportReader reader = new ImpExImportReader(impexcode);

		final Discount disc = (Discount) reader.readLine(); //read the discount line
		assertThat("XXX").isEqualTo(disc.getCode());
		final Discount disc2 = (Discount) reader.readLine(); //read the discount line
		assertThat("YYY").isEqualTo(disc2.getCode());
		final Discount disc3 = (Discount) reader.readLine(); //read the discount line
		assertThat("ZZZ").isEqualTo(disc3.getCode());
		final GlobalDiscountRow discRow = (GlobalDiscountRow) reader.readLine(); //read the discount row line
		assertThat(50d).isEqualTo(discRow.getValue());
		final Customer cust1 = (Customer) reader.readLine();
		assertThat("customer XX 1").isEqualTo(cust1.getName());

		assertThat(Europe1PriceFactory.getInstance().getEurope1Discounts(cust1)).hasSize(3);

		//when
		final CustomerModel customerModel = modelService.get(cust1.getPK());
		assertThat("customer XX 1").isEqualTo(customerModel.getName());


		final List<GlobalDiscountRowModel> europe1Discounts = Lists.newArrayList(customerModel.getEurope1Discounts());

		//then
		assertThat(europe1Discounts).hasSize(3);

		final GlobalDiscountRowModel userRow1 = europe1Discounts.get(0);
		final GlobalDiscountRowModel userRow2 = europe1Discounts.get(1);
		final GlobalDiscountRowModel wildCardRow = europe1Discounts.get(2);

		assertThat(userRow1.getUser()).isNotNull();
		assertThat(userRow2.getUser()).isNotNull();
		assertThat(wildCardRow.getUser()).isNull();

		assertThat(customerModel.getOwnEurope1Discounts()).containsOnly(userRow1, userRow2);

		modelService.remove(customerModel);

		assertThat(modelService.isRemoved(userRow1)).isTrue();
		assertThat(modelService.isRemoved(userRow2)).isTrue();
		assertThat(modelService.isRemoved(wildCardRow)).isFalse();

	}

	@Test
	public void shouldSearchRestrictionsBeDisabledWhenGettingGroupsFromCustomer()
	{

		//given
		final CustomerModel customerModel = modelService.create(CustomerModel.class);
		customerModel.setUid("customer");

		final UserGroupModel customerGroup = modelService.create(UserGroupModel.class);
		customerGroup.setUid("customerGroup");

		customerModel.setGroups(Collections.singleton(customerGroup));


		modelService.save(customerModel);

		assertThat(customerModel.getGroups()).containsOnly(customerGroup);


		final SearchRestrictionModel searchRestrictionModel = modelService.create(SearchRestrictionModel.class);
		searchRestrictionModel.setQuery("{" + ItemModel.PK + "} IS NULL");
		searchRestrictionModel.setPrincipal(customerModel);
		searchRestrictionModel.setActive(Boolean.TRUE);
		searchRestrictionModel.setGenerate(Boolean.FALSE);
		searchRestrictionModel.setCode("customerSearchRestriction");
		searchRestrictionModel.setRestrictedType(typeService.getComposedTypeForClass(PrincipalGroupModel.class));

		//when
		modelService.save(searchRestrictionModel);


		final CustomerModel persistedCustomer = modelService.get(customerModel.getPk());

		//then
		assertThat(persistedCustomer.getGroups()).hasSize(1).containsOnly(customerGroup);
	}
}