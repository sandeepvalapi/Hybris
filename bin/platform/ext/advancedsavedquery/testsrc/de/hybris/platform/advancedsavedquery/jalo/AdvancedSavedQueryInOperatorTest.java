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
package de.hybris.platform.advancedsavedquery.jalo;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.advancedsavedquery.enums.AdvancedQueryComparatorEnum;
import de.hybris.platform.advancedsavedquery.jalo.AdvancedSavedQuery.SearchParameterContainer;
import de.hybris.platform.advancedsavedquery.model.AdvancedSavedQueryModel;
import de.hybris.platform.advancedsavedquery.model.ComposedTypeAdvancedSavedQuerySearchParameterModel;
import de.hybris.platform.advancedsavedquery.model.WherePartModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;


@IntegrationTest
public class AdvancedSavedQueryInOperatorTest extends ServicelayerTransactionalBaseTest
{
	private static final String QUERY_CODE = "TEST_QUERY";
	private static final String PARAM = "<param>";
	private static final String TEST_USER = "TEST_USER999";
	private static final Collection<String> TEST_PRODUCT_CODES = ImmutableList.of("46TEST_PRODUCT37", "64TEST_PRODUCT73");
	private static final Function<Product, String> TO_CODE = new Function<Product, String>()
	{
		@Override
		public String apply(final Product product)
		{
			return product.getCode();
		}
	};
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;

	@Before
	public void setUp()
	{
		final AdvancedSavedQueryModel query = createSavedQuery();
		final WherePartModel wherePart = createWherePartFor(query);
		createParameterFor(wherePart);

		final UserModel user = createUser();
		createProductsFor(user);

		modelService.saveAll();
	}

	@Test
	public void shouldBeAbleToGetResultsForQueryWithInOperator()
	{
		final AdvancedSavedQuery savedQuery = givenQuery();
		final AbstractAdvancedSavedQuerySearchParameter parameter = givenParameterFor(savedQuery);
		final User user = givenUser();
		final SearchParameterContainer parameterComtainer = savedQuery.new SearchParameterContainer(parameter, Operator.IN, user);

		final String flexibleQuery = savedQuery.getGeneratedFlexibleSearch(Collections.singletonList(parameterComtainer));
		final List<Product> products = FlexibleSearch
				.getInstance()
				.search(flexibleQuery, Collections.singletonMap(parameter.getUniqueSearchParameterPlaceHolder(), user), Product.class)
				.getResult();

		assertThat(products).isNotNull().isNotEmpty();
		assertThat(FluentIterable.from(products).transform(TO_CODE).toSet()). //
				hasSize(TEST_PRODUCT_CODES.size()). //
				containsOnly(TEST_PRODUCT_CODES.toArray());
	}

	private User givenUser()
	{
		return (User) FlexibleSearch.getInstance()
				.search("select {PK} from {User} where {Uid}=?uid", Collections.singletonMap("uid", TEST_USER), User.class)
				.getResult().get(0);
	}

	private AbstractAdvancedSavedQuerySearchParameter givenParameterFor(final AdvancedSavedQuery query)
	{
		return query.getWherePart(PARAM).getDynamicParams().iterator().next();
	}

	private AdvancedSavedQuery givenQuery()
	{
		return (AdvancedSavedQuery) FlexibleSearch
				.getInstance()
				.search("select {PK} from {AdvancedSavedQuery} where {Code}=?code", Collections.singletonMap("code", QUERY_CODE),
						AdvancedSavedQuery.class).getResult().get(0);
	}

	private void createParameterFor(final WherePartModel wherePart)
	{
		final ComposedTypeAdvancedSavedQuerySearchParameterModel param = modelService
				.create(ComposedTypeAdvancedSavedQuerySearchParameterModel.class);
		param.setComparator(AdvancedQueryComparatorEnum.CONTAINS);
		param.setWherePart(wherePart);
		param.setTypedSearchParameter(typeService.getAttributeDescriptor(ProductModel._TYPECODE, ProductModel.OWNER));
	}

	private WherePartModel createWherePartFor(final AdvancedSavedQueryModel query)
	{
		final WherePartModel wherePart = modelService.create(WherePartModel.class);
		wherePart.setSavedQuery(query);
		wherePart.setReplacePattern(PARAM);
		return wherePart;
	}

	private AdvancedSavedQueryModel createSavedQuery()
	{
		final AdvancedSavedQueryModel query = modelService.create(AdvancedSavedQueryModel.class);
		query.setCode(QUERY_CODE);
		query.setQuery("select {pk} from {Product} where " + PARAM);
		query.setResultType(typeService.getComposedTypeForClass(ProductModel.class));
		return query;
	}

	private void createProductsFor(final UserModel user)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("TEST_ID");
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(catalog);
		catalogVersion.setVersion("TEST_VERSION");
		for (final String productCode : TEST_PRODUCT_CODES)
		{
			final ProductModel product = modelService.create(ProductModel.class);
			product.setCode(productCode);
			product.setCatalogVersion(catalogVersion);
			product.setOwner(user);
		}
	}

	private UserModel createUser()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(TEST_USER);
		return user;
	}
}
