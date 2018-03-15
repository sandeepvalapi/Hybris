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
package de.hybris.platform.util;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


@UnitTest
public class DiscountValueTest
{

	@Test
	public void testEquals()
	{
		DiscountValue dv1 = new DiscountValue("test", 9, true, 18, "USD");
		DiscountValue dv2 = new DiscountValue("test", 9, true, 27, "USD");
		assertFalse("DiscountValues are equal but shouldn't be", dv1.equals(dv2));
		dv1 = new DiscountValue("test", 9, true, 18, "USD");
		dv2 = new DiscountValue("test", 9, true, 18, "USD");
		assertTrue("DiscountValues aren't equal but should be", dv1.equals(dv2));
	}

	@Test
	public void testRelative()
	{
		final DiscountValue rel9percent = new DiscountValue("rel", 9, false, null);
		final DiscountValue applied = rel9percent.apply(10, new BigDecimal("199.99").doubleValue(), 2, "EUR");
		assertEquals( new BigDecimal("18.0").doubleValue(), applied.getAppliedValue(), 0.000001 );

		double total = new BigDecimal("199.99").doubleValue() - applied.getAppliedValue();
		assertEquals( new BigDecimal("181.99").doubleValue(), total, 0.000001 );
	}

	@Test
	public void testRelativeNegative()
	{
		final DiscountValue rel9percent = new DiscountValue("rel", -9, false, null);
		final DiscountValue applied = rel9percent.apply(10, new BigDecimal("199.99").doubleValue(), 2, "EUR");
		assertEquals( new BigDecimal("-18.0").doubleValue(), applied.getAppliedValue(), 0.000001 );

		double total = new BigDecimal("199.99").doubleValue() - applied.getAppliedValue();
		assertEquals( new BigDecimal("217.99").doubleValue(), total, 0.000001 );
	}

	
	@Test
	public void testAbsolute()
	{
		final DiscountValue abs13_33 = new DiscountValue("abs", new BigDecimal("13.33").doubleValue(), true, "EUR");
		final DiscountValue applied = abs13_33.apply(10, new BigDecimal("199.99").doubleValue(), 2, "EUR");
		assertEquals( new BigDecimal("133.30").doubleValue(), applied.getAppliedValue(), 0.000001 );

		double total = new BigDecimal("199.99").doubleValue() - applied.getAppliedValue();
		assertEquals( new BigDecimal("66.69").doubleValue(), total, 0.000001 );
	}

	@Test
	public void testAbsoluteNegativeDiscount()
	{
		final DiscountValue abs13_33 = new DiscountValue("abs", new BigDecimal("-13.33").doubleValue(), true, "EUR");
		final DiscountValue applied = abs13_33.apply(10, new BigDecimal("199.99").doubleValue(), 2, "EUR");
		assertEquals( new BigDecimal("-133.30").doubleValue(), applied.getAppliedValue(), 0.000001 );

		double total = new BigDecimal("199.99").doubleValue() - applied.getAppliedValue();
		assertEquals( new BigDecimal("333.29").doubleValue(), total, 0.000001 );
	}

	@Test
	public void testAbsoluteDiscountNegativePrice()
	{
		final DiscountValue abs13_33 = new DiscountValue("abs", new BigDecimal("20.00").doubleValue(), true, "EUR");
		final DiscountValue applied = abs13_33.apply(10, new BigDecimal("199.99").doubleValue(), 2, "EUR");
		assertEquals( new BigDecimal("200.00").doubleValue(), applied.getAppliedValue(), 0.000001 );

		double total = new BigDecimal("199.99").doubleValue() - applied.getAppliedValue();
		assertEquals( new BigDecimal("-0.01").doubleValue(), total, 0.000001 );
	}

	
	@Test
	public void testTargetPrice()
	{
		final DiscountValue tgt11_11 = new DiscountValue("tgt", new BigDecimal("11.11").doubleValue(), "EUR", true);
		
		final DiscountValue applied = tgt11_11.apply(10, new BigDecimal("199.90").doubleValue(), 2, "EUR");
		assertEquals( new BigDecimal("88.80").doubleValue(), applied.getAppliedValue(), 0.000001 );
		
		double total = new BigDecimal("199.90").doubleValue() - applied.getAppliedValue();
		assertEquals( new BigDecimal("111.10").doubleValue(), total, 0.000001 );
	}

	@Test
	public void testNegativeTargetPrice()
	{
		final DiscountValue tgt11_11 = new DiscountValue("tgt", new BigDecimal("-11.11").doubleValue(), "EUR", true);
		
		final DiscountValue applied = tgt11_11.apply(10, new BigDecimal("199.90").doubleValue(), 2, "EUR");
		assertEquals( new BigDecimal("311.00").doubleValue(), applied.getAppliedValue(), 0.000001 );
		
		double total = new BigDecimal("199.90").doubleValue() - applied.getAppliedValue();
		assertEquals( new BigDecimal("-111.10").doubleValue(), total, 0.000001 );
	}

	@Test
	public void testTargetPriceNegativeDiscount()
	{
		final DiscountValue tgt11_11 = new DiscountValue("tgt", new BigDecimal("11.11").doubleValue(), "EUR", true);
		
		final DiscountValue applied = tgt11_11.apply(10, new BigDecimal("100.00").doubleValue(), 2, "EUR");
		assertEquals( new BigDecimal("-11.10").doubleValue(), applied.getAppliedValue(), 0.000001 );
		
		double total = new BigDecimal("100.00").doubleValue() - applied.getAppliedValue();
		assertEquals( new BigDecimal("111.10").doubleValue(), total, 0.000001 );
	}

	
	@Test
	public void testTargetPriceMultipleTimes()
	{
		final DiscountValue tgt11_11 = new DiscountValue("tgt1", new BigDecimal("11.11").doubleValue(), "EUR", true);
		final DiscountValue tgt9_99 = new DiscountValue("tgt2", new BigDecimal("9.99").doubleValue(), "EUR", true);
		
		final List<DiscountValue> all = Arrays.asList(tgt11_11, tgt9_99);
		
		final List<DiscountValue> all_applied = DiscountValue.apply(10, new BigDecimal("199.90").doubleValue(), 2, all, "EUR");

		assertEquals( 2, all_applied.size() );
		assertEquals( new BigDecimal("88.80").doubleValue(), all_applied.get(0).getAppliedValue(), 0.000001 );
		assertEquals( new BigDecimal("11.20").doubleValue(), all_applied.get(1).getAppliedValue(), 0.000001 );
		
		double total = new BigDecimal("199.90").doubleValue() - all_applied.get(0).getAppliedValue() - all_applied.get(1).getAppliedValue();
		assertEquals( new BigDecimal("99.90").doubleValue(), total, 0.000001 );
	}

	@Test
	public void testTargetPriceMixed()
	{
		final DiscountValue tgt10pct = new DiscountValue("10%", new BigDecimal("10").doubleValue(), false, null );
		final DiscountValue tgt11_11 = new DiscountValue("tgt1", new BigDecimal("11.11").doubleValue(), "EUR", true);
		final DiscountValue tgt5EUR = new DiscountValue("5EUR", new BigDecimal("5.00").doubleValue(), true, "EUR" );
		
		final List<DiscountValue> all = Arrays.asList(tgt10pct, tgt11_11, tgt5EUR);
		
		final List<DiscountValue> all_applied = DiscountValue.apply(10, new BigDecimal("199.90").doubleValue(), 2, all, "EUR");

		assertEquals( 3, all_applied.size() );
		assertEquals( new BigDecimal("19.99").doubleValue(), all_applied.get(0).getAppliedValue(), 0.000001 );
		assertEquals( new BigDecimal("68.81").doubleValue(), all_applied.get(1).getAppliedValue(), 0.000001 );
		assertEquals( new BigDecimal("50.00").doubleValue(), all_applied.get(2).getAppliedValue(), 0.000001 );
		
		double total = new BigDecimal("199.90").doubleValue() - all_applied.get(0).getAppliedValue() - all_applied.get(1).getAppliedValue() - all_applied.get(2).getAppliedValue();
		assertEquals( new BigDecimal("61.10").doubleValue(), total, 0.000001 );
	}

	@Test
	public void testStringSerialization()
	{
		final DiscountValue dv_abs = new DiscountValue("code", new BigDecimal("123.456").doubleValue(),true, "EUR");
		final DiscountValue dv_rel = new DiscountValue("code", new BigDecimal("19.0").doubleValue(),false, null);
		final DiscountValue dv_tgt = new DiscountValue("code", new BigDecimal("199.99").doubleValue(),"EUR", true);
		
		final List<DiscountValue> coll = Arrays.asList(dv_abs, dv_rel, dv_tgt);
		
		assertEquals("<DV<code#123.456#true#0.0#EUR#false>VD>", dv_abs.toString() );
		assertEquals("<DV<code#19.0#false#0.0#NULL#false>VD>", dv_rel.toString() );
		assertEquals("<DV<code#199.99#true#0.0#EUR#true>VD>", dv_tgt.toString() );
		
		assertEquals(
				"["+
				"<DV<code#123.456#true#0.0#EUR#false>VD>" + 
				"|" +	"<DV<code#19.0#false#0.0#NULL#false>VD>" +
				"|" +	"<DV<code#199.99#true#0.0#EUR#true>VD>" +
				"]", DiscountValue.toString(coll) );
		
		final DiscountValue dv_abs_parsed = DiscountValue.parseDiscountValue("<DV<code#123.456#true#0.0#EUR#false>VD>");
		final DiscountValue dv_abs_parsed_legacy = DiscountValue.parseDiscountValue("<DV<code#123.456#true#0.0#EUR>VD>");
		assertEquals( dv_abs, dv_abs_parsed );
		assertEquals( dv_abs, dv_abs_parsed_legacy );

		final DiscountValue dv_rel_parsed = DiscountValue.parseDiscountValue("<DV<code#19.0#false#0.0#NULL#false>VD>");
		final DiscountValue dv_rel_parsed_legacy = DiscountValue.parseDiscountValue("<DV<code#19.0#false#0.0#NULL>VD>");
		assertEquals( dv_rel, dv_rel_parsed );
		assertEquals( dv_rel, dv_rel_parsed_legacy );

		final DiscountValue dv_tgt_parsed = DiscountValue.parseDiscountValue("<DV<code#199.99#true#0.0#EUR#true>VD>");
		assertEquals( dv_tgt, dv_tgt_parsed );
	}

	
}
