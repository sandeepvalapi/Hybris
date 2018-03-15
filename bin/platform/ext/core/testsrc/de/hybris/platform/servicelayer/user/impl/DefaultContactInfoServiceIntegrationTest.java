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
package de.hybris.platform.servicelayer.user.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.PhoneContactInfoType;
import de.hybris.platform.core.model.user.AbstractContactInfoModel;
import de.hybris.platform.core.model.user.PhoneContactInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.ContactInfoService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultContactInfoServiceIntegrationTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ContactInfoService contactInfoService;
	@Resource
	private ModelService modelService;
	private UserModel user;

	@Before
	public void setUp() throws Exception
	{
		user = createAndSaveUser();
	}

	@Test
	public void shouldRemoveAllContactInfosOnUserRemove() throws Exception
	{
		// given
		final List<AbstractContactInfoModel> phoneContacts = createPhoneContacts(user, "322990001", "322990002", "322990003");
		user.setContactInfos(phoneContacts);
		modelService.save(user);

		// when
		modelService.remove(user);

		// then
		for (final AbstractContactInfoModel phoneContact : phoneContacts)
		{
			assertThat(modelService.isRemoved(phoneContact)).isTrue();
		}
	}

	@Test
	public void shouldReturnNullMainInfoContactIfUserDoesNotHaveContacts() throws Exception
	{
		// when
		final AbstractContactInfoModel contactInfo = contactInfoService.getMainContactInfo(user);

		// then
		assertThat(user.getContactInfos()).hasSize(0);
		assertThat(contactInfo).isNull();
	}

	@Test
	public void shouldReturnFirstContactInfoFromTheListAsMainContactInfo() throws Exception
	{
		// given
		user.setContactInfos(createPhoneContacts(user, "322990001", "322990002", "322990003"));
		modelService.save(user);

		// when
		final AbstractContactInfoModel mainContactInfo = contactInfoService.getMainContactInfo(user);

		// then
		assertThat(user.getContactInfos()).hasSize(3);
		assertThat(mainContactInfo).isInstanceOf(PhoneContactInfoModel.class);
		assertThat(((PhoneContactInfoModel) mainContactInfo).getPhoneNumber()).isEqualTo("322990001");
	}

	@Test
	public void shouldSetMainContactForTheUser() throws Exception
	{
		// given
		user.setContactInfos(createPhoneContacts(user, "322990001", "322990002", "322990003"));
		modelService.save(user);
		final PhoneContactInfoModel mainContact = createPhoneContact("345556677");

		// when
		contactInfoService.setMainContactInfo(user, mainContact);
		modelService.save(user);
		final AbstractContactInfoModel mainContactInfo = contactInfoService.getMainContactInfo(user);

		// then
		assertThat(user.getContactInfos()).hasSize(4);
		assertThat(mainContactInfo).isInstanceOf(PhoneContactInfoModel.class);
		assertThat(((PhoneContactInfoModel) mainContactInfo).getPhoneNumber()).isEqualTo("345556677");
	}

	@Test
	public void shouldAddContactInfosToTheList() throws Exception
	{
		// given
		user.setContactInfos(createPhoneContacts(user, "322990001", "322990002", "322990003"));
		modelService.save(user);

		// when
		contactInfoService.addContactInfos(user, createPhoneContact("352555657"), createPhoneContact("352555658"));
		modelService.save(user);

		// then
		assertThat(user.getContactInfos()).hasSize(5);
		final AbstractContactInfoModel mainContactInfo = contactInfoService.getMainContactInfo(user);
		assertThat(((PhoneContactInfoModel) mainContactInfo).getPhoneNumber()).isEqualTo("322990001");
	}

	@Test
	public void shouldRemoveContactsFromTheList() throws Exception
	{
		// given
		final List<AbstractContactInfoModel> phoneContacts = createPhoneContacts(user, "322990001", "322990002", "322990003");
		user.setContactInfos(phoneContacts);
		modelService.save(user);

		// when
		contactInfoService.removeContactInfos(user, phoneContacts.get(1));
		modelService.save(user);

		// then
		assertThat(user.getContactInfos()).hasSize(2);
		assertThat(user.getContactInfos()).containsOnly(phoneContacts.get(0), phoneContacts.get(2));
		assertThat(modelService.isRemoved(phoneContacts.get(1)));
	}

	private UserModel createAndSaveUser()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid("testUser");
		user.setName("test");
		modelService.save(user);

		return user;
	}

	private List<AbstractContactInfoModel> createPhoneContacts(final UserModel user, final String... phoneNumbers)
	{
		final List<AbstractContactInfoModel> result = new ArrayList<>();

		for (final String number : phoneNumbers)
		{
			final PhoneContactInfoModel phone = createPhoneContact(number);
			phone.setUser(user);
			result.add(phone);
		}

		return result;
	}

	private PhoneContactInfoModel createPhoneContact(final String number)
	{
		final PhoneContactInfoModel phone = modelService.create(PhoneContactInfoModel.class);
		phone.setPhoneNumber(number);
		phone.setType(PhoneContactInfoType.HOME);

		return phone;
	}
}
