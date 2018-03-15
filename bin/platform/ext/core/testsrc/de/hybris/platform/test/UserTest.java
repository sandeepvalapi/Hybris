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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.persistence.security.SaltedMD5PasswordEncoder;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.fest.assertions.Fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * tests group assignment of users
 */
public class UserTest extends HybrisJUnit4Test
{
	static final Logger log = Logger.getLogger(UserTest.class.getName());
	private User user, user2, user3, user4, user5;
	private UserGroup g1, g2, g3;
	private UserManager um;
	private OrderManager om;
	@SuppressWarnings("unused")
	private Order o1, o2;
	private final String cartCode1 = "cartCode1";
	private final String cartCode2 = "cartCode2";
	@SuppressWarnings("unused")
	private Cart cart1, cart2;
	private String originalSalt = null;
	private final String exampleTextToEncode = "TestText";
	private Address address1, address2;


	@Before
	public void setUp() throws Exception
	{

		// TODO after the relase 3.0 was published, we have to move this code to JaloTest !!!

		final Tenant t = Registry.getCurrentTenant();
		if (t instanceof MasterTenant)
		{
			originalSalt = ((MasterTenant) t).getConfig().getParameter("password.md5.salt");
			((MasterTenant) t).getConfig().setParameter("password.md5.salt", "JUnit");
		}
		else
		{
			((SlaveTenant) t).getOwnConfig().setParameter("password.md5.salt", "JUnit");
		}
		//////////////////////////////////

		om = jaloSession.getOrderManager();
		um = jaloSession.getUserManager();
		final Currency curr = jaloSession.getC2LManager().createCurrency("testCurr");

		user = um.createCustomer("UserTest.heinz");
		user2 = um.createCustomer("UserTest2.markut");
		user3 = um.createCustomer("UserTest3.markut2");
		user4 = um.createCustomer("UserTest4.markut3");
		user5 = um.createCustomer("UserTest5.markut5");
		g1 = um.createUserGroup("UserTest.g1" + System.currentTimeMillis());
		g2 = um.createUserGroup("UserTest.g2" + System.currentTimeMillis());
		g3 = um.createUserGroup("UserTest.g3" + System.currentTimeMillis());
		o1 = om.createOrder("orderCode1", user, curr, new Date(), true);
		o2 = om.createOrder("orderCode2", user, curr, new Date(), true);
		cart1 = om.createCart(cartCode1, user, curr, new Date(), true);
		cart2 = om.createCart(cartCode2, user, curr, new Date(), false);

		address1 = um.createAddress(user5);
		address2 = um.createAddress(user5);

		user2.setEncodedPassword(exampleTextToEncode);
		user3.setEncodedPassword(exampleTextToEncode, "MD5"); // XXX: second parameter (MD5) should be changed for something else
		user4.setPassword(exampleTextToEncode, "MD5");
	}

	/*
	 * Tests getEncodedPassword and setEncodedPassword methods
	 */
	@Test
	public void testEncodePasswordMethods()
	{
		PasswordEncoder enc = null;
		try
		{
			enc = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder("MD5");
		}
		catch (final PasswordEncoderNotFoundException e)
		{
			fail("cannot get MD5 password encoder (though it is mapped) : " + e);
		}
		assertTrue("# expected [" + exampleTextToEncode + ", but got: " + user2.getEncodedPassword(),
				user2.getEncodedPassword() != null && user2.getEncodedPassword().equals(exampleTextToEncode));
		assertTrue("# expected [" + exampleTextToEncode + ", but got: " + user3.getEncodedPassword(),
				user3.getEncodedPassword() != null && user3.getEncodedPassword().equals(exampleTextToEncode));

		if (enc instanceof SaltedMD5PasswordEncoder)
		{
			((SaltedMD5PasswordEncoder) enc).setSalt("JUnit");
			final String exampleTextToEncodedSaltedMD5 = "60c8f2037e0429ee89ebd866d86b02f8"; // "ba5185db1d1a84002d8f1d5eb5047de2";
			assertTrue("expected [" + exampleTextToEncodedSaltedMD5 + ", but got: " + user4.getEncodedPassword(),
					user4.getEncodedPassword() != null && user4.getEncodedPassword().equals(exampleTextToEncodedSaltedMD5));
		}
		else
		{
			final String exampleTextToEncodedMD5 = "5281b02d9444c3264116f2b8277701af";
			assertTrue("expected [" + exampleTextToEncodedMD5 + ", but got: " + user4.getEncodedPassword(),
					user4.getEncodedPassword() != null && user4.getEncodedPassword().equals(exampleTextToEncodedMD5));
		}
	}

	@Test
	public void testWrongEncoding()
	{
		final String old = user.getPassword();
		try
		{
			try
			{
				user.setPassword("dumb");
			}
			catch (final PasswordEncoderNotFoundException e)
			{
				fail("no default password encoder installed : " + e);
			}
			try
			{
				user.setPassword("dumb");
			}
			catch (final PasswordEncoderNotFoundException e)
			{
				fail("no default password encoder installed : " + e);
			}
			try
			{
				user.setPassword("dumber", "ENC");
				fail("there should be no ENC encoding - so expected an exception here");
			}
			catch (final PasswordEncoderNotFoundException e)
			{
				// fine here
			}
		}
		finally
		{
			user.setPassword(old);
		}
	}



	@Test
	public void testWrongUID()
	{
		Customer dbl = null;
		try
		{
			dbl = um.createCustomer("UserTest.heinz");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine here
			/* conv-log */log.debug("ok: exception caught");
		}
		catch (final Exception e)
		{
			// unexpected exception !!!
			log.error(e.getMessage(), e);
			fail("unexpected exception : " + e.getClass().getName() + " message was " + e);
		}
		finally
		{
			if (dbl != null)
			{
				// wrong end: customer was created :-(
				try
				{
					dbl.remove();
					fail("duplicate customer was created!");
				}
				catch (final Exception e)
				{
					// what the ...
				}
			}
		}
	}

	@Test
	public void testGetOrders() throws Exception
	{
		final Collection coll = user.getOrders();
		assertEquals(2, coll.size());
	}

	@Test
	public void testGetCarts() throws Exception
	{
		final Collection coll = user.getCarts();
		assertEquals(2, coll.size());
	}

	@Test
	public void testGetCart() throws Exception
	{
		final Cart cart = user.getCart(cartCode1);
		assertEquals(cart1, cart);
	}

	@After
	public void tearDown() throws Exception
	{
		//	 TODO after the relase 3.0 was published, we have to move this code to JaloTest !!!

		if (originalSalt != null)
		{
			final Tenant t = Registry.getCurrentTenant();
			if (t instanceof MasterTenant)
			{
				((MasterTenant) t).getConfig().setParameter("password.md5.salt", originalSalt);
			}
		}

	}

	@Test
	public void testGroups() throws ConsistencyCheckException
	{


		// test add
		g1.addMember(user);
		g2.addMember(user);
		g3.addMember(user);
		Collection groups = user.getGroups();
		assertTrue("user was not correctly added to groups",
				groups != null && groups.size() == 3 && groups.contains(g1) && groups.contains(g2) && groups.contains(g3));
		// test remove
		log.info(g2.getClass().getName());
		g2.removeMember(user);
		groups = user.getGroups();
		log.info(groups);
		assertTrue("g2 was not correctly removed from groups",
				groups != null && groups.size() == 2 && groups.contains(g1) && groups.contains(g3) && !groups.contains(g2));
		assertFalse("g2 was not correctly removed from groups", g2.getMembers().contains(user));
		assertFalse("g2 was not correctly removed from groups", g2.containsMember(user));
		// try to do things a second time: caching works?!
		g2.addMember(user);
		assertTrue(g2.containsMember(user));
		// /*conv-log*/ log.debug("---");
		// /*conv-log*/ log.debug("Inhalt der usergroup "+g2.getPK()+":"+ g2.getMembers() );
		// /*conv-log*/ log.debug("Inhalt des users "+user.getPK()+":"+ user.getGroups() );
		assertTrue("user did not belont to assigned group g2", user.getGroups().contains(g2));
		g2.removeMember(user);
		assertFalse(g2.containsMember(user));
		assertFalse(g2.getMembers().contains(user));
		assertFalse(user.getGroups().contains(g2));
		// rest removal upon deletion of group
		g3.remove();
		groups = user.getGroups();
		assertTrue("g3 was not corretly removed from groups", groups != null && groups.size() == 1 && groups.contains(g1));

	}

	@Test
	public void testCreateAnonymousCustomerSession() throws JaloSecurityException
	{
		final User anon = UserManager.getInstance().getAnonymousCustomer();

		final JaloConnection conn = JaloConnection.getInstance();
		final JaloSession s1 = conn.createAnonymousCustomerSession();
		assertEquals(Constants.USER.ANONYMOUS_CUSTOMER, s1.getUser().getUID());


		anon.setPassword("xxx");
		final JaloSession s2 = conn.createAnonymousCustomerSession();
		assertEquals(Constants.USER.ANONYMOUS_CUSTOMER, s2.getUser().getUID());
		assertEquals(s1.getUser(), s2.getUser());
		assertNotSame(s1, s2);
		assertEquals("xxx", s1.getUser().getPassword());
	}

	/**
	 * Tests for anonymous login are placed in {@link SessionTest#testJaloSessionPerformLogin()}
	 */
	@Test
	public void testAnonymousPasswordCheck()
	{
		final Customer anon = UserManager.getInstance().getAnonymousCustomer();

		final String pwBefore = anon.getEncodedPassword();
		final String encBefore = anon.getPasswordEncoding();
		final String alwaysDisableSettingBefore = Config.getParameter(Customer.LOGIN_ANONYMOUS_ALWAYS_DISABLED);
		try
		{
			// need to enable ability to get real password check
			Config.setParameter(Customer.LOGIN_ANONYMOUS_ALWAYS_DISABLED, "false");
			assertFalse(anon.isLoginDisabledAsPrimitive());

			anon.setEncodedPassword("foo", "*");
			assertEquals("foo", anon.getPassword());
			assertTrue(anon.checkPassword("foo"));
			assertFalse(anon.checkPassword("bar"));

			anon.setEncodedPassword("bar", "*");
			assertEquals("bar", anon.getPassword());
			assertTrue(anon.checkPassword("bar"));
			assertFalse(anon.checkPassword("foo"));

			// now test enforcing anonymous being disabled
			Config.setParameter(Customer.LOGIN_ANONYMOUS_ALWAYS_DISABLED, "true");
			assertFalse(anon.isLoginDisabledAsPrimitive());
			assertFalse(anon.checkPassword("foo"));
			assertFalse(anon.checkPassword("bar"));
		}
		finally
		{
			Config.setParameter(Customer.LOGIN_ANONYMOUS_ALWAYS_DISABLED, alwaysDisableSettingBefore);
			anon.setEncodedPassword(pwBefore, encBefore);
		}
	}

	@Test
	public void testRemoveAllAddressesAttachedToOwner()
	{
		assertTrue(user5.isAlive());
		assertTrue(address1.isAlive());
		assertTrue(address2.isAlive());

		try
		{
			um.removeItem(user5);
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			Fail.failure(e.getMessage());
		}

		assertFalse(user5.isAlive());
		assertFalse(address1.isAlive());
		assertFalse(address2.isAlive());
	}

}
