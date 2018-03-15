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
package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator.CopyContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class ItemModelCloneCreatorTest extends ServicelayerTest
{
	private ItemModelCloneCreator itemModelCloneCreator;

	@Resource
	private ModelService modelService;
	@Resource
	private I18NService i18nService;
	@Resource
	private TypeService typeService;


	@Before
	public void setup()
	{
		itemModelCloneCreator = new ItemModelCloneCreator(modelService, i18nService, typeService);
	}

	@Test
	public void copyItemTest()
	{
		final OrderModel order = modelService.create(OrderModel.class);
		final AddressModel address = modelService.create(AddressModel.class);
		order.setDeliveryAddress(address);
		order.setCalculated(Boolean.TRUE);
		order.setCode("CODE_TEST_8234");
		final OrderEntryModel orderEntry = modelService.create(OrderEntryModel.class);
		orderEntry.setCalculated(Boolean.TRUE);
		orderEntry.setBasePrice(Double.valueOf(125151.111));
		final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
		list.add(orderEntry);
		order.setEntries(list);

		final OrderModel orderClone = (OrderModel) itemModelCloneCreator.copy(order);
		Assert.assertNotSame(order, orderClone);
		Assert.assertEquals(order.getCalculated(), orderClone.getCalculated());
		Assert.assertEquals(order.getCode(), orderClone.getCode());
		Assert.assertNotSame(order.getEntries().get(0), orderClone.getEntries().get(0));
		Assert.assertEquals(order.getEntries().get(0).getCalculated(), orderClone.getEntries().get(0).getCalculated());
		Assert.assertEquals(order.getEntries().get(0).getBasePrice(), orderClone.getEntries().get(0).getBasePrice());
		Assert.assertEquals(order.getDeliveryAddress(), orderClone.getDeliveryAddress());
	}

	@Test
	public void copyItemChangeTypeTest()
	{
		final CartModel cart = modelService.create(CartModel.class);
		final AddressModel address = modelService.create(AddressModel.class);
		cart.setDeliveryAddress(address);
		cart.setCalculated(Boolean.TRUE);
		cart.setCode("CODE_TEST_8234");
		final CartEntryModel orderEntry = modelService.create(CartEntryModel.class);
		orderEntry.setCalculated(Boolean.TRUE);
		orderEntry.setBasePrice(Double.valueOf(125151.111));
		final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
		list.add(orderEntry);
		cart.setEntries(list);

		final CopyContext copyContext = new CopyContext()
		{
			@Override
			public ComposedTypeModel getTargetType(final ItemModel originalModel)
			{
				if (originalModel instanceof CartEntryModel)
				{
					return typeService.getComposedTypeForClass(OrderEntryModel.class);
				}
				return super.getTargetType(originalModel);
			}
		};

		final OrderModel orderClone = (OrderModel) itemModelCloneCreator.copy(
				typeService.getComposedTypeForClass(OrderModel.class), cart, copyContext);
		Assert.assertNotSame(cart, orderClone);
		Assert.assertEquals(cart.getCalculated(), orderClone.getCalculated());
		Assert.assertEquals(cart.getCode(), orderClone.getCode());
		Assert.assertNotSame(cart.getEntries().get(0), orderClone.getEntries().get(0));
		Assert.assertEquals(cart.getEntries().get(0).getCalculated(), orderClone.getEntries().get(0).getCalculated());
		Assert.assertEquals(cart.getEntries().get(0).getBasePrice(), orderClone.getEntries().get(0).getBasePrice());
		Assert.assertEquals(cart.getDeliveryAddress(), orderClone.getDeliveryAddress());
	}


	//	public ItemModel copy(ComposedTypeModel targetType, ItemModel original, CopyContext ctx) throws CannotCloneException
	//	{
	//		return itemModelCloneCreator.copy(targetType, original, ctx);
	//	}

}
