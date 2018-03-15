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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("deprecation")
@IntegrationTest
public class Europe1PriceFactoryTaxesIntegrationTest extends ServicelayerBaseTest
{
	private static final String TEST_PRODUCT = "TEST_PRODUCT";
	private static final String TEST_USER = "TEST_USER";
	private static final String TEST_USER_GROUP = "TEST_USER_GROUP";
	private static final String TEST_PRODUCT_GROUP = "TEST_PRODUCT_GROUP";

	private Europe1PriceFactory factory;
	private Tax tax;

	private TaxRow any_any;
	private TaxRow any_given;
	private TaxRow any_group;
	private TaxRow given_any;
	private TaxRow given_given;
	private TaxRow given_group;
	private TaxRow group_any;
	private TaxRow group_given;
	private TaxRow group_group;
	private TaxRow id_any;
	private TaxRow id_given;
	private TaxRow id_group;
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
		final OrderManager orderManager = OrderManager.getInstance();
		final EnumerationManager enumerationManager = EnumerationManager.getInstance();
		final EnumerationType productGroupType = enumerationManager.getEnumerationType(Europe1Constants.TYPES.PRICE_PRODUCT_GROUP);

		tax = orderManager.createTax("TEST_TAX");

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

		any_any = createTax(anyProduct, anyUser);
		any_given = createTax(anyProduct, givenUser);
		any_group = createTax(anyProduct, givenUserGroup);
		given_any = createTax(givenProduct, anyUser);
		given_given = createTax(givenProduct, givenUser);
		given_group = createTax(givenProduct, givenUserGroup);
		group_any = createTax(givenProductGroup, anyUser);
		group_given = createTax(givenProductGroup, givenUser);
		group_group = createTax(givenProductGroup, givenUserGroup);
		id_any = createTax(TEST_PRODUCT, anyUser);
		id_given = createTax(TEST_PRODUCT, givenUser);
		id_group = createTax(TEST_PRODUCT, givenUserGroup);

	}

	@Test
	public void shouldQueryTaxesForAnyProductAndAnyUser()
	{
		final Collection<TaxRow> prices = queryForTaxes(anyProduct, anyUser);

		assertThat(prices).containsOnly(any_any);
	}

	@Test
	public void shouldQueryTaxesForAnyProductAndGivenUser()
	{
		final Collection<TaxRow> prices = queryForTaxes(anyProduct, givenUser);

		assertThat(prices).containsOnly(any_any, any_given);
	}

	@Test
	public void shouldQueryTaxesForAnyProductAndGivenUserGroup()
	{
		final Collection<TaxRow> prices = queryForTaxes(anyProduct, givenUserGroup);

		assertThat(prices).containsOnly(any_any, any_group);
	}

	@Test
	public void shouldQueryTaxesForGivenProductAndAnyUser()
	{
		final Collection<TaxRow> prices = queryForTaxes(givenProduct, anyUser);

		assertThat(prices).containsOnly(any_any, given_any, id_any);
	}

	@Test
	public void shouldQueryTaxesForGivenProductAndGivenUser()
	{
		final Collection<TaxRow> prices = queryForTaxes(givenProduct, givenUser);

		assertThat(prices).containsOnly(any_any, any_given, given_any, given_given, id_any, id_given);
	}

	@Test
	public void shouldQueryTaxesForGivenProductAndGivenUserGroup()
	{
		final Collection<TaxRow> prices = queryForTaxes(givenProduct, givenUserGroup);

		assertThat(prices).containsOnly(any_any, any_group, given_any, given_group, id_any, id_group);
	}

	@Test
	public void shouldQueryTaxesForGivenProductGroupAndAnyUser()
	{
		final Collection<TaxRow> prices = queryForTaxes(givenProductGroup, anyUser);

		assertThat(prices).containsOnly(any_any, group_any);
	}

	@Test
	public void shouldQueryTaxesForGivenProductGroupAndGivenUser()
	{
		final Collection<TaxRow> prices = queryForTaxes(givenProductGroup, givenUser);

		assertThat(prices).containsOnly(any_any, any_given, group_any, group_given);
	}

	@Test
	public void shouldQueryTaxesForGivenProductGroupAndGivenUserGroup()
	{
		final Collection<TaxRow> prices = queryForTaxes(givenProductGroup, givenUserGroup);

		assertThat(prices).containsOnly(any_any, any_group, group_any, group_group);
	}

	private Collection<TaxRow> queryForTaxes(final Object product, final Object user)
	{
		final Product prod = (product instanceof Product) ? (Product) product : null;
		final EnumerationValue prodGroup = (product instanceof EnumerationValue) ? (EnumerationValue) product : null;
		final User usr = (user instanceof User) ? (User) user : null;
		final EnumerationValue usrGroup = (user instanceof EnumerationValue) ? (EnumerationValue) user : null;

		return factory.queryTax4Price(null, prod, prodGroup, usr, usrGroup);
	}

	private TaxRow createTax(final Object product, final Object user) throws Exception
	{
		final Product prod = (product instanceof Product) ? (Product) product : null;
		final EnumerationValue prodGroup = (product instanceof EnumerationValue) ? (EnumerationValue) product : null;
		final User usr = (user instanceof User) ? (User) user : null;
		final EnumerationValue usrGroup = (user instanceof EnumerationValue) ? (EnumerationValue) user : null;
		final String productCode = (product instanceof String) ? (String) product : null;


		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();

		return (TaxRow) ComposedType.newInstance(ctx, TaxRow.class, TaxRow.PRODUCT, prod, TaxRow.PG, prodGroup, TaxRow.USER, usr,
				TaxRow.UG, usrGroup, TaxRow.TAX, tax, TaxRow.DATERANGE, null, TaxRow.VALUE, Double.valueOf(123.0), TaxRow.PRODUCTID,
				productCode);
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
