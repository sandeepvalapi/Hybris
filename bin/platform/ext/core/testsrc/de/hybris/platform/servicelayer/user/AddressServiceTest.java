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
package de.hybris.platform.servicelayer.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * Integration tests for {@link AddressService}.
 */
@IntegrationTest
public class AddressServiceTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private AddressService addressService;

	@Resource
	private ModelService modelService;

	@Test
	public void testCreateAddressForUser()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid("testGroup");
		user.setName("Testgroup");

		modelService.save(user);

		final AddressModel address = addressService.createAddressForUser(user);

		assertNotNull("Address is null.", address);
		assertTrue("Address is not new.", modelService.isNew(address));
		assertEquals("Owner differs.", user, address.getOwner());
	}

	@Test
	public void testGetAddressesForOwner()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid("testGroup");
		user.setName("Testgroup");

		final AddressModel address = modelService.create(AddressModel.class);
		address.setFirstname("Test");
		address.setLastname("Tester");
		address.setOwner(user);

		modelService.saveAll(user, address);

		final Collection<AddressModel> result = addressService.getAddressesForOwner(user);
		assertNotNull("Address collection is null.", result);
		assertEquals("Address collection size differs.", 1, result.size());
		assertEquals("Address not found in collection.", address, result.iterator().next());
	}

	@Test
	public void testCloneAddress()
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");

		final AddressModel original = modelService.create(AddressModel.class);
		original.setFirstname("Test");
		original.setLastname("Tester");
		original.setOwner(group);

		modelService.saveAll(group, original);

		final AddressModel clone = addressService.cloneAddress(original);

		assertNotNull("Cloned address is null.", clone);
		assertFalse("Cloned address is equal to original!", original.equals(clone));
		assertEquals("Firstname differs.", original.getFirstname(), clone.getFirstname());
		assertEquals("Lastname differs.", original.getLastname(), clone.getLastname());
		assertEquals("Owner differs.", original.getOwner(), clone.getOwner());
		assertEquals("Original differs.", original, clone.getOriginal());
		assertEquals("Duplicate not set.", Boolean.TRUE, clone.getDuplicate());

		modelService.save(clone);

		assertEquals("Original differs.", original, clone.getOriginal());
	}

	@Test
	public void testCloneAddressWithOriginal()
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");

		final AddressModel base = modelService.create(AddressModel.class);
		base.setFirstname("Test");
		base.setLastname("Tester");
		base.setOwner(group);

		final AddressModel original = modelService.create(AddressModel.class);
		original.setFirstname("TestCopy");
		original.setLastname("TesterCopy");
		original.setOriginal(base);
		original.setOwner(group);

		modelService.saveAll(group, base, original);

		final AddressModel clone = addressService.cloneAddress(original);

		assertNotNull("Cloned address is null.", clone);
		assertFalse("Cloned address is equal to original!", original.equals(clone));
		assertEquals("Firstname differs.", original.getFirstname(), clone.getFirstname());
		assertEquals("Lastname differs.", original.getLastname(), clone.getLastname());
		assertEquals("Owner differs.", original.getOwner(), clone.getOwner());
		assertEquals("Original differs.", original, clone.getOriginal());
		assertEquals("Duplicate not set.", Boolean.TRUE, clone.getDuplicate());

		modelService.save(clone);

		assertEquals("Original differs.", original, clone.getOriginal());
	}

	/**
	 * Tests the cloning of addresses with a self-reference in the 'original' attribute.
	 * <p>
	 * The {@link ModelService#clone(Object)} is used for cloning.
	 * <p>
	 * The expected behavior is that the cloning works and the 'original' attribute in the clone is <code>null</code> ,
	 * because an attribute, which is required for creation and contains a self-reference, is skipped during cloning. See
	 * also PLA-10356.
	 */
	@Test
	public void testCloneAddressWithSelfReference()
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");

		final AddressModel original = modelService.create(AddressModel.class);
		original.setFirstname("TestCopy");
		original.setLastname("TesterCopy");
		original.setOwner(group);

		modelService.saveAll(group, original);

		// setting self-reference
		final Address addressItem = modelService.getSource(original);
		addressItem.setOriginal(addressItem);
		addressItem.setDuplicate(true);

		modelService.refresh(original);

		assertEquals("Original differs.", original, original.getOriginal());

		// cloning address with ModelService
		final AddressModel clone = modelService.clone(original);

		assertNotNull("Cloned address is null.", clone);
		assertFalse("Cloned address is equal to original!", original.equals(clone));
		assertEquals("Firstname differs.", original.getFirstname(), clone.getFirstname());
		assertEquals("Lastname differs.", original.getLastname(), clone.getLastname());
		assertEquals("Owner differs.", original.getOwner(), clone.getOwner());
		assertNull("Original not skipped during cloning. Must be null.", clone.getOriginal());
		assertEquals("Duplicate not set.", original.getDuplicate(), clone.getDuplicate());

		// save clone
		modelService.save(clone);

		assertNull("Original not skipped during cloning. Must be null.", clone.getOriginal());
	}

	@Test
	public void testRemoveUserWithAddress()
	{
		//preparing
		//user has an address and an order. the address in the order is cloned from the users address
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("EUR");

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");

		final AddressModel orgAddress = modelService.create(AddressModel.class);
		orgAddress.setOwner(user);

		modelService.saveAll(curr, user, orgAddress);

		final AddressModel clonedAddress = addressService.cloneAddress(orgAddress);

		final OrderModel order = modelService.create(OrderModel.class);
		order.setCurrency(curr);
		order.setNet(Boolean.FALSE);
		order.setDate(new Date());
		order.setDeliveryAddress(clonedAddress);
		order.setPaymentAddress(clonedAddress);
		clonedAddress.setOwner(order);
		order.setUser(user);

		modelService.saveAll(clonedAddress, order);

		//checking
		assertEquals("Only the original address should be visible", 1, user.getAddresses().size());
		assertFalse("", order.getPaymentAddress().equals(orgAddress));
		assertTrue("", order.getUser().equals(user));
		assertTrue("", clonedAddress.getOwner().equals(order));
		assertTrue("", clonedAddress.getOriginal().equals(orgAddress));
		assertEquals("", 1, user.getOrders().size());
		assertEquals("The assigned delivery address should not be cloned", order.getDeliveryAddress(), clonedAddress);
		assertEquals("The assigned payment address should not be cloned", order.getPaymentAddress(), clonedAddress);

		//removing user
		try
		{
			modelService.remove(user);
			Assert.fail("Should not be possible to remove a user with order");
		}
		catch (final ModelRemovalException mse)
		{
			//ok here
		}
		//check remaining models
		assertFalse("User should not  be removed :)", modelService.isRemoved(user));
		assertFalse("Adress should not  be removed as partOf", modelService.isRemoved(orgAddress));
		assertFalse("Order should not  be removed as a partOf for user ", modelService.isRemoved(order));
		assertFalse("Address should not  be removed as a partOf for order  ", modelService.isRemoved(clonedAddress));
		assertFalse("This user " + curr + " should not be touched", modelService.isRemoved(curr));

		//modelService.refresh(clonedAddress);
		//assertNull("", clonedAddress.getOriginal());
	}

	@Test
	public void testRemoveAddressFromUser()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");

		final AddressModel address1 = modelService.create(AddressModel.class);
		address1.setOwner(user);

		final AddressModel address2 = modelService.create(AddressModel.class);
		address2.setOwner(user);

		user.setDefaultPaymentAddress(address1);
		user.setDefaultShipmentAddress(address2);

		modelService.saveAll(user, address1, address2);
		// load attribute again - cleared after save ( if prefetch!=all )
		assertNotNull("", user.getDefaultShipmentAddress());

		//test
		modelService.remove(address2);

		//check
		assertFalse("", modelService.isRemoved(user));
		assertFalse("", modelService.isRemoved(address1));
		assertTrue("", modelService.isRemoved(address2));
		assertNotNull("", user.getDefaultShipmentAddress());

		modelService.refresh(user);
		assertNull("", user.getDefaultShipmentAddress());

	}

	@Test
	public void testAddressDuplicateAttributeIsSetOnSave()
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");

		final AddressModel withoutOriginal = modelService.create(AddressModel.class);
		withoutOriginal.setFirstname("Test");
		withoutOriginal.setLastname("Tester");
		withoutOriginal.setOwner(group);

		modelService.saveAll(group, withoutOriginal);


		assertEquals("Duplicate not set to false.", Boolean.FALSE, withoutOriginal.getProperty(AddressModel.DUPLICATE));

		final AddressModel withOriginal = modelService.create(AddressModel.class);
		withOriginal.setFirstname("TestWith");
		withOriginal.setLastname("TesterWith");
		withOriginal.setOwner(group);
		withOriginal.setOriginal(withoutOriginal);

		modelService.save(withOriginal);


		assertEquals("Duplicate not set to true.", Boolean.TRUE, withOriginal.getProperty(AddressModel.DUPLICATE));
	}

	@Test
	public void testAddressTypeQualifierDynamicAttribute()
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");

		final AddressModel address = modelService.create(AddressModel.class);
		address.setFirstname("Test");
		address.setLastname("Tester");

		address.setBillingAddress(Boolean.TRUE);

		address.setOwner(group);

		modelService.saveAll(group, address);


		assertEquals("Type qualifier .", Localization.getLocalizedString("address.is.billing"), address.getTypeQualifier());
	}

}
