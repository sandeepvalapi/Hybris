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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.impex.Europe1ProductDiscountTranslator;
import de.hybris.platform.europe1.jalo.impex.Europe1ProductDiscountTranslator.Europe1DiscountRowTranslator;
import de.hybris.platform.europe1.jalo.impex.Europe1UserDiscountsTranslator;
import de.hybris.platform.europe1.jalo.impex.Europe1UserDiscountsTranslator.Europe1GlobalDiscountRowTranslator;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.Utilities;

import java.io.StringReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class Europe1DiscountsTranslatorTest extends HybrisJUnit4TransactionalTest
{
	private Product testProduct2, testProduct3, testProduct4;
	private User user1, user2, user3, user4;
	private Currency currency;
	private static final String currencyISOCode = "EUR";
	private Europe1PriceFactory epf;
	private Discount discount1, discount2, discount3, discount4;
	private EnumerationValue bestcustomers;

	private final SimpleDateFormat dateFormatWithTime = Utilities.getSimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	private final SimpleDateFormat dateFormatWithoutTime = Utilities.getSimpleDateFormat("dd.MM.yyyy");
	private final NumberFormat numberFormat = Utilities.getDecimalFormat("#,##0.##", Locale.GERMAN);
	private final Locale loc = Locale.GERMAN;
    private final PropertyConfigSwitcher impexLegacySwitcher = new PropertyConfigSwitcher("impex.legacy.mode");

	@Before
	public void setUp() throws Exception
	{
		// set de as language to get german locale !!!
		jaloSession.getSessionContext().setLanguage(getOrCreateLanguage("de"));

		epf = Europe1PriceFactory.getInstance();

		assertNotNull(currency = C2LManager.getInstance().createCurrency(currencyISOCode));
		assertNotNull(testProduct2 = ProductManager.getInstance().createProduct("testProduct2"));
		assertNotNull(testProduct3 = ProductManager.getInstance().createProduct("testProduct3"));
		assertNotNull(testProduct4 = ProductManager.getInstance().createProduct("testProduct4"));
		assertNotNull(user1 = UserManager.getInstance().createUser("user1"));
		assertNotNull(user2 = UserManager.getInstance().createUser("user2"));
		assertNotNull(user3 = UserManager.getInstance().createUser("user3"));
		assertNotNull(user4 = UserManager.getInstance().createUser("user4"));
		assertNotNull(bestcustomers = EnumerationManager.getInstance().createEnumerationValue(
				Europe1Constants.TYPES.PRICE_USER_GROUP, Europe1Constants.Sample.UPG_BEST));
		assertNotNull(discount1 = OrderManager.getInstance().createDiscount("global"));
		assertNotNull(discount2 = OrderManager.getInstance().createDiscount("noneglobal"));
		assertNotNull(discount3 = OrderManager.getInstance().createDiscount("TreueRabatt"));
		assertNotNull(discount4 = OrderManager.getInstance().createDiscount("Neukunden"));
		discount1.setGlobal(true);
		discount2.setGlobal(false);
		discount3.setGlobal(true);
		discount4.setGlobal(true);
	}

	@Test
	public void testGlobalDiscountRowExport() throws Exception
	{
		final Europe1GlobalDiscountRowTranslator rowTrans = new Europe1GlobalDiscountRowTranslator(dateFormatWithoutTime,
				numberFormat, loc);
		final Europe1UserDiscountsTranslator trans = new Europe1UserDiscountsTranslator(rowTrans);

		GlobalDiscountRow row = epf.createGlobalDiscountRow(null, bestcustomers, currency, new Double(10d), new StandardDateRange(
				dateFormatWithoutTime.parse("17.10.2005"), dateFormatWithoutTime.parse("28.04.2006")), discount1);
		assertEquals(Europe1Constants.Sample.UPG_BEST + " = 10 " + currencyISOCode + " " + discount1.getCode()
				+ " [17.10.2005\\,28.04.2006] ", trans.exportValue(epf.getUserGlobalDiscountRows(user1, bestcustomers)));
		row.remove();

		row = epf.createGlobalDiscountRow(null, bestcustomers, null, new Double(20d),
				new StandardDateRange(dateFormatWithoutTime.parse("01.12.2005"), dateFormatWithoutTime.parse("31.12.2005")),
				discount3);
		assertEquals(Europe1Constants.Sample.UPG_BEST + " = 20 % " + discount3.getCode() + " [01.12.2005\\,31.12.2005] ",
				trans.exportValue(epf.getUserGlobalDiscountRows(user2, bestcustomers)));
		row.remove();

		row = epf.createGlobalDiscountRow(null, null, currency, new Double(10.27d), null, discount4);
		assertEquals("10\\,27 " + currencyISOCode + " " + discount4.getCode() + " ",
				trans.exportValue(epf.getUserGlobalDiscountRows(user3, bestcustomers)));
		row.remove();

		row = epf.createGlobalDiscountRow(null, null, null, new Double(15d), null, discount4);
		assertEquals("15 % " + discount4.getCode() + " ", trans.exportValue(epf.getUserGlobalDiscountRows(user4, bestcustomers)));
		row.remove();
	}

	@Test
	public void testGlobalDiscountRowImport() throws ParseException
	{
		final Europe1GlobalDiscountRowTranslator rowTrans = new Europe1GlobalDiscountRowTranslator(dateFormatWithoutTime,
				numberFormat, loc);
		final Europe1UserDiscountsTranslator trans = new Europe1UserDiscountsTranslator(rowTrans);

		Collection<GlobalDiscountRow> rows = (Collection<GlobalDiscountRow>) trans.importValue(Europe1Constants.Sample.UPG_BEST
				+ " = 10 " + currencyISOCode + " " + discount1.getCode() + " [17.10.2005,28.04.2006]", user1);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		GlobalDiscountRow row = rows.iterator().next();
		assertEquals(currency, row.getCurrency());
		assertNotNull(row.getDateRange());
		assertEquals(dateFormatWithTime.parse("17.10.2005 00:00:00"), row.getDateRange().getStart());
		assertEquals(dateFormatWithTime.parse("28.04.2006 23:59:59"), row.getDateRange().getEnd());
		assertEquals(discount1, row.getDiscount());
		assertTrue(row.isAbsolute().booleanValue());
		assertEquals(numberFormat.parse("10").doubleValue(), row.getValue().doubleValue(), 0.00);
		assertEquals(bestcustomers, row.getUserGroup());

		rows = (Collection<GlobalDiscountRow>) trans.importValue(
				Europe1Constants.Sample.UPG_BEST + " = 20 % " + discount3.getCode() + " [ 01.12.2005 , 31.12.2005 ]", user2);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(null, row.getCurrency());
		assertNotNull(row.getDateRange());
		assertEquals(dateFormatWithTime.parse("01.12.2005 00:00:00"), row.getDateRange().getStart());
		assertEquals(dateFormatWithTime.parse("31.12.2005 23:59:59"), row.getDateRange().getEnd());
		assertEquals(discount3, row.getDiscount());
		assertFalse(row.isAbsolute().booleanValue());
		assertEquals(numberFormat.parse("20").doubleValue(), row.getValue().doubleValue(), 0.00);
		assertEquals(bestcustomers, row.getUserGroup());

		rows = (Collection<GlobalDiscountRow>) trans.importValue("10,27 " + currencyISOCode + " " + discount4.getCode(), user3);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(currency, row.getCurrency());
		assertEquals(null, row.getDateRange());
		assertEquals(discount4, row.getDiscount());
		assertTrue(row.isAbsolute().booleanValue());
		assertEquals(numberFormat.parse("10,27").doubleValue(), row.getValue().doubleValue(), 0.00);
		assertEquals(null, row.getUserGroup());

		rows = (Collection<GlobalDiscountRow>) trans.importValue("15% " + discount4.getCode(), user4);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(null, row.getCurrency());
		assertEquals(null, row.getDateRange());
		assertEquals(discount4, row.getDiscount());
		assertFalse(row.isAbsolute().booleanValue());
		assertEquals(numberFormat.parse("15").doubleValue(), row.getValue().doubleValue(), 0.00);
		assertEquals(null, row.getUserGroup());
	}

	@Test
	public void testGlobalDiscountRowOverallImport()
	{
		final String data = "#% impex.setLocale( Locale.GERMANY );\n"
				+ "INSERT_UPDATE User;uid[unique=true];europe1discounts[translator="
				+ Europe1UserDiscountsTranslator.class.getName()
				+ "]\n"
				+ ";"
				+ user1.getUID()
				+ ";"
				+ Europe1Constants.Sample.UPG_BEST
				+ " = 10 "
				+ currencyISOCode
				+ " "
				+ discount1.getCode()
				+ " [17.10.2005,28.04.2006]\n"
				+ ";"
				+ user2.getUID()
				+ ";"
				+ Europe1Constants.Sample.UPG_BEST
				+ " = 20 % "
				+ discount3.getCode()
				+ " [ 01.12.2005 , 31.12.2005 ]\n"
				+ ";"
				+ user3.getUID()
				+ ";"
				+ "10,27 "
				+ currencyISOCode
				+ " " + discount4.getCode() + "\n" + ";" + user4.getUID() + ";" + "15% " + discount4.getCode() + "\n";

		final ImpExImportReader importReader = new ImpExImportReader(new CSVReader(new StringReader(data)), null);

		//	 testing: BEST_CUSTOMERS = 10 EUR global [17.10.2005,28.04.2006]
		GlobalDiscountRow dr1 = null, dr6 = null;
		try
		{
			final User importedUser1 = (User) importReader.readLine();
			assertNotNull(importedUser1);
			final Collection discountRows1 = epf.getUserGlobalDiscountRows(user1, bestcustomers);
			dr1 = (GlobalDiscountRow) discountRows1.iterator().next();
			assertEquals(1, discountRows1.size());
			assertEquals(dr1.getCurrency().getIsoCode(), currencyISOCode);
			assertEquals(dr1.getDiscount().getCode(), discount1.getCode());
			checkDiscountRowValue(numberFormat, "10", dr1);
			assertTrue(dr1.isAbsolute().booleanValue());
			assertEquals(dr1.getUserGroup(), bestcustomers);
			final StandardDateRange dateRange = new StandardDateRange(dateFormatWithTime.parse("17.10.2005 00:00:00"),
					dateFormatWithTime.parse("28.04.2006 23:59:59"));
			assertEquals(dateRange, dr1.getDateRange());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		//	testing: BEST_CUSTOMERS = [20 % TreueRabatt [ 01.12.2005 , 31.12.2005 ] , 10 EUR global [17.10.2005,28.04.2006] ]
		try
		{
			final User importedUser2 = (User) importReader.readLine();
			assertNotNull(importedUser2);
			final List discountRows6 = new ArrayList(epf.getUserGlobalDiscountRows(user2, bestcustomers));
			assertEquals(2, discountRows6.size());
			assertTrue(discountRows6.contains(dr1));
			dr6 = dr1.equals(discountRows6.get(0)) ? (GlobalDiscountRow) discountRows6.get(1) : (GlobalDiscountRow) discountRows6
					.get(0);
			assertEquals(dr6.getUserGroup(), bestcustomers);
			assertEquals(20.00, dr6.getValue().doubleValue(), 0.00);
			assertFalse(dr6.isAbsolute().booleanValue());
			final StandardDateRange dateRange = new StandardDateRange(dateFormatWithTime.parse("01.12.2005 00:00:00"),
					dateFormatWithTime.parse("31.12.2005 23:59:59"));
			assertEquals(dateRange, dr6.getDateRange());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		// testing: 10,27 EUR Neukunden
		try
		{
			final User importedUser3 = (User) importReader.readLine();
			assertNotNull(importedUser3);
			final Collection discountRows7 = epf.getUserGlobalDiscountRows(user3, null);
			final GlobalDiscountRow dr7 = (GlobalDiscountRow) discountRows7.iterator().next();

			assertEquals(1, discountRows7.size());
			assertEquals(dr7.getCurrency().getIsoCode(), currencyISOCode);
			assertEquals(dr7.getDiscount().getCode(), discount4.getCode());
			assertEquals(dr7.getUser(), user3);
			assertTrue(dr7.isAbsolute().booleanValue());
			checkDiscountRowValue(numberFormat, "10,27", dr7);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		//	 testing: 15 % Neukunden
		try
		{
			final User importedUser4 = (User) importReader.readLine();
			assertNotNull(importedUser4);
			final Collection discountRows8 = epf.getUserGlobalDiscountRows(user4, null);
			final GlobalDiscountRow dr8 = (GlobalDiscountRow) discountRows8.iterator().next();

			assertEquals(1, discountRows8.size());
			assertEquals(dr8.getCurrency(), null);
			assertEquals(dr8.getDiscount().getCode(), discount4.getCode());
			assertEquals(dr8.getUser(), user4);
			assertFalse(dr8.isAbsolute().booleanValue());
			checkDiscountRowValue(numberFormat, "15", dr8);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void testGlobalDiscountRowReImport() throws Exception
	{
		final Europe1GlobalDiscountRowTranslator rowTrans = new Europe1GlobalDiscountRowTranslator(dateFormatWithoutTime,
				numberFormat, loc);
		final Europe1UserDiscountsTranslator trans = new Europe1UserDiscountsTranslator(rowTrans);

		GlobalDiscountRow row1 = epf.createGlobalDiscountRow(null, bestcustomers, currency, new Double(10d), new StandardDateRange(
				dateFormatWithTime.parse("17.10.2005 00:00:00"), dateFormatWithTime.parse("28.04.2006 23:59:59")), discount1);
		Collection<GlobalDiscountRow> rows = (Collection<GlobalDiscountRow>) trans.importValue(
				trans.exportValue(epf.getUserGlobalDiscountRows(user1, bestcustomers)), user1);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		GlobalDiscountRow row2 = rows.iterator().next();
		compareDiscountRows(row1, row2);
		row1.remove();
		row2.remove();

		row1 = epf.createGlobalDiscountRow(
				null,
				bestcustomers,
				null,
				new Double(20d),
				new StandardDateRange(dateFormatWithTime.parse("01.12.2005 00:00:00"), dateFormatWithTime
						.parse("31.12.2005 23:59:59")), discount3);
		rows = (Collection<GlobalDiscountRow>) trans.importValue(
				trans.exportValue(epf.getUserGlobalDiscountRows(user2, bestcustomers)), user2);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row2 = rows.iterator().next();
		compareDiscountRows(row1, row2);
		row1.remove();
		row2.remove();

		row1 = epf.createGlobalDiscountRow(null, null, currency, new Double(10.27d), null, discount4);
		rows = (Collection<GlobalDiscountRow>) trans.importValue(
				trans.exportValue(epf.getUserGlobalDiscountRows(user3, bestcustomers)), user3);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row2 = rows.iterator().next();
		compareDiscountRows(row1, row2);
		row1.remove();
		row2.remove();

		row1 = epf.createGlobalDiscountRow(null, null, null, new Double(15d), null, discount4);
		rows = (Collection<GlobalDiscountRow>) trans.importValue(
				trans.exportValue(epf.getUserGlobalDiscountRows(user4, bestcustomers)), user4);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row2 = rows.iterator().next();
		compareDiscountRows(row1, row2);
		row1.remove();
		row2.remove();
	}

	@Test
	public void testDiscountRowExport() throws Exception
	{
		final Europe1DiscountRowTranslator rowTrans = new Europe1DiscountRowTranslator(dateFormatWithoutTime, numberFormat, loc);
		final Europe1ProductDiscountTranslator trans = new Europe1ProductDiscountTranslator(rowTrans);

		epf.createDiscountRow(testProduct2, null, null, null, currency, new Double(0.97d), new StandardDateRange(
				dateFormatWithoutTime.parse("17.10.2005"), dateFormatWithoutTime.parse("28.04.2006")), discount2);
		assertEquals("0\\,97 " + currencyISOCode + " " + discount2.getCode() + " [17.10.2005\\,28.04.2006] ",
				trans.exportValue(epf.getProductDiscountRows(testProduct2, null)));

		epf.createDiscountRow(testProduct3, null, null, null, currency, new Double(4.99d), null, discount2);
		assertEquals("4\\,99 " + currencyISOCode + " " + discount2.getCode() + " ",
				trans.exportValue(epf.getProductDiscountRows(testProduct3, null)));
	}

	@Test
	public void testDiscountRowImport() throws Exception
	{
		final Europe1DiscountRowTranslator rowTrans = new Europe1DiscountRowTranslator(dateFormatWithoutTime, numberFormat, loc);
		final Europe1ProductDiscountTranslator trans = new Europe1ProductDiscountTranslator(rowTrans);

		Collection<DiscountRow> rows = (Collection<DiscountRow>) trans.importValue(
				"0,97 " + currencyISOCode + " " + discount2.getCode() + " [17.10.2005,28.04.2006]", testProduct2);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		DiscountRow row = rows.iterator().next();
		assertEquals(currency, row.getCurrency());
		assertNotNull(row.getDateRange());
		assertEquals(dateFormatWithTime.parse("17.10.2005 00:00:00"), row.getDateRange().getStart());
		assertEquals(dateFormatWithTime.parse("28.04.2006 23:59:59"), row.getDateRange().getEnd());
		assertEquals(discount2, row.getDiscount());
		assertTrue(row.isAbsolute().booleanValue());
		assertEquals(numberFormat.parse("0,97").doubleValue(), row.getValue().doubleValue(), 0.00);
		assertEquals(null, row.getUserGroup());

		rows = (Collection<DiscountRow>) trans.importValue("4,99 " + currencyISOCode + " " + discount2.getCode(), testProduct3);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row = rows.iterator().next();
		assertEquals(currency, row.getCurrency());
		assertEquals(null, row.getDateRange());
		assertEquals(discount2, row.getDiscount());
		assertTrue(row.isAbsolute().booleanValue());
		assertEquals(numberFormat.parse("4,99").doubleValue(), row.getValue().doubleValue(), 0.00);
		assertEquals(null, row.getUserGroup());
	}

	@Test
	public void testDiscountRowOverallImport()
	{
		final String data = "#% impex.setLocale( Locale.GERMANY );\n"
				+ "INSERT_UPDATE Product;code[unique=true];catalogVersion[unique=true, allownull=true];europe1discounts[translator="
				+ Europe1ProductDiscountTranslator.class.getName()
				+ "]\n"
				+ ";"
				+ testProduct2.getCode()
				+ ";;0,97 "
				+ currencyISOCode
				+ " "
				+ discount2.getCode()
				+ " [17.10.2005,28.04.2006]\n"
				+ ";"
				+ testProduct3.getCode()
				+ ";;4,99 "
				+ currencyISOCode
				+ " "
				+ discount2.getCode()
				+ "\n"
				+ ";"
				+ testProduct4.getCode()
				+ ";;4,99 "
				+ currencyISOCode + " " + discount2.getCode() + ", 8,99 " + currencyISOCode + " " + discount2.getCode() + "\n";

		final ImpExImportReader importReader = new ImpExImportReader(new CSVReader(new StringReader(data)), null);

		//	 testing: 0,99 EUR global [17.10.2005,28.04.2006]
		try
		{
			importReader.readLine();
			final Collection discountRows2 = epf.getProductDiscountRows(testProduct2, null);
			final DiscountRow dr2 = (DiscountRow) discountRows2.iterator().next();

			assertEquals(1, discountRows2.size());
			assertEquals(dr2.getCurrency().getIsoCode(), currencyISOCode);
			assertEquals(dr2.getDiscount().getCode(), discount2.getCode());
			checkDiscountRowValue(numberFormat, "0,97", dr2);
			assertTrue(dr2.isAbsolute().booleanValue());
			assertEquals(dr2.getProduct(), testProduct2);
			final StandardDateRange dateRange = new StandardDateRange(dateFormatWithTime.parse("17.10.2005 00:00:00"),
					dateFormatWithTime.parse("28.04.2006 23:59:59"));
			assertEquals(dateRange, dr2.getDateRange());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		//	testing: 4,99 EUR noneglobal
		try
		{
			final Product product3 = (Product) importReader.readLine();
			assertNotNull(product3);
			final Collection discountRows3 = epf.getProductDiscountRows(testProduct3, null);
			final DiscountRow dr3 = (DiscountRow) discountRows3.iterator().next();

			assertEquals(1, discountRows3.size());
			assertTrue(dr3.isAbsolute().booleanValue());
			assertEquals(dr3.getCurrency().getIsoCode(), currencyISOCode);
			assertEquals(dr3.getDiscount().getCode(), discount2.getCode());
			checkDiscountRowValue(numberFormat, "4,99", dr3);
			assertEquals(dr3.getProduct(), testProduct3);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		//	testing: 4,99 EUR noneglobal, 8,99 EUR noneglobal
		try
		{
			DiscountRow dr4 = null;
			DiscountRow dr5 = null;

			final Product product4 = (Product) importReader.readLine();
			assertNotNull(product4);
			final Collection discountRows4 = epf.getProductDiscountRows(testProduct4, null);
			assertEquals(discountRows4.size(), 2);

			for (final Iterator iter = discountRows4.iterator(); iter.hasNext();)
			{
				final DiscountRow element = (DiscountRow) iter.next();
				if (element.getValue().doubleValue() == 4.99)
				{
					dr4 = element;
					dr5 = (DiscountRow) iter.next();
					break;
				}
				else if (element.getValue().doubleValue() == 8.99)
				{
					dr5 = element;
					dr4 = (DiscountRow) iter.next();
					break;
				}
				else
				{
					fail("Hab weder die eine noch die andere Preiszeile gefunden. Moo!");
				}
			}
			assertTrue(dr4.isAbsolute().booleanValue());
			assertTrue(dr5.isAbsolute().booleanValue());
			checkDiscountRowValue(numberFormat, "4,99", dr4);
			checkDiscountRowValue(numberFormat, "8,99", dr5);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testDiscountRowReImport() throws Exception
	{
		final Europe1DiscountRowTranslator rowTrans = new Europe1DiscountRowTranslator(dateFormatWithoutTime, numberFormat, loc);
		final Europe1ProductDiscountTranslator trans = new Europe1ProductDiscountTranslator(rowTrans);

		DiscountRow row1 = epf.createDiscountRow(
				testProduct2,
				null,
				null,
				null,
				currency,
				new Double(0.97d),
				new StandardDateRange(dateFormatWithTime.parse("17.10.2005 00:00:00"), dateFormatWithTime
						.parse("28.04.2006 23:59:59")), discount2);
		Collection<DiscountRow> rows = (Collection<DiscountRow>) trans.importValue(
				trans.exportValue(epf.getProductDiscountRows(testProduct2, null)), testProduct2);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		DiscountRow row2 = rows.iterator().next();
		compareDiscountRows(row1, row2);

		row1 = epf.createDiscountRow(testProduct3, null, null, null, currency, new Double(4.99d), null, discount2);
		rows = (Collection<DiscountRow>) trans.importValue(trans.exportValue(epf.getProductDiscountRows(testProduct3, null)),
				testProduct3);
		assertNotNull(rows);
		assertEquals(1, rows.size());
		row2 = rows.iterator().next();
		compareDiscountRows(row1, row2);
	}

	private void checkDiscountRowValue(final NumberFormat numberFormat, final String valueAsString, final AbstractDiscountRow adr)
	{
		try
		{
			assertEquals(adr.getValue().doubleValue(), numberFormat.parse(valueAsString).doubleValue(), 0.00);
		}
		catch (final ParseException e)
		{
			throw new JaloSystemException(e);
		}
	}

	private void compareDiscountRows(final AbstractDiscountRow row1, final AbstractDiscountRow row2)
	{
		assertEquals(row1.getCurrency(), row2.getCurrency());
		assertEquals(row1.getDateRange(), row2.getDateRange());
		assertEquals(row1.getDiscount(), row2.getDiscount());
		assertEquals(row1.isAbsolute(), row2.isAbsolute());
		assertEquals(row1.getValue(), row2.getValue());
		assertEquals(row1.getUserGroup(), row2.getUserGroup());
	}

	@Test
	public void testModificatedTimeByUpdatingProductDiscountRows() throws ImpExException, ConsistencyCheckException,
			InterruptedException
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

		final String impexcode = "INSERT_UPDATE Discount;code[unique=true];value;priority;global;currency(isocode);name[lang=de];name[lang=en]\n"
				+ ";50e;50;0;false;EUR;50e;50e\n"
				+ "INSERT_UPDATE Product;code[unique=true];catalogVersion[allownull=true];europe1Discounts[translator=de.hybris.platform.europe1.jalo.impex.Europe1ProductDiscountTranslator]\n"
				+ ";testprod1;\n" + ";testprod2;;dummy1 = 50 EUR 50e, 2% 50e";

		ImpExImportReader reader = new ImpExImportReader(impexcode);

		reader.readLine(); //read the discount line
		final Product test1 = (Product) reader.readLine();
		assertEquals("testprod1", test1.getCode());
		assertEquals(0, Europe1PriceFactory.getInstance().getEurope1Discounts(test1).size());
		final Product test2 = (Product) reader.readLine();
		assertEquals("testprod2", test2.getCode());
		assertEquals(2, Europe1PriceFactory.getInstance().getEurope1Discounts(test2).size());


		final Date test1_modtime = test1.getModificationTime();
		final Date test2_modtime = test2.getModificationTime();

		//2sec waiting between the two impex operations, so we can be sure that both operations are done in different times
		Thread.sleep(2000);

		final String impexcode2 = "UPDATE Discount;code[unique=true];value;priority;global;currency(isocode);name[lang=de];name[lang=en]\n"
				+ ";50e;50;0;false;EUR;50e;50e\n"
				+ "UPDATE Product;code[unique=true];catalogVersion[allownull=true];europe1Discounts[translator=de.hybris.platform.europe1.jalo.impex.Europe1ProductDiscountTranslator]\n"
				+ ";testprod1;\n" + ";testprod2;;2% 50e, dummy1 = 50 EUR 50e";

		reader = new ImpExImportReader(impexcode2);
		reader.readLine(); //read the discount line


		final Product test1a = (Product) reader.readLine();
		final Product test2a = (Product) reader.readLine();

		assertEquals(test1_modtime, test1a.getModificationTime());
		assertEquals(test2_modtime, test2a.getModificationTime());
	}

	@Test
	public void testModificatedTimeByUpdatingUserDiscountRows() throws ImpExException, ConsistencyCheckException,
			InterruptedException
	{
        impexLegacySwitcher.switchToValue("true");

		try
		{
			C2LManager.getInstance().getCurrencyByIsoCode("EUR");
		}
		catch (final JaloItemNotFoundException e)
		{
			C2LManager.getInstance().createCurrency("EUR");
		}

		final String impexcode = "INSERT_UPDATE Discount;code[unique=true];value;priority;global;currency(isocode);name[lang=de];name[lang=en]\n"
				+ ";XXX;20;0;true;EUR;20e;20e\n"
				+ ";YYY;30;0;true;EUR;30e;30e\n"
				+ "INSERT_UPDATE Customer;uid[unique=true];name;europe1Discounts[translator=de.hybris.platform.europe1.jalo.impex.Europe1UserDiscountsTranslator]\n"
				+ ";custxx1;customer XX 1;20 EUR XXX, 10 % YYY\n";

		ImpExImportReader reader = new ImpExImportReader(impexcode);

		final Discount disc = (Discount) reader.readLine(); //read the discount line
		assertEquals("XXX", disc.getCode());
		final Discount disc2 = (Discount) reader.readLine(); //read the discount line
		assertEquals("YYY", disc2.getCode());
		final Customer cust1 = (Customer) reader.readLine();
		assertEquals("customer XX 1", cust1.getName());
		assertEquals(2, Europe1PriceFactory.getInstance().getEurope1Discounts(cust1).size());

		final Date cust1_modtime = cust1.getModificationTime();

		//2sec waiting between the two impex operations, so we can be sure that both operations are done in different times
		Thread.sleep(2000);

		final String impexcode2 = "UPDATE Customer;uid[unique=true];name;europe1Discounts[translator=de.hybris.platform.europe1.jalo.impex.Europe1UserDiscountsTranslator]\n"
				+ ";custxx1;customer XX 1;10 % YYY, 20 EUR XXX\n";

		reader = new ImpExImportReader(impexcode2);
		final Customer cust1a = (Customer) reader.readLine();

		assertEquals(cust1_modtime, cust1a.getModificationTime());

        impexLegacySwitcher.switchBackToDefault();
	}
}
