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
package de.hybris.platform.deliveryzone;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.deliveryzone.constants.ZoneDeliveryModeConstants;
import de.hybris.platform.deliveryzone.jalo.Zone;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryMode;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryModeManager;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CalculationTest extends HybrisJUnit4TransactionalTest
{
	// managers
	ZoneDeliveryModeManager zdm;
	C2LManager c2lManager;
	OrderManager orderManager;
	UserManager userManager;
	ProductManager productManager;
	TypeManager typeManager;
	// items
	Tax tax;
	ZoneDeliveryMode deliveryMode;
	Currency cu1, cu2, oldBase;
	Country co1, co2, co3;
	Zone zone1, zone2;
	User user;
	Address addr;
	Product product;
	Unit unit;
	Order order1, order2;

	@Before
	public void setUp() throws Exception
	{
		// get extension
		zdm = ZoneDeliveryModeManager.getInstance();
		// get C2L-Manager
		c2lManager = C2LManager.getInstance();
		// create some currencies
		assertNotNull(cu1 = c2lManager.createCurrency("cu1"));
		cu1.setConversionFactor(2.0);
		assertNotNull(cu2 = c2lManager.createCurrency("cu2"));
		cu2.setConversionFactor(1.0);
		oldBase = c2lManager.getBaseCurrency();
		c2lManager.setBaseCurrency(cu2);
		// create some countries
		assertNotNull(co1 = c2lManager.createCountry(null, "co1"));
		assertNotNull(co2 = c2lManager.createCountry(null, "co2"));
		assertNotNull(co3 = c2lManager.createCountry(null, "co3"));
		// get OrderManager
		orderManager = OrderManager.getInstance();
		typeManager = TypeManager.getInstance();
		// create DM
		assertNotNull(deliveryMode = (ZoneDeliveryMode) orderManager.createDeliveryMode(
				typeManager.getComposedType(ZoneDeliveryModeConstants.ComposedTypes.ZoneDeliveryMode), "zoneDM1"));
		deliveryMode.setPropertyName("weight");
		// create tax
		assertNotNull(tax = orderManager.createTax("tax"));
		tax.setValue(16);
		deliveryMode.setNet(true);
		// create some zones
		// z1 = { co1, co2 } , z2 = { co3 }		
		assertNotNull(zone1 = zdm.createZone("zone1"));
		assertNotNull(zone2 = zdm.createZone("zone2"));
		// add countries to zones
		zone1.setCountries(new LinkedHashSet(Arrays.asList(new Country[]
		{ co1, co2 })));
		zone2.addToCountries(co3);
		// set some prices
		// z1 -> cu1 -> { 0 => 10.00 , 5 => 8.00 , 10 => 5.00 }
		//    -> cu2 -> { 0 => 100.00 , 100 => 77.00 }
		// z2 -> cu2 -> { 0 => 50.0 10 => 20.00 }
		deliveryMode.setCost(cu1, 0, 10.00, zone1);
		deliveryMode.setCost(cu1, 5, 8.00, zone1);
		deliveryMode.setCost(cu1, 10, 5.00, zone1);
		deliveryMode.setCost(cu2, 0, 100.00, zone1);
		deliveryMode.setCost(cu2, 100, 77.00, zone1);
		deliveryMode.setCost(cu2, 0, 50.00, zone2);
		deliveryMode.setCost(cu2, 10, 20.00, zone2);
		// get UserManager
		userManager = UserManager.getInstance();
		// create user
		assertNotNull(user = userManager.createCustomer("c"));
		// create address
		assertNotNull(addr = userManager.createAddress(user));
		addr.setCountry(co1);
		// get ProductManager
		productManager = ProductManager.getInstance();
		// create product
		assertNotNull(product = productManager.createProduct("p"));
		product.setProperty("weight", new Double(1.0));
		// create unit
		assertNotNull(unit = productManager.createUnit(null, "weight", "test_kg"));
		product.setUnit(unit);
		// create an order
		assertNotNull(order1 = orderManager.createOrder("order1", user, cu1, new Date(), true));
		order1.setDeliveryAddress(addr);
		assertNotNull(order2 = orderManager.createOrder("order2", user, cu1, new Date(), true));
		order2.setDeliveryAddress(addr);
	}

	@Test
	public void testDeliveryCostCalculation() throws Exception
	{
		// check property name
		assertTrue("property name was not 'weight' but " + deliveryMode.getPropertyName(), deliveryMode.getPropertyName().equals("weight"));
		// check zones
		Collection coll = zone1.getCountries();
		assertTrue("expected [ co1, co2 ] but got " + coll,
				coll != null && coll.size() == 2 && coll.contains(co1) && coll.contains(co2));
		coll = zone2.getCountries();
		assertTrue("expected [ co3 ] but got " + coll, coll != null && coll.size() == 1 && coll.contains(co3));
		// z1, cu1, 1kg -> 10.0
		// add entry to order
		order1.addNewEntry(product, 1, unit);
		double costs = deliveryMode.getCost(order1).getValue();
		assertTrue("expected 10.0 as cost but got " + costs, costs == 10.0);
		// z1, cu1 11kg -> 5.0 
		order1.addNewEntry(product, 10, unit);
		costs = deliveryMode.getCost(order1).getValue();
		assertTrue("expected 5.0 as cost but got " + costs, costs == 5.0);
		// z2 , cu1 , 100 kg -> ( 20.0 * 2/1 [conversion] ) 40.0
		order2.getDeliveryAddress().setCountry(co3); // for getting z2 !!!
		order2.addNewEntry(product, 100, unit);
		costs = deliveryMode.getCost(order2).getValue();
		assertTrue("expected 40.0 as cost but got " + costs, costs == 40.0);
		// test getValues(...)
		// z1 -> cu1 -> { 0 => 10.00 , 5 => 8.00 , 10 => 5.00 }
		//    -> cu2 -> { 0 => 100.00 , 100 => 77.00 }
		// z2 -> cu2 -> { 0 => 50.0 10 => 20.00 }
		Map map = deliveryMode.getValues(cu1, zone1);
		assertTrue(
				" expected z1 -> cu1 -> { 0 => 10.00 , 5 => 8.00 , 10 => 5.00 } but got " + map,
				map != null && map.containsKey(new Double(0)) && map.get(new Double(0)).equals(new Double(10.0))
						&& map.containsKey(new Double(5)) && map.get(new Double(5.0)).equals(new Double(8.0))
						&& map.containsKey(new Double(10)) && map.get(new Double(10.0)).equals(new Double(5.0)));
		map = deliveryMode.getValues(cu2, zone1);
		assertTrue(
				" expected z1 -> cu2 -> { 0 => 100.00 , 100 => 77.00 } but got " + map,
				map != null && map.containsKey(new Double(0)) && map.get(new Double(0)).equals(new Double(100.0))
						&& map.containsKey(new Double(100)) && map.get(new Double(100.0)).equals(new Double(77.0)));
		map = deliveryMode.getValues(cu2, zone2);
		assertTrue(" expected z2 -> cu2 -> { 0 => 50.0 10 => 20.00 } but got " + map, map != null && map.containsKey(new Double(0))
				&& map.get(new Double(0)).equals(new Double(50.0)) && map.containsKey(new Double(10))
				&& map.get(new Double(10.0)).equals(new Double(20.0)));
	}

	@After
	public void tearDown() throws Exception
	{
		if (oldBase != null)
		{
			c2lManager.setBaseCurrency(oldBase);
		}
	}
}
