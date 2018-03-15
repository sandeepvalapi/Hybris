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
package de.hybris.platform.order;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.interceptors.UserRemoveInterceptor;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Test presenting change after PLA-11055. Tests a {@link UserRemoveInterceptor}.
 */
@IntegrationTest
public class RemoveUserWithOrderTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Test
	public void testDeleteUserWithOneOrder()
	{
		//preparing
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("EUR");

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");

		final AddressModel orgAddress = modelService.create(AddressModel.class);
		orgAddress.setOwner(user);

		final OrderModel order = createOrder(curr, user, orgAddress);

		modelService.saveAll(curr, user, orgAddress, order);

		try
		{
			//see User.checkRemovable(SessionContext)
			//see PLA-11055
			modelService.remove(user);
			fail("user should not be deletable because has still an order");
		}
		catch (final ModelRemovalException e)
		{
			//fine! which interceptor?
		}
		catch (final Exception e)
		{
			fail("Catched unexpected exception: " + e);
		}
		verifyNotRemoved(curr, user, orgAddress, order);
	}


	@Test
	public void testDeleteUserWithOutOrder()
	{
		//preparing
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("EUR");

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");

		final AddressModel orgAddress = modelService.create(AddressModel.class);
		orgAddress.setOwner(user);

		//final OrderModel order = createOrder(curr, user, orgAddress);

		modelService.saveAll(curr, user, orgAddress);


		modelService.remove(user);

		verifyRemoved(user, orgAddress, null);
	}


	@Test
	public void testDeleteUserWithFewOrders()
	{
		//preparing
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("EUR");

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");

		final AddressModel orgAddress = modelService.create(AddressModel.class);
		orgAddress.setOwner(user);

		final OrderModel orderOne = createOrder(curr, user, orgAddress);

		final OrderModel orderTwo = createOrder(curr, user, orgAddress);

		modelService.saveAll(curr, user, orgAddress, orderOne, orderTwo);

		try
		{
			//see User.checkRemovable(SessionContext)
			//see PLA-11055
			modelService.remove(user);
			fail("user should not be deletable because has still an order");
		}
		catch (final ModelRemovalException e)
		{
			//fine! which interceptor?
		}
		catch (final Exception e)
		{
			fail("Catched unexpected exception: " + e);
		}
		verifyNotRemoved(curr, user, orgAddress, orderOne);
		verifyNotRemoved(curr, user, orgAddress, orderTwo);
		modelService.remove(orderTwo);
		try
		{
			//see User.checkRemovable(SessionContext)
			//see PLA-11055
			modelService.remove(user);
			fail("user should not be deletable because has still an order");
		}
		catch (final ModelRemovalException e)
		{
			//fine! which interceptor?
		}
		catch (final Exception e)
		{
			fail("Catched unexpected exception: " + e);
		}
		verifyNotRemoved(curr, user, orgAddress, orderOne);

		modelService.remove(orderOne);
		modelService.remove(user);

		verifyRemoved(user, orgAddress, orderOne);
	}

	@Test
	public void testDeleteUserWithFewOrdersPriorOrdersDelete()
	{
		//preparing
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("EUR");

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");

		final AddressModel orgAddress = modelService.create(AddressModel.class);
		orgAddress.setOwner(user);

		final OrderModel orderOne = createOrder(curr, user, orgAddress);

		final OrderModel orderTwo = createOrder(curr, user, orgAddress);

		modelService.saveAll(curr, user, orgAddress, orderOne, orderTwo);

		modelService.removeAll(Arrays.asList(orderOne, orderTwo, user));

		verifyRemoved(user, orgAddress, orderOne);
		verifyRemoved(user, orgAddress, orderTwo);
	}

	@Test
	public void testDeleteUserWithFewOrdersTransactional()
	{
		//preparing
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("EUR");

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");

		final AddressModel orgAddress = modelService.create(AddressModel.class);
		orgAddress.setOwner(user);

		final OrderModel orderOne = createOrder(curr, user, orgAddress);

		final OrderModel orderTwo = createOrder(curr, user, orgAddress);
		//
		modelService.enableTransactions();
		modelService.saveAll(curr, user, orgAddress, orderOne, orderTwo);

		try
		{
			modelService.removeAll(Arrays.asList(orderOne, user));
			fail("Exception was expected (cannot remove user because it's still assigned to existing order) but not thrown");
		}
		catch (final Exception e)
		{
			Assert.assertFalse("order shouldn't be removed ", modelService.isRemoved(orderOne));
		}
	}

	/**
	 * Tests the removeAll method with user and order models. The order of passed arguments should Not matter
	 * (PLA-11084).
	 */
	@Test
	public void testDeleteUserWithOrderWithDifferentOrderOfArguments()
	{
		//preparing
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("EUR");

		UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");

		AddressModel orgAddress = modelService.create(AddressModel.class);
		orgAddress.setOwner(user);

		OrderModel orderOne = createOrder(curr, user, orgAddress);

		modelService.saveAll(curr, user, orgAddress, orderOne);

		modelService.removeAll(Arrays.asList(orderOne, user));
		verifyRemoved(user, orgAddress, orderOne);

		// now repeat it again and try to remove with different order of passed arguments

		user = modelService.create(UserModel.class);
		user.setUid("xxx");

		orgAddress = modelService.create(AddressModel.class);
		orgAddress.setOwner(user);

		orderOne = createOrder(curr, user, orgAddress);

		modelService.saveAll(curr, user, orgAddress, orderOne);

		//this is only a workaround (DISABLE_ITEMCHECK_BEFORE_REMOVABLE)
		//	consider to remove it if the servicelayer logic will be more "jalo-free". See PLA-11084 for details.  
		JaloSession.getCurrentSession().getSessionContext().setAttribute(Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.TRUE);

		modelService.removeAll(Arrays.asList(user, orderOne));
		verifyRemoved(user, orgAddress, orderOne);


	}

	/**
	 * 
	 */
	private OrderModel createOrder(final CurrencyModel curr, final UserModel user, final AddressModel orgAddress)
	{
		final OrderModel order = modelService.create(OrderModel.class);
		order.setCurrency(curr);
		order.setNet(Boolean.FALSE);
		order.setDate(new Date());
		order.setDeliveryAddress(orgAddress);
		order.setPaymentAddress(orgAddress);
		order.setUser(user);
		return order;
	}



	private void verifyNotRemoved(final CurrencyModel curr, final UserModel user, final AddressModel orgAddress,
			final OrderModel orderOne)
	{
		Assert.assertFalse("user shouldn't be removed ", modelService.isRemoved(user));
		Assert.assertFalse("orignal address shouldn't be removed ", modelService.isRemoved(orgAddress));
		Assert.assertFalse("order shouldn't be removed ", modelService.isRemoved(orderOne));
		Assert.assertFalse("currency shouldn't be removed ", modelService.isRemoved(curr));
		Assert.assertFalse("order's currency  shouldn't be removed ", modelService.isRemoved(orderOne.getCurrency()));
		Assert.assertFalse("order's payment address shouldn't be removed ", modelService.isRemoved(orderOne.getPaymentAddress()));
		Assert.assertFalse("order's delivery address shouldn't be removed ", modelService.isRemoved(orderOne.getDeliveryAddress()));
	}

	private void verifyRemoved(final UserModel user, final AddressModel orgAddress, final OrderModel orderOne)
	{
		Assert.assertTrue("user should be removed ", modelService.isRemoved(user));
		Assert.assertTrue("orignal address should be removed ", modelService.isRemoved(orgAddress));
		if (orderOne != null)
		{
			Assert.assertTrue("order should be removed ", modelService.isRemoved(orderOne));
			//Assert.assertFalse("currency shouldn't be removed ", modelService.isRemoved(curr));
			Assert.assertTrue("order's delivery address should be removed ", modelService.isRemoved(orderOne.getDeliveryAddress()));
			Assert.assertTrue("order's payment address should be removed ", modelService.isRemoved(orderOne.getPaymentAddress()));
		}
		//Assert.assertTrue("order's currency  should be removed ", modelService.isRemoved(orderOne.getCurrency()));
	}
}
