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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AddressTest extends HybrisJUnit4TransactionalTest
{
	private Customer user;
	private Address adr;

	private static final String[] adrFields = new String[]
	{ Address.FIRSTNAME, "blah", Address.LASTNAME, "blub", };

	@Before
	public void setUp() throws Exception
	{
		user = jaloSession.getUserManager().createCustomer("addresstestuser");
		assertNotNull(user);

		adr = user.createAddress();
		assertNotNull(adr);

		for (int i = 0; i < adrFields.length; i += 2)
		{
			adr.setAttribute(adrFields[i], adrFields[i + 1]);
		}
	}

	private void checkAddress(final Address adr, final String[] fields)
	{
		try
		{
			for (int i = 0; i < fields.length; i += 2)
			{
				assertEquals(fields[i + 1], adr.getAttribute(fields[i]));
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}

	@Test
	public void testFields()
	{
		try
		{
			checkAddress(adr, adrFields);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}

	@Test
	public void testShipmentAndPaymentAddresses()
	{
		try
		{
			user.setDefaultDeliveryAddress(adr);

			Address tmp = user.getDefaultDeliveryAddress();
			assertEquals(tmp.getPK(), adr.getPK());
			assertEquals(tmp, adr);
			checkAddress(tmp, adrFields);

			user.setDefaultPaymentAddress(adr);

			tmp = user.getDefaultPaymentAddress();
			assertEquals(tmp.getPK(), adr.getPK());
			assertEquals(tmp, adr);
			checkAddress(tmp, adrFields);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetFields()
	{
		try
		{
			final Address address = user.createAddress();
			assertNotNull(address);
			final Map fields = new HashMap();
			fields.put(Address.STREETNAME, "Parkallee");
			fields.put(Address.STREETNUMBER, "1");
			fields.put(Address.TOWN, "Monopolistan");
			address.setAllAttributes(fields);
			assertEquals(null, address.getAttribute(Address.APPARTMENT));
			assertEquals("Parkallee", address.getAttribute(Address.STREETNAME));
			assertEquals("1", address.getAttribute(Address.STREETNUMBER));
			assertEquals("Monopolistan", address.getAttribute(Address.TOWN));
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateWithMap() throws JaloInvalidParameterException
	{
		try
		{
			final Map fields = new HashMap();
			fields.put(Address.MIDDLENAME2, "tralalala");
			final Address address = user.createAddress(fields);
			assertNotNull(address);
			assertEquals("tralalala", address.getAttribute(Address.MIDDLENAME2));
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}


}
