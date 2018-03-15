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
package de.hybris.platform.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class QuoteServiceTest extends ServicelayerTransactionalTest
{
	@Resource
	private QuoteService quoteService;
	@Resource
	private CartService cartService;
	@Resource
	private ProductService productService;
	@Resource
	private ModelService modelService;

	private ProductModel product0;

	protected static void createQuoteData() throws ImpExException
	{
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());
		// importing test csv
		importCsv("/platformservices/test/quoteTestData.csv", "windows-1252");
	}

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		createQuoteData();

		product0 = productService.getProductForCode("testProduct0");
	}

	@Test
	public void shouldGetCurrentQuoteForCode()
	{
		QuoteModel quote = quoteService.getCurrentQuoteForCode("testQuote0");
		assertNotNull("Quote is null", quote);
		assertEquals("Unexpexted quote code", "testQuote0", quote.getCode());
		assertEquals("Unexpexted quote version", Integer.valueOf(3), quote.getVersion());
		assertEquals("Unexpected quote state", QuoteState.CANCELLED, quote.getState());

		quote = quoteService.getCurrentQuoteForCode("testQuote1");
		assertNotNull("Quote is null", quote);
		assertEquals("Unexpexted quote code", "testQuote1", quote.getCode());
		assertEquals("Unexpexted quote version", Integer.valueOf(4), quote.getVersion());
		assertEquals("Unexpected quote state", QuoteState.OFFER, quote.getState());
	}

	@Test(expected = ModelNotFoundException.class)
	public void shouldNotGetCurrentQuoteForCodeInvalidCode()
	{
		quoteService.getCurrentQuoteForCode("invalidCode");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotGetCurrentQuoteForCodeCodeNull()
	{
		quoteService.getCurrentQuoteForCode(null);
	}

	@Test
	public void shouldGetQuoteForCodeAndVersion()
	{
		QuoteModel quote = quoteService.getQuoteForCodeAndVersion("testQuote0", Integer.valueOf(1));
		assertNotNull("Quote is null", quote);
		assertEquals("Unexpexted quote code", "testQuote0", quote.getCode());
		assertEquals("Unexpexted quote version", Integer.valueOf(1), quote.getVersion());
		assertEquals("Unexpected quote state", QuoteState.CREATED, quote.getState());

		quote = quoteService.getQuoteForCodeAndVersion("testQuote0", Integer.valueOf(2));
		assertNotNull("Quote is null", quote);
		assertEquals("Unexpexted quote code", "testQuote0", quote.getCode());
		assertEquals("Unexpexted quote version", Integer.valueOf(2), quote.getVersion());
		assertEquals("Unexpected quote state", QuoteState.DRAFT, quote.getState());

		quote = quoteService.getQuoteForCodeAndVersion("testQuote0", Integer.valueOf(3));
		assertNotNull("Quote is null", quote);
		assertEquals("Unexpexted quote code", "testQuote0", quote.getCode());
		assertEquals("Unexpexted quote version", Integer.valueOf(3), quote.getVersion());
		assertEquals("Unexpected quote state", QuoteState.CANCELLED, quote.getState());
	}

	@Test(expected = ModelNotFoundException.class)
	public void shouldNotGetQuoteForCodeAndVersionInvalidCode()
	{
		quoteService.getQuoteForCodeAndVersion("invalidCode", Integer.valueOf(1));
	}

	@Test(expected = ModelNotFoundException.class)
	public void shouldNotGetQuoteForCodeAndVersionInvalidVersion()
	{
		quoteService.getQuoteForCodeAndVersion("testQuote0", Integer.valueOf(5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotGetQuoteForCodeAndVersionCodeNull()
	{
		quoteService.getQuoteForCodeAndVersion(null, Integer.valueOf(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotGetQuoteForCodeAndVersionVersionNull()
	{
		quoteService.getQuoteForCodeAndVersion("testQuote0", null);
	}

	@Test
	public void shouldGetQuotesForCode()
	{
		List<QuoteModel> quotes = quoteService.getQuotesForCode("testQuote0");
		assertNotNull("Quotes list is null for code testQuote0", quotes);
		assertEquals("Unexpected number of quotes returned for code testQuote0", 3, quotes.size());
		for (final QuoteModel quote : quotes)
		{
			assertEquals("Unexpexted quote code", "testQuote0", quote.getCode());
		}

		quotes = quoteService.getQuotesForCode("testQuote1");
		assertNotNull("Quotes list is null for code testQuote1", quotes);
		assertEquals("Unexpected number of quotes returned for code testQuote1", 4, quotes.size());
		for (final QuoteModel quote : quotes)
		{
			assertEquals("Unexpexted quote code", "testQuote1", quote.getCode());
		}
	}

	@Test
	public void shouldNotGetQuotesForCodeInvalidCode()
	{
		final List<QuoteModel> quotes = quoteService.getQuotesForCode("invalidCode");

		assertEquals("Unexpected number of quotes returned for invalid code", 0, quotes.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotGetQuotesForCodeCodeNull()
	{
		quoteService.getQuotesForCode(null);
	}

	@Test
	public void shouldCreateQuoteFromCart() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product0, 2, null);
		final QuoteModel quote = quoteService.createQuoteFromCart(cart);
		assertNotNull("Quote is null", quote);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotCreateQuoteFromCartIfCartIsNull() throws Exception
	{
		quoteService.createQuoteFromCart(null);
	}

	@Test
	public void shouldCreateQuoteSnapshot() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product0, 2, null);
		final QuoteModel quote = quoteService.createQuoteFromCart(cart);
		assertNull("Quote expirationDate is not null", quote.getExpirationTime());
		modelService.save(quote); // save to set default values for state and version
		assertEquals("Quote state is wrong", QuoteState.CREATED, quote.getState());
		assertEquals("Quote version is wrong", Integer.valueOf(1), quote.getVersion());

		final QuoteState snapshotState = QuoteState.SUBMITTED;
		final QuoteModel snapshot = quoteService.createQuoteSnapshot(quote, snapshotState);
		assertNotNull("Quote is null", snapshot);
		assertNull("Quote expirationDate is not null", quote.getExpirationTime());
		assertEquals("Quote state is wrong", snapshotState, snapshot.getState());
		assertEquals("Quote version is wrong", Integer.valueOf(quote.getVersion().intValue() + 1), snapshot.getVersion());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotCreateQuoteSnapshotIfQuoteIsNull() throws Exception
	{
		quoteService.createQuoteSnapshot(null, QuoteState.SUBMITTED);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotCreateQuoteSnapshotIfQuoteStateIsNull() throws Exception
	{
		quoteService.createQuoteSnapshot(new QuoteModel(), null);
	}
}
