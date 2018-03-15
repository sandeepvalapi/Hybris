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
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.testframework.Assert;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DiscountServiceTest extends ServicelayerTransactionalTest
{

	@Resource
	private DiscountService discountService;

	/**
	 * Creates the core data, and 4 discounts.
	 */
	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/servicelayer/test/testDiscount.csv", "windows-1252");
	}

	/**
	 * Tries to search the discount with code:
	 * <ul>
	 * <li>successful with "totally_free",</li>
	 * <li>caught UnknownIdentifierException with "No_Such_Discount",</li>
	 * <li>found two discounts, that both contains "percent".</li>
	 * </ul>
	 */
	@Test
	public void testGetDiscount()
	{
		String discountCode = "totally_free";
		DiscountModel discount = discountService.getDiscountForCode(discountCode);
		assertNotNull(discount);
		assertEquals(discountCode, discount.getCode());

		discountCode = "No_Such_Discount";
		try
		{
			discount = discountService.getDiscountForCode(discountCode);
			fail("the discount code [" + discountCode + "] should NOT be found.");
		}
		catch (final UnknownIdentifierException ue)
		{
			//expected
		}

		discountCode = "percent";
		final Collection<DiscountModel> discounts = discountService.getDiscountsForCode("%" + discountCode + "%");
		assertEquals(2, discounts.size());
		for (final DiscountModel _discount : discounts)
		{
			final boolean found = _discount.getCode().indexOf(discountCode) != -1;
			assertTrue(found);
		}
	}

	/**
	 * Tests {@link DiscountService#getDiscountsForCurrency(de.hybris.platform.core.model.c2l.CurrencyModel)}
	 */
	@Test
	public void testGetDiscountByCurrency()
	{
		boolean success = false;
		try
		{
			discountService.getDiscountsForCurrency(null);
			fail("IllegalArgumentException was expected for null currency");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		assertTrue("IllegalArgumentException was expected for null currency", success);

		final DiscountModel discount = discountService.getDiscountForCode("tenAndALittleOff");
		final Collection<DiscountModel> eurDiscounts = discountService.getDiscountsForCurrency(discount.getCurrency());
		Assert.assertCollectionElements(eurDiscounts, discount);
		assertEquals("Expected size of EUR discounts is 1", 1, eurDiscounts.size());


	}
}
