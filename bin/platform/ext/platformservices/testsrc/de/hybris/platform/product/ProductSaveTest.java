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
package de.hybris.platform.product;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ProductSaveTest extends ServicelayerTransactionalTest
{

	private CatalogVersionModel onlinecv = null;

	@Resource
	private I18NService i18nService;

	@Resource
	private ModelService modelService;

	@Resource
	private ProductService productService;


	@Before
	public void setUp() throws Exception
	{
		//creating a catalog with 2 catalog versions
		final CatalogModel defaultcatalog = modelService.create(CatalogModel.class);
		onlinecv = modelService.create(CatalogVersionModel.class);
		final CatalogVersionModel stagedcv = modelService.create(CatalogVersionModel.class);

		onlinecv.setActive(Boolean.TRUE);
		stagedcv.setActive(Boolean.FALSE);
		onlinecv.setVersion("online");
		stagedcv.setVersion("staged");
		onlinecv.setCatalog(defaultcatalog);
		stagedcv.setCatalog(defaultcatalog);
		defaultcatalog.setId("default");

		modelService.saveAll();

		final Currency eur = C2LManager.getInstance().createCurrency("EUR");
		eur.setActive(true);
		eur.setName("Euro");
		eur.setBase();
		eur.setConversionFactor(1.00);
	}

	@Test
	public void testSaveAll() //getProduct(code)
	{
		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setCode("specialunit");
		unit.setConversion(Double.valueOf(1.0));
		unit.setName("special Unit", Locale.ENGLISH);
		unit.setUnitType("something");

		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("prodWithPrice");
		product.setCatalogVersion(onlinecv);
		product.setApprovalStatus(ArticleApprovalStatus.APPROVED);

		final PriceRowModel pricerow = modelService.create(PriceRowModel.class);
		pricerow.setCurrency(i18nService.getCurrency("EUR"));
		pricerow.setMinqtd(Long.valueOf(1));
		pricerow.setNet(Boolean.TRUE);
		pricerow.setPrice(Double.valueOf(2.34));
		pricerow.setUnit(unit);
		pricerow.setProduct(product);
		pricerow.setCatalogVersion(onlinecv);

		modelService.saveAll(Arrays.asList(pricerow, unit, product)); //should not throw any error

		final ProductModel resprod = productService.getProduct("prodWithPrice");
		assertNotNull("prod not found", resprod);
		assertEquals("", "specialunit", productService.getOrderableUnit(resprod).getCode());

	}

	@Test
	public void testSaveOnlyPriceRow() //getProduct(code)
	{
		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setCode("specialunit");
		unit.setConversion(Double.valueOf(1.0));
		unit.setName("special Unit", Locale.ENGLISH);
		unit.setUnitType("something");

		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("prodWithPrice");
		product.setCatalogVersion(onlinecv);
		product.setApprovalStatus(ArticleApprovalStatus.APPROVED);

		final PriceRowModel pricerow = modelService.create(PriceRowModel.class);
		pricerow.setCurrency(i18nService.getCurrency("EUR"));
		pricerow.setMinqtd(Long.valueOf(1));
		pricerow.setNet(Boolean.TRUE);
		pricerow.setPrice(Double.valueOf(2.34));
		pricerow.setUnit(unit);
		pricerow.setProduct(product);
		pricerow.setCatalogVersion(onlinecv);

		modelService.save(pricerow); //should not throw any error, automatically saves product and unit too

		final ProductModel resprod = productService.getProduct("prodWithPrice");
		assertNotNull("prod not found", resprod);
		assertEquals("", "specialunit", productService.getOrderableUnit(resprod).getCode());
	}

	@Test
	public void testSaveMoreModelsTogether1()
	{
		//pricerow1 -> product1 -> unit1
		//pricerow2 -> product2 -> unit1
		final UnitModel unit1 = modelService.create(UnitModel.class);
		unit1.setCode("specialunit1");
		unit1.setConversion(Double.valueOf(1.0));
		unit1.setName("special Unit", Locale.ENGLISH);
		unit1.setUnitType("something");

		final ProductModel product1 = modelService.create(ProductModel.class);
		product1.setCode("prodWithPrice1");
		product1.setCatalogVersion(onlinecv);
		product1.setApprovalStatus(ArticleApprovalStatus.APPROVED);

		final ProductModel product2 = modelService.create(ProductModel.class);
		product2.setCode("prodWithPrice2");
		product2.setCatalogVersion(onlinecv);
		product2.setApprovalStatus(ArticleApprovalStatus.APPROVED);

		final PriceRowModel pricerow1 = modelService.create(PriceRowModel.class);
		pricerow1.setCurrency(i18nService.getCurrency("EUR"));
		pricerow1.setMinqtd(Long.valueOf(1));
		pricerow1.setNet(Boolean.TRUE);
		pricerow1.setPrice(Double.valueOf(2.34));
		pricerow1.setUnit(unit1);
		pricerow1.setProduct(product1);
		pricerow1.setCatalogVersion(onlinecv);

		final PriceRowModel pricerow2 = modelService.create(PriceRowModel.class);
		pricerow2.setCurrency(i18nService.getCurrency("EUR"));
		pricerow2.setMinqtd(Long.valueOf(1));
		pricerow2.setNet(Boolean.TRUE);
		pricerow2.setPrice(Double.valueOf(2.34));
		pricerow2.setUnit(unit1);
		pricerow2.setProduct(product2);
		pricerow2.setCatalogVersion(onlinecv);

		modelService.saveAll(Arrays.asList(pricerow1, pricerow2));

		final ProductModel resprod1 = productService.getProduct("prodWithPrice1");
		assertNotNull("prod not found", resprod1);
		final ProductModel resprod2 = productService.getProduct("prodWithPrice2");
		assertNotNull("prod not found", resprod2);

		assertEquals("", "specialunit1", productService.getOrderableUnit(resprod1).getCode());
		assertEquals("", "specialunit1", productService.getOrderableUnit(resprod2).getCode());
	}

	@Test
	public void testGetPricesNotSaved()
	{
		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setCode("specialunit");
		unit.setConversion(Double.valueOf(1.0));
		unit.setName("special Unit", Locale.ENGLISH);
		unit.setUnitType("something");

		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setName("testName");
		productModel.setCode("testCode");
		productModel.setCatalogVersion(onlinecv);
		modelService.save(productModel);

		final PriceRowModel priceRow1 = modelService.create(PriceRowModel.class);
		priceRow1.setUnit(unit);
		priceRow1.setCurrency(i18nService.getCurrency("EUR"));
		priceRow1.setPrice(Double.valueOf(2.3));
		priceRow1.setProduct(productModel);
		modelService.save(priceRow1);

		final PriceRowModel priceRow2 = modelService.create(PriceRowModel.class);
		priceRow2.setUnit(unit);
		priceRow2.setCurrency(i18nService.getCurrency("EUR"));
		priceRow2.setPrice(Double.valueOf(20.5));
		priceRow2.setProduct(productModel);

		productModel.setEurope1Prices(Arrays.asList(priceRow1, priceRow2));
		// one Price in collection is not saved yet - should not fail on getter
		try
		{
			final Collection<PriceRowModel> europe1Prices = productModel.getEurope1Prices();
			assertTrue(europe1Prices.containsAll(Arrays.asList(priceRow1, priceRow2)));
		}
		catch (final Exception exc)
		{
			fail("No exception should occur here. Probably the dynamic attribute handler has problems, when it comes to compare the given priceRows");
		}
	}

	@Test
	public void testSaveOwnAndOtherDiscounts()
	{
		final DiscountModel testDiscount1 = modelService.create(DiscountModel.class);
		testDiscount1.setCode("DSC1");
		final DiscountModel testDiscount2 = modelService.create(DiscountModel.class);
		testDiscount2.setCode("DSC2");
		final DiscountModel testDiscount3 = modelService.create(DiscountModel.class);
		testDiscount3.setCode("DSC3");
		modelService.saveAll(testDiscount1, testDiscount2);
		final DiscountRowModel discountRow1 = createDiscountRow(testDiscount1);
		final DiscountRowModel discountRow2 = createDiscountRow(testDiscount2);

		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("prodWithDiscounts");
		product.setCatalogVersion(onlinecv);
		product.setApprovalStatus(ArticleApprovalStatus.APPROVED);

		modelService.saveAll();

		final DiscountRowModel ownDiscountRow1 = createDiscountRow(testDiscount1);
		ownDiscountRow1.setProduct(product);

		modelService.save(ownDiscountRow1);
		modelService.refresh(product);

		//1 "own" and 2 "other" discountRows expected
		Collection<DiscountRowModel> europe1Discounts = product.getEurope1Discounts();
		assertTrue(europe1Discounts.containsAll(Arrays.asList(discountRow1, discountRow2, ownDiscountRow1)));
		Collection<DiscountRowModel> ownEurope1Discounts = product.getOwnEurope1Discounts();
		assertTrue(ownEurope1Discounts.containsAll(Arrays.asList(ownDiscountRow1)));

		final DiscountRowModel ownDiscountRow2 = createDiscountRow(testDiscount2);
		ownDiscountRow2.setProduct(product);

		final DiscountRowModel discountRow3 = createDiscountRow(testDiscount3);

		product.setEurope1Discounts(Arrays.asList(discountRow3, ownDiscountRow2));

		modelService.saveAll();
		modelService.refresh(product);
		modelService.refresh(ownDiscountRow2);
		assertEquals(ownDiscountRow2.getProduct(), product);

		//1 "own" and 3 "other" discountRows expected
		europe1Discounts = product.getEurope1Discounts();
		assertTrue(europe1Discounts.containsAll(Arrays.asList(discountRow1, discountRow2, discountRow3, ownDiscountRow2)));
		ownEurope1Discounts = product.getOwnEurope1Discounts();
		//ownDiscountRow1 is not "own" anymore but has even been removed
		assertTrue(ownEurope1Discounts.size() == 1);
		assertTrue(ownEurope1Discounts.containsAll(Arrays.asList(ownDiscountRow2)));
	}

	private DiscountRowModel createDiscountRow(final DiscountModel testDiscount)
	{
		final DiscountRowModel discountRow1 = modelService.create(DiscountRowModel.class);
		discountRow1.setCatalogVersion(onlinecv);
		discountRow1.setCurrency(i18nService.getCurrency("EUR"));
		discountRow1.setAsTargetPrice(true);
		discountRow1.setDiscount(testDiscount);
		return discountRow1;
	}

}
