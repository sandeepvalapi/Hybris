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
/*
 * [y] hybris Platnyform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.europe1.jalo;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("deprecation")
@IntegrationTest
public class Europe1PriceFactoryPricesIntegrationTest extends ServicelayerBaseTest
{
	private static final String TEST_PRODUCT = "TEST_PRODUCT";
	private static final String TEST_USER = "TEST_USER";
	private static final String TEST_USER_GROUP = "TEST_USER_GROUP";
	private static final String TEST_PRODUCT_GROUP = "TEST_PRODUCT_GROUP";

	private Europe1PriceFactory factory;
	private Unit unit;
	private Currency currency;

	private PriceRow any_any;
	private PriceRow any_given;
	private PriceRow any_group;
	private PriceRow given_any;
	private PriceRow given_given;
	private PriceRow given_group;
	private PriceRow group_any;
	private PriceRow group_given;
	private PriceRow group_group;
	private PriceRow id_any;
	private PriceRow id_given;
	private PriceRow id_group;
	private Product anyProduct;
	private Product givenProduct;
	private EnumerationValue givenProductGroup;
	private User anyUser;
	private User givenUser;
	private EnumerationValue givenUserGroup;

	@Before
	public void setUp() throws Exception
	{
		factory = Europe1PriceFactory.getInstance();
		final ProductManager productManager = ProductManager.getInstance();
		final UserManager userManager = UserManager.getInstance();
		final EnumerationManager enumerationManager = EnumerationManager.getInstance();
		final EnumerationType productGroupType = enumerationManager.getEnumerationType(Europe1Constants.TYPES.PRICE_PRODUCT_GROUP);

		unit = productManager.createUnit("pieces", "pieces");
		try
		{
			currency = C2LManager.getInstance().getCurrencyByIsoCode("EUR");
		}
		catch (final JaloItemNotFoundException e)
		{
			currency = C2LManager.getInstance().createCurrency("EUR");
		}

		productManager.createProduct(TEST_PRODUCT);
		userManager.createCustomer(TEST_USER);
		factory.createUserPriceGroup(TEST_USER_GROUP);
		enumerationManager.createEnumerationValue(productGroupType, TEST_PRODUCT_GROUP);

		anyProduct = product(null);
		givenProduct = product(TEST_PRODUCT);
		givenProductGroup = productGroup(TEST_PRODUCT_GROUP);
		anyUser = user(null);
		givenUser = user(TEST_USER);
		givenUserGroup = userGroup(TEST_USER_GROUP);

		any_any = createPrice(anyProduct, anyUser);
		any_given = createPrice(anyProduct, givenUser);
		any_group = createPrice(anyProduct, givenUserGroup);
		given_any = createPrice(givenProduct, anyUser);
		given_given = createPrice(givenProduct, givenUser);
		given_group = createPrice(givenProduct, givenUserGroup);
		group_any = createPrice(givenProductGroup, anyUser);
		group_given = createPrice(givenProductGroup, givenUser);
		group_group = createPrice(givenProductGroup, givenUserGroup);
		id_any = createPrice(TEST_PRODUCT, anyUser);
		id_given = createPrice(TEST_PRODUCT, givenUser);
		id_group = createPrice(TEST_PRODUCT, givenUserGroup);
	}

	@Test
	public void shouldQueryPricesForAnyProductAndAnyUser()
	{
		final Collection<PriceRow> prices = queryForPrices(anyProduct, anyUser);

		assertThat(prices).containsOnly(any_any);
	}

	@Test
	public void shouldQueryPricesForAnyProductAndGivenUser()
	{
		final Collection<PriceRow> prices = queryForPrices(anyProduct, givenUser);

		assertThat(prices).containsOnly(any_any, any_given);
	}

	@Test
	public void shouldQueryPricesForAnyProductAndGivenUserGroup()
	{
		final Collection<PriceRow> prices = queryForPrices(anyProduct, givenUserGroup);

		assertThat(prices).containsOnly(any_any, any_group);
	}

	@Test
	public void shouldQueryPricesForGivenProductAndAnyUser()
	{
		final Collection<PriceRow> prices = queryForPrices(givenProduct, anyUser);

		assertThat(prices).containsOnly(any_any, given_any, id_any);
	}

	@Test
	public void shouldQueryPricesForGivenProductAndGivenUser()
	{
		final Collection<PriceRow> prices = queryForPrices(givenProduct, givenUser);

		assertThat(prices).containsOnly(any_any, any_given, given_any, given_given, id_any, id_given);
	}

	@Test
	public void shouldQueryPricesForGivenProductAndGivenUserGroup()
	{
		final Collection<PriceRow> prices = queryForPrices(givenProduct, givenUserGroup);

		assertThat(prices).containsOnly(any_any, any_group, given_any, given_group, id_any, id_group);
	}

	@Test
	public void shouldQueryPricesForGivenProductGroupAndAnyUser()
	{
		final Collection<PriceRow> prices = queryForPrices(givenProductGroup, anyUser);

		assertThat(prices).containsOnly(any_any, group_any);
	}

	@Test
	public void shouldQueryPricesForGivenProductGroupAndGivenUser()
	{
		final Collection<PriceRow> prices = queryForPrices(givenProductGroup, givenUser);

		assertThat(prices).containsOnly(any_any, any_given, group_any, group_given);
	}

	@Test
	public void shouldQueryPricesForGivenProductGroupAndGivenUserGroup()
	{
		final Collection<PriceRow> prices = queryForPrices(givenProductGroup, givenUserGroup);

		assertThat(prices).containsOnly(any_any, any_group, group_any, group_group);
	}

	private Collection<PriceRow> queryForPrices(final Object product, final Object user)
	{
		final Product prod = (product instanceof Product) ? (Product) product : null;
		final EnumerationValue prodGroup = (product instanceof EnumerationValue) ? (EnumerationValue) product : null;
		final User usr = (user instanceof User) ? (User) user : null;
		final EnumerationValue usrGroup = (user instanceof EnumerationValue) ? (EnumerationValue) user : null;

		return factory.queryPriceRows4Price(null, prod, prodGroup, usr, usrGroup);
	}

	private PriceRow createPrice(final Object product, final Object user) throws Exception
	{
		final Product prod = (product instanceof Product) ? (Product) product : null;
		final EnumerationValue prodGroup = (product instanceof EnumerationValue) ? (EnumerationValue) product : null;
		final User usr = (user instanceof User) ? (User) user : null;
		final EnumerationValue usrGroup = (user instanceof EnumerationValue) ? (EnumerationValue) user : null;
		final String productCode = (product instanceof String) ? (String) product : null;

		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();

		return (PriceRow) ComposedType.newInstance(ctx, PriceRow.class, PriceRow.PRODUCT, prod, PriceRow.PG, prodGroup,
				PriceRow.USER, usr, PriceRow.UG, usrGroup, PriceRow.MINQTD, Long.valueOf(2), PriceRow.CURRENCY, currency,
				PriceRow.UNIT, unit, PriceRow.UNITFACTOR, Integer.valueOf(1), PriceRow.NET, Boolean.TRUE, PriceRow.DATERANGE, null,
				PriceRow.PRICE, Double.valueOf(123.45), PriceRow.PRODUCTID, productCode);
	}

	private User user(final String login)
	{
		if (login == null)
		{
			return null;
		}
		return UserManager.getInstance().getUserByLogin(login);
	}

	private EnumerationValue userGroup(final String code)
	{
		if (code == null)
		{
			return null;
		}

		return factory.getUserPriceGroup(code);
	}

	private Product product(final String code)
	{
		if (code == null)
		{
			return null;
		}

		final Collection candidates = ProductManager.getInstance().getProductsByCode(code);
		if (candidates == null || candidates.isEmpty())
		{
			return null;
		}
		if (candidates.size() > 1)
		{
			throw new IllegalStateException("More than one product for code " + code + " have been found.");
		}
		return (Product) candidates.iterator().next();
	}

	private EnumerationValue productGroup(final String code)
	{
		if (code == null)
		{
			return null;
		}

		final EnumerationManager manager = EnumerationManager.getInstance();
		final EnumerationType type = manager.getEnumerationType(Europe1Constants.TYPES.PRICE_PRODUCT_GROUP);

		return manager.getEnumerationValue(type, code);
	}
}
