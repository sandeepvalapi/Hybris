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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceCriteria.DefaultPriceCriteria;
import de.hybris.platform.product.impl.DefaultPriceService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PriceServiceTest extends ServicelayerTransactionalTest
{
	@Resource
	private PriceService priceService;
	@Resource
	private ProductService productService;
	@Resource
	TimeService timeService;
	@Resource
	CommonI18NService commonI18NService;
	@Resource
	ModelService modelService;
	@Resource
	UserService userService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	public void testGetPriceInformationsForProduct() throws Exception
	{
		//given
		final ProductModel product = productService.getProduct("testProduct0");
		assertNotNull("Product", product);

		//when
		final List<PriceInformation> priceInformations = priceService.getPriceInformationsForProduct(product);

		//then
		assertNotNull("Price Informations", priceInformations);
		assertFalse("Price Informations empty", priceInformations.isEmpty());
	}

	@Test
	public void testGetPriceInformations() throws Exception
	{
		Assume.assumeTrue("Right now only DefaultPriceService supports this.", priceService instanceof DefaultPriceService);

		//given
		final ProductModel product = productService.getProduct("testProduct0");
		assertNotNull("Product", product);
		final UserModel currentUser = userService.getCurrentUser();
		final PriceFactory pricefactory = OrderManager.getInstance().getPriceFactory();

		//when
		final boolean isNetUser = pricefactory.isNetUser(modelService.getSource(currentUser));
		final DefaultPriceCriteria criteria = PriceCriteria.DefaultPriceCriteria.forProduct(product)
				.forDate(timeService.getCurrentTime()).withNetPrice(Boolean.valueOf(isNetUser));
		final List<PriceInformation> priceInformations = priceService.getPriceInformations(criteria);

		//then
		assertNotNull("Price Informations", priceInformations);
		assertFalse("Price Informations empty", priceInformations.isEmpty());
	}

	@Test
	public void testTimeAndPrice()
	{
		ProductModel prod = createSampleProduct(unit("foobar"));
		assertEquals( Collections.emptyList(), priceService.getPriceInformationsForProduct(prod) );
		
		// create always-on-price
		createPrice(prod, 9.99, currency("EUR"), unit("foobar"), null, null);
		assertPrice(9.99, currency("EUR"), priceService.getPriceInformationsForProduct(prod));

		// create future price
		final long now = System.currentTimeMillis();
		final long oneWeekOffset = 7 * 24 * 60 * 60 * 1000;
		final long from = now + oneWeekOffset; // now + 7 days
		final long to = from + (2 * 24 * 60 * 60 * 1000 ); // now + 7 days + 2 days
		createPrice(prod, 1.99, currency("EUR"), unit("foobar"), new Date(from), new Date(to));
		
		// must return current price since we're in present
		assertPrice(9.99, currency("EUR"), priceService.getPriceInformationsForProduct(prod));
		
		try
		{
			// jump fixedly to  from + 12h
			timeService.setCurrentTime(new Date( from + 12 * 60 * 60 * 1000 )); 
			assertPrice(1.99, currency("EUR"), priceService.getPriceInformationsForProduct(prod));
		}
		finally
		{
			timeService.resetTimeOffset();
		}
		// go back to present
		assertPrice(9.99, currency("EUR"), priceService.getPriceInformationsForProduct(prod));

		try
		{
			// jump relatively to  from + 1 day
			timeService.setTimeOffset(oneWeekOffset + 24 * 60 * 60 * 1000 ); 
			assertPrice(1.99, currency("EUR"), priceService.getPriceInformationsForProduct(prod));
		}
		finally
		{
			timeService.resetTimeOffset();
		}
		// go back to present again
		assertPrice(9.99, currency("EUR"), priceService.getPriceInformationsForProduct(prod));
	}
	
	void assertPrice( double price, CurrencyModel curr, List<PriceInformation> infos )
	{
		assertEquals(1,infos.size());
		assertEquals(curr.getIsocode(), infos.get(0).getPriceValue().getCurrencyIso());
		assertEquals(price, infos.get(0).getPriceValue().getValue(), 0.000001);
	}
	
	UnitModel unit( String code )
	{
		try
		{
			return productService.getUnit(code);
		}
		catch (UnknownIdentifierException e)
		{
			UnitModel unit = modelService.create(UnitModel.class);
			unit.setCode(code);
			unit.setUnitType(code+"Type");
			unit.setConversion(Double.valueOf(1.0));
			modelService.save(unit);
			return unit;
		}
	}
	
	CurrencyModel currency( String iso )
	{
		try
		{
			return commonI18NService.getCurrency(iso);
		}
		catch (UnknownIdentifierException e)
		{
			CurrencyModel curr = modelService.create(CurrencyModel.class);
			curr.setIsocode(iso);
			curr.setActive(Boolean.TRUE);
			curr.setConversion(Double.valueOf(1.0));
			modelService.save(curr);
			return curr;
		}
	}
	
	void createPrice( ProductModel prod, double price, CurrencyModel curr, UnitModel unit, Date from, Date to )
	{
		PriceRowModel priceRow = modelService.create(PriceRowModel.class);
		priceRow.setProduct(prod);
		priceRow.setCurrency(curr);
		priceRow.setMinqtd(Long.valueOf(1));
		priceRow.setPrice(Double.valueOf(price));
		priceRow.setStartTime(from);
		priceRow.setEndTime(to);
		priceRow.setUnit(unit);
		modelService.save(priceRow);
	}
	
	ProductModel createSampleProduct(UnitModel unit)
	{
		CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId("PriceTestCat");

		CatalogVersionModel cv = modelService.create(CatalogVersionModel.class);
		cv.setVersion("version");
		cv.setCatalog(cat);
		cv.setActive(Boolean.TRUE);
		
		ProductModel prod = modelService.create(ProductModel.class);
		prod.setCatalogVersion(cv);
		prod.setCode("PriceTestProduct");
		prod.setUnit(unit);
		
		modelService.saveAll(cat, cv, prod);
		
		return prod;
	}

}
