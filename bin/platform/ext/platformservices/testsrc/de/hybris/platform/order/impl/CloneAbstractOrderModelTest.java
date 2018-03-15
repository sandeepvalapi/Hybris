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
package de.hybris.platform.order.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.AbstractOrderEntryTypeService;
import de.hybris.platform.order.strategies.ordercloning.impl.DefaultCloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CloneAbstractOrderModelTest
{
	private DefaultCloneAbstractOrderStrategy cloneOrderStrategy;

	@Mock
	private AbstractOrderEntryTypeService abstractOrderEntryTypeService;

	@Mock
	private ItemModelCloneCreator itemModelCloneCreator;

	@Mock
	private TypeService typeService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		cloneOrderStrategy = new DefaultCloneAbstractOrderStrategy(typeService, itemModelCloneCreator,
				abstractOrderEntryTypeService);
	}

	@Test
	public void testCloneRegularOrder()
	{
		final ComposedTypeModel orderCT = new ComposedTypeModel();
		final ComposedTypeModel orderEntryCT = new ComposedTypeModel();
		final OrderModel orderModel = new OrderModel();


		OrderEntryModel orderEntry = new OrderEntryModel();
		orderEntry.setEntryNumber(Integer.valueOf(0));
		orderModel.setEntries(Arrays.asList((AbstractOrderEntryModel) orderEntry));

		final String orderCode = "123";

		final OrderModel orderClone = new OrderModel();


		orderEntry = new OrderEntryModel();
		orderEntry.setEntryNumber(Integer.valueOf(0));
		orderClone.setEntries(Arrays.asList((AbstractOrderEntryModel) orderEntry));


		final ArgumentMatcher<ItemModelCloneCreator.CopyContext> matcher = new ArgumentMatcher<ItemModelCloneCreator.CopyContext>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ItemModelCloneCreator.CopyContext))
				{
					return false;
				}
				final ItemModelCloneCreator.CopyContext copyContext = (ItemModelCloneCreator.CopyContext) argument;

				final OrderEntryModel tmp = new OrderEntryModel();

				if (copyContext.getTargetType(tmp).equals(orderEntryCT) && copyContext.getTargetType(new OrderModel()) == null)
				{
					return true;
				}

				return false;
			}

		};


		when(itemModelCloneCreator.copy(eq(orderCT), eq(orderModel), argThat(matcher))).thenReturn(orderClone);


		final OrderModel orderClone2 = cloneOrderStrategy.clone(orderCT, orderEntryCT, orderModel, orderCode, OrderModel.class,
				OrderEntryModel.class);

		verify(itemModelCloneCreator).copy(eq(orderCT), eq(orderModel), argThat(matcher));
		assertThat(orderClone2.getCode()).isEqualTo(orderCode);
	}


	@Test
	public void testCloneRegularOrderWrongEntries()
	{
		final ComposedTypeModel orderCT = new ComposedTypeModel();
		final ComposedTypeModel orderEntryCT = new ComposedTypeModel();
		final OrderModel orderModel = new OrderModel();

		final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
		list.add(new OrderEntryModel());
		orderModel.setEntries(list);

		final String orderCode = "123";

		final OrderModel orderClone = new OrderModel();


		orderClone.setEntries(Collections.<AbstractOrderEntryModel> emptyList());


		final ArgumentMatcher<ItemModelCloneCreator.CopyContext> matcher = new ArgumentMatcher<ItemModelCloneCreator.CopyContext>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ItemModelCloneCreator.CopyContext))
				{
					return false;
				}
				final ItemModelCloneCreator.CopyContext copyContext = (ItemModelCloneCreator.CopyContext) argument;

				final OrderEntryModel tmp = new OrderEntryModel();

				if (copyContext.getTargetType(tmp).equals(orderEntryCT) && copyContext.getTargetType(new OrderModel()) == null)
				{
					return true;
				}

				return false;
			}

		};


		when(itemModelCloneCreator.copy(eq(orderCT), eq(orderModel), argThat(matcher))).thenReturn(orderClone);


		try
		{
			cloneOrderStrategy.clone(orderCT, orderEntryCT, orderModel, orderCode, OrderModel.class, OrderEntryModel.class);
			fail();
		}
		catch (final IllegalStateException e)
		{
			//ok
		}
		catch (final Exception e)
		{
			fail("got unknown exception " + e);
		}
		verify(itemModelCloneCreator).copy(eq(orderCT), eq(orderModel), argThat(matcher));

	}

	@Test
	public void testCloneRegularOrderNoTypes()
	{
		final ComposedTypeModel orderCT = new ComposedTypeModel();
		final ComposedTypeModel orderEntryCT = new ComposedTypeModel();
		final OrderModel orderModel = new OrderModel();
		OrderEntryModel entry = new OrderEntryModel();
		entry.setEntryNumber(Integer.valueOf(0));
		orderModel.setEntries(Arrays.asList((AbstractOrderEntryModel) entry));



		final String orderCode = "123";

		entry = new OrderEntryModel();
		entry.setEntryNumber(Integer.valueOf(0));
		final OrderModel orderClone = new OrderModel();
		orderClone.setEntries(Arrays.asList((AbstractOrderEntryModel) entry));


		final ArgumentMatcher<ItemModelCloneCreator.CopyContext> matcher = new ArgumentMatcher<ItemModelCloneCreator.CopyContext>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ItemModelCloneCreator.CopyContext))
				{
					return false;
				}
				final ItemModelCloneCreator.CopyContext copyContext = (ItemModelCloneCreator.CopyContext) argument;

				final OrderEntryModel tmp = new OrderEntryModel();

				if (copyContext.getTargetType(tmp).equals(orderEntryCT))
				{
					return true;
				}

				return false;
			}

		};

		when(typeService.getComposedTypeForClass(OrderModel.class)).thenReturn(orderCT);
		when(itemModelCloneCreator.copy(eq(orderCT), eq(orderModel), argThat(matcher))).thenReturn(orderClone);
		when(abstractOrderEntryTypeService.getAbstractOrderEntryType(orderModel)).thenReturn(orderEntryCT);

		final OrderModel orderClone2 = cloneOrderStrategy.clone(null, null, orderModel, orderCode, OrderModel.class,
				OrderEntryModel.class);

		verify(typeService).getComposedTypeForClass(OrderModel.class);
		verify(itemModelCloneCreator).copy(eq(orderCT), eq(orderModel), argThat(matcher));
		verify(abstractOrderEntryTypeService).getAbstractOrderEntryType(orderModel);
		assertThat(orderClone2.getCode()).isEqualTo(orderCode);
	}

	@Test
	public void testCloneRegularOrderNoTypesToCart()
	{
		final ComposedTypeModel orderCT = new ComposedTypeModel();
		final ComposedTypeModel orderEntryCT = new ComposedTypeModel();
		final OrderModel orderModel = new OrderModel();


		OrderEntryModel entry = new OrderEntryModel();
		entry.setEntryNumber(Integer.valueOf(0));
		orderModel.setEntries(Arrays.asList((AbstractOrderEntryModel) entry));

		final String orderCode = "123";

		final CartModel orderClone = new CartModel();
		entry = new OrderEntryModel();
		entry.setEntryNumber(Integer.valueOf(0));
		orderClone.setEntries(Arrays.asList((AbstractOrderEntryModel) entry));

		final ArgumentMatcher<ItemModelCloneCreator.CopyContext> matcher = new ArgumentMatcher<ItemModelCloneCreator.CopyContext>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ItemModelCloneCreator.CopyContext))
				{
					return false;
				}
				final ItemModelCloneCreator.CopyContext copyContext = (ItemModelCloneCreator.CopyContext) argument;

				final OrderEntryModel tmp = new OrderEntryModel();

				if (copyContext.getTargetType(tmp).equals(orderEntryCT))
				{
					return true;
				}

				return false;
			}

		};

		when(typeService.getComposedTypeForClass(CartModel.class)).thenReturn(orderCT);
		when(typeService.getComposedTypeForClass(CartEntryModel.class)).thenReturn(orderEntryCT);
		when(itemModelCloneCreator.copy(eq(orderCT), eq(orderModel), argThat(matcher))).thenReturn(orderClone);

		final CartModel orderClone2 = cloneOrderStrategy.clone(null, null, orderModel, orderCode, CartModel.class,
				CartEntryModel.class);

		verify(typeService).getComposedTypeForClass(CartModel.class);
		verify(typeService).getComposedTypeForClass(CartEntryModel.class);
		verify(itemModelCloneCreator).copy(eq(orderCT), eq(orderModel), argThat(matcher));

		assertThat(orderClone2.getCode()).isEqualTo(orderCode);
	}


	@Test
	public void testCloneNoOriginal()
	{
		try
		{
			cloneOrderStrategy.clone(null, null, null, null, null, null);
			fail();
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage()).isEqualTo("original must not be null!");
		}
		catch (final Exception e)
		{
			fail("wrong exception " + e);
		}
	}

	@Test
	public void testCloneNoabstractOrderClassResult()
	{
		final OrderModel orderModel = new OrderModel();

		final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
		list.add(new OrderEntryModel());
		orderModel.setEntries(list);

		try
		{
			cloneOrderStrategy.clone(null, null, orderModel, "code", null, OrderEntryModel.class);
			fail();
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage()).isEqualTo("abstractOrderClassResult must not be null!");
		}
		catch (final Exception e)
		{
			fail("wrong exception " + e);
		}

	}


	@Test
	public void testCloneNoabstractOrderEntryClassResult()
	{
		final OrderModel orderModel = new OrderModel();

		final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
		list.add(new OrderEntryModel());
		orderModel.setEntries(list);

		try
		{
			cloneOrderStrategy.clone(null, null, orderModel, "code", OrderModel.class, null);
			fail();
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage()).isEqualTo("abstractOrderEntryClassResult must not be null!");
		}
		catch (final Exception e)
		{
			fail("wrong exception " + e);
		}


	}

	@Test
	public void testCloneEntriesRegularOrder()
	{
		final ComposedTypeModel orderEntryCT = new ComposedTypeModel();
		final OrderModel orderModel = new OrderModel();

		final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
		list.add(new OrderEntryModel());
		orderModel.setEntries(list);

		final OrderModel orderClone = new OrderModel();

		final List<AbstractOrderEntryModel> listClone = new ArrayList<AbstractOrderEntryModel>();
		listClone.add(new OrderEntryModel());
		orderClone.setEntries(listClone);

		final List<ItemModel> listEntr = new ArrayList<ItemModel>(orderModel.getEntries());
		final List<ItemModel> cloneListEntr = new ArrayList<ItemModel>(orderModel.getEntries());

		when(itemModelCloneCreator.copyAll(eq(orderEntryCT), eq(listEntr), (ItemModelCloneCreator.CopyContext) Mockito.anyObject()))
				.thenReturn(cloneListEntr);


		orderClone.setEntries(cloneOrderStrategy.cloneEntries(orderEntryCT, orderModel));

		verify(itemModelCloneCreator).copyAll(eq(orderEntryCT), eq(listEntr),
				(ItemModelCloneCreator.CopyContext) Mockito.anyObject());

	}


	@Test
	public void testCloneEntriesRegularOrderReturnNull()
	{
		final ComposedTypeModel orderEntryCT = new ComposedTypeModel();
		final OrderModel orderModel = new OrderModel();

		final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
		list.add(new OrderEntryModel());
		orderModel.setEntries(list);

		final OrderModel orderClone = new OrderModel();

		final List<AbstractOrderEntryModel> listClone = new ArrayList<AbstractOrderEntryModel>();
		listClone.add(new OrderEntryModel());
		orderClone.setEntries(listClone);

		final List<ItemModel> listEntr = new ArrayList<ItemModel>(orderModel.getEntries());

		when(itemModelCloneCreator.copyAll(eq(orderEntryCT), eq(listEntr), (ItemModelCloneCreator.CopyContext) Mockito.anyObject()))
				.thenReturn(null);


		orderClone.setEntries(cloneOrderStrategy.cloneEntries(orderEntryCT, orderModel));

		verify(itemModelCloneCreator).copyAll(eq(orderEntryCT), eq(listEntr),
				(ItemModelCloneCreator.CopyContext) Mockito.anyObject());

		assertThat(orderClone.getEntries()).isEmpty();

	}

	@Test
	public void testCloneEntriesRegularOrderNoType()
	{
		final ComposedTypeModel orderEntryCT = new ComposedTypeModel();
		final OrderModel orderModel = new OrderModel();

		final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
		list.add(new OrderEntryModel());
		orderModel.setEntries(list);

		final OrderModel orderClone = new OrderModel();

		final List<AbstractOrderEntryModel> listClone = new ArrayList<AbstractOrderEntryModel>();
		listClone.add(new OrderEntryModel());
		orderClone.setEntries(listClone);

		final List<ItemModel> listEntr = new ArrayList<ItemModel>(orderModel.getEntries());

		when(itemModelCloneCreator.copyAll(eq(orderEntryCT), eq(listEntr), (ItemModelCloneCreator.CopyContext) Mockito.anyObject()))
				.thenReturn(null);

		when(abstractOrderEntryTypeService.getAbstractOrderEntryType(orderModel)).thenReturn(orderEntryCT);

		orderClone.setEntries(cloneOrderStrategy.cloneEntries(null, orderModel));

		verify(itemModelCloneCreator).copyAll(eq(orderEntryCT), eq(listEntr),
				(ItemModelCloneCreator.CopyContext) Mockito.anyObject());

		verify(abstractOrderEntryTypeService).getAbstractOrderEntryType(orderModel);

		assertThat(orderClone.getEntries()).isEmpty();

	}

}
