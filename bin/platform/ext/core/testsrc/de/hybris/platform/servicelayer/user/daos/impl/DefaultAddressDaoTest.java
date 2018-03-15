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
package de.hybris.platform.servicelayer.user.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.AddressDao;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for {@link DefaultAddressDao}.
 */
@IntegrationTest
public class DefaultAddressDaoTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private AddressDao addressDao;

	@Test
	public void testFindNonAddressesForOwner()
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");

		final AddressModel address = modelService.create(AddressModel.class);
		address.setFirstname("Test");
		address.setLastname("Tester");
		address.setOwner(group);

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("testUser");
		user.setName("Testuser");

		modelService.saveAll(group, user, address);

		final Collection<AddressModel> result = addressDao.findAddressesForOwner(user);

		assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testFindAddressesForOwner()
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");

		final AddressModel address = modelService.create(AddressModel.class);
		address.setFirstname("Test");
		address.setLastname("Tester");
		address.setOwner(group);

		modelService.saveAll(group, address);

		final Collection<AddressModel> result = addressDao.findAddressesForOwner(group);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(address, result.iterator().next());
	}
}
