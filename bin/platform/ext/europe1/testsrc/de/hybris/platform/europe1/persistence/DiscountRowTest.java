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
package de.hybris.platform.europe1.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.DiscountRow;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.persistence.order.price.EJBPriceFactoryException;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.StandardDateRange;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DiscountRowTest extends HybrisJUnit4Test
{
	Product product;
	DiscountRow discountRow;
	Currency currency;
	Discount dis;
	EnumerationValue userGroup;

	Europe1PriceFactory europe1;

	@Before
	public void setUp() throws Exception
	{
		europe1 = Europe1PriceFactory.getInstance();
		assertNotNull(currency = C2LManager.getInstance().createCurrency("europe1/dr"));
		assertNotNull(product = ProductManager.getInstance().createProduct("europe1/discount"));
		assertNotNull(dis = OrderManager.getInstance().createDiscount("dis"));
		assertNotNull(userGroup = EnumerationManager.getInstance().createEnumerationValue(Europe1Constants.TYPES.DISCOUNT_USER_GROUP,
				"test"));

		assertNotNull(discountRow = europe1.createDiscountRow(product, null, null, userGroup, currency, new Double(0.0), null, dis));
	}

	@Test
	public void testTransaction() throws EJBPriceFactoryException, ConsistencyCheckException
	{
		// dont use 0 because it's reserved !
		final StandardDateRange range = new StandardDateRange(new Date(10000L), new Date(100000L));

		final Transaction tx = Transaction.current();
		tx.begin();
		try
		{
			assertEquals(null, discountRow.getDateRange());
			assertEquals(0.0, discountRow.getValue().doubleValue(), 0.0);

			discountRow.setDateRange(range);
			discountRow.setValue(new Double(42.1));
			assertEquals(range, discountRow.getDateRange());
			assertEquals(42.1, discountRow.getValue().doubleValue(), 0.0);
		}
		finally
		{
			tx.rollback();
		}
		assertEquals(null, discountRow.getDateRange());
		assertEquals(0.0, discountRow.getValue().doubleValue(), 0.0);
	}
}
