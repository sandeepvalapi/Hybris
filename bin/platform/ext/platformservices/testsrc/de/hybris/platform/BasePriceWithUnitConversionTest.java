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
package de.hybris.platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class BasePriceWithUnitConversionTest extends ServicelayerTransactionalTest {

    private static final Logger LOG = Logger.getLogger(BasePriceWithUnitConversionTest.class);

    @Resource
    private ModelService modelService;

    @Resource
    private UserService userService;

    @Resource
    private CalculationService calculationService;

    private static final String WEIGHT_GROUP = "weight_unit_group";
    private static final Double POTATOES_PRICE_PER_KG = 1000.00;
    private static final Double CHEAP_POTATOES_PRICE_PER_KG = 1.00;


    CatalogModel testCatalog;
    CatalogVersionModel newCatalogVersion;
    EmployeeModel admin;

    UnitModel tonneUnit;
    UnitModel kgUnit;
    UnitModel gUnit;

    @Before
    public void setUp() throws Exception
    {
        testCatalog = modelService.create(CatalogModel.class);
        testCatalog.setId("testCatalog");
        modelService.save(testCatalog);

        newCatalogVersion = modelService.create(CatalogVersionModel.class);
        newCatalogVersion.setCategorySystemID("testCatVer");
        newCatalogVersion.setCatalog(testCatalog);
        newCatalogVersion.setVersion("1");
        newCatalogVersion.setActive(true);
        modelService.save(newCatalogVersion);

        admin = userService.getAdminUser();

        tonneUnit = createUnit("t", "t", WEIGHT_GROUP, 1_000_000.0);
        kgUnit = createUnit("kg", "kg", WEIGHT_GROUP, 1_000.0);
        gUnit = createUnit("g", "g", WEIGHT_GROUP, 1.0);
    }

    @Test
    public void basePriceWithUnitConversion() throws CalculationException {
        LOG.info("Base price with unit conversion test!");

        final CurrencyModel euroCurrency = createCurrency("EUR", "€");
        modelService.save(euroCurrency);

        final ProductModel potatoesProduct = createProduct("potatoes", kgUnit, newCatalogVersion);
        modelService.save(potatoesProduct);

        final PriceRowModel newPriceRow = createPriceRow(euroCurrency, kgUnit, potatoesProduct, POTATOES_PRICE_PER_KG);
        modelService.save(newPriceRow);

        final OrderModel order = createOrder(euroCurrency, new Date(), admin);
        modelService.save(order);

        final OrderEntryModel kgOrderEntry = createOrderEntry(order, potatoesProduct, 1L, kgUnit);
        modelService.save(kgOrderEntry);

        final OrderEntryModel gOrderEntry = createOrderEntry(order, potatoesProduct, 1L, gUnit);
        modelService.save(gOrderEntry);

        final OrderEntryModel tonneOrderEntry = createOrderEntry(order, potatoesProduct, 1L, tonneUnit);
        modelService.save(tonneOrderEntry);

        // calculate base price
        calculationService.calculate(order);

        LOG.info("\t tonne Order base price: " + tonneOrderEntry.getBasePrice());
        LOG.info("\t kg Order base price: " + kgOrderEntry.getBasePrice());
        LOG.info("\t g Order base price: " + gOrderEntry.getBasePrice());


        assertTrue(order.getCalculated());
        assertEquals(new Double(1), gOrderEntry.getBasePrice());
        assertEquals(new Double(1_000), kgOrderEntry.getBasePrice());
        assertEquals(new Double(1_000_000), tonneOrderEntry.getBasePrice());

        newPriceRow.setPrice(CHEAP_POTATOES_PRICE_PER_KG);
        modelService.save(newPriceRow);

        calculationService.recalculate(order);
        LOG.info("\t tonne Order base price: " + tonneOrderEntry.getBasePrice());
        LOG.info("\t kg Order base price: " + kgOrderEntry.getBasePrice());
        LOG.info("\t g Order base price: " + gOrderEntry.getBasePrice());

        assertTrue(order.getCalculated());
        assertEquals(new Double(0.001), gOrderEntry.getBasePrice());
        assertEquals(new Double(1), kgOrderEntry.getBasePrice());
        assertEquals(new Double(1_000), tonneOrderEntry.getBasePrice());
    }


    @Test
    public void preferredPricePerUnitTest() throws CalculationException {
        LOG.info("preferredPricePerUnitTest");

        final CurrencyModel euroCurrency = createCurrency("EUR", "€");
        modelService.save(euroCurrency);

        final ProductModel potatoesProduct = createProduct("potatoes", kgUnit, newCatalogVersion);
        modelService.save(potatoesProduct);

        final PriceRowModel newPriceRow = createPriceRow(euroCurrency, kgUnit, potatoesProduct, POTATOES_PRICE_PER_KG);
        modelService.save(newPriceRow);

        final PriceRowModel secPriceRow = createPriceRow(euroCurrency, tonneUnit, potatoesProduct, CHEAP_POTATOES_PRICE_PER_KG);
        modelService.save(secPriceRow);

        final OrderModel order = createOrder(euroCurrency, new Date(), admin);
        modelService.save(order);

        final OrderEntryModel kgOrderEntry = createOrderEntry(order, potatoesProduct, 1L, kgUnit);
        modelService.save(kgOrderEntry);

        final OrderEntryModel gOrderEntry = createOrderEntry(order, potatoesProduct, 1L, gUnit);
        modelService.save(gOrderEntry);

        final OrderEntryModel tonneOrderEntry = createOrderEntry(order, potatoesProduct, 1L, tonneUnit);
        modelService.save(tonneOrderEntry);

        // calculate base price
        calculationService.calculate(order);

        LOG.info("\t tonne Order base price: " + tonneOrderEntry.getBasePrice());
        LOG.info("\t kg Order base price: " + kgOrderEntry.getBasePrice());
        LOG.info("\t g Order base price: " + gOrderEntry.getBasePrice());


        assertTrue(order.getCalculated());
        assertEquals(new Double(1), gOrderEntry.getBasePrice());
        assertEquals(POTATOES_PRICE_PER_KG, kgOrderEntry.getBasePrice());
        assertEquals(CHEAP_POTATOES_PRICE_PER_KG, tonneOrderEntry.getBasePrice());
    }


    private UnitModel createUnit(final String code, final String name, final String type, final Double conversion) {
        final UnitModel unit = modelService.create(UnitModel.class);
        unit.setCode(code);
        unit.setName(name);
        unit.setUnitType(type);
        unit.setConversion(conversion);
        modelService.save(unit);

        return unit;
    }


    private ProductModel createProduct(final String code, final UnitModel unit, final CatalogVersionModel catalogVersion) {
        final ProductModel product = modelService.create(ProductModel.class);
        product.setCode(code);
        product.setUnit(unit);
        product.setCatalogVersion(catalogVersion);

        return product;
    }

    private CurrencyModel createCurrency(final String isocode, final String symbol) {
        final CurrencyModel currency = modelService.create(CurrencyModel.class);
        currency.setIsocode(isocode);
        currency.setSymbol(symbol);

        return currency;
    }

    private OrderModel createOrder(final CurrencyModel currency, final Date date, final EmployeeModel user) {
        final OrderModel order = modelService.create(OrderModel.class);
        order.setCurrency(currency);
        order.setDate(date);
        order.setUser(user);
        return order;
    }

    private OrderEntryModel createOrderEntry(final OrderModel order, final ProductModel product, final Long quantity, final UnitModel unit) {
        final OrderEntryModel orderEntry = modelService.create(OrderEntryModel.class);
        orderEntry.setOrder(order);
        orderEntry.setProduct(product);
        orderEntry.setQuantity(quantity);
        orderEntry.setUnit(unit);
        return orderEntry;
    }

    private PriceRowModel createPriceRow(final CurrencyModel currency, final UnitModel unit, final ProductModel product, final double price) {
        final PriceRowModel priceRow = modelService.create(PriceRowModel.class);
        priceRow.setCurrency(currency);
        priceRow.setUnit(unit);
        priceRow.setProduct(product);
        priceRow.setPrice(price);

        return priceRow;

    }

}
