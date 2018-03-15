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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DateRange;
import de.hybris.platform.util.StandardDateRange;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests if Europe1PriceFactory correctly handles PriceRow with channel attribute.
 */
@IntegrationTest
public class PriceRowChannelTest extends ServicelayerTest
{
	private Product product;
	private Unit unit;
	private Currency currency;
	protected static final String DETECTED_UI_EXPERIENCE_LEVEL = "UiExperienceService-Detected-Level";

	@Resource
	private ModelService modelService;

	@Resource
	private EnumerationService enumerationService;

	Europe1PriceFactory epf;

	DateRange dateRange;

	@Before
	public void setUp() throws Exception
	{
		try
		{
			currency = C2LManager.getInstance().getCurrencyByIsoCode("EUR");
		}
		catch (final JaloItemNotFoundException e)
		{
			currency = C2LManager.getInstance().createCurrency("EUR");
		}
		C2LManager.getInstance().setBaseCurrency(currency);
		product = ProductManager.getInstance().createProduct("p1");
		unit = ProductManager.getInstance().createUnit("pieces", "pieces");

		epf = Europe1PriceFactory.getInstance();
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		final Date startDate = cal.getTime();
		cal.add(Calendar.YEAR, 4);
		final Date endDate = cal.getTime();
		dateRange = new StandardDateRange(startDate, endDate);
	}

	@Test
	public void testCreatePriceRowChannelUsingModel()
	{
		final PriceRowModel priceRow = createPriceRow();
		assertNotNull(priceRow);
		assertNotNull(priceRow.getChannel());
		assertEquals(PriceRowChannel.DESKTOP, priceRow.getChannel());
	}

	@Test
	public void testCreatePriceRowChannelUsingEurope1PriceFactory() throws Exception
	{
		@SuppressWarnings("deprecation")
		final EnumerationValue channelDesktopEv = EnumerationManager.getInstance().getEnumerationValue("PriceRowChannel",
				PriceRowChannel.DESKTOP.getCode());

		final PriceRow data = epf
				.createPriceRow(product, null, null, null, 21, currency, unit, 1, true, null, 10, channelDesktopEv);

		assertNotNull(data);
		assertNotNull(data.getChannel());
		final EnumerationValue channelEV = EnumerationManager.getInstance().getEnumerationValue("PriceRowChannel",
				PriceRowChannel.DESKTOP.getCode());
		assertEquals(channelEV, data.getChannel());

		final Collection<PriceRow> results = epf.getEurope1Prices(product);
		final PriceRow resultData = results.iterator().next();
		assertNotNull(resultData);
		assertNotNull(resultData.getChannel());
		assertEquals(channelEV, resultData.getChannel());
	}

	@Test
	public void testCreatePriceRowNullChannelUsingEurope1PriceFactory() throws Exception
	{
		final PriceRow data = epf.createPriceRow(product, null, null, null, 21, currency, unit, 1, true, null, 10, null);

		assertNotNull(data);
		assertNull(data.getChannel());

		final Collection<PriceRow> results = epf.getEurope1Prices(product);
		final PriceRow resultData = results.iterator().next();
		assertNotNull(resultData);
		assertNull(resultData.getChannel());
	}

	/**
	 * Cases to Test:
	 * 
	 * Context - No UI Experience Context, Price rows without channel data.
	 */
	@Test
	public void testProductPricesNoChannel() throws Exception
	{
		//Test Default Price Row Creation
		final PriceRow defaultPrice = epf.createPriceRow(product, null, null, null, 1, currency, unit, 1, true, dateRange, 20);
		assertNotNull(defaultPrice);

		//Test - No UI Experience context and no channel pricerows
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final List<PriceInformation> data = epf.getProductPriceInformations(ctx, product, Calendar.getInstance().getTime(), false);
		assertNotNull(data);
		assertEquals(1, data.size());
		final PriceInformation priceInformation1 = data.iterator().next();
		assertEquals(20, priceInformation1.getPriceValue().getValue(), 0);
	}

	/**
	 * Cases to Test:
	 * 
	 * Context - UI Experience Context set, Price rows without channel data.
	 */
	@Test
	public void testProductPricesWithUIExperienceOnly() throws Exception
	{
		//Test Default Price Row Creation
		final PriceRow defaultPrice = epf.createPriceRow(product, null, null, null, 1, currency, unit, 1, true, dateRange, 30);
		assertNotNull(defaultPrice);

		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final EnumerationValue desktopEnumUIExpLevel = EnumerationManager.getInstance().getEnumerationValue(
				PriceRowChannel._TYPECODE, "desktop");
		ctx.setAttribute(DETECTED_UI_EXPERIENCE_LEVEL, desktopEnumUIExpLevel);
		final PriceRowChannel channelFromDb = enumerationService.getEnumerationValue(PriceRowChannel._TYPECODE,
				desktopEnumUIExpLevel.getCode());
		ctx.setAttribute("channel", channelFromDb);
		final List<PriceInformation> data = epf.getProductPriceInformations(ctx, product, Calendar.getInstance().getTime(), false);
		assertNotNull(data);
		assertEquals(1, data.size());
		final PriceInformation priceInformation1 = data.iterator().next();
		assertEquals(30, priceInformation1.getPriceValue().getValue(), 0);
	}

	/**
	 * Cases to Test:
	 * 
	 * Context - UI Experience Context set, Price rows with channel data, changing "channel" ctx attribute to see if
	 * returned prices are different.
	 */
	@Test
	public void testDefaultProductBasePriceChannel() throws Exception
	{
		@SuppressWarnings("deprecation")
		final EnumerationValue channelDesktopEv = EnumerationManager.getInstance().getEnumerationValue("PriceRowChannel",
				PriceRowChannel.DESKTOP.getCode());
		@SuppressWarnings("deprecation")
		final EnumerationValue channelMobileEv = EnumerationManager.getInstance().getEnumerationValue("PriceRowChannel",
				PriceRowChannel.MOBILE.getCode());

		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();

		//Test - desktop context and product with channel Pricerows
		final PriceRow desktopPrice = epf.createPriceRow(product, null, null, null, 1, currency, unit, 1, true, dateRange, 15,
				channelDesktopEv);
		final PriceRow mobilePrice = epf.createPriceRow(product, null, null, null, 1, currency, unit, 1, true, dateRange, 10,
				channelMobileEv);
		assertNotNull(desktopPrice);
		assertNotNull(mobilePrice);

		final EnumerationValue desktopEnumUIExpLevel = EnumerationManager.getInstance().getEnumerationValue(
				PriceRowChannel._TYPECODE, "desktop");
		ctx.setAttribute(DETECTED_UI_EXPERIENCE_LEVEL, desktopEnumUIExpLevel);
		final List<PriceInformation> desktopData = epf.getProductPriceInformations(ctx, product, Calendar.getInstance().getTime(),
				false);
		assertNotNull(desktopData);
		assertEquals(1, desktopData.size());
		final PriceInformation desktopPriceRowResult = desktopData.iterator().next();
		assertEquals(15, desktopPriceRowResult.getPriceValue().getValue(), 0);

		//Test - mobile context and product with channel Pricerows
		final PriceRowChannel mobileEnumUIExpLevel = enumerationService.getEnumerationValue(PriceRowChannel._TYPECODE, "mobile");
		ctx.setAttribute("channel", mobileEnumUIExpLevel);
		final List<PriceInformation> mobileData = epf.getProductPriceInformations(ctx, product, Calendar.getInstance().getTime(),
				false);
		assertNotNull(mobileData);
		assertEquals(1, mobileData.size());
		final PriceInformation mobilePriceRowResult = mobileData.iterator().next();
		assertEquals(10, mobilePriceRowResult.getPriceValue().getValue(), 0);

	}

	private PriceRowModel createPriceRow()
	{
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId("C3");
		modelService.save(catalogModel);

		CatalogVersionModel catalogVersionModel = null;
		catalogVersionModel = modelService.create(CatalogVersionModel.class);
		catalogVersionModel.setCatalog(catalogModel);
		catalogVersionModel.setVersion("CV3");
		modelService.save(catalogVersionModel);

		final ProductModel product = modelService.create(ProductModel.class);
		product.setEan("P2");
		product.setCode("P2");
		product.setManufacturerName("ProductManufacturer");
		product.setCatalogVersion(catalogVersionModel);
		modelService.save(product);

		final CurrencyModel currency = modelService.create(CurrencyModel.class);
		currency.setIsocode("IND");
		currency.setSymbol("IND");
		currency.setBase(Boolean.TRUE);
		currency.setActive(Boolean.TRUE);
		currency.setConversion(Double.valueOf(1));
		modelService.save(currency);

		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setCode("kg");
		unit.setUnitType("kg");
		modelService.save(unit);

		final PriceRowModel priceRow = modelService.create(PriceRowModel.class);
		priceRow.setCurrency(currency);
		priceRow.setMinqtd(Long.valueOf(1));
		priceRow.setNet(Boolean.TRUE);
		priceRow.setPrice(Double.valueOf(5.00));
		priceRow.setUnit(unit);
		priceRow.setProduct(product);
		priceRow.setCatalogVersion(catalogVersionModel);
		priceRow.setChannel(PriceRowChannel.DESKTOP);
		modelService.saveAll(Arrays.asList(priceRow, product));

		return priceRow;
	}
}
