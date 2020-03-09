/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.comparators;

import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.util.PriceValue;

@UnitTest
public class VolumeAwarePriceInformationComparatorTest {

	private VolumeAwarePriceInformationComparator comparator;
	private Map<String, Long> qualifiers1;
	private Map<String, Long> qualifiers2;
	
	@Before
	public void setUp()
	{
		comparator = new VolumeAwarePriceInformationComparator();
		qualifiers1 = new HashMap<>();
		qualifiers2 = new HashMap<>();
	}

	@Test
	public void testOneMinQuantityNullValue()
	{
		qualifiers1.put(PriceRow.MINQTD, null);
		qualifiers2.put(PriceRow.MINQTD, 20L);
		PriceValue pv1 = new PriceValue("EUR", 20.0, true);
		PriceValue pv2 = new PriceValue("EUR", 30.0, true);
		final PriceInformation priceInfo1 = new PriceInformation(qualifiers1, pv1);
		final PriceInformation priceInfo2 = new PriceInformation(qualifiers2, pv2);
		Assert.assertEquals("Lowest minqty (or null) is treated as lower", -1, comparator.compare(priceInfo1, priceInfo2));
	}
	
	@Test
	public void testBothMinQuantityNullValues()
	{
		PriceValue pv1 = new PriceValue("EUR", 20.0, true);
		PriceValue pv2 = new PriceValue("EUR", 30.0, true);
		final PriceInformation priceInfo1 = new PriceInformation(pv1);
		final PriceInformation priceInfo2 = new PriceInformation(pv2);
		Assert.assertEquals("Nulls minqty should rather be considered equal", 0, comparator.compare(priceInfo1, priceInfo2));
	}
	
	@Test
	public void testMinQuantityEquals()
	{
		qualifiers1.put(PriceRow.MINQTD, 20L);
		qualifiers2.put(PriceRow.MINQTD, 20L);
		PriceValue pv1 = new PriceValue("EUR", 20.0, true);
		PriceValue pv2 = new PriceValue("EUR", 30.0, true);
		final PriceInformation priceInfo1 = new PriceInformation(qualifiers1, pv1);
		final PriceInformation priceInfo2 = new PriceInformation(qualifiers2, pv2);
		Assert.assertEquals("PriceInformation with higher price value should be greater", -1, comparator.compare(priceInfo1, priceInfo2));
	}
	
	@Test
	public void testMinQuantityNotEquals()
	{
		qualifiers1.put(PriceRow.MINQTD, 20L);
		qualifiers2.put(PriceRow.MINQTD, 30L);
		PriceValue pv1 = new PriceValue("EUR", 30.0, true);
		PriceValue pv2 = new PriceValue("EUR", 20.0, true);
		final PriceInformation priceInfo1 = new PriceInformation(qualifiers1, pv1);
		final PriceInformation priceInfo2 = new PriceInformation(qualifiers2, pv2);
		Assert.assertEquals("PriceInformation with higher minqty value should be greater", -1, comparator.compare(priceInfo1, priceInfo2));
	}

}
