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

import static org.junit.Assert.*;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class Europe1TargetPriceDiscountsTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;
	
	@Resource
	UserService userService;
	
	@Resource
	CalculationService calculationService;
	
	ProductModel product;
	UnitModel unit;
	DiscountModel discountP1,discountP2,discountP3;
	CurrencyModel currency;
	OrderModel order;
	OrderEntryModel entry;
	
	ProductDiscountGroup discountGroup, otherDiscountGroup;
	
	@Before
	public void setUp()
	{
		CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId("cat");
		
		CatalogVersionModel catVer = modelService.create(CatalogVersionModel.class);
		catVer.setCatalog(cat);
		catVer.setVersion("ver");
		
		product = modelService.create(ProductModel.class);
		product.setCatalogVersion(catVer);
		product.setCode("product");

		currency = modelService.create(CurrencyModel.class);
		currency.setIsocode("XYZ");
		currency.setSymbol("XYZ");
		currency.setActive(Boolean.TRUE);
		currency.setConversion(Double.valueOf(1.0));
		
		discountP1 = modelService.create(DiscountModel.class);
		discountP1.setCode("discount1");
		discountP1.setValue(Double.valueOf(1));

		discountP2 = modelService.create(DiscountModel.class);
		discountP2.setCode("discount2");
		discountP2.setValue(Double.valueOf(1));

		discountP3 = modelService.create(DiscountModel.class);
		discountP3.setCode("discount3");
		discountP3.setValue(Double.valueOf(1));

		unit = modelService.create(UnitModel.class);
		unit.setCode("unit");
		unit.setUnitType("type");
		unit.setConversion(Double.valueOf(1));
		
		PriceRowModel basePrice = modelService.create(PriceRowModel.class);
		basePrice.setProduct(product);
		basePrice.setCurrency(currency);
		basePrice.setUnit(unit);
		basePrice.setPrice(Double.valueOf("99.99"));
		
		order = modelService.create(OrderModel.class);
		order.setCode("order");
		order.setDate(new Date());
		order.setUser(userService.getAnonymousUser());
		order.setCurrency(currency);
		order.setNet(Boolean.TRUE);
		
		entry = modelService.create(OrderEntryModel.class);
		entry.setOrder(order);
		entry.setEntryNumber(Integer.valueOf(1));
		entry.setProduct(product);
		entry.setQuantity(Long.valueOf(10));
		entry.setUnit(unit);
		
		discountGroup = ProductDiscountGroup.valueOf("Group");
		otherDiscountGroup = ProductDiscountGroup.valueOf("OtherGroup");
		
		modelService.saveAll(cat,catVer,product, currency, discountP1, discountP2, discountP3, order, entry, unit, basePrice, discountGroup);
	}
	
	@Test
	public void testNoDiscounts() throws CalculationException
	{
		calculationService.recalculate(order);
		
		assertEquals(Collections.emptyList(), entry.getDiscountValues());

		assertEquals(Double.valueOf("999.90"), entry.getTotalPrice());

		assertEquals(Double.valueOf("999.90"), order.getTotalPrice());
		
	}

	@Test
	public void testTargetPriceDiscount() throws CalculationException
	{
		createTargetPriceDiscountRow(null, discountP1, "79.99");
		
		calculationService.recalculate(order);
		
		assertEquals(1,entry.getDiscountValues().size());
		assertEquals(Double.valueOf("200.00").doubleValue(), entry.getDiscountValues().get(0).getAppliedValue(), 0.00001 );
		assertTrue( entry.getDiscountValues().get(0).isAsTargetPrice() );
		
		assertEquals(Double.valueOf("799.90"), entry.getTotalPrice());
		
		assertEquals(Double.valueOf("799.90"), order.getTotalPrice());
	}

	@Test
	public void testTargetPriceDiscountOverridePDG() throws CalculationException
	{
		createTargetPriceDiscountRow(discountGroup, discountP1, "79.99");

		// 1. try without override --> no discount expected
		calculationService.recalculate(order);
		assertEquals(Collections.emptyList(), entry.getDiscountValues());
		assertEquals(Double.valueOf("999.90"), entry.getTotalPrice());
		assertEquals(Double.valueOf("999.90"), order.getTotalPrice());

		// 2. ovrride PDG at entry  --> discount !!!
		entry.setEurope1PriceFactory_PDG(discountGroup);
		modelService.save(entry);
		
		calculationService.recalculate(order);
		
		assertEquals(1,entry.getDiscountValues().size());
		assertEquals(Double.valueOf("200.00").doubleValue(), entry.getDiscountValues().get(0).getAppliedValue(), 0.00001 );
		assertTrue( entry.getDiscountValues().get(0).isAsTargetPrice() );
		
		assertEquals(Double.valueOf("799.90"), entry.getTotalPrice());
		
		assertEquals(Double.valueOf("799.90"), order.getTotalPrice());

		// 3. override PDG at entry but with OTHER group --> no discount expected 
		entry.setEurope1PriceFactory_PDG(otherDiscountGroup);
		modelService.save(entry);

		calculationService.recalculate(order);

		assertEquals(Collections.emptyList(), entry.getDiscountValues());
		assertEquals(Double.valueOf("999.90"), entry.getTotalPrice());
		assertEquals(Double.valueOf("999.90"), order.getTotalPrice());
	}

	
	@Test
	public void testTargetPriceDiscountAboveBasePrice() throws CalculationException
	{
		createTargetPriceDiscountRow(null, discountP1, "109.99");
		
		calculationService.recalculate(order);
		
		assertEquals(1,entry.getDiscountValues().size());
		assertEquals(Double.valueOf("-100.00").doubleValue(), entry.getDiscountValues().get(0).getAppliedValue(), 0.00001 );
		assertTrue( entry.getDiscountValues().get(0).isAsTargetPrice() );
		
		assertEquals(Double.valueOf("1099.90"), entry.getTotalPrice());
		
		assertEquals(Double.valueOf("1099.90"), order.getTotalPrice());
	}

	@Test
	public void testNegativeTargetPriceDiscount() throws CalculationException
	{
		createTargetPriceDiscountRow(null, discountP1, "-9.99");
		
		calculationService.recalculate(order);
		
		assertEquals(1,entry.getDiscountValues().size());
		assertEquals(Double.valueOf("1099.80").doubleValue(), entry.getDiscountValues().get(0).getAppliedValue(), 0.00001 );
		assertTrue( entry.getDiscountValues().get(0).isAsTargetPrice() );
		
		assertEquals(Double.valueOf("-99.90"), entry.getTotalPrice());
		
		assertEquals(Double.valueOf("-99.90"), order.getTotalPrice());
	}

	
	@Test
	public void testTargetPriceDiscountMultipleTimes() throws CalculationException
	{
		createTargetPriceDiscountRow(null, discountP1, "79.99");
		createTargetPriceDiscountRow(null, discountP2, "69.99");
		
		calculationService.recalculate(order);
		
		assertEquals(2,entry.getDiscountValues().size());
		
		assertEquals(Double.valueOf("200.00").doubleValue(), entry.getDiscountValues().get(0).getAppliedValue(), 0.00001 );
		assertTrue( entry.getDiscountValues().get(0).isAsTargetPrice() );
		
		assertEquals(Double.valueOf("100.00").doubleValue(), entry.getDiscountValues().get(1).getAppliedValue(), 0.00001 );
		assertTrue( entry.getDiscountValues().get(1).isAsTargetPrice() );
		
		assertEquals(Double.valueOf("699.90"), entry.getTotalPrice());
		
		assertEquals(Double.valueOf("699.90"), order.getTotalPrice());
	}

	@Test
	public void testTargetPriceDiscountMixed() throws CalculationException
	{
		createRelativeDiscountRow(null, discountP1, 10);
		createTargetPriceDiscountRow(null, discountP2, "79.99");
		createAbsoluteDiscountRow(null, discountP3, "9.99");
		
		calculationService.recalculate(order);
		
		assertEquals(3,entry.getDiscountValues().size());
		assertEquals(Double.valueOf("99.99").doubleValue(), entry.getDiscountValues().get(0).getAppliedValue(), 0.00001 );
		assertFalse( entry.getDiscountValues().get(0).isAsTargetPrice() );
		assertFalse( entry.getDiscountValues().get(0).isAbsolute() );
		
		assertEquals(Double.valueOf("100.01").doubleValue(), entry.getDiscountValues().get(1).getAppliedValue(), 0.00001 );
		assertTrue( entry.getDiscountValues().get(1).isAsTargetPrice() );
		assertTrue( entry.getDiscountValues().get(1).isAbsolute() );

		assertEquals(Double.valueOf("99.90").doubleValue(), entry.getDiscountValues().get(2).getAppliedValue(), 0.00001 );
		assertFalse( entry.getDiscountValues().get(2).isAsTargetPrice() );
		assertTrue( entry.getDiscountValues().get(2).isAbsolute() );
		
		assertEquals(Double.valueOf("700.00"), entry.getTotalPrice());
		assertEquals(Double.valueOf("700.00"), order.getTotalPrice());
	}

	
	protected DiscountRowModel createRelativeDiscountRow( ProductDiscountGroup group, DiscountModel discount, int percent )
	{
		DiscountRowModel dr = modelService.create(DiscountRowModel.class);
		if( group != null ) 
		{
			dr.setPg(group);
		}
		else 
		{
			dr.setProduct(product);
		}
		dr.setValue(Double.valueOf(percent));
		dr.setDiscount(discount);
		
		modelService.save(dr);
		return dr;
	}

	protected DiscountRowModel createAbsoluteDiscountRow( ProductDiscountGroup group, DiscountModel discount, String value )
	{
		DiscountRowModel dr = modelService.create(DiscountRowModel.class);
		if( group != null ) 
		{
			dr.setPg(group);
		}
		else 
		{
			dr.setProduct(product);
		}
		dr.setValue(Double.valueOf(value));
		dr.setDiscount(discount);
		dr.setCurrency(currency);
		
		modelService.save(dr);
		return dr;
	}

	protected DiscountRowModel createTargetPriceDiscountRow( ProductDiscountGroup group, DiscountModel discount, String price )
	{
		DiscountRowModel dr = modelService.create(DiscountRowModel.class);
		if( group != null ) 
		{
			dr.setPg(group);
		}
		else 
		{
			dr.setProduct(product);
		}
		dr.setValue(Double.valueOf(price));
		dr.setDiscount(discount);
		dr.setCurrency(currency);
		dr.setAsTargetPrice(Boolean.TRUE);
		
		modelService.save(dr);
		return dr;
	}
	
}
