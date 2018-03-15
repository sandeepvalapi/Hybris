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

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests corner cases pointed out in PLA-10597.
 */
@IntegrationTest
public class AddNewEntryCornerCaseTest extends ServicelayerTransactionalTest
{
	@Resource
	private CartService cartService;
	@Resource
	private OrderService orderService;
	@Resource
	private ProductService productService;
	@Resource
	private ModelService modelService;

	private ProductModel product0;
	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();

		product0 = productService.getProductForCode("testProduct0");
		product1 = productService.getProductForCode("testProduct1");
		product2 = productService.getProductForCode("testProduct2");
		product3 = productService.getProductForCode("testProduct3");
	}


	// Add 2 entries, remove first, append new entry:
	//Added:
	// 0 -- 1 
	//removed:
	// X -- 1
	//appended:
	//X -- 1 -- 2
	@Test
	public void testRemoveEntryAppendEntry() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		//pos:0
		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		cartService.saveOrder(cart);

		//pos:1
		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, -1, false);
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());

		// Remove the first entry
		modelService.remove(cartEntry0);
		modelService.refresh(cart);

		// Append a new entry
		final CartEntryModel cartEntry2 = cartService.addNewEntry(cart, product2, 1, null, -1, false);
		assertEquals("New entry does not have requested entry number", Integer.valueOf(2), cartEntry2.getEntryNumber());
		cartService.saveOrder(cart);
	}

	// Add 3 entries, remove middle entry, add new entry at position of 3rd entry
	// Added:
	// 0 -- 1 -- 2
	//Removed:
	// 0 -- X -- 2
	//Add to position 2 - possible since we are still in carts domain
	// 0 -- X -- (2) -- 3

	@Test
	public void testReplaceRemovedEntry() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		cartService.saveOrder(cart);

		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, -1, false);
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		cartService.saveOrder(cart);

		final CartEntryModel cartEntry2 = cartService.addNewEntry(cart, product2, 1, null, -1, false);
		assertEquals(Integer.valueOf(2), cartEntry2.getEntryNumber());
		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry2.getEntryNumber());

		// Remove the second entry
		modelService.remove(cartEntry1);
		modelService.refresh(cart);

		// Add a new entry at the same position (2) of the third entry - which should be moved out of the way

		final CartEntryModel cartEntry2b = cartService.addNewEntry(cart, product3, 1, null, 2, false);

		assertEquals("New entry should be at position 2", Integer.valueOf(2), cartEntry2b.getEntryNumber());
		assertEquals("The third entry should be moved to position 3", Integer.valueOf(3), cartEntry2.getEntryNumber());
		cartService.saveOrder(cart);
		assertEquals("New entry should be at position 2", Integer.valueOf(2), cartEntry2b.getEntryNumber());
		assertEquals("The third entry should be moved to position 3", Integer.valueOf(3), cartEntry2.getEntryNumber());

		orderService.createOrderFromCart(cart);
	}

	// Add 3 entries, remove first 2 entries, add new entry at available position:
	// Added : 
	// 0 -- 1 -- 2
	//remove:
	// X -- X -- 2
	// Add at position 1 : collision free
	// X -- (1)! -- 2
	// Append new entry:
	// X -- (1)! -- 2 -- 3
	// Add at position 0 : collision free
	// (0)! -- 1 -- 2 -- 3


	@Test
	public void testAddAtAvailableSlot() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		cartService.saveOrder(cart);

		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, -1, false);
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		cartService.saveOrder(cart);

		final CartEntryModel cartEntry2 = cartService.addNewEntry(cart, product2, 1, null, -1, false);
		assertEquals(Integer.valueOf(2), cartEntry2.getEntryNumber());
		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry2.getEntryNumber());

		// Remove the first and second entry
		modelService.remove(cartEntry0);
		modelService.remove(cartEntry1);
		modelService.refresh(cart);

		// Add a new entry at position (1) which should be available
		final CartEntryModel cartEntry3 = cartService.addNewEntry(cart, product3, 1, null, 1, false);
		assertEquals("New entry does not have requested entry number", Integer.valueOf(1), cartEntry3.getEntryNumber());
		cartService.saveOrder(cart);

		//Append entry
		final CartEntryModel cartEntry4 = cartService.addNewEntry(cart, product3, 1, null, -1, false);
		assertEquals("New entry does not have requested entry number", Integer.valueOf(3), cartEntry4.getEntryNumber());
		cartService.saveOrder(cart);

		// Add a new entry at position (0) which should be available
		final CartEntryModel cartEntry5 = cartService.addNewEntry(cart, product3, 1, null, 0, false);
		assertEquals("New entry does not have requested entry number", Integer.valueOf(0), cartEntry5.getEntryNumber());
		cartService.saveOrder(cart);


	}

	// Add 2 entries without saving in between
	@Test
	public void testDoubleAdd() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, -1, false);

		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
	}

	@Test
	public void testDoubleAddWithEntryInsert() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		//at the end
		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		//at pos 0
		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, 0, false);

		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(1), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(0), cartEntry1.getEntryNumber());
	}

	@Test
	public void testDoubleAddWithEntryInsertAfterSave() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, -1, false);

		cartService.saveOrder(cart);
		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());

		final CartEntryModel cartEntry2 = cartService.addNewEntry(cart, product0, 1, null, 0, false);
		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry1.getEntryNumber());

		final CartEntryModel cartEntry3 = cartService.addNewEntry(cart, product0, 1, null, 2, false);
		final CartEntryModel cartEntry4 = cartService.addNewEntry(cart, product1, 1, null, 1, false);
		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(2), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(4), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(0), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(3), cartEntry3.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry4.getEntryNumber());
	}


	@Test
	public void testNewEntryAddingInVariousOrder() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, 0, false);
		assertEquals(Integer.valueOf(0), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry0.getEntryNumber());
		final CartEntryModel cartEntry2 = cartService.addNewEntry(cart, product1, 1, null, 10, false);
		assertEquals(Integer.valueOf(10), cartEntry2.getEntryNumber());

		cartService.saveOrder(cart);
		assertEquals(Integer.valueOf(0), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(10), cartEntry2.getEntryNumber());

		assertEquals(Integer.valueOf(0), cart.getEntries().get(0).getEntryNumber());
		assertEquals(Integer.valueOf(1), cart.getEntries().get(1).getEntryNumber());
		assertEquals(Integer.valueOf(10), cart.getEntries().get(2).getEntryNumber());

		//add at beginning : shifts all one number up
		final CartEntryModel cartEntry4 = cartService.addNewEntry(cart, product1, 1, null, 0, false);
		//and at the end:
		final CartEntryModel cartEntry5 = cartService.addNewEntry(cart, product2, 1, null, -1, false);

		assertEquals(Integer.valueOf(0), cartEntry4.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(11), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(12), cartEntry5.getEntryNumber());

		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry4.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(11), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(12), cartEntry5.getEntryNumber());

		assertEquals(Integer.valueOf(0), cart.getEntries().get(0).getEntryNumber());
		assertEquals(Integer.valueOf(1), cart.getEntries().get(1).getEntryNumber());
		assertEquals(Integer.valueOf(2), cart.getEntries().get(2).getEntryNumber());
		assertEquals(Integer.valueOf(11), cart.getEntries().get(3).getEntryNumber());
		assertEquals(Integer.valueOf(12), cart.getEntries().get(4).getEntryNumber());

		//add two in the middle
		final CartEntryModel cartEntry7 = cartService.addNewEntry(cart, product2, 1, null, 2, false);
		final CartEntryModel cartEntry6 = cartService.addNewEntry(cart, product2, 1, null, 2, false);

		assertEquals(Integer.valueOf(0), cartEntry4.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry6.getEntryNumber());
		assertEquals(Integer.valueOf(3), cartEntry7.getEntryNumber());
		assertEquals(Integer.valueOf(4), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(13), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(14), cartEntry5.getEntryNumber());

		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry4.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry6.getEntryNumber());
		assertEquals(Integer.valueOf(3), cartEntry7.getEntryNumber());
		assertEquals(Integer.valueOf(4), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(13), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(14), cartEntry5.getEntryNumber());

		assertEquals(Integer.valueOf(0), cart.getEntries().get(0).getEntryNumber());
		assertEquals(Integer.valueOf(1), cart.getEntries().get(1).getEntryNumber());
		assertEquals(Integer.valueOf(2), cart.getEntries().get(2).getEntryNumber());
		assertEquals(Integer.valueOf(3), cart.getEntries().get(3).getEntryNumber());
		assertEquals(Integer.valueOf(4), cart.getEntries().get(4).getEntryNumber());
		assertEquals(Integer.valueOf(13), cart.getEntries().get(5).getEntryNumber());
		assertEquals(Integer.valueOf(14), cart.getEntries().get(6).getEntryNumber());

		//remove first entries
		modelService.remove(cartEntry4);
		modelService.remove(cartEntry1);
		modelService.refresh(cart);

		//append two entries
		final CartEntryModel cartEntry8 = cartService.addNewEntry(cart, product3, 1, null, -1, false);
		final CartEntryModel cartEntry9 = cartService.addNewEntry(cart, product1, 1, null, -1, false);


		assertEquals(Integer.valueOf(2), cartEntry6.getEntryNumber());
		assertEquals(Integer.valueOf(3), cartEntry7.getEntryNumber());
		assertEquals(Integer.valueOf(4), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(13), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(14), cartEntry5.getEntryNumber());
		assertEquals(Integer.valueOf(15), cartEntry8.getEntryNumber());
		assertEquals(Integer.valueOf(16), cartEntry9.getEntryNumber());

		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(2), cartEntry6.getEntryNumber());
		assertEquals(Integer.valueOf(3), cartEntry7.getEntryNumber());
		assertEquals(Integer.valueOf(4), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(13), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(14), cartEntry5.getEntryNumber());
		assertEquals(Integer.valueOf(15), cartEntry8.getEntryNumber());
		assertEquals(Integer.valueOf(16), cartEntry9.getEntryNumber());

		assertEquals(Integer.valueOf(2), cart.getEntries().get(0).getEntryNumber());
		assertEquals(Integer.valueOf(3), cart.getEntries().get(1).getEntryNumber());
		assertEquals(Integer.valueOf(4), cart.getEntries().get(2).getEntryNumber());
		assertEquals(Integer.valueOf(13), cart.getEntries().get(3).getEntryNumber());
		assertEquals(Integer.valueOf(14), cart.getEntries().get(4).getEntryNumber());
		assertEquals(Integer.valueOf(15), cart.getEntries().get(5).getEntryNumber());
		assertEquals(Integer.valueOf(16), cart.getEntries().get(6).getEntryNumber());

		//fill the gap at the beginning:

		final CartEntryModel cartEntry10 = cartService.addNewEntry(cart, product3, 1, null, 0, false);
		final CartEntryModel cartEntry11 = cartService.addNewEntry(cart, product1, 1, null, 1, false);

		assertEquals(Integer.valueOf(0), cartEntry10.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry11.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry6.getEntryNumber());
		assertEquals(Integer.valueOf(3), cartEntry7.getEntryNumber());
		assertEquals(Integer.valueOf(4), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(13), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(14), cartEntry5.getEntryNumber());
		assertEquals(Integer.valueOf(15), cartEntry8.getEntryNumber());
		assertEquals(Integer.valueOf(16), cartEntry9.getEntryNumber());

		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry10.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry11.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry6.getEntryNumber());
		assertEquals(Integer.valueOf(3), cartEntry7.getEntryNumber());
		assertEquals(Integer.valueOf(4), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(13), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(14), cartEntry5.getEntryNumber());
		assertEquals(Integer.valueOf(15), cartEntry8.getEntryNumber());
		assertEquals(Integer.valueOf(16), cartEntry9.getEntryNumber());

		assertEquals(Integer.valueOf(0), cart.getEntries().get(0).getEntryNumber());
		assertEquals(Integer.valueOf(1), cart.getEntries().get(1).getEntryNumber());
		assertEquals(Integer.valueOf(2), cart.getEntries().get(2).getEntryNumber());
		assertEquals(Integer.valueOf(3), cart.getEntries().get(3).getEntryNumber());
		assertEquals(Integer.valueOf(4), cart.getEntries().get(4).getEntryNumber());
		assertEquals(Integer.valueOf(13), cart.getEntries().get(5).getEntryNumber());
		assertEquals(Integer.valueOf(14), cart.getEntries().get(6).getEntryNumber());
		assertEquals(Integer.valueOf(15), cart.getEntries().get(7).getEntryNumber());
		assertEquals(Integer.valueOf(16), cart.getEntries().get(8).getEntryNumber());
	}


	@Test
	public void testAddNewEntryAtHighEntryNumber() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();
		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null);
		assertEquals("New entry does not have requested entry number", Integer.valueOf(0), cartEntry0.getEntryNumber());
		cartService.saveOrder(cart);
		assertEquals("New entry does not have requested entry number", Integer.valueOf(0), cartEntry0.getEntryNumber());

		// Add a new entry and request the entry number 99 (which is available)
		final CartEntryModel cartEntry = cartService.addNewEntry(cart, product0, 1, null, 99, false);

		// Check if the entry has been given entry number 99.
		assertEquals("New entry does not have requested entry number", Integer.valueOf(99), cartEntry.getEntryNumber());

		//now append:
		final CartEntryModel cartEntry100 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		// Check if the entry has been given entry number 100.
		assertEquals("New entry does not have requested entry number", Integer.valueOf(100), cartEntry100.getEntryNumber());

		//now add at position 1:
		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product0, 1, null, 1, false);
		// Check if the entry has been given entry number 1.
		assertEquals("New entry does not have requested entry number", Integer.valueOf(1), cartEntry1.getEntryNumber());

	}

	// Show that save is required
	@Test
	public void testAddNewEntryRequiresSave() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final CartEntryModel cartEntry = cartService.addNewEntry(cart, product0, 1, null);

		assertTrue("Entry is not saved as javadoc says.", modelService.isNew(cartEntry));
	}

	@Test
	public void testAddNewEntryToOrder() throws InvalidCartException
	{
		final CartModel cart = cartService.getSessionCart();

		//pos:0
		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		//pos:1
		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, -1, false);
		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		cartService.saveOrder(cart);

		//try add at pos 0
		final CartEntryModel cartEntry2 = cartService.addNewEntry(cart, product2, 1, null, 0, false);

		assertEquals(Integer.valueOf(0), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry1.getEntryNumber());

		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry2.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry1.getEntryNumber());

		final OrderModel order = orderService.createOrderFromCart(cart);

		//try to insert entry at position 1:
		try
		{
			orderService.addNewEntry(order, product0, 1, null, 1, false);
			Assert.fail("should have failed with AmbiguousIdentifierException");
		}
		catch (final AmbiguousIdentifierException e)
		{
			//ok!
		}
		catch (final Exception e)
		{
			Assert.fail("should have failed with AmbiguousIdentifierException");
		}

		//but appending should work fine:

		final OrderEntryModel orderEntryModel = orderService.addNewEntry(order, product2, 1, null, -1, false);

		assertEquals(Integer.valueOf(3), orderEntryModel.getEntryNumber());
		orderService.saveOrder(order);
		assertEquals(Integer.valueOf(3), orderEntryModel.getEntryNumber());

	}


	@Test
	public void testPLA11234() throws InvalidCartException
	{
		final CartModel cart = cartService.getSessionCart();

		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		cartService.saveOrder(cart);

		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, -1, false);
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		cartService.saveOrder(cart);

		final CartEntryModel cartEntry2 = cartService.addNewEntry(cart, product2, 1, null, -1, false);
		assertEquals(Integer.valueOf(2), cartEntry2.getEntryNumber());
		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry2.getEntryNumber());

		// Remove the second entry
		modelService.remove(cartEntry1);
		modelService.refresh(cart);

		cartService.saveOrder(cart);
		final OrderModel order = orderService.createOrderFromCart(cart);
		Assertions.assertThat(order).isNotNull();

		Assertions.assertThat(order.getEntries()).hasSize(2);
	}
}
