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
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ValueToolsTest extends HybrisJUnit4TransactionalTest
{
	Tax t1, t2;
	Currency c;
	Discount d;

	DiscountValue dv1, dv2, dv3;
	TaxValue tv1, tv2;
	PriceValue pv;

	@Before
	public void setUp() throws Exception
	{
		final C2LManager cm = C2LManager.getInstance();

		c = cm.createCurrency(null, "CCC");

		final OrderManager om = OrderManager.getInstance();

		t1 = om.createTax("testtax1");
		t1.setValue(7.0);
		t2 = om.createTax("testtax2");
		t2.setValue(10.0);

		d = om.createDiscount("testdiscount");
		d.setGlobal(false);
		d.setValue(100.0);
		d.setCurrency(c);

		pv = new PriceValue(c.getIsoCode(), 123.4, true);

		tv1 = new TaxValue(t1.getCode(), t1.getValue().doubleValue(), false, null);
		tv2 = new TaxValue(t2.getCode(), -23.45, true, null);

		dv1 = new DiscountValue(d.getCode(), d.getValue().doubleValue(), true, c.getIsoCode());
		dv2 = new DiscountValue(d.getCode(), 555.5, false, null);
		dv3 = new DiscountValue("blah", 0.0, true, c.getIsoCode());
	}

	@After
	public void tearDown() throws Exception
	{
		d.remove();
		t1.remove();
		t2.remove();
		c.remove();
	}

	@Test
	public void testValueToString()
	{
		try
		{
			// single PV
			final String pvStr = pv.toString();
			final PriceValue _pv = PriceValue.parsePriceValue(pvStr);
			assertTrue("invalid toString(EJBPriceValue): \"" + pvStr + "\" != \"" + _pv + "\"", _pv != null && pv.equals(_pv));
			// single TV
			final String tvStr = tv1.toString();
			final TaxValue _tv = TaxValue.parseTaxValue(tvStr);
			assertTrue("invalid toString(EJBTaxValue): \"" + tvStr + "\" != \"" + _tv + "\"", _tv != null && tv1.equals(_tv));
			// single DV
			final String dvStr = dv1.toString();
			final DiscountValue _dv = DiscountValue.parseDiscountValue(dvStr);
			assertTrue("invalid toString(EJBDiscountValue): \"" + dvStr + "\" != \"" + _dv + "\"", _dv != null && dv1.equals(_dv));
			// privevalue collection test
			Collection coll = new HashSet(Arrays.asList(new Object[]
			{ pv }));
			String collStr = PriceValue.toString(coll);
			Collection res = new HashSet(PriceValue.parsePriceValueCollection(collStr));
			assertEquals("invalid StandardPriceValue.toString(Collection) : " + coll + " != " + res, coll, res);
			// tax value collection test
			coll = new ArrayList(Arrays.asList(new Object[]
			{ tv1, tv2 }));
			collStr = TaxValue.toString(coll);
			res = new ArrayList(TaxValue.parseTaxValueCollection(collStr));
			assertEquals("invalid StandardTaxValue.toString(Collection) : " + coll + " != " + res, coll, res);
			// tax value collection test
			coll = new ArrayList(Arrays.asList(new Object[]
			{ dv1, dv2, dv3 }));
			collStr = DiscountValue.toString(coll);
			res = new ArrayList(DiscountValue.parseDiscountValueCollection(collStr));
			assertEquals("invalid StandardDiscountValue.toString(Collection) : " + coll + " != " + res, coll, res);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}
}
