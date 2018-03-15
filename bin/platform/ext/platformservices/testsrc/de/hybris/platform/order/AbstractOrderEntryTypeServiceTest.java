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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.impl.DefaultAbstractOrderEntryTypeService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.internal.model.order.InMemoryCartEntryModel;
import de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AbstractOrderEntryTypeServiceTest extends ServicelayerTransactionalTest
{

	private DefaultAbstractOrderEntryTypeService abstractOrderEntryTypeService;

	@Resource
	private TypeService typeService;

	@Resource
	private ModelService modelService;



	private OrderModel order;
	private CartModel cart;
	private InMemoryCartModel inMemoryCart;

	@Before
	public void setUp() throws Exception
	{
		abstractOrderEntryTypeService = new DefaultAbstractOrderEntryTypeService();
		abstractOrderEntryTypeService.setTypeService(typeService);
		abstractOrderEntryTypeService.setModelService(modelService);
		//initially no mapping

		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setActive(Boolean.TRUE);
		curr.setIsocode("PLN");
		curr.setDigits(Integer.valueOf(2));
		curr.setConversion(Double.valueOf(0.76d));
		curr.setSymbol("PLN");

		final UserModel testUser = modelService.create(UserModel.class);
		testUser.setUid("testUser");

		order = modelService.create(OrderModel.class);
		order.setDate(new Date());
		order.setCurrency(curr);
		order.setUser(testUser);
		order.setNet(Boolean.FALSE);
		order.setCode("test order");

		cart = modelService.create(CartModel.class);
		cart.setDate(new Date());
		cart.setCurrency(curr);
		cart.setUser(testUser);
		cart.setNet(Boolean.FALSE);
		cart.setCode("test order");

		inMemoryCart = modelService.create(InMemoryCartModel.class);
		inMemoryCart.setDate(new Date());
		inMemoryCart.setCurrency(curr);
		inMemoryCart.setUser(testUser);
		inMemoryCart.setNet(Boolean.FALSE);
		inMemoryCart.setCode("test order");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.order.impl.DefaultAbstractOrderEntryTypeService#getAbstractOrderEntryType(de.hybris.platform.core.model.order.AbstractOrderModel)}
	 * .
	 */
	@Test
	public void testGetAbstractOrderEntryType()
	{
		//should find OrderEntryModel --examine ENTRIES attribute, because there is no order type to orderEnrty type mapping configured
		ComposedTypeModel typeForOrder = abstractOrderEntryTypeService.getAbstractOrderEntryType(order);
		Assert.assertEquals("Unexpected entry type", typeService.getComposedTypeForClass(OrderEntryModel.class), typeForOrder);

		//now I configure sample test mapping
		final Map<String, String> testOrderType2orderEntryTypeMapping = new HashMap<String, String>();
		//map Order -> to CartEntry
		testOrderType2orderEntryTypeMapping.put("Order", "CartEntry");
		abstractOrderEntryTypeService.setOrderType2orderEntryTypeMapping(testOrderType2orderEntryTypeMapping);

		//..when checking entry type for Order now, 
		typeForOrder = abstractOrderEntryTypeService.getAbstractOrderEntryType(order);
		//.. we should get a cartEntry instead
		Assert.assertEquals("Unexpected entry type", typeService.getComposedTypeForClass(CartEntryModel.class), typeForOrder);

		// neither cart nor inMemoryCart inherit from Order, which is configured in the sample test mapping above..
		ComposedTypeModel typeForCart = abstractOrderEntryTypeService.getAbstractOrderEntryType(cart);
		ComposedTypeModel typeForInMemoryCart = abstractOrderEntryTypeService.getAbstractOrderEntryType(inMemoryCart);
		//..so when try to check on entry type, service should return fallback types according to "ENTRIES" atomic type of the examined AbstractOrder type. 
		Assert.assertEquals("Unexpected entry type", typeService.getComposedTypeForClass(CartEntryModel.class), typeForCart);
		Assert.assertEquals("Unexpected entry type", typeService.getComposedTypeForClass(InMemoryCartEntryModel.class),
				typeForInMemoryCart);

		//now, configure additional mapping: map Cart -> OrderEntry
		testOrderType2orderEntryTypeMapping.put("Cart", "OrderEntry");

		//..now if check for cart and subtype of cart..
		typeForCart = abstractOrderEntryTypeService.getAbstractOrderEntryType(cart);
		typeForInMemoryCart = abstractOrderEntryTypeService.getAbstractOrderEntryType(inMemoryCart);

		//..configured OrderEntry type should be returned
		Assert.assertEquals("Unexpected entry type", typeService.getComposedTypeForClass(OrderEntryModel.class), typeForCart);
		Assert.assertEquals("Unexpected entry type", typeService.getComposedTypeForClass(OrderEntryModel.class),
				typeForInMemoryCart);
	}


}
