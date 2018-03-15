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

import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.StopWatch;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PriceInfoTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(PriceInfoTest.class.getName());

	private static final String DETECTED_UI_EXPERIENCE_LEVEL = "UiExperienceService-Detected-Level";
	private static final String DESKTOP_CHANNEL = "desktop";

	Europe1PriceFactory priceFactory;
	ProductManager productManager;
	UserManager userManager;
	C2LManager c2lManager;
	EnumerationManager enumerationManager;
	TypeManager typeManager;

	Product product1, product2, product3, product4;
	Customer customer1, customer2;
	Currency currency1, currency2;
	Unit unit1, unit2, unit3, unit4;
	EnumerationValue ppg2, ppg3, ppg4, cpg2, desktopUIExperienceLevel;
	ComposedType unitType1, unitType2;

	PriceRow pr1, pr2, pr3, pr4, pr5, pr6;

	private static final int COUNT = 100;

	User prevUser;
	Currency prevCurr;
	Language prevLang;

	@Before
	public void setUp() throws Exception
	{
		// get PriceFactory
		priceFactory = (Europe1PriceFactory) jaloSession.getExtensionManager().getExtension(
				de.hybris.platform.europe1.constants.Europe1Constants.EXTENSIONNAME);
		// get other managers
		productManager = jaloSession.getProductManager();
		userManager = jaloSession.getUserManager();
		c2lManager = jaloSession.getC2LManager();
		enumerationManager = jaloSession.getEnumerationManager();
		typeManager = jaloSession.getTypeManager();
		// backup customer
		prevUser = jaloSession.getUser();
		prevCurr = jaloSession.getSessionContext().getCurrency();
		prevLang = jaloSession.getSessionContext().getLanguage();
		// create pricing groups
		assertNotNull(ppg2 = enumerationManager.createEnumerationValue(
				enumerationManager.getEnumerationType(Europe1Constants.TYPES.PRICE_PRODUCT_GROUP), "PPG2"));
		assertNotNull(ppg3 = enumerationManager.createEnumerationValue(
				enumerationManager.getEnumerationType(Europe1Constants.TYPES.PRICE_PRODUCT_GROUP), "PPG3"));
		assertNotNull(ppg4 = enumerationManager.createEnumerationValue(
				enumerationManager.getEnumerationType(Europe1Constants.TYPES.PRICE_PRODUCT_GROUP), "PPG4"));
		assertNotNull(cpg2 = enumerationManager.createEnumerationValue(
				enumerationManager.getEnumerationType(Europe1Constants.TYPES.PRICE_USER_GROUP), "CPG2"));
		// create sample units
		assertNotNull(unitType1 = typeManager.createComposedType(typeManager.getRootComposedTypeForJaloClass(Unit.class),
				"UnitType1"));
		assertNotNull(unitType2 = typeManager.createComposedType(typeManager.getRootComposedTypeForJaloClass(Unit.class),
				"UnitType2"));
		assertNotNull(unit1 = productManager.createUnit("u1", "unit1"));
		unit1.setComposedType(unitType1);

		assertNotNull(unit2 = productManager.createUnit("u2", "unit2"));
		unit2.setComposedType(unitType2);
		unit2.setConversionFactor(1.5);

		assertNotNull(unit3 = productManager.createUnit("u3", "unit3"));
		unit3.setComposedType(unitType2);
		unit3.setConversionFactor(10.0);
		assertNotNull(unit4 = productManager.createUnit("u4", "unit4"));
		unit4.setComposedType(unitType2);
		unit4.setConversionFactor(100.0);
		// create sample products: p1,  PPG2( p2 ),  PPG3( p3 )
		assertNotNull(product1 = productManager.createProduct("p1"));
		assertNotNull(product2 = productManager.createProduct("p2"));
		product2.setProperty(Europe1Constants.PARAMS.PPG, ppg2);
		assertNotNull(product3 = productManager.createProduct("p3"));
		product3.setProperty(Europe1Constants.PARAMS.PPG, ppg3);
		assertNotNull(product4 = productManager.createProduct("p4"));
		product4.setProperty(Europe1Constants.PARAMS.PPG, ppg4);
		product4.setUnit(unit2);
		// create sample customers: c1 , CPG2( c2 )
		assertNotNull(customer1 = userManager.createCustomer("c1"));
		assertNotNull(customer2 = userManager.createCustomer("c2"));
		customer2.setProperty(Europe1Constants.PARAMS.UPG, cpg2);
		// create sample Currencies
		assertNotNull(currency1 = c2lManager.createCurrency("curr1"));
		assertNotNull(currency2 = c2lManager.createCurrency("curr2"));
		//
		// create sample prices
		//
		//    P PPG C CPG MQTD CURR UNIT UF NET DR PRICE
		//pr1 *  *  *  *   1    cr1  u1   1  f  *   100.0
		//pr2 p2 *  c2 *   3    cr1  u1   1  f  *    50.0
		//pr3 * PPG2* CPG2 2    cr1  u2   1  f  *    45.0
		//pr4 p3PPG3c2CPG2 10   cr1  u2   1  f  *    10.0
		//    (illegal since either p or ppg is valid but never both)
		assertNotNull(pr1 = priceFactory.createPriceRow(null, null, null, null, 1, currency1, unit1, 1, false, null, 100.0));
		assertNotNull(pr2 = priceFactory.createPriceRow(product2, null, customer2, null, 3, currency1, unit1, 1, false, null, 50.0));
		assertNotNull(pr3 = priceFactory.createPriceRow(null, ppg3, null, cpg2, 2, currency2, unit2, 1, false, null, 45.0));

		assertNotNull(pr5 = priceFactory.createPriceRow(null, ppg4, null, null, 20, currency1, unit3, 1, false, null, 105.0));
		assertNotNull(pr6 = priceFactory.createPriceRow(null, ppg4, null, null, 2, currency1, unit4, 1, false, null, 106.0));
	}

	private void restoreSessionSettings()
	{
		jaloSession.setUser(prevUser);
		jaloSession.getSessionContext().setCurrency(prevCurr);
		jaloSession.getSessionContext().setLanguage(prevLang);
	}

	private static void print(final PriceInformation row)
	{
		System.out.print("PriceInfoRow[ ");
		final Collection qualis = row.getQualifierKeys();
		for (final Iterator it = qualis.iterator(); it.hasNext();)
		{
			final Object object = it.next();
			System.out.print(object);
			System.out.print("=");
			System.out.print(row.getQualifierValue(object));
			System.out.print(" ");
		}
		/* conv-log */LOG.debug("price: " + row.getPriceValue());
		/* conv-log */LOG.debug(" ]");
	}

	@Test
	public void testAll()
	{
		try
		{
			pr4 = priceFactory.createPriceRow(product3, null, customer2, cpg2, 10, currency2, unit2, 1, false, null, 10.0);
			fail("illegal row did not cause exception");
		}
		catch (final JaloPriceFactoryException e)
		{
			// fine here
		}

		try
		{
			// get PriceInfos
			//
			// query p1 for cr1 and c1: expecting pr1 matching -> 100.0
			jaloSession.getSessionContext().setCurrency(currency1);
			jaloSession.setUser(customer1);
			final Date date = new Date();
			checkPriceInfos(
					priceFactory.getAllPriceInformations(jaloSession.getSessionContext(), product1, date, false).getPrices(),
					Collections.singleton(pr1));
			// query p2 for cr1 and c1: expecting pr1 , pr2 and pr3 matching
			jaloSession.getSessionContext().setCurrency(currency1);
			jaloSession.setUser(customer2);
			checkPriceInfos(
					priceFactory.getAllPriceInformations(jaloSession.getSessionContext(), product2, date, false).getPrices(),
					Arrays.asList(new Object[]
					{ pr1, pr2 }));
			// query p3 for cr2 and c2 : expecting pr3 matching
			jaloSession.getSessionContext().setCurrency(currency2);
			jaloSession.setUser(customer2);
			checkPriceInfos(
					priceFactory.getAllPriceInformations(jaloSession.getSessionContext(), product3, date, false).getPrices(),
					Collections.singleton(pr3));
			// query p4         
			jaloSession.getSessionContext().setCurrency(currency1);
			checkPriceInfos(
					priceFactory.getAllPriceInformations(jaloSession.getSessionContext(), product4, date, false).getPrices(),
					Arrays.asList(new Object[]
					{ pr1, pr5, pr6 }));
			// test performance
			jaloSession.getSessionContext().setCurrency(currency1);
			jaloSession.setUser(customer1);
			//ProductPriceInformations i1,i2,i3; 
			final StopWatch stopWatch = new StopWatch("getting price infos 3x" + COUNT + " times");
			for (int i = 0; i < COUNT; i++)
			{
				/* i1 = */priceFactory.getAllPriceInformations(jaloSession.getSessionContext(), product1, date, false);
				/* i2 = */priceFactory.getAllPriceInformations(jaloSession.getSessionContext(), product2, date, true);
				/* i3 = */priceFactory.getAllPriceInformations(jaloSession.getSessionContext(), product3, date, true);
			}
			stopWatch.stop();
		}
		catch (final JaloPriceFactoryException e)
		{
			fail(e.getMessage());
		}
		finally
		{
			restoreSessionSettings();
		}
	}

	private void checkPriceInfos(final Collection<PriceInformation> priceInfos, final Collection priceRows)
	{
		for (final PriceInformation pi : priceInfos)
		{
			print(pi);
		}
		assertEquals(priceRows.size(), priceInfos.size());

		final Map checkMap = new HashMap();
		for (final Iterator iter = priceRows.iterator(); iter.hasNext();)
		{
			final PriceRow row = (PriceRow) iter.next();
			checkMap.put(row.getUnit() + ":" + row.getMinQuantity(), row);
		}
		for (final Iterator iter = priceInfos.iterator(); iter.hasNext();)
		{
			final PriceInformation pir = (PriceInformation) iter.next();
			assertCollection(Arrays.asList(new Object[]
			{ PriceRow.MINQTD, PriceRow.UNIT, PriceRow.PRICEROW }), pir.getQualifierKeys());
			final Unit unit = (Unit) pir.getQualifierValue(PriceRow.UNIT);
			final long min = ((Long) pir.getQualifierValue(PriceRow.MINQTD)).longValue();
			assertNotNull(unit);
			final PriceRow row = (PriceRow) checkMap.get(unit + ":" + min);
			if (row == null)
			{
				fail("unexpected price info " + unit + ":" + min + " = " + pir.getPriceValue());
			}
			assertEquals(row.getPrice().doubleValue(), pir.getPriceValue().getValue(), 0.00001);
		}
	}
}
