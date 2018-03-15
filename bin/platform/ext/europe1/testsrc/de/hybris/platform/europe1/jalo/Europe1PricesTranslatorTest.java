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
package de.hybris.platform.europe1.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator;
import de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator.Europe1PriceRowTranslator;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.Utilities;

import java.io.StringReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class Europe1PricesTranslatorTest extends HybrisJUnit4TransactionalTest
{
	private Product testProduct1, testProduct2, testProduct3, testProduct4, testProduct5, testProductChannel;
	private Unit unit;
	private User user;
	private User user2;
	private EnumerationValue userpricegroup1;
	private EnumerationValue userpricegroup2;
	private Currency currency;
	private Currency currency2;
	private Currency currency3;
	private static final String EUR = "EUR";
	private static final String USD = "USD";
	private static final String GBP = "GBP";
	private SessionContext ctx;
	private Europe1PriceFactory epf;
	private final SimpleDateFormat dateFormatWithTime = Utilities.getSimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	private final SimpleDateFormat dateFormatWithoutTime = Utilities.getSimpleDateFormat("dd.MM.yyyy");
	private final NumberFormat numberFormat = Utilities.getDecimalFormat("#,##0.##", Locale.GERMAN);
	private final Locale loc = Locale.GERMAN;

	@Before
	public void setUp() throws Exception
	{

		// set de as language to get german locale !!!
		jaloSession.getSessionContext().setLanguage(getOrCreateLanguage("de"));

		ctx = jaloSession.getSessionContext();

		epf = Europe1PriceFactory.getInstance();
		try
		{
			currency = C2LManager.getInstance().getCurrencyByIsoCode(EUR);
		}
		catch (final JaloItemNotFoundException e)
		{
			currency = C2LManager.getInstance().createCurrency(EUR);
		}
		try
		{
			currency2 = C2LManager.getInstance().getCurrencyByIsoCode(USD);
		}
		catch (final JaloItemNotFoundException e)
		{
			currency2 = C2LManager.getInstance().createCurrency(USD);
		}
		try
		{
			currency3 = C2LManager.getInstance().getCurrencyByIsoCode(GBP);
		}
		catch (final JaloItemNotFoundException e)
		{
			currency3 = C2LManager.getInstance().createCurrency(GBP);
		}
		assertNotNull(currency);
		assertNotNull(currency2);
		assertNotNull(currency3);
		assertNotNull(testProduct1 = ProductManager.getInstance().createProduct("testProduct1"));
		assertNotNull(testProduct2 = ProductManager.getInstance().createProduct("testProduct2"));
		assertNotNull(testProduct3 = ProductManager.getInstance().createProduct("testProduct3"));
		assertNotNull(testProduct4 = ProductManager.getInstance().createProduct("testProduct4"));
		assertNotNull(testProduct5 = ProductManager.getInstance().createProduct("testProduct5"));
		assertNotNull(testProductChannel = ProductManager.getInstance().createProduct("testProductChannel"));
		assertNotNull(unit = ProductManager.getInstance().createUnit("pieces", "pieces"));
		assertNotNull(ProductManager.getInstance().createUnit("pieces2", "pieces2"));
		testProduct4.setUnit(unit);
		testProductChannel.setUnit(unit);
		try
		{
			user = UserManager.getInstance().getCustomerByLogin("dummy");
		}
		catch (final JaloItemNotFoundException e)
		{
			user = UserManager.getInstance().createCustomer("dummy");
		}
		try
		{
			user2 = UserManager.getInstance().getCustomerByLogin("dum2my2");
		}
		catch (final JaloItemNotFoundException e)
		{
			user2 = UserManager.getInstance().createCustomer("dum2my2");
		}
		userpricegroup1 = epf.getUserPriceGroup("dummypricegroup1");
		if (userpricegroup1 == null)
		{
			userpricegroup1 = epf.createUserPriceGroup("dummypricegroup1");
		}
		userpricegroup2 = epf.getUserPriceGroup("dummypricegroup2");
		if (userpricegroup2 == null)
		{
			userpricegroup2 = epf.createUserPriceGroup("dummypricegroup2");
		}

		epf.createUserPriceGroup("NL");
		epf.createUserPriceGroup("DE");
		epf.createUserPriceGroup("FR");
		epf.createUserPriceGroup("GB");

		assertNotNull(userpricegroup1);
		assertNotNull(userpricegroup2);

		assertNotNull(user);
		assertNotNull(user2);
	}

	@Test
	public void testExport() throws Exception
	{
		final Europe1PriceRowTranslator rowTrans = new Europe1PriceRowTranslator(dateFormatWithoutTime, numberFormat, loc);
		final Europe1PricesTranslator trans = new Europe1PricesTranslator(rowTrans);

		epf.createPriceRow(testProduct1, null, null, null, 21, currency, unit, 1, true, null, 10);
		assertEquals("21 pieces = 10 " + EUR + " N ", trans.exportValue(epf.getEurope1Prices(testProduct1)));

		epf.createPriceRow(testProduct2, null, user, null, 27, currency, unit, 1, false, null, 0.99);
		assertEquals("dummy 27 pieces = 0\\,99 " + EUR + " ", trans.exportValue(epf.getEurope1Prices(testProduct2)));

		epf.createPriceRow(testProduct3, null, user2, null, 3, currency, unit, 1, false, new StandardDateRange(
				dateFormatWithoutTime.parse("17.10.2005"), dateFormatWithoutTime.parse("28.04.2006")), 30);
		assertEquals("dum2my2 3 pieces = 30 " + EUR + " [17.10.2005\\,28.04.2006] ",
				trans.exportValue(epf.getEurope1Prices(testProduct3)));

		epf.createPriceRow(testProduct4, null, null, null, 1, currency, null, 1, false, null, 30);
		assertEquals("1 pieces = 30 " + EUR + " ", trans.exportValue(epf.getEurope1Prices(testProduct4)));

		@SuppressWarnings("deprecation")
		final EnumerationValue channelEv = EnumerationManager.getInstance().getEnumerationValue("PriceRowChannel",
				PriceRowChannel.MOBILE.getCode());
		epf.createPriceRow(testProductChannel, null, null, null, 1, currency, null, 1, false, null, 30, channelEv);
		assertEquals("1 pieces = 30 " + EUR + " mobile ", trans.exportValue(epf.getEurope1Prices(testProductChannel)));
	}

	@Test
	public void testChannelImport()
	{
		final Europe1PriceRowTranslator rowTrans = new Europe1PriceRowTranslator(dateFormatWithoutTime, numberFormat, loc);
		final Europe1PricesTranslator trans = new Europe1PricesTranslator(rowTrans);

		//channel(PriceRowChannel.DESKTOP) provided and found
		Collection<PriceRow> rows = (Collection<PriceRow>) trans.importValue("dummy 10 pieces = 30 " + EUR + " desktop",
				testProductChannel);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		PriceRow row = rows.iterator().next();
		assertEquals(testProductChannel, row.getProduct());
		assertEquals(currency, row.getCurrency());
		assertNull(row.getDateRange());
		assertEquals(30d, row.getPrice().doubleValue(), 0);
		assertEquals(10, row.getMinQuantity());
		assertEquals(unit, row.getUnit());
		assertEquals(user, row.getUser());
		assertFalse(row.isNetAsPrimitive());
		assertNotNull(row.getChannel());
		assertTrue(row.getChannel().getCode().equals(PriceRowChannel.DESKTOP.getCode()));

		assertNotNull(unit = ProductManager.getInstance().createUnit("desktop", "desktop"));
		//unit value(PriceRowChannel.DESKTOP) coincidence, channel not provided and not found
		rows = (Collection<PriceRow>) trans.importValue("dummy 10 desktop = 30 " + EUR, testProductChannel);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(testProductChannel, row.getProduct());
		assertEquals(currency, row.getCurrency());
		assertNull(row.getDateRange());
		assertEquals(30d, row.getPrice().doubleValue(), 0);
		assertEquals(10, row.getMinQuantity());
		assertEquals(unit, row.getUnit());
		assertEquals(user, row.getUser());
		assertFalse(row.isNetAsPrimitive());
		assertNull(row.getChannel());

		//unit value(PriceRowChannel.DESKTOP) coincidence, channel(PriceRowChannel.MOBILE) provided and found
		rows = (Collection<PriceRow>) trans.importValue("dummy 10 desktop = 30 " + EUR + " mobile", testProductChannel);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(testProductChannel, row.getProduct());
		assertEquals(currency, row.getCurrency());
		assertNull(row.getDateRange());
		assertEquals(30d, row.getPrice().doubleValue(), 0);
		assertEquals(10, row.getMinQuantity());
		assertEquals(unit, row.getUnit());
		assertEquals(user, row.getUser());
		assertFalse(row.isNetAsPrimitive());
		assertNotNull(row.getChannel());
		assertTrue(row.getChannel().getCode().equals(PriceRowChannel.MOBILE.getCode()));
	}

	@Test
	public void testImport() throws Exception
	{
		final Europe1PriceRowTranslator rowTrans = new Europe1PriceRowTranslator(dateFormatWithoutTime, numberFormat, loc);
		final Europe1PricesTranslator trans = new Europe1PricesTranslator(rowTrans);

		Collection<PriceRow> rows = (Collection<PriceRow>) trans.importValue("21 pieces = 10\\,00 " + EUR + " N", testProduct1);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		PriceRow row = rows.iterator().next();
		assertEquals(currency, row.getCurrency());
		assertEquals(null, row.getDateRange());
		assertEquals(10d, row.getPrice().doubleValue(), 0);
		assertEquals(21l, row.getMinQuantity());
		assertEquals(unit, row.getUnit());
		assertNull(row.getUser());
		assertTrue(row.isNetAsPrimitive());
		assertNull(row.getChannel());

		rows = (Collection<PriceRow>) trans.importValue("dummy 27 pieces = 0,99 " + EUR, testProduct2);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(testProduct2, row.getProduct());
		assertEquals(currency, row.getCurrency());
		assertEquals(null, row.getDateRange());
		assertEquals(0.99d, row.getPrice().doubleValue(), 0);
		assertEquals(27l, row.getMinQuantity());
		assertEquals(unit, row.getUnit());
		assertEquals(user, row.getUser());
		assertFalse(row.isNetAsPrimitive());
		assertNull(row.getChannel());

		rows = (Collection<PriceRow>) trans.importValue("dum2my2 3 pieces = 30 " + EUR + "[17.10.2005,28.04.2006]", testProduct3);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(testProduct3, row.getProduct());
		assertEquals(currency, row.getCurrency());
		assertNotNull(row.getDateRange());
		assertEquals(dateFormatWithTime.parse("17.10.2005 00:00:00"), row.getDateRange().getStart());
		assertEquals(dateFormatWithTime.parse("28.04.2006 23:59:59"), row.getDateRange().getEnd());
		assertEquals(30d, row.getPrice().doubleValue(), 0);
		assertEquals(3l, row.getMinQuantity());
		assertEquals(unit, row.getUnit());
		assertEquals(user2, row.getUser());
		assertFalse(row.isNetAsPrimitive());
		assertNull(row.getChannel());

		rows = (Collection<PriceRow>) trans.importValue("30 " + EUR, testProduct4);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(testProduct4, row.getProduct());
		assertEquals(currency, row.getCurrency());
		assertNull(row.getDateRange());
		assertEquals(30d, row.getPrice().doubleValue(), 0);
		assertEquals(1, row.getMinQuantity());
		assertEquals(unit, row.getUnit());
		assertEquals(null, row.getUser());
		assertFalse(row.isNetAsPrimitive());
		assertNull(row.getChannel());

		rows = (Collection<PriceRow>) trans.importValue("dummy 10 pieces = 30 " + EUR + " desktop", testProductChannel);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(testProductChannel, row.getProduct());
		assertEquals(currency, row.getCurrency());
		assertNull(row.getDateRange());
		assertEquals(30d, row.getPrice().doubleValue(), 0);
		assertEquals(10, row.getMinQuantity());
		assertEquals(unit, row.getUnit());
		assertEquals(user, row.getUser());
		assertFalse(row.isNetAsPrimitive());
		assertNotNull(row.getChannel());
		assertTrue(row.getChannel().getCode().equals(PriceRowChannel.DESKTOP.getCode()));
	}

	@Test
	public void testOverallImport()
	{
		//	NumberFormat nf = new DecimalFormat( "#,##0.###" );  // generated by: DecimalFormat.getInstance(Locale.GERMAN).toPattern()

		//create all data
		final String data = "#% impex.setLocale( Locale.GERMANY );\n"
				+ "INSERT_UPDATE Product;code[unique=true];catalogVersion[unique=true, allownull=true];europe1prices[translator=de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator]\n"
				+ ";"
				+ testProduct1.getCode(ctx) + ";;21 pieces = 10,00 " + EUR + " N\n" // 
				+ ";" + testProduct2.getCode(ctx) + ";;dummy 27 pieces = 0,99 " + EUR + "\n" //
				+ ";" + testProduct3.getCode(ctx) + ";;dummy 3 pieces = 30 " + EUR + "[17.10.2005,28.04.2006]\n"// 
				+ ";G02503;;NL 1 pieces = 100,00 EUR,GB 1 pieces = 80,00 GBP,FR 1 pieces = 100,00 EUR,DE 1 pieces = 99,95 EUR\n"//
				//
				+ ";" + testProduct5.getCode(ctx) + ";;" // 5th prod, now all possible price rows 
				+ "dummy 27 pieces = 0,99 " + EUR + " N [17.10.2005,28.04.2006], "// normal line
				+ "dum2my2 27 pieces = 0,99 " + EUR + " N [17.10.2005,28.04.2006], "// other user
				+ "dummy 28 pieces = 0,99 " + EUR + " N [17.10.2005,28.04.2006], "// other quantity
				+ "dummy 27 pieces = 1,99 " + EUR + " N [17.10.2005,28.04.2006], "// other price
				+ "dummy 27 pieces = 0,99 " + USD + " N [17.10.2005,28.04.2006], "// other currency
				+ "dummy 27 pieces = 0,99 " + EUR + " B [17.10.2005,28.04.2006],"// other brutto/netto
				+ "dummy 27 pieces = 0,99 " + EUR + " N [18.10.2005,28.04.2006],"// other startdate
				+ "dummy 27 pieces = 0,99 " + EUR + " N [17.10.2005,28.04.2006], "// same line
				+ userpricegroup1.getCode() + " 27 pieces = 0,99 " + EUR + " N [17.10.2005,28.04.2006], "// here usergroup1!!!
				+ userpricegroup2.getCode() + " 27 pieces = 0,99 " + EUR + " N [17.10.2005,28.04.2006], "// here usergroup2!!!
				+ "dummy 27 pieces = 0,99 " + EUR + " N [17.10.2005,28.05.2006]\n"//other enddate
				+ ";" + testProductChannel.getCode(ctx) + ";;dummy 27 pieces = 0,99 " + EUR + " N [17.10.2005,28.05.2006] desktop\n";//channel provided



		//
		// test: quantity unit price currency netto
		// 
		final ImpExImportReader importReader = new ImpExImportReader(new CSVReader(new StringReader(data)), null);
		importReader.enableCodeExecution(true);
		try
		{
			final Product product1 = (Product) importReader.readLine();
			assertNotNull(product1);
			final Collection priceRows1 = epf.getProductPriceRows(ctx, testProduct1, epf.getPPG(ctx, testProduct1));

			assertEquals(priceRows1.size(), 1);
			final PriceRow pr1 = (PriceRow) priceRows1.iterator().next();
			assertEquals(pr1.getCurrency(), currency);

			checkPrice(numberFormat, "10,00", pr1);

			assertEquals(pr1.getUnit(), unit);
			assertEquals(pr1.getUnitFactorAsPrimitive(), 1);
			assertEquals(pr1.getMinQuantity(), 21);
			assertNull(pr1.getUser());
			assertTrue(pr1.isNetAsPrimitive());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}


		//
		// test: user quantity unit price currency netto (default-handling)
		//
		try
		{
			final Product product2 = (Product) importReader.readLine();
			assertNotNull(product2);
			final Collection priceRows2 = epf.getProductPriceRows(ctx, testProduct2, epf.getPPG(ctx, testProduct2));
			final PriceRow pr2 = (PriceRow) priceRows2.iterator().next();

			assertEquals(priceRows2.size(), 1);
			assertEquals(pr2.getCurrency(), currency);

			checkPrice(numberFormat, "0,99", pr2);

			assertEquals(pr2.getUnit(), unit);
			assertEquals(pr2.getUnitFactorAsPrimitive(), 1);
			assertEquals(pr2.getMinQuantity(), 27);
			assertEquals(pr2.getUser(), user);
			assertFalse(pr2.isNetAsPrimitive());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}


		//
		// test: user quantity unit price currency netto (default-handling) daterange
		//
		try
		{
			final Product product3 = (Product) importReader.readLine();
			assertNotNull(product3);
			final Collection priceRows3 = epf.getProductPriceRows(ctx, testProduct3, epf.getPPG(ctx, testProduct3));
			final PriceRow pr3 = (PriceRow) priceRows3.iterator().next();

			assertEquals(priceRows3.size(), 1);
			assertEquals(pr3.getCurrency(), currency);

			checkPrice(numberFormat, "30", pr3);

			assertEquals(pr3.getUnit(), unit);
			assertEquals(pr3.getUnitFactorAsPrimitive(), 1);
			assertEquals(pr3.getMinQuantity(), 3);
			assertEquals(pr3.getUser(), user);
			assertFalse(pr3.isNetAsPrimitive());
			final StandardDateRange dateRange = new StandardDateRange(dateFormatWithTime.parse("17.10.2005 00:00:00"),
					dateFormatWithTime.parse("28.04.2006 23:59:59"));
			assertEquals(dateRange, pr3.getDateRange());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		//
		// test: minquantity(default-handling) unit(default-handling) price currency netto(default-handling)
		//
		try
		{
			final Collection priceRows4 = epf.getProductPriceRows(ctx, testProduct3, epf.getPPG(ctx, testProduct4));
			final PriceRow pr4 = (PriceRow) priceRows4.iterator().next();

			assertEquals(priceRows4.size(), 1);
			assertEquals(pr4.getUnit(), unit);
			assertFalse(pr4.isNetAsPrimitive());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		try
		{
			final Product product4 = (Product) importReader.readLine();
			assertNotNull(product4);
			assertEquals("G02503", product4.getCode());
			assertEquals(4, epf.getEurope1Prices(product4).size());

			final Product product5 = (Product) importReader.readLine();
			assertNotNull(product5);
			assertEquals(testProduct5.getCode(), product5.getCode());
			assertEquals(10, epf.getEurope1Prices(product5).size());

			final Product productChannel = (Product) importReader.readLine();
			assertNotNull(productChannel);
			assertEquals(testProductChannel.getCode(), productChannel.getCode());
			final Collection<PriceRow> priceRows = epf.getEurope1Prices(productChannel);
			assertEquals(1, priceRows.size());
			final PriceRow row = priceRows.iterator().next();
			assertTrue(row.getChannel().getCode().equals(PriceRowChannel.DESKTOP.getCode()));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testReImport() throws Exception
	{
		final Europe1PriceRowTranslator rowTrans = new Europe1PriceRowTranslator(dateFormatWithoutTime, numberFormat, loc);
		final Europe1PricesTranslator trans = new Europe1PricesTranslator(rowTrans);

		PriceRow row1 = epf.createPriceRow(testProduct1, null, null, null, 21, currency, unit, 1, true, null, 10);
		Collection<PriceRow> rows = (Collection<PriceRow>) trans.importValue(trans.exportValue(epf.getEurope1Prices(testProduct1)),
				testProduct1);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		PriceRow row2 = rows.iterator().next();
		comparePriceRows(row1, row2);

		row1 = epf.createPriceRow(testProduct2, null, user, null, 27, currency, unit, 1, false, null, 0.99);
		rows = (Collection<PriceRow>) trans.importValue(trans.exportValue(epf.getEurope1Prices(testProduct2)), testProduct2);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row2 = rows.iterator().next();
		comparePriceRows(row1, row2);

		row1 = epf.createPriceRow(testProduct3, null, user2, null, 3, currency, unit, 1, false, new StandardDateRange(
				dateFormatWithTime.parse("17.10.2005 00:00:00"), dateFormatWithTime.parse("28.04.2006 23:59:59")), 30);
		rows = (Collection<PriceRow>) trans.importValue(trans.exportValue(epf.getEurope1Prices(testProduct3)), testProduct3);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row2 = rows.iterator().next();
		comparePriceRows(row1, row2);

		row1 = epf.createPriceRow(testProduct4, null, null, null, 1, currency, null, 1, false, null, 30);
		rows = (Collection<PriceRow>) trans.importValue(trans.exportValue(epf.getEurope1Prices(testProduct4)), testProduct4);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row2 = rows.iterator().next();
		comparePriceRows(row1, row2);
	}

	private void checkPrice(final NumberFormat numberFormat, final String priceAsString, final PriceRow priceRow)
	{
		try
		{
			assertEquals(priceRow.getPriceAsPrimitive(), numberFormat.parse(priceAsString).doubleValue(), 0.00);
		}
		catch (final ParseException e)
		{
			throw new JaloSystemException(e);
		}
	}

	private void comparePriceRows(final PriceRow row1, final PriceRow row2)
	{
		assertEquals(row1.getCurrency(), row2.getCurrency());
		assertEquals(row1.getDateRange(), row2.getDateRange());
		assertEquals(row1.getPrice().doubleValue(), row2.getPrice().doubleValue(), 0);
		assertEquals(row1.getMinqtdAsPrimitive(), row2.getMinQuantity());
		assertEquals(row1.getUnit(), row2.getUnit());
		assertEquals(row1.getUser(), row2.getUser());
		assertEquals(row1.isNet(), row2.isNet());
	}

	@Test
	public void testModificatedTimeByUpdatingPriceRows() throws ImpExException, ConsistencyCheckException, InterruptedException
	{
		try
		{
			C2LManager.getInstance().getCurrencyByIsoCode("EUR");
		}
		catch (final JaloItemNotFoundException e)
		{
			C2LManager.getInstance().createCurrency("EUR");
		}
		try
		{
			UserManager.getInstance().getCustomerByLogin("dummy1");
		}
		catch (final JaloItemNotFoundException e)
		{
			UserManager.getInstance().createCustomer("dummy1");
		}

		final String impexcode = "INSERT_UPDATE Product;code[unique=true];catalogVersion[allownull=true];europe1Prices[translator=de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator]\n"
				+ ";testprodXX1;\n"
				+ ";testprodXX2;;dummy1 1 pieces = 20 EUR N [01.01.2005, 10.10.2005], 2 pieces = 15 EUR, 10 pieces = 10 EUR\n";


		ImpExImportReader reader = new ImpExImportReader(impexcode);

		final Product test1 = (Product) reader.readLine();
		assertEquals("testprodXX1", test1.getCode());
		assertEquals(0, Europe1PriceFactory.getInstance().getEurope1Prices(test1).size());
		final Product test2 = (Product) reader.readLine();
		assertEquals("testprodXX2", test2.getCode());
		assertEquals(3, Europe1PriceFactory.getInstance().getEurope1Prices(test2).size());

		final Date test1_modtime = test1.getModificationTime();
		final Date test2_modtime = test2.getModificationTime();

		//2sec waiting between the two impex operations, so we can be sure that both operations are done in different times
		Thread.sleep(2000);
		final String impexcode2 = "UPDATE Product;code[unique=true];catalogVersion[allownull=true];europe1Prices[translator=de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator]\n"
				+ ";testprodXX1;\n"
				+ ";testprodXX2;;2 pieces = 15 EUR, 10 pieces = 10 EUR, dummy1 1 pieces = 20 EUR N [01.01.2005, 10.10.2005]";

		reader = new ImpExImportReader(impexcode2);

		final Product test1a = (Product) reader.readLine();
		final Product test2a = (Product) reader.readLine();


		assertEquals(test1_modtime, test1a.getModificationTime());
		assertEquals(test2_modtime, test2a.getModificationTime());

	}

}
