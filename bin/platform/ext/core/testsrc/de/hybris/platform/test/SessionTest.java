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

import static de.hybris.platform.test.SessionCloneTestUtils.assertClonedContextAttributesEqual;
import static de.hybris.platform.test.SessionCloneTestUtils.assertSameClassNoEquals;
import static de.hybris.platform.test.SessionCloneTestUtils.deserialize;
import static de.hybris.platform.test.SessionCloneTestUtils.serialize;
import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.impl.DefaultCache;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemCacheUnit;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSession.LoginProperties;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LItem;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.C2LManager.C2LCacheKey;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.order.price.AbstractPriceFactory;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.CannotDecodePasswordException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.GenericItemEJBImpl;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.Utilities;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SessionTest extends HybrisJUnit4Test
{
	private final static Logger LOG = Logger.getLogger(SessionTest.class);

	private Product product1;
	private Product product2;
	private Unit unit1;
	private Customer customer, newCustomer;
	private ProductManager productManager;

	@Before
	public void setUp() throws Exception
	{
		productManager = jaloSession.getProductManager();
		assertNotNull(product1 = productManager.createProduct("test.product1"));
		assertNotNull(product2 = productManager.createProduct("test.product2"));
		assertNotNull(unit1 = productManager.createUnit("packaging", "pieces"));
	}


	public static class MySession extends JaloSession
	{
		// DOCTODO Document reason, why this block is empty
	}

	// PLA-13021 - check session context holding stale items
	@Test
	public void testStaleItemsInContext()
	{
		final Currency itemA = (Currency) jaloSession.getAttribute(SessionContext.CURRENCY);
		assertSame(itemA, jaloSession.getAttribute(SessionContext.CURRENCY));

		// now evict that item from cache
		Registry.getCurrentTenant().getCache().clear();
		final Currency itemB = (Currency) jaloSession.getAttribute(SessionContext.CURRENCY);
		assertNotSame(itemA, itemB);
		assertEquals(itemA.getPK(), itemB.getPK());

		// make sure special attributes are working correctly as well
		assertSame(itemB, jaloSession.getSessionContext().getCurrency());
	}

	// PLA-13021 - check C2LManager handing out stale Currency
	@Test
	public void testStaleCurrencyFromC2LManager()
	{
		final Cache c = Registry.getCurrentTenant().getCache();
		final String iso = jaloSession.getSessionContext().getCurrency().getIsoCode();
		final PK pk = jaloSession.getSessionContext().getCurrency().getPK();

		final Currency itemA = C2LManager.getInstance().getCurrencyByIsoCode(iso);
		assertSame(itemA, C2LManager.getInstance().getCurrencyByIsoCode(iso));

		// now evict everything from cache
		c.clear();
		final Currency itemB = C2LManager.getInstance().getCurrencyByIsoCode(iso);
		assertNotSame(itemA, itemB);
		assertEquals(pk, itemB.getPK());

		// now simulate eviction of itemB only ( no invalidation )
		final JaloItemCacheUnit cacheUnit = new JaloItemCacheUnit(c, pk)
		{
			@Override
			public Object compute()
			{
				throw new IllegalStateException("not supposed to be called - using unit for cache removal only!");
			}
		};
		c.removeUnit(cacheUnit);
		assertFalse(itemA.isCacheBound());
		assertFalse(itemB.isCacheBound());

		final Currency itemC = C2LManager.getInstance().getCurrencyByIsoCode(iso);
		assertNotSame(itemB, itemC);
		assertEquals(pk, itemC.getPK());
	}

	@Test
	public void testFixStaleCurrencyFromC2LManagerPerformance()
	{
		final int THREADS = 50;
		final int DURATION_SECONDS = 15;
		final String isoCode = jaloSession.getSessionContext().getCurrency().getIsoCode();

		// warm up
		runGetCurrency(10, 5, isoCode, true);

		final long invocationsWithFix = runGetCurrency(THREADS, DURATION_SECONDS, isoCode, true);
		System.out.println("running getCurrency(" + isoCode + ") " + (DURATION_SECONDS) + " seconds using " + THREADS
				+ " allowed for " + invocationsWithFix + " invocations WITH stale currency fix");

		final long invocationsWithoutFix = runGetCurrency(THREADS, DURATION_SECONDS, isoCode, false);
		System.out.println("running getCurrency(" + isoCode + ") " + (DURATION_SECONDS) + " seconds using " + THREADS
				+ " allowed for " + invocationsWithoutFix + " invocations WITHOUT stale currency fix");
	}

	private long runGetCurrency(final int THREADS, final int durationSeconds, final String isoCode, final boolean useFix)
	{
		final TestThreadsHolder<GetCurrencyRunner> threads = new TestThreadsHolder<GetCurrencyRunner>(THREADS, true)
		{
			@Override
			public GetCurrencyRunner newRunner(final int threadNumber)
			{
				return new GetCurrencyRunner(isoCode, useFix);
			}
		};
		assertTrue(threads.runAll(durationSeconds, TimeUnit.SECONDS, durationSeconds));
		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		long invocations = 0;
		for (final GetCurrencyRunner r : threads.getRunners())
		{
			assertTrue(r.gets != -1);
			invocations += r.gets;
		}

		return invocations;
	}

	class GetCurrencyRunner implements Runnable
	{
		final String isoCode;
		final boolean useFix;

		volatile long gets = -1;

		GetCurrencyRunner(final String isoCode, final boolean useFix)
		{
			this.isoCode = isoCode;
			this.useFix = useFix;
		}

		@Override
		public void run()
		{
			final Thread myThread = Thread.currentThread();
			long counter = 0;
			while (!myThread.isInterrupted())
			{
				simulateGetCurrency(isoCode, useFix).hashCode();
				counter++;
			}
			this.gets = counter;
		}
	}

	private Currency simulateGetCurrency(final String iso, final boolean useFix)
	{
		return (Currency) (new TestC2LCacheUnit(Registry.getCurrentTenant().getCache(), "TEST_CURRENCYBYISO_" + iso, useFix)
		{
			@Override
			public Object compute()
			{
				final SearchResult<Currency> res = jaloSession.getFlexibleSearch().search(
						"SELECT {" + Item.PK + "} FROM {Currency} WHERE {" + C2LItem.ISOCODE + "}=?iso", //
						Collections.singletonMap("iso", iso), //
						Currency.class);
				final Collection<Currency> coll = res.getResult();
				if (coll.size() < 1)
				{
					throw new JaloItemNotFoundException("currency with isocode '" + iso + "' not found.", 0);
				}
				else
				{
					return coll.iterator().next();
				}
			}
		}.getCached());

	}

	static abstract class TestC2LCacheUnit extends AbstractCacheUnit
	{
		private final C2LCacheKey cacheKey;
		private final boolean useFix;

		protected TestC2LCacheUnit(final Cache cache, final Object additionalKey, final boolean useFix)
		{
			super(cache);
			this.useFix = useFix;
			final Object internalAdditionalKey = additionalKey instanceof Object[] ? Arrays.asList((Object[]) additionalKey)
					: additionalKey;

			PK userPK = null;
			if (JaloSession.hasCurrentSession())
			{
				final User u = JaloSession.getCurrentSession().getUser();
				if (u != null)
				{
					userPK = u.getPK();
				}
			}
			this.cacheKey = new C2LCacheKey(cache.getTenantId(), new Object[]
			{ DefaultCache.CACHEKEY_C2LMANAGER, userPK, internalAdditionalKey //und der selbstbestimmte key
					});
		}

		@Override
		public Object[] createKey()
		{
			return cacheKey.getLegacyKey();
		}

		@Override
		public RegistrableCacheKey getKey()
		{
			return this.cacheKey;
		}

		@Override
		public abstract Object compute();

		/**
		 * get the cached object from this cache unit. if it is not yet computed, this method blocks until it's computed.
		 * <p>
		 * helper method which does exactly the same as AbstractCacheUnit.get(), but catches the exception and throws a
		 * RuntimeException (JaloSystemException) instead. because of that you don't have to add a try/catch block in your
		 * code.
		 *
		 * @return the object
		 */
		public Object getCached()
		{
			try
			{
				return useFix ? Utilities.getCacheBoundVersion(get()) : get();
			}
			catch (final Exception e)
			{
				if ((e instanceof RuntimeException))
				{
					throw (RuntimeException) e;
				}
				else
				{
					throw new JaloSystemException(e);
				}
			}
		}
	}

	// PLA-13021 - check FlexibleSearch handing out stale items
	@Test
	public void testStaleCurrencyFromFlexibleSearch()
	{
		final Cache c = Registry.getCurrentTenant().getCache();
		final String iso = jaloSession.getSessionContext().getCurrency().getIsoCode();
		final PK pk = jaloSession.getSessionContext().getCurrency().getPK();

		final Currency itemA = queryCurrency(iso);
		assertSame(itemA, queryCurrency(iso));

		// now evict everything from cache
		c.clear();
		final Currency itemB = queryCurrency(iso);
		assertNotSame(itemA, itemB);
		assertEquals(pk, itemB.getPK());

		// now simulate eviction of itemB only ( no invalidation )
		final JaloItemCacheUnit cacheUnit = new JaloItemCacheUnit(c, pk)
		{
			@Override
			public Object compute()
			{
				throw new IllegalStateException("not supposed to be called - using unit for cache removal only!");
			}
		};
		c.removeUnit(cacheUnit);
		assertFalse(itemA.isCacheBound());
		assertFalse(itemB.isCacheBound());

		final Currency itemC = queryCurrency(iso);
		assertNotSame(itemB, itemC);
		assertEquals(pk, itemC.getPK());
	}

	private Currency queryCurrency(final String iso)
	{
		return (Currency) FlexibleSearch.getInstance()
				.search("SELECT {PK} FROM {Currency} WHERE {isoCode}='" + iso + "'", Currency.class).getResult().get(0);
	}


	@Test
	public void testEvictedCartObjectNotReturned()
	{
		// initialze cart -> have to call getCart() at least once !!!
		assertNotNull(jaloSession.getCart());

		assertSessionCartsConsistent(jaloSession);

		Registry.getCurrentTenantNoFallback().getCache().clear();

		assertSessionCartsConsistent(jaloSession);

		Registry.getCurrentTenantNoFallback().getCache().clear();

		assertSessionCartsConsistent(jaloSession);
	}

	private void assertSessionCartsConsistent(final JaloSession jaloSession)
	{
		final Cart cartFromAttributes = (Cart) jaloSession.getAttributes().get(JaloSession.CART);
		final Cart cartFromAttribute = (Cart) jaloSession.getAttribute(JaloSession.CART);
		final Cart cartDirect = jaloSession.getCart();

		assertSame(cartDirect, cartFromAttribute);
		assertSame(cartDirect, cartFromAttributes);
	}

	// see PLA-12628
	@Test
	public void testCleanExpiredSessionWithDeletedUser() throws ConsistencyCheckException, JaloSecurityException
	{
		// create new user
		final User user = UserManager.getInstance().createEmployee("foo" + System.nanoTime());

		final JaloConnection jaloConnection = JaloConnection.getInstance();

		// create new session
		final JaloSession jSession = jaloConnection.createSession(user);

		// switch back to test session -> otherwise we'd not be able to remove that user
		jaloSession.activate();

		// kill user -> have to use EJBImpl object to do this since User.remove() is actually
		// checking for open sessions before allowing removal ( no, this is not preventing
		// a session from having a deleted user, since, for instance, that user may get deleted
		// by a different server node...
		((GenericItemEJBImpl) user.getImplementation()).remove(jaloSession.getSessionContext());
		assertFalse(user.isAlive());

		//
		jSession.close();

		// finally session should be closed without any exception thrown before
		assertTrue(jSession.isClosed());
	}

	@Test
	public void testCustomSessionClass() throws JaloSecurityException
	{
		final JaloSession current = jaloSession;
		try
		{
			if (!Registry.getApplicationContext().containsBean("jalosession"))
			{
				// test normal session class
				JaloSession anon = JaloConnection.getInstance().createAnonymousCustomerSession();
				assertEquals(JaloSession.class, anon.getClass());
				// test own session class
				anon = JaloConnection.getInstance().createAnonymousCustomerSession(MySession.class);
				assertEquals(MySession.class, anon.getClass());
			}
		}
		finally
		{
			current.activate();
		}
	}

	@Test
	public void testCartCurrencyChange() throws ConsistencyCheckException
	{
		final Currency current = jaloSession.getSessionContext().getCurrency();
		try
		{
			assertEquals(current, jaloSession.getCart().getCurrency());
			final Currency newOne;
			assertNotNull(newOne = jaloSession.getC2LManager().createCurrency("cartCurrTest"));
			jaloSession.getSessionContext().setCurrency(newOne);
			assertEquals(newOne, jaloSession.getCart().getCurrency());
		}
		finally
		{
			jaloSession.getSessionContext().setCurrency(current);
		}
	}

	@Test
	public void testCartPreservation() throws ConsistencyCheckException, JaloInvalidParameterException, JaloSecurityException
	{
		customer = (Customer) jaloSession.getUser();
		try
		{
			final Cart cart1 = jaloSession.getCart();
			assertNotNull(cart1);
			assertEquals(customer, cart1.getUser());
			final CartEntry c1e1 = (CartEntry) cart1.addNewEntry(product1, 3, unit1);
			final CartEntry c1e2 = (CartEntry) cart1.addNewEntry(product2, 30, unit1);
			assertCollection(Arrays.asList(new Object[]
			{ c1e1, c1e2 }), cart1.getAllEntries());
			assertEquals(product1, c1e1.getProduct());
			assertEquals(product2, c1e2.getProduct());
			assertEquals(3l, c1e1.getQuantity().longValue());
			assertEquals(30l, c1e2.getQuantity().longValue());

			assertNotNull(newCustomer = jaloSession.getUserManager().createCustomer("newCustomer"));
			newCustomer.setPassword("schnitzel");

			final Properties props = new Properties();
			props.put(JaloSession.LoginProperties.LOGIN, "newCustomer");
			props.put(JaloSession.LoginProperties.PASSWORD, "schnitzel");
			props.put(JaloSession.LoginProperties.SESSION_TYPE, JaloSession.LoginProperties.SessionTypes.CUSTOMER);

			jaloSession.transfer(props);
			assertEquals(newCustomer, jaloSession.getUser());
			final Cart cart2 = jaloSession.getCart();
			assertNotNull(cart2);
			assertEquals(cart1, cart2);
			assertEquals(newCustomer, cart2.getUser());
			assertCollection(Arrays.asList(new Object[]
			{ c1e1, c1e2 }), cart2.getAllEntries());
			assertEquals(product1, c1e1.getProduct());
			assertEquals(product2, c1e2.getProduct());
			assertEquals(3l, c1e1.getQuantity().longValue());
			assertEquals(30l, c1e2.getQuantity().longValue());

		}
		finally
		{
			jaloSession.setUser(customer);
		}
	}


	@Test
	public void testTransactionDisabling()
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();

		ctx.setAttribute(SessionContext.TRANSACTION_IN_CREATE_DISABLED, Boolean.TRUE);
		assertEquals(Boolean.TRUE, ctx.getAttribute(SessionContext.TRANSACTION_IN_CREATE_DISABLED));

		ctx.setAttribute(SessionContext.TRANSACTION_IN_CREATE_DISABLED, Boolean.FALSE);
		assertEquals(Boolean.FALSE, ctx.getAttribute(SessionContext.TRANSACTION_IN_CREATE_DISABLED));

		ctx.setAttribute(SessionContext.TRANSACTION_IN_CREATE_DISABLED, null);
		assertNull(ctx.getAttribute(SessionContext.TRANSACTION_IN_CREATE_DISABLED));

	}

	@Test
	public void testRestoreCart()
	{
		final Cart cart = jaloSession.getCart();
		assertNotNull(cart);
		LOG.info("### Using cart: " + cart.getClass().getName() + ", session cart type: "
				+ jaloSession.getSessionContext().getAttribute(JaloSession.CART_TYPE) + " configured cart "
				+ jaloSession.getTenant().getConfig().getParameter(JaloSession.CART_TYPE));
		customer = (Customer) jaloSession.getUser();
		assertNotNull(customer);

		final CartEntry entry1 = (CartEntry) cart.addNewEntry(product1, 3, unit1);
		entry1.setProperty("e1prop", Boolean.TRUE);
		final CartEntry entry2 = (CartEntry) cart.addNewEntry(product2, 4, unit1);
		entry2.setProperty("e2prop", "some text");

		assertEquals(0, entry1.getEntryNumber().intValue());
		assertEquals(1, entry2.getEntryNumber().intValue());


		Cart savedCart1 = null;
		try
		{
			assertNotNull(savedCart1 = customer.saveCurrentCart("test_1"));
		}
		catch (final ConsistencyCheckException e)
		{
			fail("Cart could not be saved." + e);
		}
		assertEquals("test_1", savedCart1.getCode());

		jaloSession.restoreSavedCart(savedCart1);

		// old entries should be removed
		assertFalse(entry1.isAlive());
		assertFalse(entry2.isAlive());

		assertEquals(2, cart.getAllEntries().size());
		final CartEntry ce1 = (CartEntry) cart.getEntry(0);
		final CartEntry ce2 = (CartEntry) cart.getEntry(1);
		assertEquals(Arrays.asList(ce1, ce2), cart.getAllEntries());

		assertEquals(0, ce1.getEntryNumber().intValue());
		assertEquals(1, ce2.getEntryNumber().intValue());

		final CartEntry se1 = (CartEntry) savedCart1.getEntry(0);
		final CartEntry se2 = (CartEntry) savedCart1.getEntry(1);

		assertEquals(0, se1.getEntryNumber().intValue());
		assertEquals(1, se2.getEntryNumber().intValue());

		assertFalse(ce1.equals(se1));
		assertFalse(ce2.equals(se2));

		assertEquals(se1.getProduct(), ce1.getProduct());
		assertEquals(se1.getQuantity(), ce1.getQuantity());
		assertEquals(se1.getUnit(), ce1.getUnit());
		assertEquals(Boolean.TRUE, se1.getProperty("e1prop"));
		assertEquals(Boolean.TRUE, ce1.getProperty("e1prop"));

		assertEquals(se2.getProduct(), ce2.getProduct());
		assertEquals(se2.getQuantity(), ce2.getQuantity());
		assertEquals(se2.getUnit(), ce2.getUnit());
		assertEquals("some text", se2.getProperty("e2prop"));
		assertEquals("some text", ce2.getProperty("e2prop"));

		jaloSession.appendSavedCart(savedCart1);

		assertEquals(4, cart.getAllEntries().size());
		final CartEntry ce3 = (CartEntry) cart.getEntry(2);
		final CartEntry ce4 = (CartEntry) cart.getEntry(3);
		assertEquals(Arrays.asList(ce1, ce2, ce3, ce4), cart.getAllEntries());

		assertEquals(2, ce3.getEntryNumber().intValue());
		assertEquals(3, ce4.getEntryNumber().intValue());

		assertFalse(se1.equals(ce3));
		assertFalse(se2.equals(ce4));
		assertFalse(ce1.equals(ce3));
		assertFalse(ce2.equals(ce4));

		assertEquals(se1.getProduct(), ce3.getProduct());
		assertEquals(se1.getQuantity(), ce3.getQuantity());
		assertEquals(se1.getUnit(), ce3.getUnit());
		assertEquals(Boolean.TRUE, ce3.getProperty("e1prop"));

		assertEquals(se2.getProduct(), ce4.getProduct());
		assertEquals(se2.getQuantity(), ce4.getQuantity());
		assertEquals(se2.getUnit(), ce4.getUnit());
		assertEquals("some text", ce4.getProperty("e2prop"));
	}

	@Test
	public void testJaloSessionPerformLogin() throws ConsistencyCheckException
	{
		final JaloSession jalosession = JaloSession.getCurrentSession();
		try
		{
			jalosession.transfer("", "");
			fail("There should be a JaloInvalidParameterException because of no login is given");
		}
		catch (final JaloInvalidParameterException e)
		{
			//ok
		}
		catch (final JaloSecurityException e)
		{
			fail("The JaloInvalidParameterException should be thrown first");
		}

		final Properties props = new Properties();
		try
		{
			props.put(JaloSession.LoginProperties.LOGIN, Constants.USER.CUSTOMER_USERGROUP);
			props.put(JaloSession.LoginProperties.PASSWORD, "schnitzel");
			props.put(JaloSession.LoginProperties.SESSION_TYPE, JaloSession.LoginProperties.SessionTypes.ANONYMOUS);
			jalosession.transfer(props);
			fail("There should be a JaloSecurityException because only anonymous session with anonymous user is allowed");
		}
		catch (final JaloSecurityException e)
		{
			//ok
		}

		props.clear();
		try
		{
			props.put(JaloSession.LoginProperties.LOGIN, Constants.USER.ANONYMOUS_CUSTOMER);
			props.put(JaloSession.LoginProperties.PASSWORD, "schnitzel");
			props.put(JaloSession.LoginProperties.SESSION_TYPE, JaloSession.LoginProperties.SessionTypes.CUSTOMER);
			jalosession.transfer(props);
			fail("There should be a JaloSecurityException because only anonymous user can login into anonymous session");
		}
		catch (final JaloSecurityException e)
		{
			//ok
		}

		//		try
		//		{
		//			jalosession.transfer(JaloConnection.ANONYMOUS_LOGIN_PROPERTIES);
		//		}
		//		catch (final JaloSecurityException e)
		//		{
		//			fail("anonymous user and anonymous session could not be transfered");
		//		}
		//
		//		props.clear();
		//		try
		//		{
		//			props.put(JaloSession.LoginProperties.LOGIN, Constants.USER.ANONYMOUS_CUSTOMER);
		//			props.put(JaloSession.LoginProperties.PASSWORD, UserManager.getInstance().getAnonymousCustomer().getPassword());
		//			props.put(JaloSession.LoginProperties.SESSION_TYPE, JaloSession.LoginProperties.SessionTypes.ANONYMOUS);
		//			jalosession.transfer(props);
		//		}
		//		catch (final CannotDecodePasswordException e)
		//		{
		//			// ok, we cannot test that
		//		}
		//		catch (final JaloSecurityException e)
		//		{
		//			fail("anonymous user and anonymous session could not be transfered even using real password");
		//		}

		props.clear();
		final Customer anon = UserManager.getInstance().getAnonymousCustomer();
		final String alwaysDisableSettingBefore = Config.getParameter(Customer.LOGIN_ANONYMOUS_ALWAYS_DISABLED);
		try
		{
			// need to enable ability to get real password check
			Config.setParameter(Customer.LOGIN_ANONYMOUS_ALWAYS_DISABLED, "false");
			assertFalse(anon.isLoginDisabledAsPrimitive());

			props.put(JaloSession.LoginProperties.USER_PK, anon.getPK());
			props.put(JaloSession.LoginProperties.SESSION_TYPE, JaloSession.LoginProperties.SessionTypes.ANONYMOUS);
			jalosession.transfer(props);
		}
		catch (final CannotDecodePasswordException e)
		{
			// ok, we cannot test that
		}
		catch (final JaloSecurityException e)
		{
			fail("anonymous user and anonymous session could not be transfered using user PK");
		}
		finally
		{
			Config.setParameter(Customer.LOGIN_ANONYMOUS_ALWAYS_DISABLED, alwaysDisableSettingBefore);
		}
	}

	@Test
	public void testDirectTransfer() throws ConsistencyCheckException, JaloInvalidParameterException, JaloSecurityException
	{
		final Employee employee = UserManager.getInstance().createEmployee("foo");
		employee.setPassword("bar");

		final User current = jaloSession.getUser();
		try
		{
			final Map props = new HashMap();
			props.put(LoginProperties.LOGIN, "foo");
			props.put(LoginProperties.PASSWORD, "bar");
			jaloSession.transfer(props);

			props.put(LoginProperties.LOGIN, "foo");
			props.put(LoginProperties.PASSWORD, "xxx");
			try
			{
				jaloSession.transfer(props);
				fail("JaloSecurityException expected");
			}
			catch (final JaloSecurityException e)
			{
				// fine
			}
			props.put(LoginProperties.USER_PK, employee.getPK());
			props.remove(LoginProperties.PASSWORD);
			jaloSession.transfer(props);

			// test optional password check
			props.put(LoginProperties.USER_PK, employee.getPK());
			props.put(LoginProperties.PASSWORD, "xxx");
			try
			{
				jaloSession.transfer(props);
				fail("JaloSecurityException expected");
			}
			catch (final JaloSecurityException e)
			{
				// fine
			}
			props.put(LoginProperties.USER_PK, employee.getPK());
			props.put(LoginProperties.PASSWORD, "bar");
			jaloSession.transfer(props);
		}
		finally
		{
			jaloSession.setUser(current);
		}
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException, InterruptedException
	{
		final JaloSession copy = (JaloSession) Utilities.copyViaSerialization(jaloSession);

		assertNotNull(copy);
		compareSessions(jaloSession, copy);
		// now try the same outside out tenant
		final Tenant current = Registry.getCurrentTenant();
		try
		{
			Registry.unsetCurrentTenant(); // no tenant at all
			assertFalse(//
					"after unset tenant " + (Registry.hasCurrentTenant() ? Registry.getCurrentTenant() : null) + " is active", //
					Registry.hasCurrentTenant()); // check if tenant is not being activated by accident
			final byte[] bytes = serialize(jaloSession);
			Thread.sleep(2000);
			assertFalse(//
					"after writing tenant " + (Registry.hasCurrentTenant() ? Registry.getCurrentTenant() : null) + " is active", //
					Registry.hasCurrentTenant()); // check if tenant is not being activated by accident
			final JaloSession copy2 = (JaloSession) deserialize(bytes);
			assertFalse(//
					"after reading tenant " + (Registry.hasCurrentTenant() ? Registry.getCurrentTenant() : null) + " is active", //
					Registry.hasCurrentTenant()); // check if tenant is not being activated by accident
			assertNotNull(copy2);
			compareSessions(jaloSession, copy2);
			assertFalse(Registry.hasCurrentTenant()); // check if tenant is not being activated by accident
		}
		finally
		{
			Registry.setCurrentTenant(current);
		}
	}

	@Test
	public void testAssertSameClassNoEquals()
	{
		// Object -> Object = success
		assertSameClassNoEquals(new Object(), new Object());

		// TestPF -> TestPF = success
		assertSameClassNoEquals(new TestPF(), new TestPF());

		// "foo" -> "bar = error
		AssertionError error = null;
		try
		{
			assertSameClassNoEquals("foo", "bar");
		}
		catch (final AssertionError e)
		{
			error = e;
		}
		assertNotNull(error);
	}

	protected void compareSessions(final JaloSession jaloSession, final JaloSession copy)
	{
		assertNotSame(jaloSession, copy);
		assertNotSame(jaloSession.getSessionContext(), copy.getSessionContext());

		assertEquals(jaloSession.getUser(), copy.getUser());
		assertEquals(jaloSession.getSessionID(), copy.getSessionID());
		assertEquals(jaloSession.getSessionContext().getLanguage(), copy.getSessionContext().getLanguage());
		assertEquals(jaloSession.getSessionContext().getUser(), copy.getSessionContext().getUser());
		assertEquals(jaloSession.getSessionContext().getCurrency(), copy.getSessionContext().getCurrency());
		assertEquals(jaloSession.getSessionContext().getTimeZone(), copy.getSessionContext().getTimeZone());
		assertEquals(jaloSession.getSessionContext().getLocale(), copy.getSessionContext().getLocale());

		assertClonedContextAttributesEqual(jaloSession.getSessionContext().getAttributes(), copy.getSessionContext()
				.getAttributes());

		assertEquals(jaloSession.<Tenant> getTenant(), copy.<Tenant> getTenant());
	}

	private static final class TestPF extends AbstractPriceFactory
	{
		@Override
		public PriceValue getBasePrice(final AbstractOrderEntry entry) throws JaloPriceFactoryException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public List getProductDiscountInformations(final SessionContext ctx, final Product product, final Date date,
				final boolean net) throws JaloPriceFactoryException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public List getProductPriceInformations(final SessionContext ctx, final Product product, final Date date, final boolean net)
				throws JaloPriceFactoryException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public List getProductTaxInformations(final SessionContext ctx, final Product product, final Date date)
				throws JaloPriceFactoryException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public String getName()
		{
			throw new UnsupportedOperationException();
		}
	}

}
