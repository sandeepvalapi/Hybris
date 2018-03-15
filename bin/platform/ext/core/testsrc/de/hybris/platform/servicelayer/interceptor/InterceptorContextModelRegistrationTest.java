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
package de.hybris.platform.servicelayer.interceptor;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.impl.DefaultInterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests <b>PLA-10368</b>.<br>
 * Test uses test implementation of {@link ModelService} :
 * <code>de.hybris.platform.servicelayer.interceptor.InterceptorTestModelService</code>.<br>
 * This <i>test service</i> knows only a simple {@link InterceptorRegistry}, which is registered only for the purpose of
 * this test. It maps only one test <code>PrepareInterceptor<code>s for the types under test.
 */
@IntegrationTest
public class InterceptorContextModelRegistrationTest extends ServicelayerBaseTest
{
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;

	private DefaultInterceptorRegistry registry;
	private InterceptorMapping orderInterceptor, addressInterceptor;

	@Before
	public void setUp() throws Exception
	{
		registry = (DefaultInterceptorRegistry) ((DefaultModelService) modelService).getInterceptorRegistry();
		orderInterceptor = createInterceptorMapping(new TestOrderPrepareInterceptor(), "Order");
		addressInterceptor = createInterceptorMapping(new TestAddressPrepareInterceptor(), "Address");
	}

	@After
	public void tearDown() throws Exception
	{
		unregister(orderInterceptor);
		unregister(addressInterceptor);
	}

	/**
	 * In the test scenario an order is created.<br>
	 * Test interceptors mapping creates a new paymentAddress per each new model creation.<br>
	 * Moreover, each new address triggers new Country creation via prepare interceptor.<br>
	 * The test checks if every new model registered in the interceptor context is really persisted.
	 */
	@Test
	public void testPersistModelsRegisteredInPrepareInterceptor()
	{
		final OrderModel order = createTestOrder("123");
		modelService.saveAll();
		Assert.assertNotNull("Order should have payment address prepared", order.getPaymentAddress());
		assertFalse("Order's payment address should not be unsaved", modelService.isNew(order.getPaymentAddress()));

		Assert.assertNotNull("Order payment address should have country prepared", order.getPaymentAddress().getCountry());
		assertFalse("Payment address' country should not be unsaved", modelService.isNew(order.getPaymentAddress().getCountry()));
	}

	@Test
	public void shouldCreateNewModelRegisteredForSaveInRemoveInterceptor()
	{
		InterceptorMapping removeInterceptor = null;
		try
		{
			// given
			final UserModel userA = createUser("userA");
			final UserModel userB = createUser("userB");

			removeInterceptor = createInterceptorMapping(new TestUserRemoveInterceptor_RegisterForCreation(userB), "User");

			modelService.save(userA);
			verifyIsNotSaved(userB);

			// when
			modelService.remove(userA);

			// then
			assertThat(modelService.isNew(userB)).isFalse();
			final UserModel userBRetrieved = modelService.get(userB.getPk());
			assertThat(userBRetrieved.getUid()).isEqualTo("userB");
		}
		finally
		{
			unregister(removeInterceptor);
		}
	}

	@Test
	public void shouldUpdateExistingModelRegisteredForSaveInRemoveInterceptor()
	{
		InterceptorMapping removeInterceptor = null;
		try
		{
			// given
			final UserModel userA = createUser("userA");
			final UserModel userB = createUser("userB");
			userB.setLoginDisabled(false);

			removeInterceptor = createInterceptorMapping(new TestUserRemoveInterceptor_RegisterForUpdate(userB), "User");

			modelService.saveAll(userA, userB);

			// when
			modelService.remove(userA);

			// then
			modelService.refresh(userB);
			assertThat(userB.isLoginDisabled()).isTrue();
		}
		finally
		{
			unregister(removeInterceptor);
		}
	}

	@Test
	public void shouldRemoveExistingModelRegisteredForRemovalInPrepareInterceptor()
	{
		InterceptorMapping prepareInterceptor = null;
		try
		{
			// given
			final UserModel userA = createUser("userA");
			final UserModel userB = createUser("userB");

			modelService.saveAll(userA, userB);

			prepareInterceptor = createInterceptorMapping(new TestUserPrepareInterceptor_RegisterForRemove(userB), "User");

			// when
			modelService.save(userA);

			// then
			verifyDoesNotExist(userB);
		}
		finally
		{
			unregister(prepareInterceptor);
		}
	}

	@Test
	public void shouldCreateAndDeleteTheSameItemRegisteredForBothCreationAndDeletion()
	{
		InterceptorMapping interceptor1 = null;
		InterceptorMapping interceptor2 = null;

		try
		{
			// given
			final UserModel userA = createUser("userA");
			final UserModel userB = createUser("userB");

			modelService.save(userB);

			interceptor1 = createInterceptorMapping(new TestUserPrepareInterceptor_ChangeAndRegisterForUpdate(userB), "User");
			interceptor2 = createInterceptorMapping(new TestUserPrepareInterceptor_VerifyUpdateAndRegisterForDelete(userB), "User");

			// when
			modelService.save(userA);

			// then
			assertThat(modelService.isNew(userB)).isFalse();
			verifyDoesNotExist(userB);
		}
		finally
		{
			unregister(interceptor1);
			unregister(interceptor2);
		}
	}

	@Test
	public void shouldSkipRegisteringMappingWithNoInterceptorWithoutException()
	{
		InterceptorMapping interceptor = null;

		try
		{
			interceptor = createInterceptorMapping(null, "User");
		}
		finally
		{
			if (interceptor != null)
			{
				unregister(interceptor);
			}
		}
	}


	private void verifyDoesNotExist(final UserModel userB)
	{
		try
		{
			modelService.get(userB.getPk());
			fail("Model " + userB + " is supposed to be deleted");
		}
		catch (final ModelLoadingException e)
		{
			assertThat(e.getMessage()).isEqualTo("No item found for given pk " + userB.getPk());
		}
	}

	private static class TestOrderPrepareInterceptor implements PrepareInterceptor
	{
		@Override
		public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			if (model instanceof OrderModel)
			{
				final OrderModel order = (OrderModel) model;
				final AddressModel newAddress = ctx.getModelService().create(AddressModel.class);
				newAddress.setOwner(order);
				newAddress.setStreetname("new");
				newAddress.setFirstname("New");
				order.setPaymentAddress(newAddress);
				ctx.registerElement(newAddress, null);
			}
		}
	}

	private static class TestAddressPrepareInterceptor implements PrepareInterceptor
	{
		@Override
		public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			if (model instanceof AddressModel)
			{
				final AddressModel address = (AddressModel) model;
				final CountryModel country = ctx.getModelService().create(CountryModel.class);
				country.setActive(Boolean.FALSE);
				country.setIsocode("PL");
				country.setName("Poland");
				address.setCountry(country);
				ctx.registerElement(country, null);
			}
		}
	}

	private static class TestUserRemoveInterceptor_RegisterForCreation implements RemoveInterceptor
	{

		private final UserModel otherUser;

		TestUserRemoveInterceptor_RegisterForCreation(final UserModel otherUser)
		{
			this.otherUser = otherUser;
		}

		@Override
		public void onRemove(final Object o, final InterceptorContext ctx) throws InterceptorException
		{
			ctx.registerElementFor(otherUser, PersistenceOperation.SAVE);
		}
	}

	private static class TestUserPrepareInterceptor_ChangeAndRegisterForUpdate implements PrepareInterceptor
	{

		private final UserModel user;

		TestUserPrepareInterceptor_ChangeAndRegisterForUpdate(final UserModel user)
		{
			this.user = user;
		}

		@Override
		public void onPrepare(final Object o, final InterceptorContext ctx) throws InterceptorException
		{
			this.user.setLoginDisabled(true);
			ctx.registerElementFor(user, PersistenceOperation.SAVE);
		}
	}

	private static class TestUserPrepareInterceptor_VerifyUpdateAndRegisterForDelete implements PrepareInterceptor
	{

		private final UserModel user;

		TestUserPrepareInterceptor_VerifyUpdateAndRegisterForDelete(final UserModel user)
		{
			this.user = user;
		}

		@Override
		public void onPrepare(final Object o, final InterceptorContext ctx) throws InterceptorException
		{
			if (o != this.user)
			{
				return;
			}

			assertThat(((UserModel) o).isLoginDisabled()).isTrue();

			ctx.registerElementFor(user, PersistenceOperation.DELETE);
		}
	}

	private static class TestUserRemoveInterceptor_RegisterForUpdate implements RemoveInterceptor
	{

		private final UserModel otherOrder;

		TestUserRemoveInterceptor_RegisterForUpdate(final UserModel otherOrder)
		{
			this.otherOrder = otherOrder;
		}

		@Override
		public void onRemove(final Object o, final InterceptorContext ctx) throws InterceptorException
		{
			otherOrder.setLoginDisabled(true);
			ctx.registerElementFor(otherOrder, PersistenceOperation.SAVE);
		}
	}

	private static class TestUserPrepareInterceptor_RegisterForRemove implements PrepareInterceptor
	{

		private final UserModel otherOrder;

		TestUserPrepareInterceptor_RegisterForRemove(final UserModel otherOrder)
		{
			this.otherOrder = otherOrder;
		}

		@Override
		public void onPrepare(final Object o, final InterceptorContext ctx) throws InterceptorException
		{
			ctx.registerElementFor(otherOrder, PersistenceOperation.DELETE);
		}
	}

	private InterceptorMapping createInterceptorMapping(final Interceptor interceptor, final String typeCode)
	{
		final InterceptorMapping mapping = new InterceptorMapping();
		mapping.setTypeCode(typeCode);
		mapping.setInterceptor(interceptor);
		registry.registerInterceptor(mapping);
		return mapping;
	}

	private void unregister(final InterceptorMapping interceptor)
	{
		if (interceptor != null)
		{
			registry.unregisterInterceptor(interceptor);
		}
	}

	private OrderModel createTestOrder(final String code)
	{
		final OrderModel order = modelService.create(OrderModel.class);
		order.setCode(code);
		order.setDate(new Date());
		order.setNet(Boolean.FALSE);
		order.setCurrency(commonI18NService.getBaseCurrency());
		order.setUser(userService.getCurrentUser());
		return order;
	}

	private UserModel createUser(final String uName1)
	{
		final UserModel testUser = modelService.create(UserModel.class);
		testUser.setUid(uName1);
		return testUser;
	}

	private void verifyIsNotSaved(final AbstractItemModel item)
	{
		Assert.assertTrue(modelService.isNew(item));
		try
		{
			modelService.get(item.getPk());
			fail("Exception should have been thrown - orderB is not supposed to have PK yet");
		}
		catch (final IllegalArgumentException e)
		{
			// it's fine - no PK yet
		}
	}

}
