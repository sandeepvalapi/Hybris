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
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.persistence.property.EJBProperty;
import de.hybris.platform.persistence.property.OldPropertyJDBC;
import de.hybris.platform.persistence.property.OldPropertyJDBC.DumpPropertyConverter;
import de.hybris.platform.persistence.test.TestItemHome;
import de.hybris.platform.persistence.test.TestItemRemote;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class HJMPTest extends HybrisJUnit4Test
{
	static final Logger LOG = Logger.getLogger(HJMPTest.class.getName());

	private TestItemRemote remote;
	private TestItemHome home;

	private static final PropertyConfigSwitcher bigDecimalRounding = new PropertyConfigSwitcher(
			"jdbcmappings.big_decimal_scale");

	@Before
	public void setUp() throws Exception
	{
		bigDecimalRounding.switchToValue("5");

		try
		{
			home = (TestItemHome) Registry.getCurrentTenant().getPersistencePool()
					.getHomeProxy(Registry.getPersistenceManager().getJNDIName(Constants.TC.TestItem));
			remote = home.create();
		}
		catch (final de.hybris.platform.util.jeeapi.YCreateException e)
		{
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception
	{
		bigDecimalRounding.switchBackToDefault();

		if (remote != null)
		{
			try
			{
				remote.remove();
			}
			catch (final Exception e)
			{
				// can't help it
			}
		}
	}

	@Test
	public void testMoveDumpProps()
	{
		final String propName = "dumpPropBalhFasel";
		final Integer propValue = Integer.valueOf(123456);
		boolean removed = false;
		try
		{
			remote.setProperty(propName, propValue);
			assertEquals(propValue, remote.getProperty(propName));

			OldPropertyJDBC.moveDumpData(Constants.TC.TestItem, propName,
					Registry.getPersistenceManager().getItemDeployment(Constants.TC.TestItem).getDumpPropertyTableName(),
					new DumpPropertyConverter()
					{
						@Override
						public boolean convert(final PK itemPK, final PK typePK, final EJBProperty dumpProp)
						{
							assertEquals(remote.getPK(), itemPK);
							assertEquals(remote.getComposedType().getPK(), typePK);
							assertEquals(propName, dumpProp.getName());
							assertEquals(propValue, dumpProp.getValue());
							assertNull(dumpProp.getLang());
							return true;
						}
					});
			removed = true;
		}
		finally
		{
			if (!removed)
			{
				remote.removeProperty(propName);
			}
		}
	}

	@Test
	public void testWriteReadValues()
	{
		LOG.debug(">>> testWriteReadValues()");

		final Float floatValue = new Float(1234.5678f);
		/* conv-LOG */LOG.debug(">>> Float (" + floatValue + ") <<<");
		remote.setFloat(floatValue);
		assertEquals(floatValue, remote.getFloat());

		final Double doubleValue = new Double(2234.5678d);
		/* conv-LOG */LOG.debug(">>> Double (" + doubleValue + ") <<<");
		remote.setDouble(doubleValue);
		assertEquals(doubleValue, remote.getDouble());

		final Character characterValue = Character.valueOf('g');
		/* conv-LOG */LOG.debug(">>> Character (" + characterValue + ") <<<");
		remote.setCharacter(characterValue);
		assertEquals(characterValue, remote.getCharacter());

		final Integer integerValue = Integer.valueOf(3357);
		/* conv-LOG */LOG.debug(">>> Integer (" + integerValue + ") <<<");
		remote.setInteger(integerValue);
		assertEquals(integerValue, remote.getInteger());

		final Long longValue = Long.valueOf(4357L);
		/* conv-LOG */LOG.debug(">>> Long (" + longValue + ") <<<");
		remote.setLong(longValue);
		assertEquals(longValue, remote.getLong());

		final Calendar now = Utilities.getDefaultCalendar();
		now.set(Calendar.MILLISECOND, 0);

		/* conv-LOG */LOG.debug(">>> Date (" + now + ", ms=" + now.getTime() + ")<<<");
		remote.setDate(now.getTime());
		final java.util.Date got = remote.getDate();
		/* conv-LOG */LOG.debug(">>> found (" + got.getTime() + ")<<<");
		assertEquals(now.getTime(), got);

		// try to get 123,4567 as big decimal
		final BigDecimal bigDecimalValue = BigDecimal.valueOf(1234567, 4);
		/* conv-LOG */LOG.debug(">>> BigDecimal (" + bigDecimalValue + ")<<<");
		remote.setBigDecimal(bigDecimalValue);
		assertTrue(remote.getBigDecimal().compareTo(bigDecimalValue) == 0);

		final java.sql.Timestamp timestamp = new java.sql.Timestamp(now.getTime().getTime());
		/* conv-LOG */LOG.debug(">>> Timestamp (" + timestamp + ", ms=" + timestamp.getTime() + ")<<<");
		remote.setDate(timestamp);
		final java.util.Date got2 = remote.getDate();
		/* conv-LOG */LOG.debug(">>> found (" + got2.getTime() + ")<<<");
		assertEquals(now.getTime(), got2);

		final String str = "Alles wird gut!";
		/* conv-LOG */LOG.debug(">>> String (" + str + ")<<<");
		remote.setString(str);
		assertEquals(str, remote.getString());

		final String longstr = "Alles wird lange gut!";
		/* conv-LOG */LOG.debug(">>> Long String (" + longstr + ")<<<");
		remote.setLongString(longstr);
		assertEquals(longstr, remote.getLongString());

		//put 50000 chars into it
		String longstr2 = "";
		for (int i2 = 0; i2 < 5000; i2++)
		{
			longstr2 += "01234567890";
		}
		remote.setLongString(longstr2);
		assertEquals(longstr2, remote.getLongString());

		/*
		 * remote.setString( "" ); assertEquals( "" , remote.getString() ); remote.setString( null ); assertNull(
		 * remote.getString() ); remote.setLongString( "" ); assertEquals( "" , remote.getLongString() );
		 * remote.setLongString( null ); assertNull( remote.getLongString() );
		 */

		final Boolean booleanValue = Boolean.TRUE;
		/* conv-LOG */LOG.debug(">>> Boolean (" + booleanValue + ")<<<");
		remote.setBoolean(booleanValue);
		assertEquals(booleanValue, remote.getBoolean());

		final float floatValue2 = 5234.5678f;
		/* conv-LOG */LOG.debug(">>> float (" + floatValue2 + ") <<<");
		remote.setPrimitiveFloat(floatValue2);
		assertTrue(">>> float (" + floatValue2 + ") <<<", floatValue2 == remote.getPrimitiveFloat());

		final double doubleValue2 = 6234.5678d;
		/* conv-LOG */LOG.debug(">>> double (" + doubleValue2 + ") <<<");
		remote.setPrimitiveDouble(doubleValue2);
		assertTrue(">>> double (" + doubleValue2 + ") <<<", doubleValue2 == remote.getPrimitiveDouble());

		final int integerValue2 = 7357;
		/* conv-LOG */LOG.debug(">>> int (" + integerValue2 + ") <<<");
		remote.setPrimitiveInteger(integerValue2);
		assertTrue(integerValue2 == remote.getPrimitiveInteger());

		final long longValue2 = 8357L;
		/* conv-LOG */LOG.debug(">>> long (" + longValue2 + ") <<<");
		remote.setPrimitiveLong(longValue2);
		assertTrue(">>> long (" + longValue2 + ") <<<", longValue2 == remote.getPrimitiveLong());

		final byte byteValue = 123;
		/* conv-LOG */LOG.debug(">>> byte (" + byteValue + ") <<<");
		remote.setPrimitiveByte(byteValue);
		assertTrue(">>> byte (" + byteValue + ") <<<", byteValue == remote.getPrimitiveByte());

		final boolean booleanValue2 = true;
		/* conv-LOG */LOG.debug(">>> boolean (" + booleanValue2 + ") <<<");
		remote.setPrimitiveBoolean(booleanValue2);
		assertTrue(">>> boolean (" + booleanValue2 + ") <<<", booleanValue2 == remote.getPrimitiveBoolean());

		final char characterValue2 = 'g';
		/* conv-LOG */LOG.debug(">>> char (" + characterValue2 + ") <<<");
		remote.setPrimitiveChar(characterValue2);
		assertTrue(">>> char (" + characterValue2 + ") <<<", characterValue2 == remote.getPrimitiveChar());

		final ArrayList list = new ArrayList();
		LOG.debug(">>> Serializable with standard classes (" + list + ") <<<");
		remote.setSerializable(list);
		assertEquals(list, remote.getSerializable());

		if (!Config.isOracleUsed())
		{
			try
			{
				final HashMap bigtest = new HashMap();
				LOG.debug(">>> Serializable with standard classes (big thing) <<<");
				final byte[] byteArray = new byte[100000];
				bigtest.put("test", byteArray);
				remote.setSerializable(bigtest);
				final Map longtestret = (Map) remote.getSerializable();
				assertTrue(longtestret.size() == 1);
				final byte[] byteArray2 = (byte[]) longtestret.get("test");
				assertTrue(byteArray2.length == 100000);
			}
			catch (final Exception e)
			{
				throw new JaloSystemException(e, "Unable to write big serializable object, db: " + Config.getDatabase() + ".", 0);
			}
		}

		/* conv-LOG */LOG.debug(">>> Serializable (null) <<<");
		remote.setSerializable(null);
		assertNull(remote.getSerializable());


		//         try
		//         {
		//            final Dummy dummy = new Dummy();
		//   		   /*conv-LOG*/ LOG.debug(">>> Serializable with custom classes ("+dummy+") <<<");
		//   		   remote.setSerializable( dummy );
		//   		   final Serializable x = remote.getSerializable();
		//   		   assertTrue( dummy.equals( x ) || x == null );
		//   		   if( x == null )
		//               /*conv-LOG*/ LOG.debug(">>> ok, we're still unable of writing non-serverside classes to database : read NULL ");
		//         }
		//         catch( Exception e )
		//         {
		//            /*conv-LOG*/ LOG.debug(">>> ok, we're still unable of writing non-serverside classes to database : " + e );
		//         }

		//
		// search
		//

		try
		{
			/* conv-LOG */LOG.debug(">>> Search ( " + str + ", " + integerValue2 + " ) <<<");
			home.finderTest(str, integerValue2);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail("TestItem not found!");
		}

		//
		// 'null' tests
		//

		/* conv-LOG */LOG.debug(">>> String (null) <<<");
		remote.setString(null);
		assertNull(remote.getString());

		/* conv-LOG */LOG.debug(">>> Character (null) <<<");
		remote.setCharacter(null);
		assertNull(remote.getCharacter());

		/* conv-LOG */LOG.debug(">>> Integer (null) <<<");
		remote.setInteger(null);
		assertNull(remote.getInteger());

		/* conv-LOG */LOG.debug(">>> Date (null) <<<");
		remote.setDate(null);
		assertNull(remote.getDate());

		/* conv-LOG */LOG.debug(">>> Double (null) <<<");
		remote.setDouble(null);
		assertNull(remote.getDouble());

		/* conv-LOG */LOG.debug(">>> Float (null) <<<");
		remote.setFloat(null);
		assertNull(remote.getFloat());
	}

	@Test
	public void testNumericWriteReadValuesRounding() {
		LOG.debug(">>> testNumericWriteReadValuesRounding()");

		final Float floatValue = new Float(13.89999f);
		/* conv-LOG */LOG.debug(">>> Float (" + floatValue + ") <<<");
		remote.setFloat(floatValue);
		assertEquals(floatValue, remote.getFloat());

		final Float floatOverValue = new Float(13.8999999f);
		/* conv-LOG */LOG.debug(">>> FloatOverValue (" + floatOverValue + ") <<<");
		remote.setFloat(floatOverValue);
		assertEquals(new Float(13.9f), remote.getFloat());

		final Double doubleValue = new Double(13.89999d);
		/* conv-LOG */LOG.debug(">>> Double (" + doubleValue + ") <<<");
		remote.setDouble(doubleValue);
		assertEquals(doubleValue, remote.getDouble());

		final Double doubleOverValue = new Double(13.8999999999999999d);
		/* conv-LOG */LOG.debug(">>> DoubleOverValue (" + doubleOverValue + ") <<<");
		remote.setDouble(doubleOverValue);
		assertEquals(new Double(13.9d), remote.getDouble());

		final BigDecimal bigDecimalValue = BigDecimal.valueOf(13.89999d);
		/* conv-LOG */LOG.debug(">>> BigDecimal (" + bigDecimalValue + ")<<<");
		remote.setBigDecimal(bigDecimalValue);
		assertTrue(remote.getBigDecimal().compareTo(bigDecimalValue) == 0);

		final BigDecimal bigDecimalOverValue = BigDecimal.valueOf(13.89999999999999d);
		/* conv-LOG */LOG.debug(">>> BigDecimalOverValue (" + bigDecimalOverValue + ")<<<");
		remote.setBigDecimal(bigDecimalOverValue);
		assertTrue(remote.getBigDecimal().compareTo(BigDecimal.valueOf(13.9d)) == 0);
	}

	public static final class Dummy implements java.io.Serializable
	{
		// DOCTODO Document reason, why this block is empty
	}

	@Test
	public void testMissingPKLookupNoRetry()
	{
		final PK nonExistingProductPK = PK.createFixedPK(Constants.TC.Product, System.nanoTime());

		final long TIMEOUT = 10 * 1000;

		final Object token = HJMPUtils.disablePKLookupRetry();
		try
		{
			assertLess("Lookup took to long", measurePKLookup(nonExistingProductPK, false), TIMEOUT);
		}
		finally
		{
			HJMPUtils.restorPKLookupRetry(token);
		}

	}

	@Test
	public void testMissingPKLookupDoRetry()
	{
		final PK nonExistingProductPK = PK.createFixedPK(Constants.TC.Product, System.nanoTime());

		final long TIMEOUT = 10 * 1000;

		final Object token = HJMPUtils.enablePKLookupRetry(TIMEOUT, 500);
		try
		{
			assertGreaterEquals("lookup was to short", measurePKLookup(nonExistingProductPK, false), TIMEOUT);
		}
		finally
		{
			HJMPUtils.restorPKLookupRetry(token);
		}
	}

	@Test
	public void testHiddenPKLookupWithRetry()
	{
		final PK nonExistingProductPK = PK.createFixedPK(Constants.TC.Product, System.nanoTime());

		final long TIMEOUT = 30 * 1000;

		final Object token = HJMPUtils.enablePKLookupRetry(TIMEOUT, 500);
		try
		{
			// start lookup in own thread -> must wait since PK doesnt exist yet
			final AtomicReference<Boolean> success = startPKLookupInOtherThread(nonExistingProductPK);

			// wait for 5 seconds to allow a decent amount of turns
			Thread.sleep(5 * 1000);
			assertNull("Result available before end of lookup retry period", success.get()); // still no result

			// now create product with exactly that pk -> now lookup should succeed eventually
			final Product product = ProductManager.getInstance().createProduct(null, nonExistingProductPK, "PPP");
			assertTrue(product.isAlive());
			final PK createdPK = product.getPK();
			assertEquals(createdPK, nonExistingProductPK); // sanity check

			// wait for lookup result for 10 seconds 
			final long maxWait = System.currentTimeMillis() + TIMEOUT;
			while (success.get() == null && System.currentTimeMillis() < maxWait)
			{
				Thread.sleep(500);
			}
			assertEquals("Wrong retry lookup result", Boolean.TRUE, success.get());
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}
		finally
		{
			HJMPUtils.restorPKLookupRetry(token);
		}
	}

	private AtomicReference<Boolean> startPKLookupInOtherThread(final PK pk)
	{
		final Tenant tenant = Registry.getCurrentTenantNoFallback();

		final AtomicReference<Boolean> success = new AtomicReference<Boolean>(null);

		final Thread thread = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				Registry.setCurrentTenant(tenant);
				try
				{
					assertNotNull(SystemEJB.getInstance().findRemoteObjectByPK(pk));
					success.set(Boolean.TRUE);
				}
				catch (final EJBItemNotFoundException e)
				{
					success.set(Boolean.FALSE);
				}
				catch (final Exception e)
				{
					success.set(Boolean.FALSE);
					fail(e.getMessage());
				}
			}
		};
		thread.start();

		return success;
	}

	private void assertGreaterEquals(final String message, final long long1, final long long2)
	{
		assertTrue((StringUtils.isNotEmpty(message) ? message : "") + " expected " + long1 + " >= " + long2, long1 >= long2);
	}

	private void assertLess(final String message, final long long1, final long long2)
	{
		assertTrue((StringUtils.isNotEmpty(message) ? message : "") + " expected " + long1 + " < " + long2, long1 < long2);
	}

	private long measurePKLookup(final PK pk, final boolean pkExists)
	{
		final long time1 = System.currentTimeMillis();
		try
		{
			SystemEJB.getInstance().findRemoteObjectByPK(pk);
			assertTrue("lookup succeeded but PK should not exist", pkExists);
		}
		catch (final EJBItemNotFoundException e)
		{
			assertFalse("PK existed but lookup failed", pkExists);
		}
		catch (final EJBInvalidParameterException e)
		{
			fail(e.getMessage());
		}
		return System.currentTimeMillis() - time1;
	}
}
