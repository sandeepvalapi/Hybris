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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests {@link OrderEntryService} bean.
 */
@IntegrationTest
public class OrderEntryServiceTest extends ServicelayerTransactionalTest
{

	@Resource
	private OrderEntryService orderEntryService;

	@Resource
	private TypeService typeService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;

	private InMemoryCartModel inMemoryCart;
	private OrderModel order;


	@Before
	public void setUp() throws Exception
	{

		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setActive(Boolean.TRUE);
		curr.setIsocode("PLN");
		curr.setDigits(Integer.valueOf(2));
		curr.setConversion(Double.valueOf(0.76d));
		curr.setSymbol("PLN");

		order = modelService.create(OrderModel.class);
		order.setDate(new Date());
		order.setCurrency(curr);
		order.setUser(userService.getCurrentUser());
		order.setNet(Boolean.FALSE);
		order.setCode("test order");

		inMemoryCart = modelService.create(InMemoryCartModel.class);
		inMemoryCart.setDate(new Date());
		inMemoryCart.setCurrency(curr);
		inMemoryCart.setUser(userService.getCurrentUser());
		inMemoryCart.setNet(Boolean.FALSE);
		inMemoryCart.setCode("test in memory cart");
	}


	@Test
	public void testCreateEntry()
	{
		//corner case
		boolean success = false;
		try
		{
			orderEntryService.createEntry(null);
			Assert.fail("IllegalArgumentException was expected for null order");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		Assert.assertTrue("IllegalArgumentException was expected", success);


		final OrderEntryModel orderEntry = orderEntryService.createEntry(order);
		Assert.assertEquals(order, orderEntry.getOrder());
		Assert.assertTrue(modelService.isNew(orderEntry));
	}

	@Test
	public void testCreateEntryForceType()
	{
		//corner case
		boolean success = false;
		try
		{
			orderEntryService.createEntry(null, order);
			Assert.fail("IllegalArgumentException was expected for null order");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		Assert.assertTrue("IllegalArgumentException was expected", success);

		final AbstractOrderEntryModel forcedAbstractEntry = orderEntryService.createEntry(
				typeService.getComposedTypeForClass(AbstractOrderEntryModel.class), order);
		Assert.assertEquals(order, forcedAbstractEntry.getOrder());

		final AbstractOrderEntryModel forcedOrderEntry = orderEntryService.createEntry(
				typeService.getComposedTypeForClass(OrderEntryModel.class), order);
		Assert.assertEquals(order, forcedOrderEntry.getOrder());
		Assert.assertTrue(forcedOrderEntry instanceof OrderEntryModel);

		final AbstractOrderEntryModel forcedCartEntry = orderEntryService.createEntry(
				typeService.getComposedTypeForClass(CartEntryModel.class), inMemoryCart);
		Assert.assertEquals(inMemoryCart, forcedCartEntry.getOrder());
		Assert.assertTrue(forcedCartEntry instanceof CartEntryModel);
	}

	@Test
	public void testAddRemoveDiscounts()
	{
		final OrderEntryModel entry = orderEntryService.createEntry(order);
		final DiscountValue testDiscount1 = new DiscountValue("testDiscount1", 1, false, null);
		final DiscountValue testDiscount2 = new DiscountValue("testDiscount2", 2, false, null);
		final DiscountValue testDiscount3 = new DiscountValue("testDiscount3", 3, false, null);

		//some corner cases first for adding
		assertAddRemoveDiscountCorenerCase(true, false, null, testDiscount1, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null entry");
		assertAddRemoveDiscountCorenerCase(true, false, entry, null, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null discount");

		orderEntryService.addDiscountValue(entry, testDiscount1);
		assertThat(entry.getDiscountValues()).hasSize(1).contains(testDiscount1);

		assertAddRemoveDiscountCorenerCase(true, true, null, null, Arrays.asList(testDiscount2, testDiscount3),
				IllegalArgumentException.class, "Should have failed with IllegalArgument exception for null entry");
		assertAddRemoveDiscountCorenerCase(true, true, entry, null, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgument exception for null entry");

		//let's add empty collection 
		orderEntryService.addAllDiscountValues(entry, Collections.EMPTY_LIST);
		//expect the same result
		assertThat(entry.getDiscountValues()).hasSize(1).contains(testDiscount1);

		orderEntryService.addAllDiscountValues(entry, Arrays.asList(testDiscount2, testDiscount3));
		assertThat(entry.getDiscountValues()).hasSize(3).contains(testDiscount1, testDiscount2, testDiscount3);

		//and now removing
		//some corner cases first for removing
		assertAddRemoveDiscountCorenerCase(false, false, null, testDiscount1, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null entry");
		assertAddRemoveDiscountCorenerCase(false, false, entry, null, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null entry");

		orderEntryService.removeDiscountValue(entry, testDiscount1);
		assertThat(entry.getDiscountValues()).hasSize(2).contains(testDiscount2, testDiscount3);
		//remove again the same
		orderEntryService.removeDiscountValue(entry, testDiscount1);
		assertThat(entry.getDiscountValues()).hasSize(2).contains(testDiscount2, testDiscount3);
		orderEntryService.removeDiscountValue(entry, testDiscount3);
		assertThat(entry.getDiscountValues()).hasSize(1).contains(testDiscount2);
		orderEntryService.removeDiscountValue(entry, testDiscount2);
		assertThat(entry.getDiscountValues()).isEmpty();

		//is it not persisted?
		Assert.assertTrue(modelService.isNew(entry));

	}

	@Test
	public void testAddRemoveTaxes()
	{
		final OrderEntryModel entry = orderEntryService.createEntry(order);
		final TaxValue testTax1 = new TaxValue("testTax1", 1, false, null);
		final TaxValue testTax2 = new TaxValue("testTax2", 2, false, null);
		final TaxValue testTax3 = new TaxValue("testTax3", 3, false, null);

		//some corner cases first for adding
		assertAddRemoveTaxCorenerCase(true, false, null, testTax1, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null entry");
		assertAddRemoveTaxCorenerCase(true, false, entry, null, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null discount");

		orderEntryService.addTaxValue(entry, testTax1);
		assertThat(entry.getTaxValues()).hasSize(1).contains(testTax1);

		assertAddRemoveTaxCorenerCase(true, true, null, null, Arrays.asList(testTax2, testTax3), IllegalArgumentException.class,
				"Should have failed with IllegalArgument exception for null entry");
		assertAddRemoveTaxCorenerCase(true, true, entry, null, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgument exception for null entry");

		//let's add empty collection 
		orderEntryService.addAllTaxValues(entry, Collections.EMPTY_LIST);
		//expect the same result
		assertThat(entry.getTaxValues()).hasSize(1).contains(testTax1);

		orderEntryService.addAllTaxValues(entry, Arrays.asList(testTax2, testTax3));
		assertThat(entry.getTaxValues()).hasSize(3).contains(testTax1, testTax2, testTax3);

		//and now removing
		//some corner cases first for removing
		assertAddRemoveTaxCorenerCase(false, false, null, testTax1, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null entry");
		assertAddRemoveTaxCorenerCase(false, false, entry, null, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null entry");

		orderEntryService.removeTaxValue(entry, testTax1);
		assertThat(entry.getTaxValues()).hasSize(2).contains(testTax2, testTax3);
		//remove again the same
		orderEntryService.removeTaxValue(entry, testTax1);
		assertThat(entry.getTaxValues()).hasSize(2).contains(testTax2, testTax3);
		orderEntryService.removeTaxValue(entry, testTax3);
		assertThat(entry.getTaxValues()).hasSize(1).contains(testTax2);
		orderEntryService.removeTaxValue(entry, testTax2);
		assertThat(entry.getTaxValues()).isEmpty();

		//is it not persisted?
		Assert.assertTrue(modelService.isNew(entry));
	}

	private void assertAddRemoveDiscountCorenerCase(final boolean add, final boolean collection, final OrderEntryModel entry,
			final DiscountValue discountValue, final List<DiscountValue> discountValues, final Class expectedExceptionClass,
			final String msg)
	{
		boolean success = false;
		try
		{
			if (add)
			{
				if (collection)
				{
					orderEntryService.addAllDiscountValues(entry, discountValues);
				}
				else
				{
					orderEntryService.addDiscountValue(entry, discountValue);
				}
			}
			else
			{
				orderEntryService.removeDiscountValue(entry, discountValue);
			}
			Assert.fail(msg);
		}
		catch (final Exception e)
		{
			success = expectedExceptionClass.isInstance(e);
		}
		Assert.assertTrue(msg, success);
	}

	private void assertAddRemoveTaxCorenerCase(final boolean add, final boolean collection, final OrderEntryModel entry,
			final TaxValue taxValue, final List<TaxValue> taxValues, final Class expectedExceptionClass, final String msg)
	{
		boolean success = false;
		try
		{
			if (add)
			{
				if (collection)
				{
					orderEntryService.addAllTaxValues(entry, taxValues);
				}
				else
				{
					orderEntryService.addTaxValue(entry, taxValue);
				}
			}
			else
			{
				orderEntryService.removeTaxValue(entry, taxValue);
			}
			Assert.fail(msg);
		}
		catch (final Exception e)
		{
			success = expectedExceptionClass.isInstance(e);
		}
		Assert.assertTrue(msg, success);
	}


}
