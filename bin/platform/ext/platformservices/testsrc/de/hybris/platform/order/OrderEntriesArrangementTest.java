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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * See: PLA-10568
 */
@IntegrationTest
public class OrderEntriesArrangementTest extends ServicelayerTest
{

	@Resource
	private UserService userService;

	@Resource
	private ProductService productService;

	@Resource
	private OrderService orderService;

	@Resource
	private ModelService modelService;

	@Resource
	private CommonI18NService commonI18NService;

	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;
	private ProductModel product4;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultUsers();
		createDefaultCatalog();
		product1 = productService.getProductForCode("testProduct1");
		product2 = productService.getProductForCode("testProduct2");
		product3 = productService.getProductForCode("testProduct3");
		product4 = productService.getProductForCode("testProduct4");
	}

	@Test
	public void testManualOrderEntryPositions()
	{
		final OrderModel newOne = createNewOrder();

		// take out entries 
		final List<AbstractOrderEntryModel> entries = newOne.getEntries();
		newOne.setEntries(null);

		modelService.save(newOne);

		Assert.assertFalse(modelService.isNew(newOne));
		Assert.assertEquals(Collections.EMPTY_LIST, newOne.getEntries());

		Assert.assertTrue(modelService.isNew(entries.get(0)));
		Assert.assertTrue(modelService.isNew(entries.get(1)));
		Assert.assertTrue(modelService.isNew(entries.get(2)));
		Assert.assertTrue(modelService.isNew(entries.get(3)));

		// now save in reverse order
		AbstractOrderEntryModel entry = entries.get(3);
		modelService.save(entry);
		Assert.assertEquals(Integer.valueOf(3), entry.getEntryNumber());

		entry = entries.get(2);
		modelService.save(entry);
		Assert.assertEquals(Integer.valueOf(2), entry.getEntryNumber());

		entry = entries.get(1);
		modelService.save(entry);
		Assert.assertEquals(Integer.valueOf(1), entry.getEntryNumber());

		entry = entries.get(0);
		modelService.save(entry);
		Assert.assertEquals(Integer.valueOf(0), entry.getEntryNumber());

		modelService.refresh(newOne);

		Assert.assertFalse(modelService.isNew(newOne));
		Assert.assertFalse(modelService.isNew(entries.get(0)));
		Assert.assertFalse(modelService.isNew(entries.get(1)));
		Assert.assertFalse(modelService.isNew(entries.get(2)));
		Assert.assertFalse(modelService.isNew(entries.get(3)));

		assertEntriesOrder(newOne);
	}

	@Test
	public void testSetEntryNumbersForAdditionalEntries()
	{
		final OrderModel order = createNewOrder();

		modelService.save(order);
		final List<AbstractOrderEntryModel> oldEntries = order.getEntries();

		Assert.assertEquals(Integer.valueOf(0), oldEntries.get(0).getEntryNumber());
		Assert.assertEquals(Integer.valueOf(1), oldEntries.get(1).getEntryNumber());
		Assert.assertEquals(Integer.valueOf(2), oldEntries.get(2).getEntryNumber());
		Assert.assertEquals(Integer.valueOf(3), oldEntries.get(3).getEntryNumber());

		final OrderEntryModel entry1 = createTestOrderEntry(order);
		final OrderEntryModel entry2 = createTestOrderEntry(order);
		modelService.saveAll(entry1, entry2);
		Assert.assertEquals(Integer.valueOf(4), entry1.getEntryNumber());
		Assert.assertEquals(Integer.valueOf(5), entry2.getEntryNumber());
	}

	@Test
	public void testSetEntryNumbersForNewEntriesHalfAutomatically()
	{
		final OrderModel order = createNewOrder();

		modelService.save(order);
		final List<AbstractOrderEntryModel> oldEntries = order.getEntries();

		Assert.assertEquals(Integer.valueOf(0), oldEntries.get(0).getEntryNumber());
		Assert.assertEquals(Integer.valueOf(1), oldEntries.get(1).getEntryNumber());
		Assert.assertEquals(Integer.valueOf(2), oldEntries.get(2).getEntryNumber());
		Assert.assertEquals(Integer.valueOf(3), oldEntries.get(3).getEntryNumber());

		final OrderEntryModel entry1 = createTestOrderEntry(order);
		entry1.setEntryNumber(5);
		final OrderEntryModel entry2 = createTestOrderEntry(order);
		entry2.setEntryNumber(4);
		final OrderEntryModel entry3 = createTestOrderEntry(order);
		final OrderEntryModel entry4 = createTestOrderEntry(order);

		modelService.saveAll(entry1, entry2, entry3, entry4);
		Assert.assertEquals(Integer.valueOf(5), entry1.getEntryNumber());
		Assert.assertEquals(Integer.valueOf(4), entry2.getEntryNumber());
		Assert.assertEquals(Integer.valueOf(6), entry3.getEntryNumber());
		Assert.assertEquals(Integer.valueOf(7), entry4.getEntryNumber());
	}

	private OrderEntryModel createTestOrderEntry(final OrderModel order)
	{
		final OrderEntryModel entry1 = modelService.create(OrderEntryModel.class);
		entry1.setOrder(order);
		entry1.setQuantity(Long.valueOf(1));
		entry1.setUnit(productService.getOrderableUnit(product1));
		entry1.setProduct(product1);
		return entry1;
	}

	@Test
	public void testOrderEntriesArrangement()
	{
		final int maxRuns = 10;
		final boolean logToConsole = false;

		for (int i = 0; i < maxRuns; i++)
		{
			doTestOrderEntriesArrangement(logToConsole, i);
		}
	}

	private void doTestOrderEntriesArrangement(final boolean logToConsole, final int loopNr)
	{
		if (logToConsole)
		{
			System.out.println("\n\n########################################");
			System.out.println("loop# = " + loopNr);
		}

		final OrderModel order = createNewOrder();

		assertEntriesOrder(order);

		if (logToConsole)
		{
			displayEntries(order, "Before save: ");
		}

		assertNew(order);
		modelService.save(order);
		assertSaved(order);


		if (logToConsole)
		{
			displayEntries(order, "After save: ");
		}

		assertEntriesOrder(order);

		modelService.remove(order);
	}


	private void assertEntriesOrder(final OrderModel order)
	{
		if (!modelService.isNew(order))
		{
			Assert.assertEquals("Unexpected product in order entry 1: ", product1, orderService.getEntryForNumber(order, 0)
					  .getProduct());
			Assert.assertEquals("Unexpected product in order entry 2: ", product2, orderService.getEntryForNumber(order, 1)
					  .getProduct());
			Assert.assertEquals("Unexpected product in order entry 3: ", product3, orderService.getEntryForNumber(order, 2)
					  .getProduct());
			Assert.assertEquals("Unexpected product in order entry 4: ", product4, orderService.getEntryForNumber(order, 3)
					  .getProduct());
		}
		Assert.assertEquals("Unexpected product in order entry 1: ", product1, order.getEntries().get(0).getProduct());
		Assert.assertEquals("Unexpected product in order entry 2: ", product2, order.getEntries().get(1).getProduct());
		Assert.assertEquals("Unexpected product in order entry 3: ", product3, order.getEntries().get(2).getProduct());
		Assert.assertEquals("Unexpected product in order entry 4: ", product4, order.getEntries().get(3).getProduct());
	}

	private void assertNew(final OrderModel order)
	{
		Assert.assertTrue(modelService.isNew(order));
		Assert.assertTrue(modelService.isNew(order.getEntries().get(0)));
		Assert.assertTrue(modelService.isNew(order.getEntries().get(1)));
		Assert.assertTrue(modelService.isNew(order.getEntries().get(2)));
		Assert.assertTrue(modelService.isNew(order.getEntries().get(3)));
	}

	private void assertSaved(final OrderModel order)
	{
		Assert.assertFalse(modelService.isNew(order));
		Assert.assertFalse(modelService.isNew(order.getEntries().get(0)));
		Assert.assertFalse(modelService.isNew(order.getEntries().get(1)));
		Assert.assertFalse(modelService.isNew(order.getEntries().get(2)));
		Assert.assertFalse(modelService.isNew(order.getEntries().get(3)));

		Assert.assertTrue(modelService.isUpToDate(order));
		Assert.assertTrue(modelService.isUpToDate(order.getEntries().get(0)));
		Assert.assertTrue(modelService.isUpToDate(order.getEntries().get(1)));
		Assert.assertTrue(modelService.isUpToDate(order.getEntries().get(2)));
		Assert.assertTrue(modelService.isUpToDate(order.getEntries().get(3)));
	}

	private OrderModel createNewOrder()
	{
		final OrderModel order = modelService.create(OrderModel.class);

		final UserModel user = userService.getCurrentUser();

		order.setUser(user);

		order.setCurrency(commonI18NService.getBaseCurrency());
		order.setDate(new java.util.Date());
		order.setNet(Boolean.FALSE);

		final OrderEntryModel entry1 = modelService.create(OrderEntryModel.class);
		entry1.setOrder(order);
		entry1.setQuantity(Long.valueOf(1));
		entry1.setUnit(productService.getOrderableUnit(product1));
		entry1.setProduct(product1);
		entry1.setEntryNumber(Integer.valueOf(0));

		final OrderEntryModel entry2 = modelService.create(OrderEntryModel.class);
		entry2.setOrder(order);
		entry2.setQuantity(Long.valueOf(1));
		entry2.setUnit(productService.getOrderableUnit(product2));
		entry2.setProduct(product2);
		entry2.setEntryNumber(Integer.valueOf(1));

		final OrderEntryModel entry3 = modelService.create(OrderEntryModel.class);
		entry3.setOrder(order);
		entry3.setQuantity(Long.valueOf(1));
		entry3.setUnit(productService.getOrderableUnit(product3));
		entry3.setProduct(product3);
		entry3.setEntryNumber(Integer.valueOf(2));

		final OrderEntryModel entry4 = modelService.create(OrderEntryModel.class);
		entry4.setOrder(order);
		entry4.setQuantity(Long.valueOf(1));
		entry4.setUnit(productService.getOrderableUnit(product4));
		entry4.setProduct(product4);
		entry4.setEntryNumber(Integer.valueOf(3));

		order.setEntries((List) Arrays.asList(entry1, entry2, entry3, entry4));

		return order;
	}

	private void displayEntries(final OrderModel order, final String prefix)
	{
		System.out.println(prefix + "Order@" + System.identityHashCode(order));
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null && !entries.isEmpty())
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				System.out.println(prefix + "Entry@" + System.identityHashCode(entry) + ", Entry.PK=" + entry.getPk()
						  + ", Entry.EntryNumber=" + entry.getEntryNumber() + ", Entry.Product.PK=" + entry.getProduct().getPk()
						  + ", Entry.Product.Code=" + entry.getProduct().getCode());
			}
		}
	}
}
